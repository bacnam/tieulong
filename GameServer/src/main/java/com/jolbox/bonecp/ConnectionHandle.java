package com.jolbox.bonecp;

import com.google.common.collect.ImmutableSet;
import com.jolbox.bonecp.hooks.AcquireFailConfig;
import com.jolbox.bonecp.hooks.ConnectionHook;
import com.jolbox.bonecp.hooks.ConnectionState;
import com.jolbox.bonecp.proxy.TransactionRecoveryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.Reference;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionHandle
        implements Connection {
    private static final String STATEMENT_NOT_CLOSED = "Stack trace of location where statement was opened follows:\n%s";
    private static final String LOG_ERROR_MESSAGE = "Connection closed twice exception detected.\n%s\n%s\n";
    private static final String CLOSED_TWICE_EXCEPTION_MESSAGE = "Connection closed from thread [%s] was closed again.\nStack trace of location where connection was first closed follows:\n";
    private static final ImmutableSet<String> sqlStateDBFailureCodes = ImmutableSet.of("08001", "08007", "08S01", "57P01");
    private static Logger logger = LoggerFactory.getLogger(ConnectionHandle.class);
    protected boolean possiblyBroken;
    protected boolean logicallyClosed = false;
    protected boolean statementCachingEnabled;
    protected TransactionRecoveryResult recoveryResult = new TransactionRecoveryResult();
    protected String url;
    protected Thread threadUsingConnection;
    private Connection connection = null;
    private long connectionLastUsedInMs = System.currentTimeMillis();
    private long connectionLastResetInMs = System.currentTimeMillis();
    private long connectionCreationTimeInMs = System.currentTimeMillis();
    private BoneCP pool;
    private ConnectionPartition originatingPartition = null;
    private IStatementCache preparedStatementCache = null;
    private IStatementCache callableStatementCache = null;
    private Object debugHandle;
    private ConnectionHook connectionHook;
    private boolean doubleCloseCheck;
    private volatile String doubleCloseException = null;
    private boolean logStatementsEnabled;
    private List<ReplayLog> replayLog;
    private boolean inReplayMode;
    private long maxConnectionAgeInMs;
    private boolean statisticsEnabled;
    private Statistics statistics;
    private volatile Thread threadWatch;
    private Map<Connection, Reference<ConnectionHandle>> finalizableRefs;
    private boolean connectionTrackingDisabled;

    public ConnectionHandle(String url, String username, String password, BoneCP pool) throws SQLException {
        this.pool = pool;
        this.url = url;
        this.connection = obtainInternalConnection();
        this.finalizableRefs = this.pool.getFinalizableRefs();
        this.connectionTrackingDisabled = pool.getConfig().isDisableConnectionTracking();
        this.statisticsEnabled = pool.getConfig().isStatisticsEnabled();
        this.statistics = pool.getStatistics();
        if (this.pool.getConfig().isTransactionRecoveryEnabled()) {
            this.replayLog = new ArrayList<ReplayLog>(30);

            this.connection = MemorizeTransactionProxy.memorize(this.connection, this);
        }

        this.maxConnectionAgeInMs = pool.getConfig().getMaxConnectionAge(TimeUnit.MILLISECONDS);
        this.doubleCloseCheck = pool.getConfig().isCloseConnectionWatch();
        this.logStatementsEnabled = pool.getConfig().isLogStatementsEnabled();
        int cacheSize = pool.getConfig().getStatementsCacheSize();
        if (cacheSize > 0) {
            this.preparedStatementCache = new StatementCache(cacheSize, pool.getConfig().isStatisticsEnabled(), pool.getStatistics());
            this.callableStatementCache = new StatementCache(cacheSize, pool.getConfig().isStatisticsEnabled(), pool.getStatistics());
            this.statementCachingEnabled = true;
        }
    }

    public ConnectionHandle(Connection connection, IStatementCache preparedStatementCache, IStatementCache callableStatementCache, BoneCP pool) {
        this.connection = connection;
        this.preparedStatementCache = preparedStatementCache;
        this.callableStatementCache = callableStatementCache;
        this.pool = pool;
        this.url = null;
        int cacheSize = pool.getConfig().getStatementsCacheSize();
        if (cacheSize > 0) {
            this.statementCachingEnabled = true;
        }
    }

    protected Connection obtainInternalConnection() throws SQLException {
        boolean tryAgain = false;
        Connection result = null;

        int acquireRetryAttempts = this.pool.getConfig().getAcquireRetryAttempts();
        long acquireRetryDelayInMs = this.pool.getConfig().getAcquireRetryDelayInMs();
        AcquireFailConfig acquireConfig = new AcquireFailConfig();
        acquireConfig.setAcquireRetryAttempts(new AtomicInteger(acquireRetryAttempts));
        acquireConfig.setAcquireRetryDelayInMs(acquireRetryDelayInMs);
        acquireConfig.setLogMessage("Failed to acquire connection");

        this.connectionHook = this.pool.getConfig().getConnectionHook();

        do {
            try {
                this.connection = this.pool.obtainRawInternalConnection();
                tryAgain = false;

                if (acquireRetryAttempts != this.pool.getConfig().getAcquireRetryAttempts()) {
                    logger.info("Successfully re-established connection to DB");
                }

                if (this.connectionHook != null) {
                    this.connectionHook.onAcquire(this);
                }

                sendInitSQL();
                result = this.connection;
            } catch (SQLException e) {

                if (this.connectionHook != null) {
                    tryAgain = this.connectionHook.onAcquireFail(e, acquireConfig);
                } else {
                    logger.error("Failed to acquire connection. Sleeping for " + acquireRetryDelayInMs + "ms. Attempts left: " + acquireRetryAttempts, e);

                    try {
                        Thread.sleep(acquireRetryDelayInMs);
                        if (acquireRetryAttempts > -1) {
                            tryAgain = (acquireRetryAttempts-- != 0);
                        }
                    } catch (InterruptedException e1) {
                        tryAgain = false;
                    }
                }
                if (!tryAgain) {
                    throw markPossiblyBroken(e);
                }
            }
        } while (tryAgain);

        return result;
    }

    public void sendInitSQL() throws SQLException {
        String initSQL = this.pool.getConfig().getInitSQL();
        if (initSQL != null) {
            Statement stmt = this.connection.createStatement();
            stmt.execute(initSQL);
            stmt.close();
        }
    }

    protected SQLException markPossiblyBroken(SQLException e) {
        String state = e.getSQLState();
        ConnectionState connectionState = (getConnectionHook() != null) ? getConnectionHook().onMarkPossiblyBroken(this, state, e) : ConnectionState.NOP;
        if (state == null) {
            state = "08999";
        }

        if ((sqlStateDBFailureCodes.contains(state) || connectionState.equals(ConnectionState.TERMINATE_ALL_CONNECTIONS)) && this.pool != null) {
            logger.error("Database access problem. Killing off all remaining connections in the connection pool. SQL State = " + state);
            this.pool.terminateAllConnections();
        }

        char firstChar = state.charAt(0);
        if (connectionState.equals(ConnectionState.CONNECTION_POSSIBLY_BROKEN) || state.equals("40001") || state.equals("HY000") || state.startsWith("08") || (firstChar >= '5' && firstChar <= '9')) {

            this.possiblyBroken = true;
        }

        if (this.possiblyBroken && getConnectionHook() != null) {
            this.possiblyBroken = getConnectionHook().onConnectionException(this, state, e);
        }

        return e;
    }

    public void clearWarnings() throws SQLException {
        checkClosed();
        try {
            this.connection.clearWarnings();
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
    }

    private void checkClosed() throws SQLException {
        if (this.logicallyClosed) {
            throw new SQLException("Connection is closed!");
        }
    }

    public void close() throws SQLException {
        try {
            if (!this.logicallyClosed) {
                this.logicallyClosed = true;
                this.threadUsingConnection = null;
                if (this.threadWatch != null) {
                    this.threadWatch.interrupt();

                    this.threadWatch = null;
                }
                this.pool.releaseConnection(this);

                if (this.doubleCloseCheck) {
                    this.doubleCloseException = this.pool.captureStackTrace("Connection closed from thread [%s] was closed again.\nStack trace of location where connection was first closed follows:\n");
                }
            } else if (this.doubleCloseCheck && this.doubleCloseException != null) {
                String currentLocation = this.pool.captureStackTrace("Last closed trace from thread [" + Thread.currentThread().getName() + "]:\n");
                logger.error(String.format("Connection closed twice exception detected.\n%s\n%s\n", new Object[]{this.doubleCloseException, currentLocation}));
            }

        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
    }

    protected void internalClose() throws SQLException {
        try {
            clearStatementCaches(true);
            if (this.connection != null) {
                this.connection.close();

                if (!this.connectionTrackingDisabled && this.finalizableRefs != null) {
                    this.finalizableRefs.remove(this.connection);
                }
            }
            this.logicallyClosed = true;
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
    }

    public void commit() throws SQLException {
        checkClosed();
        try {
            this.connection.commit();
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
    }

    public Properties getClientInfo() throws SQLException {
        Properties result = null;
        checkClosed();
        try {
            result = this.connection.getClientInfo();
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
        return result;
    }

    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        this.connection.setClientInfo(properties);
    }

    public String getClientInfo(String name) throws SQLException {
        String result = null;
        checkClosed();
        try {
            result = this.connection.getClientInfo(name);
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
        return result;
    }

    public boolean isValid(int timeout) throws SQLException {
        boolean result = false;
        checkClosed();
        try {
            result = this.connection.isValid(timeout);
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
        return result;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return this.connection.isWrapperFor(iface);
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return this.connection.unwrap(iface);
    }

    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        this.connection.setClientInfo(name, value);
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        Struct result = null;
        checkClosed();
        try {
            result = this.connection.createStruct(typeName, attributes);
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
        return result;
    }

    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        Array result = null;
        checkClosed();
        try {
            result = this.connection.createArrayOf(typeName, elements);
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }

        return result;
    }

    public Blob createBlob() throws SQLException {
        Blob result = null;
        checkClosed();
        try {
            result = this.connection.createBlob();
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
        return result;
    }

    public Clob createClob() throws SQLException {
        Clob result = null;
        checkClosed();
        try {
            result = this.connection.createClob();
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }

        return result;
    }

    public NClob createNClob() throws SQLException {
        NClob result = null;
        checkClosed();
        try {
            result = this.connection.createNClob();
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
        return result;
    }

    public SQLXML createSQLXML() throws SQLException {
        SQLXML result = null;
        checkClosed();
        try {
            result = this.connection.createSQLXML();
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
        return result;
    }

    public Statement createStatement() throws SQLException {
        Statement result = null;
        checkClosed();
        try {
            result = new StatementHandle(this.connection.createStatement(), this, this.logStatementsEnabled);
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
        return result;
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        Statement result = null;
        checkClosed();
        try {
            result = new StatementHandle(this.connection.createStatement(resultSetType, resultSetConcurrency), this, this.logStatementsEnabled);
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
        return result;
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        Statement result = null;
        checkClosed();
        try {
            result = new StatementHandle(this.connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability), this, this.logStatementsEnabled);
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }

        return result;
    }

    public boolean getAutoCommit() throws SQLException {
        boolean result = false;
        checkClosed();
        try {
            result = this.connection.getAutoCommit();
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
        return result;
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        checkClosed();
        try {
            this.connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
    }

    public String getCatalog() throws SQLException {
        String result = null;
        checkClosed();
        try {
            result = this.connection.getCatalog();
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
        return result;
    }

    public void setCatalog(String catalog) throws SQLException {
        checkClosed();
        try {
            this.connection.setCatalog(catalog);
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
    }

    public int getHoldability() throws SQLException {
        int result = 0;
        checkClosed();
        try {
            result = this.connection.getHoldability();
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }

        return result;
    }

    public void setHoldability(int holdability) throws SQLException {
        checkClosed();
        try {
            this.connection.setHoldability(holdability);
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        DatabaseMetaData result = null;
        checkClosed();
        try {
            result = this.connection.getMetaData();
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
        return result;
    }

    public int getTransactionIsolation() throws SQLException {
        int result = 0;
        checkClosed();
        try {
            result = this.connection.getTransactionIsolation();
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
        return result;
    }

    public void setTransactionIsolation(int level) throws SQLException {
        checkClosed();
        try {
            this.connection.setTransactionIsolation(level);
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException {
        Map<String, Class<?>> result = null;
        checkClosed();
        try {
            result = this.connection.getTypeMap();
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
        return result;
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        checkClosed();
        try {
            this.connection.setTypeMap(map);
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
    }

    public SQLWarning getWarnings() throws SQLException {
        SQLWarning result = null;
        checkClosed();
        try {
            result = this.connection.getWarnings();
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
        return result;
    }

    public boolean isClosed() {
        return this.logicallyClosed;
    }

    public boolean isReadOnly() throws SQLException {
        boolean result = false;
        checkClosed();
        try {
            result = this.connection.isReadOnly();
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
        return result;
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        checkClosed();
        try {
            this.connection.setReadOnly(readOnly);
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
    }

    public String nativeSQL(String sql) throws SQLException {
        String result = null;
        checkClosed();
        try {
            result = this.connection.nativeSQL(sql);
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
        return result;
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        StatementHandle result = null;
        String cacheKey = null;

        checkClosed();

        try {
            long statStart = 0L;
            if (this.statisticsEnabled) {
                statStart = System.nanoTime();
            }
            if (this.statementCachingEnabled) {
                cacheKey = sql;
                result = this.callableStatementCache.get(cacheKey);
            }

            if (result == null) {
                result = new CallableStatementHandle(this.connection.prepareCall(sql), sql, this, cacheKey, this.callableStatementCache);

                result.setLogicallyOpen();
            }

            if (this.pool.closeConnectionWatch && this.statementCachingEnabled) {
                result.setOpenStackTrace(this.pool.captureStackTrace("Stack trace of location where statement was opened follows:\n%s"));
            }
            if (this.statisticsEnabled) {
                this.statistics.addStatementPrepareTime(System.nanoTime() - statStart);
                this.statistics.incrementStatementsPrepared();
            }
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }

        return (CallableStatement) result;
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        StatementHandle result = null;
        String cacheKey = null;

        checkClosed();

        try {
            long statStart = 0L;
            if (this.statisticsEnabled) {
                statStart = System.nanoTime();
            }
            if (this.statementCachingEnabled) {
                cacheKey = this.callableStatementCache.calculateCacheKey(sql, resultSetType, resultSetConcurrency);
                result = this.callableStatementCache.get(cacheKey);
            }

            if (result == null) {
                result = new CallableStatementHandle(this.connection.prepareCall(sql, resultSetType, resultSetConcurrency), sql, this, cacheKey, this.callableStatementCache);

                result.setLogicallyOpen();
            }

            if (this.pool.closeConnectionWatch && this.statementCachingEnabled) {
                result.setOpenStackTrace(this.pool.captureStackTrace("Stack trace of location where statement was opened follows:\n%s"));
            }
            if (this.statisticsEnabled) {
                this.statistics.addStatementPrepareTime(System.nanoTime() - statStart);
                this.statistics.incrementStatementsPrepared();
            }
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }

        return (CallableStatement) result;
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        StatementHandle result = null;
        String cacheKey = null;

        checkClosed();

        try {
            long statStart = 0L;
            if (this.statisticsEnabled) {
                statStart = System.nanoTime();
            }
            if (this.statementCachingEnabled) {
                cacheKey = this.callableStatementCache.calculateCacheKey(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
                result = this.callableStatementCache.get(cacheKey);
            }

            if (result == null) {
                result = new CallableStatementHandle(this.connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability), sql, this, cacheKey, this.callableStatementCache);

                result.setLogicallyOpen();
            }

            if (this.pool.closeConnectionWatch && this.statementCachingEnabled) {
                result.setOpenStackTrace(this.pool.captureStackTrace("Stack trace of location where statement was opened follows:\n%s"));
            }
            if (this.statisticsEnabled) {
                this.statistics.addStatementPrepareTime(System.nanoTime() - statStart);
                this.statistics.incrementStatementsPrepared();
            }
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }

        return (CallableStatement) result;
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        StatementHandle result = null;
        String cacheKey = null;

        checkClosed();

        try {
            long statStart = 0L;
            if (this.statisticsEnabled) {
                statStart = System.nanoTime();
            }
            if (this.statementCachingEnabled) {
                cacheKey = sql;
                result = this.preparedStatementCache.get(cacheKey);
            }

            if (result == null) {
                result = new PreparedStatementHandle(this.connection.prepareStatement(sql), sql, this, cacheKey, this.preparedStatementCache);
                result.setLogicallyOpen();
            }

            if (this.pool.closeConnectionWatch && this.statementCachingEnabled) {
                result.setOpenStackTrace(this.pool.captureStackTrace("Stack trace of location where statement was opened follows:\n%s"));
            }

            if (this.statisticsEnabled) {
                this.statistics.addStatementPrepareTime(System.nanoTime() - statStart);
                this.statistics.incrementStatementsPrepared();
            }

        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
        return (PreparedStatement) result;
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        StatementHandle result = null;
        String cacheKey = null;

        checkClosed();

        try {
            long statStart = 0L;
            if (this.statisticsEnabled) {
                statStart = System.nanoTime();
            }
            if (this.statementCachingEnabled) {
                cacheKey = this.preparedStatementCache.calculateCacheKey(sql, autoGeneratedKeys);
                result = this.preparedStatementCache.get(cacheKey);
            }

            if (result == null) {
                result = new PreparedStatementHandle(this.connection.prepareStatement(sql, autoGeneratedKeys), sql, this, cacheKey, this.preparedStatementCache);
                result.setLogicallyOpen();
            }

            if (this.pool.closeConnectionWatch && this.statementCachingEnabled) {
                result.setOpenStackTrace(this.pool.captureStackTrace("Stack trace of location where statement was opened follows:\n%s"));
            }

            if (this.statisticsEnabled) {
                this.statistics.addStatementPrepareTime(System.nanoTime() - statStart);
                this.statistics.incrementStatementsPrepared();
            }

        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
        return (PreparedStatement) result;
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        StatementHandle result = null;
        String cacheKey = null;

        checkClosed();

        try {
            long statStart = 0L;
            if (this.statisticsEnabled) {
                statStart = System.nanoTime();
            }

            if (this.statementCachingEnabled) {
                cacheKey = this.preparedStatementCache.calculateCacheKey(sql, columnIndexes);
                result = this.preparedStatementCache.get(cacheKey);
            }

            if (result == null) {
                result = new PreparedStatementHandle(this.connection.prepareStatement(sql, columnIndexes), sql, this, cacheKey, this.preparedStatementCache);

                result.setLogicallyOpen();
            }

            if (this.pool.closeConnectionWatch && this.statementCachingEnabled) {
                result.setOpenStackTrace(this.pool.captureStackTrace("Stack trace of location where statement was opened follows:\n%s"));
            }

            if (this.statisticsEnabled) {
                this.statistics.addStatementPrepareTime(System.nanoTime() - statStart);
                this.statistics.incrementStatementsPrepared();
            }

        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }

        return (PreparedStatement) result;
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        StatementHandle result = null;
        String cacheKey = null;

        checkClosed();

        try {
            long statStart = 0L;
            if (this.statisticsEnabled) {
                statStart = System.nanoTime();
            }
            if (this.statementCachingEnabled) {
                cacheKey = this.preparedStatementCache.calculateCacheKey(sql, columnNames);
                result = this.preparedStatementCache.get(cacheKey);
            }

            if (result == null) {
                result = new PreparedStatementHandle(this.connection.prepareStatement(sql, columnNames), sql, this, cacheKey, this.preparedStatementCache);

                result.setLogicallyOpen();
            }

            if (this.pool.closeConnectionWatch && this.statementCachingEnabled) {
                result.setOpenStackTrace(this.pool.captureStackTrace("Stack trace of location where statement was opened follows:\n%s"));
            }

            if (this.statisticsEnabled) {
                this.statistics.addStatementPrepareTime(System.nanoTime() - statStart);
                this.statistics.incrementStatementsPrepared();
            }
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }

        return (PreparedStatement) result;
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        StatementHandle result = null;
        String cacheKey = null;

        checkClosed();

        try {
            long statStart = 0L;
            if (this.statisticsEnabled) {
                statStart = System.nanoTime();
            }
            if (this.statementCachingEnabled) {
                cacheKey = this.preparedStatementCache.calculateCacheKey(sql, resultSetType, resultSetConcurrency);
                result = this.preparedStatementCache.get(cacheKey);
            }

            if (result == null) {
                result = new PreparedStatementHandle(this.connection.prepareStatement(sql, resultSetType, resultSetConcurrency), sql, this, cacheKey, this.preparedStatementCache);

                result.setLogicallyOpen();
            }

            if (this.pool.closeConnectionWatch && this.statementCachingEnabled) {
                result.setOpenStackTrace(this.pool.captureStackTrace("Stack trace of location where statement was opened follows:\n%s"));
            }
            if (this.statisticsEnabled) {
                this.statistics.addStatementPrepareTime(System.nanoTime() - statStart);
                this.statistics.incrementStatementsPrepared();
            }
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }

        return (PreparedStatement) result;
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        StatementHandle result = null;
        String cacheKey = null;

        checkClosed();

        try {
            long statStart = 0L;
            if (this.statisticsEnabled) {
                statStart = System.nanoTime();
            }

            if (this.statementCachingEnabled) {
                cacheKey = this.preparedStatementCache.calculateCacheKey(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
                result = this.preparedStatementCache.get(cacheKey);
            }

            if (result == null) {
                result = new PreparedStatementHandle(this.connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability), sql, this, cacheKey, this.preparedStatementCache);

                result.setLogicallyOpen();
            }

            if (this.pool.closeConnectionWatch && this.statementCachingEnabled) {
                result.setOpenStackTrace(this.pool.captureStackTrace("Stack trace of location where statement was opened follows:\n%s"));
            }
            if (this.statisticsEnabled) {
                this.statistics.addStatementPrepareTime(System.nanoTime() - statStart);
                this.statistics.incrementStatementsPrepared();
            }
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }

        return (PreparedStatement) result;
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        checkClosed();
        try {
            this.connection.releaseSavepoint(savepoint);
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
    }

    public void rollback() throws SQLException {
        checkClosed();
        try {
            this.connection.rollback();
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        checkClosed();
        try {
            this.connection.rollback(savepoint);
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
    }

    public Savepoint setSavepoint() throws SQLException {
        checkClosed();
        Savepoint result = null;
        try {
            result = this.connection.setSavepoint();
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
        return result;
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        checkClosed();
        Savepoint result = null;
        try {
            result = this.connection.setSavepoint(name);
        } catch (SQLException e) {
            throw markPossiblyBroken(e);
        }
        return result;
    }

    public long getConnectionLastUsedInMs() {
        return this.connectionLastUsedInMs;
    }

    protected void setConnectionLastUsedInMs(long connectionLastUsed) {
        this.connectionLastUsedInMs = connectionLastUsed;
    }

    @Deprecated
    public long getConnectionLastUsed() {
        return getConnectionLastUsedInMs();
    }

    public long getConnectionLastResetInMs() {
        return this.connectionLastResetInMs;
    }

    protected void setConnectionLastResetInMs(long connectionLastReset) {
        this.connectionLastResetInMs = connectionLastReset;
    }

    @Deprecated
    public long getConnectionLastReset() {
        return getConnectionLastResetInMs();
    }

    public boolean isPossiblyBroken() {
        return this.possiblyBroken;
    }

    public ConnectionPartition getOriginatingPartition() {
        return this.originatingPartition;
    }

    protected void setOriginatingPartition(ConnectionPartition originatingPartition) {
        this.originatingPartition = originatingPartition;
    }

    protected void renewConnection() {
        this.logicallyClosed = false;
        this.threadUsingConnection = Thread.currentThread();
        if (this.doubleCloseCheck) {
            this.doubleCloseException = null;
        }
    }

    protected void clearStatementCaches(boolean internalClose) {
        if (this.statementCachingEnabled) {
            if (internalClose) {
                this.callableStatementCache.clear();
                this.preparedStatementCache.clear();
            } else if (this.pool.closeConnectionWatch) {
                this.callableStatementCache.checkForProperClosure();
                this.preparedStatementCache.checkForProperClosure();
            }
        }
    }

    public Object getDebugHandle() {
        return this.debugHandle;
    }

    public void setDebugHandle(Object debugHandle) {
        this.debugHandle = debugHandle;
    }

    @Deprecated
    public Connection getRawConnection() {
        return getInternalConnection();
    }

    public Connection getInternalConnection() {
        return this.connection;
    }

    public void setInternalConnection(Connection rawConnection) {
        this.connection = rawConnection;
    }

    public ConnectionHook getConnectionHook() {
        return this.connectionHook;
    }

    public boolean isLogStatementsEnabled() {
        return this.logStatementsEnabled;
    }

    public void setLogStatementsEnabled(boolean logStatementsEnabled) {
        this.logStatementsEnabled = logStatementsEnabled;
    }

    protected boolean isInReplayMode() {
        return this.inReplayMode;
    }

    protected void setInReplayMode(boolean inReplayMode) {
        this.inReplayMode = inReplayMode;
    }

    public boolean isConnectionAlive() {
        return this.pool.isConnectionHandleAlive(this);
    }

    public BoneCP getPool() {
        return this.pool;
    }

    public List<ReplayLog> getReplayLog() {
        return this.replayLog;
    }

    protected void setReplayLog(List<ReplayLog> replayLog) {
        this.replayLog = replayLog;
    }

    public Object getProxyTarget() {
        try {
            return Proxy.getInvocationHandler(this.connection).invoke(null, getClass().getMethod("getProxyTarget", new Class[0]), null);
        } catch (Throwable t) {
            throw new RuntimeException("BoneCP: Internal error - transaction replay log is not turned on?", t);
        }
    }

    public Thread getThreadUsingConnection() {
        return this.threadUsingConnection;
    }

    @Deprecated
    public long getConnectionCreationTime() {
        return getConnectionCreationTimeInMs();
    }

    public long getConnectionCreationTimeInMs() {
        return this.connectionCreationTimeInMs;
    }

    public boolean isExpired() {
        return isExpired(System.currentTimeMillis());
    }

    protected boolean isExpired(long currentTime) {
        return (this.maxConnectionAgeInMs > 0L && currentTime - this.connectionCreationTimeInMs > this.maxConnectionAgeInMs);
    }

    public Thread getThreadWatch() {
        return this.threadWatch;
    }

    protected void setThreadWatch(Thread threadWatch) {
        this.threadWatch = threadWatch;
    }
}

