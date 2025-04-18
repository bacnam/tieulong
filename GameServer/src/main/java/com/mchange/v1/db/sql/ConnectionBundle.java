package com.mchange.v1.db.sql;

import com.mchange.v1.util.ClosableResource;
import java.sql.Connection;
import java.sql.PreparedStatement;

public interface ConnectionBundle extends ClosableResource {
  Connection getConnection();

  PreparedStatement getStatement(String paramString);

  void putStatement(String paramString, PreparedStatement paramPreparedStatement);
}

