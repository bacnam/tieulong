package com.mchange.v1.util;

import com.mchange.lang.PotentiallySecondaryRuntimeException;

public class UnexpectedException
        extends PotentiallySecondaryRuntimeException {
    public UnexpectedException(String paramString, Throwable paramThrowable) {
        super(paramString, paramThrowable);
    }

    public UnexpectedException(Throwable paramThrowable) {
        super(paramThrowable);
    }

    public UnexpectedException(String paramString) {
        super(paramString);
    }

    public UnexpectedException() {
    }

    public UnexpectedException(Throwable paramThrowable, String paramString) {
        this(paramString, paramThrowable);
    }
}

