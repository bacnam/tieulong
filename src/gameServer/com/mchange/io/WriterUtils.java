/*    */ package com.mchange.io;
/*    */ 
/*    */ import com.mchange.util.RobustMessageLogger;
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class WriterUtils
/*    */ {
/*    */   public static void attemptClose(Writer paramWriter) {
/* 48 */     attemptClose(paramWriter, null);
/*    */   }
/*    */   
/*    */   public static void attemptClose(Writer paramWriter, RobustMessageLogger paramRobustMessageLogger) {
/*    */     try {
/* 53 */       paramWriter.close();
/* 54 */     } catch (IOException iOException) {
/* 55 */       if (paramRobustMessageLogger != null) paramRobustMessageLogger.log(iOException, "IOException trying to close Writer"); 
/* 56 */     } catch (NullPointerException nullPointerException) {
/* 57 */       if (paramRobustMessageLogger != null) paramRobustMessageLogger.log(nullPointerException, "NullPointerException trying to close Writer"); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/WriterUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */