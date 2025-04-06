/*    */ package com.mchange.v2.coalesce;
/*    */ 
/*    */ import java.lang.ref.WeakReference;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class AbstractWeakCoalescer
/*    */   implements Coalescer
/*    */ {
/*    */   Map wcoalesced;
/*    */   
/*    */   AbstractWeakCoalescer(Map paramMap) {
/* 46 */     this.wcoalesced = paramMap;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object coalesce(Object paramObject) {
/* 51 */     Object object = null;
/*    */     
/* 53 */     WeakReference<Object> weakReference = (WeakReference)this.wcoalesced.get(paramObject);
/* 54 */     if (weakReference != null) {
/* 55 */       object = weakReference.get();
/*    */     }
/* 57 */     if (object == null) {
/*    */       
/* 59 */       this.wcoalesced.put(paramObject, new WeakReference(paramObject));
/* 60 */       object = paramObject;
/*    */     } 
/* 62 */     return object;
/*    */   }
/*    */   
/*    */   public int countCoalesced() {
/* 66 */     return this.wcoalesced.size();
/*    */   }
/*    */   public Iterator iterator() {
/* 69 */     return new CoalescerIterator(this.wcoalesced.keySet().iterator());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/coalesce/AbstractWeakCoalescer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */