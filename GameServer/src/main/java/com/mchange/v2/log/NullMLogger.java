package com.mchange.v2.log;

import java.util.ResourceBundle;

public class NullMLogger
implements MLogger
{
private static final MLogger INSTANCE = new NullMLogger();

public static MLogger instance() {
return INSTANCE;
}

private static final String NAME = "NullMLogger";

public void addHandler(Object paramObject) throws SecurityException {}

public void config(String paramString) {}

public void entering(String paramString1, String paramString2) {}

public void entering(String paramString1, String paramString2, Object paramObject) {}

public void entering(String paramString1, String paramString2, Object[] paramArrayOfObject) {}

public void exiting(String paramString1, String paramString2) {}

public void exiting(String paramString1, String paramString2, Object paramObject) {}

public void fine(String paramString) {}

public void finer(String paramString) {}

public void finest(String paramString) {}

public Object getFilter() {
return null;
}
public Object[] getHandlers() {
return null;
}
public MLevel getLevel() {
return MLevel.OFF;
}
public String getName() {
return "NullMLogger";
}
public ResourceBundle getResourceBundle() {
return null;
}
public String getResourceBundleName() {
return null;
}
public boolean getUseParentHandlers() {
return false;
}

public void info(String paramString) {}

public boolean isLoggable(MLevel paramMLevel) {
return false;
}

public void log(MLevel paramMLevel, String paramString) {}

public void log(MLevel paramMLevel, String paramString, Object paramObject) {}

public void log(MLevel paramMLevel, String paramString, Object[] paramArrayOfObject) {}

public void log(MLevel paramMLevel, String paramString, Throwable paramThrowable) {}

public void logp(MLevel paramMLevel, String paramString1, String paramString2, String paramString3) {}

public void logp(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, Object paramObject) {}

public void logp(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, Object[] paramArrayOfObject) {}

public void logp(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, Throwable paramThrowable) {}

public void logrb(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, String paramString4) {}

public void logrb(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, String paramString4, Object paramObject) {}

public void logrb(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, String paramString4, Object[] paramArrayOfObject) {}

public void logrb(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, String paramString4, Throwable paramThrowable) {}

public void removeHandler(Object paramObject) throws SecurityException {}

public void setFilter(Object paramObject) throws SecurityException {}

public void setLevel(MLevel paramMLevel) throws SecurityException {}

public void setUseParentHandlers(boolean paramBoolean) {}

public void severe(String paramString) {}

public void throwing(String paramString1, String paramString2, Throwable paramThrowable) {}

public void warning(String paramString) {}
}

