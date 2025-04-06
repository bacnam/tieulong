/*    */ package com.mchange.lang;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.io.StringWriter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ThrowableUtils
/*    */ {
/*    */   public static String extractStackTrace(Throwable paramThrowable) {
/* 44 */     StringWriter stringWriter = new StringWriter();
/* 45 */     PrintWriter printWriter = new PrintWriter(stringWriter);
/* 46 */     paramThrowable.printStackTrace(printWriter);
/* 47 */     printWriter.flush();
/* 48 */     return stringWriter.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean isChecked(Throwable paramThrowable) {
/* 53 */     return (paramThrowable instanceof Exception && !(paramThrowable instanceof RuntimeException));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isUnchecked(Throwable paramThrowable) {
/* 59 */     return !isChecked(paramThrowable);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/lang/ThrowableUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */