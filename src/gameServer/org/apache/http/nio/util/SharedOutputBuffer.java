/*     */ package org.apache.http.nio.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.nio.ContentEncoder;
/*     */ import org.apache.http.nio.IOControl;
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
/*     */ @ThreadSafe
/*     */ public class SharedOutputBuffer
/*     */   extends ExpandableBuffer
/*     */   implements ContentOutputBuffer
/*     */ {
/*     */   private final ReentrantLock lock;
/*     */   private final Condition condition;
/*     */   private volatile IOControl ioctrl;
/*     */   private volatile boolean shutdown = false;
/*     */   private volatile boolean endOfStream = false;
/*     */   
/*     */   @Deprecated
/*     */   public SharedOutputBuffer(int buffersize, IOControl ioctrl, ByteBufferAllocator allocator) {
/*  71 */     super(buffersize, allocator);
/*  72 */     Args.notNull(ioctrl, "I/O content control");
/*  73 */     this.ioctrl = ioctrl;
/*  74 */     this.lock = new ReentrantLock();
/*  75 */     this.condition = this.lock.newCondition();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SharedOutputBuffer(int buffersize, ByteBufferAllocator allocator) {
/*  82 */     super(buffersize, allocator);
/*  83 */     this.lock = new ReentrantLock();
/*  84 */     this.condition = this.lock.newCondition();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SharedOutputBuffer(int buffersize) {
/*  91 */     this(buffersize, HeapByteBufferAllocator.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/*  96 */     if (this.shutdown) {
/*     */       return;
/*     */     }
/*  99 */     this.lock.lock();
/*     */     try {
/* 101 */       clear();
/* 102 */       this.endOfStream = false;
/*     */     } finally {
/* 104 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasData() {
/* 110 */     this.lock.lock();
/*     */     try {
/* 112 */       return super.hasData();
/*     */     } finally {
/* 114 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() {
/* 120 */     this.lock.lock();
/*     */     try {
/* 122 */       return super.available();
/*     */     } finally {
/* 124 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 130 */     this.lock.lock();
/*     */     try {
/* 132 */       return super.capacity();
/*     */     } finally {
/* 134 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int length() {
/* 140 */     this.lock.lock();
/*     */     try {
/* 142 */       return super.length();
/*     */     } finally {
/* 144 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int produceContent(ContentEncoder encoder) throws IOException {
/* 154 */     return produceContent(encoder, (IOControl)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int produceContent(ContentEncoder encoder, IOControl ioctrl) throws IOException {
/* 161 */     if (this.shutdown) {
/* 162 */       return -1;
/*     */     }
/* 164 */     this.lock.lock();
/*     */     try {
/* 166 */       if (ioctrl != null) {
/* 167 */         this.ioctrl = ioctrl;
/*     */       }
/* 169 */       setOutputMode();
/* 170 */       int bytesWritten = 0;
/* 171 */       if (super.hasData()) {
/* 172 */         bytesWritten = encoder.write(this.buffer);
/* 173 */         if (encoder.isCompleted()) {
/* 174 */           this.endOfStream = true;
/*     */         }
/*     */       } 
/* 177 */       if (!super.hasData()) {
/*     */ 
/*     */         
/* 180 */         if (this.endOfStream && !encoder.isCompleted()) {
/* 181 */           encoder.complete();
/*     */         }
/* 183 */         if (!this.endOfStream)
/*     */         {
/* 185 */           if (this.ioctrl != null) {
/* 186 */             this.ioctrl.suspendOutput();
/*     */           }
/*     */         }
/*     */       } 
/* 190 */       this.condition.signalAll();
/* 191 */       return bytesWritten;
/*     */     } finally {
/* 193 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void close() {
/* 198 */     shutdown();
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 202 */     if (this.shutdown) {
/*     */       return;
/*     */     }
/* 205 */     this.shutdown = true;
/* 206 */     this.lock.lock();
/*     */     try {
/* 208 */       this.condition.signalAll();
/*     */     } finally {
/* 210 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/* 216 */     if (b == null) {
/*     */       return;
/*     */     }
/* 219 */     int pos = off;
/* 220 */     this.lock.lock();
/*     */     try {
/* 222 */       Asserts.check((!this.shutdown && !this.endOfStream), "Buffer already closed for writing");
/* 223 */       setInputMode();
/* 224 */       int remaining = len;
/* 225 */       while (remaining > 0) {
/* 226 */         if (!this.buffer.hasRemaining()) {
/* 227 */           flushContent();
/* 228 */           setInputMode();
/*     */         } 
/* 230 */         int chunk = Math.min(remaining, this.buffer.remaining());
/* 231 */         this.buffer.put(b, pos, chunk);
/* 232 */         remaining -= chunk;
/* 233 */         pos += chunk;
/*     */       } 
/*     */     } finally {
/* 236 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/* 241 */     if (b == null) {
/*     */       return;
/*     */     }
/* 244 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/* 249 */     this.lock.lock();
/*     */     try {
/* 251 */       Asserts.check((!this.shutdown && !this.endOfStream), "Buffer already closed for writing");
/* 252 */       setInputMode();
/* 253 */       if (!this.buffer.hasRemaining()) {
/* 254 */         flushContent();
/* 255 */         setInputMode();
/*     */       } 
/* 257 */       this.buffer.put((byte)b);
/*     */     } finally {
/* 259 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {}
/*     */ 
/*     */   
/*     */   private void flushContent() throws IOException {
/* 268 */     this.lock.lock(); 
/*     */     try { while (true) {
/*     */         try {
/* 271 */           if (super.hasData()) {
/* 272 */             if (this.shutdown) {
/* 273 */               throw new InterruptedIOException("Output operation aborted");
/*     */             }
/* 275 */             if (this.ioctrl != null) {
/* 276 */               this.ioctrl.requestOutput();
/*     */             }
/* 278 */             this.condition.await(); continue;
/*     */           } 
/* 280 */         } catch (InterruptedException ex) {
/* 281 */           throw new IOException("Interrupted while flushing the content buffer");
/*     */         }  break;
/*     */       }  }
/* 284 */     finally { this.lock.unlock(); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeCompleted() throws IOException {
/* 290 */     this.lock.lock();
/*     */     try {
/* 292 */       if (this.endOfStream) {
/*     */         return;
/*     */       }
/* 295 */       this.endOfStream = true;
/* 296 */       if (this.ioctrl != null) {
/* 297 */         this.ioctrl.requestOutput();
/*     */       }
/*     */     } finally {
/* 300 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/util/SharedOutputBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */