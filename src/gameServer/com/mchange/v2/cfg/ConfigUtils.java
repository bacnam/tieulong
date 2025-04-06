/*     */ package com.mchange.v2.cfg;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.TreeMap;
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
/*     */ final class ConfigUtils
/*     */ {
/*  46 */   private static final String[] DFLT_VM_RSRC_PATHFILES = new String[] { "/com/mchange/v2/cfg/vmConfigResourcePaths.txt", "/mchange-config-resource-paths.txt" };
/*  47 */   private static final String[] HARDCODED_DFLT_RSRC_PATHS = new String[] { "/mchange-commons.properties", "hocon:/reference,/application,/", "/" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   static final String[] NO_PATHS = new String[0];
/*     */ 
/*     */   
/*  57 */   static MultiPropertiesConfig vmConfig = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static MultiPropertiesConfig read(String[] paramArrayOfString, List paramList) {
/*  63 */     return new BasicMultiPropertiesConfig(paramArrayOfString, paramList);
/*     */   }
/*     */   public static MultiPropertiesConfig read(String[] paramArrayOfString) {
/*  66 */     return new BasicMultiPropertiesConfig(paramArrayOfString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static MultiPropertiesConfig combine(MultiPropertiesConfig[] paramArrayOfMultiPropertiesConfig) {
/*  72 */     return (new CombinedMultiPropertiesConfig(paramArrayOfMultiPropertiesConfig)).toBasic();
/*     */   }
/*     */   public static MultiPropertiesConfig readVmConfig(String[] paramArrayOfString1, String[] paramArrayOfString2) {
/*  75 */     return readVmConfig(paramArrayOfString1, paramArrayOfString2, (List)null);
/*     */   }
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
/*     */   static List vmCondensedPaths(String[] paramArrayOfString1, String[] paramArrayOfString2, List paramList) {
/*  93 */     return condensePaths(new String[][] { paramArrayOfString1, vmResourcePaths(paramList), paramArrayOfString2 });
/*     */   }
/*     */   
/*     */   static String stringFromPathsList(List paramList) {
/*  97 */     StringBuffer stringBuffer = new StringBuffer(2048); byte b; int i;
/*  98 */     for (b = 0, i = paramList.size(); b < i; b++) {
/*     */       
/* 100 */       if (b != 0) stringBuffer.append(", "); 
/* 101 */       stringBuffer.append(paramList.get(b));
/*     */     } 
/* 103 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static MultiPropertiesConfig readVmConfig(String[] paramArrayOfString1, String[] paramArrayOfString2, List<DelayedLogItem> paramList) {
/* 108 */     paramArrayOfString1 = (paramArrayOfString1 == null) ? NO_PATHS : paramArrayOfString1;
/* 109 */     paramArrayOfString2 = (paramArrayOfString2 == null) ? NO_PATHS : paramArrayOfString2;
/* 110 */     List list = vmCondensedPaths(paramArrayOfString1, paramArrayOfString2, paramList);
/*     */     
/* 112 */     if (paramList != null) {
/* 113 */       paramList.add(new DelayedLogItem(DelayedLogItem.Level.FINER, "Reading VM config for path list " + stringFromPathsList(list)));
/*     */     }
/* 115 */     return read((String[])list.toArray((Object[])new String[list.size()]), paramList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static List condensePaths(String[][] paramArrayOfString) {
/* 125 */     HashSet<String> hashSet = new HashSet();
/* 126 */     ArrayList<String> arrayList = new ArrayList();
/* 127 */     for (int i = paramArrayOfString.length; --i >= 0;) {
/* 128 */       for (int j = (paramArrayOfString[i]).length; --j >= 0; ) {
/*     */         
/* 130 */         String str = paramArrayOfString[i][j];
/* 131 */         if (!hashSet.contains(str)) {
/*     */           
/* 133 */           hashSet.add(str);
/* 134 */           arrayList.add(str);
/*     */         } 
/*     */       } 
/* 137 */     }  Collections.reverse(arrayList);
/* 138 */     return arrayList;
/*     */   }
/*     */ 
/*     */   
/*     */   private static List readResourcePathsFromResourcePathsTextFile(String paramString, List<DelayedLogItem> paramList) {
/* 143 */     ArrayList<String> arrayList = new ArrayList();
/*     */     
/* 145 */     BufferedReader bufferedReader = null;
/*     */     
/*     */     try {
/* 148 */       InputStream inputStream = MultiPropertiesConfig.class.getResourceAsStream(paramString);
/* 149 */       if (inputStream != null) {
/*     */         
/* 151 */         bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "8859_1"));
/*     */         String str;
/* 153 */         while ((str = bufferedReader.readLine()) != null) {
/*     */           
/* 155 */           str = str.trim();
/* 156 */           if ("".equals(str) || str.startsWith("#")) {
/*     */             continue;
/*     */           }
/* 159 */           arrayList.add(str);
/*     */         } 
/*     */         
/* 162 */         if (paramList != null) {
/* 163 */           paramList.add(new DelayedLogItem(DelayedLogItem.Level.FINEST, String.format("Added paths from resource path text file at '%s'", new Object[] { paramString })));
/*     */         }
/* 165 */       } else if (paramList != null) {
/* 166 */         paramList.add(new DelayedLogItem(DelayedLogItem.Level.FINEST, String.format("Could not find resource path text file for path '%s'. Skipping.", new Object[] { paramString })));
/*     */       }
/*     */     
/* 169 */     } catch (IOException iOException) {
/* 170 */       iOException.printStackTrace();
/*     */     } finally {
/*     */       
/* 173 */       try { if (bufferedReader != null) bufferedReader.close();  }
/* 174 */       catch (IOException iOException) { iOException.printStackTrace(); }
/*     */     
/*     */     } 
/* 177 */     return arrayList;
/*     */   }
/*     */ 
/*     */   
/*     */   private static List readResourcePathsFromResourcePathsTextFiles(String[] paramArrayOfString, List paramList) {
/* 182 */     ArrayList arrayList = new ArrayList(); byte b; int i;
/* 183 */     for (b = 0, i = paramArrayOfString.length; b < i; b++)
/* 184 */       arrayList.addAll(readResourcePathsFromResourcePathsTextFile(paramArrayOfString[b], paramList)); 
/* 185 */     return arrayList;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String[] vmResourcePaths(List paramList) {
/* 190 */     List list = vmResourcePathList(paramList);
/* 191 */     return (String[])list.toArray((Object[])new String[list.size()]);
/*     */   }
/*     */   
/*     */   private static List vmResourcePathList(List paramList) {
/*     */     List<String> list1;
/* 196 */     List list = readResourcePathsFromResourcePathsTextFiles(DFLT_VM_RSRC_PATHFILES, paramList);
/*     */     
/* 198 */     if (list.size() > 0) {
/* 199 */       list1 = list;
/*     */     } else {
/* 201 */       list1 = Arrays.asList(HARDCODED_DFLT_RSRC_PATHS);
/* 202 */     }  return list1;
/*     */   }
/*     */   
/*     */   public static synchronized MultiPropertiesConfig readVmConfig() {
/* 206 */     return readVmConfig((List)null);
/*     */   }
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
/*     */   public static synchronized MultiPropertiesConfig readVmConfig(List paramList) {
/* 225 */     if (vmConfig == null) {
/*     */       
/* 227 */       List list = vmResourcePathList(paramList);
/* 228 */       vmConfig = new BasicMultiPropertiesConfig((String[])list.toArray((Object[])new String[list.size()]));
/*     */     } 
/* 230 */     return vmConfig;
/*     */   }
/*     */   
/*     */   public static synchronized boolean foundVmConfig() {
/* 234 */     return (vmConfig != null);
/*     */   }
/*     */   
/*     */   public static void dumpByPrefix(MultiPropertiesConfig paramMultiPropertiesConfig, String paramString) {
/* 238 */     Properties properties = paramMultiPropertiesConfig.getPropertiesByPrefix(paramString);
/* 239 */     TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
/* 240 */     treeMap.putAll(properties);
/* 241 */     for (Map.Entry<Object, Object> entry : treeMap.entrySet())
/*     */     {
/*     */       
/* 244 */       System.err.println((new StringBuilder()).append(entry.getKey()).append(" --> ").append(entry.getValue()).toString());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/cfg/ConfigUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */