/*    */ package com.mchange.v3.hocon;
/*    */ 
/*    */ import com.typesafe.config.Config;
/*    */ import com.typesafe.config.ConfigException;
/*    */ import java.util.HashSet;
/*    */ import java.util.Map;
/*    */ import java.util.Properties;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class HoconUtils
/*    */ {
/*    */   public static class PropertiesConversion
/*    */   {
/*    */     Properties properties;
/*    */     Set<String> unrenderable;
/*    */   }
/*    */   
/*    */   public static PropertiesConversion configToProperties(Config paramConfig) {
/* 52 */     Set set = paramConfig.entrySet();
/*    */     
/* 54 */     Properties properties = new Properties();
/* 55 */     HashSet<String> hashSet = new HashSet();
/*    */     
/* 57 */     for (Map.Entry entry : set) {
/*    */       
/* 59 */       String str1 = (String)entry.getKey();
/* 60 */       String str2 = null;
/*    */       try {
/* 62 */         str2 = paramConfig.getString(str1);
/* 63 */       } catch (com.typesafe.config.ConfigException.WrongType wrongType) {
/* 64 */         hashSet.add(str1);
/*    */       } 
/* 66 */       if (str2 != null) {
/* 67 */         properties.setProperty(str1, str2);
/*    */       }
/*    */     } 
/* 70 */     PropertiesConversion propertiesConversion = new PropertiesConversion();
/* 71 */     propertiesConversion.properties = properties;
/* 72 */     propertiesConversion.unrenderable = hashSet;
/* 73 */     return propertiesConversion;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v3/hocon/HoconUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */