/*    */ package com.mchange.v1.identicator;
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
/*    */ final class StrongIdHashKey
/*    */   extends IdHashKey
/*    */ {
/*    */   Object keyObj;
/*    */   
/*    */   public StrongIdHashKey(Object paramObject, Identicator paramIdenticator) {
/* 46 */     super(paramIdenticator);
/* 47 */     this.keyObj = paramObject;
/*    */   }
/*    */   
/*    */   public Object getKeyObj() {
/* 51 */     return this.keyObj;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 56 */     if (paramObject instanceof StrongIdHashKey) {
/* 57 */       return this.id.identical(this.keyObj, ((StrongIdHashKey)paramObject).keyObj);
/*    */     }
/* 59 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 63 */     return this.id.hash(this.keyObj);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/identicator/StrongIdHashKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */