package org.apache.http.nio.reactor;

import java.io.IOException;

public class IOReactorException
        extends IOException {
    private static final long serialVersionUID = -4248110651729635749L;

    public IOReactorException(String message, Exception cause) {
        super(message);
        if (cause != null) {
            initCause(cause);
        }
    }

    public IOReactorException(String message) {
        super(message);
    }
}

