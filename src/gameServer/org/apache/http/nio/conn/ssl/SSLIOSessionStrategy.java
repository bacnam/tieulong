/*     */ package org.apache.http.nio.conn.ssl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.X509Certificate;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
/*     */ import org.apache.http.conn.ssl.BrowserCompatHostnameVerifier;
/*     */ import org.apache.http.conn.ssl.SSLContexts;
/*     */ import org.apache.http.conn.ssl.StrictHostnameVerifier;
/*     */ import org.apache.http.conn.ssl.X509HostnameVerifier;
/*     */ import org.apache.http.nio.conn.SchemeIOSessionStrategy;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.nio.reactor.ssl.SSLIOSession;
/*     */ import org.apache.http.nio.reactor.ssl.SSLMode;
/*     */ import org.apache.http.nio.reactor.ssl.SSLSetupHandler;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
/*     */ import org.apache.http.util.TextUtils;
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
/*     */ public class SSLIOSessionStrategy
/*     */   implements SchemeIOSessionStrategy
/*     */ {
/*  61 */   public static final X509HostnameVerifier ALLOW_ALL_HOSTNAME_VERIFIER = (X509HostnameVerifier)new AllowAllHostnameVerifier();
/*     */ 
/*     */   
/*  64 */   public static final X509HostnameVerifier BROWSER_COMPATIBLE_HOSTNAME_VERIFIER = (X509HostnameVerifier)new BrowserCompatHostnameVerifier();
/*     */ 
/*     */   
/*  67 */   public static final X509HostnameVerifier STRICT_HOSTNAME_VERIFIER = (X509HostnameVerifier)new StrictHostnameVerifier(); private final SSLContext sslContext;
/*     */   private final String[] supportedProtocols;
/*     */   
/*     */   private static String[] split(String s) {
/*  71 */     if (TextUtils.isBlank(s)) {
/*  72 */       return null;
/*     */     }
/*  74 */     return s.split(" *, *");
/*     */   }
/*     */   private final String[] supportedCipherSuites; private final X509HostnameVerifier hostnameVerifier;
/*     */   public static SSLIOSessionStrategy getDefaultStrategy() {
/*  78 */     return new SSLIOSessionStrategy(SSLContexts.createDefault(), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static SSLIOSessionStrategy getSystemDefaultStrategy() {
/*  84 */     return new SSLIOSessionStrategy(SSLContexts.createSystemDefault(), split(System.getProperty("https.protocols")), split(System.getProperty("https.cipherSuites")), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
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
/*     */   public SSLIOSessionStrategy(SSLContext sslContext, String[] supportedProtocols, String[] supportedCipherSuites, X509HostnameVerifier hostnameVerifier) {
/* 102 */     this.sslContext = (SSLContext)Args.notNull(sslContext, "SSL context");
/* 103 */     this.supportedProtocols = supportedProtocols;
/* 104 */     this.supportedCipherSuites = supportedCipherSuites;
/* 105 */     this.hostnameVerifier = (hostnameVerifier != null) ? hostnameVerifier : BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLIOSessionStrategy(SSLContext sslcontext, X509HostnameVerifier hostnameVerifier) {
/* 111 */     this(sslcontext, null, null, hostnameVerifier);
/*     */   }
/*     */   
/*     */   public SSLIOSessionStrategy(SSLContext sslcontext) {
/* 115 */     this(sslcontext, null, null, BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
/*     */   }
/*     */   
/*     */   public SSLIOSession upgrade(final HttpHost host, IOSession iosession) throws IOException {
/* 119 */     Asserts.check(!(iosession instanceof SSLIOSession), "I/O session is already upgraded to TLS/SSL");
/* 120 */     SSLIOSession ssliosession = new SSLIOSession(iosession, SSLMode.CLIENT, this.sslContext, new SSLSetupHandler()
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void initalize(SSLEngine sslengine) throws SSLException
/*     */           {
/* 128 */             if (SSLIOSessionStrategy.this.supportedProtocols != null) {
/* 129 */               sslengine.setEnabledProtocols(SSLIOSessionStrategy.this.supportedProtocols);
/*     */             }
/* 131 */             if (SSLIOSessionStrategy.this.supportedCipherSuites != null) {
/* 132 */               sslengine.setEnabledCipherSuites(SSLIOSessionStrategy.this.supportedCipherSuites);
/*     */             }
/* 134 */             SSLIOSessionStrategy.this.initializeEngine(sslengine);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void verify(IOSession iosession, SSLSession sslsession) throws SSLException {
/* 140 */             SSLIOSessionStrategy.this.verifySession(host, iosession, sslsession);
/*     */           }
/*     */         });
/*     */     
/* 144 */     iosession.setAttribute("http.session.ssl", ssliosession);
/* 145 */     ssliosession.initialize();
/* 146 */     return ssliosession;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initializeEngine(SSLEngine engine) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void verifySession(HttpHost host, IOSession iosession, SSLSession sslsession) throws SSLException {
/* 156 */     Certificate[] certs = sslsession.getPeerCertificates();
/* 157 */     X509Certificate x509 = (X509Certificate)certs[0];
/* 158 */     this.hostnameVerifier.verify(host.getHostName(), x509);
/*     */   }
/*     */   
/*     */   public boolean isLayeringRequired() {
/* 162 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/conn/ssl/SSLIOSessionStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */