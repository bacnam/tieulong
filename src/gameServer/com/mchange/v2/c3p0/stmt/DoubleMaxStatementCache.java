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
/*    */ public final class DoubleMaxStatementCache
/*    */   extends GooGooStatementCache
/*    */ {
/*    */   int max_statements;
/* 45 */   GooGooStatementCache.Deathmarch globalDeathmarch = new GooGooStatementCache.Deathmarch(this);
/*    */   
/*    */   int max_statements_per_connection;
/*    */   
/*    */   GooGooStatementCache.DeathmarchConnectionStatementManager dcsm;
/*    */   
/*    */   public DoubleMaxStatementCache(AsynchronousRunner blockingTaskAsyncRunner, AsynchronousRunner deferredStatementDestroyer, int max_statements, int max_statements_per_connection) {
/* 52 */     super(blockingTaskAsyncRunner, deferredStatementDestroyer);
/* 53 */     this.max_statements = max_statements;
/* 54 */     this.max_statements_per_connection = max_statements_per_connection;
/*    */   }
/*    */ 
/*    */   
/*    */   protected GooGooStatementCache.ConnectionStatementManager createConnectionStatementManager() {
/* 59 */     return this.dcsm = new GooGooStatementCache.DeathmarchConnectionStatementManager(this);
/*    */   }
/*    */ 
/*    */   
/*    */   void addStatementToDeathmarches(Object pstmt, Connection physicalConnection) {
/* 64 */     this.globalDeathmarch.deathmarchStatement(pstmt);
/* 65 */     this.dcsm.getDeathmarch(physicalConnection).deathmarchStatement(pstmt);
/*    */   }
/*    */ 
/*    */   
/*    */   void removeStatementFromDeathmarches(Object pstmt, Connection physicalConnection) {
/* 70 */     this.globalDeathmarch.undeathmarchStatement(pstmt);
/* 71 */     this.dcsm.getDeathmarch(physicalConnection).undeathmarchStatement(pstmt);
/*    */   }
/*    */ 
/*    */   
/*    */   boolean prepareAssimilateNewStatement(Connection pcon) {
/* 76 */     int cxn_stmt_count = this.dcsm.getNumStatementsForConnection(pcon);
/* 77 */     if (cxn_stmt_count < this.max_statements_per_connection) {
/*    */       
/* 79 */       int global_size = countCachedStatements();
/* 80 */       return (global_size < this.max_statements || (global_size == this.max_statements && this.globalDeathmarch.cullNext()));
/*    */     } 
/*    */     
/* 83 */     return (cxn_stmt_count == this.max_statements_per_connection && this.dcsm.getDeathmarch(pcon).cullNext());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/stmt/DoubleMaxStatementCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */