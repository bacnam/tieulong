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
/*      */ public abstract class FilterCallableStatement
/*      */   implements CallableStatement
/*      */ {
/*      */   protected CallableStatement inner;
/*      */   
/*      */   private void __setInner(CallableStatement paramCallableStatement) {
/*   75 */     this.inner = paramCallableStatement;
/*      */   }
/*      */   
/*      */   public FilterCallableStatement(CallableStatement paramCallableStatement) {
/*   79 */     __setInner(paramCallableStatement);
/*      */   }
/*      */   
/*      */   public FilterCallableStatement() {}
/*      */   
/*      */   public void setInner(CallableStatement paramCallableStatement) {
/*   85 */     __setInner(paramCallableStatement);
/*      */   }
/*      */   public CallableStatement getInner() {
/*   88 */     return this.inner;
/*      */   }
/*      */   
/*      */   public BigDecimal getBigDecimal(int paramInt1, int paramInt2) throws SQLException {
/*   92 */     return this.inner.getBigDecimal(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public BigDecimal getBigDecimal(String paramString) throws SQLException {
/*   97 */     return this.inner.getBigDecimal(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public BigDecimal getBigDecimal(int paramInt) throws SQLException {
/*  102 */     return this.inner.getBigDecimal(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Blob getBlob(int paramInt) throws SQLException {
/*  107 */     return this.inner.getBlob(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Blob getBlob(String paramString) throws SQLException {
/*  112 */     return this.inner.getBlob(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public Reader getCharacterStream(int paramInt) throws SQLException {
/*  117 */     return this.inner.getCharacterStream(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Reader getCharacterStream(String paramString) throws SQLException {
/*  122 */     return this.inner.getCharacterStream(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public Clob getClob(int paramInt) throws SQLException {
/*  127 */     return this.inner.getClob(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Clob getClob(String paramString) throws SQLException {
/*  132 */     return this.inner.getClob(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public Reader getNCharacterStream(int paramInt) throws SQLException {
/*  137 */     return this.inner.getNCharacterStream(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Reader getNCharacterStream(String paramString) throws SQLException {
/*  142 */     return this.inner.getNCharacterStream(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public NClob getNClob(String paramString) throws SQLException {
/*  147 */     return this.inner.getNClob(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public NClob getNClob(int paramInt) throws SQLException {
/*  152 */     return this.inner.getNClob(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getNString(int paramInt) throws SQLException {
/*  157 */     return this.inner.getNString(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getNString(String paramString) throws SQLException {
/*  162 */     return this.inner.getNString(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public RowId getRowId(String paramString) throws SQLException {
/*  167 */     return this.inner.getRowId(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public RowId getRowId(int paramInt) throws SQLException {
/*  172 */     return this.inner.getRowId(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public SQLXML getSQLXML(String paramString) throws SQLException {
/*  177 */     return this.inner.getSQLXML(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public SQLXML getSQLXML(int paramInt) throws SQLException {
/*  182 */     return this.inner.getSQLXML(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean wasNull() throws SQLException {
/*  187 */     return this.inner.wasNull();
/*      */   }
/*      */ 
/*      */   
/*      */   public void registerOutParameter(String paramString1, int paramInt, String paramString2) throws SQLException {
/*  192 */     this.inner.registerOutParameter(paramString1, paramInt, paramString2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void registerOutParameter(int paramInt1, int paramInt2) throws SQLException {
/*  197 */     this.inner.registerOutParameter(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void registerOutParameter(int paramInt1, int paramInt2, String paramString) throws SQLException {
/*  202 */     this.inner.registerOutParameter(paramInt1, paramInt2, paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public void registerOutParameter(String paramString, int paramInt1, int paramInt2) throws SQLException {
/*  207 */     this.inner.registerOutParameter(paramString, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void registerOutParameter(String paramString, int paramInt) throws SQLException {
/*  212 */     this.inner.registerOutParameter(paramString, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public void registerOutParameter(int paramInt1, int paramInt2, int paramInt3) throws SQLException {
/*  217 */     this.inner.registerOutParameter(paramInt1, paramInt2, paramInt3);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAsciiStream(String paramString, InputStream paramInputStream) throws SQLException {
/*  222 */     this.inner.setAsciiStream(paramString, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAsciiStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
/*  227 */     this.inner.setAsciiStream(paramString, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAsciiStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
/*  232 */     this.inner.setAsciiStream(paramString, paramInputStream, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBigDecimal(String paramString, BigDecimal paramBigDecimal) throws SQLException {
/*  237 */     this.inner.setBigDecimal(paramString, paramBigDecimal);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBinaryStream(String paramString, InputStream paramInputStream) throws SQLException {
/*  242 */     this.inner.setBinaryStream(paramString, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBinaryStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
/*  247 */     this.inner.setBinaryStream(paramString, paramInputStream, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBinaryStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
/*  252 */     this.inner.setBinaryStream(paramString, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBlob(String paramString, Blob paramBlob) throws SQLException {
/*  257 */     this.inner.setBlob(paramString, paramBlob);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBlob(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
/*  262 */     this.inner.setBlob(paramString, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBlob(String paramString, InputStream paramInputStream) throws SQLException {
/*  267 */     this.inner.setBlob(paramString, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBytes(String paramString, byte[] paramArrayOfbyte) throws SQLException {
/*  272 */     this.inner.setBytes(paramString, paramArrayOfbyte);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException {
/*  277 */     this.inner.setCharacterStream(paramString, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCharacterStream(String paramString, Reader paramReader) throws SQLException {
/*  282 */     this.inner.setCharacterStream(paramString, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCharacterStream(String paramString, Reader paramReader, int paramInt) throws SQLException {
/*  287 */     this.inner.setCharacterStream(paramString, paramReader, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setClob(String paramString, Reader paramReader) throws SQLException {
/*  292 */     this.inner.setClob(paramString, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setClob(String paramString, Clob paramClob) throws SQLException {
/*  297 */     this.inner.setClob(paramString, paramClob);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setClob(String paramString, Reader paramReader, long paramLong) throws SQLException {
/*  302 */     this.inner.setClob(paramString, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDate(String paramString, Date paramDate, Calendar paramCalendar) throws SQLException {
/*  307 */     this.inner.setDate(paramString, paramDate, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDate(String paramString, Date paramDate) throws SQLException {
/*  312 */     this.inner.setDate(paramString, paramDate);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNCharacterStream(String paramString, Reader paramReader) throws SQLException {
/*  317 */     this.inner.setNCharacterStream(paramString, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException {
/*  322 */     this.inner.setNCharacterStream(paramString, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNClob(String paramString, Reader paramReader, long paramLong) throws SQLException {
/*  327 */     this.inner.setNClob(paramString, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNClob(String paramString, NClob paramNClob) throws SQLException {
/*  332 */     this.inner.setNClob(paramString, paramNClob);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNClob(String paramString, Reader paramReader) throws SQLException {
/*  337 */     this.inner.setNClob(paramString, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNString(String paramString1, String paramString2) throws SQLException {
/*  342 */     this.inner.setNString(paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNull(String paramString1, int paramInt, String paramString2) throws SQLException {
/*  347 */     this.inner.setNull(paramString1, paramInt, paramString2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNull(String paramString, int paramInt) throws SQLException {
/*  352 */     this.inner.setNull(paramString, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setObject(String paramString, Object paramObject) throws SQLException {
/*  357 */     this.inner.setObject(paramString, paramObject);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setObject(String paramString, Object paramObject, int paramInt1, int paramInt2) throws SQLException {
/*  362 */     this.inner.setObject(paramString, paramObject, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setObject(String paramString, Object paramObject, int paramInt) throws SQLException {
/*  367 */     this.inner.setObject(paramString, paramObject, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRowId(String paramString, RowId paramRowId) throws SQLException {
/*  372 */     this.inner.setRowId(paramString, paramRowId);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSQLXML(String paramString, SQLXML paramSQLXML) throws SQLException {
/*  377 */     this.inner.setSQLXML(paramString, paramSQLXML);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setString(String paramString1, String paramString2) throws SQLException {
/*  382 */     this.inner.setString(paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getObject(String paramString) throws SQLException {
/*  387 */     return this.inner.getObject(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getObject(String paramString, Map<String, Class<?>> paramMap) throws SQLException {
/*  392 */     return this.inner.getObject(paramString, paramMap);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getObject(int paramInt, Class<?> paramClass) throws SQLException {
/*  397 */     return this.inner.getObject(paramInt, paramClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getObject(int paramInt) throws SQLException {
/*  402 */     return this.inner.getObject(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getObject(int paramInt, Map<String, Class<?>> paramMap) throws SQLException {
/*  407 */     return this.inner.getObject(paramInt, paramMap);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getObject(String paramString, Class<?> paramClass) throws SQLException {
/*  412 */     return this.inner.getObject(paramString, paramClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getBoolean(String paramString) throws SQLException {
/*  417 */     return this.inner.getBoolean(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getBoolean(int paramInt) throws SQLException {
/*  422 */     return this.inner.getBoolean(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getByte(String paramString) throws SQLException {
/*  427 */     return this.inner.getByte(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getByte(int paramInt) throws SQLException {
/*  432 */     return this.inner.getByte(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getShort(String paramString) throws SQLException {
/*  437 */     return this.inner.getShort(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getShort(int paramInt) throws SQLException {
/*  442 */     return this.inner.getShort(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getInt(int paramInt) throws SQLException {
/*  447 */     return this.inner.getInt(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getInt(String paramString) throws SQLException {
/*  452 */     return this.inner.getInt(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLong(int paramInt) throws SQLException {
/*  457 */     return this.inner.getLong(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLong(String paramString) throws SQLException {
/*  462 */     return this.inner.getLong(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFloat(int paramInt) throws SQLException {
/*  467 */     return this.inner.getFloat(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFloat(String paramString) throws SQLException {
/*  472 */     return this.inner.getFloat(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public double getDouble(String paramString) throws SQLException {
/*  477 */     return this.inner.getDouble(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public double getDouble(int paramInt) throws SQLException {
/*  482 */     return this.inner.getDouble(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] getBytes(int paramInt) throws SQLException {
/*  487 */     return this.inner.getBytes(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] getBytes(String paramString) throws SQLException {
/*  492 */     return this.inner.getBytes(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public Array getArray(int paramInt) throws SQLException {
/*  497 */     return this.inner.getArray(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Array getArray(String paramString) throws SQLException {
/*  502 */     return this.inner.getArray(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public URL getURL(int paramInt) throws SQLException {
/*  507 */     return this.inner.getURL(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public URL getURL(String paramString) throws SQLException {
/*  512 */     return this.inner.getURL(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBoolean(String paramString, boolean paramBoolean) throws SQLException {
/*  517 */     this.inner.setBoolean(paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setByte(String paramString, byte paramByte) throws SQLException {
/*  522 */     this.inner.setByte(paramString, paramByte);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDouble(String paramString, double paramDouble) throws SQLException {
/*  527 */     this.inner.setDouble(paramString, paramDouble);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setFloat(String paramString, float paramFloat) throws SQLException {
/*  532 */     this.inner.setFloat(paramString, paramFloat);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setInt(String paramString, int paramInt) throws SQLException {
/*  537 */     this.inner.setInt(paramString, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setLong(String paramString, long paramLong) throws SQLException {
/*  542 */     this.inner.setLong(paramString, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setShort(String paramString, short paramShort) throws SQLException {
/*  547 */     this.inner.setShort(paramString, paramShort);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTimestamp(String paramString, Timestamp paramTimestamp) throws SQLException {
/*  552 */     this.inner.setTimestamp(paramString, paramTimestamp);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTimestamp(String paramString, Timestamp paramTimestamp, Calendar paramCalendar) throws SQLException {
/*  557 */     this.inner.setTimestamp(paramString, paramTimestamp, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public Ref getRef(int paramInt) throws SQLException {
/*  562 */     return this.inner.getRef(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Ref getRef(String paramString) throws SQLException {
/*  567 */     return this.inner.getRef(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getString(String paramString) throws SQLException {
/*  572 */     return this.inner.getString(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getString(int paramInt) throws SQLException {
/*  577 */     return this.inner.getString(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setURL(String paramString, URL paramURL) throws SQLException {
/*  582 */     this.inner.setURL(paramString, paramURL);
/*      */   }
/*      */ 
/*      */   
/*      */   public Date getDate(int paramInt, Calendar paramCalendar) throws SQLException {
/*  587 */     return this.inner.getDate(paramInt, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public Date getDate(int paramInt) throws SQLException {
/*  592 */     return this.inner.getDate(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Date getDate(String paramString, Calendar paramCalendar) throws SQLException {
/*  597 */     return this.inner.getDate(paramString, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public Date getDate(String paramString) throws SQLException {
/*  602 */     return this.inner.getDate(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public Time getTime(String paramString) throws SQLException {
/*  607 */     return this.inner.getTime(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public Time getTime(int paramInt) throws SQLException {
/*  612 */     return this.inner.getTime(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Time getTime(String paramString, Calendar paramCalendar) throws SQLException {
/*  617 */     return this.inner.getTime(paramString, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public Time getTime(int paramInt, Calendar paramCalendar) throws SQLException {
/*  622 */     return this.inner.getTime(paramInt, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTime(String paramString, Time paramTime, Calendar paramCalendar) throws SQLException {
/*  627 */     this.inner.setTime(paramString, paramTime, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTime(String paramString, Time paramTime) throws SQLException {
/*  632 */     this.inner.setTime(paramString, paramTime);
/*      */   }
/*      */ 
/*      */   
/*      */   public Timestamp getTimestamp(String paramString, Calendar paramCalendar) throws SQLException {
/*  637 */     return this.inner.getTimestamp(paramString, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public Timestamp getTimestamp(int paramInt) throws SQLException {
/*  642 */     return this.inner.getTimestamp(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Timestamp getTimestamp(String paramString) throws SQLException {
/*  647 */     return this.inner.getTimestamp(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public Timestamp getTimestamp(int paramInt, Calendar paramCalendar) throws SQLException {
/*  652 */     return this.inner.getTimestamp(paramInt, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean execute() throws SQLException {
/*  657 */     return this.inner.execute();
/*      */   }
/*      */ 
/*      */   
/*      */   public ResultSetMetaData getMetaData() throws SQLException {
/*  662 */     return this.inner.getMetaData();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setArray(int paramInt, Array paramArray) throws SQLException {
/*  667 */     this.inner.setArray(paramInt, paramArray);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addBatch() throws SQLException {
/*  672 */     this.inner.addBatch();
/*      */   }
/*      */ 
/*      */   
/*      */   public ResultSet executeQuery() throws SQLException {
/*  677 */     return this.inner.executeQuery();
/*      */   }
/*      */ 
/*      */   
/*      */   public int executeUpdate() throws SQLException {
/*  682 */     return this.inner.executeUpdate();
/*      */   }
/*      */ 
/*      */   
/*      */   public void clearParameters() throws SQLException {
/*  687 */     this.inner.clearParameters();
/*      */   }
/*      */ 
/*      */   
/*      */   public ParameterMetaData getParameterMetaData() throws SQLException {
/*  692 */     return this.inner.getParameterMetaData();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAsciiStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
/*  697 */     this.inner.setAsciiStream(paramInt, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAsciiStream(int paramInt, InputStream paramInputStream) throws SQLException {
/*  702 */     this.inner.setAsciiStream(paramInt, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
/*  707 */     this.inner.setAsciiStream(paramInt1, paramInputStream, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBigDecimal(int paramInt, BigDecimal paramBigDecimal) throws SQLException {
/*  712 */     this.inner.setBigDecimal(paramInt, paramBigDecimal);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
/*  717 */     this.inner.setBinaryStream(paramInt1, paramInputStream, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBinaryStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
/*  722 */     this.inner.setBinaryStream(paramInt, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBinaryStream(int paramInt, InputStream paramInputStream) throws SQLException {
/*  727 */     this.inner.setBinaryStream(paramInt, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBlob(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
/*  732 */     this.inner.setBlob(paramInt, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBlob(int paramInt, Blob paramBlob) throws SQLException {
/*  737 */     this.inner.setBlob(paramInt, paramBlob);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBlob(int paramInt, InputStream paramInputStream) throws SQLException {
/*  742 */     this.inner.setBlob(paramInt, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBytes(int paramInt, byte[] paramArrayOfbyte) throws SQLException {
/*  747 */     this.inner.setBytes(paramInt, paramArrayOfbyte);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCharacterStream(int paramInt, Reader paramReader) throws SQLException {
/*  752 */     this.inner.setCharacterStream(paramInt, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCharacterStream(int paramInt1, Reader paramReader, int paramInt2) throws SQLException {
/*  757 */     this.inner.setCharacterStream(paramInt1, paramReader, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
/*  762 */     this.inner.setCharacterStream(paramInt, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
/*  767 */     this.inner.setClob(paramInt, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setClob(int paramInt, Reader paramReader) throws SQLException {
/*  772 */     this.inner.setClob(paramInt, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setClob(int paramInt, Clob paramClob) throws SQLException {
/*  777 */     this.inner.setClob(paramInt, paramClob);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDate(int paramInt, Date paramDate) throws SQLException {
/*  782 */     this.inner.setDate(paramInt, paramDate);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDate(int paramInt, Date paramDate, Calendar paramCalendar) throws SQLException {
/*  787 */     this.inner.setDate(paramInt, paramDate, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
/*  792 */     this.inner.setNCharacterStream(paramInt, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNCharacterStream(int paramInt, Reader paramReader) throws SQLException {
/*  797 */     this.inner.setNCharacterStream(paramInt, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNClob(int paramInt, Reader paramReader) throws SQLException {
/*  802 */     this.inner.setNClob(paramInt, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
/*  807 */     this.inner.setNClob(paramInt, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNClob(int paramInt, NClob paramNClob) throws SQLException {
/*  812 */     this.inner.setNClob(paramInt, paramNClob);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNString(int paramInt, String paramString) throws SQLException {
/*  817 */     this.inner.setNString(paramInt, paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNull(int paramInt1, int paramInt2) throws SQLException {
/*  822 */     this.inner.setNull(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNull(int paramInt1, int paramInt2, String paramString) throws SQLException {
/*  827 */     this.inner.setNull(paramInt1, paramInt2, paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setObject(int paramInt1, Object paramObject, int paramInt2) throws SQLException {
/*  832 */     this.inner.setObject(paramInt1, paramObject, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setObject(int paramInt1, Object paramObject, int paramInt2, int paramInt3) throws SQLException {
/*  837 */     this.inner.setObject(paramInt1, paramObject, paramInt2, paramInt3);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setObject(int paramInt, Object paramObject) throws SQLException {
/*  842 */     this.inner.setObject(paramInt, paramObject);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRef(int paramInt, Ref paramRef) throws SQLException {
/*  847 */     this.inner.setRef(paramInt, paramRef);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRowId(int paramInt, RowId paramRowId) throws SQLException {
/*  852 */     this.inner.setRowId(paramInt, paramRowId);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSQLXML(int paramInt, SQLXML paramSQLXML) throws SQLException {
/*  857 */     this.inner.setSQLXML(paramInt, paramSQLXML);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setString(int paramInt, String paramString) throws SQLException {
/*  862 */     this.inner.setString(paramInt, paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setUnicodeStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
/*  867 */     this.inner.setUnicodeStream(paramInt1, paramInputStream, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBoolean(int paramInt, boolean paramBoolean) throws SQLException {
/*  872 */     this.inner.setBoolean(paramInt, paramBoolean);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setByte(int paramInt, byte paramByte) throws SQLException {
/*  877 */     this.inner.setByte(paramInt, paramByte);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDouble(int paramInt, double paramDouble) throws SQLException {
/*  882 */     this.inner.setDouble(paramInt, paramDouble);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setFloat(int paramInt, float paramFloat) throws SQLException {
/*  887 */     this.inner.setFloat(paramInt, paramFloat);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setInt(int paramInt1, int paramInt2) throws SQLException {
/*  892 */     this.inner.setInt(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setLong(int paramInt, long paramLong) throws SQLException {
/*  897 */     this.inner.setLong(paramInt, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setShort(int paramInt, short paramShort) throws SQLException {
/*  902 */     this.inner.setShort(paramInt, paramShort);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTimestamp(int paramInt, Timestamp paramTimestamp, Calendar paramCalendar) throws SQLException {
/*  907 */     this.inner.setTimestamp(paramInt, paramTimestamp, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTimestamp(int paramInt, Timestamp paramTimestamp) throws SQLException {
/*  912 */     this.inner.setTimestamp(paramInt, paramTimestamp);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setURL(int paramInt, URL paramURL) throws SQLException {
/*  917 */     this.inner.setURL(paramInt, paramURL);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTime(int paramInt, Time paramTime, Calendar paramCalendar) throws SQLException {
/*  922 */     this.inner.setTime(paramInt, paramTime, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTime(int paramInt, Time paramTime) throws SQLException {
/*  927 */     this.inner.setTime(paramInt, paramTime);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean execute(String paramString, int paramInt) throws SQLException {
/*  932 */     return this.inner.execute(paramString, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean execute(String paramString, String[] paramArrayOfString) throws SQLException {
/*  937 */     return this.inner.execute(paramString, paramArrayOfString);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean execute(String paramString) throws SQLException {
/*  942 */     return this.inner.execute(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean execute(String paramString, int[] paramArrayOfint) throws SQLException {
/*  947 */     return this.inner.execute(paramString, paramArrayOfint);
/*      */   }
/*      */ 
/*      */   
/*      */   public void clearWarnings() throws SQLException {
/*  952 */     this.inner.clearWarnings();
/*      */   }
/*      */ 
/*      */   
/*      */   public SQLWarning getWarnings() throws SQLException {
/*  957 */     return this.inner.getWarnings();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isClosed() throws SQLException {
/*  962 */     return this.inner.isClosed();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getFetchDirection() throws SQLException {
/*  967 */     return this.inner.getFetchDirection();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getFetchSize() throws SQLException {
/*  972 */     return this.inner.getFetchSize();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setFetchDirection(int paramInt) throws SQLException {
/*  977 */     this.inner.setFetchDirection(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setFetchSize(int paramInt) throws SQLException {
/*  982 */     this.inner.setFetchSize(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Connection getConnection() throws SQLException {
/*  987 */     return this.inner.getConnection();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getResultSetHoldability() throws SQLException {
/*  992 */     return this.inner.getResultSetHoldability();
/*      */   }
/*      */ 
/*      */   
/*      */   public void addBatch(String paramString) throws SQLException {
/*  997 */     this.inner.addBatch(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public void cancel() throws SQLException {
/* 1002 */     this.inner.cancel();
/*      */   }
/*      */ 
/*      */   
/*      */   public void clearBatch() throws SQLException {
/* 1007 */     this.inner.clearBatch();
/*      */   }
/*      */ 
/*      */   
/*      */   public void closeOnCompletion() throws SQLException {
/* 1012 */     this.inner.closeOnCompletion();
/*      */   }
/*      */ 
/*      */   
/*      */   public int[] executeBatch() throws SQLException {
/* 1017 */     return this.inner.executeBatch();
/*      */   }
/*      */ 
/*      */   
/*      */   public ResultSet executeQuery(String paramString) throws SQLException {
/* 1022 */     return this.inner.executeQuery(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public int executeUpdate(String paramString, int[] paramArrayOfint) throws SQLException {
/* 1027 */     return this.inner.executeUpdate(paramString, paramArrayOfint);
/*      */   }
/*      */ 
/*      */   
/*      */   public int executeUpdate(String paramString, String[] paramArrayOfString) throws SQLException {
/* 1032 */     return this.inner.executeUpdate(paramString, paramArrayOfString);
/*      */   }
/*      */ 
/*      */   
/*      */   public int executeUpdate(String paramString) throws SQLException {
/* 1037 */     return this.inner.executeUpdate(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public int executeUpdate(String paramString, int paramInt) throws SQLException {
/* 1042 */     return this.inner.executeUpdate(paramString, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public ResultSet getGeneratedKeys() throws SQLException {
/* 1047 */     return this.inner.getGeneratedKeys();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxFieldSize() throws SQLException {
/* 1052 */     return this.inner.getMaxFieldSize();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxRows() throws SQLException {
/* 1057 */     return this.inner.getMaxRows();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getMoreResults() throws SQLException {
/* 1062 */     return this.inner.getMoreResults();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getMoreResults(int paramInt) throws SQLException {
/* 1067 */     return this.inner.getMoreResults(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getQueryTimeout() throws SQLException {
/* 1072 */     return this.inner.getQueryTimeout();
/*      */   }
/*      */ 
/*      */   
/*      */   public ResultSet getResultSet() throws SQLException {
/* 1077 */     return this.inner.getResultSet();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getResultSetConcurrency() throws SQLException {
/* 1082 */     return this.inner.getResultSetConcurrency();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getResultSetType() throws SQLException {
/* 1087 */     return this.inner.getResultSetType();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUpdateCount() throws SQLException {
/* 1092 */     return this.inner.getUpdateCount();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCloseOnCompletion() throws SQLException {
/* 1097 */     return this.inner.isCloseOnCompletion();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isPoolable() throws SQLException {
/* 1102 */     return this.inner.isPoolable();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCursorName(String paramString) throws SQLException {
/* 1107 */     this.inner.setCursorName(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setEscapeProcessing(boolean paramBoolean) throws SQLException {
/* 1112 */     this.inner.setEscapeProcessing(paramBoolean);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMaxFieldSize(int paramInt) throws SQLException {
/* 1117 */     this.inner.setMaxFieldSize(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMaxRows(int paramInt) throws SQLException {
/* 1122 */     this.inner.setMaxRows(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPoolable(boolean paramBoolean) throws SQLException {
/* 1127 */     this.inner.setPoolable(paramBoolean);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setQueryTimeout(int paramInt) throws SQLException {
/* 1132 */     this.inner.setQueryTimeout(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public void close() throws SQLException {
/* 1137 */     this.inner.close();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
/* 1142 */     return this.inner.isWrapperFor(paramClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object unwrap(Class<?> paramClass) throws SQLException {
/* 1147 */     return this.inner.unwrap(paramClass);
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/sql/filter/FilterCallableStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */