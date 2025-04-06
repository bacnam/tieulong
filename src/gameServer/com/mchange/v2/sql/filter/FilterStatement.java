/*     */ package com.mchange.v2.sql.filter;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLWarning;
/*     */ import java.sql.Statement;
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
/*     */ public abstract class FilterStatement
/*     */   implements Statement
/*     */ {
/*     */   protected Statement inner;
/*     */   
/*     */   private void __setInner(Statement paramStatement) {
/*  57 */     this.inner = paramStatement;
/*     */   }
/*     */   
/*     */   public FilterStatement(Statement paramStatement) {
/*  61 */     __setInner(paramStatement);
/*     */   }
/*     */   
/*     */   public FilterStatement() {}
/*     */   
/*     */   public void setInner(Statement paramStatement) {
/*  67 */     __setInner(paramStatement);
/*     */   }
/*     */   public Statement getInner() {
/*  70 */     return this.inner;
/*     */   }
/*     */   
/*     */   public boolean execute(String paramString, int paramInt) throws SQLException {
/*  74 */     return this.inner.execute(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean execute(String paramString, String[] paramArrayOfString) throws SQLException {
/*  79 */     return this.inner.execute(paramString, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean execute(String paramString) throws SQLException {
/*  84 */     return this.inner.execute(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean execute(String paramString, int[] paramArrayOfint) throws SQLException {
/*  89 */     return this.inner.execute(paramString, paramArrayOfint);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearWarnings() throws SQLException {
/*  94 */     this.inner.clearWarnings();
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLWarning getWarnings() throws SQLException {
/*  99 */     return this.inner.getWarnings();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isClosed() throws SQLException {
/* 104 */     return this.inner.isClosed();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFetchDirection() throws SQLException {
/* 109 */     return this.inner.getFetchDirection();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFetchSize() throws SQLException {
/* 114 */     return this.inner.getFetchSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFetchDirection(int paramInt) throws SQLException {
/* 119 */     this.inner.setFetchDirection(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFetchSize(int paramInt) throws SQLException {
/* 124 */     this.inner.setFetchSize(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/* 129 */     return this.inner.getConnection();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getResultSetHoldability() throws SQLException {
/* 134 */     return this.inner.getResultSetHoldability();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addBatch(String paramString) throws SQLException {
/* 139 */     this.inner.addBatch(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel() throws SQLException {
/* 144 */     this.inner.cancel();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearBatch() throws SQLException {
/* 149 */     this.inner.clearBatch();
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeOnCompletion() throws SQLException {
/* 154 */     this.inner.closeOnCompletion();
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] executeBatch() throws SQLException {
/* 159 */     return this.inner.executeBatch();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet executeQuery(String paramString) throws SQLException {
/* 164 */     return this.inner.executeQuery(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public int executeUpdate(String paramString, int[] paramArrayOfint) throws SQLException {
/* 169 */     return this.inner.executeUpdate(paramString, paramArrayOfint);
/*     */   }
/*     */ 
/*     */   
/*     */   public int executeUpdate(String paramString, String[] paramArrayOfString) throws SQLException {
/* 174 */     return this.inner.executeUpdate(paramString, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   
/*     */   public int executeUpdate(String paramString) throws SQLException {
/* 179 */     return this.inner.executeUpdate(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public int executeUpdate(String paramString, int paramInt) throws SQLException {
/* 184 */     return this.inner.executeUpdate(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getGeneratedKeys() throws SQLException {
/* 189 */     return this.inner.getGeneratedKeys();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxFieldSize() throws SQLException {
/* 194 */     return this.inner.getMaxFieldSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxRows() throws SQLException {
/* 199 */     return this.inner.getMaxRows();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getMoreResults() throws SQLException {
/* 204 */     return this.inner.getMoreResults();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getMoreResults(int paramInt) throws SQLException {
/* 209 */     return this.inner.getMoreResults(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getQueryTimeout() throws SQLException {
/* 214 */     return this.inner.getQueryTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getResultSet() throws SQLException {
/* 219 */     return this.inner.getResultSet();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getResultSetConcurrency() throws SQLException {
/* 224 */     return this.inner.getResultSetConcurrency();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getResultSetType() throws SQLException {
/* 229 */     return this.inner.getResultSetType();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUpdateCount() throws SQLException {
/* 234 */     return this.inner.getUpdateCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCloseOnCompletion() throws SQLException {
/* 239 */     return this.inner.isCloseOnCompletion();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPoolable() throws SQLException {
/* 244 */     return this.inner.isPoolable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCursorName(String paramString) throws SQLException {
/* 249 */     this.inner.setCursorName(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEscapeProcessing(boolean paramBoolean) throws SQLException {
/* 254 */     this.inner.setEscapeProcessing(paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxFieldSize(int paramInt) throws SQLException {
/* 259 */     this.inner.setMaxFieldSize(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxRows(int paramInt) throws SQLException {
/* 264 */     this.inner.setMaxRows(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPoolable(boolean paramBoolean) throws SQLException {
/* 269 */     this.inner.setPoolable(paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setQueryTimeout(int paramInt) throws SQLException {
/* 274 */     this.inner.setQueryTimeout(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws SQLException {
/* 279 */     this.inner.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
/* 284 */     return this.inner.isWrapperFor(paramClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object unwrap(Class<?> paramClass) throws SQLException {
/* 289 */     return this.inner.unwrap(paramClass);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/sql/filter/FilterStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */