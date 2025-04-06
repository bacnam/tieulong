/*    */ package com.mchange.io;
/*    */ 
/*    */ import com.mchange.util.RobustMessageLogger;
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
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
/*    */ public final class ReaderUtils
/*    */ {
/*    */   public static void attemptClose(Reader paramReader) {
/* 45 */     attemptClose(paramReader, null);
/*    */   }
/*    */   
/*    */   public static void attemptClose(Reader paramReader, RobustMessageLogger paramRobustMessageLogger) {
/*    */     try {
/* 50 */       paramReader.close();
/* 51 */     } catch (IOException iOException) {
/* 52 */       if (paramRobustMessageLogger != null) paramRobustMessageLogger.log(iOException, "IOException trying to close Reader"); 
/* 53 */     } catch (NullPointerException nullPointerException) {
/* 54 */       if (paramRobustMessageLogger != null) paramRobustMessageLogger.log(nullPointerException, "NullPointerException trying to close Reader"); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/ReaderUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */