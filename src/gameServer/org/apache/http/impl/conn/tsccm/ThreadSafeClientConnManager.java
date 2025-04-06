/*     */ package org.apache.http.impl.conn.tsccm;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.ClientConnectionRequest;
/*     */ import org.apache.http.conn.ConnectionPoolTimeoutException;
/*     */ import org.apache.http.conn.ManagedClientConnection;
/*     */ import org.apache.http.conn.params.ConnPerRoute;
/*     */ import org.apache.http.conn.params.ConnPerRouteBean;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
/*     */ import org.apache.http.impl.conn.DefaultClientConnectionOperator;
/*     */ import org.apache.http.impl.conn.SchemeRegistryFactory;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
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
/*     */ @Deprecated
/*     */ @ThreadSafe
/*     */ public class ThreadSafeClientConnManager
/*     */   implements ClientConnectionManager
/*     */ {
/*     */   private final Log log;
/*     */   protected final SchemeRegistry schemeRegistry;
/*     */   protected final AbstractConnPool connectionPool;
/*     */   protected final ConnPoolByRoute pool;
/*     */   protected final ClientConnectionOperator connOperator;
/*     */   protected final ConnPerRouteBean connPerRoute;
/*     */   
/*     */   public ThreadSafeClientConnManager(SchemeRegistry schreg) {
/*  94 */     this(schreg, -1L, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadSafeClientConnManager() {
/* 101 */     this(SchemeRegistryFactory.createDefault());
/*     */   }
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
/*     */   public ThreadSafeClientConnManager(SchemeRegistry schreg, long connTTL, TimeUnit connTTLTimeUnit) {
/* 115 */     this(schreg, connTTL, connTTLTimeUnit, new ConnPerRouteBean());
/*     */   }
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
/*     */   public ThreadSafeClientConnManager(SchemeRegistry schreg, long connTTL, TimeUnit connTTLTimeUnit, ConnPerRouteBean connPerRoute) {
/* 133 */     Args.notNull(schreg, "Scheme registry");
/* 134 */     this.log = LogFactory.getLog(getClass());
/* 135 */     this.schemeRegistry = schreg;
/* 136 */     this.connPerRoute = connPerRoute;
/* 137 */     this.connOperator = createConnectionOperator(schreg);
/* 138 */     this.pool = createConnectionPool(connTTL, connTTLTimeUnit);
/* 139 */     this.connectionPool = this.pool;
/*     */   }
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
/*     */   @Deprecated
/*     */   public ThreadSafeClientConnManager(HttpParams params, SchemeRegistry schreg) {
/* 153 */     Args.notNull(schreg, "Scheme registry");
/* 154 */     this.log = LogFactory.getLog(getClass());
/* 155 */     this.schemeRegistry = schreg;
/* 156 */     this.connPerRoute = new ConnPerRouteBean();
/* 157 */     this.connOperator = createConnectionOperator(schreg);
/* 158 */     this.pool = (ConnPoolByRoute)createConnectionPool(params);
/* 159 */     this.connectionPool = this.pool;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/*     */     try {
/* 165 */       shutdown();
/*     */     } finally {
/* 167 */       super.finalize();
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
/*     */   protected AbstractConnPool createConnectionPool(HttpParams params) {
/* 180 */     return new ConnPoolByRoute(this.connOperator, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConnPoolByRoute createConnectionPool(long connTTL, TimeUnit connTTLTimeUnit) {
/* 191 */     return new ConnPoolByRoute(this.connOperator, (ConnPerRoute)this.connPerRoute, 20, connTTL, connTTLTimeUnit);
/*     */   }
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
/*     */   protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
/* 209 */     return (ClientConnectionOperator)new DefaultClientConnectionOperator(schreg);
/*     */   }
/*     */   
/*     */   public SchemeRegistry getSchemeRegistry() {
/* 213 */     return this.schemeRegistry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientConnectionRequest requestConnection(final HttpRoute route, Object state) {
/* 220 */     final PoolEntryRequest poolRequest = this.pool.requestPoolEntry(route, state);
/*     */ 
/*     */     
/* 223 */     return new ClientConnectionRequest()
/*     */       {
/*     */         public void abortRequest() {
/* 226 */           poolRequest.abortRequest();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public ManagedClientConnection getConnection(long timeout, TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
/* 232 */           Args.notNull(route, "Route");
/*     */           
/* 234 */           if (ThreadSafeClientConnManager.this.log.isDebugEnabled()) {
/* 235 */             ThreadSafeClientConnManager.this.log.debug("Get connection: " + route + ", timeout = " + timeout);
/*     */           }
/*     */           
/* 238 */           BasicPoolEntry entry = poolRequest.getPoolEntry(timeout, tunit);
/* 239 */           return (ManagedClientConnection)new BasicPooledConnAdapter(ThreadSafeClientConnManager.this, entry);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseConnection(ManagedClientConnection conn, long validDuration, TimeUnit timeUnit) {
/* 247 */     Args.check(conn instanceof BasicPooledConnAdapter, "Connection class mismatch, connection not obtained from this manager");
/*     */     
/* 249 */     BasicPooledConnAdapter hca = (BasicPooledConnAdapter)conn;
/* 250 */     if (hca.getPoolEntry() != null) {
/* 251 */       Asserts.check((hca.getManager() == this), "Connection not obtained from this manager");
/*     */     }
/* 253 */     synchronized (hca) {
/* 254 */       BasicPoolEntry entry = (BasicPoolEntry)hca.getPoolEntry();
/* 255 */       if (entry == null) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/* 260 */         if (hca.isOpen() && !hca.isMarkedReusable())
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 269 */           hca.shutdown();
/*     */         }
/* 271 */       } catch (IOException iox) {
/* 272 */         if (this.log.isDebugEnabled()) {
/* 273 */           this.log.debug("Exception shutting down released connection.", iox);
/*     */         }
/*     */       } finally {
/*     */         
/* 277 */         boolean reusable = hca.isMarkedReusable();
/* 278 */         if (this.log.isDebugEnabled()) {
/* 279 */           if (reusable) {
/* 280 */             this.log.debug("Released connection is reusable.");
/*     */           } else {
/* 282 */             this.log.debug("Released connection is not reusable.");
/*     */           } 
/*     */         }
/* 285 */         hca.detach();
/* 286 */         this.pool.freeEntry(entry, reusable, validDuration, timeUnit);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 292 */     this.log.debug("Shutting down");
/* 293 */     this.pool.shutdown();
/*     */   }
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
/*     */   public int getConnectionsInPool(HttpRoute route) {
/* 307 */     return this.pool.getConnectionsInPool(route);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getConnectionsInPool() {
/* 319 */     return this.pool.getConnectionsInPool();
/*     */   }
/*     */   
/*     */   public void closeIdleConnections(long idleTimeout, TimeUnit tunit) {
/* 323 */     if (this.log.isDebugEnabled()) {
/* 324 */       this.log.debug("Closing connections idle longer than " + idleTimeout + " " + tunit);
/*     */     }
/* 326 */     this.pool.closeIdleConnections(idleTimeout, tunit);
/*     */   }
/*     */   
/*     */   public void closeExpiredConnections() {
/* 330 */     this.log.debug("Closing expired connections");
/* 331 */     this.pool.closeExpiredConnections();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxTotal() {
/* 338 */     return this.pool.getMaxTotalConnections();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxTotal(int max) {
/* 345 */     this.pool.setMaxTotalConnections(max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDefaultMaxPerRoute() {
/* 352 */     return this.connPerRoute.getDefaultMaxPerRoute();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultMaxPerRoute(int max) {
/* 359 */     this.connPerRoute.setDefaultMaxPerRoute(max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxForRoute(HttpRoute route) {
/* 366 */     return this.connPerRoute.getMaxForRoute(route);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxForRoute(HttpRoute route, int max) {
/* 373 */     this.connPerRoute.setMaxForRoute(route, max);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/conn/tsccm/ThreadSafeClientConnManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */