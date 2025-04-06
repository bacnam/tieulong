/*    */ package BaseTask.SyncTask;
/*    */ 
/*    */ import BaseCommon.BaseCommonFun;
/*    */ import BaseCommon.CommLog;
/*    */ import java.util.Timer;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SyncTaskWrapper
/*    */ {
/*    */   private static final int TOTAL_TIMEOUT = 3000;
/*    */   private static final int RUN_TIMEOUT = 2000;
/* 20 */   private static Timer _timer = new Timer();
/*    */   private SyncTask task;
/*    */   private Exception exception;
/*    */   private String info;
/*    */   private SyncTimerTask _timeout;
/* 25 */   private long timerMS = 0L;
/*    */   private Thread thread;
/*    */   private long regTime;
/*    */   private long id;
/* 29 */   private static AtomicLong idPool = new AtomicLong();
/*    */   private SyncTaskQueue parentQueue;
/*    */   
/*    */   public SyncTaskWrapper(SyncTask _task, long timerMS, String info, SyncTaskQueue parent) {
/* 33 */     this.info = info;
/* 34 */     this.parentQueue = parent;
/* 35 */     this.task = _task;
/* 36 */     this.timerMS = timerMS;
/*    */     try {
/* 38 */       throw new Exception();
/* 39 */     } catch (Exception e) {
/* 40 */       this.exception = e;
/*    */ 
/*    */       
/* 43 */       this.id = idPool.incrementAndGet();
/* 44 */       if (this.id >= Long.MAX_VALUE) {
/* 45 */         idPool.set(0L);
/*    */       }
/* 47 */       this.regTime = BaseCommonFun.getNowTimeMS();
/*    */       return;
/*    */     } 
/*    */   } public SyncTaskQueue getParentQueue() {
/* 51 */     return this.parentQueue;
/*    */   }
/*    */   
/*    */   public void run() {
/* 55 */     this.thread = Thread.currentThread();
/*    */     
/* 57 */     long startRun = BaseCommonFun.getNowTimeMS();
/*    */     
/* 59 */     this._timeout = new SyncTimerTask(this);
/* 60 */     _timer.schedule(this._timeout, 3000L);
/*    */     
/* 62 */     if (this.task != null) {
/* 63 */       this.task.run();
/*    */     }
/* 65 */     if (this._timeout != null) {
/* 66 */       this._timeout.cancel();
/*    */     }
/*    */     
/* 69 */     long endRun = BaseCommonFun.getNowTimeMS();
/* 70 */     long waitAndRun = endRun - this.regTime;
/* 71 */     long runCost = endRun - startRun;
/*    */     
/* 73 */     if (runCost >= 2000L || waitAndRun >= this.timerMS + 3000L) {
/* 74 */       CommLog.warn(String.format("[SyncTaskManager][done][id]:%s/[pool]:%s [total]:%sms [timer]:%sms [wait]:%sms [run]:%sms [exinfo]:%s", new Object[] { Long.valueOf(this.id), 
/* 75 */               Integer.valueOf(this.parentQueue.getNormalTaskSize()), Long.valueOf(waitAndRun), Long.valueOf(this.timerMS), Long.valueOf(startRun - this.regTime - this.timerMS), Long.valueOf(runCost), this.info }));
/*    */     }
/*    */   }
/*    */   
/*    */   public long getTimer() {
/* 80 */     return this.timerMS;
/*    */   }
/*    */   
/*    */   public Thread getThread() {
/* 84 */     return this.thread;
/*    */   }
/*    */   
/*    */   public Exception getException() {
/* 88 */     return this.exception;
/*    */   }
/*    */   
/*    */   public long getID() {
/* 92 */     return this.id;
/*    */   }
/*    */   
/*    */   public String getInfo() {
/* 96 */     return this.info;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/BaseTask/SyncTask/SyncTaskWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */