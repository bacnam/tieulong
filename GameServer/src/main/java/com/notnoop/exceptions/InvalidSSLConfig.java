package com.notnoop.exceptions;

public class InvalidSSLConfig
        extends ApnsException {
    private static final long serialVersionUID = -7283168775864517167L;

    public InvalidSSLConfig() {
    }

    public InvalidSSLConfig(String message) {
        super(message);
    }

    public InvalidSSLConfig(Throwable cause) {
        super(cause);
    }

    public InvalidSSLConfig(String m, Throwable c) {
        super(m, c);
    }
}

