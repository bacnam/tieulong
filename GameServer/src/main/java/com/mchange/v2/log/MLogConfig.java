package com.mchange.v2.log;

import com.mchange.v2.cfg.DelayedLogItem;
import com.mchange.v2.cfg.MConfig;
import com.mchange.v2.cfg.MLogConfigSource;
import com.mchange.v2.cfg.MultiPropertiesConfig;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public final class MLogConfig
{
private static MultiPropertiesConfig CONFIG = null;
private static List BOOTSTRAP_LOG_ITEMS = null;
private static Method delayedDumpToLogger = null;

public static synchronized void refresh(MultiPropertiesConfig[] paramArrayOfMultiPropertiesConfig, String paramString) {
String[] arrayOfString1 = { "/com/mchange/v2/log/default-mchange-log.properties" };
String[] arrayOfString2 = { "/mchange-log.properties", "/" };

ArrayList<DelayedLogItem> arrayList = new ArrayList();
MultiPropertiesConfig multiPropertiesConfig = MLogConfigSource.readVmConfig(arrayOfString1, arrayOfString2, arrayList);

boolean bool = (CONFIG == null) ? true : false;

if (paramArrayOfMultiPropertiesConfig != null) {

int i = paramArrayOfMultiPropertiesConfig.length;
MultiPropertiesConfig[] arrayOfMultiPropertiesConfig = new MultiPropertiesConfig[i + 1];
arrayOfMultiPropertiesConfig[0] = multiPropertiesConfig;
for (byte b = 0; b < i; b++)
arrayOfMultiPropertiesConfig[b + 1] = paramArrayOfMultiPropertiesConfig[b]; 
arrayList.add(new DelayedLogItem(DelayedLogItem.Level.INFO, (bool ? "Loaded" : "Refreshed") + " MLog library log configuration, with overrides" + ((paramString == null) ? "." : (": " + paramString))));
CONFIG = MConfig.combine(arrayOfMultiPropertiesConfig);
}
else {

if (!bool)
arrayList.add(new DelayedLogItem(DelayedLogItem.Level.INFO, "Refreshed MLog library log configuration, without overrides.")); 
CONFIG = multiPropertiesConfig;
} 
BOOTSTRAP_LOG_ITEMS = arrayList;
}

private static void ensureLoad() {
if (CONFIG == null) refresh(null, null);

}

private static void ensureDelayedDumpToLogger() {
try {
if (delayedDumpToLogger == null)
{
Class<?> clazz1 = Class.forName("com.mchange.v2.cfg.MConfig");
Class<?> clazz2 = Class.forName("com.mchange.v2.cfg.DelayedLogItem");
delayedDumpToLogger = clazz1.getMethod("dumpToLogger", new Class[] { clazz2, MLogger.class });
}

} catch (RuntimeException runtimeException) {

runtimeException.printStackTrace();
throw runtimeException;
}
catch (Exception exception) {

exception.printStackTrace();
throw new RuntimeException(exception);
} 
}

public static synchronized String getProperty(String paramString) {
ensureLoad();
return CONFIG.getProperty(paramString);
}

public static synchronized void logDelayedItems(MLogger paramMLogger) {
ensureLoad();
ensureDelayedDumpToLogger();

ArrayList arrayList = new ArrayList();
arrayList.addAll(BOOTSTRAP_LOG_ITEMS);
arrayList.addAll(CONFIG.getDelayedLogItems());

HashSet hashSet = new HashSet();
hashSet.addAll(arrayList);

for (Object object : arrayList) {

if (hashSet.contains(object)) {

hashSet.remove(object);
try {
delayedDumpToLogger.invoke(null, new Object[] { object, paramMLogger });
} catch (Exception exception) {

exception.printStackTrace();
throw new Error(exception);
} 
} 
} 
}

public static synchronized String dump() {
return CONFIG.toString();
}
}

