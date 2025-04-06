/*     */ package org.apache.http.nio.protocol;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Future;
/*     */ import org.apache.http.ConnectionClosedException;
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.ExceptionLogger;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.concurrent.BasicFuture;
/*     */ import org.apache.http.concurrent.FutureCallback;
/*     */ import org.apache.http.impl.DefaultConnectionReuseStrategy;
/*     */ import org.apache.http.nio.NHttpClientConnection;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.pool.ConnPool;
/*     */ import org.apache.http.pool.PoolEntry;
/*     */ import org.apache.http.protocol.BasicHttpContext;
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
/*     */ @Immutable
/*     */ public class HttpAsyncRequester
/*     */ {
/*     */   private final HttpProcessor httpprocessor;
/*     */   private final ConnectionReuseStrategy connReuseStrategy;
/*     */   private final ExceptionLogger exceptionLogger;
/*     */   
/*     */   @Deprecated
/*     */   public HttpAsyncRequester(HttpProcessor httpprocessor, ConnectionReuseStrategy reuseStrategy, HttpParams params) {
/*  77 */     this(httpprocessor, reuseStrategy);
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
/*     */   public HttpAsyncRequester(HttpProcessor httpprocessor, ConnectionReuseStrategy connReuseStrategy, ExceptionLogger exceptionLogger) {
/*  97 */     this.httpprocessor = (HttpProcessor)Args.notNull(httpprocessor, "HTTP processor");
/*  98 */     this.connReuseStrategy = (connReuseStrategy != null) ? connReuseStrategy : (ConnectionReuseStrategy)DefaultConnectionReuseStrategy.INSTANCE;
/*     */     
/* 100 */     this.exceptionLogger = (exceptionLogger != null) ? exceptionLogger : ExceptionLogger.NO_OP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpAsyncRequester(HttpProcessor httpprocessor, ConnectionReuseStrategy connReuseStrategy) {
/* 111 */     this(httpprocessor, connReuseStrategy, (ExceptionLogger)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpAsyncRequester(HttpProcessor httpprocessor) {
/* 120 */     this(httpprocessor, null);
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
/*     */   public <T> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, NHttpClientConnection conn, HttpContext context, FutureCallback<T> callback) {
/* 140 */     Args.notNull(requestProducer, "HTTP request producer");
/* 141 */     Args.notNull(responseConsumer, "HTTP response consumer");
/* 142 */     Args.notNull(conn, "HTTP connection");
/* 143 */     Args.notNull(context, "HTTP context");
/* 144 */     BasicAsyncClientExchangeHandler<T> handler = new BasicAsyncClientExchangeHandler<T>(requestProducer, responseConsumer, callback, context, conn, this.httpprocessor, this.connReuseStrategy);
/*     */ 
/*     */     
/* 147 */     initExection(handler, conn);
/* 148 */     return handler.getFuture();
/*     */   }
/*     */ 
/*     */   
/*     */   private void initExection(HttpAsyncClientExchangeHandler handler, NHttpClientConnection conn) {
/* 153 */     conn.getContext().setAttribute("http.nio.exchange-handler", handler);
/* 154 */     if (!conn.isOpen()) {
/* 155 */       handler.failed((Exception)new ConnectionClosedException("Connection closed"));
/*     */       try {
/* 157 */         handler.close();
/* 158 */       } catch (IOException ex) {
/* 159 */         log(ex);
/*     */       } 
/*     */     } else {
/* 162 */       conn.requestOutput();
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
/*     */   public <T> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, NHttpClientConnection conn, HttpContext context) {
/* 181 */     return execute(requestProducer, responseConsumer, conn, context, (FutureCallback<T>)null);
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
/*     */   public <T> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, NHttpClientConnection conn) {
/* 197 */     return execute(requestProducer, responseConsumer, conn, (HttpContext)new BasicHttpContext());
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
/*     */   public <T, E extends PoolEntry<HttpHost, NHttpClientConnection>> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, ConnPool<HttpHost, E> connPool, HttpContext context, FutureCallback<T> callback) {
/* 218 */     Args.notNull(requestProducer, "HTTP request producer");
/* 219 */     Args.notNull(responseConsumer, "HTTP response consumer");
/* 220 */     Args.notNull(connPool, "HTTP connection pool");
/* 221 */     Args.notNull(context, "HTTP context");
/* 222 */     BasicFuture<T> future = new BasicFuture(callback);
/* 223 */     HttpHost target = requestProducer.getTarget();
/* 224 */     connPool.lease(target, null, new ConnRequestCallback<T, E>(future, requestProducer, responseConsumer, connPool, context));
/*     */     
/* 226 */     return (Future<T>)future;
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
/*     */   public <T, E extends PoolEntry<HttpHost, NHttpClientConnection>> Future<List<T>> executePipelined(HttpHost target, List<? extends HttpAsyncRequestProducer> requestProducers, List<? extends HttpAsyncResponseConsumer<T>> responseConsumers, ConnPool<HttpHost, E> connPool, HttpContext context, FutureCallback<List<T>> callback) {
/* 251 */     Args.notNull(target, "HTTP target");
/* 252 */     Args.notEmpty(requestProducers, "Request producer list");
/* 253 */     Args.notEmpty(responseConsumers, "Response consumer list");
/* 254 */     Args.notNull(connPool, "HTTP connection pool");
/* 255 */     Args.notNull(context, "HTTP context");
/* 256 */     BasicFuture<List<T>> future = new BasicFuture(callback);
/* 257 */     connPool.lease(target, null, new ConnPipelinedRequestCallback<T, E>(future, requestProducers, responseConsumers, connPool, context));
/*     */     
/* 259 */     return (Future<List<T>>)future;
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
/*     */   public <T, E extends PoolEntry<HttpHost, NHttpClientConnection>> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, E poolEntry, ConnPool<HttpHost, E> connPool, HttpContext context, FutureCallback<T> callback) {
/* 286 */     Args.notNull(requestProducer, "HTTP request producer");
/* 287 */     Args.notNull(responseConsumer, "HTTP response consumer");
/* 288 */     Args.notNull(connPool, "HTTP connection pool");
/* 289 */     Args.notNull(poolEntry, "Pool entry");
/* 290 */     Args.notNull(context, "HTTP context");
/* 291 */     BasicFuture<T> future = new BasicFuture(callback);
/* 292 */     NHttpClientConnection conn = (NHttpClientConnection)poolEntry.getConnection();
/* 293 */     BasicAsyncClientExchangeHandler<T> handler = new BasicAsyncClientExchangeHandler<T>(requestProducer, responseConsumer, new RequestExecutionCallback<T, E>(future, poolEntry, connPool), context, conn, this.httpprocessor, this.connReuseStrategy);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 298 */     initExection(handler, conn);
/* 299 */     return (Future<T>)future;
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
/*     */   public <T, E extends PoolEntry<HttpHost, NHttpClientConnection>> Future<List<T>> executePipelined(List<HttpAsyncRequestProducer> requestProducers, List<HttpAsyncResponseConsumer<T>> responseConsumers, E poolEntry, ConnPool<HttpHost, E> connPool, HttpContext context, FutureCallback<List<T>> callback) {
/* 326 */     Args.notEmpty(requestProducers, "Request producer list");
/* 327 */     Args.notEmpty(responseConsumers, "Response consumer list");
/* 328 */     Args.notNull(connPool, "HTTP connection pool");
/* 329 */     Args.notNull(poolEntry, "Pool entry");
/* 330 */     Args.notNull(context, "HTTP context");
/* 331 */     BasicFuture<List<T>> future = new BasicFuture(callback);
/* 332 */     NHttpClientConnection conn = (NHttpClientConnection)poolEntry.getConnection();
/* 333 */     PipeliningClientExchangeHandler<T> handler = new PipeliningClientExchangeHandler<T>(requestProducers, responseConsumers, new RequestExecutionCallback<List<T>, E>(future, poolEntry, connPool), context, conn, this.httpprocessor, this.connReuseStrategy);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 338 */     initExection(handler, conn);
/* 339 */     return (Future<List<T>>)future;
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
/*     */   public <T, E extends PoolEntry<HttpHost, NHttpClientConnection>> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, ConnPool<HttpHost, E> connPool, HttpContext context) {
/* 358 */     return execute(requestProducer, responseConsumer, connPool, context, (FutureCallback<T>)null);
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
/*     */   public <T, E extends PoolEntry<HttpHost, NHttpClientConnection>> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, ConnPool<HttpHost, E> connPool) {
/* 375 */     return execute(requestProducer, responseConsumer, connPool, (HttpContext)new BasicHttpContext());
/*     */   }
/*     */ 
/*     */   
/*     */   class ConnRequestCallback<T, E extends PoolEntry<HttpHost, NHttpClientConnection>>
/*     */     implements FutureCallback<E>
/*     */   {
/*     */     private final BasicFuture<T> requestFuture;
/*     */     
/*     */     private final HttpAsyncRequestProducer requestProducer;
/*     */     
/*     */     private final HttpAsyncResponseConsumer<T> responseConsumer;
/*     */     
/*     */     private final ConnPool<HttpHost, E> connPool;
/*     */     
/*     */     private final HttpContext context;
/*     */     
/*     */     ConnRequestCallback(BasicFuture<T> requestFuture, HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, ConnPool<HttpHost, E> connPool, HttpContext context) {
/* 393 */       this.requestFuture = requestFuture;
/* 394 */       this.requestProducer = requestProducer;
/* 395 */       this.responseConsumer = responseConsumer;
/* 396 */       this.connPool = connPool;
/* 397 */       this.context = context;
/*     */     }
/*     */ 
/*     */     
/*     */     public void completed(E result) {
/* 402 */       if (this.requestFuture.isDone()) {
/* 403 */         this.connPool.release(result, true);
/*     */         return;
/*     */       } 
/* 406 */       NHttpClientConnection conn = (NHttpClientConnection)result.getConnection();
/* 407 */       BasicAsyncClientExchangeHandler<T> handler = new BasicAsyncClientExchangeHandler<T>(this.requestProducer, this.responseConsumer, new HttpAsyncRequester.RequestExecutionCallback<T, E>(this.requestFuture, result, this.connPool), this.context, conn, HttpAsyncRequester.this.httpprocessor, HttpAsyncRequester.this.connReuseStrategy);
/*     */ 
/*     */ 
/*     */       
/* 411 */       HttpAsyncRequester.this.initExection(handler, conn);
/*     */     }
/*     */ 
/*     */     
/*     */     public void failed(Exception ex) {
/*     */       try {
/*     */         try {
/* 418 */           this.responseConsumer.failed(ex);
/*     */         } finally {
/* 420 */           releaseResources();
/*     */         } 
/*     */       } finally {
/* 423 */         this.requestFuture.failed(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancelled() {
/*     */       try {
/*     */         try {
/* 431 */           this.responseConsumer.cancel();
/*     */         } finally {
/* 433 */           releaseResources();
/*     */         } 
/*     */       } finally {
/* 436 */         this.requestFuture.cancel(true);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void releaseResources() {
/* 441 */       HttpAsyncRequester.this.close(this.requestProducer);
/* 442 */       HttpAsyncRequester.this.close(this.responseConsumer);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   class ConnPipelinedRequestCallback<T, E extends PoolEntry<HttpHost, NHttpClientConnection>>
/*     */     implements FutureCallback<E>
/*     */   {
/*     */     private final BasicFuture<List<T>> requestFuture;
/*     */     
/*     */     private final List<? extends HttpAsyncRequestProducer> requestProducers;
/*     */     
/*     */     private final List<? extends HttpAsyncResponseConsumer<T>> responseConsumers;
/*     */     
/*     */     private final ConnPool<HttpHost, E> connPool;
/*     */     
/*     */     private final HttpContext context;
/*     */ 
/*     */     
/*     */     ConnPipelinedRequestCallback(BasicFuture<List<T>> requestFuture, List<? extends HttpAsyncRequestProducer> requestProducers, List<? extends HttpAsyncResponseConsumer<T>> responseConsumers, ConnPool<HttpHost, E> connPool, HttpContext context) {
/* 462 */       this.requestFuture = requestFuture;
/* 463 */       this.requestProducers = requestProducers;
/* 464 */       this.responseConsumers = responseConsumers;
/* 465 */       this.connPool = connPool;
/* 466 */       this.context = context;
/*     */     }
/*     */ 
/*     */     
/*     */     public void completed(E result) {
/* 471 */       if (this.requestFuture.isDone()) {
/* 472 */         this.connPool.release(result, true);
/*     */         return;
/*     */       } 
/* 475 */       NHttpClientConnection conn = (NHttpClientConnection)result.getConnection();
/* 476 */       PipeliningClientExchangeHandler<T> handler = new PipeliningClientExchangeHandler<T>(this.requestProducers, this.responseConsumers, new HttpAsyncRequester.RequestExecutionCallback<List<T>, E>(this.requestFuture, result, this.connPool), this.context, conn, HttpAsyncRequester.this.httpprocessor, HttpAsyncRequester.this.connReuseStrategy);
/*     */ 
/*     */ 
/*     */       
/* 480 */       HttpAsyncRequester.this.initExection(handler, conn);
/*     */     }
/*     */ 
/*     */     
/*     */     public void failed(Exception ex) {
/*     */       try {
/*     */         try {
/* 487 */           for (HttpAsyncResponseConsumer<T> responseConsumer : this.responseConsumers) {
/* 488 */             responseConsumer.failed(ex);
/*     */           }
/*     */         } finally {
/* 491 */           releaseResources();
/*     */         } 
/*     */       } finally {
/* 494 */         this.requestFuture.failed(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancelled() {
/*     */       try {
/*     */         try {
/* 502 */           for (HttpAsyncResponseConsumer<T> responseConsumer : this.responseConsumers) {
/* 503 */             responseConsumer.cancel();
/*     */           }
/*     */         } finally {
/* 506 */           releaseResources();
/*     */         } 
/*     */       } finally {
/* 509 */         this.requestFuture.cancel(true);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void releaseResources() {
/* 514 */       for (HttpAsyncRequestProducer requestProducer : this.requestProducers) {
/* 515 */         HttpAsyncRequester.this.close(requestProducer);
/*     */       }
/* 517 */       for (HttpAsyncResponseConsumer<T> responseConsumer : this.responseConsumers) {
/* 518 */         HttpAsyncRequester.this.close(responseConsumer);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   class RequestExecutionCallback<T, E extends PoolEntry<HttpHost, NHttpClientConnection>>
/*     */     implements FutureCallback<T>
/*     */   {
/*     */     private final BasicFuture<T> future;
/*     */     
/*     */     private final E poolEntry;
/*     */     
/*     */     private final ConnPool<HttpHost, E> connPool;
/*     */ 
/*     */     
/*     */     RequestExecutionCallback(BasicFuture<T> future, E poolEntry, ConnPool<HttpHost, E> connPool) {
/* 536 */       this.future = future;
/* 537 */       this.poolEntry = poolEntry;
/* 538 */       this.connPool = connPool;
/*     */     }
/*     */ 
/*     */     
/*     */     public void completed(T result) {
/*     */       try {
/* 544 */         this.connPool.release(this.poolEntry, true);
/*     */       } finally {
/* 546 */         this.future.completed(result);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void failed(Exception ex) {
/*     */       try {
/* 553 */         this.connPool.release(this.poolEntry, false);
/*     */       } finally {
/* 555 */         this.future.failed(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancelled() {
/*     */       try {
/* 562 */         this.connPool.release(this.poolEntry, false);
/*     */       } finally {
/* 564 */         this.future.cancel(true);
/*     */       } 
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
/*     */   protected void log(Exception ex) {
/* 578 */     this.exceptionLogger.log(ex);
/*     */   }
/*     */   
/*     */   private void close(Closeable closeable) {
/*     */     try {
/* 583 */       closeable.close();
/* 584 */     } catch (IOException ex) {
/* 585 */       log(ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/protocol/HttpAsyncRequester.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */