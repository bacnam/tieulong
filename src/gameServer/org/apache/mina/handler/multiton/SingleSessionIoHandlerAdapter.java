/*    */ package org.apache.mina.handler.multiton;
/*    */ 
/*    */ import org.apache.mina.core.session.IdleStatus;
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
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class SingleSessionIoHandlerAdapter
/*    */   implements SingleSessionIoHandler
/*    */ {
/*    */   private final IoSession session;
/*    */   
/*    */   public SingleSessionIoHandlerAdapter(IoSession session) {
/* 46 */     if (session == null) {
/* 47 */       throw new IllegalArgumentException("session");
/*    */     }
/* 49 */     this.session = session;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected IoSession getSession() {
/* 58 */     return this.session;
/*    */   }
/*    */   
/*    */   public void exceptionCaught(Throwable th) throws Exception {}
/*    */   
/*    */   public void inputClosed(IoSession session) {}
/*    */   
/*    */   public void messageReceived(Object message) throws Exception {}
/*    */   
/*    */   public void messageSent(Object message) throws Exception {}
/*    */   
/*    */   public void sessionClosed() throws Exception {}
/*    */   
/*    */   public void sessionCreated() throws Exception {}
/*    */   
/*    */   public void sessionIdle(IdleStatus status) throws Exception {}
/*    */   
/*    */   public void sessionOpened() throws Exception {}
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/handler/multiton/SingleSessionIoHandlerAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */