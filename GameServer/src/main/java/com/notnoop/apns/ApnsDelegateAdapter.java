package com.notnoop.apns;

public class ApnsDelegateAdapter implements ApnsDelegate {
    public void messageSent(ApnsNotification message, boolean resent) {
    }

    public void messageSendFailed(ApnsNotification message, Throwable e) {
    }

    public void connectionClosed(DeliveryError e, int messageIdentifier) {
    }

    public void cacheLengthExceeded(int newCacheLength) {
    }

    public void notificationsResent(int resendCount) {
    }
}

