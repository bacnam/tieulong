package com.mchange.v1.lang.holders;

public class VolatileByteHolder
        implements ThreadSafeByteHolder {
    volatile byte value;

    public byte getValue() {
        return this.value;
    }

    public void setValue(byte paramByte) {
        this.value = paramByte;
    }
}

