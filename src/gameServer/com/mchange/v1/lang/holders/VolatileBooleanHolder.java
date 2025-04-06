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
/*    */ public class VolatileBooleanHolder
/*    */   implements ThreadSafeBooleanHolder
/*    */ {
/*    */   volatile boolean value;
/*    */   
/*    */   public boolean getValue() {
/* 46 */     return this.value;
/*    */   }
/*    */   public void setValue(boolean paramBoolean) {
/* 49 */     this.value = paramBoolean;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/lang/holders/VolatileBooleanHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */