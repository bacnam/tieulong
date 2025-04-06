/*     */ package org.apache.http.nio.pool;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.concurrent.BasicFuture;
/*     */ import org.apache.http.pool.PoolEntry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ class LeaseRequest<T, C, E extends PoolEntry<T, C>>
/*     */ {
/*     */   private final T route;
/*     */   private final Object state;
/*     */   private final long connectTimeout;
/*     */   private final long deadline;
/*     */   private final BasicFuture<E> future;
/*     */   private final AtomicBoolean completed;
/*     */   private volatile E result;
/*     */   private volatile Exception ex;
/*     */   
/*     */   public LeaseRequest(T route, Object state, long connectTimeout, long leaseTimeout, BasicFuture<E> future) {
/*  62 */     this.route = route;
/*  63 */     this.state = state;
/*  64 */     this.connectTimeout = connectTimeout;
/*  65 */     this.deadline = (leaseTimeout > 0L) ? (System.currentTimeMillis() + leaseTimeout) : Long.MAX_VALUE;
/*     */     
/*  67 */     this.future = future;
/*  68 */     this.completed = new AtomicBoolean(false);
/*     */   }
/*     */   
/*     */   public T getRoute() {
/*  72 */     return this.route;
/*     */   }
/*     */   
/*     */   public Object getState() {
/*  76 */     return this.state;
/*     */   }
/*     */   
/*     */   public long getConnectTimeout() {
/*  80 */     return this.connectTimeout;
/*     */   }
/*     */   
/*     */   public long getDeadline() {
/*  84 */     return this.deadline;
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/*  88 */     return this.completed.get();
/*     */   }
/*     */   
/*     */   public void failed(Exception ex) {
/*  92 */     if (this.completed.compareAndSet(false, true)) {
/*  93 */       this.ex = ex;
/*     */     }
/*     */   }
/*     */   
/*     */   public void completed(E result) {
/*  98 */     if (this.completed.compareAndSet(false, true)) {
/*  99 */       this.result = result;
/*     */     }
/*     */   }
/*     */   
/*     */   public BasicFuture<E> getFuture() {
/* 104 */     return this.future;
/*     */   }
/*     */   
/*     */   public E getResult() {
/* 108 */     return this.result;
/*     */   }
/*     */   
/*     */   public Exception getException() {
/* 112 */     return this.ex;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 117 */     StringBuilder buffer = new StringBuilder();
/* 118 */     buffer.append("[");
/* 119 */     buffer.append(this.route);
/* 120 */     buffer.append("][");
/* 121 */     buffer.append(this.state);
/* 122 */     buffer.append("]");
/* 123 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/pool/LeaseRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */