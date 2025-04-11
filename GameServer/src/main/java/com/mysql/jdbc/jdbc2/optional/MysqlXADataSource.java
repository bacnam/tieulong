package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ConnectionImpl;

import javax.sql.XAConnection;
import javax.sql.XADataSource;
import java.sql.SQLException;

public class MysqlXADataSource
        extends MysqlDataSource
        implements XADataSource {
    static final long serialVersionUID = 7911390333152247455L;

    public XAConnection getXAConnection() throws SQLException {
        Connection conn = getConnection();

        return wrapConnection(conn);
    }

    public XAConnection getXAConnection(String u, String p) throws SQLException {
        Connection conn = getConnection(u, p);

        return wrapConnection(conn);
    }

    private XAConnection wrapConnection(Connection conn) throws SQLException {
        if (getPinGlobalTxToPhysicalConnection() || ((Connection) conn).getPinGlobalTxToPhysicalConnection()) {
            return SuspendableXAConnection.getInstance((ConnectionImpl) conn);
        }

        return MysqlXAConnection.getInstance((ConnectionImpl) conn, getLogXaCommands());
    }
}

