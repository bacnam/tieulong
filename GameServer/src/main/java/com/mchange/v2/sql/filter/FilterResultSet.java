package com.mchange.v2.sql.filter;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public abstract class FilterResultSet
implements ResultSet
{
protected ResultSet inner;

private void __setInner(ResultSet paramResultSet) {
this.inner = paramResultSet;
}

public FilterResultSet(ResultSet paramResultSet) {
__setInner(paramResultSet);
}

public FilterResultSet() {}

public void setInner(ResultSet paramResultSet) {
__setInner(paramResultSet);
}
public ResultSet getInner() {
return this.inner;
}

public void clearWarnings() throws SQLException {
this.inner.clearWarnings();
}

public int getHoldability() throws SQLException {
return this.inner.getHoldability();
}

public ResultSetMetaData getMetaData() throws SQLException {
return this.inner.getMetaData();
}

public SQLWarning getWarnings() throws SQLException {
return this.inner.getWarnings();
}

public boolean isClosed() throws SQLException {
return this.inner.isClosed();
}

public void updateBigDecimal(int paramInt, BigDecimal paramBigDecimal) throws SQLException {
this.inner.updateBigDecimal(paramInt, paramBigDecimal);
}

public void updateBigDecimal(String paramString, BigDecimal paramBigDecimal) throws SQLException {
this.inner.updateBigDecimal(paramString, paramBigDecimal);
}

public boolean absolute(int paramInt) throws SQLException {
return this.inner.absolute(paramInt);
}

public void afterLast() throws SQLException {
this.inner.afterLast();
}

public void beforeFirst() throws SQLException {
this.inner.beforeFirst();
}

public void cancelRowUpdates() throws SQLException {
this.inner.cancelRowUpdates();
}

public void deleteRow() throws SQLException {
this.inner.deleteRow();
}

public int findColumn(String paramString) throws SQLException {
return this.inner.findColumn(paramString);
}

public boolean first() throws SQLException {
return this.inner.first();
}

public InputStream getAsciiStream(int paramInt) throws SQLException {
return this.inner.getAsciiStream(paramInt);
}

public InputStream getAsciiStream(String paramString) throws SQLException {
return this.inner.getAsciiStream(paramString);
}

public BigDecimal getBigDecimal(String paramString, int paramInt) throws SQLException {
return this.inner.getBigDecimal(paramString, paramInt);
}

public BigDecimal getBigDecimal(String paramString) throws SQLException {
return this.inner.getBigDecimal(paramString);
}

public BigDecimal getBigDecimal(int paramInt) throws SQLException {
return this.inner.getBigDecimal(paramInt);
}

public BigDecimal getBigDecimal(int paramInt1, int paramInt2) throws SQLException {
return this.inner.getBigDecimal(paramInt1, paramInt2);
}

public InputStream getBinaryStream(String paramString) throws SQLException {
return this.inner.getBinaryStream(paramString);
}

public InputStream getBinaryStream(int paramInt) throws SQLException {
return this.inner.getBinaryStream(paramInt);
}

public Blob getBlob(String paramString) throws SQLException {
return this.inner.getBlob(paramString);
}

public Blob getBlob(int paramInt) throws SQLException {
return this.inner.getBlob(paramInt);
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

public int getConcurrency() throws SQLException {
return this.inner.getConcurrency();
}

public String getCursorName() throws SQLException {
return this.inner.getCursorName();
}

public int getFetchDirection() throws SQLException {
return this.inner.getFetchDirection();
}

public int getFetchSize() throws SQLException {
return this.inner.getFetchSize();
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

public int getRow() throws SQLException {
return this.inner.getRow();
}

public RowId getRowId(int paramInt) throws SQLException {
return this.inner.getRowId(paramInt);
}

public RowId getRowId(String paramString) throws SQLException {
return this.inner.getRowId(paramString);
}

public SQLXML getSQLXML(String paramString) throws SQLException {
return this.inner.getSQLXML(paramString);
}

public SQLXML getSQLXML(int paramInt) throws SQLException {
return this.inner.getSQLXML(paramInt);
}

public Statement getStatement() throws SQLException {
return this.inner.getStatement();
}

public InputStream getUnicodeStream(int paramInt) throws SQLException {
return this.inner.getUnicodeStream(paramInt);
}

public InputStream getUnicodeStream(String paramString) throws SQLException {
return this.inner.getUnicodeStream(paramString);
}

public void insertRow() throws SQLException {
this.inner.insertRow();
}

public boolean isAfterLast() throws SQLException {
return this.inner.isAfterLast();
}

public boolean isBeforeFirst() throws SQLException {
return this.inner.isBeforeFirst();
}

public boolean isFirst() throws SQLException {
return this.inner.isFirst();
}

public boolean isLast() throws SQLException {
return this.inner.isLast();
}

public boolean last() throws SQLException {
return this.inner.last();
}

public void moveToCurrentRow() throws SQLException {
this.inner.moveToCurrentRow();
}

public void moveToInsertRow() throws SQLException {
this.inner.moveToInsertRow();
}

public void refreshRow() throws SQLException {
this.inner.refreshRow();
}

public boolean relative(int paramInt) throws SQLException {
return this.inner.relative(paramInt);
}

public boolean rowDeleted() throws SQLException {
return this.inner.rowDeleted();
}

public boolean rowInserted() throws SQLException {
return this.inner.rowInserted();
}

public boolean rowUpdated() throws SQLException {
return this.inner.rowUpdated();
}

public void setFetchDirection(int paramInt) throws SQLException {
this.inner.setFetchDirection(paramInt);
}

public void setFetchSize(int paramInt) throws SQLException {
this.inner.setFetchSize(paramInt);
}

public void updateArray(String paramString, Array paramArray) throws SQLException {
this.inner.updateArray(paramString, paramArray);
}

public void updateArray(int paramInt, Array paramArray) throws SQLException {
this.inner.updateArray(paramInt, paramArray);
}

public void updateAsciiStream(int paramInt, InputStream paramInputStream) throws SQLException {
this.inner.updateAsciiStream(paramInt, paramInputStream);
}

public void updateAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
this.inner.updateAsciiStream(paramInt1, paramInputStream, paramInt2);
}

public void updateAsciiStream(String paramString, InputStream paramInputStream) throws SQLException {
this.inner.updateAsciiStream(paramString, paramInputStream);
}

public void updateAsciiStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
this.inner.updateAsciiStream(paramString, paramInputStream, paramLong);
}

public void updateAsciiStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
this.inner.updateAsciiStream(paramString, paramInputStream, paramInt);
}

public void updateAsciiStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
this.inner.updateAsciiStream(paramInt, paramInputStream, paramLong);
}

public void updateBinaryStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
this.inner.updateBinaryStream(paramInt, paramInputStream, paramLong);
}

public void updateBinaryStream(String paramString, InputStream paramInputStream) throws SQLException {
this.inner.updateBinaryStream(paramString, paramInputStream);
}

public void updateBinaryStream(int paramInt, InputStream paramInputStream) throws SQLException {
this.inner.updateBinaryStream(paramInt, paramInputStream);
}

public void updateBinaryStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
this.inner.updateBinaryStream(paramString, paramInputStream, paramLong);
}

public void updateBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
this.inner.updateBinaryStream(paramInt1, paramInputStream, paramInt2);
}

public void updateBinaryStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
this.inner.updateBinaryStream(paramString, paramInputStream, paramInt);
}

public void updateBlob(int paramInt, Blob paramBlob) throws SQLException {
this.inner.updateBlob(paramInt, paramBlob);
}

public void updateBlob(String paramString, Blob paramBlob) throws SQLException {
this.inner.updateBlob(paramString, paramBlob);
}

public void updateBlob(String paramString, InputStream paramInputStream) throws SQLException {
this.inner.updateBlob(paramString, paramInputStream);
}

public void updateBlob(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
this.inner.updateBlob(paramString, paramInputStream, paramLong);
}

public void updateBlob(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
this.inner.updateBlob(paramInt, paramInputStream, paramLong);
}

public void updateBlob(int paramInt, InputStream paramInputStream) throws SQLException {
this.inner.updateBlob(paramInt, paramInputStream);
}

public void updateBoolean(String paramString, boolean paramBoolean) throws SQLException {
this.inner.updateBoolean(paramString, paramBoolean);
}

public void updateBoolean(int paramInt, boolean paramBoolean) throws SQLException {
this.inner.updateBoolean(paramInt, paramBoolean);
}

public void updateByte(String paramString, byte paramByte) throws SQLException {
this.inner.updateByte(paramString, paramByte);
}

public void updateByte(int paramInt, byte paramByte) throws SQLException {
this.inner.updateByte(paramInt, paramByte);
}

public void updateBytes(String paramString, byte[] paramArrayOfbyte) throws SQLException {
this.inner.updateBytes(paramString, paramArrayOfbyte);
}

public void updateBytes(int paramInt, byte[] paramArrayOfbyte) throws SQLException {
this.inner.updateBytes(paramInt, paramArrayOfbyte);
}

public void updateCharacterStream(String paramString, Reader paramReader) throws SQLException {
this.inner.updateCharacterStream(paramString, paramReader);
}

public void updateCharacterStream(String paramString, Reader paramReader, int paramInt) throws SQLException {
this.inner.updateCharacterStream(paramString, paramReader, paramInt);
}

public void updateCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
this.inner.updateCharacterStream(paramInt, paramReader, paramLong);
}

public void updateCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException {
this.inner.updateCharacterStream(paramString, paramReader, paramLong);
}

public void updateCharacterStream(int paramInt, Reader paramReader) throws SQLException {
this.inner.updateCharacterStream(paramInt, paramReader);
}

public void updateCharacterStream(int paramInt1, Reader paramReader, int paramInt2) throws SQLException {
this.inner.updateCharacterStream(paramInt1, paramReader, paramInt2);
}

public void updateClob(String paramString, Reader paramReader, long paramLong) throws SQLException {
this.inner.updateClob(paramString, paramReader, paramLong);
}

public void updateClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
this.inner.updateClob(paramInt, paramReader, paramLong);
}

public void updateClob(String paramString, Reader paramReader) throws SQLException {
this.inner.updateClob(paramString, paramReader);
}

public void updateClob(int paramInt, Reader paramReader) throws SQLException {
this.inner.updateClob(paramInt, paramReader);
}

public void updateClob(int paramInt, Clob paramClob) throws SQLException {
this.inner.updateClob(paramInt, paramClob);
}

public void updateClob(String paramString, Clob paramClob) throws SQLException {
this.inner.updateClob(paramString, paramClob);
}

public void updateDate(int paramInt, Date paramDate) throws SQLException {
this.inner.updateDate(paramInt, paramDate);
}

public void updateDate(String paramString, Date paramDate) throws SQLException {
this.inner.updateDate(paramString, paramDate);
}

public void updateDouble(int paramInt, double paramDouble) throws SQLException {
this.inner.updateDouble(paramInt, paramDouble);
}

public void updateDouble(String paramString, double paramDouble) throws SQLException {
this.inner.updateDouble(paramString, paramDouble);
}

public void updateFloat(String paramString, float paramFloat) throws SQLException {
this.inner.updateFloat(paramString, paramFloat);
}

public void updateFloat(int paramInt, float paramFloat) throws SQLException {
this.inner.updateFloat(paramInt, paramFloat);
}

public void updateInt(String paramString, int paramInt) throws SQLException {
this.inner.updateInt(paramString, paramInt);
}

public void updateInt(int paramInt1, int paramInt2) throws SQLException {
this.inner.updateInt(paramInt1, paramInt2);
}

public void updateLong(String paramString, long paramLong) throws SQLException {
this.inner.updateLong(paramString, paramLong);
}

public void updateLong(int paramInt, long paramLong) throws SQLException {
this.inner.updateLong(paramInt, paramLong);
}

public void updateNCharacterStream(int paramInt, Reader paramReader) throws SQLException {
this.inner.updateNCharacterStream(paramInt, paramReader);
}

public void updateNCharacterStream(String paramString, Reader paramReader) throws SQLException {
this.inner.updateNCharacterStream(paramString, paramReader);
}

public void updateNCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException {
this.inner.updateNCharacterStream(paramString, paramReader, paramLong);
}

public void updateNCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
this.inner.updateNCharacterStream(paramInt, paramReader, paramLong);
}

public void updateNClob(int paramInt, Reader paramReader) throws SQLException {
this.inner.updateNClob(paramInt, paramReader);
}

public void updateNClob(String paramString, Reader paramReader) throws SQLException {
this.inner.updateNClob(paramString, paramReader);
}

public void updateNClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
this.inner.updateNClob(paramInt, paramReader, paramLong);
}

public void updateNClob(int paramInt, NClob paramNClob) throws SQLException {
this.inner.updateNClob(paramInt, paramNClob);
}

public void updateNClob(String paramString, Reader paramReader, long paramLong) throws SQLException {
this.inner.updateNClob(paramString, paramReader, paramLong);
}

public void updateNClob(String paramString, NClob paramNClob) throws SQLException {
this.inner.updateNClob(paramString, paramNClob);
}

public void updateNString(String paramString1, String paramString2) throws SQLException {
this.inner.updateNString(paramString1, paramString2);
}

public void updateNString(int paramInt, String paramString) throws SQLException {
this.inner.updateNString(paramInt, paramString);
}

public void updateNull(int paramInt) throws SQLException {
this.inner.updateNull(paramInt);
}

public void updateNull(String paramString) throws SQLException {
this.inner.updateNull(paramString);
}

public void updateObject(int paramInt, Object paramObject) throws SQLException {
this.inner.updateObject(paramInt, paramObject);
}

public void updateObject(String paramString, Object paramObject) throws SQLException {
this.inner.updateObject(paramString, paramObject);
}

public void updateObject(String paramString, Object paramObject, int paramInt) throws SQLException {
this.inner.updateObject(paramString, paramObject, paramInt);
}

public void updateObject(int paramInt1, Object paramObject, int paramInt2) throws SQLException {
this.inner.updateObject(paramInt1, paramObject, paramInt2);
}

public void updateRef(int paramInt, Ref paramRef) throws SQLException {
this.inner.updateRef(paramInt, paramRef);
}

public void updateRef(String paramString, Ref paramRef) throws SQLException {
this.inner.updateRef(paramString, paramRef);
}

public void updateRow() throws SQLException {
this.inner.updateRow();
}

public void updateRowId(int paramInt, RowId paramRowId) throws SQLException {
this.inner.updateRowId(paramInt, paramRowId);
}

public void updateRowId(String paramString, RowId paramRowId) throws SQLException {
this.inner.updateRowId(paramString, paramRowId);
}

public void updateSQLXML(int paramInt, SQLXML paramSQLXML) throws SQLException {
this.inner.updateSQLXML(paramInt, paramSQLXML);
}

public void updateSQLXML(String paramString, SQLXML paramSQLXML) throws SQLException {
this.inner.updateSQLXML(paramString, paramSQLXML);
}

public void updateShort(String paramString, short paramShort) throws SQLException {
this.inner.updateShort(paramString, paramShort);
}

public void updateShort(int paramInt, short paramShort) throws SQLException {
this.inner.updateShort(paramInt, paramShort);
}

public void updateString(String paramString1, String paramString2) throws SQLException {
this.inner.updateString(paramString1, paramString2);
}

public void updateString(int paramInt, String paramString) throws SQLException {
this.inner.updateString(paramInt, paramString);
}

public void updateTime(String paramString, Time paramTime) throws SQLException {
this.inner.updateTime(paramString, paramTime);
}

public void updateTime(int paramInt, Time paramTime) throws SQLException {
this.inner.updateTime(paramInt, paramTime);
}

public void updateTimestamp(String paramString, Timestamp paramTimestamp) throws SQLException {
this.inner.updateTimestamp(paramString, paramTimestamp);
}

public void updateTimestamp(int paramInt, Timestamp paramTimestamp) throws SQLException {
this.inner.updateTimestamp(paramInt, paramTimestamp);
}

public boolean wasNull() throws SQLException {
return this.inner.wasNull();
}

public Object getObject(int paramInt, Class<?> paramClass) throws SQLException {
return this.inner.getObject(paramInt, paramClass);
}

public Object getObject(String paramString) throws SQLException {
return this.inner.getObject(paramString);
}

public Object getObject(String paramString, Class<?> paramClass) throws SQLException {
return this.inner.getObject(paramString, paramClass);
}

public Object getObject(int paramInt, Map<String, Class<?>> paramMap) throws SQLException {
return this.inner.getObject(paramInt, paramMap);
}

public Object getObject(String paramString, Map<String, Class<?>> paramMap) throws SQLException {
return this.inner.getObject(paramString, paramMap);
}

public Object getObject(int paramInt) throws SQLException {
return this.inner.getObject(paramInt);
}

public boolean getBoolean(String paramString) throws SQLException {
return this.inner.getBoolean(paramString);
}

public boolean getBoolean(int paramInt) throws SQLException {
return this.inner.getBoolean(paramInt);
}

public byte getByte(int paramInt) throws SQLException {
return this.inner.getByte(paramInt);
}

public byte getByte(String paramString) throws SQLException {
return this.inner.getByte(paramString);
}

public short getShort(String paramString) throws SQLException {
return this.inner.getShort(paramString);
}

public short getShort(int paramInt) throws SQLException {
return this.inner.getShort(paramInt);
}

public int getInt(String paramString) throws SQLException {
return this.inner.getInt(paramString);
}

public int getInt(int paramInt) throws SQLException {
return this.inner.getInt(paramInt);
}

public long getLong(String paramString) throws SQLException {
return this.inner.getLong(paramString);
}

public long getLong(int paramInt) throws SQLException {
return this.inner.getLong(paramInt);
}

public float getFloat(int paramInt) throws SQLException {
return this.inner.getFloat(paramInt);
}

public float getFloat(String paramString) throws SQLException {
return this.inner.getFloat(paramString);
}

public double getDouble(int paramInt) throws SQLException {
return this.inner.getDouble(paramInt);
}

public double getDouble(String paramString) throws SQLException {
return this.inner.getDouble(paramString);
}

public byte[] getBytes(String paramString) throws SQLException {
return this.inner.getBytes(paramString);
}

public byte[] getBytes(int paramInt) throws SQLException {
return this.inner.getBytes(paramInt);
}

public Array getArray(int paramInt) throws SQLException {
return this.inner.getArray(paramInt);
}

public Array getArray(String paramString) throws SQLException {
return this.inner.getArray(paramString);
}

public boolean next() throws SQLException {
return this.inner.next();
}

public URL getURL(int paramInt) throws SQLException {
return this.inner.getURL(paramInt);
}

public URL getURL(String paramString) throws SQLException {
return this.inner.getURL(paramString);
}

public void close() throws SQLException {
this.inner.close();
}

public int getType() throws SQLException {
return this.inner.getType();
}

public boolean previous() throws SQLException {
return this.inner.previous();
}

public Ref getRef(String paramString) throws SQLException {
return this.inner.getRef(paramString);
}

public Ref getRef(int paramInt) throws SQLException {
return this.inner.getRef(paramInt);
}

public String getString(int paramInt) throws SQLException {
return this.inner.getString(paramInt);
}

public String getString(String paramString) throws SQLException {
return this.inner.getString(paramString);
}

public Date getDate(int paramInt, Calendar paramCalendar) throws SQLException {
return this.inner.getDate(paramInt, paramCalendar);
}

public Date getDate(String paramString, Calendar paramCalendar) throws SQLException {
return this.inner.getDate(paramString, paramCalendar);
}

public Date getDate(String paramString) throws SQLException {
return this.inner.getDate(paramString);
}

public Date getDate(int paramInt) throws SQLException {
return this.inner.getDate(paramInt);
}

public Time getTime(int paramInt) throws SQLException {
return this.inner.getTime(paramInt);
}

public Time getTime(String paramString, Calendar paramCalendar) throws SQLException {
return this.inner.getTime(paramString, paramCalendar);
}

public Time getTime(String paramString) throws SQLException {
return this.inner.getTime(paramString);
}

public Time getTime(int paramInt, Calendar paramCalendar) throws SQLException {
return this.inner.getTime(paramInt, paramCalendar);
}

public Timestamp getTimestamp(int paramInt) throws SQLException {
return this.inner.getTimestamp(paramInt);
}

public Timestamp getTimestamp(String paramString) throws SQLException {
return this.inner.getTimestamp(paramString);
}

public Timestamp getTimestamp(String paramString, Calendar paramCalendar) throws SQLException {
return this.inner.getTimestamp(paramString, paramCalendar);
}

public Timestamp getTimestamp(int paramInt, Calendar paramCalendar) throws SQLException {
return this.inner.getTimestamp(paramInt, paramCalendar);
}

public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
return this.inner.isWrapperFor(paramClass);
}

public Object unwrap(Class<?> paramClass) throws SQLException {
return this.inner.unwrap(paramClass);
}
}

