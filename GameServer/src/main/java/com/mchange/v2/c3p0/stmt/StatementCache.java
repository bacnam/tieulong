package com.mchange.v2.c3p0.stmt;

import com.mchange.v1.util.ClosableResource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

public interface StatementCache extends ClosableResource {
  Object checkoutStatement(Connection paramConnection, Method paramMethod, Object[] paramArrayOfObject) throws SQLException;

  void checkinStatement(Object paramObject) throws SQLException;

  void checkinAll(Connection paramConnection) throws SQLException;

  void closeAll(Connection paramConnection) throws SQLException;

  void close() throws SQLException;
}

