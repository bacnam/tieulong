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
/*    */ class SyncedCoalescer
/*    */   implements Coalescer
/*    */ {
/*    */   Coalescer inner;
/*    */   
/*    */   public SyncedCoalescer(Coalescer paramCoalescer) {
/* 45 */     this.inner = paramCoalescer;
/*    */   }
/*    */   public synchronized Object coalesce(Object paramObject) {
/* 48 */     return this.inner.coalesce(paramObject);
/*    */   }
/*    */   public synchronized int countCoalesced() {
/* 51 */     return this.inner.countCoalesced();
/*    */   }
/*    */   public synchronized Iterator iterator() {
/* 54 */     return this.inner.iterator();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/coalesce/SyncedCoalescer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */