package com.mysql.jdbc;

import com.mysql.jdbc.log.Log;

import java.sql.*;
import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.Executor;

public class LoadBalancedMySQLConnection
        implements MySQLConnection {
    protected LoadBalancingConnectionProxy proxy;

    public LoadBalancedMySQLConnection(LoadBalancingConnectionProxy proxy) {
        this.proxy = proxy;
    }

    public LoadBalancingConnectionProxy getProxy() {
        return this.proxy;
    }

    public void setProxy(MySQLConnection proxy) {
        getActiveMySQLConnection().setProxy(proxy);
    }

    protected synchronized MySQLConnection getActiveMySQLConnection() {
        return this.proxy.currentConn;
    }

    public void abortInternal() throws SQLException {
        getActiveMySQLConnection().abortInternal();
    }

    public void changeUser(String userName, String newPassword) throws SQLException {
        getActiveMySQLConnection().changeUser(userName, newPassword);
    }

    public void checkClosed() throws SQLException {
        getActiveMySQLConnection().checkClosed();
    }

    public void clearHasTriedMaster() {
        getActiveMySQLConnection().clearHasTriedMaster();
    }

    public void clearWarnings() throws SQLException {
        getActiveMySQLConnection().clearWarnings();
    }

    public PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getActiveMySQLConnection().clientPrepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getActiveMySQLConnection().clientPrepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    public PreparedStatement clientPrepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
        return getActiveMySQLConnection().clientPrepareStatement(sql, autoGenKeyIndex);
    }

    public PreparedStatement clientPrepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
        return getActiveMySQLConnection().clientPrepareStatement(sql, autoGenKeyIndexes);
    }

    public PreparedStatement clientPrepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
        return getActiveMySQLConnection().clientPrepareStatement(sql, autoGenKeyColNames);
    }

    public PreparedStatement clientPrepareStatement(String sql) throws SQLException {
        return getActiveMySQLConnection().clientPrepareStatement(sql);
    }

    public synchronized void close() throws SQLException {
        getActiveMySQLConnection().close();
    }

    public void commit() throws SQLException {
        getActiveMySQLConnection().commit();
    }

    public void createNewIO(boolean isForReconnect) throws SQLException {
        getActiveMySQLConnection().createNewIO(isForReconnect);
    }

    public Statement createStatement() throws SQLException {
        return getActiveMySQLConnection().createStatement();
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getActiveMySQLConnection().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return getActiveMySQLConnection().createStatement(resultSetType, resultSetConcurrency);
    }

    public void dumpTestcaseQuery(String query) {
        getActiveMySQLConnection().dumpTestcaseQuery(query);
    }

    public Connection duplicate() throws SQLException {
        return getActiveMySQLConnection().duplicate();
    }

    public ResultSetInternalMethods execSQL(StatementImpl callingStatement, String sql, int maxRows, Buffer packet, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Field[] cachedMetadata, boolean isBatch) throws SQLException {
        return getActiveMySQLConnection().execSQL(callingStatement, sql, maxRows, packet, resultSetType, resultSetConcurrency, streamResults, catalog, cachedMetadata, isBatch);
    }

    public ResultSetInternalMethods execSQL(StatementImpl callingStatement, String sql, int maxRows, Buffer packet, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Field[] cachedMetadata) throws SQLException {
        return getActiveMySQLConnection().execSQL(callingStatement, sql, maxRows, packet, resultSetType, resultSetConcurrency, streamResults, catalog, cachedMetadata);
    }

    public String extractSqlFromPacket(String possibleSqlQuery, Buffer queryPacket, int endOfQueryPacketPosition) throws SQLException {
        return getActiveMySQLConnection().extractSqlFromPacket(possibleSqlQuery, queryPacket, endOfQueryPacketPosition);
    }

    public String exposeAsXml() throws SQLException {
        return getActiveMySQLConnection().exposeAsXml();
    }

    public boolean getAllowLoadLocalInfile() {
        return getActiveMySQLConnection().getAllowLoadLocalInfile();
    }

    public void setAllowLoadLocalInfile(boolean property) {
        getActiveMySQLConnection().setAllowLoadLocalInfile(property);
    }

    public boolean getAllowMultiQueries() {
        return getActiveMySQLConnection().getAllowMultiQueries();
    }

    public void setAllowMultiQueries(boolean property) {
        getActiveMySQLConnection().setAllowMultiQueries(property);
    }

    public boolean getAllowNanAndInf() {
        return getActiveMySQLConnection().getAllowNanAndInf();
    }

    public void setAllowNanAndInf(boolean flag) {
        getActiveMySQLConnection().setAllowNanAndInf(flag);
    }

    public boolean getAllowUrlInLocalInfile() {
        return getActiveMySQLConnection().getAllowUrlInLocalInfile();
    }

    public void setAllowUrlInLocalInfile(boolean flag) {
        getActiveMySQLConnection().setAllowUrlInLocalInfile(flag);
    }

    public boolean getAlwaysSendSetIsolation() {
        return getActiveMySQLConnection().getAlwaysSendSetIsolation();
    }

    public void setAlwaysSendSetIsolation(boolean flag) {
        getActiveMySQLConnection().setAlwaysSendSetIsolation(flag);
    }

    public boolean getAutoClosePStmtStreams() {
        return getActiveMySQLConnection().getAutoClosePStmtStreams();
    }

    public void setAutoClosePStmtStreams(boolean flag) {
        getActiveMySQLConnection().setAutoClosePStmtStreams(flag);
    }

    public boolean getAutoDeserialize() {
        return getActiveMySQLConnection().getAutoDeserialize();
    }

    public void setAutoDeserialize(boolean flag) {
        getActiveMySQLConnection().setAutoDeserialize(flag);
    }

    public boolean getAutoGenerateTestcaseScript() {
        return getActiveMySQLConnection().getAutoGenerateTestcaseScript();
    }

    public void setAutoGenerateTestcaseScript(boolean flag) {
        getActiveMySQLConnection().setAutoGenerateTestcaseScript(flag);
    }

    public boolean getAutoReconnectForPools() {
        return getActiveMySQLConnection().getAutoReconnectForPools();
    }

    public void setAutoReconnectForPools(boolean flag) {
        getActiveMySQLConnection().setAutoReconnectForPools(flag);
    }

    public boolean getAutoSlowLog() {
        return getActiveMySQLConnection().getAutoSlowLog();
    }

    public void setAutoSlowLog(boolean flag) {
        getActiveMySQLConnection().setAutoSlowLog(flag);
    }

    public int getBlobSendChunkSize() {
        return getActiveMySQLConnection().getBlobSendChunkSize();
    }

    public void setBlobSendChunkSize(String value) throws SQLException {
        getActiveMySQLConnection().setBlobSendChunkSize(value);
    }

    public boolean getBlobsAreStrings() {
        return getActiveMySQLConnection().getBlobsAreStrings();
    }

    public void setBlobsAreStrings(boolean flag) {
        getActiveMySQLConnection().setBlobsAreStrings(flag);
    }

    public boolean getCacheCallableStatements() {
        return getActiveMySQLConnection().getCacheCallableStatements();
    }

    public void setCacheCallableStatements(boolean flag) {
        getActiveMySQLConnection().setCacheCallableStatements(flag);
    }

    public boolean getCacheCallableStmts() {
        return getActiveMySQLConnection().getCacheCallableStmts();
    }

    public void setCacheCallableStmts(boolean flag) {
        getActiveMySQLConnection().setCacheCallableStmts(flag);
    }

    public boolean getCachePrepStmts() {
        return getActiveMySQLConnection().getCachePrepStmts();
    }

    public void setCachePrepStmts(boolean flag) {
        getActiveMySQLConnection().setCachePrepStmts(flag);
    }

    public boolean getCachePreparedStatements() {
        return getActiveMySQLConnection().getCachePreparedStatements();
    }

    public void setCachePreparedStatements(boolean flag) {
        getActiveMySQLConnection().setCachePreparedStatements(flag);
    }

    public boolean getCacheResultSetMetadata() {
        return getActiveMySQLConnection().getCacheResultSetMetadata();
    }

    public void setCacheResultSetMetadata(boolean property) {
        getActiveMySQLConnection().setCacheResultSetMetadata(property);
    }

    public boolean getCacheServerConfiguration() {
        return getActiveMySQLConnection().getCacheServerConfiguration();
    }

    public void setCacheServerConfiguration(boolean flag) {
        getActiveMySQLConnection().setCacheServerConfiguration(flag);
    }

    public int getCallableStatementCacheSize() {
        return getActiveMySQLConnection().getCallableStatementCacheSize();
    }

    public void setCallableStatementCacheSize(int size) {
        getActiveMySQLConnection().setCallableStatementCacheSize(size);
    }

    public int getCallableStmtCacheSize() {
        return getActiveMySQLConnection().getCallableStmtCacheSize();
    }

    public void setCallableStmtCacheSize(int cacheSize) {
        getActiveMySQLConnection().setCallableStmtCacheSize(cacheSize);
    }

    public boolean getCapitalizeTypeNames() {
        return getActiveMySQLConnection().getCapitalizeTypeNames();
    }

    public void setCapitalizeTypeNames(boolean flag) {
        getActiveMySQLConnection().setCapitalizeTypeNames(flag);
    }

    public String getCharacterSetResults() {
        return getActiveMySQLConnection().getCharacterSetResults();
    }

    public void setCharacterSetResults(String characterSet) {
        getActiveMySQLConnection().setCharacterSetResults(characterSet);
    }

    public String getClientCertificateKeyStorePassword() {
        return getActiveMySQLConnection().getClientCertificateKeyStorePassword();
    }

    public void setClientCertificateKeyStorePassword(String value) {
        getActiveMySQLConnection().setClientCertificateKeyStorePassword(value);
    }

    public String getClientCertificateKeyStoreType() {
        return getActiveMySQLConnection().getClientCertificateKeyStoreType();
    }

    public void setClientCertificateKeyStoreType(String value) {
        getActiveMySQLConnection().setClientCertificateKeyStoreType(value);
    }

    public String getClientCertificateKeyStoreUrl() {
        return getActiveMySQLConnection().getClientCertificateKeyStoreUrl();
    }

    public void setClientCertificateKeyStoreUrl(String value) {
        getActiveMySQLConnection().setClientCertificateKeyStoreUrl(value);
    }

    public String getClientInfoProvider() {
        return getActiveMySQLConnection().getClientInfoProvider();
    }

    public void setClientInfoProvider(String classname) {
        getActiveMySQLConnection().setClientInfoProvider(classname);
    }

    public String getClobCharacterEncoding() {
        return getActiveMySQLConnection().getClobCharacterEncoding();
    }

    public void setClobCharacterEncoding(String encoding) {
        getActiveMySQLConnection().setClobCharacterEncoding(encoding);
    }

    public boolean getClobberStreamingResults() {
        return getActiveMySQLConnection().getClobberStreamingResults();
    }

    public void setClobberStreamingResults(boolean flag) {
        getActiveMySQLConnection().setClobberStreamingResults(flag);
    }

    public boolean getCompensateOnDuplicateKeyUpdateCounts() {
        return getActiveMySQLConnection().getCompensateOnDuplicateKeyUpdateCounts();
    }

    public void setCompensateOnDuplicateKeyUpdateCounts(boolean flag) {
        getActiveMySQLConnection().setCompensateOnDuplicateKeyUpdateCounts(flag);
    }

    public int getConnectTimeout() {
        return getActiveMySQLConnection().getConnectTimeout();
    }

    public void setConnectTimeout(int timeoutMs) {
        getActiveMySQLConnection().setConnectTimeout(timeoutMs);
    }

    public String getConnectionCollation() {
        return getActiveMySQLConnection().getConnectionCollation();
    }

    public void setConnectionCollation(String collation) {
        getActiveMySQLConnection().setConnectionCollation(collation);
    }

    public String getConnectionLifecycleInterceptors() {
        return getActiveMySQLConnection().getConnectionLifecycleInterceptors();
    }

    public void setConnectionLifecycleInterceptors(String interceptors) {
        getActiveMySQLConnection().setConnectionLifecycleInterceptors(interceptors);
    }

    public boolean getContinueBatchOnError() {
        return getActiveMySQLConnection().getContinueBatchOnError();
    }

    public void setContinueBatchOnError(boolean property) {
        getActiveMySQLConnection().setContinueBatchOnError(property);
    }

    public boolean getCreateDatabaseIfNotExist() {
        return getActiveMySQLConnection().getCreateDatabaseIfNotExist();
    }

    public void setCreateDatabaseIfNotExist(boolean flag) {
        getActiveMySQLConnection().setCreateDatabaseIfNotExist(flag);
    }

    public int getDefaultFetchSize() {
        return getActiveMySQLConnection().getDefaultFetchSize();
    }

    public void setDefaultFetchSize(int n) {
        getActiveMySQLConnection().setDefaultFetchSize(n);
    }

    public boolean getDontTrackOpenResources() {
        return getActiveMySQLConnection().getDontTrackOpenResources();
    }

    public void setDontTrackOpenResources(boolean flag) {
        getActiveMySQLConnection().setDontTrackOpenResources(flag);
    }

    public boolean getDumpMetadataOnColumnNotFound() {
        return getActiveMySQLConnection().getDumpMetadataOnColumnNotFound();
    }

    public void setDumpMetadataOnColumnNotFound(boolean flag) {
        getActiveMySQLConnection().setDumpMetadataOnColumnNotFound(flag);
    }

    public boolean getDumpQueriesOnException() {
        return getActiveMySQLConnection().getDumpQueriesOnException();
    }

    public void setDumpQueriesOnException(boolean flag) {
        getActiveMySQLConnection().setDumpQueriesOnException(flag);
    }

    public boolean getDynamicCalendars() {
        return getActiveMySQLConnection().getDynamicCalendars();
    }

    public void setDynamicCalendars(boolean flag) {
        getActiveMySQLConnection().setDynamicCalendars(flag);
    }

    public boolean getElideSetAutoCommits() {
        return getActiveMySQLConnection().getElideSetAutoCommits();
    }

    public void setElideSetAutoCommits(boolean flag) {
        getActiveMySQLConnection().setElideSetAutoCommits(flag);
    }

    public boolean getEmptyStringsConvertToZero() {
        return getActiveMySQLConnection().getEmptyStringsConvertToZero();
    }

    public void setEmptyStringsConvertToZero(boolean flag) {
        getActiveMySQLConnection().setEmptyStringsConvertToZero(flag);
    }

    public boolean getEmulateLocators() {
        return getActiveMySQLConnection().getEmulateLocators();
    }

    public void setEmulateLocators(boolean property) {
        getActiveMySQLConnection().setEmulateLocators(property);
    }

    public boolean getEmulateUnsupportedPstmts() {
        return getActiveMySQLConnection().getEmulateUnsupportedPstmts();
    }

    public void setEmulateUnsupportedPstmts(boolean flag) {
        getActiveMySQLConnection().setEmulateUnsupportedPstmts(flag);
    }

    public boolean getEnablePacketDebug() {
        return getActiveMySQLConnection().getEnablePacketDebug();
    }

    public void setEnablePacketDebug(boolean flag) {
        getActiveMySQLConnection().setEnablePacketDebug(flag);
    }

    public boolean getEnableQueryTimeouts() {
        return getActiveMySQLConnection().getEnableQueryTimeouts();
    }

    public void setEnableQueryTimeouts(boolean flag) {
        getActiveMySQLConnection().setEnableQueryTimeouts(flag);
    }

    public String getEncoding() {
        return getActiveMySQLConnection().getEncoding();
    }

    public void setEncoding(String property) {
        getActiveMySQLConnection().setEncoding(property);
    }

    public String getExceptionInterceptors() {
        return getActiveMySQLConnection().getExceptionInterceptors();
    }

    public void setExceptionInterceptors(String exceptionInterceptors) {
        getActiveMySQLConnection().setExceptionInterceptors(exceptionInterceptors);
    }

    public boolean getExplainSlowQueries() {
        return getActiveMySQLConnection().getExplainSlowQueries();
    }

    public void setExplainSlowQueries(boolean flag) {
        getActiveMySQLConnection().setExplainSlowQueries(flag);
    }

    public boolean getFailOverReadOnly() {
        return getActiveMySQLConnection().getFailOverReadOnly();
    }

    public void setFailOverReadOnly(boolean flag) {
        getActiveMySQLConnection().setFailOverReadOnly(flag);
    }

    public boolean getFunctionsNeverReturnBlobs() {
        return getActiveMySQLConnection().getFunctionsNeverReturnBlobs();
    }

    public void setFunctionsNeverReturnBlobs(boolean flag) {
        getActiveMySQLConnection().setFunctionsNeverReturnBlobs(flag);
    }

    public boolean getGatherPerfMetrics() {
        return getActiveMySQLConnection().getGatherPerfMetrics();
    }

    public void setGatherPerfMetrics(boolean flag) {
        getActiveMySQLConnection().setGatherPerfMetrics(flag);
    }

    public boolean getGatherPerformanceMetrics() {
        return getActiveMySQLConnection().getGatherPerformanceMetrics();
    }

    public void setGatherPerformanceMetrics(boolean flag) {
        getActiveMySQLConnection().setGatherPerformanceMetrics(flag);
    }

    public boolean getGenerateSimpleParameterMetadata() {
        return getActiveMySQLConnection().getGenerateSimpleParameterMetadata();
    }

    public void setGenerateSimpleParameterMetadata(boolean flag) {
        getActiveMySQLConnection().setGenerateSimpleParameterMetadata(flag);
    }

    public boolean getIgnoreNonTxTables() {
        return getActiveMySQLConnection().getIgnoreNonTxTables();
    }

    public void setIgnoreNonTxTables(boolean property) {
        getActiveMySQLConnection().setIgnoreNonTxTables(property);
    }

    public boolean getIncludeInnodbStatusInDeadlockExceptions() {
        return getActiveMySQLConnection().getIncludeInnodbStatusInDeadlockExceptions();
    }

    public void setIncludeInnodbStatusInDeadlockExceptions(boolean flag) {
        getActiveMySQLConnection().setIncludeInnodbStatusInDeadlockExceptions(flag);
    }

    public int getInitialTimeout() {
        return getActiveMySQLConnection().getInitialTimeout();
    }

    public void setInitialTimeout(int property) {
        getActiveMySQLConnection().setInitialTimeout(property);
    }

    public boolean getInteractiveClient() {
        return getActiveMySQLConnection().getInteractiveClient();
    }

    public void setInteractiveClient(boolean property) {
        getActiveMySQLConnection().setInteractiveClient(property);
    }

    public boolean getIsInteractiveClient() {
        return getActiveMySQLConnection().getIsInteractiveClient();
    }

    public void setIsInteractiveClient(boolean property) {
        getActiveMySQLConnection().setIsInteractiveClient(property);
    }

    public boolean getJdbcCompliantTruncation() {
        return getActiveMySQLConnection().getJdbcCompliantTruncation();
    }

    public void setJdbcCompliantTruncation(boolean flag) {
        getActiveMySQLConnection().setJdbcCompliantTruncation(flag);
    }

    public boolean getJdbcCompliantTruncationForReads() {
        return getActiveMySQLConnection().getJdbcCompliantTruncationForReads();
    }

    public void setJdbcCompliantTruncationForReads(boolean jdbcCompliantTruncationForReads) {
        getActiveMySQLConnection().setJdbcCompliantTruncationForReads(jdbcCompliantTruncationForReads);
    }

    public String getLargeRowSizeThreshold() {
        return getActiveMySQLConnection().getLargeRowSizeThreshold();
    }

    public void setLargeRowSizeThreshold(String value) {
        getActiveMySQLConnection().setLargeRowSizeThreshold(value);
    }

    public int getLoadBalanceBlacklistTimeout() {
        return getActiveMySQLConnection().getLoadBalanceBlacklistTimeout();
    }

    public void setLoadBalanceBlacklistTimeout(int loadBalanceBlacklistTimeout) {
        getActiveMySQLConnection().setLoadBalanceBlacklistTimeout(loadBalanceBlacklistTimeout);
    }

    public int getLoadBalancePingTimeout() {
        return getActiveMySQLConnection().getLoadBalancePingTimeout();
    }

    public void setLoadBalancePingTimeout(int loadBalancePingTimeout) {
        getActiveMySQLConnection().setLoadBalancePingTimeout(loadBalancePingTimeout);
    }

    public String getLoadBalanceStrategy() {
        return getActiveMySQLConnection().getLoadBalanceStrategy();
    }

    public void setLoadBalanceStrategy(String strategy) {
        getActiveMySQLConnection().setLoadBalanceStrategy(strategy);
    }

    public boolean getLoadBalanceValidateConnectionOnSwapServer() {
        return getActiveMySQLConnection().getLoadBalanceValidateConnectionOnSwapServer();
    }

    public void setLoadBalanceValidateConnectionOnSwapServer(boolean loadBalanceValidateConnectionOnSwapServer) {
        getActiveMySQLConnection().setLoadBalanceValidateConnectionOnSwapServer(loadBalanceValidateConnectionOnSwapServer);
    }

    public String getLocalSocketAddress() {
        return getActiveMySQLConnection().getLocalSocketAddress();
    }

    public void setLocalSocketAddress(String address) {
        getActiveMySQLConnection().setLocalSocketAddress(address);
    }

    public int getLocatorFetchBufferSize() {
        return getActiveMySQLConnection().getLocatorFetchBufferSize();
    }

    public void setLocatorFetchBufferSize(String value) throws SQLException {
        getActiveMySQLConnection().setLocatorFetchBufferSize(value);
    }

    public boolean getLogSlowQueries() {
        return getActiveMySQLConnection().getLogSlowQueries();
    }

    public void setLogSlowQueries(boolean flag) {
        getActiveMySQLConnection().setLogSlowQueries(flag);
    }

    public boolean getLogXaCommands() {
        return getActiveMySQLConnection().getLogXaCommands();
    }

    public void setLogXaCommands(boolean flag) {
        getActiveMySQLConnection().setLogXaCommands(flag);
    }

    public String getLogger() {
        return getActiveMySQLConnection().getLogger();
    }

    public void setLogger(String property) {
        getActiveMySQLConnection().setLogger(property);
    }

    public String getLoggerClassName() {
        return getActiveMySQLConnection().getLoggerClassName();
    }

    public void setLoggerClassName(String className) {
        getActiveMySQLConnection().setLoggerClassName(className);
    }

    public boolean getMaintainTimeStats() {
        return getActiveMySQLConnection().getMaintainTimeStats();
    }

    public void setMaintainTimeStats(boolean flag) {
        getActiveMySQLConnection().setMaintainTimeStats(flag);
    }

    public int getMaxAllowedPacket() {
        return getActiveMySQLConnection().getMaxAllowedPacket();
    }

    public int getMaxQuerySizeToLog() {
        return getActiveMySQLConnection().getMaxQuerySizeToLog();
    }

    public void setMaxQuerySizeToLog(int sizeInBytes) {
        getActiveMySQLConnection().setMaxQuerySizeToLog(sizeInBytes);
    }

    public int getMaxReconnects() {
        return getActiveMySQLConnection().getMaxReconnects();
    }

    public void setMaxReconnects(int property) {
        getActiveMySQLConnection().setMaxReconnects(property);
    }

    public int getMaxRows() {
        return getActiveMySQLConnection().getMaxRows();
    }

    public void setMaxRows(int property) {
        getActiveMySQLConnection().setMaxRows(property);
    }

    public int getMetadataCacheSize() {
        return getActiveMySQLConnection().getMetadataCacheSize();
    }

    public void setMetadataCacheSize(int value) {
        getActiveMySQLConnection().setMetadataCacheSize(value);
    }

    public int getNetTimeoutForStreamingResults() {
        return getActiveMySQLConnection().getNetTimeoutForStreamingResults();
    }

    public void setNetTimeoutForStreamingResults(int value) {
        getActiveMySQLConnection().setNetTimeoutForStreamingResults(value);
    }

    public boolean getNoAccessToProcedureBodies() {
        return getActiveMySQLConnection().getNoAccessToProcedureBodies();
    }

    public void setNoAccessToProcedureBodies(boolean flag) {
        getActiveMySQLConnection().setNoAccessToProcedureBodies(flag);
    }

    public boolean getNoDatetimeStringSync() {
        return getActiveMySQLConnection().getNoDatetimeStringSync();
    }

    public void setNoDatetimeStringSync(boolean flag) {
        getActiveMySQLConnection().setNoDatetimeStringSync(flag);
    }

    public boolean getNoTimezoneConversionForTimeType() {
        return getActiveMySQLConnection().getNoTimezoneConversionForTimeType();
    }

    public void setNoTimezoneConversionForTimeType(boolean flag) {
        getActiveMySQLConnection().setNoTimezoneConversionForTimeType(flag);
    }

    public boolean getNullCatalogMeansCurrent() {
        return getActiveMySQLConnection().getNullCatalogMeansCurrent();
    }

    public void setNullCatalogMeansCurrent(boolean value) {
        getActiveMySQLConnection().setNullCatalogMeansCurrent(value);
    }

    public boolean getNullNamePatternMatchesAll() {
        return getActiveMySQLConnection().getNullNamePatternMatchesAll();
    }

    public void setNullNamePatternMatchesAll(boolean value) {
        getActiveMySQLConnection().setNullNamePatternMatchesAll(value);
    }

    public boolean getOverrideSupportsIntegrityEnhancementFacility() {
        return getActiveMySQLConnection().getOverrideSupportsIntegrityEnhancementFacility();
    }

    public void setOverrideSupportsIntegrityEnhancementFacility(boolean flag) {
        getActiveMySQLConnection().setOverrideSupportsIntegrityEnhancementFacility(flag);
    }

    public int getPacketDebugBufferSize() {
        return getActiveMySQLConnection().getPacketDebugBufferSize();
    }

    public void setPacketDebugBufferSize(int size) {
        getActiveMySQLConnection().setPacketDebugBufferSize(size);
    }

    public boolean getPadCharsWithSpace() {
        return getActiveMySQLConnection().getPadCharsWithSpace();
    }

    public void setPadCharsWithSpace(boolean flag) {
        getActiveMySQLConnection().setPadCharsWithSpace(flag);
    }

    public boolean getParanoid() {
        return getActiveMySQLConnection().getParanoid();
    }

    public void setParanoid(boolean property) {
        getActiveMySQLConnection().setParanoid(property);
    }

    public String getPasswordCharacterEncoding() {
        return getActiveMySQLConnection().getPasswordCharacterEncoding();
    }

    public void setPasswordCharacterEncoding(String characterSet) {
        getActiveMySQLConnection().setPasswordCharacterEncoding(characterSet);
    }

    public boolean getPedantic() {
        return getActiveMySQLConnection().getPedantic();
    }

    public void setPedantic(boolean property) {
        getActiveMySQLConnection().setPedantic(property);
    }

    public boolean getPinGlobalTxToPhysicalConnection() {
        return getActiveMySQLConnection().getPinGlobalTxToPhysicalConnection();
    }

    public void setPinGlobalTxToPhysicalConnection(boolean flag) {
        getActiveMySQLConnection().setPinGlobalTxToPhysicalConnection(flag);
    }

    public boolean getPopulateInsertRowWithDefaultValues() {
        return getActiveMySQLConnection().getPopulateInsertRowWithDefaultValues();
    }

    public void setPopulateInsertRowWithDefaultValues(boolean flag) {
        getActiveMySQLConnection().setPopulateInsertRowWithDefaultValues(flag);
    }

    public int getPrepStmtCacheSize() {
        return getActiveMySQLConnection().getPrepStmtCacheSize();
    }

    public void setPrepStmtCacheSize(int cacheSize) {
        getActiveMySQLConnection().setPrepStmtCacheSize(cacheSize);
    }

    public int getPrepStmtCacheSqlLimit() {
        return getActiveMySQLConnection().getPrepStmtCacheSqlLimit();
    }

    public void setPrepStmtCacheSqlLimit(int sqlLimit) {
        getActiveMySQLConnection().setPrepStmtCacheSqlLimit(sqlLimit);
    }

    public int getPreparedStatementCacheSize() {
        return getActiveMySQLConnection().getPreparedStatementCacheSize();
    }

    public void setPreparedStatementCacheSize(int cacheSize) {
        getActiveMySQLConnection().setPreparedStatementCacheSize(cacheSize);
    }

    public int getPreparedStatementCacheSqlLimit() {
        return getActiveMySQLConnection().getPreparedStatementCacheSqlLimit();
    }

    public void setPreparedStatementCacheSqlLimit(int cacheSqlLimit) {
        getActiveMySQLConnection().setPreparedStatementCacheSqlLimit(cacheSqlLimit);
    }

    public boolean getProcessEscapeCodesForPrepStmts() {
        return getActiveMySQLConnection().getProcessEscapeCodesForPrepStmts();
    }

    public void setProcessEscapeCodesForPrepStmts(boolean flag) {
        getActiveMySQLConnection().setProcessEscapeCodesForPrepStmts(flag);
    }

    public boolean getProfileSQL() {
        return getActiveMySQLConnection().getProfileSQL();
    }

    public void setProfileSQL(boolean flag) {
        getActiveMySQLConnection().setProfileSQL(flag);
    }

    public boolean getProfileSql() {
        return getActiveMySQLConnection().getProfileSql();
    }

    public void setProfileSql(boolean property) {
        getActiveMySQLConnection().setProfileSql(property);
    }

    public String getProfilerEventHandler() {
        return getActiveMySQLConnection().getProfilerEventHandler();
    }

    public void setProfilerEventHandler(String handler) {
        getActiveMySQLConnection().setProfilerEventHandler(handler);
    }

    public String getPropertiesTransform() {
        return getActiveMySQLConnection().getPropertiesTransform();
    }

    public void setPropertiesTransform(String value) {
        getActiveMySQLConnection().setPropertiesTransform(value);
    }

    public int getQueriesBeforeRetryMaster() {
        return getActiveMySQLConnection().getQueriesBeforeRetryMaster();
    }

    public void setQueriesBeforeRetryMaster(int property) {
        getActiveMySQLConnection().setQueriesBeforeRetryMaster(property);
    }

    public boolean getQueryTimeoutKillsConnection() {
        return getActiveMySQLConnection().getQueryTimeoutKillsConnection();
    }

    public void setQueryTimeoutKillsConnection(boolean queryTimeoutKillsConnection) {
        getActiveMySQLConnection().setQueryTimeoutKillsConnection(queryTimeoutKillsConnection);
    }

    public boolean getReconnectAtTxEnd() {
        return getActiveMySQLConnection().getReconnectAtTxEnd();
    }

    public void setReconnectAtTxEnd(boolean property) {
        getActiveMySQLConnection().setReconnectAtTxEnd(property);
    }

    public boolean getRelaxAutoCommit() {
        return getActiveMySQLConnection().getRelaxAutoCommit();
    }

    public void setRelaxAutoCommit(boolean property) {
        getActiveMySQLConnection().setRelaxAutoCommit(property);
    }

    public int getReportMetricsIntervalMillis() {
        return getActiveMySQLConnection().getReportMetricsIntervalMillis();
    }

    public void setReportMetricsIntervalMillis(int millis) {
        getActiveMySQLConnection().setReportMetricsIntervalMillis(millis);
    }

    public boolean getRequireSSL() {
        return getActiveMySQLConnection().getRequireSSL();
    }

    public void setRequireSSL(boolean property) {
        getActiveMySQLConnection().setRequireSSL(property);
    }

    public String getResourceId() {
        return getActiveMySQLConnection().getResourceId();
    }

    public void setResourceId(String resourceId) {
        getActiveMySQLConnection().setResourceId(resourceId);
    }

    public int getResultSetSizeThreshold() {
        return getActiveMySQLConnection().getResultSetSizeThreshold();
    }

    public void setResultSetSizeThreshold(int threshold) {
        getActiveMySQLConnection().setResultSetSizeThreshold(threshold);
    }

    public boolean getRetainStatementAfterResultSetClose() {
        return getActiveMySQLConnection().getRetainStatementAfterResultSetClose();
    }

    public void setRetainStatementAfterResultSetClose(boolean flag) {
        getActiveMySQLConnection().setRetainStatementAfterResultSetClose(flag);
    }

    public int getRetriesAllDown() {
        return getActiveMySQLConnection().getRetriesAllDown();
    }

    public void setRetriesAllDown(int retriesAllDown) {
        getActiveMySQLConnection().setRetriesAllDown(retriesAllDown);
    }

    public boolean getRewriteBatchedStatements() {
        return getActiveMySQLConnection().getRewriteBatchedStatements();
    }

    public void setRewriteBatchedStatements(boolean flag) {
        getActiveMySQLConnection().setRewriteBatchedStatements(flag);
    }

    public boolean getRollbackOnPooledClose() {
        return getActiveMySQLConnection().getRollbackOnPooledClose();
    }

    public void setRollbackOnPooledClose(boolean flag) {
        getActiveMySQLConnection().setRollbackOnPooledClose(flag);
    }

    public boolean getRoundRobinLoadBalance() {
        return getActiveMySQLConnection().getRoundRobinLoadBalance();
    }

    public void setRoundRobinLoadBalance(boolean flag) {
        getActiveMySQLConnection().setRoundRobinLoadBalance(flag);
    }

    public boolean getRunningCTS13() {
        return getActiveMySQLConnection().getRunningCTS13();
    }

    public void setRunningCTS13(boolean flag) {
        getActiveMySQLConnection().setRunningCTS13(flag);
    }

    public int getSecondsBeforeRetryMaster() {
        return getActiveMySQLConnection().getSecondsBeforeRetryMaster();
    }

    public void setSecondsBeforeRetryMaster(int property) {
        getActiveMySQLConnection().setSecondsBeforeRetryMaster(property);
    }

    public int getSelfDestructOnPingMaxOperations() {
        return getActiveMySQLConnection().getSelfDestructOnPingMaxOperations();
    }

    public void setSelfDestructOnPingMaxOperations(int maxOperations) {
        getActiveMySQLConnection().setSelfDestructOnPingMaxOperations(maxOperations);
    }

    public int getSelfDestructOnPingSecondsLifetime() {
        return getActiveMySQLConnection().getSelfDestructOnPingSecondsLifetime();
    }

    public void setSelfDestructOnPingSecondsLifetime(int seconds) {
        getActiveMySQLConnection().setSelfDestructOnPingSecondsLifetime(seconds);
    }

    public String getServerTimezone() {
        return getActiveMySQLConnection().getServerTimezone();
    }

    public void setServerTimezone(String property) {
        getActiveMySQLConnection().setServerTimezone(property);
    }

    public String getSessionVariables() {
        return getActiveMySQLConnection().getSessionVariables();
    }

    public void setSessionVariables(String variables) {
        getActiveMySQLConnection().setSessionVariables(variables);
    }

    public int getSlowQueryThresholdMillis() {
        return getActiveMySQLConnection().getSlowQueryThresholdMillis();
    }

    public void setSlowQueryThresholdMillis(int millis) {
        getActiveMySQLConnection().setSlowQueryThresholdMillis(millis);
    }

    public long getSlowQueryThresholdNanos() {
        return getActiveMySQLConnection().getSlowQueryThresholdNanos();
    }

    public void setSlowQueryThresholdNanos(long nanos) {
        getActiveMySQLConnection().setSlowQueryThresholdNanos(nanos);
    }

    public String getSocketFactory() {
        return getActiveMySQLConnection().getSocketFactory();
    }

    public void setSocketFactory(String name) {
        getActiveMySQLConnection().setSocketFactory(name);
    }

    public String getSocketFactoryClassName() {
        return getActiveMySQLConnection().getSocketFactoryClassName();
    }

    public void setSocketFactoryClassName(String property) {
        getActiveMySQLConnection().setSocketFactoryClassName(property);
    }

    public int getSocketTimeout() {
        return getActiveMySQLConnection().getSocketTimeout();
    }

    public void setSocketTimeout(int property) {
        getActiveMySQLConnection().setSocketTimeout(property);
    }

    public String getStatementInterceptors() {
        return getActiveMySQLConnection().getStatementInterceptors();
    }

    public void setStatementInterceptors(String value) {
        getActiveMySQLConnection().setStatementInterceptors(value);
    }

    public boolean getStrictFloatingPoint() {
        return getActiveMySQLConnection().getStrictFloatingPoint();
    }

    public void setStrictFloatingPoint(boolean property) {
        getActiveMySQLConnection().setStrictFloatingPoint(property);
    }

    public boolean getStrictUpdates() {
        return getActiveMySQLConnection().getStrictUpdates();
    }

    public void setStrictUpdates(boolean property) {
        getActiveMySQLConnection().setStrictUpdates(property);
    }

    public boolean getTcpKeepAlive() {
        return getActiveMySQLConnection().getTcpKeepAlive();
    }

    public void setTcpKeepAlive(boolean flag) {
        getActiveMySQLConnection().setTcpKeepAlive(flag);
    }

    public boolean getTcpNoDelay() {
        return getActiveMySQLConnection().getTcpNoDelay();
    }

    public void setTcpNoDelay(boolean flag) {
        getActiveMySQLConnection().setTcpNoDelay(flag);
    }

    public int getTcpRcvBuf() {
        return getActiveMySQLConnection().getTcpRcvBuf();
    }

    public void setTcpRcvBuf(int bufSize) {
        getActiveMySQLConnection().setTcpRcvBuf(bufSize);
    }

    public int getTcpSndBuf() {
        return getActiveMySQLConnection().getTcpSndBuf();
    }

    public void setTcpSndBuf(int bufSize) {
        getActiveMySQLConnection().setTcpSndBuf(bufSize);
    }

    public int getTcpTrafficClass() {
        return getActiveMySQLConnection().getTcpTrafficClass();
    }

    public void setTcpTrafficClass(int classFlags) {
        getActiveMySQLConnection().setTcpTrafficClass(classFlags);
    }

    public boolean getTinyInt1isBit() {
        return getActiveMySQLConnection().getTinyInt1isBit();
    }

    public void setTinyInt1isBit(boolean flag) {
        getActiveMySQLConnection().setTinyInt1isBit(flag);
    }

    public boolean getTraceProtocol() {
        return getActiveMySQLConnection().getTraceProtocol();
    }

    public void setTraceProtocol(boolean flag) {
        getActiveMySQLConnection().setTraceProtocol(flag);
    }

    public boolean getTransformedBitIsBoolean() {
        return getActiveMySQLConnection().getTransformedBitIsBoolean();
    }

    public void setTransformedBitIsBoolean(boolean flag) {
        getActiveMySQLConnection().setTransformedBitIsBoolean(flag);
    }

    public boolean getTreatUtilDateAsTimestamp() {
        return getActiveMySQLConnection().getTreatUtilDateAsTimestamp();
    }

    public void setTreatUtilDateAsTimestamp(boolean flag) {
        getActiveMySQLConnection().setTreatUtilDateAsTimestamp(flag);
    }

    public String getTrustCertificateKeyStorePassword() {
        return getActiveMySQLConnection().getTrustCertificateKeyStorePassword();
    }

    public void setTrustCertificateKeyStorePassword(String value) {
        getActiveMySQLConnection().setTrustCertificateKeyStorePassword(value);
    }

    public String getTrustCertificateKeyStoreType() {
        return getActiveMySQLConnection().getTrustCertificateKeyStoreType();
    }

    public void setTrustCertificateKeyStoreType(String value) {
        getActiveMySQLConnection().setTrustCertificateKeyStoreType(value);
    }

    public String getTrustCertificateKeyStoreUrl() {
        return getActiveMySQLConnection().getTrustCertificateKeyStoreUrl();
    }

    public void setTrustCertificateKeyStoreUrl(String value) {
        getActiveMySQLConnection().setTrustCertificateKeyStoreUrl(value);
    }

    public boolean getUltraDevHack() {
        return getActiveMySQLConnection().getUltraDevHack();
    }

    public void setUltraDevHack(boolean flag) {
        getActiveMySQLConnection().setUltraDevHack(flag);
    }

    public boolean getUseAffectedRows() {
        return getActiveMySQLConnection().getUseAffectedRows();
    }

    public void setUseAffectedRows(boolean flag) {
        getActiveMySQLConnection().setUseAffectedRows(flag);
    }

    public boolean getUseBlobToStoreUTF8OutsideBMP() {
        return getActiveMySQLConnection().getUseBlobToStoreUTF8OutsideBMP();
    }

    public void setUseBlobToStoreUTF8OutsideBMP(boolean flag) {
        getActiveMySQLConnection().setUseBlobToStoreUTF8OutsideBMP(flag);
    }

    public boolean getUseColumnNamesInFindColumn() {
        return getActiveMySQLConnection().getUseColumnNamesInFindColumn();
    }

    public void setUseColumnNamesInFindColumn(boolean flag) {
        getActiveMySQLConnection().setUseColumnNamesInFindColumn(flag);
    }

    public boolean getUseCompression() {
        return getActiveMySQLConnection().getUseCompression();
    }

    public void setUseCompression(boolean property) {
        getActiveMySQLConnection().setUseCompression(property);
    }

    public String getUseConfigs() {
        return getActiveMySQLConnection().getUseConfigs();
    }

    public void setUseConfigs(String configs) {
        getActiveMySQLConnection().setUseConfigs(configs);
    }

    public boolean getUseCursorFetch() {
        return getActiveMySQLConnection().getUseCursorFetch();
    }

    public void setUseCursorFetch(boolean flag) {
        getActiveMySQLConnection().setUseCursorFetch(flag);
    }

    public boolean getUseDirectRowUnpack() {
        return getActiveMySQLConnection().getUseDirectRowUnpack();
    }

    public void setUseDirectRowUnpack(boolean flag) {
        getActiveMySQLConnection().setUseDirectRowUnpack(flag);
    }

    public boolean getUseDynamicCharsetInfo() {
        return getActiveMySQLConnection().getUseDynamicCharsetInfo();
    }

    public void setUseDynamicCharsetInfo(boolean flag) {
        getActiveMySQLConnection().setUseDynamicCharsetInfo(flag);
    }

    public boolean getUseFastDateParsing() {
        return getActiveMySQLConnection().getUseFastDateParsing();
    }

    public void setUseFastDateParsing(boolean flag) {
        getActiveMySQLConnection().setUseFastDateParsing(flag);
    }

    public boolean getUseFastIntParsing() {
        return getActiveMySQLConnection().getUseFastIntParsing();
    }

    public void setUseFastIntParsing(boolean flag) {
        getActiveMySQLConnection().setUseFastIntParsing(flag);
    }

    public boolean getUseGmtMillisForDatetimes() {
        return getActiveMySQLConnection().getUseGmtMillisForDatetimes();
    }

    public void setUseGmtMillisForDatetimes(boolean flag) {
        getActiveMySQLConnection().setUseGmtMillisForDatetimes(flag);
    }

    public boolean getUseHostsInPrivileges() {
        return getActiveMySQLConnection().getUseHostsInPrivileges();
    }

    public void setUseHostsInPrivileges(boolean property) {
        getActiveMySQLConnection().setUseHostsInPrivileges(property);
    }

    public boolean getUseInformationSchema() {
        return getActiveMySQLConnection().getUseInformationSchema();
    }

    public void setUseInformationSchema(boolean flag) {
        getActiveMySQLConnection().setUseInformationSchema(flag);
    }

    public boolean getUseJDBCCompliantTimezoneShift() {
        return getActiveMySQLConnection().getUseJDBCCompliantTimezoneShift();
    }

    public void setUseJDBCCompliantTimezoneShift(boolean flag) {
        getActiveMySQLConnection().setUseJDBCCompliantTimezoneShift(flag);
    }

    public boolean getUseJvmCharsetConverters() {
        return getActiveMySQLConnection().getUseJvmCharsetConverters();
    }

    public void setUseJvmCharsetConverters(boolean flag) {
        getActiveMySQLConnection().setUseJvmCharsetConverters(flag);
    }

    public boolean getUseLegacyDatetimeCode() {
        return getActiveMySQLConnection().getUseLegacyDatetimeCode();
    }

    public void setUseLegacyDatetimeCode(boolean flag) {
        getActiveMySQLConnection().setUseLegacyDatetimeCode(flag);
    }

    public boolean getUseLocalSessionState() {
        return getActiveMySQLConnection().getUseLocalSessionState();
    }

    public void setUseLocalSessionState(boolean flag) {
        getActiveMySQLConnection().setUseLocalSessionState(flag);
    }

    public boolean getUseLocalTransactionState() {
        return getActiveMySQLConnection().getUseLocalTransactionState();
    }

    public void setUseLocalTransactionState(boolean flag) {
        getActiveMySQLConnection().setUseLocalTransactionState(flag);
    }

    public boolean getUseNanosForElapsedTime() {
        return getActiveMySQLConnection().getUseNanosForElapsedTime();
    }

    public void setUseNanosForElapsedTime(boolean flag) {
        getActiveMySQLConnection().setUseNanosForElapsedTime(flag);
    }

    public boolean getUseOldAliasMetadataBehavior() {
        return getActiveMySQLConnection().getUseOldAliasMetadataBehavior();
    }

    public void setUseOldAliasMetadataBehavior(boolean flag) {
        getActiveMySQLConnection().setUseOldAliasMetadataBehavior(flag);
    }

    public boolean getUseOldUTF8Behavior() {
        return getActiveMySQLConnection().getUseOldUTF8Behavior();
    }

    public void setUseOldUTF8Behavior(boolean flag) {
        getActiveMySQLConnection().setUseOldUTF8Behavior(flag);
    }

    public boolean getUseOnlyServerErrorMessages() {
        return getActiveMySQLConnection().getUseOnlyServerErrorMessages();
    }

    public void setUseOnlyServerErrorMessages(boolean flag) {
        getActiveMySQLConnection().setUseOnlyServerErrorMessages(flag);
    }

    public boolean getUseReadAheadInput() {
        return getActiveMySQLConnection().getUseReadAheadInput();
    }

    public void setUseReadAheadInput(boolean flag) {
        getActiveMySQLConnection().setUseReadAheadInput(flag);
    }

    public boolean getUseSSL() {
        return getActiveMySQLConnection().getUseSSL();
    }

    public void setUseSSL(boolean property) {
        getActiveMySQLConnection().setUseSSL(property);
    }

    public boolean getUseSSPSCompatibleTimezoneShift() {
        return getActiveMySQLConnection().getUseSSPSCompatibleTimezoneShift();
    }

    public void setUseSSPSCompatibleTimezoneShift(boolean flag) {
        getActiveMySQLConnection().setUseSSPSCompatibleTimezoneShift(flag);
    }

    public boolean getUseServerPrepStmts() {
        return getActiveMySQLConnection().getUseServerPrepStmts();
    }

    public void setUseServerPrepStmts(boolean flag) {
        getActiveMySQLConnection().setUseServerPrepStmts(flag);
    }

    public boolean getUseServerPreparedStmts() {
        return getActiveMySQLConnection().getUseServerPreparedStmts();
    }

    public void setUseServerPreparedStmts(boolean flag) {
        getActiveMySQLConnection().setUseServerPreparedStmts(flag);
    }

    public boolean getUseSqlStateCodes() {
        return getActiveMySQLConnection().getUseSqlStateCodes();
    }

    public void setUseSqlStateCodes(boolean flag) {
        getActiveMySQLConnection().setUseSqlStateCodes(flag);
    }

    public boolean getUseStreamLengthsInPrepStmts() {
        return getActiveMySQLConnection().getUseStreamLengthsInPrepStmts();
    }

    public void setUseStreamLengthsInPrepStmts(boolean property) {
        getActiveMySQLConnection().setUseStreamLengthsInPrepStmts(property);
    }

    public boolean getUseTimezone() {
        return getActiveMySQLConnection().getUseTimezone();
    }

    public void setUseTimezone(boolean property) {
        getActiveMySQLConnection().setUseTimezone(property);
    }

    public boolean getUseUltraDevWorkAround() {
        return getActiveMySQLConnection().getUseUltraDevWorkAround();
    }

    public void setUseUltraDevWorkAround(boolean property) {
        getActiveMySQLConnection().setUseUltraDevWorkAround(property);
    }

    public boolean getUseUnbufferedInput() {
        return getActiveMySQLConnection().getUseUnbufferedInput();
    }

    public void setUseUnbufferedInput(boolean flag) {
        getActiveMySQLConnection().setUseUnbufferedInput(flag);
    }

    public boolean getUseUnicode() {
        return getActiveMySQLConnection().getUseUnicode();
    }

    public void setUseUnicode(boolean flag) {
        getActiveMySQLConnection().setUseUnicode(flag);
    }

    public boolean getUseUsageAdvisor() {
        return getActiveMySQLConnection().getUseUsageAdvisor();
    }

    public void setUseUsageAdvisor(boolean useUsageAdvisorFlag) {
        getActiveMySQLConnection().setUseUsageAdvisor(useUsageAdvisorFlag);
    }

    public String getUtf8OutsideBmpExcludedColumnNamePattern() {
        return getActiveMySQLConnection().getUtf8OutsideBmpExcludedColumnNamePattern();
    }

    public void setUtf8OutsideBmpExcludedColumnNamePattern(String regexPattern) {
        getActiveMySQLConnection().setUtf8OutsideBmpExcludedColumnNamePattern(regexPattern);
    }

    public String getUtf8OutsideBmpIncludedColumnNamePattern() {
        return getActiveMySQLConnection().getUtf8OutsideBmpIncludedColumnNamePattern();
    }

    public void setUtf8OutsideBmpIncludedColumnNamePattern(String regexPattern) {
        getActiveMySQLConnection().setUtf8OutsideBmpIncludedColumnNamePattern(regexPattern);
    }

    public boolean getVerifyServerCertificate() {
        return getActiveMySQLConnection().getVerifyServerCertificate();
    }

    public void setVerifyServerCertificate(boolean flag) {
        getActiveMySQLConnection().setVerifyServerCertificate(flag);
    }

    public boolean getYearIsDateType() {
        return getActiveMySQLConnection().getYearIsDateType();
    }

    public void setYearIsDateType(boolean flag) {
        getActiveMySQLConnection().setYearIsDateType(flag);
    }

    public String getZeroDateTimeBehavior() {
        return getActiveMySQLConnection().getZeroDateTimeBehavior();
    }

    public void setZeroDateTimeBehavior(String behavior) {
        getActiveMySQLConnection().setZeroDateTimeBehavior(behavior);
    }

    public void setAutoReconnect(boolean flag) {
        getActiveMySQLConnection().setAutoReconnect(flag);
    }

    public void setAutoReconnectForConnectionPools(boolean property) {
        getActiveMySQLConnection().setAutoReconnectForConnectionPools(property);
    }

    public void setCapitalizeDBMDTypes(boolean property) {
        getActiveMySQLConnection().setCapitalizeDBMDTypes(property);
    }

    public void setCharacterEncoding(String encoding) {
        getActiveMySQLConnection().setCharacterEncoding(encoding);
    }

    public void setDetectServerPreparedStmts(boolean property) {
        getActiveMySQLConnection().setDetectServerPreparedStmts(property);
    }

    public boolean useUnbufferedInput() {
        return getActiveMySQLConnection().useUnbufferedInput();
    }

    public StringBuffer generateConnectionCommentBlock(StringBuffer buf) {
        return getActiveMySQLConnection().generateConnectionCommentBlock(buf);
    }

    public int getActiveStatementCount() {
        return getActiveMySQLConnection().getActiveStatementCount();
    }

    public boolean getAutoCommit() throws SQLException {
        return getActiveMySQLConnection().getAutoCommit();
    }

    public void setAutoCommit(boolean autoCommitFlag) throws SQLException {
        getActiveMySQLConnection().setAutoCommit(autoCommitFlag);
    }

    public int getAutoIncrementIncrement() {
        return getActiveMySQLConnection().getAutoIncrementIncrement();
    }

    public CachedResultSetMetaData getCachedMetaData(String sql) {
        return getActiveMySQLConnection().getCachedMetaData(sql);
    }

    public Calendar getCalendarInstanceForSessionOrNew() {
        return getActiveMySQLConnection().getCalendarInstanceForSessionOrNew();
    }

    public synchronized Timer getCancelTimer() {
        return getActiveMySQLConnection().getCancelTimer();
    }

    public String getCatalog() throws SQLException {
        return getActiveMySQLConnection().getCatalog();
    }

    public void setCatalog(String catalog) throws SQLException {
        getActiveMySQLConnection().setCatalog(catalog);
    }

    public String getCharacterSetMetadata() {
        return getActiveMySQLConnection().getCharacterSetMetadata();
    }

    public SingleByteCharsetConverter getCharsetConverter(String javaEncodingName) throws SQLException {
        return getActiveMySQLConnection().getCharsetConverter(javaEncodingName);
    }

    public String getCharsetNameForIndex(int charsetIndex) throws SQLException {
        return getActiveMySQLConnection().getCharsetNameForIndex(charsetIndex);
    }

    public TimeZone getDefaultTimeZone() {
        return getActiveMySQLConnection().getDefaultTimeZone();
    }

    public String getErrorMessageEncoding() {
        return getActiveMySQLConnection().getErrorMessageEncoding();
    }

    public ExceptionInterceptor getExceptionInterceptor() {
        return getActiveMySQLConnection().getExceptionInterceptor();
    }

    public int getHoldability() throws SQLException {
        return getActiveMySQLConnection().getHoldability();
    }

    public void setHoldability(int arg0) throws SQLException {
        getActiveMySQLConnection().setHoldability(arg0);
    }

    public String getHost() {
        return getActiveMySQLConnection().getHost();
    }

    public long getId() {
        return getActiveMySQLConnection().getId();
    }

    public long getIdleFor() {
        return getActiveMySQLConnection().getIdleFor();
    }

    public MysqlIO getIO() throws SQLException {
        return getActiveMySQLConnection().getIO();
    }

    public MySQLConnection getLoadBalanceSafeProxy() {
        return getActiveMySQLConnection().getLoadBalanceSafeProxy();
    }

    public Log getLog() throws SQLException {
        return getActiveMySQLConnection().getLog();
    }

    public int getMaxBytesPerChar(String javaCharsetName) throws SQLException {
        return getActiveMySQLConnection().getMaxBytesPerChar(javaCharsetName);
    }

    public int getMaxBytesPerChar(Integer charsetIndex, String javaCharsetName) throws SQLException {
        return getActiveMySQLConnection().getMaxBytesPerChar(charsetIndex, javaCharsetName);
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return getActiveMySQLConnection().getMetaData();
    }

    public Statement getMetadataSafeStatement() throws SQLException {
        return getActiveMySQLConnection().getMetadataSafeStatement();
    }

    public int getNetBufferLength() {
        return getActiveMySQLConnection().getNetBufferLength();
    }

    public Properties getProperties() {
        return getActiveMySQLConnection().getProperties();
    }

    public boolean getRequiresEscapingEncoder() {
        return getActiveMySQLConnection().getRequiresEscapingEncoder();
    }

    public String getServerCharacterEncoding() {
        return getActiveMySQLConnection().getServerCharacterEncoding();
    }

    public int getServerMajorVersion() {
        return getActiveMySQLConnection().getServerMajorVersion();
    }

    public int getServerMinorVersion() {
        return getActiveMySQLConnection().getServerMinorVersion();
    }

    public int getServerSubMinorVersion() {
        return getActiveMySQLConnection().getServerSubMinorVersion();
    }

    public TimeZone getServerTimezoneTZ() {
        return getActiveMySQLConnection().getServerTimezoneTZ();
    }

    public String getServerVariable(String variableName) {
        return getActiveMySQLConnection().getServerVariable(variableName);
    }

    public String getServerVersion() {
        return getActiveMySQLConnection().getServerVersion();
    }

    public Calendar getSessionLockedCalendar() {
        return getActiveMySQLConnection().getSessionLockedCalendar();
    }

    public String getStatementComment() {
        return getActiveMySQLConnection().getStatementComment();
    }

    public void setStatementComment(String comment) {
        getActiveMySQLConnection().setStatementComment(comment);
    }

    public List<StatementInterceptorV2> getStatementInterceptorsInstances() {
        return getActiveMySQLConnection().getStatementInterceptorsInstances();
    }

    public synchronized int getTransactionIsolation() throws SQLException {
        return getActiveMySQLConnection().getTransactionIsolation();
    }

    public synchronized void setTransactionIsolation(int level) throws SQLException {
        getActiveMySQLConnection().setTransactionIsolation(level);
    }

    public synchronized Map<String, Class<?>> getTypeMap() throws SQLException {
        return getActiveMySQLConnection().getTypeMap();
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        getActiveMySQLConnection().setTypeMap(map);
    }

    public String getURL() {
        return getActiveMySQLConnection().getURL();
    }

    public String getUser() {
        return getActiveMySQLConnection().getUser();
    }

    public Calendar getUtcCalendar() {
        return getActiveMySQLConnection().getUtcCalendar();
    }

    public SQLWarning getWarnings() throws SQLException {
        return getActiveMySQLConnection().getWarnings();
    }

    public boolean hasSameProperties(Connection c) {
        return getActiveMySQLConnection().hasSameProperties(c);
    }

    public boolean hasTriedMaster() {
        return getActiveMySQLConnection().hasTriedMaster();
    }

    public void incrementNumberOfPreparedExecutes() {
        getActiveMySQLConnection().incrementNumberOfPreparedExecutes();
    }

    public void incrementNumberOfPrepares() {
        getActiveMySQLConnection().incrementNumberOfPrepares();
    }

    public void incrementNumberOfResultSetsCreated() {
        getActiveMySQLConnection().incrementNumberOfResultSetsCreated();
    }

    public void initializeExtension(Extension ex) throws SQLException {
        getActiveMySQLConnection().initializeExtension(ex);
    }

    public void initializeResultsMetadataFromCache(String sql, CachedResultSetMetaData cachedMetaData, ResultSetInternalMethods resultSet) throws SQLException {
        getActiveMySQLConnection().initializeResultsMetadataFromCache(sql, cachedMetaData, resultSet);
    }

    public void initializeSafeStatementInterceptors() throws SQLException {
        getActiveMySQLConnection().initializeSafeStatementInterceptors();
    }

    public synchronized boolean isAbonormallyLongQuery(long millisOrNanos) {
        return getActiveMySQLConnection().isAbonormallyLongQuery(millisOrNanos);
    }

    public boolean isClientTzUTC() {
        return getActiveMySQLConnection().isClientTzUTC();
    }

    public boolean isCursorFetchEnabled() throws SQLException {
        return getActiveMySQLConnection().isCursorFetchEnabled();
    }

    public boolean isInGlobalTx() {
        return getActiveMySQLConnection().isInGlobalTx();
    }

    public void setInGlobalTx(boolean flag) {
        getActiveMySQLConnection().setInGlobalTx(flag);
    }

    public synchronized boolean isMasterConnection() {
        return getActiveMySQLConnection().isMasterConnection();
    }

    public boolean isNoBackslashEscapesSet() {
        return getActiveMySQLConnection().isNoBackslashEscapesSet();
    }

    public boolean isReadInfoMsgEnabled() {
        return getActiveMySQLConnection().isReadInfoMsgEnabled();
    }

    public void setReadInfoMsgEnabled(boolean flag) {
        getActiveMySQLConnection().setReadInfoMsgEnabled(flag);
    }

    public boolean isReadOnly() throws SQLException {
        return getActiveMySQLConnection().isReadOnly();
    }

    public void setReadOnly(boolean readOnlyFlag) throws SQLException {
        getActiveMySQLConnection().setReadOnly(readOnlyFlag);
    }

    public boolean isReadOnly(boolean useSessionStatus) throws SQLException {
        return getActiveMySQLConnection().isReadOnly(useSessionStatus);
    }

    public boolean isRunningOnJDK13() {
        return getActiveMySQLConnection().isRunningOnJDK13();
    }

    public synchronized boolean isSameResource(Connection otherConnection) {
        return getActiveMySQLConnection().isSameResource(otherConnection);
    }

    public boolean isServerTzUTC() {
        return getActiveMySQLConnection().isServerTzUTC();
    }

    public boolean lowerCaseTableNames() {
        return getActiveMySQLConnection().lowerCaseTableNames();
    }

    public void maxRowsChanged(Statement stmt) {
        getActiveMySQLConnection().maxRowsChanged(stmt);
    }

    public String nativeSQL(String sql) throws SQLException {
        return getActiveMySQLConnection().nativeSQL(sql);
    }

    public boolean parserKnowsUnicode() {
        return getActiveMySQLConnection().parserKnowsUnicode();
    }

    public void ping() throws SQLException {
        getActiveMySQLConnection().ping();
    }

    public void pingInternal(boolean checkForClosedConnection, int timeoutMillis) throws SQLException {
        getActiveMySQLConnection().pingInternal(checkForClosedConnection, timeoutMillis);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getActiveMySQLConnection().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getActiveMySQLConnection().prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        return getActiveMySQLConnection().prepareCall(sql);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getActiveMySQLConnection().prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getActiveMySQLConnection().prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    public PreparedStatement prepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
        return getActiveMySQLConnection().prepareStatement(sql, autoGenKeyIndex);
    }

    public PreparedStatement prepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
        return getActiveMySQLConnection().prepareStatement(sql, autoGenKeyIndexes);
    }

    public PreparedStatement prepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
        return getActiveMySQLConnection().prepareStatement(sql, autoGenKeyColNames);
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return getActiveMySQLConnection().prepareStatement(sql);
    }

    public void realClose(boolean calledExplicitly, boolean issueRollback, boolean skipLocalTeardown, Throwable reason) throws SQLException {
        getActiveMySQLConnection().realClose(calledExplicitly, issueRollback, skipLocalTeardown, reason);
    }

    public void recachePreparedStatement(ServerPreparedStatement pstmt) throws SQLException {
        getActiveMySQLConnection().recachePreparedStatement(pstmt);
    }

    public void registerQueryExecutionTime(long queryTimeMs) {
        getActiveMySQLConnection().registerQueryExecutionTime(queryTimeMs);
    }

    public void registerStatement(Statement stmt) {
        getActiveMySQLConnection().registerStatement(stmt);
    }

    public void releaseSavepoint(Savepoint arg0) throws SQLException {
        getActiveMySQLConnection().releaseSavepoint(arg0);
    }

    public void reportNumberOfTablesAccessed(int numTablesAccessed) {
        getActiveMySQLConnection().reportNumberOfTablesAccessed(numTablesAccessed);
    }

    public synchronized void reportQueryTime(long millisOrNanos) {
        getActiveMySQLConnection().reportQueryTime(millisOrNanos);
    }

    public void resetServerState() throws SQLException {
        getActiveMySQLConnection().resetServerState();
    }

    public void rollback() throws SQLException {
        getActiveMySQLConnection().rollback();
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        getActiveMySQLConnection().rollback(savepoint);
    }

    public PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getActiveMySQLConnection().serverPrepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getActiveMySQLConnection().serverPrepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    public PreparedStatement serverPrepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
        return getActiveMySQLConnection().serverPrepareStatement(sql, autoGenKeyIndex);
    }

    public PreparedStatement serverPrepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
        return getActiveMySQLConnection().serverPrepareStatement(sql, autoGenKeyIndexes);
    }

    public PreparedStatement serverPrepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
        return getActiveMySQLConnection().serverPrepareStatement(sql, autoGenKeyColNames);
    }

    public PreparedStatement serverPrepareStatement(String sql) throws SQLException {
        return getActiveMySQLConnection().serverPrepareStatement(sql);
    }

    public boolean serverSupportsConvertFn() throws SQLException {
        return getActiveMySQLConnection().serverSupportsConvertFn();
    }

    public synchronized void setFailedOver(boolean flag) {
        getActiveMySQLConnection().setFailedOver(flag);
    }

    public void setPreferSlaveDuringFailover(boolean flag) {
        getActiveMySQLConnection().setPreferSlaveDuringFailover(flag);
    }

    public void setReadOnlyInternal(boolean readOnlyFlag) throws SQLException {
        getActiveMySQLConnection().setReadOnlyInternal(readOnlyFlag);
    }

    public Savepoint setSavepoint() throws SQLException {
        return getActiveMySQLConnection().setSavepoint();
    }

    public synchronized Savepoint setSavepoint(String name) throws SQLException {
        return getActiveMySQLConnection().setSavepoint(name);
    }

    public void shutdownServer() throws SQLException {
        getActiveMySQLConnection().shutdownServer();
    }

    public boolean storesLowerCaseTableName() {
        return getActiveMySQLConnection().storesLowerCaseTableName();
    }

    public boolean supportsIsolationLevel() {
        return getActiveMySQLConnection().supportsIsolationLevel();
    }

    public boolean supportsQuotedIdentifiers() {
        return getActiveMySQLConnection().supportsQuotedIdentifiers();
    }

    public boolean supportsTransactions() {
        return getActiveMySQLConnection().supportsTransactions();
    }

    public void throwConnectionClosedException() throws SQLException {
        getActiveMySQLConnection().throwConnectionClosedException();
    }

    public void transactionBegun() throws SQLException {
        getActiveMySQLConnection().transactionBegun();
    }

    public void transactionCompleted() throws SQLException {
        getActiveMySQLConnection().transactionCompleted();
    }

    public void unregisterStatement(Statement stmt) {
        getActiveMySQLConnection().unregisterStatement(stmt);
    }

    public void unSafeStatementInterceptors() throws SQLException {
        getActiveMySQLConnection().unSafeStatementInterceptors();
    }

    public void unsetMaxRows(Statement stmt) throws SQLException {
        getActiveMySQLConnection().unsetMaxRows(stmt);
    }

    public boolean useAnsiQuotedIdentifiers() {
        return getActiveMySQLConnection().useAnsiQuotedIdentifiers();
    }

    public boolean useMaxRows() {
        return getActiveMySQLConnection().useMaxRows();
    }

    public boolean versionMeetsMinimum(int major, int minor, int subminor) throws SQLException {
        return getActiveMySQLConnection().versionMeetsMinimum(major, minor, subminor);
    }

    public boolean isClosed() throws SQLException {
        return getActiveMySQLConnection().isClosed();
    }

    public boolean getHoldResultsOpenOverStatementClose() {
        return getActiveMySQLConnection().getHoldResultsOpenOverStatementClose();
    }

    public void setHoldResultsOpenOverStatementClose(boolean flag) {
        getActiveMySQLConnection().setHoldResultsOpenOverStatementClose(flag);
    }

    public String getLoadBalanceConnectionGroup() {
        return getActiveMySQLConnection().getLoadBalanceConnectionGroup();
    }

    public void setLoadBalanceConnectionGroup(String loadBalanceConnectionGroup) {
        getActiveMySQLConnection().setLoadBalanceConnectionGroup(loadBalanceConnectionGroup);
    }

    public boolean getLoadBalanceEnableJMX() {
        return getActiveMySQLConnection().getLoadBalanceEnableJMX();
    }

    public void setLoadBalanceEnableJMX(boolean loadBalanceEnableJMX) {
        getActiveMySQLConnection().setLoadBalanceEnableJMX(loadBalanceEnableJMX);
    }

    public String getLoadBalanceExceptionChecker() {
        return getActiveMySQLConnection().getLoadBalanceExceptionChecker();
    }

    public void setLoadBalanceExceptionChecker(String loadBalanceExceptionChecker) {
        getActiveMySQLConnection().setLoadBalanceExceptionChecker(loadBalanceExceptionChecker);
    }

    public String getLoadBalanceSQLExceptionSubclassFailover() {
        return getActiveMySQLConnection().getLoadBalanceSQLExceptionSubclassFailover();
    }

    public void setLoadBalanceSQLExceptionSubclassFailover(String loadBalanceSQLExceptionSubclassFailover) {
        getActiveMySQLConnection().setLoadBalanceSQLExceptionSubclassFailover(loadBalanceSQLExceptionSubclassFailover);
    }

    public String getLoadBalanceSQLStateFailover() {
        return getActiveMySQLConnection().getLoadBalanceSQLStateFailover();
    }

    public void setLoadBalanceSQLStateFailover(String loadBalanceSQLStateFailover) {
        getActiveMySQLConnection().setLoadBalanceSQLStateFailover(loadBalanceSQLStateFailover);
    }

    public boolean shouldExecutionTriggerServerSwapAfter(String SQL) {
        return false;
    }

    public boolean isProxySet() {
        return getActiveMySQLConnection().isProxySet();
    }

    public String getLoadBalanceAutoCommitStatementRegex() {
        return getActiveMySQLConnection().getLoadBalanceAutoCommitStatementRegex();
    }

    public void setLoadBalanceAutoCommitStatementRegex(String loadBalanceAutoCommitStatementRegex) {
        getActiveMySQLConnection().setLoadBalanceAutoCommitStatementRegex(loadBalanceAutoCommitStatementRegex);
    }

    public int getLoadBalanceAutoCommitStatementThreshold() {
        return getActiveMySQLConnection().getLoadBalanceAutoCommitStatementThreshold();
    }

    public void setLoadBalanceAutoCommitStatementThreshold(int loadBalanceAutoCommitStatementThreshold) {
        getActiveMySQLConnection().setLoadBalanceAutoCommitStatementThreshold(loadBalanceAutoCommitStatementThreshold);
    }

    public boolean getIncludeThreadDumpInDeadlockExceptions() {
        return getActiveMySQLConnection().getIncludeThreadDumpInDeadlockExceptions();
    }

    public void setIncludeThreadDumpInDeadlockExceptions(boolean flag) {
        getActiveMySQLConnection().setIncludeThreadDumpInDeadlockExceptions(flag);
    }

    public boolean getIncludeThreadNamesAsStatementComment() {
        return getActiveMySQLConnection().getIncludeThreadNamesAsStatementComment();
    }

    public void setIncludeThreadNamesAsStatementComment(boolean flag) {
        getActiveMySQLConnection().setIncludeThreadNamesAsStatementComment(flag);
    }

    public synchronized boolean isServerLocal() throws SQLException {
        return getActiveMySQLConnection().isServerLocal();
    }

    public String getAuthenticationPlugins() {
        return getActiveMySQLConnection().getAuthenticationPlugins();
    }

    public void setAuthenticationPlugins(String authenticationPlugins) {
        getActiveMySQLConnection().setAuthenticationPlugins(authenticationPlugins);
    }

    public String getDisabledAuthenticationPlugins() {
        return getActiveMySQLConnection().getDisabledAuthenticationPlugins();
    }

    public void setDisabledAuthenticationPlugins(String disabledAuthenticationPlugins) {
        getActiveMySQLConnection().setDisabledAuthenticationPlugins(disabledAuthenticationPlugins);
    }

    public String getDefaultAuthenticationPlugin() {
        return getActiveMySQLConnection().getDefaultAuthenticationPlugin();
    }

    public void setDefaultAuthenticationPlugin(String defaultAuthenticationPlugin) {
        getActiveMySQLConnection().setDefaultAuthenticationPlugin(defaultAuthenticationPlugin);
    }

    public String getParseInfoCacheFactory() {
        return getActiveMySQLConnection().getParseInfoCacheFactory();
    }

    public void setParseInfoCacheFactory(String factoryClassname) {
        getActiveMySQLConnection().setParseInfoCacheFactory(factoryClassname);
    }

    public String getSchema() throws SQLException {
        return getActiveMySQLConnection().getSchema();
    }

    public void setSchema(String schema) throws SQLException {
        getActiveMySQLConnection().setSchema(schema);
    }

    public void abort(Executor executor) throws SQLException {
        getActiveMySQLConnection().abort(executor);
    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        getActiveMySQLConnection().setNetworkTimeout(executor, milliseconds);
    }

    public int getNetworkTimeout() throws SQLException {
        return getActiveMySQLConnection().getNetworkTimeout();
    }

    public String getServerConfigCacheFactory() {
        return getActiveMySQLConnection().getServerConfigCacheFactory();
    }

    public void setServerConfigCacheFactory(String factoryClassname) {
        getActiveMySQLConnection().setServerConfigCacheFactory(factoryClassname);
    }

    public boolean getDisconnectOnExpiredPasswords() {
        return getActiveMySQLConnection().getDisconnectOnExpiredPasswords();
    }

    public void setDisconnectOnExpiredPasswords(boolean disconnectOnExpiredPasswords) {
        getActiveMySQLConnection().setDisconnectOnExpiredPasswords(disconnectOnExpiredPasswords);
    }
}

