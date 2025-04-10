package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

public interface Extension {
  void init(Connection paramConnection, Properties paramProperties) throws SQLException;

  void destroy();
}

