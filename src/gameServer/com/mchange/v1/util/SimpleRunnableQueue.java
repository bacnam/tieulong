/*     */ package com.mchange.v1.util;
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
/*     */   implements RunnableQueue
/*     */ {
/*  46 */   private List taskList = new LinkedList();
/*  47 */   private Thread t = new TaskThread();
/*     */ 
/*     */   
/*     */   public SimpleRunnableQueue(boolean paramBoolean) {
/*  51 */     this.t.setDaemon(paramBoolean);
/*  52 */     this.t.start();
/*     */   }
/*     */   
/*     */   public SimpleRunnableQueue() {
/*  56 */     this(true);
/*     */   }
/*     */   
/*     */   public synchronized void postRunnable(Runnable paramRunnable) {
/*  60 */     this.taskList.add(paramRunnable);
/*  61 */     notifyAll();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void close() {
/*  66 */     this.t.interrupt();
/*  67 */     this.taskList = null;
/*  68 */     this.t = null;
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized Runnable dequeueRunnable() {
/*  73 */     Runnable runnable = this.taskList.get(0);
/*  74 */     this.taskList.remove(0);
/*  75 */     return runnable;
/*     */   }
/*     */   
/*     */   private synchronized void awaitTask() throws InterruptedException {
/*  79 */     for (; this.taskList.size() == 0; wait());
/*     */   }
/*     */   
/*     */   class TaskThread extends Thread {
/*     */     TaskThread() {
/*  84 */       super("SimpleRunnableQueue.TaskThread");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/*     */         while (true) {
/*  92 */           SimpleRunnableQueue.this.awaitTask();
/*  93 */           Runnable runnable = SimpleRunnableQueue.this.dequeueRunnable();
/*     */           try {
/*  95 */             runnable.run();
/*  96 */           } catch (Exception exception) {
/*     */             
/*  98 */             System.err.println(getClass().getName() + " -- Unexpected exception in task!");
/*     */             
/* 100 */             exception.printStackTrace();
/*     */           }
/*     */         
/*     */         } 
/* 104 */       } catch (InterruptedException interruptedException) {
/*     */ 
/*     */         
/* 107 */         System.err.println(toString() + " interrupted. Shutting down.");
/*     */         return;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/SimpleRunnableQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */