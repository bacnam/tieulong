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
/*    */ public class VolatileIntHolder
/*    */   implements ThreadSafeIntHolder
/*    */ {
/*    */   volatile int value;
/*    */   
/*    */   public int getValue() {
/* 46 */     return this.value;
/*    */   }
/*    */   public void setValue(int paramInt) {
/* 49 */     this.value = paramInt;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/lang/holders/VolatileIntHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */