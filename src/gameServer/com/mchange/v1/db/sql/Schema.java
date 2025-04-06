package com.mchange.v1.db.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface Schema {
  void createSchema(Connection paramConnection) throws SQLException;
  
  void dropSchema(Connection paramConnection) throws SQLException;
  
  String getStatementText(String paramString1, String paramString2);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/Schema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */