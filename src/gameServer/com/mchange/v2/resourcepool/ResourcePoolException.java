/*    */ package com.mchange.v2.resourcepool;
/*    */ 
/*    */ import com.mchange.lang.PotentiallySecondaryException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ResourcePoolException
/*    */   extends PotentiallySecondaryException
/*    */ {
/*    */   public ResourcePoolException(String msg, Throwable t) {
/* 43 */     super(msg, t);
/*    */   }
/*    */   public ResourcePoolException(Throwable t) {
/* 46 */     super(t);
/*    */   }
/*    */   public ResourcePoolException(String msg) {
/* 49 */     super(msg);
/*    */   }
/*    */   
/*    */   public ResourcePoolException() {}
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/resourcepool/ResourcePoolException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */