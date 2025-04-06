/*     */ package org.apache.http.impl.nio.conn;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.commons.logging.Log;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Wire
/*     */ {
/*     */   private final Log log;
/*     */   private final String id;
/*     */   
/*     */   public Wire(Log log, String id) {
/*  40 */     this.log = log;
/*  41 */     this.id = id;
/*     */   }
/*     */   
/*     */   private void wire(String header, byte[] b, int pos, int off) {
/*  45 */     StringBuilder buffer = new StringBuilder();
/*  46 */     for (int i = 0; i < off; i++) {
/*  47 */       int ch = b[pos + i];
/*  48 */       if (ch == 13) {
/*  49 */         buffer.append("[\\r]");
/*  50 */       } else if (ch == 10) {
/*  51 */         buffer.append("[\\n]\"");
/*  52 */         buffer.insert(0, "\"");
/*  53 */         buffer.insert(0, header);
/*  54 */         this.log.debug(this.id + " " + buffer.toString());
/*  55 */         buffer.setLength(0);
/*  56 */       } else if (ch < 32 || ch > 127) {
/*  57 */         buffer.append("[0x");
/*  58 */         buffer.append(Integer.toHexString(ch));
/*  59 */         buffer.append("]");
/*     */       } else {
/*  61 */         buffer.append((char)ch);
/*     */       } 
/*     */     } 
/*  64 */     if (buffer.length() > 0) {
/*  65 */       buffer.append('"');
/*  66 */       buffer.insert(0, '"');
/*  67 */       buffer.insert(0, header);
/*  68 */       this.log.debug(this.id + " " + buffer.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled() {
/*  74 */     return this.log.isDebugEnabled();
/*     */   }
/*     */   
/*     */   public void output(byte[] b, int pos, int off) {
/*  78 */     wire(">> ", b, pos, off);
/*     */   }
/*     */   
/*     */   public void input(byte[] b, int pos, int off) {
/*  82 */     wire("<< ", b, pos, off);
/*     */   }
/*     */   
/*     */   public void output(byte[] b) {
/*  86 */     output(b, 0, b.length);
/*     */   }
/*     */   
/*     */   public void input(byte[] b) {
/*  90 */     input(b, 0, b.length);
/*     */   }
/*     */   
/*     */   public void output(int b) {
/*  94 */     output(new byte[] { (byte)b });
/*     */   }
/*     */   
/*     */   public void input(int b) {
/*  98 */     input(new byte[] { (byte)b });
/*     */   }
/*     */   
/*     */   public void output(ByteBuffer b) {
/* 102 */     if (b.hasArray()) {
/* 103 */       output(b.array(), b.arrayOffset() + b.position(), b.remaining());
/*     */     } else {
/* 105 */       byte[] tmp = new byte[b.remaining()];
/* 106 */       b.get(tmp);
/* 107 */       output(tmp);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void input(ByteBuffer b) {
/* 112 */     if (b.hasArray()) {
/* 113 */       input(b.array(), b.arrayOffset() + b.position(), b.remaining());
/*     */     } else {
/* 115 */       byte[] tmp = new byte[b.remaining()];
/* 116 */       b.get(tmp);
/* 117 */       input(tmp);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/conn/Wire.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */