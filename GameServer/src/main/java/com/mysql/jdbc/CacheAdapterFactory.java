package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

public interface CacheAdapterFactory<K, V> {
    CacheAdapter<K, V> getInstance(Connection paramConnection, String paramString, int paramInt1, int paramInt2, Properties paramProperties) throws SQLException;
}

