package com.mchange.v2.sql.filter;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public abstract class SynchronizedFilterConnection
implements Connection
{
protected Connection inner;

private void __setInner(Connection paramConnection) {
this.inner = paramConnection;
}

public SynchronizedFilterConnection(Connection paramConnection) {
__setInner(paramConnection);
}

public SynchronizedFilterConnection() {}

public synchronized void setInner(Connection paramConnection) {
__setInner(paramConnection);
}
public synchronized Connection getInner() {
return this.inner;
}

public synchronized void commit() throws SQLException {
this.inner.commit();
}

public synchronized void clearWarnings() throws SQLException {
this.inner.clearWarnings();
}

public synchronized Array createArrayOf(String paramString, Object[] paramArrayOfObject) throws SQLException {
return this.inner.createArrayOf(paramString, paramArrayOfObject);
}

public synchronized Blob createBlob() throws SQLException {
return this.inner.createBlob();
}

public synchronized Clob createClob() throws SQLException {
return this.inner.createClob();
}

public synchronized NClob createNClob() throws SQLException {
return this.inner.createNClob();
}

public synchronized SQLXML createSQLXML() throws SQLException {
return this.inner.createSQLXML();
}

public synchronized Statement createStatement(int paramInt1, int paramInt2, int paramInt3) throws SQLException {
return this.inner.createStatement(paramInt1, paramInt2, paramInt3);
}

public synchronized Statement createStatement(int paramInt1, int paramInt2) throws SQLException {
return this.inner.createStatement(paramInt1, paramInt2);
}

public synchronized Statement createStatement() throws SQLException {
return this.inner.createStatement();
}

public synchronized Struct createStruct(String paramString, Object[] paramArrayOfObject) throws SQLException {
return this.inner.createStruct(paramString, paramArrayOfObject);
}

public synchronized boolean getAutoCommit() throws SQLException {
return this.inner.getAutoCommit();
}

public synchronized String getCatalog() throws SQLException {
return this.inner.getCatalog();
}

public synchronized String getClientInfo(String paramString) throws SQLException {
return this.inner.getClientInfo(paramString);
}

public synchronized Properties getClientInfo() throws SQLException {
return this.inner.getClientInfo();
}

public synchronized int getHoldability() throws SQLException {
return this.inner.getHoldability();
}

public synchronized DatabaseMetaData getMetaData() throws SQLException {
return this.inner.getMetaData();
}

public synchronized int getNetworkTimeout() throws SQLException {
return this.inner.getNetworkTimeout();
}

public synchronized String getSchema() throws SQLException {
return this.inner.getSchema();
}

public synchronized int getTransactionIsolation() throws SQLException {
return this.inner.getTransactionIsolation();
}

public synchronized Map getTypeMap() throws SQLException {
return this.inner.getTypeMap();
}

public synchronized SQLWarning getWarnings() throws SQLException {
return this.inner.getWarnings();
}

public synchronized boolean isClosed() throws SQLException {
return this.inner.isClosed();
}

public synchronized String nativeSQL(String paramString) throws SQLException {
return this.inner.nativeSQL(paramString);
}

public synchronized CallableStatement prepareCall(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
return this.inner.prepareCall(paramString, paramInt1, paramInt2, paramInt3);
}

public synchronized CallableStatement prepareCall(String paramString, int paramInt1, int paramInt2) throws SQLException {
return this.inner.prepareCall(paramString, paramInt1, paramInt2);
}

public synchronized CallableStatement prepareCall(String paramString) throws SQLException {
return this.inner.prepareCall(paramString);
}

public synchronized PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
return this.inner.prepareStatement(paramString, paramInt1, paramInt2, paramInt3);
}

public synchronized PreparedStatement prepareStatement(String paramString, int paramInt) throws SQLException {
return this.inner.prepareStatement(paramString, paramInt);
}

public synchronized PreparedStatement prepareStatement(String paramString, int[] paramArrayOfint) throws SQLException {
return this.inner.prepareStatement(paramString, paramArrayOfint);
}

public synchronized PreparedStatement prepareStatement(String paramString, String[] paramArrayOfString) throws SQLException {
return this.inner.prepareStatement(paramString, paramArrayOfString);
}

public synchronized PreparedStatement prepareStatement(String paramString) throws SQLException {
return this.inner.prepareStatement(paramString);
}

public synchronized PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2) throws SQLException {
return this.inner.prepareStatement(paramString, paramInt1, paramInt2);
}

public synchronized void releaseSavepoint(Savepoint paramSavepoint) throws SQLException {
this.inner.releaseSavepoint(paramSavepoint);
}

public synchronized void rollback() throws SQLException {
this.inner.rollback();
}

public synchronized void rollback(Savepoint paramSavepoint) throws SQLException {
this.inner.rollback(paramSavepoint);
}

public synchronized void setAutoCommit(boolean paramBoolean) throws SQLException {
this.inner.setAutoCommit(paramBoolean);
}

public synchronized void setCatalog(String paramString) throws SQLException {
this.inner.setCatalog(paramString);
}

public synchronized void setClientInfo(String paramString1, String paramString2) throws SQLClientInfoException {
this.inner.setClientInfo(paramString1, paramString2);
}

public synchronized void setClientInfo(Properties paramProperties) throws SQLClientInfoException {
this.inner.setClientInfo(paramProperties);
}

public synchronized void setHoldability(int paramInt) throws SQLException {
this.inner.setHoldability(paramInt);
}

public synchronized void setNetworkTimeout(Executor paramExecutor, int paramInt) throws SQLException {
this.inner.setNetworkTimeout(paramExecutor, paramInt);
}

public synchronized Savepoint setSavepoint() throws SQLException {
return this.inner.setSavepoint();
}

public synchronized Savepoint setSavepoint(String paramString) throws SQLException {
return this.inner.setSavepoint(paramString);
}

public synchronized void setSchema(String paramString) throws SQLException {
this.inner.setSchema(paramString);
}

public synchronized void setTransactionIsolation(int paramInt) throws SQLException {
this.inner.setTransactionIsolation(paramInt);
}

public synchronized void setTypeMap(Map<String, Class<?>> paramMap) throws SQLException {
this.inner.setTypeMap(paramMap);
}

public synchronized void setReadOnly(boolean paramBoolean) throws SQLException {
this.inner.setReadOnly(paramBoolean);
}

public synchronized void close() throws SQLException {
this.inner.close();
}

public synchronized boolean isValid(int paramInt) throws SQLException {
return this.inner.isValid(paramInt);
}

public synchronized boolean isReadOnly() throws SQLException {
return this.inner.isReadOnly();
}

public synchronized void abort(Executor paramExecutor) throws SQLException {
this.inner.abort(paramExecutor);
}

public synchronized boolean isWrapperFor(Class<?> paramClass) throws SQLException {
return this.inner.isWrapperFor(paramClass);
}

public synchronized Object unwrap(Class<?> paramClass) throws SQLException {
return this.inner.unwrap(paramClass);
}
}

