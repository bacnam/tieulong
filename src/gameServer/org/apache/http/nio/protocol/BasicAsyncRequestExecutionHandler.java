/*     */ package org.apache.http.nio.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.Future;
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.concurrent.BasicFuture;
/*     */ import org.apache.http.concurrent.FutureCallback;
/*     */ import org.apache.http.nio.ContentDecoder;
/*     */ import org.apache.http.nio.ContentEncoder;
/*     */ import org.apache.http.nio.IOControl;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.protocol.HttpProcessor;
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
/*     */ public class BasicAsyncRequestExecutionHandler<T>
/*     */   implements HttpAsyncRequestExecutionHandler<T>
/*     */ {
/*     */   private final HttpAsyncRequestProducer requestProducer;
/*     */   private final HttpAsyncResponseConsumer<T> responseConsumer;
/*     */   private final BasicFuture<T> future;
/*     */   private final HttpContext localContext;
/*     */   private final HttpProcessor httppocessor;
/*     */   private final ConnectionReuseStrategy reuseStrategy;
/*     */   private volatile boolean requestSent;
/*     */   
/*     */   public BasicAsyncRequestExecutionHandler(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, FutureCallback<T> callback, HttpContext localContext, HttpProcessor httppocessor, ConnectionReuseStrategy reuseStrategy, HttpParams params) {
/*  78 */     Args.notNull(requestProducer, "Request producer");
/*  79 */     Args.notNull(responseConsumer, "Response consumer");
/*  80 */     Args.notNull(localContext, "HTTP context");
/*  81 */     Args.notNull(httppocessor, "HTTP processor");
/*  82 */     Args.notNull(reuseStrategy, "Connection reuse strategy");
/*  83 */     Args.notNull(params, "HTTP parameters");
/*  84 */     this.requestProducer = requestProducer;
/*  85 */     this.responseConsumer = responseConsumer;
/*  86 */     this.future = new BasicFuture(callback);
/*  87 */     this.localContext = localContext;
/*  88 */     this.httppocessor = httppocessor;
/*  89 */     this.reuseStrategy = reuseStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicAsyncRequestExecutionHandler(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, HttpContext localContext, HttpProcessor httppocessor, ConnectionReuseStrategy reuseStrategy, HttpParams params) {
/*  99 */     this(requestProducer, responseConsumer, null, localContext, httppocessor, reuseStrategy, params);
/*     */   }
/*     */   
/*     */   public Future<T> getFuture() {
/* 103 */     return (Future<T>)this.future;
/*     */   }
/*     */   
/*     */   private void releaseResources() {
/*     */     try {
/* 108 */       this.responseConsumer.close();
/* 109 */     } catch (IOException ex) {}
/*     */     
/*     */     try {
/* 112 */       this.requestProducer.close();
/* 113 */     } catch (IOException ex) {}
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 118 */     releaseResources();
/* 119 */     if (!this.future.isDone()) {
/* 120 */       this.future.cancel();
/*     */     }
/*     */   }
/*     */   
/*     */   public HttpHost getTarget() {
/* 125 */     return this.requestProducer.getTarget();
/*     */   }
/*     */   
/*     */   public HttpRequest generateRequest() throws IOException, HttpException {
/* 129 */     return this.requestProducer.generateRequest();
/*     */   }
/*     */ 
/*     */   
/*     */   public void produceContent(ContentEncoder encoder, IOControl ioctrl) throws IOException {
/* 134 */     this.requestProducer.produceContent(encoder, ioctrl);
/*     */   }
/*     */   
/*     */   public void requestCompleted(HttpContext context) {
/* 138 */     this.requestProducer.requestCompleted(context);
/* 139 */     this.requestSent = true;
/*     */   }
/*     */   
/*     */   public boolean isRepeatable() {
/* 143 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetRequest() {}
/*     */   
/*     */   public void responseReceived(HttpResponse response) throws IOException, HttpException {
/* 150 */     this.responseConsumer.responseReceived(response);
/*     */   }
/*     */ 
/*     */   
/*     */   public void consumeContent(ContentDecoder decoder, IOControl ioctrl) throws IOException {
/* 155 */     this.responseConsumer.consumeContent(decoder, ioctrl);
/*     */   }
/*     */   
/*     */   public void failed(Exception ex) {
/*     */     try {
/* 160 */       if (!this.requestSent) {
/* 161 */         this.requestProducer.failed(ex);
/*     */       }
/* 163 */       this.responseConsumer.failed(ex);
/*     */     } finally {
/*     */       try {
/* 166 */         this.future.failed(ex);
/*     */       } finally {
/* 168 */         releaseResources();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean cancel() {
/*     */     try {
/* 175 */       boolean cancelled = this.responseConsumer.cancel();
/* 176 */       this.future.cancel();
/* 177 */       releaseResources();
/* 178 */       return cancelled;
/* 179 */     } catch (RuntimeException ex) {
/* 180 */       failed(ex);
/* 181 */       throw ex;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void responseCompleted(HttpContext context) {
/*     */     try {
/* 187 */       this.responseConsumer.responseCompleted(context);
/* 188 */       T result = this.responseConsumer.getResult();
/* 189 */       Exception ex = this.responseConsumer.getException();
/* 190 */       if (ex == null) {
/* 191 */         this.future.completed(result);
/*     */       } else {
/* 193 */         this.future.failed(ex);
/*     */       } 
/* 195 */       releaseResources();
/* 196 */     } catch (RuntimeException ex) {
/* 197 */       failed(ex);
/* 198 */       throw ex;
/*     */     } 
/*     */   }
/*     */   
/*     */   public T getResult() {
/* 203 */     return this.responseConsumer.getResult();
/*     */   }
/*     */   
/*     */   public Exception getException() {
/* 207 */     return this.responseConsumer.getException();
/*     */   }
/*     */   
/*     */   public HttpContext getContext() {
/* 211 */     return this.localContext;
/*     */   }
/*     */   
/*     */   public HttpProcessor getHttpProcessor() {
/* 215 */     return this.httppocessor;
/*     */   }
/*     */   
/*     */   public ConnectionReuseStrategy getConnectionReuseStrategy() {
/* 219 */     return this.reuseStrategy;
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/* 223 */     return this.responseConsumer.isDone();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/protocol/BasicAsyncRequestExecutionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */