/*    */ package org.apache.http.impl.nio.conn;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.apache.http.annotation.ThreadSafe;
/*    */ import org.apache.http.conn.routing.HttpRoute;
/*    */ import org.apache.http.nio.conn.ManagedNHttpClientConnection;
/*    */ import org.apache.http.nio.pool.AbstractNIOConnPool;
/*    */ import org.apache.http.nio.pool.NIOConnFactory;
/*    */ import org.apache.http.nio.pool.SocketAddressResolver;
/*    */ import org.apache.http.nio.reactor.ConnectingIOReactor;
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
/*    */ @ThreadSafe
/*    */ class CPool
/*    */   extends AbstractNIOConnPool<HttpRoute, ManagedNHttpClientConnection, CPoolEntry>
/*    */ {
/* 44 */   private final Log log = LogFactory.getLog(CPool.class);
/*    */ 
/*    */   
/*    */   private final long timeToLive;
/*    */ 
/*    */   
/*    */   private final TimeUnit tunit;
/*    */ 
/*    */ 
/*    */   
/*    */   public CPool(ConnectingIOReactor ioreactor, NIOConnFactory<HttpRoute, ManagedNHttpClientConnection> connFactory, SocketAddressResolver<HttpRoute> addressResolver, int defaultMaxPerRoute, int maxTotal, long timeToLive, TimeUnit tunit) {
/* 55 */     super(ioreactor, connFactory, addressResolver, defaultMaxPerRoute, maxTotal);
/* 56 */     this.timeToLive = timeToLive;
/* 57 */     this.tunit = tunit;
/*    */   }
/*    */ 
/*    */   
/*    */   protected CPoolEntry createEntry(HttpRoute route, ManagedNHttpClientConnection conn) {
/* 62 */     return new CPoolEntry(this.log, conn.getId(), route, conn, this.timeToLive, this.tunit);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/conn/CPool.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */