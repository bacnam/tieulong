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
/*    */ public class SynchronizedByteHolder
/*    */   implements ThreadSafeByteHolder
/*    */ {
/*    */   byte value;
/*    */   
/*    */   public synchronized byte getValue() {
/* 46 */     return this.value;
/*    */   }
/*    */   public synchronized void setValue(byte paramByte) {
/* 49 */     this.value = paramByte;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/lang/holders/SynchronizedByteHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */