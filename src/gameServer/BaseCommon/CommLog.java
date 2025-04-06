/*     */ package BaseCommon;
/*     */ 
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.joran.JoranConfigurator;
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.joran.spi.JoranException;
/*     */ import ch.qos.logback.core.util.StatusPrinter;
/*     */ import java.io.File;
/*     */ import java.util.Arrays;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.slf4j.Marker;
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
/*     */ public class CommLog
/*     */ {
/*  27 */   private static Logger LOG = null;
/*  28 */   private static CommLog instance = new CommLog();
/*     */   
/*     */   public static CommLog getInstance() {
/*  31 */     return instance;
/*     */   }
/*     */   
/*     */   public static void initLog() {
/*  35 */     LOG = LoggerFactory.getLogger("CommLog");
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reloadConfig() {
/*  40 */     LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
/*     */     
/*  42 */     String sConfigFile = System.getProperty("logback.configurationFile_bak");
/*  43 */     File externalConfigFile = new File(sConfigFile);
/*  44 */     if (!externalConfigFile.exists()) {
/*  45 */       error("Logback External Config File {} does nott exists", sConfigFile);
/*     */       return;
/*     */     } 
/*  48 */     if (!externalConfigFile.isFile()) {
/*  49 */       error("Logback External Config File {} exists, but does not reference a file", sConfigFile);
/*     */       return;
/*     */     } 
/*  52 */     if (!externalConfigFile.canRead()) {
/*  53 */       error("Logback External Config File { }exists and is a file, but cannot be read.", sConfigFile);
/*     */       return;
/*     */     } 
/*  56 */     JoranConfigurator configurator = new JoranConfigurator();
/*  57 */     configurator.setContext((Context)lc);
/*  58 */     lc.reset();
/*     */     try {
/*  60 */       configurator.doConfigure(sConfigFile);
/*  61 */     } catch (JoranException e) {
/*  62 */       e.printStackTrace();
/*     */     } 
/*  64 */     StatusPrinter.printInCaseOfErrorsOrWarnings((Context)lc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void trace(String msg) {
/*  71 */     LOG.trace(msg);
/*     */   }
/*     */   
/*     */   public static void trace(String format, Object arg) {
/*  75 */     LOG.trace(format, arg);
/*     */   }
/*     */   
/*     */   public static void trace(String format, Object arg1, Object arg2) {
/*  79 */     LOG.trace(format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public static void trace(String format, Object... arguments) {
/*  83 */     LOG.trace(format, arguments);
/*     */   }
/*     */   
/*     */   public static void trace(String msg, Throwable t) {
/*  87 */     LOG.trace(msg, t);
/*     */   }
/*     */   
/*     */   public static boolean isTraceEnabled(Marker marker) {
/*  91 */     return LOG.isTraceEnabled(marker);
/*     */   }
/*     */   
/*     */   public static void trace(Marker marker, String msg) {
/*  95 */     LOG.trace(marker, msg);
/*     */   }
/*     */   
/*     */   public static void trace(Marker marker, String format, Object arg) {
/*  99 */     LOG.trace(marker, format, arg);
/*     */   }
/*     */   
/*     */   public static void trace(Marker marker, String format, Object arg1, Object arg2) {
/* 103 */     LOG.trace(marker, format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public static void trace(Marker marker, String format, Object... argArray) {
/* 107 */     LOG.trace(marker, format, argArray);
/*     */   }
/*     */   
/*     */   public static void trace(Marker marker, String msg, Throwable t) {
/* 111 */     LOG.trace(marker, msg, t);
/*     */   }
/*     */   
/*     */   public static void debug(String msg) {
/* 115 */     LOG.debug(msg);
/*     */   }
/*     */   
/*     */   public static void debug(String format, Object arg) {
/* 119 */     LOG.debug(format, arg);
/*     */   }
/*     */   
/*     */   public static void debug(String format, Object arg1, Object arg2) {
/* 123 */     LOG.debug(format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public static void debug(String format, Object... arguments) {
/* 127 */     LOG.debug(format, arguments);
/*     */   }
/*     */   
/*     */   public static void debug(String msg, Throwable t) {
/* 131 */     LOG.debug(msg, t);
/*     */   }
/*     */   
/*     */   public static boolean isDebugEnabled(Marker marker) {
/* 135 */     return LOG.isDebugEnabled(marker);
/*     */   }
/*     */   
/*     */   public static void debug(Marker marker, String msg) {
/* 139 */     LOG.debug(marker, msg);
/*     */   }
/*     */   
/*     */   public static void debug(Marker marker, String format, Object arg) {
/* 143 */     LOG.debug(marker, format, arg);
/*     */   }
/*     */   
/*     */   public static void debug(Marker marker, String format, Object arg1, Object arg2) {
/* 147 */     LOG.debug(marker, format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public static void debug(Marker marker, String format, Object... arguments) {
/* 151 */     LOG.debug(marker, format, arguments);
/*     */   }
/*     */   
/*     */   public static void debug(Marker marker, String msg, Throwable t) {
/* 155 */     LOG.debug(marker, msg, t);
/*     */   }
/*     */   
/*     */   public static void info(String msg) {
/* 159 */     LOG.info(msg);
/*     */   }
/*     */   
/*     */   public static void info(String format, Object arg) {
/* 163 */     LOG.info(format, arg);
/*     */   }
/*     */   
/*     */   public static void info(String format, Object arg1, Object arg2) {
/* 167 */     LOG.info(format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public static void info(String format, Object... arguments) {
/* 171 */     LOG.info(format, arguments);
/*     */   }
/*     */   
/*     */   public static void info(String msg, Throwable t) {
/* 175 */     LOG.info(msg, t);
/*     */   }
/*     */   
/*     */   public static boolean isInfoEnabled(Marker marker) {
/* 179 */     return LOG.isInfoEnabled(marker);
/*     */   }
/*     */   
/*     */   public static void info(Marker marker, String msg) {
/* 183 */     LOG.info(marker, msg);
/*     */   }
/*     */   
/*     */   public static void info(Marker marker, String format, Object arg) {
/* 187 */     LOG.info(marker, format, arg);
/*     */   }
/*     */   
/*     */   public static void info(Marker marker, String format, Object arg1, Object arg2) {
/* 191 */     LOG.info(marker, format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public static void info(Marker marker, String format, Object... arguments) {
/* 195 */     LOG.info(marker, format, arguments);
/*     */   }
/*     */   
/*     */   public static void info(Marker marker, String msg, Throwable t) {
/* 199 */     LOG.info(marker, msg, t);
/*     */   }
/*     */   
/*     */   public static void warn(String msg) {
/* 203 */     LOG.warn(msg);
/*     */   }
/*     */   
/*     */   public static void warn(String format, Object arg) {
/* 207 */     LOG.warn(format, arg);
/*     */   }
/*     */   
/*     */   public static void warn(String format, Object... arguments) {
/* 211 */     LOG.warn(format, arguments);
/*     */   }
/*     */   
/*     */   public static void warn(String format, Object arg1, Object arg2) {
/* 215 */     LOG.warn(format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public static void warn(String msg, Throwable t) {
/* 219 */     LOG.warn(msg, t);
/*     */   }
/*     */   
/*     */   public static boolean isWarnEnabled(Marker marker) {
/* 223 */     return LOG.isWarnEnabled(marker);
/*     */   }
/*     */   
/*     */   public static void warn(Marker marker, String msg) {
/* 227 */     LOG.warn(marker, msg);
/*     */   }
/*     */   
/*     */   public static void warn(Marker marker, String format, Object arg) {
/* 231 */     LOG.warn(marker, format, arg);
/*     */   }
/*     */   
/*     */   public static void warn(Marker marker, String format, Object arg1, Object arg2) {
/* 235 */     LOG.warn(marker, format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public static void warn(Marker marker, String format, Object... arguments) {
/* 239 */     LOG.warn(marker, format, arguments);
/*     */   }
/*     */   
/*     */   public static void warn(Marker marker, String msg, Throwable t) {
/* 243 */     LOG.warn(marker, msg, t);
/*     */   }
/*     */   
/*     */   public static void error(String msg) {
/* 247 */     if (msg == null) {
/* 248 */       LOG.error("meaningless err msg found:null, check caller :{}", Arrays.toString((Object[])Thread.currentThread().getStackTrace()));
/*     */     }
/* 250 */     LOG.error(msg);
/*     */   }
/*     */   
/*     */   public static void error(String format, Object arg) {
/* 254 */     if (format == null) {
/* 255 */       LOG.error("meaningless err msg found:null, check caller :{}", Arrays.toString((Object[])Thread.currentThread().getStackTrace()));
/*     */     }
/* 257 */     LOG.error(format, arg);
/*     */   }
/*     */   
/*     */   public static void error(String format, Object arg1, Object arg2) {
/* 261 */     if (format == null) {
/* 262 */       LOG.error("meaningless err msg found:null, check caller :{}", Arrays.toString((Object[])Thread.currentThread().getStackTrace()));
/*     */     }
/* 264 */     LOG.error(format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public static void error(String format, Object... arguments) {
/* 268 */     if (format == null) {
/* 269 */       LOG.error("meaningless err msg found:null, check caller :{}", Arrays.toString((Object[])Thread.currentThread().getStackTrace()));
/*     */     }
/* 271 */     LOG.error(format, arguments);
/*     */   }
/*     */   
/*     */   public static void error(String msg, Throwable t) {
/* 275 */     LOG.error(msg, t);
/*     */   }
/*     */   
/*     */   public static boolean isErrorEnabled(Marker marker) {
/* 279 */     return LOG.isErrorEnabled(marker);
/*     */   }
/*     */   
/*     */   public static void error(Marker marker, String msg) {
/* 283 */     LOG.error(marker, msg);
/*     */   }
/*     */   
/*     */   public static void error(Marker marker, String format, Object arg) {
/* 287 */     LOG.error(marker, format, arg);
/*     */   }
/*     */   
/*     */   public static void error(Marker marker, String format, Object arg1, Object arg2) {
/* 291 */     LOG.error(marker, format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public static void error(Marker marker, String format, Object... arguments) {
/* 295 */     LOG.error(marker, format, arguments);
/*     */   }
/*     */   
/*     */   public static void error(Marker marker, String msg, Throwable t) {
/* 299 */     LOG.error(marker, msg, t);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/BaseCommon/CommLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */