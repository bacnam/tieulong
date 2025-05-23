package com.mchange.v2.sql;

import com.mchange.lang.ThrowableUtils;
import com.mchange.v2.lang.VersionUtils;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class SqlUtils
{
static final MLogger logger = MLog.getLogger(SqlUtils.class);

static final DateFormat tsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");

public static final String DRIVER_MANAGER_USER_PROPERTY = "user";

public static final String DRIVER_MANAGER_PASSWORD_PROPERTY = "password";

public static String escapeBadSqlPatternChars(String paramString) {
StringBuffer stringBuffer = new StringBuffer(paramString); byte b; int i;
for (b = 0, i = stringBuffer.length(); b < i; b++) {
if (stringBuffer.charAt(b) == '\'') {

stringBuffer.insert(b, '\'');
i++;
b += 2;
} 
}  return stringBuffer.toString();
}

public static synchronized String escapeAsTimestamp(Date paramDate) {
return "{ts '" + tsdf.format(paramDate) + "'}";
}
public static SQLException toSQLException(Throwable paramThrowable) {
return toSQLException(null, paramThrowable);
}
public static SQLException toSQLException(String paramString, Throwable paramThrowable) {
return toSQLException(paramString, null, paramThrowable);
}

public static SQLException toSQLException(String paramString1, String paramString2, Throwable paramThrowable) {
if (paramThrowable instanceof SQLException) {

if (logger.isLoggable(MLevel.FINER)) {

SQLException sQLException1 = (SQLException)paramThrowable;
StringBuffer stringBuffer = new StringBuffer(255);
stringBuffer.append("Attempted to convert SQLException to SQLException. Leaving it alone.");
stringBuffer.append(" [SQLState: ");
stringBuffer.append(sQLException1.getSQLState());
stringBuffer.append("; errorCode: ");
stringBuffer.append(sQLException1.getErrorCode());
stringBuffer.append(']');
if (paramString1 != null)
stringBuffer.append(" Ignoring suggested message: '" + paramString1 + "'."); 
logger.log(MLevel.FINER, stringBuffer.toString(), paramThrowable);

SQLException sQLException2 = sQLException1;
while ((sQLException2 = sQLException2.getNextException()) != null)
logger.log(MLevel.FINER, "Nested SQLException or SQLWarning: ", sQLException2); 
} 
return (SQLException)paramThrowable;
} 

if (logger.isLoggable(MLevel.FINE)) {
logger.log(MLevel.FINE, "Converting Throwable to SQLException...", paramThrowable);
}

if (paramString1 == null)
paramString1 = "An SQLException was provoked by the following failure: " + paramThrowable.toString(); 
if (VersionUtils.isAtLeastJavaVersion14()) {

SQLException sQLException = new SQLException(paramString1);
sQLException.initCause(paramThrowable);
return sQLException;
} 

return new SQLException(paramString1 + System.getProperty("line.separator") + "[Cause: " + ThrowableUtils.extractStackTrace(paramThrowable) + ']', paramString2);
}

public static SQLClientInfoException toSQLClientInfoException(Throwable paramThrowable) {
if (paramThrowable instanceof SQLClientInfoException)
return (SQLClientInfoException)paramThrowable; 
if (paramThrowable.getCause() instanceof SQLClientInfoException)
return (SQLClientInfoException)paramThrowable.getCause(); 
if (paramThrowable instanceof SQLException) {

SQLException sQLException = (SQLException)paramThrowable;
return new SQLClientInfoException(sQLException.getMessage(), sQLException.getSQLState(), sQLException.getErrorCode(), null, paramThrowable);
} 

return new SQLClientInfoException(paramThrowable.getMessage(), null, paramThrowable);
}
}

