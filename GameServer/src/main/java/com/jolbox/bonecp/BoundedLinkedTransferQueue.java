package com.jolbox.bonecp;

import jsr166y.LinkedTransferQueue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedLinkedTransferQueue<E>
        extends LinkedTransferQueue<E> {
    private static final long serialVersionUID = -1875525368357897907L;
    private final int maxQueueSize;
    private final ReentrantLock lock = new ReentrantLock();
    private AtomicInteger size = new AtomicInteger();

    public BoundedLinkedTransferQueue(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
    }

    public int size() {
        return this.size.get();
    }

    public int remainingCapacity() {
        return this.maxQueueSize - this.size.get();
    }

    public E poll() {
        E result = (E) super.poll();

        if (result != null) {
            this.size.decrementAndGet();
        }

        return result;
    }

    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        E result = (E) super.poll(timeout, unit);

        if (result != null) {
            this.size.decrementAndGet();
        }

        return result;
    }

    public boolean tryTransfer(E e) {
        boolean result = super.tryTransfer(e);
        if (result) {
            this.size.incrementAndGet();
        }
        return result;
    }

    public boolean offer(E e) {
        boolean result = false;
        this.lock.lock();
        try {
            if (this.size.get() < this.maxQueueSize) {
                super.put(e);
                this.size.incrementAndGet();
                result = true;
            }
        } finally {
            this.lock.unlock();
        }
        return result;
    }

    public void put(E e) {
        throw new UnsupportedOperationException();
    }
}

