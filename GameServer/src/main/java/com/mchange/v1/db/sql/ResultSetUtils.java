package com.mchange.v1.db.sql;

import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class ResultSetUtils
{
private static final MLogger logger = MLog.getLogger(ResultSetUtils.class);

public static boolean attemptClose(ResultSet paramResultSet) {
try {
if (paramResultSet != null) paramResultSet.close(); 
return true;
}
catch (SQLException sQLException) {

if (logger.isLoggable(MLevel.WARNING))
logger.log(MLevel.WARNING, "ResultSet close FAILED.", sQLException); 
return false;
} 
}
}

