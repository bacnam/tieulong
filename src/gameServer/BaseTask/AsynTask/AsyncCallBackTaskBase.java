/*    */ package BaseTask.AsynTask;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import BaseTask.SyncTask.SyncTask;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AsyncCallBackTaskBase<T>
/*    */   implements SyncTask
/*    */ {
/*    */   private T _m_OBJ;
/*    */   
/*    */   public void setCallBackParam(T _obj) {
/* 16 */     this._m_OBJ = _obj;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 21 */     if (this._m_OBJ == null) {
/* 22 */       runError();
/*    */     } else {
/*    */       try {
/* 25 */         runSuc(this._m_OBJ);
/* 26 */       } catch (Exception e) {
/* 27 */         CommLog.error("AsyncCallBackTaskBase.run", e);
/* 28 */         runError();
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract void runSuc(T paramT);
/*    */   
/*    */   public abstract void runError();
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/BaseTask/AsynTask/AsyncCallBackTaskBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */