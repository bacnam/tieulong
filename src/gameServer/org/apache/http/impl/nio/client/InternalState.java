/*     */ package org.apache.http.impl.nio.client;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.client.methods.HttpRequestWrapper;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.routing.RouteTracker;
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
/*     */ class InternalState
/*     */ {
/*  43 */   private static final AtomicLong COUNTER = new AtomicLong(1L);
/*     */   
/*     */   private final long id;
/*     */   
/*     */   private final HttpAsyncRequestProducer requestProducer;
/*     */   
/*     */   private final HttpAsyncResponseConsumer<?> responseConsumer;
/*     */   
/*     */   private final HttpClientContext localContext;
/*     */   
/*     */   private boolean routeEstablished;
/*     */   
/*     */   private RouteTracker routeTracker;
/*     */   
/*     */   private boolean reusable;
/*     */   
/*     */   private long validDuration;
/*     */   private HttpRoute route;
/*     */   private HttpRequestWrapper mainRequest;
/*     */   private HttpResponse finalResponse;
/*     */   private HttpRequestWrapper currentRequest;
/*     */   private HttpResponse currentResponse;
/*     */   private ByteBuffer tmpbuf;
/*     */   private boolean requestContentProduced;
/*     */   private int execCount;
/*     */   private int redirectCount;
/*     */   private HttpUriRequest redirect;
/*     */   
/*     */   public InternalState(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<?> responseConsumer, HttpClientContext localContext) {
/*  72 */     this.id = COUNTER.getAndIncrement();
/*  73 */     this.requestProducer = requestProducer;
/*  74 */     this.responseConsumer = responseConsumer;
/*  75 */     this.localContext = localContext;
/*     */   }
/*     */   
/*     */   public long getId() {
/*  79 */     return this.id;
/*     */   }
/*     */   
/*     */   public HttpAsyncRequestProducer getRequestProducer() {
/*  83 */     return this.requestProducer;
/*     */   }
/*     */   
/*     */   public HttpAsyncResponseConsumer<?> getResponseConsumer() {
/*  87 */     return this.responseConsumer;
/*     */   }
/*     */   
/*     */   public HttpClientContext getLocalContext() {
/*  91 */     return this.localContext;
/*     */   }
/*     */   
/*     */   public boolean isRouteEstablished() {
/*  95 */     return this.routeEstablished;
/*     */   }
/*     */   
/*     */   public void setRouteEstablished(boolean b) {
/*  99 */     this.routeEstablished = b;
/*     */   }
/*     */   
/*     */   public RouteTracker getRouteTracker() {
/* 103 */     return this.routeTracker;
/*     */   }
/*     */   
/*     */   public void setRouteTracker(RouteTracker routeTracker) {
/* 107 */     this.routeTracker = routeTracker;
/*     */   }
/*     */   
/*     */   public boolean isReusable() {
/* 111 */     return this.reusable;
/*     */   }
/*     */   
/*     */   public void setReusable() {
/* 115 */     this.reusable = true;
/*     */   }
/*     */   
/*     */   public void setNonReusable() {
/* 119 */     this.reusable = false;
/*     */   }
/*     */   
/*     */   public long getValidDuration() {
/* 123 */     return this.validDuration;
/*     */   }
/*     */   
/*     */   public void setValidDuration(long validDuration) {
/* 127 */     this.validDuration = validDuration;
/*     */   }
/*     */   
/*     */   public HttpRoute getRoute() {
/* 131 */     return this.route;
/*     */   }
/*     */   
/*     */   public void setRoute(HttpRoute route) {
/* 135 */     this.route = route;
/*     */   }
/*     */   
/*     */   public HttpRequestWrapper getMainRequest() {
/* 139 */     return this.mainRequest;
/*     */   }
/*     */   
/*     */   public void setMainRequest(HttpRequestWrapper mainRequest) {
/* 143 */     this.mainRequest = mainRequest;
/*     */   }
/*     */   
/*     */   public HttpResponse getFinalResponse() {
/* 147 */     return this.finalResponse;
/*     */   }
/*     */   
/*     */   public void setFinalResponse(HttpResponse finalResponse) {
/* 151 */     this.finalResponse = finalResponse;
/*     */   }
/*     */   
/*     */   public HttpRequestWrapper getCurrentRequest() {
/* 155 */     return this.currentRequest;
/*     */   }
/*     */   
/*     */   public void setCurrentRequest(HttpRequestWrapper currentRequest) {
/* 159 */     this.currentRequest = currentRequest;
/*     */   }
/*     */   
/*     */   public HttpResponse getCurrentResponse() {
/* 163 */     return this.currentResponse;
/*     */   }
/*     */   
/*     */   public void setCurrentResponse(HttpResponse currentResponse) {
/* 167 */     this.currentResponse = currentResponse;
/*     */   }
/*     */   
/*     */   public ByteBuffer getTmpbuf() {
/* 171 */     if (this.tmpbuf == null) {
/* 172 */       this.tmpbuf = ByteBuffer.allocate(4096);
/*     */     }
/* 174 */     return this.tmpbuf;
/*     */   }
/*     */   
/*     */   public boolean isRequestContentProduced() {
/* 178 */     return this.requestContentProduced;
/*     */   }
/*     */   
/*     */   public void setRequestContentProduced() {
/* 182 */     this.requestContentProduced = true;
/*     */   }
/*     */   
/*     */   public int getExecCount() {
/* 186 */     return this.execCount;
/*     */   }
/*     */   
/*     */   public void incrementExecCount() {
/* 190 */     this.execCount++;
/*     */   }
/*     */   
/*     */   public int getRedirectCount() {
/* 194 */     return this.redirectCount;
/*     */   }
/*     */   
/*     */   public void incrementRedirectCount() {
/* 198 */     this.redirectCount++;
/*     */   }
/*     */   
/*     */   public HttpUriRequest getRedirect() {
/* 202 */     return this.redirect;
/*     */   }
/*     */   
/*     */   public void setRedirect(HttpUriRequest redirect) {
/* 206 */     this.redirect = redirect;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 211 */     return Long.toString(this.id);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/client/InternalState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */