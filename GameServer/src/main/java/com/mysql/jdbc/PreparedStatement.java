package com.mysql.jdbc;

import com.mysql.jdbc.exceptions.MySQLStatementCancelledException;
import com.mysql.jdbc.exceptions.MySQLTimeoutException;
import com.mysql.jdbc.profiler.ProfilerEvent;

import java.io.*;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.sql.*;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class PreparedStatement
        extends StatementImpl
        implements PreparedStatement {
    private static final Constructor<?> JDBC_4_PSTMT_2_ARG_CTOR;
    private static final Constructor<?> JDBC_4_PSTMT_3_ARG_CTOR;
    private static final Constructor<?> JDBC_4_PSTMT_4_ARG_CTOR;
    private static final byte[] HEX_DIGITS = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70};

    static {
        if (Util.isJdbc4()) {
            try {
                JDBC_4_PSTMT_2_ARG_CTOR = Class.forName("com.mysql.jdbc.JDBC4PreparedStatement").getConstructor(new Class[]{MySQLConnection.class, String.class});

                JDBC_4_PSTMT_3_ARG_CTOR = Class.forName("com.mysql.jdbc.JDBC4PreparedStatement").getConstructor(new Class[]{MySQLConnection.class, String.class, String.class});

                JDBC_4_PSTMT_4_ARG_CTOR = Class.forName("com.mysql.jdbc.JDBC4PreparedStatement").getConstructor(new Class[]{MySQLConnection.class, String.class, String.class, ParseInfo.class});

            } catch (SecurityException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            JDBC_4_PSTMT_2_ARG_CTOR = null;
            JDBC_4_PSTMT_3_ARG_CTOR = null;
            JDBC_4_PSTMT_4_ARG_CTOR = null;
        }
    }

    protected boolean batchHasPlainStatements = false;
    protected char firstCharOfStmt = Character.MIN_VALUE;
    protected boolean hasLimitClause = false;
    protected boolean isLoadDataQuery = false;
    protected boolean[] isNull = null;
    protected int numberOfExecutions = 0;
    protected String originalSql = null;
    protected int parameterCount;
    protected MysqlParameterMetadata parameterMetaData;
    protected int[] parameterTypes = null;
    protected ParseInfo parseInfo;
    protected boolean useTrueBoolean = false;
    protected boolean usingAnsiMode;
    protected String batchedValuesClause;
    protected int batchCommandIndex = -1;
    protected boolean serverSupportsFracSecs;
    protected int rewrittenBatchSize;
    private DatabaseMetaData dbmd = null;
    private boolean[] isStream = null;
    private InputStream[] parameterStreams = null;
    private byte[][] parameterValues = (byte[][]) null;
    private ResultSetMetaData pstmtResultMetaData;
    private byte[][] staticSqlStrings = (byte[][]) null;
    private byte[] streamConvertBuf = null;
    private int[] streamLengths = null;
    private SimpleDateFormat tsdf = null;
    private boolean doPingInstead;
    private SimpleDateFormat ddf;
    private SimpleDateFormat tdf;
    private boolean compensateForOnDuplicateKeyUpdate = false;
    private CharsetEncoder charsetEncoder;

    public PreparedStatement(MySQLConnection conn, String catalog) throws SQLException {
        super(conn, catalog);

        this.rewrittenBatchSize = 0;
        detectFractionalSecondsSupport();
        this.compensateForOnDuplicateKeyUpdate = this.connection.getCompensateOnDuplicateKeyUpdateCounts();
    }

    public PreparedStatement(MySQLConnection conn, String sql, String catalog) throws SQLException {
        super(conn, catalog);
        this.rewrittenBatchSize = 0;
        if (sql == null)
            throw SQLError.createSQLException(Messages.getString("PreparedStatement.0"), "S1009", getExceptionInterceptor());
        detectFractionalSecondsSupport();
        this.originalSql = sql;
        if (this.originalSql.startsWith("")) {
            this.doPingInstead = true;
        } else {
            this.doPingInstead = false;
        }
        this.dbmd = this.connection.getMetaData();
        this.useTrueBoolean = this.connection.versionMeetsMinimum(3, 21, 23);
        this.parseInfo = new ParseInfo(sql, this.connection, this.dbmd, this.charEncoding, this.charConverter);
        initializeFromParseInfo();
        this.compensateForOnDuplicateKeyUpdate = this.connection.getCompensateOnDuplicateKeyUpdateCounts();
        if (conn.getRequiresEscapingEncoder()) this.charsetEncoder = Charset.forName(conn.getEncoding()).newEncoder();
    }

    public PreparedStatement(MySQLConnection conn, String sql, String catalog, ParseInfo cachedParseInfo) throws SQLException {
        super(conn, catalog);
        this.rewrittenBatchSize = 0;
        if (sql == null)
            throw SQLError.createSQLException(Messages.getString("PreparedStatement.1"), "S1009", getExceptionInterceptor());
        detectFractionalSecondsSupport();
        this.originalSql = sql;
        this.dbmd = this.connection.getMetaData();
        this.useTrueBoolean = this.connection.versionMeetsMinimum(3, 21, 23);
        this.parseInfo = cachedParseInfo;
        this.usingAnsiMode = !this.connection.useAnsiQuotedIdentifiers();
        initializeFromParseInfo();
        this.compensateForOnDuplicateKeyUpdate = this.connection.getCompensateOnDuplicateKeyUpdateCounts();
        if (conn.getRequiresEscapingEncoder()) this.charsetEncoder = Charset.forName(conn.getEncoding()).newEncoder();
    }

    protected static int readFully(Reader reader, char[] buf, int length) throws IOException {
        int numCharsRead = 0;

        while (numCharsRead < length) {
            int count = reader.read(buf, numCharsRead, length - numCharsRead);

            if (count < 0) {
                break;
            }

            numCharsRead += count;
        }

        return numCharsRead;
    }

    protected static PreparedStatement getInstance(MySQLConnection conn, String catalog) throws SQLException {
        if (!Util.isJdbc4()) {
            return new PreparedStatement(conn, catalog);
        }

        return (PreparedStatement) Util.handleNewInstance(JDBC_4_PSTMT_2_ARG_CTOR, new Object[]{conn, catalog}, conn.getExceptionInterceptor());
    }

    protected static PreparedStatement getInstance(MySQLConnection conn, String sql, String catalog) throws SQLException {
        if (!Util.isJdbc4()) {
            return new PreparedStatement(conn, sql, catalog);
        }

        return (PreparedStatement) Util.handleNewInstance(JDBC_4_PSTMT_3_ARG_CTOR, new Object[]{conn, sql, catalog}, conn.getExceptionInterceptor());
    }

    protected static PreparedStatement getInstance(MySQLConnection conn, String sql, String catalog, ParseInfo cachedParseInfo) throws SQLException {
        if (!Util.isJdbc4()) {
            return new PreparedStatement(conn, sql, catalog, cachedParseInfo);
        }

        return (PreparedStatement) Util.handleNewInstance(JDBC_4_PSTMT_4_ARG_CTOR, new Object[]{conn, sql, catalog, cachedParseInfo}, conn.getExceptionInterceptor());
    }

    protected static boolean canRewrite(String sql, boolean isOnDuplicateKeyUpdate, int locationOfOnDuplicateKeyUpdate, int statementStartPos) {
        boolean rewritableOdku = true;

        if (isOnDuplicateKeyUpdate) {
            int updateClausePos = StringUtils.indexOfIgnoreCase(locationOfOnDuplicateKeyUpdate, sql, " UPDATE ");

            if (updateClausePos != -1) {
                rewritableOdku = (StringUtils.indexOfIgnoreCaseRespectMarker(updateClausePos, sql, "LAST_INSERT_ID", "\"'`", "\"'`", false) == -1);
            }
        }

        return (StringUtils.startsWithIgnoreCaseAndWs(sql, "INSERT", statementStartPos) && StringUtils.indexOfIgnoreCaseRespectMarker(statementStartPos, sql, "SELECT", "\"'`", "\"'`", false) == -1 && rewritableOdku);
    }

    protected void detectFractionalSecondsSupport() throws SQLException {
        this.serverSupportsFracSecs = (this.connection != null && this.connection.versionMeetsMinimum(5, 6, 4));
    }

    public void addBatch() throws SQLException {
        synchronized (checkClosed()) {
            if (this.batchedArgs == null) this.batchedArgs = new ArrayList();
            for (int i = 0; i < this.parameterValues.length; i++)
                checkAllParametersSet(this.parameterValues[i], this.parameterStreams[i], i);
            this.batchedArgs.add(new BatchParams(this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths, this.isNull));
        }
    }

    public void addBatch(String sql) throws SQLException {
        synchronized (checkClosed()) {
            this.batchHasPlainStatements = true;
            super.addBatch(sql);
        }
    }

    protected String asSql() throws SQLException {
        return asSql(false);
    }

    protected String asSql(boolean quoteStreamsAndUnknowns) throws SQLException {
        synchronized (checkClosed()) {
            StringBuffer buf = new StringBuffer();
            try {
                int realParameterCount = this.parameterCount + getParameterIndexOffset();
                Object batchArg = null;
                if (this.batchCommandIndex != -1) batchArg = this.batchedArgs.get(this.batchCommandIndex);
                for (int i = 0; i < realParameterCount; i++) {
                    if (this.charEncoding != null) {
                        buf.append(StringUtils.toString(this.staticSqlStrings[i], this.charEncoding));
                    } else {
                        buf.append(StringUtils.toString(this.staticSqlStrings[i]));
                    }
                    byte[] val = null;
                    if (batchArg != null && batchArg instanceof String) {
                        buf.append((String) batchArg);
                    } else {
                        if (this.batchCommandIndex == -1) {
                            val = this.parameterValues[i];
                        } else {
                            val = ((BatchParams) batchArg).parameterStrings[i];
                        }
                        boolean isStreamParam = false;
                        if (this.batchCommandIndex == -1) {
                            isStreamParam = this.isStream[i];
                        } else {
                            isStreamParam = ((BatchParams) batchArg).isStream[i];
                        }
                        if (val == null && !isStreamParam) {
                            if (quoteStreamsAndUnknowns) buf.append("'");
                            buf.append("** NOT SPECIFIED **");
                            if (quoteStreamsAndUnknowns) buf.append("'");
                        } else if (isStreamParam) {
                            if (quoteStreamsAndUnknowns) buf.append("'");
                            buf.append("** STREAM DATA **");
                            if (quoteStreamsAndUnknowns) buf.append("'");
                        } else if (this.charConverter != null) {
                            buf.append(this.charConverter.toString(val));
                        } else if (this.charEncoding != null) {
                            buf.append(new String(val, this.charEncoding));
                        } else {
                            buf.append(StringUtils.toAsciiString(val));
                        }
                    }
                }
                if (this.charEncoding != null) {
                    buf.append(StringUtils.toString(this.staticSqlStrings[this.parameterCount + getParameterIndexOffset()], this.charEncoding));
                } else {
                    buf.append(StringUtils.toAsciiString(this.staticSqlStrings[this.parameterCount + getParameterIndexOffset()]));
                }
            } catch (UnsupportedEncodingException uue) {
                throw new RuntimeException(Messages.getString("PreparedStatement.32") + this.charEncoding + Messages.getString("PreparedStatement.33"));
            }
            return buf.toString();
        }
    }

    public void clearBatch() throws SQLException {
        synchronized (checkClosed()) {
            this.batchHasPlainStatements = false;
            super.clearBatch();
        }
    }

    public void clearParameters() throws SQLException {
        synchronized (checkClosed()) {
            for (int i = 0; i < this.parameterValues.length; i++) {
                this.parameterValues[i] = null;
                this.parameterStreams[i] = null;
                this.isStream[i] = false;
                this.isNull[i] = false;
                this.parameterTypes[i] = 0;
            }
        }
    }

    private final void escapeblockFast(byte[] buf, Buffer packet, int size) throws SQLException {
        int lastwritten = 0;
        for (int i = 0; i < size; i++) {
            byte b = buf[i];
            if (b == 0) {
                if (i > lastwritten) packet.writeBytesNoNull(buf, lastwritten, i - lastwritten);
                packet.writeByte((byte) 92);
                packet.writeByte((byte) 48);
                lastwritten = i + 1;
            } else if (b == 92 || b == 39 || (!this.usingAnsiMode && b == 34)) {
                if (i > lastwritten) packet.writeBytesNoNull(buf, lastwritten, i - lastwritten);
                packet.writeByte((byte) 92);
                lastwritten = i;
            }
        }
        if (lastwritten < size) packet.writeBytesNoNull(buf, lastwritten, size - lastwritten);
    }

    private final void escapeblockFast(byte[] buf, ByteArrayOutputStream bytesOut, int size) {
        int lastwritten = 0;
        for (int i = 0; i < size; i++) {
            byte b = buf[i];
            if (b == 0) {
                if (i > lastwritten) bytesOut.write(buf, lastwritten, i - lastwritten);
                bytesOut.write(92);
                bytesOut.write(48);
                lastwritten = i + 1;
            } else if (b == 92 || b == 39 || (!this.usingAnsiMode && b == 34)) {
                if (i > lastwritten) bytesOut.write(buf, lastwritten, i - lastwritten);
                bytesOut.write(92);
                lastwritten = i;
            }
        }
        if (lastwritten < size) bytesOut.write(buf, lastwritten, size - lastwritten);
    }

    protected boolean checkReadOnlySafeStatement() throws SQLException {
        synchronized (checkClosed()) {
            return (!this.connection.isReadOnly() || this.firstCharOfStmt == 'S');
        }
    }

    public boolean execute() throws SQLException {
        synchronized (checkClosed()) {
            MySQLConnection locallyScopedConn = this.connection;
            if (!checkReadOnlySafeStatement())
                throw SQLError.createSQLException(Messages.getString("PreparedStatement.20") + Messages.getString("PreparedStatement.21"), "S1009", getExceptionInterceptor());
            ResultSetInternalMethods rs = null;
            CachedResultSetMetaData cachedMetadata = null;
            this.lastQueryIsOnDupKeyUpdate = false;
            if (this.retrieveGeneratedKeys) this.lastQueryIsOnDupKeyUpdate = containsOnDuplicateKeyUpdateInSQL();
            boolean doStreaming = createStreamingResultSet();
            clearWarnings();
            if (doStreaming && this.connection.getNetTimeoutForStreamingResults() > 0)
                executeSimpleNonQuery(locallyScopedConn, "SET net_write_timeout=" + this.connection.getNetTimeoutForStreamingResults());
            this.batchedGeneratedKeys = null;
            Buffer sendPacket = fillSendPacket();
            String oldCatalog = null;
            if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) {
                oldCatalog = locallyScopedConn.getCatalog();
                locallyScopedConn.setCatalog(this.currentCatalog);
            }
            if (locallyScopedConn.getCacheResultSetMetadata())
                cachedMetadata = locallyScopedConn.getCachedMetaData(this.originalSql);
            Field[] metadataFromCache = null;
            if (cachedMetadata != null) metadataFromCache = cachedMetadata.fields;
            boolean oldInfoMsgState = false;
            if (this.retrieveGeneratedKeys) {
                oldInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled();
                locallyScopedConn.setReadInfoMsgEnabled(true);
            }
            if (locallyScopedConn.useMaxRows()) {
                int rowLimit = -1;
                if (this.firstCharOfStmt == 'S') {
                    if (this.hasLimitClause) {
                        rowLimit = this.maxRows;
                    } else if (this.maxRows <= 0) {
                        executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=DEFAULT");
                    } else {
                        executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=" + this.maxRows);
                    }
                } else {
                    executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=DEFAULT");
                }
                rs = executeInternal(rowLimit, sendPacket, doStreaming, (this.firstCharOfStmt == 'S'), metadataFromCache, false);
            } else {
                rs = executeInternal(-1, sendPacket, doStreaming, (this.firstCharOfStmt == 'S'), metadataFromCache, false);
            }
            if (cachedMetadata != null) {
                locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, cachedMetadata, this.results);
            } else if (rs.reallyResult() && locallyScopedConn.getCacheResultSetMetadata()) {
                locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, (CachedResultSetMetaData) null, rs);
            }
            if (this.retrieveGeneratedKeys) {
                locallyScopedConn.setReadInfoMsgEnabled(oldInfoMsgState);
                rs.setFirstCharOfQuery(this.firstCharOfStmt);
            }
            if (oldCatalog != null) locallyScopedConn.setCatalog(oldCatalog);
            if (rs != null) {
                this.lastInsertId = rs.getUpdateID();
                this.results = rs;
            }
            return (rs != null && rs.reallyResult());
        }
    }

    public int[] executeBatch() throws SQLException {
        synchronized (checkClosed()) {
            if (this.connection.isReadOnly())
                throw new SQLException(Messages.getString("PreparedStatement.25") + Messages.getString("PreparedStatement.26"), "S1009");
            if (this.batchedArgs == null || this.batchedArgs.size() == 0) return new int[0];
            int batchTimeout = this.timeoutInMillis;
            this.timeoutInMillis = 0;
            resetCancelledState();
            try {
                statementBegins();
                clearWarnings();
                if (!this.batchHasPlainStatements && this.connection.getRewriteBatchedStatements()) {
                    if (canRewriteAsMultiValueInsertAtSqlLevel()) return executeBatchedInserts(batchTimeout);
                    if (this.connection.versionMeetsMinimum(4, 1, 0) && !this.batchHasPlainStatements && this.batchedArgs != null && this.batchedArgs.size() > 3)
                        return executePreparedBatchAsMultiStatement(batchTimeout);
                }
                return executeBatchSerially(batchTimeout);
            } finally {
                this.statementExecuting.set(false);
                clearBatch();
            }
        }
    }

    public boolean canRewriteAsMultiValueInsertAtSqlLevel() throws SQLException {
        return this.parseInfo.canRewriteAsMultiValueInsert;
    }

    protected int getLocationOfOnDuplicateKeyUpdate() throws SQLException {
        return this.parseInfo.locationOfOnDuplicateKeyUpdate;
    }

    protected int[] executePreparedBatchAsMultiStatement(int batchTimeout) throws SQLException {
        synchronized (checkClosed()) {
            if (this.batchedValuesClause == null) this.batchedValuesClause = this.originalSql + ";";
            MySQLConnection locallyScopedConn = this.connection;
            boolean multiQueriesEnabled = locallyScopedConn.getAllowMultiQueries();
            StatementImpl.CancelTask timeoutTask = null;
            try {
                clearWarnings();
                int numBatchedArgs = this.batchedArgs.size();
                if (this.retrieveGeneratedKeys) this.batchedGeneratedKeys = new ArrayList<ResultSetRow>(numBatchedArgs);
                int numValuesPerBatch = computeBatchSize(numBatchedArgs);
                if (numBatchedArgs < numValuesPerBatch) numValuesPerBatch = numBatchedArgs;
                PreparedStatement batchedStatement = null;
                int batchedParamIndex = 1;
                int numberToExecuteAsMultiValue = 0;
                int batchCounter = 0;
                int updateCountCounter = 0;
            } finally {
                if (timeoutTask != null) {
                    timeoutTask.cancel();
                    locallyScopedConn.getCancelTimer().purge();
                }
                resetCancelledState();
                if (!multiQueriesEnabled) locallyScopedConn.getIO().disableMultiQueries();
                clearBatch();
            }
        }
    }

    private String generateMultiStatementForBatch(int numBatches) throws SQLException {
        synchronized (checkClosed()) {
            StringBuffer newStatementSql = new StringBuffer((this.originalSql.length() + 1) * numBatches);
            newStatementSql.append(this.originalSql);
            for (int i = 0; i < numBatches - 1; i++) {
                newStatementSql.append(';');
                newStatementSql.append(this.originalSql);
            }
            return newStatementSql.toString();
        }
    }

    protected int[] executeBatchedInserts(int batchTimeout) throws SQLException {
        synchronized (checkClosed()) {
            String valuesClause = getValuesClause();
            MySQLConnection locallyScopedConn = this.connection;
            if (valuesClause == null) return executeBatchSerially(batchTimeout);
            int numBatchedArgs = this.batchedArgs.size();
            if (this.retrieveGeneratedKeys) this.batchedGeneratedKeys = new ArrayList<ResultSetRow>(numBatchedArgs);
            int numValuesPerBatch = computeBatchSize(numBatchedArgs);
            if (numBatchedArgs < numValuesPerBatch) numValuesPerBatch = numBatchedArgs;
            PreparedStatement batchedStatement = null;
            int batchedParamIndex = 1;
            int updateCountRunningTotal = 0;
            int numberToExecuteAsMultiValue = 0;
            int batchCounter = 0;
            StatementImpl.CancelTask timeoutTask = null;
            SQLException sqlEx = null;
            int[] updateCounts = new int[numBatchedArgs];
            for (int i = 0; i < this.batchedArgs.size(); i++) updateCounts[i] = 1;
            try {
            } finally {
                if (timeoutTask != null) {
                    timeoutTask.cancel();
                    locallyScopedConn.getCancelTimer().purge();
                }
                resetCancelledState();
            }
        }
    }

    public int getRewrittenBatchSize() {
        return this.rewrittenBatchSize;
    }

    protected String getValuesClause() throws SQLException {
        return this.parseInfo.valuesClause;
    }

    protected int computeBatchSize(int numBatchedArgs) throws SQLException {
        synchronized (checkClosed()) {
            long[] combinedValues = computeMaxParameterSetSizeAndBatchSize(numBatchedArgs);
            long maxSizeOfParameterSet = combinedValues[0];
            long sizeOfEntireBatch = combinedValues[1];
            int maxAllowedPacket = this.connection.getMaxAllowedPacket();
            if (sizeOfEntireBatch < (maxAllowedPacket - this.originalSql.length())) return numBatchedArgs;
            return (int) Math.max(1L, (maxAllowedPacket - this.originalSql.length()) / maxSizeOfParameterSet);
        }
    }

    protected long[] computeMaxParameterSetSizeAndBatchSize(int numBatchedArgs) throws SQLException {
        synchronized (checkClosed()) {
            long sizeOfEntireBatch = 0L;
            long maxSizeOfParameterSet = 0L;
            for (int i = 0; i < numBatchedArgs; i++) {
                BatchParams paramArg = (BatchParams) this.batchedArgs.get(i);
                boolean[] isNullBatch = paramArg.isNull;
                boolean[] isStreamBatch = paramArg.isStream;
                long sizeOfParameterSet = 0L;
                for (int j = 0; j < isNullBatch.length; j++) {
                    if (!isNullBatch[j]) {
                        if (isStreamBatch[j]) {
                            int streamLength = paramArg.streamLengths[j];
                            if (streamLength != -1) {
                                sizeOfParameterSet += (streamLength * 2);
                            } else {
                                int paramLength = (paramArg.parameterStrings[j]).length;
                                sizeOfParameterSet += paramLength;
                            }
                        } else {
                            sizeOfParameterSet += (paramArg.parameterStrings[j]).length;
                        }
                    } else {
                        sizeOfParameterSet += 4L;
                    }
                }
                if (getValuesClause() != null) {
                    sizeOfParameterSet += (getValuesClause().length() + 1);
                } else {
                    sizeOfParameterSet += (this.originalSql.length() + 1);
                }
                sizeOfEntireBatch += sizeOfParameterSet;
                if (sizeOfParameterSet > maxSizeOfParameterSet) maxSizeOfParameterSet = sizeOfParameterSet;
            }
            (new long[2])[0] = maxSizeOfParameterSet;
            (new long[2])[1] = sizeOfEntireBatch;
            return new long[2];
        }
    }

    protected int[] executeBatchSerially(int batchTimeout) throws SQLException {
        synchronized (checkClosed()) {
            MySQLConnection locallyScopedConn = this.connection;
            if (locallyScopedConn == null) checkClosed();
            int[] updateCounts = null;
            if (this.batchedArgs != null) {
                int nbrCommands = this.batchedArgs.size();
                updateCounts = new int[nbrCommands];
                for (int i = 0; i < nbrCommands; i++) updateCounts[i] = -3;
                SQLException sqlEx = null;
                StatementImpl.CancelTask timeoutTask = null;
                try {
                    if (locallyScopedConn.getEnableQueryTimeouts() && batchTimeout != 0 && locallyScopedConn.versionMeetsMinimum(5, 0, 0)) {
                        timeoutTask = new StatementImpl.CancelTask(this, this);
                        locallyScopedConn.getCancelTimer().schedule(timeoutTask, batchTimeout);
                    }
                    if (this.retrieveGeneratedKeys)
                        this.batchedGeneratedKeys = new ArrayList<ResultSetRow>(nbrCommands);
                    for (this.batchCommandIndex = 0; this.batchCommandIndex < nbrCommands; this.batchCommandIndex++) {
                        Object arg = this.batchedArgs.get(this.batchCommandIndex);
                        if (arg instanceof String) {
                            updateCounts[this.batchCommandIndex] = executeUpdate((String) arg);
                        } else {
                            BatchParams paramArg = (BatchParams) arg;
                            try {
                                updateCounts[this.batchCommandIndex] = executeUpdate(paramArg.parameterStrings, paramArg.parameterStreams, paramArg.isStream, paramArg.streamLengths, paramArg.isNull, true);
                                if (this.retrieveGeneratedKeys) ResultSet rs = null;
                            } catch (SQLException ex) {
                                updateCounts[this.batchCommandIndex] = -3;
                                if (this.continueBatchOnError && !(ex instanceof MySQLTimeoutException) && !(ex instanceof MySQLStatementCancelledException) && !hasDeadlockOrTimeoutRolledBackTx(ex)) {
                                    sqlEx = ex;
                                } else {
                                    int[] newUpdateCounts = new int[this.batchCommandIndex];
                                    System.arraycopy(updateCounts, 0, newUpdateCounts, 0, this.batchCommandIndex);
                                    SQLException batchUpdateException = new BatchUpdateException(ex.getMessage(), ex.getSQLState(), ex.getErrorCode(), newUpdateCounts);
                                    batchUpdateException.initCause(ex);
                                    throw batchUpdateException;
                                }
                            }
                        }
                    }
                    if (sqlEx != null) {
                        SQLException batchUpdateException = new BatchUpdateException(sqlEx.getMessage(), sqlEx.getSQLState(), sqlEx.getErrorCode(), updateCounts);
                        batchUpdateException.initCause(sqlEx);
                        throw batchUpdateException;
                    }
                } catch (NullPointerException npe) {
                    try {
                        checkClosed();
                    } catch (SQLException connectionClosedEx) {
                        updateCounts[this.batchCommandIndex] = -3;
                        int[] newUpdateCounts = new int[this.batchCommandIndex];
                        System.arraycopy(updateCounts, 0, newUpdateCounts, 0, this.batchCommandIndex);
                        throw new BatchUpdateException(connectionClosedEx.getMessage(), connectionClosedEx.getSQLState(), connectionClosedEx.getErrorCode(), newUpdateCounts);
                    }
                    throw npe;
                } finally {
                    this.batchCommandIndex = -1;
                    if (timeoutTask != null) {
                        timeoutTask.cancel();
                        locallyScopedConn.getCancelTimer().purge();
                    }
                    resetCancelledState();
                }
            }
            return (updateCounts != null) ? updateCounts : new int[0];
        }
    }

    public String getDateTime(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date());
    }

    protected ResultSetInternalMethods executeInternal(int maxRowsToRetrieve, Buffer sendPacket, boolean createStreamingResultSet, boolean queryIsSelectOnly, Field[] metadataFromCache, boolean isBatch) throws SQLException {
        synchronized (checkClosed()) {
            ResultSetInternalMethods rs;
            resetCancelledState();
            MySQLConnection locallyScopedConnection = this.connection;
            this.numberOfExecutions++;
            if (this.doPingInstead) {
                doPingInstead();
                return this.results;
            }
            StatementImpl.CancelTask timeoutTask = null;
            try {
                if (locallyScopedConnection.getEnableQueryTimeouts() && this.timeoutInMillis != 0 && locallyScopedConnection.versionMeetsMinimum(5, 0, 0)) {
                    timeoutTask = new StatementImpl.CancelTask(this, this);
                    locallyScopedConnection.getCancelTimer().schedule(timeoutTask, this.timeoutInMillis);
                }
                if (!isBatch) statementBegins();
                rs = locallyScopedConnection.execSQL(this, (String) null, maxRowsToRetrieve, sendPacket, this.resultSetType, this.resultSetConcurrency, createStreamingResultSet, this.currentCatalog, metadataFromCache, isBatch);
                if (timeoutTask != null) {
                    timeoutTask.cancel();
                    locallyScopedConnection.getCancelTimer().purge();
                    if (timeoutTask.caughtWhileCancelling != null) throw timeoutTask.caughtWhileCancelling;
                    timeoutTask = null;
                }
                synchronized (this.cancelTimeoutMutex) {
                    if (this.wasCancelled) {
                        MySQLStatementCancelledException mySQLStatementCancelledException;
                        SQLException cause = null;
                        if (this.wasCancelledByTimeout) {
                            MySQLTimeoutException mySQLTimeoutException = new MySQLTimeoutException();
                        } else {
                            mySQLStatementCancelledException = new MySQLStatementCancelledException();
                        }
                        resetCancelledState();
                        throw mySQLStatementCancelledException;
                    }
                }
            } finally {
                if (!isBatch) this.statementExecuting.set(false);
                if (timeoutTask != null) {
                    timeoutTask.cancel();
                    locallyScopedConnection.getCancelTimer().purge();
                }
            }
            return rs;
        }
    }

    public ResultSet executeQuery() throws SQLException {
        synchronized (checkClosed()) {
            MySQLConnection locallyScopedConn = this.connection;
            checkForDml(this.originalSql, this.firstCharOfStmt);
            CachedResultSetMetaData cachedMetadata = null;
            clearWarnings();
            boolean doStreaming = createStreamingResultSet();
            this.batchedGeneratedKeys = null;
            if (doStreaming && this.connection.getNetTimeoutForStreamingResults() > 0) {
                Statement stmt = null;
                try {
                    stmt = this.connection.createStatement();
                    ((StatementImpl) stmt).executeSimpleNonQuery(this.connection, "SET net_write_timeout=" + this.connection.getNetTimeoutForStreamingResults());
                } finally {
                    if (stmt != null) stmt.close();
                }
            }
            Buffer sendPacket = fillSendPacket();
            if (!this.connection.getHoldResultsOpenOverStatementClose() && !this.holdResultsOpenOverClose) {
                if (this.results != null) this.results.realClose(false);
                if (this.generatedKeysResults != null) this.generatedKeysResults.realClose(false);
                closeAllOpenResults();
            }
            String oldCatalog = null;
            if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) {
                oldCatalog = locallyScopedConn.getCatalog();
                locallyScopedConn.setCatalog(this.currentCatalog);
            }
            if (locallyScopedConn.getCacheResultSetMetadata())
                cachedMetadata = locallyScopedConn.getCachedMetaData(this.originalSql);
            Field[] metadataFromCache = null;
            if (cachedMetadata != null) metadataFromCache = cachedMetadata.fields;
            if (locallyScopedConn.useMaxRows()) {
                if (this.hasLimitClause) {
                    this.results = executeInternal(this.maxRows, sendPacket, createStreamingResultSet(), true, metadataFromCache, false);
                } else {
                    if (this.maxRows <= 0) {
                        executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=DEFAULT");
                    } else {
                        executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=" + this.maxRows);
                    }
                    this.results = executeInternal(-1, sendPacket, doStreaming, true, metadataFromCache, false);
                    if (oldCatalog != null) this.connection.setCatalog(oldCatalog);
                }
            } else {
                this.results = executeInternal(-1, sendPacket, doStreaming, true, metadataFromCache, false);
            }
            if (oldCatalog != null) locallyScopedConn.setCatalog(oldCatalog);
            if (cachedMetadata != null) {
                locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, cachedMetadata, this.results);
            } else if (locallyScopedConn.getCacheResultSetMetadata()) {
                locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, (CachedResultSetMetaData) null, this.results);
            }
            this.lastInsertId = this.results.getUpdateID();
            return this.results;
        }
    }

    public int executeUpdate() throws SQLException {
        return executeUpdate(true, false);
    }

    protected int executeUpdate(boolean clearBatchedGeneratedKeysAndWarnings, boolean isBatch) throws SQLException {
        synchronized (checkClosed()) {
            if (clearBatchedGeneratedKeysAndWarnings) {
                clearWarnings();
                this.batchedGeneratedKeys = null;
            }
            return executeUpdate(this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths, this.isNull, isBatch);
        }
    }

    protected int executeUpdate(byte[][] batchedParameterStrings, InputStream[] batchedParameterStreams, boolean[] batchedIsStream, int[] batchedStreamLengths, boolean[] batchedIsNull, boolean isReallyBatch) throws SQLException {
        synchronized (checkClosed()) {
            MySQLConnection locallyScopedConn = this.connection;
            if (locallyScopedConn.isReadOnly())
                throw SQLError.createSQLException(Messages.getString("PreparedStatement.34") + Messages.getString("PreparedStatement.35"), "S1009", getExceptionInterceptor());
            if (this.firstCharOfStmt == 'S' && isSelectQuery())
                throw SQLError.createSQLException(Messages.getString("PreparedStatement.37"), "01S03", getExceptionInterceptor());
            if (!locallyScopedConn.getHoldResultsOpenOverStatementClose()) {
                if (this.results != null) this.results.realClose(false);
                if (this.generatedKeysResults != null) this.generatedKeysResults.realClose(false);
                closeAllOpenResults();
            }
            ResultSetInternalMethods rs = null;
            Buffer sendPacket = fillSendPacket(batchedParameterStrings, batchedParameterStreams, batchedIsStream, batchedStreamLengths);
            String oldCatalog = null;
            if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) {
                oldCatalog = locallyScopedConn.getCatalog();
                locallyScopedConn.setCatalog(this.currentCatalog);
            }
            if (locallyScopedConn.useMaxRows())
                executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=DEFAULT");
            boolean oldInfoMsgState = false;
            if (this.retrieveGeneratedKeys) {
                oldInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled();
                locallyScopedConn.setReadInfoMsgEnabled(true);
            }
            rs = executeInternal(-1, sendPacket, false, false, (Field[]) null, isReallyBatch);
            if (this.retrieveGeneratedKeys) {
                locallyScopedConn.setReadInfoMsgEnabled(oldInfoMsgState);
                rs.setFirstCharOfQuery(this.firstCharOfStmt);
            }
            if (oldCatalog != null) locallyScopedConn.setCatalog(oldCatalog);
            this.results = rs;
            this.updateCount = rs.getUpdateCount();
            if (containsOnDuplicateKeyUpdateInSQL() && this.compensateForOnDuplicateKeyUpdate)
                if (this.updateCount == 2L || this.updateCount == 0L) this.updateCount = 1L;
            int truncatedUpdateCount = 0;
            if (this.updateCount > 2147483647L) {
                truncatedUpdateCount = Integer.MAX_VALUE;
            } else {
                truncatedUpdateCount = (int) this.updateCount;
            }
            this.lastInsertId = rs.getUpdateID();
            return truncatedUpdateCount;
        }
    }

    protected boolean containsOnDuplicateKeyUpdateInSQL() {
        return this.parseInfo.isOnDuplicateKeyUpdate;
    }

    protected Buffer fillSendPacket() throws SQLException {
        synchronized (checkClosed()) {
            return fillSendPacket(this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths);
        }
    }

    protected Buffer fillSendPacket(byte[][] batchedParameterStrings, InputStream[] batchedParameterStreams, boolean[] batchedIsStream, int[] batchedStreamLengths) throws SQLException {
        synchronized (checkClosed()) {
            Buffer sendPacket = this.connection.getIO().getSharedSendPacket();
            sendPacket.clear();
            sendPacket.writeByte((byte) 3);
            boolean useStreamLengths = this.connection.getUseStreamLengthsInPrepStmts();
            int ensurePacketSize = 0;
            String statementComment = this.connection.getStatementComment();
            byte[] commentAsBytes = null;
            if (statementComment != null) {
                if (this.charConverter != null) {
                    commentAsBytes = this.charConverter.toBytes(statementComment);
                } else {
                    commentAsBytes = StringUtils.getBytes(statementComment, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
                }
                ensurePacketSize += commentAsBytes.length;
                ensurePacketSize += 6;
            }
            int i;
            for (i = 0; i < batchedParameterStrings.length; i++) {
                if (batchedIsStream[i] && useStreamLengths) ensurePacketSize += batchedStreamLengths[i];
            }
            if (ensurePacketSize != 0) sendPacket.ensureCapacity(ensurePacketSize);
            if (commentAsBytes != null) {
                sendPacket.writeBytesNoNull(Constants.SLASH_STAR_SPACE_AS_BYTES);
                sendPacket.writeBytesNoNull(commentAsBytes);
                sendPacket.writeBytesNoNull(Constants.SPACE_STAR_SLASH_SPACE_AS_BYTES);
            }
            for (i = 0; i < batchedParameterStrings.length; i++) {
                checkAllParametersSet(batchedParameterStrings[i], batchedParameterStreams[i], i);
                sendPacket.writeBytesNoNull(this.staticSqlStrings[i]);
                if (batchedIsStream[i]) {
                    streamToBytes(sendPacket, batchedParameterStreams[i], true, batchedStreamLengths[i], useStreamLengths);
                } else {
                    sendPacket.writeBytesNoNull(batchedParameterStrings[i]);
                }
            }
            sendPacket.writeBytesNoNull(this.staticSqlStrings[batchedParameterStrings.length]);
            return sendPacket;
        }
    }

    private void checkAllParametersSet(byte[] parameterString, InputStream parameterStream, int columnIndex) throws SQLException {
        if (parameterString == null && parameterStream == null)
            throw SQLError.createSQLException(Messages.getString("PreparedStatement.40") + (columnIndex + 1), "07001", getExceptionInterceptor());
    }

    protected PreparedStatement prepareBatchedInsertSQL(MySQLConnection localConn, int numBatches) throws SQLException {
        synchronized (checkClosed()) {
            PreparedStatement pstmt = new PreparedStatement(localConn, "Rewritten batch of: " + this.originalSql, this.currentCatalog, this.parseInfo.getParseInfoForBatch(numBatches));
            pstmt.setRetrieveGeneratedKeys(this.retrieveGeneratedKeys);
            pstmt.rewrittenBatchSize = numBatches;
            return pstmt;
        }
    }

    protected void setRetrieveGeneratedKeys(boolean flag) throws SQLException {
        synchronized (checkClosed()) {
            this.retrieveGeneratedKeys = flag;
        }
    }

    public String getNonRewrittenSql() throws SQLException {
        synchronized (checkClosed()) {
            int indexOfBatch = this.originalSql.indexOf(" of: ");

            if (indexOfBatch != -1) {
                return this.originalSql.substring(indexOfBatch + 5);
            }

            return this.originalSql;
        }
    }

    public byte[] getBytesRepresentation(int parameterIndex) throws SQLException {
        synchronized (checkClosed()) {
            if (this.isStream[parameterIndex]) {
                return streamToBytes(this.parameterStreams[parameterIndex], false, this.streamLengths[parameterIndex], this.connection.getUseStreamLengthsInPrepStmts());
            }

            byte[] parameterVal = this.parameterValues[parameterIndex];

            if (parameterVal == null) {
                return null;
            }

            if (parameterVal[0] == 39 && parameterVal[parameterVal.length - 1] == 39) {

                byte[] valNoQuotes = new byte[parameterVal.length - 2];
                System.arraycopy(parameterVal, 1, valNoQuotes, 0, parameterVal.length - 2);

                return valNoQuotes;
            }

            return parameterVal;
        }
    }

    protected byte[] getBytesRepresentationForBatch(int parameterIndex, int commandIndex) throws SQLException {
        synchronized (checkClosed()) {
            Object batchedArg = this.batchedArgs.get(commandIndex);
            if (batchedArg instanceof String) {
                try {
                    return StringUtils.getBytes((String) batchedArg, this.charEncoding);
                } catch (UnsupportedEncodingException uue) {
                    throw new RuntimeException(Messages.getString("PreparedStatement.32") + this.charEncoding + Messages.getString("PreparedStatement.33"));
                }
            }

            BatchParams params = (BatchParams) batchedArg;
            if (params.isStream[parameterIndex]) {
                return streamToBytes(params.parameterStreams[parameterIndex], false, params.streamLengths[parameterIndex], this.connection.getUseStreamLengthsInPrepStmts());
            }

            byte[] parameterVal = params.parameterStrings[parameterIndex];
            if (parameterVal == null) {
                return null;
            }
            if (parameterVal[0] == 39 && parameterVal[parameterVal.length - 1] == 39) {

                byte[] valNoQuotes = new byte[parameterVal.length - 2];
                System.arraycopy(parameterVal, 1, valNoQuotes, 0, parameterVal.length - 2);

                return valNoQuotes;
            }

            return parameterVal;
        }
    }

    private final String getDateTimePattern(String dt, boolean toTime) throws Exception {
        int dtLength = (dt != null) ? dt.length() : 0;

        if (dtLength >= 8 && dtLength <= 10) {
            int dashCount = 0;
            boolean isDateOnly = true;

            for (int k = 0; k < dtLength; k++) {
                char c = dt.charAt(k);

                if (!Character.isDigit(c) && c != '-') {
                    isDateOnly = false;

                    break;
                }

                if (c == '-') {
                    dashCount++;
                }
            }

            if (isDateOnly && dashCount == 2) {
                return "yyyy-MM-dd";
            }
        }

        boolean colonsOnly = true;

        for (int i = 0; i < dtLength; i++) {
            char c = dt.charAt(i);

            if (!Character.isDigit(c) && c != ':') {
                colonsOnly = false;

                break;
            }
        }

        if (colonsOnly) {
            return "HH:mm:ss";
        }

        StringReader reader = new StringReader(dt + " ");
        ArrayList<Object[]> vec = new ArrayList();
        ArrayList<Object[]> vecRemovelist = new ArrayList();
        Object[] nv = new Object[3];

        nv[0] = Character.valueOf('y');
        nv[1] = new StringBuffer();
        nv[2] = Integer.valueOf(0);
        vec.add(nv);

        if (toTime) {
            nv = new Object[3];
            nv[0] = Character.valueOf('h');
            nv[1] = new StringBuffer();
            nv[2] = Integer.valueOf(0);
            vec.add(nv);
        }
        int z;
        while ((z = reader.read()) != -1) {
            char separator = (char) z;
            int maxvecs = vec.size();

            for (int count = 0; count < maxvecs; count++) {
                Object[] arrayOfObject = vec.get(count);
                int n = ((Integer) arrayOfObject[2]).intValue();
                char c = getSuccessor(((Character) arrayOfObject[0]).charValue(), n);

                if (!Character.isLetterOrDigit(separator)) {
                    if (c == ((Character) arrayOfObject[0]).charValue() && c != 'S') {
                        vecRemovelist.add(arrayOfObject);
                    } else {
                        ((StringBuffer) arrayOfObject[1]).append(separator);

                        if (c == 'X' || c == 'Y') {
                            arrayOfObject[2] = Integer.valueOf(4);
                        }
                    }
                } else {
                    if (c == 'X') {
                        c = 'y';
                        nv = new Object[3];
                        nv[1] = (new StringBuffer(((StringBuffer) arrayOfObject[1]).toString())).append('M');

                        nv[0] = Character.valueOf('M');
                        nv[2] = Integer.valueOf(1);
                        vec.add(nv);
                    } else if (c == 'Y') {
                        c = 'M';
                        nv = new Object[3];
                        nv[1] = (new StringBuffer(((StringBuffer) arrayOfObject[1]).toString())).append('d');

                        nv[0] = Character.valueOf('d');
                        nv[2] = Integer.valueOf(1);
                        vec.add(nv);
                    }

                    ((StringBuffer) arrayOfObject[1]).append(c);

                    if (c == ((Character) arrayOfObject[0]).charValue()) {
                        arrayOfObject[2] = Integer.valueOf(n + 1);
                    } else {
                        arrayOfObject[0] = Character.valueOf(c);
                        arrayOfObject[2] = Integer.valueOf(1);
                    }
                }
            }

            int k = vecRemovelist.size();

            for (int m = 0; m < k; m++) {
                Object[] arrayOfObject = vecRemovelist.get(m);
                vec.remove(arrayOfObject);
            }

            vecRemovelist.clear();
        }

        int size = vec.size();
        int j;
        for (j = 0; j < size; j++) {
            Object[] arrayOfObject = vec.get(j);
            char c = ((Character) arrayOfObject[0]).charValue();
            int n = ((Integer) arrayOfObject[2]).intValue();

            boolean bk = (getSuccessor(c, n) != c);
            boolean atEnd = ((c == 's' || c == 'm' || (c == 'h' && toTime)) && bk);
            boolean finishesAtDate = (bk && c == 'd' && !toTime);
            boolean containsEnd = (((StringBuffer) arrayOfObject[1]).toString().indexOf('W') != -1);

            if ((!atEnd && !finishesAtDate) || containsEnd) {
                vecRemovelist.add(arrayOfObject);
            }
        }

        size = vecRemovelist.size();

        for (j = 0; j < size; j++) {
            vec.remove(vecRemovelist.get(j));
        }

        vecRemovelist.clear();
        Object[] v = vec.get(0);

        StringBuffer format = (StringBuffer) v[1];
        format.setLength(format.length() - 1);

        return format.toString();
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        synchronized (checkClosed()) {

            if (!isSelectQuery()) {
                return null;
            }

            PreparedStatement mdStmt = null;
            ResultSet mdRs = null;

            if (this.pstmtResultMetaData == null) {
                try {
                    mdStmt = new PreparedStatement(this.connection, this.originalSql, this.currentCatalog, this.parseInfo);

                    mdStmt.setMaxRows(1);

                    int paramCount = this.parameterValues.length;

                    for (int i = 1; i <= paramCount; i++) {
                        mdStmt.setString(i, "");
                    }

                    boolean hadResults = mdStmt.execute();

                    if (hadResults) {
                        mdRs = mdStmt.getResultSet();

                        this.pstmtResultMetaData = mdRs.getMetaData();
                    } else {
                        this.pstmtResultMetaData = new ResultSetMetaData(new Field[0], this.connection.getUseOldAliasMetadataBehavior(), getExceptionInterceptor());
                    }

                } finally {

                    SQLException sqlExRethrow = null;

                    if (mdRs != null) {
                        try {
                            mdRs.close();
                        } catch (SQLException sqlEx) {
                            sqlExRethrow = sqlEx;
                        }

                        mdRs = null;
                    }

                    if (mdStmt != null) {
                        try {
                            mdStmt.close();
                        } catch (SQLException sqlEx) {
                            sqlExRethrow = sqlEx;
                        }

                        mdStmt = null;
                    }

                    if (sqlExRethrow != null) {
                        throw sqlExRethrow;
                    }
                }
            }

            return this.pstmtResultMetaData;
        }
    }

    protected boolean isSelectQuery() throws SQLException {
        synchronized (checkClosed()) {
            return StringUtils.startsWithIgnoreCaseAndWs(StringUtils.stripComments(this.originalSql, "'\"", "'\"", true, false, true, true), "SELECT");
        }
    }

    public ParameterMetaData getParameterMetaData() throws SQLException {
        synchronized (checkClosed()) {
            if (this.parameterMetaData == null) {
                if (this.connection.getGenerateSimpleParameterMetadata()) {
                    this.parameterMetaData = new MysqlParameterMetadata(this.parameterCount);
                } else {
                    this.parameterMetaData = new MysqlParameterMetadata(null, this.parameterCount, getExceptionInterceptor());
                }
            }

            return this.parameterMetaData;
        }
    }

    ParseInfo getParseInfo() {
        return this.parseInfo;
    }

    private final char getSuccessor(char c, int n) {
        return (c == 'y' && n == 2) ? 'X' : ((c == 'y' && n < 4) ? 'y' : ((c == 'y') ? 'M' : ((c == 'M' && n == 2) ? 'Y' : ((c == 'M' && n < 3) ? 'M' : ((c == 'M') ? 'd' : ((c == 'd' && n < 2) ? 'd' : ((c == 'd') ? 'H' : ((c == 'H' && n < 2) ? 'H' : ((c == 'H') ? 'm' : ((c == 'm' && n < 2) ? 'm' : ((c == 'm') ? 's' : ((c == 's' && n < 2) ? 's' : 'W'))))))))))));
    }

    private final void hexEscapeBlock(byte[] buf, Buffer packet, int size) throws SQLException {
        for (int i = 0; i < size; i++) {
            byte b = buf[i];
            int lowBits = (b & 0xFF) / 16;
            int highBits = (b & 0xFF) % 16;

            packet.writeByte(HEX_DIGITS[lowBits]);
            packet.writeByte(HEX_DIGITS[highBits]);
        }
    }

    private void initializeFromParseInfo() throws SQLException {
        synchronized (checkClosed()) {
            this.staticSqlStrings = this.parseInfo.staticSql;
            this.hasLimitClause = this.parseInfo.foundLimitClause;
            this.isLoadDataQuery = this.parseInfo.foundLoadData;
            this.firstCharOfStmt = this.parseInfo.firstStmtChar;

            this.parameterCount = this.staticSqlStrings.length - 1;

            this.parameterValues = new byte[this.parameterCount][];
            this.parameterStreams = new InputStream[this.parameterCount];
            this.isStream = new boolean[this.parameterCount];
            this.streamLengths = new int[this.parameterCount];
            this.isNull = new boolean[this.parameterCount];
            this.parameterTypes = new int[this.parameterCount];

            clearParameters();

            for (int j = 0; j < this.parameterCount; j++) {
                this.isStream[j] = false;
            }
        }
    }

    boolean isNull(int paramIndex) throws SQLException {
        synchronized (checkClosed()) {
            return this.isNull[paramIndex];
        }
    }

    private final int readblock(InputStream i, byte[] b) throws SQLException {
        try {
            return i.read(b);
        } catch (Throwable ex) {
            SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.56") + ex.getClass().getName(), "S1000", getExceptionInterceptor());

            sqlEx.initCause(ex);

            throw sqlEx;
        }
    }

    private final int readblock(InputStream i, byte[] b, int length) throws SQLException {
        try {
            int lengthToRead = length;

            if (lengthToRead > b.length) {
                lengthToRead = b.length;
            }

            return i.read(b, 0, lengthToRead);
        } catch (Throwable ex) {
            SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.56") + ex.getClass().getName(), "S1000", getExceptionInterceptor());

            sqlEx.initCause(ex);

            throw sqlEx;
        }
    }

    protected void realClose(boolean calledExplicitly, boolean closeOpenResults) throws SQLException {
        MySQLConnection locallyScopedConn;
        try {
            locallyScopedConn = checkClosed();
        } catch (SQLException sqlEx) {
            return;
        }

        synchronized (locallyScopedConn) {

            if (this.useUsageAdvisor &&
                    this.numberOfExecutions <= 1) {
                String message = Messages.getString("PreparedStatement.43");

                this.eventSink.consumeEvent(new ProfilerEvent((byte) 0, "", this.currentCatalog, this.connectionId, getId(), -1, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, message));
            }

            super.realClose(calledExplicitly, closeOpenResults);

            this.dbmd = null;
            this.originalSql = null;
            this.staticSqlStrings = (byte[][]) null;
            this.parameterValues = (byte[][]) null;
            this.parameterStreams = null;
            this.isStream = null;
            this.streamLengths = null;
            this.isNull = null;
            this.streamConvertBuf = null;
            this.parameterTypes = null;
        }
    }

    public void setArray(int i, Array x) throws SQLException {
        throw SQLError.notImplemented();
    }

    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        if (x == null) {
            setNull(parameterIndex, 12);
        } else {
            setBinaryStream(parameterIndex, x, length);
        }
    }

    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        if (x == null) {
            setNull(parameterIndex, 3);
        } else {
            setInternal(parameterIndex, StringUtils.fixDecimalExponent(StringUtils.consistentToString(x)));

            this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 3;
        }
    }

    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        synchronized (checkClosed()) {
            if (x == null) {
                setNull(parameterIndex, -2);
            } else {
                int parameterIndexOffset = getParameterIndexOffset();

                if (parameterIndex < 1 || parameterIndex > this.staticSqlStrings.length) {
                    throw SQLError.createSQLException(Messages.getString("PreparedStatement.2") + parameterIndex + Messages.getString("PreparedStatement.3") + this.staticSqlStrings.length + Messages.getString("PreparedStatement.4"), "S1009", getExceptionInterceptor());
                }

                if (parameterIndexOffset == -1 && parameterIndex == 1) {
                    throw SQLError.createSQLException("Can't set IN parameter for return value of stored function call.", "S1009", getExceptionInterceptor());
                }

                this.parameterStreams[parameterIndex - 1 + parameterIndexOffset] = x;
                this.isStream[parameterIndex - 1 + parameterIndexOffset] = true;
                this.streamLengths[parameterIndex - 1 + parameterIndexOffset] = length;
                this.isNull[parameterIndex - 1 + parameterIndexOffset] = false;
                this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 2004;
            }
        }
    }

    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        setBinaryStream(parameterIndex, inputStream, (int) length);
    }

    public void setBlob(int i, Blob x) throws SQLException {
        if (x == null) {
            setNull(i, 2004);
        } else {
            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();

            bytesOut.write(39);
            escapeblockFast(x.getBytes(1L, (int) x.length()), bytesOut, (int) x.length());

            bytesOut.write(39);

            setInternal(i, bytesOut.toByteArray());

            this.parameterTypes[i - 1 + getParameterIndexOffset()] = 2004;
        }
    }

    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        if (this.useTrueBoolean) {
            setInternal(parameterIndex, x ? "1" : "0");
        } else {
            setInternal(parameterIndex, x ? "'t'" : "'f'");

            this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 16;
        }
    }

    public void setByte(int parameterIndex, byte x) throws SQLException {
        setInternal(parameterIndex, String.valueOf(x));

        this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = -6;
    }

    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        setBytes(parameterIndex, x, true, true);

        if (x != null) {
            this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = -2;
        }
    }

    protected void setBytes(int parameterIndex, byte[] x, boolean checkForIntroducer, boolean escapeForMBChars) throws SQLException {
        synchronized (checkClosed()) {
            if (x == null) {
                setNull(parameterIndex, -2);
            } else {
                String connectionEncoding = this.connection.getEncoding();

                try {
                    if (this.connection.isNoBackslashEscapesSet() || (escapeForMBChars && this.connection.getUseUnicode() && connectionEncoding != null && CharsetMapping.isMultibyteCharset(connectionEncoding))) {

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(x.length * 2 + 3);

                        byteArrayOutputStream.write(120);
                        byteArrayOutputStream.write(39);

                        for (int j = 0; j < x.length; j++) {
                            int lowBits = (x[j] & 0xFF) / 16;
                            int highBits = (x[j] & 0xFF) % 16;

                            byteArrayOutputStream.write(HEX_DIGITS[lowBits]);
                            byteArrayOutputStream.write(HEX_DIGITS[highBits]);
                        }

                        byteArrayOutputStream.write(39);

                        setInternal(parameterIndex, byteArrayOutputStream.toByteArray());

                        return;
                    }
                } catch (SQLException ex) {
                    throw ex;
                } catch (RuntimeException ex) {
                    SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", (ExceptionInterceptor) null);
                    sqlEx.initCause(ex);
                    throw sqlEx;
                }

                int numBytes = x.length;

                int pad = 2;

                boolean needsIntroducer = (checkForIntroducer && this.connection.versionMeetsMinimum(4, 1, 0));

                if (needsIntroducer) {
                    pad += 7;
                }

                ByteArrayOutputStream bOut = new ByteArrayOutputStream(numBytes + pad);

                if (needsIntroducer) {
                    bOut.write(95);
                    bOut.write(98);
                    bOut.write(105);
                    bOut.write(110);
                    bOut.write(97);
                    bOut.write(114);
                    bOut.write(121);
                }
                bOut.write(39);

                for (int i = 0; i < numBytes; i++) {
                    byte b = x[i];

                    switch (b) {
                        case 0:
                            bOut.write(92);
                            bOut.write(48);
                            break;

                        case 10:
                            bOut.write(92);
                            bOut.write(110);
                            break;

                        case 13:
                            bOut.write(92);
                            bOut.write(114);
                            break;

                        case 92:
                            bOut.write(92);
                            bOut.write(92);
                            break;

                        case 39:
                            bOut.write(92);
                            bOut.write(39);
                            break;

                        case 34:
                            bOut.write(92);
                            bOut.write(34);
                            break;

                        case 26:
                            bOut.write(92);
                            bOut.write(90);
                            break;

                        default:
                            bOut.write(b);
                            break;
                    }
                }
                bOut.write(39);

                setInternal(parameterIndex, bOut.toByteArray());
            }
        }
    }

    protected void setBytesNoEscape(int parameterIndex, byte[] parameterAsBytes) throws SQLException {
        byte[] parameterWithQuotes = new byte[parameterAsBytes.length + 2];
        parameterWithQuotes[0] = 39;
        System.arraycopy(parameterAsBytes, 0, parameterWithQuotes, 1, parameterAsBytes.length);

        parameterWithQuotes[parameterAsBytes.length + 1] = 39;

        setInternal(parameterIndex, parameterWithQuotes);
    }

    protected void setBytesNoEscapeNoQuotes(int parameterIndex, byte[] parameterAsBytes) throws SQLException {
        setInternal(parameterIndex, parameterAsBytes);
    }

    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        synchronized (checkClosed()) {
            try {
                if (reader == null) {
                    setNull(parameterIndex, -1);
                } else {
                    char[] c = null;
                    int len = 0;

                    boolean useLength = this.connection.getUseStreamLengthsInPrepStmts();

                    String forcedEncoding = this.connection.getClobCharacterEncoding();

                    if (useLength && length != -1) {
                        c = new char[length];

                        int numCharsRead = readFully(reader, c, length);

                        if (forcedEncoding == null) {
                            setString(parameterIndex, new String(c, 0, numCharsRead));
                        } else {
                            try {
                                setBytes(parameterIndex, StringUtils.getBytes(new String(c, 0, numCharsRead), forcedEncoding));

                            } catch (UnsupportedEncodingException uee) {
                                throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009", getExceptionInterceptor());
                            }
                        }
                    } else {

                        c = new char[4096];

                        StringBuffer buf = new StringBuffer();

                        while ((len = reader.read(c)) != -1) {
                            buf.append(c, 0, len);
                        }

                        if (forcedEncoding == null) {
                            setString(parameterIndex, buf.toString());
                        } else {
                            try {
                                setBytes(parameterIndex, StringUtils.getBytes(buf.toString(), forcedEncoding));
                            } catch (UnsupportedEncodingException uee) {
                                throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009", getExceptionInterceptor());
                            }
                        }
                    }

                    this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 2005;
                }
            } catch (IOException ioEx) {
                throw SQLError.createSQLException(ioEx.toString(), "S1000", getExceptionInterceptor());
            }
        }
    }

    public void setClob(int i, Clob x) throws SQLException {
        synchronized (checkClosed()) {
            if (x == null) {
                setNull(i, 2005);
            } else {

                String forcedEncoding = this.connection.getClobCharacterEncoding();

                if (forcedEncoding == null) {
                    setString(i, x.getSubString(1L, (int) x.length()));
                } else {
                    try {
                        setBytes(i, StringUtils.getBytes(x.getSubString(1L, (int) x.length()), forcedEncoding));
                    } catch (UnsupportedEncodingException uee) {
                        throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009", getExceptionInterceptor());
                    }
                }

                this.parameterTypes[i - 1 + getParameterIndexOffset()] = 2005;
            }
        }
    }

    public void setDate(int parameterIndex, Date x) throws SQLException {
        setDate(parameterIndex, x, (Calendar) null);
    }

    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        if (x == null) {
            setNull(parameterIndex, 91);
        } else {
            checkClosed();

            if (!this.useLegacyDatetimeCode) {
                newSetDateInternal(parameterIndex, x, cal);
            } else {

                SimpleDateFormat dateFormatter = new SimpleDateFormat("''yyyy-MM-dd''", Locale.US);

                setInternal(parameterIndex, dateFormatter.format(x));

                this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 91;
            }
        }
    }

    public void setDouble(int parameterIndex, double x) throws SQLException {
        synchronized (checkClosed()) {
            if (!this.connection.getAllowNanAndInf() && (x == Double.POSITIVE_INFINITY || x == Double.NEGATIVE_INFINITY || Double.isNaN(x))) {

                throw SQLError.createSQLException("'" + x + "' is not a valid numeric or approximate numeric value", "S1009", getExceptionInterceptor());
            }

            setInternal(parameterIndex, StringUtils.fixDecimalExponent(String.valueOf(x)));

            this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 8;
        }
    }

    public void setFloat(int parameterIndex, float x) throws SQLException {
        setInternal(parameterIndex, StringUtils.fixDecimalExponent(String.valueOf(x)));

        this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 6;
    }

    public void setInt(int parameterIndex, int x) throws SQLException {
        setInternal(parameterIndex, String.valueOf(x));

        this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 4;
    }

    protected final void setInternal(int paramIndex, byte[] val) throws SQLException {
        synchronized (checkClosed()) {

            int parameterIndexOffset = getParameterIndexOffset();

            checkBounds(paramIndex, parameterIndexOffset);

            this.isStream[paramIndex - 1 + parameterIndexOffset] = false;
            this.isNull[paramIndex - 1 + parameterIndexOffset] = false;
            this.parameterStreams[paramIndex - 1 + parameterIndexOffset] = null;
            this.parameterValues[paramIndex - 1 + parameterIndexOffset] = val;
        }
    }

    protected void checkBounds(int paramIndex, int parameterIndexOffset) throws SQLException {
        synchronized (checkClosed()) {
            if (paramIndex < 1) {
                throw SQLError.createSQLException(Messages.getString("PreparedStatement.49") + paramIndex + Messages.getString("PreparedStatement.50"), "S1009", getExceptionInterceptor());
            }

            if (paramIndex > this.parameterCount) {
                throw SQLError.createSQLException(Messages.getString("PreparedStatement.51") + paramIndex + Messages.getString("PreparedStatement.52") + this.parameterValues.length + Messages.getString("PreparedStatement.53"), "S1009", getExceptionInterceptor());
            }

            if (parameterIndexOffset == -1 && paramIndex == 1) {
                throw SQLError.createSQLException("Can't set IN parameter for return value of stored function call.", "S1009", getExceptionInterceptor());
            }
        }
    }

    protected final void setInternal(int paramIndex, String val) throws SQLException {
        synchronized (checkClosed()) {

            byte[] parameterAsBytes = null;

            if (this.charConverter != null) {
                parameterAsBytes = this.charConverter.toBytes(val);
            } else {
                parameterAsBytes = StringUtils.getBytes(val, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
            }

            setInternal(paramIndex, parameterAsBytes);
        }
    }

    public void setLong(int parameterIndex, long x) throws SQLException {
        setInternal(parameterIndex, String.valueOf(x));

        this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = -5;
    }

    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        synchronized (checkClosed()) {
            setInternal(parameterIndex, "null");
            this.isNull[parameterIndex - 1 + getParameterIndexOffset()] = true;

            this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 0;
        }
    }

    public void setNull(int parameterIndex, int sqlType, String arg) throws SQLException {
        setNull(parameterIndex, sqlType);

        this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 0;
    }

    private void setNumericObject(int parameterIndex, Object parameterObj, int targetSqlType, int scale) throws SQLException {
        Number parameterAsNum;
        if (parameterObj instanceof Boolean) {
            parameterAsNum = ((Boolean) parameterObj).booleanValue() ? Integer.valueOf(1) : Integer.valueOf(0);

        } else if (parameterObj instanceof String) {
            boolean parameterAsBoolean;
            switch (targetSqlType) {
                case -7:
                    if ("1".equals(parameterObj) || "0".equals(parameterObj)) {

                        Number number = Integer.valueOf((String) parameterObj);
                        break;
                    }
                    parameterAsBoolean = "true".equalsIgnoreCase((String) parameterObj);

                    parameterAsNum = parameterAsBoolean ? Integer.valueOf(1) : Integer.valueOf(0);
                    break;

                case -6:
                case 4:
                case 5:
                    parameterAsNum = Integer.valueOf((String) parameterObj);
                    break;

                case -5:
                    parameterAsNum = Long.valueOf((String) parameterObj);
                    break;

                case 7:
                    parameterAsNum = Float.valueOf((String) parameterObj);
                    break;

                case 6:
                case 8:
                    parameterAsNum = Double.valueOf((String) parameterObj);
                    break;

                default:
                    parameterAsNum = new BigDecimal((String) parameterObj);
                    break;
            }
        } else {
            parameterAsNum = (Number) parameterObj;
        }

        switch (targetSqlType) {
            case -7:
            case -6:
            case 4:
            case 5:
                setInt(parameterIndex, parameterAsNum.intValue());
                break;

            case -5:
                setLong(parameterIndex, parameterAsNum.longValue());
                break;

            case 7:
                setFloat(parameterIndex, parameterAsNum.floatValue());
                break;

            case 6:
            case 8:
                setDouble(parameterIndex, parameterAsNum.doubleValue());
                break;

            case 2:
            case 3:
                if (parameterAsNum instanceof BigDecimal) {
                    BigDecimal scaledBigDecimal = null;

                    try {
                        scaledBigDecimal = ((BigDecimal) parameterAsNum).setScale(scale);
                    } catch (ArithmeticException ex) {
                        try {
                            scaledBigDecimal = ((BigDecimal) parameterAsNum).setScale(scale, 4);

                        } catch (ArithmeticException arEx) {
                            throw SQLError.createSQLException("Can't set scale of '" + scale + "' for DECIMAL argument '" + parameterAsNum + "'", "S1009", getExceptionInterceptor());
                        }
                    }

                    setBigDecimal(parameterIndex, scaledBigDecimal);
                    break;
                }
                if (parameterAsNum instanceof BigInteger) {
                    setBigDecimal(parameterIndex, new BigDecimal((BigInteger) parameterAsNum, scale));

                    break;
                }

                setBigDecimal(parameterIndex, new BigDecimal(parameterAsNum.doubleValue()));
                break;
        }
    }

    public void setObject(int parameterIndex, Object parameterObj) throws SQLException {
        synchronized (checkClosed()) {
            if (parameterObj == null) {
                setNull(parameterIndex, 1111);
            } else if (parameterObj instanceof Byte) {
                setInt(parameterIndex, ((Byte) parameterObj).intValue());
            } else if (parameterObj instanceof String) {
                setString(parameterIndex, (String) parameterObj);
            } else if (parameterObj instanceof BigDecimal) {
                setBigDecimal(parameterIndex, (BigDecimal) parameterObj);
            } else if (parameterObj instanceof Short) {
                setShort(parameterIndex, ((Short) parameterObj).shortValue());
            } else if (parameterObj instanceof Integer) {
                setInt(parameterIndex, ((Integer) parameterObj).intValue());
            } else if (parameterObj instanceof Long) {
                setLong(parameterIndex, ((Long) parameterObj).longValue());
            } else if (parameterObj instanceof Float) {
                setFloat(parameterIndex, ((Float) parameterObj).floatValue());
            } else if (parameterObj instanceof Double) {
                setDouble(parameterIndex, ((Double) parameterObj).doubleValue());
            } else if (parameterObj instanceof byte[]) {
                setBytes(parameterIndex, (byte[]) parameterObj);
            } else if (parameterObj instanceof Date) {
                setDate(parameterIndex, (Date) parameterObj);
            } else if (parameterObj instanceof Time) {
                setTime(parameterIndex, (Time) parameterObj);
            } else if (parameterObj instanceof Timestamp) {
                setTimestamp(parameterIndex, (Timestamp) parameterObj);
            } else if (parameterObj instanceof Boolean) {
                setBoolean(parameterIndex, ((Boolean) parameterObj).booleanValue());
            } else if (parameterObj instanceof InputStream) {
                setBinaryStream(parameterIndex, (InputStream) parameterObj, -1);
            } else if (parameterObj instanceof Blob) {
                setBlob(parameterIndex, (Blob) parameterObj);
            } else if (parameterObj instanceof Clob) {
                setClob(parameterIndex, (Clob) parameterObj);
            } else if (this.connection.getTreatUtilDateAsTimestamp() && parameterObj instanceof Date) {

                setTimestamp(parameterIndex, new Timestamp(((Date) parameterObj).getTime()));
            } else if (parameterObj instanceof BigInteger) {
                setString(parameterIndex, parameterObj.toString());
            } else {
                setSerializableObject(parameterIndex, parameterObj);
            }
        }
    }

    public void setObject(int parameterIndex, Object parameterObj, int targetSqlType) throws SQLException {
        if (!(parameterObj instanceof BigDecimal)) {
            setObject(parameterIndex, parameterObj, targetSqlType, 0);
        } else {
            setObject(parameterIndex, parameterObj, targetSqlType, ((BigDecimal) parameterObj).scale());
        }
    }

    public void setObject(int parameterIndex, Object parameterObj, int targetSqlType, int scale) throws SQLException {
        synchronized (checkClosed()) {
            if (parameterObj == null) {
                setNull(parameterIndex, 1111);
            } else {
                try {
                    Date parameterAsDate;
                    switch (targetSqlType) {

                        case 16:
                            if (parameterObj instanceof Boolean) {
                                setBoolean(parameterIndex, ((Boolean) parameterObj).booleanValue());
                                break;
                            }
                            if (parameterObj instanceof String) {
                                setBoolean(parameterIndex, ("true".equalsIgnoreCase((String) parameterObj) || !"0".equalsIgnoreCase((String) parameterObj)));

                                break;
                            }
                            if (parameterObj instanceof Number) {
                                int intValue = ((Number) parameterObj).intValue();

                                setBoolean(parameterIndex, (intValue != 0));

                                break;
                            }
                            throw SQLError.createSQLException("No conversion from " + parameterObj.getClass().getName() + " to Types.BOOLEAN possible.", "S1009", getExceptionInterceptor());

                        case -7:
                        case -6:
                        case -5:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                            setNumericObject(parameterIndex, parameterObj, targetSqlType, scale);
                            break;

                        case -1:
                        case 1:
                        case 12:
                            if (parameterObj instanceof BigDecimal) {
                                setString(parameterIndex, StringUtils.fixDecimalExponent(StringUtils.consistentToString((BigDecimal) parameterObj)));

                                break;
                            }

                            setString(parameterIndex, parameterObj.toString());
                            break;

                        case 2005:
                            if (parameterObj instanceof Clob) {
                                setClob(parameterIndex, (Clob) parameterObj);
                                break;
                            }
                            setString(parameterIndex, parameterObj.toString());
                            break;

                        case -4:
                        case -3:
                        case -2:
                        case 2004:
                            if (parameterObj instanceof byte[]) {
                                setBytes(parameterIndex, (byte[]) parameterObj);
                                break;
                            }
                            if (parameterObj instanceof Blob) {
                                setBlob(parameterIndex, (Blob) parameterObj);
                                break;
                            }
                            setBytes(parameterIndex, StringUtils.getBytes(parameterObj.toString(), this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor()));
                            break;

                        case 91:
                        case 93:
                            if (parameterObj instanceof String) {
                                ParsePosition pp = new ParsePosition(0);
                                DateFormat sdf = new SimpleDateFormat(getDateTimePattern((String) parameterObj, false), Locale.US);

                                parameterAsDate = sdf.parse((String) parameterObj, pp);
                            } else {
                                parameterAsDate = (Date) parameterObj;
                            }

                            switch (targetSqlType) {

                                case 91:
                                    if (parameterAsDate instanceof Date) {
                                        setDate(parameterIndex, (Date) parameterAsDate);
                                        break;
                                    }
                                    setDate(parameterIndex, new Date(parameterAsDate.getTime()));
                                    break;

                                case 93:
                                    if (parameterAsDate instanceof Timestamp) {
                                        setTimestamp(parameterIndex, (Timestamp) parameterAsDate);
                                        break;
                                    }
                                    setTimestamp(parameterIndex, new Timestamp(parameterAsDate.getTime()));
                                    break;
                            }

                            break;

                        case 92:
                            if (parameterObj instanceof String) {
                                DateFormat sdf = new SimpleDateFormat(getDateTimePattern((String) parameterObj, true), Locale.US);

                                setTime(parameterIndex, new Time(sdf.parse((String) parameterObj).getTime()));
                                break;
                            }
                            if (parameterObj instanceof Timestamp) {
                                Timestamp xT = (Timestamp) parameterObj;
                                setTime(parameterIndex, new Time(xT.getTime()));
                                break;
                            }
                            setTime(parameterIndex, (Time) parameterObj);
                            break;

                        case 1111:
                            setSerializableObject(parameterIndex, parameterObj);
                            break;

                        default:
                            throw SQLError.createSQLException(Messages.getString("PreparedStatement.16"), "S1000", getExceptionInterceptor());
                    }

                } catch (Exception ex) {
                    if (ex instanceof SQLException) {
                        throw (SQLException) ex;
                    }

                    SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.17") + parameterObj.getClass().toString() + Messages.getString("PreparedStatement.18") + ex.getClass().getName() + Messages.getString("PreparedStatement.19") + ex.getMessage(), "S1000", getExceptionInterceptor());

                    sqlEx.initCause(ex);

                    throw sqlEx;
                }
            }
        }
    }

    protected int setOneBatchedParameterSet(PreparedStatement batchedStatement, int batchedParamIndex, Object paramSet) throws SQLException {
        BatchParams paramArg = (BatchParams) paramSet;

        boolean[] isNullBatch = paramArg.isNull;
        boolean[] isStreamBatch = paramArg.isStream;

        for (int j = 0; j < isNullBatch.length; j++) {
            if (isNullBatch[j]) {
                batchedStatement.setNull(batchedParamIndex++, 0);
            } else if (isStreamBatch[j]) {
                batchedStatement.setBinaryStream(batchedParamIndex++, paramArg.parameterStreams[j], paramArg.streamLengths[j]);
            } else {

                ((PreparedStatement) batchedStatement).setBytesNoEscapeNoQuotes(batchedParamIndex++, paramArg.parameterStrings[j]);
            }
        }

        return batchedParamIndex;
    }

    public void setRef(int i, Ref x) throws SQLException {
        throw SQLError.notImplemented();
    }

    private final void setSerializableObject(int parameterIndex, Object parameterObj) throws SQLException {
        try {
            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(bytesOut);
            objectOut.writeObject(parameterObj);
            objectOut.flush();
            objectOut.close();
            bytesOut.flush();
            bytesOut.close();

            byte[] buf = bytesOut.toByteArray();
            ByteArrayInputStream bytesIn = new ByteArrayInputStream(buf);
            setBinaryStream(parameterIndex, bytesIn, buf.length);
            this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = -2;
        } catch (Exception ex) {
            SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.54") + ex.getClass().getName(), "S1009", getExceptionInterceptor());

            sqlEx.initCause(ex);

            throw sqlEx;
        }
    }

    public void setShort(int parameterIndex, short x) throws SQLException {
        setInternal(parameterIndex, String.valueOf(x));

        this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 5;
    }

    public void setString(int parameterIndex, String x) throws SQLException {
        synchronized (checkClosed()) {

            if (x == null) {
                setNull(parameterIndex, 1);
            } else {
                checkClosed();

                int stringLength = x.length();

                if (this.connection.isNoBackslashEscapesSet()) {

                    boolean needsHexEscape = isEscapeNeededForString(x, stringLength);

                    if (!needsHexEscape) {
                        byte[] arrayOfByte = null;

                        StringBuffer quotedString = new StringBuffer(x.length() + 2);
                        quotedString.append('\'');
                        quotedString.append(x);
                        quotedString.append('\'');

                        if (!this.isLoadDataQuery) {
                            arrayOfByte = StringUtils.getBytes(quotedString.toString(), this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());

                        } else {

                            arrayOfByte = StringUtils.getBytes(quotedString.toString());
                        }

                        setInternal(parameterIndex, arrayOfByte);
                    } else {
                        byte[] arrayOfByte = null;

                        if (!this.isLoadDataQuery) {
                            arrayOfByte = StringUtils.getBytes(x, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());

                        } else {

                            arrayOfByte = StringUtils.getBytes(x);
                        }

                        setBytes(parameterIndex, arrayOfByte);
                    }

                    return;
                }

                String parameterAsString = x;
                boolean needsQuoted = true;

                if (this.isLoadDataQuery || isEscapeNeededForString(x, stringLength)) {
                    needsQuoted = false;

                    StringBuffer buf = new StringBuffer((int) (x.length() * 1.1D));

                    buf.append('\'');

                    for (int i = 0; i < stringLength; i++) {
                        char c = x.charAt(i);

                        switch (c) {
                            case '\000':
                                buf.append('\\');
                                buf.append('0');
                                break;

                            case '\n':
                                buf.append('\\');
                                buf.append('n');
                                break;

                            case '\r':
                                buf.append('\\');
                                buf.append('r');
                                break;

                            case '\\':
                                buf.append('\\');
                                buf.append('\\');
                                break;

                            case '\'':
                                buf.append('\\');
                                buf.append('\'');
                                break;

                            case '"':
                                if (this.usingAnsiMode) {
                                    buf.append('\\');
                                }

                                buf.append('"');
                                break;

                            case '\032':
                                buf.append('\\');
                                buf.append('Z');
                                break;

                            case '¥':
                            case '₩':
                                if (this.charsetEncoder != null) {
                                    CharBuffer cbuf = CharBuffer.allocate(1);
                                    ByteBuffer bbuf = ByteBuffer.allocate(1);
                                    cbuf.put(c);
                                    cbuf.position(0);
                                    this.charsetEncoder.encode(cbuf, bbuf, true);
                                    if (bbuf.get(0) == 92) {
                                        buf.append('\\');
                                    }
                                }

                            default:
                                buf.append(c);
                                break;
                        }
                    }
                    buf.append('\'');

                    parameterAsString = buf.toString();
                }

                byte[] parameterAsBytes = null;

                if (!this.isLoadDataQuery) {
                    if (needsQuoted) {
                        parameterAsBytes = StringUtils.getBytesWrapped(parameterAsString, '\'', '\'', this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());

                    } else {

                        parameterAsBytes = StringUtils.getBytes(parameterAsString, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());

                    }

                } else {

                    parameterAsBytes = StringUtils.getBytes(parameterAsString);
                }

                setInternal(parameterIndex, parameterAsBytes);

                this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 12;
            }
        }
    }

    private boolean isEscapeNeededForString(String x, int stringLength) {
        boolean needsHexEscape = false;

        for (int i = 0; i < stringLength; i++) {
            char c = x.charAt(i);

            switch (c) {

                case '\000':
                    needsHexEscape = true;
                    break;

                case '\n':
                    needsHexEscape = true;
                    break;

                case '\r':
                    needsHexEscape = true;
                    break;

                case '\\':
                    needsHexEscape = true;
                    break;

                case '\'':
                    needsHexEscape = true;
                    break;

                case '"':
                    needsHexEscape = true;
                    break;

                case '\032':
                    needsHexEscape = true;
                    break;
            }

            if (needsHexEscape) {
                break;
            }
        }
        return needsHexEscape;
    }

    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        setTimeInternal(parameterIndex, x, cal, cal.getTimeZone(), true);
    }

    public void setTime(int parameterIndex, Time x) throws SQLException {
        setTimeInternal(parameterIndex, x, (Calendar) null, Util.getDefaultTimeZone(), false);
    }

    private void setTimeInternal(int parameterIndex, Time x, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException {
        synchronized (checkClosed()) {
            if (x == null) {
                setNull(parameterIndex, 92);
            } else {
                checkClosed();

                if (!this.useLegacyDatetimeCode) {
                    newSetTimeInternal(parameterIndex, x, targetCalendar);
                } else {
                    Calendar sessionCalendar = getCalendarInstanceForSessionOrNew();

                    synchronized (sessionCalendar) {
                        x = TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, x, tz, this.connection.getServerTimezoneTZ(), rollForward);
                    }

                    setInternal(parameterIndex, "'" + x.toString() + "'");
                }

                this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 92;
            }
        }
    }

    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        setTimestampInternal(parameterIndex, x, cal, cal.getTimeZone(), true);
    }

    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        setTimestampInternal(parameterIndex, x, (Calendar) null, Util.getDefaultTimeZone(), false);
    }

    private void setTimestampInternal(int parameterIndex, Timestamp x, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException {
        synchronized (checkClosed()) {
            if (x == null) {
                setNull(parameterIndex, 93);
            } else {
                checkClosed();

                if (!this.useLegacyDatetimeCode) {
                    newSetTimestampInternal(parameterIndex, x, targetCalendar);
                } else {
                    Calendar sessionCalendar = this.connection.getUseJDBCCompliantTimezoneShift() ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew();

                    synchronized (sessionCalendar) {
                        x = TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, x, tz, this.connection.getServerTimezoneTZ(), rollForward);
                    }

                    if (this.connection.getUseSSPSCompatibleTimezoneShift()) {
                        doSSPSCompatibleTimezoneShift(parameterIndex, x, sessionCalendar);
                    } else {
                        synchronized (this) {
                            if (this.tsdf == null) {
                                this.tsdf = new SimpleDateFormat("''yyyy-MM-dd HH:mm:ss", Locale.US);
                            }

                            StringBuffer buf = new StringBuffer();
                            buf.append(this.tsdf.format(x));

                            if (this.serverSupportsFracSecs) {
                                int nanos = x.getNanos();

                                if (nanos != 0) {
                                    buf.append('.');
                                    buf.append(TimeUtil.formatNanos(nanos, this.serverSupportsFracSecs));
                                }
                            }

                            buf.append('\'');

                            setInternal(parameterIndex, buf.toString());
                        }
                    }
                }

                this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 93;
            }
        }
    }

    private void newSetTimestampInternal(int parameterIndex, Timestamp x, Calendar targetCalendar) throws SQLException {
        synchronized (checkClosed()) {
            if (this.tsdf == null) {
                this.tsdf = new SimpleDateFormat("''yyyy-MM-dd HH:mm:ss", Locale.US);
            }

            String timestampString = null;

            if (targetCalendar != null) {
                targetCalendar.setTime(x);
                this.tsdf.setTimeZone(targetCalendar.getTimeZone());

                timestampString = this.tsdf.format(x);
            } else {
                this.tsdf.setTimeZone(this.connection.getServerTimezoneTZ());
                timestampString = this.tsdf.format(x);
            }

            StringBuffer buf = new StringBuffer();
            buf.append(timestampString);
            buf.append('.');
            buf.append(TimeUtil.formatNanos(x.getNanos(), this.serverSupportsFracSecs));
            buf.append('\'');

            setInternal(parameterIndex, buf.toString());
        }
    }

    private void newSetTimeInternal(int parameterIndex, Time x, Calendar targetCalendar) throws SQLException {
        synchronized (checkClosed()) {
            if (this.tdf == null) {
                this.tdf = new SimpleDateFormat("''HH:mm:ss''", Locale.US);
            }

            String timeString = null;

            if (targetCalendar != null) {
                targetCalendar.setTime(x);
                this.tdf.setTimeZone(targetCalendar.getTimeZone());

                timeString = this.tdf.format(x);
            } else {
                this.tdf.setTimeZone(this.connection.getServerTimezoneTZ());
                timeString = this.tdf.format(x);
            }

            setInternal(parameterIndex, timeString);
        }
    }

    private void newSetDateInternal(int parameterIndex, Date x, Calendar targetCalendar) throws SQLException {
        synchronized (checkClosed()) {
            if (this.ddf == null) {
                this.ddf = new SimpleDateFormat("''yyyy-MM-dd''", Locale.US);
            }

            String timeString = null;

            if (targetCalendar != null) {
                targetCalendar.setTime(x);
                this.ddf.setTimeZone(targetCalendar.getTimeZone());

                timeString = this.ddf.format(x);
            } else {
                this.ddf.setTimeZone(this.connection.getServerTimezoneTZ());
                timeString = this.ddf.format(x);
            }

            setInternal(parameterIndex, timeString);
        }
    }

    private void doSSPSCompatibleTimezoneShift(int parameterIndex, Timestamp x, Calendar sessionCalendar) throws SQLException {
        synchronized (checkClosed()) {
            Calendar sessionCalendar2 = this.connection.getUseJDBCCompliantTimezoneShift() ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew();

            synchronized (sessionCalendar2) {
                Date oldTime = sessionCalendar2.getTime();

                try {
                    sessionCalendar2.setTime(x);

                    int year = sessionCalendar2.get(1);
                    int month = sessionCalendar2.get(2) + 1;
                    int date = sessionCalendar2.get(5);

                    int hour = sessionCalendar2.get(11);
                    int minute = sessionCalendar2.get(12);
                    int seconds = sessionCalendar2.get(13);

                    StringBuffer tsBuf = new StringBuffer();

                    tsBuf.append('\'');
                    tsBuf.append(year);

                    tsBuf.append("-");

                    if (month < 10) {
                        tsBuf.append('0');
                    }

                    tsBuf.append(month);

                    tsBuf.append('-');

                    if (date < 10) {
                        tsBuf.append('0');
                    }

                    tsBuf.append(date);

                    tsBuf.append(' ');

                    if (hour < 10) {
                        tsBuf.append('0');
                    }

                    tsBuf.append(hour);

                    tsBuf.append(':');

                    if (minute < 10) {
                        tsBuf.append('0');
                    }

                    tsBuf.append(minute);

                    tsBuf.append(':');

                    if (seconds < 10) {
                        tsBuf.append('0');
                    }

                    tsBuf.append(seconds);

                    tsBuf.append('.');
                    tsBuf.append(TimeUtil.formatNanos(x.getNanos(), this.serverSupportsFracSecs));
                    tsBuf.append('\'');

                    setInternal(parameterIndex, tsBuf.toString());
                } finally {

                    sessionCalendar.setTime(oldTime);
                }
            }
        }
    }

    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        if (x == null) {
            setNull(parameterIndex, 12);
        } else {
            setBinaryStream(parameterIndex, x, length);

            this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 2005;
        }
    }

    public void setURL(int parameterIndex, URL arg) throws SQLException {
        if (arg != null) {
            setString(parameterIndex, arg.toString());

            this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 70;
        } else {
            setNull(parameterIndex, 1);
        }
    }

    private final void streamToBytes(Buffer packet, InputStream in, boolean escape, int streamLength, boolean useLength) throws SQLException {
        synchronized (checkClosed()) {
            try {
                if (this.streamConvertBuf == null) {
                    this.streamConvertBuf = new byte[4096];
                }

                String connectionEncoding = this.connection.getEncoding();

                boolean hexEscape = false;

                try {
                    if (this.connection.isNoBackslashEscapesSet() || (this.connection.getUseUnicode() && connectionEncoding != null && CharsetMapping.isMultibyteCharset(connectionEncoding) && !this.connection.parserKnowsUnicode())) {

                        hexEscape = true;
                    }
                } catch (RuntimeException ex) {
                    SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", (ExceptionInterceptor) null);
                    sqlEx.initCause(ex);
                    throw sqlEx;
                }

                if (streamLength == -1) {
                    useLength = false;
                }

                int bc = -1;

                if (useLength) {
                    bc = readblock(in, this.streamConvertBuf, streamLength);
                } else {
                    bc = readblock(in, this.streamConvertBuf);
                }

                int lengthLeftToRead = streamLength - bc;

                if (hexEscape) {
                    packet.writeStringNoNull("x");
                } else if (this.connection.getIO().versionMeetsMinimum(4, 1, 0)) {
                    packet.writeStringNoNull("_binary");
                }

                if (escape) {
                    packet.writeByte((byte) 39);
                }

                while (bc > 0) {
                    if (hexEscape) {
                        hexEscapeBlock(this.streamConvertBuf, packet, bc);
                    } else if (escape) {
                        escapeblockFast(this.streamConvertBuf, packet, bc);
                    } else {
                        packet.writeBytesNoNull(this.streamConvertBuf, 0, bc);
                    }

                    if (useLength) {
                        bc = readblock(in, this.streamConvertBuf, lengthLeftToRead);

                        if (bc > 0)
                            lengthLeftToRead -= bc;
                        continue;
                    }
                    bc = readblock(in, this.streamConvertBuf);
                }

                if (escape) {
                    packet.writeByte((byte) 39);
                }
            } finally {
                if (this.connection.getAutoClosePStmtStreams()) {
                    try {
                        in.close();
                    } catch (IOException ioEx) {
                    }

                    in = null;
                }
            }
        }
    }

    private final byte[] streamToBytes(InputStream in, boolean escape, int streamLength, boolean useLength) throws SQLException {
        synchronized (checkClosed()) {
            try {
                if (this.streamConvertBuf == null) {
                    this.streamConvertBuf = new byte[4096];
                }
                if (streamLength == -1) {
                    useLength = false;
                }

                ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();

                int bc = -1;

                if (useLength) {
                    bc = readblock(in, this.streamConvertBuf, streamLength);
                } else {
                    bc = readblock(in, this.streamConvertBuf);
                }

                int lengthLeftToRead = streamLength - bc;

                if (escape) {
                    if (this.connection.versionMeetsMinimum(4, 1, 0)) {
                        bytesOut.write(95);
                        bytesOut.write(98);
                        bytesOut.write(105);
                        bytesOut.write(110);
                        bytesOut.write(97);
                        bytesOut.write(114);
                        bytesOut.write(121);
                    }

                    bytesOut.write(39);
                }

                while (bc > 0) {
                    if (escape) {
                        escapeblockFast(this.streamConvertBuf, bytesOut, bc);
                    } else {
                        bytesOut.write(this.streamConvertBuf, 0, bc);
                    }

                    if (useLength) {
                        bc = readblock(in, this.streamConvertBuf, lengthLeftToRead);

                        if (bc > 0)
                            lengthLeftToRead -= bc;
                        continue;
                    }
                    bc = readblock(in, this.streamConvertBuf);
                }

                if (escape) {
                    bytesOut.write(39);
                }

                return bytesOut.toByteArray();
            } finally {
                if (this.connection.getAutoClosePStmtStreams()) {
                    try {
                        in.close();
                    } catch (IOException ioEx) {
                    }

                    in = null;
                }
            }
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(super.toString());
        buf.append(": ");

        try {
            buf.append(asSql());
        } catch (SQLException sqlEx) {
            buf.append("EXCEPTION: " + sqlEx.toString());
        }

        return buf.toString();
    }

    protected int getParameterIndexOffset() {
        return 0;
    }

    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        setAsciiStream(parameterIndex, x, -1);
    }

    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        setAsciiStream(parameterIndex, x, (int) length);
        this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 2005;
    }

    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        setBinaryStream(parameterIndex, x, -1);
    }

    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        setBinaryStream(parameterIndex, x, (int) length);
    }

    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        setBinaryStream(parameterIndex, inputStream);
    }

    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        setCharacterStream(parameterIndex, reader, -1);
    }

    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        setCharacterStream(parameterIndex, reader, (int) length);
    }

    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        setCharacterStream(parameterIndex, reader);
    }

    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        setCharacterStream(parameterIndex, reader, length);
    }

    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        setNCharacterStream(parameterIndex, value, -1L);
    }

    public void setNString(int parameterIndex, String x) throws SQLException {
        synchronized (checkClosed()) {
            if (this.charEncoding.equalsIgnoreCase("UTF-8") || this.charEncoding.equalsIgnoreCase("utf8")) {

                setString(parameterIndex, x);

                return;
            }

            if (x == null) {
                setNull(parameterIndex, 1);
            } else {
                int stringLength = x.length();

                StringBuffer buf = new StringBuffer((int) (x.length() * 1.1D + 4.0D));
                buf.append("_utf8");
                buf.append('\'');

                for (int i = 0; i < stringLength; i++) {
                    char c = x.charAt(i);

                    switch (c) {
                        case '\000':
                            buf.append('\\');
                            buf.append('0');
                            break;

                        case '\n':
                            buf.append('\\');
                            buf.append('n');
                            break;

                        case '\r':
                            buf.append('\\');
                            buf.append('r');
                            break;

                        case '\\':
                            buf.append('\\');
                            buf.append('\\');
                            break;

                        case '\'':
                            buf.append('\\');
                            buf.append('\'');
                            break;

                        case '"':
                            if (this.usingAnsiMode) {
                                buf.append('\\');
                            }

                            buf.append('"');
                            break;

                        case '\032':
                            buf.append('\\');
                            buf.append('Z');
                            break;

                        default:
                            buf.append(c);
                            break;
                    }
                }
                buf.append('\'');

                String parameterAsString = buf.toString();

                byte[] parameterAsBytes = null;

                if (!this.isLoadDataQuery) {
                    parameterAsBytes = StringUtils.getBytes(parameterAsString, this.connection.getCharsetConverter("UTF-8"), "UTF-8", this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());

                } else {

                    parameterAsBytes = StringUtils.getBytes(parameterAsString);
                }

                setInternal(parameterIndex, parameterAsBytes);

                this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = -9;
            }
        }
    }

    public void setNCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        synchronized (checkClosed()) {
            try {
                if (reader == null) {
                    setNull(parameterIndex, -1);
                } else {

                    char[] c = null;
                    int len = 0;

                    boolean useLength = this.connection.getUseStreamLengthsInPrepStmts();

                    if (useLength && length != -1L) {
                        c = new char[(int) length];

                        int numCharsRead = readFully(reader, c, (int) length);

                        setNString(parameterIndex, new String(c, 0, numCharsRead));
                    } else {

                        c = new char[4096];

                        StringBuffer buf = new StringBuffer();

                        while ((len = reader.read(c)) != -1) {
                            buf.append(c, 0, len);
                        }

                        setNString(parameterIndex, buf.toString());
                    }

                    this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 2011;
                }
            } catch (IOException ioEx) {
                throw SQLError.createSQLException(ioEx.toString(), "S1000", getExceptionInterceptor());
            }
        }
    }

    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        setNCharacterStream(parameterIndex, reader);
    }

    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        if (reader == null) {
            setNull(parameterIndex, -1);
        } else {
            setNCharacterStream(parameterIndex, reader, length);
        }
    }

    public ParameterBindings getParameterBindings() throws SQLException {
        synchronized (checkClosed()) {
            return new EmulatedPreparedStatementBindings();
        }
    }

    public String getPreparedSql() {
        try {
            synchronized (checkClosed()) {
                if (this.rewrittenBatchSize == 0) {
                    return this.originalSql;
                }

                try {
                    return this.parseInfo.getSqlForBatch(this.parseInfo);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getUpdateCount() throws SQLException {
        int count = super.getUpdateCount();

        if (containsOnDuplicateKeyUpdateInSQL() && this.compensateForOnDuplicateKeyUpdate) {
            if (count == 2 || count == 0) {
                count = 1;
            }
        }

        return count;
    }

    static interface BatchVisitor {
        BatchVisitor increment();

        BatchVisitor decrement();

        BatchVisitor append(byte[] param1ArrayOfbyte);

        BatchVisitor merge(byte[] param1ArrayOfbyte1, byte[] param1ArrayOfbyte2);
    }

    public class BatchParams {
        public boolean[] isNull = null;

        public boolean[] isStream = null;

        public InputStream[] parameterStreams = null;

        public byte[][] parameterStrings = (byte[][]) null;

        public int[] streamLengths = null;

        BatchParams(byte[][] strings, InputStream[] streams, boolean[] isStreamFlags, int[] lengths, boolean[] isNullFlags) {
            this.parameterStrings = new byte[strings.length][];
            this.parameterStreams = new InputStream[streams.length];
            this.isStream = new boolean[isStreamFlags.length];
            this.streamLengths = new int[lengths.length];
            this.isNull = new boolean[isNullFlags.length];
            System.arraycopy(strings, 0, this.parameterStrings, 0, strings.length);

            System.arraycopy(streams, 0, this.parameterStreams, 0, streams.length);

            System.arraycopy(isStreamFlags, 0, this.isStream, 0, isStreamFlags.length);

            System.arraycopy(lengths, 0, this.streamLengths, 0, lengths.length);
            System.arraycopy(isNullFlags, 0, this.isNull, 0, isNullFlags.length);
        }
    }

    class EndPoint {
        int begin;

        int end;

        EndPoint(int b, int e) {
            this.begin = b;
            this.end = e;
        }
    }

    class ParseInfo {
        char firstStmtChar = Character.MIN_VALUE;

        boolean foundLimitClause = false;

        boolean foundLoadData = false;

        long lastUsed = 0L;

        int statementLength = 0;

        int statementStartPos = 0;

        boolean canRewriteAsMultiValueInsert = false;

        byte[][] staticSql = (byte[][]) null;

        boolean isOnDuplicateKeyUpdate = false;

        int locationOfOnDuplicateKeyUpdate = -1;

        String valuesClause;

        boolean parametersInDuplicateKeyClause = false;

        private ParseInfo batchHead;

        private ParseInfo batchValues;

        private ParseInfo batchODKUClause;

        ParseInfo(String sql, MySQLConnection conn, DatabaseMetaData dbmd, String encoding, SingleByteCharsetConverter converter) throws SQLException {
            this(sql, conn, dbmd, encoding, converter, true);
        }

        public ParseInfo(String sql, MySQLConnection conn, DatabaseMetaData dbmd, String encoding, SingleByteCharsetConverter converter, boolean buildRewriteInfo) throws SQLException {

        }

        private ParseInfo(byte[][] staticSql, char firstStmtChar, boolean foundLimitClause, boolean foundLoadData, boolean isOnDuplicateKeyUpdate, int locationOfOnDuplicateKeyUpdate, int statementLength, int statementStartPos) {
            this.firstStmtChar = firstStmtChar;
            this.foundLimitClause = foundLimitClause;
            this.foundLoadData = foundLoadData;
            this.isOnDuplicateKeyUpdate = isOnDuplicateKeyUpdate;
            this.locationOfOnDuplicateKeyUpdate = locationOfOnDuplicateKeyUpdate;
            this.statementLength = statementLength;
            this.statementStartPos = statementStartPos;
            this.staticSql = staticSql;
        }

        private void buildRewriteBatchedParams(String sql, MySQLConnection conn, DatabaseMetaData metadata, String encoding, SingleByteCharsetConverter converter) throws SQLException {
            this.valuesClause = extractValuesClause(sql);
            String odkuClause = this.isOnDuplicateKeyUpdate ? sql.substring(this.locationOfOnDuplicateKeyUpdate) : null;

            String headSql = null;

            if (this.isOnDuplicateKeyUpdate) {
                headSql = sql.substring(0, this.locationOfOnDuplicateKeyUpdate);
            } else {
                headSql = sql;
            }

            this.batchHead = new ParseInfo(headSql, conn, metadata, encoding, converter, false);

            this.batchValues = new ParseInfo("," + this.valuesClause, conn, metadata, encoding, converter, false);

            this.batchODKUClause = null;

            if (odkuClause != null && odkuClause.length() > 0) {
                this.batchODKUClause = new ParseInfo("," + this.valuesClause + " " + odkuClause, conn, metadata, encoding, converter, false);
            }
        }

        private String extractValuesClause(String sql) throws SQLException {
            String quoteCharStr = PreparedStatement.this.connection.getMetaData().getIdentifierQuoteString();

            int indexOfValues = -1;
            int valuesSearchStart = this.statementStartPos;

            while (indexOfValues == -1) {
                if (quoteCharStr.length() > 0) {
                    indexOfValues = StringUtils.indexOfIgnoreCaseRespectQuotes(valuesSearchStart, PreparedStatement.this.originalSql, "VALUES", quoteCharStr.charAt(0), false);
                } else {

                    indexOfValues = StringUtils.indexOfIgnoreCase(valuesSearchStart, PreparedStatement.this.originalSql, "VALUES");
                }

                if (indexOfValues > 0) {

                    char c = PreparedStatement.this.originalSql.charAt(indexOfValues - 1);
                    if (!Character.isWhitespace(c) && c != ')' && c != '`') {
                        valuesSearchStart = indexOfValues + 6;
                        indexOfValues = -1;
                        continue;
                    }
                    c = PreparedStatement.this.originalSql.charAt(indexOfValues + 6);
                    if (!Character.isWhitespace(c) && c != '(') {
                        valuesSearchStart = indexOfValues + 6;
                        indexOfValues = -1;
                    }
                }
            }

            if (indexOfValues == -1) {
                return null;
            }

            int indexOfFirstParen = sql.indexOf('(', indexOfValues + 6);

            if (indexOfFirstParen == -1) {
                return null;
            }

            int endOfValuesClause = sql.lastIndexOf(')');

            if (endOfValuesClause == -1) {
                return null;
            }

            if (this.isOnDuplicateKeyUpdate) {
                endOfValuesClause = this.locationOfOnDuplicateKeyUpdate - 1;
            }

            return sql.substring(indexOfFirstParen, endOfValuesClause + 1);
        }

        synchronized ParseInfo getParseInfoForBatch(int numBatch) {
            PreparedStatement.AppendingBatchVisitor apv = new PreparedStatement.AppendingBatchVisitor();
            buildInfoForBatch(numBatch, apv);

            ParseInfo batchParseInfo = new ParseInfo(apv.getStaticSqlStrings(), this.firstStmtChar, this.foundLimitClause, this.foundLoadData, this.isOnDuplicateKeyUpdate, this.locationOfOnDuplicateKeyUpdate, this.statementLength, this.statementStartPos);

            return batchParseInfo;
        }

        String getSqlForBatch(int numBatch) throws UnsupportedEncodingException {
            ParseInfo batchInfo = getParseInfoForBatch(numBatch);

            return getSqlForBatch(batchInfo);
        }

        String getSqlForBatch(ParseInfo batchInfo) throws UnsupportedEncodingException {
            int size = 0;
            byte[][] sqlStrings = batchInfo.staticSql;
            int sqlStringsLength = sqlStrings.length;

            for (int i = 0; i < sqlStringsLength; i++) {
                size += (sqlStrings[i]).length;
                size++;
            }

            StringBuffer buf = new StringBuffer(size);

            for (int j = 0; j < sqlStringsLength - 1; j++) {
                buf.append(StringUtils.toString(sqlStrings[j], PreparedStatement.this.charEncoding));
                buf.append("?");
            }

            buf.append(StringUtils.toString(sqlStrings[sqlStringsLength - 1]));

            return buf.toString();
        }

        private void buildInfoForBatch(int numBatch, PreparedStatement.BatchVisitor visitor) {
            byte[][] headStaticSql = this.batchHead.staticSql;
            int headStaticSqlLength = headStaticSql.length;

            if (headStaticSqlLength > 1) {
                for (int j = 0; j < headStaticSqlLength - 1; j++) {
                    visitor.append(headStaticSql[j]).increment();
                }
            }

            byte[] endOfHead = headStaticSql[headStaticSqlLength - 1];
            byte[][] valuesStaticSql = this.batchValues.staticSql;
            byte[] beginOfValues = valuesStaticSql[0];

            visitor.merge(endOfHead, beginOfValues).increment();

            int numValueRepeats = numBatch - 1;

            if (this.batchODKUClause != null) {
                numValueRepeats--;
            }

            int valuesStaticSqlLength = valuesStaticSql.length;
            byte[] endOfValues = valuesStaticSql[valuesStaticSqlLength - 1];

            for (int i = 0; i < numValueRepeats; i++) {
                for (int j = 1; j < valuesStaticSqlLength - 1; j++) {
                    visitor.append(valuesStaticSql[j]).increment();
                }
                visitor.merge(endOfValues, beginOfValues).increment();
            }

            if (this.batchODKUClause != null) {
                byte[][] batchOdkuStaticSql = this.batchODKUClause.staticSql;
                byte[] beginOfOdku = batchOdkuStaticSql[0];
                visitor.decrement().merge(endOfValues, beginOfOdku).increment();

                int batchOdkuStaticSqlLength = batchOdkuStaticSql.length;

                if (numBatch > 1) {
                    for (int j = 1; j < batchOdkuStaticSqlLength; j++) {
                        visitor.append(batchOdkuStaticSql[j]).increment();
                    }
                } else {

                    visitor.decrement().append(batchOdkuStaticSql[batchOdkuStaticSqlLength - 1]);
                }

            } else {

                visitor.decrement().append(this.staticSql[this.staticSql.length - 1]);
            }
        }
    }

    class AppendingBatchVisitor
            implements BatchVisitor {
        LinkedList<byte[]> statementComponents = (LinkedList) new LinkedList<byte>();

        public PreparedStatement.BatchVisitor append(byte[] values) {
            this.statementComponents.addLast(values);

            return this;
        }

        public PreparedStatement.BatchVisitor increment() {
            return this;
        }

        public PreparedStatement.BatchVisitor decrement() {
            this.statementComponents.removeLast();

            return this;
        }

        public PreparedStatement.BatchVisitor merge(byte[] front, byte[] back) {
            int mergedLength = front.length + back.length;
            byte[] merged = new byte[mergedLength];
            System.arraycopy(front, 0, merged, 0, front.length);
            System.arraycopy(back, 0, merged, front.length, back.length);
            this.statementComponents.addLast(merged);
            return this;
        }

        public byte[][] getStaticSqlStrings() {
            byte[][] asBytes = new byte[this.statementComponents.size()][];
            this.statementComponents.toArray(asBytes);

            return asBytes;
        }

        public String toString() {
            StringBuffer buf = new StringBuffer();
            Iterator<byte[]> iter = (Iterator) this.statementComponents.iterator();
            while (iter.hasNext()) {
                buf.append(StringUtils.toString(iter.next()));
            }

            return buf.toString();
        }
    }

    class EmulatedPreparedStatementBindings
            implements ParameterBindings {
        private ResultSetImpl bindingsAsRs;
        private boolean[] parameterIsNull;

        EmulatedPreparedStatementBindings() throws SQLException {
            List<ResultSetRow> rows = new ArrayList<ResultSetRow>();
            this.parameterIsNull = new boolean[PreparedStatement.this.parameterCount];
            System.arraycopy(PreparedStatement.this.isNull, 0, this.parameterIsNull, 0, PreparedStatement.this.parameterCount);

            byte[][] rowData = new byte[PreparedStatement.this.parameterCount][];
            Field[] typeMetadata = new Field[PreparedStatement.this.parameterCount];

            for (int i = 0; i < PreparedStatement.this.parameterCount; i++) {
                if (PreparedStatement.this.batchCommandIndex == -1) {
                    rowData[i] = PreparedStatement.this.getBytesRepresentation(i);
                } else {
                    rowData[i] = PreparedStatement.this.getBytesRepresentationForBatch(i, PreparedStatement.this.batchCommandIndex);
                }
                int charsetIndex = 0;

                if (PreparedStatement.this.parameterTypes[i] == -2 || PreparedStatement.this.parameterTypes[i] == 2004) {

                    charsetIndex = 63;
                } else {
                    try {
                        String mysqlEncodingName = CharsetMapping.getMysqlEncodingForJavaEncoding(PreparedStatement.this.connection.getEncoding(), PreparedStatement.this.connection);

                        charsetIndex = CharsetMapping.getCharsetIndexForMysqlEncodingName(mysqlEncodingName);
                    } catch (SQLException ex) {
                        throw ex;
                    } catch (RuntimeException ex) {
                        SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", (ExceptionInterceptor) null);
                        sqlEx.initCause(ex);
                        throw sqlEx;
                    }
                }

                Field parameterMetadata = new Field(null, "parameter_" + (i + 1), charsetIndex, PreparedStatement.this.parameterTypes[i], (rowData[i]).length);

                parameterMetadata.setConnection(PreparedStatement.this.connection);
                typeMetadata[i] = parameterMetadata;
            }

            rows.add(new ByteArrayRow(rowData, PreparedStatement.this.getExceptionInterceptor()));

            this.bindingsAsRs = new ResultSetImpl(PreparedStatement.this.connection.getCatalog(), typeMetadata, new RowDataStatic(rows), PreparedStatement.this.connection, null);

            this.bindingsAsRs.next();
        }

        public Array getArray(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getArray(parameterIndex);
        }

        public InputStream getAsciiStream(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getAsciiStream(parameterIndex);
        }

        public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getBigDecimal(parameterIndex);
        }

        public InputStream getBinaryStream(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getBinaryStream(parameterIndex);
        }

        public Blob getBlob(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getBlob(parameterIndex);
        }

        public boolean getBoolean(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getBoolean(parameterIndex);
        }

        public byte getByte(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getByte(parameterIndex);
        }

        public byte[] getBytes(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getBytes(parameterIndex);
        }

        public Reader getCharacterStream(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getCharacterStream(parameterIndex);
        }

        public Clob getClob(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getClob(parameterIndex);
        }

        public Date getDate(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getDate(parameterIndex);
        }

        public double getDouble(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getDouble(parameterIndex);
        }

        public float getFloat(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getFloat(parameterIndex);
        }

        public int getInt(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getInt(parameterIndex);
        }

        public long getLong(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getLong(parameterIndex);
        }

        public Reader getNCharacterStream(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getCharacterStream(parameterIndex);
        }

        public Reader getNClob(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getCharacterStream(parameterIndex);
        }

        public Object getObject(int parameterIndex) throws SQLException {
            PreparedStatement.this.checkBounds(parameterIndex, 0);

            if (this.parameterIsNull[parameterIndex - 1]) {
                return null;
            }

            switch (PreparedStatement.this.parameterTypes[parameterIndex - 1]) {
                case -6:
                    return Byte.valueOf(getByte(parameterIndex));
                case 5:
                    return Short.valueOf(getShort(parameterIndex));
                case 4:
                    return Integer.valueOf(getInt(parameterIndex));
                case -5:
                    return Long.valueOf(getLong(parameterIndex));
                case 6:
                    return Float.valueOf(getFloat(parameterIndex));
                case 8:
                    return Double.valueOf(getDouble(parameterIndex));
            }
            return this.bindingsAsRs.getObject(parameterIndex);
        }

        public Ref getRef(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getRef(parameterIndex);
        }

        public short getShort(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getShort(parameterIndex);
        }

        public String getString(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getString(parameterIndex);
        }

        public Time getTime(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getTime(parameterIndex);
        }

        public Timestamp getTimestamp(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getTimestamp(parameterIndex);
        }

        public URL getURL(int parameterIndex) throws SQLException {
            return this.bindingsAsRs.getURL(parameterIndex);
        }

        public boolean isNull(int parameterIndex) throws SQLException {
            PreparedStatement.this.checkBounds(parameterIndex, 0);

            return this.parameterIsNull[parameterIndex - 1];
        }
    }
}

