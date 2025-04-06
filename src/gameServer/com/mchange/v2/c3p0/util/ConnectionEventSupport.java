/*    */ package com.mchange.v2.c3p0.util;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ import javax.sql.ConnectionEvent;
/*    */ import javax.sql.ConnectionEventListener;
/*    */ import javax.sql.PooledConnection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConnectionEventSupport
/*    */ {
/*    */   PooledConnection source;
/* 45 */   HashSet mlisteners = new HashSet();
/*    */   
/*    */   public ConnectionEventSupport(PooledConnection source) {
/* 48 */     this.source = source;
/*    */   }
/*    */   public synchronized void addConnectionEventListener(ConnectionEventListener mlistener) {
/* 51 */     this.mlisteners.add(mlistener);
/*    */   }
/*    */   public synchronized void removeConnectionEventListener(ConnectionEventListener mlistener) {
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
/*    */   public void fireConnectionClosed() {
/*    */     Set mlCopy;
/* 66 */     synchronized (this) {
/* 67 */       mlCopy = (Set)this.mlisteners.clone();
/*    */     } 
/* 69 */     ConnectionEvent evt = new ConnectionEvent(this.source);
/* 70 */     for (Iterator<ConnectionEventListener> i = mlCopy.iterator(); i.hasNext(); ) {
/*    */       
/* 72 */       ConnectionEventListener cl = i.next();
/* 73 */       cl.connectionClosed(evt);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void fireConnectionErrorOccurred(SQLException error) {
/*    */     Set mlCopy;
/* 81 */     synchronized (this) {
/* 82 */       mlCopy = (Set)this.mlisteners.clone();
/*    */     } 
/* 84 */     ConnectionEvent evt = new ConnectionEvent(this.source, error);
/* 85 */     for (Iterator<ConnectionEventListener> i = mlCopy.iterator(); i.hasNext(); ) {
/*    */       
/* 87 */       ConnectionEventListener cl = i.next();
/* 88 */       cl.connectionErrorOccurred(evt);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/util/ConnectionEventSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */