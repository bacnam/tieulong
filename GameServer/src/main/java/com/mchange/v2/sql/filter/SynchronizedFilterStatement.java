package com.mchange.v2.sql.filter;

import java.sql.*;

public abstract class SynchronizedFilterStatement
        implements Statement {
    protected Statement inner;

    public SynchronizedFilterStatement(Statement paramStatement) {
        __setInner(paramStatement);
    }

    public SynchronizedFilterStatement() {
    }

    private void __setInner(Statement paramStatement) {
        this.inner = paramStatement;
    }

    public synchronized Statement getInner() {
        return this.inner;
    }

    public synchronized void setInner(Statement paramStatement) {
        __setInner(paramStatement);
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

