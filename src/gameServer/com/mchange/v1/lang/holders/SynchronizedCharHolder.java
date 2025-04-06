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
/*    */ public class SynchronizedCharHolder
/*    */   implements ThreadSafeCharHolder
/*    */ {
/*    */   char value;
/*    */   
/*    */   public synchronized char getValue() {
/* 46 */     return this.value;
/*    */   }
/*    */   public synchronized void setValue(char paramChar) {
/* 49 */     this.value = paramChar;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/lang/holders/SynchronizedCharHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */