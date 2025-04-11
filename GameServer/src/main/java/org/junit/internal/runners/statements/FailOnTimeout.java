package org.junit.internal.runners.statements;

import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestTimedOutException;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.concurrent.*;

public class FailOnTimeout
        extends Statement {
    private final Statement originalStatement;
    private final TimeUnit timeUnit;
    private final long timeout;
    private final boolean lookForStuckThread;
    private volatile ThreadGroup threadGroup = null;

    @Deprecated
    public FailOnTimeout(Statement statement, long timeoutMillis) {
        this(builder().withTimeout(timeoutMillis, TimeUnit.MILLISECONDS), statement);
    }

    private FailOnTimeout(Builder builder, Statement statement) {
        this.originalStatement = statement;
        this.timeout = builder.timeout;
        this.timeUnit = builder.unit;
        this.lookForStuckThread = builder.lookForStuckThread;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void evaluate() throws Throwable {
        CallableStatement callable = new CallableStatement();
        FutureTask<Throwable> task = new FutureTask<Throwable>(callable);
        this.threadGroup = new ThreadGroup("FailOnTimeoutGroup");
        Thread thread = new Thread(this.threadGroup, task, "Time-limited test");
        thread.setDaemon(true);
        thread.start();
        callable.awaitStarted();
        Throwable throwable = getResult(task, thread);
        if (throwable != null) {
            throw throwable;
        }
    }

    private Throwable getResult(FutureTask<Throwable> task, Thread thread) {
        try {
            if (this.timeout > 0L) {
                return task.get(this.timeout, this.timeUnit);
            }
            return task.get();
        } catch (InterruptedException e) {
            return e;
        } catch (ExecutionException e) {

            return e.getCause();
        } catch (TimeoutException e) {
            return createTimeoutException(thread);
        }
    }

    private Exception createTimeoutException(Thread thread) {
        StackTraceElement[] stackTrace = thread.getStackTrace();
        Thread stuckThread = this.lookForStuckThread ? getStuckThread(thread) : null;
        TestTimedOutException testTimedOutException = new TestTimedOutException(this.timeout, this.timeUnit);
        if (stackTrace != null) {
            testTimedOutException.setStackTrace(stackTrace);
            thread.interrupt();
        }
        if (stuckThread != null) {
            Exception stuckThreadException = new Exception("Appears to be stuck in thread " + stuckThread.getName());

            stuckThreadException.setStackTrace(getStackTrace(stuckThread));
            return (Exception) new MultipleFailureException(Arrays.asList(new Throwable[]{(Throwable) testTimedOutException, stuckThreadException}));
        }

        return (Exception) testTimedOutException;
    }

    private StackTraceElement[] getStackTrace(Thread thread) {
        try {
            return thread.getStackTrace();
        } catch (SecurityException e) {
            return new StackTraceElement[0];
        }
    }

    private Thread getStuckThread(Thread mainThread) {
        if (this.threadGroup == null) {
            return null;
        }
        Thread[] threadsInGroup = getThreadArray(this.threadGroup);
        if (threadsInGroup == null) {
            return null;
        }

        Thread stuckThread = null;
        long maxCpuTime = 0L;
        for (Thread thread : threadsInGroup) {
            if (thread.getState() == Thread.State.RUNNABLE) {
                long threadCpuTime = cpuTime(thread);
                if (stuckThread == null || threadCpuTime > maxCpuTime) {
                    stuckThread = thread;
                    maxCpuTime = threadCpuTime;
                }
            }
        }
        return (stuckThread == mainThread) ? null : stuckThread;
    }

    private Thread[] getThreadArray(ThreadGroup group) {
        int enumCount;
        Thread[] threads;
        int count = group.activeCount();
        int enumSize = Math.max(count * 2, 100);

        int loopCount = 0;
        while (true) {
            threads = new Thread[enumSize];
            enumCount = group.enumerate(threads);
            if (enumCount < enumSize) {
                break;
            }

            enumSize += 100;
            if (++loopCount >= 5) {
                return null;
            }
        }

        return copyThreads(threads, enumCount);
    }

    private Thread[] copyThreads(Thread[] threads, int count) {
        int length = Math.min(count, threads.length);
        Thread[] result = new Thread[length];
        for (int i = 0; i < length; i++) {
            result[i] = threads[i];
        }
        return result;
    }

    private long cpuTime(Thread thr) {
        ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
        if (mxBean.isThreadCpuTimeSupported()) {
            try {
                return mxBean.getThreadCpuTime(thr.getId());
            } catch (UnsupportedOperationException e) {
            }
        }

        return 0L;
    }

    public static class Builder {
        private boolean lookForStuckThread = false;

        private long timeout = 0L;
        private TimeUnit unit = TimeUnit.SECONDS;

        private Builder() {
        }

        public Builder withTimeout(long timeout, TimeUnit unit) {
            if (timeout < 0L) {
                throw new IllegalArgumentException("timeout must be non-negative");
            }
            if (unit == null) {
                throw new NullPointerException("TimeUnit cannot be null");
            }
            this.timeout = timeout;
            this.unit = unit;
            return this;
        }

        public Builder withLookingForStuckThread(boolean enable) {
            this.lookForStuckThread = enable;
            return this;
        }

        public FailOnTimeout build(Statement statement) {
            if (statement == null) {
                throw new NullPointerException("statement cannot be null");
            }
            return new FailOnTimeout(this, statement);
        }
    }

    private class CallableStatement implements Callable<Throwable> {
        private final CountDownLatch startLatch = new CountDownLatch(1);

        private CallableStatement() {
        }

        public Throwable call() throws Exception {
            try {
                this.startLatch.countDown();
                FailOnTimeout.this.originalStatement.evaluate();
            } catch (Exception e) {
                throw e;
            } catch (Throwable e) {
                return e;
            }
            return null;
        }

        public void awaitStarted() throws InterruptedException {
            this.startLatch.await();
        }
    }
}

