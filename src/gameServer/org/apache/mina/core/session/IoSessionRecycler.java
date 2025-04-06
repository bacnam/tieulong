/*    */ package org.apache.mina.core.session;
/*    */ 
/*    */ import java.net.SocketAddress;
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
/*    */ public interface IoSessionRecycler
/*    */ {
/* 39 */   public static final IoSessionRecycler NOOP = new IoSessionRecycler()
/*    */     {
/*    */       public void put(IoSession session) {}
/*    */ 
/*    */       
/*    */       public IoSession recycle(SocketAddress remoteAddress) {
/* 45 */         return null;
/*    */       }
/*    */       
/*    */       public void remove(IoSession session) {}
/*    */     };
/*    */   
/*    */   void put(IoSession paramIoSession);
/*    */   
/*    */   IoSession recycle(SocketAddress paramSocketAddress);
/*    */   
/*    */   void remove(IoSession paramIoSession);
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/session/IoSessionRecycler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */