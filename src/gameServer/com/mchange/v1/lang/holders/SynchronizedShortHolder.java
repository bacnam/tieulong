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
/*    */ public class SynchronizedShortHolder
/*    */   implements ThreadSafeShortHolder
/*    */ {
/*    */   short value;
/*    */   
/*    */   public synchronized short getValue() {
/* 46 */     return this.value;
/*    */   }
/*    */   public synchronized void setValue(short paramShort) {
/* 49 */     this.value = paramShort;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/lang/holders/SynchronizedShortHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */