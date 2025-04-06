/*     */ package org.apache.http.impl.nio.codecs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.impl.io.HttpTransportMetricsImpl;
/*     */ import org.apache.http.nio.ContentDecoder;
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
/*     */ 
/*     */ @NotThreadSafe
/*     */ public abstract class AbstractContentDecoder
/*     */   implements ContentDecoder
/*     */ {
/*     */   protected final ReadableByteChannel channel;
/*     */   protected final SessionInputBuffer buffer;
/*     */   protected final HttpTransportMetricsImpl metrics;
/*     */   protected boolean completed;
/*     */   
/*     */   public AbstractContentDecoder(ReadableByteChannel channel, SessionInputBuffer buffer, HttpTransportMetricsImpl metrics) {
/*  68 */     Args.notNull(channel, "Channel");
/*  69 */     Args.notNull(buffer, "Session input buffer");
/*  70 */     Args.notNull(metrics, "Transport metrics");
/*  71 */     this.buffer = buffer;
/*  72 */     this.channel = channel;
/*  73 */     this.metrics = metrics;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompleted() {
/*  78 */     return this.completed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int readFromChannel(ByteBuffer dst) throws IOException {
/*  90 */     int bytesRead = this.channel.read(dst);
/*  91 */     if (bytesRead > 0) {
/*  92 */       this.metrics.incrementBytesTransferred(bytesRead);
/*     */     }
/*  94 */     return bytesRead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int fillBufferFromChannel() throws IOException {
/* 104 */     int bytesRead = this.buffer.fill(this.channel);
/* 105 */     if (bytesRead > 0) {
/* 106 */       this.metrics.incrementBytesTransferred(bytesRead);
/*     */     }
/* 108 */     return bytesRead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int readFromChannel(ByteBuffer dst, int limit) throws IOException {
/*     */     int bytesRead;
/* 122 */     if (dst.remaining() > limit) {
/* 123 */       int oldLimit = dst.limit();
/* 124 */       int newLimit = oldLimit - dst.remaining() - limit;
/* 125 */       dst.limit(newLimit);
/* 126 */       bytesRead = this.channel.read(dst);
/* 127 */       dst.limit(oldLimit);
/*     */     } else {
/* 129 */       bytesRead = this.channel.read(dst);
/*     */     } 
/* 131 */     if (bytesRead > 0) {
/* 132 */       this.metrics.incrementBytesTransferred(bytesRead);
/*     */     }
/* 134 */     return bytesRead;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/codecs/AbstractContentDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */