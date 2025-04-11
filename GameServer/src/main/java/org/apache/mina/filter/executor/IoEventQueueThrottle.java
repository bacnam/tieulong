package org.apache.mina.filter.executor;

import org.apache.mina.core.session.IoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class IoEventQueueThrottle
        implements IoEventQueueHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(IoEventQueueThrottle.class);

    private final IoEventSizeEstimator eventSizeEstimator;
    private final Object lock = new Object();
    private final AtomicInteger counter = new AtomicInteger();
    private volatile int threshold;
    private int waiters;

    public IoEventQueueThrottle() {
        this(new DefaultIoEventSizeEstimator(), 65536);
    }

    public IoEventQueueThrottle(int threshold) {
        this(new DefaultIoEventSizeEstimator(), threshold);
    }

    public IoEventQueueThrottle(IoEventSizeEstimator eventSizeEstimator, int threshold) {
        if (eventSizeEstimator == null) {
            throw new IllegalArgumentException("eventSizeEstimator");
        }

        this.eventSizeEstimator = eventSizeEstimator;

        setThreshold(threshold);
    }

    public IoEventSizeEstimator getEventSizeEstimator() {
        return this.eventSizeEstimator;
    }

    public int getThreshold() {
        return this.threshold;
    }

    public void setThreshold(int threshold) {
        if (threshold <= 0) {
            throw new IllegalArgumentException("threshold: " + threshold);
        }

        this.threshold = threshold;
    }

    public int getCounter() {
        return this.counter.get();
    }

    public boolean accept(Object source, IoEvent event) {
        return true;
    }

    public void offered(Object source, IoEvent event) {
        int eventSize = estimateSize(event);
        int currentCounter = this.counter.addAndGet(eventSize);
        logState();

        if (currentCounter >= this.threshold) {
            block();
        }
    }

    public void polled(Object source, IoEvent event) {
        int eventSize = estimateSize(event);
        int currentCounter = this.counter.addAndGet(-eventSize);

        logState();

        if (currentCounter < this.threshold) {
            unblock();
        }
    }

    private int estimateSize(IoEvent event) {
        int size = getEventSizeEstimator().estimateSize(event);

        if (size < 0) {
            throw new IllegalStateException(IoEventSizeEstimator.class.getSimpleName() + " returned " + "a negative value (" + size + "): " + event);
        }

        return size;
    }

    private void logState() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(Thread.currentThread().getName() + " state: " + this.counter.get() + " / " + getThreshold());
        }
    }

    protected void block() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(Thread.currentThread().getName() + " blocked: " + this.counter.get() + " >= " + this.threshold);
        }

        synchronized (this.lock) {
            while (this.counter.get() >= this.threshold) {
                this.waiters++;
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {

                } finally {
                    this.waiters--;
                }
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(Thread.currentThread().getName() + " unblocked: " + this.counter.get() + " < " + this.threshold);
        }
    }

    protected void unblock() {
        synchronized (this.lock) {
            if (this.waiters > 0)
                this.lock.notifyAll();
        }
    }
}

