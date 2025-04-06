/*    */ package com.zhonglian.server.logger.scribe;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.OutputStreamAppender;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ScribeFormatedAppender
/*    */   extends OutputStreamAppender<ILoggingEvent>
/*    */ {
/*    */   private String host;
/*    */   private int port;
/*    */   private String encoding;
/*    */   ScribeTransfer _scribeTransfer;
/* 17 */   private ByteArrayOutputStream _byteOutStream = new ByteArrayOutputStream();
/*    */   
/*    */   public ScribeFormatedAppender() {
/* 20 */     setOutputStream(this._byteOutStream);
/*    */   }
/*    */   
/*    */   public String getHost() {
/* 24 */     return this.host;
/*    */   }
/*    */   
/*    */   public void setHost(String host) {
/* 28 */     this.host = host;
/*    */   }
/*    */   
/*    */   public int getPort() {
/* 32 */     return this.port;
/*    */   }
/*    */   
/*    */   public void setPort(int port) {
/* 36 */     this.port = port;
/*    */   }
/*    */   
/*    */   public String getEncoding() {
/* 40 */     return this.encoding;
/*    */   }
/*    */   
/*    */   public void setEncoding(String encoding) {
/* 44 */     this.encoding = encoding;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 49 */     super.start();
/* 50 */     synchronized (this) {
/*    */       try {
/* 52 */         this.encoder.init(this._byteOutStream);
/* 53 */         this._scribeTransfer = new ScribeTransfer();
/* 54 */         this._scribeTransfer.setHost(getHost());
/* 55 */         this._scribeTransfer.setPort(getPort());
/* 56 */         this._scribeTransfer.setEncoding(getEncoding());
/* 57 */         this._scribeTransfer.start();
/* 58 */       } catch (Exception e) {
/* 59 */         e.printStackTrace();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void append(ILoggingEvent event) {
/*    */     try {
/* 67 */       this.encoder.doEncode(event);
/* 68 */       String str = this._byteOutStream.toString();
/* 69 */       this._byteOutStream.reset();
/* 70 */       this._scribeTransfer.append(str);
/* 71 */     } catch (Exception e) {
/* 72 */       e.printStackTrace();
/*    */       return;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/logger/scribe/ScribeFormatedAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */