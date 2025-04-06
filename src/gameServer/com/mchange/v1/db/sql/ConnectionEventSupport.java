/*    */ package com.mchange.v1.db.sql;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import java.util.HashSet;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConnectionEventSupport
/*    */ {
/*    */   PooledConnection source;
/* 48 */   Set mlisteners = new HashSet();
/*    */   
/*    */   public ConnectionEventSupport(PooledConnection paramPooledConnection) {
/* 51 */     this.source = paramPooledConnection;
/*    */   }
/*    */   public synchronized void addConnectionEventListener(ConnectionEventListener paramConnectionEventListener) {
/* 54 */     this.mlisteners.add(paramConnectionEventListener);
/*    */   }
/*    */   public synchronized void removeConnectionEventListener(ConnectionEventListener paramConnectionEventListener) {
/* 57 */     this.mlisteners.remove(paramConnectionEventListener);
/*    */   }
/*    */   
/*    */   public synchronized void fireConnectionClosed() {
/* 61 */     ConnectionEvent connectionEvent = new ConnectionEvent(this.source);
/* 62 */     for (ConnectionEventListener connectionEventListener : this.mlisteners)
/*    */     {
/*    */       
/* 65 */       connectionEventListener.connectionClosed(connectionEvent);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void fireConnectionErrorOccurred(SQLException paramSQLException) {
/* 71 */     ConnectionEvent connectionEvent = new ConnectionEvent(this.source, paramSQLException);
/* 72 */     for (ConnectionEventListener connectionEventListener : this.mlisteners)
/*    */     {
/*    */       
/* 75 */       connectionEventListener.connectionErrorOccurred(connectionEvent);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/ConnectionEventSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */