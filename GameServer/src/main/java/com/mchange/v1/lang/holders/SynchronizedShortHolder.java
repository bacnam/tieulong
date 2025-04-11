package com.mchange.v1.lang.holders;

public class SynchronizedShortHolder
        implements ThreadSafeShortHolder {
    short value;

    public synchronized short getValue() {
        return this.value;
    }

    public synchronized void setValue(short paramShort) {
        this.value = paramShort;
    }
}

