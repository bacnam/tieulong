/*    */ package com.mchange.io;
/*    */ 
/*    */ import com.mchange.util.RobustMessageLogger;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
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
/*    */ public final class OutputStreamUtils
/*    */ {
/*    */   public static void attemptClose(OutputStream paramOutputStream) {
/* 46 */     attemptClose(paramOutputStream, null);
/*    */   }
/*    */   
/*    */   public static void attemptClose(OutputStream paramOutputStream, RobustMessageLogger paramRobustMessageLogger) {
/*    */     try {
/* 51 */       paramOutputStream.close();
/* 52 */     } catch (IOException iOException) {
/* 53 */       if (paramRobustMessageLogger != null) paramRobustMessageLogger.log(iOException, "IOException trying to close OutputStream"); 
/* 54 */     } catch (NullPointerException nullPointerException) {
/* 55 */       if (paramRobustMessageLogger != null) paramRobustMessageLogger.log(nullPointerException, "NullPointerException trying to close OutputStream"); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/OutputStreamUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */