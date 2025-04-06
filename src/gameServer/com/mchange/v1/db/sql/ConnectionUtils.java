/*    */ package com.mchange.v1.db.sql;
/*    */ 
/*    */ import com.mchange.v2.log.MLevel;
/*    */ import com.mchange.v2.log.MLog;
/*    */ import com.mchange.v2.log.MLogger;
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
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
/*    */ public final class ConnectionUtils
/*    */ {
/* 43 */   private static final MLogger logger = MLog.getLogger(ConnectionUtils.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean attemptClose(Connection paramConnection) {
/*    */     try {
/* 53 */       if (paramConnection != null) paramConnection.close();
/*    */       
/* 55 */       return true;
/*    */     }
/* 57 */     catch (SQLException sQLException) {
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 62 */       if (logger.isLoggable(MLevel.WARNING))
/* 63 */         logger.log(MLevel.WARNING, "Connection close FAILED.", sQLException); 
/* 64 */       return false;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean attemptRollback(Connection paramConnection) {
/*    */     try {
/* 72 */       if (paramConnection != null) paramConnection.rollback(); 
/* 73 */       return true;
/*    */     }
/* 75 */     catch (SQLException sQLException) {
/*    */ 
/*    */ 
/*    */       
/* 79 */       if (logger.isLoggable(MLevel.WARNING))
/* 80 */         logger.log(MLevel.WARNING, "Rollback FAILED.", sQLException); 
/* 81 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/ConnectionUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */