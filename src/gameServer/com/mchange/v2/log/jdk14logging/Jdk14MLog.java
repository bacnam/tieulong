/*     */ package com.mchange.v2.log.jdk14logging;
/*     */ 
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogConfig;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import com.mchange.v2.util.DoubleWeakHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.logging.Filter;
/*     */ import java.util.logging.Handler;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.LogManager;
/*     */ import java.util.logging.Logger;
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
/*     */ public final class Jdk14MLog
/*     */   extends MLog
/*     */ {
/*     */   static final String SUPPRESS_STACK_WALK_KEY = "com.mchange.v2.log.jdk14logging.suppressStackWalk";
/*  47 */   private static String[] UNKNOWN_ARRAY = new String[] { "UNKNOWN_CLASS", "UNKNOWN_METHOD" };
/*     */   
/*     */   private static final String CHECK_CLASS = "java.util.logging.Logger";
/*     */   
/*  51 */   private final Map namedLoggerMap = (Map)new DoubleWeakHashMap();
/*     */   
/*     */   private static final boolean suppress_stack_walk;
/*     */ 
/*     */   
/*     */   static {
/*  57 */     String str = MLogConfig.getProperty("com.mchange.v2.log.jdk14logging.suppressStackWalk");
/*  58 */     if (str == null || (str = str.trim()).length() == 0) {
/*  59 */       suppress_stack_walk = false;
/*     */     
/*     */     }
/*  62 */     else if (str.equalsIgnoreCase("true")) {
/*  63 */       suppress_stack_walk = true;
/*  64 */     } else if (str.equalsIgnoreCase("false")) {
/*  65 */       suppress_stack_walk = false;
/*     */     } else {
/*     */       
/*  68 */       System.err.println("Bad value for com.mchange.v2.log.jdk14logging.suppressStackWalk: '" + str + "'; defaulting to 'false'.");
/*  69 */       suppress_stack_walk = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  74 */   MLogger global = null;
/*     */   
/*     */   public Jdk14MLog() throws ClassNotFoundException {
/*  77 */     Class.forName("java.util.logging.Logger");
/*     */   }
/*     */   
/*     */   public synchronized MLogger getMLogger(String paramString) {
/*  81 */     paramString = paramString.intern();
/*     */     
/*  83 */     MLogger mLogger = (MLogger)this.namedLoggerMap.get(paramString);
/*  84 */     if (mLogger == null) {
/*     */       
/*  86 */       Logger logger = Logger.getLogger(paramString);
/*  87 */       mLogger = new Jdk14MLogger(logger);
/*  88 */       this.namedLoggerMap.put(paramString, mLogger);
/*     */     } 
/*  90 */     return mLogger;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized MLogger getMLogger() {
/*  95 */     if (this.global == null)
/*  96 */       this.global = new Jdk14MLogger(LogManager.getLogManager().getLogger("global")); 
/*  97 */     return this.global;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String[] findCallingClassAndMethod() {
/* 107 */     StackTraceElement[] arrayOfStackTraceElement = (new Throwable()).getStackTrace(); byte b; int i;
/* 108 */     for (b = 0, i = arrayOfStackTraceElement.length; b < i; b++) {
/*     */       
/* 110 */       StackTraceElement stackTraceElement = arrayOfStackTraceElement[b];
/* 111 */       String str = stackTraceElement.getClassName();
/* 112 */       if (str != null && !str.startsWith("com.mchange.v2.log.jdk14logging") && !str.startsWith("com.mchange.sc.v1.log"))
/* 113 */         return new String[] { stackTraceElement.getClassName(), stackTraceElement.getMethodName() }; 
/*     */     } 
/* 115 */     return UNKNOWN_ARRAY;
/*     */   }
/*     */   
/*     */   private static final class Jdk14MLogger
/*     */     implements MLogger
/*     */   {
/*     */     final Logger logger;
/*     */     final String name;
/*     */     final ClassAndMethodFinder cmFinder;
/*     */     
/*     */     Jdk14MLogger(Logger param1Logger) {
/* 126 */       this.logger = param1Logger;
/*     */ 
/*     */       
/* 129 */       this.name = param1Logger.getName();
/*     */       
/* 131 */       if (Jdk14MLog.suppress_stack_walk == true) {
/*     */         
/* 133 */         this.cmFinder = new ClassAndMethodFinder()
/*     */           {
/* 135 */             String[] fakedClassAndMethod = new String[] { this.this$0.name, "" };
/*     */             public String[] find() {
/* 137 */               return this.fakedClassAndMethod;
/*     */             }
/*     */           };
/*     */       } else {
/*     */         
/* 142 */         this.cmFinder = new ClassAndMethodFinder() {
/*     */             public String[] find() {
/* 144 */               return Jdk14MLog.findCallingClassAndMethod();
/*     */             }
/*     */           };
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static Level level(MLevel param1MLevel) {
/* 156 */       return (Level)param1MLevel.asJdk14Level();
/*     */     }
/*     */     public ResourceBundle getResourceBundle() {
/* 159 */       return this.logger.getResourceBundle();
/*     */     }
/*     */     public String getResourceBundleName() {
/* 162 */       return this.logger.getResourceBundleName();
/*     */     }
/*     */     
/*     */     public void setFilter(Object param1Object) throws SecurityException {
/* 166 */       if (!(param1Object instanceof Filter)) {
/* 167 */         throw new IllegalArgumentException("MLogger.setFilter( ... ) requires a java.util.logging.Filter. This is not enforced by the compiler only to permit building under jdk 1.3");
/*     */       }
/* 169 */       this.logger.setFilter((Filter)param1Object);
/*     */     }
/*     */     
/*     */     public Object getFilter() {
/* 173 */       return this.logger.getFilter();
/*     */     }
/*     */     
/*     */     public void log(MLevel param1MLevel, String param1String) {
/* 177 */       if (!this.logger.isLoggable(level(param1MLevel)))
/*     */         return; 
/* 179 */       String[] arrayOfString = this.cmFinder.find();
/* 180 */       this.logger.logp(level(param1MLevel), arrayOfString[0], arrayOfString[1], param1String);
/*     */     }
/*     */ 
/*     */     
/*     */     public void log(MLevel param1MLevel, String param1String, Object param1Object) {
/* 185 */       if (!this.logger.isLoggable(level(param1MLevel)))
/*     */         return; 
/* 187 */       String[] arrayOfString = this.cmFinder.find();
/* 188 */       this.logger.logp(level(param1MLevel), arrayOfString[0], arrayOfString[1], param1String, param1Object);
/*     */     }
/*     */ 
/*     */     
/*     */     public void log(MLevel param1MLevel, String param1String, Object[] param1ArrayOfObject) {
/* 193 */       if (!this.logger.isLoggable(level(param1MLevel)))
/*     */         return; 
/* 195 */       String[] arrayOfString = this.cmFinder.find();
/* 196 */       this.logger.logp(level(param1MLevel), arrayOfString[0], arrayOfString[1], param1String, param1ArrayOfObject);
/*     */     }
/*     */ 
/*     */     
/*     */     public void log(MLevel param1MLevel, String param1String, Throwable param1Throwable) {
/* 201 */       if (!this.logger.isLoggable(level(param1MLevel)))
/*     */         return; 
/* 203 */       String[] arrayOfString = this.cmFinder.find();
/* 204 */       this.logger.logp(level(param1MLevel), arrayOfString[0], arrayOfString[1], param1String, param1Throwable);
/*     */     }
/*     */ 
/*     */     
/*     */     public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3) {
/* 209 */       if (!this.logger.isLoggable(level(param1MLevel)))
/*     */         return; 
/* 211 */       if (param1String1 == null && param1String2 == null) {
/*     */         
/* 213 */         String[] arrayOfString = this.cmFinder.find();
/* 214 */         param1String1 = arrayOfString[0];
/* 215 */         param1String2 = arrayOfString[1];
/*     */       } 
/* 217 */       this.logger.logp(level(param1MLevel), param1String1, param1String2, param1String3);
/*     */     }
/*     */ 
/*     */     
/*     */     public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Object param1Object) {
/* 222 */       if (!this.logger.isLoggable(level(param1MLevel)))
/*     */         return; 
/* 224 */       if (param1String1 == null && param1String2 == null) {
/*     */         
/* 226 */         String[] arrayOfString = this.cmFinder.find();
/* 227 */         param1String1 = arrayOfString[0];
/* 228 */         param1String2 = arrayOfString[1];
/*     */       } 
/* 230 */       this.logger.logp(level(param1MLevel), param1String1, param1String2, param1String3, param1Object);
/*     */     }
/*     */ 
/*     */     
/*     */     public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Object[] param1ArrayOfObject) {
/* 235 */       if (!this.logger.isLoggable(level(param1MLevel)))
/*     */         return; 
/* 237 */       if (param1String1 == null && param1String2 == null) {
/*     */         
/* 239 */         String[] arrayOfString = this.cmFinder.find();
/* 240 */         param1String1 = arrayOfString[0];
/* 241 */         param1String2 = arrayOfString[1];
/*     */       } 
/* 243 */       this.logger.logp(level(param1MLevel), param1String1, param1String2, param1String3, param1ArrayOfObject);
/*     */     }
/*     */ 
/*     */     
/*     */     public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Throwable param1Throwable) {
/* 248 */       if (!this.logger.isLoggable(level(param1MLevel)))
/*     */         return; 
/* 250 */       if (param1String1 == null && param1String2 == null) {
/*     */         
/* 252 */         String[] arrayOfString = this.cmFinder.find();
/* 253 */         param1String1 = arrayOfString[0];
/* 254 */         param1String2 = arrayOfString[1];
/*     */       } 
/* 256 */       this.logger.logp(level(param1MLevel), param1String1, param1String2, param1String3, param1Throwable);
/*     */     }
/*     */ 
/*     */     
/*     */     public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4) {
/* 261 */       if (!this.logger.isLoggable(level(param1MLevel)))
/*     */         return; 
/* 263 */       if (param1String1 == null && param1String2 == null) {
/*     */         
/* 265 */         String[] arrayOfString = this.cmFinder.find();
/* 266 */         param1String1 = arrayOfString[0];
/* 267 */         param1String2 = arrayOfString[1];
/*     */       } 
/* 269 */       this.logger.logrb(level(param1MLevel), param1String1, param1String2, param1String3, param1String4);
/*     */     }
/*     */ 
/*     */     
/*     */     public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Object param1Object) {
/* 274 */       if (!this.logger.isLoggable(level(param1MLevel)))
/*     */         return; 
/* 276 */       if (param1String1 == null && param1String2 == null) {
/*     */         
/* 278 */         String[] arrayOfString = this.cmFinder.find();
/* 279 */         param1String1 = arrayOfString[0];
/* 280 */         param1String2 = arrayOfString[1];
/*     */       } 
/* 282 */       this.logger.logrb(level(param1MLevel), param1String1, param1String2, param1String3, param1String4, param1Object);
/*     */     }
/*     */ 
/*     */     
/*     */     public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Object[] param1ArrayOfObject) {
/* 287 */       if (!this.logger.isLoggable(level(param1MLevel)))
/*     */         return; 
/* 289 */       if (param1String1 == null && param1String2 == null) {
/*     */         
/* 291 */         String[] arrayOfString = this.cmFinder.find();
/* 292 */         param1String1 = arrayOfString[0];
/* 293 */         param1String2 = arrayOfString[1];
/*     */       } 
/* 295 */       this.logger.logrb(level(param1MLevel), param1String1, param1String2, param1String3, param1String4, param1ArrayOfObject);
/*     */     }
/*     */ 
/*     */     
/*     */     public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Throwable param1Throwable) {
/* 300 */       if (!this.logger.isLoggable(level(param1MLevel)))
/*     */         return; 
/* 302 */       if (param1String1 == null && param1String2 == null) {
/*     */         
/* 304 */         String[] arrayOfString = this.cmFinder.find();
/* 305 */         param1String1 = arrayOfString[0];
/* 306 */         param1String2 = arrayOfString[1];
/*     */       } 
/* 308 */       this.logger.logrb(level(param1MLevel), param1String1, param1String2, param1String3, param1String4, param1Throwable);
/*     */     }
/*     */ 
/*     */     
/*     */     public void entering(String param1String1, String param1String2) {
/* 313 */       if (!this.logger.isLoggable(Level.FINER))
/*     */         return; 
/* 315 */       this.logger.entering(param1String1, param1String2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void entering(String param1String1, String param1String2, Object param1Object) {
/* 320 */       if (!this.logger.isLoggable(Level.FINER))
/*     */         return; 
/* 322 */       this.logger.entering(param1String1, param1String2, param1Object);
/*     */     }
/*     */ 
/*     */     
/*     */     public void entering(String param1String1, String param1String2, Object[] param1ArrayOfObject) {
/* 327 */       if (!this.logger.isLoggable(Level.FINER))
/*     */         return; 
/* 329 */       this.logger.entering(param1String1, param1String2, param1ArrayOfObject);
/*     */     }
/*     */ 
/*     */     
/*     */     public void exiting(String param1String1, String param1String2) {
/* 334 */       if (!this.logger.isLoggable(Level.FINER))
/*     */         return; 
/* 336 */       this.logger.exiting(param1String1, param1String2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void exiting(String param1String1, String param1String2, Object param1Object) {
/* 341 */       if (!this.logger.isLoggable(Level.FINER))
/*     */         return; 
/* 343 */       this.logger.exiting(param1String1, param1String2, param1Object);
/*     */     }
/*     */ 
/*     */     
/*     */     public void throwing(String param1String1, String param1String2, Throwable param1Throwable) {
/* 348 */       if (!this.logger.isLoggable(Level.FINER))
/*     */         return; 
/* 350 */       this.logger.throwing(param1String1, param1String2, param1Throwable);
/*     */     }
/*     */ 
/*     */     
/*     */     public void severe(String param1String) {
/* 355 */       if (!this.logger.isLoggable(Level.SEVERE))
/*     */         return; 
/* 357 */       String[] arrayOfString = this.cmFinder.find();
/* 358 */       this.logger.logp(Level.SEVERE, arrayOfString[0], arrayOfString[1], param1String);
/*     */     }
/*     */ 
/*     */     
/*     */     public void warning(String param1String) {
/* 363 */       if (!this.logger.isLoggable(Level.WARNING))
/*     */         return; 
/* 365 */       String[] arrayOfString = this.cmFinder.find();
/* 366 */       this.logger.logp(Level.WARNING, arrayOfString[0], arrayOfString[1], param1String);
/*     */     }
/*     */ 
/*     */     
/*     */     public void info(String param1String) {
/* 371 */       if (!this.logger.isLoggable(Level.INFO))
/*     */         return; 
/* 373 */       String[] arrayOfString = this.cmFinder.find();
/* 374 */       this.logger.logp(Level.INFO, arrayOfString[0], arrayOfString[1], param1String);
/*     */     }
/*     */ 
/*     */     
/*     */     public void config(String param1String) {
/* 379 */       if (!this.logger.isLoggable(Level.CONFIG))
/*     */         return; 
/* 381 */       String[] arrayOfString = this.cmFinder.find();
/* 382 */       this.logger.logp(Level.CONFIG, arrayOfString[0], arrayOfString[1], param1String);
/*     */     }
/*     */ 
/*     */     
/*     */     public void fine(String param1String) {
/* 387 */       if (!this.logger.isLoggable(Level.FINE))
/*     */         return; 
/* 389 */       String[] arrayOfString = this.cmFinder.find();
/* 390 */       this.logger.logp(Level.FINE, arrayOfString[0], arrayOfString[1], param1String);
/*     */     }
/*     */ 
/*     */     
/*     */     public void finer(String param1String) {
/* 395 */       if (!this.logger.isLoggable(Level.FINER))
/*     */         return; 
/* 397 */       String[] arrayOfString = this.cmFinder.find();
/* 398 */       this.logger.logp(Level.FINER, arrayOfString[0], arrayOfString[1], param1String);
/*     */     }
/*     */ 
/*     */     
/*     */     public void finest(String param1String) {
/* 403 */       if (!this.logger.isLoggable(Level.FINEST))
/*     */         return; 
/* 405 */       String[] arrayOfString = this.cmFinder.find();
/* 406 */       this.logger.logp(Level.FINEST, arrayOfString[0], arrayOfString[1], param1String);
/*     */     }
/*     */     
/*     */     public void setLevel(MLevel param1MLevel) throws SecurityException {
/* 410 */       this.logger.setLevel(level(param1MLevel));
/*     */     }
/*     */     public MLevel getLevel() {
/* 413 */       return MLevel.fromIntValue(this.logger.getLevel().intValue());
/*     */     }
/*     */     public boolean isLoggable(MLevel param1MLevel) {
/* 416 */       return this.logger.isLoggable(level(param1MLevel));
/*     */     }
/*     */     public String getName() {
/* 419 */       return this.name;
/*     */     }
/*     */     
/*     */     public void addHandler(Object param1Object) throws SecurityException {
/* 423 */       if (!(param1Object instanceof Handler)) {
/* 424 */         throw new IllegalArgumentException("MLogger.addHandler( ... ) requires a java.util.logging.Handler. This is not enforced by the compiler only to permit building under jdk 1.3");
/*     */       }
/* 426 */       this.logger.addHandler((Handler)param1Object);
/*     */     }
/*     */ 
/*     */     
/*     */     public void removeHandler(Object param1Object) throws SecurityException {
/* 431 */       if (!(param1Object instanceof Handler)) {
/* 432 */         throw new IllegalArgumentException("MLogger.removeHandler( ... ) requires a java.util.logging.Handler. This is not enforced by the compiler only to permit building under jdk 1.3");
/*     */       }
/* 434 */       this.logger.removeHandler((Handler)param1Object);
/*     */     }
/*     */     
/*     */     public Object[] getHandlers() {
/* 438 */       return (Object[])this.logger.getHandlers();
/*     */     }
/*     */     public void setUseParentHandlers(boolean param1Boolean) {
/* 441 */       this.logger.setUseParentHandlers(param1Boolean);
/*     */     }
/*     */     public boolean getUseParentHandlers() {
/* 444 */       return this.logger.getUseParentHandlers();
/*     */     }
/*     */     
/*     */     static interface ClassAndMethodFinder {
/*     */       String[] find();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/log/jdk14logging/Jdk14MLog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */