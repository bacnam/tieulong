/*    */ package com.mchange.v2.c3p0.util;
/*    */ 
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.SQLException;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ import javax.sql.PooledConnection;
/*    */ import javax.sql.StatementEvent;
/*    */ import javax.sql.StatementEventListener;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StatementEventSupport
/*    */ {
/*    */   PooledConnection source;
/* 45 */   HashSet mlisteners = new HashSet();
/*    */   
/*    */   public StatementEventSupport(PooledConnection source) {
/* 48 */     this.source = source;
/*    */   }
/*    */   public synchronized void addStatementEventListener(StatementEventListener mlistener) {
/* 51 */     this.mlisteners.add(mlistener);
/*    */   }
/*    */   public synchronized void removeStatementEventListener(StatementEventListener mlistener) {
/* 54 */     this.mlisteners.remove(mlistener);
/*    */   }
/*    */   public synchronized void printListeners() {
/* 57 */     System.err.println(this.mlisteners);
/*    */   }
/*    */   public synchronized int getListenerCount() {
/* 60 */     return this.mlisteners.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public void fireStatementClosed(PreparedStatement ps) {
/*    */     Set mlCopy;
/* 66 */     synchronized (this) {
/* 67 */       mlCopy = (Set)this.mlisteners.clone();
/*    */     } 
/* 69 */     StatementEvent evt = new StatementEvent(this.source, ps);
/* 70 */     for (Iterator<StatementEventListener> i = mlCopy.iterator(); i.hasNext(); ) {
/*    */       
/* 72 */       StatementEventListener cl = i.next();
/* 73 */       cl.statementClosed(evt);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void fireStatementErrorOccurred(PreparedStatement ps, SQLException error) {
/*    */     Set mlCopy;
/* 81 */     synchronized (this) {
/* 82 */       mlCopy = (Set)this.mlisteners.clone();
/*    */     } 
/* 84 */     StatementEvent evt = new StatementEvent(this.source, ps, error);
/* 85 */     for (Iterator<StatementEventListener> i = mlCopy.iterator(); i.hasNext(); ) {
/*    */       
/* 87 */       StatementEventListener cl = i.next();
/* 88 */       cl.statementErrorOccurred(evt);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/util/StatementEventSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */