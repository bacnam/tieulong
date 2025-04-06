/*     */ package org.apache.http.impl.nio.conn;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.impl.nio.DefaultNHttpClientConnection;
/*     */ import org.apache.http.nio.conn.ClientAsyncConnection;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.nio.util.ByteBufferAllocator;
/*     */ import org.apache.http.params.HttpParams;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class DefaultClientAsyncConnection
/*     */   extends DefaultNHttpClientConnection
/*     */   implements ClientAsyncConnection
/*     */ {
/*  45 */   private final Log headerlog = LogFactory.getLog("org.apache.http.headers");
/*  46 */   private final Log wirelog = LogFactory.getLog("org.apache.http.wire");
/*     */ 
/*     */   
/*     */   private final Log log;
/*     */ 
/*     */   
/*     */   private final String id;
/*     */   
/*     */   private IOSession original;
/*     */ 
/*     */   
/*     */   public DefaultClientAsyncConnection(String id, IOSession iosession, HttpResponseFactory responseFactory, ByteBufferAllocator allocator, HttpParams params) {
/*  58 */     super(iosession, responseFactory, allocator, params);
/*  59 */     this.id = id;
/*  60 */     this.original = iosession;
/*  61 */     this.log = LogFactory.getLog(iosession.getClass());
/*  62 */     if (this.log.isDebugEnabled() || this.wirelog.isDebugEnabled()) {
/*  63 */       bind(new LoggingIOSession(iosession, this.id, this.log, this.wirelog));
/*     */     }
/*     */   }
/*     */   
/*     */   public void upgrade(IOSession iosession) {
/*  68 */     this.original = iosession;
/*  69 */     if (this.log.isDebugEnabled() || this.wirelog.isDebugEnabled()) {
/*  70 */       this.log.debug(this.id + " Upgrade session " + iosession);
/*  71 */       bind(new LoggingIOSession(iosession, this.id, this.headerlog, this.wirelog));
/*     */     } else {
/*  73 */       bind(iosession);
/*     */     } 
/*     */   }
/*     */   
/*     */   public IOSession getIOSession() {
/*  78 */     return this.original;
/*     */   }
/*     */   
/*     */   public String getId() {
/*  82 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onResponseReceived(HttpResponse response) {
/*  87 */     if (response != null && this.headerlog.isDebugEnabled()) {
/*  88 */       this.headerlog.debug(this.id + " << " + response.getStatusLine().toString());
/*  89 */       Header[] headers = response.getAllHeaders();
/*  90 */       for (Header header : headers) {
/*  91 */         this.headerlog.debug(this.id + " << " + header.toString());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onRequestSubmitted(HttpRequest request) {
/*  98 */     if (request != null && this.headerlog.isDebugEnabled()) {
/*  99 */       this.headerlog.debug(this.id + " >> " + request.getRequestLine().toString());
/* 100 */       Header[] headers = request.getAllHeaders();
/* 101 */       for (Header header : headers) {
/* 102 */         this.headerlog.debug(this.id + " >> " + header.toString());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 109 */     StringBuilder buf = new StringBuilder();
/* 110 */     buf.append(this.id);
/* 111 */     buf.append(" [");
/* 112 */     switch (this.status) {
/*     */       case 0:
/* 114 */         buf.append("ACTIVE");
/* 115 */         if (this.inbuf.hasData()) {
/* 116 */           buf.append("(").append(this.inbuf.length()).append(")");
/*     */         }
/*     */         break;
/*     */       case 1:
/* 120 */         buf.append("CLOSING");
/*     */         break;
/*     */       case 2:
/* 123 */         buf.append("CLOSED");
/*     */         break;
/*     */     } 
/* 126 */     buf.append("]");
/* 127 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/conn/DefaultClientAsyncConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */