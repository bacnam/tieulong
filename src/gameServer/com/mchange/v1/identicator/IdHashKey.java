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
/*    */ abstract class IdHashKey
/*    */ {
/*    */   Identicator id;
/*    */   
/*    */   public IdHashKey(Identicator paramIdenticator) {
/* 43 */     this.id = paramIdenticator;
/*    */   }
/*    */ 
/*    */   
/*    */   public Identicator getIdenticator() {
/* 48 */     return this.id;
/*    */   }
/*    */   
/*    */   public abstract Object getKeyObj();
/*    */   
/*    */   public abstract boolean equals(Object paramObject);
/*    */   
/*    */   public abstract int hashCode();
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/identicator/IdHashKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */