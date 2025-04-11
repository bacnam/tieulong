package org.apache.mina.filter.executor;

import org.apache.mina.core.session.IoEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class UnorderedThreadPoolExecutor
        extends ThreadPoolExecutor {
    private static final Runnable EXIT_SIGNAL = new Runnable() {
        public void run() {
            throw new Error("This method shouldn't be called. Please file a bug report.");
        }
    };

    private final Set<Worker> workers = new HashSet<Worker>();
    private final AtomicInteger idleWorkers = new AtomicInteger();
    private final IoEventQueueHandler queueHandler;
    private volatile int corePoolSize;
    private volatile int maximumPoolSize;
    private volatile int largestPoolSize;
    private long completedTaskCount;
    private volatile boolean shutdown;

    public UnorderedThreadPoolExecutor() {
        this(16);
    }

    public UnorderedThreadPoolExecutor(int maximumPoolSize) {
        this(0, maximumPoolSize);
    }

    public UnorderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize) {
        this(corePoolSize, maximumPoolSize, 30L, TimeUnit.SECONDS);
    }

    public UnorderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory());
    }

    public UnorderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, IoEventQueueHandler queueHandler) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory(), queueHandler);
    }

    public UnorderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, threadFactory, (IoEventQueueHandler) null);
    }

    public UnorderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory, IoEventQueueHandler queueHandler) {
        super(0, 1, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>(), threadFactory, new ThreadPoolExecutor.AbortPolicy());
        if (corePoolSize < 0) {
            throw new IllegalArgumentException("corePoolSize: " + corePoolSize);
        }

        if (maximumPoolSize == 0 || maximumPoolSize < corePoolSize) {
            throw new IllegalArgumentException("maximumPoolSize: " + maximumPoolSize);
        }

        if (queueHandler == null) {
            queueHandler = IoEventQueueHandler.NOOP;
        }

        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.queueHandler = queueHandler;
    }

    public IoEventQueueHandler getQueueHandler() {
        return this.queueHandler;
    }

    public void setRejectedExecutionHandler(RejectedExecutionHandler handler) {
    }

    private void addWorker() {
        synchronized (this.workers) {
            if (this.workers.size() >= this.maximumPoolSize) {
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
            if (this.workers.size() <= this.corePoolSize) {
                return;
            }
            getQueue().offer(EXIT_SIGNAL);
        }
    }

    public int getMaximumPoolSize() {
        return this.maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        if (maximumPoolSize <= 0 || maximumPoolSize < this.corePoolSize) {
            throw new IllegalArgumentException("maximumPoolSize: " + maximumPoolSize);
        }

        synchronized (this.workers) {
            this.maximumPoolSize = maximumPoolSize;
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
                getQueue().offer(EXIT_SIGNAL);
            }
        }
    }

    public List<Runnable> shutdownNow() {
        shutdown();

        List<Runnable> answer = new ArrayList<Runnable>();
        Runnable task;
        while ((task = getQueue().poll()) != null) {
            if (task == EXIT_SIGNAL) {
                getQueue().offer(EXIT_SIGNAL);
                Thread.yield();

                continue;
            }
            getQueueHandler().polled(this, (IoEvent) task);
            answer.add(task);
        }

        return answer;
    }

    public void execute(Runnable task) {
        if (this.shutdown) {
            rejectTask(task);
        }

        checkTaskType(task);

        IoEvent e = (IoEvent) task;
        boolean offeredEvent = this.queueHandler.accept(this, e);

        if (offeredEvent) {
            getQueue().offer(e);
        }

        addWorkerIfNecessary();

        if (offeredEvent) {
            this.queueHandler.offered(this, e);
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
            for (int i = this.corePoolSize - this.workers.size(); i > 0; i--) {
                addWorker();
                answer++;
            }
        }
        return answer;
    }

    public boolean prestartCoreThread() {
        synchronized (this.workers) {
            if (this.workers.size() < this.corePoolSize) {
                addWorker();
                return true;
            }

            return false;
        }
    }

    public void purge() {
    }

    public boolean remove(Runnable task) {
        boolean removed = super.remove(task);
        if (removed) {
            getQueueHandler().polled(this, (IoEvent) task);
        }
        return removed;
    }

    public int getCorePoolSize() {
        return this.corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        if (corePoolSize < 0) {
            throw new IllegalArgumentException("corePoolSize: " + corePoolSize);
        }
        if (corePoolSize > this.maximumPoolSize) {
            throw new IllegalArgumentException("corePoolSize exceeds maximumPoolSize");
        }

        synchronized (this.workers) {
            if (this.corePoolSize > corePoolSize) {
                for (int i = this.corePoolSize - corePoolSize; i > 0; i--) {
                    removeWorker();
                }
            }
            this.corePoolSize = corePoolSize;
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
                    Runnable task = fetchTask();

                    UnorderedThreadPoolExecutor.this.idleWorkers.decrementAndGet();

                    if (task == null) {
                        synchronized (UnorderedThreadPoolExecutor.this.workers) {
                            if (UnorderedThreadPoolExecutor.this.workers.size() > UnorderedThreadPoolExecutor.this.corePoolSize) {

                                UnorderedThreadPoolExecutor.this.workers.remove(this);

                                break;
                            }
                        }
                    }
                    if (task == UnorderedThreadPoolExecutor.EXIT_SIGNAL) {
                        break;
                    }

                    try {
                        if (task != null) {
                            UnorderedThreadPoolExecutor.this.queueHandler.polled(UnorderedThreadPoolExecutor.this, (IoEvent) task);
                            runTask(task);
                        }
                    } finally {
                        UnorderedThreadPoolExecutor.this.idleWorkers.incrementAndGet();
                    }
                }
            } finally {
                synchronized (UnorderedThreadPoolExecutor.this.workers) {
                    UnorderedThreadPoolExecutor.this.workers.remove(this);
                    UnorderedThreadPoolExecutor.this.completedTaskCount += this.completedTaskCount;
                    UnorderedThreadPoolExecutor.this.workers.notifyAll();
                }
            }
        }

        private Runnable fetchTask() {
            Runnable task = null;
            long currentTime = System.currentTimeMillis();
            long deadline = currentTime + UnorderedThreadPoolExecutor.this.getKeepAliveTime(TimeUnit.MILLISECONDS);
            while (true) {
                try {
                    long waitTime = deadline - currentTime;
                    if (waitTime <= 0L) {
                        break;
                    }

                    try {
                        task = UnorderedThreadPoolExecutor.this.getQueue().poll(waitTime, TimeUnit.MILLISECONDS);
                    } finally {

                        if (task == null)
                            currentTime = System.currentTimeMillis();
                    }
                    break;
                } catch (InterruptedException e) {
                }
            }

            return task;
        }

        private void runTask(Runnable task) {
            UnorderedThreadPoolExecutor.this.beforeExecute(this.thread, task);
            boolean ran = false;
            try {
                task.run();
                ran = true;
                UnorderedThreadPoolExecutor.this.afterExecute(task, null);
                this.completedTaskCount++;
            } catch (RuntimeException e) {
                if (!ran) {
                    UnorderedThreadPoolExecutor.this.afterExecute(task, e);
                }
                throw e;
            }
        }
    }
}

