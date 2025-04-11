package com.zhonglian.server.common.db;

import java.sql.ResultSet;
import java.util.List;

public interface IBaseBO {
    void getFromResultSet(ResultSet paramResultSet, List paramList) throws Exception;

    String getItemsName();

    String getTableName();

    String getItemsValue();

    long getId();

    void setId(long paramLong);

    IDBConnectionFactory getConnectionFactory();
}

