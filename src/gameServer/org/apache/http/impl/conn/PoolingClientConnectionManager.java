/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.ClientConnectionRequest;
/*     */ import org.apache.http.conn.ConnectionPoolTimeoutException;
/*     */ import org.apache.http.conn.DnsResolver;
/*     */ import org.apache.http.conn.ManagedClientConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
/*     */ import org.apache.http.pool.ConnPoolControl;
/*     */ import org.apache.http.pool.PoolStats;
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
/*     */ @Deprecated
/*     */ @ThreadSafe
/*     */ public class PoolingClientConnectionManager
/*     */   implements ClientConnectionManager, ConnPoolControl<HttpRoute>
/*     */ {
/*  75 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*     */   private final SchemeRegistry schemeRegistry;
/*     */   
/*     */   private final HttpConnPool pool;
/*     */   
/*     */   private final ClientConnectionOperator operator;
/*     */   
/*     */   private final DnsResolver dnsResolver;
/*     */ 
/*     */   
/*     */   public PoolingClientConnectionManager(SchemeRegistry schreg) {
/*  87 */     this(schreg, -1L, TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */   public PoolingClientConnectionManager(SchemeRegistry schreg, DnsResolver dnsResolver) {
/*  91 */     this(schreg, -1L, TimeUnit.MILLISECONDS, dnsResolver);
/*     */   }
/*     */   
/*     */   public PoolingClientConnectionManager() {
/*  95 */     this(SchemeRegistryFactory.createDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingClientConnectionManager(SchemeRegistry schemeRegistry, long timeToLive, TimeUnit tunit) {
/* 101 */     this(schemeRegistry, timeToLive, tunit, new SystemDefaultDnsResolver());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingClientConnectionManager(SchemeRegistry schemeRegistry, long timeToLive, TimeUnit tunit, DnsResolver dnsResolver) {
/* 108 */     Args.notNull(schemeRegistry, "Scheme registry");
/* 109 */     Args.notNull(dnsResolver, "DNS resolver");
/* 110 */     this.schemeRegistry = schemeRegistry;
/* 111 */     this.dnsResolver = dnsResolver;
/* 112 */     this.operator = createConnectionOperator(schemeRegistry);
/* 113 */     this.pool = new HttpConnPool(this.log, this.operator, 2, 20, timeToLive, tunit);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/*     */     try {
/* 119 */       shutdown();
/*     */     } finally {
/* 121 */       super.finalize();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
/* 138 */     return new DefaultClientConnectionOperator(schreg, this.dnsResolver);
/*     */   }
/*     */   
/*     */   public SchemeRegistry getSchemeRegistry() {
/* 142 */     return this.schemeRegistry;
/*     */   }
/*     */   
/*     */   private String format(HttpRoute route, Object state) {
/* 146 */     StringBuilder buf = new StringBuilder();
/* 147 */     buf.append("[route: ").append(route).append("]");
/* 148 */     if (state != null) {
/* 149 */       buf.append("[state: ").append(state).append("]");
/*     */     }
/* 151 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private String formatStats(HttpRoute route) {
/* 155 */     StringBuilder buf = new StringBuilder();
/* 156 */     PoolStats totals = this.pool.getTotalStats();
/* 157 */     PoolStats stats = this.pool.getStats(route);
/* 158 */     buf.append("[total kept alive: ").append(totals.getAvailable()).append("; ");
/* 159 */     buf.append("route allocated: ").append(stats.getLeased() + stats.getAvailable());
/* 160 */     buf.append(" of ").append(stats.getMax()).append("; ");
/* 161 */     buf.append("total allocated: ").append(totals.getLeased() + totals.getAvailable());
/* 162 */     buf.append(" of ").append(totals.getMax()).append("]");
/* 163 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private String format(HttpPoolEntry entry) {
/* 167 */     StringBuilder buf = new StringBuilder();
/* 168 */     buf.append("[id: ").append(entry.getId()).append("]");
/* 169 */     buf.append("[route: ").append(entry.getRoute()).append("]");
/* 170 */     Object state = entry.getState();
/* 171 */     if (state != null) {
/* 172 */       buf.append("[state: ").append(state).append("]");
/*     */     }
/* 174 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientConnectionRequest requestConnection(HttpRoute route, Object state) {
/* 180 */     Args.notNull(route, "HTTP route");
/* 181 */     if (this.log.isDebugEnabled()) {
/* 182 */       this.log.debug("Connection request: " + format(route, state) + formatStats(route));
/*     */     }
/* 184 */     final Future<HttpPoolEntry> future = this.pool.lease(route, state);
/*     */     
/* 186 */     return new ClientConnectionRequest()
/*     */       {
/*     */         public void abortRequest() {
/* 189 */           future.cancel(true);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public ManagedClientConnection getConnection(long timeout, TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
/* 195 */           return PoolingClientConnectionManager.this.leaseConnection(future, timeout, tunit);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ManagedClientConnection leaseConnection(Future<HttpPoolEntry> future, long timeout, TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
/*     */     try {
/* 208 */       HttpPoolEntry entry = future.get(timeout, tunit);
/* 209 */       if (entry == null || future.isCancelled()) {
/* 210 */         throw new InterruptedException();
/*     */       }
/* 212 */       Asserts.check((entry.getConnection() != null), "Pool entry with no connection");
/* 213 */       if (this.log.isDebugEnabled()) {
/* 214 */         this.log.debug("Connection leased: " + format(entry) + formatStats((HttpRoute)entry.getRoute()));
/*     */       }
/* 216 */       return new ManagedClientConnectionImpl(this, this.operator, entry);
/* 217 */     } catch (ExecutionException ex) {
/* 218 */       Throwable cause = ex.getCause();
/* 219 */       if (cause == null) {
/* 220 */         cause = ex;
/*     */       }
/* 222 */       this.log.error("Unexpected exception leasing connection from pool", cause);
/*     */       
/* 224 */       throw new InterruptedException();
/* 225 */     } catch (TimeoutException ex) {
/* 226 */       throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseConnection(ManagedClientConnection conn, long keepalive, TimeUnit tunit) {
/* 233 */     Args.check(conn instanceof ManagedClientConnectionImpl, "Connection class mismatch, connection not obtained from this manager");
/*     */     
/* 235 */     ManagedClientConnectionImpl managedConn = (ManagedClientConnectionImpl)conn;
/* 236 */     Asserts.check((managedConn.getManager() == this), "Connection not obtained from this manager");
/* 237 */     synchronized (managedConn) {
/* 238 */       HttpPoolEntry entry = managedConn.detach();
/* 239 */       if (entry == null) {
/*     */         return;
/*     */       }
/*     */       try {
/* 243 */         if (managedConn.isOpen() && !managedConn.isMarkedReusable()) {
/*     */           try {
/* 245 */             managedConn.shutdown();
/* 246 */           } catch (IOException iox) {
/* 247 */             if (this.log.isDebugEnabled()) {
/* 248 */               this.log.debug("I/O exception shutting down released connection", iox);
/*     */             }
/*     */           } 
/*     */         }
/*     */         
/* 253 */         if (managedConn.isMarkedReusable()) {
/* 254 */           entry.updateExpiry(keepalive, (tunit != null) ? tunit : TimeUnit.MILLISECONDS);
/* 255 */           if (this.log.isDebugEnabled()) {
/*     */             String s;
/* 257 */             if (keepalive > 0L) {
/* 258 */               s = "for " + keepalive + " " + tunit;
/*     */             } else {
/* 260 */               s = "indefinitely";
/*     */             } 
/* 262 */             this.log.debug("Connection " + format(entry) + " can be kept alive " + s);
/*     */           } 
/*     */         } 
/*     */       } finally {
/* 266 */         this.pool.release(entry, managedConn.isMarkedReusable());
/*     */       } 
/* 268 */       if (this.log.isDebugEnabled()) {
/* 269 */         this.log.debug("Connection released: " + format(entry) + formatStats((HttpRoute)entry.getRoute()));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 275 */     this.log.debug("Connection manager is shutting down");
/*     */     try {
/* 277 */       this.pool.shutdown();
/* 278 */     } catch (IOException ex) {
/* 279 */       this.log.debug("I/O exception shutting down connection manager", ex);
/*     */     } 
/* 281 */     this.log.debug("Connection manager shut down");
/*     */   }
/*     */   
/*     */   public void closeIdleConnections(long idleTimeout, TimeUnit tunit) {
/* 285 */     if (this.log.isDebugEnabled()) {
/* 286 */       this.log.debug("Closing connections idle longer than " + idleTimeout + " " + tunit);
/*     */     }
/* 288 */     this.pool.closeIdle(idleTimeout, tunit);
/*     */   }
/*     */   
/*     */   public void closeExpiredConnections() {
/* 292 */     this.log.debug("Closing expired connections");
/* 293 */     this.pool.closeExpired();
/*     */   }
/*     */   
/*     */   public int getMaxTotal() {
/* 297 */     return this.pool.getMaxTotal();
/*     */   }
/*     */   
/*     */   public void setMaxTotal(int max) {
/* 301 */     this.pool.setMaxTotal(max);
/*     */   }
/*     */   
/*     */   public int getDefaultMaxPerRoute() {
/* 305 */     return this.pool.getDefaultMaxPerRoute();
/*     */   }
/*     */   
/*     */   public void setDefaultMaxPerRoute(int max) {
/* 309 */     this.pool.setDefaultMaxPerRoute(max);
/*     */   }
/*     */   
/*     */   public int getMaxPerRoute(HttpRoute route) {
/* 313 */     return this.pool.getMaxPerRoute(route);
/*     */   }
/*     */   
/*     */   public void setMaxPerRoute(HttpRoute route, int max) {
/* 317 */     this.pool.setMaxPerRoute(route, max);
/*     */   }
/*     */   
/*     */   public PoolStats getTotalStats() {
/* 321 */     return this.pool.getTotalStats();
/*     */   }
/*     */   
/*     */   public PoolStats getStats(HttpRoute route) {
/* 325 */     return this.pool.getStats(route);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/conn/PoolingClientConnectionManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */