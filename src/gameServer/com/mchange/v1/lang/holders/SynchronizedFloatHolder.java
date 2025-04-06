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
/*    */ public class SynchronizedFloatHolder
/*    */   implements ThreadSafeFloatHolder
/*    */ {
/*    */   float value;
/*    */   
/*    */   public synchronized float getValue() {
/* 46 */     return this.value;
/*    */   }
/*    */   public synchronized void setValue(float paramFloat) {
/* 49 */     this.value = paramFloat;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/lang/holders/SynchronizedFloatHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */