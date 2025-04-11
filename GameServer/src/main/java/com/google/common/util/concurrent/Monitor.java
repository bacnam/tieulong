package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Beta
public final class Monitor {
    private final ReentrantLock lock;
    private final ArrayList<Guard> activeGuards = Lists.newArrayListWithCapacity(1);

    public Monitor() {
        this(false);
    }

    public Monitor(boolean fair) {
        this.lock = new ReentrantLock(fair);
    }

    public void enter() {
        this.lock.lock();
    }

    public void enterInterruptibly() throws InterruptedException {
        this.lock.lockInterruptibly();
    }

    public boolean enter(long time, TimeUnit unit) {
        ReentrantLock lock = this.lock;
        long startNanos = System.nanoTime();
        long timeoutNanos = unit.toNanos(time);
        long remainingNanos = timeoutNanos;
        boolean interruptIgnored = false;

        while (true) {
            try {
                return lock.tryLock(remainingNanos, TimeUnit.NANOSECONDS);
            } catch (InterruptedException ignored) {
                interruptIgnored = true;

            } finally {

                if (interruptIgnored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public boolean enterInterruptibly(long time, TimeUnit unit) throws InterruptedException {
        return this.lock.tryLock(time, unit);
    }

    public boolean tryEnter() {
        return this.lock.tryLock();
    }

    public void enterWhen(Guard guard) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        boolean reentrant = lock.isHeldByCurrentThread();
        lock.lockInterruptibly();
        try {
            waitInterruptibly(guard, reentrant);
        } catch (Throwable throwable) {
            lock.unlock();
            throw Throwables.propagate(throwable);
        }
    }

    public void enterWhenUninterruptibly(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        boolean reentrant = lock.isHeldByCurrentThread();
        lock.lock();
        try {
            waitUninterruptibly(guard, reentrant);
        } catch (Throwable throwable) {
            lock.unlock();
            throw Throwables.propagate(throwable);
        }
    }

    public boolean enterWhen(Guard guard, long time, TimeUnit unit) throws InterruptedException {
        boolean satisfied;
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        boolean reentrant = lock.isHeldByCurrentThread();
        long startNanos = System.nanoTime();
        if (!lock.tryLock(time, unit)) {
            return false;
        }

        try {
            long remainingNanos = unit.toNanos(time) - System.nanoTime() - startNanos;
            satisfied = waitInterruptibly(guard, remainingNanos, reentrant);
        } catch (Throwable throwable) {
            lock.unlock();
            throw Throwables.propagate(throwable);
        }
        if (satisfied) {
            return true;
        }
        lock.unlock();
        return false;
    }

    public boolean enterWhenUninterruptibly(Guard guard, long time, TimeUnit unit) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        boolean reentrant = lock.isHeldByCurrentThread();
        long startNanos = System.nanoTime();
        long timeoutNanos = unit.toNanos(time);
        long remainingNanos = timeoutNanos;
        boolean interruptIgnored = false;
        try {
            boolean satisfied;
            while (true) {
                try {
                    if (lock.tryLock(remainingNanos, TimeUnit.NANOSECONDS)) {

                        remainingNanos = timeoutNanos - System.nanoTime() - startNanos;

                        break;
                    }

                    satisfied = false;

                    return satisfied;
                } catch (InterruptedException ignored) {
                    interruptIgnored = true;
                } finally {
                    remainingNanos = timeoutNanos - System.nanoTime() - startNanos;
                }
            }
            try {
                satisfied = waitUninterruptibly(guard, remainingNanos, reentrant);
            } catch (Throwable throwable) {
                lock.unlock();
                throw Throwables.propagate(throwable);
            }
            if (satisfied) return true;
            lock.unlock();
            return false;
        } finally {
            if (interruptIgnored) Thread.currentThread().interrupt();
        }

    }

    public boolean enterIf(Guard guard) {
        boolean satisfied;
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        lock.lock();

        try {
            satisfied = guard.isSatisfied();
        } catch (Throwable throwable) {
            lock.unlock();
            throw Throwables.propagate(throwable);
        }
        if (satisfied) {
            return true;
        }
        lock.unlock();
        return false;
    }

    public boolean enterIfInterruptibly(Guard guard) throws InterruptedException {
        boolean satisfied;
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        lock.lockInterruptibly();

        try {
            satisfied = guard.isSatisfied();
        } catch (Throwable throwable) {
            lock.unlock();
            throw Throwables.propagate(throwable);
        }
        if (satisfied) {
            return true;
        }
        lock.unlock();
        return false;
    }

    public boolean enterIf(Guard guard, long time, TimeUnit unit) {
        boolean satisfied;
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        if (!enter(time, unit)) {
            return false;
        }

        try {
            satisfied = guard.isSatisfied();
        } catch (Throwable throwable) {
            lock.unlock();
            throw Throwables.propagate(throwable);
        }
        if (satisfied) {
            return true;
        }
        lock.unlock();
        return false;
    }

    public boolean enterIfInterruptibly(Guard guard, long time, TimeUnit unit) throws InterruptedException {
        boolean satisfied;
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        if (!lock.tryLock(time, unit)) {
            return false;
        }

        try {
            satisfied = guard.isSatisfied();
        } catch (Throwable throwable) {
            lock.unlock();
            throw Throwables.propagate(throwable);
        }
        if (satisfied) {
            return true;
        }
        lock.unlock();
        return false;
    }

    public boolean tryEnterIf(Guard guard) {
        boolean satisfied;
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        if (!lock.tryLock()) {
            return false;
        }

        try {
            satisfied = guard.isSatisfied();
        } catch (Throwable throwable) {
            lock.unlock();
            throw Throwables.propagate(throwable);
        }
        if (satisfied) {
            return true;
        }
        lock.unlock();
        return false;
    }

    @GuardedBy("lock")
    public void waitFor(Guard guard) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        if (!this.lock.isHeldByCurrentThread()) {
            throw new IllegalMonitorStateException();
        }
        waitInterruptibly(guard, true);
    }

    @GuardedBy("lock")
    public void waitForUninterruptibly(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        if (!this.lock.isHeldByCurrentThread()) {
            throw new IllegalMonitorStateException();
        }
        waitUninterruptibly(guard, true);
    }

    @GuardedBy("lock")
    public boolean waitFor(Guard guard, long time, TimeUnit unit) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        if (!this.lock.isHeldByCurrentThread()) {
            throw new IllegalMonitorStateException();
        }
        return waitInterruptibly(guard, unit.toNanos(time), true);
    }

    @GuardedBy("lock")
    public boolean waitForUninterruptibly(Guard guard, long time, TimeUnit unit) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        if (!this.lock.isHeldByCurrentThread()) {
            throw new IllegalMonitorStateException();
        }
        return waitUninterruptibly(guard, unit.toNanos(time), true);
    }

    @GuardedBy("lock")
    public void leave() {
        ReentrantLock lock = this.lock;
        if (!lock.isHeldByCurrentThread()) {
            throw new IllegalMonitorStateException();
        }
        try {
            signalConditionsOfSatisfiedGuards(null);
        } finally {
            lock.unlock();
        }
    }

    public void reevaluateGuards() {
        ReentrantLock lock = this.lock;
        lock.lock();
        try {
            signalConditionsOfSatisfiedGuards(null);
        } finally {
            lock.unlock();
        }
    }

    public boolean isFair() {
        return this.lock.isFair();
    }

    public boolean isOccupied() {
        return this.lock.isLocked();
    }

    public boolean isOccupiedByCurrentThread() {
        return this.lock.isHeldByCurrentThread();
    }

    public int getOccupiedDepth() {
        return this.lock.getHoldCount();
    }

    public int getQueueLength() {
        return this.lock.getQueueLength();
    }

    public boolean hasQueuedThreads() {
        return this.lock.hasQueuedThreads();
    }

    public boolean hasQueuedThread(Thread thread) {
        return this.lock.hasQueuedThread(thread);
    }

    public boolean hasWaiters(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        this.lock.lock();
        try {
            return (guard.waiterCount > 0);
        } finally {
            this.lock.unlock();
        }
    }

    public int getWaitQueueLength(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        this.lock.lock();
        try {
            return guard.waiterCount;
        } finally {
            this.lock.unlock();
        }
    }

    @GuardedBy("lock")
    private void signalConditionsOfSatisfiedGuards(@Nullable Guard interruptedGuard) {
        ArrayList<Guard> guards = this.activeGuards;
        int guardCount = guards.size();
        try {
            for (int i = 0; i < guardCount; i++) {
                Guard guard = guards.get(i);
                if (guard != interruptedGuard || guard.waiterCount != 1) {

                    if (guard.isSatisfied()) {
                        guard.condition.signal();
                        return;
                    }
                }
            }
        } catch (Throwable throwable) {
            for (int i = 0; i < guardCount; i++) {
                Guard guard = guards.get(i);
                guard.condition.signalAll();
            }
            throw Throwables.propagate(throwable);
        }
    }

    @GuardedBy("lock")
    private void incrementWaiters(Guard guard) {
        int waiters = guard.waiterCount++;
        if (waiters == 0) {
            this.activeGuards.add(guard);
        }
    }

    @GuardedBy("lock")
    private void decrementWaiters(Guard guard) {
        int waiters = --guard.waiterCount;
        if (waiters == 0) {
            this.activeGuards.remove(guard);
        }
    }

    @GuardedBy("lock")
    private void waitInterruptibly(Guard guard, boolean signalBeforeWaiting) throws InterruptedException {
        if (!guard.isSatisfied()) {
            if (signalBeforeWaiting) {
                signalConditionsOfSatisfiedGuards(null);
            }
            incrementWaiters(guard);
            try {
                Condition condition = guard.condition;
                do {
                    try {
                        condition.await();
                    } catch (InterruptedException interrupt) {
                        try {
                            signalConditionsOfSatisfiedGuards(guard);
                        } catch (Throwable throwable) {
                            Thread.currentThread().interrupt();
                            throw Throwables.propagate(throwable);
                        }
                        throw interrupt;
                    }
                } while (!guard.isSatisfied());
            } finally {
                decrementWaiters(guard);
            }
        }
    }

    @GuardedBy("lock")
    private void waitUninterruptibly(Guard guard, boolean signalBeforeWaiting) {
        if (!guard.isSatisfied()) {
            if (signalBeforeWaiting) {
                signalConditionsOfSatisfiedGuards(null);
            }
            incrementWaiters(guard);
            try {
                Condition condition = guard.condition;
                do {
                    condition.awaitUninterruptibly();
                } while (!guard.isSatisfied());
            } finally {
                decrementWaiters(guard);
            }
        }
    }

    @GuardedBy("lock")
    private boolean waitInterruptibly(Guard guard, long remainingNanos, boolean signalBeforeWaiting) throws InterruptedException {
        if (!guard.isSatisfied()) {
            if (signalBeforeWaiting) {
                signalConditionsOfSatisfiedGuards(null);
            }
            incrementWaiters(guard);
            try {
                Condition condition = guard.condition;
                do {
                    if (remainingNanos <= 0L) {
                        return false;
                    }
                    try {
                        remainingNanos = condition.awaitNanos(remainingNanos);
                    } catch (InterruptedException interrupt) {
                        try {
                            signalConditionsOfSatisfiedGuards(guard);
                        } catch (Throwable throwable) {
                            Thread.currentThread().interrupt();
                            throw Throwables.propagate(throwable);
                        }
                        throw interrupt;
                    }
                } while (!guard.isSatisfied());
            } finally {
                decrementWaiters(guard);
            }
        }
        return true;
    }

    @GuardedBy("lock")
    private boolean waitUninterruptibly(Guard guard, long timeoutNanos, boolean signalBeforeWaiting) {
        if (!guard.isSatisfied()) {
            long startNanos = System.nanoTime();
            if (signalBeforeWaiting) {
                signalConditionsOfSatisfiedGuards(null);
            }
            boolean interruptIgnored = false;
            try {
                incrementWaiters(guard);
                try {
                    Condition condition = guard.condition;
                    long remainingNanos = timeoutNanos;
                    do {
                        if (remainingNanos <= 0L) {
                            return false;
                        }
                        try {
                            remainingNanos = condition.awaitNanos(remainingNanos);
                        } catch (InterruptedException ignored) {
                            try {
                                signalConditionsOfSatisfiedGuards(guard);
                            } catch (Throwable throwable) {
                                Thread.currentThread().interrupt();
                                throw Throwables.propagate(throwable);
                            }
                            interruptIgnored = true;
                            remainingNanos = timeoutNanos - System.nanoTime() - startNanos;
                        }
                    } while (!guard.isSatisfied());
                } finally {
                    decrementWaiters(guard);
                }
            } finally {
                if (interruptIgnored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return true;
    }

    @Beta
    public static abstract class Guard {
        final Monitor monitor;
        final Condition condition;
        @GuardedBy("monitor.lock")
        int waiterCount = 0;

        protected Guard(Monitor monitor) {
            this.monitor = (Monitor) Preconditions.checkNotNull(monitor, "monitor");
            this.condition = monitor.lock.newCondition();
        }

        public abstract boolean isSatisfied();

        public final boolean equals(Object other) {
            return (this == other);
        }

        public final int hashCode() {
            return super.hashCode();
        }
    }
}

