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
/*    */ public final class GlobalMaxOnlyStatementCache
/*    */   extends GooGooStatementCache
/*    */ {
/*    */   int max_statements;
/* 45 */   GooGooStatementCache.Deathmarch globalDeathmarch = new GooGooStatementCache.Deathmarch(this);
/*    */ 
/*    */   
/*    */   public GlobalMaxOnlyStatementCache(AsynchronousRunner blockingTaskAsyncRunner, AsynchronousRunner deferredStatementDestroyer, int max_statements) {
/* 49 */     super(blockingTaskAsyncRunner, deferredStatementDestroyer);
/* 50 */     this.max_statements = max_statements;
/*    */   }
/*    */ 
/*    */   
/*    */   protected GooGooStatementCache.ConnectionStatementManager createConnectionStatementManager() {
/* 55 */     return new GooGooStatementCache.SimpleConnectionStatementManager();
/*    */   }
/*    */   
/*    */   void addStatementToDeathmarches(Object pstmt, Connection physicalConnection) {
/* 59 */     this.globalDeathmarch.deathmarchStatement(pstmt);
/*    */   }
/*    */   void removeStatementFromDeathmarches(Object pstmt, Connection physicalConnection) {
/* 62 */     this.globalDeathmarch.undeathmarchStatement(pstmt);
/*    */   }
/*    */   
/*    */   boolean prepareAssimilateNewStatement(Connection pcon) {
/* 66 */     int global_size = countCachedStatements();
/* 67 */     return (global_size < this.max_statements || (global_size == this.max_statements && this.globalDeathmarch.cullNext()));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/stmt/GlobalMaxOnlyStatementCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */