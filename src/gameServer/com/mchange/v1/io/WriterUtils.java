/*    */ package com.mchange.v1.io;
/*    */ 
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
/*    */ public final class WriterUtils
/*    */ {
/*    */   public static void attemptClose(Writer paramWriter) {
/*    */     try {
/* 46 */       if (paramWriter != null) paramWriter.close(); 
/* 47 */     } catch (IOException iOException) {
/* 48 */       iOException.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/io/WriterUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */