package com.mchange.v2.log;

import java.util.ResourceBundle;

public interface MLogger {
  String getName();

  void log(MLevel paramMLevel, String paramString);

  void log(MLevel paramMLevel, String paramString, Object paramObject);

  void log(MLevel paramMLevel, String paramString, Object[] paramArrayOfObject);

  void log(MLevel paramMLevel, String paramString, Throwable paramThrowable);

  void logp(MLevel paramMLevel, String paramString1, String paramString2, String paramString3);

  void logp(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, Object paramObject);

  void logp(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, Object[] paramArrayOfObject);

  void logp(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, Throwable paramThrowable);

  void logrb(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, String paramString4);

  void logrb(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, String paramString4, Object paramObject);

  void logrb(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, String paramString4, Object[] paramArrayOfObject);

  void logrb(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, String paramString4, Throwable paramThrowable);

  void entering(String paramString1, String paramString2);

  void entering(String paramString1, String paramString2, Object paramObject);

  void entering(String paramString1, String paramString2, Object[] paramArrayOfObject);

  void exiting(String paramString1, String paramString2);

  void exiting(String paramString1, String paramString2, Object paramObject);

  void throwing(String paramString1, String paramString2, Throwable paramThrowable);

  void severe(String paramString);

  void warning(String paramString);

  void info(String paramString);

  void config(String paramString);

  void fine(String paramString);

  void finer(String paramString);

  void finest(String paramString);

  boolean isLoggable(MLevel paramMLevel);

  ResourceBundle getResourceBundle();

  String getResourceBundleName();

  void setFilter(Object paramObject) throws SecurityException;

  Object getFilter();

  void setLevel(MLevel paramMLevel) throws SecurityException;

  MLevel getLevel();

  void addHandler(Object paramObject) throws SecurityException;

  void removeHandler(Object paramObject) throws SecurityException;

  Object[] getHandlers();

  void setUseParentHandlers(boolean paramBoolean);

  boolean getUseParentHandlers();
}

