/*     */ package com.mysql.jdbc.jdbc2.optional;
/*     */ 
/*     */ import com.mysql.jdbc.Connection;
/*     */ import com.mysql.jdbc.ConnectionImpl;
/*     */ import com.mysql.jdbc.Util;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.sql.XAConnection;
/*     */ import javax.transaction.xa.XAException;
/*     */ import javax.transaction.xa.XAResource;
/*     */ import javax.transaction.xa.Xid;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SuspendableXAConnection
/*     */   extends MysqlPooledConnection
/*     */   implements XAConnection, XAResource
/*     */ {
/*     */   private static final Constructor<?> JDBC_4_XA_CONNECTION_WRAPPER_CTOR;
/*     */   
/*     */   static {
/*  49 */     if (Util.isJdbc4()) {
/*     */       try {
/*  51 */         JDBC_4_XA_CONNECTION_WRAPPER_CTOR = Class.forName("com.mysql.jdbc.jdbc2.optional.JDBC4SuspendableXAConnection").getConstructor(new Class[] { ConnectionImpl.class });
/*     */ 
/*     */       
/*     */       }
/*  55 */       catch (SecurityException e) {
/*  56 */         throw new RuntimeException(e);
/*  57 */       } catch (NoSuchMethodException e) {
/*  58 */         throw new RuntimeException(e);
/*  59 */       } catch (ClassNotFoundException e) {
/*  60 */         throw new RuntimeException(e);
/*     */       } 
/*     */     } else {
/*  63 */       JDBC_4_XA_CONNECTION_WRAPPER_CTOR = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static SuspendableXAConnection getInstance(ConnectionImpl mysqlConnection) throws SQLException {
/*  68 */     if (!Util.isJdbc4()) {
/*  69 */       return new SuspendableXAConnection(mysqlConnection);
/*     */     }
/*     */     
/*  72 */     return (SuspendableXAConnection)Util.handleNewInstance(JDBC_4_XA_CONNECTION_WRAPPER_CTOR, new Object[] { mysqlConnection }, mysqlConnection.getExceptionInterceptor());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SuspendableXAConnection(ConnectionImpl connection) {
/*  78 */     super((Connection)connection);
/*  79 */     this.underlyingConnection = connection;
/*     */   }
/*     */   
/*  82 */   private static final Map<Xid, XAConnection> XIDS_TO_PHYSICAL_CONNECTIONS = new HashMap<Xid, XAConnection>();
/*     */ 
/*     */   
/*     */   private Xid currentXid;
/*     */ 
/*     */   
/*     */   private XAConnection currentXAConnection;
/*     */ 
/*     */   
/*     */   private XAResource currentXAResource;
/*     */ 
/*     */   
/*     */   private ConnectionImpl underlyingConnection;
/*     */ 
/*     */ 
/*     */   
/*     */   private static synchronized XAConnection findConnectionForXid(ConnectionImpl connectionToWrap, Xid xid) throws SQLException {
/*  99 */     XAConnection conn = XIDS_TO_PHYSICAL_CONNECTIONS.get(xid);
/*     */     
/* 101 */     if (conn == null) {
/* 102 */       conn = new MysqlXAConnection(connectionToWrap, connectionToWrap.getLogXaCommands());
/*     */       
/* 104 */       XIDS_TO_PHYSICAL_CONNECTIONS.put(xid, conn);
/*     */     } 
/*     */     
/* 107 */     return conn;
/*     */   }
/*     */   
/*     */   private static synchronized void removeXAConnectionMapping(Xid xid) {
/* 111 */     XIDS_TO_PHYSICAL_CONNECTIONS.remove(xid);
/*     */   }
/*     */   
/*     */   private synchronized void switchToXid(Xid xid) throws XAException {
/* 115 */     if (xid == null) {
/* 116 */       throw new XAException();
/*     */     }
/*     */     
/*     */     try {
/* 120 */       if (!xid.equals(this.currentXid)) {
/* 121 */         XAConnection toSwitchTo = findConnectionForXid(this.underlyingConnection, xid);
/* 122 */         this.currentXAConnection = toSwitchTo;
/* 123 */         this.currentXid = xid;
/* 124 */         this.currentXAResource = toSwitchTo.getXAResource();
/*     */       } 
/* 126 */     } catch (SQLException sqlEx) {
/* 127 */       throw new XAException();
/*     */     } 
/*     */   }
/*     */   
/*     */   public XAResource getXAResource() throws SQLException {
/* 132 */     return this;
/*     */   }
/*     */   
/*     */   public void commit(Xid xid, boolean arg1) throws XAException {
/* 136 */     switchToXid(xid);
/* 137 */     this.currentXAResource.commit(xid, arg1);
/* 138 */     removeXAConnectionMapping(xid);
/*     */   }
/*     */   
/*     */   public void end(Xid xid, int arg1) throws XAException {
/* 142 */     switchToXid(xid);
/* 143 */     this.currentXAResource.end(xid, arg1);
/*     */   }
/*     */   
/*     */   public void forget(Xid xid) throws XAException {
/* 147 */     switchToXid(xid);
/* 148 */     this.currentXAResource.forget(xid);
/*     */     
/* 150 */     removeXAConnectionMapping(xid);
/*     */   }
/*     */   
/*     */   public int getTransactionTimeout() throws XAException {
/* 154 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean isSameRM(XAResource xaRes) throws XAException {
/* 158 */     return (xaRes == this);
/*     */   }
/*     */   
/*     */   public int prepare(Xid xid) throws XAException {
/* 162 */     switchToXid(xid);
/* 163 */     return this.currentXAResource.prepare(xid);
/*     */   }
/*     */   
/*     */   public Xid[] recover(int flag) throws XAException {
/* 167 */     return MysqlXAConnection.recover((Connection)this.underlyingConnection, flag);
/*     */   }
/*     */   
/*     */   public void rollback(Xid xid) throws XAException {
/* 171 */     switchToXid(xid);
/* 172 */     this.currentXAResource.rollback(xid);
/* 173 */     removeXAConnectionMapping(xid);
/*     */   }
/*     */   
/*     */   public boolean setTransactionTimeout(int arg0) throws XAException {
/* 177 */     return false;
/*     */   }
/*     */   
/*     */   public void start(Xid xid, int arg1) throws XAException {
/* 181 */     switchToXid(xid);
/*     */     
/* 183 */     if (arg1 != 2097152) {
/* 184 */       this.currentXAResource.start(xid, arg1);
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 193 */     this.currentXAResource.start(xid, 134217728);
/*     */   }
/*     */   
/*     */   public synchronized Connection getConnection() throws SQLException {
/* 197 */     if (this.currentXAConnection == null) {
/* 198 */       return getConnection(false, true);
/*     */     }
/*     */     
/* 201 */     return this.currentXAConnection.getConnection();
/*     */   }
/*     */   
/*     */   public void close() throws SQLException {
/* 205 */     if (this.currentXAConnection == null) {
/* 206 */       super.close();
/*     */     } else {
/* 208 */       removeXAConnectionMapping(this.currentXid);
/* 209 */       this.currentXAConnection.close();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mysql/jdbc/jdbc2/optional/SuspendableXAConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */