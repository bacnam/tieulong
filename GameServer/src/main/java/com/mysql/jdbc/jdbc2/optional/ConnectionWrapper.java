package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.*;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.log.Log;

import java.lang.reflect.Constructor;
import java.sql.*;
import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.Executor;

public class ConnectionWrapper
        extends WrapperBase
        implements Connection {
    private static final Constructor<?> JDBC_4_CONNECTION_WRAPPER_CTOR;

    static {
        if (Util.isJdbc4()) {
            try {
                JDBC_4_CONNECTION_WRAPPER_CTOR = Class.forName("com.mysql.jdbc.jdbc2.optional.JDBC4ConnectionWrapper").getConstructor(new Class[]{MysqlPooledConnection.class, Connection.class, boolean.class});

            } catch (SecurityException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            JDBC_4_CONNECTION_WRAPPER_CTOR = null;
        }
    }

    protected Connection mc = null;
    private String invalidHandleStr = "Logical handle no longer valid";
    private boolean closed;
    private boolean isForXa;

    public ConnectionWrapper(MysqlPooledConnection mysqlPooledConnection, Connection mysqlConnection, boolean forXa) throws SQLException {
        super(mysqlPooledConnection);

        this.mc = mysqlConnection;
        this.closed = false;
        this.isForXa = forXa;

        if (this.isForXa) {
            setInGlobalTx(false);
        }
    }

    protected static ConnectionWrapper getInstance(MysqlPooledConnection mysqlPooledConnection, Connection mysqlConnection, boolean forXa) throws SQLException {
        if (!Util.isJdbc4()) {
            return new ConnectionWrapper(mysqlPooledConnection, mysqlConnection, forXa);
        }

        return (ConnectionWrapper) Util.handleNewInstance(JDBC_4_CONNECTION_WRAPPER_CTOR, new Object[]{mysqlPooledConnection, mysqlConnection, Boolean.valueOf(forXa)}, mysqlPooledConnection.getExceptionInterceptor());
    }

    public boolean getAutoCommit() throws SQLException {
        checkClosed();

        try {
            return this.mc.getAutoCommit();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return false;
        }
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        checkClosed();

        if (autoCommit && isInGlobalTx()) {
            throw SQLError.createSQLException("Can't set autocommit to 'true' on an XAConnection", "2D000", 1401, this.exceptionInterceptor);
        }

        try {
            this.mc.setAutoCommit(autoCommit);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    public String getCatalog() throws SQLException {
        checkClosed();

        try {
            return this.mc.getCatalog();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public void setCatalog(String catalog) throws SQLException {
        checkClosed();

        try {
            this.mc.setCatalog(catalog);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    public boolean isClosed() throws SQLException {
        return (this.closed || this.mc.isClosed());
    }

    public boolean isMasterConnection() {
        return this.mc.isMasterConnection();
    }

    public int getHoldability() throws SQLException {
        checkClosed();

        try {
            return this.mc.getHoldability();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return 1;
        }
    }

    public void setHoldability(int arg0) throws SQLException {
        checkClosed();

        try {
            this.mc.setHoldability(arg0);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    public long getIdleFor() {
        return this.mc.getIdleFor();
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        checkClosed();

        try {
            return this.mc.getMetaData();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public boolean isReadOnly() throws SQLException {
        checkClosed();

        try {
            return this.mc.isReadOnly();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return false;
        }
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        checkClosed();

        try {
            this.mc.setReadOnly(readOnly);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    public Savepoint setSavepoint() throws SQLException {
        checkClosed();

        if (isInGlobalTx()) {
            throw SQLError.createSQLException("Can't set autocommit to 'true' on an XAConnection", "2D000", 1401, this.exceptionInterceptor);
        }

        try {
            return this.mc.setSavepoint();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public Savepoint setSavepoint(String arg0) throws SQLException {
        checkClosed();

        if (isInGlobalTx()) {
            throw SQLError.createSQLException("Can't set autocommit to 'true' on an XAConnection", "2D000", 1401, this.exceptionInterceptor);
        }

        try {
            return this.mc.setSavepoint(arg0);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public int getTransactionIsolation() throws SQLException {
        checkClosed();

        try {
            return this.mc.getTransactionIsolation();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return 4;
        }
    }

    public void setTransactionIsolation(int level) throws SQLException {
        checkClosed();

        try {
            this.mc.setTransactionIsolation(level);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException {
        checkClosed();

        try {
            return this.mc.getTypeMap();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        checkClosed();

        try {
            this.mc.setTypeMap(map);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    public SQLWarning getWarnings() throws SQLException {
        checkClosed();

        try {
            return this.mc.getWarnings();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public void clearWarnings() throws SQLException {
        checkClosed();

        try {
            this.mc.clearWarnings();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    public void close() throws SQLException {
        close(true);
    }

    public void commit() throws SQLException {
        checkClosed();

        if (isInGlobalTx()) {
            throw SQLError.createSQLException("Can't call commit() on an XAConnection associated with a global transaction", "2D000", 1401, this.exceptionInterceptor);
        }

        try {
            this.mc.commit();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    public Statement createStatement() throws SQLException {
        checkClosed();

        try {
            return StatementWrapper.getInstance(this, this.pooledConnection, this.mc.createStatement());
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        checkClosed();

        try {
            return StatementWrapper.getInstance(this, this.pooledConnection, this.mc.createStatement(resultSetType, resultSetConcurrency));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public Statement createStatement(int arg0, int arg1, int arg2) throws SQLException {
        checkClosed();

        try {
            return StatementWrapper.getInstance(this, this.pooledConnection, this.mc.createStatement(arg0, arg1, arg2));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public String nativeSQL(String sql) throws SQLException {
        checkClosed();

        try {
            return this.mc.nativeSQL(sql);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        checkClosed();

        try {
            return CallableStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareCall(sql));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        checkClosed();

        try {
            return CallableStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareCall(sql, resultSetType, resultSetConcurrency));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public CallableStatement prepareCall(String arg0, int arg1, int arg2, int arg3) throws SQLException {
        checkClosed();

        try {
            return CallableStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareCall(arg0, arg1, arg2, arg3));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public PreparedStatement clientPrepare(String sql) throws SQLException {
        checkClosed();

        try {
            return new PreparedStatementWrapper(this, this.pooledConnection, this.mc.clientPrepareStatement(sql));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public PreparedStatement clientPrepare(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        checkClosed();

        try {
            return new PreparedStatementWrapper(this, this.pooledConnection, this.mc.clientPrepareStatement(sql, resultSetType, resultSetConcurrency));

        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        checkClosed();

        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareStatement(sql));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        checkClosed();

        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareStatement(sql, resultSetType, resultSetConcurrency));

        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public PreparedStatement prepareStatement(String arg0, int arg1, int arg2, int arg3) throws SQLException {
        checkClosed();

        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareStatement(arg0, arg1, arg2, arg3));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public PreparedStatement prepareStatement(String arg0, int arg1) throws SQLException {
        checkClosed();

        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareStatement(arg0, arg1));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public PreparedStatement prepareStatement(String arg0, int[] arg1) throws SQLException {
        checkClosed();

        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareStatement(arg0, arg1));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public PreparedStatement prepareStatement(String arg0, String[] arg1) throws SQLException {
        checkClosed();

        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareStatement(arg0, arg1));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public void releaseSavepoint(Savepoint arg0) throws SQLException {
        checkClosed();

        try {
            this.mc.releaseSavepoint(arg0);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    public void rollback() throws SQLException {
        checkClosed();

        if (isInGlobalTx()) {
            throw SQLError.createSQLException("Can't call rollback() on an XAConnection associated with a global transaction", "2D000", 1401, this.exceptionInterceptor);
        }

        try {
            this.mc.rollback();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    public void rollback(Savepoint arg0) throws SQLException {
        checkClosed();

        if (isInGlobalTx()) {
            throw SQLError.createSQLException("Can't call rollback() on an XAConnection associated with a global transaction", "2D000", 1401, this.exceptionInterceptor);
        }

        try {
            this.mc.rollback(arg0);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    public boolean isSameResource(Connection c) {
        if (c instanceof ConnectionWrapper) {
            return this.mc.isSameResource(((ConnectionWrapper) c).mc);
        }
        return this.mc.isSameResource(c);
    }

    protected void close(boolean fireClosedEvent) throws SQLException {
        synchronized (this.pooledConnection) {
            if (this.closed) {
                return;
            }

            if (!isInGlobalTx() && this.mc.getRollbackOnPooledClose() && !getAutoCommit()) {
                rollback();
            }

            if (fireClosedEvent) {
                this.pooledConnection.callConnectionEventListeners(2, null);
            }

            this.closed = true;
        }
    }

    protected void checkClosed() throws SQLException {
        if (this.closed) {
            throw SQLError.createSQLException(this.invalidHandleStr, this.exceptionInterceptor);
        }
    }

    public boolean isInGlobalTx() {
        return this.mc.isInGlobalTx();
    }

    public void setInGlobalTx(boolean flag) {
        this.mc.setInGlobalTx(flag);
    }

    public void ping() throws SQLException {
        if (this.mc != null) {
            this.mc.ping();
        }
    }

    public void changeUser(String userName, String newPassword) throws SQLException {
        checkClosed();

        try {
            this.mc.changeUser(userName, newPassword);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    public void clearHasTriedMaster() {
        this.mc.clearHasTriedMaster();
    }

    public PreparedStatement clientPrepareStatement(String sql) throws SQLException {
        checkClosed();

        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.clientPrepareStatement(sql));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public PreparedStatement clientPrepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.clientPrepareStatement(sql, autoGenKeyIndex));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.clientPrepareStatement(sql, resultSetType, resultSetConcurrency));

        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.clientPrepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability));

        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public PreparedStatement clientPrepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.clientPrepareStatement(sql, autoGenKeyIndexes));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public PreparedStatement clientPrepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.clientPrepareStatement(sql, autoGenKeyColNames));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public int getActiveStatementCount() {
        return this.mc.getActiveStatementCount();
    }

    public Log getLog() throws SQLException {
        return this.mc.getLog();
    }

    public String getServerCharacterEncoding() {
        return this.mc.getServerCharacterEncoding();
    }

    public TimeZone getServerTimezoneTZ() {
        return this.mc.getServerTimezoneTZ();
    }

    public String getStatementComment() {
        return this.mc.getStatementComment();
    }

    public void setStatementComment(String comment) {
        this.mc.setStatementComment(comment);
    }

    public boolean hasTriedMaster() {
        return this.mc.hasTriedMaster();
    }

    public boolean isAbonormallyLongQuery(long millisOrNanos) {
        return this.mc.isAbonormallyLongQuery(millisOrNanos);
    }

    public boolean isNoBackslashEscapesSet() {
        return this.mc.isNoBackslashEscapesSet();
    }

    public boolean lowerCaseTableNames() {
        return this.mc.lowerCaseTableNames();
    }

    public boolean parserKnowsUnicode() {
        return this.mc.parserKnowsUnicode();
    }

    public void reportQueryTime(long millisOrNanos) {
        this.mc.reportQueryTime(millisOrNanos);
    }

    public void resetServerState() throws SQLException {
        checkClosed();

        try {
            this.mc.resetServerState();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    public PreparedStatement serverPrepareStatement(String sql) throws SQLException {
        checkClosed();

        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.serverPrepareStatement(sql));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public PreparedStatement serverPrepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.serverPrepareStatement(sql, autoGenKeyIndex));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.serverPrepareStatement(sql, resultSetType, resultSetConcurrency));

        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.serverPrepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability));

        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public PreparedStatement serverPrepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.serverPrepareStatement(sql, autoGenKeyIndexes));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public PreparedStatement serverPrepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.serverPrepareStatement(sql, autoGenKeyColNames));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public void setFailedOver(boolean flag) {
        this.mc.setFailedOver(flag);
    }

    public void setPreferSlaveDuringFailover(boolean flag) {
        this.mc.setPreferSlaveDuringFailover(flag);
    }

    public void shutdownServer() throws SQLException {
        checkClosed();

        try {
            this.mc.shutdownServer();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    public boolean supportsIsolationLevel() {
        return this.mc.supportsIsolationLevel();
    }

    public boolean supportsQuotedIdentifiers() {
        return this.mc.supportsQuotedIdentifiers();
    }

    public boolean supportsTransactions() {
        return this.mc.supportsTransactions();
    }

    public boolean versionMeetsMinimum(int major, int minor, int subminor) throws SQLException {
        checkClosed();

        try {
            return this.mc.versionMeetsMinimum(major, minor, subminor);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return false;
        }
    }

    public String exposeAsXml() throws SQLException {
        checkClosed();

        try {
            return this.mc.exposeAsXml();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);

            return null;
        }
    }

    public boolean getAllowLoadLocalInfile() {
        return this.mc.getAllowLoadLocalInfile();
    }

    public void setAllowLoadLocalInfile(boolean property) {
        this.mc.setAllowLoadLocalInfile(property);
    }

    public boolean getAllowMultiQueries() {
        return this.mc.getAllowMultiQueries();
    }

    public void setAllowMultiQueries(boolean property) {
        this.mc.setAllowMultiQueries(property);
    }

    public boolean getAllowNanAndInf() {
        return this.mc.getAllowNanAndInf();
    }

    public void setAllowNanAndInf(boolean flag) {
        this.mc.setAllowNanAndInf(flag);
    }

    public boolean getAllowUrlInLocalInfile() {
        return this.mc.getAllowUrlInLocalInfile();
    }

    public void setAllowUrlInLocalInfile(boolean flag) {
        this.mc.setAllowUrlInLocalInfile(flag);
    }

    public boolean getAlwaysSendSetIsolation() {
        return this.mc.getAlwaysSendSetIsolation();
    }

    public void setAlwaysSendSetIsolation(boolean flag) {
        this.mc.setAlwaysSendSetIsolation(flag);
    }

    public boolean getAutoClosePStmtStreams() {
        return this.mc.getAutoClosePStmtStreams();
    }

    public void setAutoClosePStmtStreams(boolean flag) {
        this.mc.setAutoClosePStmtStreams(flag);
    }

    public boolean getAutoDeserialize() {
        return this.mc.getAutoDeserialize();
    }

    public void setAutoDeserialize(boolean flag) {
        this.mc.setAutoDeserialize(flag);
    }

    public boolean getAutoGenerateTestcaseScript() {
        return this.mc.getAutoGenerateTestcaseScript();
    }

    public void setAutoGenerateTestcaseScript(boolean flag) {
        this.mc.setAutoGenerateTestcaseScript(flag);
    }

    public boolean getAutoReconnectForPools() {
        return this.mc.getAutoReconnectForPools();
    }

    public void setAutoReconnectForPools(boolean flag) {
        this.mc.setAutoReconnectForPools(flag);
    }

    public boolean getAutoSlowLog() {
        return this.mc.getAutoSlowLog();
    }

    public void setAutoSlowLog(boolean flag) {
        this.mc.setAutoSlowLog(flag);
    }

    public int getBlobSendChunkSize() {
        return this.mc.getBlobSendChunkSize();
    }

    public void setBlobSendChunkSize(String value) throws SQLException {
        this.mc.setBlobSendChunkSize(value);
    }

    public boolean getBlobsAreStrings() {
        return this.mc.getBlobsAreStrings();
    }

    public void setBlobsAreStrings(boolean flag) {
        this.mc.setBlobsAreStrings(flag);
    }

    public boolean getCacheCallableStatements() {
        return this.mc.getCacheCallableStatements();
    }

    public void setCacheCallableStatements(boolean flag) {
        this.mc.setCacheCallableStatements(flag);
    }

    public boolean getCacheCallableStmts() {
        return this.mc.getCacheCallableStmts();
    }

    public void setCacheCallableStmts(boolean flag) {
        this.mc.setCacheCallableStmts(flag);
    }

    public boolean getCachePrepStmts() {
        return this.mc.getCachePrepStmts();
    }

    public void setCachePrepStmts(boolean flag) {
        this.mc.setCachePrepStmts(flag);
    }

    public boolean getCachePreparedStatements() {
        return this.mc.getCachePreparedStatements();
    }

    public void setCachePreparedStatements(boolean flag) {
        this.mc.setCachePreparedStatements(flag);
    }

    public boolean getCacheResultSetMetadata() {
        return this.mc.getCacheResultSetMetadata();
    }

    public void setCacheResultSetMetadata(boolean property) {
        this.mc.setCacheResultSetMetadata(property);
    }

    public boolean getCacheServerConfiguration() {
        return this.mc.getCacheServerConfiguration();
    }

    public void setCacheServerConfiguration(boolean flag) {
        this.mc.setCacheServerConfiguration(flag);
    }

    public int getCallableStatementCacheSize() {
        return this.mc.getCallableStatementCacheSize();
    }

    public void setCallableStatementCacheSize(int size) {
        this.mc.setCallableStatementCacheSize(size);
    }

    public int getCallableStmtCacheSize() {
        return this.mc.getCallableStmtCacheSize();
    }

    public void setCallableStmtCacheSize(int cacheSize) {
        this.mc.setCallableStmtCacheSize(cacheSize);
    }

    public boolean getCapitalizeTypeNames() {
        return this.mc.getCapitalizeTypeNames();
    }

    public void setCapitalizeTypeNames(boolean flag) {
        this.mc.setCapitalizeTypeNames(flag);
    }

    public String getCharacterSetResults() {
        return this.mc.getCharacterSetResults();
    }

    public void setCharacterSetResults(String characterSet) {
        this.mc.setCharacterSetResults(characterSet);
    }

    public String getClientCertificateKeyStorePassword() {
        return this.mc.getClientCertificateKeyStorePassword();
    }

    public void setClientCertificateKeyStorePassword(String value) {
        this.mc.setClientCertificateKeyStorePassword(value);
    }

    public String getClientCertificateKeyStoreType() {
        return this.mc.getClientCertificateKeyStoreType();
    }

    public void setClientCertificateKeyStoreType(String value) {
        this.mc.setClientCertificateKeyStoreType(value);
    }

    public String getClientCertificateKeyStoreUrl() {
        return this.mc.getClientCertificateKeyStoreUrl();
    }

    public void setClientCertificateKeyStoreUrl(String value) {
        this.mc.setClientCertificateKeyStoreUrl(value);
    }

    public String getClientInfoProvider() {
        return this.mc.getClientInfoProvider();
    }

    public void setClientInfoProvider(String classname) {
        this.mc.setClientInfoProvider(classname);
    }

    public String getClobCharacterEncoding() {
        return this.mc.getClobCharacterEncoding();
    }

    public void setClobCharacterEncoding(String encoding) {
        this.mc.setClobCharacterEncoding(encoding);
    }

    public boolean getClobberStreamingResults() {
        return this.mc.getClobberStreamingResults();
    }

    public void setClobberStreamingResults(boolean flag) {
        this.mc.setClobberStreamingResults(flag);
    }

    public int getConnectTimeout() {
        return this.mc.getConnectTimeout();
    }

    public void setConnectTimeout(int timeoutMs) {
        this.mc.setConnectTimeout(timeoutMs);
    }

    public String getConnectionCollation() {
        return this.mc.getConnectionCollation();
    }

    public void setConnectionCollation(String collation) {
        this.mc.setConnectionCollation(collation);
    }

    public String getConnectionLifecycleInterceptors() {
        return this.mc.getConnectionLifecycleInterceptors();
    }

    public void setConnectionLifecycleInterceptors(String interceptors) {
        this.mc.setConnectionLifecycleInterceptors(interceptors);
    }

    public boolean getContinueBatchOnError() {
        return this.mc.getContinueBatchOnError();
    }

    public void setContinueBatchOnError(boolean property) {
        this.mc.setContinueBatchOnError(property);
    }

    public boolean getCreateDatabaseIfNotExist() {
        return this.mc.getCreateDatabaseIfNotExist();
    }

    public void setCreateDatabaseIfNotExist(boolean flag) {
        this.mc.setCreateDatabaseIfNotExist(flag);
    }

    public int getDefaultFetchSize() {
        return this.mc.getDefaultFetchSize();
    }

    public void setDefaultFetchSize(int n) {
        this.mc.setDefaultFetchSize(n);
    }

    public boolean getDontTrackOpenResources() {
        return this.mc.getDontTrackOpenResources();
    }

    public void setDontTrackOpenResources(boolean flag) {
        this.mc.setDontTrackOpenResources(flag);
    }

    public boolean getDumpMetadataOnColumnNotFound() {
        return this.mc.getDumpMetadataOnColumnNotFound();
    }

    public void setDumpMetadataOnColumnNotFound(boolean flag) {
        this.mc.setDumpMetadataOnColumnNotFound(flag);
    }

    public boolean getDumpQueriesOnException() {
        return this.mc.getDumpQueriesOnException();
    }

    public void setDumpQueriesOnException(boolean flag) {
        this.mc.setDumpQueriesOnException(flag);
    }

    public boolean getDynamicCalendars() {
        return this.mc.getDynamicCalendars();
    }

    public void setDynamicCalendars(boolean flag) {
        this.mc.setDynamicCalendars(flag);
    }

    public boolean getElideSetAutoCommits() {
        return this.mc.getElideSetAutoCommits();
    }

    public void setElideSetAutoCommits(boolean flag) {
        this.mc.setElideSetAutoCommits(flag);
    }

    public boolean getEmptyStringsConvertToZero() {
        return this.mc.getEmptyStringsConvertToZero();
    }

    public void setEmptyStringsConvertToZero(boolean flag) {
        this.mc.setEmptyStringsConvertToZero(flag);
    }

    public boolean getEmulateLocators() {
        return this.mc.getEmulateLocators();
    }

    public void setEmulateLocators(boolean property) {
        this.mc.setEmulateLocators(property);
    }

    public boolean getEmulateUnsupportedPstmts() {
        return this.mc.getEmulateUnsupportedPstmts();
    }

    public void setEmulateUnsupportedPstmts(boolean flag) {
        this.mc.setEmulateUnsupportedPstmts(flag);
    }

    public boolean getEnablePacketDebug() {
        return this.mc.getEnablePacketDebug();
    }

    public void setEnablePacketDebug(boolean flag) {
        this.mc.setEnablePacketDebug(flag);
    }

    public boolean getEnableQueryTimeouts() {
        return this.mc.getEnableQueryTimeouts();
    }

    public void setEnableQueryTimeouts(boolean flag) {
        this.mc.setEnableQueryTimeouts(flag);
    }

    public String getEncoding() {
        return this.mc.getEncoding();
    }

    public void setEncoding(String property) {
        this.mc.setEncoding(property);
    }

    public boolean getExplainSlowQueries() {
        return this.mc.getExplainSlowQueries();
    }

    public void setExplainSlowQueries(boolean flag) {
        this.mc.setExplainSlowQueries(flag);
    }

    public boolean getFailOverReadOnly() {
        return this.mc.getFailOverReadOnly();
    }

    public void setFailOverReadOnly(boolean flag) {
        this.mc.setFailOverReadOnly(flag);
    }

    public boolean getFunctionsNeverReturnBlobs() {
        return this.mc.getFunctionsNeverReturnBlobs();
    }

    public void setFunctionsNeverReturnBlobs(boolean flag) {
        this.mc.setFunctionsNeverReturnBlobs(flag);
    }

    public boolean getGatherPerfMetrics() {
        return this.mc.getGatherPerfMetrics();
    }

    public void setGatherPerfMetrics(boolean flag) {
        this.mc.setGatherPerfMetrics(flag);
    }

    public boolean getGatherPerformanceMetrics() {
        return this.mc.getGatherPerformanceMetrics();
    }

    public void setGatherPerformanceMetrics(boolean flag) {
        this.mc.setGatherPerformanceMetrics(flag);
    }

    public boolean getGenerateSimpleParameterMetadata() {
        return this.mc.getGenerateSimpleParameterMetadata();
    }

    public void setGenerateSimpleParameterMetadata(boolean flag) {
        this.mc.setGenerateSimpleParameterMetadata(flag);
    }

    public boolean getHoldResultsOpenOverStatementClose() {
        return this.mc.getHoldResultsOpenOverStatementClose();
    }

    public void setHoldResultsOpenOverStatementClose(boolean flag) {
        this.mc.setHoldResultsOpenOverStatementClose(flag);
    }

    public boolean getIgnoreNonTxTables() {
        return this.mc.getIgnoreNonTxTables();
    }

    public void setIgnoreNonTxTables(boolean property) {
        this.mc.setIgnoreNonTxTables(property);
    }

    public boolean getIncludeInnodbStatusInDeadlockExceptions() {
        return this.mc.getIncludeInnodbStatusInDeadlockExceptions();
    }

    public void setIncludeInnodbStatusInDeadlockExceptions(boolean flag) {
        this.mc.setIncludeInnodbStatusInDeadlockExceptions(flag);
    }

    public int getInitialTimeout() {
        return this.mc.getInitialTimeout();
    }

    public void setInitialTimeout(int property) {
        this.mc.setInitialTimeout(property);
    }

    public boolean getInteractiveClient() {
        return this.mc.getInteractiveClient();
    }

    public void setInteractiveClient(boolean property) {
        this.mc.setInteractiveClient(property);
    }

    public boolean getIsInteractiveClient() {
        return this.mc.getIsInteractiveClient();
    }

    public void setIsInteractiveClient(boolean property) {
        this.mc.setIsInteractiveClient(property);
    }

    public boolean getJdbcCompliantTruncation() {
        return this.mc.getJdbcCompliantTruncation();
    }

    public void setJdbcCompliantTruncation(boolean flag) {
        this.mc.setJdbcCompliantTruncation(flag);
    }

    public boolean getJdbcCompliantTruncationForReads() {
        return this.mc.getJdbcCompliantTruncationForReads();
    }

    public void setJdbcCompliantTruncationForReads(boolean jdbcCompliantTruncationForReads) {
        this.mc.setJdbcCompliantTruncationForReads(jdbcCompliantTruncationForReads);
    }

    public String getLargeRowSizeThreshold() {
        return this.mc.getLargeRowSizeThreshold();
    }

    public void setLargeRowSizeThreshold(String value) {
        this.mc.setLargeRowSizeThreshold(value);
    }

    public String getLoadBalanceStrategy() {
        return this.mc.getLoadBalanceStrategy();
    }

    public void setLoadBalanceStrategy(String strategy) {
        this.mc.setLoadBalanceStrategy(strategy);
    }

    public String getLocalSocketAddress() {
        return this.mc.getLocalSocketAddress();
    }

    public void setLocalSocketAddress(String address) {
        this.mc.setLocalSocketAddress(address);
    }

    public int getLocatorFetchBufferSize() {
        return this.mc.getLocatorFetchBufferSize();
    }

    public void setLocatorFetchBufferSize(String value) throws SQLException {
        this.mc.setLocatorFetchBufferSize(value);
    }

    public boolean getLogSlowQueries() {
        return this.mc.getLogSlowQueries();
    }

    public void setLogSlowQueries(boolean flag) {
        this.mc.setLogSlowQueries(flag);
    }

    public boolean getLogXaCommands() {
        return this.mc.getLogXaCommands();
    }

    public void setLogXaCommands(boolean flag) {
        this.mc.setLogXaCommands(flag);
    }

    public String getLogger() {
        return this.mc.getLogger();
    }

    public void setLogger(String property) {
        this.mc.setLogger(property);
    }

    public String getLoggerClassName() {
        return this.mc.getLoggerClassName();
    }

    public void setLoggerClassName(String className) {
        this.mc.setLoggerClassName(className);
    }

    public boolean getMaintainTimeStats() {
        return this.mc.getMaintainTimeStats();
    }

    public void setMaintainTimeStats(boolean flag) {
        this.mc.setMaintainTimeStats(flag);
    }

    public int getMaxQuerySizeToLog() {
        return this.mc.getMaxQuerySizeToLog();
    }

    public void setMaxQuerySizeToLog(int sizeInBytes) {
        this.mc.setMaxQuerySizeToLog(sizeInBytes);
    }

    public int getMaxReconnects() {
        return this.mc.getMaxReconnects();
    }

    public void setMaxReconnects(int property) {
        this.mc.setMaxReconnects(property);
    }

    public int getMaxRows() {
        return this.mc.getMaxRows();
    }

    public void setMaxRows(int property) {
        this.mc.setMaxRows(property);
    }

    public int getMetadataCacheSize() {
        return this.mc.getMetadataCacheSize();
    }

    public void setMetadataCacheSize(int value) {
        this.mc.setMetadataCacheSize(value);
    }

    public int getNetTimeoutForStreamingResults() {
        return this.mc.getNetTimeoutForStreamingResults();
    }

    public void setNetTimeoutForStreamingResults(int value) {
        this.mc.setNetTimeoutForStreamingResults(value);
    }

    public boolean getNoAccessToProcedureBodies() {
        return this.mc.getNoAccessToProcedureBodies();
    }

    public void setNoAccessToProcedureBodies(boolean flag) {
        this.mc.setNoAccessToProcedureBodies(flag);
    }

    public boolean getNoDatetimeStringSync() {
        return this.mc.getNoDatetimeStringSync();
    }

    public void setNoDatetimeStringSync(boolean flag) {
        this.mc.setNoDatetimeStringSync(flag);
    }

    public boolean getNoTimezoneConversionForTimeType() {
        return this.mc.getNoTimezoneConversionForTimeType();
    }

    public void setNoTimezoneConversionForTimeType(boolean flag) {
        this.mc.setNoTimezoneConversionForTimeType(flag);
    }

    public boolean getNullCatalogMeansCurrent() {
        return this.mc.getNullCatalogMeansCurrent();
    }

    public void setNullCatalogMeansCurrent(boolean value) {
        this.mc.setNullCatalogMeansCurrent(value);
    }

    public boolean getNullNamePatternMatchesAll() {
        return this.mc.getNullNamePatternMatchesAll();
    }

    public void setNullNamePatternMatchesAll(boolean value) {
        this.mc.setNullNamePatternMatchesAll(value);
    }

    public boolean getOverrideSupportsIntegrityEnhancementFacility() {
        return this.mc.getOverrideSupportsIntegrityEnhancementFacility();
    }

    public void setOverrideSupportsIntegrityEnhancementFacility(boolean flag) {
        this.mc.setOverrideSupportsIntegrityEnhancementFacility(flag);
    }

    public int getPacketDebugBufferSize() {
        return this.mc.getPacketDebugBufferSize();
    }

    public void setPacketDebugBufferSize(int size) {
        this.mc.setPacketDebugBufferSize(size);
    }

    public boolean getPadCharsWithSpace() {
        return this.mc.getPadCharsWithSpace();
    }

    public void setPadCharsWithSpace(boolean flag) {
        this.mc.setPadCharsWithSpace(flag);
    }

    public boolean getParanoid() {
        return this.mc.getParanoid();
    }

    public void setParanoid(boolean property) {
        this.mc.setParanoid(property);
    }

    public boolean getPedantic() {
        return this.mc.getPedantic();
    }

    public void setPedantic(boolean property) {
        this.mc.setPedantic(property);
    }

    public boolean getPinGlobalTxToPhysicalConnection() {
        return this.mc.getPinGlobalTxToPhysicalConnection();
    }

    public void setPinGlobalTxToPhysicalConnection(boolean flag) {
        this.mc.setPinGlobalTxToPhysicalConnection(flag);
    }

    public boolean getPopulateInsertRowWithDefaultValues() {
        return this.mc.getPopulateInsertRowWithDefaultValues();
    }

    public void setPopulateInsertRowWithDefaultValues(boolean flag) {
        this.mc.setPopulateInsertRowWithDefaultValues(flag);
    }

    public int getPrepStmtCacheSize() {
        return this.mc.getPrepStmtCacheSize();
    }

    public void setPrepStmtCacheSize(int cacheSize) {
        this.mc.setPrepStmtCacheSize(cacheSize);
    }

    public int getPrepStmtCacheSqlLimit() {
        return this.mc.getPrepStmtCacheSqlLimit();
    }

    public void setPrepStmtCacheSqlLimit(int sqlLimit) {
        this.mc.setPrepStmtCacheSqlLimit(sqlLimit);
    }

    public int getPreparedStatementCacheSize() {
        return this.mc.getPreparedStatementCacheSize();
    }

    public void setPreparedStatementCacheSize(int cacheSize) {
        this.mc.setPreparedStatementCacheSize(cacheSize);
    }

    public int getPreparedStatementCacheSqlLimit() {
        return this.mc.getPreparedStatementCacheSqlLimit();
    }

    public void setPreparedStatementCacheSqlLimit(int cacheSqlLimit) {
        this.mc.setPreparedStatementCacheSqlLimit(cacheSqlLimit);
    }

    public boolean getProcessEscapeCodesForPrepStmts() {
        return this.mc.getProcessEscapeCodesForPrepStmts();
    }

    public void setProcessEscapeCodesForPrepStmts(boolean flag) {
        this.mc.setProcessEscapeCodesForPrepStmts(flag);
    }

    public boolean getProfileSQL() {
        return this.mc.getProfileSQL();
    }

    public void setProfileSQL(boolean flag) {
        this.mc.setProfileSQL(flag);
    }

    public boolean getProfileSql() {
        return this.mc.getProfileSql();
    }

    public void setProfileSql(boolean property) {
        this.mc.setProfileSql(property);
    }

    public String getPropertiesTransform() {
        return this.mc.getPropertiesTransform();
    }

    public void setPropertiesTransform(String value) {
        this.mc.setPropertiesTransform(value);
    }

    public int getQueriesBeforeRetryMaster() {
        return this.mc.getQueriesBeforeRetryMaster();
    }

    public void setQueriesBeforeRetryMaster(int property) {
        this.mc.setQueriesBeforeRetryMaster(property);
    }

    public boolean getReconnectAtTxEnd() {
        return this.mc.getReconnectAtTxEnd();
    }

    public void setReconnectAtTxEnd(boolean property) {
        this.mc.setReconnectAtTxEnd(property);
    }

    public boolean getRelaxAutoCommit() {
        return this.mc.getRelaxAutoCommit();
    }

    public void setRelaxAutoCommit(boolean property) {
        this.mc.setRelaxAutoCommit(property);
    }

    public int getReportMetricsIntervalMillis() {
        return this.mc.getReportMetricsIntervalMillis();
    }

    public void setReportMetricsIntervalMillis(int millis) {
        this.mc.setReportMetricsIntervalMillis(millis);
    }

    public boolean getRequireSSL() {
        return this.mc.getRequireSSL();
    }

    public void setRequireSSL(boolean property) {
        this.mc.setRequireSSL(property);
    }

    public String getResourceId() {
        return this.mc.getResourceId();
    }

    public void setResourceId(String resourceId) {
        this.mc.setResourceId(resourceId);
    }

    public int getResultSetSizeThreshold() {
        return this.mc.getResultSetSizeThreshold();
    }

    public void setResultSetSizeThreshold(int threshold) {
        this.mc.setResultSetSizeThreshold(threshold);
    }

    public boolean getRewriteBatchedStatements() {
        return this.mc.getRewriteBatchedStatements();
    }

    public void setRewriteBatchedStatements(boolean flag) {
        this.mc.setRewriteBatchedStatements(flag);
    }

    public boolean getRollbackOnPooledClose() {
        return this.mc.getRollbackOnPooledClose();
    }

    public void setRollbackOnPooledClose(boolean flag) {
        this.mc.setRollbackOnPooledClose(flag);
    }

    public boolean getRoundRobinLoadBalance() {
        return this.mc.getRoundRobinLoadBalance();
    }

    public void setRoundRobinLoadBalance(boolean flag) {
        this.mc.setRoundRobinLoadBalance(flag);
    }

    public boolean getRunningCTS13() {
        return this.mc.getRunningCTS13();
    }

    public void setRunningCTS13(boolean flag) {
        this.mc.setRunningCTS13(flag);
    }

    public int getSecondsBeforeRetryMaster() {
        return this.mc.getSecondsBeforeRetryMaster();
    }

    public void setSecondsBeforeRetryMaster(int property) {
        this.mc.setSecondsBeforeRetryMaster(property);
    }

    public String getServerTimezone() {
        return this.mc.getServerTimezone();
    }

    public void setServerTimezone(String property) {
        this.mc.setServerTimezone(property);
    }

    public String getSessionVariables() {
        return this.mc.getSessionVariables();
    }

    public void setSessionVariables(String variables) {
        this.mc.setSessionVariables(variables);
    }

    public int getSlowQueryThresholdMillis() {
        return this.mc.getSlowQueryThresholdMillis();
    }

    public void setSlowQueryThresholdMillis(int millis) {
        this.mc.setSlowQueryThresholdMillis(millis);
    }

    public long getSlowQueryThresholdNanos() {
        return this.mc.getSlowQueryThresholdNanos();
    }

    public void setSlowQueryThresholdNanos(long nanos) {
        this.mc.setSlowQueryThresholdNanos(nanos);
    }

    public String getSocketFactory() {
        return this.mc.getSocketFactory();
    }

    public void setSocketFactory(String name) {
        this.mc.setSocketFactory(name);
    }

    public String getSocketFactoryClassName() {
        return this.mc.getSocketFactoryClassName();
    }

    public void setSocketFactoryClassName(String property) {
        this.mc.setSocketFactoryClassName(property);
    }

    public int getSocketTimeout() {
        return this.mc.getSocketTimeout();
    }

    public void setSocketTimeout(int property) {
        this.mc.setSocketTimeout(property);
    }

    public String getStatementInterceptors() {
        return this.mc.getStatementInterceptors();
    }

    public void setStatementInterceptors(String value) {
        this.mc.setStatementInterceptors(value);
    }

    public boolean getStrictFloatingPoint() {
        return this.mc.getStrictFloatingPoint();
    }

    public void setStrictFloatingPoint(boolean property) {
        this.mc.setStrictFloatingPoint(property);
    }

    public boolean getStrictUpdates() {
        return this.mc.getStrictUpdates();
    }

    public void setStrictUpdates(boolean property) {
        this.mc.setStrictUpdates(property);
    }

    public boolean getTcpKeepAlive() {
        return this.mc.getTcpKeepAlive();
    }

    public void setTcpKeepAlive(boolean flag) {
        this.mc.setTcpKeepAlive(flag);
    }

    public boolean getTcpNoDelay() {
        return this.mc.getTcpNoDelay();
    }

    public void setTcpNoDelay(boolean flag) {
        this.mc.setTcpNoDelay(flag);
    }

    public int getTcpRcvBuf() {
        return this.mc.getTcpRcvBuf();
    }

    public void setTcpRcvBuf(int bufSize) {
        this.mc.setTcpRcvBuf(bufSize);
    }

    public int getTcpSndBuf() {
        return this.mc.getTcpSndBuf();
    }

    public void setTcpSndBuf(int bufSize) {
        this.mc.setTcpSndBuf(bufSize);
    }

    public int getTcpTrafficClass() {
        return this.mc.getTcpTrafficClass();
    }

    public void setTcpTrafficClass(int classFlags) {
        this.mc.setTcpTrafficClass(classFlags);
    }

    public boolean getTinyInt1isBit() {
        return this.mc.getTinyInt1isBit();
    }

    public void setTinyInt1isBit(boolean flag) {
        this.mc.setTinyInt1isBit(flag);
    }

    public boolean getTraceProtocol() {
        return this.mc.getTraceProtocol();
    }

    public void setTraceProtocol(boolean flag) {
        this.mc.setTraceProtocol(flag);
    }

    public boolean getTransformedBitIsBoolean() {
        return this.mc.getTransformedBitIsBoolean();
    }

    public void setTransformedBitIsBoolean(boolean flag) {
        this.mc.setTransformedBitIsBoolean(flag);
    }

    public boolean getTreatUtilDateAsTimestamp() {
        return this.mc.getTreatUtilDateAsTimestamp();
    }

    public void setTreatUtilDateAsTimestamp(boolean flag) {
        this.mc.setTreatUtilDateAsTimestamp(flag);
    }

    public String getTrustCertificateKeyStorePassword() {
        return this.mc.getTrustCertificateKeyStorePassword();
    }

    public void setTrustCertificateKeyStorePassword(String value) {
        this.mc.setTrustCertificateKeyStorePassword(value);
    }

    public String getTrustCertificateKeyStoreType() {
        return this.mc.getTrustCertificateKeyStoreType();
    }

    public void setTrustCertificateKeyStoreType(String value) {
        this.mc.setTrustCertificateKeyStoreType(value);
    }

    public String getTrustCertificateKeyStoreUrl() {
        return this.mc.getTrustCertificateKeyStoreUrl();
    }

    public void setTrustCertificateKeyStoreUrl(String value) {
        this.mc.setTrustCertificateKeyStoreUrl(value);
    }

    public boolean getUltraDevHack() {
        return this.mc.getUltraDevHack();
    }

    public void setUltraDevHack(boolean flag) {
        this.mc.setUltraDevHack(flag);
    }

    public boolean getUseBlobToStoreUTF8OutsideBMP() {
        return this.mc.getUseBlobToStoreUTF8OutsideBMP();
    }

    public void setUseBlobToStoreUTF8OutsideBMP(boolean flag) {
        this.mc.setUseBlobToStoreUTF8OutsideBMP(flag);
    }

    public boolean getUseCompression() {
        return this.mc.getUseCompression();
    }

    public void setUseCompression(boolean property) {
        this.mc.setUseCompression(property);
    }

    public String getUseConfigs() {
        return this.mc.getUseConfigs();
    }

    public void setUseConfigs(String configs) {
        this.mc.setUseConfigs(configs);
    }

    public boolean getUseCursorFetch() {
        return this.mc.getUseCursorFetch();
    }

    public void setUseCursorFetch(boolean flag) {
        this.mc.setUseCursorFetch(flag);
    }

    public boolean getUseDirectRowUnpack() {
        return this.mc.getUseDirectRowUnpack();
    }

    public void setUseDirectRowUnpack(boolean flag) {
        this.mc.setUseDirectRowUnpack(flag);
    }

    public boolean getUseDynamicCharsetInfo() {
        return this.mc.getUseDynamicCharsetInfo();
    }

    public void setUseDynamicCharsetInfo(boolean flag) {
        this.mc.setUseDynamicCharsetInfo(flag);
    }

    public boolean getUseFastDateParsing() {
        return this.mc.getUseFastDateParsing();
    }

    public void setUseFastDateParsing(boolean flag) {
        this.mc.setUseFastDateParsing(flag);
    }

    public boolean getUseFastIntParsing() {
        return this.mc.getUseFastIntParsing();
    }

    public void setUseFastIntParsing(boolean flag) {
        this.mc.setUseFastIntParsing(flag);
    }

    public boolean getUseGmtMillisForDatetimes() {
        return this.mc.getUseGmtMillisForDatetimes();
    }

    public void setUseGmtMillisForDatetimes(boolean flag) {
        this.mc.setUseGmtMillisForDatetimes(flag);
    }

    public boolean getUseHostsInPrivileges() {
        return this.mc.getUseHostsInPrivileges();
    }

    public void setUseHostsInPrivileges(boolean property) {
        this.mc.setUseHostsInPrivileges(property);
    }

    public boolean getUseInformationSchema() {
        return this.mc.getUseInformationSchema();
    }

    public void setUseInformationSchema(boolean flag) {
        this.mc.setUseInformationSchema(flag);
    }

    public boolean getUseJDBCCompliantTimezoneShift() {
        return this.mc.getUseJDBCCompliantTimezoneShift();
    }

    public void setUseJDBCCompliantTimezoneShift(boolean flag) {
        this.mc.setUseJDBCCompliantTimezoneShift(flag);
    }

    public boolean getUseJvmCharsetConverters() {
        return this.mc.getUseJvmCharsetConverters();
    }

    public void setUseJvmCharsetConverters(boolean flag) {
        this.mc.setUseJvmCharsetConverters(flag);
    }

    public boolean getUseLocalSessionState() {
        return this.mc.getUseLocalSessionState();
    }

    public void setUseLocalSessionState(boolean flag) {
        this.mc.setUseLocalSessionState(flag);
    }

    public boolean getUseNanosForElapsedTime() {
        return this.mc.getUseNanosForElapsedTime();
    }

    public void setUseNanosForElapsedTime(boolean flag) {
        this.mc.setUseNanosForElapsedTime(flag);
    }

    public boolean getUseOldAliasMetadataBehavior() {
        return this.mc.getUseOldAliasMetadataBehavior();
    }

    public void setUseOldAliasMetadataBehavior(boolean flag) {
        this.mc.setUseOldAliasMetadataBehavior(flag);
    }

    public boolean getUseOldUTF8Behavior() {
        return this.mc.getUseOldUTF8Behavior();
    }

    public void setUseOldUTF8Behavior(boolean flag) {
        this.mc.setUseOldUTF8Behavior(flag);
    }

    public boolean getUseOnlyServerErrorMessages() {
        return this.mc.getUseOnlyServerErrorMessages();
    }

    public void setUseOnlyServerErrorMessages(boolean flag) {
        this.mc.setUseOnlyServerErrorMessages(flag);
    }

    public boolean getUseReadAheadInput() {
        return this.mc.getUseReadAheadInput();
    }

    public void setUseReadAheadInput(boolean flag) {
        this.mc.setUseReadAheadInput(flag);
    }

    public boolean getUseSSL() {
        return this.mc.getUseSSL();
    }

    public void setUseSSL(boolean property) {
        this.mc.setUseSSL(property);
    }

    public boolean getUseSSPSCompatibleTimezoneShift() {
        return this.mc.getUseSSPSCompatibleTimezoneShift();
    }

    public void setUseSSPSCompatibleTimezoneShift(boolean flag) {
        this.mc.setUseSSPSCompatibleTimezoneShift(flag);
    }

    public boolean getUseServerPrepStmts() {
        return this.mc.getUseServerPrepStmts();
    }

    public void setUseServerPrepStmts(boolean flag) {
        this.mc.setUseServerPrepStmts(flag);
    }

    public boolean getUseServerPreparedStmts() {
        return this.mc.getUseServerPreparedStmts();
    }

    public void setUseServerPreparedStmts(boolean flag) {
        this.mc.setUseServerPreparedStmts(flag);
    }

    public boolean getUseSqlStateCodes() {
        return this.mc.getUseSqlStateCodes();
    }

    public void setUseSqlStateCodes(boolean flag) {
        this.mc.setUseSqlStateCodes(flag);
    }

    public boolean getUseStreamLengthsInPrepStmts() {
        return this.mc.getUseStreamLengthsInPrepStmts();
    }

    public void setUseStreamLengthsInPrepStmts(boolean property) {
        this.mc.setUseStreamLengthsInPrepStmts(property);
    }

    public boolean getUseTimezone() {
        return this.mc.getUseTimezone();
    }

    public void setUseTimezone(boolean property) {
        this.mc.setUseTimezone(property);
    }

    public boolean getUseUltraDevWorkAround() {
        return this.mc.getUseUltraDevWorkAround();
    }

    public void setUseUltraDevWorkAround(boolean property) {
        this.mc.setUseUltraDevWorkAround(property);
    }

    public boolean getUseUnbufferedInput() {
        return this.mc.getUseUnbufferedInput();
    }

    public void setUseUnbufferedInput(boolean flag) {
        this.mc.setUseUnbufferedInput(flag);
    }

    public boolean getUseUnicode() {
        return this.mc.getUseUnicode();
    }

    public void setUseUnicode(boolean flag) {
        this.mc.setUseUnicode(flag);
    }

    public boolean getUseUsageAdvisor() {
        return this.mc.getUseUsageAdvisor();
    }

    public void setUseUsageAdvisor(boolean useUsageAdvisorFlag) {
        this.mc.setUseUsageAdvisor(useUsageAdvisorFlag);
    }

    public String getUtf8OutsideBmpExcludedColumnNamePattern() {
        return this.mc.getUtf8OutsideBmpExcludedColumnNamePattern();
    }

    public void setUtf8OutsideBmpExcludedColumnNamePattern(String regexPattern) {
        this.mc.setUtf8OutsideBmpExcludedColumnNamePattern(regexPattern);
    }

    public String getUtf8OutsideBmpIncludedColumnNamePattern() {
        return this.mc.getUtf8OutsideBmpIncludedColumnNamePattern();
    }

    public void setUtf8OutsideBmpIncludedColumnNamePattern(String regexPattern) {
        this.mc.setUtf8OutsideBmpIncludedColumnNamePattern(regexPattern);
    }

    public boolean getYearIsDateType() {
        return this.mc.getYearIsDateType();
    }

    public void setYearIsDateType(boolean flag) {
        this.mc.setYearIsDateType(flag);
    }

    public String getZeroDateTimeBehavior() {
        return this.mc.getZeroDateTimeBehavior();
    }

    public void setZeroDateTimeBehavior(String behavior) {
        this.mc.setZeroDateTimeBehavior(behavior);
    }

    public void setAutoReconnect(boolean flag) {
        this.mc.setAutoReconnect(flag);
    }

    public void setAutoReconnectForConnectionPools(boolean property) {
        this.mc.setAutoReconnectForConnectionPools(property);
    }

    public void setCapitalizeDBMDTypes(boolean property) {
        this.mc.setCapitalizeDBMDTypes(property);
    }

    public void setCharacterEncoding(String encoding) {
        this.mc.setCharacterEncoding(encoding);
    }

    public void setDetectServerPreparedStmts(boolean property) {
        this.mc.setDetectServerPreparedStmts(property);
    }

    public boolean useUnbufferedInput() {
        return this.mc.useUnbufferedInput();
    }

    public void initializeExtension(Extension ex) throws SQLException {
        this.mc.initializeExtension(ex);
    }

    public String getProfilerEventHandler() {
        return this.mc.getProfilerEventHandler();
    }

    public void setProfilerEventHandler(String handler) {
        this.mc.setProfilerEventHandler(handler);
    }

    public boolean getVerifyServerCertificate() {
        return this.mc.getVerifyServerCertificate();
    }

    public void setVerifyServerCertificate(boolean flag) {
        this.mc.setVerifyServerCertificate(flag);
    }

    public boolean getUseLegacyDatetimeCode() {
        return this.mc.getUseLegacyDatetimeCode();
    }

    public void setUseLegacyDatetimeCode(boolean flag) {
        this.mc.setUseLegacyDatetimeCode(flag);
    }

    public int getSelfDestructOnPingMaxOperations() {
        return this.mc.getSelfDestructOnPingMaxOperations();
    }

    public void setSelfDestructOnPingMaxOperations(int maxOperations) {
        this.mc.setSelfDestructOnPingMaxOperations(maxOperations);
    }

    public int getSelfDestructOnPingSecondsLifetime() {
        return this.mc.getSelfDestructOnPingSecondsLifetime();
    }

    public void setSelfDestructOnPingSecondsLifetime(int seconds) {
        this.mc.setSelfDestructOnPingSecondsLifetime(seconds);
    }

    public boolean getUseColumnNamesInFindColumn() {
        return this.mc.getUseColumnNamesInFindColumn();
    }

    public void setUseColumnNamesInFindColumn(boolean flag) {
        this.mc.setUseColumnNamesInFindColumn(flag);
    }

    public boolean getUseLocalTransactionState() {
        return this.mc.getUseLocalTransactionState();
    }

    public void setUseLocalTransactionState(boolean flag) {
        this.mc.setUseLocalTransactionState(flag);
    }

    public boolean getCompensateOnDuplicateKeyUpdateCounts() {
        return this.mc.getCompensateOnDuplicateKeyUpdateCounts();
    }

    public void setCompensateOnDuplicateKeyUpdateCounts(boolean flag) {
        this.mc.setCompensateOnDuplicateKeyUpdateCounts(flag);
    }

    public boolean getUseAffectedRows() {
        return this.mc.getUseAffectedRows();
    }

    public void setUseAffectedRows(boolean flag) {
        this.mc.setUseAffectedRows(flag);
    }

    public String getPasswordCharacterEncoding() {
        return this.mc.getPasswordCharacterEncoding();
    }

    public void setPasswordCharacterEncoding(String characterSet) {
        this.mc.setPasswordCharacterEncoding(characterSet);
    }

    public int getAutoIncrementIncrement() {
        return this.mc.getAutoIncrementIncrement();
    }

    public int getLoadBalanceBlacklistTimeout() {
        return this.mc.getLoadBalanceBlacklistTimeout();
    }

    public void setLoadBalanceBlacklistTimeout(int loadBalanceBlacklistTimeout) {
        this.mc.setLoadBalanceBlacklistTimeout(loadBalanceBlacklistTimeout);
    }

    public int getLoadBalancePingTimeout() {
        return this.mc.getLoadBalancePingTimeout();
    }

    public void setLoadBalancePingTimeout(int loadBalancePingTimeout) {
        this.mc.setLoadBalancePingTimeout(loadBalancePingTimeout);
    }

    public boolean getLoadBalanceValidateConnectionOnSwapServer() {
        return this.mc.getLoadBalanceValidateConnectionOnSwapServer();
    }

    public void setLoadBalanceValidateConnectionOnSwapServer(boolean loadBalanceValidateConnectionOnSwapServer) {
        this.mc.setLoadBalanceValidateConnectionOnSwapServer(loadBalanceValidateConnectionOnSwapServer);
    }

    public int getRetriesAllDown() {
        return this.mc.getRetriesAllDown();
    }

    public void setRetriesAllDown(int retriesAllDown) {
        this.mc.setRetriesAllDown(retriesAllDown);
    }

    public ExceptionInterceptor getExceptionInterceptor() {
        return this.pooledConnection.getExceptionInterceptor();
    }

    public String getExceptionInterceptors() {
        return this.mc.getExceptionInterceptors();
    }

    public void setExceptionInterceptors(String exceptionInterceptors) {
        this.mc.setExceptionInterceptors(exceptionInterceptors);
    }

    public boolean getQueryTimeoutKillsConnection() {
        return this.mc.getQueryTimeoutKillsConnection();
    }

    public void setQueryTimeoutKillsConnection(boolean queryTimeoutKillsConnection) {
        this.mc.setQueryTimeoutKillsConnection(queryTimeoutKillsConnection);
    }

    public boolean hasSameProperties(Connection c) {
        return this.mc.hasSameProperties(c);
    }

    public Properties getProperties() {
        return this.mc.getProperties();
    }

    public String getHost() {
        return this.mc.getHost();
    }

    public void setProxy(MySQLConnection conn) {
        this.mc.setProxy(conn);
    }

    public boolean getRetainStatementAfterResultSetClose() {
        return this.mc.getRetainStatementAfterResultSetClose();
    }

    public void setRetainStatementAfterResultSetClose(boolean flag) {
        this.mc.setRetainStatementAfterResultSetClose(flag);
    }

    public int getMaxAllowedPacket() {
        return this.mc.getMaxAllowedPacket();
    }

    public String getLoadBalanceConnectionGroup() {
        return this.mc.getLoadBalanceConnectionGroup();
    }

    public void setLoadBalanceConnectionGroup(String loadBalanceConnectionGroup) {
        this.mc.setLoadBalanceConnectionGroup(loadBalanceConnectionGroup);
    }

    public boolean getLoadBalanceEnableJMX() {
        return this.mc.getLoadBalanceEnableJMX();
    }

    public void setLoadBalanceEnableJMX(boolean loadBalanceEnableJMX) {
        this.mc.setLoadBalanceEnableJMX(loadBalanceEnableJMX);
    }

    public String getLoadBalanceExceptionChecker() {
        return this.mc.getLoadBalanceExceptionChecker();
    }

    public void setLoadBalanceExceptionChecker(String loadBalanceExceptionChecker) {
        this.mc.setLoadBalanceExceptionChecker(loadBalanceExceptionChecker);
    }

    public String getLoadBalanceSQLExceptionSubclassFailover() {
        return this.mc.getLoadBalanceSQLExceptionSubclassFailover();
    }

    public void setLoadBalanceSQLExceptionSubclassFailover(String loadBalanceSQLExceptionSubclassFailover) {
        this.mc.setLoadBalanceSQLExceptionSubclassFailover(loadBalanceSQLExceptionSubclassFailover);
    }

    public String getLoadBalanceSQLStateFailover() {
        return this.mc.getLoadBalanceSQLStateFailover();
    }

    public void setLoadBalanceSQLStateFailover(String loadBalanceSQLStateFailover) {
        this.mc.setLoadBalanceSQLStateFailover(loadBalanceSQLStateFailover);
    }

    public String getLoadBalanceAutoCommitStatementRegex() {
        return this.mc.getLoadBalanceAutoCommitStatementRegex();
    }

    public void setLoadBalanceAutoCommitStatementRegex(String loadBalanceAutoCommitStatementRegex) {
        this.mc.setLoadBalanceAutoCommitStatementRegex(loadBalanceAutoCommitStatementRegex);
    }

    public int getLoadBalanceAutoCommitStatementThreshold() {
        return this.mc.getLoadBalanceAutoCommitStatementThreshold();
    }

    public void setLoadBalanceAutoCommitStatementThreshold(int loadBalanceAutoCommitStatementThreshold) {
        this.mc.setLoadBalanceAutoCommitStatementThreshold(loadBalanceAutoCommitStatementThreshold);
    }

    public boolean getIncludeThreadDumpInDeadlockExceptions() {
        return this.mc.getIncludeThreadDumpInDeadlockExceptions();
    }

    public void setIncludeThreadDumpInDeadlockExceptions(boolean flag) {
        this.mc.setIncludeThreadDumpInDeadlockExceptions(flag);
    }

    public boolean getIncludeThreadNamesAsStatementComment() {
        return this.mc.getIncludeThreadNamesAsStatementComment();
    }

    public void setIncludeThreadNamesAsStatementComment(boolean flag) {
        this.mc.setIncludeThreadNamesAsStatementComment(flag);
    }

    public boolean isServerLocal() throws SQLException {
        return this.mc.isServerLocal();
    }

    public String getAuthenticationPlugins() {
        return this.mc.getAuthenticationPlugins();
    }

    public void setAuthenticationPlugins(String authenticationPlugins) {
        this.mc.setAuthenticationPlugins(authenticationPlugins);
    }

    public String getDisabledAuthenticationPlugins() {
        return this.mc.getDisabledAuthenticationPlugins();
    }

    public void setDisabledAuthenticationPlugins(String disabledAuthenticationPlugins) {
        this.mc.setDisabledAuthenticationPlugins(disabledAuthenticationPlugins);
    }

    public String getDefaultAuthenticationPlugin() {
        return this.mc.getDefaultAuthenticationPlugin();
    }

    public void setDefaultAuthenticationPlugin(String defaultAuthenticationPlugin) {
        this.mc.setDefaultAuthenticationPlugin(defaultAuthenticationPlugin);
    }

    public String getParseInfoCacheFactory() {
        return this.mc.getParseInfoCacheFactory();
    }

    public void setParseInfoCacheFactory(String factoryClassname) {
        this.mc.setParseInfoCacheFactory(factoryClassname);
    }

    public String getSchema() throws SQLException {
        return this.mc.getSchema();
    }

    public void setSchema(String schema) throws SQLException {
        this.mc.setSchema(schema);
    }

    public void abort(Executor executor) throws SQLException {
        this.mc.abort(executor);
    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        this.mc.setNetworkTimeout(executor, milliseconds);
    }

    public int getNetworkTimeout() throws SQLException {
        return this.mc.getNetworkTimeout();
    }

    public String getServerConfigCacheFactory() {
        return this.mc.getServerConfigCacheFactory();
    }

    public void setServerConfigCacheFactory(String factoryClassname) {
        this.mc.setServerConfigCacheFactory(factoryClassname);
    }

    public boolean getDisconnectOnExpiredPasswords() {
        return this.mc.getDisconnectOnExpiredPasswords();
    }

    public void setDisconnectOnExpiredPasswords(boolean disconnectOnExpiredPasswords) {
        this.mc.setDisconnectOnExpiredPasswords(disconnectOnExpiredPasswords);
    }
}

