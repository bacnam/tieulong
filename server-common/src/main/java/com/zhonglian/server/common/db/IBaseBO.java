package com.zhonglian.server.common.db;

import java.sql.ResultSet;
import java.util.List;

public interface IBaseBO {
    void getFromResultSet(ResultSet var1, List var2) throws Exception;

    String getItemsName();

    String getTableName();

    String getItemsValue();

    void setId(long var1);

    long getId();

    IDBConnectionFactory getConnectionFactory();
}
