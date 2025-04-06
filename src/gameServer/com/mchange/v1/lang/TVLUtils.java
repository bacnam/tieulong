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
/*    */ 
/*    */ public final class TVLUtils
/*    */ {
/*    */   public static final boolean isDefinitelyTrue(Boolean paramBoolean) {
/* 46 */     return (paramBoolean != null && paramBoolean.booleanValue());
/*    */   }
/*    */   public static final boolean isDefinitelyFalse(Boolean paramBoolean) {
/* 49 */     return (paramBoolean != null && !paramBoolean.booleanValue());
/*    */   }
/*    */   public static final boolean isPossiblyTrue(Boolean paramBoolean) {
/* 52 */     return (paramBoolean == null || paramBoolean.booleanValue());
/*    */   }
/*    */   public static final boolean isPossiblyFalse(Boolean paramBoolean) {
/* 55 */     return (paramBoolean == null || !paramBoolean.booleanValue());
/*    */   }
/*    */   public static final boolean isUnknown(Boolean paramBoolean) {
/* 58 */     return (paramBoolean == null);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/lang/TVLUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */