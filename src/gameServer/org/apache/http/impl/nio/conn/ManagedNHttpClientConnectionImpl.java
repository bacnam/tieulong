/*     */ package org.apache.http.impl.nio.conn;
/*     */ 
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.config.MessageConstraints;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
/*     */ import org.apache.http.impl.nio.DefaultNHttpClientConnection;
/*     */ import org.apache.http.nio.NHttpMessageParserFactory;
/*     */ import org.apache.http.nio.NHttpMessageWriterFactory;
/*     */ import org.apache.http.nio.conn.ManagedNHttpClientConnection;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.nio.reactor.ssl.SSLIOSession;
/*     */ import org.apache.http.nio.util.ByteBufferAllocator;
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
/*     */ class ManagedNHttpClientConnectionImpl
/*     */   extends DefaultNHttpClientConnection
/*     */   implements ManagedNHttpClientConnection
/*     */ {
/*     */   private final Log headerlog;
/*     */   private final Log wirelog;
/*     */   private final Log log;
/*     */   private final String id;
/*     */   private IOSession original;
/*     */   
/*     */   public ManagedNHttpClientConnectionImpl(String id, Log log, Log headerlog, Log wirelog, IOSession iosession, int buffersize, int fragmentSizeHint, ByteBufferAllocator allocator, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, NHttpMessageWriterFactory<HttpRequest> requestWriterFactory, NHttpMessageParserFactory<HttpResponse> responseParserFactory) {
/*  76 */     super(iosession, buffersize, fragmentSizeHint, allocator, chardecoder, charencoder, constraints, incomingContentStrategy, outgoingContentStrategy, requestWriterFactory, responseParserFactory);
/*     */ 
/*     */     
/*  79 */     this.id = id;
/*  80 */     this.log = log;
/*  81 */     this.headerlog = headerlog;
/*  82 */     this.wirelog = wirelog;
/*  83 */     this.original = iosession;
/*  84 */     if (this.log.isDebugEnabled() || this.wirelog.isDebugEnabled()) {
/*  85 */       super.bind(new LoggingIOSession(iosession, this.id, this.log, this.wirelog));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void bind(IOSession iosession) {
/*  91 */     Args.notNull(iosession, "I/O session");
/*  92 */     Asserts.check(!iosession.isClosed(), "I/O session is closed");
/*  93 */     this.status = 0;
/*  94 */     this.original = iosession;
/*  95 */     if (this.log.isDebugEnabled() || this.wirelog.isDebugEnabled()) {
/*  96 */       this.log.debug(this.id + " Upgrade session " + iosession);
/*  97 */       super.bind(new LoggingIOSession(iosession, this.id, this.log, this.wirelog));
/*     */     } else {
/*  99 */       super.bind(iosession);
/*     */     } 
/*     */   }
/*     */   
/*     */   public IOSession getIOSession() {
/* 104 */     return this.original;
/*     */   }
/*     */   
/*     */   public SSLSession getSSLSession() {
/* 108 */     if (this.original instanceof SSLIOSession) {
/* 109 */       return ((SSLIOSession)this.original).getSSLSession();
/*     */     }
/* 111 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/* 116 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onResponseReceived(HttpResponse response) {
/* 121 */     if (response != null && this.headerlog.isDebugEnabled()) {
/* 122 */       this.headerlog.debug(this.id + " << " + response.getStatusLine().toString());
/* 123 */       Header[] headers = response.getAllHeaders();
/* 124 */       for (Header header : headers) {
/* 125 */         this.headerlog.debug(this.id + " << " + header.toString());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onRequestSubmitted(HttpRequest request) {
/* 132 */     if (request != null && this.headerlog.isDebugEnabled()) {
/* 133 */       this.headerlog.debug(this.id + " >> " + request.getRequestLine().toString());
/* 134 */       Header[] headers = request.getAllHeaders();
/* 135 */       for (Header header : headers) {
/* 136 */         this.headerlog.debug(this.id + " >> " + header.toString());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 143 */     StringBuilder buf = new StringBuilder();
/* 144 */     buf.append(this.id);
/* 145 */     buf.append(" [");
/* 146 */     switch (this.status) {
/*     */       case 0:
/* 148 */         buf.append("ACTIVE");
/* 149 */         if (this.inbuf.hasData()) {
/* 150 */           buf.append("(").append(this.inbuf.length()).append(")");
/*     */         }
/*     */         break;
/*     */       case 1:
/* 154 */         buf.append("CLOSING");
/*     */         break;
/*     */       case 2:
/* 157 */         buf.append("CLOSED");
/*     */         break;
/*     */     } 
/* 160 */     buf.append("]");
/* 161 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/conn/ManagedNHttpClientConnectionImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */