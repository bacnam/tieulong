/*    */ package com.mchange.v1.db.sql;
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
/*    */ class DbAuth
/*    */ {
/*    */   String username;
/*    */   String password;
/*    */   
/*    */   public DbAuth(String paramString1, String paramString2) {
/* 48 */     this.username = paramString1;
/* 49 */     this.password = paramString2;
/*    */   }
/*    */   
/*    */   public String getUsername() {
/* 53 */     return this.username;
/*    */   }
/*    */   public String getPassword() {
/* 56 */     return this.password;
/*    */   }
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 60 */     if (paramObject != null && getClass() == paramObject.getClass()) {
/*    */       
/* 62 */       DbAuth dbAuth = (DbAuth)paramObject;
/* 63 */       return (this.username.equals(dbAuth.username) && this.password.equals(dbAuth.password));
/*    */     } 
/*    */ 
/*    */     
/* 67 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 71 */     return this.username.hashCode() ^ this.password.hashCode();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/DbAuth.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */