package com.notnoop.apns;

public interface ApnsNotification {
    byte[] getDeviceToken();

    byte[] getPayload();

    int getIdentifier();

    int getExpiry();

    byte[] marshall();
}

