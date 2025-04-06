/*    */ package com.mchange.v1.db.sql;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.SQLException;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public class ConnectionBundleImpl
/*    */   implements ConnectionBundle
/*    */ {
/*    */   Connection con;
/* 47 */   Map map = new HashMap<Object, Object>();
/*    */   
/*    */   public ConnectionBundleImpl(Connection paramConnection) {
/* 50 */     this.con = paramConnection;
/*    */   }
/*    */   public Connection getConnection() {
/* 53 */     return this.con;
/*    */   }
/*    */   public PreparedStatement getStatement(String paramString) {
/* 56 */     return (PreparedStatement)this.map.get(paramString);
/*    */   }
/*    */   public void putStatement(String paramString, PreparedStatement paramPreparedStatement) {
/* 59 */     this.map.put(paramString, paramPreparedStatement);
/*    */   }
/*    */   public void close() throws SQLException {
/* 62 */     this.con.close();
/*    */   }
/*    */   public void finalize() throws Exception {
/* 65 */     if (!this.con.isClosed()) close(); 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/ConnectionBundleImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */