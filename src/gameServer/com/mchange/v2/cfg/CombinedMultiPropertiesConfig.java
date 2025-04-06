/*     */ package com.mchange.v2.cfg;
/*     */ 
/*     */ import java.util.Collections;
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
/*     */ 
/*     */ class CombinedMultiPropertiesConfig
/*     */   extends MultiPropertiesConfig
/*     */ {
/*     */   MultiPropertiesConfig[] configs;
/*     */   String[] resourcePaths;
/*     */   List parseMessages;
/*     */   
/*     */   CombinedMultiPropertiesConfig(MultiPropertiesConfig[] paramArrayOfMultiPropertiesConfig) {
/*  49 */     this.configs = paramArrayOfMultiPropertiesConfig;
/*     */     
/*  51 */     LinkedList<String> linkedList = new LinkedList();
/*     */     
/*  53 */     for (int i = paramArrayOfMultiPropertiesConfig.length - 1; i >= 0; i--) {
/*     */       
/*  55 */       String[] arrayOfString = paramArrayOfMultiPropertiesConfig[i].getPropertiesResourcePaths();
/*  56 */       for (int k = arrayOfString.length - 1; k >= 0; k--) {
/*     */         
/*  58 */         String str = arrayOfString[k];
/*  59 */         if (!linkedList.contains(str))
/*  60 */           linkedList.add(0, str); 
/*     */       } 
/*     */     } 
/*  63 */     this.resourcePaths = linkedList.<String>toArray(new String[linkedList.size()]);
/*     */     
/*  65 */     LinkedList<?> linkedList1 = new LinkedList(); byte b; int j;
/*  66 */     for (b = 0, j = paramArrayOfMultiPropertiesConfig.length; b < j; b++)
/*  67 */       linkedList1.addAll(paramArrayOfMultiPropertiesConfig[b].getDelayedLogItems()); 
/*  68 */     this.parseMessages = Collections.unmodifiableList(linkedList1);
/*     */   }
/*     */ 
/*     */   
/*     */   private Map getPropsByResourcePaths() {
/*  73 */     HashMap<Object, Object> hashMap = new HashMap<Object, Object>(); byte b; int i;
/*  74 */     for (b = 0, i = this.resourcePaths.length; b < i; b++) {
/*     */       
/*  76 */       String str = this.resourcePaths[b];
/*  77 */       hashMap.put(str, getPropertiesByResourcePath(str));
/*     */     } 
/*  79 */     return Collections.unmodifiableMap(hashMap);
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicMultiPropertiesConfig toBasic() {
/*  84 */     String[] arrayOfString = getPropertiesResourcePaths();
/*  85 */     Map map = getPropsByResourcePaths();
/*  86 */     List list = getDelayedLogItems();
/*     */     
/*  88 */     return new BasicMultiPropertiesConfig(arrayOfString, map, list);
/*     */   }
/*     */   
/*     */   public String[] getPropertiesResourcePaths() {
/*  92 */     return (String[])this.resourcePaths.clone();
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
/*     */   public Properties getPropertiesByResourcePath(String paramString) {
/* 107 */     Properties properties = new Properties(); byte b; int i;
/* 108 */     for (b = 0, i = this.configs.length; b < i; b++) {
/*     */       
/* 110 */       MultiPropertiesConfig multiPropertiesConfig = this.configs[b];
/* 111 */       Properties properties1 = multiPropertiesConfig.getPropertiesByResourcePath(paramString);
/* 112 */       if (properties1 != null) properties.putAll(properties1); 
/*     */     } 
/* 114 */     return (properties.size() > 0) ? properties : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Properties getPropertiesByPrefix(String paramString) {
/* 119 */     LinkedList<Map.Entry<Object, Object>> linkedList = new LinkedList();
/* 120 */     for (int i = this.configs.length - 1; i >= 0; i--) {
/*     */       
/* 122 */       MultiPropertiesConfig multiPropertiesConfig = this.configs[i];
/* 123 */       Properties properties1 = multiPropertiesConfig.getPropertiesByPrefix(paramString);
/* 124 */       if (properties1 != null)
/* 125 */         linkedList.addAll(0, properties1.entrySet()); 
/*     */     } 
/* 127 */     if (linkedList.size() == 0) {
/* 128 */       return null;
/*     */     }
/*     */     
/* 131 */     Properties properties = new Properties();
/* 132 */     for (Map.Entry<Object, Object> entry : linkedList)
/*     */     {
/*     */       
/* 135 */       properties.put(entry.getKey(), entry.getValue());
/*     */     }
/* 137 */     return properties;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getProperty(String paramString) {
/* 143 */     for (int i = this.configs.length - 1; i >= 0; i--) {
/*     */       
/* 145 */       MultiPropertiesConfig multiPropertiesConfig = this.configs[i];
/* 146 */       String str = multiPropertiesConfig.getProperty(paramString);
/* 147 */       if (str != null)
/* 148 */         return str; 
/*     */     } 
/* 150 */     return null;
/*     */   }
/*     */   
/*     */   public List getDelayedLogItems() {
/* 154 */     return this.parseMessages;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/cfg/CombinedMultiPropertiesConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */