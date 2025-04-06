/*    */ package org.apache.mina.core.service;
/*    */ 
/*    */ import org.apache.mina.core.session.IdleStatus;
/*    */ import org.apache.mina.core.session.IoSession;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ public class IoHandlerAdapter
/*    */   implements IoHandler
/*    */ {
/* 35 */   private static final Logger LOGGER = LoggerFactory.getLogger(IoHandlerAdapter.class);
/*    */ 
/*    */ 
/*    */   
/*    */   public void sessionCreated(IoSession session) throws Exception {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void sessionOpened(IoSession session) throws Exception {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void sessionClosed(IoSession session) throws Exception {}
/*    */ 
/*    */   
/*    */   public void sessionIdle(IoSession session, IdleStatus status) throws Exception {}
/*    */ 
/*    */   
/*    */   public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
/* 54 */     if (LOGGER.isWarnEnabled()) {
/* 55 */       LOGGER.warn("EXCEPTION, please implement " + getClass().getName() + ".exceptionCaught() for proper handling:", cause);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void messageReceived(IoSession session, Object message) throws Exception {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void messageSent(IoSession session, Object message) throws Exception {}
/*    */ 
/*    */   
/*    */   public void inputClosed(IoSession session) throws Exception {
/* 69 */     session.close(true);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/service/IoHandlerAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */