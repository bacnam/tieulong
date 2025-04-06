/*     */ package org.apache.http.impl.nio.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.http.HttpConnectionMetrics;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.routing.RouteTracker;
/*     */ import org.apache.http.impl.conn.ConnectionShutdownException;
/*     */ import org.apache.http.nio.conn.ClientAsyncConnection;
/*     */ import org.apache.http.nio.conn.ClientAsyncConnectionFactory;
/*     */ import org.apache.http.nio.conn.ClientAsyncConnectionManager;
/*     */ import org.apache.http.nio.conn.ManagedClientAsyncConnection;
/*     */ import org.apache.http.nio.conn.scheme.AsyncScheme;
/*     */ import org.apache.http.nio.conn.scheme.AsyncSchemeRegistry;
/*     */ import org.apache.http.nio.conn.scheme.LayeringStrategy;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.nio.reactor.ssl.SSLIOSession;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ class ManagedClientAsyncConnectionImpl
/*     */   implements ManagedClientAsyncConnection
/*     */ {
/*     */   private final ClientAsyncConnectionManager manager;
/*     */   private final ClientAsyncConnectionFactory connFactory;
/*     */   private volatile HttpPoolEntry poolEntry;
/*     */   private volatile boolean reusable;
/*     */   private volatile long duration;
/*     */   
/*     */   ManagedClientAsyncConnectionImpl(ClientAsyncConnectionManager manager, ClientAsyncConnectionFactory connFactory, HttpPoolEntry poolEntry) {
/*  71 */     this.manager = manager;
/*  72 */     this.connFactory = connFactory;
/*  73 */     this.poolEntry = poolEntry;
/*  74 */     this.reusable = true;
/*  75 */     this.duration = Long.MAX_VALUE;
/*     */   }
/*     */   
/*     */   HttpPoolEntry getPoolEntry() {
/*  79 */     return this.poolEntry;
/*     */   }
/*     */   
/*     */   HttpPoolEntry detach() {
/*  83 */     HttpPoolEntry local = this.poolEntry;
/*  84 */     this.poolEntry = null;
/*  85 */     return local;
/*     */   }
/*     */   
/*     */   public ClientAsyncConnectionManager getManager() {
/*  89 */     return this.manager;
/*     */   }
/*     */   
/*     */   private ClientAsyncConnection getConnection() {
/*  93 */     HttpPoolEntry local = this.poolEntry;
/*  94 */     if (local == null) {
/*  95 */       return null;
/*     */     }
/*  97 */     IOSession session = (IOSession)local.getConnection();
/*  98 */     return (ClientAsyncConnection)session.getAttribute("http.connection");
/*     */   }
/*     */   
/*     */   private ClientAsyncConnection ensureConnection() {
/* 102 */     HttpPoolEntry local = this.poolEntry;
/* 103 */     if (local == null) {
/* 104 */       throw new ConnectionShutdownException();
/*     */     }
/* 106 */     IOSession session = (IOSession)local.getConnection();
/* 107 */     return (ClientAsyncConnection)session.getAttribute("http.connection");
/*     */   }
/*     */   
/*     */   private HttpPoolEntry ensurePoolEntry() {
/* 111 */     HttpPoolEntry local = this.poolEntry;
/* 112 */     if (local == null) {
/* 113 */       throw new ConnectionShutdownException();
/*     */     }
/* 115 */     return local;
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 119 */     ClientAsyncConnection conn = getConnection();
/* 120 */     if (conn != null) {
/* 121 */       conn.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public void shutdown() throws IOException {
/* 126 */     ClientAsyncConnection conn = getConnection();
/* 127 */     if (conn != null) {
/* 128 */       conn.shutdown();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isOpen() {
/* 133 */     ClientAsyncConnection conn = getConnection();
/* 134 */     if (conn != null) {
/* 135 */       return conn.isOpen();
/*     */     }
/* 137 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStale() {
/* 142 */     return isOpen();
/*     */   }
/*     */   
/*     */   public void setSocketTimeout(int timeout) {
/* 146 */     ClientAsyncConnection conn = ensureConnection();
/* 147 */     conn.setSocketTimeout(timeout);
/*     */   }
/*     */   
/*     */   public int getSocketTimeout() {
/* 151 */     ClientAsyncConnection conn = ensureConnection();
/* 152 */     return conn.getSocketTimeout();
/*     */   }
/*     */   
/*     */   public HttpConnectionMetrics getMetrics() {
/* 156 */     ClientAsyncConnection conn = ensureConnection();
/* 157 */     return conn.getMetrics();
/*     */   }
/*     */   
/*     */   public InetAddress getLocalAddress() {
/* 161 */     ClientAsyncConnection conn = ensureConnection();
/* 162 */     return conn.getLocalAddress();
/*     */   }
/*     */   
/*     */   public int getLocalPort() {
/* 166 */     ClientAsyncConnection conn = ensureConnection();
/* 167 */     return conn.getLocalPort();
/*     */   }
/*     */   
/*     */   public InetAddress getRemoteAddress() {
/* 171 */     ClientAsyncConnection conn = ensureConnection();
/* 172 */     return conn.getRemoteAddress();
/*     */   }
/*     */   
/*     */   public int getRemotePort() {
/* 176 */     ClientAsyncConnection conn = ensureConnection();
/* 177 */     return conn.getRemotePort();
/*     */   }
/*     */   
/*     */   public int getStatus() {
/* 181 */     ClientAsyncConnection conn = ensureConnection();
/* 182 */     return conn.getStatus();
/*     */   }
/*     */   
/*     */   public HttpRequest getHttpRequest() {
/* 186 */     ClientAsyncConnection conn = ensureConnection();
/* 187 */     return conn.getHttpRequest();
/*     */   }
/*     */   
/*     */   public HttpResponse getHttpResponse() {
/* 191 */     ClientAsyncConnection conn = ensureConnection();
/* 192 */     return conn.getHttpResponse();
/*     */   }
/*     */   
/*     */   public HttpContext getContext() {
/* 196 */     ClientAsyncConnection conn = ensureConnection();
/* 197 */     return conn.getContext();
/*     */   }
/*     */   
/*     */   public void requestInput() {
/* 201 */     ClientAsyncConnection conn = ensureConnection();
/* 202 */     conn.requestInput();
/*     */   }
/*     */   
/*     */   public void suspendInput() {
/* 206 */     ClientAsyncConnection conn = ensureConnection();
/* 207 */     conn.suspendInput();
/*     */   }
/*     */   
/*     */   public void requestOutput() {
/* 211 */     ClientAsyncConnection conn = ensureConnection();
/* 212 */     conn.requestOutput();
/*     */   }
/*     */   
/*     */   public void suspendOutput() {
/* 216 */     ClientAsyncConnection conn = ensureConnection();
/* 217 */     conn.suspendOutput();
/*     */   }
/*     */   
/*     */   public void submitRequest(HttpRequest request) throws IOException, HttpException {
/* 221 */     ClientAsyncConnection conn = ensureConnection();
/* 222 */     conn.submitRequest(request);
/*     */   }
/*     */   
/*     */   public boolean isRequestSubmitted() {
/* 226 */     ClientAsyncConnection conn = ensureConnection();
/* 227 */     return conn.isRequestSubmitted();
/*     */   }
/*     */   
/*     */   public void resetOutput() {
/* 231 */     ClientAsyncConnection conn = ensureConnection();
/* 232 */     conn.resetOutput();
/*     */   }
/*     */   
/*     */   public void resetInput() {
/* 236 */     ClientAsyncConnection conn = ensureConnection();
/* 237 */     conn.resetInput();
/*     */   }
/*     */   
/*     */   public boolean isSecure() {
/* 241 */     ClientAsyncConnection conn = ensureConnection();
/* 242 */     return conn.getIOSession() instanceof SSLIOSession;
/*     */   }
/*     */   
/*     */   public HttpRoute getRoute() {
/* 246 */     HttpPoolEntry entry = ensurePoolEntry();
/* 247 */     return entry.getEffectiveRoute();
/*     */   }
/*     */   
/*     */   public SSLSession getSSLSession() {
/* 251 */     ClientAsyncConnection conn = ensureConnection();
/* 252 */     IOSession iosession = conn.getIOSession();
/* 253 */     if (iosession instanceof SSLIOSession) {
/* 254 */       return ((SSLIOSession)iosession).getSSLSession();
/*     */     }
/* 256 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getState() {
/* 261 */     HttpPoolEntry entry = ensurePoolEntry();
/* 262 */     return entry.getState();
/*     */   }
/*     */   
/*     */   public void setState(Object state) {
/* 266 */     HttpPoolEntry entry = ensurePoolEntry();
/* 267 */     entry.setState(state);
/*     */   }
/*     */   
/*     */   public void markReusable() {
/* 271 */     this.reusable = true;
/*     */   }
/*     */   
/*     */   public void unmarkReusable() {
/* 275 */     this.reusable = false;
/*     */   }
/*     */   
/*     */   public boolean isMarkedReusable() {
/* 279 */     return this.reusable;
/*     */   }
/*     */   
/*     */   public void setIdleDuration(long duration, TimeUnit unit) {
/* 283 */     if (duration > 0L) {
/* 284 */       this.duration = unit.toMillis(duration);
/*     */     } else {
/* 286 */       this.duration = -1L;
/*     */     } 
/*     */   }
/*     */   
/*     */   private AsyncSchemeRegistry getSchemeRegistry(HttpContext context) {
/* 291 */     AsyncSchemeRegistry reg = (AsyncSchemeRegistry)context.getAttribute("http.scheme-registry");
/*     */     
/* 293 */     if (reg == null) {
/* 294 */       reg = this.manager.getSchemeRegistry();
/*     */     }
/* 296 */     return reg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void open(HttpRoute route, HttpContext context, HttpParams params) throws IOException {
/* 303 */     HttpPoolEntry entry = ensurePoolEntry();
/* 304 */     RouteTracker tracker = entry.getTracker();
/* 305 */     if (tracker.isConnected()) {
/* 306 */       throw new IllegalStateException("Connection already open");
/*     */     }
/*     */     
/* 309 */     HttpHost target = route.getTargetHost();
/* 310 */     HttpHost proxy = route.getProxyHost();
/* 311 */     IOSession iosession = (IOSession)entry.getConnection();
/*     */     
/* 313 */     if (proxy == null) {
/* 314 */       AsyncScheme scheme = getSchemeRegistry(context).getScheme(target);
/* 315 */       LayeringStrategy layeringStrategy = scheme.getLayeringStrategy();
/* 316 */       if (layeringStrategy != null) {
/* 317 */         iosession = layeringStrategy.layer(iosession);
/*     */       }
/*     */     } 
/*     */     
/* 321 */     ClientAsyncConnection conn = this.connFactory.create("http-outgoing-" + entry.getId(), iosession, params);
/*     */ 
/*     */ 
/*     */     
/* 325 */     iosession.setAttribute("http.connection", conn);
/*     */     
/* 327 */     if (proxy == null) {
/* 328 */       tracker.connectTarget(conn.getIOSession() instanceof SSLIOSession);
/*     */     } else {
/* 330 */       tracker.connectProxy(proxy, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void tunnelProxy(HttpHost next, HttpParams params) throws IOException {
/* 336 */     HttpPoolEntry entry = ensurePoolEntry();
/* 337 */     RouteTracker tracker = entry.getTracker();
/* 338 */     if (!tracker.isConnected()) {
/* 339 */       throw new IllegalStateException("Connection not open");
/*     */     }
/* 341 */     tracker.tunnelProxy(next, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void tunnelTarget(HttpParams params) throws IOException {
/* 346 */     HttpPoolEntry entry = ensurePoolEntry();
/* 347 */     RouteTracker tracker = entry.getTracker();
/* 348 */     if (!tracker.isConnected()) {
/* 349 */       throw new IllegalStateException("Connection not open");
/*     */     }
/* 351 */     if (tracker.isTunnelled()) {
/* 352 */       throw new IllegalStateException("Connection is already tunnelled");
/*     */     }
/* 354 */     tracker.tunnelTarget(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void layerProtocol(HttpContext context, HttpParams params) throws IOException {
/* 359 */     HttpPoolEntry entry = ensurePoolEntry();
/* 360 */     RouteTracker tracker = entry.getTracker();
/* 361 */     if (!tracker.isConnected()) {
/* 362 */       throw new IllegalStateException("Connection not open");
/*     */     }
/* 364 */     if (!tracker.isTunnelled()) {
/* 365 */       throw new IllegalStateException("Protocol layering without a tunnel not supported");
/*     */     }
/* 367 */     if (tracker.isLayered()) {
/* 368 */       throw new IllegalStateException("Multiple protocol layering not supported");
/*     */     }
/* 370 */     HttpHost target = tracker.getTargetHost();
/* 371 */     AsyncScheme scheme = getSchemeRegistry(context).getScheme(target);
/* 372 */     LayeringStrategy layeringStrategy = scheme.getLayeringStrategy();
/* 373 */     if (layeringStrategy == null) {
/* 374 */       throw new IllegalStateException(scheme.getName() + " scheme does not provider support for protocol layering");
/*     */     }
/*     */     
/* 377 */     IOSession iosession = (IOSession)entry.getConnection();
/* 378 */     ClientAsyncConnection conn = (ClientAsyncConnection)iosession.getAttribute("http.connection");
/*     */     
/* 380 */     conn.upgrade(layeringStrategy.layer(iosession));
/* 381 */     tracker.layerProtocol(layeringStrategy.isSecure());
/*     */   }
/*     */   
/*     */   public synchronized void releaseConnection() {
/* 385 */     if (this.poolEntry == null) {
/*     */       return;
/*     */     }
/* 388 */     this.manager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
/* 389 */     this.poolEntry = null;
/*     */   }
/*     */   
/*     */   public synchronized void abortConnection() {
/* 393 */     if (this.poolEntry == null) {
/*     */       return;
/*     */     }
/* 396 */     this.reusable = false;
/* 397 */     IOSession iosession = (IOSession)this.poolEntry.getConnection();
/* 398 */     ClientAsyncConnection conn = (ClientAsyncConnection)iosession.getAttribute("http.connection");
/*     */     
/*     */     try {
/* 401 */       conn.shutdown();
/* 402 */     } catch (IOException ignore) {}
/*     */     
/* 404 */     this.manager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
/* 405 */     this.poolEntry = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String toString() {
/* 410 */     if (this.poolEntry != null) {
/* 411 */       return this.poolEntry.toString();
/*     */     }
/* 413 */     return "released";
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/conn/ManagedClientAsyncConnectionImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */