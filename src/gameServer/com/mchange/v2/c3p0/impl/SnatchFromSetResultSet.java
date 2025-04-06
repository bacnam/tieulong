/*    */ package com.mchange.v2.c3p0.impl;
/*    */ 
/*    */ import com.mchange.v2.sql.filter.FilterResultSet;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
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
/*    */ final class SnatchFromSetResultSet
/*    */   extends FilterResultSet
/*    */ {
/*    */   Set activeResultSets;
/*    */   
/*    */   SnatchFromSetResultSet(Set activeResultSets) {
/* 47 */     this.activeResultSets = activeResultSets;
/*    */   }
/*    */   
/*    */   public synchronized void setInner(ResultSet inner) {
/* 51 */     this.inner = inner;
/* 52 */     this.activeResultSets.add(inner);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void close() throws SQLException {
/* 57 */     this.inner.close();
/* 58 */     this.activeResultSets.remove(this.inner);
/* 59 */     this.inner = null;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/SnatchFromSetResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */