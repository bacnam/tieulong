package com.mchange.v2.c3p0;

import java.sql.Connection;

public interface FullQueryConnectionTester extends QueryConnectionTester {
  int statusOnException(Connection paramConnection, Throwable paramThrowable, String paramString);
}

