package com.notnoop.exceptions;

public abstract class ApnsException
        extends RuntimeException {
    private static final long serialVersionUID = -4756693306121825229L;

    public ApnsException() {
    }

    public ApnsException(String message) {
        super(message);
    }

    public ApnsException(Throwable cause) {
        super(cause);
    }

    public ApnsException(String m, Throwable c) {
        super(m, c);
    }
}

