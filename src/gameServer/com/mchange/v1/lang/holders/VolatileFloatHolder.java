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
/*    */ public class VolatileFloatHolder
/*    */   implements ThreadSafeFloatHolder
/*    */ {
/*    */   volatile float value;
/*    */   
/*    */   public float getValue() {
/* 46 */     return this.value;
/*    */   }
/*    */   public void setValue(float paramFloat) {
/* 49 */     this.value = paramFloat;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/lang/holders/VolatileFloatHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */