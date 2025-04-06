/*     */ package org.apache.http.impl.nio.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.nio.ContentDecoder;
/*     */ import org.apache.http.nio.ContentEncoder;
/*     */ import org.apache.http.nio.NHttpClientConnection;
/*     */ import org.apache.http.nio.protocol.HttpAsyncRequestExecutor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class LoggingAsyncRequestExecutor
/*     */   extends HttpAsyncRequestExecutor
/*     */ {
/*  42 */   private final Log log = LogFactory.getLog(HttpAsyncRequestExecutor.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void log(Exception ex) {
/*  50 */     this.log.debug(ex.getMessage(), ex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connected(NHttpClientConnection conn, Object attachment) throws IOException, HttpException {
/*  57 */     if (this.log.isDebugEnabled()) {
/*  58 */       this.log.debug(conn + ": Connected");
/*     */     }
/*  60 */     super.connected(conn, attachment);
/*     */   }
/*     */ 
/*     */   
/*     */   public void closed(NHttpClientConnection conn) {
/*  65 */     if (this.log.isDebugEnabled()) {
/*  66 */       this.log.debug(conn + ": Disconnected");
/*     */     }
/*  68 */     super.closed(conn);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void requestReady(NHttpClientConnection conn) throws IOException, HttpException {
/*  74 */     if (this.log.isDebugEnabled()) {
/*  75 */       this.log.debug(conn + " Request ready");
/*     */     }
/*  77 */     super.requestReady(conn);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void inputReady(NHttpClientConnection conn, ContentDecoder decoder) throws IOException, HttpException {
/*  84 */     if (this.log.isDebugEnabled()) {
/*  85 */       this.log.debug(conn + " Input ready");
/*     */     }
/*  87 */     super.inputReady(conn, decoder);
/*  88 */     if (this.log.isDebugEnabled()) {
/*  89 */       this.log.debug(conn + " " + decoder);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void outputReady(NHttpClientConnection conn, ContentEncoder encoder) throws IOException, HttpException {
/*  97 */     if (this.log.isDebugEnabled()) {
/*  98 */       this.log.debug(conn + " Output ready");
/*     */     }
/* 100 */     super.outputReady(conn, encoder);
/* 101 */     if (this.log.isDebugEnabled()) {
/* 102 */       this.log.debug(conn + " " + encoder);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void responseReceived(NHttpClientConnection conn) throws HttpException, IOException {
/* 109 */     if (this.log.isDebugEnabled()) {
/* 110 */       this.log.debug(conn + " Response received");
/*     */     }
/* 112 */     super.responseReceived(conn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void timeout(NHttpClientConnection conn) throws IOException {
/* 117 */     if (this.log.isDebugEnabled()) {
/* 118 */       this.log.debug(conn + " Timeout");
/*     */     }
/* 120 */     super.timeout(conn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void endOfInput(NHttpClientConnection conn) throws IOException {
/* 125 */     if (this.log.isDebugEnabled()) {
/* 126 */       this.log.debug(conn + " End of input");
/*     */     }
/* 128 */     super.endOfInput(conn);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/client/LoggingAsyncRequestExecutor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */