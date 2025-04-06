/*    */ package ConsoleTask;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConsoleTaskManager
/*    */ {
/* 15 */   private static ConsoleTaskManager instance = new ConsoleTaskManager();
/*    */   
/*    */   public static ConsoleTaskManager GetInstance() {
/* 18 */     return instance;
/*    */   }
/*    */   
/* 21 */   private _AConsoleTaskRunner runner = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setRunner(_AConsoleTaskRunner _runner) {
/* 29 */     this.runner = _runner;
/*    */   }
/*    */   
/*    */   public void run(String cmd) {
/* 33 */     if (this.runner == null) {
/* 34 */       CommLog.info(String.format("Class<_ACosCmdRunner> doesn't reg to CosCmdManager, reg it to deal the cmd:%s", new Object[] { cmd }));
/*    */       
/*    */       return;
/*    */     } 
/* 38 */     this.runner.run(cmd);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ConsoleTask/ConsoleTaskManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */