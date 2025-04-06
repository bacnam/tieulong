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
/*    */ public final class ObjectUtils
/*    */ {
/*    */   public static boolean eqOrBothNull(Object paramObject1, Object paramObject2) {
/* 42 */     if (paramObject1 == paramObject2)
/* 43 */       return true; 
/* 44 */     if (paramObject1 == null) {
/* 45 */       return false;
/*    */     }
/* 47 */     return paramObject1.equals(paramObject2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int hashOrZero(Object paramObject) {
/* 56 */     return (paramObject == null) ? 0 : paramObject.hashCode();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/lang/ObjectUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */