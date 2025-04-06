/*    */ package BaseTask.SyncTask;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import BaseThread.ThreadManager;
/*    */ import BaseThread.ThreadMutexInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SyncTaskDealThread
/*    */   extends Thread
/*    */ {
/*    */   private ThreadMutexInfo _m_tmiThreadMutexInfo;
/*    */   private boolean _m_bThreadExit;
/*    */   private SyncTaskQueue taskManager;
/* 20 */   private int _id = 0;
/*    */   
/*    */   public SyncTaskDealThread(SyncTaskQueue _mgr, int id) {
/* 23 */     this._m_tmiThreadMutexInfo = null;
/* 24 */     this._m_bThreadExit = false;
/* 25 */     this.taskManager = _mgr;
/*    */     
/* 27 */     this._id = id;
/* 28 */     setName("STT-" + _mgr.getTag() + "-" + this._id);
/*    */   }
/*    */   
/*    */   public void dispose() {
/* 32 */     this._m_bThreadExit = true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/* 39 */     if (ThreadManager.getInstance().getCheckDeadLock()) {
/*    */       
/* 41 */       long threadID = Thread.currentThread().getId();
/* 42 */       this._m_tmiThreadMutexInfo = ThreadManager.getInstance().regThread(threadID);
/*    */       
/* 44 */       if (this._m_tmiThreadMutexInfo == null) {
/*    */         return;
/*    */       }
/*    */     } 
/* 48 */     while (!this._m_bThreadExit) {
/*    */ 
/*    */       
/* 51 */       SyncTaskWrapper curTask = this.taskManager.PopTask();
/*    */       
/* 53 */       if (curTask != null) {
/*    */         
/*    */         try {
/* 56 */           curTask.run();
/* 57 */         } catch (Exception e) {
/* 58 */           CommLog.error(String.valueOf(curTask.getClass().getName()) + " Error!!", e);
/* 59 */           if (ThreadManager.getInstance().getCheckDeadLock()) {
/* 60 */             this._m_tmiThreadMutexInfo.releaseAllMutex();
/*    */           }
/*    */         } 
/* 63 */         if (ThreadManager.getInstance().getCheckDeadLock() && 
/* 64 */           !this._m_tmiThreadMutexInfo.judgeAllMutexRelease()) {
/*    */           
/* 66 */           CommLog.error(String.valueOf(curTask.getClass().getName()) + " Still get some mutexs are not released, info:" + curTask.getInfo());
/* 67 */           this._m_tmiThreadMutexInfo.releaseAllMutex();
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/BaseTask/SyncTask/SyncTaskDealThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */