/*    */ package com.jolbox.bonecp;
/*    */ 
/*    */ import com.google.common.base.Objects;
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
/*    */ public class UsernamePassword
/*    */ {
/*    */   private String username;
/*    */   private String password;
/*    */   
/*    */   public UsernamePassword(String username, String password) {
/* 37 */     this.username = username;
/* 38 */     this.password = password;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUsername() {
/* 46 */     return this.username;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getPassword() {
/* 54 */     return this.password;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 60 */     if (obj instanceof UsernamePassword) {
/* 61 */       UsernamePassword that = (UsernamePassword)obj;
/* 62 */       return (Objects.equal(this.username, that.getUsername()) && Objects.equal(this.password, that.getPassword()));
/*    */     } 
/*    */     
/* 65 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 70 */     return Objects.hashCode(new Object[] { this.username, this.password });
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/UsernamePassword.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */