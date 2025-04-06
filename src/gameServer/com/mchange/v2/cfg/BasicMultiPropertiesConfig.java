/*     */ package com.mchange.v2.cfg;
/*     */ 
/*     */ import com.mchange.v3.hocon.HoconPropertiesConfigSource;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
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
/*     */ final class BasicMultiPropertiesConfig
/*     */   extends MultiPropertiesConfig
/*     */ {
/*     */   private static final String HOCON_CFG_CNAME = "com.typesafe.config.Config";
/*     */   private static final int HOCON_PFX_LEN = 6;
/*     */   String[] rps;
/*     */   Map propsByResourcePaths;
/*     */   Map propsByPrefixes;
/*  50 */   static final BasicMultiPropertiesConfig EMPTY = new BasicMultiPropertiesConfig();
/*     */   List parseMessages;
/*     */   Properties propsByKey;
/*     */   
/*     */   static final class SystemPropertiesConfigSource implements PropertiesConfigSource {
/*     */     public PropertiesConfigSource.Parse propertiesFromSource(String param1String) throws FileNotFoundException, Exception {
/*  56 */       if ("/".equals(param1String)) {
/*  57 */         return new PropertiesConfigSource.Parse((Properties)System.getProperties().clone(), Collections.emptyList());
/*     */       }
/*  59 */       throw new Exception(String.format("Unexpected identifier for System properties: '%s'", new Object[] { param1String }));
/*     */     }
/*     */   }
/*     */   
/*     */   static boolean isHoconPath(String paramString) {
/*  64 */     return (paramString.length() > 6 && paramString.substring(0, 6).toLowerCase().equals("hocon:"));
/*     */   }
/*     */   
/*     */   private static PropertiesConfigSource configSource(String paramString) throws Exception {
/*  68 */     boolean bool = isHoconPath(paramString);
/*     */     
/*  70 */     if (!bool && !paramString.startsWith("/")) {
/*  71 */       throw new IllegalArgumentException(String.format("Resource identifier '%s' is neither an absolute resource path nor a HOCON path. (Resource paths should be specified beginning with '/' or 'hocon:/')", new Object[] { paramString }));
/*     */     }
/*  73 */     if (bool) {
/*     */       
/*     */       try {
/*     */         
/*  77 */         Class.forName("com.typesafe.config.Config");
/*  78 */         return (PropertiesConfigSource)new HoconPropertiesConfigSource();
/*     */       }
/*  80 */       catch (ClassNotFoundException classNotFoundException) {
/*     */ 
/*     */         
/*  83 */         int i = paramString.lastIndexOf('#');
/*  84 */         String str = (i > 0) ? paramString.substring(6, i) : paramString.substring(6);
/*  85 */         if (BasicMultiPropertiesConfig.class.getResource(str) == null) {
/*  86 */           throw new FileNotFoundException(String.format("HOCON lib (typesafe-config) is not available. Also, no resource available at '%s' for HOCON identifier '%s'.", new Object[] { str, paramString }));
/*     */         }
/*  88 */         throw new Exception(String.format("Could not decode HOCON resource '%s', even though the resource exists, because HOCON lib (typesafe-config) is not available.", new Object[] { paramString }), classNotFoundException);
/*     */       } 
/*     */     }
/*  91 */     if ("/".equals(paramString)) {
/*  92 */       return new SystemPropertiesConfigSource();
/*     */     }
/*  94 */     return new BasicPropertiesConfigSource();
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
/*     */   public BasicMultiPropertiesConfig(String[] paramArrayOfString) {
/* 106 */     this(paramArrayOfString, (List)null);
/*     */   }
/*     */   
/*     */   BasicMultiPropertiesConfig(String[] paramArrayOfString, List paramList) {
/* 110 */     firstInit(paramArrayOfString, paramList);
/* 111 */     finishInit(paramList);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicMultiPropertiesConfig(String paramString, Properties paramProperties) {
/* 133 */     this(new String[] { paramString }, resourcePathToPropertiesMap(paramString, paramProperties), Collections.emptyList());
/*     */   }
/*     */   
/*     */   private static Map resourcePathToPropertiesMap(String paramString, Properties paramProperties) {
/* 137 */     HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
/* 138 */     hashMap.put(paramString, paramProperties);
/* 139 */     return hashMap;
/*     */   }
/*     */ 
/*     */   
/*     */   BasicMultiPropertiesConfig(String[] paramArrayOfString, Map paramMap, List paramList) {
/* 144 */     this.rps = paramArrayOfString;
/* 145 */     this.propsByResourcePaths = paramMap;
/*     */     
/* 147 */     ArrayList arrayList = new ArrayList();
/* 148 */     arrayList.addAll(paramList);
/* 149 */     finishInit(arrayList);
/*     */     
/* 151 */     this.parseMessages = arrayList;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private BasicMultiPropertiesConfig() {
/* 157 */     this.rps = new String[0];
/* 158 */     Map<?, ?> map1 = Collections.emptyMap();
/* 159 */     Map<?, ?> map2 = Collections.emptyMap();
/*     */     
/* 161 */     List<?> list = Collections.emptyList();
/*     */     
/* 163 */     Properties properties = new Properties();
/*     */   }
/*     */ 
/*     */   
/*     */   private void firstInit(String[] paramArrayOfString, List<DelayedLogItem> paramList) {
/* 168 */     boolean bool = false;
/* 169 */     if (paramList == null) {
/*     */       
/* 171 */       paramList = new ArrayList();
/* 172 */       bool = true;
/*     */     } 
/*     */     
/* 175 */     HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
/* 176 */     ArrayList<String> arrayList = new ArrayList(); byte b;
/*     */     int i;
/* 178 */     for (b = 0, i = paramArrayOfString.length; b < i; b++) {
/*     */       
/* 180 */       String str = paramArrayOfString[b];
/*     */ 
/*     */       
/*     */       try {
/* 184 */         PropertiesConfigSource propertiesConfigSource = configSource(str);
/* 185 */         PropertiesConfigSource.Parse parse = propertiesConfigSource.propertiesFromSource(str);
/* 186 */         hashMap.put(str, parse.getProperties());
/* 187 */         arrayList.add(str);
/* 188 */         paramList.addAll(parse.getDelayedLogItems());
/*     */       }
/* 190 */       catch (FileNotFoundException fileNotFoundException) {
/* 191 */         paramList.add(new DelayedLogItem(DelayedLogItem.Level.FINE, String.format("The configuration file for resource identifier '%s' could not be found. Skipping.", new Object[] { str }), fileNotFoundException));
/* 192 */       } catch (Exception exception) {
/* 193 */         paramList.add(new DelayedLogItem(DelayedLogItem.Level.WARNING, String.format("An Exception occurred while trying to read configuration data at resource identifier '%s'.", new Object[] { str }), exception));
/*     */       } 
/*     */     } 
/* 196 */     this.rps = arrayList.<String>toArray(new String[arrayList.size()]);
/* 197 */     this.propsByResourcePaths = Collections.unmodifiableMap(hashMap);
/* 198 */     this.parseMessages = Collections.unmodifiableList(paramList);
/*     */     
/* 200 */     if (bool) {
/* 201 */       dumpToSysErr(paramList);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void finishInit(List paramList) {
/* 209 */     boolean bool = false;
/* 210 */     if (paramList == null) {
/*     */       
/* 212 */       paramList = new ArrayList();
/* 213 */       bool = true;
/*     */     } 
/*     */     
/* 216 */     this.propsByPrefixes = Collections.unmodifiableMap(extractPrefixMapFromRsrcPathMap(this.rps, this.propsByResourcePaths, paramList));
/* 217 */     this.propsByKey = extractPropsByKey(this.rps, this.propsByResourcePaths, paramList);
/*     */     
/* 219 */     if (bool)
/* 220 */       dumpToSysErr(paramList); 
/*     */   }
/*     */   
/*     */   public List getDelayedLogItems() {
/* 224 */     return this.parseMessages;
/*     */   }
/*     */   
/*     */   private static void dumpToSysErr(List paramList) {
/* 228 */     for (Object object : paramList) {
/* 229 */       System.err.println(object);
/*     */     }
/*     */   }
/*     */   
/*     */   private static String extractPrefix(String paramString) {
/* 234 */     int i = paramString.lastIndexOf('.');
/* 235 */     if (i < 0) {
/*     */       
/* 237 */       if ("".equals(paramString)) {
/* 238 */         return null;
/*     */       }
/* 240 */       return "";
/*     */     } 
/*     */     
/* 243 */     return paramString.substring(0, i);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Properties findProps(String paramString, Map paramMap) {
/* 267 */     return (Properties)paramMap.get(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Properties extractPropsByKey(String[] paramArrayOfString, Map paramMap, List<DelayedLogItem> paramList) {
/* 276 */     Properties properties = new Properties(); byte b; int i;
/* 277 */     for (b = 0, i = paramArrayOfString.length; b < i; b++) {
/*     */       
/* 279 */       String str = paramArrayOfString[b];
/* 280 */       Properties properties1 = findProps(str, paramMap);
/* 281 */       if (properties1 == null) {
/*     */         
/* 283 */         paramList.add(new DelayedLogItem(DelayedLogItem.Level.WARNING, BasicMultiPropertiesConfig.class.getName() + ".extractPropsByKey(): Could not find loaded properties for resource path: " + str));
/*     */       }
/*     */       else {
/*     */         
/* 287 */         for (String str1 : properties1.keySet()) {
/*     */ 
/*     */           
/* 290 */           if (!(str1 instanceof String)) {
/*     */             
/* 292 */             String str4 = BasicMultiPropertiesConfig.class.getName() + ": " + "Properties object found at resource path " + ("/".equals(str) ? "[system properties]" : ("'" + str + "'")) + "' contains a key that is not a String: " + str1 + "; Skipping...";
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
/* 310 */             paramList.add(new DelayedLogItem(DelayedLogItem.Level.WARNING, str4));
/*     */             continue;
/*     */           } 
/* 313 */           Object object = properties1.get(str1);
/* 314 */           if (object != null && !(object instanceof String)) {
/*     */             
/* 316 */             String str4 = BasicMultiPropertiesConfig.class.getName() + ": " + "Properties object found at resource path " + ("/".equals(str) ? "[system properties]" : ("'" + str + "'")) + " contains a value that is not a String: " + object + "; Skipping...";
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
/* 334 */             paramList.add(new DelayedLogItem(DelayedLogItem.Level.WARNING, str4));
/*     */             
/*     */             continue;
/*     */           } 
/* 338 */           String str2 = str1;
/* 339 */           String str3 = (String)object;
/* 340 */           properties.put(str2, str3);
/*     */         } 
/*     */       } 
/* 343 */     }  return properties;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Map extractPrefixMapFromRsrcPathMap(String[] paramArrayOfString, Map paramMap, List<DelayedLogItem> paramList) {
/* 348 */     HashMap<Object, Object> hashMap = new HashMap<Object, Object>(); byte b;
/*     */     int i;
/* 350 */     for (b = 0, i = paramArrayOfString.length; b < i; b++) {
/*     */       
/* 352 */       String str = paramArrayOfString[b];
/* 353 */       Properties properties = findProps(str, paramMap);
/* 354 */       if (properties == null) {
/*     */         
/* 356 */         String str1 = BasicMultiPropertiesConfig.class.getName() + ".extractPrefixMapFromRsrcPathMap(): Could not find loaded properties for resource path: " + str;
/*     */         
/* 358 */         paramList.add(new DelayedLogItem(DelayedLogItem.Level.WARNING, str1));
/*     */       } else {
/*     */         
/* 361 */         for (String str1 : properties.keySet()) {
/*     */ 
/*     */           
/* 364 */           if (!(str1 instanceof String)) {
/*     */             
/* 366 */             String str4 = BasicMultiPropertiesConfig.class.getName() + ": " + "Properties object found at resource path " + ("/".equals(str) ? "[system properties]" : ("'" + str + "'")) + "' contains a key that is not a String: " + str1 + "; Skipping...";
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
/* 385 */             paramList.add(new DelayedLogItem(DelayedLogItem.Level.WARNING, str4));
/*     */             
/*     */             continue;
/*     */           } 
/* 389 */           String str2 = str1;
/* 390 */           String str3 = extractPrefix(str2);
/* 391 */           while (str3 != null) {
/*     */             
/* 393 */             Properties properties1 = (Properties)hashMap.get(str3);
/* 394 */             if (properties1 == null) {
/*     */               
/* 396 */               properties1 = new Properties();
/* 397 */               hashMap.put(str3, properties1);
/*     */             } 
/* 399 */             properties1.put(str2, properties.get(str2));
/*     */             
/* 401 */             str3 = extractPrefix(str3);
/*     */           } 
/*     */         } 
/*     */       } 
/* 405 */     }  return hashMap;
/*     */   }
/*     */   
/*     */   public String[] getPropertiesResourcePaths() {
/* 409 */     return (String[])this.rps.clone();
/*     */   }
/*     */   
/*     */   public Properties getPropertiesByResourcePath(String paramString) {
/* 413 */     Properties properties = (Properties)this.propsByResourcePaths.get(paramString);
/* 414 */     return (properties == null) ? new Properties() : properties;
/*     */   }
/*     */ 
/*     */   
/*     */   public Properties getPropertiesByPrefix(String paramString) {
/* 419 */     Properties properties = (Properties)this.propsByPrefixes.get(paramString);
/* 420 */     return (properties == null) ? new Properties() : properties;
/*     */   }
/*     */   
/*     */   public String getProperty(String paramString) {
/* 424 */     return this.propsByKey.getProperty(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String dump() {
/* 431 */     return String.format("[ propertiesByResourcePaths -> %s, propertiesByPrefixes -> %s ]", new Object[] { this.propsByResourcePaths, this.propsByPrefixes });
/*     */   }
/*     */   public String toString() {
/* 434 */     return super.toString() + " " + dump();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/cfg/BasicMultiPropertiesConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */