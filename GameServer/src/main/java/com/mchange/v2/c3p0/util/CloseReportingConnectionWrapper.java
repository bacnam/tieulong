package com.mchange.v2.c3p0.util;

import com.mchange.v2.sql.filter.FilterConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;

public class CloseReportingConnectionWrapper
        extends FilterConnection {
    public CloseReportingConnectionWrapper(Connection conn) {
        super(conn);
    }

    public void close() throws SQLException {
        (new SQLWarning("Connection.close() called!")).printStackTrace();
        super.close();
    }
}

