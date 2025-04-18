package com.mchange.v2.c3p0;

import java.sql.Connection;

public abstract class AbstractConnectionTester
implements UnifiedConnectionTester
{
public abstract int activeCheckConnection(Connection paramConnection, String paramString, Throwable[] paramArrayOfThrowable);

public abstract int statusOnException(Connection paramConnection, Throwable paramThrowable, String paramString, Throwable[] paramArrayOfThrowable);

public int activeCheckConnection(Connection c) {
return activeCheckConnection(c, null, null);
}
public int activeCheckConnection(Connection c, Throwable[] rootCauseOutParamHolder) {
return activeCheckConnection(c, null, rootCauseOutParamHolder);
}
public int activeCheckConnection(Connection c, String preferredTestQuery) {
return activeCheckConnection(c, preferredTestQuery, null);
}
public int statusOnException(Connection c, Throwable t) {
return statusOnException(c, t, null, null);
}
public int statusOnException(Connection c, Throwable t, Throwable[] rootCauseOutParamHolder) {
return statusOnException(c, t, null, rootCauseOutParamHolder);
}
public int statusOnException(Connection c, Throwable t, String preferredTestQuery) {
return statusOnException(c, t, preferredTestQuery, null);
}
}

