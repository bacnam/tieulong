/*    */ package BaseThread;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MutexInfo
/*    */ {
/*    */   private int _m_iMutexLevel;
/*    */   private CosMutex _m_iCosObject;
/*    */   private int _m_iLockTime;
/*    */   
/*    */   public MutexInfo(CosMutex _cosObj) {
/* 18 */     this._m_iMutexLevel = _cosObj.getMutexLevel();
/* 19 */     this._m_iCosObject = _cosObj;
/* 20 */     this._m_iLockTime = 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getLockLevel() {
/* 29 */     return this._m_iMutexLevel;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getLockTime() {
/* 38 */     return this._m_iLockTime;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addLockTime() {
/* 45 */     this._m_iLockTime++;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void reduceLockTime() {
/* 52 */     this._m_iLockTime--;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void releaseAllLock() {
/* 59 */     while (this._m_iLockTime > 0) {
/* 60 */       if (this._m_iCosObject != null) {
/* 61 */         this._m_iCosObject.unlock(); continue;
/*    */       } 
/* 63 */       this._m_iLockTime = 0;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CosMutex getObj() {
/* 73 */     return this._m_iCosObject;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/BaseThread/MutexInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */