/*    */ package com.mchange.v2.c3p0.util;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CloseReportingConnectionWrapper
/*    */   extends FilterConnection
/*    */ {
/*    */   public CloseReportingConnectionWrapper(Connection conn) {
/* 47 */     super(conn);
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws SQLException {
/* 52 */     (new SQLWarning("Connection.close() called!")).printStackTrace();
/* 53 */     super.close();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/util/CloseReportingConnectionWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */