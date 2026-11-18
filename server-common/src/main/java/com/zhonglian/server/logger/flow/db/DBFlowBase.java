package com.zhonglian.server.logger.flow.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class DBFlowBase {
    public abstract String getTableName();

    public abstract String getInsertSql();

    public abstract void addToBatch(PreparedStatement var1) throws SQLException;
}
