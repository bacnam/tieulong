/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.annotation.GuardedBy;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.ClientConnectionRequest;
/*     */ import org.apache.http.conn.ManagedClientConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.routing.RouteTracker;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
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
/*     */ @Deprecated
/*     */ @ThreadSafe
/*     */ public class SingleClientConnManager
/*     */   implements ClientConnectionManager
/*     */ {
/*  68 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String MISUSE_MESSAGE = "Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.";
/*     */ 
/*     */ 
/*     */   
/*     */   protected final SchemeRegistry schemeRegistry;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ClientConnectionOperator connOperator;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean alwaysShutDown;
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   protected volatile PoolEntry uniquePoolEntry;
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   protected volatile ConnAdapter managedConn;
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   protected volatile long lastReleaseTime;
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   protected volatile long connectionExpiresTime;
/*     */ 
/*     */ 
/*     */   
/*     */   protected volatile boolean isShutDown;
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public SingleClientConnManager(HttpParams params, SchemeRegistry schreg) {
/* 114 */     this(schreg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SingleClientConnManager(SchemeRegistry schreg) {
/* 122 */     Args.notNull(schreg, "Scheme registry");
/* 123 */     this.schemeRegistry = schreg;
/* 124 */     this.connOperator = createConnectionOperator(schreg);
/* 125 */     this.uniquePoolEntry = new PoolEntry();
/* 126 */     this.managedConn = null;
/* 127 */     this.lastReleaseTime = -1L;
/* 128 */     this.alwaysShutDown = false;
/* 129 */     this.isShutDown = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SingleClientConnManager() {
/* 136 */     this(SchemeRegistryFactory.createDefault());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/*     */     try {
/* 142 */       shutdown();
/*     */     } finally {
/* 144 */       super.finalize();
/*     */     } 
/*     */   }
/*     */   
/*     */   public SchemeRegistry getSchemeRegistry() {
/* 149 */     return this.schemeRegistry;
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
/*     */   protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
/* 166 */     return new DefaultClientConnectionOperator(schreg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void assertStillUp() throws IllegalStateException {
/* 175 */     Asserts.check(!this.isShutDown, "Manager is shut down");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ClientConnectionRequest requestConnection(final HttpRoute route, final Object state) {
/* 182 */     return new ClientConnectionRequest()
/*     */       {
/*     */         public void abortRequest() {}
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public ManagedClientConnection getConnection(long timeout, TimeUnit tunit) {
/* 190 */           return SingleClientConnManager.this.getConnection(route, state);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ManagedClientConnection getConnection(HttpRoute route, Object state) {
/* 206 */     Args.notNull(route, "Route");
/* 207 */     assertStillUp();
/*     */     
/* 209 */     if (this.log.isDebugEnabled()) {
/* 210 */       this.log.debug("Get connection for route " + route);
/*     */     }
/*     */     
/* 213 */     synchronized (this) {
/*     */       
/* 215 */       Asserts.check((this.managedConn == null), "Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.");
/*     */ 
/*     */       
/* 218 */       boolean recreate = false;
/* 219 */       boolean shutdown = false;
/*     */ 
/*     */       
/* 222 */       closeExpiredConnections();
/*     */       
/* 224 */       if (this.uniquePoolEntry.connection.isOpen()) {
/* 225 */         RouteTracker tracker = this.uniquePoolEntry.tracker;
/* 226 */         shutdown = (tracker == null || !tracker.toRoute().equals(route));
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */ 
/*     */         
/* 234 */         recreate = true;
/*     */       } 
/*     */       
/* 237 */       if (shutdown) {
/* 238 */         recreate = true;
/*     */         try {
/* 240 */           this.uniquePoolEntry.shutdown();
/* 241 */         } catch (IOException iox) {
/* 242 */           this.log.debug("Problem shutting down connection.", iox);
/*     */         } 
/*     */       } 
/*     */       
/* 246 */       if (recreate) {
/* 247 */         this.uniquePoolEntry = new PoolEntry();
/*     */       }
/*     */       
/* 250 */       this.managedConn = new ConnAdapter(this.uniquePoolEntry, route);
/*     */       
/* 252 */       return this.managedConn;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseConnection(ManagedClientConnection conn, long validDuration, TimeUnit timeUnit) {
/* 259 */     Args.check(conn instanceof ConnAdapter, "Connection class mismatch, connection not obtained from this manager");
/*     */     
/* 261 */     assertStillUp();
/*     */     
/* 263 */     if (this.log.isDebugEnabled()) {
/* 264 */       this.log.debug("Releasing connection " + conn);
/*     */     }
/*     */     
/* 267 */     ConnAdapter sca = (ConnAdapter)conn;
/* 268 */     synchronized (sca) {
/* 269 */       if (sca.poolEntry == null) {
/*     */         return;
/*     */       }
/*     */       
/* 273 */       ClientConnectionManager manager = sca.getManager();
/* 274 */       Asserts.check((manager == this), "Connection not obtained from this manager");
/*     */       
/*     */       try {
/* 277 */         if (sca.isOpen() && (this.alwaysShutDown || !sca.isMarkedReusable())) {
/*     */ 
/*     */           
/* 280 */           if (this.log.isDebugEnabled()) {
/* 281 */             this.log.debug("Released connection open but not reusable.");
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 288 */           sca.shutdown();
/*     */         } 
/* 290 */       } catch (IOException iox) {
/* 291 */         if (this.log.isDebugEnabled()) {
/* 292 */           this.log.debug("Exception shutting down released connection.", iox);
/*     */         }
/*     */       } finally {
/*     */         
/* 296 */         sca.detach();
/* 297 */         synchronized (this) {
/* 298 */           this.managedConn = null;
/* 299 */           this.lastReleaseTime = System.currentTimeMillis();
/* 300 */           if (validDuration > 0L) {
/* 301 */             this.connectionExpiresTime = timeUnit.toMillis(validDuration) + this.lastReleaseTime;
/*     */           } else {
/* 303 */             this.connectionExpiresTime = Long.MAX_VALUE;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void closeExpiredConnections() {
/* 311 */     long time = this.connectionExpiresTime;
/* 312 */     if (System.currentTimeMillis() >= time) {
/* 313 */       closeIdleConnections(0L, TimeUnit.MILLISECONDS);
/*     */     }
/*     */   }
/*     */   
/*     */   public void closeIdleConnections(long idletime, TimeUnit tunit) {
/* 318 */     assertStillUp();
/*     */ 
/*     */     
/* 321 */     Args.notNull(tunit, "Time unit");
/*     */     
/* 323 */     synchronized (this) {
/* 324 */       if (this.managedConn == null && this.uniquePoolEntry.connection.isOpen()) {
/* 325 */         long cutoff = System.currentTimeMillis() - tunit.toMillis(idletime);
/*     */         
/* 327 */         if (this.lastReleaseTime <= cutoff) {
/*     */           try {
/* 329 */             this.uniquePoolEntry.close();
/* 330 */           } catch (IOException iox) {
/*     */             
/* 332 */             this.log.debug("Problem closing idle connection.", iox);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 340 */     this.isShutDown = true;
/* 341 */     synchronized (this) {
/*     */       try {
/* 343 */         if (this.uniquePoolEntry != null) {
/* 344 */           this.uniquePoolEntry.shutdown();
/*     */         }
/* 346 */       } catch (IOException iox) {
/*     */         
/* 348 */         this.log.debug("Problem while shutting down manager.", iox);
/*     */       } finally {
/* 350 */         this.uniquePoolEntry = null;
/* 351 */         this.managedConn = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void revokeConnection() {
/* 357 */     ConnAdapter conn = this.managedConn;
/* 358 */     if (conn == null) {
/*     */       return;
/*     */     }
/* 361 */     conn.detach();
/*     */     
/* 363 */     synchronized (this) {
/*     */       try {
/* 365 */         this.uniquePoolEntry.shutdown();
/* 366 */       } catch (IOException iox) {
/*     */         
/* 368 */         this.log.debug("Problem while shutting down connection.", iox);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class PoolEntry
/*     */     extends AbstractPoolEntry
/*     */   {
/*     */     protected PoolEntry() {
/* 383 */       super(SingleClientConnManager.this.connOperator, null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void close() throws IOException {
/* 390 */       shutdownEntry();
/* 391 */       if (this.connection.isOpen()) {
/* 392 */         this.connection.close();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void shutdown() throws IOException {
/* 400 */       shutdownEntry();
/* 401 */       if (this.connection.isOpen()) {
/* 402 */         this.connection.shutdown();
/*     */       }
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
/*     */   protected class ConnAdapter
/*     */     extends AbstractPooledConnAdapter
/*     */   {
/*     */     protected ConnAdapter(SingleClientConnManager.PoolEntry entry, HttpRoute route) {
/* 420 */       super(SingleClientConnManager.this, entry);
/* 421 */       markReusable();
/* 422 */       entry.route = route;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/conn/SingleClientConnManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */