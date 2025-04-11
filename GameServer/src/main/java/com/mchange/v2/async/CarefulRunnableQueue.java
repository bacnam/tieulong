package com.mchange.v2.async;

import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import com.mchange.v2.util.ResourceClosedException;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CarefulRunnableQueue
        implements RunnableQueue, Queuable, StrandedTaskReporting {
    private static final MLogger logger = MLog.getLogger(CarefulRunnableQueue.class);

    private List taskList = new LinkedList();
    private TaskThread t = new TaskThread();

    private boolean shutdown_on_interrupt;

    private boolean gentle_close_requested = false;

    private List strandedTasks = null;

    public CarefulRunnableQueue(boolean paramBoolean1, boolean paramBoolean2) {
        this.shutdown_on_interrupt = paramBoolean2;
        this.t.setDaemon(paramBoolean1);
        this.t.start();
    }

    public RunnableQueue asRunnableQueue() {
        return this;
    }

    public synchronized void postRunnable(Runnable paramRunnable) {
        try {
            if (this.gentle_close_requested) {
                throw new ResourceClosedException("Attempted to post a task to a closing CarefulRunnableQueue.");
            }

            this.taskList.add(paramRunnable);
            notifyAll();
        } catch (NullPointerException nullPointerException) {

            if (logger.isLoggable(MLevel.FINE)) {
                logger.log(MLevel.FINE, "NullPointerException while posting Runnable.", nullPointerException);
            }
            if (this.taskList == null) {
                throw new ResourceClosedException("Attempted to post a task to a CarefulRunnableQueue which has been closed, or whose TaskThread has been interrupted.");
            }

            throw nullPointerException;
        }
    }

    public synchronized void close(boolean paramBoolean) {
        if (paramBoolean) {

            this.t.safeStop();
            this.t.interrupt();
        } else {

            this.gentle_close_requested = true;
        }
    }

    public synchronized void close() {
        close(true);
    }

    public synchronized List getStrandedTasks() {
        try {
            while (this.gentle_close_requested && this.taskList != null)
                wait();
            return this.strandedTasks;
        } catch (InterruptedException interruptedException) {

            if (logger.isLoggable(MLevel.WARNING)) {
                logger.log(MLevel.WARNING, Thread.currentThread() + " interrupted while waiting for stranded tasks from CarefulRunnableQueue.", interruptedException);
            }

            throw new RuntimeException(Thread.currentThread() + " interrupted while waiting for stranded tasks from CarefulRunnableQueue.");
        }
    }

    private synchronized Runnable dequeueRunnable() {
        Runnable runnable = this.taskList.get(0);
        this.taskList.remove(0);
        return runnable;
    }

    private synchronized void awaitTask() throws InterruptedException {
        while (this.taskList.size() == 0) {

            if (this.gentle_close_requested) {

                this.t.safeStop();
                this.t.interrupt();
            }
            wait();
        }
    }

    class TaskThread
            extends Thread {
        boolean should_stop = false;

        TaskThread() {
            super("CarefulRunnableQueue.TaskThread");
        }

        public synchronized void safeStop() {
            this.should_stop = true;
        }

        private synchronized boolean shouldStop() {
            return this.should_stop;
        }

        public void run() {
            try {
                while (!shouldStop()) {

                    try {
                        CarefulRunnableQueue.this.awaitTask();
                        Runnable runnable = CarefulRunnableQueue.this.dequeueRunnable();
                        try {
                            runnable.run();
                        } catch (Exception exception) {

                            if (CarefulRunnableQueue.logger.isLoggable(MLevel.WARNING)) {
                                CarefulRunnableQueue.logger.log(MLevel.WARNING, getClass().getName() + " -- Unexpected exception in task!", exception);
                            }
                        }
                    } catch (InterruptedException interruptedException) {

                        if (CarefulRunnableQueue.this.shutdown_on_interrupt) {

                            CarefulRunnableQueue.this.close(false);

                            if (CarefulRunnableQueue.logger.isLoggable(MLevel.INFO)) {
                                CarefulRunnableQueue.logger.info(toString() + " interrupted. Shutting down after current tasks" + " have completed.");
                            }

                            continue;
                        }

                        CarefulRunnableQueue.logger.info(toString() + " received interrupt. IGNORING.");

                    }

                }

            } finally {

                synchronized (CarefulRunnableQueue.this) {

                    CarefulRunnableQueue.this.strandedTasks = Collections.unmodifiableList(CarefulRunnableQueue.this.taskList);
                    CarefulRunnableQueue.this.taskList = null;
                    CarefulRunnableQueue.this.t = null;
                    CarefulRunnableQueue.this.notifyAll();
                }
            }
        }
    }
}

