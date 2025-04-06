/*    */ package org.apache.http.nio.conn;
/*    */ 
/*    */ import org.apache.http.HttpHost;
/*    */ import org.apache.http.nio.reactor.IOSession;
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
/*    */ public class NoopIOSessionStrategy
/*    */   implements SchemeIOSessionStrategy
/*    */ {
/* 40 */   public static final NoopIOSessionStrategy INSTANCE = new NoopIOSessionStrategy();
/*    */   
/*    */   public IOSession upgrade(HttpHost host, IOSession iosession) {
/* 43 */     return iosession;
/*    */   }
/*    */   
/*    */   public boolean isLayeringRequired() {
/* 47 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/conn/NoopIOSessionStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */