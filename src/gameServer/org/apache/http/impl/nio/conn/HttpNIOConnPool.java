/*    */ package org.apache.http.impl.nio.conn;
/*    */ 
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.SocketAddress;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.http.HttpHost;
/*    */ import org.apache.http.conn.routing.HttpRoute;
/*    */ import org.apache.http.nio.conn.scheme.AsyncScheme;
/*    */ import org.apache.http.nio.conn.scheme.AsyncSchemeRegistry;
/*    */ import org.apache.http.nio.pool.AbstractNIOConnPool;
/*    */ import org.apache.http.nio.reactor.ConnectingIOReactor;
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
/*    */ @Deprecated
/*    */ class HttpNIOConnPool
/*    */   extends AbstractNIOConnPool<HttpRoute, IOSession, HttpPoolEntry>
/*    */ {
/* 46 */   private static final AtomicLong COUNTER = new AtomicLong(1L);
/*    */   
/*    */   private final Log log;
/*    */   
/*    */   private final AsyncSchemeRegistry schemeRegistry;
/*    */   
/*    */   private final long connTimeToLive;
/*    */   
/*    */   private final TimeUnit tunit;
/*    */ 
/*    */   
/*    */   HttpNIOConnPool(Log log, ConnectingIOReactor ioreactor, AsyncSchemeRegistry schemeRegistry, long connTimeToLive, TimeUnit tunit) {
/* 58 */     super(ioreactor, new HttpNIOConnPoolFactory(), 2, 20);
/* 59 */     this.log = log;
/* 60 */     this.schemeRegistry = schemeRegistry;
/* 61 */     this.connTimeToLive = connTimeToLive;
/* 62 */     this.tunit = tunit;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SocketAddress resolveLocalAddress(HttpRoute route) {
/* 67 */     return new InetSocketAddress(route.getLocalAddress(), 0);
/*    */   }
/*    */ 
/*    */   
/*    */   protected SocketAddress resolveRemoteAddress(HttpRoute route) {
/* 72 */     HttpHost firsthop = route.getProxyHost();
/* 73 */     if (firsthop == null) {
/* 74 */       firsthop = route.getTargetHost();
/*    */     }
/* 76 */     String hostname = firsthop.getHostName();
/* 77 */     int port = firsthop.getPort();
/* 78 */     if (port < 0) {
/* 79 */       AsyncScheme scheme = this.schemeRegistry.getScheme(firsthop);
/* 80 */       port = scheme.resolvePort(port);
/*    */     } 
/* 82 */     return new InetSocketAddress(hostname, port);
/*    */   }
/*    */ 
/*    */   
/*    */   protected HttpPoolEntry createEntry(HttpRoute route, IOSession session) {
/* 87 */     String id = Long.toString(COUNTER.getAndIncrement());
/* 88 */     return new HttpPoolEntry(this.log, id, route, session, this.connTimeToLive, this.tunit);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/conn/HttpNIOConnPool.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */