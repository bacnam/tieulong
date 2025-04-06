/*    */ package com.mchange.v2.c3p0.impl;
/*    */ 
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.Statement;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class NullStatementSetManagedResultSet
/*    */   extends SetManagedResultSet
/*    */ {
/*    */   NullStatementSetManagedResultSet(Set activeResultSets) {
/* 52 */     super(activeResultSets);
/*    */   }
/*    */   NullStatementSetManagedResultSet(ResultSet inner, Set activeResultSets) {
/* 55 */     super(inner, activeResultSets);
/*    */   }
/*    */   public Statement getStatement() {
/* 58 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/NullStatementSetManagedResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */