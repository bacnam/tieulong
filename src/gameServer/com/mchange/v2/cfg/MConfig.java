/*     */ package com.mchange.v2.cfg;
/*     */ 
/*     */ import com.mchange.v1.cachedstore.CachedStore;
/*     */ import com.mchange.v1.cachedstore.CachedStoreException;
/*     */ import com.mchange.v1.cachedstore.CachedStoreFactory;
/*     */ import com.mchange.v1.cachedstore.CachedStoreUtils;
/*     */ import com.mchange.v1.util.ArrayUtils;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public final class MConfig
/*     */ {
/*  45 */   private static final MLogger logger = MLog.getLogger(MConfig.class);
/*     */ 
/*     */   
/*     */   private static final Map<DelayedLogItem.Level, MLevel> levelMap;
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  53 */       HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
/*  54 */       for (DelayedLogItem.Level level : DelayedLogItem.Level.values())
/*  55 */         hashMap.put(level, MLevel.class.getField(level.toString()).get(null)); 
/*  56 */       levelMap = Collections.unmodifiableMap(hashMap);
/*     */     }
/*  58 */     catch (RuntimeException runtimeException) {
/*     */       
/*  60 */       runtimeException.printStackTrace();
/*  61 */       throw runtimeException;
/*     */     }
/*  63 */     catch (Exception exception) {
/*     */       
/*  65 */       exception.printStackTrace();
/*  66 */       throw new RuntimeException(exception);
/*     */     } 
/*     */   }
/*     */   
/*  70 */   static final CachedStore cache = CachedStoreUtils.synchronizedCachedStore((CachedStore)CachedStoreFactory.createNoCleanupCachedStore(new CSManager()));
/*     */ 
/*     */   
/*     */   public static MultiPropertiesConfig readVmConfig(String[] paramArrayOfString1, String[] paramArrayOfString2) {
/*     */     try {
/*  75 */       return (MultiPropertiesConfig)cache.find(new PathsKey(paramArrayOfString1, paramArrayOfString2));
/*  76 */     } catch (CachedStoreException cachedStoreException) {
/*  77 */       throw new RuntimeException(cachedStoreException);
/*     */     } 
/*     */   }
/*     */   public static MultiPropertiesConfig readVmConfig() {
/*  81 */     return readVmConfig(ConfigUtils.NO_PATHS, ConfigUtils.NO_PATHS);
/*     */   }
/*     */   
/*     */   public static MultiPropertiesConfig readConfig(String[] paramArrayOfString) {
/*     */     try {
/*  86 */       return (MultiPropertiesConfig)cache.find(new PathsKey(paramArrayOfString));
/*  87 */     } catch (CachedStoreException cachedStoreException) {
/*  88 */       throw new RuntimeException(cachedStoreException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static MultiPropertiesConfig combine(MultiPropertiesConfig[] paramArrayOfMultiPropertiesConfig) {
/*  95 */     return ConfigUtils.combine(paramArrayOfMultiPropertiesConfig);
/*     */   }
/*     */   public static void dumpToLogger(List<DelayedLogItem> paramList, MLogger paramMLogger) {
/*  98 */     for (DelayedLogItem delayedLogItem : paramList) dumpToLogger(delayedLogItem, paramMLogger); 
/*     */   }
/*     */   public static void dumpToLogger(DelayedLogItem paramDelayedLogItem, MLogger paramMLogger) {
/* 101 */     paramMLogger.log(levelMap.get(paramDelayedLogItem.getLevel()), paramDelayedLogItem.getText(), paramDelayedLogItem.getException());
/*     */   }
/*     */   
/*     */   private static final class PathsKey
/*     */   {
/*     */     String[] paths;
/*     */     List delayedLogItems;
/*     */     
/*     */     public boolean equals(Object param1Object) {
/* 110 */       if (param1Object instanceof PathsKey) {
/* 111 */         return Arrays.equals((Object[])this.paths, (Object[])((PathsKey)param1Object).paths);
/*     */       }
/* 113 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 117 */       return ArrayUtils.hashArray((Object[])this.paths);
/*     */     }
/*     */     
/*     */     PathsKey(String[] param1ArrayOfString1, String[] param1ArrayOfString2) {
/* 121 */       this.delayedLogItems = new ArrayList();
/*     */       
/* 123 */       List list = ConfigUtils.vmCondensedPaths(param1ArrayOfString1, param1ArrayOfString2, this.delayedLogItems);
/* 124 */       this.paths = (String[])list.toArray((Object[])new String[list.size()]);
/*     */     }
/*     */ 
/*     */     
/*     */     PathsKey(String[] param1ArrayOfString) {
/* 129 */       this.delayedLogItems = Collections.emptyList();
/* 130 */       this.paths = param1ArrayOfString;
/*     */     } }
/*     */   
/*     */   private static class CSManager implements CachedStore.Manager {
/*     */     private CSManager() {}
/*     */     
/*     */     public boolean isDirty(Object param1Object1, Object param1Object2) throws Exception {
/* 137 */       return false;
/*     */     }
/*     */     
/*     */     public Object recreateFromKey(Object param1Object) throws Exception {
/* 141 */       MConfig.PathsKey pathsKey = (MConfig.PathsKey)param1Object;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 151 */       ArrayList<DelayedLogItem> arrayList = new ArrayList();
/* 152 */       arrayList.addAll(pathsKey.delayedLogItems);
/* 153 */       MultiPropertiesConfig multiPropertiesConfig = ConfigUtils.read(pathsKey.paths, arrayList);
/* 154 */       MConfig.dumpToLogger(arrayList, MConfig.logger);
/* 155 */       return multiPropertiesConfig;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/cfg/MConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */