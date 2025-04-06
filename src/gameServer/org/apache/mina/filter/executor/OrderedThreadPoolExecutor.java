/*     */ package org.apache.mina.filter.executor;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.SynchronousQueue;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.mina.core.session.AttributeKey;
/*     */ import org.apache.mina.core.session.DummySession;
/*     */ import org.apache.mina.core.session.IoEvent;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OrderedThreadPoolExecutor
/*     */   extends ThreadPoolExecutor
/*     */ {
/*  56 */   private static Logger LOGGER = LoggerFactory.getLogger(OrderedThreadPoolExecutor.class);
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_INITIAL_THREAD_POOL_SIZE = 0;
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_MAX_THREAD_POOL = 16;
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_KEEP_ALIVE = 30;
/*     */   
/*  67 */   private static final IoSession EXIT_SIGNAL = (IoSession)new DummySession();
/*     */ 
/*     */   
/*  70 */   private final AttributeKey TASKS_QUEUE = new AttributeKey(getClass(), "tasksQueue");
/*     */ 
/*     */   
/*  73 */   private final BlockingQueue<IoSession> waitingSessions = new LinkedBlockingQueue<IoSession>();
/*     */   
/*  75 */   private final Set<Worker> workers = new HashSet<Worker>();
/*     */   
/*     */   private volatile int largestPoolSize;
/*     */   
/*  79 */   private final AtomicInteger idleWorkers = new AtomicInteger();
/*     */ 
/*     */ 
/*     */   
/*     */   private long completedTaskCount;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean shutdown;
/*     */ 
/*     */ 
/*     */   
/*     */   private final IoEventQueueHandler eventQueueHandler;
/*     */ 
/*     */ 
/*     */   
/*     */   public OrderedThreadPoolExecutor() {
/*  96 */     this(0, 16, 30L, TimeUnit.SECONDS, Executors.defaultThreadFactory(), (IoEventQueueHandler)null);
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
/*     */   public OrderedThreadPoolExecutor(int maximumPoolSize) {
/* 110 */     this(0, maximumPoolSize, 30L, TimeUnit.SECONDS, Executors.defaultThreadFactory(), (IoEventQueueHandler)null);
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
/*     */   public OrderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize) {
/* 124 */     this(corePoolSize, maximumPoolSize, 30L, TimeUnit.SECONDS, Executors.defaultThreadFactory(), (IoEventQueueHandler)null);
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
/*     */   public OrderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit) {
/* 139 */     this(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory(), (IoEventQueueHandler)null);
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
/*     */   public OrderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, IoEventQueueHandler eventQueueHandler) {
/* 154 */     this(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory(), eventQueueHandler);
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
/*     */   public OrderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory) {
/* 169 */     this(corePoolSize, maximumPoolSize, keepAliveTime, unit, threadFactory, (IoEventQueueHandler)null);
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
/*     */   public OrderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory, IoEventQueueHandler eventQueueHandler) {
/* 187 */     super(0, 1, keepAliveTime, unit, new SynchronousQueue<Runnable>(), threadFactory, new ThreadPoolExecutor.AbortPolicy());
/*     */ 
/*     */     
/* 190 */     if (corePoolSize < 0) {
/* 191 */       throw new IllegalArgumentException("corePoolSize: " + corePoolSize);
/*     */     }
/*     */     
/* 194 */     if (maximumPoolSize == 0 || maximumPoolSize < corePoolSize) {
/* 195 */       throw new IllegalArgumentException("maximumPoolSize: " + maximumPoolSize);
/*     */     }
/*     */ 
/*     */     
/* 199 */     super.setCorePoolSize(corePoolSize);
/* 200 */     super.setMaximumPoolSize(maximumPoolSize);
/*     */ 
/*     */     
/* 203 */     if (eventQueueHandler == null) {
/* 204 */       this.eventQueueHandler = IoEventQueueHandler.NOOP;
/*     */     } else {
/* 206 */       this.eventQueueHandler = eventQueueHandler;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SessionTasksQueue getSessionTasksQueue(IoSession session) {
/* 214 */     SessionTasksQueue queue = (SessionTasksQueue)session.getAttribute(this.TASKS_QUEUE);
/*     */     
/* 216 */     if (queue == null) {
/* 217 */       queue = new SessionTasksQueue();
/* 218 */       SessionTasksQueue oldQueue = (SessionTasksQueue)session.setAttributeIfAbsent(this.TASKS_QUEUE, queue);
/*     */       
/* 220 */       if (oldQueue != null) {
/* 221 */         queue = oldQueue;
/*     */       }
/*     */     } 
/*     */     
/* 225 */     return queue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoEventQueueHandler getQueueHandler() {
/* 232 */     return this.eventQueueHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRejectedExecutionHandler(RejectedExecutionHandler handler) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addWorker() {
/* 248 */     synchronized (this.workers) {
/* 249 */       if (this.workers.size() >= super.getMaximumPoolSize()) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 254 */       Worker worker = new Worker();
/* 255 */       Thread thread = getThreadFactory().newThread(worker);
/*     */ 
/*     */       
/* 258 */       this.idleWorkers.incrementAndGet();
/*     */ 
/*     */       
/* 261 */       thread.start();
/* 262 */       this.workers.add(worker);
/*     */       
/* 264 */       if (this.workers.size() > this.largestPoolSize) {
/* 265 */         this.largestPoolSize = this.workers.size();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addWorkerIfNecessary() {
/* 274 */     if (this.idleWorkers.get() == 0) {
/* 275 */       synchronized (this.workers) {
/* 276 */         if (this.workers.isEmpty() || this.idleWorkers.get() == 0) {
/* 277 */           addWorker();
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void removeWorker() {
/* 284 */     synchronized (this.workers) {
/* 285 */       if (this.workers.size() <= super.getCorePoolSize()) {
/*     */         return;
/*     */       }
/* 288 */       this.waitingSessions.offer(EXIT_SIGNAL);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaximumPoolSize() {
/* 297 */     return super.getMaximumPoolSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaximumPoolSize(int maximumPoolSize) {
/* 305 */     if (maximumPoolSize <= 0 || maximumPoolSize < super.getCorePoolSize()) {
/* 306 */       throw new IllegalArgumentException("maximumPoolSize: " + maximumPoolSize);
/*     */     }
/*     */     
/* 309 */     synchronized (this.workers) {
/* 310 */       super.setMaximumPoolSize(maximumPoolSize);
/* 311 */       int difference = this.workers.size() - maximumPoolSize;
/* 312 */       while (difference > 0) {
/* 313 */         removeWorker();
/* 314 */         difference--;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
/* 325 */     long deadline = System.currentTimeMillis() + unit.toMillis(timeout);
/*     */     
/* 327 */     synchronized (this.workers) {
/* 328 */       while (!isTerminated()) {
/* 329 */         long waitTime = deadline - System.currentTimeMillis();
/* 330 */         if (waitTime <= 0L) {
/*     */           break;
/*     */         }
/*     */         
/* 334 */         this.workers.wait(waitTime);
/*     */       } 
/*     */     } 
/* 337 */     return isTerminated();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isShutdown() {
/* 345 */     return this.shutdown;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTerminated() {
/* 353 */     if (!this.shutdown) {
/* 354 */       return false;
/*     */     }
/*     */     
/* 357 */     synchronized (this.workers) {
/* 358 */       return this.workers.isEmpty();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 367 */     if (this.shutdown) {
/*     */       return;
/*     */     }
/*     */     
/* 371 */     this.shutdown = true;
/*     */     
/* 373 */     synchronized (this.workers) {
/* 374 */       for (int i = this.workers.size(); i > 0; i--) {
/* 375 */         this.waitingSessions.offer(EXIT_SIGNAL);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Runnable> shutdownNow() {
/* 385 */     shutdown();
/*     */     
/* 387 */     List<Runnable> answer = new ArrayList<Runnable>();
/*     */     
/*     */     IoSession session;
/* 390 */     while ((session = this.waitingSessions.poll()) != null) {
/* 391 */       if (session == EXIT_SIGNAL) {
/* 392 */         this.waitingSessions.offer(EXIT_SIGNAL);
/* 393 */         Thread.yield();
/*     */         
/*     */         continue;
/*     */       } 
/* 397 */       SessionTasksQueue sessionTasksQueue = (SessionTasksQueue)session.getAttribute(this.TASKS_QUEUE);
/*     */       
/* 399 */       synchronized (sessionTasksQueue.tasksQueue) {
/*     */         
/* 401 */         for (Runnable task : sessionTasksQueue.tasksQueue) {
/* 402 */           getQueueHandler().polled(this, (IoEvent)task);
/* 403 */           answer.add(task);
/*     */         } 
/*     */         
/* 406 */         sessionTasksQueue.tasksQueue.clear();
/*     */       } 
/*     */     } 
/*     */     
/* 410 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void print(Queue<Runnable> queue, IoEvent event) {
/* 417 */     StringBuilder sb = new StringBuilder();
/* 418 */     sb.append("Adding event ").append(event.getType()).append(" to session ").append(event.getSession().getId());
/* 419 */     boolean first = true;
/* 420 */     sb.append("\nQueue : [");
/* 421 */     for (Runnable elem : queue) {
/* 422 */       if (first) {
/* 423 */         first = false;
/*     */       } else {
/* 425 */         sb.append(", ");
/*     */       } 
/*     */       
/* 428 */       sb.append(((IoEvent)elem).getType()).append(", ");
/*     */     } 
/* 430 */     sb.append("]\n");
/* 431 */     LOGGER.debug(sb.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Runnable task) {
/*     */     boolean offerSession;
/* 439 */     if (this.shutdown) {
/* 440 */       rejectTask(task);
/*     */     }
/*     */ 
/*     */     
/* 444 */     checkTaskType(task);
/*     */     
/* 446 */     IoEvent event = (IoEvent)task;
/*     */ 
/*     */     
/* 449 */     IoSession session = event.getSession();
/*     */ 
/*     */     
/* 452 */     SessionTasksQueue sessionTasksQueue = getSessionTasksQueue(session);
/* 453 */     Queue<Runnable> tasksQueue = sessionTasksQueue.tasksQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 460 */     boolean offerEvent = this.eventQueueHandler.accept(this, event);
/*     */     
/* 462 */     if (offerEvent) {
/*     */       
/* 464 */       synchronized (tasksQueue) {
/*     */         
/* 466 */         tasksQueue.offer(event);
/*     */         
/* 468 */         if (sessionTasksQueue.processingCompleted) {
/* 469 */           sessionTasksQueue.processingCompleted = false;
/* 470 */           offerSession = true;
/*     */         } else {
/* 472 */           offerSession = false;
/*     */         } 
/*     */         
/* 475 */         if (LOGGER.isDebugEnabled()) {
/* 476 */           print(tasksQueue, event);
/*     */         }
/*     */       } 
/*     */     } else {
/* 480 */       offerSession = false;
/*     */     } 
/*     */     
/* 483 */     if (offerSession)
/*     */     {
/*     */ 
/*     */       
/* 487 */       this.waitingSessions.offer(session);
/*     */     }
/*     */     
/* 490 */     addWorkerIfNecessary();
/*     */     
/* 492 */     if (offerEvent) {
/* 493 */       this.eventQueueHandler.offered(this, event);
/*     */     }
/*     */   }
/*     */   
/*     */   private void rejectTask(Runnable task) {
/* 498 */     getRejectedExecutionHandler().rejectedExecution(task, this);
/*     */   }
/*     */   
/*     */   private void checkTaskType(Runnable task) {
/* 502 */     if (!(task instanceof IoEvent)) {
/* 503 */       throw new IllegalArgumentException("task must be an IoEvent or its subclass.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getActiveCount() {
/* 512 */     synchronized (this.workers) {
/* 513 */       return this.workers.size() - this.idleWorkers.get();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCompletedTaskCount() {
/* 522 */     synchronized (this.workers) {
/* 523 */       long answer = this.completedTaskCount;
/* 524 */       for (Worker w : this.workers) {
/* 525 */         answer += w.completedTaskCount;
/*     */       }
/*     */       
/* 528 */       return answer;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLargestPoolSize() {
/* 537 */     return this.largestPoolSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPoolSize() {
/* 545 */     synchronized (this.workers) {
/* 546 */       return this.workers.size();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTaskCount() {
/* 555 */     return getCompletedTaskCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTerminating() {
/* 563 */     synchronized (this.workers) {
/* 564 */       return (isShutdown() && !isTerminated());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int prestartAllCoreThreads() {
/* 573 */     int answer = 0;
/* 574 */     synchronized (this.workers) {
/* 575 */       for (int i = super.getCorePoolSize() - this.workers.size(); i > 0; i--) {
/* 576 */         addWorker();
/* 577 */         answer++;
/*     */       } 
/*     */     } 
/* 580 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean prestartCoreThread() {
/* 588 */     synchronized (this.workers) {
/* 589 */       if (this.workers.size() < super.getCorePoolSize()) {
/* 590 */         addWorker();
/* 591 */         return true;
/*     */       } 
/* 593 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockingQueue<Runnable> getQueue() {
/* 603 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void purge() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove(Runnable task) {
/*     */     boolean removed;
/* 619 */     checkTaskType(task);
/* 620 */     IoEvent event = (IoEvent)task;
/* 621 */     IoSession session = event.getSession();
/* 622 */     SessionTasksQueue sessionTasksQueue = (SessionTasksQueue)session.getAttribute(this.TASKS_QUEUE);
/* 623 */     Queue<Runnable> tasksQueue = sessionTasksQueue.tasksQueue;
/*     */     
/* 625 */     if (sessionTasksQueue == null) {
/* 626 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 631 */     synchronized (tasksQueue) {
/* 632 */       removed = tasksQueue.remove(task);
/*     */     } 
/*     */     
/* 635 */     if (removed) {
/* 636 */       getQueueHandler().polled(this, event);
/*     */     }
/*     */     
/* 639 */     return removed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCorePoolSize() {
/* 647 */     return super.getCorePoolSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCorePoolSize(int corePoolSize) {
/* 655 */     if (corePoolSize < 0) {
/* 656 */       throw new IllegalArgumentException("corePoolSize: " + corePoolSize);
/*     */     }
/* 658 */     if (corePoolSize > super.getMaximumPoolSize()) {
/* 659 */       throw new IllegalArgumentException("corePoolSize exceeds maximumPoolSize");
/*     */     }
/*     */     
/* 662 */     synchronized (this.workers) {
/* 663 */       if (super.getCorePoolSize() > corePoolSize) {
/* 664 */         for (int i = super.getCorePoolSize() - corePoolSize; i > 0; i--) {
/* 665 */           removeWorker();
/*     */         }
/*     */       }
/* 668 */       super.setCorePoolSize(corePoolSize);
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
/* 679 */       this.thread = Thread.currentThread();
/*     */       
/*     */       try {
/*     */         while (true) {
/* 683 */           IoSession session = fetchSession();
/*     */           
/* 685 */           OrderedThreadPoolExecutor.this.idleWorkers.decrementAndGet();
/*     */           
/* 687 */           if (session == null) {
/* 688 */             synchronized (OrderedThreadPoolExecutor.this.workers) {
/* 689 */               if (OrderedThreadPoolExecutor.this.workers.size() > OrderedThreadPoolExecutor.this.getCorePoolSize()) {
/*     */                 
/* 691 */                 OrderedThreadPoolExecutor.this.workers.remove(this);
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */           }
/* 697 */           if (session == OrderedThreadPoolExecutor.EXIT_SIGNAL) {
/*     */             break;
/*     */           }
/*     */           
/*     */           try {
/* 702 */             if (session != null) {
/* 703 */               runTasks(OrderedThreadPoolExecutor.this.getSessionTasksQueue(session));
/*     */             }
/*     */           } finally {
/* 706 */             OrderedThreadPoolExecutor.this.idleWorkers.incrementAndGet();
/*     */           } 
/*     */         } 
/*     */       } finally {
/* 710 */         synchronized (OrderedThreadPoolExecutor.this.workers) {
/* 711 */           OrderedThreadPoolExecutor.this.workers.remove(this);
/* 712 */           OrderedThreadPoolExecutor.this.completedTaskCount += this.completedTaskCount;
/* 713 */           OrderedThreadPoolExecutor.this.workers.notifyAll();
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private IoSession fetchSession() {
/* 719 */       IoSession session = null;
/* 720 */       long currentTime = System.currentTimeMillis();
/* 721 */       long deadline = currentTime + OrderedThreadPoolExecutor.this.getKeepAliveTime(TimeUnit.MILLISECONDS);
/*     */       while (true) {
/*     */         try {
/* 724 */           long waitTime = deadline - currentTime;
/* 725 */           if (waitTime <= 0L) {
/*     */             break;
/*     */           }
/*     */           
/*     */           try {
/* 730 */             session = OrderedThreadPoolExecutor.this.waitingSessions.poll(waitTime, TimeUnit.MILLISECONDS);
/*     */           } finally {
/*     */             
/* 733 */             if (session == null)
/* 734 */               currentTime = System.currentTimeMillis(); 
/*     */           } 
/*     */           break;
/* 737 */         } catch (InterruptedException e) {}
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 742 */       return session;
/*     */     }
/*     */     
/*     */     private void runTasks(OrderedThreadPoolExecutor.SessionTasksQueue sessionTasksQueue) {
/*     */       while (true) {
/*     */         Runnable task;
/* 748 */         Queue<Runnable> tasksQueue = sessionTasksQueue.tasksQueue;
/*     */         
/* 750 */         synchronized (tasksQueue) {
/* 751 */           task = tasksQueue.poll();
/*     */           
/* 753 */           if (task == null) {
/* 754 */             sessionTasksQueue.processingCompleted = true;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 759 */         OrderedThreadPoolExecutor.this.eventQueueHandler.polled(OrderedThreadPoolExecutor.this, (IoEvent)task);
/*     */         
/* 761 */         runTask(task);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void runTask(Runnable task) {
/* 766 */       OrderedThreadPoolExecutor.this.beforeExecute(this.thread, task);
/* 767 */       boolean ran = false;
/*     */       try {
/* 769 */         task.run();
/* 770 */         ran = true;
/* 771 */         OrderedThreadPoolExecutor.this.afterExecute(task, null);
/* 772 */         this.completedTaskCount++;
/* 773 */       } catch (RuntimeException e) {
/* 774 */         if (!ran) {
/* 775 */           OrderedThreadPoolExecutor.this.afterExecute(task, e);
/*     */         }
/* 777 */         throw e;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class SessionTasksQueue
/*     */   {
/*     */     private SessionTasksQueue() {}
/*     */ 
/*     */     
/* 788 */     private final Queue<Runnable> tasksQueue = new ConcurrentLinkedQueue<Runnable>();
/*     */     private boolean processingCompleted = true;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/executor/OrderedThreadPoolExecutor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */