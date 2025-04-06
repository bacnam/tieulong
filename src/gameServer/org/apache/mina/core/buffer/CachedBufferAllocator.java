/*     */ package org.apache.mina.core.buffer;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CachedBufferAllocator
/*     */   implements IoBufferAllocator
/*     */ {
/*     */   private static final int DEFAULT_MAX_POOL_SIZE = 8;
/*     */   private static final int DEFAULT_MAX_CACHED_BUFFER_SIZE = 262144;
/*     */   private final int maxPoolSize;
/*     */   private final int maxCachedBufferSize;
/*     */   private final ThreadLocal<Map<Integer, Queue<CachedBuffer>>> heapBuffers;
/*     */   private final ThreadLocal<Map<Integer, Queue<CachedBuffer>>> directBuffers;
/*     */   
/*     */   public CachedBufferAllocator() {
/*  79 */     this(8, 262144);
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
/*     */   public CachedBufferAllocator(int maxPoolSize, int maxCachedBufferSize) {
/*  92 */     if (maxPoolSize < 0) {
/*  93 */       throw new IllegalArgumentException("maxPoolSize: " + maxPoolSize);
/*     */     }
/*     */     
/*  96 */     if (maxCachedBufferSize < 0) {
/*  97 */       throw new IllegalArgumentException("maxCachedBufferSize: " + maxCachedBufferSize);
/*     */     }
/*     */     
/* 100 */     this.maxPoolSize = maxPoolSize;
/* 101 */     this.maxCachedBufferSize = maxCachedBufferSize;
/*     */     
/* 103 */     this.heapBuffers = new ThreadLocal<Map<Integer, Queue<CachedBuffer>>>()
/*     */       {
/*     */         protected Map<Integer, Queue<CachedBufferAllocator.CachedBuffer>> initialValue() {
/* 106 */           return CachedBufferAllocator.this.newPoolMap();
/*     */         }
/*     */       };
/*     */     
/* 110 */     this.directBuffers = new ThreadLocal<Map<Integer, Queue<CachedBuffer>>>()
/*     */       {
/*     */         protected Map<Integer, Queue<CachedBufferAllocator.CachedBuffer>> initialValue() {
/* 113 */           return CachedBufferAllocator.this.newPoolMap();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxPoolSize() {
/* 123 */     return this.maxPoolSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxCachedBufferSize() {
/* 132 */     return this.maxCachedBufferSize;
/*     */   }
/*     */   
/*     */   Map<Integer, Queue<CachedBuffer>> newPoolMap() {
/* 136 */     Map<Integer, Queue<CachedBuffer>> poolMap = new HashMap<Integer, Queue<CachedBuffer>>();
/*     */     
/* 138 */     for (int i = 0; i < 31; i++) {
/* 139 */       poolMap.put(Integer.valueOf(1 << i), new ConcurrentLinkedQueue<CachedBuffer>());
/*     */     }
/*     */     
/* 142 */     poolMap.put(Integer.valueOf(0), new ConcurrentLinkedQueue<CachedBuffer>());
/* 143 */     poolMap.put(Integer.valueOf(2147483647), new ConcurrentLinkedQueue<CachedBuffer>());
/*     */     
/* 145 */     return poolMap;
/*     */   }
/*     */   public IoBuffer allocate(int requestedCapacity, boolean direct) {
/*     */     IoBuffer buf;
/* 149 */     int actualCapacity = IoBuffer.normalizeCapacity(requestedCapacity);
/*     */ 
/*     */     
/* 152 */     if (this.maxCachedBufferSize != 0 && actualCapacity > this.maxCachedBufferSize) {
/* 153 */       if (direct) {
/* 154 */         buf = wrap(ByteBuffer.allocateDirect(actualCapacity));
/*     */       } else {
/* 156 */         buf = wrap(ByteBuffer.allocate(actualCapacity));
/*     */       } 
/*     */     } else {
/*     */       Queue<CachedBuffer> pool;
/*     */       
/* 161 */       if (direct) {
/* 162 */         pool = (Queue<CachedBuffer>)((Map)this.directBuffers.get()).get(Integer.valueOf(actualCapacity));
/*     */       } else {
/* 164 */         pool = (Queue<CachedBuffer>)((Map)this.heapBuffers.get()).get(Integer.valueOf(actualCapacity));
/*     */       } 
/*     */ 
/*     */       
/* 168 */       buf = pool.poll();
/*     */       
/* 170 */       if (buf != null) {
/* 171 */         buf.clear();
/* 172 */         buf.setAutoExpand(false);
/* 173 */         buf.order(ByteOrder.BIG_ENDIAN);
/*     */       }
/* 175 */       else if (direct) {
/* 176 */         buf = wrap(ByteBuffer.allocateDirect(actualCapacity));
/*     */       } else {
/* 178 */         buf = wrap(ByteBuffer.allocate(actualCapacity));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 183 */     buf.limit(requestedCapacity);
/* 184 */     return buf;
/*     */   }
/*     */   
/*     */   public ByteBuffer allocateNioBuffer(int capacity, boolean direct) {
/* 188 */     return allocate(capacity, direct).buf();
/*     */   }
/*     */   
/*     */   public IoBuffer wrap(ByteBuffer nioBuffer) {
/* 192 */     return new CachedBuffer(nioBuffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {}
/*     */   
/*     */   private class CachedBuffer
/*     */     extends AbstractIoBuffer
/*     */   {
/*     */     private final Thread ownerThread;
/*     */     private ByteBuffer buf;
/*     */     
/*     */     protected CachedBuffer(ByteBuffer buf) {
/* 205 */       super(CachedBufferAllocator.this, buf.capacity());
/* 206 */       this.ownerThread = Thread.currentThread();
/* 207 */       this.buf = buf;
/* 208 */       buf.order(ByteOrder.BIG_ENDIAN);
/*     */     }
/*     */     
/*     */     protected CachedBuffer(CachedBuffer parent, ByteBuffer buf) {
/* 212 */       super(parent);
/* 213 */       this.ownerThread = Thread.currentThread();
/* 214 */       this.buf = buf;
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteBuffer buf() {
/* 219 */       if (this.buf == null) {
/* 220 */         throw new IllegalStateException("Buffer has been freed already.");
/*     */       }
/* 222 */       return this.buf;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void buf(ByteBuffer buf) {
/* 227 */       ByteBuffer oldBuf = this.buf;
/* 228 */       this.buf = buf;
/* 229 */       free(oldBuf);
/*     */     }
/*     */ 
/*     */     
/*     */     protected IoBuffer duplicate0() {
/* 234 */       return new CachedBuffer(this, buf().duplicate());
/*     */     }
/*     */ 
/*     */     
/*     */     protected IoBuffer slice0() {
/* 239 */       return new CachedBuffer(this, buf().slice());
/*     */     }
/*     */ 
/*     */     
/*     */     protected IoBuffer asReadOnlyBuffer0() {
/* 244 */       return new CachedBuffer(this, buf().asReadOnlyBuffer());
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] array() {
/* 249 */       return buf().array();
/*     */     }
/*     */ 
/*     */     
/*     */     public int arrayOffset() {
/* 254 */       return buf().arrayOffset();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasArray() {
/* 259 */       return buf().hasArray();
/*     */     }
/*     */ 
/*     */     
/*     */     public void free() {
/* 264 */       free(this.buf);
/* 265 */       this.buf = null;
/*     */     }
/*     */     private void free(ByteBuffer oldBuf) {
/*     */       Queue<CachedBuffer> pool;
/* 269 */       if (oldBuf == null || (CachedBufferAllocator.this.maxCachedBufferSize != 0 && oldBuf.capacity() > CachedBufferAllocator.this.maxCachedBufferSize) || oldBuf.isReadOnly() || isDerived() || Thread.currentThread() != this.ownerThread) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 277 */       if (oldBuf.isDirect()) {
/* 278 */         pool = (Queue<CachedBuffer>)((Map)CachedBufferAllocator.this.directBuffers.get()).get(Integer.valueOf(oldBuf.capacity()));
/*     */       } else {
/* 280 */         pool = (Queue<CachedBuffer>)((Map)CachedBufferAllocator.this.heapBuffers.get()).get(Integer.valueOf(oldBuf.capacity()));
/*     */       } 
/*     */       
/* 283 */       if (pool == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 288 */       if (CachedBufferAllocator.this.maxPoolSize == 0 || pool.size() < CachedBufferAllocator.this.maxPoolSize)
/* 289 */         pool.offer(new CachedBuffer(oldBuf)); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/buffer/CachedBufferAllocator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */