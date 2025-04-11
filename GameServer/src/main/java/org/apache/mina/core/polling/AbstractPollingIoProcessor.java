package org.apache.mina.core.polling;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.file.FileRegion;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.filterchain.IoFilterChainBuilder;
import org.apache.mina.core.future.DefaultIoFuture;
import org.apache.mina.core.service.AbstractIoService;
import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.core.service.IoServiceListenerSupport;
import org.apache.mina.core.session.AbstractIoSession;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.core.session.SessionState;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.core.write.WriteRequestQueue;
import org.apache.mina.core.write.WriteToClosedSessionException;
import org.apache.mina.transport.socket.AbstractDatagramSessionConfig;
import org.apache.mina.util.ExceptionMonitor;
import org.apache.mina.util.NamePreservingRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.ClosedSelectorException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractPollingIoProcessor<S extends AbstractIoSession>
        implements IoProcessor<S> {
    private static final Logger LOG = LoggerFactory.getLogger(IoProcessor.class);

    private static final int WRITE_SPIN_COUNT = 256;

    private static final long SELECT_TIMEOUT = 1000L;

    private static final ConcurrentHashMap<Class<?>, AtomicInteger> threadIds = new ConcurrentHashMap<Class<?>, AtomicInteger>();

    private final String threadName;

    private final Executor executor;

    private final Queue<S> newSessions = new ConcurrentLinkedQueue<S>();

    private final Queue<S> removingSessions = new ConcurrentLinkedQueue<S>();

    private final Queue<S> flushingSessions = new ConcurrentLinkedQueue<S>();

    private final Queue<S> trafficControllingSessions = new ConcurrentLinkedQueue<S>();

    private final AtomicReference<Processor> processorRef = new AtomicReference<Processor>();
    private final Object disposalLock = new Object();
    private final DefaultIoFuture disposalFuture = new DefaultIoFuture(null);
    protected AtomicBoolean wakeupCalled = new AtomicBoolean(false);
    private long lastIdleCheckTime;
    private volatile boolean disposing;
    private volatile boolean disposed;

    protected AbstractPollingIoProcessor(Executor executor) {
        if (executor == null) {
            throw new IllegalArgumentException("executor");
        }

        this.threadName = nextThreadName();
        this.executor = executor;
    }

    private String nextThreadName() {
        int newThreadId;
        Class<?> cls = getClass();

        AtomicInteger threadId = threadIds.putIfAbsent(cls, new AtomicInteger(1));

        if (threadId == null) {
            newThreadId = 1;
        } else {

            newThreadId = threadId.incrementAndGet();
        }

        return cls.getSimpleName() + '-' + newThreadId;
    }

    public final boolean isDisposing() {
        return this.disposing;
    }

    public final boolean isDisposed() {
        return this.disposed;
    }

    public final void dispose() {
        if (this.disposed || this.disposing) {
            return;
        }

        synchronized (this.disposalLock) {
            this.disposing = true;
            startupProcessor();
        }

        this.disposalFuture.awaitUninterruptibly();
        this.disposed = true;
    }

    public final void add(S session) {
        if (this.disposed || this.disposing) {
            throw new IllegalStateException("Already disposed.");
        }

        this.newSessions.add(session);
        startupProcessor();
    }

    public final void remove(S session) {
        scheduleRemove(session);
        startupProcessor();
    }

    private void scheduleRemove(S session) {
        this.removingSessions.add(session);
    }

    public void write(S session, WriteRequest writeRequest) {
        WriteRequestQueue writeRequestQueue = session.getWriteRequestQueue();

        writeRequestQueue.offer((IoSession) session, writeRequest);

        if (!session.isWriteSuspended()) {
            flush(session);
        }
    }

    public final void flush(S session) {
        if (session.setScheduledForFlush(true)) {
            this.flushingSessions.add(session);
            wakeup();
        }
    }

    private void scheduleFlush(S session) {
        if (session.setScheduledForFlush(true)) {
            this.flushingSessions.add(session);
        }
    }

    public final void updateTrafficMask(S session) {
        this.trafficControllingSessions.add(session);
        wakeup();
    }

    private void startupProcessor() {
        Processor processor = this.processorRef.get();

        if (processor == null) {
            processor = new Processor();

            if (this.processorRef.compareAndSet(null, processor)) {
                this.executor.execute((Runnable) new NamePreservingRunnable(processor, this.threadName));
            }
        }

        wakeup();
    }

    private int handleNewSessions() {
        int addedSessions = 0;

        for (AbstractIoSession abstractIoSession = (AbstractIoSession) this.newSessions.poll(); abstractIoSession != null; abstractIoSession = (AbstractIoSession) this.newSessions.poll()) {
            if (addNow((S) abstractIoSession)) {
                addedSessions++;
            }
        }

        return addedSessions;
    }

    private boolean addNow(S session) {
        boolean registered = false;

        try {
            init(session);
            registered = true;

            IoFilterChainBuilder chainBuilder = session.getService().getFilterChainBuilder();
            chainBuilder.buildFilterChain(session.getFilterChain());

            IoServiceListenerSupport listeners = ((AbstractIoService) session.getService()).getListeners();
            listeners.fireSessionCreated((IoSession) session);
        } catch (Exception e) {
            ExceptionMonitor.getInstance().exceptionCaught(e);

            try {
                destroy(session);
            } catch (Exception e1) {
                ExceptionMonitor.getInstance().exceptionCaught(e1);
            } finally {
                registered = false;
            }
        }

        return registered;
    }

    private int removeSessions() {
        int removedSessions = 0;

        for (AbstractIoSession abstractIoSession = (AbstractIoSession) this.removingSessions.poll(); abstractIoSession != null; abstractIoSession = (AbstractIoSession) this.removingSessions.poll()) {
            SessionState state = getState((S) abstractIoSession);

            switch (state) {

                case OPENED:
                    if (removeNow((S) abstractIoSession)) {
                        removedSessions++;
                    }
                    break;

                case CLOSING:
                    break;

                case OPENING:
                    this.newSessions.remove(abstractIoSession);

                    if (removeNow((S) abstractIoSession)) {
                        removedSessions++;
                    }
                    break;

                default:
                    throw new IllegalStateException(String.valueOf(state));
            }

        }
        return removedSessions;
    }

    private boolean removeNow(S session) {
        clearWriteRequestQueue(session);

        try {
            destroy(session);
            return true;
        } catch (Exception e) {
            IoFilterChain filterChain = session.getFilterChain();
            filterChain.fireExceptionCaught(e);
        } finally {
            clearWriteRequestQueue(session);
            ((AbstractIoService) session.getService()).getListeners().fireSessionDestroyed((IoSession) session);
        }
        return false;
    }

    private void clearWriteRequestQueue(S session) {
        WriteRequestQueue writeRequestQueue = session.getWriteRequestQueue();

        List<WriteRequest> failedRequests = new ArrayList<WriteRequest>();
        WriteRequest req;
        if ((req = writeRequestQueue.poll((IoSession) session)) != null) {
            Object message = req.getMessage();

            if (message instanceof IoBuffer) {
                IoBuffer buf = (IoBuffer) message;

                if (buf.hasRemaining()) {
                    buf.reset();
                    failedRequests.add(req);
                } else {
                    IoFilterChain filterChain = session.getFilterChain();
                    filterChain.fireMessageSent(req);
                }
            } else {
                failedRequests.add(req);
            }

            while ((req = writeRequestQueue.poll((IoSession) session)) != null) {
                failedRequests.add(req);
            }
        }

        if (!failedRequests.isEmpty()) {
            WriteToClosedSessionException cause = new WriteToClosedSessionException(failedRequests);

            for (WriteRequest r : failedRequests) {
                session.decreaseScheduledBytesAndMessages(r);
                r.getFuture().setException((Throwable) cause);
            }

            IoFilterChain filterChain = session.getFilterChain();
            filterChain.fireExceptionCaught((Throwable) cause);
        }
    }

    private void process() throws Exception {
        for (Iterator<S> i = selectedSessions(); i.hasNext(); ) {
            AbstractIoSession abstractIoSession = (AbstractIoSession) i.next();
            process((S) abstractIoSession);
            i.remove();
        }
    }

    private void process(S session) {
        if (isReadable(session) && !session.isReadSuspended()) {
            read(session);
        }

        if (isWritable(session) && !session.isWriteSuspended()) {
            if (session.setScheduledForFlush(true)) {
                this.flushingSessions.add(session);
            }
        }
    }

    private void read(S session) {
        IoSessionConfig config = session.getConfig();
        int bufferSize = config.getReadBufferSize();
        IoBuffer buf = IoBuffer.allocate(bufferSize);

        boolean hasFragmentation = session.getTransportMetadata().hasFragmentation();

        try {
            int ret, readBytes = 0;

            try {
                if (hasFragmentation) {

                    while ((ret = read(session, buf)) > 0) {
                        readBytes += ret;

                        if (!buf.hasRemaining()) {
                            break;
                        }
                    }
                } else {
                    ret = read(session, buf);

                    if (ret > 0) {
                        readBytes = ret;
                    }
                }
            } finally {
                buf.flip();
            }

            if (readBytes > 0) {
                IoFilterChain filterChain = session.getFilterChain();
                filterChain.fireMessageReceived(buf);
                buf = null;

                if (hasFragmentation) {
                    if (readBytes << 1 < config.getReadBufferSize()) {
                        session.decreaseReadBufferSize();
                    } else if (readBytes == config.getReadBufferSize()) {
                        session.increaseReadBufferSize();
                    }
                }
            }

            if (ret < 0) {

                IoFilterChain filterChain = session.getFilterChain();
                filterChain.fireInputClosed();
            }
        } catch (Exception e) {
            if (e instanceof IOException && (
                    !(e instanceof java.net.PortUnreachableException) || !AbstractDatagramSessionConfig.class.isAssignableFrom(config.getClass()) || ((AbstractDatagramSessionConfig) config).isCloseOnPortUnreachable())) {

                scheduleRemove(session);
            }

            IoFilterChain filterChain = session.getFilterChain();
            filterChain.fireExceptionCaught(e);
        }
    }

    private void notifyIdleSessions(long currentTime) throws Exception {
        if (currentTime - this.lastIdleCheckTime >= 1000L) {
            this.lastIdleCheckTime = currentTime;
            AbstractIoSession.notifyIdleness(allSessions(), currentTime);
        }
    }

    private void flush(long currentTime) {
        if (this.flushingSessions.isEmpty()) {
            return;
        }

        do {
            AbstractIoSession abstractIoSession = (AbstractIoSession) this.flushingSessions.poll();

            if (abstractIoSession == null) {
                break;
            }

            abstractIoSession.unscheduledForFlush();

            SessionState state = getState((S) abstractIoSession);

            switch (state) {
                case OPENED:
                    try {
                        boolean flushedAll = flushNow((S) abstractIoSession, currentTime);

                        if (flushedAll && !abstractIoSession.getWriteRequestQueue().isEmpty((IoSession) abstractIoSession) && !abstractIoSession.isScheduledForFlush()) {
                            scheduleFlush((S) abstractIoSession);
                        }
                    } catch (Exception e) {
                        scheduleRemove((S) abstractIoSession);
                        IoFilterChain filterChain = abstractIoSession.getFilterChain();
                        filterChain.fireExceptionCaught(e);
                    }
                    break;

                case CLOSING:
                    break;

                case OPENING:
                    scheduleFlush((S) abstractIoSession);
                    return;

                default:
                    throw new IllegalStateException(String.valueOf(state));
            }

        } while (!this.flushingSessions.isEmpty());
    }

    private boolean flushNow(S session, long currentTime) {
        if (!session.isConnected()) {
            scheduleRemove(session);
            return false;
        }

        boolean hasFragmentation = session.getTransportMetadata().hasFragmentation();

        WriteRequestQueue writeRequestQueue = session.getWriteRequestQueue();

        int maxWrittenBytes = session.getConfig().getMaxReadBufferSize() + (session.getConfig().getMaxReadBufferSize() >>> 1);

        int writtenBytes = 0;
        WriteRequest req = null;

        try {
            setInterestedInWrite(session, false);

            do {
                req = session.getCurrentWriteRequest();

                if (req == null) {
                    req = writeRequestQueue.poll((IoSession) session);

                    if (req == null) {
                        break;
                    }

                    session.setCurrentWriteRequest(req);
                }

                int localWrittenBytes = 0;
                Object message = req.getMessage();

                if (message instanceof IoBuffer) {
                    localWrittenBytes = writeBuffer(session, req, hasFragmentation, maxWrittenBytes - writtenBytes, currentTime);

                    if (localWrittenBytes > 0 && ((IoBuffer) message).hasRemaining()) {

                        writtenBytes += localWrittenBytes;
                        setInterestedInWrite(session, true);
                        return false;
                    }
                } else if (message instanceof FileRegion) {
                    localWrittenBytes = writeFile(session, req, hasFragmentation, maxWrittenBytes - writtenBytes, currentTime);

                    if (localWrittenBytes > 0 && ((FileRegion) message).getRemainingBytes() > 0L) {
                        writtenBytes += localWrittenBytes;
                        setInterestedInWrite(session, true);
                        return false;
                    }
                } else {
                    throw new IllegalStateException("Don't know how to handle message of type '" + message.getClass().getName() + "'.  Are you missing a protocol encoder?");
                }

                if (localWrittenBytes == 0) {

                    setInterestedInWrite(session, true);
                    return false;
                }

                writtenBytes += localWrittenBytes;

                if (writtenBytes >= maxWrittenBytes) {

                    scheduleFlush(session);
                    return false;
                }

                if (!(message instanceof IoBuffer))
                    continue;
                ((IoBuffer) message).free();
            }
            while (writtenBytes < maxWrittenBytes);
        } catch (Exception e) {
            if (req != null) {
                req.getFuture().setException(e);
            }

            IoFilterChain filterChain = session.getFilterChain();
            filterChain.fireExceptionCaught(e);
            return false;
        }

        return true;
    }

    private int writeBuffer(S session, WriteRequest req, boolean hasFragmentation, int maxLength, long currentTime) throws Exception {
        IoBuffer buf = (IoBuffer) req.getMessage();
        int localWrittenBytes = 0;

        if (buf.hasRemaining()) {
            int length;

            if (hasFragmentation) {
                length = Math.min(buf.remaining(), maxLength);
            } else {
                length = buf.remaining();
            }

            try {
                localWrittenBytes = write(session, buf, length);
            } catch (IOException ioe) {

                buf.free();
                session.close(true);
                destroy(session);

                return 0;
            }
        }

        session.increaseWrittenBytes(localWrittenBytes, currentTime);

        if (!buf.hasRemaining() || (!hasFragmentation && localWrittenBytes != 0)) {

            int pos = buf.position();
            buf.reset();

            fireMessageSent(session, req);

            buf.position(pos);
        }

        return localWrittenBytes;
    }

    private int writeFile(S session, WriteRequest req, boolean hasFragmentation, int maxLength, long currentTime) throws Exception {
        int localWrittenBytes;
        FileRegion region = (FileRegion) req.getMessage();

        if (region.getRemainingBytes() > 0L) {
            int length;

            if (hasFragmentation) {
                length = (int) Math.min(region.getRemainingBytes(), maxLength);
            } else {
                length = (int) Math.min(2147483647L, region.getRemainingBytes());
            }

            localWrittenBytes = transferFile(session, region, length);
            region.update(localWrittenBytes);
        } else {
            localWrittenBytes = 0;
        }

        session.increaseWrittenBytes(localWrittenBytes, currentTime);

        if (region.getRemainingBytes() <= 0L || (!hasFragmentation && localWrittenBytes != 0)) {
            fireMessageSent(session, req);
        }

        return localWrittenBytes;
    }

    private void fireMessageSent(S session, WriteRequest req) {
        session.setCurrentWriteRequest(null);
        IoFilterChain filterChain = session.getFilterChain();
        filterChain.fireMessageSent(req);
    }

    protected abstract void doDispose() throws Exception;

    protected abstract int select(long paramLong) throws Exception;

    protected abstract int select() throws Exception;

    protected abstract boolean isSelectorEmpty();

    protected abstract void wakeup();

    protected abstract Iterator<S> allSessions();

    private void updateTrafficMask() {
        int queueSize = this.trafficControllingSessions.size();

        while (queueSize > 0) {
            AbstractIoSession abstractIoSession = (AbstractIoSession) this.trafficControllingSessions.poll();

            if (abstractIoSession == null) {
                return;
            }

            SessionState state = getState((S) abstractIoSession);

            switch (state) {
                case OPENED:
                    updateTrafficControl((S) abstractIoSession);
                    break;

                case CLOSING:
                    break;

                case OPENING:
                    this.trafficControllingSessions.add((S) abstractIoSession);
                    break;

                default:
                    throw new IllegalStateException(String.valueOf(state));
            }

            queueSize--;
        }
    }

    protected abstract Iterator<S> selectedSessions();

    protected abstract SessionState getState(S paramS);

    protected abstract boolean isWritable(S paramS);

    protected abstract boolean isReadable(S paramS);

    protected abstract void setInterestedInWrite(S paramS, boolean paramBoolean) throws Exception;

    protected abstract void setInterestedInRead(S paramS, boolean paramBoolean) throws Exception;

    protected abstract boolean isInterestedInRead(S paramS);

    protected abstract boolean isInterestedInWrite(S paramS);

    protected abstract void init(S paramS) throws Exception;

    protected abstract void destroy(S paramS) throws Exception;

    public void updateTrafficControl(S session) {
        try {
            setInterestedInRead(session, !session.isReadSuspended());
        } catch (Exception e) {
            IoFilterChain filterChain = session.getFilterChain();
            filterChain.fireExceptionCaught(e);
        }

        try {
            setInterestedInWrite(session, (!session.getWriteRequestQueue().isEmpty((IoSession) session) && !session.isWriteSuspended()));
        } catch (Exception e) {
            IoFilterChain filterChain = session.getFilterChain();
            filterChain.fireExceptionCaught(e);
        }
    }

    protected abstract int read(S paramS, IoBuffer paramIoBuffer) throws Exception;

    protected abstract int write(S paramS, IoBuffer paramIoBuffer, int paramInt) throws Exception;

    protected abstract int transferFile(S paramS, FileRegion paramFileRegion, int paramInt) throws Exception;

    protected abstract void registerNewSelector() throws IOException;

    protected abstract boolean isBrokenConnection() throws IOException;

    private class Processor implements Runnable {
        private Processor() {
        }

        public void run() {
            assert AbstractPollingIoProcessor.access$100(AbstractPollingIoProcessor.this).get() == this;

            int nSessions = 0;
            AbstractPollingIoProcessor.this.lastIdleCheckTime = System.currentTimeMillis();

            while (true) {
                try {
                    long t0 = System.currentTimeMillis();
                    int selected = AbstractPollingIoProcessor.this.select(1000L);
                    long t1 = System.currentTimeMillis();
                    long delta = t1 - t0;

                    if (selected == 0 && !AbstractPollingIoProcessor.this.wakeupCalled.get() && delta < 100L) {

                        if (AbstractPollingIoProcessor.this.isBrokenConnection()) {
                            AbstractPollingIoProcessor.LOG.warn("Broken connection");

                            AbstractPollingIoProcessor.this.wakeupCalled.getAndSet(false);

                            continue;
                        }
                        AbstractPollingIoProcessor.LOG.warn("Create a new selector. Selected is 0, delta = " + (t1 - t0));

                        AbstractPollingIoProcessor.this.registerNewSelector();

                        AbstractPollingIoProcessor.this.wakeupCalled.getAndSet(false);

                        continue;
                    }

                    nSessions += AbstractPollingIoProcessor.this.handleNewSessions();

                    AbstractPollingIoProcessor.this.updateTrafficMask();

                    if (selected > 0) {
                        AbstractPollingIoProcessor.this.process();
                    }

                    long currentTime = System.currentTimeMillis();
                    AbstractPollingIoProcessor.this.flush(currentTime);

                    nSessions -= AbstractPollingIoProcessor.this.removeSessions();

                    AbstractPollingIoProcessor.this.notifyIdleSessions(currentTime);

                    if (nSessions == 0) {
                        AbstractPollingIoProcessor.access$100(AbstractPollingIoProcessor.this).set(null);

                        if (AbstractPollingIoProcessor.this.newSessions.isEmpty() && AbstractPollingIoProcessor.this.isSelectorEmpty()) {

                            assert AbstractPollingIoProcessor.access$100(AbstractPollingIoProcessor.this).get() != this;

                            break;
                        }
                        assert AbstractPollingIoProcessor.access$100(AbstractPollingIoProcessor.this).get() != this;

                        if (!AbstractPollingIoProcessor.access$100(AbstractPollingIoProcessor.this).compareAndSet(null, this)) {

                            assert AbstractPollingIoProcessor.access$100(AbstractPollingIoProcessor.this).get() != this;

                            break;
                        }
                        assert AbstractPollingIoProcessor.access$100(AbstractPollingIoProcessor.this).get() == this;
                    }

                    if (AbstractPollingIoProcessor.this.isDisposing()) {
                        for (Iterator<S> i = AbstractPollingIoProcessor.this.allSessions(); i.hasNext(); ) {
                            AbstractPollingIoProcessor.this.scheduleRemove(i.next());
                        }

                        AbstractPollingIoProcessor.this.wakeup();
                    }
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

            try {
                synchronized (AbstractPollingIoProcessor.this.disposalLock) {
                    if (AbstractPollingIoProcessor.this.disposing) {
                        AbstractPollingIoProcessor.this.doDispose();
                    }
                }
            } catch (Exception e) {
                ExceptionMonitor.getInstance().exceptionCaught(e);
            } finally {
                AbstractPollingIoProcessor.this.disposalFuture.setValue(Boolean.valueOf(true));
            }
        }
    }

}

