package com.mchange.v2.sql.filter;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;

public abstract class SynchronizedFilterDataSource
implements DataSource
{
protected DataSource inner;

private void __setInner(DataSource paramDataSource) {
this.inner = paramDataSource;
}

public SynchronizedFilterDataSource(DataSource paramDataSource) {
__setInner(paramDataSource);
}

public SynchronizedFilterDataSource() {}

public synchronized void setInner(DataSource paramDataSource) {
__setInner(paramDataSource);
}
public synchronized DataSource getInner() {
return this.inner;
}

public synchronized Connection getConnection() throws SQLException {
return this.inner.getConnection();
}

public synchronized Connection getConnection(String paramString1, String paramString2) throws SQLException {
return this.inner.getConnection(paramString1, paramString2);
}

public synchronized PrintWriter getLogWriter() throws SQLException {
return this.inner.getLogWriter();
}

public synchronized int getLoginTimeout() throws SQLException {
return this.inner.getLoginTimeout();
}

public synchronized Logger getParentLogger() throws SQLFeatureNotSupportedException {
return this.inner.getParentLogger();
}

public synchronized void setLogWriter(PrintWriter paramPrintWriter) throws SQLException {
this.inner.setLogWriter(paramPrintWriter);
}

public synchronized void setLoginTimeout(int paramInt) throws SQLException {
this.inner.setLoginTimeout(paramInt);
}

public synchronized boolean isWrapperFor(Class<?> paramClass) throws SQLException {
return this.inner.isWrapperFor(paramClass);
}

public synchronized Object unwrap(Class<?> paramClass) throws SQLException {
return this.inner.unwrap(paramClass);
}
}

