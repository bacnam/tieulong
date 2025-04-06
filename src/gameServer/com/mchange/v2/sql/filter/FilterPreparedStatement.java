/*     */ package com.mchange.v2.sql.filter;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.math.BigDecimal;
/*     */ import java.net.URL;
/*     */ import java.sql.Array;
/*     */ import java.sql.Blob;
/*     */ import java.sql.Clob;
/*     */ import java.sql.Connection;
/*     */ import java.sql.Date;
/*     */ import java.sql.NClob;
/*     */ import java.sql.ParameterMetaData;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.Ref;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.ResultSetMetaData;
/*     */ import java.sql.RowId;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLWarning;
/*     */ import java.sql.SQLXML;
/*     */ import java.sql.Time;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.Calendar;
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
/*     */ public abstract class FilterPreparedStatement
/*     */   implements PreparedStatement
/*     */ {
/*     */   protected PreparedStatement inner;
/*     */   
/*     */   private void __setInner(PreparedStatement paramPreparedStatement) {
/*  74 */     this.inner = paramPreparedStatement;
/*     */   }
/*     */   
/*     */   public FilterPreparedStatement(PreparedStatement paramPreparedStatement) {
/*  78 */     __setInner(paramPreparedStatement);
/*     */   }
/*     */   
/*     */   public FilterPreparedStatement() {}
/*     */   
/*     */   public void setInner(PreparedStatement paramPreparedStatement) {
/*  84 */     __setInner(paramPreparedStatement);
/*     */   }
/*     */   public PreparedStatement getInner() {
/*  87 */     return this.inner;
/*     */   }
/*     */   
/*     */   public boolean execute() throws SQLException {
/*  91 */     return this.inner.execute();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSetMetaData getMetaData() throws SQLException {
/*  96 */     return this.inner.getMetaData();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setArray(int paramInt, Array paramArray) throws SQLException {
/* 101 */     this.inner.setArray(paramInt, paramArray);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addBatch() throws SQLException {
/* 106 */     this.inner.addBatch();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet executeQuery() throws SQLException {
/* 111 */     return this.inner.executeQuery();
/*     */   }
/*     */ 
/*     */   
/*     */   public int executeUpdate() throws SQLException {
/* 116 */     return this.inner.executeUpdate();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearParameters() throws SQLException {
/* 121 */     this.inner.clearParameters();
/*     */   }
/*     */ 
/*     */   
/*     */   public ParameterMetaData getParameterMetaData() throws SQLException {
/* 126 */     return this.inner.getParameterMetaData();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAsciiStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
/* 131 */     this.inner.setAsciiStream(paramInt, paramInputStream, paramLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAsciiStream(int paramInt, InputStream paramInputStream) throws SQLException {
/* 136 */     this.inner.setAsciiStream(paramInt, paramInputStream);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
/* 141 */     this.inner.setAsciiStream(paramInt1, paramInputStream, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBigDecimal(int paramInt, BigDecimal paramBigDecimal) throws SQLException {
/* 146 */     this.inner.setBigDecimal(paramInt, paramBigDecimal);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
/* 151 */     this.inner.setBinaryStream(paramInt1, paramInputStream, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBinaryStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
/* 156 */     this.inner.setBinaryStream(paramInt, paramInputStream, paramLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBinaryStream(int paramInt, InputStream paramInputStream) throws SQLException {
/* 161 */     this.inner.setBinaryStream(paramInt, paramInputStream);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlob(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
/* 166 */     this.inner.setBlob(paramInt, paramInputStream, paramLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlob(int paramInt, Blob paramBlob) throws SQLException {
/* 171 */     this.inner.setBlob(paramInt, paramBlob);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlob(int paramInt, InputStream paramInputStream) throws SQLException {
/* 176 */     this.inner.setBlob(paramInt, paramInputStream);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBytes(int paramInt, byte[] paramArrayOfbyte) throws SQLException {
/* 181 */     this.inner.setBytes(paramInt, paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCharacterStream(int paramInt, Reader paramReader) throws SQLException {
/* 186 */     this.inner.setCharacterStream(paramInt, paramReader);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCharacterStream(int paramInt1, Reader paramReader, int paramInt2) throws SQLException {
/* 191 */     this.inner.setCharacterStream(paramInt1, paramReader, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
/* 196 */     this.inner.setCharacterStream(paramInt, paramReader, paramLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
/* 201 */     this.inner.setClob(paramInt, paramReader, paramLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setClob(int paramInt, Reader paramReader) throws SQLException {
/* 206 */     this.inner.setClob(paramInt, paramReader);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setClob(int paramInt, Clob paramClob) throws SQLException {
/* 211 */     this.inner.setClob(paramInt, paramClob);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDate(int paramInt, Date paramDate) throws SQLException {
/* 216 */     this.inner.setDate(paramInt, paramDate);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDate(int paramInt, Date paramDate, Calendar paramCalendar) throws SQLException {
/* 221 */     this.inner.setDate(paramInt, paramDate, paramCalendar);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
/* 226 */     this.inner.setNCharacterStream(paramInt, paramReader, paramLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNCharacterStream(int paramInt, Reader paramReader) throws SQLException {
/* 231 */     this.inner.setNCharacterStream(paramInt, paramReader);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNClob(int paramInt, Reader paramReader) throws SQLException {
/* 236 */     this.inner.setNClob(paramInt, paramReader);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
/* 241 */     this.inner.setNClob(paramInt, paramReader, paramLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNClob(int paramInt, NClob paramNClob) throws SQLException {
/* 246 */     this.inner.setNClob(paramInt, paramNClob);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNString(int paramInt, String paramString) throws SQLException {
/* 251 */     this.inner.setNString(paramInt, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNull(int paramInt1, int paramInt2) throws SQLException {
/* 256 */     this.inner.setNull(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNull(int paramInt1, int paramInt2, String paramString) throws SQLException {
/* 261 */     this.inner.setNull(paramInt1, paramInt2, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setObject(int paramInt1, Object paramObject, int paramInt2) throws SQLException {
/* 266 */     this.inner.setObject(paramInt1, paramObject, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setObject(int paramInt1, Object paramObject, int paramInt2, int paramInt3) throws SQLException {
/* 271 */     this.inner.setObject(paramInt1, paramObject, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setObject(int paramInt, Object paramObject) throws SQLException {
/* 276 */     this.inner.setObject(paramInt, paramObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRef(int paramInt, Ref paramRef) throws SQLException {
/* 281 */     this.inner.setRef(paramInt, paramRef);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRowId(int paramInt, RowId paramRowId) throws SQLException {
/* 286 */     this.inner.setRowId(paramInt, paramRowId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSQLXML(int paramInt, SQLXML paramSQLXML) throws SQLException {
/* 291 */     this.inner.setSQLXML(paramInt, paramSQLXML);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setString(int paramInt, String paramString) throws SQLException {
/* 296 */     this.inner.setString(paramInt, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUnicodeStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
/* 301 */     this.inner.setUnicodeStream(paramInt1, paramInputStream, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBoolean(int paramInt, boolean paramBoolean) throws SQLException {
/* 306 */     this.inner.setBoolean(paramInt, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setByte(int paramInt, byte paramByte) throws SQLException {
/* 311 */     this.inner.setByte(paramInt, paramByte);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDouble(int paramInt, double paramDouble) throws SQLException {
/* 316 */     this.inner.setDouble(paramInt, paramDouble);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFloat(int paramInt, float paramFloat) throws SQLException {
/* 321 */     this.inner.setFloat(paramInt, paramFloat);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInt(int paramInt1, int paramInt2) throws SQLException {
/* 326 */     this.inner.setInt(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLong(int paramInt, long paramLong) throws SQLException {
/* 331 */     this.inner.setLong(paramInt, paramLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setShort(int paramInt, short paramShort) throws SQLException {
/* 336 */     this.inner.setShort(paramInt, paramShort);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTimestamp(int paramInt, Timestamp paramTimestamp, Calendar paramCalendar) throws SQLException {
/* 341 */     this.inner.setTimestamp(paramInt, paramTimestamp, paramCalendar);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTimestamp(int paramInt, Timestamp paramTimestamp) throws SQLException {
/* 346 */     this.inner.setTimestamp(paramInt, paramTimestamp);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setURL(int paramInt, URL paramURL) throws SQLException {
/* 351 */     this.inner.setURL(paramInt, paramURL);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTime(int paramInt, Time paramTime, Calendar paramCalendar) throws SQLException {
/* 356 */     this.inner.setTime(paramInt, paramTime, paramCalendar);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTime(int paramInt, Time paramTime) throws SQLException {
/* 361 */     this.inner.setTime(paramInt, paramTime);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean execute(String paramString, int paramInt) throws SQLException {
/* 366 */     return this.inner.execute(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean execute(String paramString, String[] paramArrayOfString) throws SQLException {
/* 371 */     return this.inner.execute(paramString, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean execute(String paramString) throws SQLException {
/* 376 */     return this.inner.execute(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean execute(String paramString, int[] paramArrayOfint) throws SQLException {
/* 381 */     return this.inner.execute(paramString, paramArrayOfint);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearWarnings() throws SQLException {
/* 386 */     this.inner.clearWarnings();
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLWarning getWarnings() throws SQLException {
/* 391 */     return this.inner.getWarnings();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isClosed() throws SQLException {
/* 396 */     return this.inner.isClosed();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFetchDirection() throws SQLException {
/* 401 */     return this.inner.getFetchDirection();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFetchSize() throws SQLException {
/* 406 */     return this.inner.getFetchSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFetchDirection(int paramInt) throws SQLException {
/* 411 */     this.inner.setFetchDirection(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFetchSize(int paramInt) throws SQLException {
/* 416 */     this.inner.setFetchSize(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/* 421 */     return this.inner.getConnection();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getResultSetHoldability() throws SQLException {
/* 426 */     return this.inner.getResultSetHoldability();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addBatch(String paramString) throws SQLException {
/* 431 */     this.inner.addBatch(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel() throws SQLException {
/* 436 */     this.inner.cancel();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearBatch() throws SQLException {
/* 441 */     this.inner.clearBatch();
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeOnCompletion() throws SQLException {
/* 446 */     this.inner.closeOnCompletion();
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] executeBatch() throws SQLException {
/* 451 */     return this.inner.executeBatch();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet executeQuery(String paramString) throws SQLException {
/* 456 */     return this.inner.executeQuery(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public int executeUpdate(String paramString, int[] paramArrayOfint) throws SQLException {
/* 461 */     return this.inner.executeUpdate(paramString, paramArrayOfint);
/*     */   }
/*     */ 
/*     */   
/*     */   public int executeUpdate(String paramString, String[] paramArrayOfString) throws SQLException {
/* 466 */     return this.inner.executeUpdate(paramString, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   
/*     */   public int executeUpdate(String paramString) throws SQLException {
/* 471 */     return this.inner.executeUpdate(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public int executeUpdate(String paramString, int paramInt) throws SQLException {
/* 476 */     return this.inner.executeUpdate(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getGeneratedKeys() throws SQLException {
/* 481 */     return this.inner.getGeneratedKeys();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxFieldSize() throws SQLException {
/* 486 */     return this.inner.getMaxFieldSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxRows() throws SQLException {
/* 491 */     return this.inner.getMaxRows();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getMoreResults() throws SQLException {
/* 496 */     return this.inner.getMoreResults();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getMoreResults(int paramInt) throws SQLException {
/* 501 */     return this.inner.getMoreResults(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getQueryTimeout() throws SQLException {
/* 506 */     return this.inner.getQueryTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getResultSet() throws SQLException {
/* 511 */     return this.inner.getResultSet();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getResultSetConcurrency() throws SQLException {
/* 516 */     return this.inner.getResultSetConcurrency();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getResultSetType() throws SQLException {
/* 521 */     return this.inner.getResultSetType();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUpdateCount() throws SQLException {
/* 526 */     return this.inner.getUpdateCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCloseOnCompletion() throws SQLException {
/* 531 */     return this.inner.isCloseOnCompletion();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPoolable() throws SQLException {
/* 536 */     return this.inner.isPoolable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCursorName(String paramString) throws SQLException {
/* 541 */     this.inner.setCursorName(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEscapeProcessing(boolean paramBoolean) throws SQLException {
/* 546 */     this.inner.setEscapeProcessing(paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxFieldSize(int paramInt) throws SQLException {
/* 551 */     this.inner.setMaxFieldSize(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxRows(int paramInt) throws SQLException {
/* 556 */     this.inner.setMaxRows(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPoolable(boolean paramBoolean) throws SQLException {
/* 561 */     this.inner.setPoolable(paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setQueryTimeout(int paramInt) throws SQLException {
/* 566 */     this.inner.setQueryTimeout(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws SQLException {
/* 571 */     this.inner.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
/* 576 */     return this.inner.isWrapperFor(paramClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object unwrap(Class<?> paramClass) throws SQLException {
/* 581 */     return this.inner.unwrap(paramClass);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/sql/filter/FilterPreparedStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */