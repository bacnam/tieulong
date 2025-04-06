/*    */ package com.mchange.v2.c3p0.test;
/*    */ 
/*    */ import com.mchange.v2.c3p0.AbstractConnectionCustomizer;
/*    */ import java.sql.Connection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TestConnectionCustomizer
/*    */   extends AbstractConnectionCustomizer
/*    */ {
/*    */   public void onAcquire(Connection c, String pdsIdt) {
/* 44 */     System.err.println("Acquired " + c + " [" + pdsIdt + "]");
/*    */   }
/*    */   public void onDestroy(Connection c, String pdsIdt) {
/* 47 */     System.err.println("Destroying " + c + " [" + pdsIdt + "]");
/*    */   }
/*    */   public void onCheckOut(Connection c, String pdsIdt) {
/* 50 */     System.err.println("Checked out " + c + " [" + pdsIdt + "]");
/*    */   }
/*    */   public void onCheckIn(Connection c, String pdsIdt) {
/* 53 */     System.err.println("Checking in " + c + " [" + pdsIdt + "]");
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/test/TestConnectionCustomizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */