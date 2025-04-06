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
/*     */ import org.apache.http.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class LengthDelimitedEncoder
/*     */   extends AbstractContentEncoder
/*     */   implements FileContentEncoder
/*     */ {
/*     */   private final long contentLength;
/*     */   private final int fragHint;
/*     */   private long remaining;
/*     */   
/*     */   public LengthDelimitedEncoder(WritableByteChannel channel, SessionOutputBuffer buffer, HttpTransportMetricsImpl metrics, long contentLength, int fragementSizeHint) {
/*  80 */     super(channel, buffer, metrics);
/*  81 */     Args.notNegative(contentLength, "Content length");
/*  82 */     this.contentLength = contentLength;
/*  83 */     this.fragHint = (fragementSizeHint > 0) ? fragementSizeHint : 0;
/*  84 */     this.remaining = contentLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LengthDelimitedEncoder(WritableByteChannel channel, SessionOutputBuffer buffer, HttpTransportMetricsImpl metrics, long contentLength) {
/*  92 */     this(channel, buffer, metrics, contentLength, 0);
/*     */   }
/*     */   
/*     */   private int nextChunk(ByteBuffer src) {
/*  96 */     return (int)Math.min(Math.min(this.remaining, 2147483647L), src.remaining());
/*     */   }
/*     */ 
/*     */   
/*     */   public int write(ByteBuffer src) throws IOException {
/* 101 */     if (src == null) {
/* 102 */       return 0;
/*     */     }
/* 104 */     assertNotCompleted();
/*     */     
/* 106 */     int total = 0;
/* 107 */     while (src.hasRemaining() && this.remaining > 0L) {
/* 108 */       if (this.buffer.hasData() || this.fragHint > 0) {
/* 109 */         int chunk = nextChunk(src);
/* 110 */         if (chunk <= this.fragHint) {
/* 111 */           int capacity = this.fragHint - this.buffer.length();
/* 112 */           if (capacity > 0) {
/* 113 */             int limit = Math.min(capacity, chunk);
/* 114 */             int bytesWritten = writeToBuffer(src, limit);
/* 115 */             this.remaining -= bytesWritten;
/* 116 */             total += bytesWritten;
/*     */           } 
/*     */         } 
/*     */       } 
/* 120 */       if (this.buffer.hasData()) {
/* 121 */         int chunk = nextChunk(src);
/* 122 */         if (this.buffer.length() >= this.fragHint || chunk > 0) {
/* 123 */           int bytesWritten = flushToChannel();
/* 124 */           if (bytesWritten == 0) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/* 129 */       if (!this.buffer.hasData()) {
/* 130 */         int chunk = nextChunk(src);
/* 131 */         if (chunk > this.fragHint) {
/* 132 */           int bytesWritten = writeToChannel(src, chunk);
/* 133 */           this.remaining -= bytesWritten;
/* 134 */           total += bytesWritten;
/* 135 */           if (bytesWritten == 0) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 141 */     if (this.remaining <= 0L) {
/* 142 */       complete();
/*     */     }
/* 144 */     return total;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long transfer(FileChannel src, long position, long count) throws IOException {
/* 153 */     if (src == null) {
/* 154 */       return 0L;
/*     */     }
/* 156 */     assertNotCompleted();
/*     */     
/* 158 */     flushToChannel();
/* 159 */     if (this.buffer.hasData()) {
/* 160 */       return 0L;
/*     */     }
/*     */     
/* 163 */     long chunk = Math.min(this.remaining, count);
/* 164 */     long bytesWritten = src.transferTo(position, chunk, this.channel);
/* 165 */     if (bytesWritten > 0L) {
/* 166 */       this.metrics.incrementBytesTransferred(bytesWritten);
/*     */     }
/* 168 */     this.remaining -= bytesWritten;
/* 169 */     if (this.remaining <= 0L) {
/* 170 */       complete();
/*     */     }
/* 172 */     return bytesWritten;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 177 */     StringBuilder sb = new StringBuilder();
/* 178 */     sb.append("[content length: ");
/* 179 */     sb.append(this.contentLength);
/* 180 */     sb.append("; pos: ");
/* 181 */     sb.append(this.contentLength - this.remaining);
/* 182 */     sb.append("; completed: ");
/* 183 */     sb.append(isCompleted());
/* 184 */     sb.append("]");
/* 185 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/codecs/LengthDelimitedEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */