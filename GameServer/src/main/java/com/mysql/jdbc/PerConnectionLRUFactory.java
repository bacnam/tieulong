package com.mysql.jdbc;

import com.mysql.jdbc.util.LRUCache;

import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;

public class PerConnectionLRUFactory
        implements CacheAdapterFactory<String, PreparedStatement.ParseInfo> {
    public CacheAdapter<String, PreparedStatement.ParseInfo> getInstance(Connection forConnection, String url, int cacheMaxSize, int maxKeySize, Properties connectionProperties) throws SQLException {
        return new PerConnectionLRU(forConnection, cacheMaxSize, maxKeySize);
    }

    class PerConnectionLRU
            implements CacheAdapter<String, PreparedStatement.ParseInfo> {
        private final int cacheSqlLimit;
        private final LRUCache cache;
        private final Connection conn;

        protected PerConnectionLRU(Connection forConnection, int cacheMaxSize, int maxKeySize) {
            int cacheSize = cacheMaxSize;
            this.cacheSqlLimit = maxKeySize;
            this.cache = new LRUCache(cacheSize);
            this.conn = forConnection;
        }

        public PreparedStatement.ParseInfo get(String key) {
            if (key == null || key.length() > this.cacheSqlLimit) {
                return null;
            }

            synchronized (this.conn) {
                return (PreparedStatement.ParseInfo) this.cache.get(key);
            }
        }

        public void put(String key, PreparedStatement.ParseInfo value) {
            if (key == null || key.length() > this.cacheSqlLimit) {
                return;
            }

            synchronized (this.conn) {
                this.cache.put(key, value);
            }
        }

        public void invalidate(String key) {
            synchronized (this.conn) {
                this.cache.remove(key);
            }
        }

        public void invalidateAll(Set<String> keys) {
            synchronized (this.conn) {
                for (String key : keys) {
                    this.cache.remove(key);
                }
            }
        }

        public void invalidateAll() {
            synchronized (this.conn) {
                this.cache.clear();
            }
        }
    }
}

