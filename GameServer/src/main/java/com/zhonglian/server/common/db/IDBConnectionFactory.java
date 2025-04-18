package com.zhonglian.server.common.db;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

public interface IDBConnectionFactory {
  Connection getConnection();

  PrintWriter getLogWriter() throws SQLException;

  void setLogWriter(PrintWriter paramPrintWriter) throws SQLException;

  void shutdown();

  String getCatalog();

  int getUsedConnectCount();
}

