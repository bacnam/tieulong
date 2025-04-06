/*    */ package com.mchange.v2.c3p0.stmt;
/*    */ 
/*    */ import com.mchange.v2.async.AsynchronousRunner;
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
/*    */ public final class PerConnectionMaxOnlyStatementCache
/*    */   extends GooGooStatementCache
/*    */ {
/*    */   int max_statements_per_connection;
/*    */   GooGooStatementCache.DeathmarchConnectionStatementManager dcsm;
/*    */   
/*    */   public PerConnectionMaxOnlyStatementCache(AsynchronousRunner blockingTaskAsyncRunner, AsynchronousRunner deferredStatementDestroyer, int max_statements_per_connection) {
/* 49 */     super(blockingTaskAsyncRunner, deferredStatementDestroyer);
/* 50 */     this.max_statements_per_connection = max_statements_per_connection;
/*    */   }
/*    */ 
/*    */   
/*    */   protected GooGooStatementCache.ConnectionStatementManager createConnectionStatementManager() {
/* 55 */     return this.dcsm = new GooGooStatementCache.DeathmarchConnectionStatementManager(this);
/*    */   }
/*    */   
/*    */   void addStatementToDeathmarches(Object pstmt, Connection physicalConnection) {
/* 59 */     this.dcsm.getDeathmarch(physicalConnection).deathmarchStatement(pstmt);
/*    */   }
/*    */   void removeStatementFromDeathmarches(Object pstmt, Connection physicalConnection) {
/* 62 */     this.dcsm.getDeathmarch(physicalConnection).undeathmarchStatement(pstmt);
/*    */   }
/*    */   
/*    */   boolean prepareAssimilateNewStatement(Connection pcon) {
/* 66 */     int cxn_stmt_count = this.dcsm.getNumStatementsForConnection(pcon);
/* 67 */     return (cxn_stmt_count < this.max_statements_per_connection || (cxn_stmt_count == this.max_statements_per_connection && this.dcsm.getDeathmarch(pcon).cullNext()));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/stmt/PerConnectionMaxOnlyStatementCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */