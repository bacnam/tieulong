package com.mchange.v1.lang.holders;

public class VolatileBooleanHolder
        implements ThreadSafeBooleanHolder {
    volatile boolean value;

    public boolean getValue() {
        return this.value;
    }

    public void setValue(boolean paramBoolean) {
        this.value = paramBoolean;
    }
}

