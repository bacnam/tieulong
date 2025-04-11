package com.mchange.v1.lang.holders;

public class SynchronizedByteHolder
        implements ThreadSafeByteHolder {
    byte value;

    public synchronized byte getValue() {
        return this.value;
    }

    public synchronized void setValue(byte paramByte) {
        this.value = paramByte;
    }
}

