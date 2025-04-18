package com.mchange.v1.db.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface Schema {
  void createSchema(Connection paramConnection) throws SQLException;

  void dropSchema(Connection paramConnection) throws SQLException;

  String getStatementText(String paramString1, String paramString2);
}

