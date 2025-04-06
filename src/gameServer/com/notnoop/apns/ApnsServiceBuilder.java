/*     */ package com.notnoop.apns;
/*     */ 
/*     */ import com.notnoop.apns.internal.ApnsConnection;
/*     */ import com.notnoop.apns.internal.ApnsConnectionImpl;
/*     */ import com.notnoop.apns.internal.ApnsFeedbackConnection;
/*     */ import com.notnoop.apns.internal.ApnsPooledConnection;
/*     */ import com.notnoop.apns.internal.ApnsServiceImpl;
/*     */ import com.notnoop.apns.internal.BatchApnsService;
/*     */ import com.notnoop.apns.internal.QueuedApnsService;
/*     */ import com.notnoop.apns.internal.Utilities;
/*     */ import com.notnoop.exceptions.InvalidSSLConfig;
/*     */ import com.notnoop.exceptions.RuntimeIOException;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.InputStream;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Proxy;
/*     */ import java.net.Socket;
/*     */ import java.security.KeyStore;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ApnsServiceBuilder
/*     */ {
/*     */   private static final String KEYSTORE_TYPE = "PKCS12";
/*     */   private static final String KEY_ALGORITHM = "sunx509";
/*     */   private SSLContext sslContext;
/*     */   private String gatewayHost;
/*  78 */   private int gatewaPort = -1;
/*     */   
/*     */   private String feedbackHost;
/*     */   private int feedbackPort;
/*  82 */   private int pooledMax = 1;
/*  83 */   private int cacheLength = 100;
/*     */   private boolean autoAdjustCacheLength = true;
/*  85 */   private ExecutorService executor = null;
/*     */   
/*  87 */   private ReconnectPolicy reconnectPolicy = ReconnectPolicy.Provided.NEVER.newObject();
/*     */   
/*     */   private boolean isQueued = false;
/*     */   
/*     */   private boolean isBatched = false;
/*     */   private int batchWaitTimeInSec;
/*     */   private int batchMaxWaitTimeInSec;
/*     */   private ThreadFactory batchThreadFactory;
/*  95 */   private ApnsDelegate delegate = ApnsDelegate.EMPTY;
/*  96 */   private Proxy proxy = null;
/*     */   
/*     */   private boolean errorDetection = true;
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder() {
/* 102 */     this.sslContext = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder withCert(String fileName, String password) throws RuntimeIOException, InvalidSSLConfig {
/* 131 */     FileInputStream stream = null;
/*     */     try {
/* 133 */       stream = new FileInputStream(fileName);
/* 134 */       return withCert(stream, password);
/* 135 */     } catch (FileNotFoundException e) {
/* 136 */       throw new RuntimeIOException(e);
/*     */     } finally {
/* 138 */       Utilities.close(stream);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder withCert(InputStream stream, String password) throws InvalidSSLConfig {
/* 166 */     assertPasswordNotEmpty(password);
/* 167 */     return withSSLContext(Utilities.newSSLContext(stream, password, "PKCS12", "sunx509"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder withCert(KeyStore keyStore, String password) throws InvalidSSLConfig {
/* 192 */     assertPasswordNotEmpty(password);
/* 193 */     return withSSLContext(Utilities.newSSLContext(keyStore, password, "sunx509"));
/*     */   }
/*     */ 
/*     */   
/*     */   private void assertPasswordNotEmpty(String password) {
/* 198 */     if (password == null || password.length() == 0) {
/* 199 */       throw new IllegalArgumentException("Passwords must be specified.Oracle Java SDK does not support passwordless p12 certificates");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder withSSLContext(SSLContext sslContext) {
/* 217 */     this.sslContext = sslContext;
/* 218 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder withGatewayDestination(String host, int port) {
/* 234 */     this.gatewayHost = host;
/* 235 */     this.gatewaPort = port;
/* 236 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder withFeedbackDestination(String host, int port) {
/* 252 */     this.feedbackHost = host;
/* 253 */     this.feedbackPort = port;
/* 254 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder withAppleDestination(boolean isProduction) {
/* 268 */     if (isProduction) {
/* 269 */       return withProductionDestination();
/*     */     }
/* 271 */     return withSandboxDestination();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder withSandboxDestination() {
/* 285 */     return withGatewayDestination("gateway.sandbox.push.apple.com", 2195).withFeedbackDestination("feedback.sandbox.push.apple.com", 2196);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder withProductionDestination() {
/* 300 */     return withGatewayDestination("gateway.push.apple.com", 2195).withFeedbackDestination("feedback.push.apple.com", 2196);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder withReconnectPolicy(ReconnectPolicy rp) {
/* 311 */     this.reconnectPolicy = rp;
/* 312 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder withAutoAdjustCacheLength(boolean autoAdjustCacheLength) {
/* 323 */     this.autoAdjustCacheLength = autoAdjustCacheLength;
/* 324 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder withReconnectPolicy(ReconnectPolicy.Provided rp) {
/* 334 */     this.reconnectPolicy = rp.newObject();
/* 335 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder withSocksProxy(String host, int port) {
/* 354 */     Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(host, port));
/*     */     
/* 356 */     return withProxy(proxy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder withProxy(Proxy proxy) {
/* 371 */     this.proxy = proxy;
/* 372 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder withCacheLength(int cacheLength) {
/* 383 */     this.cacheLength = cacheLength;
/* 384 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ApnsServiceBuilder withProxySocket(Socket proxySocket) {
/* 399 */     return withProxy(new Proxy(Proxy.Type.SOCKS, proxySocket.getRemoteSocketAddress()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder asPool(int maxConnections) {
/* 413 */     return asPool(Executors.newFixedThreadPool(maxConnections), maxConnections);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder asPool(ExecutorService executor, int maxConnections) {
/* 429 */     this.pooledMax = maxConnections;
/* 430 */     this.executor = executor;
/* 431 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder asQueued() {
/* 441 */     this.isQueued = true;
/* 442 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder asBatched() {
/* 453 */     return asBatched(5, 10);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder asBatched(int waitTimeInSec, int maxWaitTimeInSec) {
/* 470 */     return asBatched(waitTimeInSec, maxWaitTimeInSec, Executors.defaultThreadFactory());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder asBatched(int waitTimeInSec, int maxWaitTimeInSec, ThreadFactory threadFactory) {
/* 493 */     this.isBatched = true;
/* 494 */     this.batchWaitTimeInSec = waitTimeInSec;
/* 495 */     this.batchMaxWaitTimeInSec = maxWaitTimeInSec;
/* 496 */     this.batchThreadFactory = threadFactory;
/* 497 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder withDelegate(ApnsDelegate delegate) {
/* 509 */     this.delegate = (delegate == null) ? ApnsDelegate.EMPTY : delegate;
/* 510 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsServiceBuilder withNoErrorDetection() {
/* 524 */     this.errorDetection = false;
/* 525 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsService build() {
/*     */     BatchApnsService batchApnsService;
/*     */     ApnsPooledConnection apnsPooledConnection;
/* 535 */     checkInitialization();
/*     */ 
/*     */     
/* 538 */     SSLSocketFactory sslFactory = this.sslContext.getSocketFactory();
/* 539 */     ApnsFeedbackConnection feedback = new ApnsFeedbackConnection(sslFactory, this.feedbackHost, this.feedbackPort, this.proxy);
/*     */     
/* 541 */     ApnsConnectionImpl apnsConnectionImpl = new ApnsConnectionImpl(sslFactory, this.gatewayHost, this.gatewaPort, this.proxy, this.reconnectPolicy, this.delegate, this.errorDetection, this.cacheLength, this.autoAdjustCacheLength);
/*     */ 
/*     */     
/* 544 */     if (this.pooledMax != 1) {
/* 545 */       apnsPooledConnection = new ApnsPooledConnection((ApnsConnection)apnsConnectionImpl, this.pooledMax, this.executor);
/*     */     }
/*     */     
/* 548 */     ApnsServiceImpl apnsServiceImpl = new ApnsServiceImpl((ApnsConnection)apnsPooledConnection, feedback);
/*     */     
/* 550 */     if (this.isQueued) {
/* 551 */       QueuedApnsService queuedApnsService = new QueuedApnsService((ApnsService)apnsServiceImpl);
/*     */     }
/*     */     
/* 554 */     if (this.isBatched) {
/* 555 */       batchApnsService = new BatchApnsService((ApnsConnection)apnsPooledConnection, feedback, this.batchWaitTimeInSec, this.batchMaxWaitTimeInSec, this.batchThreadFactory);
/*     */     }
/*     */     
/* 558 */     batchApnsService.start();
/*     */     
/* 560 */     return (ApnsService)batchApnsService;
/*     */   }
/*     */   
/*     */   private void checkInitialization() {
/* 564 */     if (this.sslContext == null) {
/* 565 */       throw new IllegalStateException("SSL Certificates and attribute are not initialized\nUse .withCert() methods.");
/*     */     }
/*     */     
/* 568 */     if (this.gatewayHost == null || this.gatewaPort == -1)
/* 569 */       throw new IllegalStateException("The Destination APNS server is not stated\nUse .withDestination(), withSandboxDestination(), or withProductionDestination()."); 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/apns/ApnsServiceBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */