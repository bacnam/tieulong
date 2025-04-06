/*     */ package org.apache.http.impl.nio.codecs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.ReadableByteChannel;
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
/*     */ @NotThreadSafe
/*     */ public class IdentityDecoder
/*     */   extends AbstractContentDecoder
/*     */   implements FileContentDecoder
/*     */ {
/*     */   public IdentityDecoder(ReadableByteChannel channel, SessionInputBuffer buffer, HttpTransportMetricsImpl metrics) {
/*  61 */     super(channel, buffer, metrics);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCompleted(boolean completed) {
/*  72 */     this.completed = completed;
/*     */   }
/*     */   
/*     */   public int read(ByteBuffer dst) throws IOException {
/*     */     int bytesRead;
/*  77 */     Args.notNull(dst, "Byte buffer");
/*  78 */     if (this.completed) {
/*  79 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*  83 */     if (this.buffer.hasData()) {
/*  84 */       bytesRead = this.buffer.read(dst);
/*     */     } else {
/*  86 */       bytesRead = readFromChannel(dst);
/*     */     } 
/*  88 */     if (bytesRead == -1) {
/*  89 */       this.completed = true;
/*     */     }
/*  91 */     return bytesRead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long transfer(FileChannel dst, long position, long count) throws IOException {
/*     */     long bytesRead;
/* 100 */     if (dst == null) {
/* 101 */       return 0L;
/*     */     }
/* 103 */     if (this.completed) {
/* 104 */       return 0L;
/*     */     }
/*     */ 
/*     */     
/* 108 */     if (this.buffer.hasData()) {
/* 109 */       dst.position(position);
/* 110 */       bytesRead = this.buffer.read(dst);
/*     */     } else {
/* 112 */       if (this.channel.isOpen()) {
/* 113 */         if (position > dst.size()) {
/* 114 */           throw new IOException("Position past end of file [" + position + " > " + dst.size() + "]");
/*     */         }
/*     */         
/* 117 */         bytesRead = dst.transferFrom(this.channel, position, count);
/* 118 */         if (count > 0L && bytesRead == 0L) {
/* 119 */           bytesRead = this.buffer.fill(this.channel);
/*     */         }
/*     */       } else {
/* 122 */         bytesRead = -1L;
/*     */       } 
/* 124 */       if (bytesRead > 0L) {
/* 125 */         this.metrics.incrementBytesTransferred(bytesRead);
/*     */       }
/*     */     } 
/* 128 */     if (bytesRead == -1L) {
/* 129 */       this.completed = true;
/*     */     }
/* 131 */     return bytesRead;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 136 */     StringBuilder sb = new StringBuilder();
/* 137 */     sb.append("[identity; completed: ");
/* 138 */     sb.append(this.completed);
/* 139 */     sb.append("]");
/* 140 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/codecs/IdentityDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */