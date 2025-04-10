package com.zhonglian.server.common.db;

import BaseCommon.CommLog;
import com.jolbox.bonecp.BoneCPDataSource;
import com.zhonglian.server.common.utils.CommTime;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseFactory
implements IDBConnectionFactory
{
private BoneCPDataSource _source = null;

private static final String db_driver = "com.mysql.jdbc.Driver";
private String _url;

private void setDataSourceParams() {
int maxconnPerPartition = Integer.getInteger("boneCP.maxconnPerPartition", 20).intValue();
int minconnPerPartition = Integer.getInteger("boneCP.minconnPerPartition", 10).intValue();
this._source.setMaxConnectionsPerPartition(maxconnPerPartition);
this._source.setMinConnectionsPerPartition(minconnPerPartition);
this._source.setIdleConnectionTestPeriodInMinutes(2L);
this._source.setIdleMaxAgeInMinutes(6L);
this._source.setAcquireIncrement(5);
this._source.setReleaseHelperThreads(3);
}

public DatabaseFactory(String url, String user, String password) {
CommLog.info("数据库连接地址：{}", url);
this._url = url;
this._source = new BoneCPDataSource();
this._source.setDriverClass("com.mysql.jdbc.Driver");
this._source.setJdbcUrl(url);
this._source.setUsername(user);
this._source.setPassword(password);
setDataSourceParams();
}

public String getCatalog() {
Connection conn = null;
try {
conn = this._source.getConnection();
return conn.getCatalog();
} catch (SQLException e) {
CommLog.error(e.getMessage(), e);
} finally {
if (conn != null) {
try {
conn.close();
} catch (SQLException ex) {
CommLog.error(ex.getMessage(), ex);
} 
}
} 
return null;
}

public PrintWriter getLogWriter() throws SQLException {
return this._source.getLogWriter();
}

public void setLogWriter(PrintWriter out) throws SQLException {
this._source.setLogWriter(out);
}

public void shutdown() {
try {
this._source.close();
this._source.setLogWriter(null);
} catch (Throwable ex) {
CommLog.error(null, ex);
} 
}

public Connection getConnection() {
try {
long ms = CommTime.nowMS();
Connection conn = this._source.getConnection();
long usedMs = CommTime.nowMS() - ms;
if (usedMs > 100L) {
CommLog.warn("DatabaseFactory getConnection() used {} ms, url {}", Long.valueOf(usedMs), this._url);
}
return conn;
} catch (SQLException ex) {
CommLog.error(null, ex);

return null;
} 
}
public int getUsedConnectCount() {
return this._source.getTotalLeased();
}
}

