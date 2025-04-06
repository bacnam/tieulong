/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.Socket;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.config.MessageConstraints;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
/*     */ import org.apache.http.io.HttpMessageParserFactory;
/*     */ import org.apache.http.io.HttpMessageWriterFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ class LoggingManagedHttpClientConnection
/*     */   extends DefaultManagedHttpClientConnection
/*     */ {
/*     */   private final Log log;
/*     */   private final Log headerlog;
/*     */   private final Wire wire;
/*     */   
/*     */   public LoggingManagedHttpClientConnection(String id, Log log, Log headerlog, Log wirelog, int buffersize, int fragmentSizeHint, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, HttpMessageWriterFactory<HttpRequest> requestWriterFactory, HttpMessageParserFactory<HttpResponse> responseParserFactory) {
/*  68 */     super(id, buffersize, fragmentSizeHint, chardecoder, charencoder, constraints, incomingContentStrategy, outgoingContentStrategy, requestWriterFactory, responseParserFactory);
/*     */ 
/*     */     
/*  71 */     this.log = log;
/*  72 */     this.headerlog = headerlog;
/*  73 */     this.wire = new Wire(wirelog, id);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  78 */     if (this.log.isDebugEnabled()) {
/*  79 */       this.log.debug(getId() + ": Close connection");
/*     */     }
/*  81 */     super.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() throws IOException {
/*  86 */     if (this.log.isDebugEnabled()) {
/*  87 */       this.log.debug(getId() + ": Shutdown connection");
/*     */     }
/*  89 */     super.shutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   protected InputStream getSocketInputStream(Socket socket) throws IOException {
/*  94 */     InputStream in = super.getSocketInputStream(socket);
/*  95 */     if (this.wire.enabled()) {
/*  96 */       in = new LoggingInputStream(in, this.wire);
/*     */     }
/*  98 */     return in;
/*     */   }
/*     */ 
/*     */   
/*     */   protected OutputStream getSocketOutputStream(Socket socket) throws IOException {
/* 103 */     OutputStream out = super.getSocketOutputStream(socket);
/* 104 */     if (this.wire.enabled()) {
/* 105 */       out = new LoggingOutputStream(out, this.wire);
/*     */     }
/* 107 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onResponseReceived(HttpResponse response) {
/* 112 */     if (response != null && this.headerlog.isDebugEnabled()) {
/* 113 */       this.headerlog.debug(getId() + " << " + response.getStatusLine().toString());
/* 114 */       Header[] headers = response.getAllHeaders();
/* 115 */       for (Header header : headers) {
/* 116 */         this.headerlog.debug(getId() + " << " + header.toString());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onRequestSubmitted(HttpRequest request) {
/* 123 */     if (request != null && this.headerlog.isDebugEnabled()) {
/* 124 */       this.headerlog.debug(getId() + " >> " + request.getRequestLine().toString());
/* 125 */       Header[] headers = request.getAllHeaders();
/* 126 */       for (Header header : headers)
/* 127 */         this.headerlog.debug(getId() + " >> " + header.toString()); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/conn/LoggingManagedHttpClientConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */