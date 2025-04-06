/*    */ package com.mchange.v1.db.sql;
/*    */ 
/*    */ import java.sql.ResultSet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class CBPCursor
/*    */   extends SimpleCursor
/*    */ {
/*    */   ConnectionBundle returnMe;
/*    */   ConnectionBundlePool home;
/*    */   
/*    */   public CBPCursor(ResultSet paramResultSet, ConnectionBundle paramConnectionBundle, ConnectionBundlePool paramConnectionBundlePool) {
/* 47 */     super(paramResultSet);
/* 48 */     this.returnMe = paramConnectionBundle;
/* 49 */     this.home = paramConnectionBundlePool;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws Exception {
/*    */     try {
/* 55 */       super.close();
/*    */     } finally {
/* 57 */       this.home.checkinBundle(this.returnMe);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/CBPCursor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */