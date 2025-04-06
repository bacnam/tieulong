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
/*    */ 
/*    */ public interface MessageHandler<E>
/*    */ {
/* 38 */   public static final MessageHandler<Object> NOOP = new MessageHandler<Object>() {
/*    */       public void handleMessage(IoSession session, Object message) {}
/*    */     };
/*    */   
/*    */   void handleMessage(IoSession paramIoSession, E paramE) throws Exception;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/handler/demux/MessageHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */