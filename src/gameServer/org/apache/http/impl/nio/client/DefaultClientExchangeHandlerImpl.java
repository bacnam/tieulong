/*     */ package org.apache.http.impl.nio.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.http.ConnectionClosedException;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.HttpExecutionAware;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.concurrent.BasicFuture;
/*     */ import org.apache.http.concurrent.Cancellable;
/*     */ import org.apache.http.concurrent.FutureCallback;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.nio.ContentDecoder;
/*     */ import org.apache.http.nio.ContentEncoder;
/*     */ import org.apache.http.nio.IOControl;
/*     */ import org.apache.http.nio.NHttpClientConnection;
/*     */ import org.apache.http.nio.conn.NHttpClientConnectionManager;
/*     */ import org.apache.http.nio.protocol.HttpAsyncClientExchangeHandler;
/*     */ import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
/*     */ import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DefaultClientExchangeHandlerImpl<T>
/*     */   implements HttpAsyncClientExchangeHandler, InternalConnManager, Cancellable
/*     */ {
/*     */   private final Log log;
/*     */   private final HttpAsyncRequestProducer requestProducer;
/*     */   private final HttpAsyncResponseConsumer<T> responseConsumer;
/*     */   private final HttpClientContext localContext;
/*     */   private final BasicFuture<T> resultFuture;
/*     */   private final NHttpClientConnectionManager connmgr;
/*     */   private final InternalClientExec exec;
/*     */   private final InternalState state;
/*     */   private final AtomicReference<NHttpClientConnection> managedConn;
/*     */   private final AtomicBoolean closed;
/*     */   private final AtomicBoolean completed;
/*     */   
/*     */   public DefaultClientExchangeHandlerImpl(Log log, HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, HttpClientContext localContext, BasicFuture<T> resultFuture, NHttpClientConnectionManager connmgr, InternalClientExec exec) {
/*  88 */     this.log = log;
/*  89 */     this.requestProducer = requestProducer;
/*  90 */     this.responseConsumer = responseConsumer;
/*  91 */     this.localContext = localContext;
/*  92 */     this.resultFuture = resultFuture;
/*  93 */     this.connmgr = connmgr;
/*  94 */     this.exec = exec;
/*  95 */     this.state = new InternalState(requestProducer, responseConsumer, localContext);
/*  96 */     this.closed = new AtomicBoolean(false);
/*  97 */     this.completed = new AtomicBoolean(false);
/*  98 */     this.managedConn = new AtomicReference<NHttpClientConnection>(null);
/*     */   }
/*     */   
/*     */   public void close() {
/* 102 */     if (this.closed.getAndSet(true)) {
/*     */       return;
/*     */     }
/* 105 */     abortConnection();
/*     */     try {
/* 107 */       this.requestProducer.close();
/* 108 */     } catch (IOException ex) {
/* 109 */       this.log.debug("I/O error closing request producer", ex);
/*     */     } 
/*     */     try {
/* 112 */       this.responseConsumer.close();
/* 113 */     } catch (IOException ex) {
/* 114 */       this.log.debug("I/O error closing response consumer", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void start() throws HttpException, IOException {
/* 119 */     HttpHost target = this.requestProducer.getTarget();
/* 120 */     HttpRequest original = this.requestProducer.generateRequest();
/*     */     
/* 122 */     if (original instanceof HttpExecutionAware) {
/* 123 */       ((HttpExecutionAware)original).setCancellable(this);
/*     */     }
/* 125 */     this.exec.prepare(this.state, target, original);
/* 126 */     requestConnection();
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/* 130 */     return this.completed.get();
/*     */   }
/*     */   
/*     */   public HttpRequest generateRequest() throws IOException, HttpException {
/* 134 */     return this.exec.generateRequest(this.state, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void produceContent(ContentEncoder encoder, IOControl ioctrl) throws IOException {
/* 139 */     this.exec.produceContent(this.state, encoder, ioctrl);
/*     */   }
/*     */   
/*     */   public void requestCompleted() {
/* 143 */     this.exec.requestCompleted(this.state);
/*     */   }
/*     */ 
/*     */   
/*     */   public void responseReceived(HttpResponse response) throws IOException, HttpException {
/* 148 */     this.exec.responseReceived(this.state, response);
/*     */   }
/*     */ 
/*     */   
/*     */   public void consumeContent(ContentDecoder decoder, IOControl ioctrl) throws IOException {
/* 153 */     this.exec.consumeContent(this.state, decoder, ioctrl);
/* 154 */     if (!decoder.isCompleted() && this.responseConsumer.isDone()) {
/* 155 */       if (this.completed.compareAndSet(false, true)) {
/* 156 */         this.resultFuture.cancel();
/*     */       }
/* 158 */       this.state.setNonReusable();
/* 159 */       releaseConnection();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void responseCompleted() throws IOException, HttpException {
/* 164 */     this.exec.responseCompleted(this.state, this);
/*     */     
/* 166 */     if (this.state.getFinalResponse() != null || this.resultFuture.isDone()) {
/*     */       try {
/* 168 */         this.completed.set(true);
/* 169 */         releaseConnection();
/* 170 */         T result = (T)this.responseConsumer.getResult();
/* 171 */         Exception ex = this.responseConsumer.getException();
/* 172 */         if (ex == null) {
/* 173 */           this.resultFuture.completed(result);
/*     */         } else {
/* 175 */           this.resultFuture.failed(ex);
/*     */         } 
/*     */       } finally {
/* 178 */         close();
/*     */       } 
/*     */     } else {
/* 181 */       NHttpClientConnection localConn = this.managedConn.get();
/* 182 */       if (localConn != null && !localConn.isOpen()) {
/* 183 */         releaseConnection();
/* 184 */         localConn = null;
/*     */       } 
/* 186 */       if (localConn != null) {
/* 187 */         localConn.requestOutput();
/*     */       } else {
/* 189 */         requestConnection();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void inputTerminated() {
/* 195 */     if (!this.completed.get()) {
/* 196 */       requestConnection();
/*     */     } else {
/* 198 */       close();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void releaseConnection() {
/* 203 */     NHttpClientConnection localConn = this.managedConn.getAndSet(null);
/* 204 */     if (localConn != null) {
/* 205 */       if (this.log.isDebugEnabled()) {
/* 206 */         this.log.debug("[exchange: " + this.state.getId() + "] releasing connection");
/*     */       }
/* 208 */       localConn.getContext().removeAttribute("http.nio.exchange-handler");
/* 209 */       if (this.state.isReusable()) {
/* 210 */         this.connmgr.releaseConnection(localConn, this.localContext.getUserToken(), this.state.getValidDuration(), TimeUnit.MILLISECONDS);
/*     */       } else {
/*     */ 
/*     */         
/*     */         try {
/* 215 */           localConn.close();
/* 216 */           if (this.log.isDebugEnabled()) {
/* 217 */             this.log.debug("[exchange: " + this.state.getId() + "] connection discarded");
/*     */           }
/* 219 */         } catch (IOException ex) {
/* 220 */           if (this.log.isDebugEnabled()) {
/* 221 */             this.log.debug(ex.getMessage(), ex);
/*     */           }
/*     */         } finally {
/* 224 */           this.connmgr.releaseConnection(localConn, null, 0L, TimeUnit.MILLISECONDS);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void abortConnection() {
/* 231 */     discardConnection();
/*     */   }
/*     */   
/*     */   private void discardConnection() {
/* 235 */     NHttpClientConnection localConn = this.managedConn.getAndSet(null);
/* 236 */     if (localConn != null) {
/*     */       try {
/* 238 */         localConn.shutdown();
/* 239 */         if (this.log.isDebugEnabled()) {
/* 240 */           this.log.debug("[exchange: " + this.state.getId() + "] connection aborted");
/*     */         }
/* 242 */       } catch (IOException ex) {
/* 243 */         if (this.log.isDebugEnabled()) {
/* 244 */           this.log.debug(ex.getMessage(), ex);
/*     */         }
/*     */       } finally {
/* 247 */         this.connmgr.releaseConnection(localConn, null, 0L, TimeUnit.MILLISECONDS);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void failed(Exception ex) {
/*     */     try {
/* 254 */       this.requestProducer.failed(ex);
/* 255 */       this.responseConsumer.failed(ex);
/*     */     } finally {
/*     */       try {
/* 258 */         this.resultFuture.failed(ex);
/*     */       } finally {
/* 260 */         close();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean cancel() {
/* 266 */     if (this.log.isDebugEnabled()) {
/* 267 */       this.log.debug("[exchange: " + this.state.getId() + "] Cancelled");
/*     */     }
/*     */     try {
/* 270 */       boolean cancelled = this.responseConsumer.cancel();
/*     */       
/* 272 */       T result = (T)this.responseConsumer.getResult();
/* 273 */       Exception ex = this.responseConsumer.getException();
/* 274 */       if (ex != null) {
/* 275 */         this.resultFuture.failed(ex);
/* 276 */       } else if (result != null) {
/* 277 */         this.resultFuture.completed(result);
/*     */       } else {
/* 279 */         this.resultFuture.cancel();
/*     */       } 
/* 281 */       return cancelled;
/* 282 */     } catch (RuntimeException runex) {
/* 283 */       this.resultFuture.failed(runex);
/* 284 */       throw runex;
/*     */     } finally {
/* 286 */       close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void connectionAllocated(NHttpClientConnection managedConn) {
/*     */     try {
/* 292 */       if (this.log.isDebugEnabled()) {
/* 293 */         this.log.debug("[exchange: " + this.state.getId() + "] Connection allocated: " + managedConn);
/*     */       }
/* 295 */       this.managedConn.set(managedConn);
/*     */       
/* 297 */       if (this.closed.get()) {
/* 298 */         releaseConnection();
/*     */         
/*     */         return;
/*     */       } 
/* 302 */       managedConn.getContext().setAttribute("http.nio.exchange-handler", this);
/* 303 */       managedConn.requestOutput();
/* 304 */       if (!managedConn.isOpen()) {
/* 305 */         failed((Exception)new ConnectionClosedException("Connection closed"));
/*     */       }
/* 307 */     } catch (RuntimeException runex) {
/* 308 */       failed(runex);
/* 309 */       throw runex;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void connectionRequestFailed(Exception ex) {
/* 314 */     if (this.log.isDebugEnabled()) {
/* 315 */       this.log.debug("[exchange: " + this.state.getId() + "] connection request failed");
/*     */     }
/*     */     try {
/* 318 */       this.resultFuture.failed(ex);
/*     */     } finally {
/* 320 */       close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void connectionRequestCancelled() {
/* 325 */     if (this.log.isDebugEnabled()) {
/* 326 */       this.log.debug("[exchange: " + this.state.getId() + "] Connection request cancelled");
/*     */     }
/*     */     try {
/* 329 */       this.resultFuture.cancel();
/*     */     } finally {
/* 331 */       close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void requestConnection() {
/* 336 */     if (this.log.isDebugEnabled()) {
/* 337 */       this.log.debug("[exchange: " + this.state.getId() + "] Request connection for " + this.state.getRoute());
/*     */     }
/*     */ 
/*     */     
/* 341 */     discardConnection();
/*     */     
/* 343 */     this.state.setValidDuration(0L);
/* 344 */     this.state.setNonReusable();
/* 345 */     this.state.setRouteEstablished(false);
/* 346 */     this.state.setRouteTracker(null);
/*     */     
/* 348 */     HttpRoute route = this.state.getRoute();
/* 349 */     Object userToken = this.localContext.getUserToken();
/* 350 */     RequestConfig config = this.localContext.getRequestConfig();
/* 351 */     this.connmgr.requestConnection(route, userToken, config.getConnectTimeout(), config.getConnectionRequestTimeout(), TimeUnit.MILLISECONDS, new FutureCallback<NHttpClientConnection>()
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void completed(NHttpClientConnection managedConn)
/*     */           {
/* 360 */             DefaultClientExchangeHandlerImpl.this.connectionAllocated(managedConn);
/*     */           }
/*     */           
/*     */           public void failed(Exception ex) {
/* 364 */             DefaultClientExchangeHandlerImpl.this.connectionRequestFailed(ex);
/*     */           }
/*     */           
/*     */           public void cancelled() {
/* 368 */             DefaultClientExchangeHandlerImpl.this.connectionRequestCancelled();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public NHttpClientConnection getConnection() {
/* 375 */     return this.managedConn.get();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/client/DefaultClientExchangeHandlerImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */