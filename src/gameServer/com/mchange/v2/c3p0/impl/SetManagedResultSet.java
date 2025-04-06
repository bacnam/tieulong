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
/*    */ 
/*    */ abstract class SetManagedResultSet
/*    */   extends FilterResultSet
/*    */ {
/*    */   Set activeResultSets;
/*    */   
/*    */   SetManagedResultSet(Set activeResultSets) {
/* 48 */     this.activeResultSets = activeResultSets;
/*    */   }
/*    */ 
/*    */   
/*    */   SetManagedResultSet(ResultSet inner, Set activeResultSets) {
/* 53 */     super(inner);
/* 54 */     this.activeResultSets = activeResultSets;
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void setInner(ResultSet inner) {
/* 59 */     this.inner = inner;
/* 60 */     this.activeResultSets.add(inner);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void close() throws SQLException {
/* 65 */     if (this.inner != null) {
/*    */       
/* 67 */       this.inner.close();
/* 68 */       this.activeResultSets.remove(this.inner);
/* 69 */       this.inner = null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/SetManagedResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */