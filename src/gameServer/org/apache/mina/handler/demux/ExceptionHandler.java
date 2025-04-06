/*    */ package org.apache.mina.handler.demux;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ExceptionHandler<E extends Throwable>
/*    */ {
/* 37 */   public static final ExceptionHandler<Throwable> NOOP = new ExceptionHandler<Throwable>()
/*    */     {
/*    */       public void exceptionCaught(IoSession session, Throwable cause) {}
/*    */     };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   public static final ExceptionHandler<Throwable> CLOSE = new ExceptionHandler<Throwable>() {
/*    */       public void exceptionCaught(IoSession session, Throwable cause) {
/* 50 */         session.close(true);
/*    */       }
/*    */     };
/*    */   
/*    */   void exceptionCaught(IoSession paramIoSession, E paramE) throws Exception;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/handler/demux/ExceptionHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */