/*     */ package org.apache.mina.filter.executor;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.mina.core.session.IoEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnorderedThreadPoolExecutor
/*     */   extends ThreadPoolExecutor
/*     */ {
/*  56 */   private static final Runnable EXIT_SIGNAL = new Runnable() {
/*     */       public void run() {
/*  58 */         throw new Error("This method shouldn't be called. Please file a bug report.");
/*     */       }
/*     */     };
/*     */   
/*  62 */   private final Set<Worker> workers = new HashSet<Worker>();
/*     */   
/*     */   private volatile int corePoolSize;
/*     */   
/*     */   private volatile int maximumPoolSize;
/*     */   
/*     */   private volatile int largestPoolSize;
/*     */   
/*  70 */   private final AtomicInteger idleWorkers = new AtomicInteger();
/*     */   
/*     */   private long completedTaskCount;
/*     */   
/*     */   private volatile boolean shutdown;
/*     */   
/*     */   private final IoEventQueueHandler queueHandler;
/*     */   
/*     */   public UnorderedThreadPoolExecutor() {
/*  79 */     this(16);
/*     */   }
/*     */   
/*     */   public UnorderedThreadPoolExecutor(int maximumPoolSize) {
/*  83 */     this(0, maximumPoolSize);
/*     */   }
/*     */   
/*     */   public UnorderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize) {
/*  87 */     this(corePoolSize, maximumPoolSize, 30L, TimeUnit.SECONDS);
/*     */   }
/*     */   
/*     */   public UnorderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit) {
/*  91 */     this(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory());
/*     */   }
/*     */ 
/*     */   
/*     */   public UnorderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, IoEventQueueHandler queueHandler) {
/*  96 */     this(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory(), queueHandler);
/*     */   }
/*     */ 
/*     */   
/*     */   public UnorderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory) {
/* 101 */     this(corePoolSize, maximumPoolSize, keepAliveTime, unit, threadFactory, (IoEventQueueHandler)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public UnorderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory, IoEventQueueHandler queueHandler) {
/* 106 */     super(0, 1, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>(), threadFactory, new ThreadPoolExecutor.AbortPolicy());
/* 107 */     if (corePoolSize < 0) {
/* 108 */       throw new IllegalArgumentException("corePoolSize: " + corePoolSize);
/*     */     }
/*     */     
/* 111 */     if (maximumPoolSize == 0 || maximumPoolSize < corePoolSize) {
/* 112 */       throw new IllegalArgumentException("maximumPoolSize: " + maximumPoolSize);
/*     */     }
/*     */     
/* 115 */     if (queueHandler == null) {
/* 116 */       queueHandler = IoEventQueueHandler.NOOP;
/*     */     }
/*     */     
/* 119 */     this.corePoolSize = corePoolSize;
/* 120 */     this.maximumPoolSize = maximumPoolSize;
/* 121 */     this.queueHandler = queueHandler;
/*     */   }
/*     */   
/*     */   public IoEventQueueHandler getQueueHandler() {
/* 125 */     return this.queueHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRejectedExecutionHandler(RejectedExecutionHandler handler) {}
/*     */ 
/*     */   
/*     */   private void addWorker() {
/* 134 */     synchronized (this.workers) {
/* 135 */       if (this.workers.size() >= this.maximumPoolSize) {
/*     */         return;
/*     */       }
/*     */       
/* 139 */       Worker worker = new Worker();
/* 140 */       Thread thread = getThreadFactory().newThread(worker);
/* 141 */       this.idleWorkers.incrementAndGet();
/* 142 */       thread.start();
/* 143 */       this.workers.add(worker);
/*     */       
/* 145 */       if (this.workers.size() > this.largestPoolSize) {
/* 146 */         this.largestPoolSize = this.workers.size();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addWorkerIfNecessary() {
/* 152 */     if (this.idleWorkers.get() == 0) {
/* 153 */       synchronized (this.workers) {
/* 154 */         if (this.workers.isEmpty() || this.idleWorkers.get() == 0) {
/* 155 */           addWorker();
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void removeWorker() {
/* 162 */     synchronized (this.workers) {
/* 163 */       if (this.workers.size() <= this.corePoolSize) {
/*     */         return;
/*     */       }
/* 166 */       getQueue().offer(EXIT_SIGNAL);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaximumPoolSize() {
/* 172 */     return this.maximumPoolSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaximumPoolSize(int maximumPoolSize) {
/* 177 */     if (maximumPoolSize <= 0 || maximumPoolSize < this.corePoolSize) {
/* 178 */       throw new IllegalArgumentException("maximumPoolSize: " + maximumPoolSize);
/*     */     }
/*     */     
/* 181 */     synchronized (this.workers) {
/* 182 */       this.maximumPoolSize = maximumPoolSize;
/* 183 */       int difference = this.workers.size() - maximumPoolSize;
/* 184 */       while (difference > 0) {
/* 185 */         removeWorker();
/* 186 */         difference--;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
/* 194 */     long deadline = System.currentTimeMillis() + unit.toMillis(timeout);
/*     */     
/* 196 */     synchronized (this.workers) {
/* 197 */       while (!isTerminated()) {
/* 198 */         long waitTime = deadline - System.currentTimeMillis();
/* 199 */         if (waitTime <= 0L) {
/*     */           break;
/*     */         }
/*     */         
/* 203 */         this.workers.wait(waitTime);
/*     */       } 
/*     */     } 
/* 206 */     return isTerminated();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShutdown() {
/* 211 */     return this.shutdown;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTerminated() {
/* 216 */     if (!this.shutdown) {
/* 217 */       return false;
/*     */     }
/*     */     
/* 220 */     synchronized (this.workers) {
/* 221 */       return this.workers.isEmpty();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 227 */     if (this.shutdown) {
/*     */       return;
/*     */     }
/*     */     
/* 231 */     this.shutdown = true;
/*     */     
/* 233 */     synchronized (this.workers) {
/* 234 */       for (int i = this.workers.size(); i > 0; i--) {
/* 235 */         getQueue().offer(EXIT_SIGNAL);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Runnable> shutdownNow() {
/* 242 */     shutdown();
/*     */     
/* 244 */     List<Runnable> answer = new ArrayList<Runnable>();
/*     */     Runnable task;
/* 246 */     while ((task = getQueue().poll()) != null) {
/* 247 */       if (task == EXIT_SIGNAL) {
/* 248 */         getQueue().offer(EXIT_SIGNAL);
/* 249 */         Thread.yield();
/*     */         
/*     */         continue;
/*     */       } 
/* 253 */       getQueueHandler().polled(this, (IoEvent)task);
/* 254 */       answer.add(task);
/*     */     } 
/*     */     
/* 257 */     return answer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(Runnable task) {
/* 262 */     if (this.shutdown) {
/* 263 */       rejectTask(task);
/*     */     }
/*     */     
/* 266 */     checkTaskType(task);
/*     */     
/* 268 */     IoEvent e = (IoEvent)task;
/* 269 */     boolean offeredEvent = this.queueHandler.accept(this, e);
/*     */     
/* 271 */     if (offeredEvent) {
/* 272 */       getQueue().offer(e);
/*     */     }
/*     */     
/* 275 */     addWorkerIfNecessary();
/*     */     
/* 277 */     if (offeredEvent) {
/* 278 */       this.queueHandler.offered(this, e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void rejectTask(Runnable task) {
/* 283 */     getRejectedExecutionHandler().rejectedExecution(task, this);
/*     */   }
/*     */   
/*     */   private void checkTaskType(Runnable task) {
/* 287 */     if (!(task instanceof IoEvent)) {
/* 288 */       throw new IllegalArgumentException("task must be an IoEvent or its subclass.");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getActiveCount() {
/* 294 */     synchronized (this.workers) {
/* 295 */       return this.workers.size() - this.idleWorkers.get();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long getCompletedTaskCount() {
/* 301 */     synchronized (this.workers) {
/* 302 */       long answer = this.completedTaskCount;
/* 303 */       for (Worker w : this.workers) {
/* 304 */         answer += w.completedTaskCount;
/*     */       }
/*     */       
/* 307 */       return answer;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLargestPoolSize() {
/* 313 */     return this.largestPoolSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPoolSize() {
/* 318 */     synchronized (this.workers) {
/* 319 */       return this.workers.size();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long getTaskCount() {
/* 325 */     return getCompletedTaskCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTerminating() {
/* 330 */     synchronized (this.workers) {
/* 331 */       return (isShutdown() && !isTerminated());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int prestartAllCoreThreads() {
/* 337 */     int answer = 0;
/* 338 */     synchronized (this.workers) {
/* 339 */       for (int i = this.corePoolSize - this.workers.size(); i > 0; i--) {
/* 340 */         addWorker();
/* 341 */         answer++;
/*     */       } 
/*     */     } 
/* 344 */     return answer;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean prestartCoreThread() {
/* 349 */     synchronized (this.workers) {
/* 350 */       if (this.workers.size() < this.corePoolSize) {
/* 351 */         addWorker();
/* 352 */         return true;
/*     */       } 
/*     */       
/* 355 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void purge() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove(Runnable task) {
/* 366 */     boolean removed = super.remove(task);
/* 367 */     if (removed) {
/* 368 */       getQueueHandler().polled(this, (IoEvent)task);
/*     */     }
/* 370 */     return removed;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCorePoolSize() {
/* 375 */     return this.corePoolSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCorePoolSize(int corePoolSize) {
/* 380 */     if (corePoolSize < 0) {
/* 381 */       throw new IllegalArgumentException("corePoolSize: " + corePoolSize);
/*     */     }
/* 383 */     if (corePoolSize > this.maximumPoolSize) {
/* 384 */       throw new IllegalArgumentException("corePoolSize exceeds maximumPoolSize");
/*     */     }
/*     */     
/* 387 */     synchronized (this.workers) {
/* 388 */       if (this.corePoolSize > corePoolSize) {
/* 389 */         for (int i = this.corePoolSize - corePoolSize; i > 0; i--) {
/* 390 */           removeWorker();
/*     */         }
/*     */       }
/* 393 */       this.corePoolSize = corePoolSize;
/*     */     } 
/*     */   }
/*     */   
/*     */   private class Worker implements Runnable {
/*     */     private volatile long completedTaskCount;
/*     */     private Thread thread;
/*     */     
/*     */     private Worker() {}
/*     */     
/*     */     public void run() {
/* 404 */       this.thread = Thread.currentThread();
/*     */       
/*     */       try {
/*     */         while (true) {
/* 408 */           Runnable task = fetchTask();
/*     */           
/* 410 */           UnorderedThreadPoolExecutor.this.idleWorkers.decrementAndGet();
/*     */           
/* 412 */           if (task == null) {
/* 413 */             synchronized (UnorderedThreadPoolExecutor.this.workers) {
/* 414 */               if (UnorderedThreadPoolExecutor.this.workers.size() > UnorderedThreadPoolExecutor.this.corePoolSize) {
/*     */                 
/* 416 */                 UnorderedThreadPoolExecutor.this.workers.remove(this);
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */           }
/* 422 */           if (task == UnorderedThreadPoolExecutor.EXIT_SIGNAL) {
/*     */             break;
/*     */           }
/*     */           
/*     */           try {
/* 427 */             if (task != null) {
/* 428 */               UnorderedThreadPoolExecutor.this.queueHandler.polled(UnorderedThreadPoolExecutor.this, (IoEvent)task);
/* 429 */               runTask(task);
/*     */             } 
/*     */           } finally {
/* 432 */             UnorderedThreadPoolExecutor.this.idleWorkers.incrementAndGet();
/*     */           } 
/*     */         } 
/*     */       } finally {
/* 436 */         synchronized (UnorderedThreadPoolExecutor.this.workers) {
/* 437 */           UnorderedThreadPoolExecutor.this.workers.remove(this);
/* 438 */           UnorderedThreadPoolExecutor.this.completedTaskCount += this.completedTaskCount;
/* 439 */           UnorderedThreadPoolExecutor.this.workers.notifyAll();
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private Runnable fetchTask() {
/* 445 */       Runnable task = null;
/* 446 */       long currentTime = System.currentTimeMillis();
/* 447 */       long deadline = currentTime + UnorderedThreadPoolExecutor.this.getKeepAliveTime(TimeUnit.MILLISECONDS);
/*     */       while (true) {
/*     */         try {
/* 450 */           long waitTime = deadline - currentTime;
/* 451 */           if (waitTime <= 0L) {
/*     */             break;
/*     */           }
/*     */           
/*     */           try {
/* 456 */             task = UnorderedThreadPoolExecutor.this.getQueue().poll(waitTime, TimeUnit.MILLISECONDS);
/*     */           } finally {
/*     */             
/* 459 */             if (task == null)
/* 460 */               currentTime = System.currentTimeMillis(); 
/*     */           } 
/*     */           break;
/* 463 */         } catch (InterruptedException e) {}
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 468 */       return task;
/*     */     }
/*     */     
/*     */     private void runTask(Runnable task) {
/* 472 */       UnorderedThreadPoolExecutor.this.beforeExecute(this.thread, task);
/* 473 */       boolean ran = false;
/*     */       try {
/* 475 */         task.run();
/* 476 */         ran = true;
/* 477 */         UnorderedThreadPoolExecutor.this.afterExecute(task, null);
/* 478 */         this.completedTaskCount++;
/* 479 */       } catch (RuntimeException e) {
/* 480 */         if (!ran) {
/* 481 */           UnorderedThreadPoolExecutor.this.afterExecute(task, e);
/*     */         }
/* 483 */         throw e;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/executor/UnorderedThreadPoolExecutor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */