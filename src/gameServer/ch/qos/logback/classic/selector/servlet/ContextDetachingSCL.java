package ch.qos.logback.classic.selector.servlet;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.selector.ContextSelector;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
import ch.qos.logback.classic.util.JNDIUtil;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextDetachingSCL
implements ServletContextListener
{
public void contextDestroyed(ServletContextEvent servletContextEvent) {
String loggerContextName = null;

try {
Context ctx = JNDIUtil.getInitialContext();
loggerContextName = JNDIUtil.lookup(ctx, "java:comp/env/logback/context-name");
} catch (NamingException ne) {}

if (loggerContextName != null) {
System.out.println("About to detach context named " + loggerContextName);

ContextSelector selector = ContextSelectorStaticBinder.getSingleton().getContextSelector();
if (selector == null) {
System.out.println("Selector is null, cannot detach context. Skipping.");
return;
} 
LoggerContext context = selector.getLoggerContext(loggerContextName);
if (context != null) {
Logger logger = context.getLogger("ROOT");
logger.warn("Stopping logger context " + loggerContextName);
selector.detachLoggerContext(loggerContextName);

context.stop();
} else {
System.out.println("No context named " + loggerContextName + " was found.");
} 
} 
}

public void contextInitialized(ServletContextEvent arg0) {}
}

