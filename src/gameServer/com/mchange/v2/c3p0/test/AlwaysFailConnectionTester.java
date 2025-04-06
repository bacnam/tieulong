/*    */ package com.mchange.v2.c3p0.test;
/*    */ 
/*    */ import com.mchange.v2.c3p0.QueryConnectionTester;
/*    */ import com.mchange.v2.log.MLevel;
/*    */ import com.mchange.v2.log.MLog;
/*    */ import com.mchange.v2.log.MLogger;
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
/*    */ 
/*    */ 
/*    */ public final class AlwaysFailConnectionTester
/*    */   implements QueryConnectionTester
/*    */ {
/*    */   public AlwaysFailConnectionTester() {
/* 49 */     logger.log(MLevel.WARNING, "Instantiated: " + this, new Exception("Instantiation Stack Trace."));
/*    */   }
/*    */ 
/*    */   
/*    */   public int activeCheckConnection(Connection c) {
/* 54 */     logger.warning(this + ": activeCheckConnection(Connection c)");
/* 55 */     return -1;
/*    */   }
/*    */   static final MLogger logger = MLog.getLogger(AlwaysFailConnectionTester.class);
/*    */   
/*    */   public int statusOnException(Connection c, Throwable t) {
/* 60 */     logger.warning(this + ": statusOnException(Connection c, Throwable t)");
/* 61 */     return -1;
/*    */   }
/*    */ 
/*    */   
/*    */   public int activeCheckConnection(Connection c, String preferredTestQuery) {
/* 66 */     logger.warning(this + ": activeCheckConnection(Connection c, String preferredTestQuery)");
/* 67 */     return -1;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 71 */     return o instanceof AlwaysFailConnectionTester;
/*    */   }
/*    */   public int hashCode() {
/* 74 */     return 1;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/test/AlwaysFailConnectionTester.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */