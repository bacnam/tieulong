/*    */ package com.mchange.v2.coalesce;
/*    */ 
/*    */ import com.mchange.v1.identicator.Identicator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CoalesceIdenticator
/*    */   implements Identicator
/*    */ {
/*    */   CoalesceChecker cc;
/*    */   
/*    */   CoalesceIdenticator(CoalesceChecker paramCoalesceChecker) {
/* 45 */     this.cc = paramCoalesceChecker;
/*    */   }
/*    */   public boolean identical(Object paramObject1, Object paramObject2) {
/* 48 */     return this.cc.checkCoalesce(paramObject1, paramObject2);
/*    */   }
/*    */   public int hash(Object paramObject) {
/* 51 */     return this.cc.coalesceHash(paramObject);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/coalesce/CoalesceIdenticator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */