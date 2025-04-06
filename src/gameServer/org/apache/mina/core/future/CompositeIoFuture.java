/*    */ package org.apache.mina.core.future;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CompositeIoFuture<E extends IoFuture>
/*    */   extends DefaultIoFuture
/*    */ {
/* 39 */   private final NotifyingListener listener = new NotifyingListener();
/*    */   
/* 41 */   private final AtomicInteger unnotified = new AtomicInteger();
/*    */   
/*    */   private volatile boolean constructionFinished;
/*    */   
/*    */   public CompositeIoFuture(Iterable<E> children) {
/* 46 */     super(null);
/*    */     
/* 48 */     for (IoFuture ioFuture : children) {
/* 49 */       ioFuture.addListener(this.listener);
/* 50 */       this.unnotified.incrementAndGet();
/*    */     } 
/*    */     
/* 53 */     this.constructionFinished = true;
/* 54 */     if (this.unnotified.get() == 0)
/* 55 */       setValue(Boolean.valueOf(true)); 
/*    */   }
/*    */   
/*    */   private class NotifyingListener
/*    */     implements IoFutureListener<IoFuture> {
/*    */     public void operationComplete(IoFuture future) {
/* 61 */       if (CompositeIoFuture.this.unnotified.decrementAndGet() == 0 && CompositeIoFuture.this.constructionFinished)
/* 62 */         CompositeIoFuture.this.setValue(Boolean.valueOf(true)); 
/*    */     }
/*    */     
/*    */     private NotifyingListener() {}
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/future/CompositeIoFuture.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */