/*     */ package com.mchange.v1.lang;
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
/*     */ public abstract class GentleThread
/*     */   extends Thread
/*     */ {
/*     */   boolean should_stop = false;
/*     */   boolean should_suspend = false;
/*     */   
/*     */   public GentleThread() {}
/*     */   
/*     */   public GentleThread(String paramString) {
/*  53 */     super(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void run();
/*     */ 
/*     */   
/*     */   public synchronized void gentleStop() {
/*  61 */     this.should_stop = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void gentleSuspend() {
/*  67 */     this.should_suspend = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void gentleResume() {
/*  74 */     this.should_suspend = false;
/*  75 */     notifyAll();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized boolean shouldStop() {
/*  85 */     return this.should_stop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized boolean shouldSuspend() {
/*  96 */     return this.should_suspend;
/*     */   }
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
/*     */   protected synchronized void allowSuspend() throws InterruptedException {
/* 109 */     for (; this.should_suspend; wait());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/lang/GentleThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */