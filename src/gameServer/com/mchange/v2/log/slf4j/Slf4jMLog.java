/*     */ package com.mchange.v2.log.slf4j;
/*     */ 
/*     */ import com.mchange.v2.log.FallbackMLog;
/*     */ import com.mchange.v2.log.LogUtils;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import com.mchange.v2.log.NullMLogger;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ResourceBundle;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Slf4jMLog
/*     */   extends MLog
/*     */ {
/*  49 */   static final Object[] EMPTY_OBJ_ARRAY = new Object[0];
/*     */   
/*  51 */   private static final int ALL_INTVAL = MLevel.ALL.intValue();
/*  52 */   private static final int CONFIG_INTVAL = MLevel.CONFIG.intValue();
/*  53 */   private static final int FINE_INTVAL = MLevel.FINE.intValue();
/*  54 */   private static final int FINER_INTVAL = MLevel.FINER.intValue();
/*  55 */   private static final int FINEST_INTVAL = MLevel.FINEST.intValue();
/*  56 */   private static final int INFO_INTVAL = MLevel.INFO.intValue();
/*  57 */   private static final int OFF_INTVAL = MLevel.OFF.intValue();
/*  58 */   private static final int SEVERE_INTVAL = MLevel.SEVERE.intValue();
/*  59 */   private static final int WARNING_INTVAL = MLevel.WARNING.intValue();
/*     */   
/*     */   static final String CHECK_CLASS = "org.slf4j.LoggerFactory";
/*     */   
/*     */   static final String DFLT_LOGGER_NAME = "global";
/*     */   
/*     */   public Slf4jMLog() throws ClassNotFoundException {
/*  66 */     Class.forName("org.slf4j.LoggerFactory");
/*     */   }
/*     */   
/*     */   public MLogger getMLogger(String paramString) {
/*  70 */     Logger logger = LoggerFactory.getLogger(paramString);
/*  71 */     if (logger == null) {
/*     */       
/*  73 */       fallbackWarn(" with name '" + paramString + "'");
/*  74 */       return NullMLogger.instance();
/*     */     } 
/*     */     
/*  77 */     return new Slf4jMLogger(logger);
/*     */   }
/*     */ 
/*     */   
/*     */   public MLogger getMLogger() {
/*  82 */     Logger logger = LoggerFactory.getLogger("global");
/*  83 */     if (logger == null) {
/*     */       
/*  85 */       fallbackWarn(" (default, with name 'global')");
/*  86 */       return NullMLogger.instance();
/*     */     } 
/*     */     
/*  89 */     return new Slf4jMLogger(logger);
/*     */   }
/*     */ 
/*     */   
/*     */   private void fallbackWarn(String paramString) {
/*  94 */     FallbackMLog.getLogger().warning("Could not create or find slf4j Logger" + paramString + ". " + "Using NullMLogger. All messages sent to this" + "logger will be silently ignored. You might want to fix this.");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class Slf4jMLogger
/*     */     implements MLogger
/*     */   {
/* 102 */     static final String FQCN = Slf4jMLogger.class.getName();
/*     */     
/*     */     final Logger logger;
/*     */     
/*     */     final LevelLogger traceL;
/*     */     
/*     */     final LevelLogger debugL;
/*     */     
/*     */     final LevelLogger infoL;
/*     */     
/*     */     final LevelLogger warnL;
/*     */     final LevelLogger errorL;
/*     */     final LevelLogger offL;
/* 115 */     MLevel myLevel = null;
/*     */ 
/*     */     
/*     */     Slf4jMLogger(Logger param1Logger) {
/* 119 */       this.logger = param1Logger;
/* 120 */       this.traceL = new TraceLogger();
/* 121 */       this.debugL = new DebugLogger();
/* 122 */       this.infoL = new InfoLogger();
/* 123 */       this.warnL = new WarnLogger();
/* 124 */       this.errorL = new ErrorLogger();
/* 125 */       this.offL = new OffLogger();
/*     */     }
/*     */ 
/*     */     
/*     */     private MLevel guessMLevel() {
/* 130 */       if (this.logger.isErrorEnabled())
/* 131 */         return MLevel.SEVERE; 
/* 132 */       if (this.logger.isWarnEnabled())
/* 133 */         return MLevel.WARNING; 
/* 134 */       if (this.logger.isInfoEnabled())
/* 135 */         return MLevel.INFO; 
/* 136 */       if (this.logger.isDebugEnabled())
/* 137 */         return MLevel.FINER; 
/* 138 */       if (this.logger.isTraceEnabled()) {
/* 139 */         return MLevel.FINEST;
/*     */       }
/* 141 */       return MLevel.OFF;
/*     */     }
/*     */     
/*     */     private synchronized boolean myLevelIsLoggable(int param1Int) {
/* 145 */       return (this.myLevel == null || param1Int >= this.myLevel.intValue());
/*     */     } private static interface LevelLogger {
/*     */       void log(String param2String); void log(String param2String, Object param2Object); void log(String param2String, Object[] param2ArrayOfObject); void log(String param2String, Throwable param2Throwable); }
/*     */     private LevelLogger levelLogger(MLevel param1MLevel) {
/* 149 */       int i = param1MLevel.intValue();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 154 */       if (!myLevelIsLoggable(i)) return this.offL;
/*     */       
/* 156 */       if (i >= Slf4jMLog.SEVERE_INTVAL) return this.errorL; 
/* 157 */       if (i >= Slf4jMLog.WARNING_INTVAL) return this.warnL; 
/* 158 */       if (i >= Slf4jMLog.INFO_INTVAL) return this.infoL; 
/* 159 */       if (i >= Slf4jMLog.FINER_INTVAL) return this.debugL; 
/* 160 */       if (i >= Slf4jMLog.FINEST_INTVAL) return this.traceL; 
/* 161 */       return this.offL;
/*     */     }
/*     */     
/*     */     private class OffLogger
/*     */       implements LevelLogger {
/*     */       private OffLogger() {}
/*     */       
/*     */       public void log(String param2String) {}
/*     */       
/*     */       public void log(String param2String, Object param2Object) {}
/*     */       
/*     */       public void log(String param2String, Object[] param2ArrayOfObject) {}
/*     */       
/*     */       public void log(String param2String, Throwable param2Throwable) {}
/*     */     }
/*     */     
/*     */     private class TraceLogger implements LevelLogger
/*     */     {
/*     */       private TraceLogger() {}
/*     */       
/*     */       public void log(String param2String) {
/* 182 */         Slf4jMLog.Slf4jMLogger.this.logger.trace(param2String); }
/* 183 */       public void log(String param2String, Object param2Object) { Slf4jMLog.Slf4jMLogger.this.logger.trace(param2String, param2Object); }
/* 184 */       public void log(String param2String, Object[] param2ArrayOfObject) { Slf4jMLog.Slf4jMLogger.this.logger.trace(param2String, param2ArrayOfObject); } public void log(String param2String, Throwable param2Throwable) {
/* 185 */         Slf4jMLog.Slf4jMLogger.this.logger.trace(param2String, param2Throwable);
/*     */       } }
/*     */     
/*     */     private class DebugLogger implements LevelLogger { private DebugLogger() {}
/*     */       
/* 190 */       public void log(String param2String) { Slf4jMLog.Slf4jMLogger.this.logger.debug(param2String); }
/* 191 */       public void log(String param2String, Object param2Object) { Slf4jMLog.Slf4jMLogger.this.logger.debug(param2String, param2Object); }
/* 192 */       public void log(String param2String, Object[] param2ArrayOfObject) { Slf4jMLog.Slf4jMLogger.this.logger.debug(param2String, param2ArrayOfObject); } public void log(String param2String, Throwable param2Throwable) {
/* 193 */         Slf4jMLog.Slf4jMLogger.this.logger.debug(param2String, param2Throwable);
/*     */       } }
/*     */     
/*     */     private class InfoLogger implements LevelLogger { private InfoLogger() {}
/*     */       
/* 198 */       public void log(String param2String) { Slf4jMLog.Slf4jMLogger.this.logger.info(param2String); }
/* 199 */       public void log(String param2String, Object param2Object) { Slf4jMLog.Slf4jMLogger.this.logger.info(param2String, param2Object); }
/* 200 */       public void log(String param2String, Object[] param2ArrayOfObject) { Slf4jMLog.Slf4jMLogger.this.logger.info(param2String, param2ArrayOfObject); } public void log(String param2String, Throwable param2Throwable) {
/* 201 */         Slf4jMLog.Slf4jMLogger.this.logger.info(param2String, param2Throwable);
/*     */       } }
/*     */     
/*     */     private class WarnLogger implements LevelLogger { private WarnLogger() {}
/*     */       
/* 206 */       public void log(String param2String) { Slf4jMLog.Slf4jMLogger.this.logger.warn(param2String); }
/* 207 */       public void log(String param2String, Object param2Object) { Slf4jMLog.Slf4jMLogger.this.logger.warn(param2String, param2Object); }
/* 208 */       public void log(String param2String, Object[] param2ArrayOfObject) { Slf4jMLog.Slf4jMLogger.this.logger.warn(param2String, param2ArrayOfObject); } public void log(String param2String, Throwable param2Throwable) {
/* 209 */         Slf4jMLog.Slf4jMLogger.this.logger.warn(param2String, param2Throwable);
/*     */       } }
/*     */     
/*     */     private class ErrorLogger implements LevelLogger { private ErrorLogger() {}
/*     */       
/* 214 */       public void log(String param2String) { Slf4jMLog.Slf4jMLogger.this.logger.error(param2String); }
/* 215 */       public void log(String param2String, Object param2Object) { Slf4jMLog.Slf4jMLogger.this.logger.error(param2String, param2Object); }
/* 216 */       public void log(String param2String, Object[] param2ArrayOfObject) { Slf4jMLog.Slf4jMLogger.this.logger.error(param2String, param2ArrayOfObject); } public void log(String param2String, Throwable param2Throwable) {
/* 217 */         Slf4jMLog.Slf4jMLogger.this.logger.error(param2String, param2Throwable);
/*     */       } }
/*     */     
/*     */     public ResourceBundle getResourceBundle() {
/* 221 */       return null;
/*     */     }
/*     */     public String getResourceBundleName() {
/* 224 */       return null;
/*     */     }
/*     */     public void setFilter(Object param1Object) throws SecurityException {
/* 227 */       warning("setFilter() not supported by MLogger " + getClass().getName());
/*     */     }
/*     */     public Object getFilter() {
/* 230 */       return null;
/*     */     }
/*     */     public void log(MLevel param1MLevel, String param1String) {
/* 233 */       levelLogger(param1MLevel).log(param1String);
/*     */     }
/*     */     public void log(MLevel param1MLevel, String param1String, Object param1Object) {
/* 236 */       levelLogger(param1MLevel).log(param1String, param1Object);
/*     */     }
/*     */     public void log(MLevel param1MLevel, String param1String, Object[] param1ArrayOfObject) {
/* 239 */       levelLogger(param1MLevel).log(param1String, param1ArrayOfObject);
/*     */     }
/*     */     public void log(MLevel param1MLevel, String param1String, Throwable param1Throwable) {
/* 242 */       levelLogger(param1MLevel).log(param1String, param1Throwable);
/*     */     }
/*     */     public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3) {
/* 245 */       levelLogger(param1MLevel).log(LogUtils.createMessage(param1String1, param1String2, param1String3));
/*     */     }
/*     */     public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Object param1Object) {
/* 248 */       levelLogger(param1MLevel).log(LogUtils.createMessage(param1String1, param1String2, (param1String3 != null) ? MessageFormat.format(param1String3, new Object[] { param1Object }) : null));
/*     */     }
/*     */     public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Object[] param1ArrayOfObject) {
/* 251 */       levelLogger(param1MLevel).log(LogUtils.createMessage(param1String1, param1String2, (param1String3 != null) ? MessageFormat.format(param1String3, param1ArrayOfObject) : null));
/*     */     }
/*     */     public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Throwable param1Throwable) {
/* 254 */       levelLogger(param1MLevel).log(LogUtils.createMessage(param1String1, param1String2, param1String3), param1Throwable);
/*     */     }
/*     */     public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4) {
/* 257 */       levelLogger(param1MLevel).log(LogUtils.createMessage(param1String1, param1String2, LogUtils.formatMessage(param1String3, param1String4, null)));
/*     */     }
/*     */     public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Object param1Object) {
/* 260 */       levelLogger(param1MLevel).log(LogUtils.createMessage(param1String1, param1String2, LogUtils.formatMessage(param1String3, param1String4, new Object[] { param1Object })));
/*     */     }
/*     */     public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Object[] param1ArrayOfObject) {
/* 263 */       levelLogger(param1MLevel).log(LogUtils.createMessage(param1String1, param1String2, LogUtils.formatMessage(param1String3, param1String4, param1ArrayOfObject)));
/*     */     }
/*     */     public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Throwable param1Throwable) {
/* 266 */       levelLogger(param1MLevel).log(LogUtils.createMessage(param1String1, param1String2, LogUtils.formatMessage(param1String3, param1String4, null)), param1Throwable);
/*     */     }
/*     */     public void entering(String param1String1, String param1String2) {
/* 269 */       this.traceL.log(LogUtils.createMessage(param1String1, param1String2, "entering method."));
/*     */     }
/*     */     public void entering(String param1String1, String param1String2, Object param1Object) {
/* 272 */       this.traceL.log(LogUtils.createMessage(param1String1, param1String2, "entering method... param: " + param1Object.toString()));
/*     */     }
/*     */     public void entering(String param1String1, String param1String2, Object[] param1ArrayOfObject) {
/* 275 */       this.traceL.log(LogUtils.createMessage(param1String1, param1String2, "entering method... " + LogUtils.createParamsList(param1ArrayOfObject)));
/*     */     }
/*     */     public void exiting(String param1String1, String param1String2) {
/* 278 */       this.traceL.log(LogUtils.createMessage(param1String1, param1String2, "exiting method."));
/*     */     }
/*     */     public void exiting(String param1String1, String param1String2, Object param1Object) {
/* 281 */       this.traceL.log(LogUtils.createMessage(param1String1, param1String2, "exiting method... result: " + param1Object.toString()));
/*     */     }
/*     */     public void throwing(String param1String1, String param1String2, Throwable param1Throwable) {
/* 284 */       this.traceL.log(LogUtils.createMessage(param1String1, param1String2, "throwing exception... "), param1Throwable);
/*     */     }
/*     */     public void severe(String param1String) {
/* 287 */       this.errorL.log(param1String);
/*     */     }
/*     */     public void warning(String param1String) {
/* 290 */       this.warnL.log(param1String);
/*     */     }
/*     */     public void info(String param1String) {
/* 293 */       this.infoL.log(param1String);
/*     */     }
/*     */     public void config(String param1String) {
/* 296 */       this.debugL.log(param1String);
/*     */     }
/*     */     public void fine(String param1String) {
/* 299 */       this.debugL.log(param1String);
/*     */     }
/*     */     public void finer(String param1String) {
/* 302 */       this.debugL.log(param1String);
/*     */     }
/*     */     public void finest(String param1String) {
/* 305 */       this.traceL.log(param1String);
/*     */     }
/*     */     public synchronized void setLevel(MLevel param1MLevel) throws SecurityException {
/* 308 */       this.myLevel = param1MLevel;
/*     */     }
/*     */     
/*     */     public synchronized MLevel getLevel() {
/* 312 */       if (this.myLevel == null)
/* 313 */         this.myLevel = guessMLevel(); 
/* 314 */       return this.myLevel;
/*     */     }
/*     */     
/*     */     public boolean isLoggable(MLevel param1MLevel) {
/* 318 */       return (levelLogger(param1MLevel) != this.offL);
/*     */     }
/*     */     public String getName() {
/* 321 */       return this.logger.getName();
/*     */     }
/*     */     
/*     */     public void addHandler(Object param1Object) throws SecurityException {
/* 325 */       throw new UnsupportedOperationException("Handlers not supported; the 'handler' " + param1Object + " is not compatible with MLogger " + this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void removeHandler(Object param1Object) throws SecurityException {
/* 330 */       throw new UnsupportedOperationException("Handlers not supported; the 'handler' " + param1Object + " is not compatible with MLogger " + this);
/*     */     }
/*     */     
/*     */     public Object[] getHandlers() {
/* 334 */       return Slf4jMLog.EMPTY_OBJ_ARRAY;
/*     */     }
/*     */     public void setUseParentHandlers(boolean param1Boolean) {
/* 337 */       throw new UnsupportedOperationException("Handlers not supported.");
/*     */     }
/*     */     public boolean getUseParentHandlers() {
/* 340 */       throw new UnsupportedOperationException("Handlers not supported.");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/log/slf4j/Slf4jMLog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */