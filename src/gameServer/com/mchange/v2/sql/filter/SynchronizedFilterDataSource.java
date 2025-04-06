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
/*     */ public abstract class SynchronizedFilterDataSource
/*     */   implements DataSource
/*     */ {
/*     */   protected DataSource inner;
/*     */   
/*     */   private void __setInner(DataSource paramDataSource) {
/*  58 */     this.inner = paramDataSource;
/*     */   }
/*     */   
/*     */   public SynchronizedFilterDataSource(DataSource paramDataSource) {
/*  62 */     __setInner(paramDataSource);
/*     */   }
/*     */   
/*     */   public SynchronizedFilterDataSource() {}
/*     */   
/*     */   public synchronized void setInner(DataSource paramDataSource) {
/*  68 */     __setInner(paramDataSource);
/*     */   }
/*     */   public synchronized DataSource getInner() {
/*  71 */     return this.inner;
/*     */   }
/*     */   
/*     */   public synchronized Connection getConnection() throws SQLException {
/*  75 */     return this.inner.getConnection();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Connection getConnection(String paramString1, String paramString2) throws SQLException {
/*  80 */     return this.inner.getConnection(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized PrintWriter getLogWriter() throws SQLException {
/*  85 */     return this.inner.getLogWriter();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getLoginTimeout() throws SQLException {
/*  90 */     return this.inner.getLoginTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Logger getParentLogger() throws SQLFeatureNotSupportedException {
/*  95 */     return this.inner.getParentLogger();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setLogWriter(PrintWriter paramPrintWriter) throws SQLException {
/* 100 */     this.inner.setLogWriter(paramPrintWriter);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setLoginTimeout(int paramInt) throws SQLException {
/* 105 */     this.inner.setLoginTimeout(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean isWrapperFor(Class<?> paramClass) throws SQLException {
/* 110 */     return this.inner.isWrapperFor(paramClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Object unwrap(Class<?> paramClass) throws SQLException {
/* 115 */     return this.inner.unwrap(paramClass);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/sql/filter/SynchronizedFilterDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */