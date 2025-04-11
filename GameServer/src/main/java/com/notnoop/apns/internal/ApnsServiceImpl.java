package com.notnoop.apns.internal;

import com.notnoop.apns.ApnsNotification;
import com.notnoop.exceptions.NetworkIOException;

public class ApnsServiceImpl
        extends AbstractApnsService {
    private ApnsConnection connection;

    public ApnsServiceImpl(ApnsConnection connection, ApnsFeedbackConnection feedback) {
        super(feedback);
        this.connection = connection;
    }

    public void push(ApnsNotification msg) throws NetworkIOException {
        this.connection.sendMessage(msg);
    }

    public void start() {
    }

    public void stop() {
        Utilities.close(this.connection);
    }

    public void testConnection() {
        this.connection.testConnection();
    }
}

