package com.notnoop.apns.internal;

import com.notnoop.apns.*;
import com.notnoop.exceptions.ApnsDeliveryErrorException;
import com.notnoop.exceptions.NetworkIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ApnsConnectionImpl
        implements ApnsConnection {
    private static final Logger logger = LoggerFactory.getLogger(ApnsConnectionImpl.class);
    private static final int RETRIES = 3;
    private final SocketFactory factory;
    private final String host;
    private final int port;
    private final Proxy proxy;
    private final ReconnectPolicy reconnectPolicy;
    private final ApnsDelegate delegate;
    private final boolean errorDetection;
    private final boolean autoAdjustCacheLength;
    private final ConcurrentLinkedQueue<ApnsNotification> cachedNotifications;
    private final ConcurrentLinkedQueue<ApnsNotification> notificationsBuffer;
    int DELAY_IN_MS;
    private int cacheLength;
    private Socket socket;

    public ApnsConnectionImpl(SocketFactory factory, String host, int port) {
        this(factory, host, port, new ReconnectPolicies.Never(), ApnsDelegate.EMPTY);
    }

    public ApnsConnectionImpl(SocketFactory factory, String host, int port, ReconnectPolicy reconnectPolicy, ApnsDelegate delegate) {
        this(factory, host, port, null, reconnectPolicy, delegate, false, 100, true);
    }

    public ApnsConnectionImpl(SocketFactory factory, String host, int port, Proxy proxy, ReconnectPolicy reconnectPolicy, ApnsDelegate delegate, boolean errorDetection, int cacheLength, boolean autoAdjustCacheLength) {
        this.DELAY_IN_MS = 1000;
        this.factory = factory;
        this.host = host;
        this.port = port;
        this.reconnectPolicy = reconnectPolicy;
        this.delegate = (delegate == null) ? ApnsDelegate.EMPTY : delegate;
        this.proxy = proxy;
        this.errorDetection = errorDetection;
        this.cacheLength = cacheLength;
        this.autoAdjustCacheLength = autoAdjustCacheLength;
        this.cachedNotifications = new ConcurrentLinkedQueue<ApnsNotification>();
        this.notificationsBuffer = new ConcurrentLinkedQueue<ApnsNotification>();
    }

    public synchronized void close() {
        Utilities.close(this.socket);
    }

    public synchronized void sendMessage(ApnsNotification m) throws NetworkIOException {
        sendMessage(m, false);
    }

    private void monitorSocket(final Socket socket) {
        class MonitoringThread extends Thread {
            public void run() {
                try {
                    InputStream in = socket.getInputStream();
                    int expectedSize = 6;
                    byte[] bytes = new byte[6];
                    while (in.read(bytes) == 6) {
                        int command = bytes[0] & 0xFF;
                        if (command != 8)
                            throw new IOException("Unexpected command byte " + command);
                        int statusCode = bytes[1] & 0xFF;
                        DeliveryError e = DeliveryError.ofCode(statusCode);
                        int id = Utilities.parseBytes(bytes[2], bytes[3], bytes[4], bytes[5]);
                        Queue<ApnsNotification> tempCache = new LinkedList<ApnsNotification>();
                        ApnsNotification notification = null;
                        boolean foundNotification = false;
                        while (!ApnsConnectionImpl.this.cachedNotifications.isEmpty()) {
                            notification = ApnsConnectionImpl.this.cachedNotifications.poll();
                            if (notification.getIdentifier() == id) {
                                foundNotification = true;
                                break;
                            }
                            tempCache.add(notification);
                        }
                        if (foundNotification) {
                            ApnsConnectionImpl.this.delegate.messageSendFailed(notification, (Throwable) new ApnsDeliveryErrorException(e));
                        } else {
                            ApnsConnectionImpl.this.cachedNotifications.addAll(tempCache);
                            int i = tempCache.size();
                            ApnsConnectionImpl.logger.warn("Received error for message that wasn't in the cache...");
                            if (ApnsConnectionImpl.this.autoAdjustCacheLength) {
                                ApnsConnectionImpl.this.cacheLength = ApnsConnectionImpl.this.cacheLength + i / 2;
                                ApnsConnectionImpl.this.delegate.cacheLengthExceeded(ApnsConnectionImpl.this.cacheLength);
                            }
                            ApnsConnectionImpl.this.delegate.messageSendFailed(null, (Throwable) new ApnsDeliveryErrorException(e));
                        }
                        int resendSize = 0;
                        while (!ApnsConnectionImpl.this.cachedNotifications.isEmpty()) {
                            resendSize++;
                            ApnsConnectionImpl.this.notificationsBuffer.add(ApnsConnectionImpl.this.cachedNotifications.poll());
                        }
                        ApnsConnectionImpl.this.delegate.notificationsResent(resendSize);
                        ApnsConnectionImpl.this.delegate.connectionClosed(e, id);
                        ApnsConnectionImpl.this.drainBuffer();
                    }
                } catch (Exception e) {
                    ApnsConnectionImpl.logger.info("Exception while waiting for error code", e);
                    ApnsConnectionImpl.this.delegate.connectionClosed(DeliveryError.UNKNOWN, -1);
                } finally {
                    ApnsConnectionImpl.this.close();
                }
            }
        }
        ;
        Thread t = new MonitoringThread();
        t.setDaemon(true);
        t.start();
    }

    public synchronized void sendMessage(ApnsNotification m, boolean fromBuffer) throws NetworkIOException {
        int attempts = 0;
        while (true) {
            try {
                attempts++;
                Socket socket = socket();
                socket.getOutputStream().write(m.marshall());
                socket.getOutputStream().flush();
                cacheNotification(m);

                this.delegate.messageSent(m, fromBuffer);

                logger.debug("Message \"{}\" sent", m);

                attempts = 0;
                drainBuffer();
                break;
            } catch (Exception e) {
                Utilities.close(this.socket);
                this.socket = null;
                if (attempts >= 3) {
                    logger.error("Couldn't send message after 3 retries." + m, e);
                    this.delegate.messageSendFailed(m, e);
                    Utilities.wrapAndThrowAsRuntimeException(e);
                }

                if (attempts != 1) {
                    logger.info("Failed to send message " + m + "... trying again after delay", e);
                    Utilities.sleep(this.DELAY_IN_MS);
                }
            }
        }
    }

    private synchronized Socket socket() throws NetworkIOException {
        if (this.reconnectPolicy.shouldReconnect()) {
            Utilities.close(this.socket);
            this.socket = null;
        }
        if (this.socket == null || this.socket.isClosed())
            try {
                if (this.proxy == null) {
                    this.socket = this.factory.createSocket(this.host, this.port);
                } else if (this.proxy.type() == Proxy.Type.HTTP) {
                    TlsTunnelBuilder tunnelBuilder = new TlsTunnelBuilder();
                    this.socket = tunnelBuilder.build((SSLSocketFactory) this.factory, this.proxy, this.host, this.port);
                } else {
                    boolean success = false;
                    Socket proxySocket = null;
                    try {
                        proxySocket = new Socket(this.proxy);
                        proxySocket.connect(new InetSocketAddress(this.host, this.port));
                        this.socket = ((SSLSocketFactory) this.factory).createSocket(proxySocket, this.host, this.port, false);
                        success = true;
                    } finally {
                        if (!success)
                            Utilities.close(proxySocket);
                    }
                }
                if (this.errorDetection)
                    monitorSocket(this.socket);
                this.reconnectPolicy.reconnected();
                logger.debug("Made a new connection to APNS");
            } catch (IOException e) {
                logger.error("Couldn't connect to APNS server", e);
                throw new NetworkIOException(e);
            }
        return this.socket;
    }

    private void drainBuffer() {
        if (!this.notificationsBuffer.isEmpty()) {
            sendMessage(this.notificationsBuffer.poll(), true);
        }
    }

    private void cacheNotification(ApnsNotification notification) {
        this.cachedNotifications.add(notification);
        while (this.cachedNotifications.size() > this.cacheLength) {
            this.cachedNotifications.poll();
            logger.debug("Removing notification from cache " + notification);
        }
    }

    public ApnsConnectionImpl copy() {
        return new ApnsConnectionImpl(this.factory, this.host, this.port, this.proxy, this.reconnectPolicy.copy(), this.delegate, this.errorDetection, this.cacheLength, this.autoAdjustCacheLength);
    }

    public void testConnection() throws NetworkIOException {
        ApnsConnectionImpl testConnection = null;
        try {
            testConnection = new ApnsConnectionImpl(this.factory, this.host, this.port, this.reconnectPolicy.copy(), ApnsDelegate.EMPTY);
            testConnection.sendMessage((ApnsNotification) new SimpleApnsNotification(new byte[]{0}, new byte[]{0}));
        } finally {
            if (testConnection != null) {
                testConnection.close();
            }
        }
    }

    public int getCacheLength() {
        return this.cacheLength;
    }

    public void setCacheLength(int cacheLength) {
        this.cacheLength = cacheLength;
    }
}

