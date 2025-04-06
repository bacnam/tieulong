/*    */ package com.mchange.v2.coalesce;
/*    */ 
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
/*    */ class AbstractStrongCoalescer
/*    */   implements Coalescer
/*    */ {
/*    */   Map coalesced;
/*    */   
/*    */   AbstractStrongCoalescer(Map paramMap) {
/* 45 */     this.coalesced = paramMap;
/*    */   }
/*    */   
/*    */   public Object coalesce(Object paramObject) {
/* 49 */     Object object = this.coalesced.get(paramObject);
/* 50 */     if (object == null) {
/*    */       
/* 52 */       this.coalesced.put(paramObject, paramObject);
/* 53 */       object = paramObject;
/*    */     } 
/* 55 */     return object;
/*    */   }
/*    */   
/*    */   public int countCoalesced() {
/* 59 */     return this.coalesced.size();
/*    */   }
/*    */   public Iterator iterator() {
/* 62 */     return new CoalescerIterator(this.coalesced.keySet().iterator());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/coalesce/AbstractStrongCoalescer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */