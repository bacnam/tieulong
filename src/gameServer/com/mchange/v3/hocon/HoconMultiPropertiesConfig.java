/*     */ package com.mchange.v3.hocon;
/*     */ 
/*     */ import com.mchange.v2.cfg.DelayedLogItem;
/*     */ import com.mchange.v2.cfg.MultiPropertiesConfig;
/*     */ import com.typesafe.config.Config;
/*     */ import com.typesafe.config.ConfigList;
/*     */ import com.typesafe.config.ConfigValue;
/*     */ import com.typesafe.config.ConfigValueType;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
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
/*     */ public class HoconMultiPropertiesConfig
/*     */   extends MultiPropertiesConfig
/*     */ {
/*     */   String quasiResourcePath;
/*     */   Properties props;
/*  50 */   List<DelayedLogItem> delayedLogItems = new LinkedList<DelayedLogItem>();
/*     */ 
/*     */   
/*  53 */   Map<String, Properties> propsByPrefix = new HashMap<String, Properties>();
/*     */ 
/*     */   
/*     */   public HoconMultiPropertiesConfig(String paramString, Config paramConfig) {
/*  57 */     this.quasiResourcePath = paramString;
/*  58 */     this.props = propsForConfig(paramConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   private Properties propsForConfig(Config paramConfig) {
/*  63 */     Properties properties = new Properties();
/*  64 */     for (Map.Entry entry : paramConfig.entrySet()) {
/*     */       
/*     */       try {
/*  67 */         properties.put(entry.getKey(), asSimpleString((ConfigValue)entry.getValue()));
/*  68 */       } catch (IllegalArgumentException illegalArgumentException) {
/*  69 */         this.delayedLogItems.add(new DelayedLogItem(DelayedLogItem.Level.FINE, "For property '" + (String)entry.getKey() + "', " + illegalArgumentException.getMessage()));
/*     */       } 
/*  71 */     }  return properties; } private static String asSimpleString(ConfigValue paramConfigValue) throws IllegalArgumentException {
/*     */     ConfigList<ConfigValue> configList;
/*     */     StringBuilder stringBuilder;
/*     */     byte b;
/*     */     int i;
/*  76 */     ConfigValueType configValueType = paramConfigValue.valueType();
/*  77 */     switch (configValueType) {
/*     */       
/*     */       case BOOLEAN:
/*     */       case NUMBER:
/*     */       case STRING:
/*  82 */         return String.valueOf(paramConfigValue.unwrapped());
/*     */       case LIST:
/*  84 */         configList = (ConfigList)paramConfigValue;
/*  85 */         for (ConfigValue configValue : configList) {
/*  86 */           if (!isSimple(configValue))
/*  87 */             throw new IllegalArgumentException("value is a complex list, could not be rendered as a simple property: " + paramConfigValue); 
/*  88 */         }  stringBuilder = new StringBuilder();
/*  89 */         for (b = 0, i = configList.size(); b < i; b++) {
/*     */           
/*  91 */           if (b != 0) stringBuilder.append(','); 
/*  92 */           stringBuilder.append(asSimpleString(configList.get(b)));
/*     */         } 
/*  94 */         return stringBuilder.toString();
/*     */       case OBJECT:
/*  96 */         throw new IllegalArgumentException("value is a ConfigValue object rather than an atom or list of atoms: " + paramConfigValue);
/*     */       case NULL:
/*  98 */         throw new IllegalArgumentException("value is a null; will be excluded from the MultiPropertiesConfig: " + paramConfigValue);
/*     */     } 
/* 100 */     throw new IllegalArgumentException("value of an unexpected type: (value->" + paramConfigValue + ", type->" + configValueType + ")");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isSimple(ConfigValue paramConfigValue) {
/* 106 */     ConfigValueType configValueType = paramConfigValue.valueType();
/* 107 */     switch (configValueType) {
/*     */       
/*     */       case BOOLEAN:
/*     */       case NUMBER:
/*     */       case STRING:
/* 112 */         return true;
/*     */     } 
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getPropertiesResourcePaths() {
/* 120 */     return new String[] { this.quasiResourcePath };
/*     */   }
/*     */ 
/*     */   
/*     */   public Properties getPropertiesByResourcePath(String paramString) {
/* 125 */     if (paramString.equals(this.quasiResourcePath)) {
/*     */       
/* 127 */       Properties properties = new Properties();
/* 128 */       properties.putAll(this.props);
/* 129 */       return properties;
/*     */     } 
/*     */     
/* 132 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Properties getPropertiesByPrefix(String paramString) {
/* 138 */     Properties properties = this.propsByPrefix.get(paramString);
/* 139 */     if (properties == null) {
/*     */       
/* 141 */       properties = new Properties();
/*     */       
/* 143 */       if ("".equals(paramString)) {
/* 144 */         properties.putAll(this.props);
/*     */       } else {
/*     */         
/* 147 */         String str = paramString + '.';
/* 148 */         for (Map.Entry<Object, Object> entry : this.props.entrySet()) {
/*     */           
/* 150 */           String str1 = (String)entry.getKey();
/* 151 */           if (str1.startsWith(str)) {
/* 152 */             properties.put(str1, entry.getValue());
/*     */           }
/*     */         } 
/*     */       } 
/* 156 */       this.propsByPrefix.put(paramString, properties);
/*     */     } 
/* 158 */     return properties;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getProperty(String paramString) {
/* 163 */     return (String)this.props.get(paramString);
/*     */   }
/*     */   
/*     */   public List getDelayedLogItems() {
/* 167 */     return this.delayedLogItems;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v3/hocon/HoconMultiPropertiesConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */