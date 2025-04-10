package com.jolbox.bonecp;

import com.jolbox.bonecp.hooks.ConnectionHook;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import jsr166y.LinkedTransferQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatementHandle
implements Statement
{
protected AtomicBoolean logicallyClosed = new AtomicBoolean();

protected Statement internalStatement;

protected String sql;

protected IStatementCache cache;

protected ConnectionHandle connectionHandle;

private String cacheKey;

protected boolean logStatementsEnabled;

public volatile boolean inCache = false;

public String openStackTrace;

protected static Logger logger = LoggerFactory.getLogger(StatementHandle.class);

protected long queryExecuteTimeLimit;

protected ConnectionHook connectionHook;

private volatile boolean statementReleaseHelperEnabled;

private LinkedTransferQueue<StatementHandle> statementsPendingRelease;

private Object debugHandle;

private boolean statisticsEnabled;

private Statistics statistics;

protected volatile boolean enqueuedForClosure;

protected Map<Object, Object> logParams = new TreeMap<Object, Object>();

protected StringBuilder batchSQL = new StringBuilder();

public StatementHandle(Statement internalStatement, String sql, IStatementCache cache, ConnectionHandle connectionHandle, String cacheKey, boolean logStatementsEnabled) {
this.sql = sql;
this.internalStatement = internalStatement;
this.cache = cache;
this.cacheKey = cacheKey;
this.connectionHandle = connectionHandle;
this.logStatementsEnabled = logStatementsEnabled;
BoneCPConfig config = connectionHandle.getPool().getConfig();
this.connectionHook = config.getConnectionHook();
this.statistics = connectionHandle.getPool().getStatistics();
this.statisticsEnabled = config.isStatisticsEnabled();

this.statementReleaseHelperEnabled = connectionHandle.getPool().isStatementReleaseHelperThreadsConfigured();
if (this.statementReleaseHelperEnabled) {
this.statementsPendingRelease = connectionHandle.getPool().getStatementsPendingRelease();
}

try {
this.queryExecuteTimeLimit = connectionHandle.getOriginatingPartition().getQueryExecuteTimeLimitinNanoSeconds();
} catch (Exception e) {

this.queryExecuteTimeLimit = 0L;
} 

if (this.cache != null) {
this.cache.putIfAbsent(this.cacheKey, this);
}
}

public StatementHandle(Statement internalStatement, ConnectionHandle connectionHandle, boolean logStatementsEnabled) {
this(internalStatement, null, null, connectionHandle, null, logStatementsEnabled);
}

protected void closeStatement() throws SQLException {
this.logicallyClosed.set(true);
if (this.logStatementsEnabled) {
this.logParams.clear();
this.batchSQL = new StringBuilder();
} 
if (this.cache == null || !this.inCache) {
this.internalStatement.close();
}
this.enqueuedForClosure = false;
}

protected boolean tryTransferOffer(StatementHandle e) {
boolean result = true;

if (!this.statementsPendingRelease.tryTransfer(e)) {
result = this.statementsPendingRelease.offer(e);
}
return result;
}

public void close() throws SQLException {
if (this.statementReleaseHelperEnabled) {
this.enqueuedForClosure = true;

if (!tryTransferOffer(this)) {
this.enqueuedForClosure = false;

closeStatement();
} 
} else {

closeStatement();
} 
}

public void addBatch(String sql) throws SQLException {
checkClosed();
try {
if (this.logStatementsEnabled) {
this.batchSQL.append(sql);
}

this.internalStatement.addBatch(sql);
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 
}

protected void checkClosed() throws SQLException {
if (this.logicallyClosed.get()) {
throw new SQLException("Statement is closed");
}
}

public void cancel() throws SQLException {
checkClosed();
try {
this.internalStatement.cancel();
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 
}

public void clearBatch() throws SQLException {
checkClosed();
try {
if (this.logStatementsEnabled) {
this.batchSQL = new StringBuilder();
}
this.internalStatement.clearBatch();
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 
}

public void clearWarnings() throws SQLException {
checkClosed();
try {
this.internalStatement.clearWarnings();
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 
}

public boolean execute(String sql) throws SQLException {
boolean result = false;
checkClosed();
try {
if (this.logStatementsEnabled && logger.isDebugEnabled()) {
logger.debug(PoolUtil.fillLogParams(sql, this.logParams));
}
long timer = queryTimerStart();
if (this.connectionHook != null) {
this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, sql, this.logParams);
}
result = this.internalStatement.execute(sql);
if (this.connectionHook != null) {
this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, sql, this.logParams);
}
queryTimerEnd(sql, timer);
}
catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

protected void queryTimerEnd(String sql, long queryStartTime) {
if (this.queryExecuteTimeLimit != 0L && this.connectionHook != null) {

long timeElapsed = System.nanoTime() - queryStartTime;

if (timeElapsed > this.queryExecuteTimeLimit) {
this.connectionHook.onQueryExecuteTimeLimitExceeded(this.connectionHandle, this, sql, this.logParams, timeElapsed);
}
} 

if (this.statisticsEnabled) {
this.statistics.incrementStatementsExecuted();
this.statistics.addStatementExecuteTime(System.nanoTime() - queryStartTime);
} 
}

public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
boolean result = false;
checkClosed();
try {
if (this.logStatementsEnabled && logger.isDebugEnabled()) {
logger.debug(PoolUtil.fillLogParams(sql, this.logParams));
}

long queryStartTime = queryTimerStart();
if (this.connectionHook != null) {
this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, sql, this.logParams);
}
result = this.internalStatement.execute(sql, autoGeneratedKeys);

if (this.connectionHook != null) {
this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, sql, this.logParams);
}

queryTimerEnd(sql, queryStartTime);
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

protected long queryTimerStart() {
return (this.statisticsEnabled || (this.queryExecuteTimeLimit != 0L && this.connectionHook != null)) ? System.nanoTime() : Long.MAX_VALUE;
}

public boolean execute(String sql, int[] columnIndexes) throws SQLException {
boolean result = false;
checkClosed();
try {
if (this.logStatementsEnabled && logger.isDebugEnabled()) {
logger.debug(PoolUtil.fillLogParams(sql, this.logParams));
}

long queryStartTime = queryTimerStart();
if (this.connectionHook != null) {
this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, sql, this.logParams);
}

result = this.internalStatement.execute(sql, columnIndexes);

if (this.connectionHook != null)
{

this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, sql, this.logParams);
}
queryTimerEnd(sql, queryStartTime);
}
catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public boolean execute(String sql, String[] columnNames) throws SQLException {
boolean result = false;
checkClosed();
try {
if (this.logStatementsEnabled && logger.isDebugEnabled()) {
logger.debug(PoolUtil.fillLogParams(sql, this.logParams));
}
long queryStartTime = queryTimerStart();
if (this.connectionHook != null) {
this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, sql, this.logParams);
}
result = this.internalStatement.execute(sql, columnNames);
if (this.connectionHook != null) {
this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, sql, this.logParams);
}

queryTimerEnd(sql, queryStartTime);
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public int[] executeBatch() throws SQLException {
int[] result = null;
checkClosed();
try {
if (this.logStatementsEnabled && logger.isDebugEnabled()) {
logger.debug(PoolUtil.fillLogParams(this.batchSQL.toString(), this.logParams));
}
long queryStartTime = queryTimerStart();
String query = this.logStatementsEnabled ? this.batchSQL.toString() : "";
if (this.connectionHook != null) {
this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, query, this.logParams);
}
result = this.internalStatement.executeBatch();

if (this.connectionHook != null) {
this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, query, this.logParams);
}

queryTimerEnd(this.logStatementsEnabled ? this.batchSQL.toString() : "", queryStartTime);
}
catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public ResultSet executeQuery(String sql) throws SQLException {
ResultSet result = null;
checkClosed();
try {
if (this.logStatementsEnabled && logger.isDebugEnabled()) {
logger.debug(PoolUtil.fillLogParams(sql, this.logParams));
}
long queryStartTime = queryTimerStart();
if (this.connectionHook != null) {
this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, sql, this.logParams);
}
result = this.internalStatement.executeQuery(sql);
if (this.connectionHook != null) {
this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, sql, this.logParams);
}

queryTimerEnd(sql, queryStartTime);
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public int executeUpdate(String sql) throws SQLException {
int result = 0;
checkClosed();
try {
if (this.logStatementsEnabled && logger.isDebugEnabled()) {
logger.debug(PoolUtil.fillLogParams(sql, this.logParams));
}
long queryStartTime = queryTimerStart();
if (this.connectionHook != null) {
this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, sql, this.logParams);
}
result = this.internalStatement.executeUpdate(sql);
if (this.connectionHook != null) {
this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, sql, this.logParams);
}

queryTimerEnd(sql, queryStartTime);
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
int result = 0;
checkClosed();
try {
if (this.logStatementsEnabled && logger.isDebugEnabled()) {
logger.debug(PoolUtil.fillLogParams(sql, this.logParams));
}
long queryStartTime = queryTimerStart();
if (this.connectionHook != null) {
this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, sql, this.logParams);
}
result = this.internalStatement.executeUpdate(sql, autoGeneratedKeys);
if (this.connectionHook != null) {
this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, sql, this.logParams);
}

queryTimerEnd(sql, queryStartTime);
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
int result = 0;
checkClosed();
try {
if (this.logStatementsEnabled && logger.isDebugEnabled()) {
logger.debug(PoolUtil.fillLogParams(sql, this.logParams), columnIndexes);
}
long queryStartTime = queryTimerStart();
if (this.connectionHook != null) {
this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, sql, this.logParams);
}
result = this.internalStatement.executeUpdate(sql, columnIndexes);
if (this.connectionHook != null) {
this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, sql, this.logParams);
}

queryTimerEnd(sql, queryStartTime);
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public int executeUpdate(String sql, String[] columnNames) throws SQLException {
int result = 0;
checkClosed();
try {
if (this.logStatementsEnabled && logger.isDebugEnabled()) {
logger.debug(PoolUtil.fillLogParams(sql, this.logParams), (Object[])columnNames);
}
long queryStartTime = queryTimerStart();
if (this.connectionHook != null) {
this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, sql, this.logParams);
}
result = this.internalStatement.executeUpdate(sql, columnNames);
if (this.connectionHook != null) {
this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, sql, this.logParams);
}

queryTimerEnd(sql, queryStartTime);
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public Connection getConnection() throws SQLException {
checkClosed();
return this.connectionHandle;
}

public int getFetchDirection() throws SQLException {
int result = 0;
checkClosed();
try {
result = this.internalStatement.getFetchDirection();
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public int getFetchSize() throws SQLException {
int result = 0;
checkClosed();
try {
result = this.internalStatement.getFetchSize();
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public ResultSet getGeneratedKeys() throws SQLException {
ResultSet result = null;
checkClosed();
try {
result = this.internalStatement.getGeneratedKeys();
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public int getMaxFieldSize() throws SQLException {
int result = 0;
checkClosed();
try {
result = this.internalStatement.getMaxFieldSize();
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public int getMaxRows() throws SQLException {
int result = 0;
checkClosed();
try {
result = this.internalStatement.getMaxRows();
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public boolean getMoreResults() throws SQLException {
boolean result = false;
checkClosed();
try {
result = this.internalStatement.getMoreResults();
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public boolean getMoreResults(int current) throws SQLException {
boolean result = false;
checkClosed();

try {
result = this.internalStatement.getMoreResults(current);
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public int getQueryTimeout() throws SQLException {
int result = 0;
checkClosed();
try {
result = this.internalStatement.getQueryTimeout();
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public ResultSet getResultSet() throws SQLException {
ResultSet result = null;
checkClosed();
try {
result = this.internalStatement.getResultSet();
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public int getResultSetConcurrency() throws SQLException {
int result = 0;
checkClosed();
try {
result = this.internalStatement.getResultSetConcurrency();
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public int getResultSetHoldability() throws SQLException {
int result = 0;
checkClosed();
try {
result = this.internalStatement.getResultSetHoldability();
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public int getResultSetType() throws SQLException {
int result = 0;
checkClosed();
try {
result = this.internalStatement.getResultSetType();
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public int getUpdateCount() throws SQLException {
int result = 0;
checkClosed();
try {
result = this.internalStatement.getUpdateCount();
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public SQLWarning getWarnings() throws SQLException {
SQLWarning result = null;
checkClosed();
try {
result = this.internalStatement.getWarnings();
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public boolean isClosed() {
return this.logicallyClosed.get();
}

public void setPoolable(boolean poolable) throws SQLException {
checkClosed();
try {
this.internalStatement.setPoolable(poolable);
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 
}

public boolean isWrapperFor(Class<?> iface) throws SQLException {
boolean result = false;
try {
result = this.internalStatement.isWrapperFor(iface);
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public <T> T unwrap(Class<T> iface) throws SQLException {
T result = null;

try {
result = this.internalStatement.unwrap(iface);
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public boolean isPoolable() throws SQLException {
boolean result = false;
checkClosed();
try {
result = this.internalStatement.isPoolable();
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 

return result;
}

public void setCursorName(String name) throws SQLException {
checkClosed();
try {
this.internalStatement.setCursorName(name);
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 
}

public void setEscapeProcessing(boolean enable) throws SQLException {
checkClosed();
try {
this.internalStatement.setEscapeProcessing(enable);
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 
}

public void setFetchDirection(int direction) throws SQLException {
checkClosed();
try {
this.internalStatement.setFetchDirection(direction);
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 
}

public void setFetchSize(int rows) throws SQLException {
checkClosed();
try {
this.internalStatement.setFetchSize(rows);
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 
}

public void setMaxFieldSize(int max) throws SQLException {
checkClosed();
try {
this.internalStatement.setMaxFieldSize(max);
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 
}

public void setMaxRows(int max) throws SQLException {
checkClosed();
try {
this.internalStatement.setMaxRows(max);
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 
}

public void setQueryTimeout(int seconds) throws SQLException {
checkClosed();
try {
this.internalStatement.setQueryTimeout(seconds);
} catch (SQLException e) {
throw this.connectionHandle.markPossiblyBroken(e);
} 
}

protected void clearCache() {
if (this.cache != null) {
this.cache.clear();
}
}

protected void setLogicallyOpen() {
this.logicallyClosed.set(false);
}

public String toString() {
return this.sql;
}

public String getOpenStackTrace() {
return this.openStackTrace;
}

public void setOpenStackTrace(String openStackTrace) {
this.openStackTrace = openStackTrace;
}

public Statement getInternalStatement() {
return this.internalStatement;
}

public void setInternalStatement(Statement internalStatement) {
this.internalStatement = internalStatement;
}

public void setDebugHandle(Object debugHandle) {
this.debugHandle = debugHandle;
}

public Object getDebugHandle() {
return this.debugHandle;
}

public boolean isEnqueuedForClosure() {
return this.enqueuedForClosure;
}

public boolean isClosedOrEnqueuedForClosure() {
return (this.enqueuedForClosure || isClosed());
}
}

