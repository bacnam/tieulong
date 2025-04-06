/*      */ package com.jolbox.bonecp;
/*      */ 
/*      */ import com.jolbox.bonecp.hooks.ConnectionHook;
/*      */ import java.sql.Connection;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.Statement;
/*      */ import java.util.Map;
/*      */ import java.util.TreeMap;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import jsr166y.LinkedTransferQueue;
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
/*      */ public class StatementHandle
/*      */   implements Statement
/*      */ {
/*   43 */   protected AtomicBoolean logicallyClosed = new AtomicBoolean();
/*      */   
/*      */   protected Statement internalStatement;
/*      */   
/*      */   protected String sql;
/*      */   
/*      */   protected IStatementCache cache;
/*      */   
/*      */   protected ConnectionHandle connectionHandle;
/*      */   
/*      */   private String cacheKey;
/*      */   
/*      */   protected boolean logStatementsEnabled;
/*      */   
/*      */   public volatile boolean inCache = false;
/*      */   
/*      */   public String openStackTrace;
/*      */   
/*   61 */   protected static Logger logger = LoggerFactory.getLogger(StatementHandle.class);
/*      */ 
/*      */   
/*      */   protected long queryExecuteTimeLimit;
/*      */   
/*      */   protected ConnectionHook connectionHook;
/*      */   
/*      */   private volatile boolean statementReleaseHelperEnabled;
/*      */   
/*      */   private LinkedTransferQueue<StatementHandle> statementsPendingRelease;
/*      */   
/*      */   private Object debugHandle;
/*      */   
/*      */   private boolean statisticsEnabled;
/*      */   
/*      */   private Statistics statistics;
/*      */   
/*      */   protected volatile boolean enqueuedForClosure;
/*      */   
/*   80 */   protected Map<Object, Object> logParams = new TreeMap<Object, Object>();
/*      */ 
/*      */   
/*   83 */   protected StringBuilder batchSQL = new StringBuilder();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StatementHandle(Statement internalStatement, String sql, IStatementCache cache, ConnectionHandle connectionHandle, String cacheKey, boolean logStatementsEnabled) {
/*   99 */     this.sql = sql;
/*  100 */     this.internalStatement = internalStatement;
/*  101 */     this.cache = cache;
/*  102 */     this.cacheKey = cacheKey;
/*  103 */     this.connectionHandle = connectionHandle;
/*  104 */     this.logStatementsEnabled = logStatementsEnabled;
/*  105 */     BoneCPConfig config = connectionHandle.getPool().getConfig();
/*  106 */     this.connectionHook = config.getConnectionHook();
/*  107 */     this.statistics = connectionHandle.getPool().getStatistics();
/*  108 */     this.statisticsEnabled = config.isStatisticsEnabled();
/*      */     
/*  110 */     this.statementReleaseHelperEnabled = connectionHandle.getPool().isStatementReleaseHelperThreadsConfigured();
/*  111 */     if (this.statementReleaseHelperEnabled) {
/*  112 */       this.statementsPendingRelease = connectionHandle.getPool().getStatementsPendingRelease();
/*      */     }
/*      */     
/*      */     try {
/*  116 */       this.queryExecuteTimeLimit = connectionHandle.getOriginatingPartition().getQueryExecuteTimeLimitinNanoSeconds();
/*  117 */     } catch (Exception e) {
/*      */       
/*  119 */       this.queryExecuteTimeLimit = 0L;
/*      */     } 
/*      */     
/*  122 */     if (this.cache != null) {
/*  123 */       this.cache.putIfAbsent(this.cacheKey, this);
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
/*      */   public StatementHandle(Statement internalStatement, ConnectionHandle connectionHandle, boolean logStatementsEnabled) {
/*  136 */     this(internalStatement, null, null, connectionHandle, null, logStatementsEnabled);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void closeStatement() throws SQLException {
/*  145 */     this.logicallyClosed.set(true);
/*  146 */     if (this.logStatementsEnabled) {
/*  147 */       this.logParams.clear();
/*  148 */       this.batchSQL = new StringBuilder();
/*      */     } 
/*  150 */     if (this.cache == null || !this.inCache) {
/*  151 */       this.internalStatement.close();
/*      */     }
/*  153 */     this.enqueuedForClosure = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean tryTransferOffer(StatementHandle e) {
/*  163 */     boolean result = true;
/*      */ 
/*      */ 
/*      */     
/*  167 */     if (!this.statementsPendingRelease.tryTransfer(e)) {
/*  168 */       result = this.statementsPendingRelease.offer(e);
/*      */     }
/*  170 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void close() throws SQLException {
/*  175 */     if (this.statementReleaseHelperEnabled) {
/*  176 */       this.enqueuedForClosure = true;
/*      */       
/*  178 */       if (!tryTransferOffer(this)) {
/*  179 */         this.enqueuedForClosure = false;
/*      */         
/*  181 */         closeStatement();
/*      */       } 
/*      */     } else {
/*      */       
/*  185 */       closeStatement();
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
/*      */   public void addBatch(String sql) throws SQLException {
/*  198 */     checkClosed();
/*      */     try {
/*  200 */       if (this.logStatementsEnabled) {
/*  201 */         this.batchSQL.append(sql);
/*      */       }
/*      */       
/*  204 */       this.internalStatement.addBatch(sql);
/*  205 */     } catch (SQLException e) {
/*  206 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   protected void checkClosed() throws SQLException {
/*  219 */     if (this.logicallyClosed.get()) {
/*  220 */       throw new SQLException("Statement is closed");
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
/*      */   public void cancel() throws SQLException {
/*  234 */     checkClosed();
/*      */     try {
/*  236 */       this.internalStatement.cancel();
/*  237 */     } catch (SQLException e) {
/*  238 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void clearBatch() throws SQLException {
/*  251 */     checkClosed();
/*      */     try {
/*  253 */       if (this.logStatementsEnabled) {
/*  254 */         this.batchSQL = new StringBuilder();
/*      */       }
/*  256 */       this.internalStatement.clearBatch();
/*  257 */     } catch (SQLException e) {
/*  258 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void clearWarnings() throws SQLException {
/*  272 */     checkClosed();
/*      */     try {
/*  274 */       this.internalStatement.clearWarnings();
/*  275 */     } catch (SQLException e) {
/*  276 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public boolean execute(String sql) throws SQLException {
/*  290 */     boolean result = false;
/*  291 */     checkClosed();
/*      */     try {
/*  293 */       if (this.logStatementsEnabled && logger.isDebugEnabled()) {
/*  294 */         logger.debug(PoolUtil.fillLogParams(sql, this.logParams));
/*      */       }
/*  296 */       long timer = queryTimerStart();
/*  297 */       if (this.connectionHook != null) {
/*  298 */         this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, sql, this.logParams);
/*      */       }
/*  300 */       result = this.internalStatement.execute(sql);
/*  301 */       if (this.connectionHook != null) {
/*  302 */         this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, sql, this.logParams);
/*      */       }
/*  304 */       queryTimerEnd(sql, timer);
/*      */     }
/*  306 */     catch (SQLException e) {
/*  307 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  310 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void queryTimerEnd(String sql, long queryStartTime) {
/*  321 */     if (this.queryExecuteTimeLimit != 0L && this.connectionHook != null) {
/*      */       
/*  323 */       long timeElapsed = System.nanoTime() - queryStartTime;
/*      */       
/*  325 */       if (timeElapsed > this.queryExecuteTimeLimit) {
/*  326 */         this.connectionHook.onQueryExecuteTimeLimitExceeded(this.connectionHandle, this, sql, this.logParams, timeElapsed);
/*      */       }
/*      */     } 
/*      */     
/*  330 */     if (this.statisticsEnabled) {
/*  331 */       this.statistics.incrementStatementsExecuted();
/*  332 */       this.statistics.addStatementExecuteTime(System.nanoTime() - queryStartTime);
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
/*      */   public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
/*  347 */     boolean result = false;
/*  348 */     checkClosed();
/*      */     try {
/*  350 */       if (this.logStatementsEnabled && logger.isDebugEnabled()) {
/*  351 */         logger.debug(PoolUtil.fillLogParams(sql, this.logParams));
/*      */       }
/*      */       
/*  354 */       long queryStartTime = queryTimerStart();
/*  355 */       if (this.connectionHook != null) {
/*  356 */         this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, sql, this.logParams);
/*      */       }
/*  358 */       result = this.internalStatement.execute(sql, autoGeneratedKeys);
/*      */       
/*  360 */       if (this.connectionHook != null) {
/*  361 */         this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, sql, this.logParams);
/*      */       }
/*      */       
/*  364 */       queryTimerEnd(sql, queryStartTime);
/*  365 */     } catch (SQLException e) {
/*  366 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  369 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected long queryTimerStart() {
/*  378 */     return (this.statisticsEnabled || (this.queryExecuteTimeLimit != 0L && this.connectionHook != null)) ? System.nanoTime() : Long.MAX_VALUE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean execute(String sql, int[] columnIndexes) throws SQLException {
/*  389 */     boolean result = false;
/*  390 */     checkClosed();
/*      */     try {
/*  392 */       if (this.logStatementsEnabled && logger.isDebugEnabled()) {
/*  393 */         logger.debug(PoolUtil.fillLogParams(sql, this.logParams));
/*      */       }
/*      */       
/*  396 */       long queryStartTime = queryTimerStart();
/*  397 */       if (this.connectionHook != null) {
/*  398 */         this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, sql, this.logParams);
/*      */       }
/*      */       
/*  401 */       result = this.internalStatement.execute(sql, columnIndexes);
/*      */       
/*  403 */       if (this.connectionHook != null)
/*      */       {
/*      */         
/*  406 */         this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, sql, this.logParams);
/*      */       }
/*  408 */       queryTimerEnd(sql, queryStartTime);
/*      */     }
/*  410 */     catch (SQLException e) {
/*  411 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  414 */     return result;
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
/*      */   public boolean execute(String sql, String[] columnNames) throws SQLException {
/*  426 */     boolean result = false;
/*  427 */     checkClosed();
/*      */     try {
/*  429 */       if (this.logStatementsEnabled && logger.isDebugEnabled()) {
/*  430 */         logger.debug(PoolUtil.fillLogParams(sql, this.logParams));
/*      */       }
/*  432 */       long queryStartTime = queryTimerStart();
/*  433 */       if (this.connectionHook != null) {
/*  434 */         this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, sql, this.logParams);
/*      */       }
/*  436 */       result = this.internalStatement.execute(sql, columnNames);
/*  437 */       if (this.connectionHook != null) {
/*  438 */         this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, sql, this.logParams);
/*      */       }
/*      */       
/*  441 */       queryTimerEnd(sql, queryStartTime);
/*  442 */     } catch (SQLException e) {
/*  443 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  446 */     return result;
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
/*      */   public int[] executeBatch() throws SQLException {
/*  458 */     int[] result = null;
/*  459 */     checkClosed();
/*      */     try {
/*  461 */       if (this.logStatementsEnabled && logger.isDebugEnabled()) {
/*  462 */         logger.debug(PoolUtil.fillLogParams(this.batchSQL.toString(), this.logParams));
/*      */       }
/*  464 */       long queryStartTime = queryTimerStart();
/*  465 */       String query = this.logStatementsEnabled ? this.batchSQL.toString() : "";
/*  466 */       if (this.connectionHook != null) {
/*  467 */         this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, query, this.logParams);
/*      */       }
/*  469 */       result = this.internalStatement.executeBatch();
/*      */       
/*  471 */       if (this.connectionHook != null) {
/*  472 */         this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, query, this.logParams);
/*      */       }
/*      */       
/*  475 */       queryTimerEnd(this.logStatementsEnabled ? this.batchSQL.toString() : "", queryStartTime);
/*      */     }
/*  477 */     catch (SQLException e) {
/*  478 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  481 */     return result;
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
/*      */   public ResultSet executeQuery(String sql) throws SQLException {
/*  493 */     ResultSet result = null;
/*  494 */     checkClosed();
/*      */     try {
/*  496 */       if (this.logStatementsEnabled && logger.isDebugEnabled()) {
/*  497 */         logger.debug(PoolUtil.fillLogParams(sql, this.logParams));
/*      */       }
/*  499 */       long queryStartTime = queryTimerStart();
/*  500 */       if (this.connectionHook != null) {
/*  501 */         this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, sql, this.logParams);
/*      */       }
/*  503 */       result = this.internalStatement.executeQuery(sql);
/*  504 */       if (this.connectionHook != null) {
/*  505 */         this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, sql, this.logParams);
/*      */       }
/*      */       
/*  508 */       queryTimerEnd(sql, queryStartTime);
/*  509 */     } catch (SQLException e) {
/*  510 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  513 */     return result;
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
/*      */   public int executeUpdate(String sql) throws SQLException {
/*  526 */     int result = 0;
/*  527 */     checkClosed();
/*      */     try {
/*  529 */       if (this.logStatementsEnabled && logger.isDebugEnabled()) {
/*  530 */         logger.debug(PoolUtil.fillLogParams(sql, this.logParams));
/*      */       }
/*  532 */       long queryStartTime = queryTimerStart();
/*  533 */       if (this.connectionHook != null) {
/*  534 */         this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, sql, this.logParams);
/*      */       }
/*  536 */       result = this.internalStatement.executeUpdate(sql);
/*  537 */       if (this.connectionHook != null) {
/*  538 */         this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, sql, this.logParams);
/*      */       }
/*      */       
/*  541 */       queryTimerEnd(sql, queryStartTime);
/*  542 */     } catch (SQLException e) {
/*  543 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  546 */     return result;
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
/*      */   public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
/*  559 */     int result = 0;
/*  560 */     checkClosed();
/*      */     try {
/*  562 */       if (this.logStatementsEnabled && logger.isDebugEnabled()) {
/*  563 */         logger.debug(PoolUtil.fillLogParams(sql, this.logParams));
/*      */       }
/*  565 */       long queryStartTime = queryTimerStart();
/*  566 */       if (this.connectionHook != null) {
/*  567 */         this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, sql, this.logParams);
/*      */       }
/*  569 */       result = this.internalStatement.executeUpdate(sql, autoGeneratedKeys);
/*  570 */       if (this.connectionHook != null) {
/*  571 */         this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, sql, this.logParams);
/*      */       }
/*      */       
/*  574 */       queryTimerEnd(sql, queryStartTime);
/*  575 */     } catch (SQLException e) {
/*  576 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  579 */     return result;
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
/*      */   public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
/*  592 */     int result = 0;
/*  593 */     checkClosed();
/*      */     try {
/*  595 */       if (this.logStatementsEnabled && logger.isDebugEnabled()) {
/*  596 */         logger.debug(PoolUtil.fillLogParams(sql, this.logParams), columnIndexes);
/*      */       }
/*  598 */       long queryStartTime = queryTimerStart();
/*  599 */       if (this.connectionHook != null) {
/*  600 */         this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, sql, this.logParams);
/*      */       }
/*  602 */       result = this.internalStatement.executeUpdate(sql, columnIndexes);
/*  603 */       if (this.connectionHook != null) {
/*  604 */         this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, sql, this.logParams);
/*      */       }
/*      */       
/*  607 */       queryTimerEnd(sql, queryStartTime);
/*  608 */     } catch (SQLException e) {
/*  609 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  612 */     return result;
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
/*      */   public int executeUpdate(String sql, String[] columnNames) throws SQLException {
/*  625 */     int result = 0;
/*  626 */     checkClosed();
/*      */     try {
/*  628 */       if (this.logStatementsEnabled && logger.isDebugEnabled()) {
/*  629 */         logger.debug(PoolUtil.fillLogParams(sql, this.logParams), (Object[])columnNames);
/*      */       }
/*  631 */       long queryStartTime = queryTimerStart();
/*  632 */       if (this.connectionHook != null) {
/*  633 */         this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, sql, this.logParams);
/*      */       }
/*  635 */       result = this.internalStatement.executeUpdate(sql, columnNames);
/*  636 */       if (this.connectionHook != null) {
/*  637 */         this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, sql, this.logParams);
/*      */       }
/*      */       
/*  640 */       queryTimerEnd(sql, queryStartTime);
/*  641 */     } catch (SQLException e) {
/*  642 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  645 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Connection getConnection() throws SQLException {
/*  656 */     checkClosed();
/*  657 */     return this.connectionHandle;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getFetchDirection() throws SQLException {
/*  668 */     int result = 0;
/*  669 */     checkClosed();
/*      */     try {
/*  671 */       result = this.internalStatement.getFetchDirection();
/*  672 */     } catch (SQLException e) {
/*  673 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  676 */     return result;
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
/*      */   public int getFetchSize() throws SQLException {
/*  688 */     int result = 0;
/*  689 */     checkClosed();
/*      */     try {
/*  691 */       result = this.internalStatement.getFetchSize();
/*  692 */     } catch (SQLException e) {
/*  693 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  696 */     return result;
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
/*      */   public ResultSet getGeneratedKeys() throws SQLException {
/*  708 */     ResultSet result = null;
/*  709 */     checkClosed();
/*      */     try {
/*  711 */       result = this.internalStatement.getGeneratedKeys();
/*  712 */     } catch (SQLException e) {
/*  713 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  716 */     return result;
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
/*      */   public int getMaxFieldSize() throws SQLException {
/*  728 */     int result = 0;
/*  729 */     checkClosed();
/*      */     try {
/*  731 */       result = this.internalStatement.getMaxFieldSize();
/*  732 */     } catch (SQLException e) {
/*  733 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  736 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxRows() throws SQLException {
/*  747 */     int result = 0;
/*  748 */     checkClosed();
/*      */     try {
/*  750 */       result = this.internalStatement.getMaxRows();
/*  751 */     } catch (SQLException e) {
/*  752 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  755 */     return result;
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
/*      */   public boolean getMoreResults() throws SQLException {
/*  768 */     boolean result = false;
/*  769 */     checkClosed();
/*      */     try {
/*  771 */       result = this.internalStatement.getMoreResults();
/*  772 */     } catch (SQLException e) {
/*  773 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  776 */     return result;
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
/*      */   public boolean getMoreResults(int current) throws SQLException {
/*  788 */     boolean result = false;
/*  789 */     checkClosed();
/*      */     
/*      */     try {
/*  792 */       result = this.internalStatement.getMoreResults(current);
/*  793 */     } catch (SQLException e) {
/*  794 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  797 */     return result;
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
/*      */   public int getQueryTimeout() throws SQLException {
/*  809 */     int result = 0;
/*  810 */     checkClosed();
/*      */     try {
/*  812 */       result = this.internalStatement.getQueryTimeout();
/*  813 */     } catch (SQLException e) {
/*  814 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  817 */     return result;
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
/*      */   public ResultSet getResultSet() throws SQLException {
/*  829 */     ResultSet result = null;
/*  830 */     checkClosed();
/*      */     try {
/*  832 */       result = this.internalStatement.getResultSet();
/*  833 */     } catch (SQLException e) {
/*  834 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  837 */     return result;
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
/*      */   public int getResultSetConcurrency() throws SQLException {
/*  849 */     int result = 0;
/*  850 */     checkClosed();
/*      */     try {
/*  852 */       result = this.internalStatement.getResultSetConcurrency();
/*  853 */     } catch (SQLException e) {
/*  854 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  857 */     return result;
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
/*      */   public int getResultSetHoldability() throws SQLException {
/*  869 */     int result = 0;
/*  870 */     checkClosed();
/*      */     try {
/*  872 */       result = this.internalStatement.getResultSetHoldability();
/*  873 */     } catch (SQLException e) {
/*  874 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  877 */     return result;
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
/*      */   public int getResultSetType() throws SQLException {
/*  889 */     int result = 0;
/*  890 */     checkClosed();
/*      */     try {
/*  892 */       result = this.internalStatement.getResultSetType();
/*  893 */     } catch (SQLException e) {
/*  894 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  897 */     return result;
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
/*      */   public int getUpdateCount() throws SQLException {
/*  909 */     int result = 0;
/*  910 */     checkClosed();
/*      */     try {
/*  912 */       result = this.internalStatement.getUpdateCount();
/*  913 */     } catch (SQLException e) {
/*  914 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  917 */     return result;
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
/*      */   public SQLWarning getWarnings() throws SQLException {
/*  929 */     SQLWarning result = null;
/*  930 */     checkClosed();
/*      */     try {
/*  932 */       result = this.internalStatement.getWarnings();
/*  933 */     } catch (SQLException e) {
/*  934 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  937 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isClosed() {
/*  946 */     return this.logicallyClosed.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPoolable(boolean poolable) throws SQLException {
/*  953 */     checkClosed();
/*      */     try {
/*  955 */       this.internalStatement.setPoolable(poolable);
/*  956 */     } catch (SQLException e) {
/*  957 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWrapperFor(Class<?> iface) throws SQLException {
/*  966 */     boolean result = false;
/*      */     try {
/*  968 */       result = this.internalStatement.isWrapperFor(iface);
/*  969 */     } catch (SQLException e) {
/*  970 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  973 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T unwrap(Class<T> iface) throws SQLException {
/*  979 */     T result = null;
/*      */     
/*      */     try {
/*  982 */       result = this.internalStatement.unwrap(iface);
/*  983 */     } catch (SQLException e) {
/*  984 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/*  987 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPoolable() throws SQLException {
/*  994 */     boolean result = false;
/*  995 */     checkClosed();
/*      */     try {
/*  997 */       result = this.internalStatement.isPoolable();
/*  998 */     } catch (SQLException e) {
/*  999 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */     
/* 1002 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCursorName(String name) throws SQLException {
/* 1010 */     checkClosed();
/*      */     try {
/* 1012 */       this.internalStatement.setCursorName(name);
/* 1013 */     } catch (SQLException e) {
/* 1014 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setEscapeProcessing(boolean enable) throws SQLException {
/* 1028 */     checkClosed();
/*      */     try {
/* 1030 */       this.internalStatement.setEscapeProcessing(enable);
/* 1031 */     } catch (SQLException e) {
/* 1032 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setFetchDirection(int direction) throws SQLException {
/* 1046 */     checkClosed();
/*      */     try {
/* 1048 */       this.internalStatement.setFetchDirection(direction);
/* 1049 */     } catch (SQLException e) {
/* 1050 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setFetchSize(int rows) throws SQLException {
/* 1064 */     checkClosed();
/*      */     try {
/* 1066 */       this.internalStatement.setFetchSize(rows);
/* 1067 */     } catch (SQLException e) {
/* 1068 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setMaxFieldSize(int max) throws SQLException {
/* 1082 */     checkClosed();
/*      */     try {
/* 1084 */       this.internalStatement.setMaxFieldSize(max);
/* 1085 */     } catch (SQLException e) {
/* 1086 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setMaxRows(int max) throws SQLException {
/* 1100 */     checkClosed();
/*      */     try {
/* 1102 */       this.internalStatement.setMaxRows(max);
/* 1103 */     } catch (SQLException e) {
/* 1104 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setQueryTimeout(int seconds) throws SQLException {
/* 1119 */     checkClosed();
/*      */     try {
/* 1121 */       this.internalStatement.setQueryTimeout(seconds);
/* 1122 */     } catch (SQLException e) {
/* 1123 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void clearCache() {
/* 1133 */     if (this.cache != null) {
/* 1134 */       this.cache.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setLogicallyOpen() {
/* 1145 */     this.logicallyClosed.set(false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1151 */     return this.sql;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getOpenStackTrace() {
/* 1159 */     return this.openStackTrace;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOpenStackTrace(String openStackTrace) {
/* 1167 */     this.openStackTrace = openStackTrace;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Statement getInternalStatement() {
/* 1175 */     return this.internalStatement;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInternalStatement(Statement internalStatement) {
/* 1183 */     this.internalStatement = internalStatement;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDebugHandle(Object debugHandle) {
/* 1191 */     this.debugHandle = debugHandle;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getDebugHandle() {
/* 1200 */     return this.debugHandle;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnqueuedForClosure() {
/* 1208 */     return this.enqueuedForClosure;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isClosedOrEnqueuedForClosure() {
/* 1215 */     return (this.enqueuedForClosure || isClosed());
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/StatementHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */