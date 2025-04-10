package ch.qos.logback.core.joran.util;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.joran.spi.ConfigurationWatchList;
import ch.qos.logback.core.status.InfoStatus;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.status.WarnStatus;
import java.net.URL;

public class ConfigurationWatchListUtil
{
static final ConfigurationWatchListUtil origin = new ConfigurationWatchListUtil();

public static void setMainWatchURL(Context context, URL url) {
ConfigurationWatchList cwl = getConfigurationWatchList(context);
if (cwl == null) {
cwl = new ConfigurationWatchList();
cwl.setContext(context);
context.putObject("CONFIGURATION_WATCH_LIST", cwl);
} else {
cwl.clear();
} 
setConfigurationWatchListResetFlag(context, true);
cwl.setMainURL(url);
}

public static URL getMainWatchURL(Context context) {
ConfigurationWatchList cwl = getConfigurationWatchList(context);
if (cwl == null) {
return null;
}
return cwl.getMainURL();
}

public static void addToWatchList(Context context, URL url) {
ConfigurationWatchList cwl = getConfigurationWatchList(context);
if (cwl == null) {
addWarn(context, "Null ConfigurationWatchList. Cannot add " + url);
} else {
addInfo(context, "Adding [" + url + "] to configuration watch list.");
cwl.addToWatchList(url);
} 
}

public static boolean wasConfigurationWatchListReset(Context context) {
Object o = context.getObject("CONFIGURATION_WATCH_LIST_RESET");
if (o == null) {
return false;
}
return ((Boolean)o).booleanValue();
}

public static void setConfigurationWatchListResetFlag(Context context, boolean val) {
context.putObject("CONFIGURATION_WATCH_LIST_RESET", new Boolean(val));
}

public static ConfigurationWatchList getConfigurationWatchList(Context context) {
return (ConfigurationWatchList)context.getObject("CONFIGURATION_WATCH_LIST");
}

static void addStatus(Context context, Status s) {
if (context == null) {
System.out.println("Null context in " + ConfigurationWatchList.class.getName());
return;
} 
StatusManager sm = context.getStatusManager();
if (sm == null)
return;  sm.add(s);
}

static void addInfo(Context context, String msg) {
addStatus(context, (Status)new InfoStatus(msg, origin));
}

static void addWarn(Context context, String msg) {
addStatus(context, (Status)new WarnStatus(msg, origin));
}
}

