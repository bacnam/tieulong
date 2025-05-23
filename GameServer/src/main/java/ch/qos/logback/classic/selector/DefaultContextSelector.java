package ch.qos.logback.classic.selector;

import ch.qos.logback.classic.LoggerContext;
import java.util.Arrays;
import java.util.List;

public class DefaultContextSelector
implements ContextSelector
{
private LoggerContext defaultLoggerContext;

public DefaultContextSelector(LoggerContext context) {
this.defaultLoggerContext = context;
}

public LoggerContext getLoggerContext() {
return getDefaultLoggerContext();
}

public LoggerContext getDefaultLoggerContext() {
return this.defaultLoggerContext;
}

public LoggerContext detachLoggerContext(String loggerContextName) {
return this.defaultLoggerContext;
}

public List<String> getContextNames() {
return Arrays.asList(new String[] { this.defaultLoggerContext.getName() });
}

public LoggerContext getLoggerContext(String name) {
if (this.defaultLoggerContext.getName().equals(name)) {
return this.defaultLoggerContext;
}
return null;
}
}

