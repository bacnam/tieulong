package com.mchange.v1.db.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ConnectionBundleImpl
        implements ConnectionBundle {
    Connection con;
    Map map = new HashMap<Object, Object>();

    public ConnectionBundleImpl(Connection paramConnection) {
        this.con = paramConnection;
    }

    public Connection getConnection() {
        return this.con;
    }

    public PreparedStatement getStatement(String paramString) {
        return (PreparedStatement) this.map.get(paramString);
    }

    public void putStatement(String paramString, PreparedStatement paramPreparedStatement) {
        this.map.put(paramString, paramPreparedStatement);
    }

    public void close() throws SQLException {
        this.con.close();
    }

    public void finalize() throws Exception {
        if (!this.con.isClosed()) close();
    }
}

