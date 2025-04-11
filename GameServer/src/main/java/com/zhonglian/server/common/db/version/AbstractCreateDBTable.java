package com.zhonglian.server.common.db.version;

import BaseCommon.CommLog;
import com.zhonglian.server.common.db.IDBConnectionFactory;
import com.zhonglian.server.common.db.SQLExecutor;

public abstract class AbstractCreateDBTable
        implements ICreateDBTable {
    private String m_dbName = "";

    private String m_tableName = "";

    protected AbstractCreateDBTable(String dbName, String tableName) {
        this.m_dbName = dbName;
        this.m_tableName = tableName;
    }

    public String getDBName() {
        return this.m_dbName;
    }

    public String getTableName() {
        return this.m_tableName;
    }

    public abstract String getCreateSql();

    public boolean run(IDBConnectionFactory _conInfo) {
        boolean isOk = false;

        try {
            isOk = SQLExecutor.execute(getCreateSql(), _conInfo);
        } catch (Throwable e) {
            return false;
        } finally {
            if (isOk) {
                CommLog.info("process db=[{}] table=[{}]: succeed!", this.m_dbName, this.m_tableName);
            } else {
                CommLog.error("process db=[{}] table=[{}]: failed!", this.m_dbName, this.m_tableName);
            }
        }
        return isOk;
    }
}

