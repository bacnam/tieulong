/*      */ package com.mchange.v2.c3p0.impl;
/*      */ 
/*      */ import com.mchange.v2.c3p0.C3P0ProxyConnection;
/*      */ import com.mchange.v2.log.MLevel;
/*      */ import com.mchange.v2.log.MLog;
/*      */ import com.mchange.v2.log.MLogger;
/*      */ import com.mchange.v2.sql.SqlUtils;
/*      */ import com.mchange.v2.util.ResourceClosedException;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.sql.Array;
/*      */ import java.sql.Blob;
/*      */ import java.sql.CallableStatement;
/*      */ import java.sql.Clob;
/*      */ import java.sql.Connection;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.NClob;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLClientInfoException;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.SQLXML;
/*      */ import java.sql.Savepoint;
/*      */ import java.sql.Statement;
/*      */ import java.sql.Struct;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.concurrent.Executor;
/*      */ import javax.sql.ConnectionEvent;
/*      */ import javax.sql.ConnectionEventListener;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class NewProxyConnection
/*      */   implements Connection, C3P0ProxyConnection
/*      */ {
/*      */   protected Connection inner;
/*      */   
/*      */   private void __setInner(Connection inner) {
/*   48 */     this.inner = inner;
/*      */   }
/*      */   
/*      */   NewProxyConnection(Connection inner) {
/*   52 */     __setInner(inner);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setReadOnly(boolean a) throws SQLException {
/*      */     try {
/*   58 */       this.inner.setReadOnly(a);
/*   59 */       this.parentPooledConnection.markNewReadOnly(a);
/*      */     }
/*   61 */     catch (NullPointerException exc) {
/*      */       
/*   63 */       if (isDetached())
/*      */       {
/*   65 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*   67 */       throw exc;
/*      */     }
/*   69 */     catch (Exception exc) {
/*      */       
/*   71 */       if (!isDetached())
/*      */       {
/*   73 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*   75 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void close() throws SQLException {
/*      */     try {
/*   83 */       if (!isDetached())
/*      */       {
/*   85 */         NewPooledConnection npc = this.parentPooledConnection;
/*   86 */         detach();
/*   87 */         npc.markClosedProxyConnection(this, this.txn_known_resolved);
/*   88 */         this.inner = null;
/*      */       }
/*   90 */       else if (logger.isLoggable(MLevel.FINE))
/*      */       {
/*   92 */         logger.log(MLevel.FINE, this + ": close() called more than once.");
/*      */       }
/*      */     
/*   95 */     } catch (NullPointerException exc) {
/*      */       
/*   97 */       if (isDetached()) {
/*      */         
/*   99 */         if (logger.isLoggable(MLevel.FINE))
/*      */         {
/*  101 */           logger.log(MLevel.FINE, this + ": close() called more than once.");
/*      */         }
/*      */       } else {
/*  104 */         throw exc;
/*      */       } 
/*  106 */     } catch (Exception exc) {
/*      */       
/*  108 */       if (!isDetached())
/*      */       {
/*  110 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  112 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized boolean isReadOnly() throws SQLException {
/*      */     try {
/*  120 */       this.txn_known_resolved = false;
/*      */       
/*  122 */       return this.inner.isReadOnly();
/*      */     }
/*  124 */     catch (NullPointerException exc) {
/*      */       
/*  126 */       if (isDetached())
/*      */       {
/*  128 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*  130 */       throw exc;
/*      */     }
/*  132 */     catch (Exception exc) {
/*      */       
/*  134 */       if (!isDetached())
/*      */       {
/*  136 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  138 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void abort(Executor a) throws SQLException {
/*      */     try {
/*  146 */       this.txn_known_resolved = false;
/*      */       
/*  148 */       this.inner.abort(a);
/*      */     }
/*  150 */     catch (NullPointerException exc) {
/*      */       
/*  152 */       if (isDetached())
/*      */       {
/*  154 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*  156 */       throw exc;
/*      */     }
/*  158 */     catch (Exception exc) {
/*      */       
/*  160 */       if (!isDetached())
/*      */       {
/*  162 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  164 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Statement createStatement(int a, int b) throws SQLException {
/*      */     try {
/*  172 */       this.txn_known_resolved = false;
/*      */       
/*  174 */       Statement innerStmt = this.inner.createStatement(a, b);
/*  175 */       this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
/*  176 */       return new NewProxyStatement(innerStmt, this.parentPooledConnection, false, this);
/*      */     }
/*  178 */     catch (NullPointerException exc) {
/*      */       
/*  180 */       if (isDetached())
/*      */       {
/*  182 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*  184 */       throw exc;
/*      */     }
/*  186 */     catch (Exception exc) {
/*      */       
/*  188 */       if (!isDetached())
/*      */       {
/*  190 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  192 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Statement createStatement(int a, int b, int c) throws SQLException {
/*      */     try {
/*  200 */       this.txn_known_resolved = false;
/*      */       
/*  202 */       Statement innerStmt = this.inner.createStatement(a, b, c);
/*  203 */       this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
/*  204 */       return new NewProxyStatement(innerStmt, this.parentPooledConnection, false, this);
/*      */     }
/*  206 */     catch (NullPointerException exc) {
/*      */       
/*  208 */       if (isDetached())
/*      */       {
/*  210 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*  212 */       throw exc;
/*      */     }
/*  214 */     catch (Exception exc) {
/*      */       
/*  216 */       if (!isDetached())
/*      */       {
/*  218 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  220 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Statement createStatement() throws SQLException {
/*      */     try {
/*  228 */       this.txn_known_resolved = false;
/*      */       
/*  230 */       Statement innerStmt = this.inner.createStatement();
/*  231 */       this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
/*  232 */       return new NewProxyStatement(innerStmt, this.parentPooledConnection, false, this);
/*      */     }
/*  234 */     catch (NullPointerException exc) {
/*      */       
/*  236 */       if (isDetached())
/*      */       {
/*  238 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*  240 */       throw exc;
/*      */     }
/*  242 */     catch (Exception exc) {
/*      */       
/*  244 */       if (!isDetached())
/*      */       {
/*  246 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  248 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized PreparedStatement prepareStatement(String a, int b, int c) throws SQLException {
/*      */     try {
/*  256 */       this.txn_known_resolved = false;
/*      */ 
/*      */ 
/*      */       
/*  260 */       if (this.parentPooledConnection.isStatementCaching()) {
/*      */         
/*      */         try {
/*      */           
/*  264 */           Class[] argTypes = { String.class, int.class, int.class };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  270 */           Method method = Connection.class.getMethod("prepareStatement", argTypes);
/*      */           
/*  272 */           Object[] args = { a, new Integer(b), new Integer(c) };
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  277 */           PreparedStatement preparedStatement = (PreparedStatement)this.parentPooledConnection.checkoutStatement(method, args);
/*  278 */           return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, true, this);
/*      */         }
/*  280 */         catch (ResourceClosedException e) {
/*      */           
/*  282 */           if (logger.isLoggable(MLevel.FINE))
/*  283 */             logger.log(MLevel.FINE, "A Connection tried to prepare a Statement via a Statement cache that is already closed. This can happen -- rarely -- if a DataSource is closed or reset() while Connections are checked-out and in use.", (Throwable)e); 
/*  284 */           PreparedStatement preparedStatement = this.inner.prepareStatement(a, b, c);
/*  285 */           this.parentPooledConnection.markActiveUncachedStatement(preparedStatement);
/*  286 */           return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, false, this);
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*  291 */       PreparedStatement innerStmt = this.inner.prepareStatement(a, b, c);
/*  292 */       this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
/*  293 */       return new NewProxyPreparedStatement(innerStmt, this.parentPooledConnection, false, this);
/*      */     
/*      */     }
/*  296 */     catch (NullPointerException exc) {
/*      */       
/*  298 */       if (isDetached())
/*      */       {
/*  300 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*  302 */       throw exc;
/*      */     }
/*  304 */     catch (Exception exc) {
/*      */       
/*  306 */       if (!isDetached())
/*      */       {
/*  308 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  310 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized PreparedStatement prepareStatement(String a, int[] b) throws SQLException {
/*      */     try {
/*  318 */       this.txn_known_resolved = false;
/*      */ 
/*      */ 
/*      */       
/*  322 */       if (this.parentPooledConnection.isStatementCaching()) {
/*      */         
/*      */         try {
/*      */           
/*  326 */           Class[] argTypes = { String.class, int[].class };
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  331 */           Method method = Connection.class.getMethod("prepareStatement", argTypes);
/*      */           
/*  333 */           Object[] args = { a, b };
/*      */ 
/*      */ 
/*      */           
/*  337 */           PreparedStatement preparedStatement = (PreparedStatement)this.parentPooledConnection.checkoutStatement(method, args);
/*  338 */           return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, true, this);
/*      */         }
/*  340 */         catch (ResourceClosedException e) {
/*      */           
/*  342 */           if (logger.isLoggable(MLevel.FINE))
/*  343 */             logger.log(MLevel.FINE, "A Connection tried to prepare a Statement via a Statement cache that is already closed. This can happen -- rarely -- if a DataSource is closed or reset() while Connections are checked-out and in use.", (Throwable)e); 
/*  344 */           PreparedStatement preparedStatement = this.inner.prepareStatement(a, b);
/*  345 */           this.parentPooledConnection.markActiveUncachedStatement(preparedStatement);
/*  346 */           return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, false, this);
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*  351 */       PreparedStatement innerStmt = this.inner.prepareStatement(a, b);
/*  352 */       this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
/*  353 */       return new NewProxyPreparedStatement(innerStmt, this.parentPooledConnection, false, this);
/*      */     
/*      */     }
/*  356 */     catch (NullPointerException exc) {
/*      */       
/*  358 */       if (isDetached())
/*      */       {
/*  360 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*  362 */       throw exc;
/*      */     }
/*  364 */     catch (Exception exc) {
/*      */       
/*  366 */       if (!isDetached())
/*      */       {
/*  368 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  370 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized PreparedStatement prepareStatement(String a, String[] b) throws SQLException {
/*      */     try {
/*  378 */       this.txn_known_resolved = false;
/*      */ 
/*      */ 
/*      */       
/*  382 */       if (this.parentPooledConnection.isStatementCaching()) {
/*      */         
/*      */         try {
/*      */           
/*  386 */           Class[] argTypes = { String.class, String[].class };
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  391 */           Method method = Connection.class.getMethod("prepareStatement", argTypes);
/*      */           
/*  393 */           Object[] args = { a, b };
/*      */ 
/*      */ 
/*      */           
/*  397 */           PreparedStatement preparedStatement = (PreparedStatement)this.parentPooledConnection.checkoutStatement(method, args);
/*  398 */           return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, true, this);
/*      */         }
/*  400 */         catch (ResourceClosedException e) {
/*      */           
/*  402 */           if (logger.isLoggable(MLevel.FINE))
/*  403 */             logger.log(MLevel.FINE, "A Connection tried to prepare a Statement via a Statement cache that is already closed. This can happen -- rarely -- if a DataSource is closed or reset() while Connections are checked-out and in use.", (Throwable)e); 
/*  404 */           PreparedStatement preparedStatement = this.inner.prepareStatement(a, b);
/*  405 */           this.parentPooledConnection.markActiveUncachedStatement(preparedStatement);
/*  406 */           return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, false, this);
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*  411 */       PreparedStatement innerStmt = this.inner.prepareStatement(a, b);
/*  412 */       this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
/*  413 */       return new NewProxyPreparedStatement(innerStmt, this.parentPooledConnection, false, this);
/*      */     
/*      */     }
/*  416 */     catch (NullPointerException exc) {
/*      */       
/*  418 */       if (isDetached())
/*      */       {
/*  420 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*  422 */       throw exc;
/*      */     }
/*  424 */     catch (Exception exc) {
/*      */       
/*  426 */       if (!isDetached())
/*      */       {
/*  428 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  430 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized PreparedStatement prepareStatement(String a) throws SQLException {
/*      */     try {
/*  438 */       this.txn_known_resolved = false;
/*      */ 
/*      */ 
/*      */       
/*  442 */       if (this.parentPooledConnection.isStatementCaching()) {
/*      */         
/*      */         try {
/*      */           
/*  446 */           Class[] argTypes = { String.class };
/*      */ 
/*      */ 
/*      */           
/*  450 */           Method method = Connection.class.getMethod("prepareStatement", argTypes);
/*      */           
/*  452 */           Object[] args = { a };
/*      */ 
/*      */           
/*  455 */           PreparedStatement preparedStatement = (PreparedStatement)this.parentPooledConnection.checkoutStatement(method, args);
/*  456 */           return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, true, this);
/*      */         }
/*  458 */         catch (ResourceClosedException e) {
/*      */           
/*  460 */           if (logger.isLoggable(MLevel.FINE))
/*  461 */             logger.log(MLevel.FINE, "A Connection tried to prepare a Statement via a Statement cache that is already closed. This can happen -- rarely -- if a DataSource is closed or reset() while Connections are checked-out and in use.", (Throwable)e); 
/*  462 */           PreparedStatement preparedStatement = this.inner.prepareStatement(a);
/*  463 */           this.parentPooledConnection.markActiveUncachedStatement(preparedStatement);
/*  464 */           return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, false, this);
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*  469 */       PreparedStatement innerStmt = this.inner.prepareStatement(a);
/*  470 */       this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
/*  471 */       return new NewProxyPreparedStatement(innerStmt, this.parentPooledConnection, false, this);
/*      */     
/*      */     }
/*  474 */     catch (NullPointerException exc) {
/*      */       
/*  476 */       if (isDetached())
/*      */       {
/*  478 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*  480 */       throw exc;
/*      */     }
/*  482 */     catch (Exception exc) {
/*      */       
/*  484 */       if (!isDetached())
/*      */       {
/*  486 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  488 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized PreparedStatement prepareStatement(String a, int b, int c, int d) throws SQLException {
/*      */     try {
/*  496 */       this.txn_known_resolved = false;
/*      */ 
/*      */ 
/*      */       
/*  500 */       if (this.parentPooledConnection.isStatementCaching()) {
/*      */         
/*      */         try {
/*      */           
/*  504 */           Class[] argTypes = { String.class, int.class, int.class, int.class };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  511 */           Method method = Connection.class.getMethod("prepareStatement", argTypes);
/*      */           
/*  513 */           Object[] args = { a, new Integer(b), new Integer(c), new Integer(d) };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  519 */           PreparedStatement preparedStatement = (PreparedStatement)this.parentPooledConnection.checkoutStatement(method, args);
/*  520 */           return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, true, this);
/*      */         }
/*  522 */         catch (ResourceClosedException e) {
/*      */           
/*  524 */           if (logger.isLoggable(MLevel.FINE))
/*  525 */             logger.log(MLevel.FINE, "A Connection tried to prepare a Statement via a Statement cache that is already closed. This can happen -- rarely -- if a DataSource is closed or reset() while Connections are checked-out and in use.", (Throwable)e); 
/*  526 */           PreparedStatement preparedStatement = this.inner.prepareStatement(a, b, c, d);
/*  527 */           this.parentPooledConnection.markActiveUncachedStatement(preparedStatement);
/*  528 */           return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, false, this);
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*  533 */       PreparedStatement innerStmt = this.inner.prepareStatement(a, b, c, d);
/*  534 */       this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
/*  535 */       return new NewProxyPreparedStatement(innerStmt, this.parentPooledConnection, false, this);
/*      */     
/*      */     }
/*  538 */     catch (NullPointerException exc) {
/*      */       
/*  540 */       if (isDetached())
/*      */       {
/*  542 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*  544 */       throw exc;
/*      */     }
/*  546 */     catch (Exception exc) {
/*      */       
/*  548 */       if (!isDetached())
/*      */       {
/*  550 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  552 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized PreparedStatement prepareStatement(String a, int b) throws SQLException {
/*      */     try {
/*  560 */       this.txn_known_resolved = false;
/*      */ 
/*      */ 
/*      */       
/*  564 */       if (this.parentPooledConnection.isStatementCaching()) {
/*      */         
/*      */         try {
/*      */           
/*  568 */           Class[] argTypes = { String.class, int.class };
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  573 */           Method method = Connection.class.getMethod("prepareStatement", argTypes);
/*      */           
/*  575 */           Object[] args = { a, new Integer(b) };
/*      */ 
/*      */ 
/*      */           
/*  579 */           PreparedStatement preparedStatement = (PreparedStatement)this.parentPooledConnection.checkoutStatement(method, args);
/*  580 */           return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, true, this);
/*      */         }
/*  582 */         catch (ResourceClosedException e) {
/*      */           
/*  584 */           if (logger.isLoggable(MLevel.FINE))
/*  585 */             logger.log(MLevel.FINE, "A Connection tried to prepare a Statement via a Statement cache that is already closed. This can happen -- rarely -- if a DataSource is closed or reset() while Connections are checked-out and in use.", (Throwable)e); 
/*  586 */           PreparedStatement preparedStatement = this.inner.prepareStatement(a, b);
/*  587 */           this.parentPooledConnection.markActiveUncachedStatement(preparedStatement);
/*  588 */           return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, false, this);
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*  593 */       PreparedStatement innerStmt = this.inner.prepareStatement(a, b);
/*  594 */       this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
/*  595 */       return new NewProxyPreparedStatement(innerStmt, this.parentPooledConnection, false, this);
/*      */     
/*      */     }
/*  598 */     catch (NullPointerException exc) {
/*      */       
/*  600 */       if (isDetached())
/*      */       {
/*  602 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*  604 */       throw exc;
/*      */     }
/*  606 */     catch (Exception exc) {
/*      */       
/*  608 */       if (!isDetached())
/*      */       {
/*  610 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  612 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized CallableStatement prepareCall(String a, int b, int c) throws SQLException {
/*      */     try {
/*  620 */       this.txn_known_resolved = false;
/*      */ 
/*      */ 
/*      */       
/*  624 */       if (this.parentPooledConnection.isStatementCaching()) {
/*      */         
/*      */         try {
/*      */           
/*  628 */           Class[] argTypes = { String.class, int.class, int.class };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  634 */           Method method = Connection.class.getMethod("prepareCall", argTypes);
/*      */           
/*  636 */           Object[] args = { a, new Integer(b), new Integer(c) };
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  641 */           CallableStatement callableStatement = (CallableStatement)this.parentPooledConnection.checkoutStatement(method, args);
/*  642 */           return new NewProxyCallableStatement(callableStatement, this.parentPooledConnection, true, this);
/*      */         }
/*  644 */         catch (ResourceClosedException e) {
/*      */           
/*  646 */           if (logger.isLoggable(MLevel.FINE))
/*  647 */             logger.log(MLevel.FINE, "A Connection tried to prepare a CallableStatement via a Statement cache that is already closed. This can happen -- rarely -- if a DataSource is closed or reset() while Connections are checked-out and in use.", (Throwable)e); 
/*  648 */           CallableStatement callableStatement = this.inner.prepareCall(a, b, c);
/*  649 */           this.parentPooledConnection.markActiveUncachedStatement(callableStatement);
/*  650 */           return new NewProxyCallableStatement(callableStatement, this.parentPooledConnection, false, this);
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*  655 */       CallableStatement innerStmt = this.inner.prepareCall(a, b, c);
/*  656 */       this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
/*  657 */       return new NewProxyCallableStatement(innerStmt, this.parentPooledConnection, false, this);
/*      */     
/*      */     }
/*  660 */     catch (NullPointerException exc) {
/*      */       
/*  662 */       if (isDetached())
/*      */       {
/*  664 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*  666 */       throw exc;
/*      */     }
/*  668 */     catch (Exception exc) {
/*      */       
/*  670 */       if (!isDetached())
/*      */       {
/*  672 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  674 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized CallableStatement prepareCall(String a) throws SQLException {
/*      */     try {
/*  682 */       this.txn_known_resolved = false;
/*      */ 
/*      */ 
/*      */       
/*  686 */       if (this.parentPooledConnection.isStatementCaching()) {
/*      */         
/*      */         try {
/*      */           
/*  690 */           Class[] argTypes = { String.class };
/*      */ 
/*      */ 
/*      */           
/*  694 */           Method method = Connection.class.getMethod("prepareCall", argTypes);
/*      */           
/*  696 */           Object[] args = { a };
/*      */ 
/*      */           
/*  699 */           CallableStatement callableStatement = (CallableStatement)this.parentPooledConnection.checkoutStatement(method, args);
/*  700 */           return new NewProxyCallableStatement(callableStatement, this.parentPooledConnection, true, this);
/*      */         }
/*  702 */         catch (ResourceClosedException e) {
/*      */           
/*  704 */           if (logger.isLoggable(MLevel.FINE))
/*  705 */             logger.log(MLevel.FINE, "A Connection tried to prepare a CallableStatement via a Statement cache that is already closed. This can happen -- rarely -- if a DataSource is closed or reset() while Connections are checked-out and in use.", (Throwable)e); 
/*  706 */           CallableStatement callableStatement = this.inner.prepareCall(a);
/*  707 */           this.parentPooledConnection.markActiveUncachedStatement(callableStatement);
/*  708 */           return new NewProxyCallableStatement(callableStatement, this.parentPooledConnection, false, this);
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*  713 */       CallableStatement innerStmt = this.inner.prepareCall(a);
/*  714 */       this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
/*  715 */       return new NewProxyCallableStatement(innerStmt, this.parentPooledConnection, false, this);
/*      */     
/*      */     }
/*  718 */     catch (NullPointerException exc) {
/*      */       
/*  720 */       if (isDetached())
/*      */       {
/*  722 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*  724 */       throw exc;
/*      */     }
/*  726 */     catch (Exception exc) {
/*      */       
/*  728 */       if (!isDetached())
/*      */       {
/*  730 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  732 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized CallableStatement prepareCall(String a, int b, int c, int d) throws SQLException {
/*      */     try {
/*  740 */       this.txn_known_resolved = false;
/*      */ 
/*      */ 
/*      */       
/*  744 */       if (this.parentPooledConnection.isStatementCaching()) {
/*      */         
/*      */         try {
/*      */           
/*  748 */           Class[] argTypes = { String.class, int.class, int.class, int.class };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  755 */           Method method = Connection.class.getMethod("prepareCall", argTypes);
/*      */           
/*  757 */           Object[] args = { a, new Integer(b), new Integer(c), new Integer(d) };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  763 */           CallableStatement callableStatement = (CallableStatement)this.parentPooledConnection.checkoutStatement(method, args);
/*  764 */           return new NewProxyCallableStatement(callableStatement, this.parentPooledConnection, true, this);
/*      */         }
/*  766 */         catch (ResourceClosedException e) {
/*      */           
/*  768 */           if (logger.isLoggable(MLevel.FINE))
/*  769 */             logger.log(MLevel.FINE, "A Connection tried to prepare a CallableStatement via a Statement cache that is already closed. This can happen -- rarely -- if a DataSource is closed or reset() while Connections are checked-out and in use.", (Throwable)e); 
/*  770 */           CallableStatement callableStatement = this.inner.prepareCall(a, b, c, d);
/*  771 */           this.parentPooledConnection.markActiveUncachedStatement(callableStatement);
/*  772 */           return new NewProxyCallableStatement(callableStatement, this.parentPooledConnection, false, this);
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*  777 */       CallableStatement innerStmt = this.inner.prepareCall(a, b, c, d);
/*  778 */       this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
/*  779 */       return new NewProxyCallableStatement(innerStmt, this.parentPooledConnection, false, this);
/*      */     
/*      */     }
/*  782 */     catch (NullPointerException exc) {
/*      */       
/*  784 */       if (isDetached())
/*      */       {
/*  786 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*  788 */       throw exc;
/*      */     }
/*  790 */     catch (Exception exc) {
/*      */       
/*  792 */       if (!isDetached())
/*      */       {
/*  794 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  796 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized DatabaseMetaData getMetaData() throws SQLException {
/*      */     try {
/*  804 */       this.txn_known_resolved = false;
/*      */       
/*  806 */       if (this.metaData == null) {
/*      */         
/*  808 */         DatabaseMetaData innerMetaData = this.inner.getMetaData();
/*  809 */         this.metaData = new NewProxyDatabaseMetaData(innerMetaData, this.parentPooledConnection, this);
/*      */       } 
/*  811 */       return this.metaData;
/*      */     }
/*  813 */     catch (NullPointerException exc) {
/*      */       
/*  815 */       if (isDetached())
/*      */       {
/*  817 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*  819 */       throw exc;
/*      */     }
/*  821 */     catch (Exception exc) {
/*      */       
/*  823 */       if (!isDetached())
/*      */       {
/*  825 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  827 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setTransactionIsolation(int a) throws SQLException {
/*      */     try {
/*  835 */       this.inner.setTransactionIsolation(a);
/*  836 */       this.parentPooledConnection.markNewTxnIsolation(a);
/*      */     }
/*  838 */     catch (NullPointerException exc) {
/*      */       
/*  840 */       if (isDetached())
/*      */       {
/*  842 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*  844 */       throw exc;
/*      */     }
/*  846 */     catch (Exception exc) {
/*      */       
/*  848 */       if (!isDetached())
/*      */       {
/*  850 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  852 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setCatalog(String a) throws SQLException {
/*      */     try {
/*  860 */       this.inner.setCatalog(a);
/*  861 */       this.parentPooledConnection.markNewCatalog(a);
/*      */     }
/*  863 */     catch (NullPointerException exc) {
/*      */       
/*  865 */       if (isDetached())
/*      */       {
/*  867 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*  869 */       throw exc;
/*      */     }
/*  871 */     catch (Exception exc) {
/*      */       
/*  873 */       if (!isDetached())
/*      */       {
/*  875 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  877 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setHoldability(int a) throws SQLException {
/*      */     try {
/*  885 */       this.inner.setHoldability(a);
/*  886 */       this.parentPooledConnection.markNewHoldability(a);
/*      */     }
/*  888 */     catch (NullPointerException exc) {
/*      */       
/*  890 */       if (isDetached())
/*      */       {
/*  892 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*  894 */       throw exc;
/*      */     }
/*  896 */     catch (Exception exc) {
/*      */       
/*  898 */       if (!isDetached())
/*      */       {
/*  900 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  902 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setTypeMap(Map<String, Class<?>> a) throws SQLException {
/*      */     try {
/*  910 */       this.inner.setTypeMap(a);
/*  911 */       this.parentPooledConnection.markNewTypeMap(a);
/*      */     }
/*  913 */     catch (NullPointerException exc) {
/*      */       
/*  915 */       if (isDetached())
/*      */       {
/*  917 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*  919 */       throw exc;
/*      */     }
/*  921 */     catch (Exception exc) {
/*      */       
/*  923 */       if (!isDetached())
/*      */       {
/*  925 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  927 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized SQLWarning getWarnings() throws SQLException {
/*      */     try {
/*  935 */       return this.inner.getWarnings();
/*      */     }
/*  937 */     catch (NullPointerException exc) {
/*      */       
/*  939 */       if (isDetached())
/*      */       {
/*  941 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*  943 */       throw exc;
/*      */     }
/*  945 */     catch (Exception exc) {
/*      */       
/*  947 */       if (!isDetached())
/*      */       {
/*  949 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  951 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void clearWarnings() throws SQLException {
/*      */     try {
/*  959 */       this.inner.clearWarnings();
/*      */     }
/*  961 */     catch (NullPointerException exc) {
/*      */       
/*  963 */       if (isDetached())
/*      */       {
/*  965 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*  967 */       throw exc;
/*      */     }
/*  969 */     catch (Exception exc) {
/*      */       
/*  971 */       if (!isDetached())
/*      */       {
/*  973 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  975 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized boolean isClosed() throws SQLException {
/*      */     try {
/*  983 */       return isDetached();
/*      */     }
/*  985 */     catch (NullPointerException exc) {
/*      */       
/*  987 */       if (isDetached())
/*      */       {
/*  989 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/*  991 */       throw exc;
/*      */     }
/*  993 */     catch (Exception exc) {
/*      */       
/*  995 */       if (!isDetached())
/*      */       {
/*  997 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  999 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void commit() throws SQLException {
/*      */     try {
/* 1007 */       this.inner.commit();
/*      */       
/* 1009 */       this.txn_known_resolved = true;
/*      */     }
/* 1011 */     catch (NullPointerException exc) {
/*      */       
/* 1013 */       if (isDetached())
/*      */       {
/* 1015 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1017 */       throw exc;
/*      */     }
/* 1019 */     catch (Exception exc) {
/*      */       
/* 1021 */       if (!isDetached())
/*      */       {
/* 1023 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1025 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void rollback() throws SQLException {
/*      */     try {
/* 1033 */       this.inner.rollback();
/*      */       
/* 1035 */       this.txn_known_resolved = true;
/*      */     }
/* 1037 */     catch (NullPointerException exc) {
/*      */       
/* 1039 */       if (isDetached())
/*      */       {
/* 1041 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1043 */       throw exc;
/*      */     }
/* 1045 */     catch (Exception exc) {
/*      */       
/* 1047 */       if (!isDetached())
/*      */       {
/* 1049 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1051 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void rollback(Savepoint a) throws SQLException {
/*      */     try {
/* 1059 */       this.inner.rollback(a);
/*      */       
/* 1061 */       this.txn_known_resolved = true;
/*      */     }
/* 1063 */     catch (NullPointerException exc) {
/*      */       
/* 1065 */       if (isDetached())
/*      */       {
/* 1067 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1069 */       throw exc;
/*      */     }
/* 1071 */     catch (Exception exc) {
/*      */       
/* 1073 */       if (!isDetached())
/*      */       {
/* 1075 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1077 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setAutoCommit(boolean a) throws SQLException {
/*      */     try {
/* 1085 */       this.inner.setAutoCommit(a);
/*      */       
/* 1087 */       this.txn_known_resolved = true;
/*      */     }
/* 1089 */     catch (NullPointerException exc) {
/*      */       
/* 1091 */       if (isDetached())
/*      */       {
/* 1093 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1095 */       throw exc;
/*      */     }
/* 1097 */     catch (Exception exc) {
/*      */       
/* 1099 */       if (!isDetached())
/*      */       {
/* 1101 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1103 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setClientInfo(Properties a) throws SQLClientInfoException {
/*      */     try {
/*      */       try {
/* 1113 */         this.txn_known_resolved = false;
/*      */         
/* 1115 */         this.inner.setClientInfo(a);
/*      */       }
/* 1117 */       catch (NullPointerException exc) {
/*      */         
/* 1119 */         if (isDetached())
/*      */         {
/* 1121 */           throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */         }
/* 1123 */         throw exc;
/*      */       }
/* 1125 */       catch (Exception exc) {
/*      */         
/* 1127 */         if (!isDetached())
/*      */         {
/* 1129 */           throw this.parentPooledConnection.handleThrowable(exc);
/*      */         }
/* 1131 */         throw SqlUtils.toSQLException(exc);
/*      */       }
/*      */     
/* 1134 */     } catch (Exception e) {
/* 1135 */       throw SqlUtils.toSQLClientInfoException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setClientInfo(String a, String b) throws SQLClientInfoException {
/*      */     try {
/*      */       try {
/* 1144 */         this.txn_known_resolved = false;
/*      */         
/* 1146 */         this.inner.setClientInfo(a, b);
/*      */       }
/* 1148 */       catch (NullPointerException exc) {
/*      */         
/* 1150 */         if (isDetached())
/*      */         {
/* 1152 */           throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */         }
/* 1154 */         throw exc;
/*      */       }
/* 1156 */       catch (Exception exc) {
/*      */         
/* 1158 */         if (!isDetached())
/*      */         {
/* 1160 */           throw this.parentPooledConnection.handleThrowable(exc);
/*      */         }
/* 1162 */         throw SqlUtils.toSQLException(exc);
/*      */       }
/*      */     
/* 1165 */     } catch (Exception e) {
/* 1166 */       throw SqlUtils.toSQLClientInfoException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean isValid(int a) throws SQLException {
/*      */     try {
/* 1173 */       if (isDetached()) return false; 
/* 1174 */       return this.inner.isValid(a);
/*      */     }
/* 1176 */     catch (NullPointerException exc) {
/*      */       
/* 1178 */       if (isDetached())
/*      */       {
/* 1180 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1182 */       throw exc;
/*      */     }
/* 1184 */     catch (Exception exc) {
/*      */       
/* 1186 */       if (!isDetached())
/*      */       {
/* 1188 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1190 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized String nativeSQL(String a) throws SQLException {
/*      */     try {
/* 1198 */       this.txn_known_resolved = false;
/*      */       
/* 1200 */       return this.inner.nativeSQL(a);
/*      */     }
/* 1202 */     catch (NullPointerException exc) {
/*      */       
/* 1204 */       if (isDetached())
/*      */       {
/* 1206 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1208 */       throw exc;
/*      */     }
/* 1210 */     catch (Exception exc) {
/*      */       
/* 1212 */       if (!isDetached())
/*      */       {
/* 1214 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1216 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized boolean getAutoCommit() throws SQLException {
/*      */     try {
/* 1224 */       this.txn_known_resolved = false;
/*      */       
/* 1226 */       return this.inner.getAutoCommit();
/*      */     }
/* 1228 */     catch (NullPointerException exc) {
/*      */       
/* 1230 */       if (isDetached())
/*      */       {
/* 1232 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1234 */       throw exc;
/*      */     }
/* 1236 */     catch (Exception exc) {
/*      */       
/* 1238 */       if (!isDetached())
/*      */       {
/* 1240 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1242 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized String getCatalog() throws SQLException {
/*      */     try {
/* 1250 */       this.txn_known_resolved = false;
/*      */       
/* 1252 */       return this.inner.getCatalog();
/*      */     }
/* 1254 */     catch (NullPointerException exc) {
/*      */       
/* 1256 */       if (isDetached())
/*      */       {
/* 1258 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1260 */       throw exc;
/*      */     }
/* 1262 */     catch (Exception exc) {
/*      */       
/* 1264 */       if (!isDetached())
/*      */       {
/* 1266 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1268 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized int getTransactionIsolation() throws SQLException {
/*      */     try {
/* 1276 */       this.txn_known_resolved = false;
/*      */       
/* 1278 */       return this.inner.getTransactionIsolation();
/*      */     }
/* 1280 */     catch (NullPointerException exc) {
/*      */       
/* 1282 */       if (isDetached())
/*      */       {
/* 1284 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1286 */       throw exc;
/*      */     }
/* 1288 */     catch (Exception exc) {
/*      */       
/* 1290 */       if (!isDetached())
/*      */       {
/* 1292 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1294 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Map getTypeMap() throws SQLException {
/*      */     try {
/* 1302 */       this.txn_known_resolved = false;
/*      */       
/* 1304 */       return this.inner.getTypeMap();
/*      */     }
/* 1306 */     catch (NullPointerException exc) {
/*      */       
/* 1308 */       if (isDetached())
/*      */       {
/* 1310 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1312 */       throw exc;
/*      */     }
/* 1314 */     catch (Exception exc) {
/*      */       
/* 1316 */       if (!isDetached())
/*      */       {
/* 1318 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1320 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized int getHoldability() throws SQLException {
/*      */     try {
/* 1328 */       this.txn_known_resolved = false;
/*      */       
/* 1330 */       return this.inner.getHoldability();
/*      */     }
/* 1332 */     catch (NullPointerException exc) {
/*      */       
/* 1334 */       if (isDetached())
/*      */       {
/* 1336 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1338 */       throw exc;
/*      */     }
/* 1340 */     catch (Exception exc) {
/*      */       
/* 1342 */       if (!isDetached())
/*      */       {
/* 1344 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1346 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Savepoint setSavepoint(String a) throws SQLException {
/*      */     try {
/* 1354 */       this.txn_known_resolved = false;
/*      */       
/* 1356 */       return this.inner.setSavepoint(a);
/*      */     }
/* 1358 */     catch (NullPointerException exc) {
/*      */       
/* 1360 */       if (isDetached())
/*      */       {
/* 1362 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1364 */       throw exc;
/*      */     }
/* 1366 */     catch (Exception exc) {
/*      */       
/* 1368 */       if (!isDetached())
/*      */       {
/* 1370 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1372 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Savepoint setSavepoint() throws SQLException {
/*      */     try {
/* 1380 */       this.txn_known_resolved = false;
/*      */       
/* 1382 */       return this.inner.setSavepoint();
/*      */     }
/* 1384 */     catch (NullPointerException exc) {
/*      */       
/* 1386 */       if (isDetached())
/*      */       {
/* 1388 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1390 */       throw exc;
/*      */     }
/* 1392 */     catch (Exception exc) {
/*      */       
/* 1394 */       if (!isDetached())
/*      */       {
/* 1396 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1398 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void releaseSavepoint(Savepoint a) throws SQLException {
/*      */     try {
/* 1406 */       this.txn_known_resolved = false;
/*      */       
/* 1408 */       this.inner.releaseSavepoint(a);
/*      */     }
/* 1410 */     catch (NullPointerException exc) {
/*      */       
/* 1412 */       if (isDetached())
/*      */       {
/* 1414 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1416 */       throw exc;
/*      */     }
/* 1418 */     catch (Exception exc) {
/*      */       
/* 1420 */       if (!isDetached())
/*      */       {
/* 1422 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1424 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Clob createClob() throws SQLException {
/*      */     try {
/* 1432 */       this.txn_known_resolved = false;
/*      */       
/* 1434 */       return this.inner.createClob();
/*      */     }
/* 1436 */     catch (NullPointerException exc) {
/*      */       
/* 1438 */       if (isDetached())
/*      */       {
/* 1440 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1442 */       throw exc;
/*      */     }
/* 1444 */     catch (Exception exc) {
/*      */       
/* 1446 */       if (!isDetached())
/*      */       {
/* 1448 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1450 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Blob createBlob() throws SQLException {
/*      */     try {
/* 1458 */       this.txn_known_resolved = false;
/*      */       
/* 1460 */       return this.inner.createBlob();
/*      */     }
/* 1462 */     catch (NullPointerException exc) {
/*      */       
/* 1464 */       if (isDetached())
/*      */       {
/* 1466 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1468 */       throw exc;
/*      */     }
/* 1470 */     catch (Exception exc) {
/*      */       
/* 1472 */       if (!isDetached())
/*      */       {
/* 1474 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1476 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized NClob createNClob() throws SQLException {
/*      */     try {
/* 1484 */       this.txn_known_resolved = false;
/*      */       
/* 1486 */       return this.inner.createNClob();
/*      */     }
/* 1488 */     catch (NullPointerException exc) {
/*      */       
/* 1490 */       if (isDetached())
/*      */       {
/* 1492 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1494 */       throw exc;
/*      */     }
/* 1496 */     catch (Exception exc) {
/*      */       
/* 1498 */       if (!isDetached())
/*      */       {
/* 1500 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1502 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized SQLXML createSQLXML() throws SQLException {
/*      */     try {
/* 1510 */       this.txn_known_resolved = false;
/*      */       
/* 1512 */       return this.inner.createSQLXML();
/*      */     }
/* 1514 */     catch (NullPointerException exc) {
/*      */       
/* 1516 */       if (isDetached())
/*      */       {
/* 1518 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1520 */       throw exc;
/*      */     }
/* 1522 */     catch (Exception exc) {
/*      */       
/* 1524 */       if (!isDetached())
/*      */       {
/* 1526 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1528 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Properties getClientInfo() throws SQLException {
/*      */     try {
/* 1536 */       this.txn_known_resolved = false;
/*      */       
/* 1538 */       return this.inner.getClientInfo();
/*      */     }
/* 1540 */     catch (NullPointerException exc) {
/*      */       
/* 1542 */       if (isDetached())
/*      */       {
/* 1544 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1546 */       throw exc;
/*      */     }
/* 1548 */     catch (Exception exc) {
/*      */       
/* 1550 */       if (!isDetached())
/*      */       {
/* 1552 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1554 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized String getClientInfo(String a) throws SQLException {
/*      */     try {
/* 1562 */       this.txn_known_resolved = false;
/*      */       
/* 1564 */       return this.inner.getClientInfo(a);
/*      */     }
/* 1566 */     catch (NullPointerException exc) {
/*      */       
/* 1568 */       if (isDetached())
/*      */       {
/* 1570 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1572 */       throw exc;
/*      */     }
/* 1574 */     catch (Exception exc) {
/*      */       
/* 1576 */       if (!isDetached())
/*      */       {
/* 1578 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1580 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Array createArrayOf(String a, Object[] b) throws SQLException {
/*      */     try {
/* 1588 */       this.txn_known_resolved = false;
/*      */       
/* 1590 */       return this.inner.createArrayOf(a, b);
/*      */     }
/* 1592 */     catch (NullPointerException exc) {
/*      */       
/* 1594 */       if (isDetached())
/*      */       {
/* 1596 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1598 */       throw exc;
/*      */     }
/* 1600 */     catch (Exception exc) {
/*      */       
/* 1602 */       if (!isDetached())
/*      */       {
/* 1604 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1606 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Struct createStruct(String a, Object[] b) throws SQLException {
/*      */     try {
/* 1614 */       this.txn_known_resolved = false;
/*      */       
/* 1616 */       return this.inner.createStruct(a, b);
/*      */     }
/* 1618 */     catch (NullPointerException exc) {
/*      */       
/* 1620 */       if (isDetached())
/*      */       {
/* 1622 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1624 */       throw exc;
/*      */     }
/* 1626 */     catch (Exception exc) {
/*      */       
/* 1628 */       if (!isDetached())
/*      */       {
/* 1630 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1632 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setSchema(String a) throws SQLException {
/*      */     try {
/* 1640 */       this.txn_known_resolved = false;
/*      */       
/* 1642 */       this.inner.setSchema(a);
/*      */     }
/* 1644 */     catch (NullPointerException exc) {
/*      */       
/* 1646 */       if (isDetached())
/*      */       {
/* 1648 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1650 */       throw exc;
/*      */     }
/* 1652 */     catch (Exception exc) {
/*      */       
/* 1654 */       if (!isDetached())
/*      */       {
/* 1656 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1658 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized String getSchema() throws SQLException {
/*      */     try {
/* 1666 */       this.txn_known_resolved = false;
/*      */       
/* 1668 */       return this.inner.getSchema();
/*      */     }
/* 1670 */     catch (NullPointerException exc) {
/*      */       
/* 1672 */       if (isDetached())
/*      */       {
/* 1674 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1676 */       throw exc;
/*      */     }
/* 1678 */     catch (Exception exc) {
/*      */       
/* 1680 */       if (!isDetached())
/*      */       {
/* 1682 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1684 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setNetworkTimeout(Executor a, int b) throws SQLException {
/*      */     try {
/* 1692 */       this.txn_known_resolved = false;
/*      */       
/* 1694 */       this.inner.setNetworkTimeout(a, b);
/*      */     }
/* 1696 */     catch (NullPointerException exc) {
/*      */       
/* 1698 */       if (isDetached())
/*      */       {
/* 1700 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1702 */       throw exc;
/*      */     }
/* 1704 */     catch (Exception exc) {
/*      */       
/* 1706 */       if (!isDetached())
/*      */       {
/* 1708 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1710 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized int getNetworkTimeout() throws SQLException {
/*      */     try {
/* 1718 */       this.txn_known_resolved = false;
/*      */       
/* 1720 */       return this.inner.getNetworkTimeout();
/*      */     }
/* 1722 */     catch (NullPointerException exc) {
/*      */       
/* 1724 */       if (isDetached())
/*      */       {
/* 1726 */         throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
/*      */       }
/* 1728 */       throw exc;
/*      */     }
/* 1730 */     catch (Exception exc) {
/*      */       
/* 1732 */       if (!isDetached())
/*      */       {
/* 1734 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1736 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Object unwrap(Class a) throws SQLException {
/* 1742 */     if (isWrapperFor(a)) return this.inner; 
/* 1743 */     throw new SQLException(this + " is not a wrapper for " + a.getName());
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean isWrapperFor(Class<Connection> a) throws SQLException {
/* 1748 */     return (Connection.class == a || a.isAssignableFrom(this.inner.getClass()));
/*      */   }
/*      */ 
/*      */   
/*      */   boolean txn_known_resolved = true;
/* 1753 */   DatabaseMetaData metaData = null;
/*      */ 
/*      */ 
/*      */   
/*      */   public Object rawConnectionOperation(Method m, Object target, Object[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
/* 1758 */     maybeDirtyTransaction();
/*      */     
/* 1760 */     if (this.inner == null)
/* 1761 */       throw new SQLException("You cannot operate on a closed Connection!"); 
/* 1762 */     if (target == C3P0ProxyConnection.RAW_CONNECTION)
/* 1763 */       target = this.inner; 
/* 1764 */     for (int i = 0, len = args.length; i < len; i++) {
/* 1765 */       if (args[i] == C3P0ProxyConnection.RAW_CONNECTION)
/* 1766 */         args[i] = this.inner; 
/* 1767 */     }  Object out = m.invoke(target, args);
/*      */ 
/*      */     
/* 1770 */     if (out instanceof CallableStatement) {
/*      */       
/* 1772 */       CallableStatement innerStmt = (CallableStatement)out;
/* 1773 */       this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
/* 1774 */       out = new NewProxyCallableStatement(innerStmt, this.parentPooledConnection, false, this);
/*      */     }
/* 1776 */     else if (out instanceof PreparedStatement) {
/*      */       
/* 1778 */       PreparedStatement innerStmt = (PreparedStatement)out;
/* 1779 */       this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
/* 1780 */       out = new NewProxyPreparedStatement(innerStmt, this.parentPooledConnection, false, this);
/*      */     }
/* 1782 */     else if (out instanceof Statement) {
/*      */       
/* 1784 */       Statement innerStmt = (Statement)out;
/* 1785 */       this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
/* 1786 */       out = new NewProxyStatement(innerStmt, this.parentPooledConnection, false, this);
/*      */     }
/* 1788 */     else if (out instanceof ResultSet) {
/*      */       
/* 1790 */       ResultSet innerRs = (ResultSet)out;
/* 1791 */       this.parentPooledConnection.markActiveRawConnectionResultSet(innerRs);
/* 1792 */       out = new NewProxyResultSet(innerRs, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 1794 */     else if (out instanceof DatabaseMetaData) {
/* 1795 */       out = new NewProxyDatabaseMetaData((DatabaseMetaData)out, this.parentPooledConnection);
/* 1796 */     }  return out;
/*      */   }
/*      */   
/*      */   synchronized void maybeDirtyTransaction() {
/* 1800 */     this.txn_known_resolved = false;
/* 1801 */   } private static final MLogger logger = MLog.getLogger("com.mchange.v2.c3p0.impl.NewProxyConnection");
/*      */   
/*      */   volatile NewPooledConnection parentPooledConnection;
/*      */   
/* 1805 */   ConnectionEventListener cel = new ConnectionEventListener()
/*      */     {
/*      */       public void connectionErrorOccurred(ConnectionEvent evt) {}
/*      */ 
/*      */       
/*      */       public void connectionClosed(ConnectionEvent evt) {
/* 1811 */         NewProxyConnection.this.detach();
/*      */       }
/*      */     };
/*      */   
/*      */   void attach(NewPooledConnection parentPooledConnection) {
/* 1816 */     this.parentPooledConnection = parentPooledConnection;
/* 1817 */     parentPooledConnection.addConnectionEventListener(this.cel);
/*      */   }
/*      */ 
/*      */   
/*      */   private void detach() {
/* 1822 */     this.parentPooledConnection.removeConnectionEventListener(this.cel);
/* 1823 */     this.parentPooledConnection = null;
/*      */   }
/*      */ 
/*      */   
/*      */   NewProxyConnection(Connection inner, NewPooledConnection parentPooledConnection) {
/* 1828 */     this(inner);
/* 1829 */     attach(parentPooledConnection);
/*      */   }
/*      */   
/*      */   boolean isDetached() {
/* 1833 */     return (this.parentPooledConnection == null);
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/NewProxyConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */