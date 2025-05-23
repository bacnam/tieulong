package com.mchange.v2.c3p0.impl;

import com.mchange.v2.c3p0.stmt.GooGooStatementCache;
import javax.sql.PooledConnection;

interface InternalPooledConnection extends PooledConnection {
  void initStatementCache(GooGooStatementCache paramGooGooStatementCache);

  GooGooStatementCache getStatementCache();

  int getConnectionStatus();
}

