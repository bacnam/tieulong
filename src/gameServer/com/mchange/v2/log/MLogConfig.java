/*     */ package com.mchange.v2.log;
/*     */ 
/*     */ import com.mchange.v2.cfg.DelayedLogItem;
/*     */ import com.mchange.v2.cfg.MConfig;
/*     */ import com.mchange.v2.cfg.MLogConfigSource;
/*     */ import com.mchange.v2.cfg.MultiPropertiesConfig;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
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
/*     */ public final class MLogConfig
/*     */ {
/*  49 */   private static MultiPropertiesConfig CONFIG = null;
/*  50 */   private static List BOOTSTRAP_LOG_ITEMS = null;
/*  51 */   private static Method delayedDumpToLogger = null;
/*     */ 
/*     */   
/*     */   public static synchronized void refresh(MultiPropertiesConfig[] paramArrayOfMultiPropertiesConfig, String paramString) {
/*  55 */     String[] arrayOfString1 = { "/com/mchange/v2/log/default-mchange-log.properties" };
/*  56 */     String[] arrayOfString2 = { "/mchange-log.properties", "/" };
/*     */     
/*  58 */     ArrayList<DelayedLogItem> arrayList = new ArrayList();
/*  59 */     MultiPropertiesConfig multiPropertiesConfig = MLogConfigSource.readVmConfig(arrayOfString1, arrayOfString2, arrayList);
/*     */     
/*  61 */     boolean bool = (CONFIG == null) ? true : false;
/*     */     
/*  63 */     if (paramArrayOfMultiPropertiesConfig != null) {
/*     */       
/*  65 */       int i = paramArrayOfMultiPropertiesConfig.length;
/*  66 */       MultiPropertiesConfig[] arrayOfMultiPropertiesConfig = new MultiPropertiesConfig[i + 1];
/*  67 */       arrayOfMultiPropertiesConfig[0] = multiPropertiesConfig;
/*  68 */       for (byte b = 0; b < i; b++)
/*  69 */         arrayOfMultiPropertiesConfig[b + 1] = paramArrayOfMultiPropertiesConfig[b]; 
/*  70 */       arrayList.add(new DelayedLogItem(DelayedLogItem.Level.INFO, (bool ? "Loaded" : "Refreshed") + " MLog library log configuration, with overrides" + ((paramString == null) ? "." : (": " + paramString))));
/*  71 */       CONFIG = MConfig.combine(arrayOfMultiPropertiesConfig);
/*     */     }
/*     */     else {
/*     */       
/*  75 */       if (!bool)
/*  76 */         arrayList.add(new DelayedLogItem(DelayedLogItem.Level.INFO, "Refreshed MLog library log configuration, without overrides.")); 
/*  77 */       CONFIG = multiPropertiesConfig;
/*     */     } 
/*  79 */     BOOTSTRAP_LOG_ITEMS = arrayList;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void ensureLoad() {
/*  84 */     if (CONFIG == null) refresh(null, null);
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private static void ensureDelayedDumpToLogger() {
/*     */     try {
/*  91 */       if (delayedDumpToLogger == null)
/*     */       {
/*  93 */         Class<?> clazz1 = Class.forName("com.mchange.v2.cfg.MConfig");
/*  94 */         Class<?> clazz2 = Class.forName("com.mchange.v2.cfg.DelayedLogItem");
/*  95 */         delayedDumpToLogger = clazz1.getMethod("dumpToLogger", new Class[] { clazz2, MLogger.class });
/*     */       }
/*     */     
/*  98 */     } catch (RuntimeException runtimeException) {
/*     */       
/* 100 */       runtimeException.printStackTrace();
/* 101 */       throw runtimeException;
/*     */     }
/* 103 */     catch (Exception exception) {
/*     */       
/* 105 */       exception.printStackTrace();
/* 106 */       throw new RuntimeException(exception);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static synchronized String getProperty(String paramString) {
/* 112 */     ensureLoad();
/* 113 */     return CONFIG.getProperty(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized void logDelayedItems(MLogger paramMLogger) {
/* 119 */     ensureLoad();
/* 120 */     ensureDelayedDumpToLogger();
/*     */     
/* 122 */     ArrayList arrayList = new ArrayList();
/* 123 */     arrayList.addAll(BOOTSTRAP_LOG_ITEMS);
/* 124 */     arrayList.addAll(CONFIG.getDelayedLogItems());
/*     */     
/* 126 */     HashSet hashSet = new HashSet();
/* 127 */     hashSet.addAll(arrayList);
/*     */     
/* 129 */     for (Object object : arrayList) {
/*     */ 
/*     */ 
/*     */       
/* 133 */       if (hashSet.contains(object)) {
/*     */         
/* 135 */         hashSet.remove(object);
/*     */         try {
/* 137 */           delayedDumpToLogger.invoke(null, new Object[] { object, paramMLogger });
/* 138 */         } catch (Exception exception) {
/*     */ 
/*     */           
/* 141 */           exception.printStackTrace();
/* 142 */           throw new Error(exception);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static synchronized String dump() {
/* 149 */     return CONFIG.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/log/MLogConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */