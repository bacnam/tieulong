/*     */ package org.apache.http.nio.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.nio.ContentDecoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class SimpleInputBuffer
/*     */   extends ExpandableBuffer
/*     */   implements ContentInputBuffer
/*     */ {
/*     */   private boolean endOfStream = false;
/*     */   
/*     */   public SimpleInputBuffer(int buffersize, ByteBufferAllocator allocator) {
/*  47 */     super(buffersize, allocator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleInputBuffer(int buffersize) {
/*  54 */     this(buffersize, HeapByteBufferAllocator.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/*  59 */     this.endOfStream = false;
/*  60 */     clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public int consumeContent(ContentDecoder decoder) throws IOException {
/*  65 */     setInputMode();
/*  66 */     int totalRead = 0;
/*     */     int bytesRead;
/*  68 */     while ((bytesRead = decoder.read(this.buffer)) != -1) {
/*  69 */       if (bytesRead == 0) {
/*  70 */         if (!this.buffer.hasRemaining()) {
/*  71 */           expand();
/*     */           continue;
/*     */         } 
/*     */         break;
/*     */       } 
/*  76 */       totalRead += bytesRead;
/*     */     } 
/*     */     
/*  79 */     if (bytesRead == -1 || decoder.isCompleted()) {
/*  80 */       this.endOfStream = true;
/*     */     }
/*  82 */     return totalRead;
/*     */   }
/*     */   
/*     */   public boolean isEndOfStream() {
/*  86 */     return (!hasData() && this.endOfStream);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  91 */     if (isEndOfStream()) {
/*  92 */       return -1;
/*     */     }
/*  94 */     setOutputMode();
/*  95 */     return this.buffer.get() & 0xFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 100 */     if (isEndOfStream()) {
/* 101 */       return -1;
/*     */     }
/* 103 */     if (b == null) {
/* 104 */       return 0;
/*     */     }
/* 106 */     setOutputMode();
/* 107 */     int chunk = len;
/* 108 */     if (chunk > this.buffer.remaining()) {
/* 109 */       chunk = this.buffer.remaining();
/*     */     }
/* 111 */     this.buffer.get(b, off, chunk);
/* 112 */     return chunk;
/*     */   }
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/* 116 */     if (isEndOfStream()) {
/* 117 */       return -1;
/*     */     }
/* 119 */     if (b == null) {
/* 120 */       return 0;
/*     */     }
/* 122 */     return read(b, 0, b.length);
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 126 */     this.endOfStream = true;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/util/SimpleInputBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */