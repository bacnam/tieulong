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
/*    */ public class SynchronizedIntHolder
/*    */   implements ThreadSafeIntHolder
/*    */ {
/*    */   int value;
/*    */   
/*    */   public synchronized int getValue() {
/* 46 */     return this.value;
/*    */   }
/*    */   public synchronized void setValue(int paramInt) {
/* 49 */     this.value = paramInt;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/lang/holders/SynchronizedIntHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */