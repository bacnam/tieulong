/*    */ package com.mchange.util.impl;
/*    */ 
/*    */ import com.mchange.util.PasswordManager;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
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
/*    */ public class PlaintextPropertiesPasswordManager
/*    */   implements PasswordManager
/*    */ {
/*    */   private static final String PASSWORD_PROP_PFX = "password.";
/*    */   private static final String HEADER = "com.mchange.util.impl.PlaintextPropertiesPasswordManager data";
/*    */   SyncedProperties props;
/*    */   
/*    */   public PlaintextPropertiesPasswordManager(File paramFile) throws IOException {
/* 50 */     this.props = new SyncedProperties(paramFile, "com.mchange.util.impl.PlaintextPropertiesPasswordManager data");
/*    */   }
/*    */   public boolean validate(String paramString1, String paramString2) throws IOException {
/* 53 */     return paramString2.equals(this.props.getProperty("password." + paramString1));
/*    */   }
/*    */   
/*    */   public boolean updatePassword(String paramString1, String paramString2, String paramString3) throws IOException {
/* 57 */     if (!validate(paramString1, paramString2)) return false; 
/* 58 */     this.props.put("password." + paramString1, paramString3);
/* 59 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/impl/PlaintextPropertiesPasswordManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */