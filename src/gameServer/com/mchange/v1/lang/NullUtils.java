/*    */ package com.mchange.v1.lang;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class NullUtils
/*    */ {
/*    */   public static boolean equalsOrBothNull(Object paramObject1, Object paramObject2) {
/* 45 */     if (paramObject1 == paramObject2)
/* 46 */       return true; 
/* 47 */     if (paramObject1 == null) {
/* 48 */       return false;
/*    */     }
/* 50 */     return paramObject1.equals(paramObject2);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/lang/NullUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */