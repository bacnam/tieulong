package com.jolbox.bonecp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

public class PreparedStatementHandle
        extends StatementHandle
        implements PreparedStatement {
    protected static Logger logger = LoggerFactory.getLogger(PreparedStatementHandle.class);
    private PreparedStatement internalPreparedStatement;

    public PreparedStatementHandle(PreparedStatement internalPreparedStatement, String sql, ConnectionHandle connectionHandle, String cacheKey, IStatementCache cache) {
        super(internalPreparedStatement, sql, cache, connectionHandle, cacheKey, connectionHandle.isLogStatementsEnabled());
        this.internalPreparedStatement = internalPreparedStatement;
        this.connectionHandle = connectionHandle;
        this.sql = sql;
        this.cache = cache;
    }

    public void addBatch() throws SQLException {
        checkClosed();
        try {
            if (this.logStatementsEnabled) {
                this.batchSQL.append(this.sql);
            }
            this.internalPreparedStatement.addBatch();
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void clearParameters() throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.clearParameters();
            if (this.logStatementsEnabled) {
                this.logParams.clear();
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public boolean execute() throws SQLException {
        checkClosed();
        try {
            if (this.logStatementsEnabled && logger.isDebugEnabled()) {
                logger.debug(PoolUtil.fillLogParams(this.sql, this.logParams));
            }
            long queryStartTime = queryTimerStart();

            if (this.connectionHook != null) {
                this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, this.sql, this.logParams);
            }

            boolean result = this.internalPreparedStatement.execute();

            if (this.connectionHook != null) {
                this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, this.sql, this.logParams);
            }

            queryTimerEnd(this.sql, queryStartTime);

            return result;
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public ResultSet executeQuery() throws SQLException {
        checkClosed();
        try {
            if (this.logStatementsEnabled && logger.isDebugEnabled()) {
                logger.debug(PoolUtil.fillLogParams(this.sql, this.logParams));
            }
            long queryStartTime = queryTimerStart();
            if (this.connectionHook != null) {
                this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, this.sql, this.logParams);
            }
            ResultSet result = this.internalPreparedStatement.executeQuery();
            if (this.connectionHook != null) {
                this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, this.sql, this.logParams);
            }

            queryTimerEnd(this.sql, queryStartTime);

            return result;
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public int executeUpdate() throws SQLException {
        checkClosed();
        try {
            if (this.logStatementsEnabled && logger.isDebugEnabled()) {
                logger.debug(PoolUtil.fillLogParams(this.sql, this.logParams));
            }
            long queryStartTime = queryTimerStart();
            if (this.connectionHook != null) {
                this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, this.sql, this.logParams);
            }
            int result = this.internalPreparedStatement.executeUpdate();
            if (this.connectionHook != null) {
                this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, this.sql, this.logParams);
            }

            queryTimerEnd(this.sql, queryStartTime);

            return result;
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        checkClosed();
        try {
            return this.internalPreparedStatement.getMetaData();
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public ParameterMetaData getParameterMetaData() throws SQLException {
        checkClosed();
        try {
            return this.internalPreparedStatement.getParameterMetaData();
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setArray(int parameterIndex, Array x) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setArray(parameterIndex, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setBinaryStream(parameterIndex, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setBinaryStream(parameterIndex, x, length);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setBlob(parameterIndex, inputStream);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), inputStream);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setAsciiStream(parameterIndex, x, length);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setClob(parameterIndex, reader);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), reader);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setRowId(parameterIndex, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setSQLXML(parameterIndex, xmlObject);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), xmlObject);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setClob(parameterIndex, reader, length);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), reader);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setNCharacterStream(parameterIndex, value);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), value);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setNCharacterStream(parameterIndex, value, length);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), value);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setNClob(parameterIndex, value);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), value);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setNClob(parameterIndex, reader);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), reader);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setNClob(parameterIndex, reader, length);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), reader);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setNString(int parameterIndex, String value) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setNString(parameterIndex, value);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), value);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setAsciiStream(parameterIndex, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setCharacterStream(parameterIndex, reader, length);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), reader);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setBlob(parameterIndex, inputStream, length);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), inputStream);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setCharacterStream(parameterIndex, reader);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), reader);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setAsciiStream(parameterIndex, x, length);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setBigDecimal(parameterIndex, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setBinaryStream(parameterIndex, x, length);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setBlob(parameterIndex, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setBoolean(parameterIndex, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), Boolean.valueOf(x));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setByte(int parameterIndex, byte x) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setByte(parameterIndex, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), Byte.valueOf(x));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setBytes(parameterIndex, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setCharacterStream(parameterIndex, reader, length);

            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), reader);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setClob(int parameterIndex, Clob x) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setClob(parameterIndex, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setDate(int parameterIndex, Date x) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setDate(parameterIndex, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setDate(parameterIndex, x, cal);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), PoolUtil.safePrint(new Object[]{x, ", cal=", cal}));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setDouble(int parameterIndex, double x) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setDouble(parameterIndex, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), Double.valueOf(x));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setFloat(int parameterIndex, float x) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setFloat(parameterIndex, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), Float.valueOf(x));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setInt(int parameterIndex, int x) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setInt(parameterIndex, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), Integer.valueOf(x));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setLong(int parameterIndex, long x) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setLong(parameterIndex, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), Long.valueOf(x));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setNull(parameterIndex, sqlType);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), "[SQL NULL of type " + sqlType + "]");
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setNull(parameterIndex, sqlType, typeName);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), PoolUtil.safePrint(new Object[]{"[SQL NULL of type ", Integer.valueOf(sqlType), ", type = ", typeName, "]"}));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setObject(int parameterIndex, Object x) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setObject(parameterIndex, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setObject(parameterIndex, x, targetSqlType);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setRef(int parameterIndex, Ref x) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setRef(parameterIndex, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setShort(int parameterIndex, short x) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setShort(parameterIndex, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), Short.valueOf(x));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setString(int parameterIndex, String x) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setString(parameterIndex, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setTime(int parameterIndex, Time x) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setTime(parameterIndex, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setTime(parameterIndex, x, cal);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), PoolUtil.safePrint(new Object[]{x, ", cal=", cal}));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setTimestamp(parameterIndex, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setTimestamp(parameterIndex, x, cal);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), PoolUtil.safePrint(new Object[]{x, ", cal=", cal}));
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public void setURL(int parameterIndex, URL x) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setURL(parameterIndex, x);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    @Deprecated
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        checkClosed();
        try {
            this.internalPreparedStatement.setUnicodeStream(parameterIndex, x, length);
            if (this.logStatementsEnabled) {
                this.logParams.put(Integer.valueOf(parameterIndex), x);
            }
        } catch (SQLException e) {
            throw this.connectionHandle.markPossiblyBroken(e);
        }
    }

    public PreparedStatement getInternalPreparedStatement() {
        return this.internalPreparedStatement;
    }

    public void setInternalPreparedStatement(PreparedStatement internalPreparedStatement) {
        this.internalPreparedStatement = internalPreparedStatement;
    }
}

