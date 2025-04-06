package com.zhonglian.server.common.db;

import java.sql.ResultSet;
import java.util.List;

public interface IBaseBO {
  void getFromResultSet(ResultSet paramResultSet, List paramList) throws Exception;
  
  String getItemsName();
  
  String getTableName();
  
  String getItemsValue();
  
  void setId(long paramLong);
  
  long getId();
  
  IDBConnectionFactory getConnectionFactory();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/db/IBaseBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */