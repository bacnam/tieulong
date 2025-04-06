/*    */ package com.mchange.v2.coalesce;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CoalescerIterator
/*    */   implements Iterator
/*    */ {
/*    */   Iterator inner;
/*    */   
/*    */   CoalescerIterator(Iterator paramIterator) {
/* 45 */     this.inner = paramIterator;
/*    */   }
/*    */   public boolean hasNext() {
/* 48 */     return this.inner.hasNext();
/*    */   }
/*    */   public Object next() {
/* 51 */     return this.inner.next();
/*    */   }
/*    */   public void remove() {
/* 54 */     throw new UnsupportedOperationException("Objects cannot be removed from a coalescer!");
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/coalesce/CoalescerIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */