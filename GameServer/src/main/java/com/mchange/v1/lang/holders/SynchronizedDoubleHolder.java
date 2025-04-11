package com.mchange.v1.lang.holders;

public class SynchronizedDoubleHolder
        implements ThreadSafeDoubleHolder {
    double value;

    public synchronized double getValue() {
        return this.value;
    }

    public synchronized void setValue(double paramDouble) {
        this.value = paramDouble;
    }
}

