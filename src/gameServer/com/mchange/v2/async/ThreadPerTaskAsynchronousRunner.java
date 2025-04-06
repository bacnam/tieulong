/*     */ package com.mchange.v2.async;
/*     */ 
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import com.mchange.v2.util.ResourceClosedException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ThreadPerTaskAsynchronousRunner
/*     */   implements AsynchronousRunner
/*     */ {
/*     */   static final int PRESUME_DEADLOCKED_MULTIPLE = 3;
/*  46 */   static final MLogger logger = MLog.getLogger(ThreadPerTaskAsynchronousRunner.class);
/*     */ 
/*     */   
/*     */   final int max_task_threads;
/*     */   
/*     */   final long interrupt_task_delay;
/*     */   
/*  53 */   LinkedList queue = new LinkedList();
/*  54 */   ArrayList running = new ArrayList();
/*  55 */   ArrayList deadlockSnapshot = null;
/*     */   
/*     */   boolean still_open = true;
/*     */   
/*  59 */   Thread dispatchThread = new DispatchThread();
/*     */   Timer interruptAndDeadlockTimer;
/*     */   
/*     */   public ThreadPerTaskAsynchronousRunner(int paramInt) {
/*  63 */     this(paramInt, 0L);
/*     */   }
/*     */   
/*     */   public ThreadPerTaskAsynchronousRunner(int paramInt, long paramLong) {
/*  67 */     this.max_task_threads = paramInt;
/*  68 */     this.interrupt_task_delay = paramLong;
/*  69 */     if (hasIdTimer()) {
/*     */       
/*  71 */       this.interruptAndDeadlockTimer = new Timer(true);
/*  72 */       TimerTask timerTask = new TimerTask()
/*     */         {
/*     */           public void run() {
/*  75 */             ThreadPerTaskAsynchronousRunner.this.checkForDeadlock(); }
/*     */         };
/*  77 */       long l = paramLong * 3L;
/*  78 */       this.interruptAndDeadlockTimer.schedule(timerTask, l, l);
/*     */     } 
/*     */     
/*  81 */     this.dispatchThread.start();
/*     */   }
/*     */   
/*     */   private boolean hasIdTimer() {
/*  85 */     return (this.interrupt_task_delay > 0L);
/*     */   }
/*     */   
/*     */   public synchronized void postRunnable(Runnable paramRunnable) {
/*  89 */     if (this.still_open) {
/*     */       
/*  91 */       this.queue.add(paramRunnable);
/*  92 */       notifyAll();
/*     */     } else {
/*     */       
/*  95 */       throw new ResourceClosedException("Attempted to use a ThreadPerTaskAsynchronousRunner in a closed or broken state.");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void close() {
/* 100 */     close(true);
/*     */   }
/*     */   
/*     */   public synchronized void close(boolean paramBoolean) {
/* 104 */     if (this.still_open) {
/*     */       
/* 106 */       this.still_open = false;
/* 107 */       if (paramBoolean) {
/*     */         
/* 109 */         this.queue.clear();
/* 110 */         for (Iterator<Thread> iterator = this.running.iterator(); iterator.hasNext();)
/* 111 */           ((Thread)iterator.next()).interrupt(); 
/* 112 */         closeThreadResources();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized int getRunningCount() {
/* 118 */     return this.running.size();
/*     */   }
/*     */   public synchronized Collection getRunningTasks() {
/* 121 */     return (Collection)this.running.clone();
/*     */   }
/*     */   public synchronized int getWaitingCount() {
/* 124 */     return this.queue.size();
/*     */   }
/*     */   public synchronized Collection getWaitingTasks() {
/* 127 */     return (Collection)this.queue.clone();
/*     */   }
/*     */   public synchronized boolean isClosed() {
/* 130 */     return !this.still_open;
/*     */   }
/*     */   public synchronized boolean isDoneAndGone() {
/* 133 */     return (!this.dispatchThread.isAlive() && this.running.isEmpty() && this.interruptAndDeadlockTimer == null);
/*     */   }
/*     */   
/*     */   private synchronized void acknowledgeComplete(TaskThread paramTaskThread) {
/* 137 */     if (!paramTaskThread.isCompleted()) {
/*     */       
/* 139 */       this.running.remove(paramTaskThread);
/* 140 */       paramTaskThread.markCompleted();
/* 141 */       notifyAll();
/*     */       
/* 143 */       if (!this.still_open && this.queue.isEmpty() && this.running.isEmpty()) {
/* 144 */         closeThreadResources();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private synchronized void checkForDeadlock() {
/* 150 */     if (this.deadlockSnapshot == null) {
/*     */       
/* 152 */       if (this.running.size() == this.max_task_threads) {
/* 153 */         this.deadlockSnapshot = (ArrayList)this.running.clone();
/*     */       }
/* 155 */     } else if (this.running.size() < this.max_task_threads) {
/* 156 */       this.deadlockSnapshot = null;
/* 157 */     } else if (this.deadlockSnapshot.equals(this.running)) {
/*     */       
/* 159 */       if (logger.isLoggable(MLevel.WARNING)) {
/*     */         
/* 161 */         StringBuffer stringBuffer = new StringBuffer(1024);
/* 162 */         stringBuffer.append("APPARENT DEADLOCK! (");
/* 163 */         stringBuffer.append(this);
/* 164 */         stringBuffer.append(") Deadlocked threads (unresponsive to interrupt()) are being set aside as hopeless and up to ");
/* 165 */         stringBuffer.append(this.max_task_threads);
/* 166 */         stringBuffer.append(" may now be spawned for new tasks. If tasks continue to deadlock, you may run out of memory. Deadlocked task list: "); byte b1; int j;
/* 167 */         for (b1 = 0, j = this.deadlockSnapshot.size(); b1 < j; b1++) {
/*     */           
/* 169 */           if (b1 != 0) stringBuffer.append(", "); 
/* 170 */           stringBuffer.append(((TaskThread)this.deadlockSnapshot.get(b1)).getTask());
/*     */         } 
/*     */         
/* 173 */         logger.log(MLevel.WARNING, stringBuffer.toString());
/*     */       } 
/*     */       
/*     */       byte b;
/*     */       int i;
/* 178 */       for (b = 0, i = this.deadlockSnapshot.size(); b < i; b++)
/* 179 */         acknowledgeComplete(this.deadlockSnapshot.get(b)); 
/* 180 */       this.deadlockSnapshot = null;
/*     */     } else {
/*     */       
/* 183 */       this.deadlockSnapshot = (ArrayList)this.running.clone();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void closeThreadResources() {
/* 188 */     if (this.interruptAndDeadlockTimer != null) {
/*     */       
/* 190 */       this.interruptAndDeadlockTimer.cancel();
/* 191 */       this.interruptAndDeadlockTimer = null;
/*     */     } 
/* 193 */     this.dispatchThread.interrupt();
/*     */   }
/*     */   
/*     */   class DispatchThread
/*     */     extends Thread {
/*     */     DispatchThread() {
/* 199 */       super("Dispatch-Thread-for-" + ThreadPerTaskAsynchronousRunner.this);
/*     */     }
/*     */     
/*     */     public void run() {
/* 203 */       synchronized (ThreadPerTaskAsynchronousRunner.this) {
/*     */         
/*     */         while (true) {
/*     */ 
/*     */ 
/*     */           
/* 209 */           try { if (ThreadPerTaskAsynchronousRunner.this.queue.isEmpty() || ThreadPerTaskAsynchronousRunner.this.running.size() == ThreadPerTaskAsynchronousRunner.this.max_task_threads) {
/* 210 */               ThreadPerTaskAsynchronousRunner.this.wait(); continue;
/*     */             } 
/* 212 */             Runnable runnable = ThreadPerTaskAsynchronousRunner.this.queue.remove(0);
/* 213 */             ThreadPerTaskAsynchronousRunner.TaskThread taskThread = new ThreadPerTaskAsynchronousRunner.TaskThread(runnable);
/* 214 */             taskThread.start();
/* 215 */             ThreadPerTaskAsynchronousRunner.this.running.add(taskThread);
/*     */              }
/*     */           
/* 218 */           catch (InterruptedException interruptedException) { break; }
/*     */         
/* 220 */         }  if (ThreadPerTaskAsynchronousRunner.this.still_open) {
/*     */           
/* 222 */           if (ThreadPerTaskAsynchronousRunner.logger.isLoggable(MLevel.WARNING))
/* 223 */             ThreadPerTaskAsynchronousRunner.logger.log(MLevel.WARNING, getName() + " unexpectedly interrupted! Shutting down!"); 
/* 224 */           ThreadPerTaskAsynchronousRunner.this.close(false);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   class TaskThread
/*     */     extends Thread
/*     */   {
/*     */     Runnable r;
/*     */     
/*     */     boolean completed = false;
/*     */ 
/*     */     
/*     */     TaskThread(Runnable param1Runnable) {
/* 241 */       super("Task-Thread-for-" + ThreadPerTaskAsynchronousRunner.this);
/* 242 */       this.r = param1Runnable;
/*     */     }
/*     */     
/*     */     Runnable getTask() {
/* 246 */       return this.r;
/*     */     }
/*     */     synchronized void markCompleted() {
/* 249 */       this.completed = true;
/*     */     }
/*     */     synchronized boolean isCompleted() {
/* 252 */       return this.completed;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 258 */         if (ThreadPerTaskAsynchronousRunner.this.hasIdTimer()) {
/*     */           
/* 260 */           TimerTask timerTask = new TimerTask()
/*     */             {
/*     */               public void run() {
/* 263 */                 ThreadPerTaskAsynchronousRunner.TaskThread.this.interrupt(); }
/*     */             };
/* 265 */           ThreadPerTaskAsynchronousRunner.this.interruptAndDeadlockTimer.schedule(timerTask, ThreadPerTaskAsynchronousRunner.this.interrupt_task_delay);
/*     */         } 
/* 267 */         this.r.run();
/*     */       } finally {
/*     */         
/* 270 */         ThreadPerTaskAsynchronousRunner.this.acknowledgeComplete(this);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/async/ThreadPerTaskAsynchronousRunner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */