/*     */ package org.apache.http.impl.nio.client;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.net.URI;
/*     */ import java.util.concurrent.Future;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.client.ClientProtocolException;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.client.utils.URIUtils;
/*     */ import org.apache.http.concurrent.BasicFuture;
/*     */ import org.apache.http.concurrent.FutureCallback;
/*     */ import org.apache.http.nio.client.HttpAsyncClient;
/*     */ import org.apache.http.nio.client.methods.HttpAsyncMethods;
/*     */ import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
/*     */ import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
/*     */ import org.apache.http.protocol.BasicHttpContext;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.Args;
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
/*     */ @ThreadSafe
/*     */ public abstract class CloseableHttpAsyncClient
/*     */   implements HttpAsyncClient, Closeable
/*     */ {
/*     */   public abstract boolean isRunning();
/*     */   
/*     */   public abstract void start();
/*     */   
/*     */   public <T> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, FutureCallback<T> callback) {
/*  66 */     return execute(requestProducer, responseConsumer, (HttpContext)new BasicHttpContext(), callback);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<HttpResponse> execute(HttpHost target, HttpRequest request, HttpContext context, FutureCallback<HttpResponse> callback) {
/*  72 */     return execute(HttpAsyncMethods.create(target, request), HttpAsyncMethods.createConsumer(), context, callback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<HttpResponse> execute(HttpHost target, HttpRequest request, FutureCallback<HttpResponse> callback) {
/*  81 */     return execute(target, request, (HttpContext)new BasicHttpContext(), callback);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<HttpResponse> execute(HttpUriRequest request, FutureCallback<HttpResponse> callback) {
/*  87 */     return execute(request, (HttpContext)new BasicHttpContext(), callback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<HttpResponse> execute(HttpUriRequest request, HttpContext context, FutureCallback<HttpResponse> callback) {
/*     */     HttpHost target;
/*     */     try {
/*  96 */       target = determineTarget(request);
/*  97 */     } catch (ClientProtocolException ex) {
/*  98 */       BasicFuture<HttpResponse> future = new BasicFuture(callback);
/*  99 */       future.failed((Exception)ex);
/* 100 */       return (Future<HttpResponse>)future;
/*     */     } 
/* 102 */     return execute(target, (HttpRequest)request, context, callback);
/*     */   }
/*     */   
/*     */   private HttpHost determineTarget(HttpUriRequest request) throws ClientProtocolException {
/* 106 */     Args.notNull(request, "HTTP request");
/*     */ 
/*     */     
/* 109 */     HttpHost target = null;
/*     */     
/* 111 */     URI requestURI = request.getURI();
/* 112 */     if (requestURI.isAbsolute()) {
/* 113 */       target = URIUtils.extractHost(requestURI);
/* 114 */       if (target == null) {
/* 115 */         throw new ClientProtocolException("URI does not specify a valid host name: " + requestURI);
/*     */       }
/*     */     } 
/*     */     
/* 119 */     return target;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/client/CloseableHttpAsyncClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */