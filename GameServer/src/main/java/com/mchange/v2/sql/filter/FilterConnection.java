package com.mchange.v2.sql.filter;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public abstract class FilterConnection
        implements Connection {
    protected Connection inner;

    public FilterConnection(Connection paramConnection) {
        __setInner(paramConnection);
    }

    public FilterConnection() {
    }

    private void __setInner(Connection paramConnection) {
        this.inner = paramConnection;
    }

    public Connection getInner() {
        return this.inner;
    }

    public void setInner(Connection paramConnection) {
        __setInner(paramConnection);
    }

    public void commit() throws SQLException {
        this.inner.commit();
    }

    public void clearWarnings() throws SQLException {
        this.inner.clearWarnings();
    }

    public Array createArrayOf(String paramString, Object[] paramArrayOfObject) throws SQLException {
        return this.inner.createArrayOf(paramString, paramArrayOfObject);
    }

    public Blob createBlob() throws SQLException {
        return this.inner.createBlob();
    }

    public Clob createClob() throws SQLException {
        return this.inner.createClob();
    }

    public NClob createNClob() throws SQLException {
        return this.inner.createNClob();
    }

    public SQLXML createSQLXML() throws SQLException {
        return this.inner.createSQLXML();
    }

    public Statement createStatement(int paramInt1, int paramInt2, int paramInt3) throws SQLException {
        return this.inner.createStatement(paramInt1, paramInt2, paramInt3);
    }

    public Statement createStatement(int paramInt1, int paramInt2) throws SQLException {
        return this.inner.createStatement(paramInt1, paramInt2);
    }

    public Statement createStatement() throws SQLException {
        return this.inner.createStatement();
    }

    public Struct createStruct(String paramString, Object[] paramArrayOfObject) throws SQLException {
        return this.inner.createStruct(paramString, paramArrayOfObject);
    }

    public boolean getAutoCommit() throws SQLException {
        return this.inner.getAutoCommit();
    }

    public void setAutoCommit(boolean paramBoolean) throws SQLException {
        this.inner.setAutoCommit(paramBoolean);
    }

    public String getCatalog() throws SQLException {
        return this.inner.getCatalog();
    }

    public void setCatalog(String paramString) throws SQLException {
        this.inner.setCatalog(paramString);
    }

    public String getClientInfo(String paramString) throws SQLException {
        return this.inner.getClientInfo(paramString);
    }

    public Properties getClientInfo() throws SQLException {
        return this.inner.getClientInfo();
    }

    public void setClientInfo(Properties paramProperties) throws SQLClientInfoException {
        this.inner.setClientInfo(paramProperties);
    }

    public int getHoldability() throws SQLException {
        return this.inner.getHoldability();
    }

    public void setHoldability(int paramInt) throws SQLException {
        this.inner.setHoldability(paramInt);
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return this.inner.getMetaData();
    }

    public int getNetworkTimeout() throws SQLException {
        return this.inner.getNetworkTimeout();
    }

    public String getSchema() throws SQLException {
        return this.inner.getSchema();
    }

    public void setSchema(String paramString) throws SQLException {
        this.inner.setSchema(paramString);
    }

    public int getTransactionIsolation() throws SQLException {
        return this.inner.getTransactionIsolation();
    }

    public void setTransactionIsolation(int paramInt) throws SQLException {
        this.inner.setTransactionIsolation(paramInt);
    }

    public Map getTypeMap() throws SQLException {
        return this.inner.getTypeMap();
    }

    public void setTypeMap(Map<String, Class<?>> paramMap) throws SQLException {
        this.inner.setTypeMap(paramMap);
    }

    public SQLWarning getWarnings() throws SQLException {
        return this.inner.getWarnings();
    }

    public boolean isClosed() throws SQLException {
        return this.inner.isClosed();
    }

    public String nativeSQL(String paramString) throws SQLException {
        return this.inner.nativeSQL(paramString);
    }

    public CallableStatement prepareCall(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
        return this.inner.prepareCall(paramString, paramInt1, paramInt2, paramInt3);
    }

    public CallableStatement prepareCall(String paramString, int paramInt1, int paramInt2) throws SQLException {
        return this.inner.prepareCall(paramString, paramInt1, paramInt2);
    }

    public CallableStatement prepareCall(String paramString) throws SQLException {
        return this.inner.prepareCall(paramString);
    }

    public PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
        return this.inner.prepareStatement(paramString, paramInt1, paramInt2, paramInt3);
    }

    public PreparedStatement prepareStatement(String paramString, int paramInt) throws SQLException {
        return this.inner.prepareStatement(paramString, paramInt);
    }

    public PreparedStatement prepareStatement(String paramString, int[] paramArrayOfint) throws SQLException {
        return this.inner.prepareStatement(paramString, paramArrayOfint);
    }

    public PreparedStatement prepareStatement(String paramString, String[] paramArrayOfString) throws SQLException {
        return this.inner.prepareStatement(paramString, paramArrayOfString);
    }

    public PreparedStatement prepareStatement(String paramString) throws SQLException {
        return this.inner.prepareStatement(paramString);
    }

    public PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2) throws SQLException {
        return this.inner.prepareStatement(paramString, paramInt1, paramInt2);
    }

    public void releaseSavepoint(Savepoint paramSavepoint) throws SQLException {
        this.inner.releaseSavepoint(paramSavepoint);
    }

    public void rollback() throws SQLException {
        this.inner.rollback();
    }

    public void rollback(Savepoint paramSavepoint) throws SQLException {
        this.inner.rollback(paramSavepoint);
    }

    public void setClientInfo(String paramString1, String paramString2) throws SQLClientInfoException {
        this.inner.setClientInfo(paramString1, paramString2);
    }

    public void setNetworkTimeout(Executor paramExecutor, int paramInt) throws SQLException {
        this.inner.setNetworkTimeout(paramExecutor, paramInt);
    }

    public Savepoint setSavepoint() throws SQLException {
        return this.inner.setSavepoint();
    }

    public Savepoint setSavepoint(String paramString) throws SQLException {
        return this.inner.setSavepoint(paramString);
    }

    public void close() throws SQLException {
        this.inner.close();
    }

    public boolean isValid(int paramInt) throws SQLException {
        return this.inner.isValid(paramInt);
    }

    public boolean isReadOnly() throws SQLException {
        return this.inner.isReadOnly();
    }

    public void setReadOnly(boolean paramBoolean) throws SQLException {
        this.inner.setReadOnly(paramBoolean);
    }

    public void abort(Executor paramExecutor) throws SQLException {
        this.inner.abort(paramExecutor);
    }

    public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
        return this.inner.isWrapperFor(paramClass);
    }

    public Object unwrap(Class<?> paramClass) throws SQLException {
        return this.inner.unwrap(paramClass);
    }
}

