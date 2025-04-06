/*    */ package com.mchange.v2.cfg;
/*    */ 
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.LinkedList;
/*    */ import java.util.Properties;
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
/*    */ public final class BasicPropertiesConfigSource
/*    */   implements PropertiesConfigSource
/*    */ {
/*    */   public PropertiesConfigSource.Parse propertiesFromSource(String paramString) throws FileNotFoundException, Exception {
/* 47 */     InputStream inputStream = MultiPropertiesConfig.class.getResourceAsStream(paramString);
/* 48 */     if (inputStream != null) {
/*    */       
/* 50 */       BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
/* 51 */       Properties properties = new Properties();
/* 52 */       LinkedList<DelayedLogItem> linkedList = new LinkedList();
/*    */       try {
/* 54 */         properties.load(bufferedInputStream);
/*    */       } finally {
/*    */         try {
/* 57 */           if (bufferedInputStream != null) bufferedInputStream.close(); 
/* 58 */         } catch (IOException iOException) {
/* 59 */           linkedList.add(new DelayedLogItem(DelayedLogItem.Level.WARNING, "An IOException occurred while closing InputStream from resource path '" + paramString + "'.", iOException));
/*    */         } 
/* 61 */       }  return new PropertiesConfigSource.Parse(properties, linkedList);
/*    */     } 
/*    */     
/* 64 */     throw new FileNotFoundException(String.format("Resource not found at path '%s'.", new Object[] { paramString }));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/cfg/BasicPropertiesConfigSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */