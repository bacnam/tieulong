package ch.qos.logback.classic.boolex;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggerContextVO;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.boolex.JaninoEventEvaluatorBase;
import ch.qos.logback.core.boolex.Matcher;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.slf4j.Marker;

public class JaninoEventEvaluator
extends JaninoEventEvaluatorBase<ILoggingEvent>
{
public static final String IMPORT_LEVEL = "import ch.qos.logback.classic.Level;\r\n";
public static final List<String> DEFAULT_PARAM_NAME_LIST = new ArrayList<String>();
public static final List<Class> DEFAULT_PARAM_TYPE_LIST = (List)new ArrayList<Class<?>>();

static {
DEFAULT_PARAM_NAME_LIST.add("DEBUG");
DEFAULT_PARAM_NAME_LIST.add("INFO");
DEFAULT_PARAM_NAME_LIST.add("WARN");
DEFAULT_PARAM_NAME_LIST.add("ERROR");

DEFAULT_PARAM_NAME_LIST.add("event");
DEFAULT_PARAM_NAME_LIST.add("message");

DEFAULT_PARAM_NAME_LIST.add("formattedMessage");
DEFAULT_PARAM_NAME_LIST.add("logger");
DEFAULT_PARAM_NAME_LIST.add("loggerContext");
DEFAULT_PARAM_NAME_LIST.add("level");
DEFAULT_PARAM_NAME_LIST.add("timeStamp");
DEFAULT_PARAM_NAME_LIST.add("marker");
DEFAULT_PARAM_NAME_LIST.add("mdc");
DEFAULT_PARAM_NAME_LIST.add("throwableProxy");
DEFAULT_PARAM_NAME_LIST.add("throwable");

DEFAULT_PARAM_TYPE_LIST.add(int.class);
DEFAULT_PARAM_TYPE_LIST.add(int.class);
DEFAULT_PARAM_TYPE_LIST.add(int.class);
DEFAULT_PARAM_TYPE_LIST.add(int.class);

DEFAULT_PARAM_TYPE_LIST.add(ILoggingEvent.class);
DEFAULT_PARAM_TYPE_LIST.add(String.class);
DEFAULT_PARAM_TYPE_LIST.add(String.class);
DEFAULT_PARAM_TYPE_LIST.add(String.class);
DEFAULT_PARAM_TYPE_LIST.add(LoggerContextVO.class);
DEFAULT_PARAM_TYPE_LIST.add(int.class);
DEFAULT_PARAM_TYPE_LIST.add(long.class);
DEFAULT_PARAM_TYPE_LIST.add(Marker.class);
DEFAULT_PARAM_TYPE_LIST.add(Map.class);
DEFAULT_PARAM_TYPE_LIST.add(IThrowableProxy.class);
DEFAULT_PARAM_TYPE_LIST.add(Throwable.class);
}

protected String getDecoratedExpression() {
String expression = getExpression();
if (!expression.contains("return")) {
expression = "return " + expression + ";";
addInfo("Adding [return] prefix and a semicolon suffix. Expression becomes [" + expression + "]");
addInfo("See also http:
} 

return "import ch.qos.logback.classic.Level;\r\n" + expression;
}

protected String[] getParameterNames() {
List<String> fullNameList = new ArrayList<String>();
fullNameList.addAll(DEFAULT_PARAM_NAME_LIST);

for (int i = 0; i < this.matcherList.size(); i++) {
Matcher m = this.matcherList.get(i);
fullNameList.add(m.getName());
} 

return fullNameList.<String>toArray(CoreConstants.EMPTY_STRING_ARRAY);
}

protected Class[] getParameterTypes() {
List<Class<?>> fullTypeList = new ArrayList<Class<?>>();
fullTypeList.addAll((Collection)DEFAULT_PARAM_TYPE_LIST);
for (int i = 0; i < this.matcherList.size(); i++) {
fullTypeList.add(Matcher.class);
}
return (Class[])fullTypeList.<Class<?>[]>toArray((Class<?>[][])CoreConstants.EMPTY_CLASS_ARRAY);
}

protected Object[] getParameterValues(ILoggingEvent loggingEvent) {
int matcherListSize = this.matcherList.size();

int i = 0;
Object[] values = new Object[DEFAULT_PARAM_NAME_LIST.size() + matcherListSize];

values[i++] = Level.DEBUG_INTEGER;
values[i++] = Level.INFO_INTEGER;
values[i++] = Level.WARN_INTEGER;
values[i++] = Level.ERROR_INTEGER;

values[i++] = loggingEvent;
values[i++] = loggingEvent.getMessage();
values[i++] = loggingEvent.getFormattedMessage();
values[i++] = loggingEvent.getLoggerName();
values[i++] = loggingEvent.getLoggerContextVO();
values[i++] = loggingEvent.getLevel().toInteger();
values[i++] = Long.valueOf(loggingEvent.getTimeStamp());

values[i++] = loggingEvent.getMarker();
values[i++] = loggingEvent.getMDCPropertyMap();

IThrowableProxy iThrowableProxy = loggingEvent.getThrowableProxy();

if (iThrowableProxy != null) {
values[i++] = iThrowableProxy;
if (iThrowableProxy instanceof ThrowableProxy) {
values[i++] = ((ThrowableProxy)iThrowableProxy).getThrowable();
} else {
values[i++] = null;
} 
} else {
values[i++] = null;
values[i++] = null;
} 

for (int j = 0; j < matcherListSize; j++) {
values[i++] = this.matcherList.get(j);
}

return values;
}
}

