package com.mchange.v2.sql.filter;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

public abstract class FilterPreparedStatement
        implements PreparedStatement {
    protected PreparedStatement inner;

    public FilterPreparedStatement(PreparedStatement paramPreparedStatement) {
        __setInner(paramPreparedStatement);
    }

    public FilterPreparedStatement() {
    }

    private void __setInner(PreparedStatement paramPreparedStatement) {
        this.inner = paramPreparedStatement;
    }

    public PreparedStatement getInner() {
        return this.inner;
    }

    public void setInner(PreparedStatement paramPreparedStatement) {
        __setInner(paramPreparedStatement);
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

    public void setFetchDirection(int paramInt) throws SQLException {
        this.inner.setFetchDirection(paramInt);
    }

    public int getFetchSize() throws SQLException {
        return this.inner.getFetchSize();
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

    public void setMaxFieldSize(int paramInt) throws SQLException {
        this.inner.setMaxFieldSize(paramInt);
    }

    public int getMaxRows() throws SQLException {
        return this.inner.getMaxRows();
    }

    public void setMaxRows(int paramInt) throws SQLException {
        this.inner.setMaxRows(paramInt);
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

    public void setQueryTimeout(int paramInt) throws SQLException {
        this.inner.setQueryTimeout(paramInt);
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

    public void setPoolable(boolean paramBoolean) throws SQLException {
        this.inner.setPoolable(paramBoolean);
    }

    public void setCursorName(String paramString) throws SQLException {
        this.inner.setCursorName(paramString);
    }

    public void setEscapeProcessing(boolean paramBoolean) throws SQLException {
        this.inner.setEscapeProcessing(paramBoolean);
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

