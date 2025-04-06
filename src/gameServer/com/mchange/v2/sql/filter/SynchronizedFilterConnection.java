/*     */ package com.mchange.v2.sql.filter;
/*     */ 
/*     */ import java.sql.Array;
/*     */ import java.sql.Blob;
/*     */ import java.sql.CallableStatement;
/*     */ import java.sql.Clob;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.NClob;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLClientInfoException;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLWarning;
/*     */ import java.sql.SQLXML;
/*     */ import java.sql.Savepoint;
/*     */ import java.sql.Statement;
/*     */ import java.sql.Struct;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.Executor;
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
/*     */ public abstract class SynchronizedFilterConnection
/*     */   implements Connection
/*     */ {
/*     */   protected Connection inner;
/*     */   
/*     */   private void __setInner(Connection paramConnection) {
/*  70 */     this.inner = paramConnection;
/*     */   }
/*     */   
/*     */   public SynchronizedFilterConnection(Connection paramConnection) {
/*  74 */     __setInner(paramConnection);
/*     */   }
/*     */   
/*     */   public SynchronizedFilterConnection() {}
/*     */   
/*     */   public synchronized void setInner(Connection paramConnection) {
/*  80 */     __setInner(paramConnection);
/*     */   }
/*     */   public synchronized Connection getInner() {
/*  83 */     return this.inner;
/*     */   }
/*     */   
/*     */   public synchronized void commit() throws SQLException {
/*  87 */     this.inner.commit();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void clearWarnings() throws SQLException {
/*  92 */     this.inner.clearWarnings();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Array createArrayOf(String paramString, Object[] paramArrayOfObject) throws SQLException {
/*  97 */     return this.inner.createArrayOf(paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Blob createBlob() throws SQLException {
/* 102 */     return this.inner.createBlob();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Clob createClob() throws SQLException {
/* 107 */     return this.inner.createClob();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized NClob createNClob() throws SQLException {
/* 112 */     return this.inner.createNClob();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized SQLXML createSQLXML() throws SQLException {
/* 117 */     return this.inner.createSQLXML();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Statement createStatement(int paramInt1, int paramInt2, int paramInt3) throws SQLException {
/* 122 */     return this.inner.createStatement(paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Statement createStatement(int paramInt1, int paramInt2) throws SQLException {
/* 127 */     return this.inner.createStatement(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Statement createStatement() throws SQLException {
/* 132 */     return this.inner.createStatement();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Struct createStruct(String paramString, Object[] paramArrayOfObject) throws SQLException {
/* 137 */     return this.inner.createStruct(paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean getAutoCommit() throws SQLException {
/* 142 */     return this.inner.getAutoCommit();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getCatalog() throws SQLException {
/* 147 */     return this.inner.getCatalog();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getClientInfo(String paramString) throws SQLException {
/* 152 */     return this.inner.getClientInfo(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Properties getClientInfo() throws SQLException {
/* 157 */     return this.inner.getClientInfo();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getHoldability() throws SQLException {
/* 162 */     return this.inner.getHoldability();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized DatabaseMetaData getMetaData() throws SQLException {
/* 167 */     return this.inner.getMetaData();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getNetworkTimeout() throws SQLException {
/* 172 */     return this.inner.getNetworkTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getSchema() throws SQLException {
/* 177 */     return this.inner.getSchema();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getTransactionIsolation() throws SQLException {
/* 182 */     return this.inner.getTransactionIsolation();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Map getTypeMap() throws SQLException {
/* 187 */     return this.inner.getTypeMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized SQLWarning getWarnings() throws SQLException {
/* 192 */     return this.inner.getWarnings();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean isClosed() throws SQLException {
/* 197 */     return this.inner.isClosed();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String nativeSQL(String paramString) throws SQLException {
/* 202 */     return this.inner.nativeSQL(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized CallableStatement prepareCall(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
/* 207 */     return this.inner.prepareCall(paramString, paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized CallableStatement prepareCall(String paramString, int paramInt1, int paramInt2) throws SQLException {
/* 212 */     return this.inner.prepareCall(paramString, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized CallableStatement prepareCall(String paramString) throws SQLException {
/* 217 */     return this.inner.prepareCall(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
/* 222 */     return this.inner.prepareStatement(paramString, paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized PreparedStatement prepareStatement(String paramString, int paramInt) throws SQLException {
/* 227 */     return this.inner.prepareStatement(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized PreparedStatement prepareStatement(String paramString, int[] paramArrayOfint) throws SQLException {
/* 232 */     return this.inner.prepareStatement(paramString, paramArrayOfint);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized PreparedStatement prepareStatement(String paramString, String[] paramArrayOfString) throws SQLException {
/* 237 */     return this.inner.prepareStatement(paramString, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized PreparedStatement prepareStatement(String paramString) throws SQLException {
/* 242 */     return this.inner.prepareStatement(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2) throws SQLException {
/* 247 */     return this.inner.prepareStatement(paramString, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void releaseSavepoint(Savepoint paramSavepoint) throws SQLException {
/* 252 */     this.inner.releaseSavepoint(paramSavepoint);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void rollback() throws SQLException {
/* 257 */     this.inner.rollback();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void rollback(Savepoint paramSavepoint) throws SQLException {
/* 262 */     this.inner.rollback(paramSavepoint);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setAutoCommit(boolean paramBoolean) throws SQLException {
/* 267 */     this.inner.setAutoCommit(paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setCatalog(String paramString) throws SQLException {
/* 272 */     this.inner.setCatalog(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setClientInfo(String paramString1, String paramString2) throws SQLClientInfoException {
/* 277 */     this.inner.setClientInfo(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setClientInfo(Properties paramProperties) throws SQLClientInfoException {
/* 282 */     this.inner.setClientInfo(paramProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setHoldability(int paramInt) throws SQLException {
/* 287 */     this.inner.setHoldability(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setNetworkTimeout(Executor paramExecutor, int paramInt) throws SQLException {
/* 292 */     this.inner.setNetworkTimeout(paramExecutor, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Savepoint setSavepoint() throws SQLException {
/* 297 */     return this.inner.setSavepoint();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Savepoint setSavepoint(String paramString) throws SQLException {
/* 302 */     return this.inner.setSavepoint(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setSchema(String paramString) throws SQLException {
/* 307 */     this.inner.setSchema(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setTransactionIsolation(int paramInt) throws SQLException {
/* 312 */     this.inner.setTransactionIsolation(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setTypeMap(Map<String, Class<?>> paramMap) throws SQLException {
/* 317 */     this.inner.setTypeMap(paramMap);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setReadOnly(boolean paramBoolean) throws SQLException {
/* 322 */     this.inner.setReadOnly(paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void close() throws SQLException {
/* 327 */     this.inner.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean isValid(int paramInt) throws SQLException {
/* 332 */     return this.inner.isValid(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean isReadOnly() throws SQLException {
/* 337 */     return this.inner.isReadOnly();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void abort(Executor paramExecutor) throws SQLException {
/* 342 */     this.inner.abort(paramExecutor);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean isWrapperFor(Class<?> paramClass) throws SQLException {
/* 347 */     return this.inner.isWrapperFor(paramClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Object unwrap(Class<?> paramClass) throws SQLException {
/* 352 */     return this.inner.unwrap(paramClass);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/sql/filter/SynchronizedFilterConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */