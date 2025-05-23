package com.mchange.v2.log.jdk14logging;

import com.mchange.v2.log.LogUtils;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLogger;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class ForwardingLogger
extends Logger
{
MLogger forwardTo;

public ForwardingLogger(MLogger paramMLogger, String paramString) {
super(paramMLogger.getName(), paramString);
this.forwardTo = paramMLogger;
}

public void log(LogRecord paramLogRecord) {
Level level = paramLogRecord.getLevel();
MLevel mLevel = Jdk14LoggingUtils.mlevelFromLevel(level);

String str1 = paramLogRecord.getResourceBundleName();
String str2 = paramLogRecord.getMessage();
Object[] arrayOfObject = paramLogRecord.getParameters();

String str3 = LogUtils.formatMessage(str1, str2, arrayOfObject);

Throwable throwable = paramLogRecord.getThrown();

String str4 = paramLogRecord.getSourceClassName();
String str5 = paramLogRecord.getSourceMethodName();

int i = ((str4 != null) ? 1 : 0) & ((str5 != null) ? 1 : 0);

if (i == 0) {
this.forwardTo.log(mLevel, str3, throwable);
} else {
this.forwardTo.logp(mLevel, str4, str5, str3, throwable);
} 
}
}

