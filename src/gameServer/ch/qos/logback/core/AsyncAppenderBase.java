/*     */ package ch.qos.logback.core;
/*     */ 
/*     */ import ch.qos.logback.core.spi.AppenderAttachable;
/*     */ import ch.qos.logback.core.spi.AppenderAttachableImpl;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsyncAppenderBase<E>
/*     */   extends UnsynchronizedAppenderBase<E>
/*     */   implements AppenderAttachable<E>
/*     */ {
/*  41 */   AppenderAttachableImpl<E> aai = new AppenderAttachableImpl();
/*     */ 
/*     */   
/*     */   BlockingQueue<E> blockingQueue;
/*     */   
/*     */   public static final int DEFAULT_QUEUE_SIZE = 256;
/*     */   
/*  48 */   int queueSize = 256;
/*     */   
/*  50 */   int appenderCount = 0;
/*     */   
/*     */   static final int UNDEFINED = -1;
/*  53 */   int discardingThreshold = -1;
/*     */   
/*  55 */   Worker worker = new Worker();
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int DEFAULT_MAX_FLUSH_TIME = 1000;
/*     */ 
/*     */ 
/*     */   
/*  63 */   int maxFlushTime = 1000;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isDiscardable(E eventObject) {
/*  76 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void preprocess(E eventObject) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  92 */     if (this.appenderCount == 0) {
/*  93 */       addError("No attached appenders found.");
/*     */       return;
/*     */     } 
/*  96 */     if (this.queueSize < 1) {
/*  97 */       addError("Invalid queue size [" + this.queueSize + "]");
/*     */       return;
/*     */     } 
/* 100 */     this.blockingQueue = new ArrayBlockingQueue<E>(this.queueSize);
/*     */     
/* 102 */     if (this.discardingThreshold == -1)
/* 103 */       this.discardingThreshold = this.queueSize / 5; 
/* 104 */     addInfo("Setting discardingThreshold to " + this.discardingThreshold);
/* 105 */     this.worker.setDaemon(true);
/* 106 */     this.worker.setName("AsyncAppender-Worker-" + getName());
/*     */     
/* 108 */     super.start();
/* 109 */     this.worker.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 114 */     if (!isStarted()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 119 */     super.stop();
/*     */ 
/*     */ 
/*     */     
/* 123 */     this.worker.interrupt();
/*     */     try {
/* 125 */       this.worker.join(this.maxFlushTime);
/*     */ 
/*     */       
/* 128 */       if (this.worker.isAlive()) {
/* 129 */         addWarn("Max queue flush timeout (" + this.maxFlushTime + " ms) exceeded. Approximately " + this.blockingQueue.size() + " queued events were possibly discarded.");
/*     */       } else {
/*     */         
/* 132 */         addInfo("Queue flush finished successfully within timeout.");
/*     */       }
/*     */     
/* 135 */     } catch (InterruptedException e) {
/* 136 */       addError("Failed to join worker thread. " + this.blockingQueue.size() + " queued events may be discarded.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void append(E eventObject) {
/* 143 */     if (isQueueBelowDiscardingThreshold() && isDiscardable(eventObject)) {
/*     */       return;
/*     */     }
/* 146 */     preprocess(eventObject);
/* 147 */     put(eventObject);
/*     */   }
/*     */   
/*     */   private boolean isQueueBelowDiscardingThreshold() {
/* 151 */     return (this.blockingQueue.remainingCapacity() < this.discardingThreshold);
/*     */   }
/*     */   
/*     */   private void put(E eventObject) {
/*     */     try {
/* 156 */       this.blockingQueue.put(eventObject);
/* 157 */     } catch (InterruptedException e) {}
/*     */   }
/*     */ 
/*     */   
/*     */   public int getQueueSize() {
/* 162 */     return this.queueSize;
/*     */   }
/*     */   
/*     */   public void setQueueSize(int queueSize) {
/* 166 */     this.queueSize = queueSize;
/*     */   }
/*     */   
/*     */   public int getDiscardingThreshold() {
/* 170 */     return this.discardingThreshold;
/*     */   }
/*     */   
/*     */   public void setDiscardingThreshold(int discardingThreshold) {
/* 174 */     this.discardingThreshold = discardingThreshold;
/*     */   }
/*     */   
/*     */   public int getMaxFlushTime() {
/* 178 */     return this.maxFlushTime;
/*     */   }
/*     */   
/*     */   public void setMaxFlushTime(int maxFlushTime) {
/* 182 */     this.maxFlushTime = maxFlushTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumberOfElementsInQueue() {
/* 191 */     return this.blockingQueue.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRemainingCapacity() {
/* 201 */     return this.blockingQueue.remainingCapacity();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAppender(Appender<E> newAppender) {
/* 207 */     if (this.appenderCount == 0) {
/* 208 */       this.appenderCount++;
/* 209 */       addInfo("Attaching appender named [" + newAppender.getName() + "] to AsyncAppender.");
/* 210 */       this.aai.addAppender(newAppender);
/*     */     } else {
/* 212 */       addWarn("One and only one appender may be attached to AsyncAppender.");
/* 213 */       addWarn("Ignoring additional appender named [" + newAppender.getName() + "]");
/*     */     } 
/*     */   }
/*     */   
/*     */   public Iterator<Appender<E>> iteratorForAppenders() {
/* 218 */     return this.aai.iteratorForAppenders();
/*     */   }
/*     */   
/*     */   public Appender<E> getAppender(String name) {
/* 222 */     return this.aai.getAppender(name);
/*     */   }
/*     */   
/*     */   public boolean isAttached(Appender<E> eAppender) {
/* 226 */     return this.aai.isAttached(eAppender);
/*     */   }
/*     */   
/*     */   public void detachAndStopAllAppenders() {
/* 230 */     this.aai.detachAndStopAllAppenders();
/*     */   }
/*     */   
/*     */   public boolean detachAppender(Appender<E> eAppender) {
/* 234 */     return this.aai.detachAppender(eAppender);
/*     */   }
/*     */   
/*     */   public boolean detachAppender(String name) {
/* 238 */     return this.aai.detachAppender(name);
/*     */   }
/*     */   
/*     */   class Worker
/*     */     extends Thread {
/*     */     public void run() {
/* 244 */       AsyncAppenderBase<E> parent = AsyncAppenderBase.this;
/* 245 */       AppenderAttachableImpl<E> aai = parent.aai;
/*     */ 
/*     */       
/* 248 */       while (parent.isStarted()) {
/*     */         try {
/* 250 */           E e = parent.blockingQueue.take();
/* 251 */           aai.appendLoopOnAppenders(e);
/* 252 */         } catch (InterruptedException ie) {
/*     */           break;
/*     */         } 
/*     */       } 
/*     */       
/* 257 */       AsyncAppenderBase.this.addInfo("Worker thread will flush remaining events before exiting. ");
/*     */       
/* 259 */       for (E e : parent.blockingQueue) {
/* 260 */         aai.appendLoopOnAppenders(e);
/* 261 */         parent.blockingQueue.remove(e);
/*     */       } 
/*     */ 
/*     */       
/* 265 */       aai.detachAndStopAllAppenders();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/AsyncAppenderBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */