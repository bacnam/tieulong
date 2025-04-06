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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/db/ConnectionSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */