package com.mchange.v1.db.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;

public interface PSManager {
  PreparedStatement getPS(Connection paramConnection, String paramString);

  void putPS(Connection paramConnection, String paramString, PreparedStatement paramPreparedStatement);
}

