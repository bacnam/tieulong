/*    */ package BaseThread;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ThreadManager
/*    */ {
/* 17 */   private static ThreadManager g_instance = new ThreadManager();
/*    */   private boolean g_checkDeadLock = true;
/*    */   
/*    */   public static ThreadManager getInstance() {
/* 21 */     if (g_instance == null) {
/* 22 */       g_instance = new ThreadManager();
/*    */     }
/* 24 */     return g_instance;
/*    */   }
/*    */ 
/*    */   
/*    */   private Hashtable<Long, ThreadMutexInfo> _m_htThreadMutexInfoTable;
/*    */   
/*    */   protected ThreadManager() {
/* 31 */     this._m_htThreadMutexInfoTable = new Hashtable<>();
/*    */   }
/*    */   
/*    */   public void regThread() {
/* 35 */     long threadID = Thread.currentThread().getId();
/* 36 */     regThread(threadID);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ThreadMutexInfo regThread(long _threadID) {
/* 45 */     if (this._m_htThreadMutexInfoTable.containsKey(Long.valueOf(_threadID))) {
/* 46 */       return null;
/*    */     }
/* 48 */     CommLog.info("Reg thread: " + _threadID);
/*    */     
/* 50 */     ThreadMutexInfo threadMutexInfo = new ThreadMutexInfo(_threadID);
/* 51 */     this._m_htThreadMutexInfoTable.put(Long.valueOf(_threadID), threadMutexInfo);
/*    */     
/* 53 */     return threadMutexInfo;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ThreadMutexInfo getThreadMutexInfo(long _threadID) {
/* 63 */     return this._m_htThreadMutexInfoTable.get(Long.valueOf(_threadID));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean getCheckDeadLock() {
/* 72 */     return this.g_checkDeadLock;
/*    */   }
/*    */   
/*    */   public void setCheckDeadThread(boolean isCheck) {
/* 76 */     this.g_checkDeadLock = isCheck;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/BaseThread/ThreadManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */