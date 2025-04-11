package com.mchange.v2.sql.filter;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

public abstract class SynchronizedFilterResultSet
        implements ResultSet {
    protected ResultSet inner;

    public SynchronizedFilterResultSet(ResultSet paramResultSet) {
        __setInner(paramResultSet);
    }

    public SynchronizedFilterResultSet() {
    }

    private void __setInner(ResultSet paramResultSet) {
        this.inner = paramResultSet;
    }

    public synchronized ResultSet getInner() {
        return this.inner;
    }

    public synchronized void setInner(ResultSet paramResultSet) {
        __setInner(paramResultSet);
    }

    public synchronized void clearWarnings() throws SQLException {
        this.inner.clearWarnings();
    }

    public synchronized int getHoldability() throws SQLException {
        return this.inner.getHoldability();
    }

    public synchronized ResultSetMetaData getMetaData() throws SQLException {
        return this.inner.getMetaData();
    }

    public synchronized SQLWarning getWarnings() throws SQLException {
        return this.inner.getWarnings();
    }

    public synchronized boolean isClosed() throws SQLException {
        return this.inner.isClosed();
    }

    public synchronized void updateBigDecimal(int paramInt, BigDecimal paramBigDecimal) throws SQLException {
        this.inner.updateBigDecimal(paramInt, paramBigDecimal);
    }

    public synchronized void updateBigDecimal(String paramString, BigDecimal paramBigDecimal) throws SQLException {
        this.inner.updateBigDecimal(paramString, paramBigDecimal);
    }

    public synchronized boolean absolute(int paramInt) throws SQLException {
        return this.inner.absolute(paramInt);
    }

    public synchronized void afterLast() throws SQLException {
        this.inner.afterLast();
    }

    public synchronized void beforeFirst() throws SQLException {
        this.inner.beforeFirst();
    }

    public synchronized void cancelRowUpdates() throws SQLException {
        this.inner.cancelRowUpdates();
    }

    public synchronized void deleteRow() throws SQLException {
        this.inner.deleteRow();
    }

    public synchronized int findColumn(String paramString) throws SQLException {
        return this.inner.findColumn(paramString);
    }

    public synchronized boolean first() throws SQLException {
        return this.inner.first();
    }

    public synchronized InputStream getAsciiStream(int paramInt) throws SQLException {
        return this.inner.getAsciiStream(paramInt);
    }

    public synchronized InputStream getAsciiStream(String paramString) throws SQLException {
        return this.inner.getAsciiStream(paramString);
    }

    public synchronized BigDecimal getBigDecimal(String paramString, int paramInt) throws SQLException {
        return this.inner.getBigDecimal(paramString, paramInt);
    }

    public synchronized BigDecimal getBigDecimal(String paramString) throws SQLException {
        return this.inner.getBigDecimal(paramString);
    }

    public synchronized BigDecimal getBigDecimal(int paramInt) throws SQLException {
        return this.inner.getBigDecimal(paramInt);
    }

    public synchronized BigDecimal getBigDecimal(int paramInt1, int paramInt2) throws SQLException {
        return this.inner.getBigDecimal(paramInt1, paramInt2);
    }

    public synchronized InputStream getBinaryStream(String paramString) throws SQLException {
        return this.inner.getBinaryStream(paramString);
    }

    public synchronized InputStream getBinaryStream(int paramInt) throws SQLException {
        return this.inner.getBinaryStream(paramInt);
    }

    public synchronized Blob getBlob(String paramString) throws SQLException {
        return this.inner.getBlob(paramString);
    }

    public synchronized Blob getBlob(int paramInt) throws SQLException {
        return this.inner.getBlob(paramInt);
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

    public synchronized int getConcurrency() throws SQLException {
        return this.inner.getConcurrency();
    }

    public synchronized String getCursorName() throws SQLException {
        return this.inner.getCursorName();
    }

    public synchronized int getFetchDirection() throws SQLException {
        return this.inner.getFetchDirection();
    }

    public synchronized void setFetchDirection(int paramInt) throws SQLException {
        this.inner.setFetchDirection(paramInt);
    }

    public synchronized int getFetchSize() throws SQLException {
        return this.inner.getFetchSize();
    }

    public synchronized void setFetchSize(int paramInt) throws SQLException {
        this.inner.setFetchSize(paramInt);
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

    public synchronized int getRow() throws SQLException {
        return this.inner.getRow();
    }

    public synchronized RowId getRowId(int paramInt) throws SQLException {
        return this.inner.getRowId(paramInt);
    }

    public synchronized RowId getRowId(String paramString) throws SQLException {
        return this.inner.getRowId(paramString);
    }

    public synchronized SQLXML getSQLXML(String paramString) throws SQLException {
        return this.inner.getSQLXML(paramString);
    }

    public synchronized SQLXML getSQLXML(int paramInt) throws SQLException {
        return this.inner.getSQLXML(paramInt);
    }

    public synchronized Statement getStatement() throws SQLException {
        return this.inner.getStatement();
    }

    public synchronized InputStream getUnicodeStream(int paramInt) throws SQLException {
        return this.inner.getUnicodeStream(paramInt);
    }

    public synchronized InputStream getUnicodeStream(String paramString) throws SQLException {
        return this.inner.getUnicodeStream(paramString);
    }

    public synchronized void insertRow() throws SQLException {
        this.inner.insertRow();
    }

    public synchronized boolean isAfterLast() throws SQLException {
        return this.inner.isAfterLast();
    }

    public synchronized boolean isBeforeFirst() throws SQLException {
        return this.inner.isBeforeFirst();
    }

    public synchronized boolean isFirst() throws SQLException {
        return this.inner.isFirst();
    }

    public synchronized boolean isLast() throws SQLException {
        return this.inner.isLast();
    }

    public synchronized boolean last() throws SQLException {
        return this.inner.last();
    }

    public synchronized void moveToCurrentRow() throws SQLException {
        this.inner.moveToCurrentRow();
    }

    public synchronized void moveToInsertRow() throws SQLException {
        this.inner.moveToInsertRow();
    }

    public synchronized void refreshRow() throws SQLException {
        this.inner.refreshRow();
    }

    public synchronized boolean relative(int paramInt) throws SQLException {
        return this.inner.relative(paramInt);
    }

    public synchronized boolean rowDeleted() throws SQLException {
        return this.inner.rowDeleted();
    }

    public synchronized boolean rowInserted() throws SQLException {
        return this.inner.rowInserted();
    }

    public synchronized boolean rowUpdated() throws SQLException {
        return this.inner.rowUpdated();
    }

    public synchronized void updateArray(String paramString, Array paramArray) throws SQLException {
        this.inner.updateArray(paramString, paramArray);
    }

    public synchronized void updateArray(int paramInt, Array paramArray) throws SQLException {
        this.inner.updateArray(paramInt, paramArray);
    }

    public synchronized void updateAsciiStream(int paramInt, InputStream paramInputStream) throws SQLException {
        this.inner.updateAsciiStream(paramInt, paramInputStream);
    }

    public synchronized void updateAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
        this.inner.updateAsciiStream(paramInt1, paramInputStream, paramInt2);
    }

    public synchronized void updateAsciiStream(String paramString, InputStream paramInputStream) throws SQLException {
        this.inner.updateAsciiStream(paramString, paramInputStream);
    }

    public synchronized void updateAsciiStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
        this.inner.updateAsciiStream(paramString, paramInputStream, paramLong);
    }

    public synchronized void updateAsciiStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
        this.inner.updateAsciiStream(paramString, paramInputStream, paramInt);
    }

    public synchronized void updateAsciiStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
        this.inner.updateAsciiStream(paramInt, paramInputStream, paramLong);
    }

    public synchronized void updateBinaryStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
        this.inner.updateBinaryStream(paramInt, paramInputStream, paramLong);
    }

    public synchronized void updateBinaryStream(String paramString, InputStream paramInputStream) throws SQLException {
        this.inner.updateBinaryStream(paramString, paramInputStream);
    }

    public synchronized void updateBinaryStream(int paramInt, InputStream paramInputStream) throws SQLException {
        this.inner.updateBinaryStream(paramInt, paramInputStream);
    }

    public synchronized void updateBinaryStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
        this.inner.updateBinaryStream(paramString, paramInputStream, paramLong);
    }

    public synchronized void updateBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
        this.inner.updateBinaryStream(paramInt1, paramInputStream, paramInt2);
    }

    public synchronized void updateBinaryStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
        this.inner.updateBinaryStream(paramString, paramInputStream, paramInt);
    }

    public synchronized void updateBlob(int paramInt, Blob paramBlob) throws SQLException {
        this.inner.updateBlob(paramInt, paramBlob);
    }

    public synchronized void updateBlob(String paramString, Blob paramBlob) throws SQLException {
        this.inner.updateBlob(paramString, paramBlob);
    }

    public synchronized void updateBlob(String paramString, InputStream paramInputStream) throws SQLException {
        this.inner.updateBlob(paramString, paramInputStream);
    }

    public synchronized void updateBlob(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
        this.inner.updateBlob(paramString, paramInputStream, paramLong);
    }

    public synchronized void updateBlob(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
        this.inner.updateBlob(paramInt, paramInputStream, paramLong);
    }

    public synchronized void updateBlob(int paramInt, InputStream paramInputStream) throws SQLException {
        this.inner.updateBlob(paramInt, paramInputStream);
    }

    public synchronized void updateBoolean(String paramString, boolean paramBoolean) throws SQLException {
        this.inner.updateBoolean(paramString, paramBoolean);
    }

    public synchronized void updateBoolean(int paramInt, boolean paramBoolean) throws SQLException {
        this.inner.updateBoolean(paramInt, paramBoolean);
    }

    public synchronized void updateByte(String paramString, byte paramByte) throws SQLException {
        this.inner.updateByte(paramString, paramByte);
    }

    public synchronized void updateByte(int paramInt, byte paramByte) throws SQLException {
        this.inner.updateByte(paramInt, paramByte);
    }

    public synchronized void updateBytes(String paramString, byte[] paramArrayOfbyte) throws SQLException {
        this.inner.updateBytes(paramString, paramArrayOfbyte);
    }

    public synchronized void updateBytes(int paramInt, byte[] paramArrayOfbyte) throws SQLException {
        this.inner.updateBytes(paramInt, paramArrayOfbyte);
    }

    public synchronized void updateCharacterStream(String paramString, Reader paramReader) throws SQLException {
        this.inner.updateCharacterStream(paramString, paramReader);
    }

    public synchronized void updateCharacterStream(String paramString, Reader paramReader, int paramInt) throws SQLException {
        this.inner.updateCharacterStream(paramString, paramReader, paramInt);
    }

    public synchronized void updateCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
        this.inner.updateCharacterStream(paramInt, paramReader, paramLong);
    }

    public synchronized void updateCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException {
        this.inner.updateCharacterStream(paramString, paramReader, paramLong);
    }

    public synchronized void updateCharacterStream(int paramInt, Reader paramReader) throws SQLException {
        this.inner.updateCharacterStream(paramInt, paramReader);
    }

    public synchronized void updateCharacterStream(int paramInt1, Reader paramReader, int paramInt2) throws SQLException {
        this.inner.updateCharacterStream(paramInt1, paramReader, paramInt2);
    }

    public synchronized void updateClob(String paramString, Reader paramReader, long paramLong) throws SQLException {
        this.inner.updateClob(paramString, paramReader, paramLong);
    }

    public synchronized void updateClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
        this.inner.updateClob(paramInt, paramReader, paramLong);
    }

    public synchronized void updateClob(String paramString, Reader paramReader) throws SQLException {
        this.inner.updateClob(paramString, paramReader);
    }

    public synchronized void updateClob(int paramInt, Reader paramReader) throws SQLException {
        this.inner.updateClob(paramInt, paramReader);
    }

    public synchronized void updateClob(int paramInt, Clob paramClob) throws SQLException {
        this.inner.updateClob(paramInt, paramClob);
    }

    public synchronized void updateClob(String paramString, Clob paramClob) throws SQLException {
        this.inner.updateClob(paramString, paramClob);
    }

    public synchronized void updateDate(int paramInt, Date paramDate) throws SQLException {
        this.inner.updateDate(paramInt, paramDate);
    }

    public synchronized void updateDate(String paramString, Date paramDate) throws SQLException {
        this.inner.updateDate(paramString, paramDate);
    }

    public synchronized void updateDouble(int paramInt, double paramDouble) throws SQLException {
        this.inner.updateDouble(paramInt, paramDouble);
    }

    public synchronized void updateDouble(String paramString, double paramDouble) throws SQLException {
        this.inner.updateDouble(paramString, paramDouble);
    }

    public synchronized void updateFloat(String paramString, float paramFloat) throws SQLException {
        this.inner.updateFloat(paramString, paramFloat);
    }

    public synchronized void updateFloat(int paramInt, float paramFloat) throws SQLException {
        this.inner.updateFloat(paramInt, paramFloat);
    }

    public synchronized void updateInt(String paramString, int paramInt) throws SQLException {
        this.inner.updateInt(paramString, paramInt);
    }

    public synchronized void updateInt(int paramInt1, int paramInt2) throws SQLException {
        this.inner.updateInt(paramInt1, paramInt2);
    }

    public synchronized void updateLong(String paramString, long paramLong) throws SQLException {
        this.inner.updateLong(paramString, paramLong);
    }

    public synchronized void updateLong(int paramInt, long paramLong) throws SQLException {
        this.inner.updateLong(paramInt, paramLong);
    }

    public synchronized void updateNCharacterStream(int paramInt, Reader paramReader) throws SQLException {
        this.inner.updateNCharacterStream(paramInt, paramReader);
    }

    public synchronized void updateNCharacterStream(String paramString, Reader paramReader) throws SQLException {
        this.inner.updateNCharacterStream(paramString, paramReader);
    }

    public synchronized void updateNCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException {
        this.inner.updateNCharacterStream(paramString, paramReader, paramLong);
    }

    public synchronized void updateNCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
        this.inner.updateNCharacterStream(paramInt, paramReader, paramLong);
    }

    public synchronized void updateNClob(int paramInt, Reader paramReader) throws SQLException {
        this.inner.updateNClob(paramInt, paramReader);
    }

    public synchronized void updateNClob(String paramString, Reader paramReader) throws SQLException {
        this.inner.updateNClob(paramString, paramReader);
    }

    public synchronized void updateNClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
        this.inner.updateNClob(paramInt, paramReader, paramLong);
    }

    public synchronized void updateNClob(int paramInt, NClob paramNClob) throws SQLException {
        this.inner.updateNClob(paramInt, paramNClob);
    }

    public synchronized void updateNClob(String paramString, Reader paramReader, long paramLong) throws SQLException {
        this.inner.updateNClob(paramString, paramReader, paramLong);
    }

    public synchronized void updateNClob(String paramString, NClob paramNClob) throws SQLException {
        this.inner.updateNClob(paramString, paramNClob);
    }

    public synchronized void updateNString(String paramString1, String paramString2) throws SQLException {
        this.inner.updateNString(paramString1, paramString2);
    }

    public synchronized void updateNString(int paramInt, String paramString) throws SQLException {
        this.inner.updateNString(paramInt, paramString);
    }

    public synchronized void updateNull(int paramInt) throws SQLException {
        this.inner.updateNull(paramInt);
    }

    public synchronized void updateNull(String paramString) throws SQLException {
        this.inner.updateNull(paramString);
    }

    public synchronized void updateObject(int paramInt, Object paramObject) throws SQLException {
        this.inner.updateObject(paramInt, paramObject);
    }

    public synchronized void updateObject(String paramString, Object paramObject) throws SQLException {
        this.inner.updateObject(paramString, paramObject);
    }

    public synchronized void updateObject(String paramString, Object paramObject, int paramInt) throws SQLException {
        this.inner.updateObject(paramString, paramObject, paramInt);
    }

    public synchronized void updateObject(int paramInt1, Object paramObject, int paramInt2) throws SQLException {
        this.inner.updateObject(paramInt1, paramObject, paramInt2);
    }

    public synchronized void updateRef(int paramInt, Ref paramRef) throws SQLException {
        this.inner.updateRef(paramInt, paramRef);
    }

    public synchronized void updateRef(String paramString, Ref paramRef) throws SQLException {
        this.inner.updateRef(paramString, paramRef);
    }

    public synchronized void updateRow() throws SQLException {
        this.inner.updateRow();
    }

    public synchronized void updateRowId(int paramInt, RowId paramRowId) throws SQLException {
        this.inner.updateRowId(paramInt, paramRowId);
    }

    public synchronized void updateRowId(String paramString, RowId paramRowId) throws SQLException {
        this.inner.updateRowId(paramString, paramRowId);
    }

    public synchronized void updateSQLXML(int paramInt, SQLXML paramSQLXML) throws SQLException {
        this.inner.updateSQLXML(paramInt, paramSQLXML);
    }

    public synchronized void updateSQLXML(String paramString, SQLXML paramSQLXML) throws SQLException {
        this.inner.updateSQLXML(paramString, paramSQLXML);
    }

    public synchronized void updateShort(String paramString, short paramShort) throws SQLException {
        this.inner.updateShort(paramString, paramShort);
    }

    public synchronized void updateShort(int paramInt, short paramShort) throws SQLException {
        this.inner.updateShort(paramInt, paramShort);
    }

    public synchronized void updateString(String paramString1, String paramString2) throws SQLException {
        this.inner.updateString(paramString1, paramString2);
    }

    public synchronized void updateString(int paramInt, String paramString) throws SQLException {
        this.inner.updateString(paramInt, paramString);
    }

    public synchronized void updateTime(String paramString, Time paramTime) throws SQLException {
        this.inner.updateTime(paramString, paramTime);
    }

    public synchronized void updateTime(int paramInt, Time paramTime) throws SQLException {
        this.inner.updateTime(paramInt, paramTime);
    }

    public synchronized void updateTimestamp(String paramString, Timestamp paramTimestamp) throws SQLException {
        this.inner.updateTimestamp(paramString, paramTimestamp);
    }

    public synchronized void updateTimestamp(int paramInt, Timestamp paramTimestamp) throws SQLException {
        this.inner.updateTimestamp(paramInt, paramTimestamp);
    }

    public synchronized boolean wasNull() throws SQLException {
        return this.inner.wasNull();
    }

    public synchronized Object getObject(int paramInt, Class<?> paramClass) throws SQLException {
        return this.inner.getObject(paramInt, paramClass);
    }

    public synchronized Object getObject(String paramString) throws SQLException {
        return this.inner.getObject(paramString);
    }

    public synchronized Object getObject(String paramString, Class<?> paramClass) throws SQLException {
        return this.inner.getObject(paramString, paramClass);
    }

    public synchronized Object getObject(int paramInt, Map<String, Class<?>> paramMap) throws SQLException {
        return this.inner.getObject(paramInt, paramMap);
    }

    public synchronized Object getObject(String paramString, Map<String, Class<?>> paramMap) throws SQLException {
        return this.inner.getObject(paramString, paramMap);
    }

    public synchronized Object getObject(int paramInt) throws SQLException {
        return this.inner.getObject(paramInt);
    }

    public synchronized boolean getBoolean(String paramString) throws SQLException {
        return this.inner.getBoolean(paramString);
    }

    public synchronized boolean getBoolean(int paramInt) throws SQLException {
        return this.inner.getBoolean(paramInt);
    }

    public synchronized byte getByte(int paramInt) throws SQLException {
        return this.inner.getByte(paramInt);
    }

    public synchronized byte getByte(String paramString) throws SQLException {
        return this.inner.getByte(paramString);
    }

    public synchronized short getShort(String paramString) throws SQLException {
        return this.inner.getShort(paramString);
    }

    public synchronized short getShort(int paramInt) throws SQLException {
        return this.inner.getShort(paramInt);
    }

    public synchronized int getInt(String paramString) throws SQLException {
        return this.inner.getInt(paramString);
    }

    public synchronized int getInt(int paramInt) throws SQLException {
        return this.inner.getInt(paramInt);
    }

    public synchronized long getLong(String paramString) throws SQLException {
        return this.inner.getLong(paramString);
    }

    public synchronized long getLong(int paramInt) throws SQLException {
        return this.inner.getLong(paramInt);
    }

    public synchronized float getFloat(int paramInt) throws SQLException {
        return this.inner.getFloat(paramInt);
    }

    public synchronized float getFloat(String paramString) throws SQLException {
        return this.inner.getFloat(paramString);
    }

    public synchronized double getDouble(int paramInt) throws SQLException {
        return this.inner.getDouble(paramInt);
    }

    public synchronized double getDouble(String paramString) throws SQLException {
        return this.inner.getDouble(paramString);
    }

    public synchronized byte[] getBytes(String paramString) throws SQLException {
        return this.inner.getBytes(paramString);
    }

    public synchronized byte[] getBytes(int paramInt) throws SQLException {
        return this.inner.getBytes(paramInt);
    }

    public synchronized Array getArray(int paramInt) throws SQLException {
        return this.inner.getArray(paramInt);
    }

    public synchronized Array getArray(String paramString) throws SQLException {
        return this.inner.getArray(paramString);
    }

    public synchronized boolean next() throws SQLException {
        return this.inner.next();
    }

    public synchronized URL getURL(int paramInt) throws SQLException {
        return this.inner.getURL(paramInt);
    }

    public synchronized URL getURL(String paramString) throws SQLException {
        return this.inner.getURL(paramString);
    }

    public synchronized void close() throws SQLException {
        this.inner.close();
    }

    public synchronized int getType() throws SQLException {
        return this.inner.getType();
    }

    public synchronized boolean previous() throws SQLException {
        return this.inner.previous();
    }

    public synchronized Ref getRef(String paramString) throws SQLException {
        return this.inner.getRef(paramString);
    }

    public synchronized Ref getRef(int paramInt) throws SQLException {
        return this.inner.getRef(paramInt);
    }

    public synchronized String getString(int paramInt) throws SQLException {
        return this.inner.getString(paramInt);
    }

    public synchronized String getString(String paramString) throws SQLException {
        return this.inner.getString(paramString);
    }

    public synchronized Date getDate(int paramInt, Calendar paramCalendar) throws SQLException {
        return this.inner.getDate(paramInt, paramCalendar);
    }

    public synchronized Date getDate(String paramString, Calendar paramCalendar) throws SQLException {
        return this.inner.getDate(paramString, paramCalendar);
    }

    public synchronized Date getDate(String paramString) throws SQLException {
        return this.inner.getDate(paramString);
    }

    public synchronized Date getDate(int paramInt) throws SQLException {
        return this.inner.getDate(paramInt);
    }

    public synchronized Time getTime(int paramInt) throws SQLException {
        return this.inner.getTime(paramInt);
    }

    public synchronized Time getTime(String paramString, Calendar paramCalendar) throws SQLException {
        return this.inner.getTime(paramString, paramCalendar);
    }

    public synchronized Time getTime(String paramString) throws SQLException {
        return this.inner.getTime(paramString);
    }

    public synchronized Time getTime(int paramInt, Calendar paramCalendar) throws SQLException {
        return this.inner.getTime(paramInt, paramCalendar);
    }

    public synchronized Timestamp getTimestamp(int paramInt) throws SQLException {
        return this.inner.getTimestamp(paramInt);
    }

    public synchronized Timestamp getTimestamp(String paramString) throws SQLException {
        return this.inner.getTimestamp(paramString);
    }

    public synchronized Timestamp getTimestamp(String paramString, Calendar paramCalendar) throws SQLException {
        return this.inner.getTimestamp(paramString, paramCalendar);
    }

    public synchronized Timestamp getTimestamp(int paramInt, Calendar paramCalendar) throws SQLException {
        return this.inner.getTimestamp(paramInt, paramCalendar);
    }

    public synchronized boolean isWrapperFor(Class<?> paramClass) throws SQLException {
        return this.inner.isWrapperFor(paramClass);
    }

    public synchronized Object unwrap(Class<?> paramClass) throws SQLException {
        return this.inner.unwrap(paramClass);
    }
}

