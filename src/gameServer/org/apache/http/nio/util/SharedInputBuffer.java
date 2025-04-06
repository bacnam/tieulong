/*     */ package org.apache.http.nio.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.nio.ContentDecoder;
/*     */ import org.apache.http.nio.IOControl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class SharedInputBuffer
/*     */   extends ExpandableBuffer
/*     */   implements ContentInputBuffer
/*     */ {
/*     */   private final ReentrantLock lock;
/*     */   private final Condition condition;
/*     */   private volatile IOControl ioctrl;
/*     */   private volatile boolean shutdown = false;
/*     */   private volatile boolean endOfStream = false;
/*     */   
/*     */   @Deprecated
/*     */   public SharedInputBuffer(int buffersize, IOControl ioctrl, ByteBufferAllocator allocator) {
/*  69 */     super(buffersize, allocator);
/*  70 */     this.ioctrl = ioctrl;
/*  71 */     this.lock = new ReentrantLock();
/*  72 */     this.condition = this.lock.newCondition();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SharedInputBuffer(int buffersize, ByteBufferAllocator allocator) {
/*  79 */     super(buffersize, allocator);
/*  80 */     this.lock = new ReentrantLock();
/*  81 */     this.condition = this.lock.newCondition();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SharedInputBuffer(int buffersize) {
/*  88 */     this(buffersize, HeapByteBufferAllocator.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/*  93 */     if (this.shutdown) {
/*     */       return;
/*     */     }
/*  96 */     this.lock.lock();
/*     */     try {
/*  98 */       clear();
/*  99 */       this.endOfStream = false;
/*     */     } finally {
/* 101 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int consumeContent(ContentDecoder decoder) throws IOException {
/* 111 */     return consumeContent(decoder, (IOControl)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int consumeContent(ContentDecoder decoder, IOControl ioctrl) throws IOException {
/* 118 */     if (this.shutdown) {
/* 119 */       return -1;
/*     */     }
/* 121 */     this.lock.lock();
/*     */     try {
/* 123 */       if (ioctrl != null) {
/* 124 */         this.ioctrl = ioctrl;
/*     */       }
/* 126 */       setInputMode();
/* 127 */       int totalRead = 0;
/*     */       int bytesRead;
/* 129 */       while ((bytesRead = decoder.read(this.buffer)) > 0) {
/* 130 */         totalRead += bytesRead;
/*     */       }
/* 132 */       if (bytesRead == -1 || decoder.isCompleted()) {
/* 133 */         this.endOfStream = true;
/*     */       }
/* 135 */       if (!this.buffer.hasRemaining() && 
/* 136 */         this.ioctrl != null) {
/* 137 */         this.ioctrl.suspendInput();
/*     */       }
/*     */       
/* 140 */       this.condition.signalAll();
/*     */       
/* 142 */       if (totalRead > 0) {
/* 143 */         return totalRead;
/*     */       }
/* 145 */       if (this.endOfStream) {
/* 146 */         return -1;
/*     */       }
/* 148 */       return 0;
/*     */     }
/*     */     finally {
/*     */       
/* 152 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasData() {
/* 158 */     this.lock.lock();
/*     */     try {
/* 160 */       return super.hasData();
/*     */     } finally {
/* 162 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() {
/* 168 */     this.lock.lock();
/*     */     try {
/* 170 */       return super.available();
/*     */     } finally {
/* 172 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 178 */     this.lock.lock();
/*     */     try {
/* 180 */       return super.capacity();
/*     */     } finally {
/* 182 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int length() {
/* 188 */     this.lock.lock();
/*     */     try {
/* 190 */       return super.length();
/*     */     } finally {
/* 192 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void waitForData() throws IOException {
/* 197 */     this.lock.lock(); 
/*     */     try { while (true) {
/*     */         try {
/* 200 */           if (!super.hasData() && !this.endOfStream) {
/* 201 */             if (this.shutdown) {
/* 202 */               throw new InterruptedIOException("Input operation aborted");
/*     */             }
/* 204 */             if (this.ioctrl != null) {
/* 205 */               this.ioctrl.requestInput();
/*     */             }
/* 207 */             this.condition.await(); continue;
/*     */           } 
/* 209 */         } catch (InterruptedException ex) {
/* 210 */           throw new IOException("Interrupted while waiting for more data");
/*     */         }  break;
/*     */       }  }
/* 213 */     finally { this.lock.unlock(); }
/*     */   
/*     */   }
/*     */   
/*     */   public void close() {
/* 218 */     if (this.shutdown) {
/*     */       return;
/*     */     }
/* 221 */     this.endOfStream = true;
/* 222 */     this.lock.lock();
/*     */     try {
/* 224 */       this.condition.signalAll();
/*     */     } finally {
/* 226 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 231 */     if (this.shutdown) {
/*     */       return;
/*     */     }
/* 234 */     this.shutdown = true;
/* 235 */     this.lock.lock();
/*     */     try {
/* 237 */       this.condition.signalAll();
/*     */     } finally {
/* 239 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean isShutdown() {
/* 244 */     return this.shutdown;
/*     */   }
/*     */   
/*     */   protected boolean isEndOfStream() {
/* 248 */     return (this.shutdown || (!hasData() && this.endOfStream));
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 253 */     if (this.shutdown) {
/* 254 */       return -1;
/*     */     }
/* 256 */     this.lock.lock();
/*     */     try {
/* 258 */       if (!hasData()) {
/* 259 */         waitForData();
/*     */       }
/* 261 */       if (isEndOfStream()) {
/* 262 */         return -1;
/*     */       }
/* 264 */       return this.buffer.get() & 0xFF;
/*     */     } finally {
/* 266 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 272 */     if (this.shutdown) {
/* 273 */       return -1;
/*     */     }
/* 275 */     if (b == null) {
/* 276 */       return 0;
/*     */     }
/* 278 */     this.lock.lock();
/*     */     try {
/* 280 */       if (!hasData()) {
/* 281 */         waitForData();
/*     */       }
/* 283 */       if (isEndOfStream()) {
/* 284 */         return -1;
/*     */       }
/* 286 */       setOutputMode();
/* 287 */       int chunk = len;
/* 288 */       if (chunk > this.buffer.remaining()) {
/* 289 */         chunk = this.buffer.remaining();
/*     */       }
/* 291 */       this.buffer.get(b, off, chunk);
/* 292 */       return chunk;
/*     */     } finally {
/* 294 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/* 299 */     if (this.shutdown) {
/* 300 */       return -1;
/*     */     }
/* 302 */     if (b == null) {
/* 303 */       return 0;
/*     */     }
/* 305 */     return read(b, 0, b.length);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/util/SharedInputBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */