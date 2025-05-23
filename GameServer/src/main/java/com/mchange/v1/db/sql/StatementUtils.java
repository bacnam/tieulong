package com.mchange.v1.db.sql;

import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import java.sql.SQLException;
import java.sql.Statement;

public final class StatementUtils
{
private static final MLogger logger = MLog.getLogger(StatementUtils.class);

public static boolean attemptClose(Statement paramStatement) {
try {
if (paramStatement != null) paramStatement.close(); 
return true;
}
catch (SQLException sQLException) {

if (logger.isLoggable(MLevel.WARNING))
logger.log(MLevel.WARNING, "Statement close FAILED.", sQLException); 
return false;
} 
}
}

