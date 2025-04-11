package com.mysql.jdbc;

import java.sql.SQLException;

public interface ConnectionProperties {
    String exposeAsXml() throws SQLException;

    boolean getAllowLoadLocalInfile();

    void setAllowLoadLocalInfile(boolean paramBoolean);

    boolean getAllowMultiQueries();

    void setAllowMultiQueries(boolean paramBoolean);

    boolean getAllowNanAndInf();

    void setAllowNanAndInf(boolean paramBoolean);

    boolean getAllowUrlInLocalInfile();

    void setAllowUrlInLocalInfile(boolean paramBoolean);

    boolean getAlwaysSendSetIsolation();

    void setAlwaysSendSetIsolation(boolean paramBoolean);

    boolean getAutoDeserialize();

    void setAutoDeserialize(boolean paramBoolean);

    boolean getAutoGenerateTestcaseScript();

    void setAutoGenerateTestcaseScript(boolean paramBoolean);

    boolean getAutoReconnectForPools();

    void setAutoReconnectForPools(boolean paramBoolean);

    int getBlobSendChunkSize();

    void setBlobSendChunkSize(String paramString) throws SQLException;

    boolean getCacheCallableStatements();

    void setCacheCallableStatements(boolean paramBoolean);

    boolean getCachePreparedStatements();

    void setCachePreparedStatements(boolean paramBoolean);

    boolean getCacheResultSetMetadata();

    void setCacheResultSetMetadata(boolean paramBoolean);

    boolean getCacheServerConfiguration();

    void setCacheServerConfiguration(boolean paramBoolean);

    int getCallableStatementCacheSize();

    void setCallableStatementCacheSize(int paramInt);

    boolean getCapitalizeTypeNames();

    void setCapitalizeTypeNames(boolean paramBoolean);

    String getCharacterSetResults();

    void setCharacterSetResults(String paramString);

    boolean getClobberStreamingResults();

    void setClobberStreamingResults(boolean paramBoolean);

    String getClobCharacterEncoding();

    void setClobCharacterEncoding(String paramString);

    String getConnectionCollation();

    void setConnectionCollation(String paramString);

    int getConnectTimeout();

    void setConnectTimeout(int paramInt);

    boolean getContinueBatchOnError();

    void setContinueBatchOnError(boolean paramBoolean);

    boolean getCreateDatabaseIfNotExist();

    void setCreateDatabaseIfNotExist(boolean paramBoolean);

    int getDefaultFetchSize();

    void setDefaultFetchSize(int paramInt);

    boolean getDontTrackOpenResources();

    void setDontTrackOpenResources(boolean paramBoolean);

    boolean getDumpQueriesOnException();

    void setDumpQueriesOnException(boolean paramBoolean);

    boolean getDynamicCalendars();

    void setDynamicCalendars(boolean paramBoolean);

    boolean getElideSetAutoCommits();

    void setElideSetAutoCommits(boolean paramBoolean);

    boolean getEmptyStringsConvertToZero();

    void setEmptyStringsConvertToZero(boolean paramBoolean);

    boolean getEmulateLocators();

    void setEmulateLocators(boolean paramBoolean);

    boolean getEmulateUnsupportedPstmts();

    void setEmulateUnsupportedPstmts(boolean paramBoolean);

    boolean getEnablePacketDebug();

    void setEnablePacketDebug(boolean paramBoolean);

    String getEncoding();

    void setEncoding(String paramString);

    boolean getExplainSlowQueries();

    void setExplainSlowQueries(boolean paramBoolean);

    boolean getFailOverReadOnly();

    void setFailOverReadOnly(boolean paramBoolean);

    boolean getGatherPerformanceMetrics();

    void setGatherPerformanceMetrics(boolean paramBoolean);

    boolean getHoldResultsOpenOverStatementClose();

    void setHoldResultsOpenOverStatementClose(boolean paramBoolean);

    boolean getIgnoreNonTxTables();

    void setIgnoreNonTxTables(boolean paramBoolean);

    int getInitialTimeout();

    void setInitialTimeout(int paramInt);

    boolean getInteractiveClient();

    void setInteractiveClient(boolean paramBoolean);

    boolean getIsInteractiveClient();

    void setIsInteractiveClient(boolean paramBoolean);

    boolean getJdbcCompliantTruncation();

    void setJdbcCompliantTruncation(boolean paramBoolean);

    int getLocatorFetchBufferSize();

    void setLocatorFetchBufferSize(String paramString) throws SQLException;

    String getLogger();

    void setLogger(String paramString);

    String getLoggerClassName();

    void setLoggerClassName(String paramString);

    boolean getLogSlowQueries();

    void setLogSlowQueries(boolean paramBoolean);

    boolean getMaintainTimeStats();

    void setMaintainTimeStats(boolean paramBoolean);

    int getMaxQuerySizeToLog();

    void setMaxQuerySizeToLog(int paramInt);

    int getMaxReconnects();

    void setMaxReconnects(int paramInt);

    int getMaxRows();

    void setMaxRows(int paramInt);

    int getMetadataCacheSize();

    void setMetadataCacheSize(int paramInt);

    boolean getNoDatetimeStringSync();

    void setNoDatetimeStringSync(boolean paramBoolean);

    boolean getNullCatalogMeansCurrent();

    void setNullCatalogMeansCurrent(boolean paramBoolean);

    boolean getNullNamePatternMatchesAll();

    void setNullNamePatternMatchesAll(boolean paramBoolean);

    int getPacketDebugBufferSize();

    void setPacketDebugBufferSize(int paramInt);

    boolean getParanoid();

    void setParanoid(boolean paramBoolean);

    boolean getPedantic();

    void setPedantic(boolean paramBoolean);

    int getPreparedStatementCacheSize();

    void setPreparedStatementCacheSize(int paramInt);

    int getPreparedStatementCacheSqlLimit();

    void setPreparedStatementCacheSqlLimit(int paramInt);

    boolean getProfileSql();

    void setProfileSql(boolean paramBoolean);

    boolean getProfileSQL();

    void setProfileSQL(boolean paramBoolean);

    String getPropertiesTransform();

    void setPropertiesTransform(String paramString);

    int getQueriesBeforeRetryMaster();

    void setQueriesBeforeRetryMaster(int paramInt);

    boolean getReconnectAtTxEnd();

    void setReconnectAtTxEnd(boolean paramBoolean);

    boolean getRelaxAutoCommit();

    void setRelaxAutoCommit(boolean paramBoolean);

    int getReportMetricsIntervalMillis();

    void setReportMetricsIntervalMillis(int paramInt);

    boolean getRequireSSL();

    void setRequireSSL(boolean paramBoolean);

    boolean getRollbackOnPooledClose();

    void setRollbackOnPooledClose(boolean paramBoolean);

    boolean getRoundRobinLoadBalance();

    void setRoundRobinLoadBalance(boolean paramBoolean);

    boolean getRunningCTS13();

    void setRunningCTS13(boolean paramBoolean);

    int getSecondsBeforeRetryMaster();

    void setSecondsBeforeRetryMaster(int paramInt);

    String getServerTimezone();

    void setServerTimezone(String paramString);

    String getSessionVariables();

    void setSessionVariables(String paramString);

    int getSlowQueryThresholdMillis();

    void setSlowQueryThresholdMillis(int paramInt);

    String getSocketFactoryClassName();

    void setSocketFactoryClassName(String paramString);

    int getSocketTimeout();

    void setSocketTimeout(int paramInt);

    boolean getStrictFloatingPoint();

    void setStrictFloatingPoint(boolean paramBoolean);

    boolean getStrictUpdates();

    void setStrictUpdates(boolean paramBoolean);

    boolean getTinyInt1isBit();

    void setTinyInt1isBit(boolean paramBoolean);

    boolean getTraceProtocol();

    void setTraceProtocol(boolean paramBoolean);

    boolean getTransformedBitIsBoolean();

    void setTransformedBitIsBoolean(boolean paramBoolean);

    boolean getUseCompression();

    void setUseCompression(boolean paramBoolean);

    boolean getUseFastIntParsing();

    void setUseFastIntParsing(boolean paramBoolean);

    boolean getUseHostsInPrivileges();

    void setUseHostsInPrivileges(boolean paramBoolean);

    boolean getUseInformationSchema();

    void setUseInformationSchema(boolean paramBoolean);

    boolean getUseLocalSessionState();

    void setUseLocalSessionState(boolean paramBoolean);

    boolean getUseOldUTF8Behavior();

    void setUseOldUTF8Behavior(boolean paramBoolean);

    boolean getUseOnlyServerErrorMessages();

    void setUseOnlyServerErrorMessages(boolean paramBoolean);

    boolean getUseReadAheadInput();

    void setUseReadAheadInput(boolean paramBoolean);

    boolean getUseServerPreparedStmts();

    void setUseServerPreparedStmts(boolean paramBoolean);

    boolean getUseSqlStateCodes();

    void setUseSqlStateCodes(boolean paramBoolean);

    boolean getUseSSL();

    void setUseSSL(boolean paramBoolean);

    boolean getUseStreamLengthsInPrepStmts();

    void setUseStreamLengthsInPrepStmts(boolean paramBoolean);

    boolean getUseTimezone();

    void setUseTimezone(boolean paramBoolean);

    boolean getUseUltraDevWorkAround();

    void setUseUltraDevWorkAround(boolean paramBoolean);

    boolean getUseUnbufferedInput();

    void setUseUnbufferedInput(boolean paramBoolean);

    boolean getUseUnicode();

    void setUseUnicode(boolean paramBoolean);

    boolean getUseUsageAdvisor();

    void setUseUsageAdvisor(boolean paramBoolean);

    boolean getYearIsDateType();

    void setYearIsDateType(boolean paramBoolean);

    String getZeroDateTimeBehavior();

    void setZeroDateTimeBehavior(String paramString);

    void setAutoReconnect(boolean paramBoolean);

    void setAutoReconnectForConnectionPools(boolean paramBoolean);

    void setCapitalizeDBMDTypes(boolean paramBoolean);

    void setCharacterEncoding(String paramString);

    void setDetectServerPreparedStmts(boolean paramBoolean);

    boolean useUnbufferedInput();

    boolean getUseCursorFetch();

    void setUseCursorFetch(boolean paramBoolean);

    boolean getOverrideSupportsIntegrityEnhancementFacility();

    void setOverrideSupportsIntegrityEnhancementFacility(boolean paramBoolean);

    boolean getNoTimezoneConversionForTimeType();

    void setNoTimezoneConversionForTimeType(boolean paramBoolean);

    boolean getUseJDBCCompliantTimezoneShift();

    void setUseJDBCCompliantTimezoneShift(boolean paramBoolean);

    boolean getAutoClosePStmtStreams();

    void setAutoClosePStmtStreams(boolean paramBoolean);

    boolean getProcessEscapeCodesForPrepStmts();

    void setProcessEscapeCodesForPrepStmts(boolean paramBoolean);

    boolean getUseGmtMillisForDatetimes();

    void setUseGmtMillisForDatetimes(boolean paramBoolean);

    boolean getDumpMetadataOnColumnNotFound();

    void setDumpMetadataOnColumnNotFound(boolean paramBoolean);

    String getResourceId();

    void setResourceId(String paramString);

    boolean getRewriteBatchedStatements();

    void setRewriteBatchedStatements(boolean paramBoolean);

    boolean getJdbcCompliantTruncationForReads();

    void setJdbcCompliantTruncationForReads(boolean paramBoolean);

    boolean getUseJvmCharsetConverters();

    void setUseJvmCharsetConverters(boolean paramBoolean);

    boolean getPinGlobalTxToPhysicalConnection();

    void setPinGlobalTxToPhysicalConnection(boolean paramBoolean);

    boolean getGatherPerfMetrics();

    void setGatherPerfMetrics(boolean paramBoolean);

    boolean getUltraDevHack();

    void setUltraDevHack(boolean paramBoolean);

    String getSocketFactory();

    void setSocketFactory(String paramString);

    boolean getUseServerPrepStmts();

    void setUseServerPrepStmts(boolean paramBoolean);

    boolean getCacheCallableStmts();

    void setCacheCallableStmts(boolean paramBoolean);

    boolean getCachePrepStmts();

    void setCachePrepStmts(boolean paramBoolean);

    int getCallableStmtCacheSize();

    void setCallableStmtCacheSize(int paramInt);

    int getPrepStmtCacheSize();

    void setPrepStmtCacheSize(int paramInt);

    int getPrepStmtCacheSqlLimit();

    void setPrepStmtCacheSqlLimit(int paramInt);

    boolean getNoAccessToProcedureBodies();

    void setNoAccessToProcedureBodies(boolean paramBoolean);

    boolean getUseOldAliasMetadataBehavior();

    void setUseOldAliasMetadataBehavior(boolean paramBoolean);

    String getClientCertificateKeyStorePassword();

    void setClientCertificateKeyStorePassword(String paramString);

    String getClientCertificateKeyStoreType();

    void setClientCertificateKeyStoreType(String paramString);

    String getClientCertificateKeyStoreUrl();

    void setClientCertificateKeyStoreUrl(String paramString);

    String getTrustCertificateKeyStorePassword();

    void setTrustCertificateKeyStorePassword(String paramString);

    String getTrustCertificateKeyStoreType();

    void setTrustCertificateKeyStoreType(String paramString);

    String getTrustCertificateKeyStoreUrl();

    void setTrustCertificateKeyStoreUrl(String paramString);

    boolean getUseSSPSCompatibleTimezoneShift();

    void setUseSSPSCompatibleTimezoneShift(boolean paramBoolean);

    boolean getTreatUtilDateAsTimestamp();

    void setTreatUtilDateAsTimestamp(boolean paramBoolean);

    boolean getUseFastDateParsing();

    void setUseFastDateParsing(boolean paramBoolean);

    String getLocalSocketAddress();

    void setLocalSocketAddress(String paramString);

    String getUseConfigs();

    void setUseConfigs(String paramString);

    boolean getGenerateSimpleParameterMetadata();

    void setGenerateSimpleParameterMetadata(boolean paramBoolean);

    boolean getLogXaCommands();

    void setLogXaCommands(boolean paramBoolean);

    int getResultSetSizeThreshold();

    void setResultSetSizeThreshold(int paramInt);

    int getNetTimeoutForStreamingResults();

    void setNetTimeoutForStreamingResults(int paramInt);

    boolean getEnableQueryTimeouts();

    void setEnableQueryTimeouts(boolean paramBoolean);

    boolean getPadCharsWithSpace();

    void setPadCharsWithSpace(boolean paramBoolean);

    boolean getUseDynamicCharsetInfo();

    void setUseDynamicCharsetInfo(boolean paramBoolean);

    String getClientInfoProvider();

    void setClientInfoProvider(String paramString);

    boolean getPopulateInsertRowWithDefaultValues();

    void setPopulateInsertRowWithDefaultValues(boolean paramBoolean);

    String getLoadBalanceStrategy();

    void setLoadBalanceStrategy(String paramString);

    boolean getTcpNoDelay();

    void setTcpNoDelay(boolean paramBoolean);

    boolean getTcpKeepAlive();

    void setTcpKeepAlive(boolean paramBoolean);

    int getTcpRcvBuf();

    void setTcpRcvBuf(int paramInt);

    int getTcpSndBuf();

    void setTcpSndBuf(int paramInt);

    int getTcpTrafficClass();

    void setTcpTrafficClass(int paramInt);

    boolean getUseNanosForElapsedTime();

    void setUseNanosForElapsedTime(boolean paramBoolean);

    long getSlowQueryThresholdNanos();

    void setSlowQueryThresholdNanos(long paramLong);

    String getStatementInterceptors();

    void setStatementInterceptors(String paramString);

    boolean getUseDirectRowUnpack();

    void setUseDirectRowUnpack(boolean paramBoolean);

    String getLargeRowSizeThreshold();

    void setLargeRowSizeThreshold(String paramString);

    boolean getUseBlobToStoreUTF8OutsideBMP();

    void setUseBlobToStoreUTF8OutsideBMP(boolean paramBoolean);

    String getUtf8OutsideBmpExcludedColumnNamePattern();

    void setUtf8OutsideBmpExcludedColumnNamePattern(String paramString);

    String getUtf8OutsideBmpIncludedColumnNamePattern();

    void setUtf8OutsideBmpIncludedColumnNamePattern(String paramString);

    boolean getIncludeInnodbStatusInDeadlockExceptions();

    void setIncludeInnodbStatusInDeadlockExceptions(boolean paramBoolean);

    boolean getIncludeThreadDumpInDeadlockExceptions();

    void setIncludeThreadDumpInDeadlockExceptions(boolean paramBoolean);

    boolean getIncludeThreadNamesAsStatementComment();

    void setIncludeThreadNamesAsStatementComment(boolean paramBoolean);

    boolean getBlobsAreStrings();

    void setBlobsAreStrings(boolean paramBoolean);

    boolean getFunctionsNeverReturnBlobs();

    void setFunctionsNeverReturnBlobs(boolean paramBoolean);

    boolean getAutoSlowLog();

    void setAutoSlowLog(boolean paramBoolean);

    String getConnectionLifecycleInterceptors();

    void setConnectionLifecycleInterceptors(String paramString);

    String getProfilerEventHandler();

    void setProfilerEventHandler(String paramString);

    boolean getVerifyServerCertificate();

    void setVerifyServerCertificate(boolean paramBoolean);

    boolean getUseLegacyDatetimeCode();

    void setUseLegacyDatetimeCode(boolean paramBoolean);

    int getSelfDestructOnPingSecondsLifetime();

    void setSelfDestructOnPingSecondsLifetime(int paramInt);

    int getSelfDestructOnPingMaxOperations();

    void setSelfDestructOnPingMaxOperations(int paramInt);

    boolean getUseColumnNamesInFindColumn();

    void setUseColumnNamesInFindColumn(boolean paramBoolean);

    boolean getUseLocalTransactionState();

    void setUseLocalTransactionState(boolean paramBoolean);

    boolean getCompensateOnDuplicateKeyUpdateCounts();

    void setCompensateOnDuplicateKeyUpdateCounts(boolean paramBoolean);

    boolean getUseAffectedRows();

    void setUseAffectedRows(boolean paramBoolean);

    String getPasswordCharacterEncoding();

    void setPasswordCharacterEncoding(String paramString);

    int getLoadBalanceBlacklistTimeout();

    void setLoadBalanceBlacklistTimeout(int paramInt);

    int getRetriesAllDown();

    void setRetriesAllDown(int paramInt);

    ExceptionInterceptor getExceptionInterceptor();

    String getExceptionInterceptors();

    void setExceptionInterceptors(String paramString);

    boolean getQueryTimeoutKillsConnection();

    void setQueryTimeoutKillsConnection(boolean paramBoolean);

    int getMaxAllowedPacket();

    boolean getRetainStatementAfterResultSetClose();

    void setRetainStatementAfterResultSetClose(boolean paramBoolean);

    int getLoadBalancePingTimeout();

    void setLoadBalancePingTimeout(int paramInt);

    boolean getLoadBalanceValidateConnectionOnSwapServer();

    void setLoadBalanceValidateConnectionOnSwapServer(boolean paramBoolean);

    String getLoadBalanceConnectionGroup();

    void setLoadBalanceConnectionGroup(String paramString);

    String getLoadBalanceExceptionChecker();

    void setLoadBalanceExceptionChecker(String paramString);

    String getLoadBalanceSQLStateFailover();

    void setLoadBalanceSQLStateFailover(String paramString);

    String getLoadBalanceSQLExceptionSubclassFailover();

    void setLoadBalanceSQLExceptionSubclassFailover(String paramString);

    boolean getLoadBalanceEnableJMX();

    void setLoadBalanceEnableJMX(boolean paramBoolean);

    int getLoadBalanceAutoCommitStatementThreshold();

    void setLoadBalanceAutoCommitStatementThreshold(int paramInt);

    String getLoadBalanceAutoCommitStatementRegex();

    void setLoadBalanceAutoCommitStatementRegex(String paramString);

    String getAuthenticationPlugins();

    void setAuthenticationPlugins(String paramString);

    String getDisabledAuthenticationPlugins();

    void setDisabledAuthenticationPlugins(String paramString);

    String getDefaultAuthenticationPlugin();

    void setDefaultAuthenticationPlugin(String paramString);

    String getParseInfoCacheFactory();

    void setParseInfoCacheFactory(String paramString);

    String getServerConfigCacheFactory();

    void setServerConfigCacheFactory(String paramString);

    boolean getDisconnectOnExpiredPasswords();

    void setDisconnectOnExpiredPasswords(boolean paramBoolean);
}

