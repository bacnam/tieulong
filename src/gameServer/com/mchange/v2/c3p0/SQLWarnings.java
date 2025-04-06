/*    */ package com.mchange.v2.c3p0;
/*    */ 
/*    */ import com.mchange.v2.log.MLevel;
/*    */ import com.mchange.v2.log.MLog;
/*    */ import com.mchange.v2.log.MLogger;
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
/*    */ import java.sql.SQLWarning;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SQLWarnings
/*    */ {
/* 48 */   static final MLogger logger = MLog.getLogger(SQLWarnings.class);
/*    */ 
/*    */   
/*    */   public static void logAndClearWarnings(Connection con) throws SQLException {
/* 52 */     if (logger.isLoggable(MLevel.INFO))
/*    */     {
/* 54 */       for (SQLWarning w = con.getWarnings(); w != null; w = w.getNextWarning())
/* 55 */         logger.log(MLevel.INFO, w.getMessage(), w); 
/*    */     }
/* 57 */     con.clearWarnings();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/SQLWarnings.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */