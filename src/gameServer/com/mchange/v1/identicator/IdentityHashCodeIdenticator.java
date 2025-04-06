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
/*    */ public class IdentityHashCodeIdenticator
/*    */   implements Identicator
/*    */ {
/* 43 */   public static IdentityHashCodeIdenticator INSTANCE = new IdentityHashCodeIdenticator();
/*    */   
/*    */   public boolean identical(Object paramObject1, Object paramObject2) {
/* 46 */     return (System.identityHashCode(paramObject1) == System.identityHashCode(paramObject2));
/*    */   }
/*    */   public int hash(Object paramObject) {
/* 49 */     return System.identityHashCode(paramObject);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/identicator/IdentityHashCodeIdenticator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */