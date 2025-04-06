/*     */ package com.mchange.v2.lock;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExactReentrantSharedUseExclusiveUseLock
/*     */   implements SharedUseExclusiveUseLock
/*     */ {
/*  56 */   Set waitingShared = new HashSet();
/*  57 */   List activeShared = new LinkedList();
/*     */   
/*  59 */   Set waitingExclusive = new HashSet();
/*  60 */   Thread activeExclusive = null;
/*     */   
/*  62 */   int exclusive_shared_reentries = 0;
/*  63 */   int exclusive_exclusive_reentries = 0;
/*     */   
/*     */   String name;
/*     */   
/*     */   public ExactReentrantSharedUseExclusiveUseLock(String paramString) {
/*  68 */     this.name = paramString;
/*     */   }
/*     */   public ExactReentrantSharedUseExclusiveUseLock() {
/*  71 */     this(null);
/*     */   }
/*     */   
/*     */   void status(String paramString) {
/*  75 */     System.err.println(this + " -- after " + paramString);
/*  76 */     System.err.println("waitingShared: " + this.waitingShared);
/*  77 */     System.err.println("activeShared: " + this.activeShared);
/*  78 */     System.err.println("waitingExclusive: " + this.waitingExclusive);
/*  79 */     System.err.println("activeExclusive: " + this.activeExclusive);
/*  80 */     System.err.println("exclusive_shared_reentries: " + this.exclusive_shared_reentries);
/*  81 */     System.err.println("exclusive_exclusive_reentries: " + this.exclusive_exclusive_reentries);
/*  82 */     System.err.println(" ---- ");
/*  83 */     System.err.println();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void acquireShared() throws InterruptedException {
/*  88 */     Thread thread = Thread.currentThread();
/*  89 */     if (thread == this.activeExclusive) {
/*  90 */       this.exclusive_shared_reentries++;
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*  95 */         this.waitingShared.add(thread);
/*  96 */         while (!okayForShared())
/*  97 */           wait(); 
/*  98 */         this.activeShared.add(thread);
/*     */       }
/*     */       finally {
/*     */         
/* 102 */         this.waitingShared.remove(thread);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void relinquishShared() {
/* 110 */     Thread thread = Thread.currentThread();
/* 111 */     if (thread == this.activeExclusive) {
/*     */       
/* 113 */       this.exclusive_shared_reentries--;
/* 114 */       if (this.exclusive_shared_reentries < 0) {
/* 115 */         throw new IllegalStateException(thread + " relinquished a shared lock (reentrant on exclusive) it did not hold!");
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 123 */       boolean bool = this.activeShared.remove(thread);
/* 124 */       if (!bool)
/* 125 */         throw new IllegalStateException(thread + " relinquished a shared lock it did not hold!"); 
/* 126 */       notifyAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void acquireExclusive() throws InterruptedException {
/* 133 */     Thread thread = Thread.currentThread();
/* 134 */     if (thread == this.activeExclusive) {
/* 135 */       this.exclusive_exclusive_reentries++;
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/* 140 */         this.waitingExclusive.add(thread);
/* 141 */         while (!okayForExclusive(thread))
/* 142 */           wait(); 
/* 143 */         this.activeExclusive = thread;
/*     */       }
/*     */       finally {
/*     */         
/* 147 */         this.waitingExclusive.remove(thread);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void relinquishExclusive() {
/* 155 */     Thread thread = Thread.currentThread();
/* 156 */     if (thread != this.activeExclusive)
/* 157 */       throw new IllegalStateException(thread + " relinquished an exclusive lock it did not hold!"); 
/* 158 */     if (this.exclusive_exclusive_reentries > 0) {
/* 159 */       this.exclusive_exclusive_reentries--;
/*     */     } else {
/*     */       
/* 162 */       if (this.exclusive_shared_reentries != 0)
/* 163 */         throw new IllegalStateException(thread + " relinquished an exclusive lock while it had reentered but not yet relinquished shared lock acquisitions!"); 
/* 164 */       this.activeExclusive = null;
/* 165 */       notifyAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean okayForShared() {
/* 171 */     return (this.activeExclusive == null && this.waitingExclusive.size() == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean okayForExclusive(Thread paramThread) {
/* 180 */     int i = this.activeShared.size();
/* 181 */     if (i == 0)
/* 182 */       return (this.activeExclusive == null); 
/* 183 */     if (i == 1) {
/* 184 */       return (this.activeShared.get(0) == paramThread);
/*     */     }
/*     */     
/* 187 */     HashSet hashSet = new HashSet(this.activeShared);
/* 188 */     return (hashSet.size() == 1 && hashSet.contains(paramThread));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 193 */     return super.toString() + " [name=" + this.name + ']';
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/lock/ExactReentrantSharedUseExclusiveUseLock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */