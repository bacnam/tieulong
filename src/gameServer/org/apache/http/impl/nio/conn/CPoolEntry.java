/*    */ package org.apache.http.impl.nio.conn;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Date;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.http.annotation.ThreadSafe;
/*    */ import org.apache.http.conn.routing.HttpRoute;
/*    */ import org.apache.http.nio.conn.ManagedNHttpClientConnection;
/*    */ import org.apache.http.pool.PoolEntry;
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
/*    */ @ThreadSafe
/*    */ class CPoolEntry
/*    */   extends PoolEntry<HttpRoute, ManagedNHttpClientConnection>
/*    */ {
/*    */   private final Log log;
/*    */   private volatile boolean routeComplete;
/*    */   
/*    */   public CPoolEntry(Log log, String id, HttpRoute route, ManagedNHttpClientConnection conn, long timeToLive, TimeUnit tunit) {
/* 51 */     super(id, route, conn, timeToLive, tunit);
/* 52 */     this.log = log;
/*    */   }
/*    */   
/*    */   public boolean isRouteComplete() {
/* 56 */     return this.routeComplete;
/*    */   }
/*    */   
/*    */   public void markRouteComplete() {
/* 60 */     this.routeComplete = true;
/*    */   }
/*    */   
/*    */   public void closeConnection() throws IOException {
/* 64 */     ManagedNHttpClientConnection conn = (ManagedNHttpClientConnection)getConnection();
/* 65 */     conn.close();
/*    */   }
/*    */   
/*    */   public void shutdownConnection() throws IOException {
/* 69 */     ManagedNHttpClientConnection conn = (ManagedNHttpClientConnection)getConnection();
/* 70 */     conn.shutdown();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isExpired(long now) {
/* 75 */     boolean expired = super.isExpired(now);
/* 76 */     if (expired && this.log.isDebugEnabled()) {
/* 77 */       this.log.debug("Connection " + this + " expired @ " + new Date(getExpiry()));
/*    */     }
/* 79 */     return expired;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isClosed() {
/* 84 */     ManagedNHttpClientConnection conn = (ManagedNHttpClientConnection)getConnection();
/* 85 */     return !conn.isOpen();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/*    */     try {
/* 91 */       closeConnection();
/* 92 */     } catch (IOException ex) {
/* 93 */       this.log.debug("I/O error closing connection", ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/conn/CPoolEntry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */