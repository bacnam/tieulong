package com.mchange.v1.db.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;

public interface PSManager {
  PreparedStatement getPS(Connection paramConnection, String paramString);
  
  void putPS(Connection paramConnection, String paramString, PreparedStatement paramPreparedStatement);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/PSManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */