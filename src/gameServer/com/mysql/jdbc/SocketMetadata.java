package com.mysql.jdbc;

import java.sql.SQLException;

public interface SocketMetadata {
  boolean isLocallyConnected(ConnectionImpl paramConnectionImpl) throws SQLException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mysql/jdbc/SocketMetadata.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */