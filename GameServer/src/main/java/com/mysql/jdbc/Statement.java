package com.mysql.jdbc;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface Statement extends Statement {
  void enableStreamingResults() throws SQLException;

  void disableStreamingResults() throws SQLException;

  void setLocalInfileInputStream(InputStream paramInputStream);

  InputStream getLocalInfileInputStream();

  void setPingTarget(PingTarget paramPingTarget);

  ExceptionInterceptor getExceptionInterceptor();

  void removeOpenResultSet(ResultSet paramResultSet);

  int getOpenResultSetCount();

  void setHoldResultsOpenOverClose(boolean paramBoolean);
}

