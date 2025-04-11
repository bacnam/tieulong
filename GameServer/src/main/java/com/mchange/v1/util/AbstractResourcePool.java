package com.mchange.v1.util;

import java.util.*;

public abstract class AbstractResourcePool {
    private static final boolean TRACE = true;
    private static final boolean DEBUG = true;
    private static RunnableQueue sharedQueue = new SimpleRunnableQueue();

    Set managed = new HashSet();
    List unused = new LinkedList();

    int start;

    int max;
    int inc;
    int num_acq_attempts = Integer.MAX_VALUE;
    int acq_attempt_delay = 50;

    RunnableQueue rq;

    boolean initted = false;

    boolean broken = false;

    protected AbstractResourcePool(int paramInt1, int paramInt2, int paramInt3) {
        this(paramInt1, paramInt2, paramInt3, sharedQueue);
    }

    protected AbstractResourcePool(int paramInt1, int paramInt2, int paramInt3, RunnableQueue paramRunnableQueue) {
        this.start = paramInt1;
        this.max = paramInt2;
        this.inc = paramInt3;
        this.rq = paramRunnableQueue;
    }

    protected synchronized void init() throws Exception {
        for (byte b = 0; b < this.start; ) {
            assimilateResource();
            b++;
        }

        this.initted = true;
    }

    protected Object checkoutResource() throws BrokenObjectException, InterruptedException, Exception {
        return checkoutResource(0L);
    }

    protected synchronized Object checkoutResource(long paramLong) throws BrokenObjectException, InterruptedException, TimeoutException, Exception {
        if (!this.initted) init();
        ensureNotBroken();

        int i = this.unused.size();

        if (i == 0) {

            int j = this.managed.size();
            if (j < this.max)
                postAcquireMore();
            awaitAvailable(paramLong);
        }
        Object object = this.unused.get(0);
        this.unused.remove(0);

        try {
            refurbishResource(object);
        } catch (Exception exception) {

            exception.printStackTrace();
            removeResource(object);
            return checkoutResource(paramLong);
        }
        trace();
        return object;
    }

    protected synchronized void checkinResource(Object paramObject) throws BrokenObjectException {
        if (!this.managed.contains(paramObject))
            throw new IllegalArgumentException("ResourcePool: Tried to check-in a foreign resource!");
        this.unused.add(paramObject);
        notifyAll();
        trace();
    }

    protected synchronized void markBad(Object paramObject) throws Exception {
        removeResource(paramObject);
    }

    protected synchronized void close() throws Exception {
        this.broken = true;
        for (Iterator iterator = this.managed.iterator(); iterator.hasNext(); ) {

            try {
                removeResource(iterator.next());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private void postAcquireMore() throws InterruptedException {
        this.rq.postRunnable(new AcquireTask());
    }

    private void awaitAvailable(long paramLong) throws InterruptedException, TimeoutException {
        int i;
        for (; (i = this.unused.size()) == 0; wait(paramLong)) ;
        if (i == 0) {
            throw new TimeoutException();
        }
    }

    private void acquireMore() throws Exception {
        int i = this.managed.size();
        for (byte b = 0; b < Math.min(this.inc, this.max - i); b++) {
            assimilateResource();
        }
    }

    private void assimilateResource() throws Exception {
        Object object = acquireResource();
        this.managed.add(object);
        this.unused.add(object);

        notifyAll();
        trace();
    }

    private void removeResource(Object paramObject) throws Exception {
        this.managed.remove(paramObject);
        this.unused.remove(paramObject);
        destroyResource(paramObject);
        trace();
    }

    private void ensureNotBroken() throws BrokenObjectException {
        if (this.broken) throw new BrokenObjectException(this);

    }

    private synchronized void unexpectedBreak() {
        this.broken = true;
        for (Iterator iterator = this.unused.iterator(); iterator.hasNext(); ) {

            try {
                removeResource(iterator.next());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private void trace() {
        System.err.println(this + "  [managed: " + this.managed.size() + ", " + "unused: " + this.unused.size() + ']');
    }

    protected abstract Object acquireResource() throws Exception;

    protected abstract void refurbishResource(Object paramObject) throws BrokenObjectException;

    protected abstract void destroyResource(Object paramObject) throws Exception;

    class AcquireTask implements Runnable {
        boolean success = false;

        public void run() {
            for (byte b = 0; !this.success && b < AbstractResourcePool.this.num_acq_attempts; b++) {

                try {
                    if (b > 0)
                        Thread.sleep(AbstractResourcePool.this.acq_attempt_delay);
                    synchronized (AbstractResourcePool.this) {
                        AbstractResourcePool.this.acquireMore();
                    }
                    this.success = true;
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            if (!this.success) AbstractResourcePool.this.unexpectedBreak();
        }
    }

    protected class TimeoutException extends Exception {
    }
}

