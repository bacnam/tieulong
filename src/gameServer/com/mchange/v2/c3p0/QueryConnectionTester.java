package com.mchange.v2.c3p0;

import java.sql.Connection;

public interface QueryConnectionTester extends ConnectionTester {
  int activeCheckConnection(Connection paramConnection, String paramString);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/QueryConnectionTester.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */