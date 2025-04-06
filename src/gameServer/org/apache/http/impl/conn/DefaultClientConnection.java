/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.Socket;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.conn.ManagedHttpClientConnection;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.impl.SocketHttpClientConnection;
/*     */ import org.apache.http.io.HttpMessageParser;
/*     */ import org.apache.http.io.SessionInputBuffer;
/*     */ import org.apache.http.io.SessionOutputBuffer;
/*     */ import org.apache.http.params.BasicHttpParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.params.HttpProtocolParams;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ @Deprecated
/*     */ @NotThreadSafe
/*     */ public class DefaultClientConnection
/*     */   extends SocketHttpClientConnection
/*     */   implements OperatedClientConnection, ManagedHttpClientConnection, HttpContext
/*     */ {
/*  72 */   private final Log log = LogFactory.getLog(getClass());
/*  73 */   private final Log headerLog = LogFactory.getLog("org.apache.http.headers");
/*  74 */   private final Log wireLog = LogFactory.getLog("org.apache.http.wire");
/*     */ 
/*     */   
/*     */   private volatile Socket socket;
/*     */ 
/*     */   
/*     */   private HttpHost targetHost;
/*     */ 
/*     */   
/*     */   private boolean connSecure;
/*     */ 
/*     */   
/*     */   private volatile boolean shutdown;
/*     */ 
/*     */   
/*     */   private final Map<String, Object> attributes;
/*     */ 
/*     */   
/*     */   public DefaultClientConnection() {
/*  93 */     this.attributes = new HashMap<String, Object>();
/*     */   }
/*     */   
/*     */   public String getId() {
/*  97 */     return null;
/*     */   }
/*     */   
/*     */   public final HttpHost getTargetHost() {
/* 101 */     return this.targetHost;
/*     */   }
/*     */   
/*     */   public final boolean isSecure() {
/* 105 */     return this.connSecure;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Socket getSocket() {
/* 110 */     return this.socket;
/*     */   }
/*     */   
/*     */   public SSLSession getSSLSession() {
/* 114 */     if (this.socket instanceof SSLSocket) {
/* 115 */       return ((SSLSocket)this.socket).getSession();
/*     */     }
/* 117 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void opening(Socket sock, HttpHost target) throws IOException {
/* 122 */     assertNotOpen();
/* 123 */     this.socket = sock;
/* 124 */     this.targetHost = target;
/*     */ 
/*     */     
/* 127 */     if (this.shutdown) {
/* 128 */       sock.close();
/*     */       
/* 130 */       throw new InterruptedIOException("Connection already shutdown");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void openCompleted(boolean secure, HttpParams params) throws IOException {
/* 135 */     Args.notNull(params, "Parameters");
/* 136 */     assertNotOpen();
/* 137 */     this.connSecure = secure;
/* 138 */     bind(this.socket, params);
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
/*     */   public void shutdown() throws IOException {
/* 156 */     this.shutdown = true;
/*     */     try {
/* 158 */       super.shutdown();
/* 159 */       if (this.log.isDebugEnabled()) {
/* 160 */         this.log.debug("Connection " + this + " shut down");
/*     */       }
/* 162 */       Socket sock = this.socket;
/* 163 */       if (sock != null) {
/* 164 */         sock.close();
/*     */       }
/* 166 */     } catch (IOException ex) {
/* 167 */       this.log.debug("I/O error shutting down connection", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*     */     try {
/* 174 */       super.close();
/* 175 */       if (this.log.isDebugEnabled()) {
/* 176 */         this.log.debug("Connection " + this + " closed");
/*     */       }
/* 178 */     } catch (IOException ex) {
/* 179 */       this.log.debug("I/O error closing connection", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SessionInputBuffer createSessionInputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
/* 188 */     SessionInputBuffer inbuffer = super.createSessionInputBuffer(socket, (buffersize > 0) ? buffersize : 8192, params);
/*     */ 
/*     */ 
/*     */     
/* 192 */     if (this.wireLog.isDebugEnabled()) {
/* 193 */       inbuffer = new LoggingSessionInputBuffer(inbuffer, new Wire(this.wireLog), HttpProtocolParams.getHttpElementCharset(params));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 198 */     return inbuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SessionOutputBuffer createSessionOutputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
/* 206 */     SessionOutputBuffer outbuffer = super.createSessionOutputBuffer(socket, (buffersize > 0) ? buffersize : 8192, params);
/*     */ 
/*     */ 
/*     */     
/* 210 */     if (this.wireLog.isDebugEnabled()) {
/* 211 */       outbuffer = new LoggingSessionOutputBuffer(outbuffer, new Wire(this.wireLog), HttpProtocolParams.getHttpElementCharset(params));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 216 */     return outbuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpMessageParser<HttpResponse> createResponseParser(SessionInputBuffer buffer, HttpResponseFactory responseFactory, HttpParams params) {
/* 225 */     return (HttpMessageParser<HttpResponse>)new DefaultHttpResponseParser(buffer, null, responseFactory, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public void bind(Socket socket) throws IOException {
/* 230 */     bind(socket, (HttpParams)new BasicHttpParams());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(Socket sock, HttpHost target, boolean secure, HttpParams params) throws IOException {
/* 237 */     assertOpen();
/* 238 */     Args.notNull(target, "Target host");
/* 239 */     Args.notNull(params, "Parameters");
/*     */     
/* 241 */     if (sock != null) {
/* 242 */       this.socket = sock;
/* 243 */       bind(sock, params);
/*     */     } 
/* 245 */     this.targetHost = target;
/* 246 */     this.connSecure = secure;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
/* 251 */     HttpResponse response = super.receiveResponseHeader();
/* 252 */     if (this.log.isDebugEnabled()) {
/* 253 */       this.log.debug("Receiving response: " + response.getStatusLine());
/*     */     }
/* 255 */     if (this.headerLog.isDebugEnabled()) {
/* 256 */       this.headerLog.debug("<< " + response.getStatusLine().toString());
/* 257 */       Header[] headers = response.getAllHeaders();
/* 258 */       for (Header header : headers) {
/* 259 */         this.headerLog.debug("<< " + header.toString());
/*     */       }
/*     */     } 
/* 262 */     return response;
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
/* 267 */     if (this.log.isDebugEnabled()) {
/* 268 */       this.log.debug("Sending request: " + request.getRequestLine());
/*     */     }
/* 270 */     super.sendRequestHeader(request);
/* 271 */     if (this.headerLog.isDebugEnabled()) {
/* 272 */       this.headerLog.debug(">> " + request.getRequestLine().toString());
/* 273 */       Header[] headers = request.getAllHeaders();
/* 274 */       for (Header header : headers) {
/* 275 */         this.headerLog.debug(">> " + header.toString());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object getAttribute(String id) {
/* 281 */     return this.attributes.get(id);
/*     */   }
/*     */   
/*     */   public Object removeAttribute(String id) {
/* 285 */     return this.attributes.remove(id);
/*     */   }
/*     */   
/*     */   public void setAttribute(String id, Object obj) {
/* 289 */     this.attributes.put(id, obj);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/conn/DefaultClientConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */