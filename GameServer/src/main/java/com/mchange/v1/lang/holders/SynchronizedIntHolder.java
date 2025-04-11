package com.mchange.v1.lang.holders;

public class SynchronizedIntHolder
        implements ThreadSafeIntHolder {
    int value;

    public synchronized int getValue() {
        return this.value;
    }

    public synchronized void setValue(int paramInt) {
        this.value = paramInt;
    }
}

