/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.Consts;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.io.EofSensor;
/*     */ import org.apache.http.io.HttpTransportMetrics;
/*     */ import org.apache.http.io.SessionInputBuffer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public class LoggingSessionInputBuffer
/*     */   implements SessionInputBuffer, EofSensor
/*     */ {
/*     */   private final SessionInputBuffer in;
/*     */   private final EofSensor eofSensor;
/*     */   private final Wire wire;
/*     */   private final String charset;
/*     */   
/*     */   public LoggingSessionInputBuffer(SessionInputBuffer in, Wire wire, String charset) {
/*  68 */     this.in = in;
/*  69 */     this.eofSensor = (in instanceof EofSensor) ? (EofSensor)in : null;
/*  70 */     this.wire = wire;
/*  71 */     this.charset = (charset != null) ? charset : Consts.ASCII.name();
/*     */   }
/*     */   
/*     */   public LoggingSessionInputBuffer(SessionInputBuffer in, Wire wire) {
/*  75 */     this(in, wire, null);
/*     */   }
/*     */   
/*     */   public boolean isDataAvailable(int timeout) throws IOException {
/*  79 */     return this.in.isDataAvailable(timeout);
/*     */   }
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/*  83 */     int l = this.in.read(b, off, len);
/*  84 */     if (this.wire.enabled() && l > 0) {
/*  85 */       this.wire.input(b, off, l);
/*     */     }
/*  87 */     return l;
/*     */   }
/*     */   
/*     */   public int read() throws IOException {
/*  91 */     int l = this.in.read();
/*  92 */     if (this.wire.enabled() && l != -1) {
/*  93 */       this.wire.input(l);
/*     */     }
/*  95 */     return l;
/*     */   }
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/*  99 */     int l = this.in.read(b);
/* 100 */     if (this.wire.enabled() && l > 0) {
/* 101 */       this.wire.input(b, 0, l);
/*     */     }
/* 103 */     return l;
/*     */   }
/*     */   
/*     */   public String readLine() throws IOException {
/* 107 */     String s = this.in.readLine();
/* 108 */     if (this.wire.enabled() && s != null) {
/* 109 */       String tmp = s + "\r\n";
/* 110 */       this.wire.input(tmp.getBytes(this.charset));
/*     */     } 
/* 112 */     return s;
/*     */   }
/*     */   
/*     */   public int readLine(CharArrayBuffer buffer) throws IOException {
/* 116 */     int l = this.in.readLine(buffer);
/* 117 */     if (this.wire.enabled() && l >= 0) {
/* 118 */       int pos = buffer.length() - l;
/* 119 */       String s = new String(buffer.buffer(), pos, l);
/* 120 */       String tmp = s + "\r\n";
/* 121 */       this.wire.input(tmp.getBytes(this.charset));
/*     */     } 
/* 123 */     return l;
/*     */   }
/*     */   
/*     */   public HttpTransportMetrics getMetrics() {
/* 127 */     return this.in.getMetrics();
/*     */   }
/*     */   
/*     */   public boolean isEof() {
/* 131 */     if (this.eofSensor != null) {
/* 132 */       return this.eofSensor.isEof();
/*     */     }
/* 134 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/conn/LoggingSessionInputBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */