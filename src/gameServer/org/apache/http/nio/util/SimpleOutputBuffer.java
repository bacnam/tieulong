/*     */ package org.apache.http.nio.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.nio.ContentEncoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class SimpleOutputBuffer
/*     */   extends ExpandableBuffer
/*     */   implements ContentOutputBuffer
/*     */ {
/*     */   private boolean endOfStream;
/*     */   
/*     */   public SimpleOutputBuffer(int buffersize, ByteBufferAllocator allocator) {
/*  47 */     super(buffersize, allocator);
/*  48 */     this.endOfStream = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleOutputBuffer(int buffersize) {
/*  55 */     this(buffersize, HeapByteBufferAllocator.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   public int produceContent(ContentEncoder encoder) throws IOException {
/*  60 */     setOutputMode();
/*  61 */     int bytesWritten = encoder.write(this.buffer);
/*  62 */     if (!hasData() && this.endOfStream) {
/*  63 */       encoder.complete();
/*     */     }
/*  65 */     return bytesWritten;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/*  70 */     if (b == null) {
/*     */       return;
/*     */     }
/*  73 */     if (this.endOfStream) {
/*     */       return;
/*     */     }
/*  76 */     setInputMode();
/*  77 */     ensureCapacity(this.buffer.position() + len);
/*  78 */     this.buffer.put(b, off, len);
/*     */   }
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/*  82 */     if (b == null) {
/*     */       return;
/*     */     }
/*  85 */     if (this.endOfStream) {
/*     */       return;
/*     */     }
/*  88 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/*  93 */     if (this.endOfStream) {
/*     */       return;
/*     */     }
/*  96 */     setInputMode();
/*  97 */     ensureCapacity(capacity() + 1);
/*  98 */     this.buffer.put((byte)b);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/* 103 */     clear();
/* 104 */     this.endOfStream = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() {}
/*     */ 
/*     */   
/*     */   public void writeCompleted() {
/* 113 */     this.endOfStream = true;
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 117 */     this.endOfStream = true;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/util/SimpleOutputBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */