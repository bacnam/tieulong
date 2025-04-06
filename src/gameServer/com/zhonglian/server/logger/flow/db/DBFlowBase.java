package com.zhonglian.server.logger.flow.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class DBFlowBase {
  public abstract String getTableName();
  
  public abstract String getInsertSql();
  
  public abstract void addToBatch(PreparedStatement paramPreparedStatement) throws SQLException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/logger/flow/db/DBFlowBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */