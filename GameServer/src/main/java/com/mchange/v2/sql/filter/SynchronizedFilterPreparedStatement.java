package com.mchange.v2.sql.filter;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

public abstract class SynchronizedFilterPreparedStatement
        implements PreparedStatement {
    protected PreparedStatement inner;

    public SynchronizedFilterPreparedStatement(PreparedStatement paramPreparedStatement) {
        __setInner(paramPreparedStatement);
    }

    public SynchronizedFilterPreparedStatement() {
    }

    private void __setInner(PreparedStatement paramPreparedStatement) {
        this.inner = paramPreparedStatement;
    }

    public synchronized PreparedStatement getInner() {
        return this.inner;
    }

    public synchronized void setInner(PreparedStatement paramPreparedStatement) {
        __setInner(paramPreparedStatement);
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

    public synchronized void setFetchDirection(int paramInt) throws SQLException {
        this.inner.setFetchDirection(paramInt);
    }

    public synchronized int getFetchSize() throws SQLException {
        return this.inner.getFetchSize();
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

    public synchronized void setMaxFieldSize(int paramInt) throws SQLException {
        this.inner.setMaxFieldSize(paramInt);
    }

    public synchronized int getMaxRows() throws SQLException {
        return this.inner.getMaxRows();
    }

    public synchronized void setMaxRows(int paramInt) throws SQLException {
        this.inner.setMaxRows(paramInt);
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

    public synchronized void setQueryTimeout(int paramInt) throws SQLException {
        this.inner.setQueryTimeout(paramInt);
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

    public synchronized void setPoolable(boolean paramBoolean) throws SQLException {
        this.inner.setPoolable(paramBoolean);
    }

    public synchronized void setCursorName(String paramString) throws SQLException {
        this.inner.setCursorName(paramString);
    }

    public synchronized void setEscapeProcessing(boolean paramBoolean) throws SQLException {
        this.inner.setEscapeProcessing(paramBoolean);
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

