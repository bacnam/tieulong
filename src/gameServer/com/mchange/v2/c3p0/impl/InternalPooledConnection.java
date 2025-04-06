package com.mchange.v2.c3p0.impl;

import com.mchange.v2.c3p0.stmt.GooGooStatementCache;
import javax.sql.PooledConnection;

interface InternalPooledConnection extends PooledConnection {
  void initStatementCache(GooGooStatementCache paramGooGooStatementCache);
  
  GooGooStatementCache getStatementCache();
  
  int getConnectionStatus();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/InternalPooledConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */