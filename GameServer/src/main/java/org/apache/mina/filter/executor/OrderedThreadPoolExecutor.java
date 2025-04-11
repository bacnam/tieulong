package org.apache.mina.filter.executor;

import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.DummySession;
import org.apache.mina.core.session.IoEvent;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderedThreadPoolExecutor
        extends ThreadPoolExecutor {
    private static final int DEFAULT_INITIAL_THREAD_POOL_SIZE = 0;
    private static final int DEFAULT_MAX_THREAD_POOL = 16;
    private static final int DEFAULT_KEEP_ALIVE = 30;
    private static final IoSession EXIT_SIGNAL = (IoSession) new DummySession();
    private static Logger LOGGER = LoggerFactory.getLogger(OrderedThreadPoolExecutor.class);
    private final AttributeKey TASKS_QUEUE = new AttributeKey(getClass(), "tasksQueue");

    private final BlockingQueue<IoSession> waitingSessions = new LinkedBlockingQueue<IoSession>();

    private final Set<Worker> workers = new HashSet<Worker>();
    private final AtomicInteger idleWorkers = new AtomicInteger();
    private final IoEventQueueHandler eventQueueHandler;
    private volatile int largestPoolSize;
    private long completedTaskCount;
    private volatile boolean shutdown;

    public OrderedThreadPoolExecutor() {
        this(0, 16, 30L, TimeUnit.SECONDS, Executors.defaultThreadFactory(), (IoEventQueueHandler) null);
    }

    public OrderedThreadPoolExecutor(int maximumPoolSize) {
        this(0, maximumPoolSize, 30L, TimeUnit.SECONDS, Executors.defaultThreadFactory(), (IoEventQueueHandler) null);
    }

    public OrderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize) {
        this(corePoolSize, maximumPoolSize, 30L, TimeUnit.SECONDS, Executors.defaultThreadFactory(), (IoEventQueueHandler) null);
    }

    public OrderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory(), (IoEventQueueHandler) null);
    }

    public OrderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, IoEventQueueHandler eventQueueHandler) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory(), eventQueueHandler);
    }

    public OrderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, threadFactory, (IoEventQueueHandler) null);
    }

    public OrderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory, IoEventQueueHandler eventQueueHandler) {
        super(0, 1, keepAliveTime, unit, new SynchronousQueue<Runnable>(), threadFactory, new ThreadPoolExecutor.AbortPolicy());

        if (corePoolSize < 0) {
            throw new IllegalArgumentException("corePoolSize: " + corePoolSize);
        }

        if (maximumPoolSize == 0 || maximumPoolSize < corePoolSize) {
            throw new IllegalArgumentException("maximumPoolSize: " + maximumPoolSize);
        }

        super.setCorePoolSize(corePoolSize);
        super.setMaximumPoolSize(maximumPoolSize);

        if (eventQueueHandler == null) {
            this.eventQueueHandler = IoEventQueueHandler.NOOP;
        } else {
            this.eventQueueHandler = eventQueueHandler;
        }
    }

    private SessionTasksQueue getSessionTasksQueue(IoSession session) {
        SessionTasksQueue queue = (SessionTasksQueue) session.getAttribute(this.TASKS_QUEUE);

        if (queue == null) {
            queue = new SessionTasksQueue();
            SessionTasksQueue oldQueue = (SessionTasksQueue) session.setAttributeIfAbsent(this.TASKS_QUEUE, queue);

            if (oldQueue != null) {
                queue = oldQueue;
            }
        }

        return queue;
    }

    public IoEventQueueHandler getQueueHandler() {
        return this.eventQueueHandler;
    }

    public void setRejectedExecutionHandler(RejectedExecutionHandler handler) {
    }

    private void addWorker() {
        synchronized (this.workers) {
            if (this.workers.size() >= super.getMaximumPoolSize()) {
                return;
            }

            Worker worker = new Worker();
            Thread thread = getThreadFactory().newThread(worker);

            this.idleWorkers.incrementAndGet();

            thread.start();
            this.workers.add(worker);

            if (this.workers.size() > this.largestPoolSize) {
                this.largestPoolSize = this.workers.size();
            }
        }
    }

    private void addWorkerIfNecessary() {
        if (this.idleWorkers.get() == 0) {
            synchronized (this.workers) {
                if (this.workers.isEmpty() || this.idleWorkers.get() == 0) {
                    addWorker();
                }
            }
        }
    }

    private void removeWorker() {
        synchronized (this.workers) {
            if (this.workers.size() <= super.getCorePoolSize()) {
                return;
            }
            this.waitingSessions.offer(EXIT_SIGNAL);
        }
    }

    public int getMaximumPoolSize() {
        return super.getMaximumPoolSize();
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        if (maximumPoolSize <= 0 || maximumPoolSize < super.getCorePoolSize()) {
            throw new IllegalArgumentException("maximumPoolSize: " + maximumPoolSize);
        }

        synchronized (this.workers) {
            super.setMaximumPoolSize(maximumPoolSize);
            int difference = this.workers.size() - maximumPoolSize;
            while (difference > 0) {
                removeWorker();
                difference--;
            }
        }
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long deadline = System.currentTimeMillis() + unit.toMillis(timeout);

        synchronized (this.workers) {
            while (!isTerminated()) {
                long waitTime = deadline - System.currentTimeMillis();
                if (waitTime <= 0L) {
                    break;
                }

                this.workers.wait(waitTime);
            }
        }
        return isTerminated();
    }

    public boolean isShutdown() {
        return this.shutdown;
    }

    public boolean isTerminated() {
        if (!this.shutdown) {
            return false;
        }

        synchronized (this.workers) {
            return this.workers.isEmpty();
        }
    }

    public void shutdown() {
        if (this.shutdown) {
            return;
        }

        this.shutdown = true;

        synchronized (this.workers) {
            for (int i = this.workers.size(); i > 0; i--) {
                this.waitingSessions.offer(EXIT_SIGNAL);
            }
        }
    }

    public List<Runnable> shutdownNow() {
        shutdown();

        List<Runnable> answer = new ArrayList<Runnable>();

        IoSession session;
        while ((session = this.waitingSessions.poll()) != null) {
            if (session == EXIT_SIGNAL) {
                this.waitingSessions.offer(EXIT_SIGNAL);
                Thread.yield();

                continue;
            }
            SessionTasksQueue sessionTasksQueue = (SessionTasksQueue) session.getAttribute(this.TASKS_QUEUE);

            synchronized (sessionTasksQueue.tasksQueue) {

                for (Runnable task : sessionTasksQueue.tasksQueue) {
                    getQueueHandler().polled(this, (IoEvent) task);
                    answer.add(task);
                }

                sessionTasksQueue.tasksQueue.clear();
            }
        }

        return answer;
    }

    private void print(Queue<Runnable> queue, IoEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append("Adding event ").append(event.getType()).append(" to session ").append(event.getSession().getId());
        boolean first = true;
        sb.append("\nQueue : [");
        for (Runnable elem : queue) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }

            sb.append(((IoEvent) elem).getType()).append(", ");
        }
        sb.append("]\n");
        LOGGER.debug(sb.toString());
    }

    public void execute(Runnable task) {
        boolean offerSession;
        if (this.shutdown) {
            rejectTask(task);
        }

        checkTaskType(task);

        IoEvent event = (IoEvent) task;

        IoSession session = event.getSession();

        SessionTasksQueue sessionTasksQueue = getSessionTasksQueue(session);
        Queue<Runnable> tasksQueue = sessionTasksQueue.tasksQueue;

        boolean offerEvent = this.eventQueueHandler.accept(this, event);

        if (offerEvent) {

            synchronized (tasksQueue) {

                tasksQueue.offer(event);

                if (sessionTasksQueue.processingCompleted) {
                    sessionTasksQueue.processingCompleted = false;
                    offerSession = true;
                } else {
                    offerSession = false;
                }

                if (LOGGER.isDebugEnabled()) {
                    print(tasksQueue, event);
                }
            }
        } else {
            offerSession = false;
        }

        if (offerSession) {

            this.waitingSessions.offer(session);
        }

        addWorkerIfNecessary();

        if (offerEvent) {
            this.eventQueueHandler.offered(this, event);
        }
    }

    private void rejectTask(Runnable task) {
        getRejectedExecutionHandler().rejectedExecution(task, this);
    }

    private void checkTaskType(Runnable task) {
        if (!(task instanceof IoEvent)) {
            throw new IllegalArgumentException("task must be an IoEvent or its subclass.");
        }
    }

    public int getActiveCount() {
        synchronized (this.workers) {
            return this.workers.size() - this.idleWorkers.get();
        }
    }

    public long getCompletedTaskCount() {
        synchronized (this.workers) {
            long answer = this.completedTaskCount;
            for (Worker w : this.workers) {
                answer += w.completedTaskCount;
            }

            return answer;
        }
    }

    public int getLargestPoolSize() {
        return this.largestPoolSize;
    }

    public int getPoolSize() {
        synchronized (this.workers) {
            return this.workers.size();
        }
    }

    public long getTaskCount() {
        return getCompletedTaskCount();
    }

    public boolean isTerminating() {
        synchronized (this.workers) {
            return (isShutdown() && !isTerminated());
        }
    }

    public int prestartAllCoreThreads() {
        int answer = 0;
        synchronized (this.workers) {
            for (int i = super.getCorePoolSize() - this.workers.size(); i > 0; i--) {
                addWorker();
                answer++;
            }
        }
        return answer;
    }

    public boolean prestartCoreThread() {
        synchronized (this.workers) {
            if (this.workers.size() < super.getCorePoolSize()) {
                addWorker();
                return true;
            }
            return false;
        }
    }

    public BlockingQueue<Runnable> getQueue() {
        throw new UnsupportedOperationException();
    }

    public void purge() {
    }

    public boolean remove(Runnable task) {
        boolean removed;
        checkTaskType(task);
        IoEvent event = (IoEvent) task;
        IoSession session = event.getSession();
        SessionTasksQueue sessionTasksQueue = (SessionTasksQueue) session.getAttribute(this.TASKS_QUEUE);
        Queue<Runnable> tasksQueue = sessionTasksQueue.tasksQueue;

        if (sessionTasksQueue == null) {
            return false;
        }

        synchronized (tasksQueue) {
            removed = tasksQueue.remove(task);
        }

        if (removed) {
            getQueueHandler().polled(this, event);
        }

        return removed;
    }

    public int getCorePoolSize() {
        return super.getCorePoolSize();
    }

    public void setCorePoolSize(int corePoolSize) {
        if (corePoolSize < 0) {
            throw new IllegalArgumentException("corePoolSize: " + corePoolSize);
        }
        if (corePoolSize > super.getMaximumPoolSize()) {
            throw new IllegalArgumentException("corePoolSize exceeds maximumPoolSize");
        }

        synchronized (this.workers) {
            if (super.getCorePoolSize() > corePoolSize) {
                for (int i = super.getCorePoolSize() - corePoolSize; i > 0; i--) {
                    removeWorker();
                }
            }
            super.setCorePoolSize(corePoolSize);
        }
    }

    private class Worker implements Runnable {
        private volatile long completedTaskCount;
        private Thread thread;

        private Worker() {
        }

        public void run() {
            this.thread = Thread.currentThread();

            try {
                while (true) {
                    IoSession session = fetchSession();

                    OrderedThreadPoolExecutor.this.idleWorkers.decrementAndGet();

                    if (session == null) {
                        synchronized (OrderedThreadPoolExecutor.this.workers) {
                            if (OrderedThreadPoolExecutor.this.workers.size() > OrderedThreadPoolExecutor.this.getCorePoolSize()) {

                                OrderedThreadPoolExecutor.this.workers.remove(this);

                                break;
                            }
                        }
                    }
                    if (session == OrderedThreadPoolExecutor.EXIT_SIGNAL) {
                        break;
                    }

                    try {
                        if (session != null) {
                            runTasks(OrderedThreadPoolExecutor.this.getSessionTasksQueue(session));
                        }
                    } finally {
                        OrderedThreadPoolExecutor.this.idleWorkers.incrementAndGet();
                    }
                }
            } finally {
                synchronized (OrderedThreadPoolExecutor.this.workers) {
                    OrderedThreadPoolExecutor.this.workers.remove(this);
                    OrderedThreadPoolExecutor.this.completedTaskCount += this.completedTaskCount;
                    OrderedThreadPoolExecutor.this.workers.notifyAll();
                }
            }
        }

        private IoSession fetchSession() {
            IoSession session = null;
            long currentTime = System.currentTimeMillis();
            long deadline = currentTime + OrderedThreadPoolExecutor.this.getKeepAliveTime(TimeUnit.MILLISECONDS);
            while (true) {
                try {
                    long waitTime = deadline - currentTime;
                    if (waitTime <= 0L) {
                        break;
                    }

                    try {
                        session = OrderedThreadPoolExecutor.this.waitingSessions.poll(waitTime, TimeUnit.MILLISECONDS);
                    } finally {

                        if (session == null)
                            currentTime = System.currentTimeMillis();
                    }
                    break;
                } catch (InterruptedException e) {
                }
            }

            return session;
        }

        private void runTasks(OrderedThreadPoolExecutor.SessionTasksQueue sessionTasksQueue) {
            while (true) {
                Runnable task;
                Queue<Runnable> tasksQueue = sessionTasksQueue.tasksQueue;

                synchronized (tasksQueue) {
                    task = tasksQueue.poll();

                    if (task == null) {
                        sessionTasksQueue.processingCompleted = true;

                        break;
                    }
                }
                OrderedThreadPoolExecutor.this.eventQueueHandler.polled(OrderedThreadPoolExecutor.this, (IoEvent) task);

                runTask(task);
            }
        }

        private void runTask(Runnable task) {
            OrderedThreadPoolExecutor.this.beforeExecute(this.thread, task);
            boolean ran = false;
            try {
                task.run();
                ran = true;
                OrderedThreadPoolExecutor.this.afterExecute(task, null);
                this.completedTaskCount++;
            } catch (RuntimeException e) {
                if (!ran) {
                    OrderedThreadPoolExecutor.this.afterExecute(task, e);
                }
                throw e;
            }
        }
    }

    private class SessionTasksQueue {
        private final Queue<Runnable> tasksQueue = new ConcurrentLinkedQueue<Runnable>();
        private boolean processingCompleted = true;
        private SessionTasksQueue() {
        }
    }
}

