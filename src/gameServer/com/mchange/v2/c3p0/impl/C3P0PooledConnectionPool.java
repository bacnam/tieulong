/*      */ package com.mchange.v2.c3p0.impl;
/*      */ 
/*      */ import com.mchange.v2.async.AsynchronousRunner;
/*      */ import com.mchange.v2.async.ThreadPoolAsynchronousRunner;
/*      */ import com.mchange.v2.c3p0.ConnectionCustomizer;
/*      */ import com.mchange.v2.c3p0.ConnectionTester;
/*      */ import com.mchange.v2.c3p0.stmt.DoubleMaxStatementCache;
/*      */ import com.mchange.v2.c3p0.stmt.GlobalMaxOnlyStatementCache;
/*      */ import com.mchange.v2.c3p0.stmt.GooGooStatementCache;
/*      */ import com.mchange.v2.c3p0.stmt.PerConnectionMaxOnlyStatementCache;
/*      */ import com.mchange.v2.log.MLevel;
/*      */ import com.mchange.v2.log.MLog;
/*      */ import com.mchange.v2.log.MLogger;
/*      */ import com.mchange.v2.resourcepool.CannotAcquireResourceException;
/*      */ import com.mchange.v2.resourcepool.ResourcePool;
/*      */ import com.mchange.v2.resourcepool.ResourcePoolException;
/*      */ import com.mchange.v2.resourcepool.ResourcePoolFactory;
/*      */ import com.mchange.v2.resourcepool.TimeoutException;
/*      */ import com.mchange.v2.sql.SqlUtils;
/*      */ import java.sql.Connection;
/*      */ import java.sql.SQLException;
/*      */ import java.util.LinkedList;
/*      */ import javax.sql.ConnectionEvent;
/*      */ import javax.sql.ConnectionEventListener;
/*      */ import javax.sql.ConnectionPoolDataSource;
/*      */ import javax.sql.PooledConnection;
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
/*      */ public final class C3P0PooledConnectionPool
/*      */ {
/*      */   private static final boolean ASYNCHRONOUS_CONNECTION_EVENT_LISTENER = false;
/*   74 */   private static final Throwable[] EMPTY_THROWABLE_HOLDER = new Throwable[1];
/*      */   
/*   76 */   static final MLogger logger = MLog.getLogger(C3P0PooledConnectionPool.class);
/*      */   
/*      */   final ResourcePool rp;
/*   79 */   final ConnectionEventListener cl = new ConnectionEventListenerImpl();
/*      */   
/*      */   final ConnectionTester connectionTester;
/*      */   
/*      */   final GooGooStatementCache scache;
/*      */   
/*      */   final boolean c3p0PooledConnections;
/*      */   
/*      */   final boolean effectiveStatementCache;
/*      */   
/*      */   final int checkoutTimeout;
/*      */   final AsynchronousRunner sharedTaskRunner;
/*      */   final AsynchronousRunner deferredStatementDestroyer;
/*   92 */   final ThrowableHolderPool thp = new ThrowableHolderPool();
/*      */   final InUseLockFetcher inUseLockFetcher;
/*      */   
/*      */   public int getStatementDestroyerNumConnectionsInUse() {
/*   96 */     return (this.scache == null) ? -1 : this.scache.getStatementDestroyerNumConnectionsInUse();
/*   97 */   } public int getStatementDestroyerNumConnectionsWithDeferredDestroyStatements() { return (this.scache == null) ? -1 : this.scache.getStatementDestroyerNumConnectionsWithDeferredDestroyStatements(); } public int getStatementDestroyerNumDeferredDestroyStatements() {
/*   98 */     return (this.scache == null) ? -1 : this.scache.getStatementDestroyerNumDeferredDestroyStatements();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static interface InUseLockFetcher
/*      */   {
/*      */     Object getInUseLock(Object param1Object);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class ResourceItselfInUseLockFetcher
/*      */     implements InUseLockFetcher
/*      */   {
/*      */     private ResourceItselfInUseLockFetcher() {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getInUseLock(Object resc) {
/*  121 */       return resc;
/*      */     } }
/*      */   
/*      */   private static class C3P0PooledConnectionNestedLockLockFetcher implements InUseLockFetcher { private C3P0PooledConnectionNestedLockLockFetcher() {}
/*      */     
/*      */     public Object getInUseLock(Object resc) {
/*  127 */       return ((AbstractC3P0PooledConnection)resc).inInternalUseLock;
/*      */     } }
/*      */   
/*  130 */   private static InUseLockFetcher RESOURCE_ITSELF_IN_USE_LOCK_FETCHER = new ResourceItselfInUseLockFetcher();
/*  131 */   private static InUseLockFetcher C3P0_POOLED_CONNECION_NESTED_LOCK_LOCK_FETCHER = new C3P0PooledConnectionNestedLockLockFetcher();
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
/*      */   
/*      */   C3P0PooledConnectionPool(final ConnectionPoolDataSource cpds, final DbAuth auth, int min, int max, int start, int inc, int acq_retry_attempts, int acq_retry_delay, boolean break_after_acq_failure, int checkoutTimeout, int idleConnectionTestPeriod, int maxIdleTime, int maxIdleTimeExcessConnections, int maxConnectionAge, int propertyCycle, int unreturnedConnectionTimeout, boolean debugUnreturnedConnectionStackTraces, final boolean testConnectionOnCheckout, final boolean testConnectionOnCheckin, int maxStatements, int maxStatementsPerConnection, final ConnectionTester connectionTester, final ConnectionCustomizer connectionCustomizer, final String testQuery, ResourcePoolFactory fact, ThreadPoolAsynchronousRunner taskRunner, ThreadPoolAsynchronousRunner deferredStatementDestroyer, final String parentDataSourceIdentityToken) throws SQLException {
/*      */     try {
/*  165 */       if (maxStatements > 0 && maxStatementsPerConnection > 0) {
/*  166 */         this.scache = (GooGooStatementCache)new DoubleMaxStatementCache((AsynchronousRunner)taskRunner, (AsynchronousRunner)deferredStatementDestroyer, maxStatements, maxStatementsPerConnection);
/*  167 */       } else if (maxStatementsPerConnection > 0) {
/*  168 */         this.scache = (GooGooStatementCache)new PerConnectionMaxOnlyStatementCache((AsynchronousRunner)taskRunner, (AsynchronousRunner)deferredStatementDestroyer, maxStatementsPerConnection);
/*  169 */       } else if (maxStatements > 0) {
/*  170 */         this.scache = (GooGooStatementCache)new GlobalMaxOnlyStatementCache((AsynchronousRunner)taskRunner, (AsynchronousRunner)deferredStatementDestroyer, maxStatements);
/*      */       } else {
/*  172 */         this.scache = null;
/*      */       } 
/*  174 */       this.connectionTester = connectionTester;
/*      */       
/*  176 */       this.checkoutTimeout = checkoutTimeout;
/*      */       
/*  178 */       this.sharedTaskRunner = (AsynchronousRunner)taskRunner;
/*  179 */       this.deferredStatementDestroyer = (AsynchronousRunner)deferredStatementDestroyer;
/*      */       
/*  181 */       this.c3p0PooledConnections = cpds instanceof com.mchange.v2.c3p0.WrapperConnectionPoolDataSource;
/*  182 */       this.effectiveStatementCache = (this.c3p0PooledConnections && this.scache != null);
/*      */       
/*  184 */       this.inUseLockFetcher = this.c3p0PooledConnections ? C3P0_POOLED_CONNECION_NESTED_LOCK_LOCK_FETCHER : RESOURCE_ITSELF_IN_USE_LOCK_FETCHER;
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
/*  653 */       ResourcePool.Manager manager = new PooledConnectionResourcePoolManager();
/*      */       
/*  655 */       synchronized (fact)
/*      */       {
/*  657 */         fact.setMin(min);
/*  658 */         fact.setMax(max);
/*  659 */         fact.setStart(start);
/*  660 */         fact.setIncrement(inc);
/*  661 */         fact.setIdleResourceTestPeriod((idleConnectionTestPeriod * 1000));
/*  662 */         fact.setResourceMaxIdleTime((maxIdleTime * 1000));
/*  663 */         fact.setExcessResourceMaxIdleTime((maxIdleTimeExcessConnections * 1000));
/*  664 */         fact.setResourceMaxAge((maxConnectionAge * 1000));
/*  665 */         fact.setExpirationEnforcementDelay((propertyCycle * 1000));
/*  666 */         fact.setDestroyOverdueResourceTime((unreturnedConnectionTimeout * 1000));
/*  667 */         fact.setDebugStoreCheckoutStackTrace(debugUnreturnedConnectionStackTraces);
/*  668 */         fact.setAcquisitionRetryAttempts(acq_retry_attempts);
/*  669 */         fact.setAcquisitionRetryDelay(acq_retry_delay);
/*  670 */         fact.setBreakOnAcquisitionFailure(break_after_acq_failure);
/*  671 */         this.rp = fact.createPool(manager);
/*      */       }
/*      */     
/*  674 */     } catch (ResourcePoolException e) {
/*  675 */       throw SqlUtils.toSQLException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public PooledConnection checkoutPooledConnection() throws SQLException {
/*      */     try {
/*  683 */       PooledConnection pc = (PooledConnection)checkoutAndMarkConnectionInUse();
/*  684 */       pc.addConnectionEventListener(this.cl);
/*  685 */       return pc;
/*      */     }
/*  687 */     catch (TimeoutException e) {
/*  688 */       throw SqlUtils.toSQLException("An attempt by a client to checkout a Connection has timed out.", e);
/*  689 */     } catch (CannotAcquireResourceException e) {
/*  690 */       throw SqlUtils.toSQLException("Connections could not be acquired from the underlying database!", "08001", e);
/*  691 */     } catch (Exception e) {
/*  692 */       throw SqlUtils.toSQLException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void waitMarkPhysicalConnectionInUse(Connection physicalConnection) throws InterruptedException {
/*  697 */     if (this.effectiveStatementCache)
/*  698 */       this.scache.waitMarkConnectionInUse(physicalConnection); 
/*      */   }
/*      */   
/*      */   private boolean tryMarkPhysicalConnectionInUse(Connection physicalConnection) {
/*  702 */     return this.effectiveStatementCache ? this.scache.tryMarkConnectionInUse(physicalConnection) : true;
/*      */   }
/*      */   
/*      */   private void unmarkPhysicalConnectionInUse(Connection physicalConnection) {
/*  706 */     if (this.effectiveStatementCache) {
/*  707 */       this.scache.unmarkConnectionInUse(physicalConnection);
/*      */     }
/*      */   }
/*      */   
/*      */   private void waitMarkPooledConnectionInUse(PooledConnection pooledCon) throws InterruptedException {
/*  712 */     if (this.c3p0PooledConnections) {
/*  713 */       waitMarkPhysicalConnectionInUse(((AbstractC3P0PooledConnection)pooledCon).getPhysicalConnection());
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean tryMarkPooledConnectionInUse(PooledConnection pooledCon) {
/*  718 */     if (this.c3p0PooledConnections) {
/*  719 */       return tryMarkPhysicalConnectionInUse(((AbstractC3P0PooledConnection)pooledCon).getPhysicalConnection());
/*      */     }
/*  721 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private void unmarkPooledConnectionInUse(PooledConnection pooledCon) {
/*  726 */     if (this.c3p0PooledConnections) {
/*  727 */       unmarkPhysicalConnectionInUse(((AbstractC3P0PooledConnection)pooledCon).getPhysicalConnection());
/*      */     }
/*      */   }
/*      */   
/*      */   private Boolean physicalConnectionInUse(Connection physicalConnection) throws InterruptedException {
/*  732 */     if (physicalConnection != null && this.effectiveStatementCache) {
/*  733 */       return this.scache.inUse(physicalConnection);
/*      */     }
/*  735 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private Boolean pooledConnectionInUse(PooledConnection pc) throws InterruptedException {
/*  740 */     if (pc != null && this.effectiveStatementCache) {
/*  741 */       return this.scache.inUse(((AbstractC3P0PooledConnection)pc).getPhysicalConnection());
/*      */     }
/*  743 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object checkoutAndMarkConnectionInUse() throws TimeoutException, CannotAcquireResourceException, ResourcePoolException, InterruptedException {
/*  750 */     Object out = null;
/*  751 */     boolean success = false;
/*  752 */     while (!success) {
/*      */ 
/*      */       
/*      */       try {
/*  756 */         out = this.rp.checkoutResource(this.checkoutTimeout);
/*  757 */         if (out instanceof AbstractC3P0PooledConnection) {
/*      */ 
/*      */           
/*  760 */           AbstractC3P0PooledConnection acpc = (AbstractC3P0PooledConnection)out;
/*  761 */           Connection physicalConnection = acpc.getPhysicalConnection();
/*  762 */           success = tryMarkPhysicalConnectionInUse(physicalConnection);
/*      */         } else {
/*      */           
/*  765 */           success = true;
/*      */         } 
/*      */       } finally {
/*      */         
/*  769 */         try { if (!success && out != null) this.rp.checkinResource(out);  }
/*  770 */         catch (Exception e) { logger.log(MLevel.WARNING, "Failed to check in a Connection that was unusable due to pending Statement closes.", e); }
/*      */       
/*      */       } 
/*  773 */     }  return out;
/*      */   }
/*      */ 
/*      */   
/*      */   private void unmarkConnectionInUseAndCheckin(PooledConnection pcon) throws ResourcePoolException {
/*  778 */     if (this.effectiveStatementCache) {
/*      */       
/*      */       try {
/*      */ 
/*      */ 
/*      */         
/*  784 */         AbstractC3P0PooledConnection acpc = (AbstractC3P0PooledConnection)pcon;
/*  785 */         Connection physicalConnection = acpc.getPhysicalConnection();
/*  786 */         unmarkPhysicalConnectionInUse(physicalConnection);
/*      */       }
/*  788 */       catch (ClassCastException e) {
/*      */         
/*  790 */         if (logger.isLoggable(MLevel.SEVERE)) {
/*  791 */           logger.log(MLevel.SEVERE, "You are checking a non-c3p0 PooledConnection implementation intoa c3p0 PooledConnectionPool instance that expects only c3p0-generated PooledConnections.This isn't good, and may indicate a c3p0 bug, or an unusual (and unspported) use of the c3p0 library.", e);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  798 */     this.rp.checkinResource(pcon);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void checkinPooledConnection(PooledConnection pcon) throws SQLException {
/*      */     try {
/*  806 */       pcon.removeConnectionEventListener(this.cl);
/*  807 */       unmarkConnectionInUseAndCheckin(pcon);
/*      */     }
/*  809 */     catch (ResourcePoolException e) {
/*  810 */       throw SqlUtils.toSQLException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public float getEffectivePropertyCycle() throws SQLException {
/*      */     try {
/*  816 */       return (float)this.rp.getEffectiveExpirationEnforcementDelay() / 1000.0F;
/*  817 */     } catch (ResourcePoolException e) {
/*  818 */       throw SqlUtils.toSQLException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getNumThreadsAwaitingCheckout() throws SQLException {
/*      */     try {
/*  824 */       return this.rp.getNumCheckoutWaiters();
/*  825 */     } catch (ResourcePoolException e) {
/*  826 */       throw SqlUtils.toSQLException(e);
/*      */     } 
/*      */   }
/*      */   public int getStatementCacheNumStatements() {
/*  830 */     return (this.scache == null) ? 0 : this.scache.getNumStatements();
/*      */   }
/*      */   public int getStatementCacheNumCheckedOut() {
/*  833 */     return (this.scache == null) ? 0 : this.scache.getNumStatementsCheckedOut();
/*      */   }
/*      */   public int getStatementCacheNumConnectionsWithCachedStatements() {
/*  836 */     return (this.scache == null) ? 0 : this.scache.getNumConnectionsWithCachedStatements();
/*      */   }
/*      */   public String dumpStatementCacheStatus() {
/*  839 */     return (this.scache == null) ? "Statement caching disabled." : this.scache.dumpStatementCacheStatus();
/*      */   }
/*      */   public void close() throws SQLException {
/*  842 */     close(true);
/*      */   }
/*      */   
/*      */   public void close(boolean close_outstanding_connections) throws SQLException {
/*      */     ResourcePoolException resourcePoolException;
/*  847 */     Exception throwMe = null;
/*      */     try {
/*  849 */       if (this.scache != null) this.scache.close(); 
/*  850 */     } catch (SQLException e) {
/*  851 */       throwMe = e;
/*      */     } 
/*      */     try {
/*  854 */       this.rp.close(close_outstanding_connections);
/*  855 */     } catch (ResourcePoolException e) {
/*      */       
/*  857 */       if (throwMe != null && logger.isLoggable(MLevel.WARNING))
/*  858 */         logger.log(MLevel.WARNING, "An Exception occurred while closing the StatementCache.", throwMe); 
/*  859 */       resourcePoolException = e;
/*      */     } 
/*      */     
/*  862 */     if (resourcePoolException != null) {
/*  863 */       throw SqlUtils.toSQLException(resourcePoolException);
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
/*      */   class ConnectionEventListenerImpl
/*      */     implements ConnectionEventListener
/*      */   {
/*      */     public void connectionClosed(ConnectionEvent evt) {
/*  901 */       doCheckinResource(evt);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void doCheckinResource(ConnectionEvent evt) {
/*      */       try {
/*  909 */         C3P0PooledConnectionPool.this.checkinPooledConnection((PooledConnection)evt.getSource());
/*      */       }
/*  911 */       catch (Exception e) {
/*      */ 
/*      */         
/*  914 */         C3P0PooledConnectionPool.logger.log(MLevel.WARNING, "An Exception occurred while trying to check a PooledConection into a ResourcePool.", e);
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
/*      */     public void connectionErrorOccurred(ConnectionEvent evt) {
/*      */       int status;
/*  941 */       if (C3P0PooledConnectionPool.logger.isLoggable(MLevel.FINE)) {
/*  942 */         C3P0PooledConnectionPool.logger.fine("CONNECTION ERROR OCCURRED!");
/*      */       }
/*  944 */       PooledConnection pc = (PooledConnection)evt.getSource();
/*      */       
/*  946 */       if (pc instanceof C3P0PooledConnection) {
/*  947 */         status = ((C3P0PooledConnection)pc).getConnectionStatus();
/*  948 */       } else if (pc instanceof NewPooledConnection) {
/*  949 */         status = ((NewPooledConnection)pc).getConnectionStatus();
/*      */       } else {
/*  951 */         status = -1;
/*      */       } 
/*  953 */       int final_status = status;
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
/*  965 */       doMarkPoolStatus(pc, final_status);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void doMarkPoolStatus(PooledConnection pc, int status) {
/*      */       try {
/*  972 */         switch (status) {
/*      */           
/*      */           case 0:
/*  975 */             throw new RuntimeException("connectionErrorOcccurred() should only be called for errors fatal to the Connection.");
/*      */           
/*      */           case -1:
/*  978 */             C3P0PooledConnectionPool.this.rp.markBroken(pc);
/*      */             return;
/*      */           case -8:
/*  981 */             if (C3P0PooledConnectionPool.logger.isLoggable(MLevel.WARNING)) {
/*  982 */               C3P0PooledConnectionPool.logger.warning("A ConnectionTest has failed, reporting that all previously acquired Connections are likely invalid. The pool will be reset.");
/*      */             }
/*  984 */             C3P0PooledConnectionPool.this.rp.resetPool();
/*      */             return;
/*      */         } 
/*  987 */         throw new RuntimeException("Bad Connection Tester (" + C3P0PooledConnectionPool.this.connectionTester + ") " + "returned invalid status (" + status + ").");
/*      */ 
/*      */       
/*      */       }
/*  991 */       catch (ResourcePoolException e) {
/*      */ 
/*      */ 
/*      */         
/*  995 */         C3P0PooledConnectionPool.logger.log(MLevel.WARNING, "Uh oh... our resource pool is probably broken!", (Throwable)e);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public int getNumConnections() throws SQLException {
/*      */     try {
/* 1002 */       return this.rp.getPoolSize();
/* 1003 */     } catch (Exception e) {
/*      */ 
/*      */       
/* 1006 */       logger.log(MLevel.WARNING, null, e);
/* 1007 */       throw SqlUtils.toSQLException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getNumIdleConnections() throws SQLException {
/*      */     try {
/* 1013 */       return this.rp.getAvailableCount();
/* 1014 */     } catch (Exception e) {
/*      */ 
/*      */       
/* 1017 */       logger.log(MLevel.WARNING, null, e);
/* 1018 */       throw SqlUtils.toSQLException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getNumBusyConnections() throws SQLException {
/*      */     try {
/* 1026 */       synchronized (this.rp) {
/* 1027 */         return this.rp.getAwaitingCheckinCount() - this.rp.getExcludedCount();
/*      */       } 
/* 1029 */     } catch (Exception e) {
/*      */ 
/*      */       
/* 1032 */       logger.log(MLevel.WARNING, null, e);
/* 1033 */       throw SqlUtils.toSQLException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getNumUnclosedOrphanedConnections() throws SQLException {
/*      */     try {
/* 1039 */       return this.rp.getExcludedCount();
/* 1040 */     } catch (Exception e) {
/*      */ 
/*      */       
/* 1043 */       logger.log(MLevel.WARNING, null, e);
/* 1044 */       throw SqlUtils.toSQLException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public long getStartTime() throws SQLException {
/*      */     try {
/* 1050 */       return this.rp.getStartTime();
/* 1051 */     } catch (Exception e) {
/*      */ 
/*      */       
/* 1054 */       logger.log(MLevel.WARNING, null, e);
/* 1055 */       throw SqlUtils.toSQLException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public long getUpTime() throws SQLException {
/*      */     try {
/* 1061 */       return this.rp.getUpTime();
/* 1062 */     } catch (Exception e) {
/*      */ 
/*      */       
/* 1065 */       logger.log(MLevel.WARNING, null, e);
/* 1066 */       throw SqlUtils.toSQLException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public long getNumFailedCheckins() throws SQLException {
/*      */     try {
/* 1072 */       return this.rp.getNumFailedCheckins();
/* 1073 */     } catch (Exception e) {
/*      */ 
/*      */       
/* 1076 */       logger.log(MLevel.WARNING, null, e);
/* 1077 */       throw SqlUtils.toSQLException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public long getNumFailedCheckouts() throws SQLException {
/*      */     try {
/* 1083 */       return this.rp.getNumFailedCheckouts();
/* 1084 */     } catch (Exception e) {
/*      */ 
/*      */       
/* 1087 */       logger.log(MLevel.WARNING, null, e);
/* 1088 */       throw SqlUtils.toSQLException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public long getNumFailedIdleTests() throws SQLException {
/*      */     try {
/* 1094 */       return this.rp.getNumFailedIdleTests();
/* 1095 */     } catch (Exception e) {
/*      */ 
/*      */       
/* 1098 */       logger.log(MLevel.WARNING, null, e);
/* 1099 */       throw SqlUtils.toSQLException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public Throwable getLastCheckinFailure() throws SQLException {
/*      */     try {
/* 1105 */       return this.rp.getLastCheckinFailure();
/* 1106 */     } catch (Exception e) {
/*      */ 
/*      */       
/* 1109 */       logger.log(MLevel.WARNING, null, e);
/* 1110 */       throw SqlUtils.toSQLException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public Throwable getLastCheckoutFailure() throws SQLException {
/*      */     try {
/* 1116 */       return this.rp.getLastCheckoutFailure();
/* 1117 */     } catch (Exception e) {
/*      */ 
/*      */       
/* 1120 */       logger.log(MLevel.WARNING, null, e);
/* 1121 */       throw SqlUtils.toSQLException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public Throwable getLastIdleTestFailure() throws SQLException {
/*      */     try {
/* 1127 */       return this.rp.getLastIdleCheckFailure();
/* 1128 */     } catch (Exception e) {
/*      */ 
/*      */       
/* 1131 */       logger.log(MLevel.WARNING, null, e);
/* 1132 */       throw SqlUtils.toSQLException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public Throwable getLastConnectionTestFailure() throws SQLException {
/*      */     try {
/* 1138 */       return this.rp.getLastResourceTestFailure();
/* 1139 */     } catch (Exception e) {
/*      */ 
/*      */       
/* 1142 */       logger.log(MLevel.WARNING, null, e);
/* 1143 */       throw SqlUtils.toSQLException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public Throwable getLastAcquisitionFailure() throws SQLException {
/*      */     try {
/* 1149 */       return this.rp.getLastAcquisitionFailure();
/* 1150 */     } catch (Exception e) {
/*      */ 
/*      */       
/* 1153 */       logger.log(MLevel.WARNING, null, e);
/* 1154 */       throw SqlUtils.toSQLException(e);
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
/*      */   public void reset() throws SQLException {
/*      */     try {
/* 1167 */       this.rp.resetPool();
/* 1168 */     } catch (Exception e) {
/*      */ 
/*      */       
/* 1171 */       logger.log(MLevel.WARNING, null, e);
/* 1172 */       throw SqlUtils.toSQLException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   static final class ThrowableHolderPool
/*      */   {
/* 1178 */     LinkedList l = new LinkedList();
/*      */ 
/*      */     
/*      */     synchronized Throwable[] getThrowableHolder() {
/* 1182 */       if (this.l.size() == 0) {
/* 1183 */         return new Throwable[1];
/*      */       }
/* 1185 */       return this.l.remove(0);
/*      */     }
/*      */ 
/*      */     
/*      */     synchronized void returnThrowableHolder(Throwable[] th) {
/* 1190 */       th[0] = null;
/* 1191 */       this.l.add(th);
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/C3P0PooledConnectionPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */