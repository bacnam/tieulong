package com.mchange.v2.c3p0;

import java.sql.Connection;

public interface UnifiedConnectionTester extends FullQueryConnectionTester {
  public static final int CONNECTION_IS_OKAY = 0;

  public static final int CONNECTION_IS_INVALID = -1;

  public static final int DATABASE_IS_INVALID = -8;

  int activeCheckConnection(Connection paramConnection);

  int activeCheckConnection(Connection paramConnection, Throwable[] paramArrayOfThrowable);

  int activeCheckConnection(Connection paramConnection, String paramString);

  int activeCheckConnection(Connection paramConnection, String paramString, Throwable[] paramArrayOfThrowable);

  int statusOnException(Connection paramConnection, Throwable paramThrowable);

  int statusOnException(Connection paramConnection, Throwable paramThrowable, Throwable[] paramArrayOfThrowable);

  int statusOnException(Connection paramConnection, Throwable paramThrowable, String paramString);

  int statusOnException(Connection paramConnection, Throwable paramThrowable, String paramString, Throwable[] paramArrayOfThrowable);

  boolean equals(Object paramObject);

  int hashCode();
}

