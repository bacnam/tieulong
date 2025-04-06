/*    */ package com.mchange.v1.identicator;
/*    */ 
/*    */ import java.lang.ref.ReferenceQueue;
/*    */ import java.lang.ref.WeakReference;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class WeakIdHashKey
/*    */   extends IdHashKey
/*    */ {
/*    */   Ref keyRef;
/*    */   int hash;
/*    */   
/*    */   public WeakIdHashKey(Object paramObject, Identicator paramIdenticator, ReferenceQueue paramReferenceQueue) {
/* 49 */     super(paramIdenticator);
/*    */     
/* 51 */     if (paramObject == null) {
/* 52 */       throw new UnsupportedOperationException("Collection does not accept nulls!");
/*    */     }
/* 54 */     this.keyRef = new Ref(paramObject, paramReferenceQueue);
/* 55 */     this.hash = paramIdenticator.hash(paramObject);
/*    */   }
/*    */   
/*    */   public Ref getInternalRef() {
/* 59 */     return this.keyRef;
/*    */   }
/*    */   public Object getKeyObj() {
/* 62 */     return this.keyRef.get();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 67 */     if (paramObject instanceof WeakIdHashKey) {
/*    */       
/* 69 */       WeakIdHashKey weakIdHashKey = (WeakIdHashKey)paramObject;
/* 70 */       if (this.keyRef == weakIdHashKey.keyRef) {
/* 71 */         return true;
/*    */       }
/*    */       
/* 74 */       T t1 = this.keyRef.get();
/* 75 */       T t2 = weakIdHashKey.keyRef.get();
/* 76 */       if (t1 == null || t2 == null) {
/* 77 */         return false;
/*    */       }
/* 79 */       return this.id.identical(t1, t2);
/*    */     } 
/*    */ 
/*    */     
/* 83 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 87 */     return this.hash;
/*    */   }
/*    */   
/*    */   class Ref extends WeakReference {
/*    */     public Ref(Object param1Object, ReferenceQueue<? super T> param1ReferenceQueue) {
/* 92 */       super((T)param1Object, param1ReferenceQueue);
/*    */     }
/*    */     WeakIdHashKey getKey() {
/* 95 */       return WeakIdHashKey.this;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/identicator/WeakIdHashKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */