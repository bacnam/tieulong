package com.mchange.v2.sql.filter;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;

public abstract class FilterDataSource
implements DataSource
{
protected DataSource inner;

private void __setInner(DataSource paramDataSource) {
this.inner = paramDataSource;
}

public FilterDataSource(DataSource paramDataSource) {
__setInner(paramDataSource);
}

public FilterDataSource() {}

public void setInner(DataSource paramDataSource) {
__setInner(paramDataSource);
}
public DataSource getInner() {
return this.inner;
}

public Connection getConnection() throws SQLException {
return this.inner.getConnection();
}

public Connection getConnection(String paramString1, String paramString2) throws SQLException {
return this.inner.getConnection(paramString1, paramString2);
}

public PrintWriter getLogWriter() throws SQLException {
return this.inner.getLogWriter();
}

public int getLoginTimeout() throws SQLException {
return this.inner.getLoginTimeout();
}

public Logger getParentLogger() throws SQLFeatureNotSupportedException {
return this.inner.getParentLogger();
}

public void setLogWriter(PrintWriter paramPrintWriter) throws SQLException {
this.inner.setLogWriter(paramPrintWriter);
}

public void setLoginTimeout(int paramInt) throws SQLException {
this.inner.setLoginTimeout(paramInt);
}

public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
return this.inner.isWrapperFor(paramClass);
}

public Object unwrap(Class<?> paramClass) throws SQLException {
return this.inner.unwrap(paramClass);
}
}

