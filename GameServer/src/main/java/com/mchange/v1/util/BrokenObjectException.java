package com.mchange.v1.util;

public class BrokenObjectException
        extends Exception {
    Object broken;

    public BrokenObjectException(Object paramObject, String paramString) {
        super(paramString);
        this.broken = paramObject;
    }

    public BrokenObjectException(Object paramObject) {
        this.broken = paramObject;
    }

    public Object getBrokenObject() {
        return this.broken;
    }
}

