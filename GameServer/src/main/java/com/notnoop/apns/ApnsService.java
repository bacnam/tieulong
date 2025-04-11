package com.notnoop.apns;

import com.notnoop.exceptions.NetworkIOException;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public interface ApnsService {
    ApnsNotification push(String paramString1, String paramString2) throws NetworkIOException;

    EnhancedApnsNotification push(String paramString1, String paramString2, Date paramDate) throws NetworkIOException;

    ApnsNotification push(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) throws NetworkIOException;

    EnhancedApnsNotification push(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt) throws NetworkIOException;

    Collection<? extends ApnsNotification> push(Collection<String> paramCollection, String paramString) throws NetworkIOException;

    Collection<? extends EnhancedApnsNotification> push(Collection<String> paramCollection, String paramString, Date paramDate) throws NetworkIOException;

    Collection<? extends ApnsNotification> push(Collection<byte[]> paramCollection, byte[] paramArrayOfbyte) throws NetworkIOException;

    Collection<? extends EnhancedApnsNotification> push(Collection<byte[]> paramCollection, byte[] paramArrayOfbyte, int paramInt) throws NetworkIOException;

    void push(ApnsNotification paramApnsNotification) throws NetworkIOException;

    void start();

    void stop();

    Map<String, Date> getInactiveDevices() throws NetworkIOException;

    void testConnection() throws NetworkIOException;
}

