package com.mchange.v2.sql.filter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

public abstract class FilterStatement
implements Statement
{
protected Statement inner;

private void __setInner(Statement paramStatement) {
this.inner = paramStatement;
}

public FilterStatement(Statement paramStatement) {
__setInner(paramStatement);
}

public FilterStatement() {}

public void setInner(Statement paramStatement) {
__setInner(paramStatement);
}
public Statement getInner() {
return this.inner;
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

public int getFetchSize() throws SQLException {
return this.inner.getFetchSize();
}

public void setFetchDirection(int paramInt) throws SQLException {
this.inner.setFetchDirection(paramInt);
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

public int getMaxRows() throws SQLException {
return this.inner.getMaxRows();
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

public void setCursorName(String paramString) throws SQLException {
this.inner.setCursorName(paramString);
}

public void setEscapeProcessing(boolean paramBoolean) throws SQLException {
this.inner.setEscapeProcessing(paramBoolean);
}

public void setMaxFieldSize(int paramInt) throws SQLException {
this.inner.setMaxFieldSize(paramInt);
}

public void setMaxRows(int paramInt) throws SQLException {
this.inner.setMaxRows(paramInt);
}

public void setPoolable(boolean paramBoolean) throws SQLException {
this.inner.setPoolable(paramBoolean);
}

public void setQueryTimeout(int paramInt) throws SQLException {
this.inner.setQueryTimeout(paramInt);
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

