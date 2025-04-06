/*     */ package org.apache.http.nio.conn.ssl;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.X509Certificate;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ import org.apache.http.conn.ssl.BrowserCompatHostnameVerifier;
/*     */ import org.apache.http.conn.ssl.SSLContexts;
/*     */ import org.apache.http.conn.ssl.TrustStrategy;
/*     */ import org.apache.http.conn.ssl.X509HostnameVerifier;
/*     */ import org.apache.http.nio.conn.scheme.LayeringStrategy;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.nio.reactor.ssl.SSLIOSession;
/*     */ import org.apache.http.nio.reactor.ssl.SSLMode;
/*     */ import org.apache.http.nio.reactor.ssl.SSLSetupHandler;
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
/*     */ @Deprecated
/*     */ public class SSLLayeringStrategy
/*     */   implements LayeringStrategy
/*     */ {
/*     */   public static final String TLS = "TLS";
/*     */   public static final String SSL = "SSL";
/*     */   public static final String SSLV2 = "SSLv2";
/*     */   private final SSLContext sslContext;
/*     */   private final X509HostnameVerifier hostnameVerifier;
/*     */   
/*     */   public static SSLLayeringStrategy getDefaultStrategy() {
/*  68 */     return new SSLLayeringStrategy(SSLContexts.createDefault());
/*     */   }
/*     */   
/*     */   public static SSLLayeringStrategy getSystemDefaultStrategy() {
/*  72 */     return new SSLLayeringStrategy(SSLContexts.createSystemDefault());
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
/*     */   private static SSLContext createSSLContext(String algorithm, KeyStore keystore, String keystorePassword, KeyStore truststore, SecureRandom random, TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
/*  86 */     String algo = (algorithm != null) ? algorithm : "TLS";
/*  87 */     KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
/*     */     
/*  89 */     kmfactory.init(keystore, (keystorePassword != null) ? keystorePassword.toCharArray() : null);
/*  90 */     KeyManager[] keymanagers = kmfactory.getKeyManagers();
/*  91 */     TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
/*     */     
/*  93 */     tmfactory.init(truststore);
/*  94 */     TrustManager[] trustmanagers = tmfactory.getTrustManagers();
/*  95 */     if (trustmanagers != null && trustStrategy != null) {
/*  96 */       for (int i = 0; i < trustmanagers.length; i++) {
/*  97 */         TrustManager tm = trustmanagers[i];
/*  98 */         if (tm instanceof X509TrustManager) {
/*  99 */           trustmanagers[i] = new TrustManagerDecorator((X509TrustManager)tm, trustStrategy);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 104 */     SSLContext sslcontext = SSLContext.getInstance(algo);
/* 105 */     sslcontext.init(keymanagers, trustmanagers, random);
/* 106 */     return sslcontext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLLayeringStrategy(String algorithm, KeyStore keystore, String keystorePassword, KeyStore truststore, SecureRandom random, X509HostnameVerifier hostnameVerifier) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 117 */     this(createSSLContext(algorithm, keystore, keystorePassword, truststore, random, null), hostnameVerifier);
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
/*     */   public SSLLayeringStrategy(String algorithm, KeyStore keystore, String keystorePassword, KeyStore truststore, SecureRandom random, TrustStrategy trustStrategy, X509HostnameVerifier hostnameVerifier) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 131 */     this(createSSLContext(algorithm, keystore, keystorePassword, truststore, random, trustStrategy), hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLLayeringStrategy(KeyStore keystore, String keystorePassword, KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 141 */     this("TLS", keystore, keystorePassword, truststore, null, null, (X509HostnameVerifier)new BrowserCompatHostnameVerifier());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLLayeringStrategy(KeyStore keystore, String keystorePassword) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 148 */     this("TLS", keystore, keystorePassword, null, null, null, (X509HostnameVerifier)new BrowserCompatHostnameVerifier());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLLayeringStrategy(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 154 */     this("TLS", null, null, truststore, null, null, (X509HostnameVerifier)new BrowserCompatHostnameVerifier());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLLayeringStrategy(TrustStrategy trustStrategy, X509HostnameVerifier hostnameVerifier) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 161 */     this("TLS", null, null, null, null, trustStrategy, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLLayeringStrategy(TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 167 */     this("TLS", null, null, null, null, trustStrategy, (X509HostnameVerifier)new BrowserCompatHostnameVerifier());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLLayeringStrategy(SSLContext sslContext, X509HostnameVerifier hostnameVerifier) {
/* 173 */     this.sslContext = sslContext;
/* 174 */     this.hostnameVerifier = hostnameVerifier;
/*     */   }
/*     */   
/*     */   public SSLLayeringStrategy(SSLContext sslContext) {
/* 178 */     this(sslContext, (X509HostnameVerifier)new BrowserCompatHostnameVerifier());
/*     */   }
/*     */   
/*     */   public boolean isSecure() {
/* 182 */     return true;
/*     */   }
/*     */   
/*     */   public SSLIOSession layer(IOSession iosession) {
/* 186 */     SSLIOSession ssliosession = new SSLIOSession(iosession, SSLMode.CLIENT, this.sslContext, new SSLSetupHandler()
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void initalize(SSLEngine sslengine) throws SSLException
/*     */           {
/* 194 */             SSLLayeringStrategy.this.initializeEngine(sslengine);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void verify(IOSession iosession, SSLSession sslsession) throws SSLException {
/* 200 */             SSLLayeringStrategy.this.verifySession(iosession, sslsession);
/*     */           }
/*     */         });
/*     */     
/* 204 */     iosession.setAttribute("http.session.ssl", ssliosession);
/* 205 */     return ssliosession;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initializeEngine(SSLEngine engine) {}
/*     */ 
/*     */   
/*     */   protected void verifySession(IOSession iosession, SSLSession sslsession) throws SSLException {
/* 214 */     InetSocketAddress address = (InetSocketAddress)iosession.getRemoteAddress();
/*     */     
/* 216 */     Certificate[] certs = sslsession.getPeerCertificates();
/* 217 */     X509Certificate x509 = (X509Certificate)certs[0];
/* 218 */     this.hostnameVerifier.verify(address.getHostName(), x509);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/conn/ssl/SSLLayeringStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */