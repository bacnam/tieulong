/*    */ package BaseTask.SyncTask;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import java.util.TimerTask;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SyncTimerTask
/*    */   extends TimerTask
/*    */ {
/*    */   SyncTaskWrapper task;
/*    */   
/*    */   public SyncTimerTask(SyncTaskWrapper task) {
/* 17 */     this.task = task;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 22 */     Thread runningThread = this.task.getThread();
/* 23 */     String threadName = "";
/*    */     try {
/* 25 */       threadName = runningThread.getName();
/* 26 */     } catch (Exception exception) {}
/*    */ 
/*    */     
/* 29 */     StringBuffer info = new StringBuffer();
/* 30 */     info.append(String.format("[%s] [SyncTaskManager][timeout][id]:%s/[pool]:%s [run] > 3000ms, [timer]:%s, exInfo:%s \n", new Object[] { threadName, Long.valueOf(this.task.getID()), 
/* 31 */             Integer.valueOf(this.task.getParentQueue().getNormalTaskSize()), Long.valueOf(this.task.getTimer()), this.task.getInfo() }));
/* 32 */     info.append("Regist StackTrace: \n");
/* 33 */     int dept = 0;
/* 34 */     int ignoreDept = (this.task.getTimer() > 0L) ? 5 : 2; byte b; int i; StackTraceElement[] arrayOfStackTraceElement;
/* 35 */     for (i = (arrayOfStackTraceElement = this.task.getException().getStackTrace()).length, b = 0; b < i; ) { StackTraceElement st = arrayOfStackTraceElement[b];
/* 36 */       dept++;
/* 37 */       if (dept > ignoreDept) {
/* 38 */         info.append("  ");
/* 39 */         info.append(st.toString());
/* 40 */         info.append("\n");
/*    */       } 
/*    */       b++; }
/*    */     
/* 44 */     info.append(String.valueOf(threadName) + " Block StackTrace: \n");
/*    */     
/* 46 */     if (runningThread != null) {
/* 47 */       StackTraceElement[] stackTrace = runningThread.getStackTrace();
/*    */       
/* 49 */       if (stackTrace == null) {
/* 50 */         info.append("  SyncTask does not contain statckInfo\n");
/*    */       } else {
/* 52 */         StackTraceElement[] arrayOfStackTraceElement1; for (int j = (arrayOfStackTraceElement1 = stackTrace).length; i < j; ) { StackTraceElement st = arrayOfStackTraceElement1[i];
/* 53 */           info.append("  " + st.toString() + "\n"); i++; }
/*    */       
/*    */       } 
/*    */     } else {
/* 57 */       info.append("  SyncTask never run! Boring in the task_queue!\n");
/*    */     } 
/* 59 */     CommLog.warn(info.toString());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/BaseTask/SyncTask/SyncTimerTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */