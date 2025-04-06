/*    */ package org.apache.http.impl.nio.conn;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.conn.routing.HttpRoute;
/*    */ import org.apache.http.nio.pool.NIOConnFactory;
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
/*    */ @Deprecated
/*    */ class HttpNIOConnPoolFactory
/*    */   implements NIOConnFactory<HttpRoute, IOSession>
/*    */ {
/*    */   public IOSession create(HttpRoute route, IOSession session) throws IOException {
/* 39 */     return session;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/conn/HttpNIOConnPoolFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */