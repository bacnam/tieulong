/*     */ package org.apache.http.impl.nio.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.http.HttpConnectionMetrics;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.impl.conn.ConnectionShutdownException;
/*     */ import org.apache.http.nio.NHttpClientConnection;
/*     */ import org.apache.http.nio.conn.ManagedNHttpClientConnection;
/*     */ import org.apache.http.nio.reactor.IOSession;
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
/*     */ @NotThreadSafe
/*     */ class CPoolProxy
/*     */   implements ManagedNHttpClientConnection
/*     */ {
/*     */   private volatile CPoolEntry poolEntry;
/*     */   
/*     */   CPoolProxy(CPoolEntry entry) {
/*  52 */     this.poolEntry = entry;
/*     */   }
/*     */   
/*     */   CPoolEntry getPoolEntry() {
/*  56 */     return this.poolEntry;
/*     */   }
/*     */   
/*     */   CPoolEntry detach() {
/*  60 */     CPoolEntry local = this.poolEntry;
/*  61 */     this.poolEntry = null;
/*  62 */     return local;
/*     */   }
/*     */   
/*     */   ManagedNHttpClientConnection getConnection() {
/*  66 */     CPoolEntry local = this.poolEntry;
/*  67 */     if (local == null) {
/*  68 */       return null;
/*     */     }
/*  70 */     return (ManagedNHttpClientConnection)local.getConnection();
/*     */   }
/*     */   
/*     */   ManagedNHttpClientConnection getValidConnection() {
/*  74 */     ManagedNHttpClientConnection conn = getConnection();
/*  75 */     if (conn == null) {
/*  76 */       throw new ConnectionShutdownException();
/*     */     }
/*  78 */     return conn;
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/*  82 */     CPoolEntry local = this.poolEntry;
/*  83 */     if (local != null) {
/*  84 */       local.closeConnection();
/*     */     }
/*     */   }
/*     */   
/*     */   public void shutdown() throws IOException {
/*  89 */     CPoolEntry local = this.poolEntry;
/*  90 */     if (local != null) {
/*  91 */       local.shutdownConnection();
/*     */     }
/*     */   }
/*     */   
/*     */   public HttpConnectionMetrics getMetrics() {
/*  96 */     return getValidConnection().getMetrics();
/*     */   }
/*     */   
/*     */   public void requestInput() {
/* 100 */     ManagedNHttpClientConnection managedNHttpClientConnection = getConnection();
/* 101 */     if (managedNHttpClientConnection != null) {
/* 102 */       managedNHttpClientConnection.requestInput();
/*     */     }
/*     */   }
/*     */   
/*     */   public void suspendInput() {
/* 107 */     ManagedNHttpClientConnection managedNHttpClientConnection = getConnection();
/* 108 */     if (managedNHttpClientConnection != null) {
/* 109 */       managedNHttpClientConnection.suspendInput();
/*     */     }
/*     */   }
/*     */   
/*     */   public void requestOutput() {
/* 114 */     ManagedNHttpClientConnection managedNHttpClientConnection = getConnection();
/* 115 */     if (managedNHttpClientConnection != null) {
/* 116 */       managedNHttpClientConnection.requestOutput();
/*     */     }
/*     */   }
/*     */   
/*     */   public void suspendOutput() {
/* 121 */     ManagedNHttpClientConnection managedNHttpClientConnection = getConnection();
/* 122 */     if (managedNHttpClientConnection != null) {
/* 123 */       managedNHttpClientConnection.suspendOutput();
/*     */     }
/*     */   }
/*     */   
/*     */   public InetAddress getLocalAddress() {
/* 128 */     return getValidConnection().getLocalAddress();
/*     */   }
/*     */   
/*     */   public int getLocalPort() {
/* 132 */     return getValidConnection().getLocalPort();
/*     */   }
/*     */   
/*     */   public InetAddress getRemoteAddress() {
/* 136 */     return getValidConnection().getRemoteAddress();
/*     */   }
/*     */   
/*     */   public int getRemotePort() {
/* 140 */     return getValidConnection().getRemotePort();
/*     */   }
/*     */   
/*     */   public boolean isOpen() {
/* 144 */     CPoolEntry local = this.poolEntry;
/* 145 */     if (local != null) {
/* 146 */       return !local.isClosed();
/*     */     }
/* 148 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStale() {
/* 153 */     ManagedNHttpClientConnection managedNHttpClientConnection = getConnection();
/* 154 */     if (managedNHttpClientConnection != null) {
/* 155 */       return (managedNHttpClientConnection.isStale() || !managedNHttpClientConnection.isOpen());
/*     */     }
/* 157 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(int i) {
/* 162 */     getValidConnection().setSocketTimeout(i);
/*     */   }
/*     */   
/*     */   public int getSocketTimeout() {
/* 166 */     return getValidConnection().getSocketTimeout();
/*     */   }
/*     */   
/*     */   public void submitRequest(HttpRequest request) throws IOException, HttpException {
/* 170 */     getValidConnection().submitRequest(request);
/*     */   }
/*     */   
/*     */   public boolean isRequestSubmitted() {
/* 174 */     return getValidConnection().isRequestSubmitted();
/*     */   }
/*     */   
/*     */   public void resetOutput() {
/* 178 */     getValidConnection().resetOutput();
/*     */   }
/*     */   
/*     */   public void resetInput() {
/* 182 */     getValidConnection().resetInput();
/*     */   }
/*     */   
/*     */   public int getStatus() {
/* 186 */     return getValidConnection().getStatus();
/*     */   }
/*     */   
/*     */   public HttpRequest getHttpRequest() {
/* 190 */     return getValidConnection().getHttpRequest();
/*     */   }
/*     */   
/*     */   public HttpResponse getHttpResponse() {
/* 194 */     return getValidConnection().getHttpResponse();
/*     */   }
/*     */   
/*     */   public HttpContext getContext() {
/* 198 */     return getValidConnection().getContext();
/*     */   }
/*     */   
/*     */   public static NHttpClientConnection newProxy(CPoolEntry poolEntry) {
/* 202 */     return (NHttpClientConnection)new CPoolProxy(poolEntry);
/*     */   }
/*     */   
/*     */   private static CPoolProxy getProxy(NHttpClientConnection conn) {
/* 206 */     if (!CPoolProxy.class.isInstance(conn)) {
/* 207 */       throw new IllegalStateException("Unexpected connection proxy class: " + conn.getClass());
/*     */     }
/* 209 */     return CPoolProxy.class.cast(conn);
/*     */   }
/*     */   
/*     */   public static CPoolEntry getPoolEntry(NHttpClientConnection proxy) {
/* 213 */     CPoolEntry entry = getProxy(proxy).getPoolEntry();
/* 214 */     if (entry == null) {
/* 215 */       throw new ConnectionShutdownException();
/*     */     }
/* 217 */     return entry;
/*     */   }
/*     */   
/*     */   public static CPoolEntry detach(NHttpClientConnection proxy) {
/* 221 */     return getProxy(proxy).detach();
/*     */   }
/*     */   
/*     */   public String getId() {
/* 225 */     return getValidConnection().getId();
/*     */   }
/*     */   
/*     */   public void bind(IOSession iosession) {
/* 229 */     getValidConnection().bind(iosession);
/*     */   }
/*     */   
/*     */   public IOSession getIOSession() {
/* 233 */     return getValidConnection().getIOSession();
/*     */   }
/*     */   
/*     */   public SSLSession getSSLSession() {
/* 237 */     return getValidConnection().getSSLSession();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 242 */     StringBuilder sb = new StringBuilder("CPoolProxy{");
/* 243 */     ManagedNHttpClientConnection conn = getConnection();
/* 244 */     if (conn != null) {
/* 245 */       sb.append(conn);
/*     */     } else {
/* 247 */       sb.append("detached");
/*     */     } 
/* 249 */     sb.append('}');
/* 250 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/conn/CPoolProxy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */