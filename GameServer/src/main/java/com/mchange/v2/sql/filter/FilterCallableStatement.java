package com.mchange.v2.sql.filter;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public abstract class FilterCallableStatement
implements CallableStatement
{
protected CallableStatement inner;

private void __setInner(CallableStatement paramCallableStatement) {
this.inner = paramCallableStatement;
}

public FilterCallableStatement(CallableStatement paramCallableStatement) {
__setInner(paramCallableStatement);
}

public FilterCallableStatement() {}

public void setInner(CallableStatement paramCallableStatement) {
__setInner(paramCallableStatement);
}
public CallableStatement getInner() {
return this.inner;
}

public BigDecimal getBigDecimal(int paramInt1, int paramInt2) throws SQLException {
return this.inner.getBigDecimal(paramInt1, paramInt2);
}

public BigDecimal getBigDecimal(String paramString) throws SQLException {
return this.inner.getBigDecimal(paramString);
}

public BigDecimal getBigDecimal(int paramInt) throws SQLException {
return this.inner.getBigDecimal(paramInt);
}

public Blob getBlob(int paramInt) throws SQLException {
return this.inner.getBlob(paramInt);
}

public Blob getBlob(String paramString) throws SQLException {
return this.inner.getBlob(paramString);
}

public Reader getCharacterStream(int paramInt) throws SQLException {
return this.inner.getCharacterStream(paramInt);
}

public Reader getCharacterStream(String paramString) throws SQLException {
return this.inner.getCharacterStream(paramString);
}

public Clob getClob(int paramInt) throws SQLException {
return this.inner.getClob(paramInt);
}

public Clob getClob(String paramString) throws SQLException {
return this.inner.getClob(paramString);
}

public Reader getNCharacterStream(int paramInt) throws SQLException {
return this.inner.getNCharacterStream(paramInt);
}

public Reader getNCharacterStream(String paramString) throws SQLException {
return this.inner.getNCharacterStream(paramString);
}

public NClob getNClob(String paramString) throws SQLException {
return this.inner.getNClob(paramString);
}

public NClob getNClob(int paramInt) throws SQLException {
return this.inner.getNClob(paramInt);
}

public String getNString(int paramInt) throws SQLException {
return this.inner.getNString(paramInt);
}

public String getNString(String paramString) throws SQLException {
return this.inner.getNString(paramString);
}

public RowId getRowId(String paramString) throws SQLException {
return this.inner.getRowId(paramString);
}

public RowId getRowId(int paramInt) throws SQLException {
return this.inner.getRowId(paramInt);
}

public SQLXML getSQLXML(String paramString) throws SQLException {
return this.inner.getSQLXML(paramString);
}

public SQLXML getSQLXML(int paramInt) throws SQLException {
return this.inner.getSQLXML(paramInt);
}

public boolean wasNull() throws SQLException {
return this.inner.wasNull();
}

public void registerOutParameter(String paramString1, int paramInt, String paramString2) throws SQLException {
this.inner.registerOutParameter(paramString1, paramInt, paramString2);
}

public void registerOutParameter(int paramInt1, int paramInt2) throws SQLException {
this.inner.registerOutParameter(paramInt1, paramInt2);
}

public void registerOutParameter(int paramInt1, int paramInt2, String paramString) throws SQLException {
this.inner.registerOutParameter(paramInt1, paramInt2, paramString);
}

public void registerOutParameter(String paramString, int paramInt1, int paramInt2) throws SQLException {
this.inner.registerOutParameter(paramString, paramInt1, paramInt2);
}

public void registerOutParameter(String paramString, int paramInt) throws SQLException {
this.inner.registerOutParameter(paramString, paramInt);
}

public void registerOutParameter(int paramInt1, int paramInt2, int paramInt3) throws SQLException {
this.inner.registerOutParameter(paramInt1, paramInt2, paramInt3);
}

public void setAsciiStream(String paramString, InputStream paramInputStream) throws SQLException {
this.inner.setAsciiStream(paramString, paramInputStream);
}

public void setAsciiStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
this.inner.setAsciiStream(paramString, paramInputStream, paramLong);
}

public void setAsciiStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
this.inner.setAsciiStream(paramString, paramInputStream, paramInt);
}

public void setBigDecimal(String paramString, BigDecimal paramBigDecimal) throws SQLException {
this.inner.setBigDecimal(paramString, paramBigDecimal);
}

public void setBinaryStream(String paramString, InputStream paramInputStream) throws SQLException {
this.inner.setBinaryStream(paramString, paramInputStream);
}

public void setBinaryStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
this.inner.setBinaryStream(paramString, paramInputStream, paramInt);
}

public void setBinaryStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
this.inner.setBinaryStream(paramString, paramInputStream, paramLong);
}

public void setBlob(String paramString, Blob paramBlob) throws SQLException {
this.inner.setBlob(paramString, paramBlob);
}

public void setBlob(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
this.inner.setBlob(paramString, paramInputStream, paramLong);
}

public void setBlob(String paramString, InputStream paramInputStream) throws SQLException {
this.inner.setBlob(paramString, paramInputStream);
}

public void setBytes(String paramString, byte[] paramArrayOfbyte) throws SQLException {
this.inner.setBytes(paramString, paramArrayOfbyte);
}

public void setCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException {
this.inner.setCharacterStream(paramString, paramReader, paramLong);
}

public void setCharacterStream(String paramString, Reader paramReader) throws SQLException {
this.inner.setCharacterStream(paramString, paramReader);
}

public void setCharacterStream(String paramString, Reader paramReader, int paramInt) throws SQLException {
this.inner.setCharacterStream(paramString, paramReader, paramInt);
}

public void setClob(String paramString, Reader paramReader) throws SQLException {
this.inner.setClob(paramString, paramReader);
}

public void setClob(String paramString, Clob paramClob) throws SQLException {
this.inner.setClob(paramString, paramClob);
}

public void setClob(String paramString, Reader paramReader, long paramLong) throws SQLException {
this.inner.setClob(paramString, paramReader, paramLong);
}

public void setDate(String paramString, Date paramDate, Calendar paramCalendar) throws SQLException {
this.inner.setDate(paramString, paramDate, paramCalendar);
}

public void setDate(String paramString, Date paramDate) throws SQLException {
this.inner.setDate(paramString, paramDate);
}

public void setNCharacterStream(String paramString, Reader paramReader) throws SQLException {
this.inner.setNCharacterStream(paramString, paramReader);
}

public void setNCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException {
this.inner.setNCharacterStream(paramString, paramReader, paramLong);
}

public void setNClob(String paramString, Reader paramReader, long paramLong) throws SQLException {
this.inner.setNClob(paramString, paramReader, paramLong);
}

public void setNClob(String paramString, NClob paramNClob) throws SQLException {
this.inner.setNClob(paramString, paramNClob);
}

public void setNClob(String paramString, Reader paramReader) throws SQLException {
this.inner.setNClob(paramString, paramReader);
}

public void setNString(String paramString1, String paramString2) throws SQLException {
this.inner.setNString(paramString1, paramString2);
}

public void setNull(String paramString1, int paramInt, String paramString2) throws SQLException {
this.inner.setNull(paramString1, paramInt, paramString2);
}

public void setNull(String paramString, int paramInt) throws SQLException {
this.inner.setNull(paramString, paramInt);
}

public void setObject(String paramString, Object paramObject) throws SQLException {
this.inner.setObject(paramString, paramObject);
}

public void setObject(String paramString, Object paramObject, int paramInt1, int paramInt2) throws SQLException {
this.inner.setObject(paramString, paramObject, paramInt1, paramInt2);
}

public void setObject(String paramString, Object paramObject, int paramInt) throws SQLException {
this.inner.setObject(paramString, paramObject, paramInt);
}

public void setRowId(String paramString, RowId paramRowId) throws SQLException {
this.inner.setRowId(paramString, paramRowId);
}

public void setSQLXML(String paramString, SQLXML paramSQLXML) throws SQLException {
this.inner.setSQLXML(paramString, paramSQLXML);
}

public void setString(String paramString1, String paramString2) throws SQLException {
this.inner.setString(paramString1, paramString2);
}

public Object getObject(String paramString) throws SQLException {
return this.inner.getObject(paramString);
}

public Object getObject(String paramString, Map<String, Class<?>> paramMap) throws SQLException {
return this.inner.getObject(paramString, paramMap);
}

public Object getObject(int paramInt, Class<?> paramClass) throws SQLException {
return this.inner.getObject(paramInt, paramClass);
}

public Object getObject(int paramInt) throws SQLException {
return this.inner.getObject(paramInt);
}

public Object getObject(int paramInt, Map<String, Class<?>> paramMap) throws SQLException {
return this.inner.getObject(paramInt, paramMap);
}

public Object getObject(String paramString, Class<?> paramClass) throws SQLException {
return this.inner.getObject(paramString, paramClass);
}

public boolean getBoolean(String paramString) throws SQLException {
return this.inner.getBoolean(paramString);
}

public boolean getBoolean(int paramInt) throws SQLException {
return this.inner.getBoolean(paramInt);
}

public byte getByte(String paramString) throws SQLException {
return this.inner.getByte(paramString);
}

public byte getByte(int paramInt) throws SQLException {
return this.inner.getByte(paramInt);
}

public short getShort(String paramString) throws SQLException {
return this.inner.getShort(paramString);
}

public short getShort(int paramInt) throws SQLException {
return this.inner.getShort(paramInt);
}

public int getInt(int paramInt) throws SQLException {
return this.inner.getInt(paramInt);
}

public int getInt(String paramString) throws SQLException {
return this.inner.getInt(paramString);
}

public long getLong(int paramInt) throws SQLException {
return this.inner.getLong(paramInt);
}

public long getLong(String paramString) throws SQLException {
return this.inner.getLong(paramString);
}

public float getFloat(int paramInt) throws SQLException {
return this.inner.getFloat(paramInt);
}

public float getFloat(String paramString) throws SQLException {
return this.inner.getFloat(paramString);
}

public double getDouble(String paramString) throws SQLException {
return this.inner.getDouble(paramString);
}

public double getDouble(int paramInt) throws SQLException {
return this.inner.getDouble(paramInt);
}

public byte[] getBytes(int paramInt) throws SQLException {
return this.inner.getBytes(paramInt);
}

public byte[] getBytes(String paramString) throws SQLException {
return this.inner.getBytes(paramString);
}

public Array getArray(int paramInt) throws SQLException {
return this.inner.getArray(paramInt);
}

public Array getArray(String paramString) throws SQLException {
return this.inner.getArray(paramString);
}

public URL getURL(int paramInt) throws SQLException {
return this.inner.getURL(paramInt);
}

public URL getURL(String paramString) throws SQLException {
return this.inner.getURL(paramString);
}

public void setBoolean(String paramString, boolean paramBoolean) throws SQLException {
this.inner.setBoolean(paramString, paramBoolean);
}

public void setByte(String paramString, byte paramByte) throws SQLException {
this.inner.setByte(paramString, paramByte);
}

public void setDouble(String paramString, double paramDouble) throws SQLException {
this.inner.setDouble(paramString, paramDouble);
}

public void setFloat(String paramString, float paramFloat) throws SQLException {
this.inner.setFloat(paramString, paramFloat);
}

public void setInt(String paramString, int paramInt) throws SQLException {
this.inner.setInt(paramString, paramInt);
}

public void setLong(String paramString, long paramLong) throws SQLException {
this.inner.setLong(paramString, paramLong);
}

public void setShort(String paramString, short paramShort) throws SQLException {
this.inner.setShort(paramString, paramShort);
}

public void setTimestamp(String paramString, Timestamp paramTimestamp) throws SQLException {
this.inner.setTimestamp(paramString, paramTimestamp);
}

public void setTimestamp(String paramString, Timestamp paramTimestamp, Calendar paramCalendar) throws SQLException {
this.inner.setTimestamp(paramString, paramTimestamp, paramCalendar);
}

public Ref getRef(int paramInt) throws SQLException {
return this.inner.getRef(paramInt);
}

public Ref getRef(String paramString) throws SQLException {
return this.inner.getRef(paramString);
}

public String getString(String paramString) throws SQLException {
return this.inner.getString(paramString);
}

public String getString(int paramInt) throws SQLException {
return this.inner.getString(paramInt);
}

public void setURL(String paramString, URL paramURL) throws SQLException {
this.inner.setURL(paramString, paramURL);
}

public Date getDate(int paramInt, Calendar paramCalendar) throws SQLException {
return this.inner.getDate(paramInt, paramCalendar);
}

public Date getDate(int paramInt) throws SQLException {
return this.inner.getDate(paramInt);
}

public Date getDate(String paramString, Calendar paramCalendar) throws SQLException {
return this.inner.getDate(paramString, paramCalendar);
}

public Date getDate(String paramString) throws SQLException {
return this.inner.getDate(paramString);
}

public Time getTime(String paramString) throws SQLException {
return this.inner.getTime(paramString);
}

public Time getTime(int paramInt) throws SQLException {
return this.inner.getTime(paramInt);
}

public Time getTime(String paramString, Calendar paramCalendar) throws SQLException {
return this.inner.getTime(paramString, paramCalendar);
}

public Time getTime(int paramInt, Calendar paramCalendar) throws SQLException {
return this.inner.getTime(paramInt, paramCalendar);
}

public void setTime(String paramString, Time paramTime, Calendar paramCalendar) throws SQLException {
this.inner.setTime(paramString, paramTime, paramCalendar);
}

public void setTime(String paramString, Time paramTime) throws SQLException {
this.inner.setTime(paramString, paramTime);
}

public Timestamp getTimestamp(String paramString, Calendar paramCalendar) throws SQLException {
return this.inner.getTimestamp(paramString, paramCalendar);
}

public Timestamp getTimestamp(int paramInt) throws SQLException {
return this.inner.getTimestamp(paramInt);
}

public Timestamp getTimestamp(String paramString) throws SQLException {
return this.inner.getTimestamp(paramString);
}

public Timestamp getTimestamp(int paramInt, Calendar paramCalendar) throws SQLException {
return this.inner.getTimestamp(paramInt, paramCalendar);
}

public boolean execute() throws SQLException {
return this.inner.execute();
}

public ResultSetMetaData getMetaData() throws SQLException {
return this.inner.getMetaData();
}

public void setArray(int paramInt, Array paramArray) throws SQLException {
this.inner.setArray(paramInt, paramArray);
}

public void addBatch() throws SQLException {
this.inner.addBatch();
}

public ResultSet executeQuery() throws SQLException {
return this.inner.executeQuery();
}

public int executeUpdate() throws SQLException {
return this.inner.executeUpdate();
}

public void clearParameters() throws SQLException {
this.inner.clearParameters();
}

public ParameterMetaData getParameterMetaData() throws SQLException {
return this.inner.getParameterMetaData();
}

public void setAsciiStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
this.inner.setAsciiStream(paramInt, paramInputStream, paramLong);
}

public void setAsciiStream(int paramInt, InputStream paramInputStream) throws SQLException {
this.inner.setAsciiStream(paramInt, paramInputStream);
}

public void setAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
this.inner.setAsciiStream(paramInt1, paramInputStream, paramInt2);
}

public void setBigDecimal(int paramInt, BigDecimal paramBigDecimal) throws SQLException {
this.inner.setBigDecimal(paramInt, paramBigDecimal);
}

public void setBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
this.inner.setBinaryStream(paramInt1, paramInputStream, paramInt2);
}

public void setBinaryStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
this.inner.setBinaryStream(paramInt, paramInputStream, paramLong);
}

public void setBinaryStream(int paramInt, InputStream paramInputStream) throws SQLException {
this.inner.setBinaryStream(paramInt, paramInputStream);
}

public void setBlob(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
this.inner.setBlob(paramInt, paramInputStream, paramLong);
}

public void setBlob(int paramInt, Blob paramBlob) throws SQLException {
this.inner.setBlob(paramInt, paramBlob);
}

public void setBlob(int paramInt, InputStream paramInputStream) throws SQLException {
this.inner.setBlob(paramInt, paramInputStream);
}

public void setBytes(int paramInt, byte[] paramArrayOfbyte) throws SQLException {
this.inner.setBytes(paramInt, paramArrayOfbyte);
}

public void setCharacterStream(int paramInt, Reader paramReader) throws SQLException {
this.inner.setCharacterStream(paramInt, paramReader);
}

public void setCharacterStream(int paramInt1, Reader paramReader, int paramInt2) throws SQLException {
this.inner.setCharacterStream(paramInt1, paramReader, paramInt2);
}

public void setCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
this.inner.setCharacterStream(paramInt, paramReader, paramLong);
}

public void setClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
this.inner.setClob(paramInt, paramReader, paramLong);
}

public void setClob(int paramInt, Reader paramReader) throws SQLException {
this.inner.setClob(paramInt, paramReader);
}

public void setClob(int paramInt, Clob paramClob) throws SQLException {
this.inner.setClob(paramInt, paramClob);
}

public void setDate(int paramInt, Date paramDate) throws SQLException {
this.inner.setDate(paramInt, paramDate);
}

public void setDate(int paramInt, Date paramDate, Calendar paramCalendar) throws SQLException {
this.inner.setDate(paramInt, paramDate, paramCalendar);
}

public void setNCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
this.inner.setNCharacterStream(paramInt, paramReader, paramLong);
}

public void setNCharacterStream(int paramInt, Reader paramReader) throws SQLException {
this.inner.setNCharacterStream(paramInt, paramReader);
}

public void setNClob(int paramInt, Reader paramReader) throws SQLException {
this.inner.setNClob(paramInt, paramReader);
}

public void setNClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
this.inner.setNClob(paramInt, paramReader, paramLong);
}

public void setNClob(int paramInt, NClob paramNClob) throws SQLException {
this.inner.setNClob(paramInt, paramNClob);
}

public void setNString(int paramInt, String paramString) throws SQLException {
this.inner.setNString(paramInt, paramString);
}

public void setNull(int paramInt1, int paramInt2) throws SQLException {
this.inner.setNull(paramInt1, paramInt2);
}

public void setNull(int paramInt1, int paramInt2, String paramString) throws SQLException {
this.inner.setNull(paramInt1, paramInt2, paramString);
}

public void setObject(int paramInt1, Object paramObject, int paramInt2) throws SQLException {
this.inner.setObject(paramInt1, paramObject, paramInt2);
}

public void setObject(int paramInt1, Object paramObject, int paramInt2, int paramInt3) throws SQLException {
this.inner.setObject(paramInt1, paramObject, paramInt2, paramInt3);
}

public void setObject(int paramInt, Object paramObject) throws SQLException {
this.inner.setObject(paramInt, paramObject);
}

public void setRef(int paramInt, Ref paramRef) throws SQLException {
this.inner.setRef(paramInt, paramRef);
}

public void setRowId(int paramInt, RowId paramRowId) throws SQLException {
this.inner.setRowId(paramInt, paramRowId);
}

public void setSQLXML(int paramInt, SQLXML paramSQLXML) throws SQLException {
this.inner.setSQLXML(paramInt, paramSQLXML);
}

public void setString(int paramInt, String paramString) throws SQLException {
this.inner.setString(paramInt, paramString);
}

public void setUnicodeStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
this.inner.setUnicodeStream(paramInt1, paramInputStream, paramInt2);
}

public void setBoolean(int paramInt, boolean paramBoolean) throws SQLException {
this.inner.setBoolean(paramInt, paramBoolean);
}

public void setByte(int paramInt, byte paramByte) throws SQLException {
this.inner.setByte(paramInt, paramByte);
}

public void setDouble(int paramInt, double paramDouble) throws SQLException {
this.inner.setDouble(paramInt, paramDouble);
}

public void setFloat(int paramInt, float paramFloat) throws SQLException {
this.inner.setFloat(paramInt, paramFloat);
}

public void setInt(int paramInt1, int paramInt2) throws SQLException {
this.inner.setInt(paramInt1, paramInt2);
}

public void setLong(int paramInt, long paramLong) throws SQLException {
this.inner.setLong(paramInt, paramLong);
}

public void setShort(int paramInt, short paramShort) throws SQLException {
this.inner.setShort(paramInt, paramShort);
}

public void setTimestamp(int paramInt, Timestamp paramTimestamp, Calendar paramCalendar) throws SQLException {
this.inner.setTimestamp(paramInt, paramTimestamp, paramCalendar);
}

public void setTimestamp(int paramInt, Timestamp paramTimestamp) throws SQLException {
this.inner.setTimestamp(paramInt, paramTimestamp);
}

public void setURL(int paramInt, URL paramURL) throws SQLException {
this.inner.setURL(paramInt, paramURL);
}

public void setTime(int paramInt, Time paramTime, Calendar paramCalendar) throws SQLException {
this.inner.setTime(paramInt, paramTime, paramCalendar);
}

public void setTime(int paramInt, Time paramTime) throws SQLException {
this.inner.setTime(paramInt, paramTime);
}

public boolean execute(String paramString, int paramInt) throws SQLException {
return this.inner.execute(paramString, paramInt);
}

public boolean execute(String paramString, String[] paramArrayOfString) throws SQLException {
return this.inner.execute(paramString, paramArrayOfString);
}

public boolean execute(String paramString) throws SQLException {
return this.inner.execute(paramString);
}

public boolean execute(String paramString, int[] paramArrayOfint) throws SQLException {
return this.inner.execute(paramString, paramArrayOfint);
}

public void clearWarnings() throws SQLException {
this.inner.clearWarnings();
}

public SQLWarning getWarnings() throws SQLException {
return this.inner.getWarnings();
}

public boolean isClosed() throws SQLException {
return this.inner.isClosed();
}

public int getFetchDirection() throws SQLException {
return this.inner.getFetchDirection();
}

public int getFetchSize() throws SQLException {
return this.inner.getFetchSize();
}

public void setFetchDirection(int paramInt) throws SQLException {
this.inner.setFetchDirection(paramInt);
}

public void setFetchSize(int paramInt) throws SQLException {
this.inner.setFetchSize(paramInt);
}

public Connection getConnection() throws SQLException {
return this.inner.getConnection();
}

public int getResultSetHoldability() throws SQLException {
return this.inner.getResultSetHoldability();
}

public void addBatch(String paramString) throws SQLException {
this.inner.addBatch(paramString);
}

public void cancel() throws SQLException {
this.inner.cancel();
}

public void clearBatch() throws SQLException {
this.inner.clearBatch();
}

public void closeOnCompletion() throws SQLException {
this.inner.closeOnCompletion();
}

public int[] executeBatch() throws SQLException {
return this.inner.executeBatch();
}

public ResultSet executeQuery(String paramString) throws SQLException {
return this.inner.executeQuery(paramString);
}

public int executeUpdate(String paramString, int[] paramArrayOfint) throws SQLException {
return this.inner.executeUpdate(paramString, paramArrayOfint);
}

public int executeUpdate(String paramString, String[] paramArrayOfString) throws SQLException {
return this.inner.executeUpdate(paramString, paramArrayOfString);
}

public int executeUpdate(String paramString) throws SQLException {
return this.inner.executeUpdate(paramString);
}

public int executeUpdate(String paramString, int paramInt) throws SQLException {
return this.inner.executeUpdate(paramString, paramInt);
}

public ResultSet getGeneratedKeys() throws SQLException {
return this.inner.getGeneratedKeys();
}

public int getMaxFieldSize() throws SQLException {
return this.inner.getMaxFieldSize();
}

public int getMaxRows() throws SQLException {
return this.inner.getMaxRows();
}

public boolean getMoreResults() throws SQLException {
return this.inner.getMoreResults();
}

public boolean getMoreResults(int paramInt) throws SQLException {
return this.inner.getMoreResults(paramInt);
}

public int getQueryTimeout() throws SQLException {
return this.inner.getQueryTimeout();
}

public ResultSet getResultSet() throws SQLException {
return this.inner.getResultSet();
}

public int getResultSetConcurrency() throws SQLException {
return this.inner.getResultSetConcurrency();
}

public int getResultSetType() throws SQLException {
return this.inner.getResultSetType();
}

public int getUpdateCount() throws SQLException {
return this.inner.getUpdateCount();
}

public boolean isCloseOnCompletion() throws SQLException {
return this.inner.isCloseOnCompletion();
}

public boolean isPoolable() throws SQLException {
return this.inner.isPoolable();
}

public void setCursorName(String paramString) throws SQLException {
this.inner.setCursorName(paramString);
}

public void setEscapeProcessing(boolean paramBoolean) throws SQLException {
this.inner.setEscapeProcessing(paramBoolean);
}

public void setMaxFieldSize(int paramInt) throws SQLException {
this.inner.setMaxFieldSize(paramInt);
}

public void setMaxRows(int paramInt) throws SQLException {
this.inner.setMaxRows(paramInt);
}

public void setPoolable(boolean paramBoolean) throws SQLException {
this.inner.setPoolable(paramBoolean);
}

public void setQueryTimeout(int paramInt) throws SQLException {
this.inner.setQueryTimeout(paramInt);
}

public void close() throws SQLException {
this.inner.close();
}

public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
return this.inner.isWrapperFor(paramClass);
}

public Object unwrap(Class<?> paramClass) throws SQLException {
return this.inner.unwrap(paramClass);
}
}

