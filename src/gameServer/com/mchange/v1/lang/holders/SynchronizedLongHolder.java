/*    */ package com.mchange.v1.lang.holders;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SynchronizedLongHolder
/*    */   implements ThreadSafeLongHolder
/*    */ {
/*    */   long value;
/*    */   
/*    */   public synchronized long getValue() {
/* 46 */     return this.value;
/*    */   }
/*    */   public synchronized void setValue(long paramLong) {
/* 49 */     this.value = paramLong;
/*    */   }
/*    */   public SynchronizedLongHolder(long paramLong) {
/* 52 */     this.value = paramLong;
/*    */   }
/*    */   public SynchronizedLongHolder() {
/* 55 */     this(0L);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/lang/holders/SynchronizedLongHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */