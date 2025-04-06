/*     */ package com.mchange.v2.log;
/*     */ 
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
/*     */ public class NullMLogger
/*     */   implements MLogger
/*     */ {
/*  42 */   private static final MLogger INSTANCE = new NullMLogger();
/*     */   
/*     */   public static MLogger instance() {
/*  45 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final String NAME = "NullMLogger";
/*     */ 
/*     */   
/*     */   public void addHandler(Object paramObject) throws SecurityException {}
/*     */ 
/*     */   
/*     */   public void config(String paramString) {}
/*     */ 
/*     */   
/*     */   public void entering(String paramString1, String paramString2) {}
/*     */ 
/*     */   
/*     */   public void entering(String paramString1, String paramString2, Object paramObject) {}
/*     */ 
/*     */   
/*     */   public void entering(String paramString1, String paramString2, Object[] paramArrayOfObject) {}
/*     */ 
/*     */   
/*     */   public void exiting(String paramString1, String paramString2) {}
/*     */ 
/*     */   
/*     */   public void exiting(String paramString1, String paramString2, Object paramObject) {}
/*     */ 
/*     */   
/*     */   public void fine(String paramString) {}
/*     */ 
/*     */   
/*     */   public void finer(String paramString) {}
/*     */ 
/*     */   
/*     */   public void finest(String paramString) {}
/*     */ 
/*     */   
/*     */   public Object getFilter() {
/*  83 */     return null;
/*     */   }
/*     */   public Object[] getHandlers() {
/*  86 */     return null;
/*     */   }
/*     */   public MLevel getLevel() {
/*  89 */     return MLevel.OFF;
/*     */   }
/*     */   public String getName() {
/*  92 */     return "NullMLogger";
/*     */   }
/*     */   public ResourceBundle getResourceBundle() {
/*  95 */     return null;
/*     */   }
/*     */   public String getResourceBundleName() {
/*  98 */     return null;
/*     */   }
/*     */   public boolean getUseParentHandlers() {
/* 101 */     return false;
/*     */   }
/*     */   
/*     */   public void info(String paramString) {}
/*     */   
/*     */   public boolean isLoggable(MLevel paramMLevel) {
/* 107 */     return false;
/*     */   }
/*     */   
/*     */   public void log(MLevel paramMLevel, String paramString) {}
/*     */   
/*     */   public void log(MLevel paramMLevel, String paramString, Object paramObject) {}
/*     */   
/*     */   public void log(MLevel paramMLevel, String paramString, Object[] paramArrayOfObject) {}
/*     */   
/*     */   public void log(MLevel paramMLevel, String paramString, Throwable paramThrowable) {}
/*     */   
/*     */   public void logp(MLevel paramMLevel, String paramString1, String paramString2, String paramString3) {}
/*     */   
/*     */   public void logp(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, Object paramObject) {}
/*     */   
/*     */   public void logp(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, Object[] paramArrayOfObject) {}
/*     */   
/*     */   public void logp(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, Throwable paramThrowable) {}
/*     */   
/*     */   public void logrb(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, String paramString4) {}
/*     */   
/*     */   public void logrb(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, String paramString4, Object paramObject) {}
/*     */   
/*     */   public void logrb(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, String paramString4, Object[] paramArrayOfObject) {}
/*     */   
/*     */   public void logrb(MLevel paramMLevel, String paramString1, String paramString2, String paramString3, String paramString4, Throwable paramThrowable) {}
/*     */   
/*     */   public void removeHandler(Object paramObject) throws SecurityException {}
/*     */   
/*     */   public void setFilter(Object paramObject) throws SecurityException {}
/*     */   
/*     */   public void setLevel(MLevel paramMLevel) throws SecurityException {}
/*     */   
/*     */   public void setUseParentHandlers(boolean paramBoolean) {}
/*     */   
/*     */   public void severe(String paramString) {}
/*     */   
/*     */   public void throwing(String paramString1, String paramString2, Throwable paramThrowable) {}
/*     */   
/*     */   public void warning(String paramString) {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/log/NullMLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */