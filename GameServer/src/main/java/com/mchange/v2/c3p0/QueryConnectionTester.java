package com.mchange.v2.c3p0;

import java.sql.Connection;

public interface QueryConnectionTester extends ConnectionTester {
  int activeCheckConnection(Connection paramConnection, String paramString);
}

