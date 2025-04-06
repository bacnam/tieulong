/*    */ package BaseTask.SyncTask;
/*    */ 
/*    */ import java.util.Calendar;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SyncTaskTimerCheckThread
/*    */   extends Thread
/*    */ {
/*    */   private boolean _m_bThreadExit;
/* 15 */   private int _m_iCheckTime = 50;
/*    */   private SyncTaskQueue parenTaskQueue;
/*    */   
/*    */   public SyncTaskTimerCheckThread(SyncTaskQueue parent) {
/* 19 */     this.parenTaskQueue = parent;
/* 20 */     this._m_bThreadExit = false;
/* 21 */     setName(this.parenTaskQueue.getTag());
/*    */   }
/*    */   
/*    */   public void dispose() {
/* 25 */     this._m_bThreadExit = true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/* 33 */     this.parenTaskQueue._refreshTimerTaskNowTime();
/* 34 */     long startTime = this.parenTaskQueue._getNowTime();
/* 35 */     long now = Calendar.getInstance().getTimeInMillis();
/* 36 */     while (!this._m_bThreadExit) {
/* 37 */       if (Calendar.getInstance().getTimeInMillis() - now > 10000L) {
/* 38 */         now = Calendar.getInstance().getTimeInMillis();
/*    */       }
/*    */ 
/*    */       
/* 42 */       long nowTime = this.parenTaskQueue._refreshTimerTaskNowTime();
/*    */ 
/*    */       
/* 45 */       this.parenTaskQueue.transTimer2NormalList(startTime);
/*    */ 
/*    */       
/* 48 */       startTime = nowTime;
/*    */ 
/*    */       
/*    */       try {
/* 52 */         sleep(this._m_iCheckTime);
/* 53 */       } catch (InterruptedException interruptedException) {}
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/BaseTask/SyncTask/SyncTaskTimerCheckThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */