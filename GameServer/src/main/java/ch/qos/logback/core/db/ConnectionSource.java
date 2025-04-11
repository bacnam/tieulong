package ch.qos.logback.core.db;

import ch.qos.logback.core.db.dialect.SQLDialectCode;
import ch.qos.logback.core.spi.LifeCycle;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionSource extends LifeCycle {
    Connection getConnection() throws SQLException;

    SQLDialectCode getSQLDialectCode();

    boolean supportsGetGeneratedKeys();

    boolean supportsBatchUpdates();
}

