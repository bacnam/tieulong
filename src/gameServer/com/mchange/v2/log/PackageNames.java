/*    */ package com.mchange.v2.log;
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
/*    */ public class PackageNames
/*    */   implements NameTransformer
/*    */ {
/*    */   public String transformName(String paramString) {
/* 41 */     return null;
/*    */   }
/*    */   
/*    */   public String transformName(Class paramClass) {
/* 45 */     String str = paramClass.getName();
/* 46 */     int i = str.lastIndexOf('.');
/* 47 */     if (i <= 0) {
/* 48 */       return "";
/*    */     }
/* 50 */     return str.substring(0, i);
/*    */   }
/*    */   
/*    */   public String transformName() {
/* 54 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/log/PackageNames.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */