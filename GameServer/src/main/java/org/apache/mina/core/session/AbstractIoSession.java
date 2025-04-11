package org.apache.mina.core.session;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.file.DefaultFileRegion;
import org.apache.mina.core.file.FilenameFileRegion;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.future.*;
import org.apache.mina.core.service.*;
import org.apache.mina.core.write.*;
import org.apache.mina.util.ExceptionMonitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractIoSession
        implements IoSession {
    private static final AttributeKey READY_READ_FUTURES_KEY = new AttributeKey(AbstractIoSession.class, "readyReadFutures");
    private static final AttributeKey WAITING_READ_FUTURES_KEY = new AttributeKey(AbstractIoSession.class, "waitingReadFutures");
    private static final IoFutureListener<CloseFuture> SCHEDULED_COUNTER_RESETTER = new IoFutureListener<CloseFuture>() {
        public void operationComplete(CloseFuture future) {
            AbstractIoSession session = (AbstractIoSession) future.getSession();
            session.scheduledWriteBytes.set(0);
            session.scheduledWriteMessages.set(0);
            session.readBytesThroughput = 0.0D;
            session.readMessagesThroughput = 0.0D;
            session.writtenBytesThroughput = 0.0D;
            session.writtenMessagesThroughput = 0.0D;
        }
    };
    private static final WriteRequest CLOSE_REQUEST = (WriteRequest) new DefaultWriteRequest(new Object());
    private static AtomicLong idGenerator = new AtomicLong(0L);
    private final IoHandler handler;
    private final IoService service;
    private final Object lock = new Object();
    private final long creationTime;
    private final CloseFuture closeFuture = (CloseFuture) new DefaultCloseFuture(this);
    private final AtomicBoolean scheduledForFlush = new AtomicBoolean();
    private final AtomicInteger scheduledWriteBytes = new AtomicInteger();
    private final AtomicInteger scheduledWriteMessages = new AtomicInteger();
    protected IoSessionConfig config;
    private IoSessionAttributeMap attributes;
    private WriteRequestQueue writeRequestQueue;
    private WriteRequest currentWriteRequest;
    private long sessionId;
    private volatile boolean closing;
    private boolean readSuspended = false;
    private boolean writeSuspended = false;
    private long readBytes;

    private long writtenBytes;

    private long readMessages;

    private long writtenMessages;

    private long lastReadTime;

    private long lastWriteTime;

    private long lastThroughputCalculationTime;

    private long lastReadBytes;

    private long lastWrittenBytes;

    private long lastReadMessages;

    private long lastWrittenMessages;

    private double readBytesThroughput;

    private double writtenBytesThroughput;

    private double readMessagesThroughput;

    private double writtenMessagesThroughput;

    private AtomicInteger idleCountForBoth = new AtomicInteger();

    private AtomicInteger idleCountForRead = new AtomicInteger();

    private AtomicInteger idleCountForWrite = new AtomicInteger();

    private long lastIdleTimeForBoth;

    private long lastIdleTimeForRead;

    private long lastIdleTimeForWrite;

    private boolean deferDecreaseReadBuffer = true;

    protected AbstractIoSession(IoService service) {
        this.service = service;
        this.handler = service.getHandler();

        long currentTime = System.currentTimeMillis();
        this.creationTime = currentTime;
        this.lastThroughputCalculationTime = currentTime;
        this.lastReadTime = currentTime;
        this.lastWriteTime = currentTime;
        this.lastIdleTimeForBoth = currentTime;
        this.lastIdleTimeForRead = currentTime;
        this.lastIdleTimeForWrite = currentTime;

        this.closeFuture.addListener(SCHEDULED_COUNTER_RESETTER);

        this.sessionId = idGenerator.incrementAndGet();
    }

    public static void notifyIdleness(Iterator<? extends IoSession> sessions, long currentTime) {
        IoSession s = null;
        while (sessions.hasNext()) {
            s = sessions.next();
            notifyIdleSession(s, currentTime);
        }
    }

    public static void notifyIdleSession(IoSession session, long currentTime) {
        notifyIdleSession0(session, currentTime, session.getConfig().getIdleTimeInMillis(IdleStatus.BOTH_IDLE), IdleStatus.BOTH_IDLE, Math.max(session.getLastIoTime(), session.getLastIdleTime(IdleStatus.BOTH_IDLE)));

        notifyIdleSession0(session, currentTime, session.getConfig().getIdleTimeInMillis(IdleStatus.READER_IDLE), IdleStatus.READER_IDLE, Math.max(session.getLastReadTime(), session.getLastIdleTime(IdleStatus.READER_IDLE)));

        notifyIdleSession0(session, currentTime, session.getConfig().getIdleTimeInMillis(IdleStatus.WRITER_IDLE), IdleStatus.WRITER_IDLE, Math.max(session.getLastWriteTime(), session.getLastIdleTime(IdleStatus.WRITER_IDLE)));

        notifyWriteTimeout(session, currentTime);
    }

    private static void notifyIdleSession0(IoSession session, long currentTime, long idleTime, IdleStatus status, long lastIoTime) {
        if (idleTime > 0L && lastIoTime != 0L && currentTime - lastIoTime >= idleTime) {
            session.getFilterChain().fireSessionIdle(status);
        }
    }

    private static void notifyWriteTimeout(IoSession session, long currentTime) {
        long writeTimeout = session.getConfig().getWriteTimeoutInMillis();
        if (writeTimeout > 0L && currentTime - session.getLastWriteTime() >= writeTimeout && !session.getWriteRequestQueue().isEmpty(session)) {

            WriteRequest request = session.getCurrentWriteRequest();
            if (request != null) {
                session.setCurrentWriteRequest(null);
                WriteTimeoutException cause = new WriteTimeoutException(request);
                request.getFuture().setException((Throwable) cause);
                session.getFilterChain().fireExceptionCaught((Throwable) cause);

                session.close(true);
            }
        }
    }

    public final long getId() {
        return this.sessionId;
    }

    public final boolean isConnected() {
        return !this.closeFuture.isClosed();
    }

    public final boolean isClosing() {
        return (this.closing || this.closeFuture.isClosed());
    }

    public boolean isSecured() {
        return false;
    }

    public final CloseFuture getCloseFuture() {
        return this.closeFuture;
    }

    public final boolean isScheduledForFlush() {
        return this.scheduledForFlush.get();
    }

    public final void scheduledForFlush() {
        this.scheduledForFlush.set(true);
    }

    public final void unscheduledForFlush() {
        this.scheduledForFlush.set(false);
    }

    public final boolean setScheduledForFlush(boolean schedule) {
        if (schedule) {

            return this.scheduledForFlush.compareAndSet(false, schedule);
        }

        this.scheduledForFlush.set(schedule);
        return true;
    }

    public final CloseFuture close(boolean rightNow) {
        if (!isClosing()) {
            if (rightNow) {
                return close();
            }

            return closeOnFlush();
        }
        return this.closeFuture;
    }

    public final CloseFuture close() {
        synchronized (this.lock) {
            if (isClosing()) {
                return this.closeFuture;
            }

            this.closing = true;
        }

        getFilterChain().fireFilterClose();
        return this.closeFuture;
    }

    private final CloseFuture closeOnFlush() {
        getWriteRequestQueue().offer(this, CLOSE_REQUEST);
        getProcessor().flush(this);
        return this.closeFuture;
    }

    public IoHandler getHandler() {
        return this.handler;
    }

    public IoSessionConfig getConfig() {
        return this.config;
    }

    public final ReadFuture read() {
        DefaultReadFuture defaultReadFuture;
        if (!getConfig().isUseReadOperation()) {
            throw new IllegalStateException("useReadOperation is not enabled.");
        }

        Queue<ReadFuture> readyReadFutures = getReadyReadFutures();

        synchronized (readyReadFutures) {
            ReadFuture future = readyReadFutures.poll();
            if (future != null) {
                if (future.isClosed()) {
                    readyReadFutures.offer(future);
                }
            } else {
                defaultReadFuture = new DefaultReadFuture(this);
                getWaitingReadFutures().offer(defaultReadFuture);
            }
        }

        return (ReadFuture) defaultReadFuture;
    }

    public final void offerReadFuture(Object message) {
        newReadFuture().setRead(message);
    }

    public final void offerFailedReadFuture(Throwable exception) {
        newReadFuture().setException(exception);
    }

    public final void offerClosedReadFuture() {
        Queue<ReadFuture> readyReadFutures = getReadyReadFutures();
        synchronized (readyReadFutures) {
            newReadFuture().setClosed();
        }
    }

    private ReadFuture newReadFuture() {
        DefaultReadFuture defaultReadFuture;
        Queue<ReadFuture> readyReadFutures = getReadyReadFutures();
        Queue<ReadFuture> waitingReadFutures = getWaitingReadFutures();

        synchronized (readyReadFutures) {
            ReadFuture future = waitingReadFutures.poll();
            if (future == null) {
                defaultReadFuture = new DefaultReadFuture(this);
                readyReadFutures.offer(defaultReadFuture);
            }
        }
        return (ReadFuture) defaultReadFuture;
    }

    private Queue<ReadFuture> getReadyReadFutures() {
        Queue<ReadFuture> readyReadFutures = (Queue<ReadFuture>) getAttribute(READY_READ_FUTURES_KEY);
        if (readyReadFutures == null) {
            readyReadFutures = new ConcurrentLinkedQueue<ReadFuture>();

            Queue<ReadFuture> oldReadyReadFutures = (Queue<ReadFuture>) setAttributeIfAbsent(READY_READ_FUTURES_KEY, readyReadFutures);

            if (oldReadyReadFutures != null) {
                readyReadFutures = oldReadyReadFutures;
            }
        }
        return readyReadFutures;
    }

    private Queue<ReadFuture> getWaitingReadFutures() {
        Queue<ReadFuture> waitingReadyReadFutures = (Queue<ReadFuture>) getAttribute(WAITING_READ_FUTURES_KEY);
        if (waitingReadyReadFutures == null) {
            waitingReadyReadFutures = new ConcurrentLinkedQueue<ReadFuture>();

            Queue<ReadFuture> oldWaitingReadyReadFutures = (Queue<ReadFuture>) setAttributeIfAbsent(WAITING_READ_FUTURES_KEY, waitingReadyReadFutures);

            if (oldWaitingReadyReadFutures != null) {
                waitingReadyReadFutures = oldWaitingReadyReadFutures;
            }
        }
        return waitingReadyReadFutures;
    }

    public WriteFuture write(Object message) {
        return write(message, (SocketAddress) null);
    }

    public WriteFuture write(Object message, SocketAddress remoteAddress) {
        if (message == null) {
            throw new IllegalArgumentException("Trying to write a null message : not allowed");
        }

        if (!getTransportMetadata().isConnectionless() && remoteAddress != null) {
            throw new UnsupportedOperationException();
        }

        if (isClosing() || !isConnected()) {
            DefaultWriteFuture defaultWriteFuture1 = new DefaultWriteFuture(this);
            DefaultWriteRequest defaultWriteRequest1 = new DefaultWriteRequest(message, (WriteFuture) defaultWriteFuture1, remoteAddress);
            WriteToClosedSessionException writeToClosedSessionException = new WriteToClosedSessionException((WriteRequest) defaultWriteRequest1);
            defaultWriteFuture1.setException((Throwable) writeToClosedSessionException);
            return (WriteFuture) defaultWriteFuture1;
        }

        FileChannel openedFileChannel = null;

        try {
            if (message instanceof IoBuffer && !((IoBuffer) message).hasRemaining()) {
                throw new IllegalArgumentException("message is empty. Forgot to call flip()?");
            }
            if (message instanceof FileChannel) {
                FileChannel fileChannel = (FileChannel) message;
                message = new DefaultFileRegion(fileChannel, 0L, fileChannel.size());
            } else if (message instanceof File) {
                File file = (File) message;
                openedFileChannel = (new FileInputStream(file)).getChannel();
                message = new FilenameFileRegion(file, openedFileChannel, 0L, openedFileChannel.size());
            }
        } catch (IOException e) {
            ExceptionMonitor.getInstance().exceptionCaught(e);
            return DefaultWriteFuture.newNotWrittenFuture(this, e);
        }

        DefaultWriteFuture defaultWriteFuture = new DefaultWriteFuture(this);
        DefaultWriteRequest defaultWriteRequest = new DefaultWriteRequest(message, (WriteFuture) defaultWriteFuture, remoteAddress);

        IoFilterChain filterChain = getFilterChain();
        filterChain.fireFilterWrite((WriteRequest) defaultWriteRequest);

        if (openedFileChannel != null) {

            final FileChannel finalChannel = openedFileChannel;
            defaultWriteFuture.addListener(new IoFutureListener<WriteFuture>() {
                public void operationComplete(WriteFuture future) {
                    try {
                        finalChannel.close();
                    } catch (IOException e) {
                        ExceptionMonitor.getInstance().exceptionCaught(e);
                    }
                }
            });
        }

        return (WriteFuture) defaultWriteFuture;
    }

    public final Object getAttachment() {
        return getAttribute("");
    }

    public final Object setAttachment(Object attachment) {
        return setAttribute("", attachment);
    }

    public final Object getAttribute(Object key) {
        return getAttribute(key, (Object) null);
    }

    public final Object getAttribute(Object key, Object defaultValue) {
        return this.attributes.getAttribute(this, key, defaultValue);
    }

    public final Object setAttribute(Object key, Object value) {
        return this.attributes.setAttribute(this, key, value);
    }

    public final Object setAttribute(Object key) {
        return setAttribute(key, Boolean.TRUE);
    }

    public final Object setAttributeIfAbsent(Object key, Object value) {
        return this.attributes.setAttributeIfAbsent(this, key, value);
    }

    public final Object setAttributeIfAbsent(Object key) {
        return setAttributeIfAbsent(key, Boolean.TRUE);
    }

    public final Object removeAttribute(Object key) {
        return this.attributes.removeAttribute(this, key);
    }

    public final boolean removeAttribute(Object key, Object value) {
        return this.attributes.removeAttribute(this, key, value);
    }

    public final boolean replaceAttribute(Object key, Object oldValue, Object newValue) {
        return this.attributes.replaceAttribute(this, key, oldValue, newValue);
    }

    public final boolean containsAttribute(Object key) {
        return this.attributes.containsAttribute(this, key);
    }

    public final Set<Object> getAttributeKeys() {
        return this.attributes.getAttributeKeys(this);
    }

    public final IoSessionAttributeMap getAttributeMap() {
        return this.attributes;
    }

    public final void setAttributeMap(IoSessionAttributeMap attributes) {
        this.attributes = attributes;
    }

    public final void suspendRead() {
        this.readSuspended = true;
        if (isClosing() || !isConnected()) {
            return;
        }
        getProcessor().updateTrafficControl(this);
    }

    public final void suspendWrite() {
        this.writeSuspended = true;
        if (isClosing() || !isConnected()) {
            return;
        }
        getProcessor().updateTrafficControl(this);
    }

    public final void resumeRead() {
        this.readSuspended = false;
        if (isClosing() || !isConnected()) {
            return;
        }
        getProcessor().updateTrafficControl(this);
    }

    public final void resumeWrite() {
        this.writeSuspended = false;
        if (isClosing() || !isConnected()) {
            return;
        }
        getProcessor().updateTrafficControl(this);
    }

    public boolean isReadSuspended() {
        return this.readSuspended;
    }

    public boolean isWriteSuspended() {
        return this.writeSuspended;
    }

    public final long getReadBytes() {
        return this.readBytes;
    }

    public final long getWrittenBytes() {
        return this.writtenBytes;
    }

    public final long getReadMessages() {
        return this.readMessages;
    }

    public final long getWrittenMessages() {
        return this.writtenMessages;
    }

    public final double getReadBytesThroughput() {
        return this.readBytesThroughput;
    }

    public final double getWrittenBytesThroughput() {
        return this.writtenBytesThroughput;
    }

    public final double getReadMessagesThroughput() {
        return this.readMessagesThroughput;
    }

    public final double getWrittenMessagesThroughput() {
        return this.writtenMessagesThroughput;
    }

    public final void updateThroughput(long currentTime, boolean force) {
        int interval = (int) (currentTime - this.lastThroughputCalculationTime);

        long minInterval = getConfig().getThroughputCalculationIntervalInMillis();

        if ((minInterval == 0L || interval < minInterval) && !force) {
            return;
        }

        this.readBytesThroughput = (this.readBytes - this.lastReadBytes) * 1000.0D / interval;
        this.writtenBytesThroughput = (this.writtenBytes - this.lastWrittenBytes) * 1000.0D / interval;
        this.readMessagesThroughput = (this.readMessages - this.lastReadMessages) * 1000.0D / interval;
        this.writtenMessagesThroughput = (this.writtenMessages - this.lastWrittenMessages) * 1000.0D / interval;

        this.lastReadBytes = this.readBytes;
        this.lastWrittenBytes = this.writtenBytes;
        this.lastReadMessages = this.readMessages;
        this.lastWrittenMessages = this.writtenMessages;

        this.lastThroughputCalculationTime = currentTime;
    }

    public final long getScheduledWriteBytes() {
        return this.scheduledWriteBytes.get();
    }

    protected void setScheduledWriteBytes(int byteCount) {
        this.scheduledWriteBytes.set(byteCount);
    }

    public final int getScheduledWriteMessages() {
        return this.scheduledWriteMessages.get();
    }

    protected void setScheduledWriteMessages(int messages) {
        this.scheduledWriteMessages.set(messages);
    }

    public final void increaseReadBytes(long increment, long currentTime) {
        if (increment <= 0L) {
            return;
        }

        this.readBytes += increment;
        this.lastReadTime = currentTime;
        this.idleCountForBoth.set(0);
        this.idleCountForRead.set(0);

        if (getService() instanceof AbstractIoService) {
            ((AbstractIoService) getService()).getStatistics().increaseReadBytes(increment, currentTime);
        }
    }

    public final void increaseReadMessages(long currentTime) {
        this.readMessages++;
        this.lastReadTime = currentTime;
        this.idleCountForBoth.set(0);
        this.idleCountForRead.set(0);

        if (getService() instanceof AbstractIoService) {
            ((AbstractIoService) getService()).getStatistics().increaseReadMessages(currentTime);
        }
    }

    public final void increaseWrittenBytes(int increment, long currentTime) {
        if (increment <= 0) {
            return;
        }

        this.writtenBytes += increment;
        this.lastWriteTime = currentTime;
        this.idleCountForBoth.set(0);
        this.idleCountForWrite.set(0);

        if (getService() instanceof AbstractIoService) {
            ((AbstractIoService) getService()).getStatistics().increaseWrittenBytes(increment, currentTime);
        }

        increaseScheduledWriteBytes(-increment);
    }

    public final void increaseWrittenMessages(WriteRequest request, long currentTime) {
        Object message = request.getMessage();

        if (message instanceof IoBuffer) {
            IoBuffer b = (IoBuffer) message;

            if (b.hasRemaining()) {
                return;
            }
        }

        this.writtenMessages++;
        this.lastWriteTime = currentTime;

        if (getService() instanceof AbstractIoService) {
            ((AbstractIoService) getService()).getStatistics().increaseWrittenMessages(currentTime);
        }

        decreaseScheduledWriteMessages();
    }

    public final void increaseScheduledWriteBytes(int increment) {
        this.scheduledWriteBytes.addAndGet(increment);
        if (getService() instanceof AbstractIoService) {
            ((AbstractIoService) getService()).getStatistics().increaseScheduledWriteBytes(increment);
        }
    }

    public final void increaseScheduledWriteMessages() {
        this.scheduledWriteMessages.incrementAndGet();
        if (getService() instanceof AbstractIoService) {
            ((AbstractIoService) getService()).getStatistics().increaseScheduledWriteMessages();
        }
    }

    private void decreaseScheduledWriteMessages() {
        this.scheduledWriteMessages.decrementAndGet();
        if (getService() instanceof AbstractIoService) {
            ((AbstractIoService) getService()).getStatistics().decreaseScheduledWriteMessages();
        }
    }

    public final void decreaseScheduledBytesAndMessages(WriteRequest request) {
        Object message = request.getMessage();
        if (message instanceof IoBuffer) {
            IoBuffer b = (IoBuffer) message;
            if (b.hasRemaining()) {
                increaseScheduledWriteBytes(-((IoBuffer) message).remaining());
            } else {
                decreaseScheduledWriteMessages();
            }
        } else {
            decreaseScheduledWriteMessages();
        }
    }

    public final WriteRequestQueue getWriteRequestQueue() {
        if (this.writeRequestQueue == null) {
            throw new IllegalStateException();
        }
        return this.writeRequestQueue;
    }

    public final void setWriteRequestQueue(WriteRequestQueue writeRequestQueue) {
        this.writeRequestQueue = new CloseAwareWriteQueue(writeRequestQueue);
    }

    public final WriteRequest getCurrentWriteRequest() {
        return this.currentWriteRequest;
    }

    public final void setCurrentWriteRequest(WriteRequest currentWriteRequest) {
        this.currentWriteRequest = currentWriteRequest;
    }

    public final Object getCurrentWriteMessage() {
        WriteRequest req = getCurrentWriteRequest();
        if (req == null) {
            return null;
        }
        return req.getMessage();
    }

    public final void increaseReadBufferSize() {
        int newReadBufferSize = getConfig().getReadBufferSize() << 1;
        if (newReadBufferSize <= getConfig().getMaxReadBufferSize()) {
            getConfig().setReadBufferSize(newReadBufferSize);
        } else {
            getConfig().setReadBufferSize(getConfig().getMaxReadBufferSize());
        }

        this.deferDecreaseReadBuffer = true;
    }

    public final void decreaseReadBufferSize() {
        if (this.deferDecreaseReadBuffer) {
            this.deferDecreaseReadBuffer = false;

            return;
        }
        if (getConfig().getReadBufferSize() > getConfig().getMinReadBufferSize()) {
            getConfig().setReadBufferSize(getConfig().getReadBufferSize() >>> 1);
        }

        this.deferDecreaseReadBuffer = true;
    }

    public final long getCreationTime() {
        return this.creationTime;
    }

    public final long getLastIoTime() {
        return Math.max(this.lastReadTime, this.lastWriteTime);
    }

    public final long getLastReadTime() {
        return this.lastReadTime;
    }

    public final long getLastWriteTime() {
        return this.lastWriteTime;
    }

    public final boolean isIdle(IdleStatus status) {
        if (status == IdleStatus.BOTH_IDLE) {
            return (this.idleCountForBoth.get() > 0);
        }

        if (status == IdleStatus.READER_IDLE) {
            return (this.idleCountForRead.get() > 0);
        }

        if (status == IdleStatus.WRITER_IDLE) {
            return (this.idleCountForWrite.get() > 0);
        }

        throw new IllegalArgumentException("Unknown idle status: " + status);
    }

    public final boolean isBothIdle() {
        return isIdle(IdleStatus.BOTH_IDLE);
    }

    public final boolean isReaderIdle() {
        return isIdle(IdleStatus.READER_IDLE);
    }

    public final boolean isWriterIdle() {
        return isIdle(IdleStatus.WRITER_IDLE);
    }

    public final int getIdleCount(IdleStatus status) {
        if (getConfig().getIdleTime(status) == 0) {
            if (status == IdleStatus.BOTH_IDLE) {
                this.idleCountForBoth.set(0);
            }

            if (status == IdleStatus.READER_IDLE) {
                this.idleCountForRead.set(0);
            }

            if (status == IdleStatus.WRITER_IDLE) {
                this.idleCountForWrite.set(0);
            }
        }

        if (status == IdleStatus.BOTH_IDLE) {
            return this.idleCountForBoth.get();
        }

        if (status == IdleStatus.READER_IDLE) {
            return this.idleCountForRead.get();
        }

        if (status == IdleStatus.WRITER_IDLE) {
            return this.idleCountForWrite.get();
        }

        throw new IllegalArgumentException("Unknown idle status: " + status);
    }

    public final long getLastIdleTime(IdleStatus status) {
        if (status == IdleStatus.BOTH_IDLE) {
            return this.lastIdleTimeForBoth;
        }

        if (status == IdleStatus.READER_IDLE) {
            return this.lastIdleTimeForRead;
        }

        if (status == IdleStatus.WRITER_IDLE) {
            return this.lastIdleTimeForWrite;
        }

        throw new IllegalArgumentException("Unknown idle status: " + status);
    }

    public final void increaseIdleCount(IdleStatus status, long currentTime) {
        if (status == IdleStatus.BOTH_IDLE) {
            this.idleCountForBoth.incrementAndGet();
            this.lastIdleTimeForBoth = currentTime;
        } else if (status == IdleStatus.READER_IDLE) {
            this.idleCountForRead.incrementAndGet();
            this.lastIdleTimeForRead = currentTime;
        } else if (status == IdleStatus.WRITER_IDLE) {
            this.idleCountForWrite.incrementAndGet();
            this.lastIdleTimeForWrite = currentTime;
        } else {
            throw new IllegalArgumentException("Unknown idle status: " + status);
        }
    }

    public final int getBothIdleCount() {
        return getIdleCount(IdleStatus.BOTH_IDLE);
    }

    public final long getLastBothIdleTime() {
        return getLastIdleTime(IdleStatus.BOTH_IDLE);
    }

    public final long getLastReaderIdleTime() {
        return getLastIdleTime(IdleStatus.READER_IDLE);
    }

    public final long getLastWriterIdleTime() {
        return getLastIdleTime(IdleStatus.WRITER_IDLE);
    }

    public final int getReaderIdleCount() {
        return getIdleCount(IdleStatus.READER_IDLE);
    }

    public final int getWriterIdleCount() {
        return getIdleCount(IdleStatus.WRITER_IDLE);
    }

    public SocketAddress getServiceAddress() {
        IoService service = getService();
        if (service instanceof IoAcceptor) {
            return ((IoAcceptor) service).getLocalAddress();
        }

        return getRemoteAddress();
    }

    public final int hashCode() {
        return super.hashCode();
    }

    public final boolean equals(Object o) {
        return super.equals(o);
    }

    public String toString() {
        if (isConnected() || isClosing()) {
            String remote = null;
            String local = null;

            try {
                remote = String.valueOf(getRemoteAddress());
            } catch (Exception e) {
                remote = "Cannot get the remote address informations: " + e.getMessage();
            }

            try {
                local = String.valueOf(getLocalAddress());
            } catch (Exception e) {
            }

            if (getService() instanceof IoAcceptor) {
                return "(" + getIdAsString() + ": " + getServiceName() + ", server, " + remote + " => " + local + ')';
            }

            return "(" + getIdAsString() + ": " + getServiceName() + ", client, " + local + " => " + remote + ')';
        }

        return "(" + getIdAsString() + ") Session disconnected ...";
    }

    private String getIdAsString() {
        String id = Long.toHexString(getId()).toUpperCase();

        while (id.length() < 8) {
            id = '0' + id;
        }
        id = "0x" + id;

        return id;
    }

    private String getServiceName() {
        TransportMetadata tm = getTransportMetadata();
        if (tm == null) {
            return "null";
        }

        return tm.getProviderName() + ' ' + tm.getName();
    }

    public IoService getService() {
        return this.service;
    }

    public abstract IoProcessor getProcessor();

    private class CloseAwareWriteQueue
            implements WriteRequestQueue {
        private final WriteRequestQueue queue;

        public CloseAwareWriteQueue(WriteRequestQueue queue) {
            this.queue = queue;
        }

        public synchronized WriteRequest poll(IoSession session) {
            WriteRequest answer = this.queue.poll(session);

            if (answer == AbstractIoSession.CLOSE_REQUEST) {
                AbstractIoSession.this.close();
                dispose(session);
                answer = null;
            }

            return answer;
        }

        public void offer(IoSession session, WriteRequest e) {
            this.queue.offer(session, e);
        }

        public boolean isEmpty(IoSession session) {
            return this.queue.isEmpty(session);
        }

        public void clear(IoSession session) {
            this.queue.clear(session);
        }

        public void dispose(IoSession session) {
            this.queue.dispose(session);
        }

        public int size() {
            return this.queue.size();
        }
    }
}

