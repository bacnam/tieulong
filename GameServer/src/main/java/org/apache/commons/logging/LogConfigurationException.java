package org.apache.commons.logging;

public class LogConfigurationException
        extends RuntimeException {
    protected Throwable cause = null;

    public LogConfigurationException() {
    }

    public LogConfigurationException(String message) {
        super(message);
    }

    public LogConfigurationException(Throwable cause) {
        this((cause == null) ? null : cause.toString(), cause);
    }

    public LogConfigurationException(String message, Throwable cause) {
        super(message + " (Caused by " + cause + ")");
        this.cause = cause;
    }

    public Throwable getCause() {
        return this.cause;
    }
}

