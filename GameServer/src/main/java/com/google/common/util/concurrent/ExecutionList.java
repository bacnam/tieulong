package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ExecutionList {
    private static final Logger log = Logger.getLogger(ExecutionList.class.getName());

    private final Queue<RunnableExecutorPair> runnables = Lists.newLinkedList();

    private boolean executed = false;

    public void add(Runnable runnable, Executor executor) {
        Preconditions.checkNotNull(runnable, "Runnable was null.");
        Preconditions.checkNotNull(executor, "Executor was null.");

        boolean executeImmediate = false;

        synchronized (this.runnables) {
            if (!this.executed) {
                this.runnables.add(new RunnableExecutorPair(runnable, executor));
            } else {
                executeImmediate = true;
            }
        }

        if (executeImmediate) {
            (new RunnableExecutorPair(runnable, executor)).execute();
        }
    }

    @Deprecated
    @Beta
    public void run() {
        execute();
    }

    public void execute() {
        synchronized (this.runnables) {
            if (this.executed) {
                return;
            }
            this.executed = true;
        }

        while (!this.runnables.isEmpty())
            ((RunnableExecutorPair) this.runnables.poll()).execute();
    }

    private static class RunnableExecutorPair {
        final Runnable runnable;
        final Executor executor;

        RunnableExecutorPair(Runnable runnable, Executor executor) {
            this.runnable = runnable;
            this.executor = executor;
        }

        void execute() {
            try {
                this.executor.execute(this.runnable);
            } catch (RuntimeException e) {

                ExecutionList.log.log(Level.SEVERE, "RuntimeException while executing runnable " + this.runnable + " with executor " + this.executor, e);
            }
        }
    }
}

