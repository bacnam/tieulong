/*     */ package com.mchange.v2.log;
/*     */ 
/*     */ import com.mchange.v1.util.StringTokenizerUtils;
/*     */ import com.mchange.v2.cfg.MultiPropertiesConfig;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MLog
/*     */ {
/*     */   private static NameTransformer _transformer;
/*     */   private static MLog _mlog;
/*     */   private static MLogger _logger;
/*     */   
/*     */   static {
/*  51 */     refreshConfig(null, null);
/*     */   }
/*  53 */   private static synchronized NameTransformer transformer() { return _transformer; }
/*  54 */   private static synchronized MLog mlog() { return _mlog; } private static synchronized MLogger logger() {
/*  55 */     return _logger;
/*     */   }
/*     */   
/*     */   public static synchronized void refreshConfig(MultiPropertiesConfig[] paramArrayOfMultiPropertiesConfig, String paramString) {
/*  59 */     MLogConfig.refresh(paramArrayOfMultiPropertiesConfig, paramString);
/*     */     
/*  61 */     String str1 = MLogConfig.getProperty("com.mchange.v2.log.MLog");
/*  62 */     String[] arrayOfString = null;
/*  63 */     if (str1 == null)
/*  64 */       str1 = MLogConfig.getProperty("com.mchange.v2.log.mlog"); 
/*  65 */     if (str1 != null) {
/*  66 */       arrayOfString = StringTokenizerUtils.tokenizeToArray(str1, ", \t\r\n");
/*     */     }
/*  68 */     boolean bool = false;
/*  69 */     MLog mLog = null;
/*  70 */     if (arrayOfString != null)
/*  71 */       mLog = findByClassnames(arrayOfString, true); 
/*  72 */     if (mLog == null)
/*  73 */       mLog = findByClassnames(MLogClasses.SEARCH_CLASSNAMES, false); 
/*  74 */     if (mLog == null) {
/*     */       
/*  76 */       bool = true;
/*  77 */       mLog = new FallbackMLog();
/*     */     } 
/*  79 */     _mlog = mLog;
/*  80 */     if (bool) {
/*  81 */       info("Using " + _mlog.getClass().getName() + " -- Named logger's not supported, everything goes to System.err.");
/*     */     }
/*  83 */     NameTransformer nameTransformer = null;
/*  84 */     String str2 = MLogConfig.getProperty("com.mchange.v2.log.NameTransformer");
/*  85 */     if (str2 == null) {
/*  86 */       str2 = MLogConfig.getProperty("com.mchange.v2.log.nametransformer");
/*     */     }
/*     */     try {
/*  89 */       if (str2 != null) {
/*  90 */         nameTransformer = (NameTransformer)Class.forName(str2).newInstance();
/*     */       }
/*  92 */     } catch (Exception exception) {
/*     */       
/*  94 */       System.err.println("Failed to instantiate com.mchange.v2.log.NameTransformer '" + str2 + "'!");
/*  95 */       exception.printStackTrace();
/*     */     } 
/*  97 */     _transformer = nameTransformer;
/*     */     
/*  99 */     _logger = getLogger(MLog.class);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     Thread thread = new Thread("MLog-Init-Reporter")
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 110 */         final MLogger logo = MLog._logger;
/* 111 */         String loggerDesc = MLog._mlog.getClass().getName();
/*     */ 
/*     */ 
/*     */         
/*     */         public void run() {
/* 116 */           if ("com.mchange.v2.log.jdk14logging.Jdk14MLog".equals(this.loggerDesc)) {
/* 117 */             this.loggerDesc = "java 1.4+ standard";
/* 118 */           } else if ("com.mchange.v2.log.log4j.Log4jMLog".equals(this.loggerDesc)) {
/* 119 */             this.loggerDesc = "log4j";
/* 120 */           } else if ("com.mchange.v2.log.slf4j.Slf4jMLog".equals(this.loggerDesc)) {
/* 121 */             this.loggerDesc = "slf4j";
/*     */           } 
/* 123 */           if (this.logo.isLoggable(MLevel.INFO)) {
/* 124 */             this.logo.log(MLevel.INFO, "MLog clients using " + this.loggerDesc + " logging.");
/*     */           }
/*     */ 
/*     */           
/* 128 */           MLogConfig.logDelayedItems(this.logo);
/*     */           
/* 130 */           if (this.logo.isLoggable(MLevel.FINEST))
/* 131 */             this.logo.log(MLevel.FINEST, "Config available to MLog library: " + MLogConfig.dump()); 
/*     */         }
/*     */       };
/* 134 */     thread.start();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static MLog findByClassnames(String[] paramArrayOfString, boolean paramBoolean) {
/* 140 */     ArrayList<String> arrayList = null; byte b; int i;
/* 141 */     for (b = 0, i = paramArrayOfString.length; b < i; b++) {
/*     */       try {
/* 143 */         return (MLog)Class.forName(MLogClasses.resolveIfAlias(paramArrayOfString[b])).newInstance();
/* 144 */       } catch (Exception exception) {
/*     */         
/* 146 */         if (arrayList == null)
/* 147 */           arrayList = new ArrayList(); 
/* 148 */         arrayList.add(paramArrayOfString[b]);
/* 149 */         if (paramBoolean) {
/*     */           
/* 151 */           System.err.println("com.mchange.v2.log.MLog '" + paramArrayOfString[b] + "' could not be loaded!");
/* 152 */           exception.printStackTrace();
/*     */         } 
/*     */       } 
/*     */     } 
/* 156 */     System.err.println("Tried without success to load the following MLog classes:");
/* 157 */     for (b = 0, i = arrayList.size(); b < i; b++)
/* 158 */       System.err.println("\t" + arrayList.get(b)); 
/* 159 */     return null;
/*     */   }
/*     */   
/*     */   public static MLog instance() {
/* 163 */     return mlog();
/*     */   }
/*     */   public static MLogger getLogger(String paramString) {
/*     */     MLogger mLogger;
/* 167 */     NameTransformer nameTransformer = null;
/* 168 */     MLog mLog = null;
/*     */     
/* 170 */     synchronized (MLog.class) {
/*     */       
/* 172 */       nameTransformer = transformer();
/* 173 */       mLog = instance();
/*     */     } 
/*     */ 
/*     */     
/* 177 */     if (nameTransformer == null) {
/* 178 */       mLogger = instance().getMLogger(paramString);
/*     */     } else {
/*     */       
/* 181 */       String str = nameTransformer.transformName(paramString);
/* 182 */       if (str != null) {
/* 183 */         mLogger = mLog.getMLogger(str);
/*     */       } else {
/* 185 */         mLogger = mLog.getMLogger(paramString);
/*     */       } 
/* 187 */     }  return mLogger;
/*     */   }
/*     */   
/*     */   public static MLogger getLogger(Class paramClass) {
/*     */     MLogger mLogger;
/* 192 */     NameTransformer nameTransformer = null;
/* 193 */     MLog mLog = null;
/*     */     
/* 195 */     synchronized (MLog.class) {
/*     */       
/* 197 */       nameTransformer = transformer();
/* 198 */       mLog = instance();
/*     */     } 
/*     */ 
/*     */     
/* 202 */     if (nameTransformer == null) {
/* 203 */       mLogger = mLog.getMLogger(paramClass);
/*     */     } else {
/*     */       
/* 206 */       String str = nameTransformer.transformName(paramClass);
/* 207 */       if (str != null) {
/* 208 */         mLogger = mLog.getMLogger(str);
/*     */       } else {
/* 210 */         mLogger = mLog.getMLogger(paramClass);
/*     */       } 
/* 212 */     }  return mLogger;
/*     */   }
/*     */   
/*     */   public static MLogger getLogger() {
/*     */     MLogger mLogger;
/* 217 */     NameTransformer nameTransformer = null;
/* 218 */     MLog mLog = null;
/*     */     
/* 220 */     synchronized (MLog.class) {
/*     */       
/* 222 */       nameTransformer = transformer();
/* 223 */       mLog = instance();
/*     */     } 
/*     */ 
/*     */     
/* 227 */     if (nameTransformer == null) {
/* 228 */       mLogger = mLog.getMLogger();
/*     */     } else {
/*     */       
/* 231 */       String str = nameTransformer.transformName();
/* 232 */       if (str != null) {
/* 233 */         mLogger = mLog.getMLogger(str);
/*     */       } else {
/* 235 */         mLogger = mLog.getMLogger();
/*     */       } 
/* 237 */     }  return mLogger;
/*     */   }
/*     */   
/*     */   public static void log(MLevel paramMLevel, String paramString) {
/* 241 */     instance(); getLogger().log(paramMLevel, paramString);
/*     */   }
/*     */   public static void log(MLevel paramMLevel, String paramString, Object paramObject) {
/* 244 */     instance(); getLogger().log(paramMLevel, paramString, paramObject);
/*     */   }
/*     */   public static void log(MLevel paramMLevel, String paramString, Object[] paramArrayOfObject) {
/* 247 */     instance(); getLogger().log(paramMLevel, paramString, paramArrayOfObject);
/*     */   }
/*     */   public static void log(MLevel paramMLevel, String paramString, Throwable paramThrowable) {
/* 250 */     instance(); getLogger().log(paramMLevel, paramString, paramThrowable);
/*     */   }
/*     */   public static void logp(MLevel paramMLevel, String paramString1, String paramString2, String paramString3) {
/* 253 */     instance(); getLogger().logp(paramMLevel, paramString1, paramString2, paramString3);
/*     */   }
/*     */   public static void logp(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, Object paramObject) {
/* 256 */     instance(); getLogger().logp(paramMLevel, paramString1, paramString2, paramString3, paramObject);
/*     */   }
/*     */   public static void logp(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, Object[] paramArrayOfObject) {
/* 259 */     instance(); getLogger().logp(paramMLevel, paramString1, paramString2, paramString3, paramArrayOfObject);
/*     */   }
/*     */   public static void logp(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, Throwable paramThrowable) {
/* 262 */     instance(); getLogger().logp(paramMLevel, paramString1, paramString2, paramString3, paramThrowable);
/*     */   }
/*     */   public static void logrb(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, String paramString4) {
/* 265 */     instance(); getLogger().logp(paramMLevel, paramString1, paramString2, paramString3, paramString4);
/*     */   }
/*     */   public static void logrb(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, String paramString4, Object paramObject) {
/* 268 */     instance(); getLogger().logrb(paramMLevel, paramString1, paramString2, paramString3, paramString4, paramObject);
/*     */   }
/*     */   public static void logrb(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, String paramString4, Object[] paramArrayOfObject) {
/* 271 */     instance(); getLogger().logrb(paramMLevel, paramString1, paramString2, paramString3, paramString4, paramArrayOfObject);
/*     */   }
/*     */   public static void logrb(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, String paramString4, Throwable paramThrowable) {
/* 274 */     instance(); getLogger().logrb(paramMLevel, paramString1, paramString2, paramString3, paramString4, paramThrowable);
/*     */   }
/*     */   public static void entering(String paramString1, String paramString2) {
/* 277 */     instance(); getLogger().entering(paramString1, paramString2);
/*     */   }
/*     */   public static void entering(String paramString1, String paramString2, Object paramObject) {
/* 280 */     instance(); getLogger().entering(paramString1, paramString2, paramObject);
/*     */   }
/*     */   public static void entering(String paramString1, String paramString2, Object[] paramArrayOfObject) {
/* 283 */     instance(); getLogger().entering(paramString1, paramString2, paramArrayOfObject);
/*     */   }
/*     */   public static void exiting(String paramString1, String paramString2) {
/* 286 */     instance(); getLogger().exiting(paramString1, paramString2);
/*     */   }
/*     */   public static void exiting(String paramString1, String paramString2, Object paramObject) {
/* 289 */     instance(); getLogger().exiting(paramString1, paramString2, paramObject);
/*     */   }
/*     */   public static void throwing(String paramString1, String paramString2, Throwable paramThrowable) {
/* 292 */     instance(); getLogger().throwing(paramString1, paramString2, paramThrowable);
/*     */   }
/*     */   public static void severe(String paramString) {
/* 295 */     instance(); getLogger().severe(paramString);
/*     */   }
/*     */   public static void warning(String paramString) {
/* 298 */     instance(); getLogger().warning(paramString);
/*     */   }
/*     */   public static void info(String paramString) {
/* 301 */     instance(); getLogger().info(paramString);
/*     */   }
/*     */   public static void config(String paramString) {
/* 304 */     instance(); getLogger().config(paramString);
/*     */   }
/*     */   public static void fine(String paramString) {
/* 307 */     instance(); getLogger().fine(paramString);
/*     */   }
/*     */   public static void finer(String paramString) {
/* 310 */     instance(); getLogger().finer(paramString);
/*     */   }
/*     */   public static void finest(String paramString) {
/* 313 */     instance(); getLogger().finest(paramString);
/*     */   }
/*     */   
/*     */   public MLogger getMLogger(Class paramClass) {
/* 317 */     return getMLogger(paramClass.getName());
/*     */   }
/*     */   
/*     */   public abstract MLogger getMLogger(String paramString);
/*     */   
/*     */   public abstract MLogger getMLogger();
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/log/MLog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */