/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import org.apache.http.HttpConnectionMetrics;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.ManagedClientConnection;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.routing.RouteTracker;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ @Deprecated
/*     */ @NotThreadSafe
/*     */ class ManagedClientConnectionImpl
/*     */   implements ManagedClientConnection
/*     */ {
/*     */   private final ClientConnectionManager manager;
/*     */   private final ClientConnectionOperator operator;
/*     */   private volatile HttpPoolEntry poolEntry;
/*     */   private volatile boolean reusable;
/*     */   private volatile long duration;
/*     */   
/*     */   ManagedClientConnectionImpl(ClientConnectionManager manager, ClientConnectionOperator operator, HttpPoolEntry entry) {
/*  76 */     Args.notNull(manager, "Connection manager");
/*  77 */     Args.notNull(operator, "Connection operator");
/*  78 */     Args.notNull(entry, "HTTP pool entry");
/*  79 */     this.manager = manager;
/*  80 */     this.operator = operator;
/*  81 */     this.poolEntry = entry;
/*  82 */     this.reusable = false;
/*  83 */     this.duration = Long.MAX_VALUE;
/*     */   }
/*     */   
/*     */   public String getId() {
/*  87 */     return null;
/*     */   }
/*     */   
/*     */   HttpPoolEntry getPoolEntry() {
/*  91 */     return this.poolEntry;
/*     */   }
/*     */   
/*     */   HttpPoolEntry detach() {
/*  95 */     HttpPoolEntry local = this.poolEntry;
/*  96 */     this.poolEntry = null;
/*  97 */     return local;
/*     */   }
/*     */   
/*     */   public ClientConnectionManager getManager() {
/* 101 */     return this.manager;
/*     */   }
/*     */   
/*     */   private OperatedClientConnection getConnection() {
/* 105 */     HttpPoolEntry local = this.poolEntry;
/* 106 */     if (local == null) {
/* 107 */       return null;
/*     */     }
/* 109 */     return (OperatedClientConnection)local.getConnection();
/*     */   }
/*     */   
/*     */   private OperatedClientConnection ensureConnection() {
/* 113 */     HttpPoolEntry local = this.poolEntry;
/* 114 */     if (local == null) {
/* 115 */       throw new ConnectionShutdownException();
/*     */     }
/* 117 */     return (OperatedClientConnection)local.getConnection();
/*     */   }
/*     */   
/*     */   private HttpPoolEntry ensurePoolEntry() {
/* 121 */     HttpPoolEntry local = this.poolEntry;
/* 122 */     if (local == null) {
/* 123 */       throw new ConnectionShutdownException();
/*     */     }
/* 125 */     return local;
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 129 */     HttpPoolEntry local = this.poolEntry;
/* 130 */     if (local != null) {
/* 131 */       OperatedClientConnection conn = (OperatedClientConnection)local.getConnection();
/* 132 */       local.getTracker().reset();
/* 133 */       conn.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void shutdown() throws IOException {
/* 138 */     HttpPoolEntry local = this.poolEntry;
/* 139 */     if (local != null) {
/* 140 */       OperatedClientConnection conn = (OperatedClientConnection)local.getConnection();
/* 141 */       local.getTracker().reset();
/* 142 */       conn.shutdown();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isOpen() {
/* 147 */     OperatedClientConnection conn = getConnection();
/* 148 */     if (conn != null) {
/* 149 */       return conn.isOpen();
/*     */     }
/* 151 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStale() {
/* 156 */     OperatedClientConnection conn = getConnection();
/* 157 */     if (conn != null) {
/* 158 */       return conn.isStale();
/*     */     }
/* 160 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(int timeout) {
/* 165 */     OperatedClientConnection conn = ensureConnection();
/* 166 */     conn.setSocketTimeout(timeout);
/*     */   }
/*     */   
/*     */   public int getSocketTimeout() {
/* 170 */     OperatedClientConnection conn = ensureConnection();
/* 171 */     return conn.getSocketTimeout();
/*     */   }
/*     */   
/*     */   public HttpConnectionMetrics getMetrics() {
/* 175 */     OperatedClientConnection conn = ensureConnection();
/* 176 */     return conn.getMetrics();
/*     */   }
/*     */   
/*     */   public void flush() throws IOException {
/* 180 */     OperatedClientConnection conn = ensureConnection();
/* 181 */     conn.flush();
/*     */   }
/*     */   
/*     */   public boolean isResponseAvailable(int timeout) throws IOException {
/* 185 */     OperatedClientConnection conn = ensureConnection();
/* 186 */     return conn.isResponseAvailable(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public void receiveResponseEntity(HttpResponse response) throws HttpException, IOException {
/* 191 */     OperatedClientConnection conn = ensureConnection();
/* 192 */     conn.receiveResponseEntity(response);
/*     */   }
/*     */   
/*     */   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
/* 196 */     OperatedClientConnection conn = ensureConnection();
/* 197 */     return conn.receiveResponseHeader();
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
/* 202 */     OperatedClientConnection conn = ensureConnection();
/* 203 */     conn.sendRequestEntity(request);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
/* 208 */     OperatedClientConnection conn = ensureConnection();
/* 209 */     conn.sendRequestHeader(request);
/*     */   }
/*     */   
/*     */   public InetAddress getLocalAddress() {
/* 213 */     OperatedClientConnection conn = ensureConnection();
/* 214 */     return conn.getLocalAddress();
/*     */   }
/*     */   
/*     */   public int getLocalPort() {
/* 218 */     OperatedClientConnection conn = ensureConnection();
/* 219 */     return conn.getLocalPort();
/*     */   }
/*     */   
/*     */   public InetAddress getRemoteAddress() {
/* 223 */     OperatedClientConnection conn = ensureConnection();
/* 224 */     return conn.getRemoteAddress();
/*     */   }
/*     */   
/*     */   public int getRemotePort() {
/* 228 */     OperatedClientConnection conn = ensureConnection();
/* 229 */     return conn.getRemotePort();
/*     */   }
/*     */   
/*     */   public boolean isSecure() {
/* 233 */     OperatedClientConnection conn = ensureConnection();
/* 234 */     return conn.isSecure();
/*     */   }
/*     */   
/*     */   public void bind(Socket socket) throws IOException {
/* 238 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Socket getSocket() {
/* 242 */     OperatedClientConnection conn = ensureConnection();
/* 243 */     return conn.getSocket();
/*     */   }
/*     */   
/*     */   public SSLSession getSSLSession() {
/* 247 */     OperatedClientConnection conn = ensureConnection();
/* 248 */     SSLSession result = null;
/* 249 */     Socket sock = conn.getSocket();
/* 250 */     if (sock instanceof SSLSocket) {
/* 251 */       result = ((SSLSocket)sock).getSession();
/*     */     }
/* 253 */     return result;
/*     */   }
/*     */   
/*     */   public Object getAttribute(String id) {
/* 257 */     OperatedClientConnection conn = ensureConnection();
/* 258 */     if (conn instanceof HttpContext) {
/* 259 */       return ((HttpContext)conn).getAttribute(id);
/*     */     }
/* 261 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object removeAttribute(String id) {
/* 266 */     OperatedClientConnection conn = ensureConnection();
/* 267 */     if (conn instanceof HttpContext) {
/* 268 */       return ((HttpContext)conn).removeAttribute(id);
/*     */     }
/* 270 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAttribute(String id, Object obj) {
/* 275 */     OperatedClientConnection conn = ensureConnection();
/* 276 */     if (conn instanceof HttpContext) {
/* 277 */       ((HttpContext)conn).setAttribute(id, obj);
/*     */     }
/*     */   }
/*     */   
/*     */   public HttpRoute getRoute() {
/* 282 */     HttpPoolEntry local = ensurePoolEntry();
/* 283 */     return local.getEffectiveRoute();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void open(HttpRoute route, HttpContext context, HttpParams params) throws IOException {
/*     */     OperatedClientConnection conn;
/* 290 */     Args.notNull(route, "Route");
/* 291 */     Args.notNull(params, "HTTP parameters");
/*     */     
/* 293 */     synchronized (this) {
/* 294 */       if (this.poolEntry == null) {
/* 295 */         throw new ConnectionShutdownException();
/*     */       }
/* 297 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 298 */       Asserts.notNull(tracker, "Route tracker");
/* 299 */       Asserts.check(!tracker.isConnected(), "Connection already open");
/* 300 */       conn = (OperatedClientConnection)this.poolEntry.getConnection();
/*     */     } 
/*     */     
/* 303 */     HttpHost proxy = route.getProxyHost();
/* 304 */     this.operator.openConnection(conn, (proxy != null) ? proxy : route.getTargetHost(), route.getLocalAddress(), context, params);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 310 */     synchronized (this) {
/* 311 */       if (this.poolEntry == null) {
/* 312 */         throw new InterruptedIOException();
/*     */       }
/* 314 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 315 */       if (proxy == null) {
/* 316 */         tracker.connectTarget(conn.isSecure());
/*     */       } else {
/* 318 */         tracker.connectProxy(proxy, conn.isSecure());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void tunnelTarget(boolean secure, HttpParams params) throws IOException {
/*     */     HttpHost target;
/*     */     OperatedClientConnection conn;
/* 325 */     Args.notNull(params, "HTTP parameters");
/*     */ 
/*     */     
/* 328 */     synchronized (this) {
/* 329 */       if (this.poolEntry == null) {
/* 330 */         throw new ConnectionShutdownException();
/*     */       }
/* 332 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 333 */       Asserts.notNull(tracker, "Route tracker");
/* 334 */       Asserts.check(tracker.isConnected(), "Connection not open");
/* 335 */       Asserts.check(!tracker.isTunnelled(), "Connection is already tunnelled");
/* 336 */       target = tracker.getTargetHost();
/* 337 */       conn = (OperatedClientConnection)this.poolEntry.getConnection();
/*     */     } 
/*     */     
/* 340 */     conn.update(null, target, secure, params);
/*     */     
/* 342 */     synchronized (this) {
/* 343 */       if (this.poolEntry == null) {
/* 344 */         throw new InterruptedIOException();
/*     */       }
/* 346 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 347 */       tracker.tunnelTarget(secure);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tunnelProxy(HttpHost next, boolean secure, HttpParams params) throws IOException {
/*     */     OperatedClientConnection conn;
/* 353 */     Args.notNull(next, "Next proxy");
/* 354 */     Args.notNull(params, "HTTP parameters");
/*     */     
/* 356 */     synchronized (this) {
/* 357 */       if (this.poolEntry == null) {
/* 358 */         throw new ConnectionShutdownException();
/*     */       }
/* 360 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 361 */       Asserts.notNull(tracker, "Route tracker");
/* 362 */       Asserts.check(tracker.isConnected(), "Connection not open");
/* 363 */       conn = (OperatedClientConnection)this.poolEntry.getConnection();
/*     */     } 
/*     */     
/* 366 */     conn.update(null, next, secure, params);
/*     */     
/* 368 */     synchronized (this) {
/* 369 */       if (this.poolEntry == null) {
/* 370 */         throw new InterruptedIOException();
/*     */       }
/* 372 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 373 */       tracker.tunnelProxy(next, secure);
/*     */     } 
/*     */   }
/*     */   public void layerProtocol(HttpContext context, HttpParams params) throws IOException {
/*     */     HttpHost target;
/*     */     OperatedClientConnection conn;
/* 379 */     Args.notNull(params, "HTTP parameters");
/*     */ 
/*     */     
/* 382 */     synchronized (this) {
/* 383 */       if (this.poolEntry == null) {
/* 384 */         throw new ConnectionShutdownException();
/*     */       }
/* 386 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 387 */       Asserts.notNull(tracker, "Route tracker");
/* 388 */       Asserts.check(tracker.isConnected(), "Connection not open");
/* 389 */       Asserts.check(tracker.isTunnelled(), "Protocol layering without a tunnel not supported");
/* 390 */       Asserts.check(!tracker.isLayered(), "Multiple protocol layering not supported");
/* 391 */       target = tracker.getTargetHost();
/* 392 */       conn = (OperatedClientConnection)this.poolEntry.getConnection();
/*     */     } 
/* 394 */     this.operator.updateSecureConnection(conn, target, context, params);
/*     */     
/* 396 */     synchronized (this) {
/* 397 */       if (this.poolEntry == null) {
/* 398 */         throw new InterruptedIOException();
/*     */       }
/* 400 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 401 */       tracker.layerProtocol(conn.isSecure());
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object getState() {
/* 406 */     HttpPoolEntry local = ensurePoolEntry();
/* 407 */     return local.getState();
/*     */   }
/*     */   
/*     */   public void setState(Object state) {
/* 411 */     HttpPoolEntry local = ensurePoolEntry();
/* 412 */     local.setState(state);
/*     */   }
/*     */   
/*     */   public void markReusable() {
/* 416 */     this.reusable = true;
/*     */   }
/*     */   
/*     */   public void unmarkReusable() {
/* 420 */     this.reusable = false;
/*     */   }
/*     */   
/*     */   public boolean isMarkedReusable() {
/* 424 */     return this.reusable;
/*     */   }
/*     */   
/*     */   public void setIdleDuration(long duration, TimeUnit unit) {
/* 428 */     if (duration > 0L) {
/* 429 */       this.duration = unit.toMillis(duration);
/*     */     } else {
/* 431 */       this.duration = -1L;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void releaseConnection() {
/* 436 */     synchronized (this) {
/* 437 */       if (this.poolEntry == null) {
/*     */         return;
/*     */       }
/* 440 */       this.manager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
/* 441 */       this.poolEntry = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void abortConnection() {
/* 446 */     synchronized (this) {
/* 447 */       if (this.poolEntry == null) {
/*     */         return;
/*     */       }
/* 450 */       this.reusable = false;
/* 451 */       OperatedClientConnection conn = (OperatedClientConnection)this.poolEntry.getConnection();
/*     */       try {
/* 453 */         conn.shutdown();
/* 454 */       } catch (IOException ignore) {}
/*     */       
/* 456 */       this.manager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
/* 457 */       this.poolEntry = null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/conn/ManagedClientConnectionImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */