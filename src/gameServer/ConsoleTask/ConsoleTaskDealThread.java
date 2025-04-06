/*    */ package ConsoleTask;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import BaseTask.SyncTask.SyncTaskManager;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConsoleTaskDealThread
/*    */   extends Thread
/*    */ {
/*    */   private boolean _m_bThreadExit;
/*    */   
/*    */   public ConsoleTaskDealThread() {
/* 21 */     this._m_bThreadExit = false;
/* 22 */     setName("ConsoleTaskDealThread");
/*    */   }
/*    */   
/*    */   public void ExitThread() {
/* 26 */     this._m_bThreadExit = true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/* 33 */     while (!this._m_bThreadExit) {
/* 34 */       BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
/*    */       try {
/* 36 */         String str = br.readLine().trim();
/* 37 */         if (str.equals("")) {
/*    */           continue;
/*    */         }
/* 40 */         SyncTaskManager.task(() -> ConsoleTaskManager.GetInstance().run(paramString));
/* 41 */       } catch (IOException e) {
/* 42 */         CommLog.error("ConsoleTaskDealThread.run", e);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ConsoleTask/ConsoleTaskDealThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */