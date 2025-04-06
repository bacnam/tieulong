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
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.ManagedClientConnection;
/*     */ import org.apache.http.conn.OperatedClientConnection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class AbstractClientConnAdapter
/*     */   implements ManagedClientConnection, HttpContext
/*     */ {
/*     */   private final ClientConnectionManager connManager;
/*     */   private volatile OperatedClientConnection wrappedConnection;
/*     */   private volatile boolean markedReusable;
/*     */   private volatile boolean released;
/*     */   private volatile long duration;
/*     */   
/*     */   protected AbstractClientConnAdapter(ClientConnectionManager mgr, OperatedClientConnection conn) {
/* 104 */     this.connManager = mgr;
/* 105 */     this.wrappedConnection = conn;
/* 106 */     this.markedReusable = false;
/* 107 */     this.released = false;
/* 108 */     this.duration = Long.MAX_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void detach() {
/* 116 */     this.wrappedConnection = null;
/* 117 */     this.duration = Long.MAX_VALUE;
/*     */   }
/*     */   
/*     */   protected OperatedClientConnection getWrappedConnection() {
/* 121 */     return this.wrappedConnection;
/*     */   }
/*     */   
/*     */   protected ClientConnectionManager getManager() {
/* 125 */     return this.connManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected final void assertNotAborted() throws InterruptedIOException {
/* 133 */     if (isReleased()) {
/* 134 */       throw new InterruptedIOException("Connection has been shut down");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isReleased() {
/* 143 */     return this.released;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void assertValid(OperatedClientConnection wrappedConn) throws ConnectionShutdownException {
/* 154 */     if (isReleased() || wrappedConn == null) {
/* 155 */       throw new ConnectionShutdownException();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isOpen() {
/* 160 */     OperatedClientConnection conn = getWrappedConnection();
/* 161 */     if (conn == null) {
/* 162 */       return false;
/*     */     }
/*     */     
/* 165 */     return conn.isOpen();
/*     */   }
/*     */   
/*     */   public boolean isStale() {
/* 169 */     if (isReleased()) {
/* 170 */       return true;
/*     */     }
/* 172 */     OperatedClientConnection conn = getWrappedConnection();
/* 173 */     if (conn == null) {
/* 174 */       return true;
/*     */     }
/*     */     
/* 177 */     return conn.isStale();
/*     */   }
/*     */   
/*     */   public void setSocketTimeout(int timeout) {
/* 181 */     OperatedClientConnection conn = getWrappedConnection();
/* 182 */     assertValid(conn);
/* 183 */     conn.setSocketTimeout(timeout);
/*     */   }
/*     */   
/*     */   public int getSocketTimeout() {
/* 187 */     OperatedClientConnection conn = getWrappedConnection();
/* 188 */     assertValid(conn);
/* 189 */     return conn.getSocketTimeout();
/*     */   }
/*     */   
/*     */   public HttpConnectionMetrics getMetrics() {
/* 193 */     OperatedClientConnection conn = getWrappedConnection();
/* 194 */     assertValid(conn);
/* 195 */     return conn.getMetrics();
/*     */   }
/*     */   
/*     */   public void flush() throws IOException {
/* 199 */     OperatedClientConnection conn = getWrappedConnection();
/* 200 */     assertValid(conn);
/* 201 */     conn.flush();
/*     */   }
/*     */   
/*     */   public boolean isResponseAvailable(int timeout) throws IOException {
/* 205 */     OperatedClientConnection conn = getWrappedConnection();
/* 206 */     assertValid(conn);
/* 207 */     return conn.isResponseAvailable(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public void receiveResponseEntity(HttpResponse response) throws HttpException, IOException {
/* 212 */     OperatedClientConnection conn = getWrappedConnection();
/* 213 */     assertValid(conn);
/* 214 */     unmarkReusable();
/* 215 */     conn.receiveResponseEntity(response);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
/* 220 */     OperatedClientConnection conn = getWrappedConnection();
/* 221 */     assertValid(conn);
/* 222 */     unmarkReusable();
/* 223 */     return conn.receiveResponseHeader();
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
/* 228 */     OperatedClientConnection conn = getWrappedConnection();
/* 229 */     assertValid(conn);
/* 230 */     unmarkReusable();
/* 231 */     conn.sendRequestEntity(request);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
/* 236 */     OperatedClientConnection conn = getWrappedConnection();
/* 237 */     assertValid(conn);
/* 238 */     unmarkReusable();
/* 239 */     conn.sendRequestHeader(request);
/*     */   }
/*     */   
/*     */   public InetAddress getLocalAddress() {
/* 243 */     OperatedClientConnection conn = getWrappedConnection();
/* 244 */     assertValid(conn);
/* 245 */     return conn.getLocalAddress();
/*     */   }
/*     */   
/*     */   public int getLocalPort() {
/* 249 */     OperatedClientConnection conn = getWrappedConnection();
/* 250 */     assertValid(conn);
/* 251 */     return conn.getLocalPort();
/*     */   }
/*     */   
/*     */   public InetAddress getRemoteAddress() {
/* 255 */     OperatedClientConnection conn = getWrappedConnection();
/* 256 */     assertValid(conn);
/* 257 */     return conn.getRemoteAddress();
/*     */   }
/*     */   
/*     */   public int getRemotePort() {
/* 261 */     OperatedClientConnection conn = getWrappedConnection();
/* 262 */     assertValid(conn);
/* 263 */     return conn.getRemotePort();
/*     */   }
/*     */   
/*     */   public boolean isSecure() {
/* 267 */     OperatedClientConnection conn = getWrappedConnection();
/* 268 */     assertValid(conn);
/* 269 */     return conn.isSecure();
/*     */   }
/*     */   
/*     */   public void bind(Socket socket) throws IOException {
/* 273 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Socket getSocket() {
/* 277 */     OperatedClientConnection conn = getWrappedConnection();
/* 278 */     assertValid(conn);
/* 279 */     if (!isOpen()) {
/* 280 */       return null;
/*     */     }
/* 282 */     return conn.getSocket();
/*     */   }
/*     */   
/*     */   public SSLSession getSSLSession() {
/* 286 */     OperatedClientConnection conn = getWrappedConnection();
/* 287 */     assertValid(conn);
/* 288 */     if (!isOpen()) {
/* 289 */       return null;
/*     */     }
/*     */     
/* 292 */     SSLSession result = null;
/* 293 */     Socket sock = conn.getSocket();
/* 294 */     if (sock instanceof SSLSocket) {
/* 295 */       result = ((SSLSocket)sock).getSession();
/*     */     }
/* 297 */     return result;
/*     */   }
/*     */   
/*     */   public void markReusable() {
/* 301 */     this.markedReusable = true;
/*     */   }
/*     */   
/*     */   public void unmarkReusable() {
/* 305 */     this.markedReusable = false;
/*     */   }
/*     */   
/*     */   public boolean isMarkedReusable() {
/* 309 */     return this.markedReusable;
/*     */   }
/*     */   
/*     */   public void setIdleDuration(long duration, TimeUnit unit) {
/* 313 */     if (duration > 0L) {
/* 314 */       this.duration = unit.toMillis(duration);
/*     */     } else {
/* 316 */       this.duration = -1L;
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void releaseConnection() {
/* 321 */     if (this.released) {
/*     */       return;
/*     */     }
/* 324 */     this.released = true;
/* 325 */     this.connManager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */   public synchronized void abortConnection() {
/* 329 */     if (this.released) {
/*     */       return;
/*     */     }
/* 332 */     this.released = true;
/* 333 */     unmarkReusable();
/*     */     try {
/* 335 */       shutdown();
/* 336 */     } catch (IOException ignore) {}
/*     */     
/* 338 */     this.connManager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */   public Object getAttribute(String id) {
/* 342 */     OperatedClientConnection conn = getWrappedConnection();
/* 343 */     assertValid(conn);
/* 344 */     if (conn instanceof HttpContext) {
/* 345 */       return ((HttpContext)conn).getAttribute(id);
/*     */     }
/* 347 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object removeAttribute(String id) {
/* 352 */     OperatedClientConnection conn = getWrappedConnection();
/* 353 */     assertValid(conn);
/* 354 */     if (conn instanceof HttpContext) {
/* 355 */       return ((HttpContext)conn).removeAttribute(id);
/*     */     }
/* 357 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAttribute(String id, Object obj) {
/* 362 */     OperatedClientConnection conn = getWrappedConnection();
/* 363 */     assertValid(conn);
/* 364 */     if (conn instanceof HttpContext)
/* 365 */       ((HttpContext)conn).setAttribute(id, obj); 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/conn/AbstractClientConnAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */