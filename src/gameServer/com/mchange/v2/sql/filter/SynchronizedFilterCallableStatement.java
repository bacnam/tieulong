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

public abstract class SynchronizedFilterCallableStatement
implements CallableStatement
{
protected CallableStatement inner;

private void __setInner(CallableStatement paramCallableStatement) {
this.inner = paramCallableStatement;
}

public SynchronizedFilterCallableStatement(CallableStatement paramCallableStatement) {
__setInner(paramCallableStatement);
}

public SynchronizedFilterCallableStatement() {}

public synchronized void setInner(CallableStatement paramCallableStatement) {
__setInner(paramCallableStatement);
}
public synchronized CallableStatement getInner() {
return this.inner;
}

public synchronized BigDecimal getBigDecimal(int paramInt1, int paramInt2) throws SQLException {
return this.inner.getBigDecimal(paramInt1, paramInt2);
}

public synchronized BigDecimal getBigDecimal(String paramString) throws SQLException {
return this.inner.getBigDecimal(paramString);
}

public synchronized BigDecimal getBigDecimal(int paramInt) throws SQLException {
return this.inner.getBigDecimal(paramInt);
}

public synchronized Blob getBlob(int paramInt) throws SQLException {
return this.inner.getBlob(paramInt);
}

public synchronized Blob getBlob(String paramString) throws SQLException {
return this.inner.getBlob(paramString);
}

public synchronized Reader getCharacterStream(int paramInt) throws SQLException {
return this.inner.getCharacterStream(paramInt);
}

public synchronized Reader getCharacterStream(String paramString) throws SQLException {
return this.inner.getCharacterStream(paramString);
}

public synchronized Clob getClob(int paramInt) throws SQLException {
return this.inner.getClob(paramInt);
}

public synchronized Clob getClob(String paramString) throws SQLException {
return this.inner.getClob(paramString);
}

public synchronized Reader getNCharacterStream(int paramInt) throws SQLException {
return this.inner.getNCharacterStream(paramInt);
}

public synchronized Reader getNCharacterStream(String paramString) throws SQLException {
return this.inner.getNCharacterStream(paramString);
}

public synchronized NClob getNClob(String paramString) throws SQLException {
return this.inner.getNClob(paramString);
}

public synchronized NClob getNClob(int paramInt) throws SQLException {
return this.inner.getNClob(paramInt);
}

public synchronized String getNString(int paramInt) throws SQLException {
return this.inner.getNString(paramInt);
}

public synchronized String getNString(String paramString) throws SQLException {
return this.inner.getNString(paramString);
}

public synchronized RowId getRowId(String paramString) throws SQLException {
return this.inner.getRowId(paramString);
}

public synchronized RowId getRowId(int paramInt) throws SQLException {
return this.inner.getRowId(paramInt);
}

public synchronized SQLXML getSQLXML(String paramString) throws SQLException {
return this.inner.getSQLXML(paramString);
}

public synchronized SQLXML getSQLXML(int paramInt) throws SQLException {
return this.inner.getSQLXML(paramInt);
}

public synchronized boolean wasNull() throws SQLException {
return this.inner.wasNull();
}

public synchronized void registerOutParameter(String paramString1, int paramInt, String paramString2) throws SQLException {
this.inner.registerOutParameter(paramString1, paramInt, paramString2);
}

public synchronized void registerOutParameter(int paramInt1, int paramInt2) throws SQLException {
this.inner.registerOutParameter(paramInt1, paramInt2);
}

public synchronized void registerOutParameter(int paramInt1, int paramInt2, String paramString) throws SQLException {
this.inner.registerOutParameter(paramInt1, paramInt2, paramString);
}

public synchronized void registerOutParameter(String paramString, int paramInt1, int paramInt2) throws SQLException {
this.inner.registerOutParameter(paramString, paramInt1, paramInt2);
}

public synchronized void registerOutParameter(String paramString, int paramInt) throws SQLException {
this.inner.registerOutParameter(paramString, paramInt);
}

public synchronized void registerOutParameter(int paramInt1, int paramInt2, int paramInt3) throws SQLException {
this.inner.registerOutParameter(paramInt1, paramInt2, paramInt3);
}

public synchronized void setAsciiStream(String paramString, InputStream paramInputStream) throws SQLException {
this.inner.setAsciiStream(paramString, paramInputStream);
}

public synchronized void setAsciiStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
this.inner.setAsciiStream(paramString, paramInputStream, paramLong);
}

public synchronized void setAsciiStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
this.inner.setAsciiStream(paramString, paramInputStream, paramInt);
}

public synchronized void setBigDecimal(String paramString, BigDecimal paramBigDecimal) throws SQLException {
this.inner.setBigDecimal(paramString, paramBigDecimal);
}

public synchronized void setBinaryStream(String paramString, InputStream paramInputStream) throws SQLException {
this.inner.setBinaryStream(paramString, paramInputStream);
}

public synchronized void setBinaryStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
this.inner.setBinaryStream(paramString, paramInputStream, paramInt);
}

public synchronized void setBinaryStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
this.inner.setBinaryStream(paramString, paramInputStream, paramLong);
}

public synchronized void setBlob(String paramString, Blob paramBlob) throws SQLException {
this.inner.setBlob(paramString, paramBlob);
}

public synchronized void setBlob(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
this.inner.setBlob(paramString, paramInputStream, paramLong);
}

public synchronized void setBlob(String paramString, InputStream paramInputStream) throws SQLException {
this.inner.setBlob(paramString, paramInputStream);
}

public synchronized void setBytes(String paramString, byte[] paramArrayOfbyte) throws SQLException {
this.inner.setBytes(paramString, paramArrayOfbyte);
}

public synchronized void setCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException {
this.inner.setCharacterStream(paramString, paramReader, paramLong);
}

public synchronized void setCharacterStream(String paramString, Reader paramReader) throws SQLException {
this.inner.setCharacterStream(paramString, paramReader);
}

public synchronized void setCharacterStream(String paramString, Reader paramReader, int paramInt) throws SQLException {
this.inner.setCharacterStream(paramString, paramReader, paramInt);
}

public synchronized void setClob(String paramString, Reader paramReader) throws SQLException {
this.inner.setClob(paramString, paramReader);
}

public synchronized void setClob(String paramString, Clob paramClob) throws SQLException {
this.inner.setClob(paramString, paramClob);
}

public synchronized void setClob(String paramString, Reader paramReader, long paramLong) throws SQLException {
this.inner.setClob(paramString, paramReader, paramLong);
}

public synchronized void setDate(String paramString, Date paramDate, Calendar paramCalendar) throws SQLException {
this.inner.setDate(paramString, paramDate, paramCalendar);
}

public synchronized void setDate(String paramString, Date paramDate) throws SQLException {
this.inner.setDate(paramString, paramDate);
}

public synchronized void setNCharacterStream(String paramString, Reader paramReader) throws SQLException {
this.inner.setNCharacterStream(paramString, paramReader);
}

public synchronized void setNCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException {
this.inner.setNCharacterStream(paramString, paramReader, paramLong);
}

public synchronized void setNClob(String paramString, Reader paramReader, long paramLong) throws SQLException {
this.inner.setNClob(paramString, paramReader, paramLong);
}

public synchronized void setNClob(String paramString, NClob paramNClob) throws SQLException {
this.inner.setNClob(paramString, paramNClob);
}

public synchronized void setNClob(String paramString, Reader paramReader) throws SQLException {
this.inner.setNClob(paramString, paramReader);
}

public synchronized void setNString(String paramString1, String paramString2) throws SQLException {
this.inner.setNString(paramString1, paramString2);
}

public synchronized void setNull(String paramString1, int paramInt, String paramString2) throws SQLException {
this.inner.setNull(paramString1, paramInt, paramString2);
}

public synchronized void setNull(String paramString, int paramInt) throws SQLException {
this.inner.setNull(paramString, paramInt);
}

public synchronized void setObject(String paramString, Object paramObject) throws SQLException {
this.inner.setObject(paramString, paramObject);
}

public synchronized void setObject(String paramString, Object paramObject, int paramInt1, int paramInt2) throws SQLException {
this.inner.setObject(paramString, paramObject, paramInt1, paramInt2);
}

public synchronized void setObject(String paramString, Object paramObject, int paramInt) throws SQLException {
this.inner.setObject(paramString, paramObject, paramInt);
}

public synchronized void setRowId(String paramString, RowId paramRowId) throws SQLException {
this.inner.setRowId(paramString, paramRowId);
}

public synchronized void setSQLXML(String paramString, SQLXML paramSQLXML) throws SQLException {
this.inner.setSQLXML(paramString, paramSQLXML);
}

public synchronized void setString(String paramString1, String paramString2) throws SQLException {
this.inner.setString(paramString1, paramString2);
}

public synchronized Object getObject(String paramString) throws SQLException {
return this.inner.getObject(paramString);
}

public synchronized Object getObject(String paramString, Map<String, Class<?>> paramMap) throws SQLException {
return this.inner.getObject(paramString, paramMap);
}

public synchronized Object getObject(int paramInt, Class<?> paramClass) throws SQLException {
return this.inner.getObject(paramInt, paramClass);
}

public synchronized Object getObject(int paramInt) throws SQLException {
return this.inner.getObject(paramInt);
}

public synchronized Object getObject(int paramInt, Map<String, Class<?>> paramMap) throws SQLException {
return this.inner.getObject(paramInt, paramMap);
}

public synchronized Object getObject(String paramString, Class<?> paramClass) throws SQLException {
return this.inner.getObject(paramString, paramClass);
}

public synchronized boolean getBoolean(String paramString) throws SQLException {
return this.inner.getBoolean(paramString);
}

public synchronized boolean getBoolean(int paramInt) throws SQLException {
return this.inner.getBoolean(paramInt);
}

public synchronized byte getByte(String paramString) throws SQLException {
return this.inner.getByte(paramString);
}

public synchronized byte getByte(int paramInt) throws SQLException {
return this.inner.getByte(paramInt);
}

public synchronized short getShort(String paramString) throws SQLException {
return this.inner.getShort(paramString);
}

public synchronized short getShort(int paramInt) throws SQLException {
return this.inner.getShort(paramInt);
}

public synchronized int getInt(int paramInt) throws SQLException {
return this.inner.getInt(paramInt);
}

public synchronized int getInt(String paramString) throws SQLException {
return this.inner.getInt(paramString);
}

public synchronized long getLong(int paramInt) throws SQLException {
return this.inner.getLong(paramInt);
}

public synchronized long getLong(String paramString) throws SQLException {
return this.inner.getLong(paramString);
}

public synchronized float getFloat(int paramInt) throws SQLException {
return this.inner.getFloat(paramInt);
}

public synchronized float getFloat(String paramString) throws SQLException {
return this.inner.getFloat(paramString);
}

public synchronized double getDouble(String paramString) throws SQLException {
return this.inner.getDouble(paramString);
}

public synchronized double getDouble(int paramInt) throws SQLException {
return this.inner.getDouble(paramInt);
}

public synchronized byte[] getBytes(int paramInt) throws SQLException {
return this.inner.getBytes(paramInt);
}

public synchronized byte[] getBytes(String paramString) throws SQLException {
return this.inner.getBytes(paramString);
}

public synchronized Array getArray(int paramInt) throws SQLException {
return this.inner.getArray(paramInt);
}

public synchronized Array getArray(String paramString) throws SQLException {
return this.inner.getArray(paramString);
}

public synchronized URL getURL(int paramInt) throws SQLException {
return this.inner.getURL(paramInt);
}

public synchronized URL getURL(String paramString) throws SQLException {
return this.inner.getURL(paramString);
}

public synchronized void setBoolean(String paramString, boolean paramBoolean) throws SQLException {
this.inner.setBoolean(paramString, paramBoolean);
}

public synchronized void setByte(String paramString, byte paramByte) throws SQLException {
this.inner.setByte(paramString, paramByte);
}

public synchronized void setDouble(String paramString, double paramDouble) throws SQLException {
this.inner.setDouble(paramString, paramDouble);
}

public synchronized void setFloat(String paramString, float paramFloat) throws SQLException {
this.inner.setFloat(paramString, paramFloat);
}

public synchronized void setInt(String paramString, int paramInt) throws SQLException {
this.inner.setInt(paramString, paramInt);
}

public synchronized void setLong(String paramString, long paramLong) throws SQLException {
this.inner.setLong(paramString, paramLong);
}

public synchronized void setShort(String paramString, short paramShort) throws SQLException {
this.inner.setShort(paramString, paramShort);
}

public synchronized void setTimestamp(String paramString, Timestamp paramTimestamp) throws SQLException {
this.inner.setTimestamp(paramString, paramTimestamp);
}

public synchronized void setTimestamp(String paramString, Timestamp paramTimestamp, Calendar paramCalendar) throws SQLException {
this.inner.setTimestamp(paramString, paramTimestamp, paramCalendar);
}

public synchronized Ref getRef(int paramInt) throws SQLException {
return this.inner.getRef(paramInt);
}

public synchronized Ref getRef(String paramString) throws SQLException {
return this.inner.getRef(paramString);
}

public synchronized String getString(String paramString) throws SQLException {
return this.inner.getString(paramString);
}

public synchronized String getString(int paramInt) throws SQLException {
return this.inner.getString(paramInt);
}

public synchronized void setURL(String paramString, URL paramURL) throws SQLException {
this.inner.setURL(paramString, paramURL);
}

public synchronized Date getDate(int paramInt, Calendar paramCalendar) throws SQLException {
return this.inner.getDate(paramInt, paramCalendar);
}

public synchronized Date getDate(int paramInt) throws SQLException {
return this.inner.getDate(paramInt);
}

public synchronized Date getDate(String paramString, Calendar paramCalendar) throws SQLException {
return this.inner.getDate(paramString, paramCalendar);
}

public synchronized Date getDate(String paramString) throws SQLException {
return this.inner.getDate(paramString);
}

public synchronized Time getTime(String paramString) throws SQLException {
return this.inner.getTime(paramString);
}

public synchronized Time getTime(int paramInt) throws SQLException {
return this.inner.getTime(paramInt);
}

public synchronized Time getTime(String paramString, Calendar paramCalendar) throws SQLException {
return this.inner.getTime(paramString, paramCalendar);
}

public synchronized Time getTime(int paramInt, Calendar paramCalendar) throws SQLException {
return this.inner.getTime(paramInt, paramCalendar);
}

public synchronized void setTime(String paramString, Time paramTime, Calendar paramCalendar) throws SQLException {
this.inner.setTime(paramString, paramTime, paramCalendar);
}

public synchronized void setTime(String paramString, Time paramTime) throws SQLException {
this.inner.setTime(paramString, paramTime);
}

public synchronized Timestamp getTimestamp(String paramString, Calendar paramCalendar) throws SQLException {
return this.inner.getTimestamp(paramString, paramCalendar);
}

public synchronized Timestamp getTimestamp(int paramInt) throws SQLException {
return this.inner.getTimestamp(paramInt);
}

public synchronized Timestamp getTimestamp(String paramString) throws SQLException {
return this.inner.getTimestamp(paramString);
}

public synchronized Timestamp getTimestamp(int paramInt, Calendar paramCalendar) throws SQLException {
return this.inner.getTimestamp(paramInt, paramCalendar);
}

public synchronized boolean execute() throws SQLException {
return this.inner.execute();
}

public synchronized ResultSetMetaData getMetaData() throws SQLException {
return this.inner.getMetaData();
}

public synchronized void setArray(int paramInt, Array paramArray) throws SQLException {
this.inner.setArray(paramInt, paramArray);
}

public synchronized void addBatch() throws SQLException {
this.inner.addBatch();
}

public synchronized ResultSet executeQuery() throws SQLException {
return this.inner.executeQuery();
}

public synchronized int executeUpdate() throws SQLException {
return this.inner.executeUpdate();
}

public synchronized void clearParameters() throws SQLException {
this.inner.clearParameters();
}

public synchronized ParameterMetaData getParameterMetaData() throws SQLException {
return this.inner.getParameterMetaData();
}

public synchronized void setAsciiStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
this.inner.setAsciiStream(paramInt, paramInputStream, paramLong);
}

public synchronized void setAsciiStream(int paramInt, InputStream paramInputStream) throws SQLException {
this.inner.setAsciiStream(paramInt, paramInputStream);
}

public synchronized void setAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
this.inner.setAsciiStream(paramInt1, paramInputStream, paramInt2);
}

public synchronized void setBigDecimal(int paramInt, BigDecimal paramBigDecimal) throws SQLException {
this.inner.setBigDecimal(paramInt, paramBigDecimal);
}

public synchronized void setBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
this.inner.setBinaryStream(paramInt1, paramInputStream, paramInt2);
}

public synchronized void setBinaryStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
this.inner.setBinaryStream(paramInt, paramInputStream, paramLong);
}

public synchronized void setBinaryStream(int paramInt, InputStream paramInputStream) throws SQLException {
this.inner.setBinaryStream(paramInt, paramInputStream);
}

public synchronized void setBlob(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
this.inner.setBlob(paramInt, paramInputStream, paramLong);
}

public synchronized void setBlob(int paramInt, Blob paramBlob) throws SQLException {
this.inner.setBlob(paramInt, paramBlob);
}

public synchronized void setBlob(int paramInt, InputStream paramInputStream) throws SQLException {
this.inner.setBlob(paramInt, paramInputStream);
}

public synchronized void setBytes(int paramInt, byte[] paramArrayOfbyte) throws SQLException {
this.inner.setBytes(paramInt, paramArrayOfbyte);
}

public synchronized void setCharacterStream(int paramInt, Reader paramReader) throws SQLException {
this.inner.setCharacterStream(paramInt, paramReader);
}

public synchronized void setCharacterStream(int paramInt1, Reader paramReader, int paramInt2) throws SQLException {
this.inner.setCharacterStream(paramInt1, paramReader, paramInt2);
}

public synchronized void setCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
this.inner.setCharacterStream(paramInt, paramReader, paramLong);
}

public synchronized void setClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
this.inner.setClob(paramInt, paramReader, paramLong);
}

public synchronized void setClob(int paramInt, Reader paramReader) throws SQLException {
this.inner.setClob(paramInt, paramReader);
}

public synchronized void setClob(int paramInt, Clob paramClob) throws SQLException {
this.inner.setClob(paramInt, paramClob);
}

public synchronized void setDate(int paramInt, Date paramDate) throws SQLException {
this.inner.setDate(paramInt, paramDate);
}

public synchronized void setDate(int paramInt, Date paramDate, Calendar paramCalendar) throws SQLException {
this.inner.setDate(paramInt, paramDate, paramCalendar);
}

public synchronized void setNCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
this.inner.setNCharacterStream(paramInt, paramReader, paramLong);
}

public synchronized void setNCharacterStream(int paramInt, Reader paramReader) throws SQLException {
this.inner.setNCharacterStream(paramInt, paramReader);
}

public synchronized void setNClob(int paramInt, Reader paramReader) throws SQLException {
this.inner.setNClob(paramInt, paramReader);
}

public synchronized void setNClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
this.inner.setNClob(paramInt, paramReader, paramLong);
}

public synchronized void setNClob(int paramInt, NClob paramNClob) throws SQLException {
this.inner.setNClob(paramInt, paramNClob);
}

public synchronized void setNString(int paramInt, String paramString) throws SQLException {
this.inner.setNString(paramInt, paramString);
}

public synchronized void setNull(int paramInt1, int paramInt2) throws SQLException {
this.inner.setNull(paramInt1, paramInt2);
}

public synchronized void setNull(int paramInt1, int paramInt2, String paramString) throws SQLException {
this.inner.setNull(paramInt1, paramInt2, paramString);
}

public synchronized void setObject(int paramInt1, Object paramObject, int paramInt2) throws SQLException {
this.inner.setObject(paramInt1, paramObject, paramInt2);
}

public synchronized void setObject(int paramInt1, Object paramObject, int paramInt2, int paramInt3) throws SQLException {
this.inner.setObject(paramInt1, paramObject, paramInt2, paramInt3);
}

public synchronized void setObject(int paramInt, Object paramObject) throws SQLException {
this.inner.setObject(paramInt, paramObject);
}

public synchronized void setRef(int paramInt, Ref paramRef) throws SQLException {
this.inner.setRef(paramInt, paramRef);
}

public synchronized void setRowId(int paramInt, RowId paramRowId) throws SQLException {
this.inner.setRowId(paramInt, paramRowId);
}

public synchronized void setSQLXML(int paramInt, SQLXML paramSQLXML) throws SQLException {
this.inner.setSQLXML(paramInt, paramSQLXML);
}

public synchronized void setString(int paramInt, String paramString) throws SQLException {
this.inner.setString(paramInt, paramString);
}

public synchronized void setUnicodeStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
this.inner.setUnicodeStream(paramInt1, paramInputStream, paramInt2);
}

public synchronized void setBoolean(int paramInt, boolean paramBoolean) throws SQLException {
this.inner.setBoolean(paramInt, paramBoolean);
}

public synchronized void setByte(int paramInt, byte paramByte) throws SQLException {
this.inner.setByte(paramInt, paramByte);
}

public synchronized void setDouble(int paramInt, double paramDouble) throws SQLException {
this.inner.setDouble(paramInt, paramDouble);
}

public synchronized void setFloat(int paramInt, float paramFloat) throws SQLException {
this.inner.setFloat(paramInt, paramFloat);
}

public synchronized void setInt(int paramInt1, int paramInt2) throws SQLException {
this.inner.setInt(paramInt1, paramInt2);
}

public synchronized void setLong(int paramInt, long paramLong) throws SQLException {
this.inner.setLong(paramInt, paramLong);
}

public synchronized void setShort(int paramInt, short paramShort) throws SQLException {
this.inner.setShort(paramInt, paramShort);
}

public synchronized void setTimestamp(int paramInt, Timestamp paramTimestamp, Calendar paramCalendar) throws SQLException {
this.inner.setTimestamp(paramInt, paramTimestamp, paramCalendar);
}

public synchronized void setTimestamp(int paramInt, Timestamp paramTimestamp) throws SQLException {
this.inner.setTimestamp(paramInt, paramTimestamp);
}

public synchronized void setURL(int paramInt, URL paramURL) throws SQLException {
this.inner.setURL(paramInt, paramURL);
}

public synchronized void setTime(int paramInt, Time paramTime, Calendar paramCalendar) throws SQLException {
this.inner.setTime(paramInt, paramTime, paramCalendar);
}

public synchronized void setTime(int paramInt, Time paramTime) throws SQLException {
this.inner.setTime(paramInt, paramTime);
}

public synchronized boolean execute(String paramString, int paramInt) throws SQLException {
return this.inner.execute(paramString, paramInt);
}

public synchronized boolean execute(String paramString, String[] paramArrayOfString) throws SQLException {
return this.inner.execute(paramString, paramArrayOfString);
}

public synchronized boolean execute(String paramString) throws SQLException {
return this.inner.execute(paramString);
}

public synchronized boolean execute(String paramString, int[] paramArrayOfint) throws SQLException {
return this.inner.execute(paramString, paramArrayOfint);
}

public synchronized void clearWarnings() throws SQLException {
this.inner.clearWarnings();
}

public synchronized SQLWarning getWarnings() throws SQLException {
return this.inner.getWarnings();
}

public synchronized boolean isClosed() throws SQLException {
return this.inner.isClosed();
}

public synchronized int getFetchDirection() throws SQLException {
return this.inner.getFetchDirection();
}

public synchronized int getFetchSize() throws SQLException {
return this.inner.getFetchSize();
}

public synchronized void setFetchDirection(int paramInt) throws SQLException {
this.inner.setFetchDirection(paramInt);
}

public synchronized void setFetchSize(int paramInt) throws SQLException {
this.inner.setFetchSize(paramInt);
}

public synchronized Connection getConnection() throws SQLException {
return this.inner.getConnection();
}

public synchronized int getResultSetHoldability() throws SQLException {
return this.inner.getResultSetHoldability();
}

public synchronized void addBatch(String paramString) throws SQLException {
this.inner.addBatch(paramString);
}

public synchronized void cancel() throws SQLException {
this.inner.cancel();
}

public synchronized void clearBatch() throws SQLException {
this.inner.clearBatch();
}

public synchronized void closeOnCompletion() throws SQLException {
this.inner.closeOnCompletion();
}

public synchronized int[] executeBatch() throws SQLException {
return this.inner.executeBatch();
}

public synchronized ResultSet executeQuery(String paramString) throws SQLException {
return this.inner.executeQuery(paramString);
}

public synchronized int executeUpdate(String paramString, int[] paramArrayOfint) throws SQLException {
return this.inner.executeUpdate(paramString, paramArrayOfint);
}

public synchronized int executeUpdate(String paramString, String[] paramArrayOfString) throws SQLException {
return this.inner.executeUpdate(paramString, paramArrayOfString);
}

public synchronized int executeUpdate(String paramString) throws SQLException {
return this.inner.executeUpdate(paramString);
}

public synchronized int executeUpdate(String paramString, int paramInt) throws SQLException {
return this.inner.executeUpdate(paramString, paramInt);
}

public synchronized ResultSet getGeneratedKeys() throws SQLException {
return this.inner.getGeneratedKeys();
}

public synchronized int getMaxFieldSize() throws SQLException {
return this.inner.getMaxFieldSize();
}

public synchronized int getMaxRows() throws SQLException {
return this.inner.getMaxRows();
}

public synchronized boolean getMoreResults() throws SQLException {
return this.inner.getMoreResults();
}

public synchronized boolean getMoreResults(int paramInt) throws SQLException {
return this.inner.getMoreResults(paramInt);
}

public synchronized int getQueryTimeout() throws SQLException {
return this.inner.getQueryTimeout();
}

public synchronized ResultSet getResultSet() throws SQLException {
return this.inner.getResultSet();
}

public synchronized int getResultSetConcurrency() throws SQLException {
return this.inner.getResultSetConcurrency();
}

public synchronized int getResultSetType() throws SQLException {
return this.inner.getResultSetType();
}

public synchronized int getUpdateCount() throws SQLException {
return this.inner.getUpdateCount();
}

public synchronized boolean isCloseOnCompletion() throws SQLException {
return this.inner.isCloseOnCompletion();
}

public synchronized boolean isPoolable() throws SQLException {
return this.inner.isPoolable();
}

public synchronized void setCursorName(String paramString) throws SQLException {
this.inner.setCursorName(paramString);
}

public synchronized void setEscapeProcessing(boolean paramBoolean) throws SQLException {
this.inner.setEscapeProcessing(paramBoolean);
}

public synchronized void setMaxFieldSize(int paramInt) throws SQLException {
this.inner.setMaxFieldSize(paramInt);
}

public synchronized void setMaxRows(int paramInt) throws SQLException {
this.inner.setMaxRows(paramInt);
}

public synchronized void setPoolable(boolean paramBoolean) throws SQLException {
this.inner.setPoolable(paramBoolean);
}

public synchronized void setQueryTimeout(int paramInt) throws SQLException {
this.inner.setQueryTimeout(paramInt);
}

public synchronized void close() throws SQLException {
this.inner.close();
}

public synchronized boolean isWrapperFor(Class<?> paramClass) throws SQLException {
return this.inner.isWrapperFor(paramClass);
}

public synchronized Object unwrap(Class<?> paramClass) throws SQLException {
return this.inner.unwrap(paramClass);
}
}

