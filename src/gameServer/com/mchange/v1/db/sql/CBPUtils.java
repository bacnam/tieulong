/*    */ package com.mchange.v1.db.sql;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CBPUtils
/*    */ {
/*    */   public static void attemptCheckin(ConnectionBundle paramConnectionBundle, ConnectionBundlePool paramConnectionBundlePool) {
/*    */     try {
/* 43 */       paramConnectionBundlePool.checkinBundle(paramConnectionBundle);
/* 44 */     } catch (Exception exception) {
/* 45 */       exception.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/CBPUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */