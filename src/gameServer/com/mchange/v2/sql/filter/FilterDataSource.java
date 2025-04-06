/*     */ package com.mchange.v2.sql.filter;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.util.logging.Logger;
/*     */ import javax.sql.DataSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class FilterDataSource
/*     */   implements DataSource
/*     */ {
/*     */   protected DataSource inner;
/*     */   
/*     */   private void __setInner(DataSource paramDataSource) {
/*  58 */     this.inner = paramDataSource;
/*     */   }
/*     */   
/*     */   public FilterDataSource(DataSource paramDataSource) {
/*  62 */     __setInner(paramDataSource);
/*     */   }
/*     */   
/*     */   public FilterDataSource() {}
/*     */   
/*     */   public void setInner(DataSource paramDataSource) {
/*  68 */     __setInner(paramDataSource);
/*     */   }
/*     */   public DataSource getInner() {
/*  71 */     return this.inner;
/*     */   }
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/*  75 */     return this.inner.getConnection();
/*     */   }
/*     */ 
/*     */   
/*     */   public Connection getConnection(String paramString1, String paramString2) throws SQLException {
/*  80 */     return this.inner.getConnection(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   
/*     */   public PrintWriter getLogWriter() throws SQLException {
/*  85 */     return this.inner.getLogWriter();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLoginTimeout() throws SQLException {
/*  90 */     return this.inner.getLoginTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public Logger getParentLogger() throws SQLFeatureNotSupportedException {
/*  95 */     return this.inner.getParentLogger();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLogWriter(PrintWriter paramPrintWriter) throws SQLException {
/* 100 */     this.inner.setLogWriter(paramPrintWriter);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLoginTimeout(int paramInt) throws SQLException {
/* 105 */     this.inner.setLoginTimeout(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
/* 110 */     return this.inner.isWrapperFor(paramClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object unwrap(Class<?> paramClass) throws SQLException {
/* 115 */     return this.inner.unwrap(paramClass);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/sql/filter/FilterDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */