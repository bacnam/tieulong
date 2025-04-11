package com.notnoop.apns;

public interface ApnsDelegate {
    public static final ApnsDelegate EMPTY = new ApnsDelegateAdapter();

    void messageSent(ApnsNotification paramApnsNotification, boolean paramBoolean);

    void messageSendFailed(ApnsNotification paramApnsNotification, Throwable paramThrowable);

    void connectionClosed(DeliveryError paramDeliveryError, int paramInt);

    void cacheLengthExceeded(int paramInt);

    void notificationsResent(int paramInt);
}

