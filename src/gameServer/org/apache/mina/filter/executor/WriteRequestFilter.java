/*     */ package org.apache.mina.filter.executor;
/*     */ 
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.filterchain.IoFilterAdapter;
/*     */ import org.apache.mina.core.future.IoFuture;
/*     */ import org.apache.mina.core.future.IoFutureListener;
/*     */ import org.apache.mina.core.future.WriteFuture;
/*     */ import org.apache.mina.core.session.IoEvent;
/*     */ import org.apache.mina.core.session.IoEventType;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.write.WriteRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WriteRequestFilter
/*     */   extends IoFilterAdapter
/*     */ {
/*     */   private final IoEventQueueHandler queueHandler;
/*     */   
/*     */   public WriteRequestFilter() {
/*  71 */     this(new IoEventQueueThrottle());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WriteRequestFilter(IoEventQueueHandler queueHandler) {
/*  78 */     if (queueHandler == null) {
/*  79 */       throw new IllegalArgumentException("queueHandler");
/*     */     }
/*  81 */     this.queueHandler = queueHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoEventQueueHandler getQueueHandler() {
/*  89 */     return this.queueHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void filterWrite(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
/*  95 */     final IoEvent e = new IoEvent(IoEventType.WRITE, session, writeRequest);
/*     */     
/*  97 */     if (this.queueHandler.accept(this, e)) {
/*  98 */       nextFilter.filterWrite(session, writeRequest);
/*  99 */       WriteFuture writeFuture = writeRequest.getFuture();
/* 100 */       if (writeFuture == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 105 */       this.queueHandler.offered(this, e);
/* 106 */       writeFuture.addListener(new IoFutureListener<WriteFuture>() {
/*     */             public void operationComplete(WriteFuture future) {
/* 108 */               WriteRequestFilter.this.queueHandler.polled(WriteRequestFilter.this, e);
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/executor/WriteRequestFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */