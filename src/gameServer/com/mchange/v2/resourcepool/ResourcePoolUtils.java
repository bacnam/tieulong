/*    */ package com.mchange.v2.resourcepool;
/*    */ 
/*    */ import com.mchange.v2.log.MLevel;
/*    */ import com.mchange.v2.log.MLog;
/*    */ import com.mchange.v2.log.MLogger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class ResourcePoolUtils
/*    */ {
/* 42 */   static final MLogger logger = MLog.getLogger(ResourcePoolUtils.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static final ResourcePoolException convertThrowable(String msg, Throwable t) {
/* 49 */     if (logger.isLoggable(MLevel.FINE)) {
/* 50 */       logger.log(MLevel.FINE, "Converting throwable to ResourcePoolException...", t);
/*    */     }
/* 52 */     if (t instanceof ResourcePoolException) {
/* 53 */       return (ResourcePoolException)t;
/*    */     }
/* 55 */     return new ResourcePoolException(msg, t);
/*    */   }
/*    */   
/*    */   static final ResourcePoolException convertThrowable(Throwable t) {
/* 59 */     return convertThrowable("Ouch! " + t.toString(), t);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/resourcepool/ResourcePoolUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */