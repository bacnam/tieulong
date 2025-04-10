package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.Connection;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

public class MysqlConnectionPoolDataSource
extends MysqlDataSource
implements ConnectionPoolDataSource
{
static final long serialVersionUID = -7767325445592304961L;

public synchronized PooledConnection getPooledConnection() throws SQLException {
Connection connection = getConnection();
MysqlPooledConnection mysqlPooledConnection = MysqlPooledConnection.getInstance((Connection)connection);

return mysqlPooledConnection;
}

public synchronized PooledConnection getPooledConnection(String s, String s1) throws SQLException {
Connection connection = getConnection(s, s1);
MysqlPooledConnection mysqlPooledConnection = MysqlPooledConnection.getInstance((Connection)connection);

return mysqlPooledConnection;
}
}

