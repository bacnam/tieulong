/*    */ package com.mchange.v1.io;
/*    */ 
/*    */ import com.mchange.v2.log.MLevel;
/*    */ import com.mchange.v2.log.MLog;
/*    */ import com.mchange.v2.log.MLogger;
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
/*    */ public final class OutputStreamUtils
/*    */ {
/* 44 */   private static final MLogger logger = MLog.getLogger(OutputStreamUtils.class);
/*    */ 
/*    */   
/*    */   public static void attemptClose(OutputStream paramOutputStream) {
/*    */     try {
/* 49 */       if (paramOutputStream != null) paramOutputStream.close(); 
/* 50 */     } catch (IOException iOException) {
/*    */       
/* 52 */       if (logger.isLoggable(MLevel.WARNING))
/* 53 */         logger.log(MLevel.WARNING, "OutputStream close FAILED.", iOException); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/io/OutputStreamUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */