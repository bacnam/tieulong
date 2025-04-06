/*     */ package org.apache.http.impl.nio.codecs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.impl.io.HttpTransportMetricsImpl;
/*     */ import org.apache.http.nio.FileContentEncoder;
/*     */ import org.apache.http.nio.reactor.SessionOutputBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class IdentityEncoder
/*     */   extends AbstractContentEncoder
/*     */   implements FileContentEncoder
/*     */ {
/*     */   private final int fragHint;
/*     */   
/*     */   public IdentityEncoder(WritableByteChannel channel, SessionOutputBuffer buffer, HttpTransportMetricsImpl metrics, int fragementSizeHint) {
/*  73 */     super(channel, buffer, metrics);
/*  74 */     this.fragHint = (fragementSizeHint > 0) ? fragementSizeHint : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IdentityEncoder(WritableByteChannel channel, SessionOutputBuffer buffer, HttpTransportMetricsImpl metrics) {
/*  81 */     this(channel, buffer, metrics, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int write(ByteBuffer src) throws IOException {
/*  86 */     if (src == null) {
/*  87 */       return 0;
/*     */     }
/*  89 */     assertNotCompleted();
/*     */     
/*  91 */     int total = 0;
/*  92 */     while (src.hasRemaining()) {
/*  93 */       if ((this.buffer.hasData() || this.fragHint > 0) && 
/*  94 */         src.remaining() <= this.fragHint) {
/*  95 */         int capacity = this.fragHint - this.buffer.length();
/*  96 */         if (capacity > 0) {
/*  97 */           int limit = Math.min(capacity, src.remaining());
/*  98 */           int bytesWritten = writeToBuffer(src, limit);
/*  99 */           total += bytesWritten;
/*     */         } 
/*     */       } 
/*     */       
/* 103 */       if (this.buffer.hasData() && (
/* 104 */         this.buffer.length() >= this.fragHint || src.hasRemaining())) {
/* 105 */         int bytesWritten = flushToChannel();
/* 106 */         if (bytesWritten == 0) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 111 */       if (!this.buffer.hasData() && src.remaining() > this.fragHint) {
/* 112 */         int bytesWritten = writeToChannel(src);
/* 113 */         total += bytesWritten;
/* 114 */         if (bytesWritten == 0) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/* 119 */     return total;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long transfer(FileChannel src, long position, long count) throws IOException {
/* 128 */     if (src == null) {
/* 129 */       return 0L;
/*     */     }
/* 131 */     assertNotCompleted();
/*     */     
/* 133 */     flushToChannel();
/* 134 */     if (this.buffer.hasData()) {
/* 135 */       return 0L;
/*     */     }
/*     */     
/* 138 */     long bytesWritten = src.transferTo(position, count, this.channel);
/* 139 */     if (bytesWritten > 0L) {
/* 140 */       this.metrics.incrementBytesTransferred(bytesWritten);
/*     */     }
/* 142 */     return bytesWritten;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 147 */     StringBuilder sb = new StringBuilder();
/* 148 */     sb.append("[identity; completed: ");
/* 149 */     sb.append(isCompleted());
/* 150 */     sb.append("]");
/* 151 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/codecs/IdentityEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */