/*    */ package com.mchange.v2.cfg;
/*    */ 
/*    */ import java.io.FileNotFoundException;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface PropertiesConfigSource
/*    */ {
/*    */   Parse propertiesFromSource(String paramString) throws FileNotFoundException, Exception;
/*    */   
/*    */   public static class Parse
/*    */   {
/*    */     private Properties properties;
/*    */     private List<DelayedLogItem> parseMessages;
/*    */     
/*    */     public Properties getProperties() {
/* 58 */       return this.properties; } public List<DelayedLogItem> getDelayedLogItems() {
/* 59 */       return this.parseMessages;
/*    */     }
/*    */     
/*    */     public Parse(Properties param1Properties, List<DelayedLogItem> param1List) {
/* 63 */       this.properties = param1Properties;
/* 64 */       this.parseMessages = param1List;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/cfg/PropertiesConfigSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */