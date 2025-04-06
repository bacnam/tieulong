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
/*    */ public class VolatileShortHolder
/*    */   implements ThreadSafeShortHolder
/*    */ {
/*    */   volatile short value;
/*    */   
/*    */   public short getValue() {
/* 46 */     return this.value;
/*    */   }
/*    */   public void setValue(short paramShort) {
/* 49 */     this.value = paramShort;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/lang/holders/VolatileShortHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */