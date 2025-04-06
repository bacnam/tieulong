/*     */ package com.mchange.v3.hocon;
/*     */ 
/*     */ import com.mchange.v2.cfg.DelayedLogItem;
/*     */ import com.mchange.v2.cfg.PropertiesConfigSource;
/*     */ import com.typesafe.config.Config;
/*     */ import com.typesafe.config.ConfigFactory;
/*     */ import com.typesafe.config.ConfigMergeable;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HoconPropertiesConfigSource
/*     */   implements PropertiesConfigSource
/*     */ {
/*     */   private static Config extractConfig(ClassLoader paramClassLoader, String paramString, List<DelayedLogItem> paramList) throws FileNotFoundException, Exception {
/*  97 */     int i = paramString.indexOf(':');
/*     */     
/*  99 */     ArrayList<Config> arrayList = new ArrayList();
/*     */     
/* 101 */     if (i >= 0 && "hocon".equals(paramString.substring(0, i).toLowerCase())) {
/*     */       
/* 103 */       String str = paramString.substring(i + 1).trim();
/* 104 */       String[] arrayOfString = str.split("\\s*,\\s*");
/*     */       
/* 106 */       for (String str1 : arrayOfString) {
/*     */         String str2, str3;
/*     */ 
/*     */ 
/*     */         
/* 111 */         int k = str1.lastIndexOf('#');
/* 112 */         if (k > 0) {
/*     */           
/* 114 */           str2 = str1.substring(0, k);
/* 115 */           str3 = str1.substring(k + 1).replace('/', '.').trim();
/*     */         }
/*     */         else {
/*     */           
/* 119 */           str2 = str1;
/* 120 */           str3 = null;
/*     */         } 
/*     */         
/* 123 */         Config config1 = null;
/*     */         
/* 125 */         if ("/".equals(str2)) {
/* 126 */           config1 = ConfigFactory.systemProperties();
/*     */         } else {
/*     */           
/* 129 */           Config config2 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 134 */           if ("application".equals(str2) || "/application".equals(str2)) {
/*     */             String str4;
/*     */             
/* 137 */             if ((str4 = System.getProperty("config.resource")) != null) {
/* 138 */               str2 = str4;
/* 139 */             } else if ((str4 = System.getProperty("config.file")) != null) {
/*     */               
/* 141 */               File file = new File(str4);
/* 142 */               if (file.exists()) {
/*     */                 
/* 144 */                 if (file.canRead()) {
/* 145 */                   config2 = ConfigFactory.parseFile(file);
/*     */                 } else {
/* 147 */                   paramList.add(new DelayedLogItem(DelayedLogItem.Level.WARNING, String.format("Specified config.file '%s' is not readable. Falling back to standard application.(conf|json|properties).}", new Object[] { file.getAbsolutePath() })));
/*     */                 } 
/*     */               } else {
/*     */                 
/* 151 */                 paramList.add(new DelayedLogItem(DelayedLogItem.Level.WARNING, String.format("Specified config.file '%s' does not exist. Falling back to standard application.(conf|json|properties).}", new Object[] { file.getAbsolutePath() })));
/*     */               }
/*     */             
/*     */             }
/* 155 */             else if ((str4 = System.getProperty("config.url")) != null) {
/* 156 */               config2 = ConfigFactory.parseURL(new URL(str4));
/*     */             } 
/*     */           } 
/* 159 */           if (config2 == null) {
/*     */             
/* 161 */             if (str2.charAt(0) == '/') {
/* 162 */               str2 = str2.substring(1);
/*     */             }
/* 164 */             boolean bool = (str2.indexOf('.') >= 0) ? true : false;
/*     */             
/* 166 */             if (bool) {
/* 167 */               config2 = ConfigFactory.parseResources(paramClassLoader, str2);
/*     */             } else {
/* 169 */               config2 = ConfigFactory.parseResourcesAnySyntax(paramClassLoader, str2);
/*     */             } 
/*     */           } 
/* 172 */           if (config2.isEmpty()) {
/* 173 */             paramList.add(new DelayedLogItem(DelayedLogItem.Level.FINE, String.format("Missing or empty HOCON configuration for resource path '%s'.", new Object[] { str2 })));
/*     */           } else {
/* 175 */             config1 = config2;
/*     */           } 
/*     */         } 
/* 178 */         if (config1 != null) {
/*     */           
/* 180 */           if (str3 != null) {
/* 181 */             config1 = config1.getConfig(str3);
/*     */           }
/* 183 */           arrayList.add(config1);
/*     */         } 
/*     */       } 
/*     */       
/* 187 */       if (arrayList.size() == 0) {
/* 188 */         throw new FileNotFoundException(String.format("Could not find HOCON configuration at any of the listed resources in '%s'", new Object[] { paramString }));
/*     */       }
/*     */       
/* 191 */       Config config = ConfigFactory.empty();
/* 192 */       for (int j = arrayList.size(); --j >= 0;)
/* 193 */         config = config.withFallback((ConfigMergeable)arrayList.get(j)); 
/* 194 */       return config.resolve();
/*     */     } 
/*     */ 
/*     */     
/* 198 */     throw new IllegalArgumentException(String.format("Invalid resource identifier for hocon config file: '%s'", new Object[] { paramString }));
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertiesConfigSource.Parse propertiesFromSource(ClassLoader paramClassLoader, String paramString) throws FileNotFoundException, Exception {
/* 203 */     LinkedList<DelayedLogItem> linkedList = new LinkedList();
/*     */     
/* 205 */     Config config = extractConfig(paramClassLoader, paramString, linkedList);
/* 206 */     HoconUtils.PropertiesConversion propertiesConversion = HoconUtils.configToProperties(config);
/*     */     
/* 208 */     for (String str : propertiesConversion.unrenderable) {
/* 209 */       linkedList.add(new DelayedLogItem(DelayedLogItem.Level.FINE, String.format("Value at path '%s' could not be converted to a String. Skipping.", new Object[] { str })));
/*     */     } 
/* 211 */     return new PropertiesConfigSource.Parse(propertiesConversion.properties, linkedList);
/*     */   }
/*     */   
/*     */   public PropertiesConfigSource.Parse propertiesFromSource(String paramString) throws FileNotFoundException, Exception {
/* 215 */     return propertiesFromSource(HoconPropertiesConfigSource.class.getClassLoader(), paramString);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v3/hocon/HoconPropertiesConfigSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */