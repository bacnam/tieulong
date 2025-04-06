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
/*    */ public final class BooleanUtils
/*    */ {
/*    */   public static boolean parseBoolean(String paramString) throws IllegalArgumentException {
/* 42 */     if (paramString.equals("true"))
/* 43 */       return true; 
/* 44 */     if (paramString.equals("false")) {
/* 45 */       return false;
/*    */     }
/* 47 */     throw new IllegalArgumentException("\"str\" is neither \"true\" nor \"false\".");
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/lang/BooleanUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */