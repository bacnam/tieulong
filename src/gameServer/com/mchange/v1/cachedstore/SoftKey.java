/*    */ package com.mchange.v1.cachedstore;
/*    */ 
/*    */ import java.lang.ref.ReferenceQueue;
/*    */ import java.lang.ref.SoftReference;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class SoftKey
/*    */   extends SoftReference
/*    */ {
/*    */   int hash_code;
/*    */   
/*    */   SoftKey(Object paramObject, ReferenceQueue<? super T> paramReferenceQueue) {
/* 46 */     super((T)paramObject, paramReferenceQueue);
/* 47 */     this.hash_code = paramObject.hashCode();
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 51 */     return this.hash_code;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 59 */     if (this == paramObject) return true;
/*    */ 
/*    */     
/* 62 */     T t = get();
/* 63 */     if (t == null)
/* 64 */       return false; 
/* 65 */     if (getClass() == paramObject.getClass()) {
/*    */       
/* 67 */       SoftKey softKey = (SoftKey)paramObject;
/* 68 */       return t.equals(softKey.get());
/*    */     } 
/*    */     
/* 71 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/cachedstore/SoftKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */