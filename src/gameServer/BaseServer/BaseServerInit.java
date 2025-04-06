/*    */ package BaseServer;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import BaseThread.ThreadManager;
/*    */ import ConsoleTask.ConsoleTaskDealThread;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BaseServerInit
/*    */ {
/*    */   private static boolean g_inited = false;
/*    */   
/*    */   public static void initBaseServer() {
/* 17 */     if (g_inited) {
/* 18 */       CommLog.error("Try to init Cos Server twice!!");
/*    */       return;
/*    */     } 
/* 21 */     g_inited = true;
/*    */ 
/*    */     
/* 24 */     ThreadManager.getInstance().regThread();
/*    */ 
/*    */     
/* 27 */     ConsoleTaskDealThread cosCmdTaskThread = new ConsoleTaskDealThread();
/* 28 */     cosCmdTaskThread.start();
/*    */     
/* 30 */     Monitor.getInstance().start();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/BaseServer/BaseServerInit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */