/*    */ package org.junit.internal;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Throwables
/*    */ {
/*    */   public static Exception rethrowAsException(Throwable e) throws Exception {
/* 34 */     rethrow(e);
/* 35 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   private static <T extends Throwable> void rethrow(Throwable e) throws T {
/* 40 */     throw (T)e;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/internal/Throwables.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */