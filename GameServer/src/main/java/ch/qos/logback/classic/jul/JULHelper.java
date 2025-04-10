package ch.qos.logback.classic.jul;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JULHelper
{
public static final boolean isRegularNonRootLogger(Logger julLogger) {
if (julLogger == null)
return false; 
return !julLogger.getName().equals("");
}

public static final boolean isRoot(Logger julLogger) {
if (julLogger == null)
return false; 
return julLogger.getName().equals("");
}

public static Level asJULLevel(Level lbLevel) {
if (lbLevel == null) {
throw new IllegalArgumentException("Unexpected level [null]");
}
switch (lbLevel.levelInt) {
case -2147483648:
return Level.ALL;
case 5000:
return Level.FINEST;
case 10000:
return Level.FINE;
case 20000:
return Level.INFO;
case 30000:
return Level.WARNING;
case 40000:
return Level.SEVERE;
case 2147483647:
return Level.OFF;
} 
throw new IllegalArgumentException("Unexpected level [" + lbLevel + "]");
}

public static String asJULLoggerName(String loggerName) {
if ("ROOT".equals(loggerName)) {
return "";
}
return loggerName;
}

public static Logger asJULLogger(String loggerName) {
String julLoggerName = asJULLoggerName(loggerName);
return Logger.getLogger(julLoggerName);
}

public static Logger asJULLogger(Logger logger) {
return asJULLogger(logger.getName());
}
}

