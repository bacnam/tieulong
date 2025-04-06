/*    */ package com.mchange.v1.db.sql;
/*    */ 
/*    */ import com.mchange.v2.log.MLevel;
/*    */ import com.mchange.v2.log.MLog;
/*    */ import com.mchange.v2.log.MLogger;
/*    */ import java.sql.SQLException;
/*    */ import java.sql.Statement;
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
/*    */ public final class StatementUtils
/*    */ {
/* 43 */   private static final MLogger logger = MLog.getLogger(StatementUtils.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean attemptClose(Statement paramStatement) {
/*    */     try {
/* 53 */       if (paramStatement != null) paramStatement.close(); 
/* 54 */       return true;
/*    */     }
/* 56 */     catch (SQLException sQLException) {
/*    */ 
/*    */       
/* 59 */       if (logger.isLoggable(MLevel.WARNING))
/* 60 */         logger.log(MLevel.WARNING, "Statement close FAILED.", sQLException); 
/* 61 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/StatementUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */