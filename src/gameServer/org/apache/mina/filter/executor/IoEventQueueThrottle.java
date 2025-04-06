/*     */ package org.apache.mina.filter.executor;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.mina.core.session.IoEvent;
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
/*     */ public class IoEventQueueThrottle
/*     */   implements IoEventQueueHandler
/*     */ {
/*  35 */   private static final Logger LOGGER = LoggerFactory.getLogger(IoEventQueueThrottle.class);
/*     */ 
/*     */   
/*     */   private final IoEventSizeEstimator eventSizeEstimator;
/*     */   
/*     */   private volatile int threshold;
/*     */   
/*  42 */   private final Object lock = new Object();
/*     */   
/*  44 */   private final AtomicInteger counter = new AtomicInteger();
/*     */   
/*     */   private int waiters;
/*     */   
/*     */   public IoEventQueueThrottle() {
/*  49 */     this(new DefaultIoEventSizeEstimator(), 65536);
/*     */   }
/*     */   
/*     */   public IoEventQueueThrottle(int threshold) {
/*  53 */     this(new DefaultIoEventSizeEstimator(), threshold);
/*     */   }
/*     */   
/*     */   public IoEventQueueThrottle(IoEventSizeEstimator eventSizeEstimator, int threshold) {
/*  57 */     if (eventSizeEstimator == null) {
/*  58 */       throw new IllegalArgumentException("eventSizeEstimator");
/*     */     }
/*     */     
/*  61 */     this.eventSizeEstimator = eventSizeEstimator;
/*     */     
/*  63 */     setThreshold(threshold);
/*     */   }
/*     */   
/*     */   public IoEventSizeEstimator getEventSizeEstimator() {
/*  67 */     return this.eventSizeEstimator;
/*     */   }
/*     */   
/*     */   public int getThreshold() {
/*  71 */     return this.threshold;
/*     */   }
/*     */   
/*     */   public int getCounter() {
/*  75 */     return this.counter.get();
/*     */   }
/*     */   
/*     */   public void setThreshold(int threshold) {
/*  79 */     if (threshold <= 0) {
/*  80 */       throw new IllegalArgumentException("threshold: " + threshold);
/*     */     }
/*     */     
/*  83 */     this.threshold = threshold;
/*     */   }
/*     */   
/*     */   public boolean accept(Object source, IoEvent event) {
/*  87 */     return true;
/*     */   }
/*     */   
/*     */   public void offered(Object source, IoEvent event) {
/*  91 */     int eventSize = estimateSize(event);
/*  92 */     int currentCounter = this.counter.addAndGet(eventSize);
/*  93 */     logState();
/*     */     
/*  95 */     if (currentCounter >= this.threshold) {
/*  96 */       block();
/*     */     }
/*     */   }
/*     */   
/*     */   public void polled(Object source, IoEvent event) {
/* 101 */     int eventSize = estimateSize(event);
/* 102 */     int currentCounter = this.counter.addAndGet(-eventSize);
/*     */     
/* 104 */     logState();
/*     */     
/* 106 */     if (currentCounter < this.threshold) {
/* 107 */       unblock();
/*     */     }
/*     */   }
/*     */   
/*     */   private int estimateSize(IoEvent event) {
/* 112 */     int size = getEventSizeEstimator().estimateSize(event);
/*     */     
/* 114 */     if (size < 0) {
/* 115 */       throw new IllegalStateException(IoEventSizeEstimator.class.getSimpleName() + " returned " + "a negative value (" + size + "): " + event);
/*     */     }
/*     */ 
/*     */     
/* 119 */     return size;
/*     */   }
/*     */   
/*     */   private void logState() {
/* 123 */     if (LOGGER.isDebugEnabled()) {
/* 124 */       LOGGER.debug(Thread.currentThread().getName() + " state: " + this.counter.get() + " / " + getThreshold());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void block() {
/* 129 */     if (LOGGER.isDebugEnabled()) {
/* 130 */       LOGGER.debug(Thread.currentThread().getName() + " blocked: " + this.counter.get() + " >= " + this.threshold);
/*     */     }
/*     */     
/* 133 */     synchronized (this.lock) {
/* 134 */       while (this.counter.get() >= this.threshold) {
/* 135 */         this.waiters++;
/*     */         try {
/* 137 */           this.lock.wait();
/* 138 */         } catch (InterruptedException e) {
/*     */         
/*     */         } finally {
/* 141 */           this.waiters--;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 146 */     if (LOGGER.isDebugEnabled()) {
/* 147 */       LOGGER.debug(Thread.currentThread().getName() + " unblocked: " + this.counter.get() + " < " + this.threshold);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void unblock() {
/* 152 */     synchronized (this.lock) {
/* 153 */       if (this.waiters > 0)
/* 154 */         this.lock.notifyAll(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/executor/IoEventQueueThrottle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */