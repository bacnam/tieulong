package com.mchange.v2.c3p0.impl;

import java.sql.ResultSet;

public interface ProxyResultSetDetachable {
  void detachProxyResultSet(ResultSet paramResultSet);
}

