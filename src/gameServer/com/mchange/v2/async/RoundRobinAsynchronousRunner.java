/*     */ package com.mchange.v2.async;
/*     */ 
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import com.mchange.v2.util.ResourceClosedException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RoundRobinAsynchronousRunner
/*     */   implements AsynchronousRunner, Queuable
/*     */ {
/*  49 */   private static final MLogger logger = MLog.getLogger(RoundRobinAsynchronousRunner.class);
/*     */ 
/*     */   
/*     */   final RunnableQueue[] rqs;
/*     */ 
/*     */   
/*  55 */   int task_turn = 0;
/*     */ 
/*     */   
/*  58 */   int view_turn = 0;
/*     */ 
/*     */   
/*     */   public RoundRobinAsynchronousRunner(int paramInt, boolean paramBoolean) {
/*  62 */     this.rqs = new RunnableQueue[paramInt];
/*  63 */     for (byte b = 0; b < paramInt; b++) {
/*  64 */       this.rqs[b] = new CarefulRunnableQueue(paramBoolean, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void postRunnable(Runnable paramRunnable) {
/*     */     try {
/*  71 */       int i = this.task_turn;
/*  72 */       this.task_turn = (this.task_turn + 1) % this.rqs.length;
/*  73 */       this.rqs[i].postRunnable(paramRunnable);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*  87 */     catch (NullPointerException nullPointerException) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  92 */       if (logger.isLoggable(MLevel.FINE)) {
/*  93 */         logger.log(MLevel.FINE, "NullPointerException while posting Runnable -- Probably we're closed.", nullPointerException);
/*     */       }
/*  95 */       close(true);
/*  96 */       throw new ResourceClosedException("Attempted to use a RoundRobinAsynchronousRunner in a closed or broken state.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized RunnableQueue asRunnableQueue() {
/*     */     try {
/* 104 */       int i = this.view_turn;
/* 105 */       this.view_turn = (this.view_turn + 1) % this.rqs.length;
/* 106 */       return new RunnableQueueView(i);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 114 */     catch (NullPointerException nullPointerException) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 119 */       if (logger.isLoggable(MLevel.FINE)) {
/* 120 */         logger.log(MLevel.FINE, "NullPointerException in asRunnableQueue() -- Probably we're closed.", nullPointerException);
/*     */       }
/* 122 */       close(true);
/* 123 */       throw new ResourceClosedException("Attempted to use a RoundRobinAsynchronousRunner in a closed or broken state.");
/*     */     } 
/*     */   }
/*     */   public synchronized void close(boolean paramBoolean) {
/*     */     byte b;
/*     */     int i;
/* 129 */     for (b = 0, i = this.rqs.length; b < i; b++) {
/*     */       
/* 131 */       attemptClose(this.rqs[b], paramBoolean);
/* 132 */       this.rqs[b] = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void close() {
/* 137 */     close(true);
/*     */   }
/*     */   static void attemptClose(RunnableQueue paramRunnableQueue, boolean paramBoolean) {
/*     */     try {
/* 141 */       paramRunnableQueue.close(paramBoolean);
/* 142 */     } catch (Exception exception) {
/*     */ 
/*     */       
/* 145 */       if (logger.isLoggable(MLevel.WARNING))
/* 146 */         logger.log(MLevel.WARNING, "RunnableQueue close FAILED.", exception); 
/*     */     } 
/*     */   }
/*     */   
/*     */   class RunnableQueueView
/*     */     implements RunnableQueue {
/*     */     final int rq_num;
/*     */     
/*     */     RunnableQueueView(int param1Int) {
/* 155 */       this.rq_num = param1Int;
/*     */     }
/*     */     public void postRunnable(Runnable param1Runnable) {
/* 158 */       RoundRobinAsynchronousRunner.this.rqs[this.rq_num].postRunnable(param1Runnable);
/*     */     }
/*     */     
/*     */     public void close(boolean param1Boolean) {}
/*     */     
/*     */     public void close() {}
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/async/RoundRobinAsynchronousRunner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */