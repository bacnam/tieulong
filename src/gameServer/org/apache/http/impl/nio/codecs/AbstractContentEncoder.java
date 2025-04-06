/*     */ package org.apache.http.impl.nio.codecs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.impl.io.HttpTransportMetricsImpl;
/*     */ import org.apache.http.nio.ContentEncoder;
/*     */ import org.apache.http.nio.reactor.SessionOutputBuffer;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class AbstractContentEncoder
/*     */   implements ContentEncoder
/*     */ {
/*     */   protected final WritableByteChannel channel;
/*     */   protected final SessionOutputBuffer buffer;
/*     */   protected final HttpTransportMetricsImpl metrics;
/*     */   protected boolean completed;
/*     */   
/*     */   public AbstractContentEncoder(WritableByteChannel channel, SessionOutputBuffer buffer, HttpTransportMetricsImpl metrics) {
/*  72 */     Args.notNull(channel, "Channel");
/*  73 */     Args.notNull(buffer, "Session input buffer");
/*  74 */     Args.notNull(metrics, "Transport metrics");
/*  75 */     this.buffer = buffer;
/*  76 */     this.channel = channel;
/*  77 */     this.metrics = metrics;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompleted() {
/*  82 */     return this.completed;
/*     */   }
/*     */ 
/*     */   
/*     */   public void complete() throws IOException {
/*  87 */     this.completed = true;
/*     */   }
/*     */   
/*     */   protected void assertNotCompleted() {
/*  91 */     Asserts.check(!this.completed, "Encoding process already completed");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int flushToChannel() throws IOException {
/* 102 */     if (!this.buffer.hasData()) {
/* 103 */       return 0;
/*     */     }
/* 105 */     int bytesWritten = this.buffer.flush(this.channel);
/* 106 */     if (bytesWritten > 0) {
/* 107 */       this.metrics.incrementBytesTransferred(bytesWritten);
/*     */     }
/* 109 */     return bytesWritten;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int writeToChannel(ByteBuffer src) throws IOException {
/* 120 */     if (!src.hasRemaining()) {
/* 121 */       return 0;
/*     */     }
/* 123 */     int bytesWritten = this.channel.write(src);
/* 124 */     if (bytesWritten > 0) {
/* 125 */       this.metrics.incrementBytesTransferred(bytesWritten);
/*     */     }
/* 127 */     return bytesWritten;
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
/*     */   protected int writeToChannel(ByteBuffer src, int limit) throws IOException {
/* 140 */     return doWriteChunk(src, limit, true);
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
/*     */   protected int writeToBuffer(ByteBuffer src, int limit) throws IOException {
/* 153 */     return doWriteChunk(src, limit, false);
/*     */   }
/*     */ 
/*     */   
/*     */   private int doWriteChunk(ByteBuffer src, int chunk, boolean direct) throws IOException {
/*     */     int bytesWritten;
/* 159 */     if (src.remaining() > chunk) {
/* 160 */       int oldLimit = src.limit();
/* 161 */       int newLimit = oldLimit - src.remaining() - chunk;
/* 162 */       src.limit(newLimit);
/* 163 */       bytesWritten = doWriteChunk(src, direct);
/* 164 */       src.limit(oldLimit);
/*     */     } else {
/* 166 */       bytesWritten = doWriteChunk(src, direct);
/*     */     } 
/* 168 */     return bytesWritten;
/*     */   }
/*     */   
/*     */   private int doWriteChunk(ByteBuffer src, boolean direct) throws IOException {
/* 172 */     if (direct) {
/* 173 */       int bytesWritten = this.channel.write(src);
/* 174 */       if (bytesWritten > 0) {
/* 175 */         this.metrics.incrementBytesTransferred(bytesWritten);
/*     */       }
/* 177 */       return bytesWritten;
/*     */     } 
/* 179 */     int chunk = src.remaining();
/* 180 */     this.buffer.write(src);
/* 181 */     return chunk;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/codecs/AbstractContentEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */