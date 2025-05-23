package com.mchange.v1.db.sql;

import com.mchange.lang.ThrowableUtils;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class SqlUtils
{
static final DateFormat tsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");

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

public static String escapeAsTimestamp(Date paramDate) {
return "{ts '" + tsdf.format(paramDate) + "'}";
}

public static SQLException toSQLException(Throwable paramThrowable) {
if (paramThrowable instanceof SQLException) {
return (SQLException)paramThrowable;
}

paramThrowable.printStackTrace();
return new SQLException(ThrowableUtils.extractStackTrace(paramThrowable));
}
}

