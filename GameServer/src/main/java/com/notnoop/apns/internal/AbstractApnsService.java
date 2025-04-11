package com.notnoop.apns.internal;

import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.EnhancedApnsNotification;
import com.notnoop.exceptions.NetworkIOException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

abstract class AbstractApnsService
        implements ApnsService {
    private ApnsFeedbackConnection feedback;
    private AtomicInteger c = new AtomicInteger();

    public AbstractApnsService(ApnsFeedbackConnection feedback) {
        this.feedback = feedback;
    }

    public EnhancedApnsNotification push(String deviceToken, String payload) throws NetworkIOException {
        EnhancedApnsNotification notification = new EnhancedApnsNotification(this.c.incrementAndGet(), 2147483647, deviceToken, payload);

        push((ApnsNotification) notification);
        return notification;
    }

    public EnhancedApnsNotification push(String deviceToken, String payload, Date expiry) throws NetworkIOException {
        EnhancedApnsNotification notification = new EnhancedApnsNotification(this.c.incrementAndGet(), (int) (expiry.getTime() / 1000L), deviceToken, payload);

        push((ApnsNotification) notification);
        return notification;
    }

    public EnhancedApnsNotification push(byte[] deviceToken, byte[] payload) throws NetworkIOException {
        EnhancedApnsNotification notification = new EnhancedApnsNotification(this.c.incrementAndGet(), 2147483647, deviceToken, payload);

        push((ApnsNotification) notification);
        return notification;
    }

    public EnhancedApnsNotification push(byte[] deviceToken, byte[] payload, int expiry) throws NetworkIOException {
        EnhancedApnsNotification notification = new EnhancedApnsNotification(this.c.incrementAndGet(), expiry, deviceToken, payload);

        push((ApnsNotification) notification);
        return notification;
    }

    public Collection<EnhancedApnsNotification> push(Collection<String> deviceTokens, String payload) throws NetworkIOException {
        byte[] messageBytes = Utilities.toUTF8Bytes(payload);
        List<EnhancedApnsNotification> notifications = new ArrayList<EnhancedApnsNotification>(deviceTokens.size());
        for (String deviceToken : deviceTokens) {
            byte[] dtbytes = Utilities.decodeHex(deviceToken);
            EnhancedApnsNotification notification = new EnhancedApnsNotification(this.c.incrementAndGet(), 2147483647, dtbytes, messageBytes);

            notifications.add(notification);
            push((ApnsNotification) notification);
        }
        return notifications;
    }

    public Collection<EnhancedApnsNotification> push(Collection<String> deviceTokens, String payload, Date expiry) throws NetworkIOException {
        byte[] messageBytes = Utilities.toUTF8Bytes(payload);
        List<EnhancedApnsNotification> notifications = new ArrayList<EnhancedApnsNotification>(deviceTokens.size());
        for (String deviceToken : deviceTokens) {
            byte[] dtbytes = Utilities.decodeHex(deviceToken);
            EnhancedApnsNotification notification = new EnhancedApnsNotification(this.c.incrementAndGet(), (int) (expiry.getTime() / 1000L), dtbytes, messageBytes);

            notifications.add(notification);
            push((ApnsNotification) notification);
        }
        return notifications;
    }

    public Collection<EnhancedApnsNotification> push(Collection<byte[]> deviceTokens, byte[] payload) throws NetworkIOException {
        List<EnhancedApnsNotification> notifications = new ArrayList<EnhancedApnsNotification>(deviceTokens.size());
        for (byte[] deviceToken : deviceTokens) {
            EnhancedApnsNotification notification = new EnhancedApnsNotification(this.c.incrementAndGet(), 2147483647, deviceToken, payload);

            notifications.add(notification);
            push((ApnsNotification) notification);
        }
        return notifications;
    }

    public Collection<EnhancedApnsNotification> push(Collection<byte[]> deviceTokens, byte[] payload, int expiry) throws NetworkIOException {
        List<EnhancedApnsNotification> notifications = new ArrayList<EnhancedApnsNotification>(deviceTokens.size());
        for (byte[] deviceToken : deviceTokens) {
            EnhancedApnsNotification notification = new EnhancedApnsNotification(this.c.incrementAndGet(), expiry, deviceToken, payload);

            notifications.add(notification);
            push((ApnsNotification) notification);
        }
        return notifications;
    }

    public Map<String, Date> getInactiveDevices() throws NetworkIOException {
        return this.feedback.getInactiveDevices();
    }

    public abstract void push(ApnsNotification paramApnsNotification) throws NetworkIOException;
}

