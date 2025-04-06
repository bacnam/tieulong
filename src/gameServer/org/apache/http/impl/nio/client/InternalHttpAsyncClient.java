/*     */ package org.apache.http.impl.nio.client;
/*     */ 
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.auth.AuthSchemeProvider;
/*     */ import org.apache.http.auth.AuthState;
/*     */ import org.apache.http.client.CookieStore;
/*     */ import org.apache.http.client.CredentialsProvider;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.concurrent.BasicFuture;
/*     */ import org.apache.http.concurrent.FutureCallback;
/*     */ import org.apache.http.config.Lookup;
/*     */ import org.apache.http.cookie.CookieSpecProvider;
/*     */ import org.apache.http.nio.conn.NHttpClientConnectionManager;
/*     */ import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
/*     */ import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
/*     */ import org.apache.http.protocol.BasicHttpContext;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.Asserts;
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
/*     */ class InternalHttpAsyncClient
/*     */   extends CloseableHttpAsyncClientBase
/*     */ {
/*  53 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   private final NHttpClientConnectionManager connmgr;
/*     */   
/*     */   private final InternalClientExec exec;
/*     */   
/*     */   private final Lookup<CookieSpecProvider> cookieSpecRegistry;
/*     */   
/*     */   private final Lookup<AuthSchemeProvider> authSchemeRegistry;
/*     */   
/*     */   private final CookieStore cookieStore;
/*     */   
/*     */   private final CredentialsProvider credentialsProvider;
/*     */   
/*     */   private final RequestConfig defaultConfig;
/*     */ 
/*     */   
/*     */   public InternalHttpAsyncClient(NHttpClientConnectionManager connmgr, InternalClientExec exec, Lookup<CookieSpecProvider> cookieSpecRegistry, Lookup<AuthSchemeProvider> authSchemeRegistry, CookieStore cookieStore, CredentialsProvider credentialsProvider, RequestConfig defaultConfig, ThreadFactory threadFactory) {
/*  72 */     super(connmgr, threadFactory);
/*  73 */     this.connmgr = connmgr;
/*  74 */     this.exec = exec;
/*  75 */     this.cookieSpecRegistry = cookieSpecRegistry;
/*  76 */     this.authSchemeRegistry = authSchemeRegistry;
/*  77 */     this.cookieStore = cookieStore;
/*  78 */     this.credentialsProvider = credentialsProvider;
/*  79 */     this.defaultConfig = defaultConfig;
/*     */   }
/*     */   
/*     */   private void setupContext(HttpClientContext context) {
/*  83 */     if (context.getAttribute("http.auth.target-scope") == null) {
/*  84 */       context.setAttribute("http.auth.target-scope", new AuthState());
/*     */     }
/*  86 */     if (context.getAttribute("http.auth.proxy-scope") == null) {
/*  87 */       context.setAttribute("http.auth.proxy-scope", new AuthState());
/*     */     }
/*  89 */     if (context.getAttribute("http.authscheme-registry") == null) {
/*  90 */       context.setAttribute("http.authscheme-registry", this.authSchemeRegistry);
/*     */     }
/*  92 */     if (context.getAttribute("http.cookiespec-registry") == null) {
/*  93 */       context.setAttribute("http.cookiespec-registry", this.cookieSpecRegistry);
/*     */     }
/*  95 */     if (context.getAttribute("http.cookie-store") == null) {
/*  96 */       context.setAttribute("http.cookie-store", this.cookieStore);
/*     */     }
/*  98 */     if (context.getAttribute("http.auth.credentials-provider") == null) {
/*  99 */       context.setAttribute("http.auth.credentials-provider", this.credentialsProvider);
/*     */     }
/* 101 */     if (context.getAttribute("http.request-config") == null) {
/* 102 */       context.setAttribute("http.request-config", this.defaultConfig);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, HttpContext context, FutureCallback<T> callback) {
/* 111 */     CloseableHttpAsyncClientBase.Status status = getStatus();
/* 112 */     Asserts.check((status == CloseableHttpAsyncClientBase.Status.ACTIVE), "Request cannot be executed; I/O reactor status: %s", new Object[] { status });
/*     */     
/* 114 */     BasicFuture<T> future = new BasicFuture(callback);
/* 115 */     HttpClientContext localcontext = HttpClientContext.adapt((context != null) ? context : (HttpContext)new BasicHttpContext());
/*     */     
/* 117 */     setupContext(localcontext);
/*     */ 
/*     */     
/* 120 */     DefaultClientExchangeHandlerImpl<T> handler = new DefaultClientExchangeHandlerImpl<T>(this.log, requestProducer, responseConsumer, localcontext, future, this.connmgr, this.exec);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 129 */       handler.start();
/* 130 */     } catch (Exception ex) {
/* 131 */       handler.failed(ex);
/*     */     } 
/* 133 */     return (Future<T>)future;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/client/InternalHttpAsyncClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */