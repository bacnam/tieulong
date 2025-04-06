/*    */ package com.mysql.jdbc.interceptors;
/*    */ 
/*    */ import com.mysql.jdbc.Connection;
/*    */ import com.mysql.jdbc.ResultSetInternalMethods;
/*    */ import com.mysql.jdbc.Statement;
/*    */ import com.mysql.jdbc.StatementInterceptor;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.SQLException;
/*    */ import java.util.Properties;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SessionAssociationInterceptor
/*    */   implements StatementInterceptor
/*    */ {
/*    */   protected String currentSessionKey;
/* 41 */   protected static final ThreadLocal<String> sessionLocal = new ThreadLocal<String>();
/*    */   
/*    */   public static final void setSessionKey(String key) {
/* 44 */     sessionLocal.set(key);
/*    */   }
/*    */   
/*    */   public static final void resetSessionKey() {
/* 48 */     sessionLocal.set(null);
/*    */   }
/*    */   
/*    */   public static final String getSessionKey() {
/* 52 */     return sessionLocal.get();
/*    */   }
/*    */   
/*    */   public boolean executeTopLevelOnly() {
/* 56 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void init(Connection conn, Properties props) throws SQLException {}
/*    */ 
/*    */ 
/*    */   
/*    */   public ResultSetInternalMethods postProcess(String sql, Statement interceptedStatement, ResultSetInternalMethods originalResultSet, Connection connection) throws SQLException {
/* 67 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ResultSetInternalMethods preProcess(String sql, Statement interceptedStatement, Connection connection) throws SQLException {
/* 73 */     String key = getSessionKey();
/*    */     
/* 75 */     if (key != null && !key.equals(this.currentSessionKey)) {
/* 76 */       PreparedStatement pstmt = connection.clientPrepareStatement("SET @mysql_proxy_session=?");
/*    */       
/*    */       try {
/* 79 */         pstmt.setString(1, key);
/* 80 */         pstmt.execute();
/*    */       } finally {
/* 82 */         pstmt.close();
/*    */       } 
/*    */       
/* 85 */       this.currentSessionKey = key;
/*    */     } 
/*    */     
/* 88 */     return null;
/*    */   }
/*    */   
/*    */   public void destroy() {}
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mysql/jdbc/interceptors/SessionAssociationInterceptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */