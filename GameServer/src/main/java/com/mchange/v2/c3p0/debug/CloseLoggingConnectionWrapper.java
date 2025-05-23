package com.mchange.v2.c3p0.debug;

import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import com.mchange.v2.sql.filter.FilterConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;

public class CloseLoggingConnectionWrapper
extends FilterConnection
{
static final MLogger logger = MLog.getLogger(CloseLoggingConnectionWrapper.class);

final MLevel level;

public CloseLoggingConnectionWrapper(Connection conn, MLevel level) {
super(conn);
this.level = level;
}

public void close() throws SQLException {
super.close();
if (logger.isLoggable(this.level))
logger.log(this.level, "DEBUG: A Connection has closed been close()ed without error.", new SQLWarning("DEBUG STACK TRACE -- Connection.close() was called.")); 
}
}

