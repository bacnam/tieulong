/*    */ package com.mchange.v2.c3p0.impl;
/*    */ 
/*    */ import java.util.Enumeration;
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
/*    */ public class AuthMaskingProperties
/*    */   extends Properties
/*    */ {
/*    */   public AuthMaskingProperties() {}
/*    */   
/*    */   public AuthMaskingProperties(Properties p) {
/* 47 */     super(p);
/*    */   }
/*    */   
/*    */   public static AuthMaskingProperties fromAnyProperties(Properties p) {
/* 51 */     AuthMaskingProperties out = new AuthMaskingProperties();
/* 52 */     for (Enumeration<?> e = p.propertyNames(); e.hasMoreElements(); ) {
/*    */       
/* 54 */       String key = (String)e.nextElement();
/* 55 */       out.setProperty(key, p.getProperty(key));
/*    */     } 
/* 57 */     return out;
/*    */   }
/*    */   
/*    */   private String normalToString() {
/* 61 */     return super.toString();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 65 */     boolean hasUser = (get("user") != null);
/* 66 */     boolean hasPassword = (get("password") != null);
/* 67 */     if (hasUser || hasPassword) {
/*    */       
/* 69 */       AuthMaskingProperties clone = (AuthMaskingProperties)clone();
/* 70 */       if (hasUser)
/* 71 */         clone.put("user", "******"); 
/* 72 */       if (hasPassword)
/* 73 */         clone.put("password", "******"); 
/* 74 */       return clone.normalToString();
/*    */     } 
/*    */     
/* 77 */     return normalToString();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/AuthMaskingProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */