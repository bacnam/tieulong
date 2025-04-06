/*    */ package BaseTask.AsynTask;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AsyncTaskDealThread
/*    */   extends Thread
/*    */ {
/*    */   private boolean _m_bThreadExit;
/* 15 */   private int _id = 0;
/*    */   private AsyncTaskQueue.AsyncThreadQueue taskManager;
/*    */   
/*    */   public AsyncTaskDealThread(AsyncTaskQueue.AsyncThreadQueue _mgr, int id) {
/* 19 */     this._m_bThreadExit = false;
/* 20 */     this.taskManager = _mgr;
/* 21 */     this._id = id;
/* 22 */     setName("ATT-" + _mgr.getTag() + "-" + this._id);
/*    */   }
/*    */   
/*    */   public void dispose() {
/* 26 */     this._m_bThreadExit = true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/* 33 */     while (!this._m_bThreadExit) {
/*    */ 
/*    */       
/* 36 */       AsyncTaskWrapper info = this.taskManager.popFirstAsynTask();
/*    */       
/* 38 */       if (info != null)
/*    */         try {
/* 40 */           info.run();
/* 41 */         } catch (Throwable e) {
/* 42 */           CommLog.error("AsyncTaskDealThread.run", e);
/*    */         }  
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/BaseTask/AsynTask/AsyncTaskDealThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */