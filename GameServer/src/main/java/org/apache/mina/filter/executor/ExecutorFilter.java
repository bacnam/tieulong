package org.apache.mina.filter.executor;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.filterchain.IoFilterEvent;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoEventType;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

import java.util.EnumSet;
import java.util.concurrent.*;

public class ExecutorFilter
        extends IoFilterAdapter {
    private static final int DEFAULT_MAX_POOL_SIZE = 16;
    private static final int BASE_THREAD_NUMBER = 0;
    private static final long DEFAULT_KEEPALIVE_TIME = 30L;
    private static final boolean MANAGEABLE_EXECUTOR = true;
    private static final boolean NOT_MANAGEABLE_EXECUTOR = false;
    private static IoEventType[] DEFAULT_EVENT_SET = new IoEventType[]{IoEventType.EXCEPTION_CAUGHT, IoEventType.MESSAGE_RECEIVED, IoEventType.MESSAGE_SENT, IoEventType.SESSION_CLOSED, IoEventType.SESSION_IDLE, IoEventType.SESSION_OPENED};
    private EnumSet<IoEventType> eventTypes;
    private Executor executor;
    private boolean manageableExecutor;

    public ExecutorFilter() {
        Executor executor = createDefaultExecutor(0, 16, 30L, TimeUnit.SECONDS, Executors.defaultThreadFactory(), null);

        init(executor, true, new IoEventType[0]);
    }

    public ExecutorFilter(int maximumPoolSize) {
        Executor executor = createDefaultExecutor(0, maximumPoolSize, 30L, TimeUnit.SECONDS, Executors.defaultThreadFactory(), null);

        init(executor, true, new IoEventType[0]);
    }

    public ExecutorFilter(int corePoolSize, int maximumPoolSize) {
        Executor executor = createDefaultExecutor(corePoolSize, maximumPoolSize, 30L, TimeUnit.SECONDS, Executors.defaultThreadFactory(), null);

        init(executor, true, new IoEventType[0]);
    }

    public ExecutorFilter(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit) {
        Executor executor = createDefaultExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory(), null);

        init(executor, true, new IoEventType[0]);
    }

    public ExecutorFilter(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, IoEventQueueHandler queueHandler) {
        Executor executor = createDefaultExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory(), queueHandler);

        init(executor, true, new IoEventType[0]);
    }

    public ExecutorFilter(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory) {
        Executor executor = createDefaultExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, threadFactory, null);

        init(executor, true, new IoEventType[0]);
    }

    public ExecutorFilter(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory, IoEventQueueHandler queueHandler) {
        Executor executor = new OrderedThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, threadFactory, queueHandler);

        init(executor, true, new IoEventType[0]);
    }

    public ExecutorFilter(IoEventType... eventTypes) {
        Executor executor = createDefaultExecutor(0, 16, 30L, TimeUnit.SECONDS, Executors.defaultThreadFactory(), null);

        init(executor, true, eventTypes);
    }

    public ExecutorFilter(int maximumPoolSize, IoEventType... eventTypes) {
        Executor executor = createDefaultExecutor(0, maximumPoolSize, 30L, TimeUnit.SECONDS, Executors.defaultThreadFactory(), null);

        init(executor, true, eventTypes);
    }

    public ExecutorFilter(int corePoolSize, int maximumPoolSize, IoEventType... eventTypes) {
        Executor executor = createDefaultExecutor(corePoolSize, maximumPoolSize, 30L, TimeUnit.SECONDS, Executors.defaultThreadFactory(), null);

        init(executor, true, eventTypes);
    }

    public ExecutorFilter(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, IoEventType... eventTypes) {
        Executor executor = createDefaultExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory(), null);

        init(executor, true, eventTypes);
    }

    public ExecutorFilter(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, IoEventQueueHandler queueHandler, IoEventType... eventTypes) {
        Executor executor = createDefaultExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory(), queueHandler);

        init(executor, true, eventTypes);
    }

    public ExecutorFilter(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory, IoEventType... eventTypes) {
        Executor executor = createDefaultExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, threadFactory, null);

        init(executor, true, eventTypes);
    }

    public ExecutorFilter(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory, IoEventQueueHandler queueHandler, IoEventType... eventTypes) {
        Executor executor = new OrderedThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, threadFactory, queueHandler);

        init(executor, true, eventTypes);
    }

    public ExecutorFilter(Executor executor) {
        init(executor, false, new IoEventType[0]);
    }

    public ExecutorFilter(Executor executor, IoEventType... eventTypes) {
        init(executor, false, eventTypes);
    }

    private Executor createDefaultExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory, IoEventQueueHandler queueHandler) {
        Executor executor = new OrderedThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, threadFactory, queueHandler);

        return executor;
    }

    private void initEventTypes(IoEventType... eventTypes) {
        if (eventTypes == null || eventTypes.length == 0) {
            eventTypes = DEFAULT_EVENT_SET;
        }

        this.eventTypes = EnumSet.of(eventTypes[0], eventTypes);

        if (this.eventTypes.contains(IoEventType.SESSION_CREATED)) {
            this.eventTypes = null;
            throw new IllegalArgumentException(IoEventType.SESSION_CREATED + " is not allowed.");
        }
    }

    private void init(Executor executor, boolean manageableExecutor, IoEventType... eventTypes) {
        if (executor == null) {
            throw new IllegalArgumentException("executor");
        }

        initEventTypes(eventTypes);
        this.executor = executor;
        this.manageableExecutor = manageableExecutor;
    }

    public void destroy() {
        if (this.manageableExecutor) {
            ((ExecutorService) this.executor).shutdown();
        }
    }

    public final Executor getExecutor() {
        return this.executor;
    }

    protected void fireEvent(IoFilterEvent event) {
        this.executor.execute((Runnable) event);
    }

    public void onPreAdd(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws Exception {
        if (parent.contains((IoFilter) this)) {
            throw new IllegalArgumentException("You can't add the same filter instance more than once.  Create another instance and add it.");
        }
    }

    public final void sessionOpened(IoFilter.NextFilter nextFilter, IoSession session) {
        if (this.eventTypes.contains(IoEventType.SESSION_OPENED)) {
            IoFilterEvent event = new IoFilterEvent(nextFilter, IoEventType.SESSION_OPENED, session, null);
            fireEvent(event);
        } else {
            nextFilter.sessionOpened(session);
        }
    }

    public final void sessionClosed(IoFilter.NextFilter nextFilter, IoSession session) {
        if (this.eventTypes.contains(IoEventType.SESSION_CLOSED)) {
            IoFilterEvent event = new IoFilterEvent(nextFilter, IoEventType.SESSION_CLOSED, session, null);
            fireEvent(event);
        } else {
            nextFilter.sessionClosed(session);
        }
    }

    public final void sessionIdle(IoFilter.NextFilter nextFilter, IoSession session, IdleStatus status) {
        if (this.eventTypes.contains(IoEventType.SESSION_IDLE)) {
            IoFilterEvent event = new IoFilterEvent(nextFilter, IoEventType.SESSION_IDLE, session, status);
            fireEvent(event);
        } else {
            nextFilter.sessionIdle(session, status);
        }
    }

    public final void exceptionCaught(IoFilter.NextFilter nextFilter, IoSession session, Throwable cause) {
        if (this.eventTypes.contains(IoEventType.EXCEPTION_CAUGHT)) {
            IoFilterEvent event = new IoFilterEvent(nextFilter, IoEventType.EXCEPTION_CAUGHT, session, cause);
            fireEvent(event);
        } else {
            nextFilter.exceptionCaught(session, cause);
        }
    }

    public final void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object message) {
        if (this.eventTypes.contains(IoEventType.MESSAGE_RECEIVED)) {
            IoFilterEvent event = new IoFilterEvent(nextFilter, IoEventType.MESSAGE_RECEIVED, session, message);
            fireEvent(event);
        } else {
            nextFilter.messageReceived(session, message);
        }
    }

    public final void messageSent(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) {
        if (this.eventTypes.contains(IoEventType.MESSAGE_SENT)) {
            IoFilterEvent event = new IoFilterEvent(nextFilter, IoEventType.MESSAGE_SENT, session, writeRequest);
            fireEvent(event);
        } else {
            nextFilter.messageSent(session, writeRequest);
        }
    }

    public final void filterWrite(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) {
        if (this.eventTypes.contains(IoEventType.WRITE)) {
            IoFilterEvent event = new IoFilterEvent(nextFilter, IoEventType.WRITE, session, writeRequest);
            fireEvent(event);
        } else {
            nextFilter.filterWrite(session, writeRequest);
        }
    }

    public final void filterClose(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
        if (this.eventTypes.contains(IoEventType.CLOSE)) {
            IoFilterEvent event = new IoFilterEvent(nextFilter, IoEventType.CLOSE, session, null);
            fireEvent(event);
        } else {
            nextFilter.filterClose(session);
        }
    }
}

