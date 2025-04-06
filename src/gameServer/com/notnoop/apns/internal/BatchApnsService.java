/*    */ package com.notnoop.apns.internal;
/*    */ 
/*    */ import com.notnoop.apns.ApnsNotification;
/*    */ import com.notnoop.apns.EnhancedApnsNotification;
/*    */ import com.notnoop.exceptions.NetworkIOException;
/*    */ import java.util.Collection;
/*    */ import java.util.Date;
/*    */ import java.util.Map;
/*    */ import java.util.Queue;
/*    */ import java.util.concurrent.ConcurrentLinkedQueue;
/*    */ import java.util.concurrent.ScheduledExecutorService;
/*    */ import java.util.concurrent.ScheduledFuture;
/*    */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BatchApnsService
/*    */   extends AbstractApnsService
/*    */ {
/* 22 */   private int batchWaitTimeInSec = 5;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   private int maxBatchWaitTimeInSec = 10;
/*    */   
/*    */   private long firstMessageArrivedTime;
/*    */   
/*    */   private ApnsConnection prototype;
/*    */   
/* 34 */   private Queue<ApnsNotification> batch = new ConcurrentLinkedQueue<ApnsNotification>();
/*    */   
/*    */   private ScheduledExecutorService scheduleService;
/*    */   
/*    */   private ScheduledFuture<?> taskFuture;
/* 39 */   private Runnable batchRunner = new SendMessagessBatch();
/*    */   
/*    */   public BatchApnsService(ApnsConnection prototype, ApnsFeedbackConnection feedback, int batchWaitTimeInSec, int maxBachWaitTimeInSec, ThreadFactory tf) {
/* 42 */     super(feedback);
/* 43 */     this.prototype = prototype;
/* 44 */     this.batchWaitTimeInSec = batchWaitTimeInSec;
/* 45 */     this.maxBatchWaitTimeInSec = maxBachWaitTimeInSec;
/* 46 */     this.scheduleService = new ScheduledThreadPoolExecutor(1, tf);
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {}
/*    */ 
/*    */   
/*    */   public void stop() {
/* 54 */     Utilities.close(this.prototype);
/* 55 */     if (this.taskFuture != null) {
/* 56 */       this.taskFuture.cancel(true);
/*    */     }
/* 58 */     this.scheduleService.shutdownNow();
/*    */   }
/*    */   
/*    */   public void testConnection() throws NetworkIOException {
/* 62 */     this.prototype.testConnection();
/*    */   }
/*    */ 
/*    */   
/*    */   public void push(ApnsNotification message) throws NetworkIOException {
/* 67 */     if (this.batch.isEmpty()) {
/* 68 */       this.firstMessageArrivedTime = System.nanoTime();
/*    */     }
/*    */     
/* 71 */     long sincFirstMessageSec = (System.nanoTime() - this.firstMessageArrivedTime) / 1000L / 1000L / 1000L;
/*    */     
/* 73 */     if (this.taskFuture != null && sincFirstMessageSec < this.maxBatchWaitTimeInSec) {
/* 74 */       this.taskFuture.cancel(false);
/*    */     }
/*    */     
/* 77 */     this.batch.add(message);
/*    */     
/* 79 */     if (this.taskFuture == null || this.taskFuture.isDone())
/* 80 */       this.taskFuture = this.scheduleService.schedule(this.batchRunner, this.batchWaitTimeInSec, TimeUnit.SECONDS); 
/*    */   }
/*    */   
/*    */   class SendMessagessBatch
/*    */     implements Runnable {
/*    */     public void run() {
/* 86 */       ApnsConnection newConnection = BatchApnsService.this.prototype.copy();
/*    */       try {
/* 88 */         ApnsNotification msg = null;
/* 89 */         while ((msg = BatchApnsService.this.batch.poll()) != null) {
/*    */           try {
/* 91 */             newConnection.sendMessage(msg);
/* 92 */           } catch (NetworkIOException e) {}
/*    */         }
/*    */       
/*    */       } finally {
/*    */         
/* 97 */         Utilities.close(newConnection);
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/apns/internal/BatchApnsService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */