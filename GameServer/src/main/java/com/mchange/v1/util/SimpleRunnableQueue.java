package com.mchange.v1.util;

import java.util.LinkedList;
import java.util.List;

public class SimpleRunnableQueue
        implements RunnableQueue {
    private List taskList = new LinkedList();
    private Thread t = new TaskThread();

    public SimpleRunnableQueue(boolean paramBoolean) {
        this.t.setDaemon(paramBoolean);
        this.t.start();
    }

    public SimpleRunnableQueue() {
        this(true);
    }

    public synchronized void postRunnable(Runnable paramRunnable) {
        this.taskList.add(paramRunnable);
        notifyAll();
    }

    public synchronized void close() {
        this.t.interrupt();
        this.taskList = null;
        this.t = null;
    }

    private synchronized Runnable dequeueRunnable() {
        Runnable runnable = this.taskList.get(0);
        this.taskList.remove(0);
        return runnable;
    }

    private synchronized void awaitTask() throws InterruptedException {
        for (; this.taskList.size() == 0; wait()) ;
    }

    class TaskThread extends Thread {
        TaskThread() {
            super("SimpleRunnableQueue.TaskThread");
        }

        public void run() {
            try {
                while (true) {
                    SimpleRunnableQueue.this.awaitTask();
                    Runnable runnable = SimpleRunnableQueue.this.dequeueRunnable();
                    try {
                        runnable.run();
                    } catch (Exception exception) {

                        System.err.println(getClass().getName() + " -- Unexpected exception in task!");

                        exception.printStackTrace();
                    }

                }
            } catch (InterruptedException interruptedException) {

                System.err.println(toString() + " interrupted. Shutting down.");
                return;
            }
        }
    }
}

