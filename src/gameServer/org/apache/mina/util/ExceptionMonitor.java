/*    */ package org.apache.mina.util;
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
/*    */ public abstract class ExceptionMonitor
/*    */ {
/* 36 */   private static ExceptionMonitor instance = new DefaultExceptionMonitor();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ExceptionMonitor getInstance() {
/* 42 */     return instance;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setInstance(ExceptionMonitor monitor) {
/* 53 */     if (monitor == null) {
/* 54 */       monitor = new DefaultExceptionMonitor();
/*    */     }
/*    */     
/* 57 */     instance = monitor;
/*    */   }
/*    */   
/*    */   public abstract void exceptionCaught(Throwable paramThrowable);
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/ExceptionMonitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */