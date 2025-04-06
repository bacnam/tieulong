/*      */ package com.mchange.v2.c3p0.stmt;
/*      */ 
/*      */ import com.mchange.v1.db.sql.StatementUtils;
/*      */ import com.mchange.v2.async.AsynchronousRunner;
/*      */ import com.mchange.v2.io.IndentedWriter;
/*      */ import com.mchange.v2.log.MLevel;
/*      */ import com.mchange.v2.log.MLog;
/*      */ import com.mchange.v2.log.MLogger;
/*      */ import com.mchange.v2.sql.SqlUtils;
/*      */ import com.mchange.v2.util.ResourceClosedException;
/*      */ import java.io.IOException;
/*      */ import java.io.StringWriter;
/*      */ import java.lang.reflect.Method;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.SQLException;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.TreeMap;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class GooGooStatementCache
/*      */ {
/*   55 */   private static final MLogger logger = MLog.getLogger(GooGooStatementCache.class);
/*      */ 
/*      */   
/*      */   private static final int DESTROY_NEVER = 0;
/*      */ 
/*      */   
/*      */   private static final int DESTROY_IF_CHECKED_IN = 1;
/*      */ 
/*      */   
/*      */   private static final int DESTROY_IF_CHECKED_OUT = 2;
/*      */ 
/*      */   
/*      */   private static final int DESTROY_ALWAYS = 3;
/*      */ 
/*      */   
/*      */   private static final boolean CULL_ONLY_FROM_UNUSED_CONNECTIONS = false;
/*      */ 
/*      */   
/*      */   ConnectionStatementManager cxnStmtMgr;
/*      */ 
/*      */   
/*   76 */   HashMap stmtToKey = new HashMap<Object, Object>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   81 */   HashMap keyToKeyRec = new HashMap<Object, Object>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   86 */   HashSet checkedOut = new HashSet();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   AsynchronousRunner blockingTaskAsyncRunner;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  102 */   HashSet removalPending = new HashSet();
/*      */ 
/*      */   
/*      */   StatementDestructionManager destructo;
/*      */ 
/*      */ 
/*      */   
/*      */   public GooGooStatementCache(AsynchronousRunner blockingTaskAsyncRunner, AsynchronousRunner deferredStatementDestroyer) {
/*  110 */     this.blockingTaskAsyncRunner = blockingTaskAsyncRunner;
/*  111 */     this.cxnStmtMgr = createConnectionStatementManager();
/*  112 */     this.destructo = (deferredStatementDestroyer != null) ? new CautiousStatementDestructionManager(deferredStatementDestroyer) : new IncautiousStatementDestructionManager(blockingTaskAsyncRunner);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized int getNumStatements() {
/*  119 */     return isClosed() ? -1 : countCachedStatements();
/*      */   }
/*      */   public synchronized int getNumStatementsCheckedOut() {
/*  122 */     return isClosed() ? -1 : this.checkedOut.size();
/*      */   }
/*      */   public synchronized int getNumConnectionsWithCachedStatements() {
/*  125 */     return isClosed() ? -1 : this.cxnStmtMgr.getNumConnectionsWithCachedStatements();
/*      */   }
/*      */   
/*      */   public synchronized String dumpStatementCacheStatus() {
/*  129 */     if (isClosed()) {
/*  130 */       return this + "status: Closed.";
/*      */     }
/*      */     
/*  133 */     StringWriter sw = new StringWriter(2048);
/*  134 */     IndentedWriter iw = new IndentedWriter(sw);
/*      */     
/*      */     try {
/*  137 */       iw.print(this);
/*  138 */       iw.println(" status:");
/*  139 */       iw.upIndent();
/*  140 */       iw.println("core stats:");
/*  141 */       iw.upIndent();
/*  142 */       iw.print("num cached statements: ");
/*  143 */       iw.println(countCachedStatements());
/*  144 */       iw.print("num cached statements in use: ");
/*  145 */       iw.println(this.checkedOut.size());
/*  146 */       iw.print("num connections with cached statements: ");
/*  147 */       iw.println(this.cxnStmtMgr.getNumConnectionsWithCachedStatements());
/*  148 */       iw.downIndent();
/*  149 */       iw.println("cached statement dump:");
/*  150 */       iw.upIndent();
/*  151 */       for (Iterator<Connection> ii = this.cxnStmtMgr.connectionSet().iterator(); ii.hasNext(); ) {
/*      */         
/*  153 */         Connection pcon = ii.next();
/*  154 */         iw.print(pcon);
/*  155 */         iw.println(':');
/*  156 */         iw.upIndent();
/*  157 */         for (Iterator jj = this.cxnStmtMgr.statementSet(pcon).iterator(); jj.hasNext();)
/*  158 */           iw.println(jj.next()); 
/*  159 */         iw.downIndent();
/*      */       } 
/*      */       
/*  162 */       iw.downIndent();
/*  163 */       iw.downIndent();
/*  164 */       return sw.toString();
/*      */     }
/*  166 */     catch (IOException e) {
/*      */       
/*  168 */       if (logger.isLoggable(MLevel.SEVERE))
/*  169 */         logger.log(MLevel.SEVERE, "Huh? We've seen an IOException writing to s StringWriter?!", e); 
/*  170 */       return e.toString();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void waitMarkConnectionInUse(Connection physicalConnection) throws InterruptedException {
/*  175 */     this.destructo.waitMarkConnectionInUse(physicalConnection);
/*  176 */   } public boolean tryMarkConnectionInUse(Connection physicalConnection) { return this.destructo.tryMarkConnectionInUse(physicalConnection); }
/*  177 */   public void unmarkConnectionInUse(Connection physicalConnection) { this.destructo.unmarkConnectionInUse(physicalConnection); } public Boolean inUse(Connection physicalConnection) {
/*  178 */     return this.destructo.tvlInUse(physicalConnection);
/*      */   }
/*  180 */   public int getStatementDestroyerNumConnectionsInUse() { return this.destructo.getNumConnectionsInUse(); }
/*  181 */   public int getStatementDestroyerNumConnectionsWithDeferredDestroyStatements() { return this.destructo.getNumConnectionsWithDeferredDestroyStatements(); } public int getStatementDestroyerNumDeferredDestroyStatements() {
/*  182 */     return this.destructo.getNumDeferredDestroyStatements();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Object checkoutStatement(Connection physicalConnection, Method stmtProducingMethod, Object[] args) throws SQLException, ResourceClosedException {
/*      */     try {
/*  194 */       Object out = null;
/*      */       
/*  196 */       StatementCacheKey key = StatementCacheKey.find(physicalConnection, stmtProducingMethod, args);
/*      */ 
/*      */       
/*  199 */       LinkedList l = checkoutQueue(key);
/*  200 */       if (l == null || l.isEmpty()) {
/*      */ 
/*      */ 
/*      */         
/*  204 */         out = acquireStatement(physicalConnection, stmtProducingMethod, args);
/*      */         
/*  206 */         if (prepareAssimilateNewStatement(physicalConnection)) {
/*  207 */           assimilateNewCheckedOutStatement(key, physicalConnection, out);
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/*  215 */         logger.finest(getClass().getName() + " ----> CACHE HIT");
/*      */ 
/*      */         
/*  218 */         out = l.get(0);
/*  219 */         l.remove(0);
/*  220 */         if (!this.checkedOut.add(out)) {
/*  221 */           throw new RuntimeException("Internal inconsistency: Checking out a statement marked as already checked out!");
/*      */         }
/*      */         
/*  224 */         removeStatementFromDeathmarches(out, physicalConnection);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  231 */       if (logger.isLoggable(MLevel.FINEST)) {
/*  232 */         logger.finest("checkoutStatement: " + statsString());
/*      */       }
/*      */       
/*  235 */       return out;
/*      */     }
/*  237 */     catch (NullPointerException npe) {
/*      */       
/*  239 */       if (this.checkedOut == null) {
/*      */         
/*  241 */         if (logger.isLoggable(MLevel.FINE)) {
/*  242 */           logger.log(MLevel.FINE, "A client attempted to work with a closed Statement cache, provoking a NullPointerException. c3p0 recovers, but this should be rare.", npe);
/*      */         }
/*      */ 
/*      */         
/*  246 */         throw new ResourceClosedException(npe);
/*      */       } 
/*      */       
/*  249 */       throw npe;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void checkinStatement(Object pstmt) throws SQLException {
/*  256 */     if (this.checkedOut == null) {
/*      */       
/*  258 */       this.destructo.synchronousDestroyStatement(pstmt);
/*      */       
/*      */       return;
/*      */     } 
/*  262 */     if (!this.checkedOut.remove(pstmt)) {
/*      */       
/*  264 */       if (!ourResource(pstmt)) {
/*  265 */         this.destructo.uncheckedDestroyStatement(pstmt);
/*      */       }
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*      */     try {
/*  272 */       refreshStatement((PreparedStatement)pstmt);
/*  273 */     } catch (Exception e) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  279 */       if (logger.isLoggable(MLevel.INFO)) {
/*  280 */         logger.log(MLevel.INFO, "Problem with checked-in Statement, discarding.", e);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  287 */       this.checkedOut.add(pstmt);
/*      */       
/*  289 */       removeStatement(pstmt, 3);
/*      */       
/*      */       return;
/*      */     } 
/*  293 */     StatementCacheKey key = (StatementCacheKey)this.stmtToKey.get(pstmt);
/*  294 */     if (key == null) {
/*  295 */       throw new RuntimeException("Internal inconsistency: A checked-out statement has no key associated with it!");
/*      */     }
/*      */     
/*  298 */     LinkedList<Object> l = checkoutQueue(key);
/*  299 */     l.add(pstmt);
/*  300 */     addStatementToDeathmarches(pstmt, key.physicalConnection);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  306 */     if (logger.isLoggable(MLevel.FINEST)) {
/*  307 */       logger.finest("checkinStatement(): " + statsString());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void checkinAll(Connection pcon) throws SQLException {
/*  317 */     Set stmtSet = this.cxnStmtMgr.statementSet(pcon);
/*  318 */     if (stmtSet != null)
/*      */     {
/*  320 */       for (Iterator ii = stmtSet.iterator(); ii.hasNext(); ) {
/*      */         
/*  322 */         Object stmt = ii.next();
/*  323 */         if (this.checkedOut.contains(stmt)) {
/*  324 */           checkinStatement(stmt);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  332 */     if (logger.isLoggable(MLevel.FINEST)) {
/*  333 */       logger.log(MLevel.FINEST, "checkinAll(): " + statsString());
/*      */     }
/*      */   }
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
/*      */ 
/*      */   
/*      */   public void closeAll(Connection pcon) throws SQLException {
/*  349 */     if (!isClosed()) {
/*      */ 
/*      */ 
/*      */       
/*  353 */       if (logger.isLoggable(MLevel.FINEST))
/*      */       {
/*  355 */         logger.log(MLevel.FINEST, "ENTER METHOD: closeAll( " + pcon + " )! -- num_connections: " + this.cxnStmtMgr.getNumConnectionsWithCachedStatements());
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  361 */       Set stmtSet = null;
/*  362 */       synchronized (this) {
/*      */         
/*  364 */         Set<?> cSet = this.cxnStmtMgr.statementSet(pcon);
/*      */         
/*  366 */         if (cSet != null) {
/*      */ 
/*      */           
/*  369 */           stmtSet = new HashSet(cSet);
/*      */ 
/*      */           
/*  372 */           for (Iterator ii = stmtSet.iterator(); ii.hasNext(); ) {
/*      */             
/*  374 */             Object stmt = ii.next();
/*      */ 
/*      */             
/*  377 */             removeStatement(stmt, 0);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  382 */       if (stmtSet != null)
/*      */       {
/*  384 */         for (Iterator ii = stmtSet.iterator(); ii.hasNext(); ) {
/*      */           
/*  386 */           Object stmt = ii.next();
/*  387 */           this.destructo.synchronousDestroyStatement(stmt);
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  393 */       if (logger.isLoggable(MLevel.FINEST)) {
/*  394 */         logger.finest("closeAll(): " + statsString());
/*      */       }
/*      */     } 
/*      */   }
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
/*      */ 
/*      */   
/*      */   public synchronized void close() throws SQLException {
/*  411 */     if (!isClosed()) {
/*      */       
/*  413 */       for (Iterator ii = this.stmtToKey.keySet().iterator(); ii.hasNext();)
/*  414 */         this.destructo.synchronousDestroyStatement(ii.next()); 
/*  415 */       this.destructo.close();
/*      */       
/*  417 */       this.cxnStmtMgr = null;
/*  418 */       this.stmtToKey = null;
/*  419 */       this.keyToKeyRec = null;
/*  420 */       this.checkedOut = null;
/*      */ 
/*      */     
/*      */     }
/*  424 */     else if (logger.isLoggable(MLevel.FINE)) {
/*  425 */       logger.log(MLevel.FINE, this + ": duplicate call to close() [not harmful! -- debug only!]", new Exception("DUPLICATE CLOSE DEBUG STACK TRACE."));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized boolean isClosed() {
/*  432 */     return (this.cxnStmtMgr == null);
/*      */   }
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
/*      */   final int countCachedStatements() {
/*  445 */     return this.stmtToKey.size();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void assimilateNewCheckedOutStatement(StatementCacheKey key, Connection pConn, Object ps) {
/*  451 */     this.stmtToKey.put(ps, key);
/*  452 */     HashSet ks = keySet(key);
/*  453 */     if (ks == null) {
/*  454 */       this.keyToKeyRec.put(key, new KeyRec());
/*      */     }
/*      */     else {
/*      */       
/*  458 */       if (logger.isLoggable(MLevel.INFO))
/*  459 */         logger.info("Multiply-cached PreparedStatement: " + key.stmtText); 
/*  460 */       if (logger.isLoggable(MLevel.FINE)) {
/*  461 */         logger.fine("(The same statement has already been prepared by this Connection, and that other instance has not yet been closed, so the statement pool has to prepare a second PreparedStatement object rather than reusing the previously-cached Statement. The new Statement will be cached, in case you frequently need multiple copies of this Statement.)");
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  467 */     keySet(key).add(ps);
/*  468 */     this.cxnStmtMgr.addStatementForConnection(ps, pConn);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  474 */     if (logger.isLoggable(MLevel.FINEST)) {
/*  475 */       logger.finest("cxnStmtMgr.statementSet( " + pConn + " ).size(): " + this.cxnStmtMgr.statementSet(pConn).size());
/*      */     }
/*      */ 
/*      */     
/*  479 */     this.checkedOut.add(ps);
/*      */   }
/*      */ 
/*      */   
/*      */   private void removeStatement(Object ps, int destruction_policy) {
/*  484 */     synchronized (this.removalPending) {
/*      */       
/*  486 */       if (this.removalPending.contains(ps)) {
/*      */         return;
/*      */       }
/*  489 */       this.removalPending.add(ps);
/*      */     } 
/*      */     
/*  492 */     StatementCacheKey sck = (StatementCacheKey)this.stmtToKey.remove(ps);
/*  493 */     removeFromKeySet(sck, ps);
/*  494 */     Connection pConn = sck.physicalConnection;
/*      */     
/*  496 */     boolean checked_in = !this.checkedOut.contains(ps);
/*      */     
/*  498 */     if (checked_in) {
/*      */       
/*  500 */       removeStatementFromDeathmarches(ps, pConn);
/*  501 */       removeFromCheckoutQueue(sck, ps);
/*  502 */       if ((destruction_policy & 0x1) != 0) {
/*  503 */         this.destructo.deferredDestroyStatement(pConn, ps);
/*      */       }
/*      */     } else {
/*      */       
/*  507 */       this.checkedOut.remove(ps);
/*  508 */       if ((destruction_policy & 0x2) != 0) {
/*  509 */         this.destructo.deferredDestroyStatement(pConn, ps);
/*      */       }
/*      */     } 
/*      */     
/*  513 */     boolean check = this.cxnStmtMgr.removeStatementForConnection(ps, pConn);
/*  514 */     if (!check)
/*      */     {
/*      */       
/*  517 */       if (logger.isLoggable(MLevel.WARNING)) {
/*  518 */         logger.log(MLevel.WARNING, this + " removed a statement that apparently wasn't in a statement set!!!", new Exception("LOG STACK TRACE"));
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*  523 */     synchronized (this.removalPending) {
/*  524 */       this.removalPending.remove(ps);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object acquireStatement(final Connection pConn, final Method stmtProducingMethod, final Object[] args) throws SQLException {
/*      */     try {
/*  537 */       final Object[] outHolder = new Object[1];
/*  538 */       final SQLException[] exceptionHolder = new SQLException[1];
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
/*  569 */       Runnable r = new StmtAcquireTask();
/*  570 */       this.blockingTaskAsyncRunner.postRunnable(r);
/*      */       
/*  572 */       while (outHolder[0] == null && exceptionHolder[0] == null)
/*  573 */         wait(); 
/*  574 */       if (exceptionHolder[0] != null) {
/*  575 */         throw exceptionHolder[0];
/*      */       }
/*      */       
/*  578 */       Object out = outHolder[0];
/*  579 */       return out;
/*      */     
/*      */     }
/*  582 */     catch (InterruptedException e) {
/*  583 */       throw SqlUtils.toSQLException(e);
/*      */     } 
/*      */   }
/*      */   private KeyRec keyRec(StatementCacheKey key) {
/*  587 */     return (KeyRec)this.keyToKeyRec.get(key);
/*      */   }
/*      */   
/*      */   private HashSet keySet(StatementCacheKey key) {
/*  591 */     KeyRec rec = keyRec(key);
/*  592 */     return (rec == null) ? null : rec.allStmts;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean removeFromKeySet(StatementCacheKey key, Object pstmt) {
/*  598 */     HashSet stmtSet = keySet(key);
/*  599 */     boolean out = stmtSet.remove(pstmt);
/*  600 */     if (stmtSet.isEmpty() && checkoutQueue(key).isEmpty())
/*  601 */       this.keyToKeyRec.remove(key); 
/*  602 */     return out;
/*      */   }
/*      */ 
/*      */   
/*      */   private LinkedList checkoutQueue(StatementCacheKey key) {
/*  607 */     KeyRec rec = keyRec(key);
/*  608 */     return (rec == null) ? null : rec.checkoutQueue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean removeFromCheckoutQueue(StatementCacheKey key, Object pstmt) {
/*  614 */     LinkedList q = checkoutQueue(key);
/*  615 */     boolean out = q.remove(pstmt);
/*  616 */     if (q.isEmpty() && keySet(key).isEmpty())
/*  617 */       this.keyToKeyRec.remove(key); 
/*  618 */     return out;
/*      */   }
/*      */   
/*      */   private boolean ourResource(Object ps) {
/*  622 */     return this.stmtToKey.keySet().contains(ps);
/*      */   }
/*      */   
/*      */   private void refreshStatement(PreparedStatement ps) throws Exception {
/*  626 */     ps.clearParameters();
/*  627 */     ps.clearBatch();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void printStats() {
/*  633 */     int total_size = countCachedStatements();
/*  634 */     int checked_out_size = this.checkedOut.size();
/*  635 */     int num_connections = this.cxnStmtMgr.getNumConnectionsWithCachedStatements();
/*  636 */     int num_keys = this.keyToKeyRec.size();
/*  637 */     System.err.print(getClass().getName() + " stats -- ");
/*  638 */     System.err.print("total size: " + total_size);
/*  639 */     System.err.print("; checked out: " + checked_out_size);
/*  640 */     System.err.print("; num connections: " + num_connections);
/*  641 */     System.err.println("; num keys: " + num_keys);
/*      */   }
/*      */ 
/*      */   
/*      */   private String statsString() {
/*  646 */     int total_size = countCachedStatements();
/*  647 */     int checked_out_size = this.checkedOut.size();
/*  648 */     int num_connections = this.cxnStmtMgr.getNumConnectionsWithCachedStatements();
/*  649 */     int num_keys = this.keyToKeyRec.size();
/*      */     
/*  651 */     StringBuffer sb = new StringBuffer(255);
/*  652 */     sb.append(getClass().getName());
/*  653 */     sb.append(" stats -- ");
/*  654 */     sb.append("total size: ");
/*  655 */     sb.append(total_size);
/*  656 */     sb.append("; checked out: ");
/*  657 */     sb.append(checked_out_size);
/*  658 */     sb.append("; num connections: ");
/*  659 */     sb.append(num_connections);
/*  660 */     int in_use = this.destructo.countConnectionsInUse();
/*  661 */     if (in_use >= 0) {
/*      */       
/*  663 */       sb.append("; num connections in use: ");
/*  664 */       sb.append(in_use);
/*      */     } 
/*  666 */     sb.append("; num keys: ");
/*  667 */     sb.append(num_keys);
/*  668 */     return sb.toString();
/*      */   }
/*      */   abstract ConnectionStatementManager createConnectionStatementManager();
/*      */   abstract boolean prepareAssimilateNewStatement(Connection paramConnection);
/*      */   abstract void addStatementToDeathmarches(Object paramObject, Connection paramConnection);
/*      */   abstract void removeStatementFromDeathmarches(Object paramObject, Connection paramConnection);
/*  674 */   private static class KeyRec { HashSet allStmts = new HashSet(); private KeyRec() {}
/*  675 */     LinkedList checkoutQueue = new LinkedList(); }
/*      */ 
/*      */   
/*      */   protected class Deathmarch
/*      */   {
/*  680 */     TreeMap longsToStmts = new TreeMap<Object, Object>();
/*  681 */     HashMap stmtsToLongs = new HashMap<Object, Object>();
/*      */     
/*  683 */     long last_long = -1L;
/*      */ 
/*      */     
/*      */     public void deathmarchStatement(Object ps) {
/*  687 */       assert Thread.holdsLock(GooGooStatementCache.this);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  692 */       Long old = (Long)this.stmtsToLongs.get(ps);
/*  693 */       if (old != null) {
/*  694 */         throw new RuntimeException("Internal inconsistency: A statement is being double-deathmatched. no checked-out statements should be in a deathmarch already; no already checked-in statement should be deathmarched!");
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  699 */       Long youth = getNextLong();
/*  700 */       this.stmtsToLongs.put(ps, youth);
/*  701 */       this.longsToStmts.put(youth, ps);
/*      */     }
/*      */ 
/*      */     
/*      */     public void undeathmarchStatement(Object ps) {
/*  706 */       assert Thread.holdsLock(GooGooStatementCache.this);
/*      */       
/*  708 */       Long old = (Long)this.stmtsToLongs.remove(ps);
/*  709 */       if (old == null) {
/*  710 */         throw new RuntimeException("Internal inconsistency: A (not new) checking-out statement is not in deathmarch.");
/*      */       }
/*  712 */       Object check = this.longsToStmts.remove(old);
/*  713 */       if (old == null) {
/*  714 */         throw new RuntimeException("Internal inconsistency: A (not new) checking-out statement is not in deathmarch.");
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean cullNext() {
/*  720 */       assert Thread.holdsLock(GooGooStatementCache.this);
/*      */       
/*  722 */       Object cullMeStmt = null;
/*  723 */       StatementCacheKey sck = null;
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
/*  744 */       if (!this.longsToStmts.isEmpty()) {
/*      */         
/*  746 */         Long l = (Long)this.longsToStmts.firstKey();
/*  747 */         cullMeStmt = this.longsToStmts.get(l);
/*      */       } 
/*      */ 
/*      */       
/*  751 */       if (cullMeStmt == null) {
/*  752 */         return false;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  757 */       if (sck == null)
/*  758 */         sck = (StatementCacheKey)GooGooStatementCache.this.stmtToKey.get(cullMeStmt); 
/*  759 */       if (GooGooStatementCache.logger.isLoggable(MLevel.FINEST)) {
/*  760 */         GooGooStatementCache.logger.finest("CULLING: " + sck.stmtText);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  765 */       GooGooStatementCache.this.removeStatement(cullMeStmt, 3);
/*  766 */       if (contains(cullMeStmt)) {
/*  767 */         throw new RuntimeException("Inconsistency!!! Statement culled from deathmarch failed to be removed by removeStatement( ... )!");
/*      */       }
/*  769 */       return true;
/*      */     }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean contains(Object ps) {
/*  801 */       return this.stmtsToLongs.keySet().contains(ps);
/*      */     }
/*      */     public int size() {
/*  804 */       return this.longsToStmts.size();
/*      */     }
/*      */     private Long getNextLong() {
/*  807 */       return new Long(++this.last_long);
/*      */     }
/*      */   }
/*      */   
/*      */   protected static abstract class ConnectionStatementManager {
/*  812 */     Map cxnToStmtSets = new HashMap<Object, Object>();
/*      */     
/*      */     public int getNumConnectionsWithCachedStatements() {
/*  815 */       return this.cxnToStmtSets.size();
/*      */     }
/*      */     public Set connectionSet() {
/*  818 */       return this.cxnToStmtSets.keySet();
/*      */     }
/*      */     public Set statementSet(Connection pcon) {
/*  821 */       return (Set)this.cxnToStmtSets.get(pcon);
/*      */     }
/*      */     
/*      */     public int getNumStatementsForConnection(Connection pcon) {
/*  825 */       Set stmtSet = statementSet(pcon);
/*  826 */       return (stmtSet == null) ? 0 : stmtSet.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public void addStatementForConnection(Object ps, Connection pcon) {
/*  831 */       Set<Object> stmtSet = statementSet(pcon);
/*  832 */       if (stmtSet == null) {
/*      */         
/*  834 */         stmtSet = new HashSet();
/*  835 */         this.cxnToStmtSets.put(pcon, stmtSet);
/*      */       } 
/*  837 */       stmtSet.add(ps);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean removeStatementForConnection(Object ps, Connection pcon) {
/*      */       boolean out;
/*  844 */       Set stmtSet = statementSet(pcon);
/*  845 */       if (stmtSet != null) {
/*      */         
/*  847 */         out = stmtSet.remove(ps);
/*  848 */         if (stmtSet.isEmpty()) {
/*  849 */           this.cxnToStmtSets.remove(pcon);
/*      */         }
/*      */       } else {
/*  852 */         out = false;
/*      */       } 
/*  854 */       return out;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected static final class SimpleConnectionStatementManager
/*      */     extends ConnectionStatementManager {}
/*      */   
/*      */   protected final class DeathmarchConnectionStatementManager
/*      */     extends ConnectionStatementManager
/*      */   {
/*  865 */     Map cxnsToDms = new HashMap<Object, Object>();
/*      */ 
/*      */     
/*      */     public void addStatementForConnection(Object ps, Connection pcon) {
/*  869 */       super.addStatementForConnection(ps, pcon);
/*  870 */       GooGooStatementCache.Deathmarch dm = (GooGooStatementCache.Deathmarch)this.cxnsToDms.get(pcon);
/*  871 */       if (dm == null) {
/*      */         
/*  873 */         dm = new GooGooStatementCache.Deathmarch();
/*  874 */         this.cxnsToDms.put(pcon, dm);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeStatementForConnection(Object ps, Connection pcon) {
/*  880 */       boolean out = super.removeStatementForConnection(ps, pcon);
/*  881 */       if (out)
/*      */       {
/*  883 */         if (statementSet(pcon) == null)
/*  884 */           this.cxnsToDms.remove(pcon); 
/*      */       }
/*  886 */       return out;
/*      */     }
/*      */     
/*      */     public GooGooStatementCache.Deathmarch getDeathmarch(Connection pcon) {
/*  890 */       return (GooGooStatementCache.Deathmarch)this.cxnsToDms.get(pcon);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private abstract class StatementDestructionManager
/*      */   {
/*      */     AsynchronousRunner runner;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     StatementDestructionManager(AsynchronousRunner runner) {
/*  906 */       this.runner = runner;
/*      */     }
/*      */ 
/*      */     
/*      */     abstract void waitMarkConnectionInUse(Connection param1Connection) throws InterruptedException;
/*      */ 
/*      */     
/*      */     abstract boolean tryMarkConnectionInUse(Connection param1Connection);
/*      */     
/*      */     abstract void unmarkConnectionInUse(Connection param1Connection);
/*      */     
/*      */     abstract void deferredDestroyStatement(Object param1Object1, Object param1Object2);
/*      */     
/*      */     abstract int countConnectionsInUse();
/*      */     
/*      */     abstract boolean knownInUse(Connection param1Connection);
/*      */     
/*      */     abstract Boolean tvlInUse(Connection param1Connection);
/*      */     
/*      */     abstract int getNumConnectionsInUse();
/*      */     
/*      */     abstract int getNumConnectionsWithDeferredDestroyStatements();
/*      */     
/*      */     abstract int getNumDeferredDestroyStatements();
/*      */     
/*      */     abstract void close();
/*      */     
/*      */     final void uncheckedDestroyStatement(final Object pstmt) {
/*      */       class UncheckedStatementCloseTask
/*      */         implements Runnable
/*      */       {
/*      */         public void run() {
/*  938 */           StatementUtils.attemptClose((PreparedStatement)pstmt);
/*      */         }
/*      */       };
/*  941 */       Runnable r = new UncheckedStatementCloseTask();
/*      */       
/*  943 */       this.runner.postRunnable(r);
/*      */     }
/*      */     
/*      */     final void synchronousDestroyStatement(Object pstmt) {
/*  947 */       StatementUtils.attemptClose((PreparedStatement)pstmt);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private final class IncautiousStatementDestructionManager
/*      */     extends StatementDestructionManager
/*      */   {
/*      */     IncautiousStatementDestructionManager(AsynchronousRunner runner) {
/*  956 */       super(runner);
/*      */     } void waitMarkConnectionInUse(Connection physicalConnection) throws InterruptedException {}
/*      */     boolean tryMarkConnectionInUse(Connection physicalConnection) {
/*  959 */       return true;
/*      */     } void unmarkConnectionInUse(Connection physicalConnection) {} void deferredDestroyStatement(Object parentConnection, Object pstmt) {
/*  961 */       uncheckedDestroyStatement(pstmt);
/*      */     }
/*      */     void close() {}
/*      */     int countConnectionsInUse() {
/*  965 */       return -1;
/*      */     }
/*      */     
/*      */     boolean knownInUse(Connection pCon) {
/*  969 */       return false;
/*      */     } Boolean tvlInUse(Connection pCon) {
/*  971 */       return null;
/*      */     }
/*  973 */     int getNumConnectionsInUse() { return -1; }
/*  974 */     int getNumConnectionsWithDeferredDestroyStatements() { return -1; } int getNumDeferredDestroyStatements() {
/*  975 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private final class CautiousStatementDestructionManager
/*      */     extends StatementDestructionManager
/*      */   {
/*  983 */     HashSet inUseConnections = new HashSet();
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
/*      */     
/*  995 */     HashMap connectionsToZombieStatementSets = new HashMap<Object, Object>();
/*      */     
/*      */     AsynchronousRunner deferredStatementDestroyer;
/*      */     
/*      */     boolean closed = false;
/*      */     
/*      */     synchronized void close() {
/* 1002 */       this.closed = true;
/*      */     }
/*      */     
/*      */     CautiousStatementDestructionManager(AsynchronousRunner deferredStatementDestroyer) {
/* 1006 */       super(deferredStatementDestroyer);
/* 1007 */       this.deferredStatementDestroyer = deferredStatementDestroyer;
/*      */     }
/*      */ 
/*      */     
/*      */     private String trace() {
/* 1012 */       Set keys = this.connectionsToZombieStatementSets.keySet();
/* 1013 */       int sum = 0;
/* 1014 */       for (Iterator ii = keys.iterator(); ii.hasNext(); ) {
/*      */         
/* 1016 */         Object con = ii.next();
/* 1017 */         Set stmts = (Set)this.connectionsToZombieStatementSets.get(con);
/* 1018 */         synchronized (stmts) {
/* 1019 */           sum += (stmts == null) ? 0 : stmts.size();
/*      */         } 
/* 1021 */       }  return getClass().getName() + " [connections in use: " + this.inUseConnections.size() + "; connections with deferred statements: " + keys.size() + "; statements to destroy: " + sum + "]";
/*      */     }
/*      */ 
/*      */     
/*      */     private void printAllStats() {
/* 1026 */       GooGooStatementCache.this.printStats();
/* 1027 */       System.err.println(trace());
/*      */     }
/*      */ 
/*      */     
/*      */     synchronized void waitMarkConnectionInUse(Connection physicalConnection) throws InterruptedException {
/* 1032 */       if (!this.closed) {
/*      */         
/* 1034 */         Set stmts = statementsUnderDestruction(physicalConnection);
/* 1035 */         if (stmts != null) {
/*      */           
/* 1037 */           if (GooGooStatementCache.logger.isLoggable(MLevel.FINE))
/*      */           {
/*      */ 
/*      */ 
/*      */             
/* 1042 */             GooGooStatementCache.logger.log(MLevel.FINE, "A connection is waiting to be accepted by the Statement cache because " + stmts.size() + " cached Statements are still being destroyed.");
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1048 */           while (!stmts.isEmpty())
/* 1049 */             wait(); 
/*      */         } 
/* 1051 */         this.inUseConnections.add(physicalConnection);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     synchronized boolean tryMarkConnectionInUse(Connection physicalConnection) {
/* 1057 */       if (!this.closed) {
/*      */         
/* 1059 */         Set stmts = statementsUnderDestruction(physicalConnection);
/* 1060 */         if (stmts != null) {
/*      */           
/* 1062 */           int sz = stmts.size();
/* 1063 */           if (GooGooStatementCache.logger.isLoggable(MLevel.FINE))
/*      */           {
/* 1065 */             GooGooStatementCache.logger.log(MLevel.FINE, "A connection could not be accepted by the Statement cache because " + sz + " cached Statements are still being destroyed.");
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 1070 */           return false;
/*      */         } 
/*      */ 
/*      */         
/* 1074 */         this.inUseConnections.add(physicalConnection);
/* 1075 */         return true;
/*      */       } 
/*      */ 
/*      */       
/* 1079 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     synchronized void unmarkConnectionInUse(Connection physicalConnection) {
/* 1084 */       boolean unmarked = this.inUseConnections.remove(physicalConnection);
/*      */       
/* 1086 */       Set zombieStatements = (Set)this.connectionsToZombieStatementSets.get(physicalConnection);
/*      */       
/* 1088 */       if (zombieStatements != null)
/*      */       {
/*      */         
/* 1091 */         destroyAllTrackedStatements(physicalConnection);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     synchronized void deferredDestroyStatement(Object parentConnection, Object pstmt) {
/* 1097 */       if (!this.closed) {
/*      */         
/* 1099 */         if (this.inUseConnections.contains(parentConnection)) {
/*      */           
/* 1101 */           Set<?> s = (Set)this.connectionsToZombieStatementSets.get(parentConnection);
/* 1102 */           if (s == null) {
/*      */             
/* 1104 */             s = Collections.synchronizedSet(new HashSet());
/* 1105 */             this.connectionsToZombieStatementSets.put(parentConnection, s);
/*      */           } 
/* 1107 */           s.add(pstmt);
/*      */         }
/*      */         else {
/*      */           
/* 1111 */           uncheckedDestroyStatement(pstmt);
/*      */         } 
/*      */       } else {
/*      */         
/* 1115 */         uncheckedDestroyStatement(pstmt);
/*      */       } 
/*      */     }
/*      */     
/*      */     synchronized int countConnectionsInUse() {
/* 1120 */       return this.inUseConnections.size();
/*      */     }
/*      */ 
/*      */     
/*      */     synchronized boolean knownInUse(Connection pCon) {
/* 1125 */       return this.inUseConnections.contains(pCon);
/*      */     }
/*      */ 
/*      */     
/*      */     Boolean tvlInUse(Connection pCon) {
/* 1130 */       return Boolean.valueOf(knownInUse(pCon));
/*      */     }
/*      */     synchronized int getNumConnectionsInUse() {
/* 1133 */       return this.inUseConnections.size();
/*      */     }
/*      */     synchronized int getNumConnectionsWithDeferredDestroyStatements() {
/* 1136 */       return this.connectionsToZombieStatementSets.keySet().size();
/*      */     }
/*      */     
/*      */     synchronized int getNumDeferredDestroyStatements() {
/* 1140 */       Set keys = this.connectionsToZombieStatementSets.keySet();
/* 1141 */       int sum = 0;
/* 1142 */       for (Iterator ii = keys.iterator(); ii.hasNext(); ) {
/*      */         
/* 1144 */         Object con = ii.next();
/* 1145 */         Set stmts = (Set)this.connectionsToZombieStatementSets.get(con);
/* 1146 */         synchronized (stmts) {
/* 1147 */           sum += (stmts == null) ? 0 : stmts.size();
/*      */         } 
/* 1149 */       }  return sum;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void trackedDestroyStatement(final Object parentConnection, final Object pstmt) {
/*      */       final class TrackedStatementCloseTask
/*      */         implements Runnable
/*      */       {
/*      */         public void run() {
/* 1163 */           synchronized (GooGooStatementCache.CautiousStatementDestructionManager.this) {
/*      */ 
/*      */ 
/*      */             
/* 1167 */             Set stmts = (Set)GooGooStatementCache.CautiousStatementDestructionManager.this.connectionsToZombieStatementSets.get(parentConnection);
/* 1168 */             if (stmts != null) {
/*      */               
/* 1170 */               StatementUtils.attemptClose((PreparedStatement)pstmt);
/*      */               
/* 1172 */               boolean removed1 = stmts.remove(pstmt);
/* 1173 */               assert removed1;
/* 1174 */               if (stmts.isEmpty()) {
/*      */                 
/* 1176 */                 Object removed2 = GooGooStatementCache.CautiousStatementDestructionManager.this.connectionsToZombieStatementSets.remove(parentConnection);
/*      */                 
/* 1178 */                 assert removed2 == stmts;
/* 1179 */                 GooGooStatementCache.CautiousStatementDestructionManager.this.notifyAll();
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1190 */       Runnable r = new TrackedStatementCloseTask();
/*      */       
/* 1192 */       if (!this.closed) {
/*      */ 
/*      */         
/* 1195 */         this.deferredStatementDestroyer.postRunnable(r);
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1200 */         r.run();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void destroyAllTrackedStatements(final Object parentConnection) {
/*      */       final class TrackedDestroyAllStatementsTask
/*      */         implements Runnable
/*      */       {
/*      */         public void run() {
/* 1216 */           synchronized (GooGooStatementCache.CautiousStatementDestructionManager.this) {
/*      */ 
/*      */ 
/*      */             
/* 1220 */             Set stmts = (Set)GooGooStatementCache.CautiousStatementDestructionManager.this.connectionsToZombieStatementSets.remove(parentConnection);
/* 1221 */             if (stmts != null) {
/*      */ 
/*      */               
/* 1224 */               for (Iterator<PreparedStatement> ii = stmts.iterator(); ii.hasNext(); ) {
/*      */                 
/* 1226 */                 PreparedStatement pstmt = ii.next();
/* 1227 */                 StatementUtils.attemptClose(pstmt);
/* 1228 */                 ii.remove();
/*      */               } 
/* 1230 */               GooGooStatementCache.CautiousStatementDestructionManager.this.notifyAll();
/*      */             } 
/*      */           } 
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1240 */       Runnable r = new TrackedDestroyAllStatementsTask();
/*      */       
/* 1242 */       if (!this.closed) {
/*      */ 
/*      */         
/* 1245 */         this.deferredStatementDestroyer.postRunnable(r);
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1250 */         r.run();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private Set statementsUnderDestruction(Object parentConnection) {
/* 1257 */       assert Thread.holdsLock(this);
/*      */       
/* 1259 */       return (Set)this.connectionsToZombieStatementSets.get(parentConnection);
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/stmt/GooGooStatementCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */