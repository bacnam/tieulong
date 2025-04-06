/*     */ package com.mchange.v2.async;
/*     */ 
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import com.mchange.v2.util.ResourceClosedException;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CarefulRunnableQueue
/*     */   implements RunnableQueue, Queuable, StrandedTaskReporting
/*     */ {
/*  48 */   private static final MLogger logger = MLog.getLogger(CarefulRunnableQueue.class);
/*     */   
/*  50 */   private List taskList = new LinkedList();
/*  51 */   private TaskThread t = new TaskThread();
/*     */   
/*     */   private boolean shutdown_on_interrupt;
/*     */   
/*     */   private boolean gentle_close_requested = false;
/*     */   
/*  57 */   private List strandedTasks = null;
/*     */ 
/*     */   
/*     */   public CarefulRunnableQueue(boolean paramBoolean1, boolean paramBoolean2) {
/*  61 */     this.shutdown_on_interrupt = paramBoolean2;
/*  62 */     this.t.setDaemon(paramBoolean1);
/*  63 */     this.t.start();
/*     */   }
/*     */   
/*     */   public RunnableQueue asRunnableQueue() {
/*  67 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void postRunnable(Runnable paramRunnable) {
/*     */     try {
/*  73 */       if (this.gentle_close_requested) {
/*  74 */         throw new ResourceClosedException("Attempted to post a task to a closing CarefulRunnableQueue.");
/*     */       }
/*     */       
/*  77 */       this.taskList.add(paramRunnable);
/*  78 */       notifyAll();
/*     */     }
/*  80 */     catch (NullPointerException nullPointerException) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  85 */       if (logger.isLoggable(MLevel.FINE)) {
/*  86 */         logger.log(MLevel.FINE, "NullPointerException while posting Runnable.", nullPointerException);
/*     */       }
/*  88 */       if (this.taskList == null) {
/*  89 */         throw new ResourceClosedException("Attempted to post a task to a CarefulRunnableQueue which has been closed, or whose TaskThread has been interrupted.");
/*     */       }
/*     */ 
/*     */       
/*  93 */       throw nullPointerException;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void close(boolean paramBoolean) {
/*  99 */     if (paramBoolean) {
/*     */       
/* 101 */       this.t.safeStop();
/* 102 */       this.t.interrupt();
/*     */     } else {
/*     */       
/* 105 */       this.gentle_close_requested = true;
/*     */     } 
/*     */   }
/*     */   public synchronized void close() {
/* 109 */     close(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized List getStrandedTasks() {
/*     */     try {
/* 115 */       while (this.gentle_close_requested && this.taskList != null)
/* 116 */         wait(); 
/* 117 */       return this.strandedTasks;
/*     */     }
/* 119 */     catch (InterruptedException interruptedException) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 125 */       if (logger.isLoggable(MLevel.WARNING)) {
/* 126 */         logger.log(MLevel.WARNING, Thread.currentThread() + " interrupted while waiting for stranded tasks from CarefulRunnableQueue.", interruptedException);
/*     */       }
/*     */ 
/*     */       
/* 130 */       throw new RuntimeException(Thread.currentThread() + " interrupted while waiting for stranded tasks from CarefulRunnableQueue.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized Runnable dequeueRunnable() {
/* 137 */     Runnable runnable = this.taskList.get(0);
/* 138 */     this.taskList.remove(0);
/* 139 */     return runnable;
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized void awaitTask() throws InterruptedException {
/* 144 */     while (this.taskList.size() == 0) {
/*     */       
/* 146 */       if (this.gentle_close_requested) {
/*     */         
/* 148 */         this.t.safeStop();
/* 149 */         this.t.interrupt();
/*     */       } 
/* 151 */       wait();
/*     */     } 
/*     */   }
/*     */   
/*     */   class TaskThread
/*     */     extends Thread {
/*     */     boolean should_stop = false;
/*     */     
/*     */     TaskThread() {
/* 160 */       super("CarefulRunnableQueue.TaskThread");
/*     */     }
/*     */     public synchronized void safeStop() {
/* 163 */       this.should_stop = true;
/*     */     }
/*     */     private synchronized boolean shouldStop() {
/* 166 */       return this.should_stop;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 172 */         while (!shouldStop())
/*     */         {
/*     */           
/*     */           try {
/* 176 */             CarefulRunnableQueue.this.awaitTask();
/* 177 */             Runnable runnable = CarefulRunnableQueue.this.dequeueRunnable();
/*     */             try {
/* 179 */               runnable.run();
/* 180 */             } catch (Exception exception) {
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 185 */               if (CarefulRunnableQueue.logger.isLoggable(MLevel.WARNING)) {
/* 186 */                 CarefulRunnableQueue.logger.log(MLevel.WARNING, getClass().getName() + " -- Unexpected exception in task!", exception);
/*     */               }
/*     */             } 
/* 189 */           } catch (InterruptedException interruptedException) {
/*     */             
/* 191 */             if (CarefulRunnableQueue.this.shutdown_on_interrupt) {
/*     */               
/* 193 */               CarefulRunnableQueue.this.close(false);
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 198 */               if (CarefulRunnableQueue.logger.isLoggable(MLevel.INFO)) {
/* 199 */                 CarefulRunnableQueue.logger.info(toString() + " interrupted. Shutting down after current tasks" + " have completed.");
/*     */               }
/*     */ 
/*     */ 
/*     */               
/*     */               continue;
/*     */             } 
/*     */ 
/*     */             
/* 208 */             CarefulRunnableQueue.logger.info(toString() + " received interrupt. IGNORING.");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */       finally {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 234 */         synchronized (CarefulRunnableQueue.this) {
/*     */           
/* 236 */           CarefulRunnableQueue.this.strandedTasks = Collections.unmodifiableList(CarefulRunnableQueue.this.taskList);
/* 237 */           CarefulRunnableQueue.this.taskList = null;
/* 238 */           CarefulRunnableQueue.this.t = null;
/* 239 */           CarefulRunnableQueue.this.notifyAll();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/async/CarefulRunnableQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */