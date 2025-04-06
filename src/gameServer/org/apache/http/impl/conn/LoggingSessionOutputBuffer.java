/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.Consts;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.io.HttpTransportMetrics;
/*     */ import org.apache.http.io.SessionOutputBuffer;
/*     */ import org.apache.http.util.CharArrayBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @Immutable
/*     */ public class LoggingSessionOutputBuffer
/*     */   implements SessionOutputBuffer
/*     */ {
/*     */   private final SessionOutputBuffer out;
/*     */   private final Wire wire;
/*     */   private final String charset;
/*     */   
/*     */   public LoggingSessionOutputBuffer(SessionOutputBuffer out, Wire wire, String charset) {
/*  63 */     this.out = out;
/*  64 */     this.wire = wire;
/*  65 */     this.charset = (charset != null) ? charset : Consts.ASCII.name();
/*     */   }
/*     */   
/*     */   public LoggingSessionOutputBuffer(SessionOutputBuffer out, Wire wire) {
/*  69 */     this(out, wire, null);
/*     */   }
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/*  73 */     this.out.write(b, off, len);
/*  74 */     if (this.wire.enabled()) {
/*  75 */       this.wire.output(b, off, len);
/*     */     }
/*     */   }
/*     */   
/*     */   public void write(int b) throws IOException {
/*  80 */     this.out.write(b);
/*  81 */     if (this.wire.enabled()) {
/*  82 */       this.wire.output(b);
/*     */     }
/*     */   }
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/*  87 */     this.out.write(b);
/*  88 */     if (this.wire.enabled()) {
/*  89 */       this.wire.output(b);
/*     */     }
/*     */   }
/*     */   
/*     */   public void flush() throws IOException {
/*  94 */     this.out.flush();
/*     */   }
/*     */   
/*     */   public void writeLine(CharArrayBuffer buffer) throws IOException {
/*  98 */     this.out.writeLine(buffer);
/*  99 */     if (this.wire.enabled()) {
/* 100 */       String s = new String(buffer.buffer(), 0, buffer.length());
/* 101 */       String tmp = s + "\r\n";
/* 102 */       this.wire.output(tmp.getBytes(this.charset));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void writeLine(String s) throws IOException {
/* 107 */     this.out.writeLine(s);
/* 108 */     if (this.wire.enabled()) {
/* 109 */       String tmp = s + "\r\n";
/* 110 */       this.wire.output(tmp.getBytes(this.charset));
/*     */     } 
/*     */   }
/*     */   
/*     */   public HttpTransportMetrics getMetrics() {
/* 115 */     return this.out.getMetrics();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/conn/LoggingSessionOutputBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */