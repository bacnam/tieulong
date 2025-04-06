/*     */ package com.jolbox.bonecp;
/*     */ 
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import jsr166y.LinkedTransferQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BoundedLinkedTransferQueue<E>
/*     */   extends LinkedTransferQueue<E>
/*     */ {
/*     */   private static final long serialVersionUID = -1875525368357897907L;
/*  35 */   private AtomicInteger size = new AtomicInteger();
/*     */   
/*     */   private final int maxQueueSize;
/*     */   
/*  39 */   private final ReentrantLock lock = new ReentrantLock();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundedLinkedTransferQueue(int maxQueueSize) {
/*  45 */     this.maxQueueSize = maxQueueSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  50 */     return this.size.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int remainingCapacity() {
/*  60 */     return this.maxQueueSize - this.size.get();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E poll() {
/*  66 */     E result = (E)super.poll();
/*     */     
/*  68 */     if (result != null) {
/*  69 */       this.size.decrementAndGet();
/*     */     }
/*     */     
/*  72 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E poll(long timeout, TimeUnit unit) throws InterruptedException {
/*  78 */     E result = (E)super.poll(timeout, unit);
/*     */     
/*  80 */     if (result != null) {
/*  81 */       this.size.decrementAndGet();
/*     */     }
/*     */     
/*  84 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean tryTransfer(E e) {
/*  89 */     boolean result = super.tryTransfer(e);
/*  90 */     if (result) {
/*  91 */       this.size.incrementAndGet();
/*     */     }
/*  93 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean offer(E e) {
/* 103 */     boolean result = false;
/* 104 */     this.lock.lock();
/*     */     try {
/* 106 */       if (this.size.get() < this.maxQueueSize) {
/* 107 */         super.put(e);
/* 108 */         this.size.incrementAndGet();
/* 109 */         result = true;
/*     */       } 
/*     */     } finally {
/* 112 */       this.lock.unlock();
/*     */     } 
/* 114 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(E e) {
/* 119 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/BoundedLinkedTransferQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */