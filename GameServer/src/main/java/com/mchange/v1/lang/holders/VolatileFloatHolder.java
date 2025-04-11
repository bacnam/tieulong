package com.mchange.v1.lang.holders;

public class VolatileFloatHolder
        implements ThreadSafeFloatHolder {
    volatile float value;

    public float getValue() {
        return this.value;
    }

    public void setValue(float paramFloat) {
        this.value = paramFloat;
    }
}

