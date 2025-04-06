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
/*    */ public class SynchronizedDoubleHolder
/*    */   implements ThreadSafeDoubleHolder
/*    */ {
/*    */   double value;
/*    */   
/*    */   public synchronized double getValue() {
/* 46 */     return this.value;
/*    */   }
/*    */   public synchronized void setValue(double paramDouble) {
/* 49 */     this.value = paramDouble;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/lang/holders/SynchronizedDoubleHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */