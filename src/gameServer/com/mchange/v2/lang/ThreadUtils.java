/*    */ package com.mchange.v2.lang;
/*    */ 
/*    */ import com.mchange.v2.log.MLevel;
/*    */ import com.mchange.v2.log.MLog;
/*    */ import com.mchange.v2.log.MLogger;
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ThreadUtils
/*    */ {
/* 43 */   private static final MLogger logger = MLog.getLogger(ThreadUtils.class);
/*    */ 
/*    */   
/*    */   static final Method holdsLock;
/*    */ 
/*    */   
/*    */   static {
/*    */     try {
/* 51 */       method = Thread.class.getMethod("holdsLock", new Class[] { Object.class });
/* 52 */     } catch (NoSuchMethodException noSuchMethodException) {
/* 53 */       method = null;
/*    */     } 
/* 55 */     holdsLock = method;
/*    */   } static {
/*    */     Method method;
/*    */   } public static void enumerateAll(Thread[] paramArrayOfThread) {
/* 59 */     ThreadGroupUtils.rootThreadGroup().enumerate(paramArrayOfThread);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Boolean reflectiveHoldsLock(Object paramObject) {
/*    */     try {
/* 68 */       if (holdsLock == null) {
/* 69 */         return null;
/*    */       }
/* 71 */       return (Boolean)holdsLock.invoke(null, new Object[] { paramObject });
/*    */     }
/* 73 */     catch (Exception exception) {
/*    */       
/* 75 */       if (logger.isLoggable(MLevel.FINER))
/* 76 */         logger.log(MLevel.FINER, "An Exception occurred while trying to call Thread.holdsLock( ... ) reflectively.", exception); 
/* 77 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/lang/ThreadUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */