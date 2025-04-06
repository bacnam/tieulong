/*     */ package com.mchange.v2.c3p0.impl;
/*     */ 
/*     */ import com.mchange.lang.ThrowableUtils;
/*     */ import com.mchange.v2.c3p0.C3P0Registry;
/*     */ import com.mchange.v2.c3p0.PooledDataSource;
/*     */ import com.mchange.v2.c3p0.cfg.C3P0Config;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Set;
/*     */ import javax.sql.ConnectionPoolDataSource;
/*     */ import javax.sql.PooledConnection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractPoolBackedDataSource
/*     */   extends PoolBackedDataSourceBase
/*     */   implements PooledDataSource
/*     */ {
/*  61 */   static final MLogger logger = MLog.getLogger(AbstractPoolBackedDataSource.class);
/*     */   
/*     */   static final String NO_CPDS_ERR_MSG = "Attempted to use an uninitialized PoolBackedDataSource. Please call setConnectionPoolDataSource( ... ) to initialize.";
/*     */   
/*     */   transient C3P0PooledConnectionPoolManager poolManager;
/*     */   
/*     */   transient boolean is_closed = false;
/*     */   
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   private static final short VERSION = 1;
/*     */   
/*     */   protected AbstractPoolBackedDataSource(boolean autoregister) {
/*  74 */     super(autoregister);
/*  75 */     setUpPropertyEvents();
/*     */   }
/*     */ 
/*     */   
/*     */   private void setUpPropertyEvents() {
/*  80 */     PropertyChangeListener l = new PropertyChangeListener()
/*     */       {
/*     */         public void propertyChange(PropertyChangeEvent evt)
/*     */         {
/*  84 */           AbstractPoolBackedDataSource.this.resetPoolManager(false); }
/*     */       };
/*  86 */     addPropertyChangeListener(l);
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
/*     */   protected void initializeNamedConfig(String configName, boolean shouldBindUserOverridesAsString) {
/*     */     try {
/*  99 */       if (configName != null) {
/*     */         
/* 101 */         C3P0Config.bindNamedConfigToBean(this, configName, shouldBindUserOverridesAsString);
/* 102 */         if (getDataSourceName().equals(getIdentityToken())) {
/* 103 */           setDataSourceName(configName);
/*     */         }
/*     */       } 
/* 106 */     } catch (Exception e) {
/*     */       
/* 108 */       if (logger.isLoggable(MLevel.WARNING)) {
/* 109 */         logger.log(MLevel.WARNING, "Error binding PoolBackedDataSource to named-config '" + configName + "'. Some default-config values may be used.", e);
/*     */       }
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDataSourceName() {
/* 131 */     String out = super.getDataSourceName();
/* 132 */     if (out == null)
/* 133 */       out = getIdentityToken(); 
/* 134 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/* 140 */     PooledConnection pc = getPoolManager().getPool().checkoutPooledConnection();
/* 141 */     return pc.getConnection();
/*     */   }
/*     */ 
/*     */   
/*     */   public Connection getConnection(String username, String password) throws SQLException {
/* 146 */     PooledConnection pc = getPoolManager().getPool(username, password).checkoutPooledConnection();
/* 147 */     return pc.getConnection();
/*     */   }
/*     */   
/*     */   public PrintWriter getLogWriter() throws SQLException {
/* 151 */     return assertCpds().getLogWriter();
/*     */   }
/*     */   public void setLogWriter(PrintWriter out) throws SQLException {
/* 154 */     assertCpds().setLogWriter(out);
/*     */   }
/*     */   public int getLoginTimeout() throws SQLException {
/* 157 */     return assertCpds().getLoginTimeout();
/*     */   }
/*     */   public void setLoginTimeout(int seconds) throws SQLException {
/* 160 */     assertCpds().setLoginTimeout(seconds);
/*     */   }
/*     */   
/*     */   public int getNumConnections() throws SQLException {
/* 164 */     return getPoolManager().getPool().getNumConnections();
/*     */   }
/*     */   public int getNumIdleConnections() throws SQLException {
/* 167 */     return getPoolManager().getPool().getNumIdleConnections();
/*     */   }
/*     */   public int getNumBusyConnections() throws SQLException {
/* 170 */     return getPoolManager().getPool().getNumBusyConnections();
/*     */   }
/*     */   public int getNumUnclosedOrphanedConnections() throws SQLException {
/* 173 */     return getPoolManager().getPool().getNumUnclosedOrphanedConnections();
/*     */   }
/*     */   public int getNumConnectionsDefaultUser() throws SQLException {
/* 176 */     return getNumConnections();
/*     */   }
/*     */   public int getNumIdleConnectionsDefaultUser() throws SQLException {
/* 179 */     return getNumIdleConnections();
/*     */   }
/*     */   public int getNumBusyConnectionsDefaultUser() throws SQLException {
/* 182 */     return getNumBusyConnections();
/*     */   }
/*     */   public int getNumUnclosedOrphanedConnectionsDefaultUser() throws SQLException {
/* 185 */     return getNumUnclosedOrphanedConnections();
/*     */   }
/*     */   public int getStatementCacheNumStatementsDefaultUser() throws SQLException {
/* 188 */     return getPoolManager().getPool().getStatementCacheNumStatements();
/*     */   }
/*     */   public int getStatementCacheNumCheckedOutDefaultUser() throws SQLException {
/* 191 */     return getPoolManager().getPool().getStatementCacheNumCheckedOut();
/*     */   }
/*     */   public int getStatementCacheNumConnectionsWithCachedStatementsDefaultUser() throws SQLException {
/* 194 */     return getPoolManager().getPool().getStatementCacheNumConnectionsWithCachedStatements();
/*     */   }
/*     */   public float getEffectivePropertyCycleDefaultUser() throws SQLException {
/* 197 */     return getPoolManager().getPool().getEffectivePropertyCycle();
/*     */   }
/*     */   public long getStartTimeMillisDefaultUser() throws SQLException {
/* 200 */     return getPoolManager().getPool().getStartTime();
/*     */   }
/*     */   public long getUpTimeMillisDefaultUser() throws SQLException {
/* 203 */     return getPoolManager().getPool().getUpTime();
/*     */   }
/*     */   public long getNumFailedCheckinsDefaultUser() throws SQLException {
/* 206 */     return getPoolManager().getPool().getNumFailedCheckins();
/*     */   }
/*     */   public long getNumFailedCheckoutsDefaultUser() throws SQLException {
/* 209 */     return getPoolManager().getPool().getNumFailedCheckouts();
/*     */   }
/*     */   public long getNumFailedIdleTestsDefaultUser() throws SQLException {
/* 212 */     return getPoolManager().getPool().getNumFailedIdleTests();
/*     */   }
/*     */   public int getNumThreadsAwaitingCheckoutDefaultUser() throws SQLException {
/* 215 */     return getPoolManager().getPool().getNumThreadsAwaitingCheckout();
/*     */   }
/*     */   public int getThreadPoolSize() throws SQLException {
/* 218 */     return getPoolManager().getThreadPoolSize();
/*     */   }
/*     */   public int getThreadPoolNumActiveThreads() throws SQLException {
/* 221 */     return getPoolManager().getThreadPoolNumActiveThreads();
/*     */   }
/*     */   public int getThreadPoolNumIdleThreads() throws SQLException {
/* 224 */     return getPoolManager().getThreadPoolNumIdleThreads();
/*     */   }
/*     */   public int getThreadPoolNumTasksPending() throws SQLException {
/* 227 */     return getPoolManager().getThreadPoolNumTasksPending();
/*     */   }
/*     */   public String sampleThreadPoolStackTraces() throws SQLException {
/* 230 */     return getPoolManager().getThreadPoolStackTraces();
/*     */   }
/*     */   public String sampleThreadPoolStatus() throws SQLException {
/* 233 */     return getPoolManager().getThreadPoolStatus();
/*     */   }
/*     */   public String sampleStatementCacheStatusDefaultUser() throws SQLException {
/* 236 */     return getPoolManager().getPool().dumpStatementCacheStatus();
/*     */   }
/*     */   public String sampleStatementCacheStatus(String username, String password) throws SQLException {
/* 239 */     return assertAuthPool(username, password).dumpStatementCacheStatus();
/*     */   }
/*     */   public Throwable getLastAcquisitionFailureDefaultUser() throws SQLException {
/* 242 */     return getPoolManager().getPool().getLastAcquisitionFailure();
/*     */   }
/*     */   public Throwable getLastCheckinFailureDefaultUser() throws SQLException {
/* 245 */     return getPoolManager().getPool().getLastCheckinFailure();
/*     */   }
/*     */   public Throwable getLastCheckoutFailureDefaultUser() throws SQLException {
/* 248 */     return getPoolManager().getPool().getLastCheckoutFailure();
/*     */   }
/*     */   public Throwable getLastIdleTestFailureDefaultUser() throws SQLException {
/* 251 */     return getPoolManager().getPool().getLastIdleTestFailure();
/*     */   }
/*     */   public Throwable getLastConnectionTestFailureDefaultUser() throws SQLException {
/* 254 */     return getPoolManager().getPool().getLastConnectionTestFailure();
/*     */   }
/*     */   public Throwable getLastAcquisitionFailure(String username, String password) throws SQLException {
/* 257 */     return assertAuthPool(username, password).getLastAcquisitionFailure();
/*     */   }
/*     */   public Throwable getLastCheckinFailure(String username, String password) throws SQLException {
/* 260 */     return assertAuthPool(username, password).getLastCheckinFailure();
/*     */   }
/*     */   public Throwable getLastCheckoutFailure(String username, String password) throws SQLException {
/* 263 */     return assertAuthPool(username, password).getLastCheckoutFailure();
/*     */   }
/*     */   public Throwable getLastIdleTestFailure(String username, String password) throws SQLException {
/* 266 */     return assertAuthPool(username, password).getLastIdleTestFailure();
/*     */   }
/*     */   public Throwable getLastConnectionTestFailure(String username, String password) throws SQLException {
/* 269 */     return assertAuthPool(username, password).getLastConnectionTestFailure();
/*     */   }
/*     */   public int getNumThreadsAwaitingCheckout(String username, String password) throws SQLException {
/* 272 */     return assertAuthPool(username, password).getNumThreadsAwaitingCheckout();
/*     */   }
/*     */   
/*     */   public String sampleLastAcquisitionFailureStackTraceDefaultUser() throws SQLException {
/* 276 */     Throwable t = getLastAcquisitionFailureDefaultUser();
/* 277 */     return (t == null) ? null : ThrowableUtils.extractStackTrace(t);
/*     */   }
/*     */ 
/*     */   
/*     */   public String sampleLastCheckinFailureStackTraceDefaultUser() throws SQLException {
/* 282 */     Throwable t = getLastCheckinFailureDefaultUser();
/* 283 */     return (t == null) ? null : ThrowableUtils.extractStackTrace(t);
/*     */   }
/*     */ 
/*     */   
/*     */   public String sampleLastCheckoutFailureStackTraceDefaultUser() throws SQLException {
/* 288 */     Throwable t = getLastCheckoutFailureDefaultUser();
/* 289 */     return (t == null) ? null : ThrowableUtils.extractStackTrace(t);
/*     */   }
/*     */ 
/*     */   
/*     */   public String sampleLastIdleTestFailureStackTraceDefaultUser() throws SQLException {
/* 294 */     Throwable t = getLastIdleTestFailureDefaultUser();
/* 295 */     return (t == null) ? null : ThrowableUtils.extractStackTrace(t);
/*     */   }
/*     */ 
/*     */   
/*     */   public String sampleLastConnectionTestFailureStackTraceDefaultUser() throws SQLException {
/* 300 */     Throwable t = getLastConnectionTestFailureDefaultUser();
/* 301 */     return (t == null) ? null : ThrowableUtils.extractStackTrace(t);
/*     */   }
/*     */ 
/*     */   
/*     */   public String sampleLastAcquisitionFailureStackTrace(String username, String password) throws SQLException {
/* 306 */     Throwable t = getLastAcquisitionFailure(username, password);
/* 307 */     return (t == null) ? null : ThrowableUtils.extractStackTrace(t);
/*     */   }
/*     */ 
/*     */   
/*     */   public String sampleLastCheckinFailureStackTrace(String username, String password) throws SQLException {
/* 312 */     Throwable t = getLastCheckinFailure(username, password);
/* 313 */     return (t == null) ? null : ThrowableUtils.extractStackTrace(t);
/*     */   }
/*     */ 
/*     */   
/*     */   public String sampleLastCheckoutFailureStackTrace(String username, String password) throws SQLException {
/* 318 */     Throwable t = getLastCheckoutFailure(username, password);
/* 319 */     return (t == null) ? null : ThrowableUtils.extractStackTrace(t);
/*     */   }
/*     */ 
/*     */   
/*     */   public String sampleLastIdleTestFailureStackTrace(String username, String password) throws SQLException {
/* 324 */     Throwable t = getLastIdleTestFailure(username, password);
/* 325 */     return (t == null) ? null : ThrowableUtils.extractStackTrace(t);
/*     */   }
/*     */ 
/*     */   
/*     */   public String sampleLastConnectionTestFailureStackTrace(String username, String password) throws SQLException {
/* 330 */     Throwable t = getLastConnectionTestFailure(username, password);
/* 331 */     return (t == null) ? null : ThrowableUtils.extractStackTrace(t);
/*     */   }
/*     */   
/*     */   public void softResetDefaultUser() throws SQLException {
/* 335 */     getPoolManager().getPool().reset();
/*     */   }
/*     */   public int getNumConnections(String username, String password) throws SQLException {
/* 338 */     return assertAuthPool(username, password).getNumConnections();
/*     */   }
/*     */   public int getNumIdleConnections(String username, String password) throws SQLException {
/* 341 */     return assertAuthPool(username, password).getNumIdleConnections();
/*     */   }
/*     */   public int getNumBusyConnections(String username, String password) throws SQLException {
/* 344 */     return assertAuthPool(username, password).getNumBusyConnections();
/*     */   }
/*     */   public int getNumUnclosedOrphanedConnections(String username, String password) throws SQLException {
/* 347 */     return assertAuthPool(username, password).getNumUnclosedOrphanedConnections();
/*     */   }
/*     */   public int getStatementCacheNumStatements(String username, String password) throws SQLException {
/* 350 */     return assertAuthPool(username, password).getStatementCacheNumStatements();
/*     */   }
/*     */   public int getStatementCacheNumCheckedOut(String username, String password) throws SQLException {
/* 353 */     return assertAuthPool(username, password).getStatementCacheNumCheckedOut();
/*     */   }
/*     */   public int getStatementCacheNumConnectionsWithCachedStatements(String username, String password) throws SQLException {
/* 356 */     return assertAuthPool(username, password).getStatementCacheNumConnectionsWithCachedStatements();
/*     */   }
/*     */   public float getEffectivePropertyCycle(String username, String password) throws SQLException {
/* 359 */     return assertAuthPool(username, password).getEffectivePropertyCycle();
/*     */   }
/*     */   public long getStartTimeMillis(String username, String password) throws SQLException {
/* 362 */     return assertAuthPool(username, password).getStartTime();
/*     */   }
/*     */   public long getUpTimeMillis(String username, String password) throws SQLException {
/* 365 */     return assertAuthPool(username, password).getUpTime();
/*     */   }
/*     */   public long getNumFailedCheckins(String username, String password) throws SQLException {
/* 368 */     return assertAuthPool(username, password).getNumFailedCheckins();
/*     */   }
/*     */   public long getNumFailedCheckouts(String username, String password) throws SQLException {
/* 371 */     return assertAuthPool(username, password).getNumFailedCheckouts();
/*     */   }
/*     */   public long getNumFailedIdleTests(String username, String password) throws SQLException {
/* 374 */     return assertAuthPool(username, password).getNumFailedIdleTests();
/*     */   }
/*     */   public void softReset(String username, String password) throws SQLException {
/* 377 */     assertAuthPool(username, password).reset();
/*     */   }
/*     */   public int getNumBusyConnectionsAllUsers() throws SQLException {
/* 380 */     return getPoolManager().getNumBusyConnectionsAllAuths();
/*     */   }
/*     */   public int getNumIdleConnectionsAllUsers() throws SQLException {
/* 383 */     return getPoolManager().getNumIdleConnectionsAllAuths();
/*     */   }
/*     */   public int getNumConnectionsAllUsers() throws SQLException {
/* 386 */     return getPoolManager().getNumConnectionsAllAuths();
/*     */   }
/*     */   public int getNumUnclosedOrphanedConnectionsAllUsers() throws SQLException {
/* 389 */     return getPoolManager().getNumUnclosedOrphanedConnectionsAllAuths();
/*     */   }
/*     */   public int getStatementCacheNumStatementsAllUsers() throws SQLException {
/* 392 */     return getPoolManager().getStatementCacheNumStatementsAllUsers();
/*     */   }
/*     */   public int getStatementCacheNumCheckedOutStatementsAllUsers() throws SQLException {
/* 395 */     return getPoolManager().getStatementCacheNumCheckedOutStatementsAllUsers();
/*     */   }
/*     */   public int getStatementCacheNumConnectionsWithCachedStatementsAllUsers() throws SQLException {
/* 398 */     return getPoolManager().getStatementCacheNumConnectionsWithCachedStatementsAllUsers();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStatementDestroyerNumConnectionsInUseAllUsers() throws SQLException {
/* 403 */     return getPoolManager().getStatementDestroyerNumConnectionsInUseAllUsers();
/*     */   }
/*     */   public int getStatementDestroyerNumConnectionsWithDeferredDestroyStatementsAllUsers() throws SQLException {
/* 406 */     return getPoolManager().getStatementDestroyerNumConnectionsWithDeferredDestroyStatementsAllUsers();
/*     */   }
/*     */   public int getStatementDestroyerNumDeferredDestroyStatementsAllUsers() throws SQLException {
/* 409 */     return getPoolManager().getStatementDestroyerNumDeferredDestroyStatementsAllUsers();
/*     */   }
/*     */   public int getStatementDestroyerNumConnectionsInUseDefaultUser() throws SQLException {
/* 412 */     return getPoolManager().getPool().getStatementDestroyerNumConnectionsInUse();
/*     */   }
/*     */   public int getStatementDestroyerNumConnectionsWithDeferredDestroyStatementsDefaultUser() throws SQLException {
/* 415 */     return getPoolManager().getPool().getStatementDestroyerNumConnectionsWithDeferredDestroyStatements();
/*     */   }
/*     */   public int getStatementDestroyerNumDeferredDestroyStatementsDefaultUser() throws SQLException {
/* 418 */     return getPoolManager().getPool().getStatementDestroyerNumDeferredDestroyStatements();
/*     */   }
/*     */   public int getStatementDestroyerNumThreads() throws SQLException {
/* 421 */     return getPoolManager().getStatementDestroyerNumThreads();
/*     */   }
/*     */   public int getStatementDestroyerNumActiveThreads() throws SQLException {
/* 424 */     return getPoolManager().getStatementDestroyerNumActiveThreads();
/*     */   }
/*     */   public int getStatementDestroyerNumIdleThreads() throws SQLException {
/* 427 */     return getPoolManager().getStatementDestroyerNumIdleThreads();
/*     */   }
/*     */   public int getStatementDestroyerNumTasksPending() throws SQLException {
/* 430 */     return getPoolManager().getStatementDestroyerNumTasksPending();
/*     */   }
/*     */   public int getStatementDestroyerNumConnectionsInUse(String username, String password) throws SQLException {
/* 433 */     return assertAuthPool(username, password).getStatementDestroyerNumConnectionsInUse();
/*     */   }
/*     */   public int getStatementDestroyerNumConnectionsWithDeferredDestroyStatements(String username, String password) throws SQLException {
/* 436 */     return assertAuthPool(username, password).getStatementDestroyerNumConnectionsWithDeferredDestroyStatements();
/*     */   }
/*     */   public int getStatementDestroyerNumDeferredDestroyStatements(String username, String password) throws SQLException {
/* 439 */     return assertAuthPool(username, password).getStatementDestroyerNumDeferredDestroyStatements();
/*     */   }
/*     */   public String sampleStatementDestroyerStackTraces() throws SQLException {
/* 442 */     return getPoolManager().getStatementDestroyerStackTraces();
/*     */   }
/*     */   public String sampleStatementDestroyerStatus() throws SQLException {
/* 445 */     return getPoolManager().getStatementDestroyerStatus();
/*     */   }
/*     */   
/*     */   public void softResetAllUsers() throws SQLException {
/* 449 */     getPoolManager().softResetAllAuths();
/*     */   }
/*     */   public int getNumUserPools() throws SQLException {
/* 452 */     return getPoolManager().getNumManagedAuths();
/*     */   }
/*     */   
/*     */   public Collection getAllUsers() throws SQLException {
/* 456 */     LinkedList<String> out = new LinkedList();
/* 457 */     Set auths = getPoolManager().getManagedAuths();
/* 458 */     for (Iterator<DbAuth> ii = auths.iterator(); ii.hasNext();)
/* 459 */       out.add(((DbAuth)ii.next()).getUser()); 
/* 460 */     return Collections.unmodifiableList(out);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void hardReset() {
/* 465 */     resetPoolManager();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void close() {
/* 470 */     resetPoolManager();
/* 471 */     this.is_closed = true;
/*     */     
/* 473 */     C3P0Registry.markClosed(this);
/*     */     
/* 475 */     if (logger.isLoggable(MLevel.FINEST))
/*     */     {
/* 477 */       logger.log(MLevel.FINEST, getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " has been closed. ", new Exception("DEBUG STACK TRACE for PoolBackedDataSource.close()."));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close(boolean force_destroy) {
/* 489 */     close();
/*     */   }
/*     */   
/*     */   public synchronized void resetPoolManager() {
/* 493 */     resetPoolManager(true);
/*     */   }
/*     */   
/*     */   public synchronized void resetPoolManager(boolean close_checked_out_connections) {
/* 497 */     if (this.poolManager != null) {
/*     */       
/* 499 */       this.poolManager.close(close_checked_out_connections);
/* 500 */       this.poolManager = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized ConnectionPoolDataSource assertCpds() throws SQLException {
/* 506 */     if (this.is_closed) {
/* 507 */       throw new SQLException(this + " has been closed() -- you can no longer use it.");
/*     */     }
/* 509 */     ConnectionPoolDataSource out = getConnectionPoolDataSource();
/* 510 */     if (out == null)
/* 511 */       throw new SQLException("Attempted to use an uninitialized PoolBackedDataSource. Please call setConnectionPoolDataSource( ... ) to initialize."); 
/* 512 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized C3P0PooledConnectionPoolManager getPoolManager() throws SQLException {
/* 517 */     if (this.poolManager == null) {
/*     */       
/* 519 */       ConnectionPoolDataSource cpds = assertCpds();
/* 520 */       this.poolManager = new C3P0PooledConnectionPoolManager(cpds, null, null, getNumHelperThreads(), getIdentityToken(), getDataSourceName());
/* 521 */       if (logger.isLoggable(MLevel.INFO))
/* 522 */         logger.info("Initializing c3p0 pool... " + toString()); 
/*     */     } 
/* 524 */     return this.poolManager;
/*     */   }
/*     */ 
/*     */   
/*     */   private C3P0PooledConnectionPool assertAuthPool(String username, String password) throws SQLException {
/* 529 */     C3P0PooledConnectionPool authPool = getPoolManager().getPool(username, password, false);
/* 530 */     if (authPool == null) {
/* 531 */       throw new SQLException("No pool has been yet been established for Connections authenticated by user '" + username + "' with the password provided. [Use getConnection( username, password ) " + "to initialize such a pool.]");
/*     */     }
/*     */ 
/*     */     
/* 535 */     return authPool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream oos) throws IOException {
/* 544 */     oos.writeShort(1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 549 */     short version = ois.readShort();
/* 550 */     switch (version) {
/*     */       
/*     */       case 1:
/* 553 */         setUpPropertyEvents();
/*     */         return;
/*     */     } 
/* 556 */     throw new IOException("Unsupported Serialized Version: " + version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWrapperFor(Class<?> iface) throws SQLException {
/* 563 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T unwrap(Class<T> iface) throws SQLException {
/* 568 */     throw new SQLException(this + " is not a Wrapper for " + iface.getName());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/AbstractPoolBackedDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */