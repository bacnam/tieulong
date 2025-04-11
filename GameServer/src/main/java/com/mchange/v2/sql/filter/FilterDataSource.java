package com.mchange.v2.sql.filter;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public abstract class FilterDataSource
        implements DataSource {
    protected DataSource inner;

    public FilterDataSource(DataSource paramDataSource) {
        __setInner(paramDataSource);
    }

    public FilterDataSource() {
    }

    private void __setInner(DataSource paramDataSource) {
        this.inner = paramDataSource;
    }

    public DataSource getInner() {
        return this.inner;
    }

    public void setInner(DataSource paramDataSource) {
        __setInner(paramDataSource);
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

    public void setLogWriter(PrintWriter paramPrintWriter) throws SQLException {
        this.inner.setLogWriter(paramPrintWriter);
    }

    public int getLoginTimeout() throws SQLException {
        return this.inner.getLoginTimeout();
    }

    public void setLoginTimeout(int paramInt) throws SQLException {
        this.inner.setLoginTimeout(paramInt);
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.inner.getParentLogger();
    }

    public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
        return this.inner.isWrapperFor(paramClass);
    }

    public Object unwrap(Class<?> paramClass) throws SQLException {
        return this.inner.unwrap(paramClass);
    }
}

