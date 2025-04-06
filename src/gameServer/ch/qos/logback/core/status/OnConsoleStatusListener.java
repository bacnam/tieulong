/*    */ package ch.qos.logback.core.status;
/*    */ 
/*    */ import ch.qos.logback.core.Context;
/*    */ import java.io.PrintStream;
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
/*    */ public class OnConsoleStatusListener
/*    */   extends OnPrintStreamStatusListenerBase
/*    */ {
/*    */   protected PrintStream getPrintStream() {
/* 29 */     return System.out;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void addNewInstanceToContext(Context context) {
/* 40 */     OnConsoleStatusListener onConsoleStatusListener = new OnConsoleStatusListener();
/* 41 */     onConsoleStatusListener.setContext(context);
/* 42 */     onConsoleStatusListener.start();
/* 43 */     context.getStatusManager().add(onConsoleStatusListener);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/status/OnConsoleStatusListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */