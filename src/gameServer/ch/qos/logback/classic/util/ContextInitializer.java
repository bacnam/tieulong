package ch.qos.logback.classic.util;

import ch.qos.logback.classic.BasicConfigurator;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.gaffer.GafferUtil;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.LogbackException;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.core.status.InfoStatus;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.status.WarnStatus;
import ch.qos.logback.core.util.Loader;
import ch.qos.logback.core.util.OptionHelper;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

public class ContextInitializer
{
public static final String GROOVY_AUTOCONFIG_FILE = "logback.groovy";
public static final String AUTOCONFIG_FILE = "logback.xml";
public static final String TEST_AUTOCONFIG_FILE = "logback-test.xml";
public static final String CONFIG_FILE_PROPERTY = "logback.configurationFile";
public static final String STATUS_LISTENER_CLASS = "logback.statusListenerClass";
public static final String SYSOUT = "SYSOUT";
final LoggerContext loggerContext;

public ContextInitializer(LoggerContext loggerContext) {
this.loggerContext = loggerContext;
}

public void configureByResource(URL url) throws JoranException {
if (url == null) {
throw new IllegalArgumentException("URL argument cannot be null");
}
String urlString = url.toString();
if (urlString.endsWith("groovy")) {
if (EnvUtil.isGroovyAvailable()) {

GafferUtil.runGafferConfiguratorOn(this.loggerContext, this, url);
} else {
StatusManager sm = this.loggerContext.getStatusManager();
sm.add((Status)new ErrorStatus("Groovy classes are not available on the class path. ABORTING INITIALIZATION.", this.loggerContext));
}

} else if (urlString.endsWith("xml")) {
JoranConfigurator configurator = new JoranConfigurator();
configurator.setContext((Context)this.loggerContext);
configurator.doConfigure(url);
} else {
throw new LogbackException("Unexpected filename extension of file [" + url.toString() + "]. Should be either .groovy or .xml");
} 
}

void joranConfigureByResource(URL url) throws JoranException {
JoranConfigurator configurator = new JoranConfigurator();
configurator.setContext((Context)this.loggerContext);
configurator.doConfigure(url);
}

private URL findConfigFileURLFromSystemProperties(ClassLoader classLoader, boolean updateStatus) {
String logbackConfigFile = OptionHelper.getSystemProperty("logback.configurationFile");
if (logbackConfigFile != null) {
URL result = null;
try {
result = new URL(logbackConfigFile);
return result;
} catch (MalformedURLException e) {

result = Loader.getResource(logbackConfigFile, classLoader);
if (result != null) {
return result;
}
File f = new File(logbackConfigFile);
if (f.exists() && f.isFile()) {
try {
result = f.toURI().toURL();
return result;
} catch (MalformedURLException e1) {}
}
} finally {

if (updateStatus) {
statusOnResourceSearch(logbackConfigFile, classLoader, result);
}
} 
} 
return null;
}

public URL findURLOfDefaultConfigurationFile(boolean updateStatus) {
ClassLoader myClassLoader = Loader.getClassLoaderOfObject(this);
URL url = findConfigFileURLFromSystemProperties(myClassLoader, updateStatus);
if (url != null) {
return url;
}

url = getResource("logback.groovy", myClassLoader, updateStatus);
if (url != null) {
return url;
}

url = getResource("logback-test.xml", myClassLoader, updateStatus);
if (url != null) {
return url;
}

return getResource("logback.xml", myClassLoader, updateStatus);
}

private URL getResource(String filename, ClassLoader myClassLoader, boolean updateStatus) {
URL url = Loader.getResource(filename, myClassLoader);
if (updateStatus) {
statusOnResourceSearch(filename, myClassLoader, url);
}
return url;
}

public void autoConfig() throws JoranException {
StatusListenerConfigHelper.installIfAsked(this.loggerContext);
URL url = findURLOfDefaultConfigurationFile(true);
if (url != null) {
configureByResource(url);
} else {
Configurator c = EnvUtil.<Configurator>loadFromServiceLoader(Configurator.class);
if (c != null) {
try {
c.setContext((Context)this.loggerContext);
c.configure(this.loggerContext);
} catch (Exception e) {
throw new LogbackException(String.format("Failed to initialize Configurator: %s using ServiceLoader", new Object[] { (c != null) ? c.getClass().getCanonicalName() : "null" }), e);
} 
} else {

BasicConfigurator.configure(this.loggerContext);
} 
} 
}

private void multiplicityWarning(String resourceName, ClassLoader classLoader) {
Set<URL> urlSet = null;
StatusManager sm = this.loggerContext.getStatusManager();
try {
urlSet = Loader.getResourceOccurrenceCount(resourceName, classLoader);
} catch (IOException e) {
sm.add((Status)new ErrorStatus("Failed to get url list for resource [" + resourceName + "]", this.loggerContext, e));
} 

if (urlSet != null && urlSet.size() > 1) {
sm.add((Status)new WarnStatus("Resource [" + resourceName + "] occurs multiple times on the classpath.", this.loggerContext));

for (URL url : urlSet) {
sm.add((Status)new WarnStatus("Resource [" + resourceName + "] occurs at [" + url.toString() + "]", this.loggerContext));
}
} 
}

private void statusOnResourceSearch(String resourceName, ClassLoader classLoader, URL url) {
StatusManager sm = this.loggerContext.getStatusManager();
if (url == null) {
sm.add((Status)new InfoStatus("Could NOT find resource [" + resourceName + "]", this.loggerContext));
} else {

sm.add((Status)new InfoStatus("Found resource [" + resourceName + "] at [" + url.toString() + "]", this.loggerContext));

multiplicityWarning(resourceName, classLoader);
} 
}
}

