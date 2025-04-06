/*    */ package org.apache.mina.core.future;
/*    */ 
/*    */ import java.util.EventListener;
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
/*    */ public interface IoFutureListener<F extends IoFuture>
/*    */   extends EventListener
/*    */ {
/* 37 */   public static final IoFutureListener<IoFuture> CLOSE = new IoFutureListener<IoFuture>() {
/*    */       public void operationComplete(IoFuture future) {
/* 39 */         future.getSession().close(true);
/*    */       }
/*    */     };
/*    */   
/*    */   void operationComplete(F paramF);
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/future/IoFutureListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */