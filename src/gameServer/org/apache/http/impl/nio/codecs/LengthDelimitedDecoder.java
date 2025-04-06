/*     */ package org.apache.http.impl.nio.codecs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import org.apache.http.ConnectionClosedException;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.impl.io.HttpTransportMetricsImpl;
/*     */ import org.apache.http.nio.FileContentDecoder;
/*     */ import org.apache.http.nio.reactor.SessionInputBuffer;
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
/*     */ @NotThreadSafe
/*     */ public class LengthDelimitedDecoder
/*     */   extends AbstractContentDecoder
/*     */   implements FileContentDecoder
/*     */ {
/*     */   private final long contentLength;
/*     */   private long len;
/*     */   
/*     */   public LengthDelimitedDecoder(ReadableByteChannel channel, SessionInputBuffer buffer, HttpTransportMetricsImpl metrics, long contentLength) {
/*  68 */     super(channel, buffer, metrics);
/*  69 */     Args.notNegative(contentLength, "Content length");
/*  70 */     this.contentLength = contentLength;
/*     */   }
/*     */   
/*     */   public int read(ByteBuffer dst) throws IOException {
/*     */     int bytesRead;
/*  75 */     Args.notNull(dst, "Byte buffer");
/*  76 */     if (this.completed) {
/*  77 */       return -1;
/*     */     }
/*  79 */     int chunk = (int)Math.min(this.contentLength - this.len, 2147483647L);
/*     */ 
/*     */     
/*  82 */     if (this.buffer.hasData()) {
/*  83 */       int maxLen = Math.min(chunk, this.buffer.length());
/*  84 */       bytesRead = this.buffer.read(dst, maxLen);
/*     */     } else {
/*  86 */       bytesRead = readFromChannel(dst, chunk);
/*     */     } 
/*  88 */     if (bytesRead == -1) {
/*  89 */       this.completed = true;
/*  90 */       if (this.len < this.contentLength) {
/*  91 */         throw new ConnectionClosedException("Premature end of Content-Length delimited message body (expected: " + this.contentLength + "; received: " + this.len);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  96 */     this.len += bytesRead;
/*  97 */     if (this.len >= this.contentLength) {
/*  98 */       this.completed = true;
/*     */     }
/* 100 */     if (this.completed && bytesRead == 0) {
/* 101 */       return -1;
/*     */     }
/* 103 */     return bytesRead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long transfer(FileChannel dst, long position, long count) throws IOException {
/*     */     long bytesRead;
/* 113 */     if (dst == null) {
/* 114 */       return 0L;
/*     */     }
/* 116 */     if (this.completed) {
/* 117 */       return -1L;
/*     */     }
/*     */     
/* 120 */     int chunk = (int)Math.min(this.contentLength - this.len, 2147483647L);
/*     */ 
/*     */     
/* 123 */     if (this.buffer.hasData()) {
/* 124 */       int maxLen = Math.min(chunk, this.buffer.length());
/* 125 */       dst.position(position);
/* 126 */       bytesRead = this.buffer.read(dst, maxLen);
/*     */     } else {
/* 128 */       if (this.channel.isOpen()) {
/* 129 */         if (position > dst.size()) {
/* 130 */           throw new IOException("Position past end of file [" + position + " > " + dst.size() + "]");
/*     */         }
/*     */         
/* 133 */         bytesRead = dst.transferFrom(this.channel, position, (count < chunk) ? count : chunk);
/*     */       } else {
/* 135 */         bytesRead = -1L;
/*     */       } 
/* 137 */       if (bytesRead > 0L) {
/* 138 */         this.metrics.incrementBytesTransferred(bytesRead);
/*     */       }
/*     */     } 
/* 141 */     if (bytesRead == -1L) {
/* 142 */       this.completed = true;
/* 143 */       if (this.len < this.contentLength) {
/* 144 */         throw new ConnectionClosedException("Premature end of Content-Length delimited message body (expected: " + this.contentLength + "; received: " + this.len);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 149 */     this.len += bytesRead;
/* 150 */     if (this.len >= this.contentLength) {
/* 151 */       this.completed = true;
/*     */     }
/* 153 */     return bytesRead;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 158 */     StringBuilder sb = new StringBuilder();
/* 159 */     sb.append("[content length: ");
/* 160 */     sb.append(this.contentLength);
/* 161 */     sb.append("; pos: ");
/* 162 */     sb.append(this.len);
/* 163 */     sb.append("; completed: ");
/* 164 */     sb.append(this.completed);
/* 165 */     sb.append("]");
/* 166 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/codecs/LengthDelimitedDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */