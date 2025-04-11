package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;

@Beta
public class ExecutionError
        extends Error {
    private static final long serialVersionUID = 0L;

    protected ExecutionError() {
    }

    protected ExecutionError(String message) {
        super(message);
    }

    public ExecutionError(String message, Error cause) {
        super(message, cause);
    }

    public ExecutionError(Error cause) {
        super(cause);
    }
}

