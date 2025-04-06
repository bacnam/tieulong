/*     */ package org.apache.http.impl.nio.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.concurrent.BasicFuture;
/*     */ import org.apache.http.concurrent.FutureCallback;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.nio.conn.ClientAsyncConnectionFactory;
/*     */ import org.apache.http.nio.conn.ClientAsyncConnectionManager;
/*     */ import org.apache.http.nio.conn.ManagedClientAsyncConnection;
/*     */ import org.apache.http.nio.conn.scheme.AsyncSchemeRegistry;
/*     */ import org.apache.http.nio.reactor.ConnectingIOReactor;
/*     */ import org.apache.http.nio.reactor.IOEventDispatch;
/*     */ import org.apache.http.nio.reactor.IOReactorException;
/*     */ import org.apache.http.nio.reactor.IOReactorStatus;
/*     */ import org.apache.http.pool.ConnPoolControl;
/*     */ import org.apache.http.pool.PoolStats;
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
/*     */ @Deprecated
/*     */ public class PoolingClientAsyncConnectionManager
/*     */   implements ClientAsyncConnectionManager, ConnPoolControl<HttpRoute>
/*     */ {
/*  54 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*     */   private final ConnectingIOReactor ioreactor;
/*     */   
/*     */   private final HttpNIOConnPool pool;
/*     */   
/*     */   private final AsyncSchemeRegistry schemeRegistry;
/*     */   
/*     */   private final ClientAsyncConnectionFactory connFactory;
/*     */ 
/*     */   
/*     */   public PoolingClientAsyncConnectionManager(ConnectingIOReactor ioreactor, AsyncSchemeRegistry schemeRegistry, long timeToLive, TimeUnit tunit) {
/*  66 */     Args.notNull(ioreactor, "I/O reactor");
/*  67 */     Args.notNull(schemeRegistry, "Scheme registory");
/*  68 */     Args.notNull(tunit, "Time unit");
/*  69 */     this.ioreactor = ioreactor;
/*  70 */     this.pool = new HttpNIOConnPool(this.log, ioreactor, schemeRegistry, timeToLive, tunit);
/*  71 */     this.schemeRegistry = schemeRegistry;
/*  72 */     this.connFactory = createClientAsyncConnectionFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingClientAsyncConnectionManager(ConnectingIOReactor ioreactor, AsyncSchemeRegistry schemeRegistry) throws IOReactorException {
/*  78 */     this(ioreactor, schemeRegistry, -1L, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolingClientAsyncConnectionManager(ConnectingIOReactor ioreactor) throws IOReactorException {
/*  83 */     this(ioreactor, AsyncSchemeRegistryFactory.createDefault());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/*     */     try {
/*  89 */       shutdown();
/*     */     } finally {
/*  91 */       super.finalize();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected ClientAsyncConnectionFactory createClientAsyncConnectionFactory() {
/*  96 */     return new DefaultClientAsyncConnectionFactory();
/*     */   }
/*     */   
/*     */   public AsyncSchemeRegistry getSchemeRegistry() {
/* 100 */     return this.schemeRegistry;
/*     */   }
/*     */   
/*     */   public void execute(IOEventDispatch eventDispatch) throws IOException {
/* 104 */     this.ioreactor.execute(eventDispatch);
/*     */   }
/*     */   
/*     */   public IOReactorStatus getStatus() {
/* 108 */     return this.ioreactor.getStatus();
/*     */   }
/*     */   
/*     */   public void shutdown(long waitMs) throws IOException {
/* 112 */     this.log.debug("Connection manager is shutting down");
/* 113 */     this.pool.shutdown(waitMs);
/* 114 */     this.log.debug("Connection manager shut down");
/*     */   }
/*     */   
/*     */   public void shutdown() throws IOException {
/* 118 */     this.log.debug("Connection manager is shutting down");
/* 119 */     this.pool.shutdown(2000L);
/* 120 */     this.log.debug("Connection manager shut down");
/*     */   }
/*     */   
/*     */   private String format(HttpRoute route, Object state) {
/* 124 */     StringBuilder buf = new StringBuilder();
/* 125 */     buf.append("[route: ").append(route).append("]");
/* 126 */     if (state != null) {
/* 127 */       buf.append("[state: ").append(state).append("]");
/*     */     }
/* 129 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private String formatStats(HttpRoute route) {
/* 133 */     StringBuilder buf = new StringBuilder();
/* 134 */     PoolStats totals = this.pool.getTotalStats();
/* 135 */     PoolStats stats = this.pool.getStats(route);
/* 136 */     buf.append("[total kept alive: ").append(totals.getAvailable()).append("; ");
/* 137 */     buf.append("route allocated: ").append(stats.getLeased() + stats.getAvailable());
/* 138 */     buf.append(" of ").append(stats.getMax()).append("; ");
/* 139 */     buf.append("total allocated: ").append(totals.getLeased() + totals.getAvailable());
/* 140 */     buf.append(" of ").append(totals.getMax()).append("]");
/* 141 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private String format(HttpPoolEntry entry) {
/* 145 */     StringBuilder buf = new StringBuilder();
/* 146 */     buf.append("[id: ").append(entry.getId()).append("]");
/* 147 */     buf.append("[route: ").append(entry.getRoute()).append("]");
/* 148 */     Object state = entry.getState();
/* 149 */     if (state != null) {
/* 150 */       buf.append("[state: ").append(state).append("]");
/*     */     }
/* 152 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<ManagedClientAsyncConnection> leaseConnection(HttpRoute route, Object state, long connectTimeout, TimeUnit tunit, FutureCallback<ManagedClientAsyncConnection> callback) {
/* 161 */     Args.notNull(route, "HTTP route");
/* 162 */     Args.notNull(tunit, "Time unit");
/* 163 */     if (this.log.isDebugEnabled()) {
/* 164 */       this.log.debug("Connection request: " + format(route, state) + formatStats(route));
/*     */     }
/* 166 */     BasicFuture<ManagedClientAsyncConnection> future = new BasicFuture(callback);
/*     */     
/* 168 */     this.pool.lease(route, state, connectTimeout, tunit, new InternalPoolEntryCallback(future));
/* 169 */     return (Future<ManagedClientAsyncConnection>)future;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseConnection(ManagedClientAsyncConnection conn, long keepalive, TimeUnit tunit) {
/* 176 */     Args.notNull(conn, "HTTP connection");
/* 177 */     if (!(conn instanceof ManagedClientAsyncConnectionImpl)) {
/* 178 */       throw new IllegalArgumentException("Connection class mismatch, connection not obtained from this manager");
/*     */     }
/*     */     
/* 181 */     Args.notNull(tunit, "Time unit");
/* 182 */     ManagedClientAsyncConnectionImpl managedConn = (ManagedClientAsyncConnectionImpl)conn;
/* 183 */     ClientAsyncConnectionManager manager = managedConn.getManager();
/* 184 */     if (manager != null && manager != this) {
/* 185 */       throw new IllegalArgumentException("Connection not obtained from this manager");
/*     */     }
/* 187 */     if (this.pool.isShutdown()) {
/*     */       return;
/*     */     }
/*     */     
/* 191 */     synchronized (managedConn) {
/* 192 */       HttpPoolEntry entry = managedConn.getPoolEntry();
/* 193 */       if (entry == null) {
/*     */         return;
/*     */       }
/*     */       try {
/* 197 */         if (managedConn.isOpen() && !managedConn.isMarkedReusable()) {
/*     */           try {
/* 199 */             managedConn.shutdown();
/* 200 */           } catch (IOException iox) {
/* 201 */             if (this.log.isDebugEnabled()) {
/* 202 */               this.log.debug("I/O exception shutting down released connection", iox);
/*     */             }
/*     */           } 
/*     */         }
/* 206 */         if (managedConn.isOpen()) {
/* 207 */           entry.updateExpiry(keepalive, (tunit != null) ? tunit : TimeUnit.MILLISECONDS);
/* 208 */           if (this.log.isDebugEnabled()) {
/*     */             String s;
/* 210 */             if (keepalive > 0L) {
/* 211 */               s = "for " + keepalive + " " + tunit;
/*     */             } else {
/* 213 */               s = "indefinitely";
/*     */             } 
/* 215 */             this.log.debug("Connection " + format(entry) + " can be kept alive " + s);
/*     */           } 
/*     */           
/* 218 */           managedConn.setSocketTimeout(0);
/*     */         } 
/*     */       } finally {
/* 221 */         this.pool.release(managedConn.detach(), managedConn.isMarkedReusable());
/*     */       } 
/* 223 */       if (this.log.isDebugEnabled()) {
/* 224 */         this.log.debug("Connection released: " + format(entry) + formatStats((HttpRoute)entry.getRoute()));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public PoolStats getTotalStats() {
/* 230 */     return this.pool.getTotalStats();
/*     */   }
/*     */   
/*     */   public PoolStats getStats(HttpRoute route) {
/* 234 */     return this.pool.getStats(route);
/*     */   }
/*     */   
/*     */   public void setMaxTotal(int max) {
/* 238 */     this.pool.setMaxTotal(max);
/*     */   }
/*     */   
/*     */   public void setDefaultMaxPerRoute(int max) {
/* 242 */     this.pool.setDefaultMaxPerRoute(max);
/*     */   }
/*     */   
/*     */   public void setMaxPerRoute(HttpRoute route, int max) {
/* 246 */     this.pool.setMaxPerRoute(route, max);
/*     */   }
/*     */   
/*     */   public int getMaxTotal() {
/* 250 */     return this.pool.getMaxTotal();
/*     */   }
/*     */   
/*     */   public int getDefaultMaxPerRoute() {
/* 254 */     return this.pool.getDefaultMaxPerRoute();
/*     */   }
/*     */   
/*     */   public int getMaxPerRoute(HttpRoute route) {
/* 258 */     return this.pool.getMaxPerRoute(route);
/*     */   }
/*     */   
/*     */   public void closeIdleConnections(long idleTimeout, TimeUnit tunit) {
/* 262 */     if (this.log.isDebugEnabled()) {
/* 263 */       this.log.debug("Closing connections idle longer than " + idleTimeout + " " + tunit);
/*     */     }
/* 265 */     this.pool.closeIdle(idleTimeout, tunit);
/*     */   }
/*     */   
/*     */   public void closeExpiredConnections() {
/* 269 */     this.log.debug("Closing expired connections");
/* 270 */     this.pool.closeExpired();
/*     */   }
/*     */ 
/*     */   
/*     */   class InternalPoolEntryCallback
/*     */     implements FutureCallback<HttpPoolEntry>
/*     */   {
/*     */     private final BasicFuture<ManagedClientAsyncConnection> future;
/*     */     
/*     */     public InternalPoolEntryCallback(BasicFuture<ManagedClientAsyncConnection> future) {
/* 280 */       this.future = future;
/*     */     }
/*     */     
/*     */     public void completed(HttpPoolEntry entry) {
/* 284 */       if (PoolingClientAsyncConnectionManager.this.log.isDebugEnabled()) {
/* 285 */         PoolingClientAsyncConnectionManager.this.log.debug("Connection leased: " + PoolingClientAsyncConnectionManager.this.format(entry) + PoolingClientAsyncConnectionManager.this.formatStats((HttpRoute)entry.getRoute()));
/*     */       }
/* 287 */       ManagedClientAsyncConnection conn = new ManagedClientAsyncConnectionImpl(PoolingClientAsyncConnectionManager.this, PoolingClientAsyncConnectionManager.this.connFactory, entry);
/*     */ 
/*     */ 
/*     */       
/* 291 */       if (!this.future.completed(conn)) {
/* 292 */         PoolingClientAsyncConnectionManager.this.pool.release(entry, true);
/*     */       }
/*     */     }
/*     */     
/*     */     public void failed(Exception ex) {
/* 297 */       if (PoolingClientAsyncConnectionManager.this.log.isDebugEnabled()) {
/* 298 */         PoolingClientAsyncConnectionManager.this.log.debug("Connection request failed", ex);
/*     */       }
/* 300 */       this.future.failed(ex);
/*     */     }
/*     */     
/*     */     public void cancelled() {
/* 304 */       PoolingClientAsyncConnectionManager.this.log.debug("Connection request cancelled");
/* 305 */       this.future.cancel(true);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/conn/PoolingClientAsyncConnectionManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */