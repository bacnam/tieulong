/*     */ package BaseThread;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import java.util.concurrent.locks.ReentrantLock;
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
/*     */ public class CosMutex
/*     */ {
/*     */   private ReentrantLock _m_lMutex;
/*     */   private int _m_eMutexLevel;
/*     */   
/*     */   public CosMutex(int _mutexLevel) {
/*  27 */     this._m_lMutex = new ReentrantLock(true);
/*  28 */     this._m_eMutexLevel = _mutexLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMutexLevel() {
/*  37 */     return this._m_eMutexLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMutexLevel() {
/*  44 */     this._m_eMutexLevel--;
/*     */   }
/*     */   
/*     */   public void addMutexLevel(int _level) {
/*  48 */     this._m_eMutexLevel -= _level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reduceMutexLevel() {
/*  55 */     this._m_eMutexLevel++;
/*     */   }
/*     */   
/*     */   public void reduceMutexLevel(int _level) {
/*  59 */     this._m_eMutexLevel += _level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void lock() {
/*  69 */     if (!ThreadManager.getInstance().getCheckDeadLock()) {
/*  70 */       this._m_lMutex.lock();
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  75 */     long curThreadID = Thread.currentThread().getId();
/*     */     
/*  77 */     ThreadMutexInfo threadMutexInfo = ThreadManager.getInstance().getThreadMutexInfo(curThreadID);
/*     */     
/*  79 */     if (threadMutexInfo == null) {
/*  80 */       CommLog.warn("Unreg Thread try to get mutex", new Throwable());
/*     */       
/*     */       return;
/*     */     } 
/*     */     try {
/*  85 */       threadMutexInfo.tryLock(this);
/*  86 */       this._m_lMutex.lock();
/*  87 */     } catch (CosMutexException e) {
/*  88 */       CommLog.warn("无法获取高等级锁", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unlock() {
/*  98 */     if (ThreadManager.getInstance().getCheckDeadLock()) {
/*     */       
/* 100 */       long curThreadID = Thread.currentThread().getId();
/*     */       
/* 102 */       ThreadMutexInfo threadMutexInfo = ThreadManager.getInstance().getThreadMutexInfo(curThreadID);
/*     */       
/* 104 */       if (threadMutexInfo == null) {
/* 105 */         CommLog.warn("Unreg Thread try to release mutex", new Throwable());
/*     */         
/*     */         return;
/*     */       } 
/*     */       try {
/* 110 */         threadMutexInfo.tryUnlock(this);
/*     */       }
/* 112 */       catch (CosMutexException e) {
/* 113 */         CommLog.warn("尝试释放未获取的锁", e);
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 118 */       this._m_lMutex.unlock();
/* 119 */     } catch (Exception exception) {}
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/BaseThread/CosMutex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */