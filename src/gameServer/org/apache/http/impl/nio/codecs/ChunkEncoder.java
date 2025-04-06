/*     */ package org.apache.http.impl.nio.codecs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.impl.io.HttpTransportMetricsImpl;
/*     */ import org.apache.http.io.BufferInfo;
/*     */ import org.apache.http.nio.reactor.SessionOutputBuffer;
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
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class ChunkEncoder
/*     */   extends AbstractContentEncoder
/*     */ {
/*     */   private final int fragHint;
/*     */   private final CharArrayBuffer lineBuffer;
/*     */   private final BufferInfo bufferinfo;
/*     */   
/*     */   public ChunkEncoder(WritableByteChannel channel, SessionOutputBuffer buffer, HttpTransportMetricsImpl metrics, int fragementSizeHint) {
/*  69 */     super(channel, buffer, metrics);
/*  70 */     this.fragHint = (fragementSizeHint > 0) ? fragementSizeHint : 0;
/*  71 */     this.lineBuffer = new CharArrayBuffer(16);
/*  72 */     if (buffer instanceof BufferInfo) {
/*  73 */       this.bufferinfo = (BufferInfo)buffer;
/*     */     } else {
/*  75 */       this.bufferinfo = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkEncoder(WritableByteChannel channel, SessionOutputBuffer buffer, HttpTransportMetricsImpl metrics) {
/*  83 */     this(channel, buffer, metrics, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int write(ByteBuffer src) throws IOException {
/*  88 */     if (src == null) {
/*  89 */       return 0;
/*     */     }
/*  91 */     assertNotCompleted();
/*     */     
/*  93 */     int total = 0;
/*  94 */     while (src.hasRemaining()) {
/*  95 */       int avail, chunk = src.remaining();
/*     */       
/*  97 */       if (this.bufferinfo != null) {
/*  98 */         avail = this.bufferinfo.available();
/*     */       } else {
/* 100 */         avail = 4096;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 106 */       avail -= 12;
/* 107 */       if (avail > 0) {
/* 108 */         if (avail < chunk) {
/*     */           
/* 110 */           chunk = avail;
/* 111 */           this.lineBuffer.clear();
/* 112 */           this.lineBuffer.append(Integer.toHexString(chunk));
/* 113 */           this.buffer.writeLine(this.lineBuffer);
/* 114 */           int oldlimit = src.limit();
/* 115 */           src.limit(src.position() + chunk);
/* 116 */           this.buffer.write(src);
/* 117 */           src.limit(oldlimit);
/*     */         } else {
/*     */           
/* 120 */           this.lineBuffer.clear();
/* 121 */           this.lineBuffer.append(Integer.toHexString(chunk));
/* 122 */           this.buffer.writeLine(this.lineBuffer);
/* 123 */           this.buffer.write(src);
/*     */         } 
/* 125 */         this.lineBuffer.clear();
/* 126 */         this.buffer.writeLine(this.lineBuffer);
/* 127 */         total += chunk;
/*     */       } 
/* 129 */       if (this.buffer.length() >= this.fragHint || src.hasRemaining()) {
/* 130 */         int bytesWritten = flushToChannel();
/* 131 */         if (bytesWritten == 0) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/* 136 */     return total;
/*     */   }
/*     */ 
/*     */   
/*     */   public void complete() throws IOException {
/* 141 */     assertNotCompleted();
/* 142 */     this.lineBuffer.clear();
/* 143 */     this.lineBuffer.append("0");
/* 144 */     this.buffer.writeLine(this.lineBuffer);
/* 145 */     this.lineBuffer.clear();
/* 146 */     this.buffer.writeLine(this.lineBuffer);
/* 147 */     super.complete();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 152 */     StringBuilder sb = new StringBuilder();
/* 153 */     sb.append("[chunk-coded; completed: ");
/* 154 */     sb.append(isCompleted());
/* 155 */     sb.append("]");
/* 156 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/codecs/ChunkEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */