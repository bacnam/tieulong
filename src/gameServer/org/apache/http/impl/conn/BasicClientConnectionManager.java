/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpClientConnection;
/*     */ import org.apache.http.annotation.GuardedBy;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.ClientConnectionRequest;
/*     */ import org.apache.http.conn.ManagedClientConnection;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
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
/*     */ @Deprecated
/*     */ @ThreadSafe
/*     */ public class BasicClientConnectionManager
/*     */   implements ClientConnectionManager
/*     */ {
/*  73 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*  75 */   private static final AtomicLong COUNTER = new AtomicLong();
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String MISUSE_MESSAGE = "Invalid use of BasicClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.";
/*     */ 
/*     */ 
/*     */   
/*     */   private final SchemeRegistry schemeRegistry;
/*     */ 
/*     */ 
/*     */   
/*     */   private final ClientConnectionOperator connOperator;
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   private HttpPoolEntry poolEntry;
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   private ManagedClientConnectionImpl conn;
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   private volatile boolean shutdown;
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicClientConnectionManager(SchemeRegistry schreg) {
/* 106 */     Args.notNull(schreg, "Scheme registry");
/* 107 */     this.schemeRegistry = schreg;
/* 108 */     this.connOperator = createConnectionOperator(schreg);
/*     */   }
/*     */   
/*     */   public BasicClientConnectionManager() {
/* 112 */     this(SchemeRegistryFactory.createDefault());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/*     */     try {
/* 118 */       shutdown();
/*     */     } finally {
/* 120 */       super.finalize();
/*     */     } 
/*     */   }
/*     */   
/*     */   public SchemeRegistry getSchemeRegistry() {
/* 125 */     return this.schemeRegistry;
/*     */   }
/*     */   
/*     */   protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
/* 129 */     return new DefaultClientConnectionOperator(schreg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ClientConnectionRequest requestConnection(final HttpRoute route, final Object state) {
/* 136 */     return new ClientConnectionRequest()
/*     */       {
/*     */         public void abortRequest() {}
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public ManagedClientConnection getConnection(long timeout, TimeUnit tunit) {
/* 144 */           return BasicClientConnectionManager.this.getConnection(route, state);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void assertNotShutdown() {
/* 152 */     Asserts.check(!this.shutdown, "Connection manager has been shut down");
/*     */   }
/*     */   
/*     */   ManagedClientConnection getConnection(HttpRoute route, Object state) {
/* 156 */     Args.notNull(route, "Route");
/* 157 */     synchronized (this) {
/* 158 */       assertNotShutdown();
/* 159 */       if (this.log.isDebugEnabled()) {
/* 160 */         this.log.debug("Get connection for route " + route);
/*     */       }
/* 162 */       Asserts.check((this.conn == null), "Invalid use of BasicClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.");
/* 163 */       if (this.poolEntry != null && !this.poolEntry.getPlannedRoute().equals(route)) {
/* 164 */         this.poolEntry.close();
/* 165 */         this.poolEntry = null;
/*     */       } 
/* 167 */       if (this.poolEntry == null) {
/* 168 */         String id = Long.toString(COUNTER.getAndIncrement());
/* 169 */         OperatedClientConnection opconn = this.connOperator.createConnection();
/* 170 */         this.poolEntry = new HttpPoolEntry(this.log, id, route, opconn, 0L, TimeUnit.MILLISECONDS);
/*     */       } 
/* 172 */       long now = System.currentTimeMillis();
/* 173 */       if (this.poolEntry.isExpired(now)) {
/* 174 */         this.poolEntry.close();
/* 175 */         this.poolEntry.getTracker().reset();
/*     */       } 
/* 177 */       this.conn = new ManagedClientConnectionImpl(this, this.connOperator, this.poolEntry);
/* 178 */       return this.conn;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void shutdownConnection(HttpClientConnection conn) {
/*     */     try {
/* 184 */       conn.shutdown();
/* 185 */     } catch (IOException iox) {
/* 186 */       if (this.log.isDebugEnabled()) {
/* 187 */         this.log.debug("I/O exception shutting down connection", iox);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void releaseConnection(ManagedClientConnection conn, long keepalive, TimeUnit tunit) {
/* 193 */     Args.check(conn instanceof ManagedClientConnectionImpl, "Connection class mismatch, connection not obtained from this manager");
/*     */     
/* 195 */     ManagedClientConnectionImpl managedConn = (ManagedClientConnectionImpl)conn;
/* 196 */     synchronized (managedConn) {
/* 197 */       if (this.log.isDebugEnabled()) {
/* 198 */         this.log.debug("Releasing connection " + conn);
/*     */       }
/* 200 */       if (managedConn.getPoolEntry() == null) {
/*     */         return;
/*     */       }
/* 203 */       ClientConnectionManager manager = managedConn.getManager();
/* 204 */       Asserts.check((manager == this), "Connection not obtained from this manager");
/* 205 */       synchronized (this) {
/* 206 */         if (this.shutdown) {
/* 207 */           shutdownConnection((HttpClientConnection)managedConn);
/*     */           return;
/*     */         } 
/*     */         try {
/* 211 */           if (managedConn.isOpen() && !managedConn.isMarkedReusable()) {
/* 212 */             shutdownConnection((HttpClientConnection)managedConn);
/*     */           }
/* 214 */           if (managedConn.isMarkedReusable()) {
/* 215 */             this.poolEntry.updateExpiry(keepalive, (tunit != null) ? tunit : TimeUnit.MILLISECONDS);
/* 216 */             if (this.log.isDebugEnabled()) {
/*     */               String s;
/* 218 */               if (keepalive > 0L) {
/* 219 */                 s = "for " + keepalive + " " + tunit;
/*     */               } else {
/* 221 */                 s = "indefinitely";
/*     */               } 
/* 223 */               this.log.debug("Connection can be kept alive " + s);
/*     */             } 
/*     */           } 
/*     */         } finally {
/* 227 */           managedConn.detach();
/* 228 */           this.conn = null;
/* 229 */           if (this.poolEntry.isClosed()) {
/* 230 */             this.poolEntry = null;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void closeExpiredConnections() {
/* 238 */     synchronized (this) {
/* 239 */       assertNotShutdown();
/* 240 */       long now = System.currentTimeMillis();
/* 241 */       if (this.poolEntry != null && this.poolEntry.isExpired(now)) {
/* 242 */         this.poolEntry.close();
/* 243 */         this.poolEntry.getTracker().reset();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void closeIdleConnections(long idletime, TimeUnit tunit) {
/* 249 */     Args.notNull(tunit, "Time unit");
/* 250 */     synchronized (this) {
/* 251 */       assertNotShutdown();
/* 252 */       long time = tunit.toMillis(idletime);
/* 253 */       if (time < 0L) {
/* 254 */         time = 0L;
/*     */       }
/* 256 */       long deadline = System.currentTimeMillis() - time;
/* 257 */       if (this.poolEntry != null && this.poolEntry.getUpdated() <= deadline) {
/* 258 */         this.poolEntry.close();
/* 259 */         this.poolEntry.getTracker().reset();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 265 */     synchronized (this) {
/* 266 */       this.shutdown = true;
/*     */       try {
/* 268 */         if (this.poolEntry != null) {
/* 269 */           this.poolEntry.close();
/*     */         }
/*     */       } finally {
/* 272 */         this.poolEntry = null;
/* 273 */         this.conn = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/conn/BasicClientConnectionManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */