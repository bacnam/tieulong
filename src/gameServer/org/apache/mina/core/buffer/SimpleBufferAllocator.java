/*     */ package org.apache.mina.core.buffer;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleBufferAllocator
/*     */   implements IoBufferAllocator
/*     */ {
/*     */   public IoBuffer allocate(int capacity, boolean direct) {
/*  34 */     return wrap(allocateNioBuffer(capacity, direct));
/*     */   }
/*     */   
/*     */   public ByteBuffer allocateNioBuffer(int capacity, boolean direct) {
/*     */     ByteBuffer nioBuffer;
/*  39 */     if (direct) {
/*  40 */       nioBuffer = ByteBuffer.allocateDirect(capacity);
/*     */     } else {
/*  42 */       nioBuffer = ByteBuffer.allocate(capacity);
/*     */     } 
/*  44 */     return nioBuffer;
/*     */   }
/*     */   
/*     */   public IoBuffer wrap(ByteBuffer nioBuffer) {
/*  48 */     return new SimpleBuffer(nioBuffer);
/*     */   }
/*     */   
/*     */   public void dispose() {}
/*     */   
/*     */   private class SimpleBuffer
/*     */     extends AbstractIoBuffer
/*     */   {
/*     */     private ByteBuffer buf;
/*     */     
/*     */     protected SimpleBuffer(ByteBuffer buf) {
/*  59 */       super(SimpleBufferAllocator.this, buf.capacity());
/*  60 */       this.buf = buf;
/*  61 */       buf.order(ByteOrder.BIG_ENDIAN);
/*     */     }
/*     */     
/*     */     protected SimpleBuffer(SimpleBuffer parent, ByteBuffer buf) {
/*  65 */       super(parent);
/*  66 */       this.buf = buf;
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteBuffer buf() {
/*  71 */       return this.buf;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void buf(ByteBuffer buf) {
/*  76 */       this.buf = buf;
/*     */     }
/*     */ 
/*     */     
/*     */     protected IoBuffer duplicate0() {
/*  81 */       return new SimpleBuffer(this, this.buf.duplicate());
/*     */     }
/*     */ 
/*     */     
/*     */     protected IoBuffer slice0() {
/*  86 */       return new SimpleBuffer(this, this.buf.slice());
/*     */     }
/*     */ 
/*     */     
/*     */     protected IoBuffer asReadOnlyBuffer0() {
/*  91 */       return new SimpleBuffer(this, this.buf.asReadOnlyBuffer());
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] array() {
/*  96 */       return this.buf.array();
/*     */     }
/*     */ 
/*     */     
/*     */     public int arrayOffset() {
/* 101 */       return this.buf.arrayOffset();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasArray() {
/* 106 */       return this.buf.hasArray();
/*     */     }
/*     */     
/*     */     public void free() {}
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/buffer/SimpleBufferAllocator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */