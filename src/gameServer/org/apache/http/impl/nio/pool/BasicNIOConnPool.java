/*     */ package org.apache.http.impl.nio.pool;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.concurrent.FutureCallback;
/*     */ import org.apache.http.config.ConnectionConfig;
/*     */ import org.apache.http.nio.NHttpClientConnection;
/*     */ import org.apache.http.nio.pool.AbstractNIOConnPool;
/*     */ import org.apache.http.nio.pool.NIOConnFactory;
/*     */ import org.apache.http.nio.pool.SocketAddressResolver;
/*     */ import org.apache.http.nio.reactor.ConnectingIOReactor;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.pool.PoolEntry;
/*     */ import org.apache.http.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ public class BasicNIOConnPool
/*     */   extends AbstractNIOConnPool<HttpHost, NHttpClientConnection, BasicNIOPoolEntry>
/*     */ {
/*  62 */   private static final AtomicLong COUNTER = new AtomicLong();
/*     */   
/*     */   private final int connectTimeout;
/*     */   
/*     */   static class BasicAddressResolver
/*     */     implements SocketAddressResolver<HttpHost>
/*     */   {
/*     */     public SocketAddress resolveLocalAddress(HttpHost host) {
/*  70 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public SocketAddress resolveRemoteAddress(HttpHost host) {
/*  75 */       String hostname = host.getHostName();
/*  76 */       int port = host.getPort();
/*  77 */       if (port == -1) {
/*  78 */         if (host.getSchemeName().equalsIgnoreCase("http")) {
/*  79 */           port = 80;
/*  80 */         } else if (host.getSchemeName().equalsIgnoreCase("https")) {
/*  81 */           port = 443;
/*     */         } 
/*     */       }
/*  84 */       return new InetSocketAddress(hostname, port);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public BasicNIOConnPool(ConnectingIOReactor ioreactor, NIOConnFactory<HttpHost, NHttpClientConnection> connFactory, HttpParams params) {
/*  97 */     super(ioreactor, connFactory, 2, 20);
/*  98 */     Args.notNull(params, "HTTP parameters");
/*  99 */     this.connectTimeout = params.getIntParameter("http.connection.timeout", 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public BasicNIOConnPool(ConnectingIOReactor ioreactor, HttpParams params) {
/* 109 */     this(ioreactor, new BasicNIOConnFactory(params), params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicNIOConnPool(ConnectingIOReactor ioreactor, NIOConnFactory<HttpHost, NHttpClientConnection> connFactory, int connectTimeout) {
/* 119 */     super(ioreactor, connFactory, new BasicAddressResolver(), 2, 20);
/* 120 */     this.connectTimeout = connectTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicNIOConnPool(ConnectingIOReactor ioreactor, int connectTimeout, ConnectionConfig config) {
/* 130 */     this(ioreactor, new BasicNIOConnFactory(config), connectTimeout);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicNIOConnPool(ConnectingIOReactor ioreactor, ConnectionConfig config) {
/* 139 */     this(ioreactor, new BasicNIOConnFactory(config), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicNIOConnPool(ConnectingIOReactor ioreactor) {
/* 146 */     this(ioreactor, new BasicNIOConnFactory(ConnectionConfig.DEFAULT), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected SocketAddress resolveRemoteAddress(HttpHost host) {
/* 155 */     return new InetSocketAddress(host.getHostName(), host.getPort());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected SocketAddress resolveLocalAddress(HttpHost host) {
/* 164 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BasicNIOPoolEntry createEntry(HttpHost host, NHttpClientConnection conn) {
/* 169 */     BasicNIOPoolEntry entry = new BasicNIOPoolEntry(Long.toString(COUNTER.getAndIncrement()), host, conn);
/*     */     
/* 171 */     entry.setSocketTimeout(conn.getSocketTimeout());
/* 172 */     return entry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<BasicNIOPoolEntry> lease(HttpHost route, Object state, FutureCallback<BasicNIOPoolEntry> callback) {
/* 180 */     return lease(route, state, this.connectTimeout, TimeUnit.MILLISECONDS, callback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<BasicNIOPoolEntry> lease(HttpHost route, Object state) {
/* 188 */     return lease(route, state, this.connectTimeout, TimeUnit.MILLISECONDS, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onLease(BasicNIOPoolEntry entry) {
/* 194 */     NHttpClientConnection conn = (NHttpClientConnection)entry.getConnection();
/* 195 */     conn.setSocketTimeout(entry.getSocketTimeout());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onRelease(BasicNIOPoolEntry entry) {
/* 200 */     NHttpClientConnection conn = (NHttpClientConnection)entry.getConnection();
/* 201 */     entry.setSocketTimeout(conn.getSocketTimeout());
/* 202 */     conn.setSocketTimeout(0);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/pool/BasicNIOConnPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */