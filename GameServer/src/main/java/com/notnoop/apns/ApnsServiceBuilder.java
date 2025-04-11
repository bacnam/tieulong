package com.notnoop.apns;

import com.notnoop.apns.internal.*;
import com.notnoop.exceptions.InvalidSSLConfig;
import com.notnoop.exceptions.RuntimeIOException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.security.KeyStore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ApnsServiceBuilder {
    private static final String KEYSTORE_TYPE = "PKCS12";
    private static final String KEY_ALGORITHM = "sunx509";
    private SSLContext sslContext;
    private String gatewayHost;
    private int gatewaPort = -1;

    private String feedbackHost;
    private int feedbackPort;
    private int pooledMax = 1;
    private int cacheLength = 100;
    private boolean autoAdjustCacheLength = true;
    private ExecutorService executor = null;

    private ReconnectPolicy reconnectPolicy = ReconnectPolicy.Provided.NEVER.newObject();

    private boolean isQueued = false;

    private boolean isBatched = false;
    private int batchWaitTimeInSec;
    private int batchMaxWaitTimeInSec;
    private ThreadFactory batchThreadFactory;
    private ApnsDelegate delegate = ApnsDelegate.EMPTY;
    private Proxy proxy = null;

    private boolean errorDetection = true;

    public ApnsServiceBuilder() {
        this.sslContext = null;
    }

    public ApnsServiceBuilder withCert(String fileName, String password) throws RuntimeIOException, InvalidSSLConfig {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(fileName);
            return withCert(stream, password);
        } catch (FileNotFoundException e) {
            throw new RuntimeIOException(e);
        } finally {
            Utilities.close(stream);
        }
    }

    public ApnsServiceBuilder withCert(InputStream stream, String password) throws InvalidSSLConfig {
        assertPasswordNotEmpty(password);
        return withSSLContext(Utilities.newSSLContext(stream, password, "PKCS12", "sunx509"));
    }

    public ApnsServiceBuilder withCert(KeyStore keyStore, String password) throws InvalidSSLConfig {
        assertPasswordNotEmpty(password);
        return withSSLContext(Utilities.newSSLContext(keyStore, password, "sunx509"));
    }

    private void assertPasswordNotEmpty(String password) {
        if (password == null || password.length() == 0) {
            throw new IllegalArgumentException("Passwords must be specified.Oracle Java SDK does not support passwordless p12 certificates");
        }
    }

    public ApnsServiceBuilder withSSLContext(SSLContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }

    public ApnsServiceBuilder withGatewayDestination(String host, int port) {
        this.gatewayHost = host;
        this.gatewaPort = port;
        return this;
    }

    public ApnsServiceBuilder withFeedbackDestination(String host, int port) {
        this.feedbackHost = host;
        this.feedbackPort = port;
        return this;
    }

    public ApnsServiceBuilder withAppleDestination(boolean isProduction) {
        if (isProduction) {
            return withProductionDestination();
        }
        return withSandboxDestination();
    }

    public ApnsServiceBuilder withSandboxDestination() {
        return withGatewayDestination("gateway.sandbox.push.apple.com", 2195).withFeedbackDestination("feedback.sandbox.push.apple.com", 2196);
    }

    public ApnsServiceBuilder withProductionDestination() {
        return withGatewayDestination("gateway.push.apple.com", 2195).withFeedbackDestination("feedback.push.apple.com", 2196);
    }

    public ApnsServiceBuilder withReconnectPolicy(ReconnectPolicy rp) {
        this.reconnectPolicy = rp;
        return this;
    }

    public ApnsServiceBuilder withAutoAdjustCacheLength(boolean autoAdjustCacheLength) {
        this.autoAdjustCacheLength = autoAdjustCacheLength;
        return this;
    }

    public ApnsServiceBuilder withReconnectPolicy(ReconnectPolicy.Provided rp) {
        this.reconnectPolicy = rp.newObject();
        return this;
    }

    public ApnsServiceBuilder withSocksProxy(String host, int port) {
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(host, port));

        return withProxy(proxy);
    }

    public ApnsServiceBuilder withProxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public ApnsServiceBuilder withCacheLength(int cacheLength) {
        this.cacheLength = cacheLength;
        return this;
    }

    @Deprecated
    public ApnsServiceBuilder withProxySocket(Socket proxySocket) {
        return withProxy(new Proxy(Proxy.Type.SOCKS, proxySocket.getRemoteSocketAddress()));
    }

    public ApnsServiceBuilder asPool(int maxConnections) {
        return asPool(Executors.newFixedThreadPool(maxConnections), maxConnections);
    }

    public ApnsServiceBuilder asPool(ExecutorService executor, int maxConnections) {
        this.pooledMax = maxConnections;
        this.executor = executor;
        return this;
    }

    public ApnsServiceBuilder asQueued() {
        this.isQueued = true;
        return this;
    }

    public ApnsServiceBuilder asBatched() {
        return asBatched(5, 10);
    }

    public ApnsServiceBuilder asBatched(int waitTimeInSec, int maxWaitTimeInSec) {
        return asBatched(waitTimeInSec, maxWaitTimeInSec, Executors.defaultThreadFactory());
    }

    public ApnsServiceBuilder asBatched(int waitTimeInSec, int maxWaitTimeInSec, ThreadFactory threadFactory) {
        this.isBatched = true;
        this.batchWaitTimeInSec = waitTimeInSec;
        this.batchMaxWaitTimeInSec = maxWaitTimeInSec;
        this.batchThreadFactory = threadFactory;
        return this;
    }

    public ApnsServiceBuilder withDelegate(ApnsDelegate delegate) {
        this.delegate = (delegate == null) ? ApnsDelegate.EMPTY : delegate;
        return this;
    }

    public ApnsServiceBuilder withNoErrorDetection() {
        this.errorDetection = false;
        return this;
    }

    public ApnsService build() {
        BatchApnsService batchApnsService;
        ApnsPooledConnection apnsPooledConnection;
        checkInitialization();

        SSLSocketFactory sslFactory = this.sslContext.getSocketFactory();
        ApnsFeedbackConnection feedback = new ApnsFeedbackConnection(sslFactory, this.feedbackHost, this.feedbackPort, this.proxy);

        ApnsConnectionImpl apnsConnectionImpl = new ApnsConnectionImpl(sslFactory, this.gatewayHost, this.gatewaPort, this.proxy, this.reconnectPolicy, this.delegate, this.errorDetection, this.cacheLength, this.autoAdjustCacheLength);

        if (this.pooledMax != 1) {
            apnsPooledConnection = new ApnsPooledConnection((ApnsConnection) apnsConnectionImpl, this.pooledMax, this.executor);
        }

        ApnsServiceImpl apnsServiceImpl = new ApnsServiceImpl((ApnsConnection) apnsPooledConnection, feedback);

        if (this.isQueued) {
            QueuedApnsService queuedApnsService = new QueuedApnsService((ApnsService) apnsServiceImpl);
        }

        if (this.isBatched) {
            batchApnsService = new BatchApnsService((ApnsConnection) apnsPooledConnection, feedback, this.batchWaitTimeInSec, this.batchMaxWaitTimeInSec, this.batchThreadFactory);
        }

        batchApnsService.start();

        return (ApnsService) batchApnsService;
    }

    private void checkInitialization() {
        if (this.sslContext == null) {
            throw new IllegalStateException("SSL Certificates and attribute are not initialized\nUse .withCert() methods.");
        }

        if (this.gatewayHost == null || this.gatewaPort == -1)
            throw new IllegalStateException("The Destination APNS server is not stated\nUse .withDestination(), withSandboxDestination(), or withProductionDestination().");
    }
}

