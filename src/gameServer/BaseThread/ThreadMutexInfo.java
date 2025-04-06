/*     */ package BaseThread;
/*     */ 
/*     */ import java.util.ArrayList;
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
/*     */ public class ThreadMutexInfo
/*     */ {
/*     */   private long _m_lThreadID;
/*     */   private ArrayList<MutexInfo> _m_lThreadMutexList;
/*     */   
/*     */   public ThreadMutexInfo(long _threadID) {
/*  22 */     this._m_lThreadID = _threadID;
/*  23 */     this._m_lThreadMutexList = new ArrayList<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getThreadID() {
/*  32 */     return this._m_lThreadID;
/*     */   }
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
/*     */   public void tryLock(CosMutex _cosObj) throws CosMutexException {
/*  45 */     if (_cosObj == null) {
/*     */       return;
/*     */     }
/*  48 */     int objMutexLevel = _cosObj.getMutexLevel();
/*     */     
/*  50 */     MutexInfo mutexInfo = null;
/*  51 */     if (!this._m_lThreadMutexList.isEmpty()) {
/*     */       
/*  53 */       MutexInfo topLvMutexInfo = this._m_lThreadMutexList.get(0);
/*     */       
/*  55 */       if (topLvMutexInfo.getLockLevel() >= _cosObj.getMutexLevel()) {
/*     */         
/*  57 */         mutexInfo = _getMutexLevelInfo(objMutexLevel);
/*  58 */         if (mutexInfo == null || mutexInfo.getObj() != _cosObj)
/*     */         {
/*  60 */           throw new CosMutexException("无法获取高等级锁");
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  65 */     if (mutexInfo == null) {
/*  66 */       mutexInfo = new MutexInfo(_cosObj);
/*  67 */       this._m_lThreadMutexList.add(0, mutexInfo);
/*     */     } 
/*     */ 
/*     */     
/*  71 */     mutexInfo.addLockTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void tryUnlock(CosMutex _cosObj) throws CosMutexException {
/*  80 */     if (_cosObj == null) {
/*     */       return;
/*     */     }
/*  83 */     int objMutexLevel = _cosObj.getMutexLevel();
/*  84 */     MutexInfo mutexInfo = _getMutexLevelInfo(objMutexLevel);
/*     */ 
/*     */     
/*  87 */     if (mutexInfo == null || mutexInfo.getObj() != _cosObj) {
/*  88 */       throw new CosMutexException("尝试释放未获取的锁");
/*     */     }
/*     */ 
/*     */     
/*  92 */     mutexInfo.reduceLockTime();
/*     */     
/*  94 */     if (mutexInfo.getLockTime() <= 0)
/*     */     {
/*  96 */       this._m_lThreadMutexList.remove(mutexInfo);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean judgeAllMutexRelease() {
/* 106 */     return this._m_lThreadMutexList.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseAllMutex() {
/* 114 */     while (!this._m_lThreadMutexList.isEmpty()) {
/* 115 */       MutexInfo info = this._m_lThreadMutexList.get(0);
/*     */       
/* 117 */       if (info == null)
/*     */         continue; 
/* 119 */       if (info.getLockTime() <= 0) {
/* 120 */         this._m_lThreadMutexList.remove(info);
/*     */       }
/* 122 */       info.releaseAllLock();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected MutexInfo _getMutexLevelInfo(int _level) {
/* 127 */     for (int i = 0; i < this._m_lThreadMutexList.size(); i++) {
/* 128 */       MutexInfo info = this._m_lThreadMutexList.get(i);
/*     */       
/* 130 */       if (info.getLockLevel() == _level) {
/* 131 */         return info;
/*     */       }
/*     */     } 
/* 134 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/BaseThread/ThreadMutexInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */