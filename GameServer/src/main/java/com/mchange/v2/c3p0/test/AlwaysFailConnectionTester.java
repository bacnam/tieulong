package com.mchange.v2.c3p0.test;

import com.mchange.v2.c3p0.QueryConnectionTester;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import java.sql.Connection;

public final class AlwaysFailConnectionTester
implements QueryConnectionTester
{
public AlwaysFailConnectionTester() {
logger.log(MLevel.WARNING, "Instantiated: " + this, new Exception("Instantiation Stack Trace."));
}

public int activeCheckConnection(Connection c) {
logger.warning(this + ": activeCheckConnection(Connection c)");
return -1;
}
static final MLogger logger = MLog.getLogger(AlwaysFailConnectionTester.class);

public int statusOnException(Connection c, Throwable t) {
logger.warning(this + ": statusOnException(Connection c, Throwable t)");
return -1;
}

public int activeCheckConnection(Connection c, String preferredTestQuery) {
logger.warning(this + ": activeCheckConnection(Connection c, String preferredTestQuery)");
return -1;
}

public boolean equals(Object o) {
return o instanceof AlwaysFailConnectionTester;
}
public int hashCode() {
return 1;
}
}

