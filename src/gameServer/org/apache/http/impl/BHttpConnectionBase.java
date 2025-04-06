/*     */ package org.apache.http.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpConnection;
/*     */ import org.apache.http.HttpConnectionMetrics;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpInetConnection;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.config.MessageConstraints;
/*     */ import org.apache.http.entity.BasicHttpEntity;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
/*     */ import org.apache.http.impl.entity.LaxContentLengthStrategy;
/*     */ import org.apache.http.impl.entity.StrictContentLengthStrategy;
/*     */ import org.apache.http.impl.io.ChunkedInputStream;
/*     */ import org.apache.http.impl.io.ChunkedOutputStream;
/*     */ import org.apache.http.impl.io.ContentLengthInputStream;
/*     */ import org.apache.http.impl.io.ContentLengthOutputStream;
/*     */ import org.apache.http.impl.io.EmptyInputStream;
/*     */ import org.apache.http.impl.io.HttpTransportMetricsImpl;
/*     */ import org.apache.http.impl.io.IdentityInputStream;
/*     */ import org.apache.http.impl.io.IdentityOutputStream;
/*     */ import org.apache.http.impl.io.SessionInputBufferImpl;
/*     */ import org.apache.http.impl.io.SessionOutputBufferImpl;
/*     */ import org.apache.http.io.HttpTransportMetrics;
/*     */ import org.apache.http.io.SessionInputBuffer;
/*     */ import org.apache.http.io.SessionOutputBuffer;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
/*     */ import org.apache.http.util.NetUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class BHttpConnectionBase
/*     */   implements HttpConnection, HttpInetConnection
/*     */ {
/*     */   private final SessionInputBufferImpl inbuffer;
/*     */   private final SessionOutputBufferImpl outbuffer;
/*     */   private final MessageConstraints messageConstraints;
/*     */   private final HttpConnectionMetricsImpl connMetrics;
/*     */   private final ContentLengthStrategy incomingContentStrategy;
/*     */   private final ContentLengthStrategy outgoingContentStrategy;
/*     */   private final AtomicReference<Socket> socketHolder;
/*     */   
/*     */   protected BHttpConnectionBase(int buffersize, int fragmentSizeHint, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints messageConstraints, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy) {
/* 114 */     Args.positive(buffersize, "Buffer size");
/* 115 */     HttpTransportMetricsImpl inTransportMetrics = new HttpTransportMetricsImpl();
/* 116 */     HttpTransportMetricsImpl outTransportMetrics = new HttpTransportMetricsImpl();
/* 117 */     this.inbuffer = new SessionInputBufferImpl(inTransportMetrics, buffersize, -1, (messageConstraints != null) ? messageConstraints : MessageConstraints.DEFAULT, chardecoder);
/*     */     
/* 119 */     this.outbuffer = new SessionOutputBufferImpl(outTransportMetrics, buffersize, fragmentSizeHint, charencoder);
/*     */     
/* 121 */     this.messageConstraints = messageConstraints;
/* 122 */     this.connMetrics = new HttpConnectionMetricsImpl((HttpTransportMetrics)inTransportMetrics, (HttpTransportMetrics)outTransportMetrics);
/* 123 */     this.incomingContentStrategy = (incomingContentStrategy != null) ? incomingContentStrategy : (ContentLengthStrategy)LaxContentLengthStrategy.INSTANCE;
/*     */     
/* 125 */     this.outgoingContentStrategy = (outgoingContentStrategy != null) ? outgoingContentStrategy : (ContentLengthStrategy)StrictContentLengthStrategy.INSTANCE;
/*     */     
/* 127 */     this.socketHolder = new AtomicReference<Socket>();
/*     */   }
/*     */   
/*     */   protected void ensureOpen() throws IOException {
/* 131 */     Socket socket = this.socketHolder.get();
/* 132 */     Asserts.check((socket != null), "Connection is not open");
/* 133 */     if (!this.inbuffer.isBound()) {
/* 134 */       this.inbuffer.bind(getSocketInputStream(socket));
/*     */     }
/* 136 */     if (!this.outbuffer.isBound()) {
/* 137 */       this.outbuffer.bind(getSocketOutputStream(socket));
/*     */     }
/*     */   }
/*     */   
/*     */   protected InputStream getSocketInputStream(Socket socket) throws IOException {
/* 142 */     return socket.getInputStream();
/*     */   }
/*     */   
/*     */   protected OutputStream getSocketOutputStream(Socket socket) throws IOException {
/* 146 */     return socket.getOutputStream();
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
/*     */   protected void bind(Socket socket) throws IOException {
/* 160 */     Args.notNull(socket, "Socket");
/* 161 */     this.socketHolder.set(socket);
/* 162 */     this.inbuffer.bind(null);
/* 163 */     this.outbuffer.bind(null);
/*     */   }
/*     */   
/*     */   protected SessionInputBuffer getSessionInputBuffer() {
/* 167 */     return (SessionInputBuffer)this.inbuffer;
/*     */   }
/*     */   
/*     */   protected SessionOutputBuffer getSessionOutputBuffer() {
/* 171 */     return (SessionOutputBuffer)this.outbuffer;
/*     */   }
/*     */   
/*     */   protected void doFlush() throws IOException {
/* 175 */     this.outbuffer.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 180 */     return (this.socketHolder.get() != null);
/*     */   }
/*     */   
/*     */   protected Socket getSocket() {
/* 184 */     return this.socketHolder.get();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected OutputStream createOutputStream(long len, SessionOutputBuffer outbuffer) {
/* 190 */     if (len == -2L)
/* 191 */       return (OutputStream)new ChunkedOutputStream(2048, outbuffer); 
/* 192 */     if (len == -1L) {
/* 193 */       return (OutputStream)new IdentityOutputStream(outbuffer);
/*     */     }
/* 195 */     return (OutputStream)new ContentLengthOutputStream(outbuffer, len);
/*     */   }
/*     */ 
/*     */   
/*     */   protected OutputStream prepareOutput(HttpMessage message) throws HttpException {
/* 200 */     long len = this.outgoingContentStrategy.determineLength(message);
/* 201 */     return createOutputStream(len, (SessionOutputBuffer)this.outbuffer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InputStream createInputStream(long len, SessionInputBuffer inbuffer) {
/* 207 */     if (len == -2L)
/* 208 */       return (InputStream)new ChunkedInputStream(inbuffer, this.messageConstraints); 
/* 209 */     if (len == -1L)
/* 210 */       return (InputStream)new IdentityInputStream(inbuffer); 
/* 211 */     if (len == 0L) {
/* 212 */       return (InputStream)EmptyInputStream.INSTANCE;
/*     */     }
/* 214 */     return (InputStream)new ContentLengthInputStream(inbuffer, len);
/*     */   }
/*     */ 
/*     */   
/*     */   protected HttpEntity prepareInput(HttpMessage message) throws HttpException {
/* 219 */     BasicHttpEntity entity = new BasicHttpEntity();
/*     */     
/* 221 */     long len = this.incomingContentStrategy.determineLength(message);
/* 222 */     InputStream instream = createInputStream(len, (SessionInputBuffer)this.inbuffer);
/* 223 */     if (len == -2L) {
/* 224 */       entity.setChunked(true);
/* 225 */       entity.setContentLength(-1L);
/* 226 */       entity.setContent(instream);
/* 227 */     } else if (len == -1L) {
/* 228 */       entity.setChunked(false);
/* 229 */       entity.setContentLength(-1L);
/* 230 */       entity.setContent(instream);
/*     */     } else {
/* 232 */       entity.setChunked(false);
/* 233 */       entity.setContentLength(len);
/* 234 */       entity.setContent(instream);
/*     */     } 
/*     */     
/* 237 */     Header contentTypeHeader = message.getFirstHeader("Content-Type");
/* 238 */     if (contentTypeHeader != null) {
/* 239 */       entity.setContentType(contentTypeHeader);
/*     */     }
/* 241 */     Header contentEncodingHeader = message.getFirstHeader("Content-Encoding");
/* 242 */     if (contentEncodingHeader != null) {
/* 243 */       entity.setContentEncoding(contentEncodingHeader);
/*     */     }
/* 245 */     return (HttpEntity)entity;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getLocalAddress() {
/* 250 */     Socket socket = this.socketHolder.get();
/* 251 */     return (socket != null) ? socket.getLocalAddress() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLocalPort() {
/* 256 */     Socket socket = this.socketHolder.get();
/* 257 */     return (socket != null) ? socket.getLocalPort() : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getRemoteAddress() {
/* 262 */     Socket socket = this.socketHolder.get();
/* 263 */     return (socket != null) ? socket.getInetAddress() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRemotePort() {
/* 268 */     Socket socket = this.socketHolder.get();
/* 269 */     return (socket != null) ? socket.getPort() : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(int timeout) {
/* 274 */     Socket socket = this.socketHolder.get();
/* 275 */     if (socket != null) {
/*     */       try {
/* 277 */         socket.setSoTimeout(timeout);
/* 278 */       } catch (SocketException ignore) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSocketTimeout() {
/* 288 */     Socket socket = this.socketHolder.get();
/* 289 */     if (socket != null) {
/*     */       try {
/* 291 */         return socket.getSoTimeout();
/* 292 */       } catch (SocketException ignore) {
/* 293 */         return -1;
/*     */       } 
/*     */     }
/* 296 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() throws IOException {
/* 302 */     Socket socket = this.socketHolder.getAndSet(null);
/* 303 */     if (socket != null) {
/*     */ 
/*     */       
/* 306 */       try { socket.setSoLinger(true, 0); }
/* 307 */       catch (IOException ex) {  }
/*     */       finally
/* 309 */       { socket.close(); }
/*     */     
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 316 */     Socket socket = this.socketHolder.getAndSet(null);
/* 317 */     if (socket != null) {
/*     */       try {
/* 319 */         this.inbuffer.clear();
/* 320 */         this.outbuffer.flush();
/*     */         try {
/*     */           try {
/* 323 */             socket.shutdownOutput();
/* 324 */           } catch (IOException ignore) {}
/*     */           
/*     */           try {
/* 327 */             socket.shutdownInput();
/* 328 */           } catch (IOException ignore) {}
/*     */         }
/* 330 */         catch (UnsupportedOperationException ignore) {}
/*     */       }
/*     */       finally {
/*     */         
/* 334 */         socket.close();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private int fillInputBuffer(int timeout) throws IOException {
/* 340 */     Socket socket = this.socketHolder.get();
/* 341 */     int oldtimeout = socket.getSoTimeout();
/*     */     try {
/* 343 */       socket.setSoTimeout(timeout);
/* 344 */       return this.inbuffer.fillBuffer();
/*     */     } finally {
/* 346 */       socket.setSoTimeout(oldtimeout);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean awaitInput(int timeout) throws IOException {
/* 351 */     if (this.inbuffer.hasBufferedData()) {
/* 352 */       return true;
/*     */     }
/* 354 */     fillInputBuffer(timeout);
/* 355 */     return this.inbuffer.hasBufferedData();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStale() {
/* 360 */     if (!isOpen()) {
/* 361 */       return true;
/*     */     }
/*     */     try {
/* 364 */       int bytesRead = fillInputBuffer(1);
/* 365 */       return (bytesRead < 0);
/* 366 */     } catch (SocketTimeoutException ex) {
/* 367 */       return false;
/* 368 */     } catch (IOException ex) {
/* 369 */       return true;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void incrementRequestCount() {
/* 374 */     this.connMetrics.incrementRequestCount();
/*     */   }
/*     */   
/*     */   protected void incrementResponseCount() {
/* 378 */     this.connMetrics.incrementResponseCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpConnectionMetrics getMetrics() {
/* 383 */     return this.connMetrics;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 388 */     Socket socket = this.socketHolder.get();
/* 389 */     if (socket != null) {
/* 390 */       StringBuilder buffer = new StringBuilder();
/* 391 */       SocketAddress remoteAddress = socket.getRemoteSocketAddress();
/* 392 */       SocketAddress localAddress = socket.getLocalSocketAddress();
/* 393 */       if (remoteAddress != null && localAddress != null) {
/* 394 */         NetUtils.formatAddress(buffer, localAddress);
/* 395 */         buffer.append("<->");
/* 396 */         NetUtils.formatAddress(buffer, remoteAddress);
/*     */       } 
/* 398 */       return buffer.toString();
/*     */     } 
/* 400 */     return "[Not bound]";
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/BHttpConnectionBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */