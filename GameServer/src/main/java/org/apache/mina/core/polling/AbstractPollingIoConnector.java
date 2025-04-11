package org.apache.mina.core.polling;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.DefaultConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.service.AbstractIoConnector;
import org.apache.mina.core.service.AbstractIoService;
import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.core.service.SimpleIoProcessorPool;
import org.apache.mina.core.session.AbstractIoSession;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.core.session.IoSessionInitializer;
import org.apache.mina.util.ExceptionMonitor;

import java.net.ConnectException;
import java.net.SocketAddress;
import java.nio.channels.ClosedSelectorException;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractPollingIoConnector<T extends AbstractIoSession, H>
        extends AbstractIoConnector {
    private final Queue<ConnectionRequest> connectQueue = new ConcurrentLinkedQueue<ConnectionRequest>();

    private final Queue<ConnectionRequest> cancelQueue = new ConcurrentLinkedQueue<ConnectionRequest>();

    private final IoProcessor<T> processor;

    private final boolean createdProcessor;

    private final AbstractIoService.ServiceOperationFuture disposalFuture = new AbstractIoService.ServiceOperationFuture();
    private final AtomicReference<Connector> connectorRef = new AtomicReference<Connector>();
    private volatile boolean selectable;

    protected AbstractPollingIoConnector(IoSessionConfig sessionConfig, Class<? extends IoProcessor<T>> processorClass) {
        this(sessionConfig, (Executor) null, (IoProcessor<T>) new SimpleIoProcessorPool(processorClass), true);
    }

    protected AbstractPollingIoConnector(IoSessionConfig sessionConfig, Class<? extends IoProcessor<T>> processorClass, int processorCount) {
        this(sessionConfig, (Executor) null, (IoProcessor<T>) new SimpleIoProcessorPool(processorClass, processorCount), true);
    }

    protected AbstractPollingIoConnector(IoSessionConfig sessionConfig, IoProcessor<T> processor) {
        this(sessionConfig, (Executor) null, processor, false);
    }

    protected AbstractPollingIoConnector(IoSessionConfig sessionConfig, Executor executor, IoProcessor<T> processor) {
        this(sessionConfig, executor, processor, false);
    }

    private AbstractPollingIoConnector(IoSessionConfig sessionConfig, Executor executor, IoProcessor<T> processor, boolean createdProcessor) {
        super(sessionConfig, executor);

        if (processor == null) {
            throw new IllegalArgumentException("processor");
        }

        this.processor = processor;
        this.createdProcessor = createdProcessor;

        try {
            init();
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

    protected final void dispose0() throws Exception {
        startupWorker();
        wakeup();
    }

    protected final ConnectFuture connect0(SocketAddress remoteAddress, SocketAddress localAddress, IoSessionInitializer<? extends ConnectFuture> sessionInitializer) {
        H handle = null;
        boolean success = false;
        try {
            handle = newHandle(localAddress);
            if (connect(handle, remoteAddress)) {
                DefaultConnectFuture defaultConnectFuture = new DefaultConnectFuture();
                T session = newSession(this.processor, handle);
                initSession((IoSession) session, (IoFuture) defaultConnectFuture, sessionInitializer);

                session.getProcessor().add((IoSession) session);
                success = true;
                return (ConnectFuture) defaultConnectFuture;
            }

            success = true;
        } catch (Exception e) {
            return DefaultConnectFuture.newFailedFuture(e);
        } finally {
            if (!success && handle != null) {
                try {
                    close(handle);
                } catch (Exception e) {
                    ExceptionMonitor.getInstance().exceptionCaught(e);
                }
            }
        }

        ConnectionRequest request = new ConnectionRequest(handle, sessionInitializer);
        this.connectQueue.add(request);
        startupWorker();
        wakeup();

        return (ConnectFuture) request;
    }

    private void startupWorker() {
        if (!this.selectable) {
            this.connectQueue.clear();
            this.cancelQueue.clear();
        }

        Connector connector = this.connectorRef.get();

        if (connector == null) {
            connector = new Connector();

            if (this.connectorRef.compareAndSet(null, connector)) {
                executeWorker(connector);
            }
        }
    }

    private int registerNew() {
        int nHandles = 0;
        while (true) {
            ConnectionRequest req = this.connectQueue.poll();
            if (req == null) {
                break;
            }

            H handle = req.handle;
            try {
                register(handle, req);
                nHandles++;
            } catch (Exception e) {
                req.setException(e);
                try {
                    close(handle);
                } catch (Exception e2) {
                    ExceptionMonitor.getInstance().exceptionCaught(e2);
                }
            }
        }
        return nHandles;
    }

    private int cancelKeys() {
        int nHandles = 0;

        while (true) {
            ConnectionRequest req = this.cancelQueue.poll();

            if (req == null) {
                break;
            }

            H handle = req.handle;

            try {
                close(handle);
            } catch (Exception e) {
                ExceptionMonitor.getInstance().exceptionCaught(e);
            } finally {
                nHandles++;
            }
        }

        if (nHandles > 0) {
            wakeup();
        }

        return nHandles;
    }

    private int processConnections(Iterator<H> handlers) {
        int nHandles = 0;

        while (handlers.hasNext()) {
            H handle = handlers.next();
            handlers.remove();

            ConnectionRequest connectionRequest = getConnectionRequest(handle);

            if (connectionRequest == null) {
                continue;
            }

            boolean success = false;
            try {
                if (finishConnect(handle)) {
                    T session = newSession(this.processor, handle);
                    initSession((IoSession) session, (IoFuture) connectionRequest, connectionRequest.getSessionInitializer());

                    session.getProcessor().add((IoSession) session);
                    nHandles++;
                }
                success = true;
            } catch (Exception e) {
                connectionRequest.setException(e);
            } finally {
                if (!success) {
                    this.cancelQueue.offer(connectionRequest);
                }
            }
        }
        return nHandles;
    }

    protected abstract void init() throws Exception;

    protected abstract void destroy() throws Exception;

    protected abstract H newHandle(SocketAddress paramSocketAddress) throws Exception;

    protected abstract boolean connect(H paramH, SocketAddress paramSocketAddress) throws Exception;

    protected abstract boolean finishConnect(H paramH) throws Exception;

    protected abstract T newSession(IoProcessor<T> paramIoProcessor, H paramH) throws Exception;

    protected abstract void close(H paramH) throws Exception;

    private void processTimedOutSessions(Iterator<H> handles) {
        long currentTime = System.currentTimeMillis();

        while (handles.hasNext()) {
            H handle = handles.next();
            ConnectionRequest connectionRequest = getConnectionRequest(handle);

            if (connectionRequest != null && currentTime >= connectionRequest.deadline) {
                connectionRequest.setException(new ConnectException("Connection timed out."));
                this.cancelQueue.offer(connectionRequest);
            }
        }
    }

    protected abstract void wakeup();

    protected abstract int select(int paramInt) throws Exception;

    protected abstract Iterator<H> selectedHandles();

    protected abstract Iterator<H> allHandles();

    protected abstract void register(H paramH, ConnectionRequest paramConnectionRequest) throws Exception;

    protected abstract ConnectionRequest getConnectionRequest(H paramH);

    private class Connector implements Runnable {
        private Connector() {
        }

        public void run() {
            assert AbstractPollingIoConnector.access$300(AbstractPollingIoConnector.this).get() == this;

            int nHandles = 0;

            while (AbstractPollingIoConnector.this.selectable) {

                try {
                    int timeout = (int) Math.min(AbstractPollingIoConnector.this.getConnectTimeoutMillis(), 1000L);
                    int selected = AbstractPollingIoConnector.this.select(timeout);

                    nHandles += AbstractPollingIoConnector.this.registerNew();

                    if (nHandles == 0) {
                        AbstractPollingIoConnector.access$300(AbstractPollingIoConnector.this).set(null);

                        if (AbstractPollingIoConnector.access$600(AbstractPollingIoConnector.this).isEmpty()) {
                            assert AbstractPollingIoConnector.access$300(AbstractPollingIoConnector.this).get() != this;

                            break;
                        }
                        if (!AbstractPollingIoConnector.access$300(AbstractPollingIoConnector.this).compareAndSet(null, this)) {
                            assert AbstractPollingIoConnector.access$300(AbstractPollingIoConnector.this).get() != this;

                            break;
                        }
                        assert AbstractPollingIoConnector.access$300(AbstractPollingIoConnector.this).get() == this;
                    }

                    if (selected > 0) {
                        nHandles -= AbstractPollingIoConnector.this.processConnections(AbstractPollingIoConnector.this.selectedHandles());
                    }

                    AbstractPollingIoConnector.this.processTimedOutSessions(AbstractPollingIoConnector.this.allHandles());

                    nHandles -= AbstractPollingIoConnector.this.cancelKeys();
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

            if (AbstractPollingIoConnector.this.selectable && AbstractPollingIoConnector.this.isDisposing()) {
                AbstractPollingIoConnector.this.selectable = false;
                try {
                    if (AbstractPollingIoConnector.this.createdProcessor) {
                        AbstractPollingIoConnector.this.processor.dispose();
                    }
                } finally {
                    try {
                        synchronized (AbstractPollingIoConnector.this.disposalLock) {
                            if (AbstractPollingIoConnector.this.isDisposing()) {
                                AbstractPollingIoConnector.this.destroy();
                            }
                        }
                    } catch (Exception e) {
                        ExceptionMonitor.getInstance().exceptionCaught(e);
                    } finally {
                        AbstractPollingIoConnector.this.disposalFuture.setDone();
                    }
                }
            }
        }
    }

    public final class ConnectionRequest extends DefaultConnectFuture {
        private final H handle;
        private final long deadline;
        private final IoSessionInitializer<? extends ConnectFuture> sessionInitializer;

        public ConnectionRequest(H handle, IoSessionInitializer<? extends ConnectFuture> callback) {
            this.handle = handle;
            long timeout = AbstractPollingIoConnector.this.getConnectTimeoutMillis();
            if (timeout <= 0L) {
                this.deadline = Long.MAX_VALUE;
            } else {
                this.deadline = System.currentTimeMillis() + timeout;
            }
            this.sessionInitializer = callback;
        }

        public H getHandle() {
            return this.handle;
        }

        public long getDeadline() {
            return this.deadline;
        }

        public IoSessionInitializer<? extends ConnectFuture> getSessionInitializer() {
            return this.sessionInitializer;
        }

        public void cancel() {
            if (!isDone()) {
                super.cancel();
                AbstractPollingIoConnector.access$1400(AbstractPollingIoConnector.this).add(this);
                AbstractPollingIoConnector.this.startupWorker();
                AbstractPollingIoConnector.this.wakeup();
            }
        }
    }
}

