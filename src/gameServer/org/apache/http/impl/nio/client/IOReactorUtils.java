/*    */ package org.apache.http.impl.nio.client;
/*    */ 
/*    */ import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
/*    */ import org.apache.http.impl.nio.reactor.IOReactorConfig;
/*    */ import org.apache.http.nio.reactor.ConnectingIOReactor;
/*    */ import org.apache.http.nio.reactor.IOReactorException;
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
/*    */ final class IOReactorUtils
/*    */ {
/*    */   public static ConnectingIOReactor create(IOReactorConfig config) {
/*    */     try {
/* 41 */       return (ConnectingIOReactor)new DefaultConnectingIOReactor(config);
/* 42 */     } catch (IOReactorException ex) {
/* 43 */       throw new IllegalStateException(ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/client/IOReactorUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */