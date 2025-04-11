package org.apache.mina.core.polling;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.service.AbstractIoAcceptor;
import org.apache.mina.core.service.AbstractIoService;
import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.core.service.SimpleIoProcessorPool;
import org.apache.mina.core.session.AbstractIoSession;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.util.ExceptionMonitor;

import java.net.SocketAddress;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractPollingIoAcceptor<S extends AbstractIoSession, H>
        extends AbstractIoAcceptor {
    private final Semaphore lock = new Semaphore(1);

    private final IoProcessor<S> processor;

    private final boolean createdProcessor;

    private final Queue<AbstractIoAcceptor.AcceptorOperationFuture> registerQueue = new ConcurrentLinkedQueue<AbstractIoAcceptor.AcceptorOperationFuture>();

    private final Queue<AbstractIoAcceptor.AcceptorOperationFuture> cancelQueue = new ConcurrentLinkedQueue<AbstractIoAcceptor.AcceptorOperationFuture>();

    private final Map<SocketAddress, H> boundHandles = Collections.synchronizedMap(new HashMap<SocketAddress, H>());

    private final AbstractIoService.ServiceOperationFuture disposalFuture = new AbstractIoService.ServiceOperationFuture();
    protected boolean reuseAddress = false;
    protected int backlog = 50;
    private volatile boolean selectable;
    private AtomicReference<Acceptor> acceptorRef = new AtomicReference<Acceptor>();

    protected AbstractPollingIoAcceptor(IoSessionConfig sessionConfig, Class<? extends IoProcessor<S>> processorClass) {
        this(sessionConfig, (Executor) null, (IoProcessor<S>) new SimpleIoProcessorPool(processorClass), true, (SelectorProvider) null);
    }

    protected AbstractPollingIoAcceptor(IoSessionConfig sessionConfig, Class<? extends IoProcessor<S>> processorClass, int processorCount) {
        this(sessionConfig, (Executor) null, (IoProcessor<S>) new SimpleIoProcessorPool(processorClass, processorCount), true, (SelectorProvider) null);
    }

    protected AbstractPollingIoAcceptor(IoSessionConfig sessionConfig, Class<? extends IoProcessor<S>> processorClass, int processorCount, SelectorProvider selectorProvider) {
        this(sessionConfig, (Executor) null, (IoProcessor<S>) new SimpleIoProcessorPool(processorClass, processorCount, selectorProvider), true, selectorProvider);
    }

    protected AbstractPollingIoAcceptor(IoSessionConfig sessionConfig, IoProcessor<S> processor) {
        this(sessionConfig, (Executor) null, processor, false, (SelectorProvider) null);
    }

    protected AbstractPollingIoAcceptor(IoSessionConfig sessionConfig, Executor executor, IoProcessor<S> processor) {
        this(sessionConfig, executor, processor, false, (SelectorProvider) null);
    }

    private AbstractPollingIoAcceptor(IoSessionConfig sessionConfig, Executor executor, IoProcessor<S> processor, boolean createdProcessor, SelectorProvider selectorProvider) {
        super(sessionConfig, executor);

        if (processor == null) {
            throw new IllegalArgumentException("processor");
        }

        this.processor = processor;
        this.createdProcessor = createdProcessor;

        try {
            init(selectorProvider);

            this.selectable = true;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeIoException("Failed to initialize.", e);
        } finally {
            if (!this.selectable) {
                try {
                    destroy();
                } catch (Exception e) {
                    ExceptionMonitor.getInstance().exceptionCaught(e);
                }
            }
        }
    }

    protected void dispose0() throws Exception {
        unbind();

        startupAcceptor();
        wakeup();
    }

    protected final Set<SocketAddress> bindInternal(List<? extends SocketAddress> localAddresses) throws Exception {
        AbstractIoAcceptor.AcceptorOperationFuture request = new AbstractIoAcceptor.AcceptorOperationFuture(localAddresses);

        this.registerQueue.add(request);

        startupAcceptor();

        try {
            this.lock.acquire();

            Thread.sleep(10L);
            wakeup();
        } finally {
            this.lock.release();
        }

        request.awaitUninterruptibly();

        if (request.getException() != null) {
            throw request.getException();
        }

        Set<SocketAddress> newLocalAddresses = new HashSet<SocketAddress>();

        for (H handle : this.boundHandles.values()) {
            newLocalAddresses.add(localAddress(handle));
        }

        return newLocalAddresses;
    }

    private void startupAcceptor() throws InterruptedException {
        if (!this.selectable) {
            this.registerQueue.clear();
            this.cancelQueue.clear();
        }

        Acceptor acceptor = this.acceptorRef.get();

        if (acceptor == null) {
            this.lock.acquire();
            acceptor = new Acceptor();

            if (this.acceptorRef.compareAndSet(null, acceptor)) {
                executeWorker(acceptor);
            } else {
                this.lock.release();
            }
        }
    }

    protected final void unbind0(List<? extends SocketAddress> localAddresses) throws Exception {
        AbstractIoAcceptor.AcceptorOperationFuture future = new AbstractIoAcceptor.AcceptorOperationFuture(localAddresses);

        this.cancelQueue.add(future);
        startupAcceptor();
        wakeup();

        future.awaitUninterruptibly();
        if (future.getException() != null) {
            throw future.getException();
        }
    }

    private int registerHandles() {

    }

    private int unregisterHandles() {
        int cancelledHandles = 0;
        while (true) {
            AbstractIoAcceptor.AcceptorOperationFuture future = this.cancelQueue.poll();
            if (future == null) {
                break;
            }

            for (SocketAddress a : future.getLocalAddresses()) {
                H handle = this.boundHandles.remove(a);

                if (handle == null) {
                    continue;
                }

                try {
                    close(handle);
                    wakeup();
                } catch (Exception e) {
                    ExceptionMonitor.getInstance().exceptionCaught(e);
                } finally {
                    cancelledHandles++;
                }
            }

            future.setDone();
        }

        return cancelledHandles;
    }

    public final IoSession newSession(SocketAddress remoteAddress, SocketAddress localAddress) {
        throw new UnsupportedOperationException();
    }

    public int getBacklog() {
        return this.backlog;
    }

    public void setBacklog(int backlog) {
        synchronized (this.bindLock) {
            if (isActive()) {
                throw new IllegalStateException("backlog can't be set while the acceptor is bound.");
            }

            this.backlog = backlog;
        }
    }

    public boolean isReuseAddress() {
        return this.reuseAddress;
    }

    public void setReuseAddress(boolean reuseAddress) {
        synchronized (this.bindLock) {
            if (isActive()) {
                throw new IllegalStateException("backlog can't be set while the acceptor is bound.");
            }

            this.reuseAddress = reuseAddress;
        }
    }

    public SocketSessionConfig getSessionConfig() {
        return (SocketSessionConfig) this.sessionConfig;
    }

    protected abstract void init() throws Exception;

    protected abstract void init(SelectorProvider paramSelectorProvider) throws Exception;

    protected abstract void destroy() throws Exception;

    protected abstract int select() throws Exception;

    protected abstract void wakeup();

    protected abstract Iterator<H> selectedHandles();

    protected abstract H open(SocketAddress paramSocketAddress) throws Exception;

    protected abstract SocketAddress localAddress(H paramH) throws Exception;

    protected abstract S accept(IoProcessor<S> paramIoProcessor, H paramH) throws Exception;

    protected abstract void close(H paramH) throws Exception;

    private class Acceptor
            implements Runnable {
        private Acceptor() {
        }

        public void run() {
            assert AbstractPollingIoAcceptor.access$100(AbstractPollingIoAcceptor.this).get() == this;

            int nHandles = 0;

            AbstractPollingIoAcceptor.this.lock.release();

            while (AbstractPollingIoAcceptor.this.selectable) {

                try {

                    int selected = AbstractPollingIoAcceptor.this.select();

                    nHandles += AbstractPollingIoAcceptor.this.registerHandles();

                    if (nHandles == 0) {
                        AbstractPollingIoAcceptor.access$100(AbstractPollingIoAcceptor.this).set(null);

                        if (AbstractPollingIoAcceptor.this.registerQueue.isEmpty() && AbstractPollingIoAcceptor.this.cancelQueue.isEmpty()) {
                            assert AbstractPollingIoAcceptor.access$100(AbstractPollingIoAcceptor.this).get() != this;

                            break;
                        }
                        if (!AbstractPollingIoAcceptor.access$100(AbstractPollingIoAcceptor.this).compareAndSet(null, this)) {
                            assert AbstractPollingIoAcceptor.access$100(AbstractPollingIoAcceptor.this).get() != this;

                            break;
                        }
                        assert AbstractPollingIoAcceptor.access$100(AbstractPollingIoAcceptor.this).get() == this;
                    }

                    if (selected > 0) {

                        processHandles(AbstractPollingIoAcceptor.this.selectedHandles());
                    }

                    nHandles -= AbstractPollingIoAcceptor.this.unregisterHandles();
                } catch (ClosedSelectorException cse) {

                    ExceptionMonitor.getInstance().exceptionCaught(cse);
                    break;
                } catch (Exception e) {
                    ExceptionMonitor.getInstance().exceptionCaught(e);

                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e1) {
                        ExceptionMonitor.getInstance().exceptionCaught(e1);
                    }
                }
            }

            if (AbstractPollingIoAcceptor.this.selectable && AbstractPollingIoAcceptor.this.isDisposing()) {
                AbstractPollingIoAcceptor.this.selectable = false;
                try {
                    if (AbstractPollingIoAcceptor.this.createdProcessor) {
                        AbstractPollingIoAcceptor.this.processor.dispose();
                    }
                } finally {
                    try {
                        synchronized (AbstractPollingIoAcceptor.this.disposalLock) {
                            if (AbstractPollingIoAcceptor.this.isDisposing()) {
                                AbstractPollingIoAcceptor.this.destroy();
                            }
                        }
                    } catch (Exception e) {
                        ExceptionMonitor.getInstance().exceptionCaught(e);
                    } finally {
                        AbstractPollingIoAcceptor.this.disposalFuture.setDone();
                    }
                }
            }
        }

        private void processHandles(Iterator<H> handles) throws Exception {
            while (handles.hasNext()) {
                H handle = handles.next();
                handles.remove();

                S session = AbstractPollingIoAcceptor.this.accept(AbstractPollingIoAcceptor.this.processor, handle);

                if (session == null) {
                    continue;
                }

                AbstractPollingIoAcceptor.this.initSession((IoSession) session, null, null);

                session.getProcessor().add((IoSession) session);
            }
        }
    }
}

