package com.mchange.v1.db.sql;

import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import java.sql.Connection;
import java.sql.SQLException;

public final class ConnectionUtils
{
private static final MLogger logger = MLog.getLogger(ConnectionUtils.class);

public static boolean attemptClose(Connection paramConnection) {
try {
if (paramConnection != null) paramConnection.close();

return true;
}
catch (SQLException sQLException) {

if (logger.isLoggable(MLevel.WARNING))
logger.log(MLevel.WARNING, "Connection close FAILED.", sQLException); 
return false;
} 
}

public static boolean attemptRollback(Connection paramConnection) {
try {
if (paramConnection != null) paramConnection.rollback(); 
return true;
}
catch (SQLException sQLException) {

if (logger.isLoggable(MLevel.WARNING))
logger.log(MLevel.WARNING, "Rollback FAILED.", sQLException); 
return false;
} 
}
}

