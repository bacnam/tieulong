package com.notnoop.apns.internal;

import com.notnoop.apns.ApnsNotification;
import com.notnoop.exceptions.NetworkIOException;

import java.io.Closeable;

public interface ApnsConnection extends Closeable {
    public static final int DEFAULT_CACHE_LENGTH = 100;

    void sendMessage(ApnsNotification paramApnsNotification) throws NetworkIOException;

    void testConnection() throws NetworkIOException;

    ApnsConnection copy();

    int getCacheLength();

    void setCacheLength(int paramInt);
}

