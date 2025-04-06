/*    */ package org.apache.mina.handler.chain;
/*    */ 
/*    */ import org.apache.mina.core.service.IoHandlerAdapter;
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
/*    */ public class ChainedIoHandler
/*    */   extends IoHandlerAdapter
/*    */ {
/*    */   private final IoHandlerChain chain;
/*    */   
/*    */   public ChainedIoHandler() {
/* 39 */     this.chain = new IoHandlerChain();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ChainedIoHandler(IoHandlerChain chain) {
/* 49 */     if (chain == null) {
/* 50 */       throw new IllegalArgumentException("chain");
/*    */     }
/* 52 */     this.chain = chain;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public IoHandlerChain getChain() {
/* 60 */     return this.chain;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void messageReceived(IoSession session, Object message) throws Exception {
/* 70 */     this.chain.execute(null, session, message);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/handler/chain/ChainedIoHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */