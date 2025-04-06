/*    */ package org.apache.http.impl.nio.conn;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Date;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.http.conn.routing.HttpRoute;
/*    */ import org.apache.http.conn.routing.RouteTracker;
/*    */ import org.apache.http.nio.conn.ClientAsyncConnection;
/*    */ import org.apache.http.nio.reactor.IOSession;
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
/*    */ @Deprecated
/*    */ class HttpPoolEntry
/*    */   extends PoolEntry<HttpRoute, IOSession>
/*    */ {
/*    */   private final Log log;
/*    */   private final RouteTracker tracker;
/*    */   
/*    */   HttpPoolEntry(Log log, String id, HttpRoute route, IOSession session, long timeToLive, TimeUnit tunit) {
/* 49 */     super(id, route, session, timeToLive, tunit);
/* 50 */     this.log = log;
/* 51 */     this.tracker = new RouteTracker(route);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isExpired(long now) {
/* 56 */     boolean expired = super.isExpired(now);
/* 57 */     if (expired && this.log.isDebugEnabled()) {
/* 58 */       this.log.debug("Connection " + this + " expired @ " + new Date(getExpiry()));
/*    */     }
/* 60 */     return expired;
/*    */   }
/*    */   
/*    */   public ClientAsyncConnection getOperatedClientConnection() {
/* 64 */     IOSession session = (IOSession)getConnection();
/* 65 */     return (ClientAsyncConnection)session.getAttribute("http.connection");
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/*    */     try {
/* 71 */       getOperatedClientConnection().shutdown();
/* 72 */     } catch (IOException ex) {
/* 73 */       if (this.log.isDebugEnabled()) {
/* 74 */         this.log.debug("I/O error shutting down connection", ex);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isClosed() {
/* 81 */     IOSession session = (IOSession)getConnection();
/* 82 */     return session.isClosed();
/*    */   }
/*    */   
/*    */   HttpRoute getPlannedRoute() {
/* 86 */     return (HttpRoute)getRoute();
/*    */   }
/*    */   
/*    */   RouteTracker getTracker() {
/* 90 */     return this.tracker;
/*    */   }
/*    */   
/*    */   HttpRoute getEffectiveRoute() {
/* 94 */     return this.tracker.toRoute();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/conn/HttpPoolEntry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */