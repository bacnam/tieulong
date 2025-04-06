/*     */ package com.mchange.v2.log;
/*     */ 
/*     */ import com.mchange.lang.ThrowableUtils;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ResourceBundle;
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
/*     */ public final class FallbackMLog
/*     */   extends MLog
/*     */ {
/*     */   static final MLevel DEFAULT_CUTOFF_LEVEL;
/*     */   
/*     */   static {
/*  50 */     MLevel mLevel = null;
/*  51 */     String str = MLogConfig.getProperty("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL");
/*  52 */     if (str != null)
/*  53 */       mLevel = MLevel.fromSeverity(str); 
/*  54 */     if (mLevel == null)
/*  55 */       mLevel = MLevel.INFO; 
/*  56 */     DEFAULT_CUTOFF_LEVEL = mLevel;
/*     */   }
/*  58 */   static final String SEP = System.getProperty("line.separator");
/*     */ 
/*     */   
/*  61 */   MLogger logger = new FallbackMLogger();
/*     */   
/*     */   public MLogger getMLogger(String paramString) {
/*  64 */     return this.logger;
/*     */   }
/*     */   public MLogger getMLogger() {
/*  67 */     return this.logger;
/*     */   }
/*     */   
/*     */   private static final class FallbackMLogger implements MLogger {
/*  71 */     MLevel cutoffLevel = FallbackMLog.DEFAULT_CUTOFF_LEVEL;
/*     */ 
/*     */     
/*     */     private void formatrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Object[] param1ArrayOfObject, Throwable param1Throwable) {
/*  75 */       ResourceBundle resourceBundle = ResourceBundle.getBundle(param1String3);
/*  76 */       if (param1String4 != null && resourceBundle != null) {
/*     */         
/*  78 */         String str = resourceBundle.getString(param1String4);
/*  79 */         if (str != null)
/*  80 */           param1String4 = str; 
/*     */       } 
/*  82 */       format(param1MLevel, param1String1, param1String2, param1String4, param1ArrayOfObject, param1Throwable);
/*     */     }
/*     */     
/*     */     private void format(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Object[] param1ArrayOfObject, Throwable param1Throwable) {
/*  86 */       System.err.println(formatString(param1MLevel, param1String1, param1String2, param1String3, param1ArrayOfObject, param1Throwable));
/*     */     }
/*     */     
/*     */     private String formatString(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Object[] param1ArrayOfObject, Throwable param1Throwable) {
/*  90 */       boolean bool = (param1String2 != null && !param1String2.endsWith(")")) ? true : false;
/*     */       
/*  92 */       StringBuffer stringBuffer = new StringBuffer(256);
/*  93 */       stringBuffer.append(param1MLevel.getLineHeader());
/*  94 */       stringBuffer.append(' ');
/*  95 */       if (param1String1 != null && param1String2 != null) {
/*     */         
/*  97 */         stringBuffer.append('[');
/*  98 */         stringBuffer.append(param1String1);
/*  99 */         stringBuffer.append('.');
/* 100 */         stringBuffer.append(param1String2);
/* 101 */         if (bool)
/* 102 */           stringBuffer.append("()"); 
/* 103 */         stringBuffer.append(']');
/*     */       }
/* 105 */       else if (param1String1 != null) {
/*     */         
/* 107 */         stringBuffer.append('[');
/* 108 */         stringBuffer.append(param1String1);
/* 109 */         stringBuffer.append(']');
/*     */       }
/* 111 */       else if (param1String2 != null) {
/*     */         
/* 113 */         stringBuffer.append('[');
/* 114 */         stringBuffer.append(param1String2);
/* 115 */         if (bool)
/* 116 */           stringBuffer.append("()"); 
/* 117 */         stringBuffer.append(']');
/*     */       } 
/* 119 */       if (param1String3 == null) {
/*     */         
/* 121 */         if (param1ArrayOfObject != null)
/*     */         {
/* 123 */           stringBuffer.append("params: "); byte b; int i;
/* 124 */           for (b = 0, i = param1ArrayOfObject.length; b < i; b++)
/*     */           {
/* 126 */             if (b != 0) stringBuffer.append(", "); 
/* 127 */             stringBuffer.append(param1ArrayOfObject[b]);
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 133 */       else if (param1ArrayOfObject == null) {
/* 134 */         stringBuffer.append(param1String3);
/*     */       } else {
/*     */         
/* 137 */         MessageFormat messageFormat = new MessageFormat(param1String3);
/* 138 */         stringBuffer.append(messageFormat.format(param1ArrayOfObject));
/*     */       } 
/*     */ 
/*     */       
/* 142 */       if (param1Throwable != null) {
/*     */         
/* 144 */         stringBuffer.append(FallbackMLog.SEP);
/* 145 */         stringBuffer.append(ThrowableUtils.extractStackTrace(param1Throwable));
/*     */       } 
/*     */       
/* 148 */       return stringBuffer.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ResourceBundle getResourceBundle() {
/* 154 */       return null;
/*     */     }
/*     */     
/*     */     public String getResourceBundleName() {
/* 158 */       return null;
/*     */     }
/*     */     
/*     */     public void setFilter(Object param1Object) throws SecurityException {
/* 162 */       warning("Using FallbackMLog -- Filters not supported!");
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getFilter() {
/* 167 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void log(MLevel param1MLevel, String param1String) {
/* 172 */       if (isLoggable(param1MLevel)) {
/* 173 */         format(param1MLevel, null, null, param1String, null, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public void log(MLevel param1MLevel, String param1String, Object param1Object) {
/* 178 */       if (isLoggable(param1MLevel)) {
/* 179 */         format(param1MLevel, null, null, param1String, new Object[] { param1Object }, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public void log(MLevel param1MLevel, String param1String, Object[] param1ArrayOfObject) {
/* 184 */       if (isLoggable(param1MLevel)) {
/* 185 */         format(param1MLevel, null, null, param1String, param1ArrayOfObject, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public void log(MLevel param1MLevel, String param1String, Throwable param1Throwable) {
/* 190 */       if (isLoggable(param1MLevel)) {
/* 191 */         format(param1MLevel, null, null, param1String, null, param1Throwable);
/*     */       }
/*     */     }
/*     */     
/*     */     public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3) {
/* 196 */       if (isLoggable(param1MLevel)) {
/* 197 */         format(param1MLevel, param1String1, param1String2, param1String3, null, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Object param1Object) {
/* 202 */       if (isLoggable(param1MLevel)) {
/* 203 */         format(param1MLevel, param1String1, param1String2, param1String3, new Object[] { param1Object }, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Object[] param1ArrayOfObject) {
/* 208 */       if (isLoggable(param1MLevel)) {
/* 209 */         format(param1MLevel, param1String1, param1String2, param1String3, param1ArrayOfObject, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public void logp(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, Throwable param1Throwable) {
/* 214 */       if (isLoggable(param1MLevel)) {
/* 215 */         format(param1MLevel, param1String1, param1String2, param1String3, null, param1Throwable);
/*     */       }
/*     */     }
/*     */     
/*     */     public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4) {
/* 220 */       if (isLoggable(param1MLevel)) {
/* 221 */         formatrb(param1MLevel, param1String1, param1String2, param1String3, param1String4, null, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Object param1Object) {
/* 226 */       if (isLoggable(param1MLevel)) {
/* 227 */         formatrb(param1MLevel, param1String1, param1String2, param1String3, param1String4, new Object[] { param1Object }, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Object[] param1ArrayOfObject) {
/* 232 */       if (isLoggable(param1MLevel)) {
/* 233 */         formatrb(param1MLevel, param1String1, param1String2, param1String3, param1String4, param1ArrayOfObject, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public void logrb(MLevel param1MLevel, String param1String1, String param1String2, String param1String3, String param1String4, Throwable param1Throwable) {
/* 238 */       if (isLoggable(param1MLevel)) {
/* 239 */         formatrb(param1MLevel, param1String1, param1String2, param1String3, param1String4, null, param1Throwable);
/*     */       }
/*     */     }
/*     */     
/*     */     public void entering(String param1String1, String param1String2) {
/* 244 */       if (isLoggable(MLevel.FINER)) {
/* 245 */         format(MLevel.FINER, param1String1, param1String2, "Entering method.", null, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public void entering(String param1String1, String param1String2, Object param1Object) {
/* 250 */       if (isLoggable(MLevel.FINER)) {
/* 251 */         format(MLevel.FINER, param1String1, param1String2, "Entering method with argument " + param1Object, null, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public void entering(String param1String1, String param1String2, Object[] param1ArrayOfObject) {
/* 256 */       if (isLoggable(MLevel.FINER))
/*     */       {
/* 258 */         if (param1ArrayOfObject == null) {
/* 259 */           entering(param1String1, param1String2);
/*     */         } else {
/*     */           
/* 262 */           StringBuffer stringBuffer = new StringBuffer(128);
/* 263 */           stringBuffer.append("( "); byte b; int i;
/* 264 */           for (b = 0, i = param1ArrayOfObject.length; b < i; b++) {
/*     */             
/* 266 */             if (b != 0) stringBuffer.append(", "); 
/* 267 */             stringBuffer.append(param1ArrayOfObject[b]);
/*     */           } 
/* 269 */           stringBuffer.append(" )");
/* 270 */           format(MLevel.FINER, param1String1, param1String2, "Entering method with arguments " + stringBuffer.toString(), null, null);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void exiting(String param1String1, String param1String2) {
/* 277 */       if (isLoggable(MLevel.FINER)) {
/* 278 */         format(MLevel.FINER, param1String1, param1String2, "Exiting method.", null, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public void exiting(String param1String1, String param1String2, Object param1Object) {
/* 283 */       if (isLoggable(MLevel.FINER)) {
/* 284 */         format(MLevel.FINER, param1String1, param1String2, "Exiting method with result " + param1Object, null, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public void throwing(String param1String1, String param1String2, Throwable param1Throwable) {
/* 289 */       if (isLoggable(MLevel.FINE)) {
/* 290 */         format(MLevel.FINE, param1String1, param1String2, "Throwing exception.", null, param1Throwable);
/*     */       }
/*     */     }
/*     */     
/*     */     public void severe(String param1String) {
/* 295 */       if (isLoggable(MLevel.SEVERE)) {
/* 296 */         format(MLevel.SEVERE, null, null, param1String, null, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public void warning(String param1String) {
/* 301 */       if (isLoggable(MLevel.WARNING)) {
/* 302 */         format(MLevel.WARNING, null, null, param1String, null, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public void info(String param1String) {
/* 307 */       if (isLoggable(MLevel.INFO)) {
/* 308 */         format(MLevel.INFO, null, null, param1String, null, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public void config(String param1String) {
/* 313 */       if (isLoggable(MLevel.CONFIG)) {
/* 314 */         format(MLevel.CONFIG, null, null, param1String, null, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public void fine(String param1String) {
/* 319 */       if (isLoggable(MLevel.FINE)) {
/* 320 */         format(MLevel.FINE, null, null, param1String, null, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public void finer(String param1String) {
/* 325 */       if (isLoggable(MLevel.FINER)) {
/* 326 */         format(MLevel.FINER, null, null, param1String, null, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public void finest(String param1String) {
/* 331 */       if (isLoggable(MLevel.FINEST))
/* 332 */         format(MLevel.FINEST, null, null, param1String, null, null); 
/*     */     }
/*     */     
/*     */     public void setLevel(MLevel param1MLevel) throws SecurityException {
/* 336 */       this.cutoffLevel = param1MLevel;
/*     */     }
/*     */     public synchronized MLevel getLevel() {
/* 339 */       return this.cutoffLevel;
/*     */     }
/*     */     public synchronized boolean isLoggable(MLevel param1MLevel) {
/* 342 */       return (param1MLevel.intValue() >= this.cutoffLevel.intValue());
/*     */     }
/*     */     public String getName() {
/* 345 */       return "global";
/*     */     }
/*     */     
/*     */     public void addHandler(Object param1Object) throws SecurityException {
/* 349 */       warning("Using FallbackMLog -- Handlers not supported.");
/*     */     }
/*     */ 
/*     */     
/*     */     public void removeHandler(Object param1Object) throws SecurityException {
/* 354 */       warning("Using FallbackMLog -- Handlers not supported.");
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] getHandlers() {
/* 359 */       warning("Using FallbackMLog -- Handlers not supported.");
/* 360 */       return new Object[0];
/*     */     }
/*     */ 
/*     */     
/*     */     public void setUseParentHandlers(boolean param1Boolean) {
/* 365 */       warning("Using FallbackMLog -- Handlers not supported.");
/*     */     }
/*     */     
/*     */     public boolean getUseParentHandlers() {
/* 369 */       return false;
/*     */     }
/*     */     
/*     */     private FallbackMLogger() {}
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/log/FallbackMLog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */