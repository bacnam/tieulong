/*    */ package com.mchange.v2.c3p0;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractConnectionTester
/*    */   implements UnifiedConnectionTester
/*    */ {
/*    */   public abstract int activeCheckConnection(Connection paramConnection, String paramString, Throwable[] paramArrayOfThrowable);
/*    */   
/*    */   public abstract int statusOnException(Connection paramConnection, Throwable paramThrowable, String paramString, Throwable[] paramArrayOfThrowable);
/*    */   
/*    */   public int activeCheckConnection(Connection c) {
/* 79 */     return activeCheckConnection(c, null, null);
/*    */   }
/*    */   public int activeCheckConnection(Connection c, Throwable[] rootCauseOutParamHolder) {
/* 82 */     return activeCheckConnection(c, null, rootCauseOutParamHolder);
/*    */   }
/*    */   public int activeCheckConnection(Connection c, String preferredTestQuery) {
/* 85 */     return activeCheckConnection(c, preferredTestQuery, null);
/*    */   }
/*    */   public int statusOnException(Connection c, Throwable t) {
/* 88 */     return statusOnException(c, t, null, null);
/*    */   }
/*    */   public int statusOnException(Connection c, Throwable t, Throwable[] rootCauseOutParamHolder) {
/* 91 */     return statusOnException(c, t, null, rootCauseOutParamHolder);
/*    */   }
/*    */   public int statusOnException(Connection c, Throwable t, String preferredTestQuery) {
/* 94 */     return statusOnException(c, t, preferredTestQuery, null);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/AbstractConnectionTester.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */