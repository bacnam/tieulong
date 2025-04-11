package com.mysql.jdbc;

import com.mysql.jdbc.log.Log;
import com.mysql.jdbc.log.LogFactory;
import com.mysql.jdbc.log.LogUtils;
import com.mysql.jdbc.log.NullLogger;
import com.mysql.jdbc.profiler.ProfilerEvent;
import com.mysql.jdbc.profiler.ProfilerEventHandler;
import com.mysql.jdbc.util.LRUCache;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.UnsupportedCharsetException;
import java.sql.*;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.Executor;

public class ConnectionImpl
        extends ConnectionPropertiesImpl
        implements MySQLConnection {
    protected static final String DEFAULT_LOGGER_CLASS = "com.mysql.jdbc.log.StandardLogger";
    private static final long serialVersionUID = 2877471301981509474L;
    private static final SQLPermission SET_NETWORK_TIMEOUT_PERM = new SQLPermission("setNetworkTimeout");
    private static final SQLPermission ABORT_PERM = new SQLPermission("abort");
    private static final String JDBC_LOCAL_CHARACTER_SET_RESULTS = "jdbc.local.character_set_results";
    private static final Object CHARSET_CONVERTER_NOT_AVAILABLE_MARKER = new Object();
    private static final int HISTOGRAM_BUCKETS = 20;
    private static final String LOGGER_INSTANCE_NAME = "MySQL";
    private static final Log NULL_LOGGER = (Log) new NullLogger("MySQL");
    private static final Map<String, Map<Long, String>> serverCollationByUrl = new HashMap<String, Map<Long, String>>();
    private static final Map<String, Map<Integer, String>> serverJavaCharsetByUrl = new HashMap<String, Map<Integer, String>>();
    private static final Map<String, Map<Integer, String>> serverCustomCharsetByUrl = new HashMap<String, Map<Integer, String>>();
    private static final Map<String, Map<String, Integer>> serverCustomMblenByUrl = new HashMap<String, Map<String, Integer>>();
    private static final Constructor<?> JDBC_4_CONNECTION_CTOR;
    private static final int DEFAULT_RESULT_SET_TYPE = 1003;
    private static final int DEFAULT_RESULT_SET_CONCURRENCY = 1007;
    private static final Random random;
    private static final String SERVER_VERSION_STRING_VAR_NAME = "server_version_string";
    public static Map<?, ?> charsetMap;
    protected static Map<?, ?> roundRobinStatsMap;
    private static Map<String, Integer> mapTransIsolationNameToValue = null;

    static {
        mapTransIsolationNameToValue = new HashMap<String, Integer>(8);
        mapTransIsolationNameToValue.put("READ-UNCOMMITED", Integer.valueOf(1));
        mapTransIsolationNameToValue.put("READ-UNCOMMITTED", Integer.valueOf(1));
        mapTransIsolationNameToValue.put("READ-COMMITTED", Integer.valueOf(2));
        mapTransIsolationNameToValue.put("REPEATABLE-READ", Integer.valueOf(4));
        mapTransIsolationNameToValue.put("SERIALIZABLE", Integer.valueOf(8));

        if (Util.isJdbc4()) {
            try {
                JDBC_4_CONNECTION_CTOR = Class.forName("com.mysql.jdbc.JDBC4Connection").getConstructor(new Class[]{String.class, int.class, Properties.class, String.class, String.class});

            } catch (SecurityException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            JDBC_4_CONNECTION_CTOR = null;
        }

        random = new Random();
    }

    public Map<Integer, String> indexToJavaCharset = new HashMap<Integer, String>();
    public Map<Integer, String> indexToCustomMysqlCharset = new HashMap<Integer, String>();
    protected Properties props = null;
    protected LRUCache resultSetMetadataCache;
    private MySQLConnection proxy = null;
    private CacheAdapter<String, Map<String, String>> serverConfigCache;
    private long queryTimeCount;
    private double queryTimeSum;
    private double queryTimeSumSquares;
    private double queryTimeMean;
    private transient Timer cancelTimer;
    private List<Extension> connectionLifecycleInterceptors;
    private boolean autoCommit = true;
    private CacheAdapter<String, PreparedStatement.ParseInfo> cachedPreparedStatementParams;
    private String characterSetMetadata = null;
    private String characterSetResultsOnServer = null;
    private Map<String, Object> charsetConverterMap = new HashMap<String, Object>(CharsetMapping.getNumberOfCharsetsConfigured());
    private long connectionCreationTimeMillis = 0L;
    private long connectionId;
    private String database = null;
    private DatabaseMetaData dbmd = null;
    private TimeZone defaultTimeZone;
    private ProfilerEventHandler eventSink;
    private Throwable forceClosedReason;
    private boolean hasIsolationLevels = false;
    private boolean hasQuotedIdentifiers = false;
    private String host = null;
    private Map<String, Integer> mysqlCharsetToCustomMblen = new HashMap<String, Integer>();
    private transient MysqlIO io = null;
    private boolean isClientTzUTC = false;
    private boolean isClosed = true;
    private boolean isInGlobalTx = false;
    private boolean isRunningOnJDK13 = false;
    private int isolationLevel = 2;
    private boolean isServerTzUTC = false;
    private long lastQueryFinishedTime = 0L;
    private transient Log log = NULL_LOGGER;
    private long longestQueryTimeMs = 0L;
    private boolean lowerCaseTableNames = false;
    private long maximumNumberTablesAccessed = 0L;
    private boolean maxRowsChanged = false;
    private long metricsLastReportedMs;
    private long minimumNumberTablesAccessed = Long.MAX_VALUE;
    private String myURL = null;
    private boolean needsPing = false;
    private int netBufferLength = 16384;
    private boolean noBackslashEscapes = false;
    private long numberOfPreparedExecutes = 0L;
    private long numberOfPrepares = 0L;
    private long numberOfQueriesIssued = 0L;
    private long numberOfResultSetsCreated = 0L;
    private long[] numTablesMetricsHistBreakpoints;
    private int[] numTablesMetricsHistCounts;
    private long[] oldHistBreakpoints = null;
    private int[] oldHistCounts = null;
    private Map<Statement, Statement> openStatements;
    private LRUCache parsedCallableStatementCache;
    private boolean parserKnowsUnicode = false;
    private String password = null;
    private long[] perfMetricsHistBreakpoints;
    private int[] perfMetricsHistCounts;
    private String pointOfOrigin;
    private int port = 3306;
    private boolean readInfoMsg = false;
    private boolean readOnly = false;
    private TimeZone serverTimezoneTZ = null;
    private Map<String, String> serverVariables = null;
    private long shortestQueryTimeMs = Long.MAX_VALUE;
    private Map<Statement, Statement> statementsUsingMaxRows;
    private double totalQueryTimeMs = 0.0D;
    private boolean transactionsSupported = false;
    private Map<String, Class<?>> typeMap;
    private boolean useAnsiQuotes = false;
    private String user = null;
    private boolean useServerPreparedStmts = false;
    private LRUCache serverSideStatementCheckCache;
    private LRUCache serverSideStatementCache;
    private Calendar sessionCalendar;
    private Calendar utcCalendar;
    private String origHostToConnectTo;
    private int origPortToConnectTo;
    private String origDatabaseToConnectTo;
    private String errorMessageEncoding = "Cp1252";
    private boolean usePlatformCharsetConverters;
    private boolean hasTriedMasterFlag = false;
    private String statementComment = null;
    private boolean storesLowerCaseTableName;
    private List<StatementInterceptorV2> statementInterceptors;
    private boolean requiresEscapingEncoder;
    private String hostPortPair;
    private boolean usingCachedConfig;
    private int autoIncrementIncrement;
    private ExceptionInterceptor exceptionInterceptor;

    protected ConnectionImpl() {
        this.usingCachedConfig = false;

        this.autoIncrementIncrement = 0;
    }

    protected ConnectionImpl(String hostToConnectTo, int portToConnectTo, Properties info, String databaseToConnectTo, String url) throws SQLException {
        this.usingCachedConfig = false;
        this.autoIncrementIncrement = 0;
        this.connectionCreationTimeMillis = System.currentTimeMillis();
        if (databaseToConnectTo == null)
            databaseToConnectTo = "";
        this.origHostToConnectTo = hostToConnectTo;
        this.origPortToConnectTo = portToConnectTo;
        this.origDatabaseToConnectTo = databaseToConnectTo;
        try {
            Blob.class.getMethod("truncate", new Class[]{long.class});
            this.isRunningOnJDK13 = false;
        } catch (NoSuchMethodException nsme) {
            this.isRunningOnJDK13 = true;
        }
        this.sessionCalendar = new GregorianCalendar();
        this.utcCalendar = new GregorianCalendar();
        this.utcCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        this.log = LogFactory.getLogger(getLogger(), "MySQL", getExceptionInterceptor());
        this.defaultTimeZone = Util.getDefaultTimeZone();
        if ("GMT".equalsIgnoreCase(this.defaultTimeZone.getID())) {
            this.isClientTzUTC = true;
        } else {
            this.isClientTzUTC = false;
        }
        this.openStatements = new HashMap<Statement, Statement>();
        if (NonRegisteringDriver.isHostPropertiesList(hostToConnectTo)) {
            Properties hostSpecificProps = NonRegisteringDriver.expandHostKeyValues(hostToConnectTo);
            Enumeration<?> propertyNames = hostSpecificProps.propertyNames();
            while (propertyNames.hasMoreElements()) {
                String propertyName = propertyNames.nextElement().toString();
                String propertyValue = hostSpecificProps.getProperty(propertyName);
                info.setProperty(propertyName, propertyValue);
            }
        } else if (hostToConnectTo == null) {
            this.host = "localhost";
            this.hostPortPair = this.host + ":" + portToConnectTo;
        } else {
            this.host = hostToConnectTo;
            if (hostToConnectTo.indexOf(":") == -1) {
                this.hostPortPair = this.host + ":" + portToConnectTo;
            } else {
                this.hostPortPair = this.host;
            }
        }
        this.port = portToConnectTo;
        this.database = databaseToConnectTo;
        this.myURL = url;
        this.user = info.getProperty("user");
        this.password = info.getProperty("password");
        if (this.user == null || this.user.equals(""))
            this.user = "";
        if (this.password == null)
            this.password = "";
        this.props = info;
        initializeDriverProperties(info);
        if (getUseUsageAdvisor()) {
            this.pointOfOrigin = LogUtils.findCallingClassAndMethod(new Throwable());
        } else {
            this.pointOfOrigin = "";
        }
        try {
            this.dbmd = getMetaData(false, false);
            initializeSafeStatementInterceptors();
            createNewIO(false);
            unSafeStatementInterceptors();
        } catch (SQLException ex) {
            cleanup(ex);
            throw ex;
        } catch (Exception ex) {
            cleanup(ex);
            StringBuffer mesg = new StringBuffer(128);
            if (!getParanoid()) {
                mesg.append("Cannot connect to MySQL server on ");
                mesg.append(this.host);
                mesg.append(":");
                mesg.append(this.port);
                mesg.append(".\n\n");
                mesg.append("Make sure that there is a MySQL server ");
                mesg.append("running on the machine/port you are trying ");
                mesg.append("to connect to and that the machine this software is running on ");
                mesg.append("is able to connect to this host/port (i.e. not firewalled). ");
                mesg.append("Also make sure that the server has not been started with the --skip-networking ");
                mesg.append("flag.\n\n");
            } else {
                mesg.append("Unable to connect to database.");
            }
            SQLException sqlEx = SQLError.createSQLException(mesg.toString(), "08S01", getExceptionInterceptor());
            sqlEx.initCause(ex);
            throw sqlEx;
        }
        NonRegisteringDriver.trackConnection(this);
    }

    protected static SQLException appendMessageToException(SQLException sqlEx, String messageToAppend, ExceptionInterceptor interceptor) {
        String origMessage = sqlEx.getMessage();
        String sqlState = sqlEx.getSQLState();
        int vendorErrorCode = sqlEx.getErrorCode();
        StringBuffer messageBuf = new StringBuffer(origMessage.length() + messageToAppend.length());
        messageBuf.append(origMessage);
        messageBuf.append(messageToAppend);
        SQLException sqlExceptionWithNewMessage = SQLError.createSQLException(messageBuf.toString(), sqlState, vendorErrorCode, interceptor);
        try {
            Method getStackTraceMethod = null;
            Method setStackTraceMethod = null;
            Object theStackTraceAsObject = null;
            Class<?> stackTraceElementClass = Class.forName("java.lang.StackTraceElement");
            Class<?> stackTraceElementArrayClass = Array.newInstance(stackTraceElementClass, new int[]{0}).getClass();
            getStackTraceMethod = Throwable.class.getMethod("getStackTrace", new Class[0]);
            setStackTraceMethod = Throwable.class.getMethod("setStackTrace", new Class[]{stackTraceElementArrayClass});
            if (getStackTraceMethod != null && setStackTraceMethod != null) {
                theStackTraceAsObject = getStackTraceMethod.invoke(sqlEx, new Object[0]);
                setStackTraceMethod.invoke(sqlExceptionWithNewMessage, new Object[]{theStackTraceAsObject});
            }
        } catch (NoClassDefFoundError noClassDefFound) {
        } catch (NoSuchMethodException noSuchMethodEx) {
        } catch (Throwable catchAll) {
        }
        return sqlExceptionWithNewMessage;
    }

    protected static Connection getInstance(String hostToConnectTo, int portToConnectTo, Properties info, String databaseToConnectTo, String url) throws SQLException {
        if (!Util.isJdbc4())
            return new ConnectionImpl(hostToConnectTo, portToConnectTo, info, databaseToConnectTo, url);
        return (Connection) Util.handleNewInstance(JDBC_4_CONNECTION_CTOR, new Object[]{hostToConnectTo, Integer.valueOf(portToConnectTo), info, databaseToConnectTo, url}, null);
    }

    protected static synchronized int getNextRoundRobinHostIndex(String url, List<?> hostList) {
        int indexRange = hostList.size();

        int index = random.nextInt(indexRange);

        return index;
    }

    private static boolean nullSafeCompare(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return true;
        }

        if (s1 == null && s2 != null) {
            return false;
        }

        return (s1 != null && s1.equals(s2));
    }

    public String getHost() {
        return this.host;
    }

    public boolean isProxySet() {
        return (this.proxy != null);
    }

    private MySQLConnection getProxy() {
        return (this.proxy != null) ? this.proxy : this;
    }

    public void setProxy(MySQLConnection proxy) {
        this.proxy = proxy;
    }

    public MySQLConnection getLoadBalanceSafeProxy() {
        return getProxy();
    }

    public synchronized Timer getCancelTimer() {
        if (this.cancelTimer == null) {
            boolean createdNamedTimer = false;
            try {
                Constructor<Timer> ctr = Timer.class.getConstructor(new Class[]{String.class, boolean.class});
                this.cancelTimer = ctr.newInstance(new Object[]{"MySQL Statement Cancellation Timer", Boolean.TRUE});
                createdNamedTimer = true;
            } catch (Throwable t) {
                createdNamedTimer = false;
            }
            if (!createdNamedTimer)
                this.cancelTimer = new Timer(true);
        }
        return this.cancelTimer;
    }

    public void unSafeStatementInterceptors() throws SQLException {
        ArrayList<StatementInterceptorV2> unSafedStatementInterceptors = new ArrayList<StatementInterceptorV2>(this.statementInterceptors.size());

        for (int i = 0; i < this.statementInterceptors.size(); i++) {
            NoSubInterceptorWrapper wrappedInterceptor = (NoSubInterceptorWrapper) this.statementInterceptors.get(i);

            unSafedStatementInterceptors.add(wrappedInterceptor.getUnderlyingInterceptor());
        }

        this.statementInterceptors = unSafedStatementInterceptors;

        if (this.io != null) {
            this.io.setStatementInterceptors(this.statementInterceptors);
        }
    }

    public void initializeSafeStatementInterceptors() throws SQLException {
        this.isClosed = false;

        List<Extension> unwrappedInterceptors = Util.loadExtensions(this, this.props, getStatementInterceptors(), "MysqlIo.BadStatementInterceptor", getExceptionInterceptor());

        this.statementInterceptors = new ArrayList<StatementInterceptorV2>(unwrappedInterceptors.size());

        for (int i = 0; i < unwrappedInterceptors.size(); i++) {
            Extension interceptor = unwrappedInterceptors.get(i);

            if (interceptor instanceof StatementInterceptor) {
                if (ReflectiveStatementInterceptorAdapter.getV2PostProcessMethod(interceptor.getClass()) != null) {
                    this.statementInterceptors.add(new NoSubInterceptorWrapper(new ReflectiveStatementInterceptorAdapter((StatementInterceptor) interceptor)));
                } else {
                    this.statementInterceptors.add(new NoSubInterceptorWrapper(new V1toV2StatementInterceptorAdapter((StatementInterceptor) interceptor)));
                }
            } else {
                this.statementInterceptors.add(new NoSubInterceptorWrapper((StatementInterceptorV2) interceptor));
            }
        }
    }

    public List<StatementInterceptorV2> getStatementInterceptorsInstances() {
        return this.statementInterceptors;
    }

    private void addToHistogram(int[] histogramCounts, long[] histogramBreakpoints, long value, int numberOfTimes, long currentLowerBound, long currentUpperBound) {
        if (histogramCounts == null) {
            createInitialHistogram(histogramBreakpoints, currentLowerBound, currentUpperBound);
        } else {

            for (int i = 0; i < 20; i++) {
                if (histogramBreakpoints[i] >= value) {
                    histogramCounts[i] = histogramCounts[i] + numberOfTimes;
                    break;
                }
            }
        }
    }

    private void addToPerformanceHistogram(long value, int numberOfTimes) {
        checkAndCreatePerformanceHistogram();

        addToHistogram(this.perfMetricsHistCounts, this.perfMetricsHistBreakpoints, value, numberOfTimes, (this.shortestQueryTimeMs == Long.MAX_VALUE) ? 0L : this.shortestQueryTimeMs, this.longestQueryTimeMs);
    }

    private void addToTablesAccessedHistogram(long value, int numberOfTimes) {
        checkAndCreateTablesAccessedHistogram();

        addToHistogram(this.numTablesMetricsHistCounts, this.numTablesMetricsHistBreakpoints, value, numberOfTimes, (this.minimumNumberTablesAccessed == Long.MAX_VALUE) ? 0L : this.minimumNumberTablesAccessed, this.maximumNumberTablesAccessed);
    }

    private void buildCollationMapping() throws SQLException {
        HashMap<Integer, String> javaCharset = null;

        if (versionMeetsMinimum(4, 1, 0)) {

            TreeMap<Long, String> sortedCollationMap = null;
            HashMap<Integer, String> customCharset = null;
            HashMap<String, Integer> customMblen = null;

            if (getCacheServerConfiguration()) {
                synchronized (serverCollationByUrl) {
                    sortedCollationMap = (TreeMap<Long, String>) serverCollationByUrl.get(getURL());
                    javaCharset = (HashMap<Integer, String>) serverJavaCharsetByUrl.get(getURL());
                    customCharset = (HashMap<Integer, String>) serverCustomCharsetByUrl.get(getURL());
                    customMblen = (HashMap<String, Integer>) serverCustomMblenByUrl.get(getURL());
                }
            }

            Statement stmt = null;
            ResultSet results = null;

            try {
                if (sortedCollationMap == null) {
                    sortedCollationMap = new TreeMap<Long, String>();
                    javaCharset = new HashMap<Integer, String>();
                    customCharset = new HashMap<Integer, String>();
                    customMblen = new HashMap<String, Integer>();

                    stmt = getMetadataSafeStatement();

                    try {
                        results = stmt.executeQuery("SHOW COLLATION");
                        if (versionMeetsMinimum(5, 0, 0)) {
                            Util.resultSetToMap(sortedCollationMap, results, 3, 2);
                        } else {
                            while (results.next()) {
                                sortedCollationMap.put(Long.valueOf(results.getLong(3)), results.getString(2));
                            }
                        }
                    } catch (SQLException ex) {
                        if (ex.getErrorCode() != 1820 || getDisconnectOnExpiredPasswords()) {
                            throw ex;
                        }
                    }

                    for (Iterator<Map.Entry<Long, String>> indexIter = sortedCollationMap.entrySet().iterator(); indexIter.hasNext(); ) {
                        Map.Entry<Long, String> indexEntry = indexIter.next();

                        int collationIndex = ((Long) indexEntry.getKey()).intValue();
                        String charsetName = indexEntry.getValue();

                        javaCharset.put(Integer.valueOf(collationIndex), getJavaEncodingForMysqlEncoding(charsetName));

                        if (collationIndex >= 255 || !charsetName.equals(CharsetMapping.STATIC_INDEX_TO_MYSQL_CHARSET_MAP.get(Integer.valueOf(collationIndex)))) {
                            customCharset.put(Integer.valueOf(collationIndex), charsetName);
                        }

                        if (!CharsetMapping.STATIC_CHARSET_TO_NUM_BYTES_MAP.containsKey(charsetName) && !CharsetMapping.STATIC_4_0_CHARSET_TO_NUM_BYTES_MAP.containsKey(charsetName)) {
                            customMblen.put(charsetName, null);
                        }
                    }

                    if (customMblen.size() > 0) {
                        try {
                            results = stmt.executeQuery("SHOW CHARACTER SET");
                            while (results.next()) {
                                String charsetName = results.getString("Charset");
                                if (customMblen.containsKey(charsetName)) {
                                    customMblen.put(charsetName, Integer.valueOf(results.getInt("Maxlen")));
                                }
                            }
                        } catch (SQLException ex) {
                            if (ex.getErrorCode() != 1820 || getDisconnectOnExpiredPasswords()) {
                                throw ex;
                            }
                        }
                    }

                    if (getCacheServerConfiguration()) {
                        synchronized (serverCollationByUrl) {
                            serverCollationByUrl.put(getURL(), sortedCollationMap);
                            serverJavaCharsetByUrl.put(getURL(), javaCharset);
                            serverCustomCharsetByUrl.put(getURL(), customCharset);
                            serverCustomMblenByUrl.put(getURL(), customMblen);
                        }
                    }
                }

                this.indexToJavaCharset = Collections.unmodifiableMap(javaCharset);
                this.indexToCustomMysqlCharset = Collections.unmodifiableMap(customCharset);
                this.mysqlCharsetToCustomMblen = Collections.unmodifiableMap(customMblen);
            } catch (SQLException ex) {
                throw ex;
            } catch (RuntimeException ex) {
                SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", (ExceptionInterceptor) null);
                sqlEx.initCause(ex);
                throw sqlEx;
            } finally {
                if (results != null) {
                    try {
                        results.close();
                    } catch (SQLException sqlE) {
                    }
                }

                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException sqlE) {
                    }
                }
            }

        } else {

            javaCharset = new HashMap<Integer, String>();
            for (int i = 0; i < CharsetMapping.INDEX_TO_CHARSET.length; i++) {
                javaCharset.put(Integer.valueOf(i), CharsetMapping.INDEX_TO_CHARSET[i]);
            }
            this.indexToJavaCharset = Collections.unmodifiableMap(javaCharset);
        }
    }

    public String getJavaEncodingForMysqlEncoding(String mysqlEncoding) throws SQLException {
        if (versionMeetsMinimum(4, 1, 0) && "latin1".equalsIgnoreCase(mysqlEncoding)) {
            return "Cp1252";
        }

        return CharsetMapping.MYSQL_TO_JAVA_CHARSET_MAP.get(mysqlEncoding);
    }

    private boolean canHandleAsServerPreparedStatement(String sql) throws SQLException {
        if (sql == null || sql.length() == 0) {
            return true;
        }

        if (!this.useServerPreparedStmts) {
            return false;
        }

        if (getCachePreparedStatements()) {
            synchronized (this.serverSideStatementCheckCache) {
                Boolean flag = (Boolean) this.serverSideStatementCheckCache.get(sql);

                if (flag != null) {
                    return flag.booleanValue();
                }

                boolean canHandle = canHandleAsServerPreparedStatementNoCache(sql);

                if (sql.length() < getPreparedStatementCacheSqlLimit()) {
                    this.serverSideStatementCheckCache.put(sql, canHandle ? Boolean.TRUE : Boolean.FALSE);
                }

                return canHandle;
            }
        }

        return canHandleAsServerPreparedStatementNoCache(sql);
    }

    private boolean canHandleAsServerPreparedStatementNoCache(String sql) throws SQLException {
        if (StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "CALL")) {
            return false;
        }

        boolean canHandleAsStatement = true;

        if (!versionMeetsMinimum(5, 0, 7) && (StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "SELECT") || StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "DELETE") || StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "INSERT") || StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "UPDATE") || StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "REPLACE"))) {

            int currentPos = 0;
            int statementLength = sql.length();
            int lastPosToLook = statementLength - 7;
            boolean allowBackslashEscapes = !this.noBackslashEscapes;
            char quoteChar = this.useAnsiQuotes ? '"' : '\'';
            boolean foundLimitWithPlaceholder = false;

            while (currentPos < lastPosToLook) {
                int limitStart = StringUtils.indexOfIgnoreCaseRespectQuotes(currentPos, sql, "LIMIT ", quoteChar, allowBackslashEscapes);

                if (limitStart == -1) {
                    break;
                }

                currentPos = limitStart + 7;

                while (currentPos < statementLength) {
                    char c = sql.charAt(currentPos);

                    if (!Character.isDigit(c) && !Character.isWhitespace(c) && c != ',' && c != '?') {
                        break;
                    }

                    if (c == '?') {
                        foundLimitWithPlaceholder = true;

                        break;
                    }
                    currentPos++;
                }
            }

            canHandleAsStatement = !foundLimitWithPlaceholder;
        } else if (StringUtils.startsWithIgnoreCaseAndWs(sql, "CREATE TABLE")) {
            canHandleAsStatement = false;
        } else if (StringUtils.startsWithIgnoreCaseAndWs(sql, "DO")) {
            canHandleAsStatement = false;
        } else if (StringUtils.startsWithIgnoreCaseAndWs(sql, "SET")) {
            canHandleAsStatement = false;
        }

        return canHandleAsStatement;
    }

    public synchronized void changeUser(String userName, String newPassword) throws SQLException {
        checkClosed();

        if (userName == null || userName.equals("")) {
            userName = "";
        }

        if (newPassword == null) {
            newPassword = "";
        }

        this.io.changeUser(userName, newPassword, this.database);
        this.user = userName;
        this.password = newPassword;

        if (versionMeetsMinimum(4, 1, 0)) {
            configureClientCharacterSet(true);
        }

        setSessionVariables();

        setupServerForTruncationChecks();
    }

    private boolean characterSetNamesMatches(String mysqlEncodingName) {
        return (mysqlEncodingName != null && mysqlEncodingName.equalsIgnoreCase(this.serverVariables.get("character_set_client")) && mysqlEncodingName.equalsIgnoreCase(this.serverVariables.get("character_set_connection")));
    }

    private void checkAndCreatePerformanceHistogram() {
        if (this.perfMetricsHistCounts == null) {
            this.perfMetricsHistCounts = new int[20];
        }

        if (this.perfMetricsHistBreakpoints == null) {
            this.perfMetricsHistBreakpoints = new long[20];
        }
    }

    private void checkAndCreateTablesAccessedHistogram() {
        if (this.numTablesMetricsHistCounts == null) {
            this.numTablesMetricsHistCounts = new int[20];
        }

        if (this.numTablesMetricsHistBreakpoints == null) {
            this.numTablesMetricsHistBreakpoints = new long[20];
        }
    }

    public void checkClosed() throws SQLException {
        if (this.isClosed) {
            throwConnectionClosedException();
        }
    }

    public void throwConnectionClosedException() throws SQLException {
        StringBuffer messageBuf = new StringBuffer("No operations allowed after connection closed.");

        SQLException ex = SQLError.createSQLException(messageBuf.toString(), "08003", getExceptionInterceptor());

        if (this.forceClosedReason != null) {
            ex.initCause(this.forceClosedReason);
        }

        throw ex;
    }

    private void checkServerEncoding() throws SQLException {
        if (getUseUnicode() && getEncoding() != null) {
            return;
        }

        String serverEncoding = this.serverVariables.get("character_set");

        if (serverEncoding == null) {
            serverEncoding = this.serverVariables.get("character_set_server");
        }

        String mappedServerEncoding = null;

        if (serverEncoding != null) {
            try {
                mappedServerEncoding = getJavaEncodingForMysqlEncoding(serverEncoding.toUpperCase(Locale.ENGLISH));
            } catch (SQLException ex) {
                throw ex;
            } catch (RuntimeException ex) {
                SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", (ExceptionInterceptor) null);
                sqlEx.initCause(ex);
                throw sqlEx;
            }
        }

        if (!getUseUnicode() && mappedServerEncoding != null) {
            SingleByteCharsetConverter converter = getCharsetConverter(mappedServerEncoding);

            if (converter != null) {
                setUseUnicode(true);
                setEncoding(mappedServerEncoding);

                return;
            }
        }

        if (serverEncoding != null) {
            if (mappedServerEncoding == null) {

                if (Character.isLowerCase(serverEncoding.charAt(0))) {
                    char[] ach = serverEncoding.toCharArray();
                    ach[0] = Character.toUpperCase(serverEncoding.charAt(0));
                    setEncoding(new String(ach));
                }
            }

            if (mappedServerEncoding == null) {
                throw SQLError.createSQLException("Unknown character encoding on server '" + serverEncoding + "', use 'characterEncoding=' property " + " to provide correct mapping", "01S00", getExceptionInterceptor());
            }

            try {
                StringUtils.getBytes("abc", mappedServerEncoding);
                setEncoding(mappedServerEncoding);
                setUseUnicode(true);
            } catch (UnsupportedEncodingException UE) {
                throw SQLError.createSQLException("The driver can not map the character encoding '" + getEncoding() + "' that your server is using " + "to a character encoding your JVM understands. You " + "can specify this mapping manually by adding \"useUnicode=true\" " + "as well as \"characterEncoding=[an_encoding_your_jvm_understands]\" " + "to your JDBC URL.", "0S100", getExceptionInterceptor());
            }
        }
    }

    private void checkTransactionIsolationLevel() throws SQLException {
        String txIsolationName = null;

        if (versionMeetsMinimum(4, 0, 3)) {
            txIsolationName = "tx_isolation";
        } else {
            txIsolationName = "transaction_isolation";
        }

        String s = this.serverVariables.get(txIsolationName);

        if (s != null) {
            Integer intTI = mapTransIsolationNameToValue.get(s);

            if (intTI != null) {
                this.isolationLevel = intTI.intValue();
            }
        }
    }

    public void abortInternal() throws SQLException {
        if (this.io != null) {
            try {
                this.io.forceClose();
            } catch (Throwable t) {
            }

            this.io = null;
        }

        this.isClosed = true;
    }

    private void cleanup(Throwable whyCleanedUp) {
        try {
            if (this.io != null && !isClosed()) {
                realClose(false, false, false, whyCleanedUp);
            } else if (this.io != null) {
                this.io.forceClose();
            }
        } catch (SQLException sqlEx) {
        }

        this.isClosed = true;
    }

    public void clearHasTriedMaster() {
        this.hasTriedMasterFlag = false;
    }

    public void clearWarnings() throws SQLException {
    }

    public PreparedStatement clientPrepareStatement(String sql) throws SQLException {
        return clientPrepareStatement(sql, 1003, 1007);
    }

    public PreparedStatement clientPrepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
        PreparedStatement pStmt = clientPrepareStatement(sql);

        ((PreparedStatement) pStmt).setRetrieveGeneratedKeys((autoGenKeyIndex == 1));

        return pStmt;
    }

    public PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return clientPrepareStatement(sql, resultSetType, resultSetConcurrency, true);
    }

    public PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, boolean processEscapeCodesIfNeeded) throws SQLException {
        checkClosed();

        String nativeSql = (processEscapeCodesIfNeeded && getProcessEscapeCodesForPrepStmts()) ? nativeSQL(sql) : sql;

        PreparedStatement pStmt = null;

        if (getCachePreparedStatements()) {
            PreparedStatement.ParseInfo pStmtInfo = this.cachedPreparedStatementParams.get(nativeSql);

            if (pStmtInfo == null) {
                pStmt = PreparedStatement.getInstance(getLoadBalanceSafeProxy(), nativeSql, this.database);

                this.cachedPreparedStatementParams.put(nativeSql, pStmt.getParseInfo());
            } else {

                pStmt = new PreparedStatement(getLoadBalanceSafeProxy(), nativeSql, this.database, pStmtInfo);
            }
        } else {

            pStmt = PreparedStatement.getInstance(getLoadBalanceSafeProxy(), nativeSql, this.database);
        }

        pStmt.setResultSetType(resultSetType);
        pStmt.setResultSetConcurrency(resultSetConcurrency);

        return pStmt;
    }

    public PreparedStatement clientPrepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
        PreparedStatement pStmt = (PreparedStatement) clientPrepareStatement(sql);

        pStmt.setRetrieveGeneratedKeys((autoGenKeyIndexes != null && autoGenKeyIndexes.length > 0));

        return pStmt;
    }

    public PreparedStatement clientPrepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
        PreparedStatement pStmt = (PreparedStatement) clientPrepareStatement(sql);

        pStmt.setRetrieveGeneratedKeys((autoGenKeyColNames != null && autoGenKeyColNames.length > 0));

        return pStmt;
    }

    public PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return clientPrepareStatement(sql, resultSetType, resultSetConcurrency, true);
    }

    public synchronized void close() throws SQLException {
        if (this.connectionLifecycleInterceptors != null) {
            (new IterateBlock<Extension>(this.connectionLifecycleInterceptors.iterator()) {
                void forEach(Extension each) throws SQLException {
                    ((ConnectionLifecycleInterceptor) each).close();
                }
            }).doForAll();
        }

        realClose(true, true, false, (Throwable) null);
    }

    private void closeAllOpenStatements() throws SQLException {
        SQLException postponedException = null;

        if (this.openStatements != null) {
            List<Statement> currentlyOpenStatements = new ArrayList<Statement>();

            for (Iterator<Statement> iter = this.openStatements.keySet().iterator(); iter.hasNext(); ) {
                currentlyOpenStatements.add(iter.next());
            }

            int numStmts = currentlyOpenStatements.size();

            for (int i = 0; i < numStmts; i++) {
                StatementImpl stmt = (StatementImpl) currentlyOpenStatements.get(i);

                try {
                    stmt.realClose(false, true);
                } catch (SQLException sqlEx) {
                    postponedException = sqlEx;
                }
            }

            if (postponedException != null) {
                throw postponedException;
            }
        }
    }

    private void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException sqlEx) {
            }

            stmt = null;
        }
    }

    public synchronized void commit() throws SQLException {
        checkClosed();

        try {
            if (this.connectionLifecycleInterceptors != null) {
                IterateBlock<Extension> iter = new IterateBlock<Extension>(this.connectionLifecycleInterceptors.iterator()) {
                    void forEach(Extension each) throws SQLException {
                        if (!((ConnectionLifecycleInterceptor) each).commit()) {
                            this.stopIterating = true;
                        }
                    }
                };

                iter.doForAll();

                if (!iter.fullIteration()) {
                    return;
                }
            }

            if (this.autoCommit && !getRelaxAutoCommit())
                throw SQLError.createSQLException("Can't call commit when autocommit=true", getExceptionInterceptor());
            if (this.transactionsSupported) {
                if (getUseLocalTransactionState() && versionMeetsMinimum(5, 0, 0) &&
                        !this.io.inTransactionOnServer()) {
                    return;
                }

                execSQL((StatementImpl) null, "commit", -1, (Buffer) null, 1003, 1007, false, this.database, (Field[]) null, false);

            }

        } catch (SQLException sqlException) {
            if ("08S01".equals(sqlException.getSQLState())) {
                throw SQLError.createSQLException("Communications link failure during commit(). Transaction resolution unknown.", "08007", getExceptionInterceptor());
            }

            throw sqlException;
        } finally {
            this.needsPing = getReconnectAtTxEnd();
        }
    }

    private void configureCharsetProperties() throws SQLException {
        if (getEncoding() != null) {

            try {

                String testString = "abc";
                StringUtils.getBytes(testString, getEncoding());
            } catch (UnsupportedEncodingException UE) {

                String oldEncoding = getEncoding();

                try {
                    setEncoding(getJavaEncodingForMysqlEncoding(oldEncoding));
                } catch (SQLException ex) {
                    throw ex;
                } catch (RuntimeException ex) {
                    SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", (ExceptionInterceptor) null);
                    sqlEx.initCause(ex);
                    throw sqlEx;
                }

                if (getEncoding() == null) {
                    throw SQLError.createSQLException("Java does not support the MySQL character encoding  encoding '" + oldEncoding + "'.", "01S00", getExceptionInterceptor());
                }

                try {
                    String testString = "abc";
                    StringUtils.getBytes(testString, getEncoding());
                } catch (UnsupportedEncodingException encodingEx) {
                    throw SQLError.createSQLException("Unsupported character encoding '" + getEncoding() + "'.", "01S00", getExceptionInterceptor());
                }
            }
        }
    }

    private boolean configureClientCharacterSet(boolean dontCheckServerMatch) throws SQLException {
        String realJavaEncoding = getEncoding();
        boolean characterSetAlreadyConfigured = false;

        try {
            if (versionMeetsMinimum(4, 1, 0)) {
                characterSetAlreadyConfigured = true;

                setUseUnicode(true);

                configureCharsetProperties();
                realJavaEncoding = getEncoding();

                try {
                    if (this.props != null && this.props.getProperty("com.mysql.jdbc.faultInjection.serverCharsetIndex") != null) {
                        this.io.serverCharsetIndex = Integer.parseInt(this.props.getProperty("com.mysql.jdbc.faultInjection.serverCharsetIndex"));
                    }

                    String serverEncodingToSet = CharsetMapping.INDEX_TO_CHARSET[this.io.serverCharsetIndex];

                    if (serverEncodingToSet == null || serverEncodingToSet.length() == 0) {
                        if (realJavaEncoding != null) {

                            setEncoding(realJavaEncoding);
                        } else {
                            throw SQLError.createSQLException("Unknown initial character set index '" + this.io.serverCharsetIndex + "' received from server. Initial client character set can be forced via the 'characterEncoding' property.", "S1000", getExceptionInterceptor());
                        }
                    }

                    if (versionMeetsMinimum(4, 1, 0) && "ISO8859_1".equalsIgnoreCase(serverEncodingToSet)) {
                        serverEncodingToSet = "Cp1252";
                    }
                    if ("UnicodeBig".equalsIgnoreCase(serverEncodingToSet) || "UTF-16".equalsIgnoreCase(serverEncodingToSet) || "UTF-16LE".equalsIgnoreCase(serverEncodingToSet) || "UTF-32".equalsIgnoreCase(serverEncodingToSet)) {

                        serverEncodingToSet = "UTF-8";
                    }

                    setEncoding(serverEncodingToSet);
                } catch (ArrayIndexOutOfBoundsException outOfBoundsEx) {
                    if (realJavaEncoding != null) {

                        setEncoding(realJavaEncoding);
                    } else {
                        throw SQLError.createSQLException("Unknown initial character set index '" + this.io.serverCharsetIndex + "' received from server. Initial client character set can be forced via the 'characterEncoding' property.", "S1000", getExceptionInterceptor());

                    }

                } catch (SQLException ex) {
                    throw ex;
                } catch (RuntimeException ex) {
                    SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", (ExceptionInterceptor) null);
                    sqlEx.initCause(ex);
                    throw sqlEx;
                }

                if (getEncoding() == null) {
                    setEncoding("ISO8859_1");
                }

                if (getUseUnicode()) {
                    if (realJavaEncoding != null) {

                        if (realJavaEncoding.equalsIgnoreCase("UTF-8") || realJavaEncoding.equalsIgnoreCase("UTF8")) {

                            boolean utf8mb4Supported = versionMeetsMinimum(5, 5, 2);
                            boolean useutf8mb4 = false;

                            if (utf8mb4Supported) {
                                useutf8mb4 = (this.io.serverCharsetIndex == 45);
                            }

                            if (!getUseOldUTF8Behavior()) {
                                if (dontCheckServerMatch || !characterSetNamesMatches("utf8") || (utf8mb4Supported && !characterSetNamesMatches("utf8mb4"))) {
                                    execSQL((StatementImpl) null, "SET NAMES " + (useutf8mb4 ? "utf8mb4" : "utf8"), -1, (Buffer) null, 1003, 1007, false, this.database, (Field[]) null, false);

                                }
                            } else {

                                execSQL((StatementImpl) null, "SET NAMES latin1", -1, (Buffer) null, 1003, 1007, false, this.database, (Field[]) null, false);
                            }

                            setEncoding(realJavaEncoding);
                        } else {
                            String mysqlEncodingName = CharsetMapping.getMysqlEncodingForJavaEncoding(realJavaEncoding.toUpperCase(Locale.ENGLISH), this);

                            if (mysqlEncodingName != null) {
                                if (dontCheckServerMatch || !characterSetNamesMatches(mysqlEncodingName)) {
                                    execSQL((StatementImpl) null, "SET NAMES " + mysqlEncodingName, -1, (Buffer) null, 1003, 1007, false, this.database, (Field[]) null, false);
                                }
                            }

                            setEncoding(realJavaEncoding);
                        }
                    } else if (getEncoding() != null) {

                        String mysqlEncodingName = getServerCharacterEncoding();

                        if (getUseOldUTF8Behavior()) {
                            mysqlEncodingName = "latin1";
                        }

                        boolean ucs2 = false;
                        if ("ucs2".equalsIgnoreCase(mysqlEncodingName) || "utf16".equalsIgnoreCase(mysqlEncodingName) || "utf16le".equalsIgnoreCase(mysqlEncodingName) || "utf32".equalsIgnoreCase(mysqlEncodingName)) {

                            mysqlEncodingName = "utf8";
                            ucs2 = true;
                            if (getCharacterSetResults() == null) {
                                setCharacterSetResults("UTF-8");
                            }
                        }

                        if (dontCheckServerMatch || !characterSetNamesMatches(mysqlEncodingName) || ucs2) {
                            try {
                                execSQL((StatementImpl) null, "SET NAMES " + mysqlEncodingName, -1, (Buffer) null, 1003, 1007, false, this.database, (Field[]) null, false);

                            } catch (SQLException ex) {
                                if (ex.getErrorCode() != 1820 || getDisconnectOnExpiredPasswords()) {
                                    throw ex;
                                }
                            }
                        }

                        realJavaEncoding = getEncoding();
                    }
                }

                String onServer = null;
                boolean isNullOnServer = false;

                if (this.serverVariables != null) {
                    onServer = this.serverVariables.get("character_set_results");

                    isNullOnServer = (onServer == null || "NULL".equalsIgnoreCase(onServer) || onServer.length() == 0);
                }

                if (getCharacterSetResults() == null) {

                    if (!isNullOnServer) {
                        try {
                            execSQL((StatementImpl) null, "SET character_set_results = NULL", -1, (Buffer) null, 1003, 1007, false, this.database, (Field[]) null, false);

                        } catch (SQLException ex) {
                            if (ex.getErrorCode() != 1820 || getDisconnectOnExpiredPasswords()) {
                                throw ex;
                            }
                        }
                        if (!this.usingCachedConfig) {
                            this.serverVariables.put("jdbc.local.character_set_results", null);
                        }
                    } else if (!this.usingCachedConfig) {
                        this.serverVariables.put("jdbc.local.character_set_results", onServer);
                    }

                } else {

                    if (getUseOldUTF8Behavior()) {
                        try {
                            execSQL((StatementImpl) null, "SET NAMES latin1", -1, (Buffer) null, 1003, 1007, false, this.database, (Field[]) null, false);

                        } catch (SQLException ex) {
                            if (ex.getErrorCode() != 1820 || getDisconnectOnExpiredPasswords()) {
                                throw ex;
                            }
                        }
                    }
                    String charsetResults = getCharacterSetResults();
                    String mysqlEncodingName = null;

                    if ("UTF-8".equalsIgnoreCase(charsetResults) || "UTF8".equalsIgnoreCase(charsetResults)) {

                        mysqlEncodingName = "utf8";
                    } else if ("null".equalsIgnoreCase(charsetResults)) {
                        mysqlEncodingName = "NULL";
                    } else {
                        mysqlEncodingName = CharsetMapping.getMysqlEncodingForJavaEncoding(charsetResults.toUpperCase(Locale.ENGLISH), this);
                    }

                    if (mysqlEncodingName == null) {
                        throw SQLError.createSQLException("Can't map " + charsetResults + " given for characterSetResults to a supported MySQL encoding.", "S1009", getExceptionInterceptor());
                    }

                    if (!mysqlEncodingName.equalsIgnoreCase(this.serverVariables.get("character_set_results"))) {

                        StringBuffer setBuf = new StringBuffer("SET character_set_results = ".length() + mysqlEncodingName.length());

                        setBuf.append("SET character_set_results = ").append(mysqlEncodingName);

                        try {
                            execSQL((StatementImpl) null, setBuf.toString(), -1, (Buffer) null, 1003, 1007, false, this.database, (Field[]) null, false);

                        } catch (SQLException ex) {
                            if (ex.getErrorCode() != 1820 || getDisconnectOnExpiredPasswords()) {
                                throw ex;
                            }
                        }

                        if (!this.usingCachedConfig) {
                            this.serverVariables.put("jdbc.local.character_set_results", mysqlEncodingName);
                        }

                        if (versionMeetsMinimum(5, 5, 0)) {
                            this.errorMessageEncoding = charsetResults;

                        }
                    } else if (!this.usingCachedConfig) {
                        this.serverVariables.put("jdbc.local.character_set_results", onServer);
                    }
                }

                if (getConnectionCollation() != null) {
                    StringBuffer setBuf = new StringBuffer("SET collation_connection = ".length() + getConnectionCollation().length());

                    setBuf.append("SET collation_connection = ").append(getConnectionCollation());

                    try {
                        execSQL((StatementImpl) null, setBuf.toString(), -1, (Buffer) null, 1003, 1007, false, this.database, (Field[]) null, false);

                    } catch (SQLException ex) {
                        if (ex.getErrorCode() != 1820 || getDisconnectOnExpiredPasswords()) {
                            throw ex;
                        }
                    }
                }
            } else {

                realJavaEncoding = getEncoding();

            }

        } finally {

            setEncoding(realJavaEncoding);
        }

        try {
            CharsetEncoder enc = Charset.forName(getEncoding()).newEncoder();
            CharBuffer cbuf = CharBuffer.allocate(1);
            ByteBuffer bbuf = ByteBuffer.allocate(1);

            cbuf.put("¥");
            cbuf.position(0);
            enc.encode(cbuf, bbuf, true);
            if (bbuf.get(0) == 92) {
                this.requiresEscapingEncoder = true;
            } else {
                cbuf.clear();
                bbuf.clear();

                cbuf.put("₩");
                cbuf.position(0);
                enc.encode(cbuf, bbuf, true);
                if (bbuf.get(0) == 92) {
                    this.requiresEscapingEncoder = true;
                }
            }
        } catch (UnsupportedCharsetException ucex) {

            try {
                byte[] bbuf = StringUtils.getBytes("¥", getEncoding());
                if (bbuf[0] == 92) {
                    this.requiresEscapingEncoder = true;
                } else {
                    bbuf = StringUtils.getBytes("₩", getEncoding());
                    if (bbuf[0] == 92) {
                        this.requiresEscapingEncoder = true;
                    }
                }
            } catch (UnsupportedEncodingException ueex) {
                throw SQLError.createSQLException("Unable to use encoding: " + getEncoding(), "S1000", ueex, getExceptionInterceptor());
            }
        }

        return characterSetAlreadyConfigured;
    }

    private void configureTimezone() throws SQLException {
        String configuredTimeZoneOnServer = this.serverVariables.get("timezone");

        if (configuredTimeZoneOnServer == null) {
            configuredTimeZoneOnServer = this.serverVariables.get("time_zone");

            if ("SYSTEM".equalsIgnoreCase(configuredTimeZoneOnServer)) {
                configuredTimeZoneOnServer = this.serverVariables.get("system_time_zone");
            }
        }

        String canoncicalTimezone = getServerTimezone();

        if ((getUseTimezone() || !getUseLegacyDatetimeCode()) && configuredTimeZoneOnServer != null) {

            if (canoncicalTimezone == null || StringUtils.isEmptyOrWhitespaceOnly(canoncicalTimezone)) {
                try {
                    canoncicalTimezone = TimeUtil.getCanoncialTimezone(configuredTimeZoneOnServer, getExceptionInterceptor());

                    if (canoncicalTimezone == null) {
                        throw SQLError.createSQLException("Can't map timezone '" + configuredTimeZoneOnServer + "' to " + " canonical timezone.", "S1009", getExceptionInterceptor());

                    }

                } catch (IllegalArgumentException iae) {
                    throw SQLError.createSQLException(iae.getMessage(), "S1000", getExceptionInterceptor());
                }
            }
        } else {

            canoncicalTimezone = getServerTimezone();
        }

        if (canoncicalTimezone != null && canoncicalTimezone.length() > 0) {
            this.serverTimezoneTZ = TimeZone.getTimeZone(canoncicalTimezone);

            if (!canoncicalTimezone.equalsIgnoreCase("GMT") && this.serverTimezoneTZ.getID().equals("GMT")) {
                throw SQLError.createSQLException("No timezone mapping entry for '" + canoncicalTimezone + "'", "S1009", getExceptionInterceptor());
            }

            if ("GMT".equalsIgnoreCase(this.serverTimezoneTZ.getID())) {
                this.isServerTzUTC = true;
            } else {
                this.isServerTzUTC = false;
            }
        }
    }

    private void createInitialHistogram(long[] breakpoints, long lowerBound, long upperBound) {
        double bucketSize = (upperBound - lowerBound) / 20.0D * 1.25D;

        if (bucketSize < 1.0D) {
            bucketSize = 1.0D;
        }

        for (int i = 0; i < 20; i++) {
            breakpoints[i] = lowerBound;
            lowerBound = (long) (lowerBound + bucketSize);
        }
    }

    public synchronized void createNewIO(boolean isForReconnect) throws SQLException {
        Properties mergedProps = exposeAsProperties(this.props);

        if (!getHighAvailability()) {
            connectOneTryOnly(isForReconnect, mergedProps);

            return;
        }

        connectWithRetries(isForReconnect, mergedProps);
    }

    private void connectWithRetries(boolean isForReconnect, Properties mergedProps) throws SQLException {
        double timeout = getInitialTimeout();
        boolean connectionGood = false;

        Exception connectionException = null;

        int attemptCount = 0;
        for (; attemptCount < getMaxReconnects() && !connectionGood; attemptCount++) {
            try {
                boolean oldAutoCommit;
                int oldIsolationLevel;
                boolean oldReadOnly;
                String oldCatalog;
                if (this.io != null) {
                    this.io.forceClose();
                }

                coreConnect(mergedProps);
                pingInternal(false, 0);

                synchronized (this) {
                    this.connectionId = this.io.getThreadId();
                    this.isClosed = false;

                    oldAutoCommit = getAutoCommit();
                    oldIsolationLevel = this.isolationLevel;
                    oldReadOnly = isReadOnly(false);
                    oldCatalog = getCatalog();

                    this.io.setStatementInterceptors(this.statementInterceptors);
                }

                initializePropsFromServer();

                if (isForReconnect) {

                    setAutoCommit(oldAutoCommit);

                    if (this.hasIsolationLevels) {
                        setTransactionIsolation(oldIsolationLevel);
                    }

                    setCatalog(oldCatalog);
                    setReadOnly(oldReadOnly);
                }

                connectionGood = true;

                break;
            } catch (Exception EEE) {
                connectionException = EEE;
                connectionGood = false;

                if (connectionGood) {
                    break;
                }

                if (attemptCount > 0) {
                    try {
                        Thread.sleep((long) timeout * 1000L);
                    } catch (InterruptedException IE) {
                    }
                }
            }
        }

        if (!connectionGood) {

            SQLException chainedEx = SQLError.createSQLException(Messages.getString("Connection.UnableToConnectWithRetries", new Object[]{Integer.valueOf(getMaxReconnects())}), "08001", getExceptionInterceptor());

            chainedEx.initCause(connectionException);

            throw chainedEx;
        }

        if (getParanoid() && !getHighAvailability()) {
            this.password = null;
            this.user = null;
        }

        if (isForReconnect) {

            Iterator<Statement> statementIter = this.openStatements.values().iterator();

            Stack<Statement> serverPreparedStatements = null;

            while (statementIter.hasNext()) {
                Statement statementObj = statementIter.next();

                if (statementObj instanceof ServerPreparedStatement) {
                    if (serverPreparedStatements == null) {
                        serverPreparedStatements = new Stack<Statement>();
                    }

                    serverPreparedStatements.add(statementObj);
                }
            }

            if (serverPreparedStatements != null) {
                while (!serverPreparedStatements.isEmpty()) {
                    ((ServerPreparedStatement) serverPreparedStatements.pop()).rePrepare();
                }
            }
        }
    }

    private void coreConnect(Properties mergedProps) throws SQLException, IOException {
        int newPort = 3306;
        String newHost = "localhost";

        String protocol = mergedProps.getProperty("PROTOCOL");

        if (protocol != null) {

            if ("tcp".equalsIgnoreCase(protocol)) {
                newHost = normalizeHost(mergedProps.getProperty("HOST"));
                newPort = parsePortNumber(mergedProps.getProperty("PORT", "3306"));
            } else if ("pipe".equalsIgnoreCase(protocol)) {
                setSocketFactoryClassName(NamedPipeSocketFactory.class.getName());

                String path = mergedProps.getProperty("PATH");

                if (path != null) {
                    mergedProps.setProperty("namedPipePath", path);
                }
            } else {

                newHost = normalizeHost(mergedProps.getProperty("HOST"));
                newPort = parsePortNumber(mergedProps.getProperty("PORT", "3306"));
            }
        } else {

            String[] parsedHostPortPair = NonRegisteringDriver.parseHostPortPair(this.hostPortPair);

            newHost = parsedHostPortPair[0];

            newHost = normalizeHost(newHost);

            if (parsedHostPortPair[1] != null) {
                newPort = parsePortNumber(parsedHostPortPair[1]);
            }
        }

        this.port = newPort;
        this.host = newHost;

        this.io = new MysqlIO(newHost, newPort, mergedProps, getSocketFactoryClassName(), getProxy(), getSocketTimeout(), this.largeRowSizeThreshold.getValueAsInt());

        this.io.doHandshake(this.user, this.password, this.database);
    }

    private String normalizeHost(String hostname) {
        if (hostname == null || StringUtils.isEmptyOrWhitespaceOnly(hostname)) {
            return "localhost";
        }

        return hostname;
    }

    private int parsePortNumber(String portAsString) throws SQLException {
        int portNumber = 3306;
        try {
            portNumber = Integer.parseInt(portAsString);
        } catch (NumberFormatException nfe) {
            throw SQLError.createSQLException("Illegal connection port value '" + portAsString + "'", "01S00", getExceptionInterceptor());
        }

        return portNumber;
    }

    private void connectOneTryOnly(boolean isForReconnect, Properties mergedProps) throws SQLException {
        Exception connectionNotEstablishedBecause = null;

        try {
            coreConnect(mergedProps);
            this.connectionId = this.io.getThreadId();
            this.isClosed = false;

            boolean oldAutoCommit = getAutoCommit();
            int oldIsolationLevel = this.isolationLevel;
            boolean oldReadOnly = isReadOnly(false);
            String oldCatalog = getCatalog();

            this.io.setStatementInterceptors(this.statementInterceptors);

            initializePropsFromServer();

            if (isForReconnect) {

                setAutoCommit(oldAutoCommit);

                if (this.hasIsolationLevels) {
                    setTransactionIsolation(oldIsolationLevel);
                }

                setCatalog(oldCatalog);

                setReadOnly(oldReadOnly);
            }

            return;
        } catch (Exception EEE) {

            if (EEE instanceof SQLException && ((SQLException) EEE).getErrorCode() == 1820 && !getDisconnectOnExpiredPasswords()) {
                return;
            }

            if (this.io != null) {
                this.io.forceClose();
            }

            connectionNotEstablishedBecause = EEE;

            if (EEE instanceof SQLException) {
                throw (SQLException) EEE;
            }

            SQLException chainedEx = SQLError.createSQLException(Messages.getString("Connection.UnableToConnect"), "08001", getExceptionInterceptor());

            chainedEx.initCause(connectionNotEstablishedBecause);

            throw chainedEx;
        }
    }

    private synchronized void createPreparedStatementCaches() throws SQLException {
        int cacheSize = getPreparedStatementCacheSize();

        try {
            Class<?> factoryClass = Class.forName(getParseInfoCacheFactory());

            CacheAdapterFactory<String, PreparedStatement.ParseInfo> cacheFactory = (CacheAdapterFactory<String, PreparedStatement.ParseInfo>) factoryClass.newInstance();

            this.cachedPreparedStatementParams = cacheFactory.getInstance(this, this.myURL, getPreparedStatementCacheSize(), getPreparedStatementCacheSqlLimit(), this.props);
        } catch (ClassNotFoundException e) {
            SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.CantFindCacheFactory", new Object[]{getParseInfoCacheFactory(), "parseInfoCacheFactory"}), getExceptionInterceptor());

            sqlEx.initCause(e);

            throw sqlEx;
        } catch (InstantiationException e) {
            SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.CantLoadCacheFactory", new Object[]{getParseInfoCacheFactory(), "parseInfoCacheFactory"}), getExceptionInterceptor());

            sqlEx.initCause(e);

            throw sqlEx;
        } catch (IllegalAccessException e) {
            SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.CantLoadCacheFactory", new Object[]{getParseInfoCacheFactory(), "parseInfoCacheFactory"}), getExceptionInterceptor());

            sqlEx.initCause(e);

            throw sqlEx;
        }

        if (getUseServerPreparedStmts()) {
            this.serverSideStatementCheckCache = new LRUCache(cacheSize);

            this.serverSideStatementCache = new LRUCache(cacheSize) {
                private static final long serialVersionUID = 7692318650375988114L;

                protected boolean removeEldestEntry(Map.Entry<Object, Object> eldest) {
                    if (this.maxElements <= 1) {
                        return false;
                    }

                    boolean removeIt = super.removeEldestEntry(eldest);

                    if (removeIt) {
                        ServerPreparedStatement ps = (ServerPreparedStatement) eldest.getValue();

                        ps.isCached = false;
                        ps.setClosed(false);

                        try {
                            ps.close();
                        } catch (SQLException sqlEx) {
                        }
                    }

                    return removeIt;
                }
            };
        }
    }

    public Statement createStatement() throws SQLException {
        return createStatement(1003, 1007);
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        checkClosed();

        StatementImpl stmt = new StatementImpl(getLoadBalanceSafeProxy(), this.database);
        stmt.setResultSetType(resultSetType);
        stmt.setResultSetConcurrency(resultSetConcurrency);

        return stmt;
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        if (getPedantic() &&
                resultSetHoldability != 1) {
            throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", "S1009", getExceptionInterceptor());
        }

        return createStatement(resultSetType, resultSetConcurrency);
    }

    public void dumpTestcaseQuery(String query) {
        System.err.println(query);
    }

    public Connection duplicate() throws SQLException {
        return new ConnectionImpl(this.origHostToConnectTo, this.origPortToConnectTo, this.props, this.origDatabaseToConnectTo, this.myURL);
    }

    public ResultSetInternalMethods execSQL(StatementImpl callingStatement, String sql, int maxRows, Buffer packet, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Field[] cachedMetadata) throws SQLException {
        return execSQL(callingStatement, sql, maxRows, packet, resultSetType, resultSetConcurrency, streamResults, catalog, cachedMetadata, false);
    }

    public synchronized ResultSetInternalMethods execSQL(StatementImpl callingStatement, String sql, int maxRows, Buffer packet, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Field[] cachedMetadata, boolean isBatch) throws SQLException {
        long queryStartTime = 0L;

        int endOfQueryPacketPosition = 0;

        if (packet != null) {
            endOfQueryPacketPosition = packet.getPosition();
        }

        if (getGatherPerformanceMetrics()) {
            queryStartTime = System.currentTimeMillis();
        }

        this.lastQueryFinishedTime = 0L;

        if (getHighAvailability() && (this.autoCommit || getAutoReconnectForPools()) && this.needsPing && !isBatch) {

            try {

                pingInternal(false, 0);

                this.needsPing = false;
            } catch (Exception Ex) {
                createNewIO(true);
            }
        }

        try {
            if (packet == null) {
                String encoding = null;

                if (getUseUnicode()) {
                    encoding = getEncoding();
                }

                return this.io.sqlQueryDirect(callingStatement, sql, encoding, null, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, cachedMetadata);
            }

            return this.io.sqlQueryDirect(callingStatement, null, null, packet, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, cachedMetadata);

        } catch (SQLException sqlE) {

            if (getDumpQueriesOnException()) {
                String extractedSql = extractSqlFromPacket(sql, packet, endOfQueryPacketPosition);

                StringBuffer messageBuf = new StringBuffer(extractedSql.length() + 32);

                messageBuf.append("\n\nQuery being executed when exception was thrown:\n");

                messageBuf.append(extractedSql);
                messageBuf.append("\n\n");

                sqlE = appendMessageToException(sqlE, messageBuf.toString(), getExceptionInterceptor());
            }

            if (getHighAvailability()) {
                this.needsPing = true;
            } else {
                String sqlState = sqlE.getSQLState();

                if (sqlState != null && sqlState.equals("08S01")) {

                    cleanup(sqlE);
                }
            }

            throw sqlE;
        } catch (Exception ex) {
            if (getHighAvailability()) {
                this.needsPing = true;
            } else if (ex instanceof IOException) {
                cleanup(ex);
            }

            SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.UnexpectedException"), "S1000", getExceptionInterceptor());

            sqlEx.initCause(ex);

            throw sqlEx;
        } finally {
            if (getMaintainTimeStats()) {
                this.lastQueryFinishedTime = System.currentTimeMillis();
            }

            if (getGatherPerformanceMetrics()) {
                long queryTime = System.currentTimeMillis() - queryStartTime;

                registerQueryExecutionTime(queryTime);
            }
        }
    }

    public String extractSqlFromPacket(String possibleSqlQuery, Buffer queryPacket, int endOfQueryPacketPosition) throws SQLException {
        String extractedSql = null;

        if (possibleSqlQuery != null) {
            if (possibleSqlQuery.length() > getMaxQuerySizeToLog()) {
                StringBuffer truncatedQueryBuf = new StringBuffer(possibleSqlQuery.substring(0, getMaxQuerySizeToLog()));

                truncatedQueryBuf.append(Messages.getString("MysqlIO.25"));
                extractedSql = truncatedQueryBuf.toString();
            } else {
                extractedSql = possibleSqlQuery;
            }
        }

        if (extractedSql == null) {

            int extractPosition = endOfQueryPacketPosition;

            boolean truncated = false;

            if (endOfQueryPacketPosition > getMaxQuerySizeToLog()) {
                extractPosition = getMaxQuerySizeToLog();
                truncated = true;
            }

            extractedSql = StringUtils.toString(queryPacket.getByteBuffer(), 5, extractPosition - 5);

            if (truncated) {
                extractedSql = extractedSql + Messages.getString("MysqlIO.25");
            }
        }

        return extractedSql;
    }

    public StringBuffer generateConnectionCommentBlock(StringBuffer buf) {
        buf.append(getId());
        buf.append(" clock: ");
        buf.append(System.currentTimeMillis());
        buf.append(" */ ");

        return buf;
    }

    public int getActiveStatementCount() {
        if (this.openStatements != null) {
            synchronized (this.openStatements) {
                return this.openStatements.size();
            }
        }

        return 0;
    }

    public synchronized boolean getAutoCommit() throws SQLException {
        return this.autoCommit;
    }

    public synchronized void setAutoCommit(final boolean autoCommitFlag) throws SQLException {
        checkClosed();

        if (this.connectionLifecycleInterceptors != null) {
            IterateBlock<Extension> iter = new IterateBlock<Extension>(this.connectionLifecycleInterceptors.iterator()) {
                void forEach(Extension each) throws SQLException {
                    if (!((ConnectionLifecycleInterceptor) each).setAutoCommit(autoCommitFlag)) {
                        this.stopIterating = true;
                    }
                }
            };

            iter.doForAll();

            if (!iter.fullIteration()) {
                return;
            }
        }

        if (getAutoReconnectForPools()) {
            setHighAvailability(true);
        }

        try {
            if (this.transactionsSupported) {

                boolean needsSetOnServer = true;

                if (getUseLocalSessionState() && this.autoCommit == autoCommitFlag) {

                    needsSetOnServer = false;
                } else if (!getHighAvailability()) {
                    needsSetOnServer = getIO().isSetNeededForAutoCommitMode(autoCommitFlag);
                }

                this.autoCommit = autoCommitFlag;

                if (needsSetOnServer) {
                    execSQL((StatementImpl) null, autoCommitFlag ? "SET autocommit=1" : "SET autocommit=0", -1, (Buffer) null, 1003, 1007, false, this.database, (Field[]) null, false);

                }

            } else {

                if (!autoCommitFlag && !getRelaxAutoCommit()) {
                    throw SQLError.createSQLException("MySQL Versions Older than 3.23.15 do not support transactions", "08003", getExceptionInterceptor());
                }

                this.autoCommit = autoCommitFlag;
            }
        } finally {
            if (getAutoReconnectForPools()) {
                setHighAvailability(false);
            }
        }
    }

    public Calendar getCalendarInstanceForSessionOrNew() {
        if (getDynamicCalendars()) {
            return Calendar.getInstance();
        }

        return getSessionLockedCalendar();
    }

    public synchronized String getCatalog() throws SQLException {
        return this.database;
    }

    public synchronized void setCatalog(final String catalog) throws SQLException {
        checkClosed();

        if (catalog == null) {
            throw SQLError.createSQLException("Catalog can not be null", "S1009", getExceptionInterceptor());
        }

        if (this.connectionLifecycleInterceptors != null) {
            IterateBlock<Extension> iter = new IterateBlock<Extension>(this.connectionLifecycleInterceptors.iterator()) {
                void forEach(Extension each) throws SQLException {
                    if (!((ConnectionLifecycleInterceptor) each).setCatalog(catalog)) {
                        this.stopIterating = true;
                    }
                }
            };

            iter.doForAll();

            if (!iter.fullIteration()) {
                return;
            }
        }

        if (getUseLocalSessionState()) {
            if (this.lowerCaseTableNames) {
                if (this.database.equalsIgnoreCase(catalog)) {
                    return;
                }
            } else if (this.database.equals(catalog)) {
                return;
            }
        }

        String quotedId = this.dbmd.getIdentifierQuoteString();

        if (quotedId == null || quotedId.equals(" ")) {
            quotedId = "";
        }

        StringBuffer query = new StringBuffer("USE ");
        query.append(quotedId);
        query.append(catalog);
        query.append(quotedId);

        execSQL((StatementImpl) null, query.toString(), -1, (Buffer) null, 1003, 1007, false, this.database, (Field[]) null, false);

        this.database = catalog;
    }

    public synchronized String getCharacterSetMetadata() {
        return this.characterSetMetadata;
    }

    public SingleByteCharsetConverter getCharsetConverter(String javaEncodingName) throws SQLException {
        if (javaEncodingName == null) {
            return null;
        }

        if (this.usePlatformCharsetConverters) {
            return null;
        }

        SingleByteCharsetConverter converter = null;

        synchronized (this.charsetConverterMap) {
            Object asObject = this.charsetConverterMap.get(javaEncodingName);

            if (asObject == CHARSET_CONVERTER_NOT_AVAILABLE_MARKER) {
                return null;
            }

            converter = (SingleByteCharsetConverter) asObject;

            if (converter == null) {
                try {
                    converter = SingleByteCharsetConverter.getInstance(javaEncodingName, this);

                    if (converter == null) {
                        this.charsetConverterMap.put(javaEncodingName, CHARSET_CONVERTER_NOT_AVAILABLE_MARKER);
                    } else {

                        this.charsetConverterMap.put(javaEncodingName, converter);
                    }
                } catch (UnsupportedEncodingException unsupEncEx) {
                    this.charsetConverterMap.put(javaEncodingName, CHARSET_CONVERTER_NOT_AVAILABLE_MARKER);

                    converter = null;
                }
            }
        }

        return converter;
    }

    public String getCharsetNameForIndex(int charsetIndex) throws SQLException {
        String charsetName = null;

        if (getUseOldUTF8Behavior()) {
            return getEncoding();
        }

        if (charsetIndex != -1) {
            try {
                charsetName = this.indexToJavaCharset.get(Integer.valueOf(charsetIndex));

                if (charsetName == null) charsetName = CharsetMapping.INDEX_TO_CHARSET[charsetIndex];

                if (this.characterEncodingIsAliasForSjis && (
                        "sjis".equalsIgnoreCase(charsetName) || "MS932".equalsIgnoreCase(charsetName))) {

                    charsetName = getEncoding();
                }
            } catch (ArrayIndexOutOfBoundsException outOfBoundsEx) {
                throw SQLError.createSQLException("Unknown character set index for field '" + charsetIndex + "' received from server.", "S1000", getExceptionInterceptor());

            } catch (RuntimeException ex) {
                SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", (ExceptionInterceptor) null);
                sqlEx.initCause(ex);
                throw sqlEx;
            }

            if (charsetName == null) {
                charsetName = getEncoding();
            }
        } else {
            charsetName = getEncoding();
        }

        return charsetName;
    }

    public TimeZone getDefaultTimeZone() {
        return this.defaultTimeZone;
    }

    public String getErrorMessageEncoding() {
        return this.errorMessageEncoding;
    }

    public int getHoldability() throws SQLException {
        return 2;
    }

    public void setHoldability(int arg0) throws SQLException {
    }

    public long getId() {
        return this.connectionId;
    }

    public synchronized long getIdleFor() {
        if (this.lastQueryFinishedTime == 0L) {
            return 0L;
        }

        long now = System.currentTimeMillis();
        long idleTime = now - this.lastQueryFinishedTime;

        return idleTime;
    }

    public MysqlIO getIO() throws SQLException {
        if (this.io == null || this.isClosed) {
            throw SQLError.createSQLException("Operation not allowed on closed connection", "08003", getExceptionInterceptor());
        }

        return this.io;
    }

    public Log getLog() throws SQLException {
        return this.log;
    }

    public int getMaxBytesPerChar(String javaCharsetName) throws SQLException {
        return getMaxBytesPerChar((Integer) null, javaCharsetName);
    }

    public int getMaxBytesPerChar(Integer charsetIndex, String javaCharsetName) throws SQLException {
        String charset = null;

        try {
            charset = this.indexToCustomMysqlCharset.get(charsetIndex);

            if (charset == null) charset = CharsetMapping.STATIC_INDEX_TO_MYSQL_CHARSET_MAP.get(charsetIndex);

            if (charset == null) {
                charset = CharsetMapping.getMysqlEncodingForJavaEncoding(javaCharsetName, this);
                if (this.io.serverCharsetIndex == 33 && versionMeetsMinimum(5, 5, 3) && javaCharsetName.equalsIgnoreCase("UTF-8")) {
                    charset = "utf8";
                }
            }

            Integer mblen = this.mysqlCharsetToCustomMblen.get(charset);

            if (mblen == null) mblen = CharsetMapping.STATIC_CHARSET_TO_NUM_BYTES_MAP.get(charset);
            if (mblen == null) mblen = CharsetMapping.STATIC_4_0_CHARSET_TO_NUM_BYTES_MAP.get(charset);

            if (mblen != null) return mblen.intValue();
        } catch (SQLException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", (ExceptionInterceptor) null);
            sqlEx.initCause(ex);
            throw sqlEx;
        }

        return 1;
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return getMetaData(true, true);
    }

    private DatabaseMetaData getMetaData(boolean checkClosed, boolean checkForInfoSchema) throws SQLException {
        if (checkClosed) {
            checkClosed();
        }

        return DatabaseMetaData.getInstance(getLoadBalanceSafeProxy(), this.database, checkForInfoSchema);
    }

    public Statement getMetadataSafeStatement() throws SQLException {
        Statement stmt = createStatement();

        if (stmt.getMaxRows() != 0) {
            stmt.setMaxRows(0);
        }

        stmt.setEscapeProcessing(false);

        if (stmt.getFetchSize() != 0) {
            stmt.setFetchSize(0);
        }

        return stmt;
    }

    public int getNetBufferLength() {
        return this.netBufferLength;
    }

    public String getServerCharacterEncoding() {
        if (this.io.versionMeetsMinimum(4, 1, 0)) {
            String charset = this.indexToCustomMysqlCharset.get(Integer.valueOf(this.io.serverCharsetIndex));
            if (charset == null)
                charset = CharsetMapping.STATIC_INDEX_TO_MYSQL_CHARSET_MAP.get(Integer.valueOf(this.io.serverCharsetIndex));
            return (charset != null) ? charset : this.serverVariables.get("character_set_server");
        }
        return this.serverVariables.get("character_set");
    }

    public int getServerMajorVersion() {
        return this.io.getServerMajorVersion();
    }

    public int getServerMinorVersion() {
        return this.io.getServerMinorVersion();
    }

    public int getServerSubMinorVersion() {
        return this.io.getServerSubMinorVersion();
    }

    public TimeZone getServerTimezoneTZ() {
        return this.serverTimezoneTZ;
    }

    public String getServerVariable(String variableName) {
        if (this.serverVariables != null) {
            return this.serverVariables.get(variableName);
        }

        return null;
    }

    public String getServerVersion() {
        return this.io.getServerVersion();
    }

    public Calendar getSessionLockedCalendar() {
        return this.sessionCalendar;
    }

    public synchronized int getTransactionIsolation() throws SQLException {
        if (this.hasIsolationLevels && !getUseLocalSessionState()) {
            Statement stmt = null;
            ResultSet rs = null;

            try {
                stmt = getMetadataSafeStatement();

                String query = null;

                int offset = 0;

                if (versionMeetsMinimum(4, 0, 3)) {
                    query = "SELECT @@session.tx_isolation";
                    offset = 1;
                } else {
                    query = "SHOW VARIABLES LIKE 'transaction_isolation'";
                    offset = 2;
                }

                rs = stmt.executeQuery(query);

                if (rs.next()) {
                    String s = rs.getString(offset);

                    if (s != null) {
                        Integer intTI = mapTransIsolationNameToValue.get(s);

                        if (intTI != null) {
                            return intTI.intValue();
                        }
                    }

                    throw SQLError.createSQLException("Could not map transaction isolation '" + s + " to a valid JDBC level.", "S1000", getExceptionInterceptor());
                }

                throw SQLError.createSQLException("Could not retrieve transaction isolation level from server", "S1000", getExceptionInterceptor());

            } finally {

                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception ex) {
                    }

                    rs = null;
                }

                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Exception ex) {
                    }

                    stmt = null;
                }
            }
        }

        return this.isolationLevel;
    }

    public synchronized void setTransactionIsolation(int level) throws SQLException {
        checkClosed();

        if (this.hasIsolationLevels) {
            String sql = null;

            boolean shouldSendSet = false;

            if (getAlwaysSendSetIsolation()) {
                shouldSendSet = true;
            } else if (level != this.isolationLevel) {
                shouldSendSet = true;
            }

            if (getUseLocalSessionState()) {
                shouldSendSet = (this.isolationLevel != level);
            }

            if (shouldSendSet) {
                switch (level) {
                    case 0:
                        throw SQLError.createSQLException("Transaction isolation level NONE not supported by MySQL", getExceptionInterceptor());

                    case 2:
                        sql = "SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED";
                        break;

                    case 1:
                        sql = "SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED";
                        break;

                    case 4:
                        sql = "SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ";
                        break;

                    case 8:
                        sql = "SET SESSION TRANSACTION ISOLATION LEVEL SERIALIZABLE";
                        break;

                    default:
                        throw SQLError.createSQLException("Unsupported transaction isolation level '" + level + "'", "S1C00", getExceptionInterceptor());
                }

                execSQL((StatementImpl) null, sql, -1, (Buffer) null, 1003, 1007, false, this.database, (Field[]) null, false);

                this.isolationLevel = level;
            }
        } else {
            throw SQLError.createSQLException("Transaction Isolation Levels are not supported on MySQL versions older than 3.23.36.", "S1C00", getExceptionInterceptor());
        }
    }

    public synchronized Map<String, Class<?>> getTypeMap() throws SQLException {
        if (this.typeMap == null) {
            this.typeMap = new HashMap<String, Class<?>>();
        }

        return this.typeMap;
    }

    public synchronized void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        this.typeMap = map;
    }

    public String getURL() {
        return this.myURL;
    }

    public String getUser() {
        return this.user;
    }

    public Calendar getUtcCalendar() {
        return this.utcCalendar;
    }

    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    public boolean hasSameProperties(Connection c) {
        return this.props.equals(c.getProperties());
    }

    public Properties getProperties() {
        return this.props;
    }

    public boolean hasTriedMaster() {
        return this.hasTriedMasterFlag;
    }

    public void incrementNumberOfPreparedExecutes() {
        if (getGatherPerformanceMetrics()) {
            this.numberOfPreparedExecutes++;

            this.numberOfQueriesIssued++;
        }
    }

    public void incrementNumberOfPrepares() {
        if (getGatherPerformanceMetrics()) {
            this.numberOfPrepares++;
        }
    }

    public void incrementNumberOfResultSetsCreated() {
        if (getGatherPerformanceMetrics()) {
            this.numberOfResultSetsCreated++;
        }
    }

    private void initializeDriverProperties(Properties info) throws SQLException {
        initializeProperties(info);

        String exceptionInterceptorClasses = getExceptionInterceptors();

        if (exceptionInterceptorClasses != null && !"".equals(exceptionInterceptorClasses)) {
            this.exceptionInterceptor = new ExceptionInterceptorChain(exceptionInterceptorClasses);
            this.exceptionInterceptor.init(this, info);
        }

        this.usePlatformCharsetConverters = getUseJvmCharsetConverters();

        this.log = LogFactory.getLogger(getLogger(), "MySQL", getExceptionInterceptor());

        if (getProfileSql() || getUseUsageAdvisor()) {
            this.eventSink = ProfilerEventHandlerFactory.getInstance(getLoadBalanceSafeProxy());
        }

        if (getCachePreparedStatements()) {
            createPreparedStatementCaches();
        }

        if (getNoDatetimeStringSync() && getUseTimezone()) {
            throw SQLError.createSQLException("Can't enable noDatetimeSync and useTimezone configuration properties at the same time", "01S00", getExceptionInterceptor());
        }

        if (getCacheCallableStatements()) {
            this.parsedCallableStatementCache = new LRUCache(getCallableStatementCacheSize());
        }

        if (getAllowMultiQueries()) {
            setCacheResultSetMetadata(false);
        }

        if (getCacheResultSetMetadata()) {
            this.resultSetMetadataCache = new LRUCache(getMetadataCacheSize());
        }
    }

    private void initializePropsFromServer() throws SQLException {
        String connectionInterceptorClasses = getConnectionLifecycleInterceptors();

        this.connectionLifecycleInterceptors = null;

        if (connectionInterceptorClasses != null) {
            this.connectionLifecycleInterceptors = Util.loadExtensions(this, this.props, connectionInterceptorClasses, "Connection.badLifecycleInterceptor", getExceptionInterceptor());
        }

        setSessionVariables();

        if (!versionMeetsMinimum(4, 1, 0)) {
            setTransformedBitIsBoolean(false);
        }

        this.parserKnowsUnicode = versionMeetsMinimum(4, 1, 0);

        if (getUseServerPreparedStmts() && versionMeetsMinimum(4, 1, 0)) {
            this.useServerPreparedStmts = true;

            if (versionMeetsMinimum(5, 0, 0) && !versionMeetsMinimum(5, 0, 3)) {
                this.useServerPreparedStmts = false;
            }
        }

        if (versionMeetsMinimum(3, 21, 22)) {
            loadServerVariables();

            if (versionMeetsMinimum(5, 0, 2)) {
                this.autoIncrementIncrement = getServerVariableAsInt("auto_increment_increment", 1);
            } else {
                this.autoIncrementIncrement = 1;
            }

            buildCollationMapping();

            LicenseConfiguration.checkLicenseType(this.serverVariables);

            String lowerCaseTables = this.serverVariables.get("lower_case_table_names");

            this.lowerCaseTableNames = ("on".equalsIgnoreCase(lowerCaseTables) || "1".equalsIgnoreCase(lowerCaseTables) || "2".equalsIgnoreCase(lowerCaseTables));

            this.storesLowerCaseTableName = ("1".equalsIgnoreCase(lowerCaseTables) || "on".equalsIgnoreCase(lowerCaseTables));

            configureTimezone();

            if (this.serverVariables.containsKey("max_allowed_packet")) {
                int serverMaxAllowedPacket = getServerVariableAsInt("max_allowed_packet", -1);

                if (serverMaxAllowedPacket != -1 && (serverMaxAllowedPacket < getMaxAllowedPacket() || getMaxAllowedPacket() <= 0)) {

                    setMaxAllowedPacket(serverMaxAllowedPacket);
                } else if (serverMaxAllowedPacket == -1 && getMaxAllowedPacket() == -1) {
                    setMaxAllowedPacket(65535);
                }
                int preferredBlobSendChunkSize = getBlobSendChunkSize();

                int allowedBlobSendChunkSize = Math.min(preferredBlobSendChunkSize, getMaxAllowedPacket()) - 8192 - 11;

                setBlobSendChunkSize(String.valueOf(allowedBlobSendChunkSize));
            }

            if (this.serverVariables.containsKey("net_buffer_length")) {
                this.netBufferLength = getServerVariableAsInt("net_buffer_length", 16384);
            }

            checkTransactionIsolationLevel();

            if (!versionMeetsMinimum(4, 1, 0)) {
                checkServerEncoding();
            }

            this.io.checkForCharsetMismatch();

            if (this.serverVariables.containsKey("sql_mode")) {
                int sqlMode = 0;

                String sqlModeAsString = this.serverVariables.get("sql_mode");
                try {
                    sqlMode = Integer.parseInt(sqlModeAsString);
                } catch (NumberFormatException nfe) {

                    sqlMode = 0;

                    if (sqlModeAsString != null) {
                        if (sqlModeAsString.indexOf("ANSI_QUOTES") != -1) {
                            sqlMode |= 0x4;
                        }

                        if (sqlModeAsString.indexOf("NO_BACKSLASH_ESCAPES") != -1) {
                            this.noBackslashEscapes = true;
                        }
                    }
                }

                if ((sqlMode & 0x4) > 0) {
                    this.useAnsiQuotes = true;
                } else {
                    this.useAnsiQuotes = false;
                }
            }
        }

        try {
            this.errorMessageEncoding = CharsetMapping.getCharacterEncodingForErrorMessages(this);
        } catch (SQLException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", (ExceptionInterceptor) null);
            sqlEx.initCause(ex);
            throw sqlEx;
        }

        boolean overrideDefaultAutocommit = isAutoCommitNonDefaultOnServer();

        configureClientCharacterSet(false);

        if (versionMeetsMinimum(3, 23, 15)) {
            this.transactionsSupported = true;

            if (!overrideDefaultAutocommit) {
                try {
                    setAutoCommit(true);

                } catch (SQLException ex) {
                    if (ex.getErrorCode() != 1820 || getDisconnectOnExpiredPasswords()) {
                        throw ex;
                    }
                }
            }
        } else {
            this.transactionsSupported = false;
        }

        if (versionMeetsMinimum(3, 23, 36)) {
            this.hasIsolationLevels = true;
        } else {
            this.hasIsolationLevels = false;
        }

        this.hasQuotedIdentifiers = versionMeetsMinimum(3, 23, 6);

        this.io.resetMaxBuf();

        if (this.io.versionMeetsMinimum(4, 1, 0)) {
            String characterSetResultsOnServerMysql = this.serverVariables.get("jdbc.local.character_set_results");

            if (characterSetResultsOnServerMysql == null || StringUtils.startsWithIgnoreCaseAndWs(characterSetResultsOnServerMysql, "NULL") || characterSetResultsOnServerMysql.length() == 0) {

                String defaultMetadataCharsetMysql = this.serverVariables.get("character_set_system");
                String defaultMetadataCharset = null;

                if (defaultMetadataCharsetMysql != null) {
                    defaultMetadataCharset = getJavaEncodingForMysqlEncoding(defaultMetadataCharsetMysql);
                } else {
                    defaultMetadataCharset = "UTF-8";
                }

                this.characterSetMetadata = defaultMetadataCharset;
            } else {
                this.characterSetResultsOnServer = getJavaEncodingForMysqlEncoding(characterSetResultsOnServerMysql);
                this.characterSetMetadata = this.characterSetResultsOnServer;
            }
        } else {
            this.characterSetMetadata = getEncoding();
        }

        if (versionMeetsMinimum(4, 1, 0) && !versionMeetsMinimum(4, 1, 10) && getAllowMultiQueries()) {

            if (isQueryCacheEnabled()) {
                setAllowMultiQueries(false);
            }
        }

        if (versionMeetsMinimum(5, 0, 0) && (getUseLocalTransactionState() || getElideSetAutoCommits()) && isQueryCacheEnabled() && !versionMeetsMinimum(6, 0, 10)) {

            setUseLocalTransactionState(false);
            setElideSetAutoCommits(false);
        }

        setupServerForTruncationChecks();
    }

    private boolean isQueryCacheEnabled() {
        return ("ON".equalsIgnoreCase(this.serverVariables.get("query_cache_type")) && !"0".equalsIgnoreCase(this.serverVariables.get("query_cache_size")));
    }

    private int getServerVariableAsInt(String variableName, int fallbackValue) throws SQLException {
        try {
            return Integer.parseInt(this.serverVariables.get(variableName));
        } catch (NumberFormatException nfe) {
            getLog().logWarn(Messages.getString("Connection.BadValueInServerVariables", new Object[]{variableName, this.serverVariables.get(variableName), Integer.valueOf(fallbackValue)}));

            return fallbackValue;
        }
    }

    private boolean isAutoCommitNonDefaultOnServer() throws SQLException {
        boolean overrideDefaultAutocommit = false;

        String initConnectValue = this.serverVariables.get("init_connect");

        if (versionMeetsMinimum(4, 1, 2) && initConnectValue != null && initConnectValue.length() > 0) {
            if (!getElideSetAutoCommits()) {

                ResultSet rs = null;
                Statement stmt = null;

                try {
                    stmt = getMetadataSafeStatement();

                    rs = stmt.executeQuery("SELECT @@session.autocommit");

                    if (rs.next()) {
                        this.autoCommit = rs.getBoolean(1);
                        if (this.autoCommit != true) {
                            overrideDefaultAutocommit = true;
                        }
                    }
                } finally {

                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (SQLException sqlEx) {
                        }
                    }

                    if (stmt != null) {
                        try {
                            stmt.close();
                        } catch (SQLException sqlEx) {
                        }

                    }
                }

            } else if (getIO().isSetNeededForAutoCommitMode(true)) {

                this.autoCommit = false;
                overrideDefaultAutocommit = true;
            }
        }

        return overrideDefaultAutocommit;
    }

    public boolean isClientTzUTC() {
        return this.isClientTzUTC;
    }

    public boolean isClosed() {
        return this.isClosed;
    }

    public boolean isCursorFetchEnabled() throws SQLException {
        return (versionMeetsMinimum(5, 0, 2) && getUseCursorFetch());
    }

    public boolean isInGlobalTx() {
        return this.isInGlobalTx;
    }

    public void setInGlobalTx(boolean flag) {
        this.isInGlobalTx = flag;
    }

    public synchronized boolean isMasterConnection() {
        return false;
    }

    public boolean isNoBackslashEscapesSet() {
        return this.noBackslashEscapes;
    }

    public boolean isReadInfoMsgEnabled() {
        return this.readInfoMsg;
    }

    public void setReadInfoMsgEnabled(boolean flag) {
        this.readInfoMsg = flag;
    }

    public boolean isReadOnly() throws SQLException {
        return isReadOnly(true);
    }

    public void setReadOnly(boolean readOnlyFlag) throws SQLException {
        checkClosed();

        setReadOnlyInternal(readOnlyFlag);
    }

    public boolean isReadOnly(boolean useSessionStatus) throws SQLException {
        if (useSessionStatus && !this.isClosed && versionMeetsMinimum(5, 6, 5) && !getUseLocalSessionState()) {
            Statement stmt = null;
            ResultSet rs = null;

            try {
                stmt = getMetadataSafeStatement();

                rs = stmt.executeQuery("select @@session.tx_read_only");
                if (rs.next()) {
                    return (rs.getInt(1) != 0);
                }
            } catch (SQLException ex1) {
                if (ex1.getErrorCode() != 1820 || getDisconnectOnExpiredPasswords()) {
                    throw SQLError.createSQLException("Could not retrieve transation read-only status server", "S1000", ex1, getExceptionInterceptor());

                }

            } finally {

                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception ex) {
                    }

                    rs = null;
                }

                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Exception ex) {
                    }

                    stmt = null;
                }
            }
        }

        return this.readOnly;
    }

    public boolean isRunningOnJDK13() {
        return this.isRunningOnJDK13;
    }

    public synchronized boolean isSameResource(Connection otherConnection) {
        if (otherConnection == null) {
            return false;
        }

        boolean directCompare = true;

        String otherHost = ((ConnectionImpl) otherConnection).origHostToConnectTo;
        String otherOrigDatabase = ((ConnectionImpl) otherConnection).origDatabaseToConnectTo;
        String otherCurrentCatalog = ((ConnectionImpl) otherConnection).database;

        if (!nullSafeCompare(otherHost, this.origHostToConnectTo)) {
            directCompare = false;
        } else if (otherHost != null && otherHost.indexOf(',') == -1 && otherHost.indexOf(':') == -1) {

            directCompare = (((ConnectionImpl) otherConnection).origPortToConnectTo == this.origPortToConnectTo);
        }

        if (directCompare) {
            if (!nullSafeCompare(otherOrigDatabase, this.origDatabaseToConnectTo)) {
                directCompare = false;
                directCompare = false;
            } else if (!nullSafeCompare(otherCurrentCatalog, this.database)) {
                directCompare = false;
            }

        }

        if (directCompare) {
            return true;
        }

        String otherResourceId = ((ConnectionImpl) otherConnection).getResourceId();
        String myResourceId = getResourceId();

        if (otherResourceId != null || myResourceId != null) {
            directCompare = nullSafeCompare(otherResourceId, myResourceId);

            if (directCompare) {
                return true;
            }
        }

        return false;
    }

    public boolean isServerTzUTC() {
        return this.isServerTzUTC;
    }

    private synchronized void createConfigCacheIfNeeded() throws SQLException {
        if (this.serverConfigCache != null) return;
        try {
            Class<?> factoryClass = Class.forName(getServerConfigCacheFactory());
            CacheAdapterFactory<String, Map<String, String>> cacheFactory = (CacheAdapterFactory<String, Map<String, String>>) factoryClass.newInstance();
            this.serverConfigCache = cacheFactory.getInstance(this, this.myURL, 2147483647, 2147483647, this.props);
            ExceptionInterceptor evictOnCommsError = new ExceptionInterceptor() {
                public void init(Connection conn, Properties config) throws SQLException {
                }

                public void destroy() {
                }

                public SQLException interceptException(SQLException sqlEx, Connection conn) {
                    if (sqlEx.getSQLState() != null && sqlEx.getSQLState().startsWith("08"))
                        ConnectionImpl.this.serverConfigCache.invalidate(ConnectionImpl.this.getURL());
                    return null;
                }
            };
            if (this.exceptionInterceptor == null) {
                this.exceptionInterceptor = evictOnCommsError;
            } else {
                ((ExceptionInterceptorChain) this.exceptionInterceptor).addRingZero(evictOnCommsError);
            }
        } catch (ClassNotFoundException e) {
            SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.CantFindCacheFactory", new Object[]{getParseInfoCacheFactory(), "parseInfoCacheFactory"}), getExceptionInterceptor());
            sqlEx.initCause(e);
            throw sqlEx;
        } catch (InstantiationException e) {
            SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.CantLoadCacheFactory", new Object[]{getParseInfoCacheFactory(), "parseInfoCacheFactory"}), getExceptionInterceptor());
            sqlEx.initCause(e);
            throw sqlEx;
        } catch (IllegalAccessException e) {
            SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.CantLoadCacheFactory", new Object[]{getParseInfoCacheFactory(), "parseInfoCacheFactory"}), getExceptionInterceptor());
            sqlEx.initCause(e);
            throw sqlEx;
        }
    }

    public int getAutoIncrementIncrement() {
        return this.autoIncrementIncrement;
    }

    private void loadServerVariables() throws SQLException {
        if (getCacheServerConfiguration()) {
            createConfigCacheIfNeeded();
            Map<String, String> cachedVariableMap = this.serverConfigCache.get(getURL());
            if (cachedVariableMap != null) {
                String cachedServerVersion = cachedVariableMap.get("server_version_string");
                if (cachedServerVersion != null && this.io.getServerVersion() != null && cachedServerVersion.equals(this.io.getServerVersion())) {
                    this.serverVariables = cachedVariableMap;
                    this.usingCachedConfig = true;
                    return;
                }
                this.serverConfigCache.invalidate(getURL());
            }
        }
        Statement stmt = null;
        ResultSet results = null;
        try {
            stmt = getMetadataSafeStatement();
            String version = this.dbmd.getDriverVersion();
            if (version != null && version.indexOf('*') != -1) {
                StringBuffer buf = new StringBuffer(version.length() + 10);
                for (int i = 0; i < version.length(); i++) {
                    char c = version.charAt(i);
                    if (c == '*') {
                        buf.append("[star]");
                    } else {
                        buf.append(c);
                    }
                }
                version = buf.toString();
            }
            String versionComment = (getParanoid() || version == null) ? "" : ("");
            String query = versionComment + "SHOW VARIABLES";
            if (versionMeetsMinimum(5, 0, 3))
                query = versionComment + "SHOW VARIABLES WHERE Variable_name ='language'" + " OR Variable_name = 'net_write_timeout'" + " OR Variable_name = 'interactive_timeout'" + " OR Variable_name = 'wait_timeout'" + " OR Variable_name = 'character_set_client'" + " OR Variable_name = 'character_set_connection'" + " OR Variable_name = 'character_set'" + " OR Variable_name = 'character_set_server'" + " OR Variable_name = 'tx_isolation'" + " OR Variable_name = 'transaction_isolation'" + " OR Variable_name = 'character_set_results'" + " OR Variable_name = 'timezone'" + " OR Variable_name = 'time_zone'" + " OR Variable_name = 'system_time_zone'" + " OR Variable_name = 'lower_case_table_names'" + " OR Variable_name = 'max_allowed_packet'" + " OR Variable_name = 'net_buffer_length'" + " OR Variable_name = 'sql_mode'" + " OR Variable_name = 'query_cache_type'" + " OR Variable_name = 'query_cache_size'" + " OR Variable_name = 'init_connect'";
            this.serverVariables = new HashMap<String, String>();
            try {
                results = stmt.executeQuery(query);
                while (results.next())
                    this.serverVariables.put(results.getString(1), results.getString(2));
                results.close();
                results = null;
            } catch (SQLException ex) {
                if (ex.getErrorCode() != 1820 || getDisconnectOnExpiredPasswords())
                    throw ex;
            }
            if (versionMeetsMinimum(5, 0, 2))
                try {
                    results = stmt.executeQuery(versionComment + "SELECT @@session.auto_increment_increment");
                    if (results.next())
                        this.serverVariables.put("auto_increment_increment", results.getString(1));
                } catch (SQLException ex) {
                    if (ex.getErrorCode() != 1820 || getDisconnectOnExpiredPasswords())
                        throw ex;
                }
            if (getCacheServerConfiguration()) {
                this.serverVariables.put("server_version_string", this.io.getServerVersion());
                this.serverConfigCache.put(getURL(), this.serverVariables);
                this.usingCachedConfig = true;
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (results != null)
                try {
                    results.close();
                } catch (SQLException sqlE) {
                }
            if (stmt != null)
                try {
                    stmt.close();
                } catch (SQLException sqlE) {
                }
        }
    }

    public boolean lowerCaseTableNames() {
        return this.lowerCaseTableNames;
    }

    public synchronized void maxRowsChanged(Statement stmt) {
        if (this.statementsUsingMaxRows == null) {
            this.statementsUsingMaxRows = new HashMap<Statement, Statement>();
        }

        this.statementsUsingMaxRows.put(stmt, stmt);

        this.maxRowsChanged = true;
    }

    public String nativeSQL(String sql) throws SQLException {
        if (sql == null) {
            return null;
        }

        Object escapedSqlResult = EscapeProcessor.escapeSQL(sql, serverSupportsConvertFn(), getLoadBalanceSafeProxy());

        if (escapedSqlResult instanceof String) {
            return (String) escapedSqlResult;
        }

        return ((EscapeProcessorResult) escapedSqlResult).escapedSql;
    }

    private CallableStatement parseCallableStatement(String sql) throws SQLException {
        Object escapedSqlResult = EscapeProcessor.escapeSQL(sql, serverSupportsConvertFn(), getLoadBalanceSafeProxy());

        boolean isFunctionCall = false;
        String parsedSql = null;

        if (escapedSqlResult instanceof EscapeProcessorResult) {
            parsedSql = ((EscapeProcessorResult) escapedSqlResult).escapedSql;
            isFunctionCall = ((EscapeProcessorResult) escapedSqlResult).callingStoredFunction;
        } else {
            parsedSql = (String) escapedSqlResult;
            isFunctionCall = false;
        }

        return CallableStatement.getInstance(getLoadBalanceSafeProxy(), parsedSql, this.database, isFunctionCall);
    }

    public boolean parserKnowsUnicode() {
        return this.parserKnowsUnicode;
    }

    public void ping() throws SQLException {
        pingInternal(true, 0);
    }

    public void pingInternal(boolean checkForClosedConnection, int timeoutMillis) throws SQLException {
        if (checkForClosedConnection) {
            checkClosed();
        }

        long pingMillisLifetime = getSelfDestructOnPingSecondsLifetime();
        int pingMaxOperations = getSelfDestructOnPingMaxOperations();

        if ((pingMillisLifetime > 0L && System.currentTimeMillis() - this.connectionCreationTimeMillis > pingMillisLifetime) || (pingMaxOperations > 0 && pingMaxOperations <= this.io.getCommandCount())) {

            close();

            throw SQLError.createSQLException(Messages.getString("Connection.exceededConnectionLifetime"), "08S01", getExceptionInterceptor());
        }

        this.io.sendCommand(14, null, null, false, null, timeoutMillis);
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        return prepareCall(sql, 1003, 1007);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        if (versionMeetsMinimum(5, 0, 0)) {
            CallableStatement cStmt = null;

            if (!getCacheCallableStatements()) {

                cStmt = parseCallableStatement(sql);
            } else {
                synchronized (this.parsedCallableStatementCache) {
                    CompoundCacheKey key = new CompoundCacheKey(getCatalog(), sql);

                    CallableStatement.CallableStatementParamInfo cachedParamInfo = (CallableStatement.CallableStatementParamInfo) this.parsedCallableStatementCache.get(key);

                    if (cachedParamInfo != null) {
                        cStmt = CallableStatement.getInstance(getLoadBalanceSafeProxy(), cachedParamInfo);
                    } else {
                        cStmt = parseCallableStatement(sql);

                        synchronized (cStmt) {
                            cachedParamInfo = cStmt.paramInfo;
                        }

                        this.parsedCallableStatementCache.put(key, cachedParamInfo);
                    }
                }
            }

            cStmt.setResultSetType(resultSetType);
            cStmt.setResultSetConcurrency(resultSetConcurrency);

            return cStmt;
        }

        throw SQLError.createSQLException("Callable statements not supported.", "S1C00", getExceptionInterceptor());
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        if (getPedantic() &&
                resultSetHoldability != 1) {
            throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", "S1009", getExceptionInterceptor());
        }

        CallableStatement cStmt = (CallableStatement) prepareCall(sql, resultSetType, resultSetConcurrency);

        return cStmt;
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return prepareStatement(sql, 1003, 1007);
    }

    public PreparedStatement prepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
        PreparedStatement pStmt = prepareStatement(sql);

        ((PreparedStatement) pStmt).setRetrieveGeneratedKeys((autoGenKeyIndex == 1));

        return pStmt;
    }

    public synchronized PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        checkClosed();

        PreparedStatement pStmt = null;

        boolean canServerPrepare = true;

        String nativeSql = getProcessEscapeCodesForPrepStmts() ? nativeSQL(sql) : sql;

        if (this.useServerPreparedStmts && getEmulateUnsupportedPstmts()) {
            canServerPrepare = canHandleAsServerPreparedStatement(nativeSql);
        }

        if (this.useServerPreparedStmts && canServerPrepare) {
            if (getCachePreparedStatements()) {
                synchronized (this.serverSideStatementCache) {
                    pStmt = (ServerPreparedStatement) this.serverSideStatementCache.remove(sql);

                    if (pStmt != null) {
                        ((ServerPreparedStatement) pStmt).setClosed(false);
                        pStmt.clearParameters();
                    }

                    if (pStmt == null) {
                        try {
                            pStmt = ServerPreparedStatement.getInstance(getLoadBalanceSafeProxy(), nativeSql, this.database, resultSetType, resultSetConcurrency);

                            if (sql.length() < getPreparedStatementCacheSqlLimit()) {
                                ((ServerPreparedStatement) pStmt).isCached = true;
                            }

                            pStmt.setResultSetType(resultSetType);
                            pStmt.setResultSetConcurrency(resultSetConcurrency);
                        } catch (SQLException sqlEx) {

                            if (getEmulateUnsupportedPstmts()) {
                                pStmt = (PreparedStatement) clientPrepareStatement(nativeSql, resultSetType, resultSetConcurrency, false);

                                if (sql.length() < getPreparedStatementCacheSqlLimit()) {
                                    this.serverSideStatementCheckCache.put(sql, Boolean.FALSE);
                                }
                            } else {
                                throw sqlEx;
                            }
                        }
                    }
                }
            } else {
                try {
                    pStmt = ServerPreparedStatement.getInstance(getLoadBalanceSafeProxy(), nativeSql, this.database, resultSetType, resultSetConcurrency);

                    pStmt.setResultSetType(resultSetType);
                    pStmt.setResultSetConcurrency(resultSetConcurrency);
                } catch (SQLException sqlEx) {

                    if (getEmulateUnsupportedPstmts()) {
                        pStmt = (PreparedStatement) clientPrepareStatement(nativeSql, resultSetType, resultSetConcurrency, false);
                    } else {
                        throw sqlEx;
                    }
                }
            }
        } else {
            pStmt = (PreparedStatement) clientPrepareStatement(nativeSql, resultSetType, resultSetConcurrency, false);
        }

        return pStmt;
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        if (getPedantic() &&
                resultSetHoldability != 1) {
            throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", "S1009", getExceptionInterceptor());
        }

        return prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    public PreparedStatement prepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
        PreparedStatement pStmt = prepareStatement(sql);

        ((PreparedStatement) pStmt).setRetrieveGeneratedKeys((autoGenKeyIndexes != null && autoGenKeyIndexes.length > 0));

        return pStmt;
    }

    public PreparedStatement prepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
        PreparedStatement pStmt = prepareStatement(sql);

        ((PreparedStatement) pStmt).setRetrieveGeneratedKeys((autoGenKeyColNames != null && autoGenKeyColNames.length > 0));

        return pStmt;
    }

    public void realClose(boolean calledExplicitly, boolean issueRollback, boolean skipLocalTeardown, Throwable reason) throws SQLException {
        SQLException sqlEx = null;

        if (isClosed()) {
            return;
        }

        this.forceClosedReason = reason;

        try {
            if (!skipLocalTeardown) {
                if (!getAutoCommit() && issueRollback) {
                    try {
                        rollback();
                    } catch (SQLException ex) {
                        sqlEx = ex;
                    }
                }

                reportMetrics();

                if (getUseUsageAdvisor()) {
                    if (!calledExplicitly) {
                        String message = "Connection implicitly closed by Driver. You should call Connection.close() from your code to free resources more efficiently and avoid resource leaks.";

                        this.eventSink.consumeEvent(new ProfilerEvent((byte) 0, "", getCatalog(), getId(), -1, -1, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, message));
                    }

                    long connectionLifeTime = System.currentTimeMillis() - this.connectionCreationTimeMillis;

                    if (connectionLifeTime < 500L) {
                        String message = "Connection lifetime of < .5 seconds. You might be un-necessarily creating short-lived connections and should investigate connection pooling to be more efficient.";

                        this.eventSink.consumeEvent(new ProfilerEvent((byte) 0, "", getCatalog(), getId(), -1, -1, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, message));
                    }
                }

                try {
                    closeAllOpenStatements();
                } catch (SQLException ex) {
                    sqlEx = ex;
                }

                if (this.io != null) {
                    try {
                        this.io.quit();
                    } catch (Exception e) {
                    }

                }
            } else {

                this.io.forceClose();
            }

            if (this.statementInterceptors != null) {
                for (int i = 0; i < this.statementInterceptors.size(); i++) {
                    ((StatementInterceptorV2) this.statementInterceptors.get(i)).destroy();
                }
            }

            if (this.exceptionInterceptor != null) {
                this.exceptionInterceptor.destroy();
            }
        } finally {
            this.openStatements = null;
            this.io = null;
            this.statementInterceptors = null;
            this.exceptionInterceptor = null;
            ProfilerEventHandlerFactory.removeInstance(this);

            synchronized (this) {
                if (this.cancelTimer != null) {
                    this.cancelTimer.cancel();
                }
            }

            this.isClosed = true;
        }

        if (sqlEx != null) {
            throw sqlEx;
        }
    }

    public synchronized void recachePreparedStatement(ServerPreparedStatement pstmt) throws SQLException {
        if (pstmt.isPoolable()) {
            synchronized (this.serverSideStatementCache) {
                this.serverSideStatementCache.put(pstmt.originalSql, pstmt);
            }
        }
    }

    public void registerQueryExecutionTime(long queryTimeMs) {
        if (queryTimeMs > this.longestQueryTimeMs) {
            this.longestQueryTimeMs = queryTimeMs;

            repartitionPerformanceHistogram();
        }

        addToPerformanceHistogram(queryTimeMs, 1);

        if (queryTimeMs < this.shortestQueryTimeMs) {
            this.shortestQueryTimeMs = (queryTimeMs == 0L) ? 1L : queryTimeMs;
        }

        this.numberOfQueriesIssued++;

        this.totalQueryTimeMs += queryTimeMs;
    }

    public void registerStatement(Statement stmt) {
        synchronized (this.openStatements) {
            this.openStatements.put(stmt, stmt);
        }
    }

    public void releaseSavepoint(Savepoint arg0) throws SQLException {
    }

    private void repartitionHistogram(int[] histCounts, long[] histBreakpoints, long currentLowerBound, long currentUpperBound) {
        if (this.oldHistCounts == null) {
            this.oldHistCounts = new int[histCounts.length];
            this.oldHistBreakpoints = new long[histBreakpoints.length];
        }

        System.arraycopy(histCounts, 0, this.oldHistCounts, 0, histCounts.length);

        System.arraycopy(histBreakpoints, 0, this.oldHistBreakpoints, 0, histBreakpoints.length);

        createInitialHistogram(histBreakpoints, currentLowerBound, currentUpperBound);

        for (int i = 0; i < 20; i++) {
            addToHistogram(histCounts, histBreakpoints, this.oldHistBreakpoints[i], this.oldHistCounts[i], currentLowerBound, currentUpperBound);
        }
    }

    private void repartitionPerformanceHistogram() {
        checkAndCreatePerformanceHistogram();

        repartitionHistogram(this.perfMetricsHistCounts, this.perfMetricsHistBreakpoints, (this.shortestQueryTimeMs == Long.MAX_VALUE) ? 0L : this.shortestQueryTimeMs, this.longestQueryTimeMs);
    }

    private void repartitionTablesAccessedHistogram() {
        checkAndCreateTablesAccessedHistogram();

        repartitionHistogram(this.numTablesMetricsHistCounts, this.numTablesMetricsHistBreakpoints, (this.minimumNumberTablesAccessed == Long.MAX_VALUE) ? 0L : this.minimumNumberTablesAccessed, this.maximumNumberTablesAccessed);
    }

    private void reportMetrics() {
        if (getGatherPerformanceMetrics()) {
            StringBuffer logMessage = new StringBuffer(256);

            logMessage.append("** Performance Metrics Report **\n");
            logMessage.append("\nLongest reported query: " + this.longestQueryTimeMs + " ms");

            logMessage.append("\nShortest reported query: " + this.shortestQueryTimeMs + " ms");

            logMessage.append("\nAverage query execution time: " + (this.totalQueryTimeMs / this.numberOfQueriesIssued) + " ms");

            logMessage.append("\nNumber of statements executed: " + this.numberOfQueriesIssued);

            logMessage.append("\nNumber of result sets created: " + this.numberOfResultSetsCreated);

            logMessage.append("\nNumber of statements prepared: " + this.numberOfPrepares);

            logMessage.append("\nNumber of prepared statement executions: " + this.numberOfPreparedExecutes);

            if (this.perfMetricsHistBreakpoints != null) {
                logMessage.append("\n\n\tTiming Histogram:\n");
                int maxNumPoints = 20;
                int highestCount = Integer.MIN_VALUE;
                int i;
                for (i = 0; i < 20; i++) {
                    if (this.perfMetricsHistCounts[i] > highestCount) {
                        highestCount = this.perfMetricsHistCounts[i];
                    }
                }

                if (highestCount == 0) {
                    highestCount = 1;
                }

                for (i = 0; i < 19; i++) {

                    if (i == 0) {
                        logMessage.append("\n\tless than " + this.perfMetricsHistBreakpoints[i + 1] + " ms: \t" + this.perfMetricsHistCounts[i]);
                    } else {

                        logMessage.append("\n\tbetween " + this.perfMetricsHistBreakpoints[i] + " and " + this.perfMetricsHistBreakpoints[i + 1] + " ms: \t" + this.perfMetricsHistCounts[i]);
                    }

                    logMessage.append("\t");

                    int numPointsToGraph = (int) (maxNumPoints * this.perfMetricsHistCounts[i] / highestCount);

                    for (int j = 0; j < numPointsToGraph; j++) {
                        logMessage.append("*");
                    }

                    if (this.longestQueryTimeMs < this.perfMetricsHistCounts[i + 1]) {
                        break;
                    }
                }

                if (this.perfMetricsHistBreakpoints[18] < this.longestQueryTimeMs) {
                    logMessage.append("\n\tbetween ");
                    logMessage.append(this.perfMetricsHistBreakpoints[18]);

                    logMessage.append(" and ");
                    logMessage.append(this.perfMetricsHistBreakpoints[19]);

                    logMessage.append(" ms: \t");
                    logMessage.append(this.perfMetricsHistCounts[19]);
                }
            }

            if (this.numTablesMetricsHistBreakpoints != null) {
                logMessage.append("\n\n\tTable Join Histogram:\n");
                int maxNumPoints = 20;
                int highestCount = Integer.MIN_VALUE;
                int i;
                for (i = 0; i < 20; i++) {
                    if (this.numTablesMetricsHistCounts[i] > highestCount) {
                        highestCount = this.numTablesMetricsHistCounts[i];
                    }
                }

                if (highestCount == 0) {
                    highestCount = 1;
                }

                for (i = 0; i < 19; i++) {

                    if (i == 0) {
                        logMessage.append("\n\t" + this.numTablesMetricsHistBreakpoints[i + 1] + " tables or less: \t\t" + this.numTablesMetricsHistCounts[i]);

                    } else {

                        logMessage.append("\n\tbetween " + this.numTablesMetricsHistBreakpoints[i] + " and " + this.numTablesMetricsHistBreakpoints[i + 1] + " tables: \t" + this.numTablesMetricsHistCounts[i]);
                    }

                    logMessage.append("\t");

                    int numPointsToGraph = (int) (maxNumPoints * this.numTablesMetricsHistCounts[i] / highestCount);

                    for (int j = 0; j < numPointsToGraph; j++) {
                        logMessage.append("*");
                    }

                    if (this.maximumNumberTablesAccessed < this.numTablesMetricsHistBreakpoints[i + 1]) {
                        break;
                    }
                }

                if (this.numTablesMetricsHistBreakpoints[18] < this.maximumNumberTablesAccessed) {
                    logMessage.append("\n\tbetween ");
                    logMessage.append(this.numTablesMetricsHistBreakpoints[18]);

                    logMessage.append(" and ");
                    logMessage.append(this.numTablesMetricsHistBreakpoints[19]);

                    logMessage.append(" tables: ");
                    logMessage.append(this.numTablesMetricsHistCounts[19]);
                }
            }

            this.log.logInfo(logMessage);

            this.metricsLastReportedMs = System.currentTimeMillis();
        }
    }

    protected void reportMetricsIfNeeded() {
        if (getGatherPerformanceMetrics() &&
                System.currentTimeMillis() - this.metricsLastReportedMs > getReportMetricsIntervalMillis()) {
            reportMetrics();
        }
    }

    public void reportNumberOfTablesAccessed(int numTablesAccessed) {
        if (numTablesAccessed < this.minimumNumberTablesAccessed) {
            this.minimumNumberTablesAccessed = numTablesAccessed;
        }

        if (numTablesAccessed > this.maximumNumberTablesAccessed) {
            this.maximumNumberTablesAccessed = numTablesAccessed;

            repartitionTablesAccessedHistogram();
        }

        addToTablesAccessedHistogram(numTablesAccessed, 1);
    }

    public void resetServerState() throws SQLException {
        if (!getParanoid() && this.io != null && versionMeetsMinimum(4, 0, 6)) {
            changeUser(this.user, this.password);
        }
    }

    public synchronized void rollback() throws SQLException {
        checkClosed();

        try {
            if (this.connectionLifecycleInterceptors != null) {
                IterateBlock<Extension> iter = new IterateBlock<Extension>(this.connectionLifecycleInterceptors.iterator()) {
                    void forEach(Extension each) throws SQLException {
                        if (!((ConnectionLifecycleInterceptor) each).rollback()) {
                            this.stopIterating = true;
                        }
                    }
                };

                iter.doForAll();

                if (!iter.fullIteration()) {
                    return;
                }
            }

            if (this.autoCommit && !getRelaxAutoCommit()) {
                throw SQLError.createSQLException("Can't call rollback when autocommit=true", "08003", getExceptionInterceptor());
            }

            if (this.transactionsSupported) {
                try {
                    rollbackNoChecks();
                } catch (SQLException sqlEx) {

                    if (getIgnoreNonTxTables() && sqlEx.getErrorCode() == 1196) {
                        return;
                    }

                    throw sqlEx;
                }

            }
        } catch (SQLException sqlException) {
            if ("08S01".equals(sqlException.getSQLState())) {
                throw SQLError.createSQLException("Communications link failure during rollback(). Transaction resolution unknown.", "08007", getExceptionInterceptor());
            }

            throw sqlException;
        } finally {
            this.needsPing = getReconnectAtTxEnd();
        }
    }

    public synchronized void rollback(final Savepoint savepoint) throws SQLException {
        if (versionMeetsMinimum(4, 0, 14) || versionMeetsMinimum(4, 1, 1)) {
            checkClosed();

            try {
                if (this.connectionLifecycleInterceptors != null) {
                    IterateBlock<Extension> iter = new IterateBlock<Extension>(this.connectionLifecycleInterceptors.iterator()) {
                        void forEach(Extension each) throws SQLException {
                            if (!((ConnectionLifecycleInterceptor) each).rollback(savepoint)) {
                                this.stopIterating = true;
                            }
                        }
                    };

                    iter.doForAll();

                    if (!iter.fullIteration()) {
                        return;
                    }
                }

                StringBuffer rollbackQuery = new StringBuffer("ROLLBACK TO SAVEPOINT ");

                rollbackQuery.append('`');
                rollbackQuery.append(savepoint.getSavepointName());
                rollbackQuery.append('`');

                Statement stmt = null;

                try {
                    stmt = getMetadataSafeStatement();

                    stmt.executeUpdate(rollbackQuery.toString());
                } catch (SQLException sqlEx) {
                    int errno = sqlEx.getErrorCode();

                    if (errno == 1181) {
                        String msg = sqlEx.getMessage();

                        if (msg != null) {
                            int indexOfError153 = msg.indexOf("153");

                            if (indexOfError153 != -1) {
                                throw SQLError.createSQLException("Savepoint '" + savepoint.getSavepointName() + "' does not exist", "S1009", errno, getExceptionInterceptor());
                            }
                        }
                    }

                    if (getIgnoreNonTxTables() && sqlEx.getErrorCode() != 1196) {
                        throw sqlEx;
                    }

                    if ("08S01".equals(sqlEx.getSQLState())) {
                        throw SQLError.createSQLException("Communications link failure during rollback(). Transaction resolution unknown.", "08007", getExceptionInterceptor());
                    }

                    throw sqlEx;
                } finally {
                    closeStatement(stmt);
                }
            } finally {
                this.needsPing = getReconnectAtTxEnd();
            }
        } else {
            throw SQLError.notImplemented();
        }
    }

    private void rollbackNoChecks() throws SQLException {
        if (getUseLocalTransactionState() && versionMeetsMinimum(5, 0, 0) &&
                !this.io.inTransactionOnServer()) {
            return;
        }

        execSQL((StatementImpl) null, "rollback", -1, (Buffer) null, 1003, 1007, false, this.database, (Field[]) null, false);
    }

    public PreparedStatement serverPrepareStatement(String sql) throws SQLException {
        String nativeSql = getProcessEscapeCodesForPrepStmts() ? nativeSQL(sql) : sql;

        return ServerPreparedStatement.getInstance(getLoadBalanceSafeProxy(), nativeSql, getCatalog(), 1003, 1007);
    }

    public PreparedStatement serverPrepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
        String nativeSql = getProcessEscapeCodesForPrepStmts() ? nativeSQL(sql) : sql;

        PreparedStatement pStmt = ServerPreparedStatement.getInstance(getLoadBalanceSafeProxy(), nativeSql, getCatalog(), 1003, 1007);

        pStmt.setRetrieveGeneratedKeys((autoGenKeyIndex == 1));

        return pStmt;
    }

    public PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        String nativeSql = getProcessEscapeCodesForPrepStmts() ? nativeSQL(sql) : sql;

        return ServerPreparedStatement.getInstance(getLoadBalanceSafeProxy(), nativeSql, getCatalog(), resultSetType, resultSetConcurrency);
    }

    public PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        if (getPedantic() &&
                resultSetHoldability != 1) {
            throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", "S1009", getExceptionInterceptor());
        }

        return serverPrepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    public PreparedStatement serverPrepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
        PreparedStatement pStmt = (PreparedStatement) serverPrepareStatement(sql);

        pStmt.setRetrieveGeneratedKeys((autoGenKeyIndexes != null && autoGenKeyIndexes.length > 0));

        return pStmt;
    }

    public PreparedStatement serverPrepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
        PreparedStatement pStmt = (PreparedStatement) serverPrepareStatement(sql);

        pStmt.setRetrieveGeneratedKeys((autoGenKeyColNames != null && autoGenKeyColNames.length > 0));

        return pStmt;
    }

    public boolean serverSupportsConvertFn() throws SQLException {
        return versionMeetsMinimum(4, 0, 2);
    }

    public synchronized void setFailedOver(boolean flag) {
    }

    public void setPreferSlaveDuringFailover(boolean flag) {
    }

    public void setReadOnlyInternal(boolean readOnlyFlag) throws SQLException {
        if (versionMeetsMinimum(5, 6, 5) && (
                !getUseLocalSessionState() || readOnlyFlag != this.readOnly)) {
            execSQL((StatementImpl) null, "set session transaction " + (readOnlyFlag ? "read only" : "read write"), -1, (Buffer) null, 1003, 1007, false, this.database, (Field[]) null, false);
        }

        this.readOnly = readOnlyFlag;
    }

    public Savepoint setSavepoint() throws SQLException {
        MysqlSavepoint savepoint = new MysqlSavepoint(getExceptionInterceptor());

        setSavepoint(savepoint);

        return savepoint;
    }

    private synchronized void setSavepoint(MysqlSavepoint savepoint) throws SQLException {
        if (versionMeetsMinimum(4, 0, 14) || versionMeetsMinimum(4, 1, 1)) {
            checkClosed();

            StringBuffer savePointQuery = new StringBuffer("SAVEPOINT ");
            savePointQuery.append('`');
            savePointQuery.append(savepoint.getSavepointName());
            savePointQuery.append('`');

            Statement stmt = null;

            try {
                stmt = getMetadataSafeStatement();

                stmt.executeUpdate(savePointQuery.toString());
            } finally {
                closeStatement(stmt);
            }
        } else {
            throw SQLError.notImplemented();
        }
    }

    public synchronized Savepoint setSavepoint(String name) throws SQLException {
        MysqlSavepoint savepoint = new MysqlSavepoint(name, getExceptionInterceptor());

        setSavepoint(savepoint);

        return savepoint;
    }

    private void setSessionVariables() throws SQLException {
        if (versionMeetsMinimum(4, 0, 0) && getSessionVariables() != null) {
            List<String> variablesToSet = StringUtils.split(getSessionVariables(), ",", "\"'", "\"'", false);

            int numVariablesToSet = variablesToSet.size();

            Statement stmt = null;

            try {
                stmt = getMetadataSafeStatement();

                for (int i = 0; i < numVariablesToSet; i++) {
                    String variableValuePair = variablesToSet.get(i);

                    if (variableValuePair.startsWith("@")) {
                        stmt.executeUpdate("SET " + variableValuePair);
                    } else {
                        stmt.executeUpdate("SET SESSION " + variableValuePair);
                    }
                }
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
    }

    private void setupServerForTruncationChecks() throws SQLException {
        if (getJdbcCompliantTruncation() &&
                versionMeetsMinimum(5, 0, 2)) {
            String currentSqlMode = this.serverVariables.get("sql_mode");

            boolean strictTransTablesIsSet = (StringUtils.indexOfIgnoreCase(currentSqlMode, "STRICT_TRANS_TABLES") != -1);

            if (currentSqlMode == null || currentSqlMode.length() == 0 || !strictTransTablesIsSet) {

                StringBuffer commandBuf = new StringBuffer("SET sql_mode='");

                if (currentSqlMode != null && currentSqlMode.length() > 0) {
                    commandBuf.append(currentSqlMode);
                    commandBuf.append(",");
                }

                commandBuf.append("STRICT_TRANS_TABLES'");

                execSQL((StatementImpl) null, commandBuf.toString(), -1, (Buffer) null, 1003, 1007, false, this.database, (Field[]) null, false);

                setJdbcCompliantTruncation(false);
            } else if (strictTransTablesIsSet) {

                setJdbcCompliantTruncation(false);
            }
        }
    }

    public void shutdownServer() throws SQLException {
        try {
            this.io.sendCommand(8, null, null, false, null, 0);
        } catch (Exception ex) {
            SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.UnhandledExceptionDuringShutdown"), "S1000", getExceptionInterceptor());

            sqlEx.initCause(ex);

            throw sqlEx;
        }
    }

    public boolean supportsIsolationLevel() {
        return this.hasIsolationLevels;
    }

    public boolean supportsQuotedIdentifiers() {
        return this.hasQuotedIdentifiers;
    }

    public boolean supportsTransactions() {
        return this.transactionsSupported;
    }

    public void unregisterStatement(Statement stmt) {
        if (this.openStatements != null) {
            synchronized (this.openStatements) {
                this.openStatements.remove(stmt);
            }
        }
    }

    public synchronized void unsetMaxRows(Statement stmt) throws SQLException {
        if (this.statementsUsingMaxRows != null) {
            Object found = this.statementsUsingMaxRows.remove(stmt);

            if (found != null && this.statementsUsingMaxRows.size() == 0) {

                execSQL((StatementImpl) null, "SET SQL_SELECT_LIMIT=DEFAULT", -1, (Buffer) null, 1003, 1007, false, this.database, (Field[]) null, false);

                this.maxRowsChanged = false;
            }
        }
    }

    public synchronized boolean useAnsiQuotedIdentifiers() {
        return this.useAnsiQuotes;
    }

    public synchronized boolean useMaxRows() {
        return this.maxRowsChanged;
    }

    public boolean versionMeetsMinimum(int major, int minor, int subminor) throws SQLException {
        checkClosed();

        return this.io.versionMeetsMinimum(major, minor, subminor);
    }

    public CachedResultSetMetaData getCachedMetaData(String sql) {
        if (this.resultSetMetadataCache != null) {
            synchronized (this.resultSetMetadataCache) {
                return (CachedResultSetMetaData) this.resultSetMetadataCache.get(sql);
            }
        }

        return null;
    }

    public void initializeResultsMetadataFromCache(String sql, CachedResultSetMetaData cachedMetaData, ResultSetInternalMethods resultSet) throws SQLException {
        if (cachedMetaData == null) {

            cachedMetaData = new CachedResultSetMetaData();

            resultSet.buildIndexMapping();
            resultSet.initializeWithMetadata();

            if (resultSet instanceof UpdatableResultSet) {
                ((UpdatableResultSet) resultSet).checkUpdatability();
            }

            resultSet.populateCachedMetaData(cachedMetaData);

            this.resultSetMetadataCache.put(sql, cachedMetaData);
        } else {
            resultSet.initializeFromCachedMetaData(cachedMetaData);
            resultSet.initializeWithMetadata();

            if (resultSet instanceof UpdatableResultSet) {
                ((UpdatableResultSet) resultSet).checkUpdatability();
            }
        }
    }

    public String getStatementComment() {
        return this.statementComment;
    }

    public void setStatementComment(String comment) {
        this.statementComment = comment;
    }

    public synchronized void reportQueryTime(long millisOrNanos) {
        this.queryTimeCount++;
        this.queryTimeSum += millisOrNanos;
        this.queryTimeSumSquares += (millisOrNanos * millisOrNanos);
        this.queryTimeMean = (this.queryTimeMean * (this.queryTimeCount - 1L) + millisOrNanos) / this.queryTimeCount;
    }

    public synchronized boolean isAbonormallyLongQuery(long millisOrNanos) {
        if (this.queryTimeCount < 15L) {
            return false;
        }

        double stddev = Math.sqrt((this.queryTimeSumSquares - this.queryTimeSum * this.queryTimeSum / this.queryTimeCount) / (this.queryTimeCount - 1L));

        return (millisOrNanos > this.queryTimeMean + 5.0D * stddev);
    }

    public void initializeExtension(Extension ex) throws SQLException {
        ex.init(this, this.props);
    }

    public synchronized void transactionBegun() throws SQLException {
        if (this.connectionLifecycleInterceptors != null) {
            IterateBlock<Extension> iter = new IterateBlock<Extension>(this.connectionLifecycleInterceptors.iterator()) {
                void forEach(Extension each) throws SQLException {
                    ((ConnectionLifecycleInterceptor) each).transactionBegun();
                }
            };

            iter.doForAll();
        }
    }

    public synchronized void transactionCompleted() throws SQLException {
        if (this.connectionLifecycleInterceptors != null) {
            IterateBlock<Extension> iter = new IterateBlock<Extension>(this.connectionLifecycleInterceptors.iterator()) {
                void forEach(Extension each) throws SQLException {
                    ((ConnectionLifecycleInterceptor) each).transactionCompleted();
                }
            };

            iter.doForAll();
        }
    }

    public boolean storesLowerCaseTableName() {
        return this.storesLowerCaseTableName;
    }

    public ExceptionInterceptor getExceptionInterceptor() {
        return this.exceptionInterceptor;
    }

    public boolean getRequiresEscapingEncoder() {
        return this.requiresEscapingEncoder;
    }

    public synchronized boolean isServerLocal() throws SQLException {
        SocketFactory factory = (getIO()).socketFactory;

        if (factory instanceof SocketMetadata) {
            return ((SocketMetadata) factory).isLocallyConnected(this);
        }
        getLog().logWarn(Messages.getString("Connection.NoMetadataOnSocketFactory"));
        return false;
    }

    public synchronized String getSchema() throws SQLException {
        checkClosed();

        return null;
    }

    public synchronized void setSchema(String schema) throws SQLException {
        checkClosed();
    }

    public void abort(Executor executor) throws SQLException {
        SecurityManager sec = System.getSecurityManager();

        if (sec != null) {
            sec.checkPermission(ABORT_PERM);
        }

        if (executor == null) {
            throw SQLError.createSQLException("Executor can not be null", "S1009", getExceptionInterceptor());
        }

        executor.execute(new Runnable() {
            public void run() {
                try {
                    ConnectionImpl.this.abortInternal();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public synchronized void setNetworkTimeout(Executor executor, final int milliseconds) throws SQLException {
        SecurityManager sec = System.getSecurityManager();

        if (sec != null) {
            sec.checkPermission(SET_NETWORK_TIMEOUT_PERM);
        }

        if (executor == null) {
            throw SQLError.createSQLException("Executor can not be null", "S1009", getExceptionInterceptor());
        }

        checkClosed();
        final MysqlIO mysqlIo = this.io;

        executor.execute(new Runnable() {
            public void run() {
                ConnectionImpl.this.setSocketTimeout(milliseconds);
                try {
                    mysqlIo.setSocketTimeout(milliseconds);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public synchronized int getNetworkTimeout() throws SQLException {
        checkClosed();
        return getSocketTimeout();
    }

    static class CompoundCacheKey {
        String componentOne;

        String componentTwo;

        int hashCode;

        CompoundCacheKey(String partOne, String partTwo) {
            this.componentOne = partOne;
            this.componentTwo = partTwo;

            this.hashCode = (((this.componentOne != null) ? this.componentOne : "") + this.componentTwo).hashCode();
        }

        public boolean equals(Object obj) {
            if (obj instanceof CompoundCacheKey) {
                CompoundCacheKey another = (CompoundCacheKey) obj;

                boolean firstPartEqual = false;

                if (this.componentOne == null) {
                    firstPartEqual = (another.componentOne == null);
                } else {
                    firstPartEqual = this.componentOne.equals(another.componentOne);
                }

                return (firstPartEqual && this.componentTwo.equals(another.componentTwo));
            }

            return false;
        }

        public int hashCode() {
            return this.hashCode;
        }
    }

    class ExceptionInterceptorChain
            implements ExceptionInterceptor {
        List<Extension> interceptors;

        ExceptionInterceptorChain(String interceptorClasses) throws SQLException {
            this.interceptors = Util.loadExtensions(ConnectionImpl.this, ConnectionImpl.this.props, interceptorClasses, "Connection.BadExceptionInterceptor", this);
        }

        void addRingZero(ExceptionInterceptor interceptor) throws SQLException {
            this.interceptors.add(0, interceptor);
        }

        public SQLException interceptException(SQLException sqlEx, Connection conn) {
            if (this.interceptors != null) {
                Iterator<Extension> iter = this.interceptors.iterator();

                while (iter.hasNext()) {
                    sqlEx = ((ExceptionInterceptor) iter.next()).interceptException(sqlEx, ConnectionImpl.this);
                }
            }

            return sqlEx;
        }

        public void destroy() {
            if (this.interceptors != null) {
                Iterator<Extension> iter = this.interceptors.iterator();

                while (iter.hasNext()) {
                    ((ExceptionInterceptor) iter.next()).destroy();
                }
            }
        }

        public void init(Connection conn, Properties properties) throws SQLException {
            if (this.interceptors != null) {
                Iterator<Extension> iter = this.interceptors.iterator();

                while (iter.hasNext()) {
                    ((ExceptionInterceptor) iter.next()).init(conn, properties);
                }
            }
        }
    }
}

