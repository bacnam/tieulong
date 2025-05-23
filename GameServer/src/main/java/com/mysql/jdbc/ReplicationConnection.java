package com.mysql.jdbc;

import com.mysql.jdbc.log.Log;
import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.Executor;

public class ReplicationConnection
implements Connection, PingTarget
{
protected Connection currentConnection;
protected Connection masterConnection;
protected Connection slavesConnection;

protected ReplicationConnection() {}

public ReplicationConnection(Properties masterProperties, Properties slaveProperties) throws SQLException {
NonRegisteringDriver driver = new NonRegisteringDriver();

StringBuffer masterUrl = new StringBuffer("jdbc:mysql:
StringBuffer slaveUrl = new StringBuffer("jdbc:mysql:loadbalance:

String masterHost = masterProperties.getProperty("HOST");

if (masterHost != null) {
masterUrl.append(masterHost);
}

int numHosts = Integer.parseInt(slaveProperties.getProperty("NUM_HOSTS"));

for (int i = 1; i <= numHosts; i++) {
String slaveHost = slaveProperties.getProperty("HOST." + i);

if (slaveHost != null) {
if (i > 1) {
slaveUrl.append(',');
}
slaveUrl.append(slaveHost);
} 
} 

String masterDb = masterProperties.getProperty("DBNAME");

masterUrl.append("/");

if (masterDb != null) {
masterUrl.append(masterDb);
}

String slaveDb = slaveProperties.getProperty("DBNAME");

slaveUrl.append("/");

if (slaveDb != null) {
slaveUrl.append(slaveDb);
}

slaveProperties.setProperty("roundRobinLoadBalance", "true");

this.masterConnection = (Connection)driver.connect(masterUrl.toString(), masterProperties);

this.slavesConnection = (Connection)driver.connect(slaveUrl.toString(), slaveProperties);

this.slavesConnection.setReadOnly(true);

this.currentConnection = this.masterConnection;
}

public synchronized void clearWarnings() throws SQLException {
this.currentConnection.clearWarnings();
}

public synchronized void close() throws SQLException {
this.masterConnection.close();
this.slavesConnection.close();
}

public synchronized void commit() throws SQLException {
this.currentConnection.commit();
}

public Statement createStatement() throws SQLException {
Statement stmt = this.currentConnection.createStatement();
((Statement)stmt).setPingTarget(this);

return stmt;
}

public synchronized Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
Statement stmt = this.currentConnection.createStatement(resultSetType, resultSetConcurrency);

((Statement)stmt).setPingTarget(this);

return stmt;
}

public synchronized Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
Statement stmt = this.currentConnection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);

((Statement)stmt).setPingTarget(this);

return stmt;
}

public synchronized boolean getAutoCommit() throws SQLException {
return this.currentConnection.getAutoCommit();
}

public synchronized String getCatalog() throws SQLException {
return this.currentConnection.getCatalog();
}

public synchronized Connection getCurrentConnection() {
return this.currentConnection;
}

public synchronized int getHoldability() throws SQLException {
return this.currentConnection.getHoldability();
}

public synchronized Connection getMasterConnection() {
return this.masterConnection;
}

public synchronized DatabaseMetaData getMetaData() throws SQLException {
return this.currentConnection.getMetaData();
}

public synchronized Connection getSlavesConnection() {
return this.slavesConnection;
}

public synchronized int getTransactionIsolation() throws SQLException {
return this.currentConnection.getTransactionIsolation();
}

public synchronized Map<String, Class<?>> getTypeMap() throws SQLException {
return this.currentConnection.getTypeMap();
}

public synchronized SQLWarning getWarnings() throws SQLException {
return this.currentConnection.getWarnings();
}

public synchronized boolean isClosed() throws SQLException {
return this.currentConnection.isClosed();
}

public synchronized boolean isReadOnly() throws SQLException {
return (this.currentConnection == this.slavesConnection);
}

public synchronized String nativeSQL(String sql) throws SQLException {
return this.currentConnection.nativeSQL(sql);
}

public CallableStatement prepareCall(String sql) throws SQLException {
return this.currentConnection.prepareCall(sql);
}

public synchronized CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
return this.currentConnection.prepareCall(sql, resultSetType, resultSetConcurrency);
}

public synchronized CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
return this.currentConnection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
}

public PreparedStatement prepareStatement(String sql) throws SQLException {
PreparedStatement pstmt = this.currentConnection.prepareStatement(sql);

((Statement)pstmt).setPingTarget(this);

return pstmt;
}

public synchronized PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
PreparedStatement pstmt = this.currentConnection.prepareStatement(sql, autoGeneratedKeys);

((Statement)pstmt).setPingTarget(this);

return pstmt;
}

public synchronized PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
PreparedStatement pstmt = this.currentConnection.prepareStatement(sql, resultSetType, resultSetConcurrency);

((Statement)pstmt).setPingTarget(this);

return pstmt;
}

public synchronized PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
PreparedStatement pstmt = this.currentConnection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);

((Statement)pstmt).setPingTarget(this);

return pstmt;
}

public synchronized PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
PreparedStatement pstmt = this.currentConnection.prepareStatement(sql, columnIndexes);

((Statement)pstmt).setPingTarget(this);

return pstmt;
}

public synchronized PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
PreparedStatement pstmt = this.currentConnection.prepareStatement(sql, columnNames);

((Statement)pstmt).setPingTarget(this);

return pstmt;
}

public synchronized void releaseSavepoint(Savepoint savepoint) throws SQLException {
this.currentConnection.releaseSavepoint(savepoint);
}

public synchronized void rollback() throws SQLException {
this.currentConnection.rollback();
}

public synchronized void rollback(Savepoint savepoint) throws SQLException {
this.currentConnection.rollback(savepoint);
}

public synchronized void setAutoCommit(boolean autoCommit) throws SQLException {
this.currentConnection.setAutoCommit(autoCommit);
}

public synchronized void setCatalog(String catalog) throws SQLException {
this.currentConnection.setCatalog(catalog);
}

public synchronized void setHoldability(int holdability) throws SQLException {
this.currentConnection.setHoldability(holdability);
}

public synchronized void setReadOnly(boolean readOnly) throws SQLException {
if (readOnly) {
if (this.currentConnection != this.slavesConnection) {
switchToSlavesConnection();
}
}
else if (this.currentConnection != this.masterConnection) {
switchToMasterConnection();
} 
}

public synchronized Savepoint setSavepoint() throws SQLException {
return this.currentConnection.setSavepoint();
}

public synchronized Savepoint setSavepoint(String name) throws SQLException {
return this.currentConnection.setSavepoint(name);
}

public synchronized void setTransactionIsolation(int level) throws SQLException {
this.currentConnection.setTransactionIsolation(level);
}

private synchronized void switchToMasterConnection() throws SQLException {
swapConnections(this.masterConnection, this.slavesConnection);
}

private synchronized void switchToSlavesConnection() throws SQLException {
swapConnections(this.slavesConnection, this.masterConnection);
this.slavesConnection.setReadOnly(true);
}

private synchronized void swapConnections(Connection switchToConnection, Connection switchFromConnection) throws SQLException {
String switchFromCatalog = switchFromConnection.getCatalog();
String switchToCatalog = switchToConnection.getCatalog();

if (switchToCatalog != null && !switchToCatalog.equals(switchFromCatalog)) {
switchToConnection.setCatalog(switchFromCatalog);
} else if (switchFromCatalog != null) {
switchToConnection.setCatalog(switchFromCatalog);
} 

boolean switchToAutoCommit = switchToConnection.getAutoCommit();
boolean switchFromConnectionAutoCommit = switchFromConnection.getAutoCommit();

if (switchFromConnectionAutoCommit != switchToAutoCommit) {
switchToConnection.setAutoCommit(switchFromConnectionAutoCommit);
}

int switchToIsolation = switchToConnection.getTransactionIsolation();

int switchFromIsolation = switchFromConnection.getTransactionIsolation();

if (switchFromIsolation != switchToIsolation) {
switchToConnection.setTransactionIsolation(switchFromIsolation);
}

this.currentConnection = switchToConnection;
}

public synchronized void doPing() throws SQLException {
if (this.masterConnection != null) {
this.masterConnection.ping();
}

if (this.slavesConnection != null) {
this.slavesConnection.ping();
}
}

public synchronized void changeUser(String userName, String newPassword) throws SQLException {
this.masterConnection.changeUser(userName, newPassword);
this.slavesConnection.changeUser(userName, newPassword);
}

public synchronized void clearHasTriedMaster() {
this.masterConnection.clearHasTriedMaster();
this.slavesConnection.clearHasTriedMaster();
}

public synchronized PreparedStatement clientPrepareStatement(String sql) throws SQLException {
PreparedStatement pstmt = this.currentConnection.clientPrepareStatement(sql);
((Statement)pstmt).setPingTarget(this);

return pstmt;
}

public synchronized PreparedStatement clientPrepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
PreparedStatement pstmt = this.currentConnection.clientPrepareStatement(sql, autoGenKeyIndex);
((Statement)pstmt).setPingTarget(this);

return pstmt;
}

public synchronized PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
PreparedStatement pstmt = this.currentConnection.clientPrepareStatement(sql, resultSetType, resultSetConcurrency);
((Statement)pstmt).setPingTarget(this);

return pstmt;
}

public synchronized PreparedStatement clientPrepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
PreparedStatement pstmt = this.currentConnection.clientPrepareStatement(sql, autoGenKeyIndexes);
((Statement)pstmt).setPingTarget(this);

return pstmt;
}

public synchronized PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
PreparedStatement pstmt = this.currentConnection.clientPrepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
((Statement)pstmt).setPingTarget(this);

return pstmt;
}

public synchronized PreparedStatement clientPrepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
PreparedStatement pstmt = this.currentConnection.clientPrepareStatement(sql, autoGenKeyColNames);
((Statement)pstmt).setPingTarget(this);

return pstmt;
}

public synchronized int getActiveStatementCount() {
return this.currentConnection.getActiveStatementCount();
}

public synchronized long getIdleFor() {
return this.currentConnection.getIdleFor();
}

public synchronized Log getLog() throws SQLException {
return this.currentConnection.getLog();
}

public synchronized String getServerCharacterEncoding() {
return this.currentConnection.getServerCharacterEncoding();
}

public synchronized TimeZone getServerTimezoneTZ() {
return this.currentConnection.getServerTimezoneTZ();
}

public synchronized String getStatementComment() {
return this.currentConnection.getStatementComment();
}

public synchronized boolean hasTriedMaster() {
return this.currentConnection.hasTriedMaster();
}

public synchronized void initializeExtension(Extension ex) throws SQLException {
this.currentConnection.initializeExtension(ex);
}

public synchronized boolean isAbonormallyLongQuery(long millisOrNanos) {
return this.currentConnection.isAbonormallyLongQuery(millisOrNanos);
}

public synchronized boolean isInGlobalTx() {
return this.currentConnection.isInGlobalTx();
}

public synchronized boolean isMasterConnection() {
return this.currentConnection.isMasterConnection();
}

public synchronized boolean isNoBackslashEscapesSet() {
return this.currentConnection.isNoBackslashEscapesSet();
}

public synchronized boolean lowerCaseTableNames() {
return this.currentConnection.lowerCaseTableNames();
}

public synchronized boolean parserKnowsUnicode() {
return this.currentConnection.parserKnowsUnicode();
}

public synchronized void ping() throws SQLException {
this.masterConnection.ping();
this.slavesConnection.ping();
}

public synchronized void reportQueryTime(long millisOrNanos) {
this.currentConnection.reportQueryTime(millisOrNanos);
}

public synchronized void resetServerState() throws SQLException {
this.currentConnection.resetServerState();
}

public synchronized PreparedStatement serverPrepareStatement(String sql) throws SQLException {
PreparedStatement pstmt = this.currentConnection.serverPrepareStatement(sql);
((Statement)pstmt).setPingTarget(this);

return pstmt;
}

public synchronized PreparedStatement serverPrepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
PreparedStatement pstmt = this.currentConnection.serverPrepareStatement(sql, autoGenKeyIndex);
((Statement)pstmt).setPingTarget(this);

return pstmt;
}

public synchronized PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
PreparedStatement pstmt = this.currentConnection.serverPrepareStatement(sql, resultSetType, resultSetConcurrency);
((Statement)pstmt).setPingTarget(this);

return pstmt;
}

public synchronized PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
PreparedStatement pstmt = this.currentConnection.serverPrepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
((Statement)pstmt).setPingTarget(this);

return pstmt;
}

public synchronized PreparedStatement serverPrepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
PreparedStatement pstmt = this.currentConnection.serverPrepareStatement(sql, autoGenKeyIndexes);
((Statement)pstmt).setPingTarget(this);

return pstmt;
}

public synchronized PreparedStatement serverPrepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
PreparedStatement pstmt = this.currentConnection.serverPrepareStatement(sql, autoGenKeyColNames);
((Statement)pstmt).setPingTarget(this);

return pstmt;
}

public synchronized void setFailedOver(boolean flag) {
this.currentConnection.setFailedOver(flag);
}

public synchronized void setPreferSlaveDuringFailover(boolean flag) {
this.currentConnection.setPreferSlaveDuringFailover(flag);
}

public synchronized void setStatementComment(String comment) {
this.masterConnection.setStatementComment(comment);
this.slavesConnection.setStatementComment(comment);
}

public synchronized void shutdownServer() throws SQLException {
this.currentConnection.shutdownServer();
}

public synchronized boolean supportsIsolationLevel() {
return this.currentConnection.supportsIsolationLevel();
}

public synchronized boolean supportsQuotedIdentifiers() {
return this.currentConnection.supportsQuotedIdentifiers();
}

public synchronized boolean supportsTransactions() {
return this.currentConnection.supportsTransactions();
}

public synchronized boolean versionMeetsMinimum(int major, int minor, int subminor) throws SQLException {
return this.currentConnection.versionMeetsMinimum(major, minor, subminor);
}

public synchronized String exposeAsXml() throws SQLException {
return this.currentConnection.exposeAsXml();
}

public synchronized boolean getAllowLoadLocalInfile() {
return this.currentConnection.getAllowLoadLocalInfile();
}

public synchronized boolean getAllowMultiQueries() {
return this.currentConnection.getAllowMultiQueries();
}

public synchronized boolean getAllowNanAndInf() {
return this.currentConnection.getAllowNanAndInf();
}

public synchronized boolean getAllowUrlInLocalInfile() {
return this.currentConnection.getAllowUrlInLocalInfile();
}

public synchronized boolean getAlwaysSendSetIsolation() {
return this.currentConnection.getAlwaysSendSetIsolation();
}

public synchronized boolean getAutoClosePStmtStreams() {
return this.currentConnection.getAutoClosePStmtStreams();
}

public synchronized boolean getAutoDeserialize() {
return this.currentConnection.getAutoDeserialize();
}

public synchronized boolean getAutoGenerateTestcaseScript() {
return this.currentConnection.getAutoGenerateTestcaseScript();
}

public synchronized boolean getAutoReconnectForPools() {
return this.currentConnection.getAutoReconnectForPools();
}

public synchronized boolean getAutoSlowLog() {
return this.currentConnection.getAutoSlowLog();
}

public synchronized int getBlobSendChunkSize() {
return this.currentConnection.getBlobSendChunkSize();
}

public synchronized boolean getBlobsAreStrings() {
return this.currentConnection.getBlobsAreStrings();
}

public synchronized boolean getCacheCallableStatements() {
return this.currentConnection.getCacheCallableStatements();
}

public synchronized boolean getCacheCallableStmts() {
return this.currentConnection.getCacheCallableStmts();
}

public synchronized boolean getCachePrepStmts() {
return this.currentConnection.getCachePrepStmts();
}

public synchronized boolean getCachePreparedStatements() {
return this.currentConnection.getCachePreparedStatements();
}

public synchronized boolean getCacheResultSetMetadata() {
return this.currentConnection.getCacheResultSetMetadata();
}

public synchronized boolean getCacheServerConfiguration() {
return this.currentConnection.getCacheServerConfiguration();
}

public synchronized int getCallableStatementCacheSize() {
return this.currentConnection.getCallableStatementCacheSize();
}

public synchronized int getCallableStmtCacheSize() {
return this.currentConnection.getCallableStmtCacheSize();
}

public synchronized boolean getCapitalizeTypeNames() {
return this.currentConnection.getCapitalizeTypeNames();
}

public synchronized String getCharacterSetResults() {
return this.currentConnection.getCharacterSetResults();
}

public synchronized String getClientCertificateKeyStorePassword() {
return this.currentConnection.getClientCertificateKeyStorePassword();
}

public synchronized String getClientCertificateKeyStoreType() {
return this.currentConnection.getClientCertificateKeyStoreType();
}

public synchronized String getClientCertificateKeyStoreUrl() {
return this.currentConnection.getClientCertificateKeyStoreUrl();
}

public synchronized String getClientInfoProvider() {
return this.currentConnection.getClientInfoProvider();
}

public synchronized String getClobCharacterEncoding() {
return this.currentConnection.getClobCharacterEncoding();
}

public synchronized boolean getClobberStreamingResults() {
return this.currentConnection.getClobberStreamingResults();
}

public synchronized int getConnectTimeout() {
return this.currentConnection.getConnectTimeout();
}

public synchronized String getConnectionCollation() {
return this.currentConnection.getConnectionCollation();
}

public synchronized String getConnectionLifecycleInterceptors() {
return this.currentConnection.getConnectionLifecycleInterceptors();
}

public synchronized boolean getContinueBatchOnError() {
return this.currentConnection.getContinueBatchOnError();
}

public synchronized boolean getCreateDatabaseIfNotExist() {
return this.currentConnection.getCreateDatabaseIfNotExist();
}

public synchronized int getDefaultFetchSize() {
return this.currentConnection.getDefaultFetchSize();
}

public synchronized boolean getDontTrackOpenResources() {
return this.currentConnection.getDontTrackOpenResources();
}

public synchronized boolean getDumpMetadataOnColumnNotFound() {
return this.currentConnection.getDumpMetadataOnColumnNotFound();
}

public synchronized boolean getDumpQueriesOnException() {
return this.currentConnection.getDumpQueriesOnException();
}

public synchronized boolean getDynamicCalendars() {
return this.currentConnection.getDynamicCalendars();
}

public synchronized boolean getElideSetAutoCommits() {
return this.currentConnection.getElideSetAutoCommits();
}

public synchronized boolean getEmptyStringsConvertToZero() {
return this.currentConnection.getEmptyStringsConvertToZero();
}

public synchronized boolean getEmulateLocators() {
return this.currentConnection.getEmulateLocators();
}

public synchronized boolean getEmulateUnsupportedPstmts() {
return this.currentConnection.getEmulateUnsupportedPstmts();
}

public synchronized boolean getEnablePacketDebug() {
return this.currentConnection.getEnablePacketDebug();
}

public synchronized boolean getEnableQueryTimeouts() {
return this.currentConnection.getEnableQueryTimeouts();
}

public synchronized String getEncoding() {
return this.currentConnection.getEncoding();
}

public synchronized boolean getExplainSlowQueries() {
return this.currentConnection.getExplainSlowQueries();
}

public synchronized boolean getFailOverReadOnly() {
return this.currentConnection.getFailOverReadOnly();
}

public synchronized boolean getFunctionsNeverReturnBlobs() {
return this.currentConnection.getFunctionsNeverReturnBlobs();
}

public synchronized boolean getGatherPerfMetrics() {
return this.currentConnection.getGatherPerfMetrics();
}

public synchronized boolean getGatherPerformanceMetrics() {
return this.currentConnection.getGatherPerformanceMetrics();
}

public synchronized boolean getGenerateSimpleParameterMetadata() {
return this.currentConnection.getGenerateSimpleParameterMetadata();
}

public synchronized boolean getHoldResultsOpenOverStatementClose() {
return this.currentConnection.getHoldResultsOpenOverStatementClose();
}

public synchronized boolean getIgnoreNonTxTables() {
return this.currentConnection.getIgnoreNonTxTables();
}

public synchronized boolean getIncludeInnodbStatusInDeadlockExceptions() {
return this.currentConnection.getIncludeInnodbStatusInDeadlockExceptions();
}

public synchronized int getInitialTimeout() {
return this.currentConnection.getInitialTimeout();
}

public synchronized boolean getInteractiveClient() {
return this.currentConnection.getInteractiveClient();
}

public synchronized boolean getIsInteractiveClient() {
return this.currentConnection.getIsInteractiveClient();
}

public synchronized boolean getJdbcCompliantTruncation() {
return this.currentConnection.getJdbcCompliantTruncation();
}

public synchronized boolean getJdbcCompliantTruncationForReads() {
return this.currentConnection.getJdbcCompliantTruncationForReads();
}

public synchronized String getLargeRowSizeThreshold() {
return this.currentConnection.getLargeRowSizeThreshold();
}

public synchronized String getLoadBalanceStrategy() {
return this.currentConnection.getLoadBalanceStrategy();
}

public synchronized String getLocalSocketAddress() {
return this.currentConnection.getLocalSocketAddress();
}

public synchronized int getLocatorFetchBufferSize() {
return this.currentConnection.getLocatorFetchBufferSize();
}

public synchronized boolean getLogSlowQueries() {
return this.currentConnection.getLogSlowQueries();
}

public synchronized boolean getLogXaCommands() {
return this.currentConnection.getLogXaCommands();
}

public synchronized String getLogger() {
return this.currentConnection.getLogger();
}

public synchronized String getLoggerClassName() {
return this.currentConnection.getLoggerClassName();
}

public synchronized boolean getMaintainTimeStats() {
return this.currentConnection.getMaintainTimeStats();
}

public synchronized int getMaxQuerySizeToLog() {
return this.currentConnection.getMaxQuerySizeToLog();
}

public synchronized int getMaxReconnects() {
return this.currentConnection.getMaxReconnects();
}

public synchronized int getMaxRows() {
return this.currentConnection.getMaxRows();
}

public synchronized int getMetadataCacheSize() {
return this.currentConnection.getMetadataCacheSize();
}

public synchronized int getNetTimeoutForStreamingResults() {
return this.currentConnection.getNetTimeoutForStreamingResults();
}

public synchronized boolean getNoAccessToProcedureBodies() {
return this.currentConnection.getNoAccessToProcedureBodies();
}

public synchronized boolean getNoDatetimeStringSync() {
return this.currentConnection.getNoDatetimeStringSync();
}

public synchronized boolean getNoTimezoneConversionForTimeType() {
return this.currentConnection.getNoTimezoneConversionForTimeType();
}

public synchronized boolean getNullCatalogMeansCurrent() {
return this.currentConnection.getNullCatalogMeansCurrent();
}

public synchronized boolean getNullNamePatternMatchesAll() {
return this.currentConnection.getNullNamePatternMatchesAll();
}

public synchronized boolean getOverrideSupportsIntegrityEnhancementFacility() {
return this.currentConnection.getOverrideSupportsIntegrityEnhancementFacility();
}

public synchronized int getPacketDebugBufferSize() {
return this.currentConnection.getPacketDebugBufferSize();
}

public synchronized boolean getPadCharsWithSpace() {
return this.currentConnection.getPadCharsWithSpace();
}

public synchronized boolean getParanoid() {
return this.currentConnection.getParanoid();
}

public synchronized boolean getPedantic() {
return this.currentConnection.getPedantic();
}

public synchronized boolean getPinGlobalTxToPhysicalConnection() {
return this.currentConnection.getPinGlobalTxToPhysicalConnection();
}

public synchronized boolean getPopulateInsertRowWithDefaultValues() {
return this.currentConnection.getPopulateInsertRowWithDefaultValues();
}

public synchronized int getPrepStmtCacheSize() {
return this.currentConnection.getPrepStmtCacheSize();
}

public synchronized int getPrepStmtCacheSqlLimit() {
return this.currentConnection.getPrepStmtCacheSqlLimit();
}

public synchronized int getPreparedStatementCacheSize() {
return this.currentConnection.getPreparedStatementCacheSize();
}

public synchronized int getPreparedStatementCacheSqlLimit() {
return this.currentConnection.getPreparedStatementCacheSqlLimit();
}

public synchronized boolean getProcessEscapeCodesForPrepStmts() {
return this.currentConnection.getProcessEscapeCodesForPrepStmts();
}

public synchronized boolean getProfileSQL() {
return this.currentConnection.getProfileSQL();
}

public synchronized boolean getProfileSql() {
return this.currentConnection.getProfileSql();
}

public synchronized String getProfilerEventHandler() {
return this.currentConnection.getProfilerEventHandler();
}

public synchronized String getPropertiesTransform() {
return this.currentConnection.getPropertiesTransform();
}

public synchronized int getQueriesBeforeRetryMaster() {
return this.currentConnection.getQueriesBeforeRetryMaster();
}

public synchronized boolean getReconnectAtTxEnd() {
return this.currentConnection.getReconnectAtTxEnd();
}

public synchronized boolean getRelaxAutoCommit() {
return this.currentConnection.getRelaxAutoCommit();
}

public synchronized int getReportMetricsIntervalMillis() {
return this.currentConnection.getReportMetricsIntervalMillis();
}

public synchronized boolean getRequireSSL() {
return this.currentConnection.getRequireSSL();
}

public synchronized String getResourceId() {
return this.currentConnection.getResourceId();
}

public synchronized int getResultSetSizeThreshold() {
return this.currentConnection.getResultSetSizeThreshold();
}

public synchronized boolean getRewriteBatchedStatements() {
return this.currentConnection.getRewriteBatchedStatements();
}

public synchronized boolean getRollbackOnPooledClose() {
return this.currentConnection.getRollbackOnPooledClose();
}

public synchronized boolean getRoundRobinLoadBalance() {
return this.currentConnection.getRoundRobinLoadBalance();
}

public synchronized boolean getRunningCTS13() {
return this.currentConnection.getRunningCTS13();
}

public synchronized int getSecondsBeforeRetryMaster() {
return this.currentConnection.getSecondsBeforeRetryMaster();
}

public synchronized int getSelfDestructOnPingMaxOperations() {
return this.currentConnection.getSelfDestructOnPingMaxOperations();
}

public synchronized int getSelfDestructOnPingSecondsLifetime() {
return this.currentConnection.getSelfDestructOnPingSecondsLifetime();
}

public synchronized String getServerTimezone() {
return this.currentConnection.getServerTimezone();
}

public synchronized String getSessionVariables() {
return this.currentConnection.getSessionVariables();
}

public synchronized int getSlowQueryThresholdMillis() {
return this.currentConnection.getSlowQueryThresholdMillis();
}

public synchronized long getSlowQueryThresholdNanos() {
return this.currentConnection.getSlowQueryThresholdNanos();
}

public synchronized String getSocketFactory() {
return this.currentConnection.getSocketFactory();
}

public synchronized String getSocketFactoryClassName() {
return this.currentConnection.getSocketFactoryClassName();
}

public synchronized int getSocketTimeout() {
return this.currentConnection.getSocketTimeout();
}

public synchronized String getStatementInterceptors() {
return this.currentConnection.getStatementInterceptors();
}

public synchronized boolean getStrictFloatingPoint() {
return this.currentConnection.getStrictFloatingPoint();
}

public synchronized boolean getStrictUpdates() {
return this.currentConnection.getStrictUpdates();
}

public synchronized boolean getTcpKeepAlive() {
return this.currentConnection.getTcpKeepAlive();
}

public synchronized boolean getTcpNoDelay() {
return this.currentConnection.getTcpNoDelay();
}

public synchronized int getTcpRcvBuf() {
return this.currentConnection.getTcpRcvBuf();
}

public synchronized int getTcpSndBuf() {
return this.currentConnection.getTcpSndBuf();
}

public synchronized int getTcpTrafficClass() {
return this.currentConnection.getTcpTrafficClass();
}

public synchronized boolean getTinyInt1isBit() {
return this.currentConnection.getTinyInt1isBit();
}

public synchronized boolean getTraceProtocol() {
return this.currentConnection.getTraceProtocol();
}

public synchronized boolean getTransformedBitIsBoolean() {
return this.currentConnection.getTransformedBitIsBoolean();
}

public synchronized boolean getTreatUtilDateAsTimestamp() {
return this.currentConnection.getTreatUtilDateAsTimestamp();
}

public synchronized String getTrustCertificateKeyStorePassword() {
return this.currentConnection.getTrustCertificateKeyStorePassword();
}

public synchronized String getTrustCertificateKeyStoreType() {
return this.currentConnection.getTrustCertificateKeyStoreType();
}

public synchronized String getTrustCertificateKeyStoreUrl() {
return this.currentConnection.getTrustCertificateKeyStoreUrl();
}

public synchronized boolean getUltraDevHack() {
return this.currentConnection.getUltraDevHack();
}

public synchronized boolean getUseBlobToStoreUTF8OutsideBMP() {
return this.currentConnection.getUseBlobToStoreUTF8OutsideBMP();
}

public synchronized boolean getUseCompression() {
return this.currentConnection.getUseCompression();
}

public synchronized String getUseConfigs() {
return this.currentConnection.getUseConfigs();
}

public synchronized boolean getUseCursorFetch() {
return this.currentConnection.getUseCursorFetch();
}

public synchronized boolean getUseDirectRowUnpack() {
return this.currentConnection.getUseDirectRowUnpack();
}

public synchronized boolean getUseDynamicCharsetInfo() {
return this.currentConnection.getUseDynamicCharsetInfo();
}

public synchronized boolean getUseFastDateParsing() {
return this.currentConnection.getUseFastDateParsing();
}

public synchronized boolean getUseFastIntParsing() {
return this.currentConnection.getUseFastIntParsing();
}

public synchronized boolean getUseGmtMillisForDatetimes() {
return this.currentConnection.getUseGmtMillisForDatetimes();
}

public synchronized boolean getUseHostsInPrivileges() {
return this.currentConnection.getUseHostsInPrivileges();
}

public synchronized boolean getUseInformationSchema() {
return this.currentConnection.getUseInformationSchema();
}

public synchronized boolean getUseJDBCCompliantTimezoneShift() {
return this.currentConnection.getUseJDBCCompliantTimezoneShift();
}

public synchronized boolean getUseJvmCharsetConverters() {
return this.currentConnection.getUseJvmCharsetConverters();
}

public synchronized boolean getUseLegacyDatetimeCode() {
return this.currentConnection.getUseLegacyDatetimeCode();
}

public synchronized boolean getUseLocalSessionState() {
return this.currentConnection.getUseLocalSessionState();
}

public synchronized boolean getUseNanosForElapsedTime() {
return this.currentConnection.getUseNanosForElapsedTime();
}

public synchronized boolean getUseOldAliasMetadataBehavior() {
return this.currentConnection.getUseOldAliasMetadataBehavior();
}

public synchronized boolean getUseOldUTF8Behavior() {
return this.currentConnection.getUseOldUTF8Behavior();
}

public synchronized boolean getUseOnlyServerErrorMessages() {
return this.currentConnection.getUseOnlyServerErrorMessages();
}

public synchronized boolean getUseReadAheadInput() {
return this.currentConnection.getUseReadAheadInput();
}

public synchronized boolean getUseSSL() {
return this.currentConnection.getUseSSL();
}

public synchronized boolean getUseSSPSCompatibleTimezoneShift() {
return this.currentConnection.getUseSSPSCompatibleTimezoneShift();
}

public synchronized boolean getUseServerPrepStmts() {
return this.currentConnection.getUseServerPrepStmts();
}

public synchronized boolean getUseServerPreparedStmts() {
return this.currentConnection.getUseServerPreparedStmts();
}

public synchronized boolean getUseSqlStateCodes() {
return this.currentConnection.getUseSqlStateCodes();
}

public synchronized boolean getUseStreamLengthsInPrepStmts() {
return this.currentConnection.getUseStreamLengthsInPrepStmts();
}

public synchronized boolean getUseTimezone() {
return this.currentConnection.getUseTimezone();
}

public synchronized boolean getUseUltraDevWorkAround() {
return this.currentConnection.getUseUltraDevWorkAround();
}

public synchronized boolean getUseUnbufferedInput() {
return this.currentConnection.getUseUnbufferedInput();
}

public synchronized boolean getUseUnicode() {
return this.currentConnection.getUseUnicode();
}

public synchronized boolean getUseUsageAdvisor() {
return this.currentConnection.getUseUsageAdvisor();
}

public synchronized String getUtf8OutsideBmpExcludedColumnNamePattern() {
return this.currentConnection.getUtf8OutsideBmpExcludedColumnNamePattern();
}

public synchronized String getUtf8OutsideBmpIncludedColumnNamePattern() {
return this.currentConnection.getUtf8OutsideBmpIncludedColumnNamePattern();
}

public synchronized boolean getVerifyServerCertificate() {
return this.currentConnection.getVerifyServerCertificate();
}

public synchronized boolean getYearIsDateType() {
return this.currentConnection.getYearIsDateType();
}

public synchronized String getZeroDateTimeBehavior() {
return this.currentConnection.getZeroDateTimeBehavior();
}

public synchronized void setAllowLoadLocalInfile(boolean property) {}

public synchronized void setAllowMultiQueries(boolean property) {}

public synchronized void setAllowNanAndInf(boolean flag) {}

public synchronized void setAllowUrlInLocalInfile(boolean flag) {}

public synchronized void setAlwaysSendSetIsolation(boolean flag) {}

public synchronized void setAutoClosePStmtStreams(boolean flag) {}

public synchronized void setAutoDeserialize(boolean flag) {}

public synchronized void setAutoGenerateTestcaseScript(boolean flag) {}

public synchronized void setAutoReconnect(boolean flag) {}

public synchronized void setAutoReconnectForConnectionPools(boolean property) {}

public synchronized void setAutoReconnectForPools(boolean flag) {}

public synchronized void setAutoSlowLog(boolean flag) {}

public synchronized void setBlobSendChunkSize(String value) throws SQLException {}

public synchronized void setBlobsAreStrings(boolean flag) {}

public synchronized void setCacheCallableStatements(boolean flag) {}

public synchronized void setCacheCallableStmts(boolean flag) {}

public synchronized void setCachePrepStmts(boolean flag) {}

public synchronized void setCachePreparedStatements(boolean flag) {}

public synchronized void setCacheResultSetMetadata(boolean property) {}

public synchronized void setCacheServerConfiguration(boolean flag) {}

public synchronized void setCallableStatementCacheSize(int size) {}

public synchronized void setCallableStmtCacheSize(int cacheSize) {}

public synchronized void setCapitalizeDBMDTypes(boolean property) {}

public synchronized void setCapitalizeTypeNames(boolean flag) {}

public synchronized void setCharacterEncoding(String encoding) {}

public synchronized void setCharacterSetResults(String characterSet) {}

public synchronized void setClientCertificateKeyStorePassword(String value) {}

public synchronized void setClientCertificateKeyStoreType(String value) {}

public synchronized void setClientCertificateKeyStoreUrl(String value) {}

public synchronized void setClientInfoProvider(String classname) {}

public synchronized void setClobCharacterEncoding(String encoding) {}

public synchronized void setClobberStreamingResults(boolean flag) {}

public synchronized void setConnectTimeout(int timeoutMs) {}

public synchronized void setConnectionCollation(String collation) {}

public synchronized void setConnectionLifecycleInterceptors(String interceptors) {}

public synchronized void setContinueBatchOnError(boolean property) {}

public synchronized void setCreateDatabaseIfNotExist(boolean flag) {}

public synchronized void setDefaultFetchSize(int n) {}

public synchronized void setDetectServerPreparedStmts(boolean property) {}

public synchronized void setDontTrackOpenResources(boolean flag) {}

public synchronized void setDumpMetadataOnColumnNotFound(boolean flag) {}

public synchronized void setDumpQueriesOnException(boolean flag) {}

public synchronized void setDynamicCalendars(boolean flag) {}

public synchronized void setElideSetAutoCommits(boolean flag) {}

public synchronized void setEmptyStringsConvertToZero(boolean flag) {}

public synchronized void setEmulateLocators(boolean property) {}

public synchronized void setEmulateUnsupportedPstmts(boolean flag) {}

public synchronized void setEnablePacketDebug(boolean flag) {}

public synchronized void setEnableQueryTimeouts(boolean flag) {}

public synchronized void setEncoding(String property) {}

public synchronized void setExplainSlowQueries(boolean flag) {}

public synchronized void setFailOverReadOnly(boolean flag) {}

public synchronized void setFunctionsNeverReturnBlobs(boolean flag) {}

public synchronized void setGatherPerfMetrics(boolean flag) {}

public synchronized void setGatherPerformanceMetrics(boolean flag) {}

public synchronized void setGenerateSimpleParameterMetadata(boolean flag) {}

public synchronized void setHoldResultsOpenOverStatementClose(boolean flag) {}

public synchronized void setIgnoreNonTxTables(boolean property) {}

public synchronized void setIncludeInnodbStatusInDeadlockExceptions(boolean flag) {}

public synchronized void setInitialTimeout(int property) {}

public synchronized void setInteractiveClient(boolean property) {}

public synchronized void setIsInteractiveClient(boolean property) {}

public synchronized void setJdbcCompliantTruncation(boolean flag) {}

public synchronized void setJdbcCompliantTruncationForReads(boolean jdbcCompliantTruncationForReads) {}

public synchronized void setLargeRowSizeThreshold(String value) {}

public synchronized void setLoadBalanceStrategy(String strategy) {}

public synchronized void setLocalSocketAddress(String address) {}

public synchronized void setLocatorFetchBufferSize(String value) throws SQLException {}

public synchronized void setLogSlowQueries(boolean flag) {}

public synchronized void setLogXaCommands(boolean flag) {}

public synchronized void setLogger(String property) {}

public synchronized void setLoggerClassName(String className) {}

public synchronized void setMaintainTimeStats(boolean flag) {}

public synchronized void setMaxQuerySizeToLog(int sizeInBytes) {}

public synchronized void setMaxReconnects(int property) {}

public synchronized void setMaxRows(int property) {}

public synchronized void setMetadataCacheSize(int value) {}

public synchronized void setNetTimeoutForStreamingResults(int value) {}

public synchronized void setNoAccessToProcedureBodies(boolean flag) {}

public synchronized void setNoDatetimeStringSync(boolean flag) {}

public synchronized void setNoTimezoneConversionForTimeType(boolean flag) {}

public synchronized void setNullCatalogMeansCurrent(boolean value) {}

public synchronized void setNullNamePatternMatchesAll(boolean value) {}

public synchronized void setOverrideSupportsIntegrityEnhancementFacility(boolean flag) {}

public synchronized void setPacketDebugBufferSize(int size) {}

public synchronized void setPadCharsWithSpace(boolean flag) {}

public synchronized void setParanoid(boolean property) {}

public synchronized void setPedantic(boolean property) {}

public synchronized void setPinGlobalTxToPhysicalConnection(boolean flag) {}

public synchronized void setPopulateInsertRowWithDefaultValues(boolean flag) {}

public synchronized void setPrepStmtCacheSize(int cacheSize) {}

public synchronized void setPrepStmtCacheSqlLimit(int sqlLimit) {}

public synchronized void setPreparedStatementCacheSize(int cacheSize) {}

public synchronized void setPreparedStatementCacheSqlLimit(int cacheSqlLimit) {}

public synchronized void setProcessEscapeCodesForPrepStmts(boolean flag) {}

public synchronized void setProfileSQL(boolean flag) {}

public synchronized void setProfileSql(boolean property) {}

public synchronized void setProfilerEventHandler(String handler) {}

public synchronized void setPropertiesTransform(String value) {}

public synchronized void setQueriesBeforeRetryMaster(int property) {}

public synchronized void setReconnectAtTxEnd(boolean property) {}

public synchronized void setRelaxAutoCommit(boolean property) {}

public synchronized void setReportMetricsIntervalMillis(int millis) {}

public synchronized void setRequireSSL(boolean property) {}

public synchronized void setResourceId(String resourceId) {}

public synchronized void setResultSetSizeThreshold(int threshold) {}

public synchronized void setRetainStatementAfterResultSetClose(boolean flag) {}

public synchronized void setRewriteBatchedStatements(boolean flag) {}

public synchronized void setRollbackOnPooledClose(boolean flag) {}

public synchronized void setRoundRobinLoadBalance(boolean flag) {}

public synchronized void setRunningCTS13(boolean flag) {}

public synchronized void setSecondsBeforeRetryMaster(int property) {}

public synchronized void setSelfDestructOnPingMaxOperations(int maxOperations) {}

public synchronized void setSelfDestructOnPingSecondsLifetime(int seconds) {}

public synchronized void setServerTimezone(String property) {}

public synchronized void setSessionVariables(String variables) {}

public synchronized void setSlowQueryThresholdMillis(int millis) {}

public synchronized void setSlowQueryThresholdNanos(long nanos) {}

public synchronized void setSocketFactory(String name) {}

public synchronized void setSocketFactoryClassName(String property) {}

public synchronized void setSocketTimeout(int property) {}

public synchronized void setStatementInterceptors(String value) {}

public synchronized void setStrictFloatingPoint(boolean property) {}

public synchronized void setStrictUpdates(boolean property) {}

public synchronized void setTcpKeepAlive(boolean flag) {}

public synchronized void setTcpNoDelay(boolean flag) {}

public synchronized void setTcpRcvBuf(int bufSize) {}

public synchronized void setTcpSndBuf(int bufSize) {}

public synchronized void setTcpTrafficClass(int classFlags) {}

public synchronized void setTinyInt1isBit(boolean flag) {}

public synchronized void setTraceProtocol(boolean flag) {}

public synchronized void setTransformedBitIsBoolean(boolean flag) {}

public synchronized void setTreatUtilDateAsTimestamp(boolean flag) {}

public synchronized void setTrustCertificateKeyStorePassword(String value) {}

public synchronized void setTrustCertificateKeyStoreType(String value) {}

public synchronized void setTrustCertificateKeyStoreUrl(String value) {}

public synchronized void setUltraDevHack(boolean flag) {}

public synchronized void setUseBlobToStoreUTF8OutsideBMP(boolean flag) {}

public synchronized void setUseCompression(boolean property) {}

public synchronized void setUseConfigs(String configs) {}

public synchronized void setUseCursorFetch(boolean flag) {}

public synchronized void setUseDirectRowUnpack(boolean flag) {}

public synchronized void setUseDynamicCharsetInfo(boolean flag) {}

public synchronized void setUseFastDateParsing(boolean flag) {}

public synchronized void setUseFastIntParsing(boolean flag) {}

public synchronized void setUseGmtMillisForDatetimes(boolean flag) {}

public synchronized void setUseHostsInPrivileges(boolean property) {}

public synchronized void setUseInformationSchema(boolean flag) {}

public synchronized void setUseJDBCCompliantTimezoneShift(boolean flag) {}

public synchronized void setUseJvmCharsetConverters(boolean flag) {}

public synchronized void setUseLegacyDatetimeCode(boolean flag) {}

public synchronized void setUseLocalSessionState(boolean flag) {}

public synchronized void setUseNanosForElapsedTime(boolean flag) {}

public synchronized void setUseOldAliasMetadataBehavior(boolean flag) {}

public synchronized void setUseOldUTF8Behavior(boolean flag) {}

public synchronized void setUseOnlyServerErrorMessages(boolean flag) {}

public synchronized void setUseReadAheadInput(boolean flag) {}

public synchronized void setUseSSL(boolean property) {}

public synchronized void setUseSSPSCompatibleTimezoneShift(boolean flag) {}

public synchronized void setUseServerPrepStmts(boolean flag) {}

public synchronized void setUseServerPreparedStmts(boolean flag) {}

public synchronized void setUseSqlStateCodes(boolean flag) {}

public synchronized void setUseStreamLengthsInPrepStmts(boolean property) {}

public synchronized void setUseTimezone(boolean property) {}

public synchronized void setUseUltraDevWorkAround(boolean property) {}

public synchronized void setUseUnbufferedInput(boolean flag) {}

public synchronized void setUseUnicode(boolean flag) {}

public synchronized void setUseUsageAdvisor(boolean useUsageAdvisorFlag) {}

public synchronized void setUtf8OutsideBmpExcludedColumnNamePattern(String regexPattern) {}

public synchronized void setUtf8OutsideBmpIncludedColumnNamePattern(String regexPattern) {}

public synchronized void setVerifyServerCertificate(boolean flag) {}

public synchronized void setYearIsDateType(boolean flag) {}

public synchronized void setZeroDateTimeBehavior(String behavior) {}

public synchronized boolean useUnbufferedInput() {
return this.currentConnection.useUnbufferedInput();
}

public synchronized boolean isSameResource(Connection c) {
return this.currentConnection.isSameResource(c);
}

public void setInGlobalTx(boolean flag) {
this.currentConnection.setInGlobalTx(flag);
}

public boolean getUseColumnNamesInFindColumn() {
return this.currentConnection.getUseColumnNamesInFindColumn();
}

public void setUseColumnNamesInFindColumn(boolean flag) {}

public boolean getUseLocalTransactionState() {
return this.currentConnection.getUseLocalTransactionState();
}

public void setUseLocalTransactionState(boolean flag) {}

public boolean getCompensateOnDuplicateKeyUpdateCounts() {
return this.currentConnection.getCompensateOnDuplicateKeyUpdateCounts();
}

public void setCompensateOnDuplicateKeyUpdateCounts(boolean flag) {}

public boolean getUseAffectedRows() {
return this.currentConnection.getUseAffectedRows();
}

public void setUseAffectedRows(boolean flag) {}

public String getPasswordCharacterEncoding() {
return this.currentConnection.getPasswordCharacterEncoding();
}

public void setPasswordCharacterEncoding(String characterSet) {
this.currentConnection.setPasswordCharacterEncoding(characterSet);
}

public int getAutoIncrementIncrement() {
return this.currentConnection.getAutoIncrementIncrement();
}

public int getLoadBalanceBlacklistTimeout() {
return this.currentConnection.getLoadBalanceBlacklistTimeout();
}

public void setLoadBalanceBlacklistTimeout(int loadBalanceBlacklistTimeout) {
this.currentConnection.setLoadBalanceBlacklistTimeout(loadBalanceBlacklistTimeout);
}

public int getLoadBalancePingTimeout() {
return this.currentConnection.getLoadBalancePingTimeout();
}

public void setLoadBalancePingTimeout(int loadBalancePingTimeout) {
this.currentConnection.setLoadBalancePingTimeout(loadBalancePingTimeout);
}

public boolean getLoadBalanceValidateConnectionOnSwapServer() {
return this.currentConnection.getLoadBalanceValidateConnectionOnSwapServer();
}

public void setLoadBalanceValidateConnectionOnSwapServer(boolean loadBalanceValidateConnectionOnSwapServer) {
this.currentConnection.setLoadBalanceValidateConnectionOnSwapServer(loadBalanceValidateConnectionOnSwapServer);
}

public int getRetriesAllDown() {
return this.currentConnection.getRetriesAllDown();
}

public void setRetriesAllDown(int retriesAllDown) {
this.currentConnection.setRetriesAllDown(retriesAllDown);
}

public ExceptionInterceptor getExceptionInterceptor() {
return this.currentConnection.getExceptionInterceptor();
}

public String getExceptionInterceptors() {
return this.currentConnection.getExceptionInterceptors();
}

public void setExceptionInterceptors(String exceptionInterceptors) {
this.currentConnection.setExceptionInterceptors(exceptionInterceptors);
}

public boolean getQueryTimeoutKillsConnection() {
return this.currentConnection.getQueryTimeoutKillsConnection();
}

public void setQueryTimeoutKillsConnection(boolean queryTimeoutKillsConnection) {
this.currentConnection.setQueryTimeoutKillsConnection(queryTimeoutKillsConnection);
}

public boolean hasSameProperties(Connection c) {
return (this.masterConnection.hasSameProperties(c) && this.slavesConnection.hasSameProperties(c));
}

public Properties getProperties() {
Properties props = new Properties();
props.putAll(this.masterConnection.getProperties());
props.putAll(this.slavesConnection.getProperties());

return props;
}

public String getHost() {
return this.currentConnection.getHost();
}

public void setProxy(MySQLConnection proxy) {
this.currentConnection.setProxy(proxy);
}

public synchronized boolean getRetainStatementAfterResultSetClose() {
return this.currentConnection.getRetainStatementAfterResultSetClose();
}

public int getMaxAllowedPacket() {
return this.currentConnection.getMaxAllowedPacket();
}

public String getLoadBalanceConnectionGroup() {
return this.currentConnection.getLoadBalanceConnectionGroup();
}

public boolean getLoadBalanceEnableJMX() {
return this.currentConnection.getLoadBalanceEnableJMX();
}

public String getLoadBalanceExceptionChecker() {
return this.currentConnection.getLoadBalanceExceptionChecker();
}

public String getLoadBalanceSQLExceptionSubclassFailover() {
return this.currentConnection.getLoadBalanceSQLExceptionSubclassFailover();
}

public String getLoadBalanceSQLStateFailover() {
return this.currentConnection.getLoadBalanceSQLStateFailover();
}

public void setLoadBalanceConnectionGroup(String loadBalanceConnectionGroup) {
this.currentConnection.setLoadBalanceConnectionGroup(loadBalanceConnectionGroup);
}

public void setLoadBalanceEnableJMX(boolean loadBalanceEnableJMX) {
this.currentConnection.setLoadBalanceEnableJMX(loadBalanceEnableJMX);
}

public void setLoadBalanceExceptionChecker(String loadBalanceExceptionChecker) {
this.currentConnection.setLoadBalanceExceptionChecker(loadBalanceExceptionChecker);
}

public void setLoadBalanceSQLExceptionSubclassFailover(String loadBalanceSQLExceptionSubclassFailover) {
this.currentConnection.setLoadBalanceSQLExceptionSubclassFailover(loadBalanceSQLExceptionSubclassFailover);
}

public void setLoadBalanceSQLStateFailover(String loadBalanceSQLStateFailover) {
this.currentConnection.setLoadBalanceSQLStateFailover(loadBalanceSQLStateFailover);
}

public String getLoadBalanceAutoCommitStatementRegex() {
return this.currentConnection.getLoadBalanceAutoCommitStatementRegex();
}

public int getLoadBalanceAutoCommitStatementThreshold() {
return this.currentConnection.getLoadBalanceAutoCommitStatementThreshold();
}

public void setLoadBalanceAutoCommitStatementRegex(String loadBalanceAutoCommitStatementRegex) {
this.currentConnection.setLoadBalanceAutoCommitStatementRegex(loadBalanceAutoCommitStatementRegex);
}

public void setLoadBalanceAutoCommitStatementThreshold(int loadBalanceAutoCommitStatementThreshold) {
this.currentConnection.setLoadBalanceAutoCommitStatementThreshold(loadBalanceAutoCommitStatementThreshold);
}

public void setTypeMap(Map<String, Class<?>> map) throws SQLException {}

public boolean getIncludeThreadDumpInDeadlockExceptions() {
return this.currentConnection.getIncludeThreadDumpInDeadlockExceptions();
}

public void setIncludeThreadDumpInDeadlockExceptions(boolean flag) {
this.currentConnection.setIncludeThreadDumpInDeadlockExceptions(flag);
}

public boolean getIncludeThreadNamesAsStatementComment() {
return this.currentConnection.getIncludeThreadNamesAsStatementComment();
}

public void setIncludeThreadNamesAsStatementComment(boolean flag) {
this.currentConnection.setIncludeThreadNamesAsStatementComment(flag);
}

public synchronized boolean isServerLocal() throws SQLException {
return this.currentConnection.isServerLocal();
}

public void setAuthenticationPlugins(String authenticationPlugins) {
this.currentConnection.setAuthenticationPlugins(authenticationPlugins);
}

public String getAuthenticationPlugins() {
return this.currentConnection.getAuthenticationPlugins();
}

public void setDisabledAuthenticationPlugins(String disabledAuthenticationPlugins) {
this.currentConnection.setDisabledAuthenticationPlugins(disabledAuthenticationPlugins);
}

public String getDisabledAuthenticationPlugins() {
return this.currentConnection.getDisabledAuthenticationPlugins();
}

public void setDefaultAuthenticationPlugin(String defaultAuthenticationPlugin) {
this.currentConnection.setDefaultAuthenticationPlugin(defaultAuthenticationPlugin);
}

public String getDefaultAuthenticationPlugin() {
return this.currentConnection.getDefaultAuthenticationPlugin();
}

public void setParseInfoCacheFactory(String factoryClassname) {
this.currentConnection.setParseInfoCacheFactory(factoryClassname);
}

public String getParseInfoCacheFactory() {
return this.currentConnection.getParseInfoCacheFactory();
}

public void setSchema(String schema) throws SQLException {
this.currentConnection.setSchema(schema);
}

public String getSchema() throws SQLException {
return this.currentConnection.getSchema();
}

public void abort(Executor executor) throws SQLException {
this.currentConnection.abort(executor);
}

public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
this.currentConnection.setNetworkTimeout(executor, milliseconds);
}

public int getNetworkTimeout() throws SQLException {
return this.currentConnection.getNetworkTimeout();
}

public void setServerConfigCacheFactory(String factoryClassname) {
this.currentConnection.setServerConfigCacheFactory(factoryClassname);
}

public String getServerConfigCacheFactory() {
return this.currentConnection.getServerConfigCacheFactory();
}

public void setDisconnectOnExpiredPasswords(boolean disconnectOnExpiredPasswords) {
this.currentConnection.setDisconnectOnExpiredPasswords(disconnectOnExpiredPasswords);
}

public boolean getDisconnectOnExpiredPasswords() {
return this.currentConnection.getDisconnectOnExpiredPasswords();
}
}

