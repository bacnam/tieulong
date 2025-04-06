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
/*    */ public class StrongIdentityIdenticator
/*    */   implements Identicator
/*    */ {
/*    */   public boolean identical(Object paramObject1, Object paramObject2) {
/* 41 */     return (paramObject1 == paramObject2);
/*    */   }
/*    */   public int hash(Object paramObject) {
/* 44 */     return System.identityHashCode(paramObject);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/identicator/StrongIdentityIdenticator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */