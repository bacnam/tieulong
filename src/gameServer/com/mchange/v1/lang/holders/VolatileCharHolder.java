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
/*    */ public class VolatileCharHolder
/*    */   implements ThreadSafeCharHolder
/*    */ {
/*    */   volatile char value;
/*    */   
/*    */   public char getValue() {
/* 46 */     return this.value;
/*    */   }
/*    */   public void setValue(char paramChar) {
/* 49 */     this.value = paramChar;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/lang/holders/VolatileCharHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */