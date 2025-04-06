/*     */ package com.mchange.v2.async;
/*     */ 
/*     */ import com.mchange.v2.io.IndentedWriter;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import com.mchange.v2.util.ResourceClosedException;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
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
/*     */ public final class ThreadPoolAsynchronousRunner
/*     */   implements AsynchronousRunner
/*     */ {
/*  51 */   static final MLogger logger = MLog.getLogger(ThreadPoolAsynchronousRunner.class);
/*     */   
/*     */   static final int POLL_FOR_STOP_INTERVAL = 5000;
/*     */   
/*     */   static final int DFLT_DEADLOCK_DETECTOR_INTERVAL = 10000;
/*     */   
/*     */   static final int DFLT_INTERRUPT_DELAY_AFTER_APPARENT_DEADLOCK = 60000;
/*     */   
/*     */   static final int DFLT_MAX_INDIVIDUAL_TASK_TIME = 0;
/*     */   
/*     */   static final int DFLT_MAX_EMERGENCY_THREADS = 10;
/*     */   
/*     */   static final long PURGE_EVERY = 500L;
/*     */   
/*     */   int deadlock_detector_interval;
/*     */   int interrupt_delay_after_apparent_deadlock;
/*     */   int max_individual_task_time;
/*     */   int num_threads;
/*     */   boolean daemon;
/*     */   HashSet managed;
/*     */   HashSet available;
/*     */   LinkedList pendingTasks;
/*  73 */   Random rnd = new Random();
/*     */   
/*     */   Timer myTimer;
/*     */   
/*     */   boolean should_cancel_timer;
/*  78 */   TimerTask deadlockDetector = new DeadlockDetector();
/*  79 */   TimerTask replacedThreadInterruptor = null;
/*     */   
/*  81 */   Map stoppedThreadsToStopDates = new HashMap<Object, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String threadLabel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ThreadPoolAsynchronousRunner(int paramInt1, boolean paramBoolean1, int paramInt2, int paramInt3, int paramInt4, Timer paramTimer, boolean paramBoolean2, String paramString) {
/*  94 */     this.num_threads = paramInt1;
/*  95 */     this.daemon = paramBoolean1;
/*  96 */     this.max_individual_task_time = paramInt2;
/*  97 */     this.deadlock_detector_interval = paramInt3;
/*  98 */     this.interrupt_delay_after_apparent_deadlock = paramInt4;
/*  99 */     this.myTimer = paramTimer;
/* 100 */     this.should_cancel_timer = paramBoolean2;
/*     */     
/* 102 */     this.threadLabel = paramString;
/*     */     
/* 104 */     recreateThreadsAndTasks();
/*     */     
/* 106 */     paramTimer.schedule(this.deadlockDetector, paramInt3, paramInt3);
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
/*     */   private ThreadPoolAsynchronousRunner(int paramInt1, boolean paramBoolean1, int paramInt2, int paramInt3, int paramInt4, Timer paramTimer, boolean paramBoolean2) {
/* 118 */     this(paramInt1, paramBoolean1, paramInt2, paramInt3, paramInt4, paramTimer, paramBoolean2, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadPoolAsynchronousRunner(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, int paramInt4, Timer paramTimer, String paramString) {
/* 136 */     this(paramInt1, paramBoolean, paramInt2, paramInt3, paramInt4, paramTimer, false, paramString);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadPoolAsynchronousRunner(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, int paramInt4, Timer paramTimer) {
/* 153 */     this(paramInt1, paramBoolean, paramInt2, paramInt3, paramInt4, paramTimer, false);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadPoolAsynchronousRunner(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, int paramInt4, String paramString) {
/* 169 */     this(paramInt1, paramBoolean, paramInt2, paramInt3, paramInt4, new Timer(true), true, paramString);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadPoolAsynchronousRunner(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, int paramInt4) {
/* 185 */     this(paramInt1, paramBoolean, paramInt2, paramInt3, paramInt4, new Timer(true), true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadPoolAsynchronousRunner(int paramInt, boolean paramBoolean, Timer paramTimer, String paramString) {
/* 196 */     this(paramInt, paramBoolean, 0, 10000, 60000, paramTimer, false, paramString);
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
/*     */   public ThreadPoolAsynchronousRunner(int paramInt, boolean paramBoolean, Timer paramTimer) {
/* 208 */     this(paramInt, paramBoolean, 0, 10000, 60000, paramTimer, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadPoolAsynchronousRunner(int paramInt, boolean paramBoolean) {
/* 219 */     this(paramInt, paramBoolean, 0, 10000, 60000, new Timer(true), true);
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
/*     */   public synchronized void postRunnable(Runnable paramRunnable) {
/*     */     try {
/* 232 */       this.pendingTasks.add(paramRunnable);
/* 233 */       notifyAll();
/*     */       
/* 235 */       if (logger.isLoggable(MLevel.FINEST)) {
/* 236 */         logger.log(MLevel.FINEST, this + ": Adding task to queue -- " + paramRunnable);
/*     */       }
/* 238 */     } catch (NullPointerException nullPointerException) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 243 */       if (logger.isLoggable(MLevel.FINE)) {
/* 244 */         logger.log(MLevel.FINE, "NullPointerException while posting Runnable -- Probably we're closed.", nullPointerException);
/*     */       }
/* 246 */       throw new ResourceClosedException("Attempted to use a ThreadPoolAsynchronousRunner in a closed or broken state.");
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized int getThreadCount() {
/* 251 */     return this.managed.size();
/*     */   }
/*     */   
/*     */   public void close(boolean paramBoolean) {
/* 255 */     synchronized (this) {
/*     */       
/* 257 */       if (this.managed == null)
/* 258 */         return;  this.deadlockDetector.cancel();
/*     */       
/* 260 */       if (this.should_cancel_timer)
/* 261 */         this.myTimer.cancel(); 
/* 262 */       this.myTimer = null;
/* 263 */       for (PoolThread poolThread : this.managed) {
/*     */ 
/*     */         
/* 266 */         poolThread.gentleStop();
/* 267 */         if (paramBoolean)
/* 268 */           poolThread.interrupt(); 
/*     */       } 
/* 270 */       this.managed = null;
/*     */       
/* 272 */       if (!paramBoolean)
/*     */       {
/* 274 */         for (Iterator<Runnable> iterator = this.pendingTasks.iterator(); iterator.hasNext(); ) {
/*     */           
/* 276 */           Runnable runnable = iterator.next();
/* 277 */           (new Thread(runnable)).start();
/* 278 */           iterator.remove();
/*     */         } 
/*     */       }
/* 281 */       this.available = null;
/* 282 */       this.pendingTasks = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void close() {
/* 287 */     close(true);
/*     */   }
/*     */   public synchronized int getActiveCount() {
/* 290 */     return this.managed.size() - this.available.size();
/*     */   }
/*     */   public synchronized int getIdleCount() {
/* 293 */     return this.available.size();
/*     */   }
/*     */   public synchronized int getPendingTaskCount() {
/* 296 */     return this.pendingTasks.size();
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
/*     */   public synchronized String getStatus() {
/* 308 */     return getMultiLineStatusString();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getStackTraces() {
/* 313 */     return getStackTraces(0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String getStackTraces(int paramInt) {
/* 319 */     assert Thread.holdsLock(this);
/*     */     
/* 321 */     if (this.managed == null) {
/* 322 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 326 */       Method method = Thread.class.getMethod("getStackTrace", null);
/*     */       
/* 328 */       StringWriter stringWriter = new StringWriter(2048);
/* 329 */       IndentedWriter indentedWriter = new IndentedWriter(stringWriter); byte b;
/* 330 */       for (b = 0; b < paramInt; b++)
/* 331 */         indentedWriter.upIndent(); 
/* 332 */       for (Object object : this.managed) {
/*     */ 
/*     */         
/* 335 */         Object[] arrayOfObject = (Object[])method.invoke(object, null);
/* 336 */         printStackTraces(indentedWriter, object, arrayOfObject);
/*     */       } 
/* 338 */       for (b = 0; b < paramInt; b++)
/* 339 */         indentedWriter.downIndent(); 
/* 340 */       indentedWriter.flush();
/* 341 */       String str = stringWriter.toString();
/* 342 */       indentedWriter.close();
/* 343 */       return str;
/*     */     }
/* 345 */     catch (NoSuchMethodException noSuchMethodException) {
/*     */       
/* 347 */       if (logger.isLoggable(MLevel.FINE))
/* 348 */         logger.fine(this + ": stack traces unavailable because this is a pre-Java 1.5 VM."); 
/* 349 */       return null;
/*     */     }
/* 351 */     catch (Exception exception) {
/*     */       
/* 353 */       if (logger.isLoggable(MLevel.FINE))
/* 354 */         logger.log(MLevel.FINE, this + ": An Exception occurred while trying to extract PoolThread stack traces.", exception); 
/* 355 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getJvmStackTraces(int paramInt) {
/*     */     try {
/* 364 */       Method method = Thread.class.getMethod("getAllStackTraces", null);
/* 365 */       Map map = (Map)method.invoke(null, null);
/*     */       
/* 367 */       StringWriter stringWriter = new StringWriter(2048);
/* 368 */       IndentedWriter indentedWriter = new IndentedWriter(stringWriter); byte b;
/* 369 */       for (b = 0; b < paramInt; b++)
/* 370 */         indentedWriter.upIndent(); 
/* 371 */       for (Map.Entry entry : map.entrySet()) {
/*     */ 
/*     */         
/* 374 */         Object object = entry.getKey();
/* 375 */         Object[] arrayOfObject = (Object[])entry.getValue();
/* 376 */         printStackTraces(indentedWriter, object, arrayOfObject);
/*     */       } 
/* 378 */       for (b = 0; b < paramInt; b++)
/* 379 */         indentedWriter.downIndent(); 
/* 380 */       indentedWriter.flush();
/* 381 */       String str = stringWriter.toString();
/* 382 */       indentedWriter.close();
/* 383 */       return str;
/*     */     }
/* 385 */     catch (NoSuchMethodException noSuchMethodException) {
/*     */       
/* 387 */       if (logger.isLoggable(MLevel.FINE))
/* 388 */         logger.fine(this + ": JVM stack traces unavailable because this is a pre-Java 1.5 VM."); 
/* 389 */       return null;
/*     */     }
/* 391 */     catch (Exception exception) {
/*     */       
/* 393 */       if (logger.isLoggable(MLevel.FINE))
/* 394 */         logger.log(MLevel.FINE, this + ": An Exception occurred while trying to extract PoolThread stack traces.", exception); 
/* 395 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void printStackTraces(IndentedWriter paramIndentedWriter, Object paramObject, Object[] paramArrayOfObject) throws IOException {
/* 402 */     paramIndentedWriter.println(paramObject);
/* 403 */     paramIndentedWriter.upIndent(); byte b; int i;
/* 404 */     for (b = 0, i = paramArrayOfObject.length; b < i; b++)
/* 405 */       paramIndentedWriter.println(paramArrayOfObject[b]); 
/* 406 */     paramIndentedWriter.downIndent();
/*     */   }
/*     */   
/*     */   public synchronized String getMultiLineStatusString() {
/* 410 */     return getMultiLineStatusString(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getMultiLineStatusString(int paramInt) {
/*     */     try {
/* 418 */       StringWriter stringWriter = new StringWriter(2048);
/* 419 */       IndentedWriter indentedWriter = new IndentedWriter(stringWriter);
/*     */       byte b;
/* 421 */       for (b = 0; b < paramInt; b++) {
/* 422 */         indentedWriter.upIndent();
/*     */       }
/* 424 */       if (this.managed == null) {
/*     */         
/* 426 */         indentedWriter.print("[");
/* 427 */         indentedWriter.print(this);
/* 428 */         indentedWriter.println(" closed.]");
/*     */       }
/*     */       else {
/*     */         
/* 432 */         HashSet hashSet = (HashSet)this.managed.clone();
/* 433 */         hashSet.removeAll(this.available);
/*     */         
/* 435 */         indentedWriter.print("Managed Threads: ");
/* 436 */         indentedWriter.println(this.managed.size());
/* 437 */         indentedWriter.print("Active Threads: ");
/* 438 */         indentedWriter.println(hashSet.size());
/* 439 */         indentedWriter.println("Active Tasks: ");
/* 440 */         indentedWriter.upIndent();
/* 441 */         for (PoolThread poolThread : hashSet) {
/*     */ 
/*     */           
/* 444 */           indentedWriter.println(poolThread.getCurrentTask());
/* 445 */           indentedWriter.upIndent();
/* 446 */           indentedWriter.print("on thread: ");
/* 447 */           indentedWriter.println(poolThread.getName());
/* 448 */           indentedWriter.downIndent();
/*     */         } 
/* 450 */         indentedWriter.downIndent();
/* 451 */         indentedWriter.println("Pending Tasks: ");
/* 452 */         indentedWriter.upIndent(); byte b1; int i;
/* 453 */         for (b1 = 0, i = this.pendingTasks.size(); b1 < i; b1++)
/* 454 */           indentedWriter.println(this.pendingTasks.get(b1)); 
/* 455 */         indentedWriter.downIndent();
/*     */       } 
/*     */       
/* 458 */       for (b = 0; b < paramInt; b++)
/* 459 */         indentedWriter.downIndent(); 
/* 460 */       indentedWriter.flush();
/* 461 */       String str = stringWriter.toString();
/* 462 */       indentedWriter.close();
/* 463 */       return str;
/*     */     }
/* 465 */     catch (IOException iOException) {
/*     */       
/* 467 */       if (logger.isLoggable(MLevel.WARNING))
/* 468 */         logger.log(MLevel.WARNING, "Huh? An IOException when working with a StringWriter?!?", iOException); 
/* 469 */       throw new RuntimeException("Huh? An IOException when working with a StringWriter?!? " + iOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendStatusString(StringBuffer paramStringBuffer) {
/* 477 */     if (this.managed == null) {
/* 478 */       paramStringBuffer.append("[closed]");
/*     */     } else {
/*     */       
/* 481 */       HashSet hashSet = (HashSet)this.managed.clone();
/* 482 */       hashSet.removeAll(this.available);
/* 483 */       paramStringBuffer.append("[num_managed_threads: ");
/* 484 */       paramStringBuffer.append(this.managed.size());
/* 485 */       paramStringBuffer.append(", num_active: ");
/* 486 */       paramStringBuffer.append(hashSet.size());
/* 487 */       paramStringBuffer.append("; activeTasks: ");
/* 488 */       boolean bool = true;
/* 489 */       for (Iterator<PoolThread> iterator = hashSet.iterator(); iterator.hasNext(); ) {
/*     */         
/* 491 */         if (bool) {
/* 492 */           bool = false;
/*     */         } else {
/* 494 */           paramStringBuffer.append(", ");
/* 495 */         }  PoolThread poolThread = iterator.next();
/* 496 */         paramStringBuffer.append(poolThread.getCurrentTask());
/* 497 */         paramStringBuffer.append(" (");
/* 498 */         paramStringBuffer.append(poolThread.getName());
/* 499 */         paramStringBuffer.append(')');
/*     */       } 
/* 501 */       paramStringBuffer.append("; pendingTasks: "); byte b; int i;
/* 502 */       for (b = 0, i = this.pendingTasks.size(); b < i; b++) {
/*     */         
/* 504 */         if (b != 0) paramStringBuffer.append(", "); 
/* 505 */         paramStringBuffer.append(this.pendingTasks.get(b));
/*     */       } 
/* 507 */       paramStringBuffer.append(']');
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void recreateThreadsAndTasks() {
/* 515 */     if (this.managed != null) {
/*     */       
/* 517 */       Date date = new Date();
/* 518 */       for (PoolThread poolThread : this.managed) {
/*     */ 
/*     */         
/* 521 */         poolThread.gentleStop();
/* 522 */         this.stoppedThreadsToStopDates.put(poolThread, date);
/* 523 */         ensureReplacedThreadsProcessing();
/*     */       } 
/*     */     } 
/*     */     
/* 527 */     this.managed = new HashSet();
/* 528 */     this.available = new HashSet();
/* 529 */     this.pendingTasks = new LinkedList();
/* 530 */     for (byte b = 0; b < this.num_threads; b++) {
/*     */       
/* 532 */       PoolThread poolThread = new PoolThread(b, this.daemon);
/* 533 */       this.managed.add(poolThread);
/* 534 */       this.available.add(poolThread);
/* 535 */       poolThread.start();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processReplacedThreads() {
/* 543 */     long l = System.currentTimeMillis();
/* 544 */     for (Iterator<PoolThread> iterator = this.stoppedThreadsToStopDates.keySet().iterator(); iterator.hasNext(); ) {
/*     */       
/* 546 */       PoolThread poolThread = iterator.next();
/* 547 */       if (!poolThread.isAlive()) {
/* 548 */         iterator.remove();
/*     */       } else {
/*     */         
/* 551 */         Date date = (Date)this.stoppedThreadsToStopDates.get(poolThread);
/* 552 */         if (l - date.getTime() > this.interrupt_delay_after_apparent_deadlock) {
/*     */           
/* 554 */           if (logger.isLoggable(MLevel.WARNING)) {
/* 555 */             logger.log(MLevel.WARNING, "Task " + poolThread.getCurrentTask() + " (in deadlocked PoolThread) failed to complete in maximum time " + this.interrupt_delay_after_apparent_deadlock + "ms. Trying interrupt().");
/*     */           }
/*     */           
/* 558 */           poolThread.interrupt();
/* 559 */           iterator.remove();
/*     */         } 
/*     */       } 
/*     */       
/* 563 */       if (this.stoppedThreadsToStopDates.isEmpty()) {
/* 564 */         stopReplacedThreadsProcessing();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void ensureReplacedThreadsProcessing() {
/* 572 */     if (this.replacedThreadInterruptor == null) {
/*     */       
/* 574 */       if (logger.isLoggable(MLevel.FINE)) {
/* 575 */         logger.fine("Apparently some threads have been replaced. Replacement thread processing enabled.");
/*     */       }
/* 577 */       this.replacedThreadInterruptor = new ReplacedThreadInterruptor();
/* 578 */       int i = this.interrupt_delay_after_apparent_deadlock / 4;
/* 579 */       this.myTimer.schedule(this.replacedThreadInterruptor, i, i);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void stopReplacedThreadsProcessing() {
/* 587 */     if (this.replacedThreadInterruptor != null) {
/*     */       
/* 589 */       this.replacedThreadInterruptor.cancel();
/* 590 */       this.replacedThreadInterruptor = null;
/*     */       
/* 592 */       if (logger.isLoggable(MLevel.FINE)) {
/* 593 */         logger.fine("Apparently all replaced threads have either completed their tasks or been interrupted(). Replacement thread processing cancelled.");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void shuttingDown(PoolThread paramPoolThread) {
/* 602 */     if (this.managed != null && this.managed.contains(paramPoolThread)) {
/*     */       
/* 604 */       this.managed.remove(paramPoolThread);
/* 605 */       this.available.remove(paramPoolThread);
/* 606 */       PoolThread poolThread = new PoolThread(paramPoolThread.getIndex(), this.daemon);
/* 607 */       this.managed.add(poolThread);
/* 608 */       this.available.add(poolThread);
/* 609 */       poolThread.start();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   class PoolThread
/*     */     extends Thread
/*     */   {
/*     */     Runnable currentTask;
/*     */ 
/*     */     
/*     */     boolean should_stop;
/*     */ 
/*     */     
/*     */     int index;
/*     */     
/* 626 */     TimerTask maxIndividualTaskTimeEnforcer = null;
/*     */ 
/*     */     
/*     */     PoolThread(int param1Int, boolean param1Boolean) {
/* 630 */       setName(((ThreadPoolAsynchronousRunner.this.threadLabel == null) ? getClass().getName() : ThreadPoolAsynchronousRunner.this.threadLabel) + "-#" + param1Int);
/* 631 */       setDaemon(param1Boolean);
/* 632 */       this.index = param1Int;
/*     */     }
/*     */     
/*     */     public int getIndex() {
/* 636 */       return this.index;
/*     */     }
/*     */ 
/*     */     
/*     */     void gentleStop() {
/* 641 */       this.should_stop = true;
/*     */     }
/*     */ 
/*     */     
/*     */     Runnable getCurrentTask() {
/* 646 */       return this.currentTask;
/*     */     }
/*     */ 
/*     */     
/*     */     private void setMaxIndividualTaskTimeEnforcer() {
/* 651 */       this.maxIndividualTaskTimeEnforcer = new ThreadPoolAsynchronousRunner.MaxIndividualTaskTimeEnforcer(this);
/* 652 */       ThreadPoolAsynchronousRunner.this.myTimer.schedule(this.maxIndividualTaskTimeEnforcer, ThreadPoolAsynchronousRunner.this.max_individual_task_time);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void cancelMaxIndividualTaskTimeEnforcer() {
/* 658 */       this.maxIndividualTaskTimeEnforcer.cancel();
/* 659 */       this.maxIndividualTaskTimeEnforcer = null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void purgeTimer() {
/* 665 */       ThreadPoolAsynchronousRunner.this.myTimer.purge();
/* 666 */       if (ThreadPoolAsynchronousRunner.logger.isLoggable(MLevel.FINER)) {
/* 667 */         ThreadPoolAsynchronousRunner.logger.log(MLevel.FINER, getClass().getName() + " -- PURGING TIMER");
/*     */       }
/*     */     }
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
/*     */     public void run() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield this$0 : Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;
/*     */       //   4: getfield rnd : Ljava/util/Random;
/*     */       //   7: invokevirtual nextLong : ()J
/*     */       //   10: lstore_1
/*     */       //   11: aload_0
/*     */       //   12: getfield this$0 : Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;
/*     */       //   15: dup
/*     */       //   16: astore #4
/*     */       //   18: monitorenter
/*     */       //   19: aload_0
/*     */       //   20: getfield should_stop : Z
/*     */       //   23: ifne -> 52
/*     */       //   26: aload_0
/*     */       //   27: getfield this$0 : Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;
/*     */       //   30: getfield pendingTasks : Ljava/util/LinkedList;
/*     */       //   33: invokevirtual size : ()I
/*     */       //   36: ifne -> 52
/*     */       //   39: aload_0
/*     */       //   40: getfield this$0 : Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;
/*     */       //   43: ldc2_w 5000
/*     */       //   46: invokevirtual wait : (J)V
/*     */       //   49: goto -> 19
/*     */       //   52: aload_0
/*     */       //   53: getfield should_stop : Z
/*     */       //   56: ifeq -> 65
/*     */       //   59: aload #4
/*     */       //   61: monitorexit
/*     */       //   62: goto -> 562
/*     */       //   65: aload_0
/*     */       //   66: getfield this$0 : Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;
/*     */       //   69: getfield available : Ljava/util/HashSet;
/*     */       //   72: aload_0
/*     */       //   73: invokevirtual remove : (Ljava/lang/Object;)Z
/*     */       //   76: ifne -> 89
/*     */       //   79: new java/lang/InternalError
/*     */       //   82: dup
/*     */       //   83: ldc 'An unavailable PoolThread tried to check itself out!!!'
/*     */       //   85: invokespecial <init> : (Ljava/lang/String;)V
/*     */       //   88: athrow
/*     */       //   89: aload_0
/*     */       //   90: getfield this$0 : Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;
/*     */       //   93: getfield pendingTasks : Ljava/util/LinkedList;
/*     */       //   96: iconst_0
/*     */       //   97: invokevirtual remove : (I)Ljava/lang/Object;
/*     */       //   100: checkcast java/lang/Runnable
/*     */       //   103: astore_3
/*     */       //   104: aload_0
/*     */       //   105: aload_3
/*     */       //   106: putfield currentTask : Ljava/lang/Runnable;
/*     */       //   109: aload #4
/*     */       //   111: monitorexit
/*     */       //   112: goto -> 123
/*     */       //   115: astore #5
/*     */       //   117: aload #4
/*     */       //   119: monitorexit
/*     */       //   120: aload #5
/*     */       //   122: athrow
/*     */       //   123: aload_0
/*     */       //   124: getfield this$0 : Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;
/*     */       //   127: getfield max_individual_task_time : I
/*     */       //   130: ifle -> 137
/*     */       //   133: aload_0
/*     */       //   134: invokespecial setMaxIndividualTaskTimeEnforcer : ()V
/*     */       //   137: aload_3
/*     */       //   138: invokeinterface run : ()V
/*     */       //   143: aload_0
/*     */       //   144: getfield maxIndividualTaskTimeEnforcer : Ljava/util/TimerTask;
/*     */       //   147: ifnull -> 188
/*     */       //   150: aload_0
/*     */       //   151: invokespecial cancelMaxIndividualTaskTimeEnforcer : ()V
/*     */       //   154: lload_1
/*     */       //   155: lload_1
/*     */       //   156: bipush #21
/*     */       //   158: lshl
/*     */       //   159: lxor
/*     */       //   160: lstore_1
/*     */       //   161: lload_1
/*     */       //   162: lload_1
/*     */       //   163: bipush #35
/*     */       //   165: lushr
/*     */       //   166: lxor
/*     */       //   167: lstore_1
/*     */       //   168: lload_1
/*     */       //   169: lload_1
/*     */       //   170: iconst_4
/*     */       //   171: lshl
/*     */       //   172: lxor
/*     */       //   173: lstore_1
/*     */       //   174: lload_1
/*     */       //   175: ldc2_w 500
/*     */       //   178: lrem
/*     */       //   179: lconst_0
/*     */       //   180: lcmp
/*     */       //   181: ifne -> 188
/*     */       //   184: aload_0
/*     */       //   185: invokespecial purgeTimer : ()V
/*     */       //   188: aload_0
/*     */       //   189: getfield this$0 : Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;
/*     */       //   192: dup
/*     */       //   193: astore #4
/*     */       //   195: monitorenter
/*     */       //   196: aload_0
/*     */       //   197: getfield should_stop : Z
/*     */       //   200: ifeq -> 209
/*     */       //   203: aload #4
/*     */       //   205: monitorexit
/*     */       //   206: goto -> 562
/*     */       //   209: aload_0
/*     */       //   210: getfield this$0 : Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;
/*     */       //   213: getfield available : Ljava/util/HashSet;
/*     */       //   216: ifnull -> 243
/*     */       //   219: aload_0
/*     */       //   220: getfield this$0 : Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;
/*     */       //   223: getfield available : Ljava/util/HashSet;
/*     */       //   226: aload_0
/*     */       //   227: invokevirtual add : (Ljava/lang/Object;)Z
/*     */       //   230: ifne -> 243
/*     */       //   233: new java/lang/InternalError
/*     */       //   236: dup
/*     */       //   237: ldc 'An apparently available PoolThread tried to check itself in!!!'
/*     */       //   239: invokespecial <init> : (Ljava/lang/String;)V
/*     */       //   242: athrow
/*     */       //   243: aload_0
/*     */       //   244: aconst_null
/*     */       //   245: putfield currentTask : Ljava/lang/Runnable;
/*     */       //   248: aload #4
/*     */       //   250: monitorexit
/*     */       //   251: goto -> 262
/*     */       //   254: astore #6
/*     */       //   256: aload #4
/*     */       //   258: monitorexit
/*     */       //   259: aload #6
/*     */       //   261: athrow
/*     */       //   262: goto -> 559
/*     */       //   265: astore #4
/*     */       //   267: getstatic com/mchange/v2/async/ThreadPoolAsynchronousRunner.logger : Lcom/mchange/v2/log/MLogger;
/*     */       //   270: getstatic com/mchange/v2/log/MLevel.WARNING : Lcom/mchange/v2/log/MLevel;
/*     */       //   273: invokeinterface isLoggable : (Lcom/mchange/v2/log/MLevel;)Z
/*     */       //   278: ifeq -> 313
/*     */       //   281: getstatic com/mchange/v2/async/ThreadPoolAsynchronousRunner.logger : Lcom/mchange/v2/log/MLogger;
/*     */       //   284: getstatic com/mchange/v2/log/MLevel.WARNING : Lcom/mchange/v2/log/MLevel;
/*     */       //   287: new java/lang/StringBuilder
/*     */       //   290: dup
/*     */       //   291: invokespecial <init> : ()V
/*     */       //   294: aload_0
/*     */       //   295: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */       //   298: ldc ' -- caught unexpected Exception while executing posted task.'
/*     */       //   300: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */       //   303: invokevirtual toString : ()Ljava/lang/String;
/*     */       //   306: aload #4
/*     */       //   308: invokeinterface log : (Lcom/mchange/v2/log/MLevel;Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */       //   313: aload_0
/*     */       //   314: getfield maxIndividualTaskTimeEnforcer : Ljava/util/TimerTask;
/*     */       //   317: ifnull -> 358
/*     */       //   320: aload_0
/*     */       //   321: invokespecial cancelMaxIndividualTaskTimeEnforcer : ()V
/*     */       //   324: lload_1
/*     */       //   325: lload_1
/*     */       //   326: bipush #21
/*     */       //   328: lshl
/*     */       //   329: lxor
/*     */       //   330: lstore_1
/*     */       //   331: lload_1
/*     */       //   332: lload_1
/*     */       //   333: bipush #35
/*     */       //   335: lushr
/*     */       //   336: lxor
/*     */       //   337: lstore_1
/*     */       //   338: lload_1
/*     */       //   339: lload_1
/*     */       //   340: iconst_4
/*     */       //   341: lshl
/*     */       //   342: lxor
/*     */       //   343: lstore_1
/*     */       //   344: lload_1
/*     */       //   345: ldc2_w 500
/*     */       //   348: lrem
/*     */       //   349: lconst_0
/*     */       //   350: lcmp
/*     */       //   351: ifne -> 358
/*     */       //   354: aload_0
/*     */       //   355: invokespecial purgeTimer : ()V
/*     */       //   358: aload_0
/*     */       //   359: getfield this$0 : Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;
/*     */       //   362: dup
/*     */       //   363: astore #4
/*     */       //   365: monitorenter
/*     */       //   366: aload_0
/*     */       //   367: getfield should_stop : Z
/*     */       //   370: ifeq -> 379
/*     */       //   373: aload #4
/*     */       //   375: monitorexit
/*     */       //   376: goto -> 562
/*     */       //   379: aload_0
/*     */       //   380: getfield this$0 : Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;
/*     */       //   383: getfield available : Ljava/util/HashSet;
/*     */       //   386: ifnull -> 413
/*     */       //   389: aload_0
/*     */       //   390: getfield this$0 : Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;
/*     */       //   393: getfield available : Ljava/util/HashSet;
/*     */       //   396: aload_0
/*     */       //   397: invokevirtual add : (Ljava/lang/Object;)Z
/*     */       //   400: ifne -> 413
/*     */       //   403: new java/lang/InternalError
/*     */       //   406: dup
/*     */       //   407: ldc 'An apparently available PoolThread tried to check itself in!!!'
/*     */       //   409: invokespecial <init> : (Ljava/lang/String;)V
/*     */       //   412: athrow
/*     */       //   413: aload_0
/*     */       //   414: aconst_null
/*     */       //   415: putfield currentTask : Ljava/lang/Runnable;
/*     */       //   418: aload #4
/*     */       //   420: monitorexit
/*     */       //   421: goto -> 432
/*     */       //   424: astore #7
/*     */       //   426: aload #4
/*     */       //   428: monitorexit
/*     */       //   429: aload #7
/*     */       //   431: athrow
/*     */       //   432: goto -> 559
/*     */       //   435: astore #8
/*     */       //   437: aload_0
/*     */       //   438: getfield maxIndividualTaskTimeEnforcer : Ljava/util/TimerTask;
/*     */       //   441: ifnull -> 482
/*     */       //   444: aload_0
/*     */       //   445: invokespecial cancelMaxIndividualTaskTimeEnforcer : ()V
/*     */       //   448: lload_1
/*     */       //   449: lload_1
/*     */       //   450: bipush #21
/*     */       //   452: lshl
/*     */       //   453: lxor
/*     */       //   454: lstore_1
/*     */       //   455: lload_1
/*     */       //   456: lload_1
/*     */       //   457: bipush #35
/*     */       //   459: lushr
/*     */       //   460: lxor
/*     */       //   461: lstore_1
/*     */       //   462: lload_1
/*     */       //   463: lload_1
/*     */       //   464: iconst_4
/*     */       //   465: lshl
/*     */       //   466: lxor
/*     */       //   467: lstore_1
/*     */       //   468: lload_1
/*     */       //   469: ldc2_w 500
/*     */       //   472: lrem
/*     */       //   473: lconst_0
/*     */       //   474: lcmp
/*     */       //   475: ifne -> 482
/*     */       //   478: aload_0
/*     */       //   479: invokespecial purgeTimer : ()V
/*     */       //   482: aload_0
/*     */       //   483: getfield this$0 : Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;
/*     */       //   486: dup
/*     */       //   487: astore #9
/*     */       //   489: monitorenter
/*     */       //   490: aload_0
/*     */       //   491: getfield should_stop : Z
/*     */       //   494: ifeq -> 503
/*     */       //   497: aload #9
/*     */       //   499: monitorexit
/*     */       //   500: goto -> 562
/*     */       //   503: aload_0
/*     */       //   504: getfield this$0 : Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;
/*     */       //   507: getfield available : Ljava/util/HashSet;
/*     */       //   510: ifnull -> 537
/*     */       //   513: aload_0
/*     */       //   514: getfield this$0 : Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;
/*     */       //   517: getfield available : Ljava/util/HashSet;
/*     */       //   520: aload_0
/*     */       //   521: invokevirtual add : (Ljava/lang/Object;)Z
/*     */       //   524: ifne -> 537
/*     */       //   527: new java/lang/InternalError
/*     */       //   530: dup
/*     */       //   531: ldc 'An apparently available PoolThread tried to check itself in!!!'
/*     */       //   533: invokespecial <init> : (Ljava/lang/String;)V
/*     */       //   536: athrow
/*     */       //   537: aload_0
/*     */       //   538: aconst_null
/*     */       //   539: putfield currentTask : Ljava/lang/Runnable;
/*     */       //   542: aload #9
/*     */       //   544: monitorexit
/*     */       //   545: goto -> 556
/*     */       //   548: astore #10
/*     */       //   550: aload #9
/*     */       //   552: monitorexit
/*     */       //   553: aload #10
/*     */       //   555: athrow
/*     */       //   556: aload #8
/*     */       //   558: athrow
/*     */       //   559: goto -> 11
/*     */       //   562: aload_0
/*     */       //   563: getfield this$0 : Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;
/*     */       //   566: dup
/*     */       //   567: astore_3
/*     */       //   568: monitorenter
/*     */       //   569: aload_0
/*     */       //   570: getfield this$0 : Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;
/*     */       //   573: aload_0
/*     */       //   574: invokestatic access$000 : (Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner$PoolThread;)V
/*     */       //   577: aload_3
/*     */       //   578: monitorexit
/*     */       //   579: goto -> 589
/*     */       //   582: astore #11
/*     */       //   584: aload_3
/*     */       //   585: monitorexit
/*     */       //   586: aload #11
/*     */       //   588: athrow
/*     */       //   589: goto -> 759
/*     */       //   592: astore_3
/*     */       //   593: aload_0
/*     */       //   594: getfield this$0 : Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;
/*     */       //   597: dup
/*     */       //   598: astore_3
/*     */       //   599: monitorenter
/*     */       //   600: aload_0
/*     */       //   601: getfield this$0 : Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;
/*     */       //   604: aload_0
/*     */       //   605: invokestatic access$000 : (Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner$PoolThread;)V
/*     */       //   608: aload_3
/*     */       //   609: monitorexit
/*     */       //   610: goto -> 620
/*     */       //   613: astore #12
/*     */       //   615: aload_3
/*     */       //   616: monitorexit
/*     */       //   617: aload #12
/*     */       //   619: athrow
/*     */       //   620: goto -> 759
/*     */       //   623: astore_3
/*     */       //   624: getstatic com/mchange/v2/async/ThreadPoolAsynchronousRunner.logger : Lcom/mchange/v2/log/MLogger;
/*     */       //   627: getstatic com/mchange/v2/log/MLevel.WARNING : Lcom/mchange/v2/log/MLevel;
/*     */       //   630: invokeinterface isLoggable : (Lcom/mchange/v2/log/MLevel;)Z
/*     */       //   635: ifeq -> 669
/*     */       //   638: getstatic com/mchange/v2/async/ThreadPoolAsynchronousRunner.logger : Lcom/mchange/v2/log/MLogger;
/*     */       //   641: getstatic com/mchange/v2/log/MLevel.WARNING : Lcom/mchange/v2/log/MLevel;
/*     */       //   644: new java/lang/StringBuilder
/*     */       //   647: dup
/*     */       //   648: invokespecial <init> : ()V
/*     */       //   651: ldc 'An unexpected RuntimException is implicated in the closing of '
/*     */       //   653: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */       //   656: aload_0
/*     */       //   657: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */       //   660: invokevirtual toString : ()Ljava/lang/String;
/*     */       //   663: aload_3
/*     */       //   664: invokeinterface log : (Lcom/mchange/v2/log/MLevel;Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */       //   669: aload_3
/*     */       //   670: athrow
/*     */       //   671: astore_3
/*     */       //   672: getstatic com/mchange/v2/async/ThreadPoolAsynchronousRunner.logger : Lcom/mchange/v2/log/MLogger;
/*     */       //   675: getstatic com/mchange/v2/log/MLevel.WARNING : Lcom/mchange/v2/log/MLevel;
/*     */       //   678: invokeinterface isLoggable : (Lcom/mchange/v2/log/MLevel;)Z
/*     */       //   683: ifeq -> 722
/*     */       //   686: getstatic com/mchange/v2/async/ThreadPoolAsynchronousRunner.logger : Lcom/mchange/v2/log/MLogger;
/*     */       //   689: getstatic com/mchange/v2/log/MLevel.WARNING : Lcom/mchange/v2/log/MLevel;
/*     */       //   692: new java/lang/StringBuilder
/*     */       //   695: dup
/*     */       //   696: invokespecial <init> : ()V
/*     */       //   699: ldc 'An Error forced the closing of '
/*     */       //   701: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */       //   704: aload_0
/*     */       //   705: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */       //   708: ldc '. Will attempt to reconstruct, but this might mean that something bad is happening.'
/*     */       //   710: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */       //   713: invokevirtual toString : ()Ljava/lang/String;
/*     */       //   716: aload_3
/*     */       //   717: invokeinterface log : (Lcom/mchange/v2/log/MLevel;Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */       //   722: aload_3
/*     */       //   723: athrow
/*     */       //   724: astore #13
/*     */       //   726: aload_0
/*     */       //   727: getfield this$0 : Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;
/*     */       //   730: dup
/*     */       //   731: astore #14
/*     */       //   733: monitorenter
/*     */       //   734: aload_0
/*     */       //   735: getfield this$0 : Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;
/*     */       //   738: aload_0
/*     */       //   739: invokestatic access$000 : (Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner;Lcom/mchange/v2/async/ThreadPoolAsynchronousRunner$PoolThread;)V
/*     */       //   742: aload #14
/*     */       //   744: monitorexit
/*     */       //   745: goto -> 756
/*     */       //   748: astore #15
/*     */       //   750: aload #14
/*     */       //   752: monitorexit
/*     */       //   753: aload #15
/*     */       //   755: athrow
/*     */       //   756: aload #13
/*     */       //   758: athrow
/*     */       //   759: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #672	-> 0
/*     */       //   #680	-> 11
/*     */       //   #682	-> 19
/*     */       //   #683	-> 39
/*     */       //   #684	-> 52
/*     */       //   #685	-> 59
/*     */       //   #687	-> 65
/*     */       //   #688	-> 79
/*     */       //   #689	-> 89
/*     */       //   #690	-> 104
/*     */       //   #691	-> 109
/*     */       //   #694	-> 123
/*     */       //   #695	-> 133
/*     */       //   #696	-> 137
/*     */       //   #706	-> 143
/*     */       //   #708	-> 150
/*     */       //   #713	-> 154
/*     */       //   #714	-> 161
/*     */       //   #715	-> 168
/*     */       //   #716	-> 174
/*     */       //   #717	-> 184
/*     */       //   #720	-> 188
/*     */       //   #722	-> 196
/*     */       //   #723	-> 203
/*     */       //   #725	-> 209
/*     */       //   #726	-> 233
/*     */       //   #727	-> 243
/*     */       //   #728	-> 248
/*     */       //   #729	-> 262
/*     */       //   #698	-> 265
/*     */       //   #700	-> 267
/*     */       //   #701	-> 281
/*     */       //   #706	-> 313
/*     */       //   #708	-> 320
/*     */       //   #713	-> 324
/*     */       //   #714	-> 331
/*     */       //   #715	-> 338
/*     */       //   #716	-> 344
/*     */       //   #717	-> 354
/*     */       //   #720	-> 358
/*     */       //   #722	-> 366
/*     */       //   #723	-> 373
/*     */       //   #725	-> 379
/*     */       //   #726	-> 403
/*     */       //   #727	-> 413
/*     */       //   #728	-> 418
/*     */       //   #729	-> 432
/*     */       //   #706	-> 435
/*     */       //   #708	-> 444
/*     */       //   #713	-> 448
/*     */       //   #714	-> 455
/*     */       //   #715	-> 462
/*     */       //   #716	-> 468
/*     */       //   #717	-> 478
/*     */       //   #720	-> 482
/*     */       //   #722	-> 490
/*     */       //   #723	-> 497
/*     */       //   #725	-> 503
/*     */       //   #726	-> 527
/*     */       //   #727	-> 537
/*     */       //   #728	-> 542
/*     */       //   #730	-> 559
/*     */       //   #757	-> 562
/*     */       //   #758	-> 569
/*     */       //   #759	-> 589
/*     */       //   #732	-> 592
/*     */       //   #757	-> 593
/*     */       //   #758	-> 600
/*     */       //   #759	-> 620
/*     */       //   #740	-> 623
/*     */       //   #742	-> 624
/*     */       //   #743	-> 638
/*     */       //   #744	-> 669
/*     */       //   #746	-> 671
/*     */       //   #748	-> 672
/*     */       //   #749	-> 686
/*     */       //   #753	-> 722
/*     */       //   #757	-> 724
/*     */       //   #758	-> 734
/*     */       //   #760	-> 759
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   11	562	592	java/lang/InterruptedException
/*     */       //   11	562	623	java/lang/RuntimeException
/*     */       //   11	562	671	java/lang/Error
/*     */       //   11	562	724	finally
/*     */       //   19	62	115	finally
/*     */       //   65	112	115	finally
/*     */       //   115	120	115	finally
/*     */       //   123	143	265	java/lang/RuntimeException
/*     */       //   123	143	435	finally
/*     */       //   196	206	254	finally
/*     */       //   209	251	254	finally
/*     */       //   254	259	254	finally
/*     */       //   265	313	435	finally
/*     */       //   366	376	424	finally
/*     */       //   379	421	424	finally
/*     */       //   424	429	424	finally
/*     */       //   435	437	435	finally
/*     */       //   490	500	548	finally
/*     */       //   503	545	548	finally
/*     */       //   548	553	548	finally
/*     */       //   569	579	582	finally
/*     */       //   582	586	582	finally
/*     */       //   592	593	724	finally
/*     */       //   600	610	613	finally
/*     */       //   613	617	613	finally
/*     */       //   623	726	724	finally
/*     */       //   734	745	748	finally
/*     */       //   748	753	748	finally
/*     */     }
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
/*     */   class DeadlockDetector
/*     */     extends TimerTask
/*     */   {
/* 765 */     LinkedList last = null;
/* 766 */     LinkedList current = null;
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/* 771 */       boolean bool = false;
/* 772 */       synchronized (ThreadPoolAsynchronousRunner.this) {
/*     */         
/* 774 */         if (ThreadPoolAsynchronousRunner.this.pendingTasks.size() == 0) {
/*     */           
/* 776 */           this.last = null;
/* 777 */           if (ThreadPoolAsynchronousRunner.logger.isLoggable(MLevel.FINEST)) {
/* 778 */             ThreadPoolAsynchronousRunner.logger.log(MLevel.FINEST, this + " -- Running DeadlockDetector[Exiting. No pending tasks.]");
/*     */           }
/*     */           return;
/*     */         } 
/* 782 */         this.current = (LinkedList)ThreadPoolAsynchronousRunner.this.pendingTasks.clone();
/* 783 */         if (ThreadPoolAsynchronousRunner.logger.isLoggable(MLevel.FINEST)) {
/* 784 */           ThreadPoolAsynchronousRunner.logger.log(MLevel.FINEST, this + " -- Running DeadlockDetector[last->" + this.last + ",current->" + this.current + ']');
/*     */         }
/* 786 */         if (this.current.equals(this.last)) {
/*     */ 
/*     */           
/* 789 */           if (ThreadPoolAsynchronousRunner.logger.isLoggable(MLevel.WARNING)) {
/*     */             
/* 791 */             ThreadPoolAsynchronousRunner.logger.warning(this + " -- APPARENT DEADLOCK!!! Creating emergency threads for unassigned pending tasks!");
/* 792 */             StringWriter stringWriter = new StringWriter(4096);
/* 793 */             PrintWriter printWriter = new PrintWriter(stringWriter);
/*     */ 
/*     */ 
/*     */             
/* 797 */             printWriter.print(this);
/* 798 */             printWriter.println(" -- APPARENT DEADLOCK!!! Complete Status: ");
/* 799 */             printWriter.print(ThreadPoolAsynchronousRunner.this.getMultiLineStatusString(1));
/* 800 */             printWriter.println("Pool thread stack traces:");
/* 801 */             String str = ThreadPoolAsynchronousRunner.this.getStackTraces(1);
/* 802 */             if (str == null) {
/* 803 */               printWriter.println("\t[Stack traces of deadlocked task threads not available.]");
/*     */             } else {
/* 805 */               printWriter.print(str);
/* 806 */             }  printWriter.flush();
/* 807 */             ThreadPoolAsynchronousRunner.logger.warning(stringWriter.toString());
/* 808 */             printWriter.close();
/*     */           } 
/* 810 */           if (ThreadPoolAsynchronousRunner.logger.isLoggable(MLevel.FINEST)) {
/*     */             
/* 812 */             StringWriter stringWriter = new StringWriter(4096);
/* 813 */             PrintWriter printWriter = new PrintWriter(stringWriter);
/* 814 */             printWriter.print(this);
/* 815 */             printWriter.println(" -- APPARENT DEADLOCK extra info, full JVM thread dump: ");
/* 816 */             String str = ThreadPoolAsynchronousRunner.this.getJvmStackTraces(1);
/* 817 */             if (str == null) {
/* 818 */               printWriter.println("\t[Full JVM thread dump not available.]");
/*     */             } else {
/* 820 */               printWriter.print(str);
/* 821 */             }  printWriter.flush();
/* 822 */             ThreadPoolAsynchronousRunner.logger.finest(stringWriter.toString());
/* 823 */             printWriter.close();
/*     */           } 
/* 825 */           ThreadPoolAsynchronousRunner.this.recreateThreadsAndTasks();
/* 826 */           bool = true;
/*     */         } 
/*     */       } 
/* 829 */       if (bool) {
/*     */         
/* 831 */         ThreadPerTaskAsynchronousRunner threadPerTaskAsynchronousRunner = new ThreadPerTaskAsynchronousRunner(10, ThreadPoolAsynchronousRunner.this.max_individual_task_time);
/* 832 */         for (Iterator<Runnable> iterator = this.current.iterator(); iterator.hasNext();)
/* 833 */           threadPerTaskAsynchronousRunner.postRunnable(iterator.next()); 
/* 834 */         threadPerTaskAsynchronousRunner.close(false);
/* 835 */         this.last = null;
/*     */       } else {
/*     */         
/* 838 */         this.last = this.current;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 844 */       this.current = null;
/*     */     }
/*     */   }
/*     */   
/*     */   class MaxIndividualTaskTimeEnforcer
/*     */     extends TimerTask
/*     */   {
/*     */     ThreadPoolAsynchronousRunner.PoolThread pt;
/*     */     Thread interruptMe;
/*     */     String threadStr;
/*     */     String fixedTaskStr;
/*     */     
/*     */     MaxIndividualTaskTimeEnforcer(ThreadPoolAsynchronousRunner.PoolThread param1PoolThread) {
/* 857 */       this.pt = param1PoolThread;
/* 858 */       this.interruptMe = param1PoolThread;
/* 859 */       this.threadStr = param1PoolThread.toString();
/* 860 */       this.fixedTaskStr = null;
/*     */     }
/*     */ 
/*     */     
/*     */     MaxIndividualTaskTimeEnforcer(Thread param1Thread, String param1String1, String param1String2) {
/* 865 */       this.pt = null;
/* 866 */       this.interruptMe = param1Thread;
/* 867 */       this.threadStr = param1String1;
/* 868 */       this.fixedTaskStr = param1String2;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       String str;
/* 875 */       if (this.fixedTaskStr != null) {
/* 876 */         str = this.fixedTaskStr;
/* 877 */       } else if (this.pt != null) {
/*     */         
/* 879 */         synchronized (ThreadPoolAsynchronousRunner.this) {
/* 880 */           str = String.valueOf(this.pt.getCurrentTask());
/*     */         } 
/*     */       } else {
/* 883 */         str = "Unknown task?!";
/*     */       } 
/* 885 */       if (ThreadPoolAsynchronousRunner.logger.isLoggable(MLevel.WARNING)) {
/* 886 */         ThreadPoolAsynchronousRunner.logger.warning("A task has exceeded the maximum allowable task time. Will interrupt() thread [" + this.threadStr + "], with current task: " + str);
/*     */       }
/*     */       
/* 889 */       this.interruptMe.interrupt();
/*     */       
/* 891 */       if (ThreadPoolAsynchronousRunner.logger.isLoggable(MLevel.WARNING)) {
/* 892 */         ThreadPoolAsynchronousRunner.logger.warning("Thread [" + this.threadStr + "] interrupted.");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void runInEmergencyThread(Runnable paramRunnable) {
/* 899 */     Thread thread = new Thread(paramRunnable);
/* 900 */     thread.start();
/* 901 */     if (this.max_individual_task_time > 0) {
/*     */       
/* 903 */       MaxIndividualTaskTimeEnforcer maxIndividualTaskTimeEnforcer = new MaxIndividualTaskTimeEnforcer(thread, thread + " [One-off emergency thread!!!]", paramRunnable.toString());
/* 904 */       this.myTimer.schedule(maxIndividualTaskTimeEnforcer, this.max_individual_task_time);
/*     */     } 
/*     */   }
/*     */   
/*     */   class ReplacedThreadInterruptor
/*     */     extends TimerTask
/*     */   {
/*     */     public void run() {
/* 912 */       synchronized (ThreadPoolAsynchronousRunner.this) {
/* 913 */         ThreadPoolAsynchronousRunner.this.processReplacedThreads();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/async/ThreadPoolAsynchronousRunner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */