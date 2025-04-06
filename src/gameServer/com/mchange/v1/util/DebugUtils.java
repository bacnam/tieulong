/*    */ package com.mchange.v1.util;
/*    */ 
/*    */ import com.mchange.util.AssertException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DebugUtils
/*    */ {
/*    */   public static void myAssert(boolean paramBoolean) {
/* 45 */     if (!paramBoolean) throw new AssertException(); 
/*    */   }
/*    */   public static void myAssert(boolean paramBoolean, String paramString) {
/* 48 */     if (!paramBoolean) throw new AssertException(paramString); 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/DebugUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */