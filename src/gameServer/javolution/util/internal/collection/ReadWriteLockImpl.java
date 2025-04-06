/*     */ package javolution.util.internal.collection;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ReadWriteLockImpl
/*     */   implements ReadWriteLock, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   
/*     */   public final class ReadLock
/*     */     implements Lock, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1536L;
/*     */     
/*     */     public void lock() {
/*     */       try {
/*  32 */         lockInterruptibly();
/*  33 */       } catch (InterruptedException e) {}
/*     */     }
/*     */ 
/*     */     
/*     */     public void lockInterruptibly() throws InterruptedException {
/*  38 */       synchronized (ReadWriteLockImpl.this) {
/*  39 */         if (ReadWriteLockImpl.this.writerThread == Thread.currentThread())
/*  40 */           return;  while (ReadWriteLockImpl.this.writerThread != null || ReadWriteLockImpl.this.waitingWriters != 0) {
/*  41 */           ReadWriteLockImpl.this.wait();
/*     */         }
/*  43 */         ReadWriteLockImpl.this.givenLocks++;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Condition newCondition() {
/*  49 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryLock() {
/*  54 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
/*  60 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public void unlock() {
/*  65 */       synchronized (ReadWriteLockImpl.this) {
/*  66 */         if (ReadWriteLockImpl.this.writerThread == Thread.currentThread())
/*  67 */           return;  assert ReadWriteLockImpl.this.givenLocks > 0;
/*  68 */         ReadWriteLockImpl.this.givenLocks--;
/*  69 */         ReadWriteLockImpl.this.notifyAll();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public final class WriteLock
/*     */     implements Lock, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1536L;
/*     */     
/*     */     public void lock() {
/*     */       try {
/*  81 */         lockInterruptibly();
/*  82 */       } catch (InterruptedException e) {}
/*     */     }
/*     */ 
/*     */     
/*     */     public void lockInterruptibly() throws InterruptedException {
/*  87 */       synchronized (ReadWriteLockImpl.this) {
/*  88 */         ReadWriteLockImpl.this.waitingWriters++;
/*  89 */         while (ReadWriteLockImpl.this.givenLocks != 0) {
/*  90 */           ReadWriteLockImpl.this.wait();
/*     */         }
/*  92 */         ReadWriteLockImpl.this.waitingWriters--;
/*  93 */         ReadWriteLockImpl.this.writerThread = Thread.currentThread();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Condition newCondition() {
/*  99 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryLock() {
/* 104 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
/* 110 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public void unlock() {
/* 115 */       synchronized (ReadWriteLockImpl.this) {
/* 116 */         ReadWriteLockImpl.this.writerThread = null;
/* 117 */         ReadWriteLockImpl.this.notifyAll();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 123 */   public final ReadLock readLock = new ReadLock();
/* 124 */   public final WriteLock writeLock = new WriteLock();
/*     */   
/*     */   private transient int givenLocks;
/*     */   private transient int waitingWriters;
/*     */   private transient Thread writerThread;
/*     */   
/*     */   public ReadLock readLock() {
/* 131 */     return this.readLock;
/*     */   }
/*     */ 
/*     */   
/*     */   public WriteLock writeLock() {
/* 136 */     return this.writeLock;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/collection/ReadWriteLockImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */