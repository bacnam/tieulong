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
/*    */ public class CannotAcquireResourceException
/*    */   extends ResourcePoolException
/*    */ {
/*    */   public CannotAcquireResourceException(String msg, Throwable t) {
/* 41 */     super(msg, t);
/*    */   }
/*    */   public CannotAcquireResourceException(Throwable t) {
/* 44 */     super(t);
/*    */   }
/*    */   public CannotAcquireResourceException(String msg) {
/* 47 */     super(msg);
/*    */   }
/*    */   
/*    */   public CannotAcquireResourceException() {}
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/resourcepool/CannotAcquireResourceException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */