package ch.qos.logback.classic.util;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.status.OnConsoleStatusListener;
import ch.qos.logback.core.status.StatusListener;
import ch.qos.logback.core.util.OptionHelper;

public class StatusListenerConfigHelper
{
static void installIfAsked(LoggerContext loggerContext) {
String slClass = OptionHelper.getSystemProperty("logback.statusListenerClass");

if (!OptionHelper.isEmpty(slClass)) {
addStatusListener(loggerContext, slClass);
}
}

private static void addStatusListener(LoggerContext loggerContext, String listenerClass) {
StatusListener listener = null;
if ("SYSOUT".equalsIgnoreCase(listenerClass)) {
OnConsoleStatusListener onConsoleStatusListener = new OnConsoleStatusListener();
} else {
listener = createListenerPerClassName(loggerContext, listenerClass);
} 
initAndAddListener(loggerContext, listener);
}

private static void initAndAddListener(LoggerContext loggerContext, StatusListener listener) {
if (listener != null) {
if (listener instanceof ContextAware)
((ContextAware)listener).setContext((Context)loggerContext); 
if (listener instanceof LifeCycle)
((LifeCycle)listener).start(); 
loggerContext.getStatusManager().add(listener);
} 
}

private static StatusListener createListenerPerClassName(LoggerContext loggerContext, String listenerClass) {
try {
return (StatusListener)OptionHelper.instantiateByClassName(listenerClass, StatusListener.class, (Context)loggerContext);
}
catch (Exception e) {

e.printStackTrace();
return null;
} 
}
}

