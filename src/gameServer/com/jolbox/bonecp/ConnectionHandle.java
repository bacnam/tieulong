/*      */ package com.jolbox.bonecp;
/*      */ 
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.jolbox.bonecp.hooks.AcquireFailConfig;
/*      */ import com.jolbox.bonecp.hooks.ConnectionHook;
/*      */ import com.jolbox.bonecp.hooks.ConnectionState;
/*      */ import com.jolbox.bonecp.proxy.TransactionRecoveryResult;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.reflect.Proxy;
/*      */ import java.sql.Array;
/*      */ import java.sql.Blob;
/*      */ import java.sql.CallableStatement;
/*      */ import java.sql.Clob;
/*      */ import java.sql.Connection;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.NClob;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.SQLClientInfoException;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.SQLXML;
/*      */ import java.sql.Savepoint;
/*      */ import java.sql.Statement;
/*      */ import java.sql.Struct;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import org.slf4j.Logger;
/*      */ import org.slf4j.LoggerFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ConnectionHandle
/*      */   implements Connection
/*      */ {
/*      */   private static final String STATEMENT_NOT_CLOSED = "Stack trace of location where statement was opened follows:\n%s";
/*      */   private static final String LOG_ERROR_MESSAGE = "Connection closed twice exception detected.\n%s\n%s\n";
/*      */   private static final String CLOSED_TWICE_EXCEPTION_MESSAGE = "Connection closed from thread [%s] was closed again.\nStack trace of location where connection was first closed follows:\n";
/*   68 */   private Connection connection = null;
/*      */   
/*   70 */   private long connectionLastUsedInMs = System.currentTimeMillis();
/*      */   
/*   72 */   private long connectionLastResetInMs = System.currentTimeMillis();
/*      */   
/*   74 */   private long connectionCreationTimeInMs = System.currentTimeMillis();
/*      */ 
/*      */   
/*      */   private BoneCP pool;
/*      */ 
/*      */   
/*      */   protected boolean possiblyBroken;
/*      */ 
/*      */   
/*      */   protected boolean logicallyClosed = false;
/*      */ 
/*      */   
/*   86 */   private ConnectionPartition originatingPartition = null;
/*      */   
/*   88 */   private IStatementCache preparedStatementCache = null;
/*      */   
/*   90 */   private IStatementCache callableStatementCache = null;
/*      */   
/*   92 */   private static Logger logger = LoggerFactory.getLogger(ConnectionHandle.class);
/*      */   
/*      */   private Object debugHandle;
/*      */   
/*      */   private ConnectionHook connectionHook;
/*      */   
/*      */   private boolean doubleCloseCheck;
/*      */   
/*  100 */   private volatile String doubleCloseException = null;
/*      */   
/*      */   private boolean logStatementsEnabled;
/*      */   
/*      */   protected boolean statementCachingEnabled;
/*      */   
/*      */   private List<ReplayLog> replayLog;
/*      */   
/*      */   private boolean inReplayMode;
/*      */   
/*  110 */   protected TransactionRecoveryResult recoveryResult = new TransactionRecoveryResult();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String url;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Thread threadUsingConnection;
/*      */ 
/*      */ 
/*      */   
/*      */   private long maxConnectionAgeInMs;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean statisticsEnabled;
/*      */ 
/*      */ 
/*      */   
/*      */   private Statistics statistics;
/*      */ 
/*      */ 
/*      */   
/*      */   private volatile Thread threadWatch;
/*      */ 
/*      */ 
/*      */   
/*      */   private Map<Connection, Reference<ConnectionHandle>> finalizableRefs;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean connectionTrackingDisabled;
/*      */ 
/*      */ 
/*      */   
/*  148 */   private static final ImmutableSet<String> sqlStateDBFailureCodes = ImmutableSet.of("08001", "08007", "08S01", "57P01");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConnectionHandle(String url, String username, String password, BoneCP pool) throws SQLException {
/*  168 */     this.pool = pool;
/*  169 */     this.url = url;
/*  170 */     this.connection = obtainInternalConnection();
/*  171 */     this.finalizableRefs = this.pool.getFinalizableRefs();
/*  172 */     this.connectionTrackingDisabled = pool.getConfig().isDisableConnectionTracking();
/*  173 */     this.statisticsEnabled = pool.getConfig().isStatisticsEnabled();
/*  174 */     this.statistics = pool.getStatistics();
/*  175 */     if (this.pool.getConfig().isTransactionRecoveryEnabled()) {
/*  176 */       this.replayLog = new ArrayList<ReplayLog>(30);
/*      */       
/*  178 */       this.connection = MemorizeTransactionProxy.memorize(this.connection, this);
/*      */     } 
/*      */     
/*  181 */     this.maxConnectionAgeInMs = pool.getConfig().getMaxConnectionAge(TimeUnit.MILLISECONDS);
/*  182 */     this.doubleCloseCheck = pool.getConfig().isCloseConnectionWatch();
/*  183 */     this.logStatementsEnabled = pool.getConfig().isLogStatementsEnabled();
/*  184 */     int cacheSize = pool.getConfig().getStatementsCacheSize();
/*  185 */     if (cacheSize > 0) {
/*  186 */       this.preparedStatementCache = new StatementCache(cacheSize, pool.getConfig().isStatisticsEnabled(), pool.getStatistics());
/*  187 */       this.callableStatementCache = new StatementCache(cacheSize, pool.getConfig().isStatisticsEnabled(), pool.getStatistics());
/*  188 */       this.statementCachingEnabled = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Connection obtainInternalConnection() throws SQLException {
/*  197 */     boolean tryAgain = false;
/*  198 */     Connection result = null;
/*      */     
/*  200 */     int acquireRetryAttempts = this.pool.getConfig().getAcquireRetryAttempts();
/*  201 */     long acquireRetryDelayInMs = this.pool.getConfig().getAcquireRetryDelayInMs();
/*  202 */     AcquireFailConfig acquireConfig = new AcquireFailConfig();
/*  203 */     acquireConfig.setAcquireRetryAttempts(new AtomicInteger(acquireRetryAttempts));
/*  204 */     acquireConfig.setAcquireRetryDelayInMs(acquireRetryDelayInMs);
/*  205 */     acquireConfig.setLogMessage("Failed to acquire connection");
/*      */     
/*  207 */     this.connectionHook = this.pool.getConfig().getConnectionHook();
/*      */     
/*      */     do {
/*      */       try {
/*  211 */         this.connection = this.pool.obtainRawInternalConnection();
/*  212 */         tryAgain = false;
/*      */         
/*  214 */         if (acquireRetryAttempts != this.pool.getConfig().getAcquireRetryAttempts()) {
/*  215 */           logger.info("Successfully re-established connection to DB");
/*      */         }
/*      */         
/*  218 */         if (this.connectionHook != null) {
/*  219 */           this.connectionHook.onAcquire(this);
/*      */         }
/*      */         
/*  222 */         sendInitSQL();
/*  223 */         result = this.connection;
/*  224 */       } catch (SQLException e) {
/*      */         
/*  226 */         if (this.connectionHook != null) {
/*  227 */           tryAgain = this.connectionHook.onAcquireFail(e, acquireConfig);
/*      */         } else {
/*  229 */           logger.error("Failed to acquire connection. Sleeping for " + acquireRetryDelayInMs + "ms. Attempts left: " + acquireRetryAttempts, e);
/*      */           
/*      */           try {
/*  232 */             Thread.sleep(acquireRetryDelayInMs);
/*  233 */             if (acquireRetryAttempts > -1) {
/*  234 */               tryAgain = (acquireRetryAttempts-- != 0);
/*      */             }
/*  236 */           } catch (InterruptedException e1) {
/*  237 */             tryAgain = false;
/*      */           } 
/*      */         } 
/*  240 */         if (!tryAgain) {
/*  241 */           throw markPossiblyBroken(e);
/*      */         }
/*      */       } 
/*  244 */     } while (tryAgain);
/*      */     
/*  246 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConnectionHandle(Connection connection, IStatementCache preparedStatementCache, IStatementCache callableStatementCache, BoneCP pool) {
/*  256 */     this.connection = connection;
/*  257 */     this.preparedStatementCache = preparedStatementCache;
/*  258 */     this.callableStatementCache = callableStatementCache;
/*  259 */     this.pool = pool;
/*  260 */     this.url = null;
/*  261 */     int cacheSize = pool.getConfig().getStatementsCacheSize();
/*  262 */     if (cacheSize > 0) {
/*  263 */       this.statementCachingEnabled = true;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendInitSQL() throws SQLException {
/*  272 */     String initSQL = this.pool.getConfig().getInitSQL();
/*  273 */     if (initSQL != null) {
/*  274 */       Statement stmt = this.connection.createStatement();
/*  275 */       stmt.execute(initSQL);
/*  276 */       stmt.close();
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
/*      */   protected SQLException markPossiblyBroken(SQLException e) {
/*  290 */     String state = e.getSQLState();
/*  291 */     ConnectionState connectionState = (getConnectionHook() != null) ? getConnectionHook().onMarkPossiblyBroken(this, state, e) : ConnectionState.NOP;
/*  292 */     if (state == null) {
/*  293 */       state = "08999";
/*      */     }
/*      */     
/*  296 */     if ((sqlStateDBFailureCodes.contains(state) || connectionState.equals(ConnectionState.TERMINATE_ALL_CONNECTIONS)) && this.pool != null) {
/*  297 */       logger.error("Database access problem. Killing off all remaining connections in the connection pool. SQL State = " + state);
/*  298 */       this.pool.terminateAllConnections();
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
/*  315 */     char firstChar = state.charAt(0);
/*  316 */     if (connectionState.equals(ConnectionState.CONNECTION_POSSIBLY_BROKEN) || state.equals("40001") || state.equals("HY000") || state.startsWith("08") || (firstChar >= '5' && firstChar <= '9'))
/*      */     {
/*      */       
/*  319 */       this.possiblyBroken = true;
/*      */     }
/*      */ 
/*      */     
/*  323 */     if (this.possiblyBroken && getConnectionHook() != null) {
/*  324 */       this.possiblyBroken = getConnectionHook().onConnectionException(this, state, e);
/*      */     }
/*      */     
/*  327 */     return e;
/*      */   }
/*      */ 
/*      */   
/*      */   public void clearWarnings() throws SQLException {
/*  332 */     checkClosed();
/*      */     try {
/*  334 */       this.connection.clearWarnings();
/*  335 */     } catch (SQLException e) {
/*  336 */       throw markPossiblyBroken(e);
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
/*      */   private void checkClosed() throws SQLException {
/*  349 */     if (this.logicallyClosed) {
/*  350 */       throw new SQLException("Connection is closed!");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() throws SQLException {
/*      */     try {
/*  361 */       if (!this.logicallyClosed) {
/*  362 */         this.logicallyClosed = true;
/*  363 */         this.threadUsingConnection = null;
/*  364 */         if (this.threadWatch != null) {
/*  365 */           this.threadWatch.interrupt();
/*      */ 
/*      */           
/*  368 */           this.threadWatch = null;
/*      */         } 
/*  370 */         this.pool.releaseConnection(this);
/*      */         
/*  372 */         if (this.doubleCloseCheck) {
/*  373 */           this.doubleCloseException = this.pool.captureStackTrace("Connection closed from thread [%s] was closed again.\nStack trace of location where connection was first closed follows:\n");
/*      */         }
/*      */       }
/*  376 */       else if (this.doubleCloseCheck && this.doubleCloseException != null) {
/*  377 */         String currentLocation = this.pool.captureStackTrace("Last closed trace from thread [" + Thread.currentThread().getName() + "]:\n");
/*  378 */         logger.error(String.format("Connection closed twice exception detected.\n%s\n%s\n", new Object[] { this.doubleCloseException, currentLocation }));
/*      */       }
/*      */     
/*  381 */     } catch (SQLException e) {
/*  382 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void internalClose() throws SQLException {
/*      */     try {
/*  394 */       clearStatementCaches(true);
/*  395 */       if (this.connection != null) {
/*  396 */         this.connection.close();
/*      */         
/*  398 */         if (!this.connectionTrackingDisabled && this.finalizableRefs != null) {
/*  399 */           this.finalizableRefs.remove(this.connection);
/*      */         }
/*      */       } 
/*  402 */       this.logicallyClosed = true;
/*  403 */     } catch (SQLException e) {
/*  404 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void commit() throws SQLException {
/*  409 */     checkClosed();
/*      */     try {
/*  411 */       this.connection.commit();
/*  412 */     } catch (SQLException e) {
/*  413 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public Properties getClientInfo() throws SQLException {
/*  418 */     Properties result = null;
/*  419 */     checkClosed();
/*      */     try {
/*  421 */       result = this.connection.getClientInfo();
/*  422 */     } catch (SQLException e) {
/*  423 */       throw markPossiblyBroken(e);
/*      */     } 
/*  425 */     return result;
/*      */   }
/*      */   
/*      */   public String getClientInfo(String name) throws SQLException {
/*  429 */     String result = null;
/*  430 */     checkClosed();
/*      */     try {
/*  432 */       result = this.connection.getClientInfo(name);
/*  433 */     } catch (SQLException e) {
/*  434 */       throw markPossiblyBroken(e);
/*      */     } 
/*  436 */     return result;
/*      */   }
/*      */   
/*      */   public boolean isValid(int timeout) throws SQLException {
/*  440 */     boolean result = false;
/*  441 */     checkClosed();
/*      */     try {
/*  443 */       result = this.connection.isValid(timeout);
/*  444 */     } catch (SQLException e) {
/*  445 */       throw markPossiblyBroken(e);
/*      */     } 
/*  447 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWrapperFor(Class<?> iface) throws SQLException {
/*  452 */     return this.connection.isWrapperFor(iface);
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> T unwrap(Class<T> iface) throws SQLException {
/*  457 */     return this.connection.unwrap(iface);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setClientInfo(Properties properties) throws SQLClientInfoException {
/*  462 */     this.connection.setClientInfo(properties);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setClientInfo(String name, String value) throws SQLClientInfoException {
/*  467 */     this.connection.setClientInfo(name, value);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
/*  473 */     Struct result = null;
/*  474 */     checkClosed();
/*      */     try {
/*  476 */       result = this.connection.createStruct(typeName, attributes);
/*  477 */     } catch (SQLException e) {
/*  478 */       throw markPossiblyBroken(e);
/*      */     } 
/*  480 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
/*  486 */     Array result = null;
/*  487 */     checkClosed();
/*      */     try {
/*  489 */       result = this.connection.createArrayOf(typeName, elements);
/*  490 */     } catch (SQLException e) {
/*  491 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  494 */     return result;
/*      */   }
/*      */   
/*      */   public Blob createBlob() throws SQLException {
/*  498 */     Blob result = null;
/*  499 */     checkClosed();
/*      */     try {
/*  501 */       result = this.connection.createBlob();
/*  502 */     } catch (SQLException e) {
/*  503 */       throw markPossiblyBroken(e);
/*      */     } 
/*  505 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public Clob createClob() throws SQLException {
/*  510 */     Clob result = null;
/*  511 */     checkClosed();
/*      */     try {
/*  513 */       result = this.connection.createClob();
/*  514 */     } catch (SQLException e) {
/*  515 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  518 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public NClob createNClob() throws SQLException {
/*  523 */     NClob result = null;
/*  524 */     checkClosed();
/*      */     try {
/*  526 */       result = this.connection.createNClob();
/*  527 */     } catch (SQLException e) {
/*  528 */       throw markPossiblyBroken(e);
/*      */     } 
/*  530 */     return result;
/*      */   }
/*      */   
/*      */   public SQLXML createSQLXML() throws SQLException {
/*  534 */     SQLXML result = null;
/*  535 */     checkClosed();
/*      */     try {
/*  537 */       result = this.connection.createSQLXML();
/*  538 */     } catch (SQLException e) {
/*  539 */       throw markPossiblyBroken(e);
/*      */     } 
/*  541 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public Statement createStatement() throws SQLException {
/*  546 */     Statement result = null;
/*  547 */     checkClosed();
/*      */     try {
/*  549 */       result = new StatementHandle(this.connection.createStatement(), this, this.logStatementsEnabled);
/*  550 */     } catch (SQLException e) {
/*  551 */       throw markPossiblyBroken(e);
/*      */     } 
/*  553 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
/*  558 */     Statement result = null;
/*  559 */     checkClosed();
/*      */     try {
/*  561 */       result = new StatementHandle(this.connection.createStatement(resultSetType, resultSetConcurrency), this, this.logStatementsEnabled);
/*  562 */     } catch (SQLException e) {
/*  563 */       throw markPossiblyBroken(e);
/*      */     } 
/*  565 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
/*  571 */     Statement result = null;
/*  572 */     checkClosed();
/*      */     try {
/*  574 */       result = new StatementHandle(this.connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability), this, this.logStatementsEnabled);
/*  575 */     } catch (SQLException e) {
/*  576 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  579 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getAutoCommit() throws SQLException {
/*  584 */     boolean result = false;
/*  585 */     checkClosed();
/*      */     try {
/*  587 */       result = this.connection.getAutoCommit();
/*  588 */     } catch (SQLException e) {
/*  589 */       throw markPossiblyBroken(e);
/*      */     } 
/*  591 */     return result;
/*      */   }
/*      */   
/*      */   public String getCatalog() throws SQLException {
/*  595 */     String result = null;
/*  596 */     checkClosed();
/*      */     try {
/*  598 */       result = this.connection.getCatalog();
/*  599 */     } catch (SQLException e) {
/*  600 */       throw markPossiblyBroken(e);
/*      */     } 
/*  602 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getHoldability() throws SQLException {
/*  607 */     int result = 0;
/*  608 */     checkClosed();
/*      */     try {
/*  610 */       result = this.connection.getHoldability();
/*  611 */     } catch (SQLException e) {
/*  612 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  615 */     return result;
/*      */   }
/*      */   
/*      */   public DatabaseMetaData getMetaData() throws SQLException {
/*  619 */     DatabaseMetaData result = null;
/*  620 */     checkClosed();
/*      */     try {
/*  622 */       result = this.connection.getMetaData();
/*  623 */     } catch (SQLException e) {
/*  624 */       throw markPossiblyBroken(e);
/*      */     } 
/*  626 */     return result;
/*      */   }
/*      */   
/*      */   public int getTransactionIsolation() throws SQLException {
/*  630 */     int result = 0;
/*  631 */     checkClosed();
/*      */     try {
/*  633 */       result = this.connection.getTransactionIsolation();
/*  634 */     } catch (SQLException e) {
/*  635 */       throw markPossiblyBroken(e);
/*      */     } 
/*  637 */     return result;
/*      */   }
/*      */   
/*      */   public Map<String, Class<?>> getTypeMap() throws SQLException {
/*  641 */     Map<String, Class<?>> result = null;
/*  642 */     checkClosed();
/*      */     try {
/*  644 */       result = this.connection.getTypeMap();
/*  645 */     } catch (SQLException e) {
/*  646 */       throw markPossiblyBroken(e);
/*      */     } 
/*  648 */     return result;
/*      */   }
/*      */   
/*      */   public SQLWarning getWarnings() throws SQLException {
/*  652 */     SQLWarning result = null;
/*  653 */     checkClosed();
/*      */     try {
/*  655 */       result = this.connection.getWarnings();
/*  656 */     } catch (SQLException e) {
/*  657 */       throw markPossiblyBroken(e);
/*      */     } 
/*  659 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isClosed() {
/*  668 */     return this.logicallyClosed;
/*      */   }
/*      */   
/*      */   public boolean isReadOnly() throws SQLException {
/*  672 */     boolean result = false;
/*  673 */     checkClosed();
/*      */     try {
/*  675 */       result = this.connection.isReadOnly();
/*  676 */     } catch (SQLException e) {
/*  677 */       throw markPossiblyBroken(e);
/*      */     } 
/*  679 */     return result;
/*      */   }
/*      */   
/*      */   public String nativeSQL(String sql) throws SQLException {
/*  683 */     String result = null;
/*  684 */     checkClosed();
/*      */     try {
/*  686 */       result = this.connection.nativeSQL(sql);
/*  687 */     } catch (SQLException e) {
/*  688 */       throw markPossiblyBroken(e);
/*      */     } 
/*  690 */     return result;
/*      */   }
/*      */   
/*      */   public CallableStatement prepareCall(String sql) throws SQLException {
/*  694 */     StatementHandle result = null;
/*  695 */     String cacheKey = null;
/*      */     
/*  697 */     checkClosed();
/*      */     
/*      */     try {
/*  700 */       long statStart = 0L;
/*  701 */       if (this.statisticsEnabled) {
/*  702 */         statStart = System.nanoTime();
/*      */       }
/*  704 */       if (this.statementCachingEnabled) {
/*  705 */         cacheKey = sql;
/*  706 */         result = this.callableStatementCache.get(cacheKey);
/*      */       } 
/*      */       
/*  709 */       if (result == null) {
/*  710 */         result = new CallableStatementHandle(this.connection.prepareCall(sql), sql, this, cacheKey, this.callableStatementCache);
/*      */         
/*  712 */         result.setLogicallyOpen();
/*      */       } 
/*      */       
/*  715 */       if (this.pool.closeConnectionWatch && this.statementCachingEnabled) {
/*  716 */         result.setOpenStackTrace(this.pool.captureStackTrace("Stack trace of location where statement was opened follows:\n%s"));
/*      */       }
/*  718 */       if (this.statisticsEnabled) {
/*  719 */         this.statistics.addStatementPrepareTime(System.nanoTime() - statStart);
/*  720 */         this.statistics.incrementStatementsPrepared();
/*      */       } 
/*  722 */     } catch (SQLException e) {
/*  723 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  726 */     return (CallableStatement)result;
/*      */   }
/*      */   
/*      */   public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
/*  730 */     StatementHandle result = null;
/*  731 */     String cacheKey = null;
/*      */     
/*  733 */     checkClosed();
/*      */     
/*      */     try {
/*  736 */       long statStart = 0L;
/*  737 */       if (this.statisticsEnabled) {
/*  738 */         statStart = System.nanoTime();
/*      */       }
/*  740 */       if (this.statementCachingEnabled) {
/*  741 */         cacheKey = this.callableStatementCache.calculateCacheKey(sql, resultSetType, resultSetConcurrency);
/*  742 */         result = this.callableStatementCache.get(cacheKey);
/*      */       } 
/*      */       
/*  745 */       if (result == null) {
/*  746 */         result = new CallableStatementHandle(this.connection.prepareCall(sql, resultSetType, resultSetConcurrency), sql, this, cacheKey, this.callableStatementCache);
/*      */         
/*  748 */         result.setLogicallyOpen();
/*      */       } 
/*      */       
/*  751 */       if (this.pool.closeConnectionWatch && this.statementCachingEnabled) {
/*  752 */         result.setOpenStackTrace(this.pool.captureStackTrace("Stack trace of location where statement was opened follows:\n%s"));
/*      */       }
/*  754 */       if (this.statisticsEnabled) {
/*  755 */         this.statistics.addStatementPrepareTime(System.nanoTime() - statStart);
/*  756 */         this.statistics.incrementStatementsPrepared();
/*      */       } 
/*  758 */     } catch (SQLException e) {
/*  759 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  762 */     return (CallableStatement)result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
/*  768 */     StatementHandle result = null;
/*  769 */     String cacheKey = null;
/*      */     
/*  771 */     checkClosed();
/*      */     
/*      */     try {
/*  774 */       long statStart = 0L;
/*  775 */       if (this.statisticsEnabled) {
/*  776 */         statStart = System.nanoTime();
/*      */       }
/*  778 */       if (this.statementCachingEnabled) {
/*  779 */         cacheKey = this.callableStatementCache.calculateCacheKey(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
/*  780 */         result = this.callableStatementCache.get(cacheKey);
/*      */       } 
/*      */       
/*  783 */       if (result == null) {
/*  784 */         result = new CallableStatementHandle(this.connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability), sql, this, cacheKey, this.callableStatementCache);
/*      */         
/*  786 */         result.setLogicallyOpen();
/*      */       } 
/*      */       
/*  789 */       if (this.pool.closeConnectionWatch && this.statementCachingEnabled) {
/*  790 */         result.setOpenStackTrace(this.pool.captureStackTrace("Stack trace of location where statement was opened follows:\n%s"));
/*      */       }
/*  792 */       if (this.statisticsEnabled) {
/*  793 */         this.statistics.addStatementPrepareTime(System.nanoTime() - statStart);
/*  794 */         this.statistics.incrementStatementsPrepared();
/*      */       } 
/*  796 */     } catch (SQLException e) {
/*  797 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  800 */     return (CallableStatement)result;
/*      */   }
/*      */   
/*      */   public PreparedStatement prepareStatement(String sql) throws SQLException {
/*  804 */     StatementHandle result = null;
/*  805 */     String cacheKey = null;
/*      */     
/*  807 */     checkClosed();
/*      */     
/*      */     try {
/*  810 */       long statStart = 0L;
/*  811 */       if (this.statisticsEnabled) {
/*  812 */         statStart = System.nanoTime();
/*      */       }
/*  814 */       if (this.statementCachingEnabled) {
/*  815 */         cacheKey = sql;
/*  816 */         result = this.preparedStatementCache.get(cacheKey);
/*      */       } 
/*      */       
/*  819 */       if (result == null) {
/*  820 */         result = new PreparedStatementHandle(this.connection.prepareStatement(sql), sql, this, cacheKey, this.preparedStatementCache);
/*  821 */         result.setLogicallyOpen();
/*      */       } 
/*      */ 
/*      */       
/*  825 */       if (this.pool.closeConnectionWatch && this.statementCachingEnabled) {
/*  826 */         result.setOpenStackTrace(this.pool.captureStackTrace("Stack trace of location where statement was opened follows:\n%s"));
/*      */       }
/*      */       
/*  829 */       if (this.statisticsEnabled) {
/*  830 */         this.statistics.addStatementPrepareTime(System.nanoTime() - statStart);
/*  831 */         this.statistics.incrementStatementsPrepared();
/*      */       }
/*      */     
/*  834 */     } catch (SQLException e) {
/*  835 */       throw markPossiblyBroken(e);
/*      */     } 
/*  837 */     return (PreparedStatement)result;
/*      */   }
/*      */ 
/*      */   
/*      */   public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
/*  842 */     StatementHandle result = null;
/*  843 */     String cacheKey = null;
/*      */     
/*  845 */     checkClosed();
/*      */     
/*      */     try {
/*  848 */       long statStart = 0L;
/*  849 */       if (this.statisticsEnabled) {
/*  850 */         statStart = System.nanoTime();
/*      */       }
/*  852 */       if (this.statementCachingEnabled) {
/*  853 */         cacheKey = this.preparedStatementCache.calculateCacheKey(sql, autoGeneratedKeys);
/*  854 */         result = this.preparedStatementCache.get(cacheKey);
/*      */       } 
/*      */       
/*  857 */       if (result == null) {
/*  858 */         result = new PreparedStatementHandle(this.connection.prepareStatement(sql, autoGeneratedKeys), sql, this, cacheKey, this.preparedStatementCache);
/*  859 */         result.setLogicallyOpen();
/*      */       } 
/*      */       
/*  862 */       if (this.pool.closeConnectionWatch && this.statementCachingEnabled) {
/*  863 */         result.setOpenStackTrace(this.pool.captureStackTrace("Stack trace of location where statement was opened follows:\n%s"));
/*      */       }
/*      */       
/*  866 */       if (this.statisticsEnabled) {
/*  867 */         this.statistics.addStatementPrepareTime(System.nanoTime() - statStart);
/*  868 */         this.statistics.incrementStatementsPrepared();
/*      */       }
/*      */     
/*  871 */     } catch (SQLException e) {
/*  872 */       throw markPossiblyBroken(e);
/*      */     } 
/*  874 */     return (PreparedStatement)result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
/*  880 */     StatementHandle result = null;
/*  881 */     String cacheKey = null;
/*      */     
/*  883 */     checkClosed();
/*      */     
/*      */     try {
/*  886 */       long statStart = 0L;
/*  887 */       if (this.statisticsEnabled) {
/*  888 */         statStart = System.nanoTime();
/*      */       }
/*      */       
/*  891 */       if (this.statementCachingEnabled) {
/*  892 */         cacheKey = this.preparedStatementCache.calculateCacheKey(sql, columnIndexes);
/*  893 */         result = this.preparedStatementCache.get(cacheKey);
/*      */       } 
/*      */       
/*  896 */       if (result == null) {
/*  897 */         result = new PreparedStatementHandle(this.connection.prepareStatement(sql, columnIndexes), sql, this, cacheKey, this.preparedStatementCache);
/*      */         
/*  899 */         result.setLogicallyOpen();
/*      */       } 
/*      */       
/*  902 */       if (this.pool.closeConnectionWatch && this.statementCachingEnabled) {
/*  903 */         result.setOpenStackTrace(this.pool.captureStackTrace("Stack trace of location where statement was opened follows:\n%s"));
/*      */       }
/*      */       
/*  906 */       if (this.statisticsEnabled) {
/*  907 */         this.statistics.addStatementPrepareTime(System.nanoTime() - statStart);
/*  908 */         this.statistics.incrementStatementsPrepared();
/*      */       }
/*      */     
/*  911 */     } catch (SQLException e) {
/*  912 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  915 */     return (PreparedStatement)result;
/*      */   }
/*      */ 
/*      */   
/*      */   public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
/*  920 */     StatementHandle result = null;
/*  921 */     String cacheKey = null;
/*      */     
/*  923 */     checkClosed();
/*      */     
/*      */     try {
/*  926 */       long statStart = 0L;
/*  927 */       if (this.statisticsEnabled) {
/*  928 */         statStart = System.nanoTime();
/*      */       }
/*  930 */       if (this.statementCachingEnabled) {
/*  931 */         cacheKey = this.preparedStatementCache.calculateCacheKey(sql, columnNames);
/*  932 */         result = this.preparedStatementCache.get(cacheKey);
/*      */       } 
/*      */       
/*  935 */       if (result == null) {
/*  936 */         result = new PreparedStatementHandle(this.connection.prepareStatement(sql, columnNames), sql, this, cacheKey, this.preparedStatementCache);
/*      */         
/*  938 */         result.setLogicallyOpen();
/*      */       } 
/*      */       
/*  941 */       if (this.pool.closeConnectionWatch && this.statementCachingEnabled) {
/*  942 */         result.setOpenStackTrace(this.pool.captureStackTrace("Stack trace of location where statement was opened follows:\n%s"));
/*      */       }
/*      */       
/*  945 */       if (this.statisticsEnabled) {
/*  946 */         this.statistics.addStatementPrepareTime(System.nanoTime() - statStart);
/*  947 */         this.statistics.incrementStatementsPrepared();
/*      */       } 
/*  949 */     } catch (SQLException e) {
/*  950 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  953 */     return (PreparedStatement)result;
/*      */   }
/*      */ 
/*      */   
/*      */   public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
/*  958 */     StatementHandle result = null;
/*  959 */     String cacheKey = null;
/*      */     
/*  961 */     checkClosed();
/*      */     
/*      */     try {
/*  964 */       long statStart = 0L;
/*  965 */       if (this.statisticsEnabled) {
/*  966 */         statStart = System.nanoTime();
/*      */       }
/*  968 */       if (this.statementCachingEnabled) {
/*  969 */         cacheKey = this.preparedStatementCache.calculateCacheKey(sql, resultSetType, resultSetConcurrency);
/*  970 */         result = this.preparedStatementCache.get(cacheKey);
/*      */       } 
/*      */       
/*  973 */       if (result == null) {
/*  974 */         result = new PreparedStatementHandle(this.connection.prepareStatement(sql, resultSetType, resultSetConcurrency), sql, this, cacheKey, this.preparedStatementCache);
/*      */         
/*  976 */         result.setLogicallyOpen();
/*      */       } 
/*      */       
/*  979 */       if (this.pool.closeConnectionWatch && this.statementCachingEnabled) {
/*  980 */         result.setOpenStackTrace(this.pool.captureStackTrace("Stack trace of location where statement was opened follows:\n%s"));
/*      */       }
/*  982 */       if (this.statisticsEnabled) {
/*  983 */         this.statistics.addStatementPrepareTime(System.nanoTime() - statStart);
/*  984 */         this.statistics.incrementStatementsPrepared();
/*      */       } 
/*  986 */     } catch (SQLException e) {
/*  987 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  990 */     return (PreparedStatement)result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
/*  996 */     StatementHandle result = null;
/*  997 */     String cacheKey = null;
/*      */     
/*  999 */     checkClosed();
/*      */     
/*      */     try {
/* 1002 */       long statStart = 0L;
/* 1003 */       if (this.statisticsEnabled) {
/* 1004 */         statStart = System.nanoTime();
/*      */       }
/*      */       
/* 1007 */       if (this.statementCachingEnabled) {
/* 1008 */         cacheKey = this.preparedStatementCache.calculateCacheKey(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
/* 1009 */         result = this.preparedStatementCache.get(cacheKey);
/*      */       } 
/*      */       
/* 1012 */       if (result == null) {
/* 1013 */         result = new PreparedStatementHandle(this.connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability), sql, this, cacheKey, this.preparedStatementCache);
/*      */         
/* 1015 */         result.setLogicallyOpen();
/*      */       } 
/*      */       
/* 1018 */       if (this.pool.closeConnectionWatch && this.statementCachingEnabled) {
/* 1019 */         result.setOpenStackTrace(this.pool.captureStackTrace("Stack trace of location where statement was opened follows:\n%s"));
/*      */       }
/* 1021 */       if (this.statisticsEnabled) {
/* 1022 */         this.statistics.addStatementPrepareTime(System.nanoTime() - statStart);
/* 1023 */         this.statistics.incrementStatementsPrepared();
/*      */       } 
/* 1025 */     } catch (SQLException e) {
/* 1026 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */     
/* 1029 */     return (PreparedStatement)result;
/*      */   }
/*      */   
/*      */   public void releaseSavepoint(Savepoint savepoint) throws SQLException {
/* 1033 */     checkClosed();
/*      */     try {
/* 1035 */       this.connection.releaseSavepoint(savepoint);
/* 1036 */     } catch (SQLException e) {
/* 1037 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void rollback() throws SQLException {
/* 1043 */     checkClosed();
/*      */     try {
/* 1045 */       this.connection.rollback();
/* 1046 */     } catch (SQLException e) {
/* 1047 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void rollback(Savepoint savepoint) throws SQLException {
/* 1052 */     checkClosed();
/*      */     try {
/* 1054 */       this.connection.rollback(savepoint);
/* 1055 */     } catch (SQLException e) {
/* 1056 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setAutoCommit(boolean autoCommit) throws SQLException {
/* 1061 */     checkClosed();
/*      */     try {
/* 1063 */       this.connection.setAutoCommit(autoCommit);
/* 1064 */     } catch (SQLException e) {
/* 1065 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setCatalog(String catalog) throws SQLException {
/* 1070 */     checkClosed();
/*      */     try {
/* 1072 */       this.connection.setCatalog(catalog);
/* 1073 */     } catch (SQLException e) {
/* 1074 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setHoldability(int holdability) throws SQLException {
/* 1080 */     checkClosed();
/*      */     try {
/* 1082 */       this.connection.setHoldability(holdability);
/* 1083 */     } catch (SQLException e) {
/* 1084 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setReadOnly(boolean readOnly) throws SQLException {
/* 1089 */     checkClosed();
/*      */     try {
/* 1091 */       this.connection.setReadOnly(readOnly);
/* 1092 */     } catch (SQLException e) {
/* 1093 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public Savepoint setSavepoint() throws SQLException {
/* 1098 */     checkClosed();
/* 1099 */     Savepoint result = null;
/*      */     try {
/* 1101 */       result = this.connection.setSavepoint();
/* 1102 */     } catch (SQLException e) {
/* 1103 */       throw markPossiblyBroken(e);
/*      */     } 
/* 1105 */     return result;
/*      */   }
/*      */   
/*      */   public Savepoint setSavepoint(String name) throws SQLException {
/* 1109 */     checkClosed();
/* 1110 */     Savepoint result = null;
/*      */     try {
/* 1112 */       result = this.connection.setSavepoint(name);
/* 1113 */     } catch (SQLException e) {
/* 1114 */       throw markPossiblyBroken(e);
/*      */     } 
/* 1116 */     return result;
/*      */   }
/*      */   
/*      */   public void setTransactionIsolation(int level) throws SQLException {
/* 1120 */     checkClosed();
/*      */     try {
/* 1122 */       this.connection.setTransactionIsolation(level);
/* 1123 */     } catch (SQLException e) {
/* 1124 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
/* 1129 */     checkClosed();
/*      */     try {
/* 1131 */       this.connection.setTypeMap(map);
/* 1132 */     } catch (SQLException e) {
/* 1133 */       throw markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getConnectionLastUsedInMs() {
/* 1141 */     return this.connectionLastUsedInMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long getConnectionLastUsed() {
/* 1151 */     return getConnectionLastUsedInMs();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setConnectionLastUsedInMs(long connectionLastUsed) {
/* 1159 */     this.connectionLastUsedInMs = connectionLastUsed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getConnectionLastResetInMs() {
/* 1166 */     return this.connectionLastResetInMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long getConnectionLastReset() {
/* 1175 */     return getConnectionLastResetInMs();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setConnectionLastResetInMs(long connectionLastReset) {
/* 1184 */     this.connectionLastResetInMs = connectionLastReset;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPossiblyBroken() {
/* 1193 */     return this.possiblyBroken;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConnectionPartition getOriginatingPartition() {
/* 1203 */     return this.originatingPartition;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setOriginatingPartition(ConnectionPartition originatingPartition) {
/* 1213 */     this.originatingPartition = originatingPartition;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void renewConnection() {
/* 1221 */     this.logicallyClosed = false;
/* 1222 */     this.threadUsingConnection = Thread.currentThread();
/* 1223 */     if (this.doubleCloseCheck) {
/* 1224 */       this.doubleCloseException = null;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void clearStatementCaches(boolean internalClose) {
/* 1234 */     if (this.statementCachingEnabled)
/*      */     {
/* 1236 */       if (internalClose) {
/* 1237 */         this.callableStatementCache.clear();
/* 1238 */         this.preparedStatementCache.clear();
/*      */       }
/* 1240 */       else if (this.pool.closeConnectionWatch) {
/* 1241 */         this.callableStatementCache.checkForProperClosure();
/* 1242 */         this.preparedStatementCache.checkForProperClosure();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getDebugHandle() {
/* 1252 */     return this.debugHandle;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDebugHandle(Object debugHandle) {
/* 1260 */     this.debugHandle = debugHandle;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Connection getRawConnection() {
/* 1269 */     return getInternalConnection();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Connection getInternalConnection() {
/* 1276 */     return this.connection;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConnectionHook getConnectionHook() {
/* 1283 */     return this.connectionHook;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLogStatementsEnabled() {
/* 1290 */     return this.logStatementsEnabled;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLogStatementsEnabled(boolean logStatementsEnabled) {
/* 1297 */     this.logStatementsEnabled = logStatementsEnabled;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isInReplayMode() {
/* 1304 */     return this.inReplayMode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setInReplayMode(boolean inReplayMode) {
/* 1311 */     this.inReplayMode = inReplayMode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isConnectionAlive() {
/* 1318 */     return this.pool.isConnectionHandleAlive(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInternalConnection(Connection rawConnection) {
/* 1326 */     this.connection = rawConnection;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BoneCP getPool() {
/* 1333 */     return this.pool;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ReplayLog> getReplayLog() {
/* 1340 */     return this.replayLog;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setReplayLog(List<ReplayLog> replayLog) {
/* 1347 */     this.replayLog = replayLog;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getProxyTarget() {
/*      */     try {
/* 1355 */       return Proxy.getInvocationHandler(this.connection).invoke(null, getClass().getMethod("getProxyTarget", new Class[0]), null);
/* 1356 */     } catch (Throwable t) {
/* 1357 */       throw new RuntimeException("BoneCP: Internal error - transaction replay log is not turned on?", t);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Thread getThreadUsingConnection() {
/* 1365 */     return this.threadUsingConnection;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long getConnectionCreationTime() {
/* 1375 */     return getConnectionCreationTimeInMs();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getConnectionCreationTimeInMs() {
/* 1383 */     return this.connectionCreationTimeInMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isExpired() {
/* 1390 */     return isExpired(System.currentTimeMillis());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isExpired(long currentTime) {
/* 1398 */     return (this.maxConnectionAgeInMs > 0L && currentTime - this.connectionCreationTimeInMs > this.maxConnectionAgeInMs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setThreadWatch(Thread threadWatch) {
/* 1406 */     this.threadWatch = threadWatch;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Thread getThreadWatch() {
/* 1414 */     return this.threadWatch;
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/ConnectionHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */