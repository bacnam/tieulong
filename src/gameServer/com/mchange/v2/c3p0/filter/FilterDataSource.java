/*    */ package com.mchange.v2.c3p0.filter;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
/*    */ import javax.sql.DataSource;
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
/*    */ 
/*    */ public abstract class FilterDataSource
/*    */   implements DataSource
/*    */ {
/*    */   protected DataSource inner;
/*    */   
/*    */   public FilterDataSource(DataSource inner) {
/* 51 */     this.inner = inner;
/*    */   }
/*    */ 
/*    */   
/*    */   public Connection getConnection() throws SQLException {
/* 56 */     return this.inner.getConnection();
/*    */   }
/*    */ 
/*    */   
/*    */   public Connection getConnection(String a, String b) throws SQLException {
/* 61 */     return this.inner.getConnection(a, b);
/*    */   }
/*    */ 
/*    */   
/*    */   public PrintWriter getLogWriter() throws SQLException {
/* 66 */     return this.inner.getLogWriter();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLoginTimeout() throws SQLException {
/* 71 */     return this.inner.getLoginTimeout();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLogWriter(PrintWriter a) throws SQLException {
/* 76 */     this.inner.setLogWriter(a);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLoginTimeout(int a) throws SQLException {
/* 81 */     this.inner.setLoginTimeout(a);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/filter/FilterDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */