package com.mysql.jdbc;

import java.sql.SQLException;

public interface SocketMetadata {
  boolean isLocallyConnected(ConnectionImpl paramConnectionImpl) throws SQLException;
}

