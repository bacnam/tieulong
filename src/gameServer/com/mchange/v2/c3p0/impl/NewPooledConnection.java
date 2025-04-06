/*     */ package com.mchange.v2.c3p0.impl;
/*     */ 
/*     */ import com.mchange.v2.c3p0.ConnectionCustomizer;
/*     */ import com.mchange.v2.c3p0.ConnectionTester;
/*     */ import com.mchange.v2.c3p0.FullQueryConnectionTester;
/*     */ import com.mchange.v2.c3p0.stmt.GooGooStatementCache;
/*     */ import com.mchange.v2.c3p0.util.ConnectionEventSupport;
/*     */ import com.mchange.v2.c3p0.util.StatementEventSupport;
/*     */ import com.mchange.v2.lang.ObjectUtils;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import com.mchange.v2.sql.SqlUtils;
/*     */ import java.lang.reflect.Method;
/*     */ import java.sql.Connection;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.sql.ConnectionEventListener;
/*     */ import javax.sql.StatementEventListener;
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
/*     */ public final class NewPooledConnection
/*     */   extends AbstractC3P0PooledConnection
/*     */ {
/*  52 */   private static final MLogger logger = MLog.getLogger(NewPooledConnection.class);
/*     */   
/*  54 */   private static final SQLException NORMAL_CLOSE_PLACEHOLDER = new SQLException("This pooled Connection was explicitly close()ed by a client, not invalidated due to an error.");
/*     */ 
/*     */ 
/*     */   
/*  58 */   static Set holdabilityBugKeys = null;
/*     */   
/*     */   final Connection physicalConnection;
/*     */   
/*     */   final ConnectionTester connectionTester;
/*     */   
/*     */   final boolean autoCommitOnClose;
/*     */   
/*     */   final boolean forceIgnoreUnresolvedTransactions;
/*     */   final String preferredTestQuery;
/*     */   final boolean supports_setHoldability;
/*     */   final boolean supports_setReadOnly;
/*     */   final boolean supports_setTypeMap;
/*     */   final int dflt_txn_isolation;
/*     */   final String dflt_catalog;
/*     */   final int dflt_holdability;
/*     */   final boolean dflt_readOnly;
/*     */   final Map dflt_typeMap;
/*     */   final ConnectionEventSupport ces;
/*     */   final StatementEventSupport ses;
/*  78 */   GooGooStatementCache scache = null;
/*  79 */   Throwable invalidatingException = null;
/*  80 */   int connection_status = 0;
/*  81 */   Set uncachedActiveStatements = new HashSet();
/*  82 */   Map resultSetsForStatements = new HashMap<Object, Object>();
/*  83 */   Set metaDataResultSets = new HashSet();
/*  84 */   Set rawConnectionResultSets = null;
/*     */   
/*     */   boolean connection_error_signaled = false;
/*     */   
/*  88 */   volatile NewProxyConnection exposedProxy = null;
/*     */ 
/*     */   
/*     */   volatile boolean isolation_lvl_nondefault = false;
/*     */ 
/*     */   
/*     */   volatile boolean catalog_nondefault = false;
/*     */ 
/*     */   
/*     */   volatile boolean holdability_nondefault = false;
/*     */   
/*     */   volatile boolean readOnly_nondefault = false;
/*     */   
/*     */   volatile boolean typeMap_nondefault = false;
/*     */ 
/*     */   
/*     */   public NewPooledConnection(Connection con, ConnectionTester connectionTester, boolean autoCommitOnClose, boolean forceIgnoreUnresolvedTransactions, String preferredTestQuery, ConnectionCustomizer cc, String pdsIdt) throws SQLException {
/*     */     try {
/* 106 */       if (cc != null) {
/* 107 */         cc.onAcquire(con, pdsIdt);
/*     */       }
/* 109 */     } catch (Exception e) {
/* 110 */       throw SqlUtils.toSQLException(e);
/*     */     } 
/* 112 */     this.physicalConnection = con;
/* 113 */     this.connectionTester = connectionTester;
/* 114 */     this.autoCommitOnClose = autoCommitOnClose;
/* 115 */     this.forceIgnoreUnresolvedTransactions = forceIgnoreUnresolvedTransactions;
/* 116 */     this.preferredTestQuery = preferredTestQuery;
/* 117 */     this.supports_setHoldability = C3P0ImplUtils.supportsMethod(con, "setHoldability", new Class[] { int.class });
/* 118 */     this.supports_setReadOnly = C3P0ImplUtils.supportsMethod(con, "setReadOnly", new Class[] { boolean.class });
/* 119 */     this.supports_setTypeMap = C3P0ImplUtils.supportsMethod(con, "setTypeMap", new Class[] { Map.class });
/* 120 */     this.dflt_txn_isolation = con.getTransactionIsolation();
/* 121 */     this.dflt_catalog = con.getCatalog();
/* 122 */     this.dflt_holdability = this.supports_setHoldability ? carefulCheckHoldability(con) : 2;
/* 123 */     this.dflt_readOnly = this.supports_setReadOnly ? carefulCheckReadOnly(con) : false;
/* 124 */     this.dflt_typeMap = (this.supports_setTypeMap && carefulCheckTypeMap(con) == null) ? null : Collections.EMPTY_MAP;
/* 125 */     this.ces = new ConnectionEventSupport(this);
/* 126 */     this.ses = new StatementEventSupport(this);
/*     */   }
/*     */   
/*     */   private static int carefulCheckHoldability(Connection con) {
/*     */     try {
/* 131 */       return con.getHoldability();
/* 132 */     } catch (Exception e) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 141 */       return 2;
/*     */     }
/* 143 */     catch (Error e) {
/*     */       
/* 145 */       synchronized (NewPooledConnection.class) {
/*     */         
/* 147 */         if (holdabilityBugKeys == null)
/* 148 */           holdabilityBugKeys = new HashSet(); 
/* 149 */         String hbk = holdabilityBugKey(con, e);
/* 150 */         if (!holdabilityBugKeys.contains(hbk)) {
/*     */           
/* 152 */           if (logger.isLoggable(MLevel.WARNING)) {
/* 153 */             logger.log(MLevel.WARNING, con + " threw an Error when we tried to check its default " + "holdability. This is probably due to a bug in your JDBC driver that c3p0 can harmlessly " + "work around (reported for some DB2 drivers). Please verify that the error stack trace is consistent" + "with the getHoldability() method not being properly implemented, and is not due to some deeper problem. " + "This message will not be repeated for Connections of type " + con.getClass().getName() + " that " + "provoke errors of type " + e.getClass().getName() + " when getHoldability() is called.", e);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 159 */           holdabilityBugKeys.add(hbk);
/*     */         } 
/*     */       } 
/* 162 */       return 2;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String holdabilityBugKey(Connection con, Error err) {
/* 167 */     return con.getClass().getName() + '|' + err.getClass().getName();
/*     */   }
/*     */   private static boolean carefulCheckReadOnly(Connection con) {
/*     */     try {
/* 171 */       return con.isReadOnly();
/* 172 */     } catch (Exception e) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 181 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Map carefulCheckTypeMap(Connection con) {
/*     */     try {
/* 187 */       return con.getTypeMap();
/* 188 */     } catch (Exception e) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 197 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Connection getConnection() throws SQLException {
/*     */     try {
/* 208 */       if (this.exposedProxy == null) {
/*     */         
/* 210 */         this.exposedProxy = new NewProxyConnection(this.physicalConnection, this);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 218 */       else if (logger.isLoggable(MLevel.WARNING)) {
/* 219 */         logger.log(MLevel.WARNING, "c3p0 -- Uh oh... getConnection() was called on a PooledConnection when it had already provided a client with a Connection that has not yet been closed. This probably indicates a bug in the connection pool!!!");
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 229 */       return this.exposedProxy;
/*     */     }
/* 231 */     catch (Exception e) {
/*     */       
/* 233 */       SQLException sqle = handleThrowable(e);
/* 234 */       throw sqle;
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized int getConnectionStatus() {
/* 239 */     return this.connection_status;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void closeAll() throws SQLException {
/*     */     try {
/* 245 */       closeAllCachedStatements();
/*     */     }
/* 247 */     catch (Exception e) {
/*     */       
/* 249 */       SQLException sqle = handleThrowable(e);
/* 250 */       throw sqle;
/*     */     } 
/*     */   }
/*     */   
/*     */   synchronized void closeMaybeCheckedOut(boolean checked_out) throws SQLException {
/* 255 */     close(null, checked_out);
/*     */   }
/*     */   public synchronized void close() throws SQLException {
/* 258 */     close(null);
/*     */   }
/*     */   public void addConnectionEventListener(ConnectionEventListener cel) {
/* 261 */     this.ces.addConnectionEventListener(cel);
/*     */   }
/*     */   public void removeConnectionEventListener(ConnectionEventListener cel) {
/* 264 */     this.ces.removeConnectionEventListener(cel);
/*     */   }
/*     */   public void printConnectionListeners() {
/* 267 */     this.ces.printListeners();
/*     */   }
/*     */   
/*     */   public void addStatementEventListener(StatementEventListener sel) {
/* 271 */     if (logger.isLoggable(MLevel.INFO)) {
/* 272 */       logger.info("Per the JDBC4 spec, " + getClass().getName() + " accepts StatementListeners, but for now there is no circumstance under which they are notified!");
/*     */     }
/*     */     
/* 275 */     this.ses.addStatementEventListener(sel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeStatementEventListener(StatementEventListener sel) {
/* 280 */     this.ses.removeStatementEventListener(sel);
/*     */   }
/*     */   
/*     */   public void printStatementListeners() {
/* 284 */     this.ses.printListeners();
/*     */   }
/*     */   
/*     */   public synchronized void initStatementCache(GooGooStatementCache scache) {
/* 288 */     this.scache = scache;
/*     */   }
/*     */   public synchronized GooGooStatementCache getStatementCache() {
/* 291 */     return this.scache;
/*     */   }
/*     */ 
/*     */   
/*     */   void markNewTxnIsolation(int lvl) {
/* 296 */     this.isolation_lvl_nondefault = (lvl != this.dflt_txn_isolation);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void markNewCatalog(String catalog) {
/* 302 */     this.catalog_nondefault = ObjectUtils.eqOrBothNull(catalog, this.dflt_catalog);
/*     */   }
/*     */ 
/*     */   
/*     */   void markNewHoldability(int holdability) {
/* 307 */     this.holdability_nondefault = (holdability != this.dflt_holdability);
/*     */   }
/*     */ 
/*     */   
/*     */   void markNewReadOnly(boolean readOnly) {
/* 312 */     this.readOnly_nondefault = (readOnly != this.dflt_readOnly);
/*     */   }
/*     */ 
/*     */   
/*     */   void markNewTypeMap(Map typeMap) {
/* 317 */     this.typeMap_nondefault = (typeMap != this.dflt_typeMap);
/*     */   }
/*     */   
/*     */   synchronized Object checkoutStatement(Method stmtProducingMethod, Object[] args) throws SQLException {
/* 321 */     return this.scache.checkoutStatement(this.physicalConnection, stmtProducingMethod, args);
/*     */   }
/*     */   
/*     */   synchronized void checkinStatement(Statement stmt) throws SQLException {
/* 325 */     cleanupStatementResultSets(stmt);
/* 326 */     this.scache.checkinStatement(stmt);
/*     */   }
/*     */   
/*     */   synchronized void markActiveUncachedStatement(Statement stmt) {
/* 330 */     this.uncachedActiveStatements.add(stmt);
/*     */   }
/*     */   
/*     */   synchronized void markInactiveUncachedStatement(Statement stmt) {
/* 334 */     cleanupStatementResultSets(stmt);
/* 335 */     this.uncachedActiveStatements.remove(stmt);
/*     */   }
/*     */ 
/*     */   
/*     */   synchronized void markActiveResultSetForStatement(Statement stmt, ResultSet rs) {
/* 340 */     Set<ResultSet> rss = resultSets(stmt, true);
/* 341 */     rss.add(rs);
/*     */   }
/*     */ 
/*     */   
/*     */   synchronized void markInactiveResultSetForStatement(Statement stmt, ResultSet rs) {
/* 346 */     Set rss = resultSets(stmt, false);
/* 347 */     if (rss == null) {
/*     */       
/* 349 */       if (logger.isLoggable(MLevel.FINE)) {
/* 350 */         logger.fine("ResultSet " + rs + " was apparently closed after the Statement that created it had already been closed.");
/*     */       }
/* 352 */     } else if (!rss.remove(rs)) {
/* 353 */       throw new InternalError("Marking a ResultSet inactive that we did not know was opened!");
/*     */     } 
/*     */   }
/*     */   
/*     */   synchronized void markActiveRawConnectionResultSet(ResultSet rs) {
/* 358 */     if (this.rawConnectionResultSets == null)
/* 359 */       this.rawConnectionResultSets = new HashSet(); 
/* 360 */     this.rawConnectionResultSets.add(rs);
/*     */   }
/*     */ 
/*     */   
/*     */   synchronized void markInactiveRawConnectionResultSet(ResultSet rs) {
/* 365 */     if (!this.rawConnectionResultSets.remove(rs))
/* 366 */       throw new InternalError("Marking a raw Connection ResultSet inactive that we did not know was opened!"); 
/*     */   }
/*     */   
/*     */   synchronized void markActiveMetaDataResultSet(ResultSet rs) {
/* 370 */     this.metaDataResultSets.add(rs);
/*     */   }
/*     */   synchronized void markInactiveMetaDataResultSet(ResultSet rs) {
/* 373 */     this.metaDataResultSets.remove(rs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void markClosedProxyConnection(NewProxyConnection npc, boolean txn_known_resolved) {
/* 384 */     SQLException trouble = null;
/*     */     
/*     */     try {
/* 387 */       synchronized (this)
/*     */       {
/*     */         
/*     */         try {
/* 391 */           if (npc != this.exposedProxy) {
/* 392 */             throw new InternalError("C3P0 Error: An exposed proxy asked a PooledConnection that was not its parents to clean up its resources!");
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 397 */           this.exposedProxy = null;
/*     */           
/* 399 */           List<Throwable> closeExceptions = new LinkedList();
/* 400 */           cleanupResultSets(closeExceptions);
/* 401 */           cleanupUncachedStatements(closeExceptions);
/* 402 */           checkinAllCachedStatements(closeExceptions);
/* 403 */           if (closeExceptions.size() > 0) {
/*     */ 
/*     */             
/* 406 */             if (logger.isLoggable(MLevel.INFO))
/* 407 */               logger.info("[c3p0] The following Exceptions occurred while trying to clean up a Connection's stranded resources:"); 
/* 408 */             for (Iterator<Throwable> ii = closeExceptions.iterator(); ii.hasNext(); ) {
/*     */               
/* 410 */               Throwable t = ii.next();
/*     */ 
/*     */               
/* 413 */               if (logger.isLoggable(MLevel.INFO))
/* 414 */                 logger.log(MLevel.INFO, "[c3p0 -- conection resource close Exception]", t); 
/*     */             } 
/*     */           } 
/* 417 */           reset(txn_known_resolved);
/*     */           
/* 419 */           if (closeExceptions.size() > 0) {
/* 420 */             trouble = SqlUtils.toSQLException(closeExceptions.get(0));
/*     */           }
/* 422 */         } catch (SQLException e) {
/*     */ 
/*     */           
/* 425 */           if (logger.isLoggable(MLevel.FINE)) {
/* 426 */             logger.log(MLevel.FINE, "An exception occurred while reseting a closed Connection. Invalidating Connection.", e);
/*     */           }
/* 428 */           updateConnectionStatus(-1);
/*     */           
/* 430 */           trouble = e;
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 439 */       if (trouble != null)
/*     */       {
/*     */         
/* 442 */         fireConnectionErrorOccurred(trouble);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 452 */       fireConnectionClosed();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void reset(boolean txn_known_resolved) throws SQLException {
/* 461 */     C3P0ImplUtils.resetTxnState(this.physicalConnection, this.forceIgnoreUnresolvedTransactions, this.autoCommitOnClose, txn_known_resolved);
/* 462 */     if (this.isolation_lvl_nondefault) {
/*     */       
/* 464 */       this.physicalConnection.setTransactionIsolation(this.dflt_txn_isolation);
/* 465 */       this.isolation_lvl_nondefault = false;
/*     */     } 
/*     */     
/* 468 */     if (this.catalog_nondefault) {
/*     */       
/* 470 */       this.physicalConnection.setCatalog(this.dflt_catalog);
/* 471 */       this.catalog_nondefault = false;
/*     */     } 
/* 473 */     if (this.holdability_nondefault) {
/*     */       
/* 475 */       this.physicalConnection.setHoldability(this.dflt_holdability);
/* 476 */       this.holdability_nondefault = false;
/*     */     } 
/* 478 */     if (this.readOnly_nondefault) {
/*     */       
/* 480 */       this.physicalConnection.setReadOnly(this.dflt_readOnly);
/* 481 */       this.readOnly_nondefault = false;
/*     */     } 
/* 483 */     if (this.typeMap_nondefault) {
/*     */       
/* 485 */       this.physicalConnection.setTypeMap(this.dflt_typeMap);
/* 486 */       this.typeMap_nondefault = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   synchronized boolean isStatementCaching() {
/* 491 */     return (this.scache != null);
/*     */   }
/*     */ 
/*     */   
/*     */   SQLException handleThrowable(Throwable t) {
/* 496 */     boolean fire_cxn_error = false;
/* 497 */     SQLException sqle = null;
/*     */     
/*     */     try {
/* 500 */       synchronized (this) {
/*     */         int status;
/* 502 */         if (logger.isLoggable(MLevel.FINER)) {
/* 503 */           logger.log(MLevel.FINER, this + " handling a throwable.", t);
/*     */         }
/* 505 */         sqle = SqlUtils.toSQLException(t);
/*     */ 
/*     */ 
/*     */         
/* 509 */         if (this.connectionTester instanceof FullQueryConnectionTester) {
/* 510 */           status = ((FullQueryConnectionTester)this.connectionTester).statusOnException(this.physicalConnection, sqle, this.preferredTestQuery);
/*     */         } else {
/* 512 */           status = this.connectionTester.statusOnException(this.physicalConnection, sqle);
/*     */         } 
/* 514 */         updateConnectionStatus(status);
/* 515 */         if (status != 0)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 521 */           if (logger.isLoggable(MLevel.FINE)) {
/* 522 */             logger.log(MLevel.FINE, this + " invalidated by Exception.", t);
/*     */           }
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
/* 551 */           if (!this.connection_error_signaled) {
/* 552 */             fire_cxn_error = true;
/*     */ 
/*     */ 
/*     */           
/*     */           }
/* 557 */           else if (logger.isLoggable(MLevel.WARNING)) {
/*     */             
/* 559 */             logger.log(MLevel.WARNING, "[c3p0] A PooledConnection that has already signalled a Connection error is still in use!");
/* 560 */             logger.log(MLevel.WARNING, "[c3p0] Another error has occurred [ " + t + " ] which will not be reported to listeners!", t);
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       } 
/*     */     } finally {
/*     */       
/* 568 */       if (fire_cxn_error) {
/*     */         
/* 570 */         fireConnectionErrorOccurred(sqle);
/* 571 */         this.connection_error_signaled = true;
/*     */       } 
/*     */     } 
/* 574 */     return sqle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fireConnectionClosed() {
/* 582 */     assert !Thread.holdsLock(this);
/* 583 */     this.ces.fireConnectionClosed();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void fireConnectionErrorOccurred(SQLException error) {
/* 589 */     assert !Thread.holdsLock(this);
/* 590 */     this.ces.fireConnectionErrorOccurred(error);
/*     */   }
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
/*     */   private void close(Throwable cause) throws SQLException {
/* 603 */     close(cause, false);
/*     */   }
/*     */   
/*     */   private void close(Throwable cause, boolean forced) throws SQLException {
/* 607 */     assert Thread.holdsLock(this);
/*     */     
/* 609 */     if (this.invalidatingException == null) {
/*     */       
/* 611 */       List<SQLException> closeExceptions = new LinkedList();
/*     */ 
/*     */       
/* 614 */       cleanupResultSets(closeExceptions);
/*     */ 
/*     */ 
/*     */       
/* 618 */       cleanupUncachedStatements(closeExceptions);
/*     */ 
/*     */       
/*     */       try {
/* 622 */         closeAllCachedStatements();
/* 623 */       } catch (SQLException e) {
/* 624 */         closeExceptions.add(e);
/*     */       } 
/* 626 */       if (forced) {
/*     */         
/*     */         try {
/* 629 */           C3P0ImplUtils.resetTxnState(this.physicalConnection, this.forceIgnoreUnresolvedTransactions, this.autoCommitOnClose, false);
/* 630 */         } catch (Exception e) {
/*     */           
/* 632 */           if (logger.isLoggable(MLevel.FINER)) {
/* 633 */             logger.log(MLevel.FINER, "Failed to reset the transaction state of  " + this.physicalConnection + "just prior to close(). " + "Only relevant at all if this was a Connection being forced close()ed midtransaction.", e);
/*     */           }
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 642 */         this.physicalConnection.close();
/* 643 */       } catch (SQLException e) {
/*     */         
/* 645 */         if (logger.isLoggable(MLevel.FINER)) {
/* 646 */           logger.log(MLevel.FINER, "Failed to close physical Connection: " + this.physicalConnection, e);
/*     */         }
/* 648 */         closeExceptions.add(e);
/*     */       } 
/*     */ 
/*     */       
/* 652 */       if (this.connection_status == 0)
/* 653 */         this.connection_status = -1; 
/* 654 */       if (cause == null) {
/*     */         
/* 656 */         this.invalidatingException = NORMAL_CLOSE_PLACEHOLDER;
/*     */         
/* 658 */         if (logger.isLoggable(MLevel.FINEST)) {
/* 659 */           logger.log(MLevel.FINEST, this + " closed by a client.", new Exception("DEBUG -- CLOSE BY CLIENT STACK TRACE"));
/*     */         }
/* 661 */         logCloseExceptions(null, closeExceptions);
/*     */         
/* 663 */         if (closeExceptions.size() > 0) {
/* 664 */           throw new SQLException("Some resources failed to close properly while closing " + this);
/*     */         }
/*     */       } else {
/*     */         
/* 668 */         this.invalidatingException = cause;
/*     */         
/* 670 */         logCloseExceptions(cause, closeExceptions);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cleanupResultSets(List closeExceptions) {
/* 679 */     cleanupAllStatementResultSets(closeExceptions);
/* 680 */     cleanupUnclosedResultSetsSet(this.metaDataResultSets, closeExceptions);
/* 681 */     if (this.rawConnectionResultSets != null) {
/* 682 */       cleanupUnclosedResultSetsSet(this.rawConnectionResultSets, closeExceptions);
/*     */     }
/*     */   }
/*     */   
/*     */   private void cleanupUnclosedResultSetsSet(Set rsSet, List<SQLException> closeExceptions) {
/* 687 */     for (Iterator<ResultSet> ii = rsSet.iterator(); ii.hasNext(); ) {
/*     */       
/* 689 */       ResultSet rs = ii.next();
/*     */       try {
/* 691 */         rs.close();
/* 692 */       } catch (SQLException e) {
/* 693 */         closeExceptions.add(e);
/*     */       } 
/* 695 */       ii.remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void cleanupStatementResultSets(Statement stmt) {
/* 701 */     Set rss = resultSets(stmt, false);
/* 702 */     if (rss != null)
/*     */     {
/* 704 */       for (Iterator<ResultSet> ii = rss.iterator(); ii.hasNext();) {
/*     */         
/*     */         try {
/* 707 */           ((ResultSet)ii.next()).close();
/* 708 */         } catch (Exception e) {
/*     */ 
/*     */ 
/*     */           
/* 712 */           if (logger.isLoggable(MLevel.INFO))
/* 713 */             logger.log(MLevel.INFO, "ResultSet close() failed.", e); 
/*     */         } 
/*     */       } 
/*     */     }
/* 717 */     this.resultSetsForStatements.remove(stmt);
/*     */   }
/*     */ 
/*     */   
/*     */   private void cleanupAllStatementResultSets(List<SQLException> closeExceptions) {
/* 722 */     for (Iterator ii = this.resultSetsForStatements.keySet().iterator(); ii.hasNext(); ) {
/*     */       
/* 724 */       Object stmt = ii.next();
/* 725 */       Set rss = (Set)this.resultSetsForStatements.get(stmt);
/* 726 */       for (Iterator<ResultSet> jj = rss.iterator(); jj.hasNext(); ) {
/*     */         
/* 728 */         ResultSet rs = jj.next();
/*     */         try {
/* 730 */           rs.close();
/* 731 */         } catch (SQLException e) {
/*     */           
/* 733 */           closeExceptions.add(e);
/*     */ 
/*     */           
/* 736 */           if (logger.isLoggable(MLevel.FINER)) {
/* 737 */             logger.log(MLevel.FINER, "An Exception occurred while trying to cleanup the following ResultSet: " + rs, e);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 743 */     this.resultSetsForStatements.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   private void cleanupUncachedStatements(List<SQLException> closeExceptions) {
/* 748 */     for (Iterator<Statement> ii = this.uncachedActiveStatements.iterator(); ii.hasNext(); ) {
/*     */       
/* 750 */       Statement stmt = ii.next();
/*     */       try {
/* 752 */         stmt.close();
/* 753 */       } catch (SQLException e) {
/*     */         
/* 755 */         closeExceptions.add(e);
/*     */         
/* 757 */         if (logger.isLoggable(MLevel.FINER)) {
/* 758 */           logger.log(MLevel.FINER, "An Exception occurred while trying to cleanup the following uncached Statement: " + stmt, e);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 763 */       ii.remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkinAllCachedStatements(List<SQLException> closeExceptions) {
/*     */     try {
/* 771 */       if (this.scache != null) {
/* 772 */         this.scache.checkinAll(this.physicalConnection);
/*     */       }
/* 774 */     } catch (SQLException e) {
/* 775 */       closeExceptions.add(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void closeAllCachedStatements() throws SQLException {
/* 780 */     if (this.scache != null) {
/* 781 */       this.scache.closeAll(this.physicalConnection);
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateConnectionStatus(int status) {
/* 786 */     switch (this.connection_status) {
/*     */       case -8:
/*     */         return;
/*     */ 
/*     */       
/*     */       case -1:
/* 792 */         if (status == -8) {
/* 793 */           this.connection_status = status;
/*     */         }
/*     */       case 0:
/* 796 */         if (status != 0) {
/* 797 */           this.connection_status = status;
/*     */         }
/*     */     } 
/* 800 */     throw new InternalError(this + " -- Illegal Connection Status: " + this.connection_status);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Set resultSets(Statement stmt, boolean create) {
/* 806 */     Set out = (Set)this.resultSetsForStatements.get(stmt);
/* 807 */     if (out == null && create) {
/*     */       
/* 809 */       out = new HashSet();
/* 810 */       this.resultSetsForStatements.put(stmt, out);
/*     */     } 
/* 812 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   Connection getPhysicalConnection() {
/* 817 */     return this.physicalConnection;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void logCloseExceptions(Throwable cause, Collection exceptions) {
/* 822 */     if (logger.isLoggable(MLevel.INFO)) {
/*     */       
/* 824 */       if (cause != null)
/*     */       {
/*     */ 
/*     */         
/* 828 */         logger.log(MLevel.INFO, "[c3p0] A PooledConnection died due to the following error!", cause);
/*     */       }
/* 830 */       if (exceptions != null && exceptions.size() > 0) {
/*     */         
/* 832 */         if (cause == null) {
/* 833 */           logger.info("[c3p0] Exceptions occurred while trying to close a PooledConnection's resources normally.");
/*     */         } else {
/*     */           
/* 836 */           logger.info("[c3p0] Exceptions occurred while trying to close a Broken PooledConnection.");
/*     */         } 
/* 838 */         for (Iterator<Throwable> ii = exceptions.iterator(); ii.hasNext(); ) {
/*     */           
/* 840 */           Throwable t = ii.next();
/*     */ 
/*     */           
/* 843 */           logger.log(MLevel.INFO, "[c3p0] NewPooledConnection close Exception.", t);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/NewPooledConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */