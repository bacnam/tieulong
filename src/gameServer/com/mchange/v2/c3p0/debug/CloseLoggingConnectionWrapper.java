/*    */ package com.mchange.v2.c3p0.debug;
/*    */ 
/*    */ import com.mchange.v2.log.MLevel;
/*    */ import com.mchange.v2.log.MLog;
/*    */ import com.mchange.v2.log.MLogger;
/*    */ import com.mchange.v2.sql.filter.FilterConnection;
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
/*    */ public class CloseLoggingConnectionWrapper
/*    */   extends FilterConnection
/*    */ {
/* 45 */   static final MLogger logger = MLog.getLogger(CloseLoggingConnectionWrapper.class);
/*    */   
/*    */   final MLevel level;
/*    */ 
/*    */   
/*    */   public CloseLoggingConnectionWrapper(Connection conn, MLevel level) {
/* 51 */     super(conn);
/* 52 */     this.level = level;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws SQLException {
/* 57 */     super.close();
/* 58 */     if (logger.isLoggable(this.level))
/* 59 */       logger.log(this.level, "DEBUG: A Connection has closed been close()ed without error.", new SQLWarning("DEBUG STACK TRACE -- Connection.close() was called.")); 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/debug/CloseLoggingConnectionWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */