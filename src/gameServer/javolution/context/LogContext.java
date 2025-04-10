package javolution.context;

import javolution.lang.Configurable;
import javolution.osgi.internal.OSGiServices;

public abstract class LogContext
extends AbstractContext
{
public enum Level
{
DEBUG, INFO, WARNING, ERROR, FATAL;
}

public static final Configurable<Level> LEVEL = new Configurable<Level>()
{
protected LogContext.Level getDefault() {
return LogContext.Level.DEBUG;
}

public LogContext.Level parse(String str) {
return LogContext.Level.valueOf(str);
}
};

public static void debug(Object... message) {
currentLogContext().log(Level.DEBUG, message);
}

public static LogContext enter() {
return (LogContext)currentLogContext().enterInner();
}

public static void error(Object... message) {
currentLogContext().log(Level.ERROR, message);
}

public static void info(Object... message) {
currentLogContext().log(Level.INFO, message);
}

public static void warning(Object... message) {
currentLogContext().log(Level.WARNING, message);
}

private static LogContext currentLogContext() {
LogContext ctx = current(LogContext.class);
if (ctx != null)
return ctx; 
return OSGiServices.getLogContext();
}

public abstract void prefix(Object... paramVarArgs);

public abstract void setLevel(Level paramLevel);

public abstract void suffix(Object... paramVarArgs);

protected abstract void log(Level paramLevel, Object... paramVarArgs);
}

