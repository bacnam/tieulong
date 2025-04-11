package com.zhonglian.server.common.db;

public class DBCons {
    private static IDBConnectionFactory DBFactory = null;
    private static IDBConnectionFactory logDBFactory = null;

    public static IDBConnectionFactory getDBFactory() {
        return DBFactory;
    }

    public static void setDBFactory(IDBConnectionFactory aGameDBFactory) {
        DBFactory = aGameDBFactory;
    }

    public static IDBConnectionFactory getLogDBFactory() {
        return logDBFactory;
    }

    public static void setLogDBFactory(IDBConnectionFactory aLogDBFactory) {
        logDBFactory = aLogDBFactory;
    }
}

