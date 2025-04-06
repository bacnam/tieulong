/*     */ package org.apache.http.impl.nio.client;
/*     */ 
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.client.protocol.RequestClientConnControl;
/*     */ import org.apache.http.concurrent.BasicFuture;
/*     */ import org.apache.http.concurrent.FutureCallback;
/*     */ import org.apache.http.conn.ConnectionKeepAliveStrategy;
/*     */ import org.apache.http.impl.DefaultConnectionReuseStrategy;
/*     */ import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
/*     */ import org.apache.http.nio.conn.NHttpClientConnectionManager;
/*     */ import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
/*     */ import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
/*     */ import org.apache.http.protocol.BasicHttpContext;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.protocol.HttpProcessor;
/*     */ import org.apache.http.protocol.ImmutableHttpProcessor;
/*     */ import org.apache.http.protocol.RequestContent;
/*     */ import org.apache.http.protocol.RequestTargetHost;
/*     */ import org.apache.http.protocol.RequestUserAgent;
/*     */ import org.apache.http.util.Asserts;
/*     */ import org.apache.http.util.VersionInfo;
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
/*     */ class MinimalHttpAsyncClient
/*     */   extends CloseableHttpAsyncClientBase
/*     */ {
/*  56 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*     */   private final NHttpClientConnectionManager connmgr;
/*     */   
/*     */   private final InternalClientExec execChain;
/*     */ 
/*     */   
/*     */   public MinimalHttpAsyncClient(NHttpClientConnectionManager connmgr, ThreadFactory threadFactory) {
/*  64 */     super(connmgr, threadFactory);
/*  65 */     this.connmgr = connmgr;
/*  66 */     ImmutableHttpProcessor immutableHttpProcessor = new ImmutableHttpProcessor(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new RequestContent(), (HttpRequestInterceptor)new RequestTargetHost(), (HttpRequestInterceptor)new RequestClientConnControl(), (HttpRequestInterceptor)new RequestUserAgent(VersionInfo.getUserAgent("Apache-HttpAsyncClient", "org.apache.http.nio.client", getClass())) });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     this.execChain = new MinimalClientExec(connmgr, (HttpProcessor)immutableHttpProcessor, (ConnectionReuseStrategy)DefaultConnectionReuseStrategy.INSTANCE, (ConnectionKeepAliveStrategy)DefaultConnectionKeepAliveStrategy.INSTANCE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MinimalHttpAsyncClient(NHttpClientConnectionManager connmgr) {
/*  80 */     this(connmgr, Executors.defaultThreadFactory());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, HttpContext context, FutureCallback<T> callback) {
/*  88 */     CloseableHttpAsyncClientBase.Status status = getStatus();
/*  89 */     Asserts.check((status == CloseableHttpAsyncClientBase.Status.ACTIVE), "Request cannot be executed; I/O reactor status: %s", new Object[] { status });
/*     */     
/*  91 */     BasicFuture<T> future = new BasicFuture(callback);
/*  92 */     HttpClientContext localcontext = HttpClientContext.adapt((context != null) ? context : (HttpContext)new BasicHttpContext());
/*     */ 
/*     */ 
/*     */     
/*  96 */     DefaultClientExchangeHandlerImpl<T> handler = new DefaultClientExchangeHandlerImpl<T>(this.log, requestProducer, responseConsumer, localcontext, future, this.connmgr, this.execChain);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 105 */       handler.start();
/* 106 */     } catch (Exception ex) {
/* 107 */       handler.failed(ex);
/*     */     } 
/* 109 */     return (Future<T>)future;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/client/MinimalHttpAsyncClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */