/*     */ package com.mchange.v2.log.log4j;
/*     */ 
/*     */ import com.mchange.v2.log.FallbackMLog;
/*     */ import com.mchange.v2.log.LogUtils;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import com.mchange.v2.log.NullMLogger;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedList;
/*     */ import java.util.ResourceBundle;
/*     */ import org.apache.log4j.Appender;
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.log4j.Priority;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Log4jMLog
/*     */   extends MLog
/*     */ {
/*     */   static final String CHECK_CLASS = "org.apache.log4j.Logger";
/*     */   
/*     */   public Log4jMLog() throws ClassNotFoundException {
/*  52 */     Class.forName("org.apache.log4j.Logger");
/*     */   }
/*     */   
/*     */   public MLogger getMLogger(String paramString) {
/*  56 */     Logger logger = Logger.getLogger(paramString);
/*  57 */     if (logger == null) {
/*     */       
/*  59 */       fallbackWarn(" with name '" + paramString + "'");
/*  60 */       return NullMLogger.instance();
/*     */     } 
/*     */     
/*  63 */     return new Log4jMLogger(logger);
/*     */   }
/*     */ 
/*     */   
/*     */   public MLogger getMLogger(Class paramClass) {
/*  68 */     Logger logger = Logger.getLogger(paramClass);
/*  69 */     if (logger == null) {
/*     */       
/*  71 */       fallbackWarn(" for class '" + paramClass.getName() + "'");
/*  72 */       return NullMLogger.instance();
/*     */     } 
/*     */     
/*  75 */     return new Log4jMLogger(logger);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MLogger getMLogger() {
/*  81 */     Logger logger = Logger.getRootLogger();
/*  82 */     if (logger == null) {
/*     */       
/*  84 */       fallbackWarn(" (root logger)");
/*  85 */       return NullMLogger.instance();
/*     */     } 
/*     */     
/*  88 */     return new Log4jMLogger(logger);
/*     */   }
/*     */ 
/*     */   
/*     */   private void fallbackWarn(String paramString) {
/*  93 */     FallbackMLog.getLogger().warning("Could not create or find log4j Logger" + paramString + ". " + "Using NullMLogger. All messages sent to this" + "logger will be silently ignored. You might want to fix this.");
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class Log4jMLogger
/*     */     implements MLogger
/*     */   {
/* 100 */     static final String FQCN = Log4jMLogger.class.getName();
/*     */ 
/*     */     
/* 103 */     MLevel myLevel = null;
/*     */     
/*     */     final Logger logger;
/*     */     
/*     */     Log4jMLogger(Logger param1Logger) {
/* 108 */       this.logger = param1Logger;
/*     */     }
/*     */     
/*     */     private static MLevel guessMLevel(Level param1Level) {
/* 112 */       if (param1Level == null)
/* 113 */         return null; 
/* 114 */       if (param1Level == Level.ALL)
/* 115 */         return MLevel.ALL; 
/* 116 */       if (param1Level == Level.DEBUG)
/* 117 */         return MLevel.FINEST; 
/* 118 */       if (param1Level == Level.ERROR)
/* 119 */         return MLevel.SEVERE; 
/* 120 */       if (param1Level == Level.FATAL)
/* 121 */         return MLevel.SEVERE; 
/* 122 */       if (param1Level == Level.INFO)
/* 123 */         return MLevel.INFO; 
/* 124 */       if (param1Level == Level.OFF)
/* 125 */         return MLevel.OFF; 
/* 126 */       if (param1Level == Level.WARN) {
/* 127 */         return MLevel.WARNING;
/*     */       }
/* 129 */       throw new IllegalArgumentException("Unknown level: " + param1Level);
/*     */     }
/*     */ 
/*     */     
/*     */     private static Level level(MLevel param1MLevel) {
/* 134 */       if (param1MLevel == null)
/* 135 */         return null; 
/* 136 */       if (param1MLevel == MLevel.ALL)
/* 137 */         return Level.ALL; 
/* 138 */       if (param1MLevel == MLevel.CONFIG)
/* 139 */         return Level.DEBUG; 
/* 140 */       if (param1MLevel == MLevel.FINE)
/* 141 */         return Level.DEBUG; 
/* 142 */       if (param1MLevel == MLevel.FINER)
/* 143 */         return Level.DEBUG; 
/* 144 */       if (param1MLevel == MLevel.FINEST)
/* 145 */         return Level.DEBUG; 
/* 146 */       if (param1MLevel == MLevel.INFO)
/* 147 */         return Level.INFO; 
/* 148 */       if (param1MLevel == MLevel.OFF)
/* 149 */         return Level.OFF; 
/* 150 */       if (param1MLevel == MLevel.SEVERE)
/* 151 */         return Level.ERROR; 
/* 152 */       if (param1MLevel == MLevel.WARNING) {
/* 153 */         return Level.WARN;
/*     */       }
/* 155 */       throw new IllegalArgumentException("Unknown MLevel: " + param1MLevel);
/*     */     }
/*     */     
/*     */     public ResourceBundle getResourceBundle() {
/* 159 */       return null;
/*     */     }
/*     */     public String getResourceBundleName() {
/* 162 */       return null;
/*     */     }
/*     */     public void setFilter(Object param1Object) throws SecurityException {
/* 165 */       warning("setFilter() not supported by MLogger " + getClass().getName());
/*     */     }
/*     */     public Object getFilter() {
/* 168 */       return null;
/*     */     }
/*     */     private void log(Level param1Level, Object param1Object, Throwable param1Throwable) {
/* 171 */       this.logger.log(FQCN, (Priority)param1Level, param1Object, param1Throwable);
/*     */     }
/*     */     public void log(MLevel param1MLevel, String param1String) {
/* 174 */       log(level(param1MLevel), param1String, (Throwable)null);
/*     */     }
/*     */     public void log(MLevel param1MLevel, String param1String, Object param1Object) {
/* 177 */       log(level(param1MLevel), (param1String != null) ? MessageFormat.format(param1String, new Object[] { param1Object }) : null, (Throwable)null);
/*     */     }
/*     */     public void log(MLevel param1MLevel, String param1String, Object[] param1ArrayOfObject) {
/* 180 */       log(level(param1MLevel), (param1String != null) ? MessageFormat.format(param1String, param1ArrayOfObject) : null, (Throwable)null);
/*     */     }
/*     */     public void log(MLevel param1MLevel, String param1String, Throwable param1Throwable) {
/* 183 */       log(level(param1MLevel), param1String, param1Throwable);
/*     */     }
/*     */     public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3) {
/* 186 */       log(level(param1MLevel), LogUtils.createMessage(param1String1, param1String2, param1String3), (Throwable)null);
/*     */     }
/*     */     public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Object param1Object) {
/* 189 */       log(level(param1MLevel), LogUtils.createMessage(param1String1, param1String2, (param1String3 != null) ? MessageFormat.format(param1String3, new Object[] { param1Object }) : null), (Throwable)null);
/*     */     }
/*     */     public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Object[] param1ArrayOfObject) {
/* 192 */       log(level(param1MLevel), LogUtils.createMessage(param1String1, param1String2, (param1String3 != null) ? MessageFormat.format(param1String3, param1ArrayOfObject) : null), (Throwable)null);
/*     */     }
/*     */     public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Throwable param1Throwable) {
/* 195 */       log(level(param1MLevel), LogUtils.createMessage(param1String1, param1String2, param1String3), param1Throwable);
/*     */     }
/*     */     public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4) {
/* 198 */       log(level(param1MLevel), LogUtils.createMessage(param1String1, param1String2, LogUtils.formatMessage(param1String3, param1String4, null)), (Throwable)null);
/*     */     }
/*     */     public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Object param1Object) {
/* 201 */       log(level(param1MLevel), LogUtils.createMessage(param1String1, param1String2, LogUtils.formatMessage(param1String3, param1String4, new Object[] { param1Object })), (Throwable)null);
/*     */     }
/*     */     public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Object[] param1ArrayOfObject) {
/* 204 */       log(level(param1MLevel), LogUtils.createMessage(param1String1, param1String2, LogUtils.formatMessage(param1String3, param1String4, param1ArrayOfObject)), (Throwable)null);
/*     */     }
/*     */     public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Throwable param1Throwable) {
/* 207 */       log(level(param1MLevel), LogUtils.createMessage(param1String1, param1String2, LogUtils.formatMessage(param1String3, param1String4, null)), param1Throwable);
/*     */     }
/*     */     public void entering(String param1String1, String param1String2) {
/* 210 */       log(Level.DEBUG, LogUtils.createMessage(param1String1, param1String2, "entering method."), (Throwable)null);
/*     */     }
/*     */     public void entering(String param1String1, String param1String2, Object param1Object) {
/* 213 */       log(Level.DEBUG, LogUtils.createMessage(param1String1, param1String2, "entering method... param: " + param1Object.toString()), (Throwable)null);
/*     */     }
/*     */     public void entering(String param1String1, String param1String2, Object[] param1ArrayOfObject) {
/* 216 */       log(Level.DEBUG, LogUtils.createMessage(param1String1, param1String2, "entering method... " + LogUtils.createParamsList(param1ArrayOfObject)), (Throwable)null);
/*     */     }
/*     */     public void exiting(String param1String1, String param1String2) {
/* 219 */       log(Level.DEBUG, LogUtils.createMessage(param1String1, param1String2, "exiting method."), (Throwable)null);
/*     */     }
/*     */     public void exiting(String param1String1, String param1String2, Object param1Object) {
/* 222 */       log(Level.DEBUG, LogUtils.createMessage(param1String1, param1String2, "exiting method... result: " + param1Object.toString()), (Throwable)null);
/*     */     }
/*     */     public void throwing(String param1String1, String param1String2, Throwable param1Throwable) {
/* 225 */       log(Level.DEBUG, LogUtils.createMessage(param1String1, param1String2, "throwing exception... "), param1Throwable);
/*     */     }
/*     */     public void severe(String param1String) {
/* 228 */       log(Level.ERROR, param1String, (Throwable)null);
/*     */     }
/*     */     public void warning(String param1String) {
/* 231 */       log(Level.WARN, param1String, (Throwable)null);
/*     */     }
/*     */     public void info(String param1String) {
/* 234 */       log(Level.INFO, param1String, (Throwable)null);
/*     */     }
/*     */     public void config(String param1String) {
/* 237 */       log(Level.DEBUG, param1String, (Throwable)null);
/*     */     }
/*     */     public void fine(String param1String) {
/* 240 */       log(Level.DEBUG, param1String, (Throwable)null);
/*     */     }
/*     */     public void finer(String param1String) {
/* 243 */       log(Level.DEBUG, param1String, (Throwable)null);
/*     */     }
/*     */     public void finest(String param1String) {
/* 246 */       log(Level.DEBUG, param1String, (Throwable)null);
/*     */     }
/*     */     
/*     */     public synchronized void setLevel(MLevel param1MLevel) throws SecurityException {
/* 250 */       this.logger.setLevel(level(param1MLevel));
/* 251 */       this.myLevel = param1MLevel;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized MLevel getLevel() {
/* 257 */       if (this.myLevel == null)
/* 258 */         this.myLevel = guessMLevel(this.logger.getLevel()); 
/* 259 */       return this.myLevel;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isLoggable(MLevel param1MLevel) {
/* 267 */       return this.logger.isEnabledFor((Priority)level(param1MLevel));
/*     */     }
/*     */     
/*     */     public String getName() {
/* 271 */       return this.logger.getName();
/*     */     }
/*     */     
/*     */     public void addHandler(Object param1Object) throws SecurityException {
/* 275 */       if (!(param1Object instanceof Appender))
/* 276 */         throw new IllegalArgumentException("The 'handler' " + param1Object + " is not compatible with MLogger " + this); 
/* 277 */       this.logger.addAppender((Appender)param1Object);
/*     */     }
/*     */ 
/*     */     
/*     */     public void removeHandler(Object param1Object) throws SecurityException {
/* 282 */       if (!(param1Object instanceof Appender))
/* 283 */         throw new IllegalArgumentException("The 'handler' " + param1Object + " is not compatible with MLogger " + this); 
/* 284 */       this.logger.removeAppender((Appender)param1Object);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] getHandlers() {
/* 289 */       LinkedList linkedList = new LinkedList();
/* 290 */       for (Enumeration enumeration = this.logger.getAllAppenders(); enumeration.hasMoreElements();)
/* 291 */         linkedList.add(enumeration.nextElement()); 
/* 292 */       return linkedList.toArray();
/*     */     }
/*     */     
/*     */     public void setUseParentHandlers(boolean param1Boolean) {
/* 296 */       this.logger.setAdditivity(param1Boolean);
/*     */     }
/*     */     public boolean getUseParentHandlers() {
/* 299 */       return this.logger.getAdditivity();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/log/log4j/Log4jMLog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */