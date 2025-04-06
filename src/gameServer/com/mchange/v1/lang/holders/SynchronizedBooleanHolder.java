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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SynchronizedBooleanHolder
/*    */   implements ThreadSafeBooleanHolder
/*    */ {
/*    */   boolean value;
/*    */   
/*    */   public synchronized boolean getValue() {
/* 52 */     return this.value;
/*    */   }
/*    */   public synchronized void setValue(boolean paramBoolean) {
/* 55 */     this.value = paramBoolean;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/lang/holders/SynchronizedBooleanHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */