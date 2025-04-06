/*    */ package com.mchange.v2.lang;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ThreadGroupUtils
/*    */ {
/*    */   public static ThreadGroup rootThreadGroup() {
/* 42 */     ThreadGroup threadGroup1 = Thread.currentThread().getThreadGroup();
/* 43 */     ThreadGroup threadGroup2 = threadGroup1.getParent();
/* 44 */     while (threadGroup2 != null) {
/*    */       
/* 46 */       threadGroup1 = threadGroup2;
/* 47 */       threadGroup2 = threadGroup1.getParent();
/*    */     } 
/* 49 */     return threadGroup1;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/lang/ThreadGroupUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */