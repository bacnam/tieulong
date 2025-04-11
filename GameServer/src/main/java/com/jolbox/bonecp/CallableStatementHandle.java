package com.jolbox.bonecp;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

public class CallableStatementHandle
        extends PreparedStatementHandle
        implements CallableStatement {
    private CallableStatement internalCallableStatement;

    public CallableStatementHandle(CallableStatement internalCallableStatement, String sql, ConnectionHandle connectionHandle, String cacheKey, IStatementCache cache) {
        super(internalCallableStatement, sql, connectionHandle, cacheKey, cache);
        this.internalCallableStatement = internalCallableStatement;
        this.connectionHandle = connectionHandle;
        this.sql = sql;
        this.cache = cache;
    }

    public Array getArray(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getArray(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Array getArray(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getArray(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getBigDecimal(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getBigDecimal(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    @Deprecated
    public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getBigDecimal(parameterIndex, scale);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Blob getBlob(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getBlob(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Blob getBlob(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getBlob(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public boolean getBoolean(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getBoolean(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public boolean getBoolean(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getBoolean(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public byte getByte(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getByte(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public byte getByte(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getByte(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public byte[] getBytes(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getBytes(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public byte[] getBytes(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getBytes(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Reader getCharacterStream(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getCharacterStream(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Reader getCharacterStream(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getCharacterStream(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Reader getNCharacterStream(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getNCharacterStream(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Reader getNCharacterStream(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getNCharacterStream(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public NClob getNClob(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getNClob(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public NClob getNClob(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getNClob(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public String getNString(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getNString(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public String getNString(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getNString(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public RowId getRowId(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getRowId(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public RowId getRowId(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getRowId(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public SQLXML getSQLXML(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getSQLXML(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public SQLXML getSQLXML(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getSQLXML(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setAsciiStream(parameterName, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setAsciiStream(parameterName, x, length);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setBinaryStream(parameterName, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setBinaryStream(parameterName, x, length);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setBlob(String parameterName, Blob x) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setBlob(parameterName, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setBlob(parameterName, inputStream);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, inputStream);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setBlob(parameterName, inputStream, length);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, inputStream);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setCharacterStream(parameterName, reader);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, reader);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setCharacterStream(parameterName, reader, length);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, reader);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setClob(String parameterName, Clob x) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setClob(parameterName, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setClob(String parameterName, Reader reader) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setClob(parameterName, reader);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, reader);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setClob(String parameterName, Reader reader, long length) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setClob(parameterName, reader, length);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, reader);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setNCharacterStream(parameterName, value);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, value);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setNCharacterStream(parameterName, value, length);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, value);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setNClob(String parameterName, NClob value) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setNClob(parameterName, value);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, value);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setNClob(String parameterName, Reader reader) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setNClob(parameterName, reader);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, reader);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setNClob(parameterName, reader, length);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, reader);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setNString(String parameterName, String value) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setNString(parameterName, value);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, value);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setRowId(String parameterName, RowId x) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setRowId(parameterName, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setSQLXML(parameterName, xmlObject);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, xmlObject);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Clob getClob(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getClob(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Clob getClob(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getClob(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Date getDate(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getDate(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Date getDate(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getDate(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getDate(parameterIndex, cal);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Date getDate(String parameterName, Calendar cal) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getDate(parameterName, cal);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public double getDouble(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getDouble(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public double getDouble(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getDouble(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public float getFloat(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getFloat(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public float getFloat(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getFloat(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public int getInt(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getInt(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public int getInt(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getInt(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public long getLong(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getLong(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public long getLong(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getLong(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Object getObject(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getObject(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Object getObject(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getObject(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getObject(parameterIndex, map);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getObject(parameterName, map);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Ref getRef(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getRef(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Ref getRef(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getRef(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public short getShort(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getShort(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public short getShort(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getShort(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public String getString(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getString(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public String getString(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getString(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Time getTime(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getTime(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Time getTime(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getTime(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getTime(parameterIndex, cal);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Time getTime(String parameterName, Calendar cal) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getTime(parameterName, cal);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getTimestamp(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Timestamp getTimestamp(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getTimestamp(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getTimestamp(parameterIndex, cal);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getTimestamp(parameterName, cal);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public URL getURL(int parameterIndex) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getURL(parameterIndex);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public URL getURL(String parameterName) throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.getURL(parameterName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.registerOutParameter(parameterIndex, sqlType);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.registerOutParameter(parameterName, sqlType);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.registerOutParameter(parameterIndex, sqlType, scale);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.registerOutParameter(parameterIndex, sqlType, typeName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.registerOutParameter(parameterName, sqlType, scale);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.registerOutParameter(parameterName, sqlType, typeName);
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setAsciiStream(parameterName, x, length);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setBigDecimal(parameterName, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setBinaryStream(parameterName, x, length);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setBoolean(String parameterName, boolean x) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setBoolean(parameterName, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, Boolean.valueOf(x));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setByte(String parameterName, byte x) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setByte(parameterName, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, Byte.valueOf(x));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setBytes(String parameterName, byte[] x) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setBytes(parameterName, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setCharacterStream(parameterName, reader, length);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, reader);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setDate(String parameterName, Date x) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setDate(parameterName, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setDate(parameterName, x, cal);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, PoolUtil.safePrint(new Object[]{x, ", cal=", cal}));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setDouble(String parameterName, double x) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setDouble(parameterName, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, Double.valueOf(x));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setFloat(String parameterName, float x) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setFloat(parameterName, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, Float.valueOf(x));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setInt(String parameterName, int x) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setInt(parameterName, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, Integer.valueOf(x));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setLong(String parameterName, long x) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setLong(parameterName, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, Long.valueOf(x));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setNull(String parameterName, int sqlType) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setNull(parameterName, sqlType);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, PoolUtil.safePrint(new Object[]{"[SQL NULL type ", Integer.valueOf(sqlType), "]"}));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setNull(parameterName, sqlType, typeName);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, PoolUtil.safePrint(new Object[]{"[SQL NULL type ", Integer.valueOf(sqlType), ", type=", typeName + "]"}));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setObject(String parameterName, Object x) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setObject(parameterName, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setObject(parameterName, x, targetSqlType);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setObject(parameterName, x, targetSqlType, scale);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setShort(String parameterName, short x) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setShort(parameterName, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, Short.valueOf(x));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setString(String parameterName, String x) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setString(parameterName, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setTime(String parameterName, Time x) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setTime(parameterName, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setTime(parameterName, x, cal);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, PoolUtil.safePrint(new Object[]{x, ", cal=", cal}));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setTimestamp(parameterName, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setTimestamp(parameterName, x, cal);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, PoolUtil.safePrint(new Object[]{x, ", cal=", cal}));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setURL(String parameterName, URL val) throws SQLException {
        checkClosed();
        try {
            this.internalCallableStatement.setURL(parameterName, val);
            if (this.logStatementsEnabled) {
                this.logParams.put(parameterName, val);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public boolean wasNull() throws SQLException {
        checkClosed();
        try {
            return this.internalCallableStatement.wasNull();
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public CallableStatement getInternalCallableStatement() {
        return this.internalCallableStatement;
    }

    public void setInternalCallableStatement(CallableStatement internalCallableStatement) {
        this.internalCallableStatement = internalCallableStatement;
    }
}

