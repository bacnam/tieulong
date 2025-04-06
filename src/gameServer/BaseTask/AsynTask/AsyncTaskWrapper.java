/*    */ package BaseTask.AsynTask;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import BaseTask.SyncTask.SyncTaskManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AsyncTaskWrapper<T>
/*    */ {
/*    */   private AsyncTaskBase<T> _m_tCallObj;
/*    */   private AsyncCallBackTaskBase<T> _m_tCallBackObj;
/*    */   
/*    */   public AsyncTaskWrapper(AsyncTaskBase<T> _callObj, AsyncCallBackTaskBase<T> _callBackObj) {
/* 17 */     this._m_tCallObj = _callObj;
/* 18 */     this._m_tCallBackObj = _callBackObj;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/* 24 */     T object = null;
/*    */     try {
/* 26 */       if (this._m_tCallObj != null) {
/* 27 */         object = this._m_tCallObj.doAsynTask();
/*    */       }
/* 29 */     } catch (Exception e) {
/* 30 */       CommLog.error("AsyncTaskWrapper.run", e);
/*    */     } 
/*    */ 
/*    */     
/* 34 */     if (this._m_tCallBackObj != null) {
/* 35 */       this._m_tCallBackObj.setCallBackParam(object);
/* 36 */       SyncTaskManager.getInstance().getQueue("AsyncCallBack").RegisterTask(this._m_tCallBackObj, "FromCosAsynTaskInfo");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/BaseTask/AsynTask/AsyncTaskWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */