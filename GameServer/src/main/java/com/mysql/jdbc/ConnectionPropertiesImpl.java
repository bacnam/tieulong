package com.mysql.jdbc;

import com.mysql.jdbc.log.Log;
import com.mysql.jdbc.log.StandardLogger;

import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.*;

public class ConnectionPropertiesImpl
        implements Serializable, ConnectionProperties {
    protected static final String ZERO_DATETIME_BEHAVIOR_CONVERT_TO_NULL = "convertToNull";
    protected static final String ZERO_DATETIME_BEHAVIOR_EXCEPTION = "exception";
    protected static final String ZERO_DATETIME_BEHAVIOR_ROUND = "round";
    private static final long serialVersionUID = 4257801713007640580L;
    private static final String CONNECTION_AND_AUTH_CATEGORY = Messages.getString("ConnectionProperties.categoryConnectionAuthentication");
    private static final String NETWORK_CATEGORY = Messages.getString("ConnectionProperties.categoryNetworking");
    private static final String DEBUGING_PROFILING_CATEGORY = Messages.getString("ConnectionProperties.categoryDebuggingProfiling");
    private static final String HA_CATEGORY = Messages.getString("ConnectionProperties.categorryHA");
    private static final String MISC_CATEGORY = Messages.getString("ConnectionProperties.categoryMisc");
    private static final String PERFORMANCE_CATEGORY = Messages.getString("ConnectionProperties.categoryPerformance");
    private static final String SECURITY_CATEGORY = Messages.getString("ConnectionProperties.categorySecurity");
    private static final String[] PROPERTY_CATEGORIES = new String[]{CONNECTION_AND_AUTH_CATEGORY, NETWORK_CATEGORY, HA_CATEGORY, SECURITY_CATEGORY, PERFORMANCE_CATEGORY, DEBUGING_PROFILING_CATEGORY, MISC_CATEGORY};
    private static final ArrayList<Field> PROPERTY_LIST = new ArrayList<Field>();
    private static final String STANDARD_LOGGER_NAME = StandardLogger.class.getName();

    static {
        try {
            Field[] declaredFields = ConnectionPropertiesImpl.class.getDeclaredFields();

            for (int i = 0; i < declaredFields.length; i++) {
                if (ConnectionProperty.class.isAssignableFrom(declaredFields[i].getType())) {
                    PROPERTY_LIST.add(declaredFields[i]);
                }
            }
        } catch (Exception ex) {
            RuntimeException rtEx = new RuntimeException();
            rtEx.initCause(ex);

            throw rtEx;
        }
    }

    protected boolean characterEncodingIsAliasForSjis = false;
    protected MemorySizeConnectionProperty largeRowSizeThreshold = new MemorySizeConnectionProperty("largeRowSizeThreshold", 2048, 0, 2147483647, Messages.getString("ConnectionProperties.largeRowSizeThreshold"), "5.1.1", PERFORMANCE_CATEGORY, -2147483648);
    private BooleanConnectionProperty allowLoadLocalInfile = new BooleanConnectionProperty("allowLoadLocalInfile", true, Messages.getString("ConnectionProperties.loadDataLocal"), "3.0.3", SECURITY_CATEGORY, 2147483647);
    private BooleanConnectionProperty allowMultiQueries = new BooleanConnectionProperty("allowMultiQueries", false, Messages.getString("ConnectionProperties.allowMultiQueries"), "3.1.1", SECURITY_CATEGORY, 1);
    private BooleanConnectionProperty allowNanAndInf = new BooleanConnectionProperty("allowNanAndInf", false, Messages.getString("ConnectionProperties.allowNANandINF"), "3.1.5", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty allowUrlInLocalInfile = new BooleanConnectionProperty("allowUrlInLocalInfile", false, Messages.getString("ConnectionProperties.allowUrlInLoadLocal"), "3.1.4", SECURITY_CATEGORY, 2147483647);
    private BooleanConnectionProperty alwaysSendSetIsolation = new BooleanConnectionProperty("alwaysSendSetIsolation", true, Messages.getString("ConnectionProperties.alwaysSendSetIsolation"), "3.1.7", PERFORMANCE_CATEGORY, 2147483647);
    private BooleanConnectionProperty autoClosePStmtStreams = new BooleanConnectionProperty("autoClosePStmtStreams", false, Messages.getString("ConnectionProperties.autoClosePstmtStreams"), "3.1.12", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty autoDeserialize = new BooleanConnectionProperty("autoDeserialize", false, Messages.getString("ConnectionProperties.autoDeserialize"), "3.1.5", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty autoGenerateTestcaseScript = new BooleanConnectionProperty("autoGenerateTestcaseScript", false, Messages.getString("ConnectionProperties.autoGenerateTestcaseScript"), "3.1.9", DEBUGING_PROFILING_CATEGORY, -2147483648);
    private boolean autoGenerateTestcaseScriptAsBoolean = false;
    private BooleanConnectionProperty autoReconnect = new BooleanConnectionProperty("autoReconnect", false, Messages.getString("ConnectionProperties.autoReconnect"), "1.1", HA_CATEGORY, 0);
    private BooleanConnectionProperty autoReconnectForPools = new BooleanConnectionProperty("autoReconnectForPools", false, Messages.getString("ConnectionProperties.autoReconnectForPools"), "3.1.3", HA_CATEGORY, 1);
    private boolean autoReconnectForPoolsAsBoolean = false;
    private MemorySizeConnectionProperty blobSendChunkSize = new MemorySizeConnectionProperty("blobSendChunkSize", 1048576, 1, 2147483647, Messages.getString("ConnectionProperties.blobSendChunkSize"), "3.1.9", PERFORMANCE_CATEGORY, -2147483648);
    private BooleanConnectionProperty autoSlowLog = new BooleanConnectionProperty("autoSlowLog", true, Messages.getString("ConnectionProperties.autoSlowLog"), "5.1.4", DEBUGING_PROFILING_CATEGORY, -2147483648);
    private BooleanConnectionProperty blobsAreStrings = new BooleanConnectionProperty("blobsAreStrings", false, "Should the driver always treat BLOBs as Strings - specifically to work around dubious metadata returned by the server for GROUP BY clauses?", "5.0.8", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty functionsNeverReturnBlobs = new BooleanConnectionProperty("functionsNeverReturnBlobs", false, "Should the driver always treat data from functions returning BLOBs as Strings - specifically to work around dubious metadata returned by the server for GROUP BY clauses?", "5.0.8", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty cacheCallableStatements = new BooleanConnectionProperty("cacheCallableStmts", false, Messages.getString("ConnectionProperties.cacheCallableStatements"), "3.1.2", PERFORMANCE_CATEGORY, -2147483648);
    private BooleanConnectionProperty cachePreparedStatements = new BooleanConnectionProperty("cachePrepStmts", false, Messages.getString("ConnectionProperties.cachePrepStmts"), "3.0.10", PERFORMANCE_CATEGORY, -2147483648);
    private BooleanConnectionProperty cacheResultSetMetadata = new BooleanConnectionProperty("cacheResultSetMetadata", false, Messages.getString("ConnectionProperties.cacheRSMetadata"), "3.1.1", PERFORMANCE_CATEGORY, -2147483648);
    private boolean cacheResultSetMetaDataAsBoolean;
    private StringConnectionProperty serverConfigCacheFactory = new StringConnectionProperty("serverConfigCacheFactory", PerVmServerConfigCacheFactory.class.getName(), Messages.getString("ConnectionProperties.serverConfigCacheFactory"), "5.1.1", PERFORMANCE_CATEGORY, 12);
    private BooleanConnectionProperty cacheServerConfiguration = new BooleanConnectionProperty("cacheServerConfiguration", false, Messages.getString("ConnectionProperties.cacheServerConfiguration"), "3.1.5", PERFORMANCE_CATEGORY, -2147483648);
    private IntegerConnectionProperty callableStatementCacheSize = new IntegerConnectionProperty("callableStmtCacheSize", 100, 0, 2147483647, Messages.getString("ConnectionProperties.callableStmtCacheSize"), "3.1.2", PERFORMANCE_CATEGORY, 5);
    private BooleanConnectionProperty capitalizeTypeNames = new BooleanConnectionProperty("capitalizeTypeNames", true, Messages.getString("ConnectionProperties.capitalizeTypeNames"), "2.0.7", MISC_CATEGORY, -2147483648);
    private StringConnectionProperty characterEncoding = new StringConnectionProperty("characterEncoding", null, Messages.getString("ConnectionProperties.characterEncoding"), "1.1g", MISC_CATEGORY, 5);
    private String characterEncodingAsString = null;
    private StringConnectionProperty characterSetResults = new StringConnectionProperty("characterSetResults", null, Messages.getString("ConnectionProperties.characterSetResults"), "3.0.13", MISC_CATEGORY, 6);
    private StringConnectionProperty clientInfoProvider = new StringConnectionProperty("clientInfoProvider", "com.mysql.jdbc.JDBC4CommentClientInfoProvider", Messages.getString("ConnectionProperties.clientInfoProvider"), "5.1.0", DEBUGING_PROFILING_CATEGORY, -2147483648);
    private BooleanConnectionProperty clobberStreamingResults = new BooleanConnectionProperty("clobberStreamingResults", false, Messages.getString("ConnectionProperties.clobberStreamingResults"), "3.0.9", MISC_CATEGORY, -2147483648);
    private StringConnectionProperty clobCharacterEncoding = new StringConnectionProperty("clobCharacterEncoding", null, Messages.getString("ConnectionProperties.clobCharacterEncoding"), "5.0.0", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty compensateOnDuplicateKeyUpdateCounts = new BooleanConnectionProperty("compensateOnDuplicateKeyUpdateCounts", false, Messages.getString("ConnectionProperties.compensateOnDuplicateKeyUpdateCounts"), "5.1.7", MISC_CATEGORY, -2147483648);
    private StringConnectionProperty connectionCollation = new StringConnectionProperty("connectionCollation", null, Messages.getString("ConnectionProperties.connectionCollation"), "3.0.13", MISC_CATEGORY, 7);
    private StringConnectionProperty connectionLifecycleInterceptors = new StringConnectionProperty("connectionLifecycleInterceptors", null, Messages.getString("ConnectionProperties.connectionLifecycleInterceptors"), "5.1.4", CONNECTION_AND_AUTH_CATEGORY, 2147483647);
    private IntegerConnectionProperty connectTimeout = new IntegerConnectionProperty("connectTimeout", 0, 0, 2147483647, Messages.getString("ConnectionProperties.connectTimeout"), "3.0.1", CONNECTION_AND_AUTH_CATEGORY, 9);
    private BooleanConnectionProperty continueBatchOnError = new BooleanConnectionProperty("continueBatchOnError", true, Messages.getString("ConnectionProperties.continueBatchOnError"), "3.0.3", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty createDatabaseIfNotExist = new BooleanConnectionProperty("createDatabaseIfNotExist", false, Messages.getString("ConnectionProperties.createDatabaseIfNotExist"), "3.1.9", MISC_CATEGORY, -2147483648);
    private IntegerConnectionProperty defaultFetchSize = new IntegerConnectionProperty("defaultFetchSize", 0, Messages.getString("ConnectionProperties.defaultFetchSize"), "3.1.9", PERFORMANCE_CATEGORY, -2147483648);
    private BooleanConnectionProperty detectServerPreparedStmts = new BooleanConnectionProperty("useServerPrepStmts", false, Messages.getString("ConnectionProperties.useServerPrepStmts"), "3.1.0", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty dontTrackOpenResources = new BooleanConnectionProperty("dontTrackOpenResources", false, Messages.getString("ConnectionProperties.dontTrackOpenResources"), "3.1.7", PERFORMANCE_CATEGORY, -2147483648);
    private BooleanConnectionProperty dumpQueriesOnException = new BooleanConnectionProperty("dumpQueriesOnException", false, Messages.getString("ConnectionProperties.dumpQueriesOnException"), "3.1.3", DEBUGING_PROFILING_CATEGORY, -2147483648);
    private BooleanConnectionProperty dynamicCalendars = new BooleanConnectionProperty("dynamicCalendars", false, Messages.getString("ConnectionProperties.dynamicCalendars"), "3.1.5", PERFORMANCE_CATEGORY, -2147483648);
    private BooleanConnectionProperty elideSetAutoCommits = new BooleanConnectionProperty("elideSetAutoCommits", false, Messages.getString("ConnectionProperties.eliseSetAutoCommit"), "3.1.3", PERFORMANCE_CATEGORY, -2147483648);
    private BooleanConnectionProperty emptyStringsConvertToZero = new BooleanConnectionProperty("emptyStringsConvertToZero", true, Messages.getString("ConnectionProperties.emptyStringsConvertToZero"), "3.1.8", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty emulateLocators = new BooleanConnectionProperty("emulateLocators", false, Messages.getString("ConnectionProperties.emulateLocators"), "3.1.0", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty emulateUnsupportedPstmts = new BooleanConnectionProperty("emulateUnsupportedPstmts", true, Messages.getString("ConnectionProperties.emulateUnsupportedPstmts"), "3.1.7", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty enablePacketDebug = new BooleanConnectionProperty("enablePacketDebug", false, Messages.getString("ConnectionProperties.enablePacketDebug"), "3.1.3", DEBUGING_PROFILING_CATEGORY, -2147483648);
    private BooleanConnectionProperty enableQueryTimeouts = new BooleanConnectionProperty("enableQueryTimeouts", true, Messages.getString("ConnectionProperties.enableQueryTimeouts"), "5.0.6", PERFORMANCE_CATEGORY, -2147483648);
    private BooleanConnectionProperty explainSlowQueries = new BooleanConnectionProperty("explainSlowQueries", false, Messages.getString("ConnectionProperties.explainSlowQueries"), "3.1.2", DEBUGING_PROFILING_CATEGORY, -2147483648);
    private StringConnectionProperty exceptionInterceptors = new StringConnectionProperty("exceptionInterceptors", null, Messages.getString("ConnectionProperties.exceptionInterceptors"), "5.1.8", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty failOverReadOnly = new BooleanConnectionProperty("failOverReadOnly", true, Messages.getString("ConnectionProperties.failoverReadOnly"), "3.0.12", HA_CATEGORY, 2);
    private BooleanConnectionProperty gatherPerformanceMetrics = new BooleanConnectionProperty("gatherPerfMetrics", false, Messages.getString("ConnectionProperties.gatherPerfMetrics"), "3.1.2", DEBUGING_PROFILING_CATEGORY, 1);
    private BooleanConnectionProperty generateSimpleParameterMetadata = new BooleanConnectionProperty("generateSimpleParameterMetadata", false, Messages.getString("ConnectionProperties.generateSimpleParameterMetadata"), "5.0.5", MISC_CATEGORY, -2147483648);
    private boolean highAvailabilityAsBoolean = false;
    private BooleanConnectionProperty holdResultsOpenOverStatementClose = new BooleanConnectionProperty("holdResultsOpenOverStatementClose", false, Messages.getString("ConnectionProperties.holdRSOpenOverStmtClose"), "3.1.7", PERFORMANCE_CATEGORY, -2147483648);
    private BooleanConnectionProperty includeInnodbStatusInDeadlockExceptions = new BooleanConnectionProperty("includeInnodbStatusInDeadlockExceptions", false, Messages.getString("ConnectionProperties.includeInnodbStatusInDeadlockExceptions"), "5.0.7", DEBUGING_PROFILING_CATEGORY, -2147483648);
    private BooleanConnectionProperty includeThreadDumpInDeadlockExceptions = new BooleanConnectionProperty("includeThreadDumpInDeadlockExceptions", false, Messages.getString("ConnectionProperties.includeThreadDumpInDeadlockExceptions"), "5.1.15", DEBUGING_PROFILING_CATEGORY, -2147483648);
    private BooleanConnectionProperty includeThreadNamesAsStatementComment = new BooleanConnectionProperty("includeThreadNamesAsStatementComment", false, Messages.getString("ConnectionProperties.includeThreadNamesAsStatementComment"), "5.1.15", DEBUGING_PROFILING_CATEGORY, -2147483648);
    private BooleanConnectionProperty ignoreNonTxTables = new BooleanConnectionProperty("ignoreNonTxTables", false, Messages.getString("ConnectionProperties.ignoreNonTxTables"), "3.0.9", MISC_CATEGORY, -2147483648);
    private IntegerConnectionProperty initialTimeout = new IntegerConnectionProperty("initialTimeout", 2, 1, 2147483647, Messages.getString("ConnectionProperties.initialTimeout"), "1.1", HA_CATEGORY, 5);
    private BooleanConnectionProperty isInteractiveClient = new BooleanConnectionProperty("interactiveClient", false, Messages.getString("ConnectionProperties.interactiveClient"), "3.1.0", CONNECTION_AND_AUTH_CATEGORY, -2147483648);
    private BooleanConnectionProperty jdbcCompliantTruncation = new BooleanConnectionProperty("jdbcCompliantTruncation", true, Messages.getString("ConnectionProperties.jdbcCompliantTruncation"), "3.1.2", MISC_CATEGORY, -2147483648);
    private boolean jdbcCompliantTruncationForReads = this.jdbcCompliantTruncation.getValueAsBoolean();
    private StringConnectionProperty loadBalanceStrategy = new StringConnectionProperty("loadBalanceStrategy", "random", null, Messages.getString("ConnectionProperties.loadBalanceStrategy"), "5.0.6", PERFORMANCE_CATEGORY, -2147483648);
    private IntegerConnectionProperty loadBalanceBlacklistTimeout = new IntegerConnectionProperty("loadBalanceBlacklistTimeout", 0, 0, 2147483647, Messages.getString("ConnectionProperties.loadBalanceBlacklistTimeout"), "5.1.0", MISC_CATEGORY, -2147483648);
    private IntegerConnectionProperty loadBalancePingTimeout = new IntegerConnectionProperty("loadBalancePingTimeout", 0, 0, 2147483647, Messages.getString("ConnectionProperties.loadBalancePingTimeout"), "5.1.13", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty loadBalanceValidateConnectionOnSwapServer = new BooleanConnectionProperty("loadBalanceValidateConnectionOnSwapServer", false, Messages.getString("ConnectionProperties.loadBalanceValidateConnectionOnSwapServer"), "5.1.13", MISC_CATEGORY, -2147483648);
    private StringConnectionProperty loadBalanceConnectionGroup = new StringConnectionProperty("loadBalanceConnectionGroup", null, Messages.getString("ConnectionProperties.loadBalanceConnectionGroup"), "5.1.13", MISC_CATEGORY, -2147483648);
    private StringConnectionProperty loadBalanceExceptionChecker = new StringConnectionProperty("loadBalanceExceptionChecker", "com.mysql.jdbc.StandardLoadBalanceExceptionChecker", null, Messages.getString("ConnectionProperties.loadBalanceExceptionChecker"), "5.1.13", MISC_CATEGORY, -2147483648);
    private StringConnectionProperty loadBalanceSQLStateFailover = new StringConnectionProperty("loadBalanceSQLStateFailover", null, Messages.getString("ConnectionProperties.loadBalanceSQLStateFailover"), "5.1.13", MISC_CATEGORY, -2147483648);
    private StringConnectionProperty loadBalanceSQLExceptionSubclassFailover = new StringConnectionProperty("loadBalanceSQLExceptionSubclassFailover", null, Messages.getString("ConnectionProperties.loadBalanceSQLExceptionSubclassFailover"), "5.1.13", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty loadBalanceEnableJMX = new BooleanConnectionProperty("loadBalanceEnableJMX", false, Messages.getString("ConnectionProperties.loadBalanceEnableJMX"), "5.1.13", MISC_CATEGORY, 2147483647);
    private StringConnectionProperty loadBalanceAutoCommitStatementRegex = new StringConnectionProperty("loadBalanceAutoCommitStatementRegex", null, Messages.getString("ConnectionProperties.loadBalanceAutoCommitStatementRegex"), "5.1.15", MISC_CATEGORY, -2147483648);
    private IntegerConnectionProperty loadBalanceAutoCommitStatementThreshold = new IntegerConnectionProperty("loadBalanceAutoCommitStatementThreshold", 0, 0, 2147483647, Messages.getString("ConnectionProperties.loadBalanceAutoCommitStatementThreshold"), "5.1.15", MISC_CATEGORY, -2147483648);
    private StringConnectionProperty localSocketAddress = new StringConnectionProperty("localSocketAddress", null, Messages.getString("ConnectionProperties.localSocketAddress"), "5.0.5", CONNECTION_AND_AUTH_CATEGORY, -2147483648);
    private MemorySizeConnectionProperty locatorFetchBufferSize = new MemorySizeConnectionProperty("locatorFetchBufferSize", 1048576, 0, 2147483647, Messages.getString("ConnectionProperties.locatorFetchBufferSize"), "3.2.1", PERFORMANCE_CATEGORY, -2147483648);
    private StringConnectionProperty loggerClassName = new StringConnectionProperty("logger", STANDARD_LOGGER_NAME, Messages.getString("ConnectionProperties.logger", new Object[]{Log.class.getName(), STANDARD_LOGGER_NAME}), "3.1.1", DEBUGING_PROFILING_CATEGORY, 0);
    private BooleanConnectionProperty logSlowQueries = new BooleanConnectionProperty("logSlowQueries", false, Messages.getString("ConnectionProperties.logSlowQueries"), "3.1.2", DEBUGING_PROFILING_CATEGORY, -2147483648);
    private BooleanConnectionProperty logXaCommands = new BooleanConnectionProperty("logXaCommands", false, Messages.getString("ConnectionProperties.logXaCommands"), "5.0.5", DEBUGING_PROFILING_CATEGORY, -2147483648);
    private BooleanConnectionProperty maintainTimeStats = new BooleanConnectionProperty("maintainTimeStats", true, Messages.getString("ConnectionProperties.maintainTimeStats"), "3.1.9", PERFORMANCE_CATEGORY, 2147483647);
    private boolean maintainTimeStatsAsBoolean = true;
    private IntegerConnectionProperty maxQuerySizeToLog = new IntegerConnectionProperty("maxQuerySizeToLog", 2048, 0, 2147483647, Messages.getString("ConnectionProperties.maxQuerySizeToLog"), "3.1.3", DEBUGING_PROFILING_CATEGORY, 4);
    private IntegerConnectionProperty maxReconnects = new IntegerConnectionProperty("maxReconnects", 3, 1, 2147483647, Messages.getString("ConnectionProperties.maxReconnects"), "1.1", HA_CATEGORY, 4);
    private IntegerConnectionProperty retriesAllDown = new IntegerConnectionProperty("retriesAllDown", 120, 0, 2147483647, Messages.getString("ConnectionProperties.retriesAllDown"), "5.1.6", HA_CATEGORY, 4);
    private IntegerConnectionProperty maxRows = new IntegerConnectionProperty("maxRows", -1, -1, 2147483647, Messages.getString("ConnectionProperties.maxRows"), Messages.getString("ConnectionProperties.allVersions"), MISC_CATEGORY, -2147483648);
    private int maxRowsAsInt = -1;
    private IntegerConnectionProperty metadataCacheSize = new IntegerConnectionProperty("metadataCacheSize", 50, 1, 2147483647, Messages.getString("ConnectionProperties.metadataCacheSize"), "3.1.1", PERFORMANCE_CATEGORY, 5);
    private IntegerConnectionProperty netTimeoutForStreamingResults = new IntegerConnectionProperty("netTimeoutForStreamingResults", 600, 0, 2147483647, Messages.getString("ConnectionProperties.netTimeoutForStreamingResults"), "5.1.0", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty noAccessToProcedureBodies = new BooleanConnectionProperty("noAccessToProcedureBodies", false, "When determining procedure parameter types for CallableStatements, and the connected user  can't access procedure bodies through \"SHOW CREATE PROCEDURE\" or select on mysql.proc  should the driver instead create basic metadata (all parameters reported as IN VARCHARs, but allowing registerOutParameter() to be called on them anyway) instead  of throwing an exception?", "5.0.3", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty noDatetimeStringSync = new BooleanConnectionProperty("noDatetimeStringSync", false, Messages.getString("ConnectionProperties.noDatetimeStringSync"), "3.1.7", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty noTimezoneConversionForTimeType = new BooleanConnectionProperty("noTimezoneConversionForTimeType", false, Messages.getString("ConnectionProperties.noTzConversionForTimeType"), "5.0.0", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty nullCatalogMeansCurrent = new BooleanConnectionProperty("nullCatalogMeansCurrent", true, Messages.getString("ConnectionProperties.nullCatalogMeansCurrent"), "3.1.8", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty nullNamePatternMatchesAll = new BooleanConnectionProperty("nullNamePatternMatchesAll", true, Messages.getString("ConnectionProperties.nullNamePatternMatchesAll"), "3.1.8", MISC_CATEGORY, -2147483648);
    private IntegerConnectionProperty packetDebugBufferSize = new IntegerConnectionProperty("packetDebugBufferSize", 20, 0, 2147483647, Messages.getString("ConnectionProperties.packetDebugBufferSize"), "3.1.3", DEBUGING_PROFILING_CATEGORY, 7);
    private BooleanConnectionProperty padCharsWithSpace = new BooleanConnectionProperty("padCharsWithSpace", false, Messages.getString("ConnectionProperties.padCharsWithSpace"), "5.0.6", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty paranoid = new BooleanConnectionProperty("paranoid", false, Messages.getString("ConnectionProperties.paranoid"), "3.0.1", SECURITY_CATEGORY, -2147483648);
    private BooleanConnectionProperty pedantic = new BooleanConnectionProperty("pedantic", false, Messages.getString("ConnectionProperties.pedantic"), "3.0.0", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty pinGlobalTxToPhysicalConnection = new BooleanConnectionProperty("pinGlobalTxToPhysicalConnection", false, Messages.getString("ConnectionProperties.pinGlobalTxToPhysicalConnection"), "5.0.1", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty populateInsertRowWithDefaultValues = new BooleanConnectionProperty("populateInsertRowWithDefaultValues", false, Messages.getString("ConnectionProperties.populateInsertRowWithDefaultValues"), "5.0.5", MISC_CATEGORY, -2147483648);
    private IntegerConnectionProperty preparedStatementCacheSize = new IntegerConnectionProperty("prepStmtCacheSize", 25, 0, 2147483647, Messages.getString("ConnectionProperties.prepStmtCacheSize"), "3.0.10", PERFORMANCE_CATEGORY, 10);
    private IntegerConnectionProperty preparedStatementCacheSqlLimit = new IntegerConnectionProperty("prepStmtCacheSqlLimit", 256, 1, 2147483647, Messages.getString("ConnectionProperties.prepStmtCacheSqlLimit"), "3.0.10", PERFORMANCE_CATEGORY, 11);
    private StringConnectionProperty parseInfoCacheFactory = new StringConnectionProperty("parseInfoCacheFactory", PerConnectionLRUFactory.class.getName(), Messages.getString("ConnectionProperties.parseInfoCacheFactory"), "5.1.1", PERFORMANCE_CATEGORY, 12);
    private BooleanConnectionProperty processEscapeCodesForPrepStmts = new BooleanConnectionProperty("processEscapeCodesForPrepStmts", true, Messages.getString("ConnectionProperties.processEscapeCodesForPrepStmts"), "3.1.12", MISC_CATEGORY, -2147483648);
    private StringConnectionProperty profilerEventHandler = new StringConnectionProperty("profilerEventHandler", "com.mysql.jdbc.profiler.LoggingProfilerEventHandler", Messages.getString("ConnectionProperties.profilerEventHandler"), "5.1.6", DEBUGING_PROFILING_CATEGORY, -2147483648);
    private StringConnectionProperty profileSql = new StringConnectionProperty("profileSql", null, Messages.getString("ConnectionProperties.profileSqlDeprecated"), "2.0.14", DEBUGING_PROFILING_CATEGORY, 3);
    private BooleanConnectionProperty profileSQL = new BooleanConnectionProperty("profileSQL", false, Messages.getString("ConnectionProperties.profileSQL"), "3.1.0", DEBUGING_PROFILING_CATEGORY, 1);
    private boolean profileSQLAsBoolean = false;
    private StringConnectionProperty propertiesTransform = new StringConnectionProperty("propertiesTransform", null, Messages.getString("ConnectionProperties.connectionPropertiesTransform"), "3.1.4", CONNECTION_AND_AUTH_CATEGORY, -2147483648);
    private IntegerConnectionProperty queriesBeforeRetryMaster = new IntegerConnectionProperty("queriesBeforeRetryMaster", 50, 1, 2147483647, Messages.getString("ConnectionProperties.queriesBeforeRetryMaster"), "3.0.2", HA_CATEGORY, 7);
    private BooleanConnectionProperty queryTimeoutKillsConnection = new BooleanConnectionProperty("queryTimeoutKillsConnection", false, Messages.getString("ConnectionProperties.queryTimeoutKillsConnection"), "5.1.9", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty reconnectAtTxEnd = new BooleanConnectionProperty("reconnectAtTxEnd", false, Messages.getString("ConnectionProperties.reconnectAtTxEnd"), "3.0.10", HA_CATEGORY, 4);
    private boolean reconnectTxAtEndAsBoolean = false;
    private BooleanConnectionProperty relaxAutoCommit = new BooleanConnectionProperty("relaxAutoCommit", false, Messages.getString("ConnectionProperties.relaxAutoCommit"), "2.0.13", MISC_CATEGORY, -2147483648);
    private IntegerConnectionProperty reportMetricsIntervalMillis = new IntegerConnectionProperty("reportMetricsIntervalMillis", 30000, 0, 2147483647, Messages.getString("ConnectionProperties.reportMetricsIntervalMillis"), "3.1.2", DEBUGING_PROFILING_CATEGORY, 3);
    private BooleanConnectionProperty requireSSL = new BooleanConnectionProperty("requireSSL", false, Messages.getString("ConnectionProperties.requireSSL"), "3.1.0", SECURITY_CATEGORY, 3);
    private StringConnectionProperty resourceId = new StringConnectionProperty("resourceId", null, Messages.getString("ConnectionProperties.resourceId"), "5.0.1", HA_CATEGORY, -2147483648);
    private IntegerConnectionProperty resultSetSizeThreshold = new IntegerConnectionProperty("resultSetSizeThreshold", 100, Messages.getString("ConnectionProperties.resultSetSizeThreshold"), "5.0.5", DEBUGING_PROFILING_CATEGORY, -2147483648);
    private BooleanConnectionProperty retainStatementAfterResultSetClose = new BooleanConnectionProperty("retainStatementAfterResultSetClose", false, Messages.getString("ConnectionProperties.retainStatementAfterResultSetClose"), "3.1.11", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty rewriteBatchedStatements = new BooleanConnectionProperty("rewriteBatchedStatements", false, Messages.getString("ConnectionProperties.rewriteBatchedStatements"), "3.1.13", PERFORMANCE_CATEGORY, -2147483648);
    private BooleanConnectionProperty rollbackOnPooledClose = new BooleanConnectionProperty("rollbackOnPooledClose", true, Messages.getString("ConnectionProperties.rollbackOnPooledClose"), "3.0.15", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty roundRobinLoadBalance = new BooleanConnectionProperty("roundRobinLoadBalance", false, Messages.getString("ConnectionProperties.roundRobinLoadBalance"), "3.1.2", HA_CATEGORY, 5);
    private BooleanConnectionProperty runningCTS13 = new BooleanConnectionProperty("runningCTS13", false, Messages.getString("ConnectionProperties.runningCTS13"), "3.1.7", MISC_CATEGORY, -2147483648);
    private IntegerConnectionProperty secondsBeforeRetryMaster = new IntegerConnectionProperty("secondsBeforeRetryMaster", 30, 1, 2147483647, Messages.getString("ConnectionProperties.secondsBeforeRetryMaster"), "3.0.2", HA_CATEGORY, 8);
    private IntegerConnectionProperty selfDestructOnPingSecondsLifetime = new IntegerConnectionProperty("selfDestructOnPingSecondsLifetime", 0, 0, 2147483647, Messages.getString("ConnectionProperties.selfDestructOnPingSecondsLifetime"), "5.1.6", HA_CATEGORY, 2147483647);
    private IntegerConnectionProperty selfDestructOnPingMaxOperations = new IntegerConnectionProperty("selfDestructOnPingMaxOperations", 0, 0, 2147483647, Messages.getString("ConnectionProperties.selfDestructOnPingMaxOperations"), "5.1.6", HA_CATEGORY, 2147483647);
    private StringConnectionProperty serverTimezone = new StringConnectionProperty("serverTimezone", null, Messages.getString("ConnectionProperties.serverTimezone"), "3.0.2", MISC_CATEGORY, -2147483648);
    private StringConnectionProperty sessionVariables = new StringConnectionProperty("sessionVariables", null, Messages.getString("ConnectionProperties.sessionVariables"), "3.1.8", MISC_CATEGORY, 2147483647);
    private IntegerConnectionProperty slowQueryThresholdMillis = new IntegerConnectionProperty("slowQueryThresholdMillis", 2000, 0, 2147483647, Messages.getString("ConnectionProperties.slowQueryThresholdMillis"), "3.1.2", DEBUGING_PROFILING_CATEGORY, 9);
    private LongConnectionProperty slowQueryThresholdNanos = new LongConnectionProperty("slowQueryThresholdNanos", 0L, Messages.getString("ConnectionProperties.slowQueryThresholdNanos"), "5.0.7", DEBUGING_PROFILING_CATEGORY, 10);
    private StringConnectionProperty socketFactoryClassName = new StringConnectionProperty("socketFactory", StandardSocketFactory.class.getName(), Messages.getString("ConnectionProperties.socketFactory"), "3.0.3", CONNECTION_AND_AUTH_CATEGORY, 4);
    private IntegerConnectionProperty socketTimeout = new IntegerConnectionProperty("socketTimeout", 0, 0, 2147483647, Messages.getString("ConnectionProperties.socketTimeout"), "3.0.1", CONNECTION_AND_AUTH_CATEGORY, 10);
    private StringConnectionProperty statementInterceptors = new StringConnectionProperty("statementInterceptors", null, Messages.getString("ConnectionProperties.statementInterceptors"), "5.1.1", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty strictFloatingPoint = new BooleanConnectionProperty("strictFloatingPoint", false, Messages.getString("ConnectionProperties.strictFloatingPoint"), "3.0.0", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty strictUpdates = new BooleanConnectionProperty("strictUpdates", true, Messages.getString("ConnectionProperties.strictUpdates"), "3.0.4", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty overrideSupportsIntegrityEnhancementFacility = new BooleanConnectionProperty("overrideSupportsIntegrityEnhancementFacility", false, Messages.getString("ConnectionProperties.overrideSupportsIEF"), "3.1.12", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty tcpNoDelay = new BooleanConnectionProperty("tcpNoDelay", Boolean.valueOf("true").booleanValue(), Messages.getString("ConnectionProperties.tcpNoDelay"), "5.0.7", NETWORK_CATEGORY, -2147483648);
    private BooleanConnectionProperty tcpKeepAlive = new BooleanConnectionProperty("tcpKeepAlive", Boolean.valueOf("true").booleanValue(), Messages.getString("ConnectionProperties.tcpKeepAlive"), "5.0.7", NETWORK_CATEGORY, -2147483648);
    private IntegerConnectionProperty tcpRcvBuf = new IntegerConnectionProperty("tcpRcvBuf", Integer.parseInt("0"), 0, 2147483647, Messages.getString("ConnectionProperties.tcpSoRcvBuf"), "5.0.7", NETWORK_CATEGORY, -2147483648);
    private IntegerConnectionProperty tcpSndBuf = new IntegerConnectionProperty("tcpSndBuf", Integer.parseInt("0"), 0, 2147483647, Messages.getString("ConnectionProperties.tcpSoSndBuf"), "5.0.7", NETWORK_CATEGORY, -2147483648);
    private IntegerConnectionProperty tcpTrafficClass = new IntegerConnectionProperty("tcpTrafficClass", Integer.parseInt("0"), 0, 255, Messages.getString("ConnectionProperties.tcpTrafficClass"), "5.0.7", NETWORK_CATEGORY, -2147483648);
    private BooleanConnectionProperty tinyInt1isBit = new BooleanConnectionProperty("tinyInt1isBit", true, Messages.getString("ConnectionProperties.tinyInt1isBit"), "3.0.16", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty traceProtocol = new BooleanConnectionProperty("traceProtocol", false, Messages.getString("ConnectionProperties.traceProtocol"), "3.1.2", DEBUGING_PROFILING_CATEGORY, -2147483648);
    private BooleanConnectionProperty treatUtilDateAsTimestamp = new BooleanConnectionProperty("treatUtilDateAsTimestamp", true, Messages.getString("ConnectionProperties.treatUtilDateAsTimestamp"), "5.0.5", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty transformedBitIsBoolean = new BooleanConnectionProperty("transformedBitIsBoolean", false, Messages.getString("ConnectionProperties.transformedBitIsBoolean"), "3.1.9", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty useBlobToStoreUTF8OutsideBMP = new BooleanConnectionProperty("useBlobToStoreUTF8OutsideBMP", false, Messages.getString("ConnectionProperties.useBlobToStoreUTF8OutsideBMP"), "5.1.3", MISC_CATEGORY, 128);
    private StringConnectionProperty utf8OutsideBmpExcludedColumnNamePattern = new StringConnectionProperty("utf8OutsideBmpExcludedColumnNamePattern", null, Messages.getString("ConnectionProperties.utf8OutsideBmpExcludedColumnNamePattern"), "5.1.3", MISC_CATEGORY, 129);
    private StringConnectionProperty utf8OutsideBmpIncludedColumnNamePattern = new StringConnectionProperty("utf8OutsideBmpIncludedColumnNamePattern", null, Messages.getString("ConnectionProperties.utf8OutsideBmpIncludedColumnNamePattern"), "5.1.3", MISC_CATEGORY, 129);
    private BooleanConnectionProperty useCompression = new BooleanConnectionProperty("useCompression", false, Messages.getString("ConnectionProperties.useCompression"), "3.0.17", CONNECTION_AND_AUTH_CATEGORY, -2147483648);
    private BooleanConnectionProperty useColumnNamesInFindColumn = new BooleanConnectionProperty("useColumnNamesInFindColumn", false, Messages.getString("ConnectionProperties.useColumnNamesInFindColumn"), "5.1.7", MISC_CATEGORY, 2147483647);
    private StringConnectionProperty useConfigs = new StringConnectionProperty("useConfigs", null, Messages.getString("ConnectionProperties.useConfigs"), "3.1.5", CONNECTION_AND_AUTH_CATEGORY, 2147483647);
    private BooleanConnectionProperty useCursorFetch = new BooleanConnectionProperty("useCursorFetch", false, Messages.getString("ConnectionProperties.useCursorFetch"), "5.0.0", PERFORMANCE_CATEGORY, 2147483647);
    private BooleanConnectionProperty useDynamicCharsetInfo = new BooleanConnectionProperty("useDynamicCharsetInfo", true, Messages.getString("ConnectionProperties.useDynamicCharsetInfo"), "5.0.6", PERFORMANCE_CATEGORY, -2147483648);
    private BooleanConnectionProperty useDirectRowUnpack = new BooleanConnectionProperty("useDirectRowUnpack", true, "Use newer result set row unpacking code that skips a copy from network buffers  to a MySQL packet instance and instead reads directly into the result set row data buffers.", "5.1.1", PERFORMANCE_CATEGORY, -2147483648);
    private BooleanConnectionProperty useFastIntParsing = new BooleanConnectionProperty("useFastIntParsing", true, Messages.getString("ConnectionProperties.useFastIntParsing"), "3.1.4", PERFORMANCE_CATEGORY, -2147483648);
    private BooleanConnectionProperty useFastDateParsing = new BooleanConnectionProperty("useFastDateParsing", true, Messages.getString("ConnectionProperties.useFastDateParsing"), "5.0.5", PERFORMANCE_CATEGORY, -2147483648);
    private BooleanConnectionProperty useHostsInPrivileges = new BooleanConnectionProperty("useHostsInPrivileges", true, Messages.getString("ConnectionProperties.useHostsInPrivileges"), "3.0.2", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty useInformationSchema = new BooleanConnectionProperty("useInformationSchema", false, Messages.getString("ConnectionProperties.useInformationSchema"), "5.0.0", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty useJDBCCompliantTimezoneShift = new BooleanConnectionProperty("useJDBCCompliantTimezoneShift", false, Messages.getString("ConnectionProperties.useJDBCCompliantTimezoneShift"), "5.0.0", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty useLocalSessionState = new BooleanConnectionProperty("useLocalSessionState", false, Messages.getString("ConnectionProperties.useLocalSessionState"), "3.1.7", PERFORMANCE_CATEGORY, 5);
    private BooleanConnectionProperty useLocalTransactionState = new BooleanConnectionProperty("useLocalTransactionState", false, Messages.getString("ConnectionProperties.useLocalTransactionState"), "5.1.7", PERFORMANCE_CATEGORY, 6);
    private BooleanConnectionProperty useLegacyDatetimeCode = new BooleanConnectionProperty("useLegacyDatetimeCode", true, Messages.getString("ConnectionProperties.useLegacyDatetimeCode"), "5.1.6", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty useNanosForElapsedTime = new BooleanConnectionProperty("useNanosForElapsedTime", false, Messages.getString("ConnectionProperties.useNanosForElapsedTime"), "5.0.7", DEBUGING_PROFILING_CATEGORY, -2147483648);
    private BooleanConnectionProperty useOldAliasMetadataBehavior = new BooleanConnectionProperty("useOldAliasMetadataBehavior", false, Messages.getString("ConnectionProperties.useOldAliasMetadataBehavior"), "5.0.4", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty useOldUTF8Behavior = new BooleanConnectionProperty("useOldUTF8Behavior", false, Messages.getString("ConnectionProperties.useOldUtf8Behavior"), "3.1.6", MISC_CATEGORY, -2147483648);
    private boolean useOldUTF8BehaviorAsBoolean = false;
    private BooleanConnectionProperty useOnlyServerErrorMessages = new BooleanConnectionProperty("useOnlyServerErrorMessages", true, Messages.getString("ConnectionProperties.useOnlyServerErrorMessages"), "3.0.15", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty useReadAheadInput = new BooleanConnectionProperty("useReadAheadInput", true, Messages.getString("ConnectionProperties.useReadAheadInput"), "3.1.5", PERFORMANCE_CATEGORY, -2147483648);
    private BooleanConnectionProperty useSqlStateCodes = new BooleanConnectionProperty("useSqlStateCodes", true, Messages.getString("ConnectionProperties.useSqlStateCodes"), "3.1.3", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty useSSL = new BooleanConnectionProperty("useSSL", false, Messages.getString("ConnectionProperties.useSSL"), "3.0.2", SECURITY_CATEGORY, 2);
    private BooleanConnectionProperty useSSPSCompatibleTimezoneShift = new BooleanConnectionProperty("useSSPSCompatibleTimezoneShift", false, Messages.getString("ConnectionProperties.useSSPSCompatibleTimezoneShift"), "5.0.5", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty useStreamLengthsInPrepStmts = new BooleanConnectionProperty("useStreamLengthsInPrepStmts", true, Messages.getString("ConnectionProperties.useStreamLengthsInPrepStmts"), "3.0.2", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty useTimezone = new BooleanConnectionProperty("useTimezone", false, Messages.getString("ConnectionProperties.useTimezone"), "3.0.2", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty useUltraDevWorkAround = new BooleanConnectionProperty("ultraDevHack", false, Messages.getString("ConnectionProperties.ultraDevHack"), "2.0.3", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty useUnbufferedInput = new BooleanConnectionProperty("useUnbufferedInput", true, Messages.getString("ConnectionProperties.useUnbufferedInput"), "3.0.11", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty useUnicode = new BooleanConnectionProperty("useUnicode", true, Messages.getString("ConnectionProperties.useUnicode"), "1.1g", MISC_CATEGORY, 0);
    private boolean useUnicodeAsBoolean = true;
    private BooleanConnectionProperty useUsageAdvisor = new BooleanConnectionProperty("useUsageAdvisor", false, Messages.getString("ConnectionProperties.useUsageAdvisor"), "3.1.1", DEBUGING_PROFILING_CATEGORY, 10);
    private boolean useUsageAdvisorAsBoolean = false;
    private BooleanConnectionProperty yearIsDateType = new BooleanConnectionProperty("yearIsDateType", true, Messages.getString("ConnectionProperties.yearIsDateType"), "3.1.9", MISC_CATEGORY, -2147483648);
    private StringConnectionProperty zeroDateTimeBehavior = new StringConnectionProperty("zeroDateTimeBehavior", "exception", new String[]{"exception", "round", "convertToNull"}, Messages.getString("ConnectionProperties.zeroDateTimeBehavior", new Object[]{"exception", "round", "convertToNull"}), "3.1.4", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty useJvmCharsetConverters = new BooleanConnectionProperty("useJvmCharsetConverters", false, Messages.getString("ConnectionProperties.useJvmCharsetConverters"), "5.0.1", PERFORMANCE_CATEGORY, -2147483648);
    private BooleanConnectionProperty useGmtMillisForDatetimes = new BooleanConnectionProperty("useGmtMillisForDatetimes", false, Messages.getString("ConnectionProperties.useGmtMillisForDatetimes"), "3.1.12", MISC_CATEGORY, -2147483648);
    private BooleanConnectionProperty dumpMetadataOnColumnNotFound = new BooleanConnectionProperty("dumpMetadataOnColumnNotFound", false, Messages.getString("ConnectionProperties.dumpMetadataOnColumnNotFound"), "3.1.13", DEBUGING_PROFILING_CATEGORY, -2147483648);
    private StringConnectionProperty clientCertificateKeyStoreUrl = new StringConnectionProperty("clientCertificateKeyStoreUrl", null, Messages.getString("ConnectionProperties.clientCertificateKeyStoreUrl"), "5.1.0", SECURITY_CATEGORY, 5);
    private StringConnectionProperty trustCertificateKeyStoreUrl = new StringConnectionProperty("trustCertificateKeyStoreUrl", null, Messages.getString("ConnectionProperties.trustCertificateKeyStoreUrl"), "5.1.0", SECURITY_CATEGORY, 8);
    private StringConnectionProperty clientCertificateKeyStoreType = new StringConnectionProperty("clientCertificateKeyStoreType", "JKS", Messages.getString("ConnectionProperties.clientCertificateKeyStoreType"), "5.1.0", SECURITY_CATEGORY, 6);
    private StringConnectionProperty clientCertificateKeyStorePassword = new StringConnectionProperty("clientCertificateKeyStorePassword", null, Messages.getString("ConnectionProperties.clientCertificateKeyStorePassword"), "5.1.0", SECURITY_CATEGORY, 7);
    private StringConnectionProperty trustCertificateKeyStoreType = new StringConnectionProperty("trustCertificateKeyStoreType", "JKS", Messages.getString("ConnectionProperties.trustCertificateKeyStoreType"), "5.1.0", SECURITY_CATEGORY, 9);
    private StringConnectionProperty trustCertificateKeyStorePassword = new StringConnectionProperty("trustCertificateKeyStorePassword", null, Messages.getString("ConnectionProperties.trustCertificateKeyStorePassword"), "5.1.0", SECURITY_CATEGORY, 10);
    private BooleanConnectionProperty verifyServerCertificate = new BooleanConnectionProperty("verifyServerCertificate", true, Messages.getString("ConnectionProperties.verifyServerCertificate"), "5.1.6", SECURITY_CATEGORY, 4);
    private BooleanConnectionProperty useAffectedRows = new BooleanConnectionProperty("useAffectedRows", false, Messages.getString("ConnectionProperties.useAffectedRows"), "5.1.7", MISC_CATEGORY, -2147483648);
    private StringConnectionProperty passwordCharacterEncoding = new StringConnectionProperty("passwordCharacterEncoding", null, Messages.getString("ConnectionProperties.passwordCharacterEncoding"), "5.1.7", SECURITY_CATEGORY, -2147483648);
    private IntegerConnectionProperty maxAllowedPacket = new IntegerConnectionProperty("maxAllowedPacket", -1, Messages.getString("ConnectionProperties.maxAllowedPacket"), "5.1.8", NETWORK_CATEGORY, -2147483648);
    private StringConnectionProperty authenticationPlugins = new StringConnectionProperty("authenticationPlugins", null, Messages.getString("ConnectionProperties.authenticationPlugins"), "5.1.19", CONNECTION_AND_AUTH_CATEGORY, -2147483648);
    private StringConnectionProperty disabledAuthenticationPlugins = new StringConnectionProperty("disabledAuthenticationPlugins", null, Messages.getString("ConnectionProperties.disabledAuthenticationPlugins"), "5.1.19", CONNECTION_AND_AUTH_CATEGORY, -2147483648);
    private StringConnectionProperty defaultAuthenticationPlugin = new StringConnectionProperty("defaultAuthenticationPlugin", "com.mysql.jdbc.authentication.MysqlNativePasswordPlugin", Messages.getString("ConnectionProperties.defaultAuthenticationPlugin"), "5.1.19", CONNECTION_AND_AUTH_CATEGORY, -2147483648);
    private BooleanConnectionProperty disconnectOnExpiredPasswords = new BooleanConnectionProperty("disconnectOnExpiredPasswords", true, Messages.getString("ConnectionProperties.disconnectOnExpiredPasswords"), "5.1.23", CONNECTION_AND_AUTH_CATEGORY, -2147483648);

    protected static DriverPropertyInfo[] exposeAsDriverPropertyInfo(Properties info, int slotsToReserve) throws SQLException {
        return (new ConnectionPropertiesImpl() {
            private static final long serialVersionUID = 4257801713007640581L;
        }).exposeAsDriverPropertyInfoInternal(info, slotsToReserve);
    }

    public ExceptionInterceptor getExceptionInterceptor() {
        return null;
    }

    protected DriverPropertyInfo[] exposeAsDriverPropertyInfoInternal(Properties info, int slotsToReserve) throws SQLException {
        initializeProperties(info);

        int numProperties = PROPERTY_LIST.size();

        int listSize = numProperties + slotsToReserve;

        DriverPropertyInfo[] driverProperties = new DriverPropertyInfo[listSize];

        for (int i = slotsToReserve; i < listSize; i++) {
            Field propertyField = PROPERTY_LIST.get(i - slotsToReserve);

            try {
                ConnectionProperty propToExpose = (ConnectionProperty) propertyField.get(this);

                if (info != null) {
                    propToExpose.initializeFrom(info);
                }

                driverProperties[i] = propToExpose.getAsDriverPropertyInfo();
            } catch (IllegalAccessException iae) {
                throw SQLError.createSQLException(Messages.getString("ConnectionProperties.InternalPropertiesFailure"), "S1000", getExceptionInterceptor());
            }
        }

        return driverProperties;
    }

    protected Properties exposeAsProperties(Properties info) throws SQLException {
        if (info == null) {
            info = new Properties();
        }

        int numPropertiesToSet = PROPERTY_LIST.size();

        for (int i = 0; i < numPropertiesToSet; i++) {
            Field propertyField = PROPERTY_LIST.get(i);

            try {
                ConnectionProperty propToGet = (ConnectionProperty) propertyField.get(this);

                Object propValue = propToGet.getValueAsObject();

                if (propValue != null) {
                    info.setProperty(propToGet.getPropertyName(), propValue.toString());
                }
            } catch (IllegalAccessException iae) {
                throw SQLError.createSQLException("Internal properties failure", "S1000", getExceptionInterceptor());
            }
        }

        return info;
    }

    public String exposeAsXml() throws SQLException {
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append("<ConnectionProperties>");

        int numPropertiesToSet = PROPERTY_LIST.size();

        int numCategories = PROPERTY_CATEGORIES.length;

        Map<String, XmlMap> propertyListByCategory = new HashMap<String, XmlMap>();

        for (int i = 0; i < numCategories; i++) {
            propertyListByCategory.put(PROPERTY_CATEGORIES[i], new XmlMap());
        }

        StringConnectionProperty userProp = new StringConnectionProperty("user", null, Messages.getString("ConnectionProperties.Username"), Messages.getString("ConnectionProperties.allVersions"), CONNECTION_AND_AUTH_CATEGORY, -2147483647);

        StringConnectionProperty passwordProp = new StringConnectionProperty("password", null, Messages.getString("ConnectionProperties.Password"), Messages.getString("ConnectionProperties.allVersions"), CONNECTION_AND_AUTH_CATEGORY, -2147483646);

        XmlMap connectionSortMaps = propertyListByCategory.get(CONNECTION_AND_AUTH_CATEGORY);
        TreeMap<String, ConnectionProperty> userMap = new TreeMap<String, ConnectionProperty>();
        userMap.put(userProp.getPropertyName(), userProp);

        connectionSortMaps.ordered.put(Integer.valueOf(userProp.getOrder()), userMap);

        TreeMap<String, ConnectionProperty> passwordMap = new TreeMap<String, ConnectionProperty>();
        passwordMap.put(passwordProp.getPropertyName(), passwordProp);

        connectionSortMaps.ordered.put(new Integer(passwordProp.getOrder()), passwordMap);

        try {
            for (int k = 0; k < numPropertiesToSet; k++) {
                Field propertyField = PROPERTY_LIST.get(k);

                ConnectionProperty propToGet = (ConnectionProperty) propertyField.get(this);

                XmlMap sortMaps = propertyListByCategory.get(propToGet.getCategoryName());

                int orderInCategory = propToGet.getOrder();

                if (orderInCategory == Integer.MIN_VALUE) {
                    sortMaps.alpha.put(propToGet.getPropertyName(), propToGet);
                } else {
                    Integer order = Integer.valueOf(orderInCategory);
                    Map<String, ConnectionProperty> orderMap = sortMaps.ordered.get(order);

                    if (orderMap == null) {
                        orderMap = new TreeMap<String, ConnectionProperty>();
                        sortMaps.ordered.put(order, orderMap);
                    }

                    orderMap.put(propToGet.getPropertyName(), propToGet);
                }
            }

            for (int j = 0; j < numCategories; j++) {
                XmlMap sortMaps = propertyListByCategory.get(PROPERTY_CATEGORIES[j]);

                xmlBuf.append("\n <PropertyCategory name=\"");
                xmlBuf.append(PROPERTY_CATEGORIES[j]);
                xmlBuf.append("\">");

                for (Map<String, ConnectionProperty> orderedEl : sortMaps.ordered.values()) {
                    for (ConnectionProperty propToGet : orderedEl.values()) {
                        xmlBuf.append("\n  <Property name=\"");
                        xmlBuf.append(propToGet.getPropertyName());
                        xmlBuf.append("\" required=\"");
                        xmlBuf.append(propToGet.required ? "Yes" : "No");

                        xmlBuf.append("\" default=\"");

                        if (propToGet.getDefaultValue() != null) {
                            xmlBuf.append(propToGet.getDefaultValue());
                        }

                        xmlBuf.append("\" sortOrder=\"");
                        xmlBuf.append(propToGet.getOrder());
                        xmlBuf.append("\" since=\"");
                        xmlBuf.append(propToGet.sinceVersion);
                        xmlBuf.append("\">\n");
                        xmlBuf.append("    ");
                        String escapedDescription = propToGet.description;
                        escapedDescription = escapedDescription.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");

                        xmlBuf.append(escapedDescription);
                        xmlBuf.append("\n  </Property>");
                    }
                }

                for (ConnectionProperty propToGet : sortMaps.alpha.values()) {
                    xmlBuf.append("\n  <Property name=\"");
                    xmlBuf.append(propToGet.getPropertyName());
                    xmlBuf.append("\" required=\"");
                    xmlBuf.append(propToGet.required ? "Yes" : "No");

                    xmlBuf.append("\" default=\"");

                    if (propToGet.getDefaultValue() != null) {
                        xmlBuf.append(propToGet.getDefaultValue());
                    }

                    xmlBuf.append("\" sortOrder=\"alpha\" since=\"");
                    xmlBuf.append(propToGet.sinceVersion);
                    xmlBuf.append("\">\n");
                    xmlBuf.append("    ");
                    xmlBuf.append(propToGet.description);
                    xmlBuf.append("\n  </Property>");
                }

                xmlBuf.append("\n </PropertyCategory>");
            }
        } catch (IllegalAccessException iae) {
            throw SQLError.createSQLException("Internal properties failure", "S1000", getExceptionInterceptor());
        }

        xmlBuf.append("\n</ConnectionProperties>");

        return xmlBuf.toString();
    }

    public boolean getAllowLoadLocalInfile() {
        return this.allowLoadLocalInfile.getValueAsBoolean();
    }

    public void setAllowLoadLocalInfile(boolean property) {
        this.allowLoadLocalInfile.setValue(property);
    }

    public boolean getAllowMultiQueries() {
        return this.allowMultiQueries.getValueAsBoolean();
    }

    public void setAllowMultiQueries(boolean property) {
        this.allowMultiQueries.setValue(property);
    }

    public boolean getAllowNanAndInf() {
        return this.allowNanAndInf.getValueAsBoolean();
    }

    public void setAllowNanAndInf(boolean flag) {
        this.allowNanAndInf.setValue(flag);
    }

    public boolean getAllowUrlInLocalInfile() {
        return this.allowUrlInLocalInfile.getValueAsBoolean();
    }

    public void setAllowUrlInLocalInfile(boolean flag) {
        this.allowUrlInLocalInfile.setValue(flag);
    }

    public boolean getAlwaysSendSetIsolation() {
        return this.alwaysSendSetIsolation.getValueAsBoolean();
    }

    public void setAlwaysSendSetIsolation(boolean flag) {
        this.alwaysSendSetIsolation.setValue(flag);
    }

    public boolean getAutoDeserialize() {
        return this.autoDeserialize.getValueAsBoolean();
    }

    public void setAutoDeserialize(boolean flag) {
        this.autoDeserialize.setValue(flag);
    }

    public boolean getAutoGenerateTestcaseScript() {
        return this.autoGenerateTestcaseScriptAsBoolean;
    }

    public void setAutoGenerateTestcaseScript(boolean flag) {
        this.autoGenerateTestcaseScript.setValue(flag);
        this.autoGenerateTestcaseScriptAsBoolean = this.autoGenerateTestcaseScript.getValueAsBoolean();
    }

    public boolean getAutoReconnectForPools() {
        return this.autoReconnectForPoolsAsBoolean;
    }

    public void setAutoReconnectForPools(boolean flag) {
        this.autoReconnectForPools.setValue(flag);
    }

    public int getBlobSendChunkSize() {
        return this.blobSendChunkSize.getValueAsInt();
    }

    public void setBlobSendChunkSize(String value) throws SQLException {
        this.blobSendChunkSize.setValue(value);
    }

    public boolean getCacheCallableStatements() {
        return this.cacheCallableStatements.getValueAsBoolean();
    }

    public void setCacheCallableStatements(boolean flag) {
        this.cacheCallableStatements.setValue(flag);
    }

    public boolean getCachePreparedStatements() {
        return ((Boolean) this.cachePreparedStatements.getValueAsObject()).booleanValue();
    }

    public void setCachePreparedStatements(boolean flag) {
        this.cachePreparedStatements.setValue(flag);
    }

    public boolean getCacheResultSetMetadata() {
        return this.cacheResultSetMetaDataAsBoolean;
    }

    public void setCacheResultSetMetadata(boolean property) {
        this.cacheResultSetMetadata.setValue(property);
        this.cacheResultSetMetaDataAsBoolean = this.cacheResultSetMetadata.getValueAsBoolean();
    }

    public boolean getCacheServerConfiguration() {
        return this.cacheServerConfiguration.getValueAsBoolean();
    }

    public void setCacheServerConfiguration(boolean flag) {
        this.cacheServerConfiguration.setValue(flag);
    }

    public int getCallableStatementCacheSize() {
        return this.callableStatementCacheSize.getValueAsInt();
    }

    public void setCallableStatementCacheSize(int size) {
        this.callableStatementCacheSize.setValue(size);
    }

    public boolean getCapitalizeTypeNames() {
        return this.capitalizeTypeNames.getValueAsBoolean();
    }

    public void setCapitalizeTypeNames(boolean flag) {
        this.capitalizeTypeNames.setValue(flag);
    }

    public String getCharacterSetResults() {
        return this.characterSetResults.getValueAsString();
    }

    public void setCharacterSetResults(String characterSet) {
        this.characterSetResults.setValue(characterSet);
    }

    public boolean getClobberStreamingResults() {
        return this.clobberStreamingResults.getValueAsBoolean();
    }

    public void setClobberStreamingResults(boolean flag) {
        this.clobberStreamingResults.setValue(flag);
    }

    public String getClobCharacterEncoding() {
        return this.clobCharacterEncoding.getValueAsString();
    }

    public void setClobCharacterEncoding(String encoding) {
        this.clobCharacterEncoding.setValue(encoding);
    }

    public String getConnectionCollation() {
        return this.connectionCollation.getValueAsString();
    }

    public void setConnectionCollation(String collation) {
        this.connectionCollation.setValue(collation);
    }

    public int getConnectTimeout() {
        return this.connectTimeout.getValueAsInt();
    }

    public void setConnectTimeout(int timeoutMs) {
        this.connectTimeout.setValue(timeoutMs);
    }

    public boolean getContinueBatchOnError() {
        return this.continueBatchOnError.getValueAsBoolean();
    }

    public void setContinueBatchOnError(boolean property) {
        this.continueBatchOnError.setValue(property);
    }

    public boolean getCreateDatabaseIfNotExist() {
        return this.createDatabaseIfNotExist.getValueAsBoolean();
    }

    public void setCreateDatabaseIfNotExist(boolean flag) {
        this.createDatabaseIfNotExist.setValue(flag);
    }

    public int getDefaultFetchSize() {
        return this.defaultFetchSize.getValueAsInt();
    }

    public void setDefaultFetchSize(int n) {
        this.defaultFetchSize.setValue(n);
    }

    public boolean getDontTrackOpenResources() {
        return this.dontTrackOpenResources.getValueAsBoolean();
    }

    public void setDontTrackOpenResources(boolean flag) {
        this.dontTrackOpenResources.setValue(flag);
    }

    public boolean getDumpQueriesOnException() {
        return this.dumpQueriesOnException.getValueAsBoolean();
    }

    public void setDumpQueriesOnException(boolean flag) {
        this.dumpQueriesOnException.setValue(flag);
    }

    public boolean getDynamicCalendars() {
        return this.dynamicCalendars.getValueAsBoolean();
    }

    public void setDynamicCalendars(boolean flag) {
        this.dynamicCalendars.setValue(flag);
    }

    public boolean getElideSetAutoCommits() {
        return this.elideSetAutoCommits.getValueAsBoolean();
    }

    public void setElideSetAutoCommits(boolean flag) {
        this.elideSetAutoCommits.setValue(flag);
    }

    public boolean getEmptyStringsConvertToZero() {
        return this.emptyStringsConvertToZero.getValueAsBoolean();
    }

    public void setEmptyStringsConvertToZero(boolean flag) {
        this.emptyStringsConvertToZero.setValue(flag);
    }

    public boolean getEmulateLocators() {
        return this.emulateLocators.getValueAsBoolean();
    }

    public void setEmulateLocators(boolean property) {
        this.emulateLocators.setValue(property);
    }

    public boolean getEmulateUnsupportedPstmts() {
        return this.emulateUnsupportedPstmts.getValueAsBoolean();
    }

    public void setEmulateUnsupportedPstmts(boolean flag) {
        this.emulateUnsupportedPstmts.setValue(flag);
    }

    public boolean getEnablePacketDebug() {
        return this.enablePacketDebug.getValueAsBoolean();
    }

    public void setEnablePacketDebug(boolean flag) {
        this.enablePacketDebug.setValue(flag);
    }

    public String getEncoding() {
        return this.characterEncodingAsString;
    }

    public void setEncoding(String property) {
        this.characterEncoding.setValue(property);
        this.characterEncodingAsString = this.characterEncoding.getValueAsString();

        this.characterEncodingIsAliasForSjis = CharsetMapping.isAliasForSjis(this.characterEncodingAsString);
    }

    public boolean getExplainSlowQueries() {
        return this.explainSlowQueries.getValueAsBoolean();
    }

    public void setExplainSlowQueries(boolean flag) {
        this.explainSlowQueries.setValue(flag);
    }

    public boolean getFailOverReadOnly() {
        return this.failOverReadOnly.getValueAsBoolean();
    }

    public void setFailOverReadOnly(boolean flag) {
        this.failOverReadOnly.setValue(flag);
    }

    public boolean getGatherPerformanceMetrics() {
        return this.gatherPerformanceMetrics.getValueAsBoolean();
    }

    public void setGatherPerformanceMetrics(boolean flag) {
        this.gatherPerformanceMetrics.setValue(flag);
    }

    protected boolean getHighAvailability() {
        return this.highAvailabilityAsBoolean;
    }

    protected void setHighAvailability(boolean property) {
        this.autoReconnect.setValue(property);
        this.highAvailabilityAsBoolean = this.autoReconnect.getValueAsBoolean();
    }

    public boolean getHoldResultsOpenOverStatementClose() {
        return this.holdResultsOpenOverStatementClose.getValueAsBoolean();
    }

    public void setHoldResultsOpenOverStatementClose(boolean flag) {
        this.holdResultsOpenOverStatementClose.setValue(flag);
    }

    public boolean getIgnoreNonTxTables() {
        return this.ignoreNonTxTables.getValueAsBoolean();
    }

    public void setIgnoreNonTxTables(boolean property) {
        this.ignoreNonTxTables.setValue(property);
    }

    public int getInitialTimeout() {
        return this.initialTimeout.getValueAsInt();
    }

    public void setInitialTimeout(int property) {
        this.initialTimeout.setValue(property);
    }

    public boolean getInteractiveClient() {
        return this.isInteractiveClient.getValueAsBoolean();
    }

    public void setInteractiveClient(boolean property) {
        setIsInteractiveClient(property);
    }

    public boolean getIsInteractiveClient() {
        return this.isInteractiveClient.getValueAsBoolean();
    }

    public void setIsInteractiveClient(boolean property) {
        this.isInteractiveClient.setValue(property);
    }

    public boolean getJdbcCompliantTruncation() {
        return this.jdbcCompliantTruncation.getValueAsBoolean();
    }

    public void setJdbcCompliantTruncation(boolean flag) {
        this.jdbcCompliantTruncation.setValue(flag);
    }

    public int getLocatorFetchBufferSize() {
        return this.locatorFetchBufferSize.getValueAsInt();
    }

    public void setLocatorFetchBufferSize(String value) throws SQLException {
        this.locatorFetchBufferSize.setValue(value);
    }

    public String getLogger() {
        return this.loggerClassName.getValueAsString();
    }

    public void setLogger(String property) {
        this.loggerClassName.setValueAsObject(property);
    }

    public String getLoggerClassName() {
        return this.loggerClassName.getValueAsString();
    }

    public void setLoggerClassName(String className) {
        this.loggerClassName.setValue(className);
    }

    public boolean getLogSlowQueries() {
        return this.logSlowQueries.getValueAsBoolean();
    }

    public void setLogSlowQueries(boolean flag) {
        this.logSlowQueries.setValue(flag);
    }

    public boolean getMaintainTimeStats() {
        return this.maintainTimeStatsAsBoolean;
    }

    public void setMaintainTimeStats(boolean flag) {
        this.maintainTimeStats.setValue(flag);
        this.maintainTimeStatsAsBoolean = this.maintainTimeStats.getValueAsBoolean();
    }

    public int getMaxQuerySizeToLog() {
        return this.maxQuerySizeToLog.getValueAsInt();
    }

    public void setMaxQuerySizeToLog(int sizeInBytes) {
        this.maxQuerySizeToLog.setValue(sizeInBytes);
    }

    public int getMaxReconnects() {
        return this.maxReconnects.getValueAsInt();
    }

    public void setMaxReconnects(int property) {
        this.maxReconnects.setValue(property);
    }

    public int getMaxRows() {
        return this.maxRowsAsInt;
    }

    public void setMaxRows(int property) {
        this.maxRows.setValue(property);
        this.maxRowsAsInt = this.maxRows.getValueAsInt();
    }

    public int getMetadataCacheSize() {
        return this.metadataCacheSize.getValueAsInt();
    }

    public void setMetadataCacheSize(int value) {
        this.metadataCacheSize.setValue(value);
    }

    public boolean getNoDatetimeStringSync() {
        return this.noDatetimeStringSync.getValueAsBoolean();
    }

    public void setNoDatetimeStringSync(boolean flag) {
        this.noDatetimeStringSync.setValue(flag);
    }

    public boolean getNullCatalogMeansCurrent() {
        return this.nullCatalogMeansCurrent.getValueAsBoolean();
    }

    public void setNullCatalogMeansCurrent(boolean value) {
        this.nullCatalogMeansCurrent.setValue(value);
    }

    public boolean getNullNamePatternMatchesAll() {
        return this.nullNamePatternMatchesAll.getValueAsBoolean();
    }

    public void setNullNamePatternMatchesAll(boolean value) {
        this.nullNamePatternMatchesAll.setValue(value);
    }

    public int getPacketDebugBufferSize() {
        return this.packetDebugBufferSize.getValueAsInt();
    }

    public void setPacketDebugBufferSize(int size) {
        this.packetDebugBufferSize.setValue(size);
    }

    public boolean getParanoid() {
        return this.paranoid.getValueAsBoolean();
    }

    public void setParanoid(boolean property) {
        this.paranoid.setValue(property);
    }

    public boolean getPedantic() {
        return this.pedantic.getValueAsBoolean();
    }

    public void setPedantic(boolean property) {
        this.pedantic.setValue(property);
    }

    public int getPreparedStatementCacheSize() {
        return ((Integer) this.preparedStatementCacheSize.getValueAsObject()).intValue();
    }

    public void setPreparedStatementCacheSize(int cacheSize) {
        this.preparedStatementCacheSize.setValue(cacheSize);
    }

    public int getPreparedStatementCacheSqlLimit() {
        return ((Integer) this.preparedStatementCacheSqlLimit.getValueAsObject()).intValue();
    }

    public void setPreparedStatementCacheSqlLimit(int cacheSqlLimit) {
        this.preparedStatementCacheSqlLimit.setValue(cacheSqlLimit);
    }

    public boolean getProfileSql() {
        return this.profileSQLAsBoolean;
    }

    public void setProfileSql(boolean property) {
        this.profileSQL.setValue(property);
        this.profileSQLAsBoolean = this.profileSQL.getValueAsBoolean();
    }

    public boolean getProfileSQL() {
        return this.profileSQL.getValueAsBoolean();
    }

    public void setProfileSQL(boolean flag) {
        this.profileSQL.setValue(flag);
    }

    public String getPropertiesTransform() {
        return this.propertiesTransform.getValueAsString();
    }

    public void setPropertiesTransform(String value) {
        this.propertiesTransform.setValue(value);
    }

    public int getQueriesBeforeRetryMaster() {
        return this.queriesBeforeRetryMaster.getValueAsInt();
    }

    public void setQueriesBeforeRetryMaster(int property) {
        this.queriesBeforeRetryMaster.setValue(property);
    }

    public boolean getReconnectAtTxEnd() {
        return this.reconnectTxAtEndAsBoolean;
    }

    public void setReconnectAtTxEnd(boolean property) {
        this.reconnectAtTxEnd.setValue(property);
        this.reconnectTxAtEndAsBoolean = this.reconnectAtTxEnd.getValueAsBoolean();
    }

    public boolean getRelaxAutoCommit() {
        return this.relaxAutoCommit.getValueAsBoolean();
    }

    public void setRelaxAutoCommit(boolean property) {
        this.relaxAutoCommit.setValue(property);
    }

    public int getReportMetricsIntervalMillis() {
        return this.reportMetricsIntervalMillis.getValueAsInt();
    }

    public void setReportMetricsIntervalMillis(int millis) {
        this.reportMetricsIntervalMillis.setValue(millis);
    }

    public boolean getRequireSSL() {
        return this.requireSSL.getValueAsBoolean();
    }

    public void setRequireSSL(boolean property) {
        this.requireSSL.setValue(property);
    }

    public boolean getRetainStatementAfterResultSetClose() {
        return this.retainStatementAfterResultSetClose.getValueAsBoolean();
    }

    public void setRetainStatementAfterResultSetClose(boolean flag) {
        this.retainStatementAfterResultSetClose.setValue(flag);
    }

    public boolean getRollbackOnPooledClose() {
        return this.rollbackOnPooledClose.getValueAsBoolean();
    }

    public void setRollbackOnPooledClose(boolean flag) {
        this.rollbackOnPooledClose.setValue(flag);
    }

    public boolean getRoundRobinLoadBalance() {
        return this.roundRobinLoadBalance.getValueAsBoolean();
    }

    public void setRoundRobinLoadBalance(boolean flag) {
        this.roundRobinLoadBalance.setValue(flag);
    }

    public boolean getRunningCTS13() {
        return this.runningCTS13.getValueAsBoolean();
    }

    public void setRunningCTS13(boolean flag) {
        this.runningCTS13.setValue(flag);
    }

    public int getSecondsBeforeRetryMaster() {
        return this.secondsBeforeRetryMaster.getValueAsInt();
    }

    public void setSecondsBeforeRetryMaster(int property) {
        this.secondsBeforeRetryMaster.setValue(property);
    }

    public String getServerTimezone() {
        return this.serverTimezone.getValueAsString();
    }

    public void setServerTimezone(String property) {
        this.serverTimezone.setValue(property);
    }

    public String getSessionVariables() {
        return this.sessionVariables.getValueAsString();
    }

    public void setSessionVariables(String variables) {
        this.sessionVariables.setValue(variables);
    }

    public int getSlowQueryThresholdMillis() {
        return this.slowQueryThresholdMillis.getValueAsInt();
    }

    public void setSlowQueryThresholdMillis(int millis) {
        this.slowQueryThresholdMillis.setValue(millis);
    }

    public String getSocketFactoryClassName() {
        return this.socketFactoryClassName.getValueAsString();
    }

    public void setSocketFactoryClassName(String property) {
        this.socketFactoryClassName.setValue(property);
    }

    public int getSocketTimeout() {
        return this.socketTimeout.getValueAsInt();
    }

    public void setSocketTimeout(int property) {
        this.socketTimeout.setValue(property);
    }

    public boolean getStrictFloatingPoint() {
        return this.strictFloatingPoint.getValueAsBoolean();
    }

    public void setStrictFloatingPoint(boolean property) {
        this.strictFloatingPoint.setValue(property);
    }

    public boolean getStrictUpdates() {
        return this.strictUpdates.getValueAsBoolean();
    }

    public void setStrictUpdates(boolean property) {
        this.strictUpdates.setValue(property);
    }

    public boolean getTinyInt1isBit() {
        return this.tinyInt1isBit.getValueAsBoolean();
    }

    public void setTinyInt1isBit(boolean flag) {
        this.tinyInt1isBit.setValue(flag);
    }

    public boolean getTraceProtocol() {
        return this.traceProtocol.getValueAsBoolean();
    }

    public void setTraceProtocol(boolean flag) {
        this.traceProtocol.setValue(flag);
    }

    public boolean getTransformedBitIsBoolean() {
        return this.transformedBitIsBoolean.getValueAsBoolean();
    }

    public void setTransformedBitIsBoolean(boolean flag) {
        this.transformedBitIsBoolean.setValue(flag);
    }

    public boolean getUseCompression() {
        return this.useCompression.getValueAsBoolean();
    }

    public void setUseCompression(boolean property) {
        this.useCompression.setValue(property);
    }

    public boolean getUseFastIntParsing() {
        return this.useFastIntParsing.getValueAsBoolean();
    }

    public void setUseFastIntParsing(boolean flag) {
        this.useFastIntParsing.setValue(flag);
    }

    public boolean getUseHostsInPrivileges() {
        return this.useHostsInPrivileges.getValueAsBoolean();
    }

    public void setUseHostsInPrivileges(boolean property) {
        this.useHostsInPrivileges.setValue(property);
    }

    public boolean getUseInformationSchema() {
        return this.useInformationSchema.getValueAsBoolean();
    }

    public void setUseInformationSchema(boolean flag) {
        this.useInformationSchema.setValue(flag);
    }

    public boolean getUseLocalSessionState() {
        return this.useLocalSessionState.getValueAsBoolean();
    }

    public void setUseLocalSessionState(boolean flag) {
        this.useLocalSessionState.setValue(flag);
    }

    public boolean getUseOldUTF8Behavior() {
        return this.useOldUTF8BehaviorAsBoolean;
    }

    public void setUseOldUTF8Behavior(boolean flag) {
        this.useOldUTF8Behavior.setValue(flag);
        this.useOldUTF8BehaviorAsBoolean = this.useOldUTF8Behavior.getValueAsBoolean();
    }

    public boolean getUseOnlyServerErrorMessages() {
        return this.useOnlyServerErrorMessages.getValueAsBoolean();
    }

    public void setUseOnlyServerErrorMessages(boolean flag) {
        this.useOnlyServerErrorMessages.setValue(flag);
    }

    public boolean getUseReadAheadInput() {
        return this.useReadAheadInput.getValueAsBoolean();
    }

    public void setUseReadAheadInput(boolean flag) {
        this.useReadAheadInput.setValue(flag);
    }

    public boolean getUseServerPreparedStmts() {
        return this.detectServerPreparedStmts.getValueAsBoolean();
    }

    public void setUseServerPreparedStmts(boolean flag) {
        this.detectServerPreparedStmts.setValue(flag);
    }

    public boolean getUseSqlStateCodes() {
        return this.useSqlStateCodes.getValueAsBoolean();
    }

    public void setUseSqlStateCodes(boolean flag) {
        this.useSqlStateCodes.setValue(flag);
    }

    public boolean getUseSSL() {
        return this.useSSL.getValueAsBoolean();
    }

    public void setUseSSL(boolean property) {
        this.useSSL.setValue(property);
    }

    public boolean getUseStreamLengthsInPrepStmts() {
        return this.useStreamLengthsInPrepStmts.getValueAsBoolean();
    }

    public void setUseStreamLengthsInPrepStmts(boolean property) {
        this.useStreamLengthsInPrepStmts.setValue(property);
    }

    public boolean getUseTimezone() {
        return this.useTimezone.getValueAsBoolean();
    }

    public void setUseTimezone(boolean property) {
        this.useTimezone.setValue(property);
    }

    public boolean getUseUltraDevWorkAround() {
        return this.useUltraDevWorkAround.getValueAsBoolean();
    }

    public void setUseUltraDevWorkAround(boolean property) {
        this.useUltraDevWorkAround.setValue(property);
    }

    public boolean getUseUnbufferedInput() {
        return this.useUnbufferedInput.getValueAsBoolean();
    }

    public void setUseUnbufferedInput(boolean flag) {
        this.useUnbufferedInput.setValue(flag);
    }

    public boolean getUseUnicode() {
        return this.useUnicodeAsBoolean;
    }

    public void setUseUnicode(boolean flag) {
        this.useUnicode.setValue(flag);
        this.useUnicodeAsBoolean = this.useUnicode.getValueAsBoolean();
    }

    public boolean getUseUsageAdvisor() {
        return this.useUsageAdvisorAsBoolean;
    }

    public void setUseUsageAdvisor(boolean useUsageAdvisorFlag) {
        this.useUsageAdvisor.setValue(useUsageAdvisorFlag);
        this.useUsageAdvisorAsBoolean = this.useUsageAdvisor.getValueAsBoolean();
    }

    public boolean getYearIsDateType() {
        return this.yearIsDateType.getValueAsBoolean();
    }

    public void setYearIsDateType(boolean flag) {
        this.yearIsDateType.setValue(flag);
    }

    public String getZeroDateTimeBehavior() {
        return this.zeroDateTimeBehavior.getValueAsString();
    }

    public void setZeroDateTimeBehavior(String behavior) {
        this.zeroDateTimeBehavior.setValue(behavior);
    }

    protected void initializeFromRef(Reference ref) throws SQLException {
        int numPropertiesToSet = PROPERTY_LIST.size();

        for (int i = 0; i < numPropertiesToSet; i++) {
            Field propertyField = PROPERTY_LIST.get(i);

            try {
                ConnectionProperty propToSet = (ConnectionProperty) propertyField.get(this);

                if (ref != null) {
                    propToSet.initializeFrom(ref);
                }
            } catch (IllegalAccessException iae) {
                throw SQLError.createSQLException("Internal properties failure", "S1000", getExceptionInterceptor());
            }
        }

        postInitialization();
    }

    protected void initializeProperties(Properties info) throws SQLException {
        if (info != null) {

            String profileSqlLc = info.getProperty("profileSql");

            if (profileSqlLc != null) {
                info.put("profileSQL", profileSqlLc);
            }

            Properties infoCopy = (Properties) info.clone();

            infoCopy.remove("HOST");
            infoCopy.remove("user");
            infoCopy.remove("password");
            infoCopy.remove("DBNAME");
            infoCopy.remove("PORT");
            infoCopy.remove("profileSql");

            int numPropertiesToSet = PROPERTY_LIST.size();

            for (int i = 0; i < numPropertiesToSet; i++) {
                Field propertyField = PROPERTY_LIST.get(i);

                try {
                    ConnectionProperty propToSet = (ConnectionProperty) propertyField.get(this);

                    propToSet.initializeFrom(infoCopy);
                } catch (IllegalAccessException iae) {
                    throw SQLError.createSQLException(Messages.getString("ConnectionProperties.unableToInitDriverProperties") + iae.toString(), "S1000", getExceptionInterceptor());
                }
            }

            postInitialization();
        }
    }

    protected void postInitialization() throws SQLException {
        if (this.profileSql.getValueAsObject() != null) {
            this.profileSQL.initializeFrom(this.profileSql.getValueAsObject().toString());
        }

        this.reconnectTxAtEndAsBoolean = ((Boolean) this.reconnectAtTxEnd.getValueAsObject()).booleanValue();

        if (getMaxRows() == 0) {

            this.maxRows.setValueAsObject(Integer.valueOf(-1));
        }

        String testEncoding = getEncoding();

        if (testEncoding != null) {

            try {

                String testString = "abc";
                StringUtils.getBytes(testString, testEncoding);
            } catch (UnsupportedEncodingException UE) {
                throw SQLError.createSQLException(Messages.getString("ConnectionProperties.unsupportedCharacterEncoding", new Object[]{testEncoding}), "0S100", getExceptionInterceptor());
            }
        }

        if (((Boolean) this.cacheResultSetMetadata.getValueAsObject()).booleanValue()) {

            try {
                Class.forName("java.util.LinkedHashMap");
            } catch (ClassNotFoundException cnfe) {
                this.cacheResultSetMetadata.setValue(false);
            }
        }

        this.cacheResultSetMetaDataAsBoolean = this.cacheResultSetMetadata.getValueAsBoolean();

        this.useUnicodeAsBoolean = this.useUnicode.getValueAsBoolean();
        this.characterEncodingAsString = (String) this.characterEncoding.getValueAsObject();

        this.characterEncodingIsAliasForSjis = CharsetMapping.isAliasForSjis(this.characterEncodingAsString);
        this.highAvailabilityAsBoolean = this.autoReconnect.getValueAsBoolean();
        this.autoReconnectForPoolsAsBoolean = this.autoReconnectForPools.getValueAsBoolean();

        this.maxRowsAsInt = ((Integer) this.maxRows.getValueAsObject()).intValue();

        this.profileSQLAsBoolean = this.profileSQL.getValueAsBoolean();
        this.useUsageAdvisorAsBoolean = this.useUsageAdvisor.getValueAsBoolean();

        this.useOldUTF8BehaviorAsBoolean = this.useOldUTF8Behavior.getValueAsBoolean();

        this.autoGenerateTestcaseScriptAsBoolean = this.autoGenerateTestcaseScript.getValueAsBoolean();

        this.maintainTimeStatsAsBoolean = this.maintainTimeStats.getValueAsBoolean();

        this.jdbcCompliantTruncationForReads = getJdbcCompliantTruncation();

        if (getUseCursorFetch()) {

            setDetectServerPreparedStmts(true);
        }
    }

    public void setAutoReconnect(boolean flag) {
        this.autoReconnect.setValue(flag);
    }

    public void setAutoReconnectForConnectionPools(boolean property) {
        this.autoReconnectForPools.setValue(property);
        this.autoReconnectForPoolsAsBoolean = this.autoReconnectForPools.getValueAsBoolean();
    }

    public void setCapitalizeDBMDTypes(boolean property) {
        this.capitalizeTypeNames.setValue(property);
    }

    public void setCharacterEncoding(String encoding) {
        this.characterEncoding.setValue(encoding);
    }

    public void setDetectServerPreparedStmts(boolean property) {
        this.detectServerPreparedStmts.setValue(property);
    }

    protected void storeToRef(Reference ref) throws SQLException {
        int numPropertiesToSet = PROPERTY_LIST.size();

        for (int i = 0; i < numPropertiesToSet; i++) {
            Field propertyField = PROPERTY_LIST.get(i);

            try {
                ConnectionProperty propToStore = (ConnectionProperty) propertyField.get(this);

                if (ref != null) {
                    propToStore.storeTo(ref);
                }
            } catch (IllegalAccessException iae) {
                throw SQLError.createSQLException(Messages.getString("ConnectionProperties.errorNotExpected"), getExceptionInterceptor());
            }
        }
    }

    public boolean useUnbufferedInput() {
        return this.useUnbufferedInput.getValueAsBoolean();
    }

    public boolean getUseCursorFetch() {
        return this.useCursorFetch.getValueAsBoolean();
    }

    public void setUseCursorFetch(boolean flag) {
        this.useCursorFetch.setValue(flag);
    }

    public boolean getOverrideSupportsIntegrityEnhancementFacility() {
        return this.overrideSupportsIntegrityEnhancementFacility.getValueAsBoolean();
    }

    public void setOverrideSupportsIntegrityEnhancementFacility(boolean flag) {
        this.overrideSupportsIntegrityEnhancementFacility.setValue(flag);
    }

    public boolean getNoTimezoneConversionForTimeType() {
        return this.noTimezoneConversionForTimeType.getValueAsBoolean();
    }

    public void setNoTimezoneConversionForTimeType(boolean flag) {
        this.noTimezoneConversionForTimeType.setValue(flag);
    }

    public boolean getUseJDBCCompliantTimezoneShift() {
        return this.useJDBCCompliantTimezoneShift.getValueAsBoolean();
    }

    public void setUseJDBCCompliantTimezoneShift(boolean flag) {
        this.useJDBCCompliantTimezoneShift.setValue(flag);
    }

    public boolean getAutoClosePStmtStreams() {
        return this.autoClosePStmtStreams.getValueAsBoolean();
    }

    public void setAutoClosePStmtStreams(boolean flag) {
        this.autoClosePStmtStreams.setValue(flag);
    }

    public boolean getProcessEscapeCodesForPrepStmts() {
        return this.processEscapeCodesForPrepStmts.getValueAsBoolean();
    }

    public void setProcessEscapeCodesForPrepStmts(boolean flag) {
        this.processEscapeCodesForPrepStmts.setValue(flag);
    }

    public boolean getUseGmtMillisForDatetimes() {
        return this.useGmtMillisForDatetimes.getValueAsBoolean();
    }

    public void setUseGmtMillisForDatetimes(boolean flag) {
        this.useGmtMillisForDatetimes.setValue(flag);
    }

    public boolean getDumpMetadataOnColumnNotFound() {
        return this.dumpMetadataOnColumnNotFound.getValueAsBoolean();
    }

    public void setDumpMetadataOnColumnNotFound(boolean flag) {
        this.dumpMetadataOnColumnNotFound.setValue(flag);
    }

    public String getResourceId() {
        return this.resourceId.getValueAsString();
    }

    public void setResourceId(String resourceId) {
        this.resourceId.setValue(resourceId);
    }

    public boolean getRewriteBatchedStatements() {
        return this.rewriteBatchedStatements.getValueAsBoolean();
    }

    public void setRewriteBatchedStatements(boolean flag) {
        this.rewriteBatchedStatements.setValue(flag);
    }

    public boolean getJdbcCompliantTruncationForReads() {
        return this.jdbcCompliantTruncationForReads;
    }

    public void setJdbcCompliantTruncationForReads(boolean jdbcCompliantTruncationForReads) {
        this.jdbcCompliantTruncationForReads = jdbcCompliantTruncationForReads;
    }

    public boolean getUseJvmCharsetConverters() {
        return this.useJvmCharsetConverters.getValueAsBoolean();
    }

    public void setUseJvmCharsetConverters(boolean flag) {
        this.useJvmCharsetConverters.setValue(flag);
    }

    public boolean getPinGlobalTxToPhysicalConnection() {
        return this.pinGlobalTxToPhysicalConnection.getValueAsBoolean();
    }

    public void setPinGlobalTxToPhysicalConnection(boolean flag) {
        this.pinGlobalTxToPhysicalConnection.setValue(flag);
    }

    public boolean getGatherPerfMetrics() {
        return getGatherPerformanceMetrics();
    }

    public void setGatherPerfMetrics(boolean flag) {
        setGatherPerformanceMetrics(flag);
    }

    public boolean getUltraDevHack() {
        return getUseUltraDevWorkAround();
    }

    public void setUltraDevHack(boolean flag) {
        setUseUltraDevWorkAround(flag);
    }

    public String getSocketFactory() {
        return getSocketFactoryClassName();
    }

    public void setSocketFactory(String name) {
        setSocketFactoryClassName(name);
    }

    public boolean getUseServerPrepStmts() {
        return getUseServerPreparedStmts();
    }

    public void setUseServerPrepStmts(boolean flag) {
        setUseServerPreparedStmts(flag);
    }

    public boolean getCacheCallableStmts() {
        return getCacheCallableStatements();
    }

    public void setCacheCallableStmts(boolean flag) {
        setCacheCallableStatements(flag);
    }

    public boolean getCachePrepStmts() {
        return getCachePreparedStatements();
    }

    public void setCachePrepStmts(boolean flag) {
        setCachePreparedStatements(flag);
    }

    public int getCallableStmtCacheSize() {
        return getCallableStatementCacheSize();
    }

    public void setCallableStmtCacheSize(int cacheSize) {
        setCallableStatementCacheSize(cacheSize);
    }

    public int getPrepStmtCacheSize() {
        return getPreparedStatementCacheSize();
    }

    public void setPrepStmtCacheSize(int cacheSize) {
        setPreparedStatementCacheSize(cacheSize);
    }

    public int getPrepStmtCacheSqlLimit() {
        return getPreparedStatementCacheSqlLimit();
    }

    public void setPrepStmtCacheSqlLimit(int sqlLimit) {
        setPreparedStatementCacheSqlLimit(sqlLimit);
    }

    public boolean getNoAccessToProcedureBodies() {
        return this.noAccessToProcedureBodies.getValueAsBoolean();
    }

    public void setNoAccessToProcedureBodies(boolean flag) {
        this.noAccessToProcedureBodies.setValue(flag);
    }

    public boolean getUseOldAliasMetadataBehavior() {
        return this.useOldAliasMetadataBehavior.getValueAsBoolean();
    }

    public void setUseOldAliasMetadataBehavior(boolean flag) {
        this.useOldAliasMetadataBehavior.setValue(flag);
    }

    public String getClientCertificateKeyStorePassword() {
        return this.clientCertificateKeyStorePassword.getValueAsString();
    }

    public void setClientCertificateKeyStorePassword(String value) {
        this.clientCertificateKeyStorePassword.setValue(value);
    }

    public String getClientCertificateKeyStoreType() {
        return this.clientCertificateKeyStoreType.getValueAsString();
    }

    public void setClientCertificateKeyStoreType(String value) {
        this.clientCertificateKeyStoreType.setValue(value);
    }

    public String getClientCertificateKeyStoreUrl() {
        return this.clientCertificateKeyStoreUrl.getValueAsString();
    }

    public void setClientCertificateKeyStoreUrl(String value) {
        this.clientCertificateKeyStoreUrl.setValue(value);
    }

    public String getTrustCertificateKeyStorePassword() {
        return this.trustCertificateKeyStorePassword.getValueAsString();
    }

    public void setTrustCertificateKeyStorePassword(String value) {
        this.trustCertificateKeyStorePassword.setValue(value);
    }

    public String getTrustCertificateKeyStoreType() {
        return this.trustCertificateKeyStoreType.getValueAsString();
    }

    public void setTrustCertificateKeyStoreType(String value) {
        this.trustCertificateKeyStoreType.setValue(value);
    }

    public String getTrustCertificateKeyStoreUrl() {
        return this.trustCertificateKeyStoreUrl.getValueAsString();
    }

    public void setTrustCertificateKeyStoreUrl(String value) {
        this.trustCertificateKeyStoreUrl.setValue(value);
    }

    public boolean getUseSSPSCompatibleTimezoneShift() {
        return this.useSSPSCompatibleTimezoneShift.getValueAsBoolean();
    }

    public void setUseSSPSCompatibleTimezoneShift(boolean flag) {
        this.useSSPSCompatibleTimezoneShift.setValue(flag);
    }

    public boolean getTreatUtilDateAsTimestamp() {
        return this.treatUtilDateAsTimestamp.getValueAsBoolean();
    }

    public void setTreatUtilDateAsTimestamp(boolean flag) {
        this.treatUtilDateAsTimestamp.setValue(flag);
    }

    public boolean getUseFastDateParsing() {
        return this.useFastDateParsing.getValueAsBoolean();
    }

    public void setUseFastDateParsing(boolean flag) {
        this.useFastDateParsing.setValue(flag);
    }

    public String getLocalSocketAddress() {
        return this.localSocketAddress.getValueAsString();
    }

    public void setLocalSocketAddress(String address) {
        this.localSocketAddress.setValue(address);
    }

    public String getUseConfigs() {
        return this.useConfigs.getValueAsString();
    }

    public void setUseConfigs(String configs) {
        this.useConfigs.setValue(configs);
    }

    public boolean getGenerateSimpleParameterMetadata() {
        return this.generateSimpleParameterMetadata.getValueAsBoolean();
    }

    public void setGenerateSimpleParameterMetadata(boolean flag) {
        this.generateSimpleParameterMetadata.setValue(flag);
    }

    public boolean getLogXaCommands() {
        return this.logXaCommands.getValueAsBoolean();
    }

    public void setLogXaCommands(boolean flag) {
        this.logXaCommands.setValue(flag);
    }

    public int getResultSetSizeThreshold() {
        return this.resultSetSizeThreshold.getValueAsInt();
    }

    public void setResultSetSizeThreshold(int threshold) {
        this.resultSetSizeThreshold.setValue(threshold);
    }

    public int getNetTimeoutForStreamingResults() {
        return this.netTimeoutForStreamingResults.getValueAsInt();
    }

    public void setNetTimeoutForStreamingResults(int value) {
        this.netTimeoutForStreamingResults.setValue(value);
    }

    public boolean getEnableQueryTimeouts() {
        return this.enableQueryTimeouts.getValueAsBoolean();
    }

    public void setEnableQueryTimeouts(boolean flag) {
        this.enableQueryTimeouts.setValue(flag);
    }

    public boolean getPadCharsWithSpace() {
        return this.padCharsWithSpace.getValueAsBoolean();
    }

    public void setPadCharsWithSpace(boolean flag) {
        this.padCharsWithSpace.setValue(flag);
    }

    public boolean getUseDynamicCharsetInfo() {
        return this.useDynamicCharsetInfo.getValueAsBoolean();
    }

    public void setUseDynamicCharsetInfo(boolean flag) {
        this.useDynamicCharsetInfo.setValue(flag);
    }

    public String getClientInfoProvider() {
        return this.clientInfoProvider.getValueAsString();
    }

    public void setClientInfoProvider(String classname) {
        this.clientInfoProvider.setValue(classname);
    }

    public boolean getPopulateInsertRowWithDefaultValues() {
        return this.populateInsertRowWithDefaultValues.getValueAsBoolean();
    }

    public void setPopulateInsertRowWithDefaultValues(boolean flag) {
        this.populateInsertRowWithDefaultValues.setValue(flag);
    }

    public String getLoadBalanceStrategy() {
        return this.loadBalanceStrategy.getValueAsString();
    }

    public void setLoadBalanceStrategy(String strategy) {
        this.loadBalanceStrategy.setValue(strategy);
    }

    public boolean getTcpNoDelay() {
        return this.tcpNoDelay.getValueAsBoolean();
    }

    public void setTcpNoDelay(boolean flag) {
        this.tcpNoDelay.setValue(flag);
    }

    public boolean getTcpKeepAlive() {
        return this.tcpKeepAlive.getValueAsBoolean();
    }

    public void setTcpKeepAlive(boolean flag) {
        this.tcpKeepAlive.setValue(flag);
    }

    public int getTcpRcvBuf() {
        return this.tcpRcvBuf.getValueAsInt();
    }

    public void setTcpRcvBuf(int bufSize) {
        this.tcpRcvBuf.setValue(bufSize);
    }

    public int getTcpSndBuf() {
        return this.tcpSndBuf.getValueAsInt();
    }

    public void setTcpSndBuf(int bufSize) {
        this.tcpSndBuf.setValue(bufSize);
    }

    public int getTcpTrafficClass() {
        return this.tcpTrafficClass.getValueAsInt();
    }

    public void setTcpTrafficClass(int classFlags) {
        this.tcpTrafficClass.setValue(classFlags);
    }

    public boolean getUseNanosForElapsedTime() {
        return this.useNanosForElapsedTime.getValueAsBoolean();
    }

    public void setUseNanosForElapsedTime(boolean flag) {
        this.useNanosForElapsedTime.setValue(flag);
    }

    public long getSlowQueryThresholdNanos() {
        return this.slowQueryThresholdNanos.getValueAsLong();
    }

    public void setSlowQueryThresholdNanos(long nanos) {
        this.slowQueryThresholdNanos.setValue(nanos);
    }

    public String getStatementInterceptors() {
        return this.statementInterceptors.getValueAsString();
    }

    public void setStatementInterceptors(String value) {
        this.statementInterceptors.setValue(value);
    }

    public boolean getUseDirectRowUnpack() {
        return this.useDirectRowUnpack.getValueAsBoolean();
    }

    public void setUseDirectRowUnpack(boolean flag) {
        this.useDirectRowUnpack.setValue(flag);
    }

    public String getLargeRowSizeThreshold() {
        return this.largeRowSizeThreshold.getValueAsString();
    }

    public void setLargeRowSizeThreshold(String value) {
        try {
            this.largeRowSizeThreshold.setValue(value);
        } catch (SQLException sqlEx) {
            RuntimeException ex = new RuntimeException(sqlEx.getMessage());
            ex.initCause(sqlEx);

            throw ex;
        }
    }

    public boolean getUseBlobToStoreUTF8OutsideBMP() {
        return this.useBlobToStoreUTF8OutsideBMP.getValueAsBoolean();
    }

    public void setUseBlobToStoreUTF8OutsideBMP(boolean flag) {
        this.useBlobToStoreUTF8OutsideBMP.setValue(flag);
    }

    public String getUtf8OutsideBmpExcludedColumnNamePattern() {
        return this.utf8OutsideBmpExcludedColumnNamePattern.getValueAsString();
    }

    public void setUtf8OutsideBmpExcludedColumnNamePattern(String regexPattern) {
        this.utf8OutsideBmpExcludedColumnNamePattern.setValue(regexPattern);
    }

    public String getUtf8OutsideBmpIncludedColumnNamePattern() {
        return this.utf8OutsideBmpIncludedColumnNamePattern.getValueAsString();
    }

    public void setUtf8OutsideBmpIncludedColumnNamePattern(String regexPattern) {
        this.utf8OutsideBmpIncludedColumnNamePattern.setValue(regexPattern);
    }

    public boolean getIncludeInnodbStatusInDeadlockExceptions() {
        return this.includeInnodbStatusInDeadlockExceptions.getValueAsBoolean();
    }

    public void setIncludeInnodbStatusInDeadlockExceptions(boolean flag) {
        this.includeInnodbStatusInDeadlockExceptions.setValue(flag);
    }

    public boolean getBlobsAreStrings() {
        return this.blobsAreStrings.getValueAsBoolean();
    }

    public void setBlobsAreStrings(boolean flag) {
        this.blobsAreStrings.setValue(flag);
    }

    public boolean getFunctionsNeverReturnBlobs() {
        return this.functionsNeverReturnBlobs.getValueAsBoolean();
    }

    public void setFunctionsNeverReturnBlobs(boolean flag) {
        this.functionsNeverReturnBlobs.setValue(flag);
    }

    public boolean getAutoSlowLog() {
        return this.autoSlowLog.getValueAsBoolean();
    }

    public void setAutoSlowLog(boolean flag) {
        this.autoSlowLog.setValue(flag);
    }

    public String getConnectionLifecycleInterceptors() {
        return this.connectionLifecycleInterceptors.getValueAsString();
    }

    public void setConnectionLifecycleInterceptors(String interceptors) {
        this.connectionLifecycleInterceptors.setValue(interceptors);
    }

    public String getProfilerEventHandler() {
        return this.profilerEventHandler.getValueAsString();
    }

    public void setProfilerEventHandler(String handler) {
        this.profilerEventHandler.setValue(handler);
    }

    public boolean getVerifyServerCertificate() {
        return this.verifyServerCertificate.getValueAsBoolean();
    }

    public void setVerifyServerCertificate(boolean flag) {
        this.verifyServerCertificate.setValue(flag);
    }

    public boolean getUseLegacyDatetimeCode() {
        return this.useLegacyDatetimeCode.getValueAsBoolean();
    }

    public void setUseLegacyDatetimeCode(boolean flag) {
        this.useLegacyDatetimeCode.setValue(flag);
    }

    public int getSelfDestructOnPingSecondsLifetime() {
        return this.selfDestructOnPingSecondsLifetime.getValueAsInt();
    }

    public void setSelfDestructOnPingSecondsLifetime(int seconds) {
        this.selfDestructOnPingSecondsLifetime.setValue(seconds);
    }

    public int getSelfDestructOnPingMaxOperations() {
        return this.selfDestructOnPingMaxOperations.getValueAsInt();
    }

    public void setSelfDestructOnPingMaxOperations(int maxOperations) {
        this.selfDestructOnPingMaxOperations.setValue(maxOperations);
    }

    public boolean getUseColumnNamesInFindColumn() {
        return this.useColumnNamesInFindColumn.getValueAsBoolean();
    }

    public void setUseColumnNamesInFindColumn(boolean flag) {
        this.useColumnNamesInFindColumn.setValue(flag);
    }

    public boolean getUseLocalTransactionState() {
        return this.useLocalTransactionState.getValueAsBoolean();
    }

    public void setUseLocalTransactionState(boolean flag) {
        this.useLocalTransactionState.setValue(flag);
    }

    public boolean getCompensateOnDuplicateKeyUpdateCounts() {
        return this.compensateOnDuplicateKeyUpdateCounts.getValueAsBoolean();
    }

    public void setCompensateOnDuplicateKeyUpdateCounts(boolean flag) {
        this.compensateOnDuplicateKeyUpdateCounts.setValue(flag);
    }

    public int getLoadBalanceBlacklistTimeout() {
        return this.loadBalanceBlacklistTimeout.getValueAsInt();
    }

    public void setLoadBalanceBlacklistTimeout(int loadBalanceBlacklistTimeout) {
        this.loadBalanceBlacklistTimeout.setValue(loadBalanceBlacklistTimeout);
    }

    public int getLoadBalancePingTimeout() {
        return this.loadBalancePingTimeout.getValueAsInt();
    }

    public void setLoadBalancePingTimeout(int loadBalancePingTimeout) {
        this.loadBalancePingTimeout.setValue(loadBalancePingTimeout);
    }

    public int getRetriesAllDown() {
        return this.retriesAllDown.getValueAsInt();
    }

    public void setRetriesAllDown(int retriesAllDown) {
        this.retriesAllDown.setValue(retriesAllDown);
    }

    public boolean getUseAffectedRows() {
        return this.useAffectedRows.getValueAsBoolean();
    }

    public void setUseAffectedRows(boolean flag) {
        this.useAffectedRows.setValue(flag);
    }

    public String getPasswordCharacterEncoding() {
        return this.passwordCharacterEncoding.getValueAsString();
    }

    public void setPasswordCharacterEncoding(String characterSet) {
        this.passwordCharacterEncoding.setValue(characterSet);
    }

    public String getExceptionInterceptors() {
        return this.exceptionInterceptors.getValueAsString();
    }

    public void setExceptionInterceptors(String exceptionInterceptors) {
        this.exceptionInterceptors.setValue(exceptionInterceptors);
    }

    public int getMaxAllowedPacket() {
        return this.maxAllowedPacket.getValueAsInt();
    }

    public void setMaxAllowedPacket(int max) {
        this.maxAllowedPacket.setValue(max);
    }

    public boolean getQueryTimeoutKillsConnection() {
        return this.queryTimeoutKillsConnection.getValueAsBoolean();
    }

    public void setQueryTimeoutKillsConnection(boolean queryTimeoutKillsConnection) {
        this.queryTimeoutKillsConnection.setValue(queryTimeoutKillsConnection);
    }

    public boolean getLoadBalanceValidateConnectionOnSwapServer() {
        return this.loadBalanceValidateConnectionOnSwapServer.getValueAsBoolean();
    }

    public void setLoadBalanceValidateConnectionOnSwapServer(boolean loadBalanceValidateConnectionOnSwapServer) {
        this.loadBalanceValidateConnectionOnSwapServer.setValue(loadBalanceValidateConnectionOnSwapServer);
    }

    public String getLoadBalanceConnectionGroup() {
        return this.loadBalanceConnectionGroup.getValueAsString();
    }

    public void setLoadBalanceConnectionGroup(String loadBalanceConnectionGroup) {
        this.loadBalanceConnectionGroup.setValue(loadBalanceConnectionGroup);
    }

    public String getLoadBalanceExceptionChecker() {
        return this.loadBalanceExceptionChecker.getValueAsString();
    }

    public void setLoadBalanceExceptionChecker(String loadBalanceExceptionChecker) {
        this.loadBalanceExceptionChecker.setValue(loadBalanceExceptionChecker);
    }

    public String getLoadBalanceSQLStateFailover() {
        return this.loadBalanceSQLStateFailover.getValueAsString();
    }

    public void setLoadBalanceSQLStateFailover(String loadBalanceSQLStateFailover) {
        this.loadBalanceSQLStateFailover.setValue(loadBalanceSQLStateFailover);
    }

    public String getLoadBalanceSQLExceptionSubclassFailover() {
        return this.loadBalanceSQLExceptionSubclassFailover.getValueAsString();
    }

    public void setLoadBalanceSQLExceptionSubclassFailover(String loadBalanceSQLExceptionSubclassFailover) {
        this.loadBalanceSQLExceptionSubclassFailover.setValue(loadBalanceSQLExceptionSubclassFailover);
    }

    public boolean getLoadBalanceEnableJMX() {
        return this.loadBalanceEnableJMX.getValueAsBoolean();
    }

    public void setLoadBalanceEnableJMX(boolean loadBalanceEnableJMX) {
        this.loadBalanceEnableJMX.setValue(loadBalanceEnableJMX);
    }

    public int getLoadBalanceAutoCommitStatementThreshold() {
        return this.loadBalanceAutoCommitStatementThreshold.getValueAsInt();
    }

    public void setLoadBalanceAutoCommitStatementThreshold(int loadBalanceAutoCommitStatementThreshold) {
        this.loadBalanceAutoCommitStatementThreshold.setValue(loadBalanceAutoCommitStatementThreshold);
    }

    public String getLoadBalanceAutoCommitStatementRegex() {
        return this.loadBalanceAutoCommitStatementRegex.getValueAsString();
    }

    public void setLoadBalanceAutoCommitStatementRegex(String loadBalanceAutoCommitStatementRegex) {
        this.loadBalanceAutoCommitStatementRegex.setValue(loadBalanceAutoCommitStatementRegex);
    }

    public boolean getIncludeThreadDumpInDeadlockExceptions() {
        return this.includeThreadDumpInDeadlockExceptions.getValueAsBoolean();
    }

    public void setIncludeThreadDumpInDeadlockExceptions(boolean flag) {
        this.includeThreadDumpInDeadlockExceptions.setValue(flag);
    }

    public boolean getIncludeThreadNamesAsStatementComment() {
        return this.includeThreadNamesAsStatementComment.getValueAsBoolean();
    }

    public void setIncludeThreadNamesAsStatementComment(boolean flag) {
        this.includeThreadNamesAsStatementComment.setValue(flag);
    }

    public String getAuthenticationPlugins() {
        return this.authenticationPlugins.getValueAsString();
    }

    public void setAuthenticationPlugins(String authenticationPlugins) {
        this.authenticationPlugins.setValue(authenticationPlugins);
    }

    public String getDisabledAuthenticationPlugins() {
        return this.disabledAuthenticationPlugins.getValueAsString();
    }

    public void setDisabledAuthenticationPlugins(String disabledAuthenticationPlugins) {
        this.disabledAuthenticationPlugins.setValue(disabledAuthenticationPlugins);
    }

    public String getDefaultAuthenticationPlugin() {
        return this.defaultAuthenticationPlugin.getValueAsString();
    }

    public void setDefaultAuthenticationPlugin(String defaultAuthenticationPlugin) {
        this.defaultAuthenticationPlugin.setValue(defaultAuthenticationPlugin);
    }

    public String getParseInfoCacheFactory() {
        return this.parseInfoCacheFactory.getValueAsString();
    }

    public void setParseInfoCacheFactory(String factoryClassname) {
        this.parseInfoCacheFactory.setValue(factoryClassname);
    }

    public String getServerConfigCacheFactory() {
        return this.serverConfigCacheFactory.getValueAsString();
    }

    public void setServerConfigCacheFactory(String factoryClassname) {
        this.serverConfigCacheFactory.setValue(factoryClassname);
    }

    public boolean getDisconnectOnExpiredPasswords() {
        return this.disconnectOnExpiredPasswords.getValueAsBoolean();
    }

    public void setDisconnectOnExpiredPasswords(boolean disconnectOnExpiredPasswords) {
        this.disconnectOnExpiredPasswords.setValue(disconnectOnExpiredPasswords);
    }

    class BooleanConnectionProperty
            extends ConnectionProperty
            implements Serializable {
        private static final long serialVersionUID = 2540132501709159404L;

        BooleanConnectionProperty(String propertyNameToSet, boolean defaultValueToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory) {
            super(propertyNameToSet, Boolean.valueOf(defaultValueToSet), null, 0, 0, descriptionToSet, sinceVersionToSet, category, orderInCategory);
        }

        String[] getAllowableValues() {
            return new String[]{"true", "false", "yes", "no"};
        }

        boolean getValueAsBoolean() {
            return ((Boolean) this.valueAsObject).booleanValue();
        }

        boolean hasValueConstraints() {
            return true;
        }

        void initializeFrom(String extractedValue) throws SQLException {
            if (extractedValue != null) {
                validateStringValues(extractedValue);

                this.valueAsObject = Boolean.valueOf((extractedValue.equalsIgnoreCase("TRUE") || extractedValue.equalsIgnoreCase("YES")));
            } else {

                this.valueAsObject = this.defaultValue;
            }
        }

        boolean isRangeBased() {
            return false;
        }

        void setValue(boolean valueFlag) {
            this.valueAsObject = Boolean.valueOf(valueFlag);
        }
    }

    abstract class ConnectionProperty
            implements Serializable {
        static final long serialVersionUID = -6644853639584478367L;

        String[] allowableValues;

        String categoryName;

        Object defaultValue;

        int lowerBound;

        int order;

        String propertyName;

        String sinceVersion;

        int upperBound;

        Object valueAsObject;

        boolean required;

        String description;

        public ConnectionProperty() {
        }

        ConnectionProperty(String propertyNameToSet, Object defaultValueToSet, String[] allowableValuesToSet, int lowerBoundToSet, int upperBoundToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory) {
            this.description = descriptionToSet;
            this.propertyName = propertyNameToSet;
            this.defaultValue = defaultValueToSet;
            this.valueAsObject = defaultValueToSet;
            this.allowableValues = allowableValuesToSet;
            this.lowerBound = lowerBoundToSet;
            this.upperBound = upperBoundToSet;
            this.required = false;
            this.sinceVersion = sinceVersionToSet;
            this.categoryName = category;
            this.order = orderInCategory;
        }

        String[] getAllowableValues() {
            return this.allowableValues;
        }

        String getCategoryName() {
            return this.categoryName;
        }

        void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        Object getDefaultValue() {
            return this.defaultValue;
        }

        int getLowerBound() {
            return this.lowerBound;
        }

        int getOrder() {
            return this.order;
        }

        void setOrder(int order) {
            this.order = order;
        }

        String getPropertyName() {
            return this.propertyName;
        }

        int getUpperBound() {
            return this.upperBound;
        }

        Object getValueAsObject() {
            return this.valueAsObject;
        }

        void setValueAsObject(Object obj) {
            this.valueAsObject = obj;
        }

        abstract boolean hasValueConstraints();

        void initializeFrom(Properties extractFrom) throws SQLException {
            String extractedValue = extractFrom.getProperty(getPropertyName());
            extractFrom.remove(getPropertyName());
            initializeFrom(extractedValue);
        }

        void initializeFrom(Reference ref) throws SQLException {
            RefAddr refAddr = ref.get(getPropertyName());

            if (refAddr != null) {
                String refContentAsString = (String) refAddr.getContent();

                initializeFrom(refContentAsString);
            }
        }

        abstract void initializeFrom(String param1String) throws SQLException;

        abstract boolean isRangeBased();

        void storeTo(Reference ref) {
            if (getValueAsObject() != null) {
                ref.add(new StringRefAddr(getPropertyName(), getValueAsObject().toString()));
            }
        }

        DriverPropertyInfo getAsDriverPropertyInfo() {
            DriverPropertyInfo dpi = new DriverPropertyInfo(this.propertyName, null);
            dpi.choices = getAllowableValues();
            dpi.value = (this.valueAsObject != null) ? this.valueAsObject.toString() : null;
            dpi.required = this.required;
            dpi.description = this.description;

            return dpi;
        }

        void validateStringValues(String valueToValidate) throws SQLException {
            String[] validateAgainst = getAllowableValues();

            if (valueToValidate == null) {
                return;
            }

            if (validateAgainst == null || validateAgainst.length == 0) {
                return;
            }

            for (int i = 0; i < validateAgainst.length; i++) {
                if (validateAgainst[i] != null && validateAgainst[i].equalsIgnoreCase(valueToValidate)) {
                    return;
                }
            }

            StringBuffer errorMessageBuf = new StringBuffer();

            errorMessageBuf.append("The connection property '");
            errorMessageBuf.append(getPropertyName());
            errorMessageBuf.append("' only accepts values of the form: ");

            if (validateAgainst.length != 0) {
                errorMessageBuf.append("'");
                errorMessageBuf.append(validateAgainst[0]);
                errorMessageBuf.append("'");

                for (int j = 1; j < validateAgainst.length - 1; j++) {
                    errorMessageBuf.append(", ");
                    errorMessageBuf.append("'");
                    errorMessageBuf.append(validateAgainst[j]);
                    errorMessageBuf.append("'");
                }

                errorMessageBuf.append(" or '");
                errorMessageBuf.append(validateAgainst[validateAgainst.length - 1]);

                errorMessageBuf.append("'");
            }

            errorMessageBuf.append(". The value '");
            errorMessageBuf.append(valueToValidate);
            errorMessageBuf.append("' is not in this set.");

            throw SQLError.createSQLException(errorMessageBuf.toString(), "S1009", ConnectionPropertiesImpl.this.getExceptionInterceptor());
        }
    }

    class IntegerConnectionProperty
            extends ConnectionProperty
            implements Serializable {
        private static final long serialVersionUID = -3004305481796850832L;

        int multiplier;

        public IntegerConnectionProperty(String propertyNameToSet, Object defaultValueToSet, String[] allowableValuesToSet, int lowerBoundToSet, int upperBoundToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory) {
            super(propertyNameToSet, defaultValueToSet, allowableValuesToSet, lowerBoundToSet, upperBoundToSet, descriptionToSet, sinceVersionToSet, category, orderInCategory);

            this.multiplier = 1;
        }

        IntegerConnectionProperty(String propertyNameToSet, int defaultValueToSet, int lowerBoundToSet, int upperBoundToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory) {
            super(propertyNameToSet, Integer.valueOf(defaultValueToSet), null, lowerBoundToSet, upperBoundToSet, descriptionToSet, sinceVersionToSet, category, orderInCategory);
            this.multiplier = 1;
        }

        IntegerConnectionProperty(String propertyNameToSet, int defaultValueToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory) {
            this(propertyNameToSet, defaultValueToSet, 0, 0, descriptionToSet, sinceVersionToSet, category, orderInCategory);
        }

        String[] getAllowableValues() {
            return null;
        }

        int getLowerBound() {
            return this.lowerBound;
        }

        int getUpperBound() {
            return this.upperBound;
        }

        int getValueAsInt() {
            return ((Integer) this.valueAsObject).intValue();
        }

        boolean hasValueConstraints() {
            return false;
        }

        void initializeFrom(String extractedValue) throws SQLException {
            if (extractedValue != null) {

                try {
                    int intValue = Double.valueOf(extractedValue).intValue();

                    this.valueAsObject = Integer.valueOf(intValue * this.multiplier);
                } catch (NumberFormatException nfe) {
                    throw SQLError.createSQLException("The connection property '" + getPropertyName() + "' only accepts integer values. The value '" + extractedValue + "' can not be converted to an integer.", "S1009", ConnectionPropertiesImpl.this.getExceptionInterceptor());

                }

            } else {

                this.valueAsObject = this.defaultValue;
            }
        }

        boolean isRangeBased() {
            return (getUpperBound() != getLowerBound());
        }

        void setValue(int valueFlag) {
            this.valueAsObject = Integer.valueOf(valueFlag);
        }
    }

    public class LongConnectionProperty
            extends IntegerConnectionProperty {
        private static final long serialVersionUID = 6068572984340480895L;

        LongConnectionProperty(String propertyNameToSet, long defaultValueToSet, long lowerBoundToSet, long upperBoundToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory) {
            super(propertyNameToSet, Long.valueOf(defaultValueToSet), (String[]) null, (int) lowerBoundToSet, (int) upperBoundToSet, descriptionToSet, sinceVersionToSet, category, orderInCategory);
        }

        LongConnectionProperty(String propertyNameToSet, long defaultValueToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory) {
            this(propertyNameToSet, defaultValueToSet, 0L, 0L, descriptionToSet, sinceVersionToSet, category, orderInCategory);
        }

        void setValue(long value) {
            this.valueAsObject = Long.valueOf(value);
        }

        long getValueAsLong() {
            return ((Long) this.valueAsObject).longValue();
        }

        void initializeFrom(String extractedValue) throws SQLException {
            if (extractedValue != null) {

                try {
                    long longValue = Double.valueOf(extractedValue).longValue();

                    this.valueAsObject = Long.valueOf(longValue);
                } catch (NumberFormatException nfe) {
                    throw SQLError.createSQLException("The connection property '" + getPropertyName() + "' only accepts long integer values. The value '" + extractedValue + "' can not be converted to a long integer.", "S1009", ConnectionPropertiesImpl.this.getExceptionInterceptor());

                }

            } else {

                this.valueAsObject = this.defaultValue;
            }
        }
    }

    class MemorySizeConnectionProperty
            extends IntegerConnectionProperty
            implements Serializable {
        private static final long serialVersionUID = 7351065128998572656L;

        private String valueAsString;

        MemorySizeConnectionProperty(String propertyNameToSet, int defaultValueToSet, int lowerBoundToSet, int upperBoundToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory) {
            super(propertyNameToSet, defaultValueToSet, lowerBoundToSet, upperBoundToSet, descriptionToSet, sinceVersionToSet, category, orderInCategory);
        }

        void initializeFrom(String extractedValue) throws SQLException {
            this.valueAsString = extractedValue;

            if (extractedValue != null) {
                if (extractedValue.endsWith("k") || extractedValue.endsWith("K") || extractedValue.endsWith("kb") || extractedValue.endsWith("Kb") || extractedValue.endsWith("kB")) {

                    this.multiplier = 1024;
                    int indexOfK = StringUtils.indexOfIgnoreCase(extractedValue, "k");

                    extractedValue = extractedValue.substring(0, indexOfK);
                } else if (extractedValue.endsWith("m") || extractedValue.endsWith("M") || extractedValue.endsWith("G") || extractedValue.endsWith("mb") || extractedValue.endsWith("Mb") || extractedValue.endsWith("mB")) {

                    this.multiplier = 1048576;
                    int indexOfM = StringUtils.indexOfIgnoreCase(extractedValue, "m");

                    extractedValue = extractedValue.substring(0, indexOfM);
                } else if (extractedValue.endsWith("g") || extractedValue.endsWith("G") || extractedValue.endsWith("gb") || extractedValue.endsWith("Gb") || extractedValue.endsWith("gB")) {

                    this.multiplier = 1073741824;
                    int indexOfG = StringUtils.indexOfIgnoreCase(extractedValue, "g");

                    extractedValue = extractedValue.substring(0, indexOfG);
                }
            }

            super.initializeFrom(extractedValue);
        }

        void setValue(String value) throws SQLException {
            initializeFrom(value);
        }

        String getValueAsString() {
            return this.valueAsString;
        }
    }

    class StringConnectionProperty
            extends ConnectionProperty
            implements Serializable {
        private static final long serialVersionUID = 5432127962785948272L;

        StringConnectionProperty(String propertyNameToSet, String defaultValueToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory) {
            this(propertyNameToSet, defaultValueToSet, (String[]) null, descriptionToSet, sinceVersionToSet, category, orderInCategory);
        }

        StringConnectionProperty(String propertyNameToSet, String defaultValueToSet, String[] allowableValuesToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory) {
            super(propertyNameToSet, defaultValueToSet, allowableValuesToSet, 0, 0, descriptionToSet, sinceVersionToSet, category, orderInCategory);
        }

        String getValueAsString() {
            return (String) this.valueAsObject;
        }

        boolean hasValueConstraints() {
            return (this.allowableValues != null && this.allowableValues.length > 0);
        }

        void initializeFrom(String extractedValue) throws SQLException {
            if (extractedValue != null) {
                validateStringValues(extractedValue);

                this.valueAsObject = extractedValue;
            } else {
                this.valueAsObject = this.defaultValue;
            }
        }

        boolean isRangeBased() {
            return false;
        }

        void setValue(String valueFlag) {
            this.valueAsObject = valueFlag;
        }
    }

    class XmlMap {
        protected Map<Integer, Map<String, ConnectionPropertiesImpl.ConnectionProperty>> ordered;
        protected Map<String, ConnectionPropertiesImpl.ConnectionProperty> alpha;
        XmlMap() {
            this.ordered = new TreeMap<Integer, Map<String, ConnectionPropertiesImpl.ConnectionProperty>>();
            this.alpha = new TreeMap<String, ConnectionPropertiesImpl.ConnectionProperty>();
        }
    }
}

