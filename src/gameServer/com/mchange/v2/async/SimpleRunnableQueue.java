/*     */ package com.mchange.v2.async;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleRunnableQueue
/*     */   implements RunnableQueue, Queuable
/*     */ {
/*  46 */   private List taskList = new LinkedList();
/*  47 */   private Thread t = new TaskThread();
/*     */   
/*     */   boolean gentle_close_requested = false;
/*     */ 
/*     */   
/*     */   public SimpleRunnableQueue(boolean paramBoolean) {
/*  53 */     this.t.setDaemon(paramBoolean);
/*  54 */     this.t.start();
/*     */   }
/*     */   
/*     */   public SimpleRunnableQueue() {
/*  58 */     this(true);
/*     */   }
/*     */   public RunnableQueue asRunnableQueue() {
/*  61 */     return this;
/*     */   }
/*     */   
/*     */   public synchronized void postRunnable(Runnable paramRunnable) {
/*  65 */     if (this.gentle_close_requested) {
/*  66 */       throw new IllegalStateException("Attempted to post a task to a closed AsynchronousRunner.");
/*     */     }
/*     */     
/*  69 */     this.taskList.add(paramRunnable);
/*  70 */     notifyAll();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void close(boolean paramBoolean) {
/*  75 */     if (paramBoolean) {
/*  76 */       this.t.interrupt();
/*     */     } else {
/*  78 */       this.gentle_close_requested = true;
/*     */     } 
/*     */   }
/*     */   public synchronized void close() {
/*  82 */     close(true);
/*     */   }
/*     */   
/*     */   private synchronized Runnable dequeueRunnable() {
/*  86 */     Runnable runnable = this.taskList.get(0);
/*  87 */     this.taskList.remove(0);
/*  88 */     return runnable;
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized void awaitTask() throws InterruptedException {
/*  93 */     while (this.taskList.size() == 0) {
/*     */       
/*  95 */       if (this.gentle_close_requested)
/*  96 */         this.t.interrupt(); 
/*  97 */       wait();
/*     */     } 
/*     */   }
/*     */   
/*     */   class TaskThread
/*     */     extends Thread {
/*     */     TaskThread() {
/* 104 */       super("SimpleRunnableQueue.TaskThread");
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 110 */         while (!isInterrupted()) {
/*     */           
/* 112 */           SimpleRunnableQueue.this.awaitTask();
/* 113 */           Runnable runnable = SimpleRunnableQueue.this.dequeueRunnable();
/*     */           try {
/* 115 */             runnable.run();
/* 116 */           } catch (Exception exception) {
/*     */             
/* 118 */             System.err.println(getClass().getName() + " -- Unexpected exception in task!");
/*     */             
/* 120 */             exception.printStackTrace();
/*     */           }
/*     */         
/*     */         } 
/* 124 */       } catch (InterruptedException interruptedException) {
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */       finally {
/*     */         
/* 131 */         SimpleRunnableQueue.this.taskList = null;
/* 132 */         SimpleRunnableQueue.this.t = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/async/SimpleRunnableQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */