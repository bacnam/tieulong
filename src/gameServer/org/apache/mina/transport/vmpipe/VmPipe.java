/*    */ package org.apache.mina.transport.vmpipe;
/*    */ 
/*    */ import org.apache.mina.core.service.IoHandler;
/*    */ import org.apache.mina.core.service.IoServiceListenerSupport;
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
/*    */ class VmPipe
/*    */ {
/*    */   private final VmPipeAcceptor acceptor;
/*    */   private final VmPipeAddress address;
/*    */   private final IoHandler handler;
/*    */   private final IoServiceListenerSupport listeners;
/*    */   
/*    */   VmPipe(VmPipeAcceptor acceptor, VmPipeAddress address, IoHandler handler, IoServiceListenerSupport listeners) {
/* 40 */     this.acceptor = acceptor;
/* 41 */     this.address = address;
/* 42 */     this.handler = handler;
/* 43 */     this.listeners = listeners;
/*    */   }
/*    */   
/*    */   public VmPipeAcceptor getAcceptor() {
/* 47 */     return this.acceptor;
/*    */   }
/*    */   
/*    */   public VmPipeAddress getAddress() {
/* 51 */     return this.address;
/*    */   }
/*    */   
/*    */   public IoHandler getHandler() {
/* 55 */     return this.handler;
/*    */   }
/*    */   
/*    */   public IoServiceListenerSupport getListeners() {
/* 59 */     return this.listeners;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/transport/vmpipe/VmPipe.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */