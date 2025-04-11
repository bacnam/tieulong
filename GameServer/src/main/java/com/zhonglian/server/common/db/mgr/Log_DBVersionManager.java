package com.zhonglian.server.common.db.mgr;

import com.zhonglian.server.common.db.DBCons;
import com.zhonglian.server.common.db.IDBConnectionFactory;
import com.zhonglian.server.common.db.version.DBVersionManager;

public class Log_DBVersionManager
        extends DBVersionManager {
    private static Log_DBVersionManager instance = null;

    public static Log_DBVersionManager getInstance() {
        if (instance == null) {
            instance = new Log_DBVersionManager();
        }
        return instance;
    }

    public IDBConnectionFactory getConn() {
        return DBCons.getLogDBFactory();
    }
}

