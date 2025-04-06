/*    */ package com.mchange.v2.resourcepool;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TimeoutException
/*    */   extends ResourcePoolException
/*    */ {
/*    */   public TimeoutException(String msg, Throwable t) {
/* 41 */     super(msg, t);
/*    */   }
/*    */   public TimeoutException(Throwable t) {
/* 44 */     super(t);
/*    */   }
/*    */   public TimeoutException(String msg) {
/* 47 */     super(msg);
/*    */   }
/*    */   
/*    */   public TimeoutException() {}
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/resourcepool/TimeoutException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */