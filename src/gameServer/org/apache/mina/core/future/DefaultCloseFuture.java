/*    */ package org.apache.mina.core.future;
/*    */ 
/*    */ import org.apache.mina.core.session.IoSession;
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
/*    */ public class DefaultCloseFuture
/*    */   extends DefaultIoFuture
/*    */   implements CloseFuture
/*    */ {
/*    */   public DefaultCloseFuture(IoSession session) {
/* 34 */     super(session);
/*    */   }
/*    */   
/*    */   public boolean isClosed() {
/* 38 */     if (isDone()) {
/* 39 */       return ((Boolean)getValue()).booleanValue();
/*    */     }
/* 41 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setClosed() {
/* 46 */     setValue(Boolean.TRUE);
/*    */   }
/*    */ 
/*    */   
/*    */   public CloseFuture await() throws InterruptedException {
/* 51 */     return (CloseFuture)super.await();
/*    */   }
/*    */ 
/*    */   
/*    */   public CloseFuture awaitUninterruptibly() {
/* 56 */     return (CloseFuture)super.awaitUninterruptibly();
/*    */   }
/*    */ 
/*    */   
/*    */   public CloseFuture addListener(IoFutureListener<?> listener) {
/* 61 */     return (CloseFuture)super.addListener(listener);
/*    */   }
/*    */ 
/*    */   
/*    */   public CloseFuture removeListener(IoFutureListener<?> listener) {
/* 66 */     return (CloseFuture)super.removeListener(listener);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/future/DefaultCloseFuture.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */