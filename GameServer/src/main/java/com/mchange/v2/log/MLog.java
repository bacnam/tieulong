package com.mchange.v2.log;

import com.mchange.v1.util.StringTokenizerUtils;
import com.mchange.v2.cfg.MultiPropertiesConfig;
import java.util.ArrayList;

public abstract class MLog
{
private static NameTransformer _transformer;
private static MLog _mlog;
private static MLogger _logger;

static {
refreshConfig(null, null);
}
private static synchronized NameTransformer transformer() { return _transformer; }
private static synchronized MLog mlog() { return _mlog; } private static synchronized MLogger logger() {
return _logger;
}

public static synchronized void refreshConfig(MultiPropertiesConfig[] paramArrayOfMultiPropertiesConfig, String paramString) {
MLogConfig.refresh(paramArrayOfMultiPropertiesConfig, paramString);

String str1 = MLogConfig.getProperty("com.mchange.v2.log.MLog");
String[] arrayOfString = null;
if (str1 == null)
str1 = MLogConfig.getProperty("com.mchange.v2.log.mlog"); 
if (str1 != null) {
arrayOfString = StringTokenizerUtils.tokenizeToArray(str1, ", \t\r\n");
}
boolean bool = false;
MLog mLog = null;
if (arrayOfString != null)
mLog = findByClassnames(arrayOfString, true); 
if (mLog == null)
mLog = findByClassnames(MLogClasses.SEARCH_CLASSNAMES, false); 
if (mLog == null) {

bool = true;
mLog = new FallbackMLog();
} 
_mlog = mLog;
if (bool) {
info("Using " + _mlog.getClass().getName() + " -- Named logger's not supported, everything goes to System.err.");
}
NameTransformer nameTransformer = null;
String str2 = MLogConfig.getProperty("com.mchange.v2.log.NameTransformer");
if (str2 == null) {
str2 = MLogConfig.getProperty("com.mchange.v2.log.nametransformer");
}
try {
if (str2 != null) {
nameTransformer = (NameTransformer)Class.forName(str2).newInstance();
}
} catch (Exception exception) {

System.err.println("Failed to instantiate com.mchange.v2.log.NameTransformer '" + str2 + "'!");
exception.printStackTrace();
} 
_transformer = nameTransformer;

_logger = getLogger(MLog.class);

Thread thread = new Thread("MLog-Init-Reporter")
{

final MLogger logo = MLog._logger;
String loggerDesc = MLog._mlog.getClass().getName();

public void run() {
if ("com.mchange.v2.log.jdk14logging.Jdk14MLog".equals(this.loggerDesc)) {
this.loggerDesc = "java 1.4+ standard";
} else if ("com.mchange.v2.log.log4j.Log4jMLog".equals(this.loggerDesc)) {
this.loggerDesc = "log4j";
} else if ("com.mchange.v2.log.slf4j.Slf4jMLog".equals(this.loggerDesc)) {
this.loggerDesc = "slf4j";
} 
if (this.logo.isLoggable(MLevel.INFO)) {
this.logo.log(MLevel.INFO, "MLog clients using " + this.loggerDesc + " logging.");
}

MLogConfig.logDelayedItems(this.logo);

if (this.logo.isLoggable(MLevel.FINEST))
this.logo.log(MLevel.FINEST, "Config available to MLog library: " + MLogConfig.dump()); 
}
};
thread.start();
}

public static MLog findByClassnames(String[] paramArrayOfString, boolean paramBoolean) {
ArrayList<String> arrayList = null; byte b; int i;
for (b = 0, i = paramArrayOfString.length; b < i; b++) {
try {
return (MLog)Class.forName(MLogClasses.resolveIfAlias(paramArrayOfString[b])).newInstance();
} catch (Exception exception) {

if (arrayList == null)
arrayList = new ArrayList(); 
arrayList.add(paramArrayOfString[b]);
if (paramBoolean) {

System.err.println("com.mchange.v2.log.MLog '" + paramArrayOfString[b] + "' could not be loaded!");
exception.printStackTrace();
} 
} 
} 
System.err.println("Tried without success to load the following MLog classes:");
for (b = 0, i = arrayList.size(); b < i; b++)
System.err.println("\t" + arrayList.get(b)); 
return null;
}

public static MLog instance() {
return mlog();
}
public static MLogger getLogger(String paramString) {
MLogger mLogger;
NameTransformer nameTransformer = null;
MLog mLog = null;

synchronized (MLog.class) {

nameTransformer = transformer();
mLog = instance();
} 

if (nameTransformer == null) {
mLogger = instance().getMLogger(paramString);
} else {

String str = nameTransformer.transformName(paramString);
if (str != null) {
mLogger = mLog.getMLogger(str);
} else {
mLogger = mLog.getMLogger(paramString);
} 
}  return mLogger;
}

public static MLogger getLogger(Class paramClass) {
MLogger mLogger;
NameTransformer nameTransformer = null;
MLog mLog = null;

synchronized (MLog.class) {

nameTransformer = transformer();
mLog = instance();
} 

if (nameTransformer == null) {
mLogger = mLog.getMLogger(paramClass);
} else {

String str = nameTransformer.transformName(paramClass);
if (str != null) {
mLogger = mLog.getMLogger(str);
} else {
mLogger = mLog.getMLogger(paramClass);
} 
}  return mLogger;
}

public static MLogger getLogger() {
MLogger mLogger;
NameTransformer nameTransformer = null;
MLog mLog = null;

synchronized (MLog.class) {

nameTransformer = transformer();
mLog = instance();
} 

if (nameTransformer == null) {
mLogger = mLog.getMLogger();
} else {

String str = nameTransformer.transformName();
if (str != null) {
mLogger = mLog.getMLogger(str);
} else {
mLogger = mLog.getMLogger();
} 
}  return mLogger;
}

public static void log(MLevel paramMLevel, String paramString) {
instance(); getLogger().log(paramMLevel, paramString);
}
public static void log(MLevel paramMLevel, String paramString, Object paramObject) {
instance(); getLogger().log(paramMLevel, paramString, paramObject);
}
public static void log(MLevel paramMLevel, String paramString, Object[] paramArrayOfObject) {
instance(); getLogger().log(paramMLevel, paramString, paramArrayOfObject);
}
public static void log(MLevel paramMLevel, String paramString, Throwable paramThrowable) {
instance(); getLogger().log(paramMLevel, paramString, paramThrowable);
}
public static void logp(MLevel paramMLevel, String paramString1, String paramString2, String paramString3) {
instance(); getLogger().logp(paramMLevel, paramString1, paramString2, paramString3);
}
public static void logp(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, Object paramObject) {
instance(); getLogger().logp(paramMLevel, paramString1, paramString2, paramString3, paramObject);
}
public static void logp(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, Object[] paramArrayOfObject) {
instance(); getLogger().logp(paramMLevel, paramString1, paramString2, paramString3, paramArrayOfObject);
}
public static void logp(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, Throwable paramThrowable) {
instance(); getLogger().logp(paramMLevel, paramString1, paramString2, paramString3, paramThrowable);
}
public static void logrb(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, String paramString4) {
instance(); getLogger().logp(paramMLevel, paramString1, paramString2, paramString3, paramString4);
}
public static void logrb(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, String paramString4, Object paramObject) {
instance(); getLogger().logrb(paramMLevel, paramString1, paramString2, paramString3, paramString4, paramObject);
}
public static void logrb(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, String paramString4, Object[] paramArrayOfObject) {
instance(); getLogger().logrb(paramMLevel, paramString1, paramString2, paramString3, paramString4, paramArrayOfObject);
}
public static void logrb(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, String paramString4, Throwable paramThrowable) {
instance(); getLogger().logrb(paramMLevel, paramString1, paramString2, paramString3, paramString4, paramThrowable);
}
public static void entering(String paramString1, String paramString2) {
instance(); getLogger().entering(paramString1, paramString2);
}
public static void entering(String paramString1, String paramString2, Object paramObject) {
instance(); getLogger().entering(paramString1, paramString2, paramObject);
}
public static void entering(String paramString1, String paramString2, Object[] paramArrayOfObject) {
instance(); getLogger().entering(paramString1, paramString2, paramArrayOfObject);
}
public static void exiting(String paramString1, String paramString2) {
instance(); getLogger().exiting(paramString1, paramString2);
}
public static void exiting(String paramString1, String paramString2, Object paramObject) {
instance(); getLogger().exiting(paramString1, paramString2, paramObject);
}
public static void throwing(String paramString1, String paramString2, Throwable paramThrowable) {
instance(); getLogger().throwing(paramString1, paramString2, paramThrowable);
}
public static void severe(String paramString) {
instance(); getLogger().severe(paramString);
}
public static void warning(String paramString) {
instance(); getLogger().warning(paramString);
}
public static void info(String paramString) {
instance(); getLogger().info(paramString);
}
public static void config(String paramString) {
instance(); getLogger().config(paramString);
}
public static void fine(String paramString) {
instance(); getLogger().fine(paramString);
}
public static void finer(String paramString) {
instance(); getLogger().finer(paramString);
}
public static void finest(String paramString) {
instance(); getLogger().finest(paramString);
}

public MLogger getMLogger(Class paramClass) {
return getMLogger(paramClass.getName());
}

public abstract MLogger getMLogger(String paramString);

public abstract MLogger getMLogger();
}

