/*    */ package BaseServer;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import BaseTask.AsynTask.AsyncTaskManager;
/*    */ import BaseTask.SyncTask.SyncTaskManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Monitor
/*    */   extends Thread
/*    */ {
/*    */   private boolean _m_bThreadExit;
/* 16 */   private static Monitor instance = new Monitor();
/*    */   
/*    */   private _ACleanMemory cleanMemory;
/*    */   private static final int MB = 1049600;
/*    */   
/*    */   public static Monitor getInstance() {
/* 22 */     return instance;
/*    */   }
/*    */   
/*    */   private boolean needLog = false;
/*    */   
/*    */   public void regLog() {
/* 28 */     this.needLog = true;
/*    */   }
/*    */   
/*    */   public void regCleanMemory(_ACleanMemory cleanMemory) {
/* 32 */     this.cleanMemory = cleanMemory;
/*    */   }
/*    */   
/*    */   public Monitor() {
/* 36 */     this._m_bThreadExit = false;
/* 37 */     setName("CosCmdTaskThread");
/*    */   }
/*    */   
/*    */   public void ExitThread() {
/* 41 */     this._m_bThreadExit = true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/* 48 */     while (!this._m_bThreadExit) {
/*    */       
/*    */       try {
/* 51 */         sleep(60000L);
/* 52 */       } catch (InterruptedException e) {
/* 53 */         CommLog.error("Monitor", e);
/*    */       } 
/*    */ 
/*    */       
/* 57 */       if (this.needLog) {
/* 58 */         this.needLog = false;
/* 59 */         StringBuilder sBuilder = new StringBuilder();
/* 60 */         sBuilder.append("ThreadMonitor\n");
/* 61 */         sBuilder.append(SyncTaskManager.getInstance().toString());
/* 62 */         sBuilder.append(AsyncTaskManager.getInstance().toString());
/* 63 */         CommLog.warn(sBuilder.toString());
/*    */       } 
/*    */       
/* 66 */       long total = Runtime.getRuntime().totalMemory() / 1049600L;
/* 67 */       long free = Runtime.getRuntime().freeMemory() / 1049600L;
/* 68 */       long max = Runtime.getRuntime().maxMemory() / 1049600L;
/* 69 */       long usable = max - total - free;
/*    */       
/* 71 */       if (usable < max / 5L) {
/* 72 */         CommLog.info(String.format("[Memory] max：%10sMB total：%10sMB free：%10sMB available：%10sMB", new Object[] { Long.valueOf(max), Long.valueOf(total), Long.valueOf(free), Long.valueOf(usable) }));
/* 73 */         if (this.cleanMemory != null) {
/* 74 */           SyncTaskManager.task(() -> {
/*    */                 try {
/*    */                   CommLog.info("[Memory] 剩余内存不足20%尝试进行内存释放");
/*    */                   this.cleanMemory.run();
/* 78 */                 } catch (Throwable t) {
/*    */                   CommLog.error("尝试清理内存时发生异常", t);
/*    */                 } 
/*    */               });
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void outputMemoryInfo() {
/* 88 */     long total = Runtime.getRuntime().totalMemory() / 1049600L;
/* 89 */     long free = Runtime.getRuntime().freeMemory() / 1049600L;
/* 90 */     long max = Runtime.getRuntime().maxMemory() / 1049600L;
/* 91 */     long usable = max - total - free;
/* 92 */     CommLog.info(String.format("[Memory] max：%10sMB total：%10sMB free：%10sMB available：%10sMB", new Object[] { Long.valueOf(max), Long.valueOf(total), Long.valueOf(free), Long.valueOf(usable) }));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/BaseServer/Monitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */