/*    */ package com.mchange.v1.util;
/*    */ 
/*    */ import com.mchange.v2.log.MLevel;
/*    */ import com.mchange.v2.log.MLog;
/*    */ import com.mchange.v2.log.MLogger;
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
/*    */ public final class ClosableResourceUtils
/*    */ {
/* 42 */   private static final MLogger logger = MLog.getLogger(ClosableResourceUtils.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Exception attemptClose(ClosableResource paramClosableResource) {
/*    */     try {
/* 53 */       if (paramClosableResource != null) paramClosableResource.close(); 
/* 54 */       return null;
/*    */     }
/* 56 */     catch (Exception exception) {
/*    */ 
/*    */       
/* 59 */       if (logger.isLoggable(MLevel.WARNING))
/* 60 */         logger.log(MLevel.WARNING, "CloseableResource close FAILED.", exception); 
/* 61 */       return exception;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/ClosableResourceUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */