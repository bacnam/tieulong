/*     */ package org.apache.mina.util.byteaccess;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Stack;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ByteArrayPool
/*     */   implements ByteArrayFactory
/*     */ {
/*  37 */   private final int MAX_BITS = 32;
/*     */   
/*     */   private boolean freed;
/*     */   
/*     */   private final boolean direct;
/*     */   
/*     */   private ArrayList<Stack<DirectBufferByteArray>> freeBuffers;
/*     */   
/*  45 */   private int freeBufferCount = 0;
/*     */   
/*  47 */   private long freeMemory = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int maxFreeBuffers;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int maxFreeMemory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteArrayPool(boolean direct, int maxFreeBuffers, int maxFreeMemory) {
/*  64 */     this.direct = direct;
/*  65 */     this.freeBuffers = new ArrayList<Stack<DirectBufferByteArray>>();
/*  66 */     for (int i = 0; i < 32; i++) {
/*  67 */       this.freeBuffers.add(new Stack<DirectBufferByteArray>());
/*     */     }
/*  69 */     this.maxFreeBuffers = maxFreeBuffers;
/*  70 */     this.maxFreeMemory = maxFreeMemory;
/*  71 */     this.freed = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteArray create(int size) {
/*  81 */     if (size < 1) {
/*  82 */       throw new IllegalArgumentException("Buffer size must be at least 1: " + size);
/*     */     }
/*  84 */     int bits = bits(size);
/*  85 */     synchronized (this) {
/*  86 */       if (!((Stack)this.freeBuffers.get(bits)).isEmpty()) {
/*  87 */         DirectBufferByteArray directBufferByteArray = ((Stack<DirectBufferByteArray>)this.freeBuffers.get(bits)).pop();
/*  88 */         directBufferByteArray.setFreed(false);
/*  89 */         directBufferByteArray.getSingleIoBuffer().limit(size);
/*  90 */         return directBufferByteArray;
/*     */       } 
/*     */     } 
/*     */     
/*  94 */     int bbSize = 1 << bits;
/*  95 */     IoBuffer bb = IoBuffer.allocate(bbSize, this.direct);
/*  96 */     bb.limit(size);
/*  97 */     DirectBufferByteArray ba = new DirectBufferByteArray(bb);
/*  98 */     ba.setFreed(false);
/*  99 */     return ba;
/*     */   }
/*     */   
/*     */   private int bits(int index) {
/* 103 */     int bits = 0;
/* 104 */     while (1 << bits < index) {
/* 105 */       bits++;
/*     */     }
/* 107 */     return bits;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void free() {
/* 115 */     synchronized (this) {
/* 116 */       if (this.freed) {
/* 117 */         throw new IllegalStateException("Already freed.");
/*     */       }
/* 119 */       this.freed = true;
/* 120 */       this.freeBuffers.clear();
/* 121 */       this.freeBuffers = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private class DirectBufferByteArray
/*     */     extends BufferByteArray {
/*     */     private boolean freed;
/*     */     
/*     */     public DirectBufferByteArray(IoBuffer bb) {
/* 130 */       super(bb);
/*     */     }
/*     */     
/*     */     public void setFreed(boolean freed) {
/* 134 */       this.freed = freed;
/*     */     }
/*     */ 
/*     */     
/*     */     public void free() {
/* 139 */       synchronized (this) {
/* 140 */         if (this.freed) {
/* 141 */           throw new IllegalStateException("Already freed.");
/*     */         }
/* 143 */         this.freed = true;
/*     */       } 
/* 145 */       int bits = ByteArrayPool.this.bits(last());
/* 146 */       synchronized (ByteArrayPool.this) {
/* 147 */         if (ByteArrayPool.this.freeBuffers != null && ByteArrayPool.this.freeBufferCount < ByteArrayPool.this.maxFreeBuffers && ByteArrayPool.this.freeMemory + last() <= ByteArrayPool.this.maxFreeMemory) {
/* 148 */           ((Stack<DirectBufferByteArray>)ByteArrayPool.this.freeBuffers.get(bits)).push(this);
/* 149 */           ByteArrayPool.this.freeBufferCount++;
/* 150 */           ByteArrayPool.this.freeMemory += last();
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/byteaccess/ByteArrayPool.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */