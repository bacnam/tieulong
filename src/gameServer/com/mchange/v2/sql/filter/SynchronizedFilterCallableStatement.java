/*      */ package com.mchange.v2.sql.filter;
/*      */ 
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.math.BigDecimal;
/*      */ import java.net.URL;
/*      */ import java.sql.Array;
/*      */ import java.sql.Blob;
/*      */ import java.sql.CallableStatement;
/*      */ import java.sql.Clob;
/*      */ import java.sql.Connection;
/*      */ import java.sql.Date;
/*      */ import java.sql.NClob;
/*      */ import java.sql.ParameterMetaData;
/*      */ import java.sql.Ref;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.ResultSetMetaData;
/*      */ import java.sql.RowId;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.SQLXML;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class SynchronizedFilterCallableStatement
/*      */   implements CallableStatement
/*      */ {
/*      */   protected CallableStatement inner;
/*      */   
/*      */   private void __setInner(CallableStatement paramCallableStatement) {
/*   75 */     this.inner = paramCallableStatement;
/*      */   }
/*      */   
/*      */   public SynchronizedFilterCallableStatement(CallableStatement paramCallableStatement) {
/*   79 */     __setInner(paramCallableStatement);
/*      */   }
/*      */   
/*      */   public SynchronizedFilterCallableStatement() {}
/*      */   
/*      */   public synchronized void setInner(CallableStatement paramCallableStatement) {
/*   85 */     __setInner(paramCallableStatement);
/*      */   }
/*      */   public synchronized CallableStatement getInner() {
/*   88 */     return this.inner;
/*      */   }
/*      */   
/*      */   public synchronized BigDecimal getBigDecimal(int paramInt1, int paramInt2) throws SQLException {
/*   92 */     return this.inner.getBigDecimal(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized BigDecimal getBigDecimal(String paramString) throws SQLException {
/*   97 */     return this.inner.getBigDecimal(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized BigDecimal getBigDecimal(int paramInt) throws SQLException {
/*  102 */     return this.inner.getBigDecimal(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Blob getBlob(int paramInt) throws SQLException {
/*  107 */     return this.inner.getBlob(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Blob getBlob(String paramString) throws SQLException {
/*  112 */     return this.inner.getBlob(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Reader getCharacterStream(int paramInt) throws SQLException {
/*  117 */     return this.inner.getCharacterStream(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Reader getCharacterStream(String paramString) throws SQLException {
/*  122 */     return this.inner.getCharacterStream(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Clob getClob(int paramInt) throws SQLException {
/*  127 */     return this.inner.getClob(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Clob getClob(String paramString) throws SQLException {
/*  132 */     return this.inner.getClob(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Reader getNCharacterStream(int paramInt) throws SQLException {
/*  137 */     return this.inner.getNCharacterStream(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Reader getNCharacterStream(String paramString) throws SQLException {
/*  142 */     return this.inner.getNCharacterStream(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized NClob getNClob(String paramString) throws SQLException {
/*  147 */     return this.inner.getNClob(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized NClob getNClob(int paramInt) throws SQLException {
/*  152 */     return this.inner.getNClob(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized String getNString(int paramInt) throws SQLException {
/*  157 */     return this.inner.getNString(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized String getNString(String paramString) throws SQLException {
/*  162 */     return this.inner.getNString(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized RowId getRowId(String paramString) throws SQLException {
/*  167 */     return this.inner.getRowId(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized RowId getRowId(int paramInt) throws SQLException {
/*  172 */     return this.inner.getRowId(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized SQLXML getSQLXML(String paramString) throws SQLException {
/*  177 */     return this.inner.getSQLXML(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized SQLXML getSQLXML(int paramInt) throws SQLException {
/*  182 */     return this.inner.getSQLXML(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean wasNull() throws SQLException {
/*  187 */     return this.inner.wasNull();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void registerOutParameter(String paramString1, int paramInt, String paramString2) throws SQLException {
/*  192 */     this.inner.registerOutParameter(paramString1, paramInt, paramString2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void registerOutParameter(int paramInt1, int paramInt2) throws SQLException {
/*  197 */     this.inner.registerOutParameter(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void registerOutParameter(int paramInt1, int paramInt2, String paramString) throws SQLException {
/*  202 */     this.inner.registerOutParameter(paramInt1, paramInt2, paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void registerOutParameter(String paramString, int paramInt1, int paramInt2) throws SQLException {
/*  207 */     this.inner.registerOutParameter(paramString, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void registerOutParameter(String paramString, int paramInt) throws SQLException {
/*  212 */     this.inner.registerOutParameter(paramString, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void registerOutParameter(int paramInt1, int paramInt2, int paramInt3) throws SQLException {
/*  217 */     this.inner.registerOutParameter(paramInt1, paramInt2, paramInt3);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setAsciiStream(String paramString, InputStream paramInputStream) throws SQLException {
/*  222 */     this.inner.setAsciiStream(paramString, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setAsciiStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
/*  227 */     this.inner.setAsciiStream(paramString, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setAsciiStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
/*  232 */     this.inner.setAsciiStream(paramString, paramInputStream, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setBigDecimal(String paramString, BigDecimal paramBigDecimal) throws SQLException {
/*  237 */     this.inner.setBigDecimal(paramString, paramBigDecimal);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setBinaryStream(String paramString, InputStream paramInputStream) throws SQLException {
/*  242 */     this.inner.setBinaryStream(paramString, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setBinaryStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
/*  247 */     this.inner.setBinaryStream(paramString, paramInputStream, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setBinaryStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
/*  252 */     this.inner.setBinaryStream(paramString, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setBlob(String paramString, Blob paramBlob) throws SQLException {
/*  257 */     this.inner.setBlob(paramString, paramBlob);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setBlob(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
/*  262 */     this.inner.setBlob(paramString, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setBlob(String paramString, InputStream paramInputStream) throws SQLException {
/*  267 */     this.inner.setBlob(paramString, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setBytes(String paramString, byte[] paramArrayOfbyte) throws SQLException {
/*  272 */     this.inner.setBytes(paramString, paramArrayOfbyte);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException {
/*  277 */     this.inner.setCharacterStream(paramString, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setCharacterStream(String paramString, Reader paramReader) throws SQLException {
/*  282 */     this.inner.setCharacterStream(paramString, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setCharacterStream(String paramString, Reader paramReader, int paramInt) throws SQLException {
/*  287 */     this.inner.setCharacterStream(paramString, paramReader, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setClob(String paramString, Reader paramReader) throws SQLException {
/*  292 */     this.inner.setClob(paramString, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setClob(String paramString, Clob paramClob) throws SQLException {
/*  297 */     this.inner.setClob(paramString, paramClob);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setClob(String paramString, Reader paramReader, long paramLong) throws SQLException {
/*  302 */     this.inner.setClob(paramString, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setDate(String paramString, Date paramDate, Calendar paramCalendar) throws SQLException {
/*  307 */     this.inner.setDate(paramString, paramDate, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setDate(String paramString, Date paramDate) throws SQLException {
/*  312 */     this.inner.setDate(paramString, paramDate);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setNCharacterStream(String paramString, Reader paramReader) throws SQLException {
/*  317 */     this.inner.setNCharacterStream(paramString, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setNCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException {
/*  322 */     this.inner.setNCharacterStream(paramString, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setNClob(String paramString, Reader paramReader, long paramLong) throws SQLException {
/*  327 */     this.inner.setNClob(paramString, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setNClob(String paramString, NClob paramNClob) throws SQLException {
/*  332 */     this.inner.setNClob(paramString, paramNClob);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setNClob(String paramString, Reader paramReader) throws SQLException {
/*  337 */     this.inner.setNClob(paramString, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setNString(String paramString1, String paramString2) throws SQLException {
/*  342 */     this.inner.setNString(paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setNull(String paramString1, int paramInt, String paramString2) throws SQLException {
/*  347 */     this.inner.setNull(paramString1, paramInt, paramString2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setNull(String paramString, int paramInt) throws SQLException {
/*  352 */     this.inner.setNull(paramString, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setObject(String paramString, Object paramObject) throws SQLException {
/*  357 */     this.inner.setObject(paramString, paramObject);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setObject(String paramString, Object paramObject, int paramInt1, int paramInt2) throws SQLException {
/*  362 */     this.inner.setObject(paramString, paramObject, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setObject(String paramString, Object paramObject, int paramInt) throws SQLException {
/*  367 */     this.inner.setObject(paramString, paramObject, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setRowId(String paramString, RowId paramRowId) throws SQLException {
/*  372 */     this.inner.setRowId(paramString, paramRowId);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setSQLXML(String paramString, SQLXML paramSQLXML) throws SQLException {
/*  377 */     this.inner.setSQLXML(paramString, paramSQLXML);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setString(String paramString1, String paramString2) throws SQLException {
/*  382 */     this.inner.setString(paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Object getObject(String paramString) throws SQLException {
/*  387 */     return this.inner.getObject(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Object getObject(String paramString, Map<String, Class<?>> paramMap) throws SQLException {
/*  392 */     return this.inner.getObject(paramString, paramMap);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Object getObject(int paramInt, Class<?> paramClass) throws SQLException {
/*  397 */     return this.inner.getObject(paramInt, paramClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Object getObject(int paramInt) throws SQLException {
/*  402 */     return this.inner.getObject(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Object getObject(int paramInt, Map<String, Class<?>> paramMap) throws SQLException {
/*  407 */     return this.inner.getObject(paramInt, paramMap);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Object getObject(String paramString, Class<?> paramClass) throws SQLException {
/*  412 */     return this.inner.getObject(paramString, paramClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean getBoolean(String paramString) throws SQLException {
/*  417 */     return this.inner.getBoolean(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean getBoolean(int paramInt) throws SQLException {
/*  422 */     return this.inner.getBoolean(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized byte getByte(String paramString) throws SQLException {
/*  427 */     return this.inner.getByte(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized byte getByte(int paramInt) throws SQLException {
/*  432 */     return this.inner.getByte(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized short getShort(String paramString) throws SQLException {
/*  437 */     return this.inner.getShort(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized short getShort(int paramInt) throws SQLException {
/*  442 */     return this.inner.getShort(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getInt(int paramInt) throws SQLException {
/*  447 */     return this.inner.getInt(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getInt(String paramString) throws SQLException {
/*  452 */     return this.inner.getInt(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized long getLong(int paramInt) throws SQLException {
/*  457 */     return this.inner.getLong(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized long getLong(String paramString) throws SQLException {
/*  462 */     return this.inner.getLong(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized float getFloat(int paramInt) throws SQLException {
/*  467 */     return this.inner.getFloat(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized float getFloat(String paramString) throws SQLException {
/*  472 */     return this.inner.getFloat(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized double getDouble(String paramString) throws SQLException {
/*  477 */     return this.inner.getDouble(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized double getDouble(int paramInt) throws SQLException {
/*  482 */     return this.inner.getDouble(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized byte[] getBytes(int paramInt) throws SQLException {
/*  487 */     return this.inner.getBytes(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized byte[] getBytes(String paramString) throws SQLException {
/*  492 */     return this.inner.getBytes(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Array getArray(int paramInt) throws SQLException {
/*  497 */     return this.inner.getArray(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Array getArray(String paramString) throws SQLException {
/*  502 */     return this.inner.getArray(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized URL getURL(int paramInt) throws SQLException {
/*  507 */     return this.inner.getURL(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized URL getURL(String paramString) throws SQLException {
/*  512 */     return this.inner.getURL(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setBoolean(String paramString, boolean paramBoolean) throws SQLException {
/*  517 */     this.inner.setBoolean(paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setByte(String paramString, byte paramByte) throws SQLException {
/*  522 */     this.inner.setByte(paramString, paramByte);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setDouble(String paramString, double paramDouble) throws SQLException {
/*  527 */     this.inner.setDouble(paramString, paramDouble);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setFloat(String paramString, float paramFloat) throws SQLException {
/*  532 */     this.inner.setFloat(paramString, paramFloat);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setInt(String paramString, int paramInt) throws SQLException {
/*  537 */     this.inner.setInt(paramString, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setLong(String paramString, long paramLong) throws SQLException {
/*  542 */     this.inner.setLong(paramString, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setShort(String paramString, short paramShort) throws SQLException {
/*  547 */     this.inner.setShort(paramString, paramShort);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setTimestamp(String paramString, Timestamp paramTimestamp) throws SQLException {
/*  552 */     this.inner.setTimestamp(paramString, paramTimestamp);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setTimestamp(String paramString, Timestamp paramTimestamp, Calendar paramCalendar) throws SQLException {
/*  557 */     this.inner.setTimestamp(paramString, paramTimestamp, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Ref getRef(int paramInt) throws SQLException {
/*  562 */     return this.inner.getRef(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Ref getRef(String paramString) throws SQLException {
/*  567 */     return this.inner.getRef(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized String getString(String paramString) throws SQLException {
/*  572 */     return this.inner.getString(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized String getString(int paramInt) throws SQLException {
/*  577 */     return this.inner.getString(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setURL(String paramString, URL paramURL) throws SQLException {
/*  582 */     this.inner.setURL(paramString, paramURL);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Date getDate(int paramInt, Calendar paramCalendar) throws SQLException {
/*  587 */     return this.inner.getDate(paramInt, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Date getDate(int paramInt) throws SQLException {
/*  592 */     return this.inner.getDate(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Date getDate(String paramString, Calendar paramCalendar) throws SQLException {
/*  597 */     return this.inner.getDate(paramString, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Date getDate(String paramString) throws SQLException {
/*  602 */     return this.inner.getDate(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Time getTime(String paramString) throws SQLException {
/*  607 */     return this.inner.getTime(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Time getTime(int paramInt) throws SQLException {
/*  612 */     return this.inner.getTime(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Time getTime(String paramString, Calendar paramCalendar) throws SQLException {
/*  617 */     return this.inner.getTime(paramString, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Time getTime(int paramInt, Calendar paramCalendar) throws SQLException {
/*  622 */     return this.inner.getTime(paramInt, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setTime(String paramString, Time paramTime, Calendar paramCalendar) throws SQLException {
/*  627 */     this.inner.setTime(paramString, paramTime, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setTime(String paramString, Time paramTime) throws SQLException {
/*  632 */     this.inner.setTime(paramString, paramTime);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Timestamp getTimestamp(String paramString, Calendar paramCalendar) throws SQLException {
/*  637 */     return this.inner.getTimestamp(paramString, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Timestamp getTimestamp(int paramInt) throws SQLException {
/*  642 */     return this.inner.getTimestamp(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Timestamp getTimestamp(String paramString) throws SQLException {
/*  647 */     return this.inner.getTimestamp(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Timestamp getTimestamp(int paramInt, Calendar paramCalendar) throws SQLException {
/*  652 */     return this.inner.getTimestamp(paramInt, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean execute() throws SQLException {
/*  657 */     return this.inner.execute();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized ResultSetMetaData getMetaData() throws SQLException {
/*  662 */     return this.inner.getMetaData();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setArray(int paramInt, Array paramArray) throws SQLException {
/*  667 */     this.inner.setArray(paramInt, paramArray);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void addBatch() throws SQLException {
/*  672 */     this.inner.addBatch();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized ResultSet executeQuery() throws SQLException {
/*  677 */     return this.inner.executeQuery();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int executeUpdate() throws SQLException {
/*  682 */     return this.inner.executeUpdate();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void clearParameters() throws SQLException {
/*  687 */     this.inner.clearParameters();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized ParameterMetaData getParameterMetaData() throws SQLException {
/*  692 */     return this.inner.getParameterMetaData();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setAsciiStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
/*  697 */     this.inner.setAsciiStream(paramInt, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setAsciiStream(int paramInt, InputStream paramInputStream) throws SQLException {
/*  702 */     this.inner.setAsciiStream(paramInt, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
/*  707 */     this.inner.setAsciiStream(paramInt1, paramInputStream, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setBigDecimal(int paramInt, BigDecimal paramBigDecimal) throws SQLException {
/*  712 */     this.inner.setBigDecimal(paramInt, paramBigDecimal);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
/*  717 */     this.inner.setBinaryStream(paramInt1, paramInputStream, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setBinaryStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
/*  722 */     this.inner.setBinaryStream(paramInt, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setBinaryStream(int paramInt, InputStream paramInputStream) throws SQLException {
/*  727 */     this.inner.setBinaryStream(paramInt, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setBlob(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
/*  732 */     this.inner.setBlob(paramInt, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setBlob(int paramInt, Blob paramBlob) throws SQLException {
/*  737 */     this.inner.setBlob(paramInt, paramBlob);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setBlob(int paramInt, InputStream paramInputStream) throws SQLException {
/*  742 */     this.inner.setBlob(paramInt, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setBytes(int paramInt, byte[] paramArrayOfbyte) throws SQLException {
/*  747 */     this.inner.setBytes(paramInt, paramArrayOfbyte);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setCharacterStream(int paramInt, Reader paramReader) throws SQLException {
/*  752 */     this.inner.setCharacterStream(paramInt, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setCharacterStream(int paramInt1, Reader paramReader, int paramInt2) throws SQLException {
/*  757 */     this.inner.setCharacterStream(paramInt1, paramReader, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
/*  762 */     this.inner.setCharacterStream(paramInt, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
/*  767 */     this.inner.setClob(paramInt, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setClob(int paramInt, Reader paramReader) throws SQLException {
/*  772 */     this.inner.setClob(paramInt, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setClob(int paramInt, Clob paramClob) throws SQLException {
/*  777 */     this.inner.setClob(paramInt, paramClob);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setDate(int paramInt, Date paramDate) throws SQLException {
/*  782 */     this.inner.setDate(paramInt, paramDate);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setDate(int paramInt, Date paramDate, Calendar paramCalendar) throws SQLException {
/*  787 */     this.inner.setDate(paramInt, paramDate, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setNCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
/*  792 */     this.inner.setNCharacterStream(paramInt, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setNCharacterStream(int paramInt, Reader paramReader) throws SQLException {
/*  797 */     this.inner.setNCharacterStream(paramInt, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setNClob(int paramInt, Reader paramReader) throws SQLException {
/*  802 */     this.inner.setNClob(paramInt, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setNClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
/*  807 */     this.inner.setNClob(paramInt, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setNClob(int paramInt, NClob paramNClob) throws SQLException {
/*  812 */     this.inner.setNClob(paramInt, paramNClob);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setNString(int paramInt, String paramString) throws SQLException {
/*  817 */     this.inner.setNString(paramInt, paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setNull(int paramInt1, int paramInt2) throws SQLException {
/*  822 */     this.inner.setNull(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setNull(int paramInt1, int paramInt2, String paramString) throws SQLException {
/*  827 */     this.inner.setNull(paramInt1, paramInt2, paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setObject(int paramInt1, Object paramObject, int paramInt2) throws SQLException {
/*  832 */     this.inner.setObject(paramInt1, paramObject, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setObject(int paramInt1, Object paramObject, int paramInt2, int paramInt3) throws SQLException {
/*  837 */     this.inner.setObject(paramInt1, paramObject, paramInt2, paramInt3);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setObject(int paramInt, Object paramObject) throws SQLException {
/*  842 */     this.inner.setObject(paramInt, paramObject);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setRef(int paramInt, Ref paramRef) throws SQLException {
/*  847 */     this.inner.setRef(paramInt, paramRef);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setRowId(int paramInt, RowId paramRowId) throws SQLException {
/*  852 */     this.inner.setRowId(paramInt, paramRowId);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setSQLXML(int paramInt, SQLXML paramSQLXML) throws SQLException {
/*  857 */     this.inner.setSQLXML(paramInt, paramSQLXML);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setString(int paramInt, String paramString) throws SQLException {
/*  862 */     this.inner.setString(paramInt, paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setUnicodeStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
/*  867 */     this.inner.setUnicodeStream(paramInt1, paramInputStream, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setBoolean(int paramInt, boolean paramBoolean) throws SQLException {
/*  872 */     this.inner.setBoolean(paramInt, paramBoolean);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setByte(int paramInt, byte paramByte) throws SQLException {
/*  877 */     this.inner.setByte(paramInt, paramByte);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setDouble(int paramInt, double paramDouble) throws SQLException {
/*  882 */     this.inner.setDouble(paramInt, paramDouble);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setFloat(int paramInt, float paramFloat) throws SQLException {
/*  887 */     this.inner.setFloat(paramInt, paramFloat);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setInt(int paramInt1, int paramInt2) throws SQLException {
/*  892 */     this.inner.setInt(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setLong(int paramInt, long paramLong) throws SQLException {
/*  897 */     this.inner.setLong(paramInt, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setShort(int paramInt, short paramShort) throws SQLException {
/*  902 */     this.inner.setShort(paramInt, paramShort);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setTimestamp(int paramInt, Timestamp paramTimestamp, Calendar paramCalendar) throws SQLException {
/*  907 */     this.inner.setTimestamp(paramInt, paramTimestamp, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setTimestamp(int paramInt, Timestamp paramTimestamp) throws SQLException {
/*  912 */     this.inner.setTimestamp(paramInt, paramTimestamp);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setURL(int paramInt, URL paramURL) throws SQLException {
/*  917 */     this.inner.setURL(paramInt, paramURL);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setTime(int paramInt, Time paramTime, Calendar paramCalendar) throws SQLException {
/*  922 */     this.inner.setTime(paramInt, paramTime, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setTime(int paramInt, Time paramTime) throws SQLException {
/*  927 */     this.inner.setTime(paramInt, paramTime);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean execute(String paramString, int paramInt) throws SQLException {
/*  932 */     return this.inner.execute(paramString, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean execute(String paramString, String[] paramArrayOfString) throws SQLException {
/*  937 */     return this.inner.execute(paramString, paramArrayOfString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean execute(String paramString) throws SQLException {
/*  942 */     return this.inner.execute(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean execute(String paramString, int[] paramArrayOfint) throws SQLException {
/*  947 */     return this.inner.execute(paramString, paramArrayOfint);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void clearWarnings() throws SQLException {
/*  952 */     this.inner.clearWarnings();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized SQLWarning getWarnings() throws SQLException {
/*  957 */     return this.inner.getWarnings();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean isClosed() throws SQLException {
/*  962 */     return this.inner.isClosed();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getFetchDirection() throws SQLException {
/*  967 */     return this.inner.getFetchDirection();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getFetchSize() throws SQLException {
/*  972 */     return this.inner.getFetchSize();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setFetchDirection(int paramInt) throws SQLException {
/*  977 */     this.inner.setFetchDirection(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setFetchSize(int paramInt) throws SQLException {
/*  982 */     this.inner.setFetchSize(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Connection getConnection() throws SQLException {
/*  987 */     return this.inner.getConnection();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getResultSetHoldability() throws SQLException {
/*  992 */     return this.inner.getResultSetHoldability();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void addBatch(String paramString) throws SQLException {
/*  997 */     this.inner.addBatch(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void cancel() throws SQLException {
/* 1002 */     this.inner.cancel();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void clearBatch() throws SQLException {
/* 1007 */     this.inner.clearBatch();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void closeOnCompletion() throws SQLException {
/* 1012 */     this.inner.closeOnCompletion();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int[] executeBatch() throws SQLException {
/* 1017 */     return this.inner.executeBatch();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized ResultSet executeQuery(String paramString) throws SQLException {
/* 1022 */     return this.inner.executeQuery(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int executeUpdate(String paramString, int[] paramArrayOfint) throws SQLException {
/* 1027 */     return this.inner.executeUpdate(paramString, paramArrayOfint);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int executeUpdate(String paramString, String[] paramArrayOfString) throws SQLException {
/* 1032 */     return this.inner.executeUpdate(paramString, paramArrayOfString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int executeUpdate(String paramString) throws SQLException {
/* 1037 */     return this.inner.executeUpdate(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int executeUpdate(String paramString, int paramInt) throws SQLException {
/* 1042 */     return this.inner.executeUpdate(paramString, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized ResultSet getGeneratedKeys() throws SQLException {
/* 1047 */     return this.inner.getGeneratedKeys();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getMaxFieldSize() throws SQLException {
/* 1052 */     return this.inner.getMaxFieldSize();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getMaxRows() throws SQLException {
/* 1057 */     return this.inner.getMaxRows();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean getMoreResults() throws SQLException {
/* 1062 */     return this.inner.getMoreResults();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean getMoreResults(int paramInt) throws SQLException {
/* 1067 */     return this.inner.getMoreResults(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getQueryTimeout() throws SQLException {
/* 1072 */     return this.inner.getQueryTimeout();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized ResultSet getResultSet() throws SQLException {
/* 1077 */     return this.inner.getResultSet();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getResultSetConcurrency() throws SQLException {
/* 1082 */     return this.inner.getResultSetConcurrency();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getResultSetType() throws SQLException {
/* 1087 */     return this.inner.getResultSetType();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getUpdateCount() throws SQLException {
/* 1092 */     return this.inner.getUpdateCount();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean isCloseOnCompletion() throws SQLException {
/* 1097 */     return this.inner.isCloseOnCompletion();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean isPoolable() throws SQLException {
/* 1102 */     return this.inner.isPoolable();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setCursorName(String paramString) throws SQLException {
/* 1107 */     this.inner.setCursorName(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setEscapeProcessing(boolean paramBoolean) throws SQLException {
/* 1112 */     this.inner.setEscapeProcessing(paramBoolean);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setMaxFieldSize(int paramInt) throws SQLException {
/* 1117 */     this.inner.setMaxFieldSize(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setMaxRows(int paramInt) throws SQLException {
/* 1122 */     this.inner.setMaxRows(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setPoolable(boolean paramBoolean) throws SQLException {
/* 1127 */     this.inner.setPoolable(paramBoolean);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setQueryTimeout(int paramInt) throws SQLException {
/* 1132 */     this.inner.setQueryTimeout(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void close() throws SQLException {
/* 1137 */     this.inner.close();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean isWrapperFor(Class<?> paramClass) throws SQLException {
/* 1142 */     return this.inner.isWrapperFor(paramClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Object unwrap(Class<?> paramClass) throws SQLException {
/* 1147 */     return this.inner.unwrap(paramClass);
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/sql/filter/SynchronizedFilterCallableStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */