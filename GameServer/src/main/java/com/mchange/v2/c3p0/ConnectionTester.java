package com.mchange.v2.c3p0;

import java.io.Serializable;
import java.sql.Connection;

public interface ConnectionTester extends Serializable {
  public static final int CONNECTION_IS_OKAY = 0;

  public static final int CONNECTION_IS_INVALID = -1;

  public static final int DATABASE_IS_INVALID = -8;

  int activeCheckConnection(Connection paramConnection);

  int statusOnException(Connection paramConnection, Throwable paramThrowable);

  boolean equals(Object paramObject);

  int hashCode();
}

