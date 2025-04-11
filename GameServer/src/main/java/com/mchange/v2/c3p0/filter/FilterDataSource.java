package com.mchange.v2.c3p0.filter;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class FilterDataSource
        implements DataSource {
    protected DataSource inner;

    public FilterDataSource(DataSource inner) {
        this.inner = inner;
    }

    public Connection getConnection() throws SQLException {
        return this.inner.getConnection();
    }

    public Connection getConnection(String a, String b) throws SQLException {
        return this.inner.getConnection(a, b);
    }

    public PrintWriter getLogWriter() throws SQLException {
        return this.inner.getLogWriter();
    }

    public void setLogWriter(PrintWriter a) throws SQLException {
        this.inner.setLogWriter(a);
    }

    public int getLoginTimeout() throws SQLException {
        return this.inner.getLoginTimeout();
    }

    public void setLoginTimeout(int a) throws SQLException {
        this.inner.setLoginTimeout(a);
    }
}

