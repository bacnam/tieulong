package com.mysql.jdbc;

import com.mysql.jdbc.authentication.MysqlClearPasswordPlugin;
import com.mysql.jdbc.authentication.MysqlNativePasswordPlugin;
import com.mysql.jdbc.authentication.MysqlOldPasswordPlugin;
import com.mysql.jdbc.authentication.Sha256PasswordPlugin;
import com.mysql.jdbc.exceptions.MySQLStatementCancelledException;
import com.mysql.jdbc.exceptions.MySQLTimeoutException;
import com.mysql.jdbc.log.LogUtils;
import com.mysql.jdbc.profiler.ProfilerEvent;
import com.mysql.jdbc.profiler.ProfilerEventHandler;
import com.mysql.jdbc.util.ReadAheadInputStream;
import com.mysql.jdbc.util.ResultSetUtil;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.lang.ref.SoftReference;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.zip.Deflater;

public class MysqlIO {
    protected static final int NULL_LENGTH = -1;
    protected static final int COMP_HEADER_LENGTH = 3;
    protected static final int MIN_COMPRESS_LEN = 50;
    protected static final int HEADER_LENGTH = 4;
    protected static final int AUTH_411_OVERHEAD = 33;
    protected static final int CLIENT_CONNECT_WITH_DB = 8;
    protected static final int CLIENT_SSL = 2048;
    protected static final int CLIENT_RESERVED = 16384;
    protected static final int CLIENT_SECURE_CONNECTION = 32768;
    protected static final int MAX_QUERY_SIZE_TO_LOG = 1024;
    protected static final int MAX_QUERY_SIZE_TO_EXPLAIN = 1048576;
    protected static final int INITIAL_PACKET_SIZE = 1024;
    protected static final String ZERO_DATE_VALUE_MARKER = "0000-00-00";
    protected static final String ZERO_DATETIME_VALUE_MARKER = "0000-00-00 00:00:00";
    static final int SERVER_MORE_RESULTS_EXISTS = 8;
    private static final int UTF8_CHARSET_INDEX = 33;
    private static final String CODE_PAGE_1252 = "Cp1252";
    private static final int CLIENT_COMPRESS = 32;
    private static final int CLIENT_FOUND_ROWS = 2;
    private static final int CLIENT_LOCAL_FILES = 128;
    private static final int CLIENT_LONG_FLAG = 4;
    private static final int CLIENT_LONG_PASSWORD = 1;
    private static final int CLIENT_PROTOCOL_41 = 512;
    private static final int CLIENT_INTERACTIVE = 1024;
    private static final int CLIENT_TRANSACTIONS = 8192;
    private static final int CLIENT_MULTI_QUERIES = 65536;
    private static final int CLIENT_MULTI_RESULTS = 131072;
    private static final int CLIENT_PLUGIN_AUTH = 524288;
    private static final int CLIENT_CAN_HANDLE_EXPIRED_PASSWORD = 4194304;
    private static final int SERVER_STATUS_IN_TRANS = 1;
    private static final int SERVER_STATUS_AUTOCOMMIT = 2;
    private static final int SERVER_QUERY_NO_GOOD_INDEX_USED = 16;
    private static final int SERVER_QUERY_NO_INDEX_USED = 32;
    private static final int SERVER_QUERY_WAS_SLOW = 2048;
    private static final int SERVER_STATUS_CURSOR_EXISTS = 64;
    private static final String FALSE_SCRAMBLE = "xxxxxxxx";
    private static final int MAX_PACKET_DUMP_LENGTH = 1024;
    private static int maxBufferSize = 65535;
    private static String jvmPlatformCharset = null;

    static {
        OutputStreamWriter outWriter = null;

        try {
            outWriter = new OutputStreamWriter(new ByteArrayOutputStream());
            jvmPlatformCharset = outWriter.getEncoding();
        } finally {
            try {
                if (outWriter != null) {
                    outWriter.close();
                }
            } catch (IOException ioEx) {
            }
        }
    }

    protected int serverCharsetIndex;
    protected BufferedOutputStream mysqlOutput = null;
    protected MySQLConnection connection;
    protected InputStream mysqlInput = null;
    protected Socket mysqlConnection = null;
    protected SocketFactory socketFactory = null;
    protected String host = null;
    protected String seed;
    protected int maxThreeBytes = 16581375;
    protected int port = 3306;
    protected int serverCapabilities;
    protected long clientParam = 0L;
    protected long lastPacketSentTimeMs = 0L;
    protected long lastPacketReceivedTimeMs = 0L;
    private boolean packetSequenceReset = false;
    private Buffer reusablePacket = null;
    private Buffer sendPacket = null;
    private Buffer sharedSendPacket = null;
    private Deflater deflater = null;
    private LinkedList<StringBuffer> packetDebugRingBuffer = null;
    private RowData streamingData = null;
    private SoftReference<Buffer> loadFileBufRef;
    private SoftReference<Buffer> splitBufRef;
    private SoftReference<Buffer> compressBufRef;
    private String serverVersion = null;
    private String socketFactoryClassName = null;
    private byte[] packetHeaderBuf = new byte[4];
    private boolean colDecimalNeedsBump = false;
    private boolean hadWarnings = false;
    private boolean has41NewNewProt = false;
    private boolean hasLongColumnInfo = false;
    private boolean isInteractiveClient = false;
    private boolean logSlowQueries = false;
    private boolean platformDbCharsetMatches = true;
    private boolean profileSql = false;
    private boolean queryBadIndexUsed = false;
    private boolean queryNoIndexUsed = false;
    private boolean serverQueryWasSlow = false;
    private boolean use41Extensions = false;
    private boolean useCompression = false;
    private boolean useNewLargePackets = false;
    private boolean useNewUpdateCounts = false;
    private byte packetSequence = 0;
    private byte compressedPacketSequence = 0;
    private byte readPacketSequence = -1;
    private boolean checkPacketSequence = false;
    private byte protocolVersion = 0;
    private int maxAllowedPacket = 1048576;
    private int serverMajorVersion = 0;
    private int serverMinorVersion = 0;
    private int oldServerStatus = 0;
    private int serverStatus = 0;
    private int serverSubMinorVersion = 0;
    private int warningCount = 0;
    private boolean traceProtocol = false;
    private boolean enablePacketDebug = false;
    private boolean useConnectWithDb;
    private boolean needToGrabQueryFromPacket;
    private boolean autoGenerateTestcaseScript;
    private long threadId;
    private boolean useNanosForElapsedTime;
    private long slowQueryThreshold;
    private String queryTimingUnits;
    private boolean useDirectRowUnpack = true;
    private int useBufferRowSizeThreshold;
    private int commandCount = 0;
    private List<StatementInterceptorV2> statementInterceptors;
    private ExceptionInterceptor exceptionInterceptor;
    private int authPluginDataLength = 0;

    private Map<String, AuthenticationPlugin> authenticationPlugins;

    private List<String> disabledAuthenticationPlugins;

    private String defaultAuthenticationPlugin;

    private String defaultAuthenticationPluginProtocolName;

    private int statementExecutionDepth;

    private boolean useAutoSlowLog;

    public MysqlIO(String host, int port, Properties props, String socketFactoryClassName, MySQLConnection conn, int socketTimeout, int useBufferRowSizeThreshold) throws IOException, SQLException {
        this.authenticationPlugins = null;

        this.disabledAuthenticationPlugins = null;

        this.defaultAuthenticationPlugin = null;

        this.defaultAuthenticationPluginProtocolName = null;

        this.statementExecutionDepth = 0;
        this.connection = conn;
        if (this.connection.getEnablePacketDebug()) this.packetDebugRingBuffer = new LinkedList<StringBuffer>();
        this.traceProtocol = this.connection.getTraceProtocol();
        this.useAutoSlowLog = this.connection.getAutoSlowLog();
        this.useBufferRowSizeThreshold = useBufferRowSizeThreshold;
        this.useDirectRowUnpack = this.connection.getUseDirectRowUnpack();
        this.logSlowQueries = this.connection.getLogSlowQueries();
        this.reusablePacket = new Buffer(1024);
        this.sendPacket = new Buffer(1024);
        this.port = port;
        this.host = host;
        this.socketFactoryClassName = socketFactoryClassName;
        this.socketFactory = createSocketFactory();
        this.exceptionInterceptor = this.connection.getExceptionInterceptor();
        try {
            this.mysqlConnection = this.socketFactory.connect(this.host, this.port, props);
            if (socketTimeout != 0) try {
                this.mysqlConnection.setSoTimeout(socketTimeout);
            } catch (Exception ex) {
            }
            this.mysqlConnection = this.socketFactory.beforeHandshake();
            if (this.connection.getUseReadAheadInput()) {
                this.mysqlInput = (InputStream) new ReadAheadInputStream(this.mysqlConnection.getInputStream(), 16384, this.connection.getTraceProtocol(), this.connection.getLog());
            } else if (this.connection.useUnbufferedInput()) {
                this.mysqlInput = this.mysqlConnection.getInputStream();
            } else {
                this.mysqlInput = new BufferedInputStream(this.mysqlConnection.getInputStream(), 16384);
            }
            this.mysqlOutput = new BufferedOutputStream(this.mysqlConnection.getOutputStream(), 16384);
            this.isInteractiveClient = this.connection.getInteractiveClient();
            this.profileSql = this.connection.getProfileSql();
            this.autoGenerateTestcaseScript = this.connection.getAutoGenerateTestcaseScript();
            this.needToGrabQueryFromPacket = (this.profileSql || this.logSlowQueries || this.autoGenerateTestcaseScript);
            if (this.connection.getUseNanosForElapsedTime() && Util.nanoTimeAvailable()) {
                this.useNanosForElapsedTime = true;
                this.queryTimingUnits = Messages.getString("Nanoseconds");
            } else {
                this.queryTimingUnits = Messages.getString("Milliseconds");
            }
            if (this.connection.getLogSlowQueries()) calculateSlowQueryThreshold();
        } catch (IOException ioEx) {
            throw SQLError.createCommunicationsException(this.connection, 0L, 0L, ioEx, getExceptionInterceptor());
        }
    }

    static int getMaxBuf() {
        return maxBufferSize;
    }

    private static final String getPacketDumpToLog(Buffer packetToDump, int packetLength) {
        if (packetLength < 1024) {
            return packetToDump.dump(packetLength);
        }

        StringBuffer packetDumpBuf = new StringBuffer(4096);
        packetDumpBuf.append(packetToDump.dump(1024));
        packetDumpBuf.append(Messages.getString("MysqlIO.36"));
        packetDumpBuf.append(1024);
        packetDumpBuf.append(Messages.getString("MysqlIO.37"));

        return packetDumpBuf.toString();
    }

    public static boolean useBufferRowExplicit(Field[] fields) {
        if (fields == null) {
            return false;
        }

        for (int i = 0; i < fields.length; i++) {
            switch (fields[i].getSQLType()) {
                case -4:
                case -1:
                case 2004:
                case 2005:
                    return true;
            }

        }
        return false;
    }

    public boolean hasLongColumnInfo() {
        return this.hasLongColumnInfo;
    }

    protected boolean isDataAvailable() throws SQLException {
        try {
            return (this.mysqlInput.available() > 0);
        } catch (IOException ioEx) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
        }
    }

    protected long getLastPacketSentTimeMs() {
        return this.lastPacketSentTimeMs;
    }

    protected long getLastPacketReceivedTimeMs() {
        return this.lastPacketReceivedTimeMs;
    }

    protected ResultSetImpl getResultSet(StatementImpl callingStatement, long columnCount, int maxRows, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, boolean isBinaryEncoded, Field[] metadataFromCache) throws SQLException {
        Field[] fields = null;

        if (metadataFromCache == null) {
            fields = new Field[(int) columnCount];

            for (int i = 0; i < columnCount; i++) {
                Buffer fieldPacket = null;

                fieldPacket = readPacket();
                fields[i] = unpackField(fieldPacket, false);
            }
        } else {
            for (int i = 0; i < columnCount; i++) {
                skipPacket();
            }
        }

        Buffer packet = reuseAndReadPacket(this.reusablePacket);

        readServerStatusForResultSets(packet);

        if (this.connection.versionMeetsMinimum(5, 0, 2) && this.connection.getUseCursorFetch() && isBinaryEncoded && callingStatement != null && callingStatement.getFetchSize() != 0 && callingStatement.getResultSetType() == 1003) {

            ServerPreparedStatement prepStmt = (ServerPreparedStatement) callingStatement;

            boolean usingCursor = true;

            if (this.connection.versionMeetsMinimum(5, 0, 5)) {
                usingCursor = ((this.serverStatus & 0x40) != 0);
            }

            if (usingCursor) {
                RowData rows = new RowDataCursor(this, prepStmt, fields);

                ResultSetImpl resultSetImpl = buildResultSetWithRows(callingStatement, catalog, fields, rows, resultSetType, resultSetConcurrency, isBinaryEncoded);

                if (usingCursor) {
                    resultSetImpl.setFetchSize(callingStatement.getFetchSize());
                }

                return resultSetImpl;
            }
        }

        RowData rowData = null;

        if (!streamResults) {
            rowData = readSingleRowSet(columnCount, maxRows, resultSetConcurrency, isBinaryEncoded, (metadataFromCache == null) ? fields : metadataFromCache);
        } else {

            rowData = new RowDataDynamic(this, (int) columnCount, (metadataFromCache == null) ? fields : metadataFromCache, isBinaryEncoded);

            this.streamingData = rowData;
        }

        ResultSetImpl rs = buildResultSetWithRows(callingStatement, catalog, (metadataFromCache == null) ? fields : metadataFromCache, rowData, resultSetType, resultSetConcurrency, isBinaryEncoded);

        return rs;
    }

    protected NetworkResources getNetworkResources() {
        return new NetworkResources(this.mysqlConnection, this.mysqlInput, this.mysqlOutput);
    }

    protected final void forceClose() {
        try {
            getNetworkResources().forceClose();
        } finally {
            this.mysqlConnection = null;
            this.mysqlInput = null;
            this.mysqlOutput = null;
        }
    }

    protected final void skipPacket() throws SQLException {
        try {
            int lengthRead = readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4);

            if (lengthRead < 4) {
                forceClose();
                throw new IOException(Messages.getString("MysqlIO.1"));
            }

            int packetLength = (this.packetHeaderBuf[0] & 0xFF) + ((this.packetHeaderBuf[1] & 0xFF) << 8) + ((this.packetHeaderBuf[2] & 0xFF) << 16);

            if (this.traceProtocol) {
                StringBuffer traceMessageBuf = new StringBuffer();

                traceMessageBuf.append(Messages.getString("MysqlIO.2"));
                traceMessageBuf.append(packetLength);
                traceMessageBuf.append(Messages.getString("MysqlIO.3"));
                traceMessageBuf.append(StringUtils.dumpAsHex(this.packetHeaderBuf, 4));

                this.connection.getLog().logTrace(traceMessageBuf.toString());
            }

            byte multiPacketSeq = this.packetHeaderBuf[3];

            if (!this.packetSequenceReset) {
                if (this.enablePacketDebug && this.checkPacketSequence) {
                    checkPacketSequencing(multiPacketSeq);
                }
            } else {
                this.packetSequenceReset = false;
            }

            this.readPacketSequence = multiPacketSeq;

            skipFully(this.mysqlInput, packetLength);
        } catch (IOException ioEx) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
        } catch (OutOfMemoryError oom) {
            try {
                this.connection.realClose(false, false, true, oom);
            } catch (Exception ex) {
            }

            throw oom;
        }
    }

    protected final Buffer readPacket() throws SQLException {
        try {
            int lengthRead = readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4);

            if (lengthRead < 4) {
                forceClose();
                throw new IOException(Messages.getString("MysqlIO.1"));
            }

            int packetLength = (this.packetHeaderBuf[0] & 0xFF) + ((this.packetHeaderBuf[1] & 0xFF) << 8) + ((this.packetHeaderBuf[2] & 0xFF) << 16);

            if (packetLength > this.maxAllowedPacket) {
                throw new PacketTooBigException(packetLength, this.maxAllowedPacket);
            }

            if (this.traceProtocol) {
                StringBuffer traceMessageBuf = new StringBuffer();

                traceMessageBuf.append(Messages.getString("MysqlIO.2"));
                traceMessageBuf.append(packetLength);
                traceMessageBuf.append(Messages.getString("MysqlIO.3"));
                traceMessageBuf.append(StringUtils.dumpAsHex(this.packetHeaderBuf, 4));

                this.connection.getLog().logTrace(traceMessageBuf.toString());
            }

            byte multiPacketSeq = this.packetHeaderBuf[3];

            if (!this.packetSequenceReset) {
                if (this.enablePacketDebug && this.checkPacketSequence) {
                    checkPacketSequencing(multiPacketSeq);
                }
            } else {
                this.packetSequenceReset = false;
            }

            this.readPacketSequence = multiPacketSeq;

            byte[] buffer = new byte[packetLength + 1];
            int numBytesRead = readFully(this.mysqlInput, buffer, 0, packetLength);

            if (numBytesRead != packetLength) {
                throw new IOException("Short read, expected " + packetLength + " bytes, only read " + numBytesRead);
            }

            buffer[packetLength] = 0;

            Buffer packet = new Buffer(buffer);
            packet.setBufLength(packetLength + 1);

            if (this.traceProtocol) {
                StringBuffer traceMessageBuf = new StringBuffer();

                traceMessageBuf.append(Messages.getString("MysqlIO.4"));
                traceMessageBuf.append(getPacketDumpToLog(packet, packetLength));

                this.connection.getLog().logTrace(traceMessageBuf.toString());
            }

            if (this.enablePacketDebug) {
                enqueuePacketForDebugging(false, false, 0, this.packetHeaderBuf, packet);
            }

            if (this.connection.getMaintainTimeStats()) {
                this.lastPacketReceivedTimeMs = System.currentTimeMillis();
            }

            return packet;
        } catch (IOException ioEx) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
        } catch (OutOfMemoryError oom) {
            try {
                this.connection.realClose(false, false, true, oom);
            } catch (Exception ex) {
            }

            throw oom;
        }
    }

    protected final Field unpackField(Buffer packet, boolean extractDefaultValues) throws SQLException {
        if (this.use41Extensions) {

            if (this.has41NewNewProt) {

                int catalogNameStart = packet.getPosition() + 1;
                int catalogNameLength = packet.fastSkipLenString();
                catalogNameStart = adjustStartForFieldLength(catalogNameStart, catalogNameLength);
            }

            int databaseNameStart = packet.getPosition() + 1;
            int databaseNameLength = packet.fastSkipLenString();
            databaseNameStart = adjustStartForFieldLength(databaseNameStart, databaseNameLength);

            int i = packet.getPosition() + 1;
            int j = packet.fastSkipLenString();
            i = adjustStartForFieldLength(i, j);

            int originalTableNameStart = packet.getPosition() + 1;
            int originalTableNameLength = packet.fastSkipLenString();
            originalTableNameStart = adjustStartForFieldLength(originalTableNameStart, originalTableNameLength);

            int k = packet.getPosition() + 1;
            int m = packet.fastSkipLenString();

            k = adjustStartForFieldLength(k, m);

            int originalColumnNameStart = packet.getPosition() + 1;
            int originalColumnNameLength = packet.fastSkipLenString();
            originalColumnNameStart = adjustStartForFieldLength(originalColumnNameStart, originalColumnNameLength);

            packet.readByte();

            short charSetNumber = (short) packet.readInt();

            long l = 0L;

            if (this.has41NewNewProt) {
                l = packet.readLong();
            } else {
                l = packet.readLongInt();
            }

            int n = packet.readByte() & 0xFF;

            short s1 = 0;

            if (this.hasLongColumnInfo) {
                s1 = (short) packet.readInt();
            } else {
                s1 = (short) (packet.readByte() & 0xFF);
            }

            int i1 = packet.readByte() & 0xFF;

            int defaultValueStart = -1;
            int defaultValueLength = -1;

            if (extractDefaultValues) {
                defaultValueStart = packet.getPosition() + 1;
                defaultValueLength = packet.fastSkipLenString();
            }

            Field field1 = new Field(this.connection, packet.getByteBuffer(), databaseNameStart, databaseNameLength, i, j, originalTableNameStart, originalTableNameLength, k, m, originalColumnNameStart, originalColumnNameLength, l, n, s1, i1, defaultValueStart, defaultValueLength, charSetNumber);

            return field1;
        }

        int tableNameStart = packet.getPosition() + 1;
        int tableNameLength = packet.fastSkipLenString();
        tableNameStart = adjustStartForFieldLength(tableNameStart, tableNameLength);

        int nameStart = packet.getPosition() + 1;
        int nameLength = packet.fastSkipLenString();
        nameStart = adjustStartForFieldLength(nameStart, nameLength);

        int colLength = packet.readnBytes();
        int colType = packet.readnBytes();
        packet.readByte();

        short colFlag = 0;

        if (this.hasLongColumnInfo) {
            colFlag = (short) packet.readInt();
        } else {
            colFlag = (short) (packet.readByte() & 0xFF);
        }

        int colDecimals = packet.readByte() & 0xFF;

        if (this.colDecimalNeedsBump) {
            colDecimals++;
        }

        Field field = new Field(this.connection, packet.getByteBuffer(), nameStart, nameLength, tableNameStart, tableNameLength, colLength, colType, colFlag, colDecimals);

        return field;
    }

    private int adjustStartForFieldLength(int nameStart, int nameLength) {
        if (nameLength < 251) {
            return nameStart;
        }

        if (nameLength >= 251 && nameLength < 65536) {
            return nameStart + 2;
        }

        if (nameLength >= 65536 && nameLength < 16777216) {
            return nameStart + 3;
        }

        return nameStart + 8;
    }

    protected boolean isSetNeededForAutoCommitMode(boolean autoCommitFlag) {
        if (this.use41Extensions && this.connection.getElideSetAutoCommits()) {
            boolean autoCommitModeOnServer = ((this.serverStatus & 0x2) != 0);

            if (!autoCommitFlag && versionMeetsMinimum(5, 0, 0)) {

                boolean inTransactionOnServer = ((this.serverStatus & 0x1) != 0);

                return !inTransactionOnServer;
            }

            return (autoCommitModeOnServer != autoCommitFlag);
        }

        return true;
    }

    protected boolean inTransactionOnServer() {
        return ((this.serverStatus & 0x1) != 0);
    }

    protected void changeUser(String userName, String password, String database) throws SQLException {
        this.packetSequence = -1;
        this.compressedPacketSequence = -1;

        int passwordLength = 16;
        int userLength = (userName != null) ? userName.length() : 0;
        int databaseLength = (database != null) ? database.length() : 0;

        int packLength = (userLength + passwordLength + databaseLength) * 2 + 7 + 4 + 33;

        if ((this.serverCapabilities & 0x80000) != 0) {

            proceedHandshakeWithPluggableAuthentication(userName, password, database, null);
        } else if ((this.serverCapabilities & 0x8000) != 0) {
            Buffer changeUserPacket = new Buffer(packLength + 1);
            changeUserPacket.writeByte((byte) 17);

            if (versionMeetsMinimum(4, 1, 1)) {
                secureAuth411(changeUserPacket, packLength, userName, password, database, false);
            } else {

                secureAuth(changeUserPacket, packLength, userName, password, database, false);
            }

        } else {

            Buffer packet = new Buffer(packLength);
            packet.writeByte((byte) 17);

            packet.writeString(userName);

            if (this.protocolVersion > 9) {
                packet.writeString(Util.newCrypt(password, this.seed));
            } else {
                packet.writeString(Util.oldCrypt(password, this.seed));
            }

            boolean localUseConnectWithDb = (this.useConnectWithDb && database != null && database.length() > 0);

            if (localUseConnectWithDb) {
                packet.writeString(database);
            }

            send(packet, packet.getPosition());
            checkErrorPacket();

            if (!localUseConnectWithDb) {
                changeDatabaseTo(database);
            }
        }
    }

    protected Buffer checkErrorPacket() throws SQLException {
        return checkErrorPacket(-1);
    }

    protected void checkForCharsetMismatch() {
        if (this.connection.getUseUnicode() && this.connection.getEncoding() != null) {

            String encodingToCheck = jvmPlatformCharset;

            if (encodingToCheck == null) {
                encodingToCheck = System.getProperty("file.encoding");
            }

            if (encodingToCheck == null) {
                this.platformDbCharsetMatches = false;
            } else {
                this.platformDbCharsetMatches = encodingToCheck.equals(this.connection.getEncoding());
            }
        }
    }

    protected void clearInputStream() throws SQLException {
        try {
            int len = this.mysqlInput.available();

            while (len > 0) {
                this.mysqlInput.skip(len);
                len = this.mysqlInput.available();
            }
        } catch (IOException ioEx) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
        }
    }

    protected void resetReadPacketSequence() {
        this.readPacketSequence = 0;
    }

    protected void dumpPacketRingBuffer() throws SQLException {
        if (this.packetDebugRingBuffer != null && this.connection.getEnablePacketDebug()) {

            StringBuffer dumpBuffer = new StringBuffer();

            dumpBuffer.append("Last " + this.packetDebugRingBuffer.size() + " packets received from server, from oldest->newest:\n");

            dumpBuffer.append("\n");

            Iterator<StringBuffer> ringBufIter = this.packetDebugRingBuffer.iterator();
            while (ringBufIter.hasNext()) {
                dumpBuffer.append(ringBufIter.next());
                dumpBuffer.append("\n");
            }

            this.connection.getLog().logTrace(dumpBuffer.toString());
        }
    }

    protected void explainSlowQuery(byte[] querySQL, String truncatedQuery) throws SQLException {
        if (StringUtils.startsWithIgnoreCaseAndWs(truncatedQuery, "SELECT")) {

            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                stmt = (PreparedStatement) this.connection.clientPrepareStatement("EXPLAIN ?");
                stmt.setBytesNoEscapeNoQuotes(1, querySQL);
                rs = stmt.executeQuery();

                StringBuffer explainResults = new StringBuffer(Messages.getString("MysqlIO.8") + truncatedQuery + Messages.getString("MysqlIO.9"));

                ResultSetUtil.appendResultSetSlashGStyle(explainResults, rs);

                this.connection.getLog().logWarn(explainResults.toString());
            } catch (SQLException sqlEx) {
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (stmt != null) {
                    stmt.close();
                }
            }

        }
    }

    final int getServerMajorVersion() {
        return this.serverMajorVersion;
    }

    final int getServerMinorVersion() {
        return this.serverMinorVersion;
    }

    final int getServerSubMinorVersion() {
        return this.serverSubMinorVersion;
    }

    String getServerVersion() {
        return this.serverVersion;
    }

    void doHandshake(String user, String password, String database) throws SQLException {
        this.checkPacketSequence = false;
        this.readPacketSequence = 0;

        Buffer buf = readPacket();

        this.protocolVersion = buf.readByte();

        if (this.protocolVersion == -1) {
            try {
                this.mysqlConnection.close();
            } catch (Exception e) {
            }

            int errno = 2000;

            errno = buf.readInt();

            String serverErrorMessage = buf.readString("ASCII", getExceptionInterceptor());

            StringBuffer errorBuf = new StringBuffer(Messages.getString("MysqlIO.10"));

            errorBuf.append(serverErrorMessage);
            errorBuf.append("\"");

            String xOpen = SQLError.mysqlToSqlState(errno, this.connection.getUseSqlStateCodes());

            throw SQLError.createSQLException(SQLError.get(xOpen) + ", " + errorBuf.toString(), xOpen, errno, getExceptionInterceptor());
        }

        this.serverVersion = buf.readString("ASCII", getExceptionInterceptor());

        int point = this.serverVersion.indexOf('.');

        if (point != -1) {
            try {
                int n = Integer.parseInt(this.serverVersion.substring(0, point));
                this.serverMajorVersion = n;
            } catch (NumberFormatException NFE1) {
            }

            String remaining = this.serverVersion.substring(point + 1, this.serverVersion.length());

            point = remaining.indexOf('.');

            if (point != -1) {
                try {
                    int n = Integer.parseInt(remaining.substring(0, point));
                    this.serverMinorVersion = n;
                } catch (NumberFormatException nfe) {
                }

                remaining = remaining.substring(point + 1, remaining.length());

                int pos = 0;

                while (pos < remaining.length() &&
                        remaining.charAt(pos) >= '0' && remaining.charAt(pos) <= '9') {

                    pos++;
                }

                try {
                    int n = Integer.parseInt(remaining.substring(0, pos));
                    this.serverSubMinorVersion = n;
                } catch (NumberFormatException nfe) {
                }
            }
        }

        if (versionMeetsMinimum(4, 0, 8)) {
            this.maxThreeBytes = 16777215;
            this.useNewLargePackets = true;
        } else {
            this.maxThreeBytes = 16581375;
            this.useNewLargePackets = false;
        }

        this.colDecimalNeedsBump = versionMeetsMinimum(3, 23, 0);
        this.colDecimalNeedsBump = !versionMeetsMinimum(3, 23, 15);
        this.useNewUpdateCounts = versionMeetsMinimum(3, 22, 5);

        this.threadId = buf.readLong();
        this.seed = buf.readString("ASCII", getExceptionInterceptor());

        this.serverCapabilities = 0;

        if (buf.getPosition() < buf.getBufLength()) {
            this.serverCapabilities = buf.readInt();
        }

        if (versionMeetsMinimum(4, 1, 1) || (this.protocolVersion > 9 && (this.serverCapabilities & 0x200) != 0)) {
            int position = buf.getPosition();

            this.serverCharsetIndex = buf.readByte() & 0xFF;
            this.serverStatus = buf.readInt();
            checkTransactionState(0);

            this.serverCapabilities += 65536 * buf.readInt();
            this.authPluginDataLength = buf.readByte() & 0xFF;

            buf.setPosition(position + 16);

            String seedPart2 = buf.readString("ASCII", getExceptionInterceptor());
            StringBuffer newSeed = new StringBuffer(20);
            newSeed.append(this.seed);
            newSeed.append(seedPart2);
            this.seed = newSeed.toString();
        }

        if ((this.serverCapabilities & 0x20) != 0 && this.connection.getUseCompression()) {
            this.clientParam |= 0x20L;
        }

        this.useConnectWithDb = (database != null && database.length() > 0 && !this.connection.getCreateDatabaseIfNotExist());

        if (this.useConnectWithDb) {
            this.clientParam |= 0x8L;
        }

        if ((this.serverCapabilities & 0x800) == 0 && this.connection.getUseSSL()) {

            if (this.connection.getRequireSSL()) {
                this.connection.close();
                forceClose();
                throw SQLError.createSQLException(Messages.getString("MysqlIO.15"), "08001", getExceptionInterceptor());
            }

            this.connection.setUseSSL(false);
        }

        if ((this.serverCapabilities & 0x4) != 0) {

            this.clientParam |= 0x4L;
            this.hasLongColumnInfo = true;
        }

        if (!this.connection.getUseAffectedRows()) {
            this.clientParam |= 0x2L;
        }

        if (this.connection.getAllowLoadLocalInfile()) {
            this.clientParam |= 0x80L;
        }

        if (this.isInteractiveClient) {
            this.clientParam |= 0x400L;
        }

        if ((this.serverCapabilities & 0x80000) != 0) {
            proceedHandshakeWithPluggableAuthentication(user, password, database, buf);

            return;
        }

        if (this.protocolVersion > 9) {
            this.clientParam |= 0x1L;
        } else {
            this.clientParam &= 0xFFFFFFFFFFFFFFFEL;
        }

        if (versionMeetsMinimum(4, 1, 0) || (this.protocolVersion > 9 && (this.serverCapabilities & 0x4000) != 0)) {
            if (versionMeetsMinimum(4, 1, 1) || (this.protocolVersion > 9 && (this.serverCapabilities & 0x200) != 0)) {
                this.clientParam |= 0x200L;
                this.has41NewNewProt = true;

                this.clientParam |= 0x2000L;

                this.clientParam |= 0x20000L;

                if (this.connection.getAllowMultiQueries()) {
                    this.clientParam |= 0x10000L;
                }
            } else {
                this.clientParam |= 0x4000L;
                this.has41NewNewProt = false;
            }

            this.use41Extensions = true;
        }

        int passwordLength = 16;
        int userLength = (user != null) ? user.length() : 0;
        int databaseLength = (database != null) ? database.length() : 0;

        int packLength = (userLength + passwordLength + databaseLength) * 2 + 7 + 4 + 33;

        Buffer packet = null;

        if (!this.connection.getUseSSL()) {
            if ((this.serverCapabilities & 0x8000) != 0) {
                this.clientParam |= 0x8000L;

                if (versionMeetsMinimum(4, 1, 1) || (this.protocolVersion > 9 && (this.serverCapabilities & 0x200) != 0)) {
                    secureAuth411(null, packLength, user, password, database, true);
                } else {

                    secureAuth(null, packLength, user, password, database, true);
                }
            } else {

                packet = new Buffer(packLength);

                if ((this.clientParam & 0x4000L) != 0L) {
                    if (versionMeetsMinimum(4, 1, 1) || (this.protocolVersion > 9 && (this.serverCapabilities & 0x200) != 0)) {
                        packet.writeLong(this.clientParam);
                        packet.writeLong(this.maxThreeBytes);

                        packet.writeByte((byte) 8);

                        packet.writeBytesNoNull(new byte[23]);
                    } else {
                        packet.writeLong(this.clientParam);
                        packet.writeLong(this.maxThreeBytes);
                    }
                } else {
                    packet.writeInt((int) this.clientParam);
                    packet.writeLongInt(this.maxThreeBytes);
                }

                packet.writeString(user, "Cp1252", this.connection);

                if (this.protocolVersion > 9) {
                    packet.writeString(Util.newCrypt(password, this.seed), "Cp1252", this.connection);
                } else {
                    packet.writeString(Util.oldCrypt(password, this.seed), "Cp1252", this.connection);
                }

                if (this.useConnectWithDb) {
                    packet.writeString(database, "Cp1252", this.connection);
                }

                send(packet, packet.getPosition());
            }
        } else {
            negotiateSSLConnection(user, password, database, packLength);

            if ((this.serverCapabilities & 0x8000) != 0) {
                if (versionMeetsMinimum(4, 1, 1)) {
                    secureAuth411(null, packLength, user, password, database, true);
                } else {
                    secureAuth411(null, packLength, user, password, database, true);
                }
            } else {

                packet = new Buffer(packLength);

                if (this.use41Extensions) {
                    packet.writeLong(this.clientParam);
                    packet.writeLong(this.maxThreeBytes);
                } else {
                    packet.writeInt((int) this.clientParam);
                    packet.writeLongInt(this.maxThreeBytes);
                }

                packet.writeString(user);

                if (this.protocolVersion > 9) {
                    packet.writeString(Util.newCrypt(password, this.seed));
                } else {
                    packet.writeString(Util.oldCrypt(password, this.seed));
                }

                if ((this.serverCapabilities & 0x8) != 0 && database != null && database.length() > 0) {
                    packet.writeString(database);
                }

                send(packet, packet.getPosition());
            }
        }

        if (!versionMeetsMinimum(4, 1, 1) && this.protocolVersion > 9 && (this.serverCapabilities & 0x200) != 0) {
            checkErrorPacket();
        }

        if ((this.serverCapabilities & 0x20) != 0 && this.connection.getUseCompression()) {

            this.deflater = new Deflater();
            this.useCompression = true;
            this.mysqlInput = new CompressedInputStream(this.connection, this.mysqlInput);
        }

        if (!this.useConnectWithDb) {
            changeDatabaseTo(database);
        }

        try {
            this.mysqlConnection = this.socketFactory.afterHandshake();
        } catch (IOException ioEx) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
        }
    }

    private void loadAuthenticationPlugins() throws SQLException {
        this.defaultAuthenticationPlugin = this.connection.getDefaultAuthenticationPlugin();
        if (this.defaultAuthenticationPlugin == null || "".equals(this.defaultAuthenticationPlugin.trim()))
            throw SQLError.createSQLException(Messages.getString("Connection.BadDefaultAuthenticationPlugin", new Object[]{this.defaultAuthenticationPlugin}), getExceptionInterceptor());
        String disabledPlugins = this.connection.getDisabledAuthenticationPlugins();
        if (disabledPlugins != null && !"".equals(disabledPlugins)) {
            this.disabledAuthenticationPlugins = new ArrayList<String>();
            List<String> pluginsToDisable = StringUtils.split(disabledPlugins, ",", true);
            Iterator<String> iter = pluginsToDisable.iterator();
            while (iter.hasNext()) this.disabledAuthenticationPlugins.add(iter.next());
        }
        this.authenticationPlugins = new HashMap<String, AuthenticationPlugin>();
        MysqlOldPasswordPlugin mysqlOldPasswordPlugin = new MysqlOldPasswordPlugin();
        mysqlOldPasswordPlugin.init(this.connection, this.connection.getProperties());
        boolean defaultIsFound = addAuthenticationPlugin((AuthenticationPlugin) mysqlOldPasswordPlugin);
        MysqlNativePasswordPlugin mysqlNativePasswordPlugin = new MysqlNativePasswordPlugin();
        mysqlNativePasswordPlugin.init(this.connection, this.connection.getProperties());
        if (addAuthenticationPlugin((AuthenticationPlugin) mysqlNativePasswordPlugin)) defaultIsFound = true;
        MysqlClearPasswordPlugin mysqlClearPasswordPlugin = new MysqlClearPasswordPlugin();
        mysqlClearPasswordPlugin.init(this.connection, this.connection.getProperties());
        if (addAuthenticationPlugin((AuthenticationPlugin) mysqlClearPasswordPlugin)) defaultIsFound = true;
        Sha256PasswordPlugin sha256PasswordPlugin = new Sha256PasswordPlugin();
        sha256PasswordPlugin.init(this.connection, this.connection.getProperties());
        if (addAuthenticationPlugin((AuthenticationPlugin) sha256PasswordPlugin)) defaultIsFound = true;
        String authenticationPluginClasses = this.connection.getAuthenticationPlugins();
        if (authenticationPluginClasses != null && !"".equals(authenticationPluginClasses)) {
            List<Extension> plugins = Util.loadExtensions(this.connection, this.connection.getProperties(), authenticationPluginClasses, "Connection.BadAuthenticationPlugin", getExceptionInterceptor());
            for (Extension object : plugins) {
                AuthenticationPlugin authenticationPlugin = (AuthenticationPlugin) object;
                if (addAuthenticationPlugin(authenticationPlugin)) defaultIsFound = true;
            }
        }
        if (!defaultIsFound)
            throw SQLError.createSQLException(Messages.getString("Connection.DefaultAuthenticationPluginIsNotListed", new Object[]{this.defaultAuthenticationPlugin}), getExceptionInterceptor());
    }

    private boolean addAuthenticationPlugin(AuthenticationPlugin plugin) throws SQLException {
        boolean isDefault = false;
        String pluginClassName = plugin.getClass().getName();
        String pluginProtocolName = plugin.getProtocolPluginName();
        boolean disabledByClassName = (this.disabledAuthenticationPlugins != null && this.disabledAuthenticationPlugins.contains(pluginClassName));
        boolean disabledByMechanism = (this.disabledAuthenticationPlugins != null && this.disabledAuthenticationPlugins.contains(pluginProtocolName));
        if (disabledByClassName || disabledByMechanism) {
            if (this.defaultAuthenticationPlugin.equals(pluginClassName))
                throw SQLError.createSQLException(Messages.getString("Connection.BadDisabledAuthenticationPlugin", new Object[]{disabledByClassName ? pluginClassName : pluginProtocolName}), getExceptionInterceptor());
        } else {
            this.authenticationPlugins.put(pluginProtocolName, plugin);
            if (this.defaultAuthenticationPlugin.equals(pluginClassName)) {
                this.defaultAuthenticationPluginProtocolName = pluginProtocolName;
                isDefault = true;
            }
        }
        return isDefault;
    }

    private AuthenticationPlugin getAuthenticationPlugin(String pluginName) throws SQLException {
        AuthenticationPlugin plugin = this.authenticationPlugins.get(pluginName);
        if (plugin != null && !plugin.isReusable()) try {
            plugin = (AuthenticationPlugin) plugin.getClass().newInstance();
            plugin.init(this.connection, this.connection.getProperties());
        } catch (Throwable t) {
            SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.BadAuthenticationPlugin", new Object[]{plugin.getClass().getName()}), getExceptionInterceptor());
            sqlEx.initCause(t);
            throw sqlEx;
        }
        return plugin;
    }

    private void checkConfidentiality(AuthenticationPlugin plugin) throws SQLException {
        if (plugin.requiresConfidentiality() && (!this.connection.getUseSSL() || !this.connection.getRequireSSL()))
            throw SQLError.createSQLException(Messages.getString("Connection.AuthenticationPluginRequiresSSL", new Object[]{plugin.getProtocolPluginName()}), getExceptionInterceptor());
    }

    private void proceedHandshakeWithPluggableAuthentication(String user, String password, String database, Buffer challenge) throws SQLException {
        if (this.authenticationPlugins == null) loadAuthenticationPlugins();
        int passwordLength = 16;
        int userLength = (user != null) ? user.length() : 0;
        int databaseLength = (database != null) ? database.length() : 0;
        int packLength = (userLength + passwordLength + databaseLength) * 2 + 7 + 4 + 33;
        AuthenticationPlugin plugin = null;
        Buffer fromServer = null;
        ArrayList<Buffer> toServer = new ArrayList<Buffer>();
        Boolean done = null;
        Buffer last_sent = null;
        boolean old_raw_challenge = false;
        int counter = 100;
        while (0 < counter--) {
            if (done == null) {
                if (challenge != null) {
                    this.clientParam |= 0xAA201L;
                    if (this.connection.getAllowMultiQueries()) this.clientParam |= 0x10000L;
                    if ((this.serverCapabilities & 0x400000) != 0 && !this.connection.getDisconnectOnExpiredPasswords())
                        this.clientParam |= 0x400000L;
                    this.has41NewNewProt = true;
                    this.use41Extensions = true;
                    if (this.connection.getUseSSL()) negotiateSSLConnection(user, password, database, packLength);
                    String pluginName = null;
                    if ((this.serverCapabilities & 0x80000) != 0)
                        if (!versionMeetsMinimum(5, 5, 10) || (versionMeetsMinimum(5, 6, 0) && !versionMeetsMinimum(5, 6, 2))) {
                            pluginName = challenge.readString("ASCII", getExceptionInterceptor(), this.authPluginDataLength);
                        } else {
                            pluginName = challenge.readString("ASCII", getExceptionInterceptor());
                        }
                    plugin = getAuthenticationPlugin(pluginName);
                    if (plugin == null) plugin = getAuthenticationPlugin(this.defaultAuthenticationPluginProtocolName);
                    checkConfidentiality(plugin);
                    fromServer = new Buffer(StringUtils.getBytes(this.seed));
                } else {
                    plugin = getAuthenticationPlugin(this.defaultAuthenticationPluginProtocolName);
                    checkConfidentiality(plugin);
                }
            } else {
                challenge = checkErrorPacket();
                old_raw_challenge = false;
                if (challenge.isOKPacket()) {
                    if (!done.booleanValue())
                        throw SQLError.createSQLException(Messages.getString("Connection.UnexpectedAuthenticationApproval", new Object[]{plugin.getProtocolPluginName()}), getExceptionInterceptor());
                    plugin.destroy();
                    break;
                }
                if (challenge.isAuthMethodSwitchRequestPacket()) {
                    String pluginName = challenge.readString("ASCII", getExceptionInterceptor());
                    if (plugin != null && !plugin.getProtocolPluginName().equals(pluginName)) {
                        plugin.destroy();
                        plugin = getAuthenticationPlugin(pluginName);
                        if (plugin == null)
                            throw SQLError.createSQLException(Messages.getString("Connection.BadAuthenticationPlugin", new Object[]{pluginName}), getExceptionInterceptor());
                    }
                    checkConfidentiality(plugin);
                    fromServer = new Buffer(StringUtils.getBytes(challenge.readString("ASCII", getExceptionInterceptor())));
                } else if (versionMeetsMinimum(5, 5, 16)) {
                    fromServer = new Buffer(challenge.getBytes(challenge.getPosition(), challenge.getBufLength() - challenge.getPosition()));
                } else {
                    old_raw_challenge = true;
                    fromServer = new Buffer(challenge.getBytes(challenge.getPosition() - 1, challenge.getBufLength() - challenge.getPosition() + 1));
                }
            }
            try {
                plugin.setAuthenticationParameters(user, password);
                done = Boolean.valueOf(plugin.nextAuthenticationStep(fromServer, toServer));
            } catch (SQLException e) {
                throw SQLError.createSQLException(e.getMessage(), e.getSQLState(), e, getExceptionInterceptor());
            }
            if (toServer.size() > 0) {
                if (challenge == null) {
                    String enc = this.connection.getEncoding();
                    int charsetIndex = 0;
                    if (enc != null) {
                        charsetIndex = CharsetMapping.getCharsetIndexForMysqlEncodingName(CharsetMapping.getMysqlEncodingForJavaEncoding(enc, this.connection));
                    } else {
                        enc = "utf-8";
                    }
                    if (charsetIndex == 0) charsetIndex = 33;
                    last_sent = new Buffer(packLength + 1);
                    last_sent.writeByte((byte) 17);
                    last_sent.writeString(user, enc, this.connection);
                    last_sent.writeByte((byte) ((Buffer) toServer.get(0)).getBufLength());
                    last_sent.writeBytesNoNull(((Buffer) toServer.get(0)).getByteBuffer(), 0, ((Buffer) toServer.get(0)).getBufLength());
                    if (this.useConnectWithDb) {
                        last_sent.writeString(database, enc, this.connection);
                    } else {
                        last_sent.writeByte((byte) 0);
                    }
                    last_sent.writeByte((byte) (charsetIndex % 256));
                    if (charsetIndex > 255) {
                        last_sent.writeByte((byte) (charsetIndex / 256));
                    } else {
                        last_sent.writeByte((byte) 0);
                    }
                    if ((this.serverCapabilities & 0x80000) != 0)
                        last_sent.writeString(plugin.getProtocolPluginName(), enc, this.connection);
                    send(last_sent, last_sent.getPosition());
                    continue;
                }
                if (challenge.isAuthMethodSwitchRequestPacket()) {
                    byte savePacketSequence = this.packetSequence = (byte) (this.packetSequence + 1);
                    this.packetSequence = savePacketSequence = (byte) (savePacketSequence + 1);
                    last_sent = new Buffer(((Buffer) toServer.get(0)).getBufLength() + 4);
                    last_sent.writeBytesNoNull(((Buffer) toServer.get(0)).getByteBuffer(), 0, ((Buffer) toServer.get(0)).getBufLength());
                    send(last_sent, last_sent.getPosition());
                    continue;
                }
                if (challenge.isRawPacket() || old_raw_challenge) {
                    byte savePacketSequence = this.packetSequence = (byte) (this.packetSequence + 1);
                    for (Buffer buffer : toServer) {
                        this.packetSequence = savePacketSequence = (byte) (savePacketSequence + 1);
                        last_sent = new Buffer(buffer.getBufLength() + 4);
                        last_sent.writeBytesNoNull(buffer.getByteBuffer(), 0, ((Buffer) toServer.get(0)).getBufLength());
                        send(last_sent, last_sent.getPosition());
                    }
                    continue;
                }
                last_sent = new Buffer(packLength);
                last_sent.writeLong(this.clientParam);
                last_sent.writeLong(this.maxThreeBytes);
                last_sent.writeByte((byte) 33);
                last_sent.writeBytesNoNull(new byte[23]);
                last_sent.writeString(user, "utf-8", this.connection);
                last_sent.writeByte((byte) ((Buffer) toServer.get(0)).getBufLength());
                last_sent.writeBytesNoNull(((Buffer) toServer.get(0)).getByteBuffer(), 0, ((Buffer) toServer.get(0)).getBufLength());
                if (this.useConnectWithDb) {
                    last_sent.writeString(database, "utf-8", this.connection);
                } else {
                    last_sent.writeByte((byte) 0);
                }
                if ((this.serverCapabilities & 0x80000) != 0)
                    last_sent.writeString(plugin.getProtocolPluginName(), "utf-8", this.connection);
                send(last_sent, last_sent.getPosition());
            }
        }
        if (counter == 0)
            throw SQLError.createSQLException(Messages.getString("CommunicationsException.TooManyAuthenticationPluginNegotiations"), getExceptionInterceptor());
        if ((this.serverCapabilities & 0x20) != 0 && this.connection.getUseCompression()) {
            this.deflater = new Deflater();
            this.useCompression = true;
            this.mysqlInput = new CompressedInputStream(this.connection, this.mysqlInput);
        }
        if (!this.useConnectWithDb)
            changeDatabaseTo(database);
        try {
            this.mysqlConnection = this.socketFactory.afterHandshake();
        } catch (IOException ioEx) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
        }
    }

    private void changeDatabaseTo(String database) throws SQLException {
        if (database == null || database.length() == 0)
            return;
        try {
            sendCommand(2, database, null, false, null, 0);
        } catch (Exception ex) {
            if (this.connection.getCreateDatabaseIfNotExist()) {
                sendCommand(3, "CREATE DATABASE IF NOT EXISTS " + database, null, false, null, 0);
                sendCommand(2, database, null, false, null, 0);
            } else {
                throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ex, getExceptionInterceptor());
            }
        }
    }

    final ResultSetRow nextRow(Field[] fields, int columnCount, boolean isBinaryEncoded, int resultSetConcurrency, boolean useBufferRowIfPossible, boolean useBufferRowExplicit, boolean canReuseRowPacketForBufferRow, Buffer existingRowPacket) throws SQLException {
        if (this.useDirectRowUnpack && existingRowPacket == null && !isBinaryEncoded && !useBufferRowIfPossible && !useBufferRowExplicit)
            return nextRowFast(fields, columnCount, isBinaryEncoded, resultSetConcurrency, useBufferRowIfPossible, useBufferRowExplicit, canReuseRowPacketForBufferRow);
        Buffer rowPacket = null;
        if (existingRowPacket == null) {
            rowPacket = checkErrorPacket();
            if (!useBufferRowExplicit && useBufferRowIfPossible && rowPacket.getBufLength() > this.useBufferRowSizeThreshold)
                useBufferRowExplicit = true;
        } else {
            rowPacket = existingRowPacket;
            checkErrorPacket(existingRowPacket);
        }
        if (!isBinaryEncoded) {
            rowPacket.setPosition(rowPacket.getPosition() - 1);
            if (!rowPacket.isLastDataPacket()) {
                if (resultSetConcurrency == 1008 || (!useBufferRowIfPossible && !useBufferRowExplicit)) {
                    byte[][] rowData = new byte[columnCount][];
                    for (int i = 0; i < columnCount; i++) rowData[i] = rowPacket.readLenByteArray(0);
                    return new ByteArrayRow(rowData, getExceptionInterceptor());
                }
                if (!canReuseRowPacketForBufferRow)
                    this.reusablePacket = new Buffer(rowPacket.getBufLength());
                return new BufferRow(rowPacket, fields, false, getExceptionInterceptor());
            }
            readServerStatusForResultSets(rowPacket);
            return null;
        }
        if (!rowPacket.isLastDataPacket()) {
            if (resultSetConcurrency == 1008 || (!useBufferRowIfPossible && !useBufferRowExplicit))
                return unpackBinaryResultSetRow(fields, rowPacket, resultSetConcurrency);
            if (!canReuseRowPacketForBufferRow)
                this.reusablePacket = new Buffer(rowPacket.getBufLength());
            return new BufferRow(rowPacket, fields, true, getExceptionInterceptor());
        }
        rowPacket.setPosition(rowPacket.getPosition() - 1);
        readServerStatusForResultSets(rowPacket);
        return null;
    }

    protected boolean shouldIntercept() {
        return (this.statementInterceptors != null);
    }

    final ResultSetRow nextRowFast(Field[] fields, int columnCount, boolean isBinaryEncoded, int resultSetConcurrency, boolean useBufferRowIfPossible, boolean useBufferRowExplicit, boolean canReuseRowPacket) throws SQLException {
        try {
            int lengthRead = readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4);
            if (lengthRead < 4) {
                forceClose();
                throw new RuntimeException(Messages.getString("MysqlIO.43"));
            }
            int packetLength = (this.packetHeaderBuf[0] & 0xFF) + ((this.packetHeaderBuf[1] & 0xFF) << 8) + ((this.packetHeaderBuf[2] & 0xFF) << 16);
            if (packetLength == this.maxThreeBytes) {
                reuseAndReadPacket(this.reusablePacket, packetLength);
                return nextRow(fields, columnCount, isBinaryEncoded, resultSetConcurrency, useBufferRowIfPossible, useBufferRowExplicit, canReuseRowPacket, this.reusablePacket);
            }
            if (packetLength > this.useBufferRowSizeThreshold) {
                reuseAndReadPacket(this.reusablePacket, packetLength);
                return nextRow(fields, columnCount, isBinaryEncoded, resultSetConcurrency, true, true, false, this.reusablePacket);
            }
            int remaining = packetLength;
            boolean firstTime = true;
            byte[][] rowData = (byte[][]) null;
            for (int i = 0; i < columnCount; i++) {
                int sw = this.mysqlInput.read() & 0xFF;
                remaining--;
                if (firstTime) {
                    if (sw == 255) {
                        Buffer errorPacket = new Buffer(packetLength + 4);
                        errorPacket.setPosition(0);
                        errorPacket.writeByte(this.packetHeaderBuf[0]);
                        errorPacket.writeByte(this.packetHeaderBuf[1]);
                        errorPacket.writeByte(this.packetHeaderBuf[2]);
                        errorPacket.writeByte((byte) 1);
                        errorPacket.writeByte((byte) sw);
                        readFully(this.mysqlInput, errorPacket.getByteBuffer(), 5, packetLength - 1);
                        errorPacket.setPosition(4);
                        checkErrorPacket(errorPacket);
                    }
                    if (sw == 254 && packetLength < 9) {
                        if (this.use41Extensions) {
                            this.warningCount = this.mysqlInput.read() & 0xFF | (this.mysqlInput.read() & 0xFF) << 8;
                            remaining -= 2;
                            if (this.warningCount > 0)
                                this.hadWarnings = true;
                            this.oldServerStatus = this.serverStatus;
                            this.serverStatus = this.mysqlInput.read() & 0xFF | (this.mysqlInput.read() & 0xFF) << 8;
                            checkTransactionState(this.oldServerStatus);
                            remaining -= 2;
                            if (remaining > 0)
                                skipFully(this.mysqlInput, remaining);
                        }
                        return null;
                    }
                    rowData = new byte[columnCount][];
                    firstTime = false;
                }
                int len = 0;
                switch (sw) {
                    case 251:
                        len = -1;
                        break;
                    case 252:
                        len = this.mysqlInput.read() & 0xFF | (this.mysqlInput.read() & 0xFF) << 8;
                        remaining -= 2;
                        break;
                    case 253:
                        len = this.mysqlInput.read() & 0xFF | (this.mysqlInput.read() & 0xFF) << 8 | (this.mysqlInput.read() & 0xFF) << 16;
                        remaining -= 3;
                        break;
                    case 254:
                        len = (int) ((this.mysqlInput.read() & 0xFF) | (this.mysqlInput.read() & 0xFF) << 8L | (this.mysqlInput.read() & 0xFF) << 16L | (this.mysqlInput.read() & 0xFF) << 24L | (this.mysqlInput.read() & 0xFF) << 32L | (this.mysqlInput.read() & 0xFF) << 40L | (this.mysqlInput.read() & 0xFF) << 48L | (this.mysqlInput.read() & 0xFF) << 56L);
                        remaining -= 8;
                        break;
                    default:
                        len = sw;
                        break;
                }
                if (len == -1) {
                    rowData[i] = null;
                } else if (len == 0) {
                    rowData[i] = Constants.EMPTY_BYTE_ARRAY;
                } else {
                    rowData[i] = new byte[len];
                    int bytesRead = readFully(this.mysqlInput, rowData[i], 0, len);
                    if (bytesRead != len)
                        throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, new IOException(Messages.getString("MysqlIO.43")), getExceptionInterceptor());
                    remaining -= bytesRead;
                }
            }
            if (remaining > 0)
                skipFully(this.mysqlInput, remaining);
            return new ByteArrayRow(rowData, getExceptionInterceptor());
        } catch (IOException ioEx) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
        }
    }

    final void quit() throws SQLException {
        try {
            if (!this.mysqlConnection.isClosed())
                try {
                    this.mysqlConnection.shutdownInput();
                } catch (UnsupportedOperationException ex) {
                }
        } catch (IOException ioEx) {
            this.connection.getLog().logWarn("Caught while disconnecting...", ioEx);
        } finally {
            forceClose();
        }
    }

    Buffer getSharedSendPacket() {
        if (this.sharedSendPacket == null)
            this.sharedSendPacket = new Buffer(1024);
        return this.sharedSendPacket;
    }

    void closeStreamer(RowData streamer) throws SQLException {
        if (this.streamingData == null)
            throw SQLError.createSQLException(Messages.getString("MysqlIO.17") + streamer + Messages.getString("MysqlIO.18"), getExceptionInterceptor());
        if (streamer != this.streamingData)
            throw SQLError.createSQLException(Messages.getString("MysqlIO.19") + streamer + Messages.getString("MysqlIO.20") + Messages.getString("MysqlIO.21") + Messages.getString("MysqlIO.22"), getExceptionInterceptor());
        this.streamingData = null;
    }

    boolean tackOnMoreStreamingResults(ResultSetImpl addingTo) throws SQLException {
        if ((this.serverStatus & 0x8) != 0) {
            boolean moreRowSetsExist = true;
            ResultSetImpl currentResultSet = addingTo;
            boolean firstTime = true;
            while (moreRowSetsExist && (firstTime || !currentResultSet.reallyResult())) {
                firstTime = false;
                Buffer fieldPacket = checkErrorPacket();
                fieldPacket.setPosition(0);
                Statement owningStatement = addingTo.getStatement();
                int maxRows = owningStatement.getMaxRows();
                ResultSetImpl newResultSet = readResultsForQueryOrUpdate((StatementImpl) owningStatement, maxRows, owningStatement.getResultSetType(), owningStatement.getResultSetConcurrency(), true, owningStatement.getConnection().getCatalog(), fieldPacket, addingTo.isBinaryEncoded, -1L, null);
                currentResultSet.setNextResultSet(newResultSet);
                currentResultSet = newResultSet;
                moreRowSetsExist = ((this.serverStatus & 0x8) != 0);
                if (!currentResultSet.reallyResult() && !moreRowSetsExist)
                    return false;
            }
            return true;
        }
        return false;
    }

    ResultSetImpl readAllResults(StatementImpl callingStatement, int maxRows, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Buffer resultPacket, boolean isBinaryEncoded, long preSentColumnCount, Field[] metadataFromCache) throws SQLException {
        resultPacket.setPosition(resultPacket.getPosition() - 1);
        ResultSetImpl topLevelResultSet = readResultsForQueryOrUpdate(callingStatement, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, resultPacket, isBinaryEncoded, preSentColumnCount, metadataFromCache);
        ResultSetImpl currentResultSet = topLevelResultSet;
        boolean checkForMoreResults = ((this.clientParam & 0x20000L) != 0L);
        boolean serverHasMoreResults = ((this.serverStatus & 0x8) != 0);
        if (serverHasMoreResults && streamResults) {
            if (topLevelResultSet.getUpdateCount() != -1L)
                tackOnMoreStreamingResults(topLevelResultSet);
            reclaimLargeReusablePacket();
            return topLevelResultSet;
        }
        boolean moreRowSetsExist = checkForMoreResults & serverHasMoreResults;
        while (moreRowSetsExist) {
            Buffer fieldPacket = checkErrorPacket();
            fieldPacket.setPosition(0);
            ResultSetImpl newResultSet = readResultsForQueryOrUpdate(callingStatement, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, fieldPacket, isBinaryEncoded, preSentColumnCount, metadataFromCache);
            currentResultSet.setNextResultSet(newResultSet);
            currentResultSet = newResultSet;
            moreRowSetsExist = ((this.serverStatus & 0x8) != 0);
        }
        if (!streamResults)
            clearInputStream();
        reclaimLargeReusablePacket();
        return topLevelResultSet;
    }

    void resetMaxBuf() {
        this.maxAllowedPacket = this.connection.getMaxAllowedPacket();
    }

    final Buffer sendCommand(int command, String extraData, Buffer queryPacket, boolean skipCheck, String extraDataCharEncoding, int timeoutMillis) throws SQLException {
        this.commandCount++;
        this.enablePacketDebug = this.connection.getEnablePacketDebug();
        this.readPacketSequence = 0;
        int oldTimeout = 0;
        if (timeoutMillis != 0)
            try {
                oldTimeout = this.mysqlConnection.getSoTimeout();
                this.mysqlConnection.setSoTimeout(timeoutMillis);
            } catch (SocketException e) {
                throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, e, getExceptionInterceptor());
            }
        try {
            checkForOutstandingStreamingData();
            this.oldServerStatus = this.serverStatus;
            this.serverStatus = 0;
            this.hadWarnings = false;
            this.warningCount = 0;
            this.queryNoIndexUsed = false;
            this.queryBadIndexUsed = false;
            this.serverQueryWasSlow = false;
            if (this.useCompression) {
                int bytesLeft = this.mysqlInput.available();
                if (bytesLeft > 0)
                    this.mysqlInput.skip(bytesLeft);
            }
            try {
                clearInputStream();
                if (queryPacket == null) {
                    int packLength = 8 + ((extraData != null) ? extraData.length() : 0) + 2;
                    if (this.sendPacket == null)
                        this.sendPacket = new Buffer(packLength);
                    this.packetSequence = -1;
                    this.compressedPacketSequence = -1;
                    this.readPacketSequence = 0;
                    this.checkPacketSequence = true;
                    this.sendPacket.clear();
                    this.sendPacket.writeByte((byte) command);
                    if (command == 2 || command == 5 || command == 6 || command == 3 || command == 22) {
                        if (extraDataCharEncoding == null) {
                            this.sendPacket.writeStringNoNull(extraData);
                        } else {
                            this.sendPacket.writeStringNoNull(extraData, extraDataCharEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), this.connection);
                        }
                    } else if (command == 12) {
                        long id = Long.parseLong(extraData);
                        this.sendPacket.writeLong(id);
                    }
                    send(this.sendPacket, this.sendPacket.getPosition());
                } else {
                    this.packetSequence = -1;
                    this.compressedPacketSequence = -1;
                    send(queryPacket, queryPacket.getPosition());
                }
            } catch (SQLException sqlEx) {
                throw sqlEx;
            } catch (Exception ex) {
                throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ex, getExceptionInterceptor());
            }
            Buffer returnPacket = null;
            if (!skipCheck) {
                if (command == 23 || command == 26) {
                    this.readPacketSequence = 0;
                    this.packetSequenceReset = true;
                }
                returnPacket = checkErrorPacket(command);
            }
            return returnPacket;
        } catch (IOException ioEx) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
        } finally {
            if (timeoutMillis != 0)
                try {
                    this.mysqlConnection.setSoTimeout(oldTimeout);
                } catch (SocketException e) {
                    throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, e, getExceptionInterceptor());
                }
        }
    }

    final ResultSetInternalMethods sqlQueryDirect(StatementImpl callingStatement, String query, String characterEncoding, Buffer queryPacket, int maxRows, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Field[] cachedMetadata) throws Exception {
        this.statementExecutionDepth++;

        try {
            if (this.statementInterceptors != null) {
                ResultSetInternalMethods interceptedResults = invokeStatementInterceptorsPre(query, callingStatement, false);

                if (interceptedResults != null) {
                    return interceptedResults;
                }
            }

            long queryStartTime = 0L;
            long queryEndTime = 0L;

            String statementComment = this.connection.getStatementComment();

            if (this.connection.getIncludeThreadNamesAsStatementComment()) {
                statementComment = ((statementComment != null) ? (statementComment + ", ") : "") + "java thread: " + Thread.currentThread().getName();
            }

            if (query != null) {

                int packLength = 5 + query.length() * 2 + 2;

                byte[] commentAsBytes = null;

                if (statementComment != null) {
                    commentAsBytes = StringUtils.getBytes(statementComment, (SingleByteCharsetConverter) null, characterEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());

                    packLength += commentAsBytes.length;
                    packLength += 6;
                }

                if (this.sendPacket == null) {
                    this.sendPacket = new Buffer(packLength);
                } else {
                    this.sendPacket.clear();
                }

                this.sendPacket.writeByte((byte) 3);

                if (commentAsBytes != null) {
                    this.sendPacket.writeBytesNoNull(Constants.SLASH_STAR_SPACE_AS_BYTES);
                    this.sendPacket.writeBytesNoNull(commentAsBytes);
                    this.sendPacket.writeBytesNoNull(Constants.SPACE_STAR_SLASH_SPACE_AS_BYTES);
                }

                if (characterEncoding != null) {
                    if (this.platformDbCharsetMatches) {
                        this.sendPacket.writeStringNoNull(query, characterEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), this.connection);

                    } else if (StringUtils.startsWithIgnoreCaseAndWs(query, "LOAD DATA")) {
                        this.sendPacket.writeBytesNoNull(StringUtils.getBytes(query));
                    } else {
                        this.sendPacket.writeStringNoNull(query, characterEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), this.connection);

                    }

                } else {

                    this.sendPacket.writeStringNoNull(query);
                }

                queryPacket = this.sendPacket;
            }

            byte[] queryBuf = null;
            int oldPacketPosition = 0;

            if (this.needToGrabQueryFromPacket) {
                queryBuf = queryPacket.getByteBuffer();

                oldPacketPosition = queryPacket.getPosition();

                queryStartTime = getCurrentTimeNanosOrMillis();
            }

            if (this.autoGenerateTestcaseScript) {
                String testcaseQuery = null;

                if (query != null) {
                    if (statementComment != null) {
                        testcaseQuery = "" + query;
                    } else {
                        testcaseQuery = query;
                    }
                } else {
                    testcaseQuery = StringUtils.toString(queryBuf, 5, oldPacketPosition - 5);
                }

                StringBuffer debugBuf = new StringBuffer(testcaseQuery.length() + 32);
                this.connection.generateConnectionCommentBlock(debugBuf);
                debugBuf.append(testcaseQuery);
                debugBuf.append(';');
                this.connection.dumpTestcaseQuery(debugBuf.toString());
            }

            Buffer resultPacket = sendCommand(3, null, queryPacket, false, null, 0);

            long fetchBeginTime = 0L;
            long fetchEndTime = 0L;

            String profileQueryToLog = null;

            boolean queryWasSlow = false;

            if (this.profileSql || this.logSlowQueries) {
                queryEndTime = getCurrentTimeNanosOrMillis();

                boolean shouldExtractQuery = false;

                if (this.profileSql) {
                    shouldExtractQuery = true;
                } else if (this.logSlowQueries) {
                    long queryTime = queryEndTime - queryStartTime;

                    boolean logSlow = false;

                    if (!this.useAutoSlowLog) {
                        logSlow = (queryTime > this.connection.getSlowQueryThresholdMillis());
                    } else {
                        logSlow = this.connection.isAbonormallyLongQuery(queryTime);

                        this.connection.reportQueryTime(queryTime);
                    }

                    if (logSlow) {
                        shouldExtractQuery = true;
                        queryWasSlow = true;
                    }
                }

                if (shouldExtractQuery) {

                    boolean truncated = false;

                    int extractPosition = oldPacketPosition;

                    if (oldPacketPosition > this.connection.getMaxQuerySizeToLog()) {
                        extractPosition = this.connection.getMaxQuerySizeToLog() + 5;
                        truncated = true;
                    }

                    profileQueryToLog = StringUtils.toString(queryBuf, 5, extractPosition - 5);

                    if (truncated) {
                        profileQueryToLog = profileQueryToLog + Messages.getString("MysqlIO.25");
                    }
                }

                fetchBeginTime = queryEndTime;
            }

            ResultSetInternalMethods rs = readAllResults(callingStatement, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, resultPacket, false, -1L, cachedMetadata);

            if (queryWasSlow && !this.serverQueryWasSlow) {
                StringBuffer mesgBuf = new StringBuffer(48 + profileQueryToLog.length());

                mesgBuf.append(Messages.getString("MysqlIO.SlowQuery", new Object[]{String.valueOf(this.useAutoSlowLog ? " 95% of all queries " : Long.valueOf(this.slowQueryThreshold)), this.queryTimingUnits, Long.valueOf(queryEndTime - queryStartTime)}));

                mesgBuf.append(profileQueryToLog);

                ProfilerEventHandler eventSink = ProfilerEventHandlerFactory.getInstance(this.connection);

                eventSink.consumeEvent(new ProfilerEvent((byte) 6, "", catalog, this.connection.getId(), (callingStatement != null) ? callingStatement.getId() : 999, ((ResultSetImpl) rs).resultId, System.currentTimeMillis(), (int) (queryEndTime - queryStartTime), this.queryTimingUnits, null, LogUtils.findCallingClassAndMethod(new Throwable()), mesgBuf.toString()));

                if (this.connection.getExplainSlowQueries()) {
                    if (oldPacketPosition < 1048576) {
                        explainSlowQuery(queryPacket.getBytes(5, oldPacketPosition - 5), profileQueryToLog);
                    } else {

                        this.connection.getLog().logWarn(Messages.getString("MysqlIO.28") + 1048576 + Messages.getString("MysqlIO.29"));
                    }
                }
            }

            if (this.logSlowQueries) {

                ProfilerEventHandler eventSink = ProfilerEventHandlerFactory.getInstance(this.connection);

                if (this.queryBadIndexUsed && this.profileSql) {
                    eventSink.consumeEvent(new ProfilerEvent((byte) 6, "", catalog, this.connection.getId(), (callingStatement != null) ? callingStatement.getId() : 999, ((ResultSetImpl) rs).resultId, System.currentTimeMillis(), queryEndTime - queryStartTime, this.queryTimingUnits, null, LogUtils.findCallingClassAndMethod(new Throwable()), Messages.getString("MysqlIO.33") + profileQueryToLog));
                }

                if (this.queryNoIndexUsed && this.profileSql) {
                    eventSink.consumeEvent(new ProfilerEvent((byte) 6, "", catalog, this.connection.getId(), (callingStatement != null) ? callingStatement.getId() : 999, ((ResultSetImpl) rs).resultId, System.currentTimeMillis(), queryEndTime - queryStartTime, this.queryTimingUnits, null, LogUtils.findCallingClassAndMethod(new Throwable()), Messages.getString("MysqlIO.35") + profileQueryToLog));
                }

                if (this.serverQueryWasSlow && this.profileSql) {
                    eventSink.consumeEvent(new ProfilerEvent((byte) 6, "", catalog, this.connection.getId(), (callingStatement != null) ? callingStatement.getId() : 999, ((ResultSetImpl) rs).resultId, System.currentTimeMillis(), queryEndTime - queryStartTime, this.queryTimingUnits, null, LogUtils.findCallingClassAndMethod(new Throwable()), Messages.getString("MysqlIO.ServerSlowQuery") + profileQueryToLog));
                }
            }

            if (this.profileSql) {
                fetchEndTime = getCurrentTimeNanosOrMillis();

                ProfilerEventHandler eventSink = ProfilerEventHandlerFactory.getInstance(this.connection);

                eventSink.consumeEvent(new ProfilerEvent((byte) 3, "", catalog, this.connection.getId(), (callingStatement != null) ? callingStatement.getId() : 999, ((ResultSetImpl) rs).resultId, System.currentTimeMillis(), queryEndTime - queryStartTime, this.queryTimingUnits, null, LogUtils.findCallingClassAndMethod(new Throwable()), profileQueryToLog));

                eventSink.consumeEvent(new ProfilerEvent((byte) 5, "", catalog, this.connection.getId(), (callingStatement != null) ? callingStatement.getId() : 999, ((ResultSetImpl) rs).resultId, System.currentTimeMillis(), fetchEndTime - fetchBeginTime, this.queryTimingUnits, null, LogUtils.findCallingClassAndMethod(new Throwable()), null));
            }

            if (this.hadWarnings) {
                scanForAndThrowDataTruncation();
            }

            if (this.statementInterceptors != null) {
                ResultSetInternalMethods interceptedResults = invokeStatementInterceptorsPost(query, callingStatement, rs, false, null);

                if (interceptedResults != null) {
                    rs = interceptedResults;
                }
            }

            return rs;
        } catch (SQLException sqlEx) {
            if (this.statementInterceptors != null) {
                invokeStatementInterceptorsPost(query, callingStatement, null, false, sqlEx);
            }

            if (callingStatement != null) {
                synchronized (callingStatement.cancelTimeoutMutex) {
                    if (callingStatement.wasCancelled) {
                        MySQLStatementCancelledException mySQLStatementCancelledException;
                        SQLException cause = null;

                        if (callingStatement.wasCancelledByTimeout) {
                            MySQLTimeoutException mySQLTimeoutException = new MySQLTimeoutException();
                        } else {
                            mySQLStatementCancelledException = new MySQLStatementCancelledException();
                        }

                        callingStatement.resetCancelledState();

                        throw mySQLStatementCancelledException;
                    }
                }
            }

            throw sqlEx;
        } finally {
            this.statementExecutionDepth--;
        }
    }

    ResultSetInternalMethods invokeStatementInterceptorsPre(String sql, Statement interceptedStatement, boolean forceExecute) throws SQLException {
        ResultSetInternalMethods previousResultSet = null;

        Iterator<StatementInterceptorV2> interceptors = this.statementInterceptors.iterator();

        while (interceptors.hasNext()) {
            StatementInterceptorV2 interceptor = interceptors.next();

            boolean executeTopLevelOnly = interceptor.executeTopLevelOnly();
            boolean shouldExecute = ((executeTopLevelOnly && (this.statementExecutionDepth == 1 || forceExecute)) || !executeTopLevelOnly);

            if (shouldExecute) {
                String sqlToInterceptor = sql;

                ResultSetInternalMethods interceptedResultSet = interceptor.preProcess(sqlToInterceptor, interceptedStatement, this.connection);

                if (interceptedResultSet != null) {
                    previousResultSet = interceptedResultSet;
                }
            }
        }

        return previousResultSet;
    }

    ResultSetInternalMethods invokeStatementInterceptorsPost(String sql, Statement interceptedStatement, ResultSetInternalMethods originalResultSet, boolean forceExecute, SQLException statementException) throws SQLException {
        Iterator<StatementInterceptorV2> interceptors = this.statementInterceptors.iterator();

        while (interceptors.hasNext()) {
            StatementInterceptorV2 interceptor = interceptors.next();

            boolean executeTopLevelOnly = interceptor.executeTopLevelOnly();
            boolean shouldExecute = ((executeTopLevelOnly && (this.statementExecutionDepth == 1 || forceExecute)) || !executeTopLevelOnly);

            if (shouldExecute) {
                String sqlToInterceptor = sql;

                ResultSetInternalMethods interceptedResultSet = interceptor.postProcess(sqlToInterceptor, interceptedStatement, originalResultSet, this.connection, this.warningCount, this.queryNoIndexUsed, this.queryBadIndexUsed, statementException);

                if (interceptedResultSet != null) {
                    originalResultSet = interceptedResultSet;
                }
            }
        }

        return originalResultSet;
    }

    private void calculateSlowQueryThreshold() {
        this.slowQueryThreshold = this.connection.getSlowQueryThresholdMillis();

        if (this.connection.getUseNanosForElapsedTime()) {
            long nanosThreshold = this.connection.getSlowQueryThresholdNanos();

            if (nanosThreshold != 0L) {
                this.slowQueryThreshold = nanosThreshold;
            } else {
                this.slowQueryThreshold *= 1000000L;
            }
        }
    }

    protected long getCurrentTimeNanosOrMillis() {
        if (this.useNanosForElapsedTime) {
            return Util.getCurrentTimeNanosOrMillis();
        }

        return System.currentTimeMillis();
    }

    String getHost() {
        return this.host;
    }

    boolean isVersion(int major, int minor, int subminor) {
        return (major == getServerMajorVersion() && minor == getServerMinorVersion() && subminor == getServerSubMinorVersion());
    }

    boolean versionMeetsMinimum(int major, int minor, int subminor) {
        if (getServerMajorVersion() >= major) {
            if (getServerMajorVersion() == major) {
                if (getServerMinorVersion() >= minor) {
                    if (getServerMinorVersion() == minor) {
                        return (getServerSubMinorVersion() >= subminor);
                    }

                    return true;
                }

                return false;
            }

            return true;
        }

        return false;
    }

    private final int readFully(InputStream in, byte[] b, int off, int len) throws IOException {
        if (len < 0) {
            throw new IndexOutOfBoundsException();
        }

        int n = 0;

        while (n < len) {
            int count = in.read(b, off + n, len - n);

            if (count < 0) {
                throw new EOFException(Messages.getString("MysqlIO.EOF", new Object[]{Integer.valueOf(len), Integer.valueOf(n)}));
            }

            n += count;
        }

        return n;
    }

    private final long skipFully(InputStream in, long len) throws IOException {
        if (len < 0L) {
            throw new IOException("Negative skip length not allowed");
        }

        long n = 0L;

        while (n < len) {
            long count = in.skip(len - n);

            if (count < 0L) {
                throw new EOFException(Messages.getString("MysqlIO.EOF", new Object[]{Long.valueOf(len), Long.valueOf(n)}));
            }

            n += count;
        }

        return n;
    }

    protected final ResultSetImpl readResultsForQueryOrUpdate(StatementImpl callingStatement, int maxRows, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Buffer resultPacket, boolean isBinaryEncoded, long preSentColumnCount, Field[] metadataFromCache) throws SQLException {
        long columnCount = resultPacket.readFieldLength();

        if (columnCount == 0L)
            return buildResultSetWithUpdates(callingStatement, resultPacket);
        if (columnCount == -1L) {
            String charEncoding = null;

            if (this.connection.getUseUnicode()) {
                charEncoding = this.connection.getEncoding();
            }

            String fileName = null;

            if (this.platformDbCharsetMatches) {
                fileName = (charEncoding != null) ? resultPacket.readString(charEncoding, getExceptionInterceptor()) : resultPacket.readString();
            } else {

                fileName = resultPacket.readString();
            }

            return sendFileToServer(callingStatement, fileName);
        }
        ResultSetImpl results = getResultSet(callingStatement, columnCount, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, isBinaryEncoded, metadataFromCache);

        return results;
    }

    private int alignPacketSize(int a, int l) {
        return a + l - 1 & (l - 1 ^ 0xFFFFFFFF);
    }

    private ResultSetImpl buildResultSetWithRows(StatementImpl callingStatement, String catalog, Field[] fields, RowData rows, int resultSetType, int resultSetConcurrency, boolean isBinaryEncoded) throws SQLException {
        ResultSetImpl rs = null;

        switch (resultSetConcurrency) {
            case 1007:
                rs = ResultSetImpl.getInstance(catalog, fields, rows, this.connection, callingStatement, false);

                if (isBinaryEncoded) {
                    rs.setBinaryEncoded();
                }

                rs.setResultSetType(resultSetType);
                rs.setResultSetConcurrency(resultSetConcurrency);

                return rs;
            case 1008:
                rs = ResultSetImpl.getInstance(catalog, fields, rows, this.connection, callingStatement, true);
                rs.setResultSetType(resultSetType);
                rs.setResultSetConcurrency(resultSetConcurrency);
                return rs;
        }
        return ResultSetImpl.getInstance(catalog, fields, rows, this.connection, callingStatement, false);
    }

    private ResultSetImpl buildResultSetWithUpdates(StatementImpl callingStatement, Buffer resultPacket) throws SQLException {
        long updateCount = -1L;
        long updateID = -1L;
        String info = null;

        try {
            if (this.useNewUpdateCounts) {
                updateCount = resultPacket.newReadLength();
                updateID = resultPacket.newReadLength();
            } else {
                updateCount = resultPacket.readLength();
                updateID = resultPacket.readLength();
            }

            if (this.use41Extensions) {

                this.serverStatus = resultPacket.readInt();

                checkTransactionState(this.oldServerStatus);

                this.warningCount = resultPacket.readInt();

                if (this.warningCount > 0) {
                    this.hadWarnings = true;
                }

                resultPacket.readByte();

                setServerSlowQueryFlags();
            }

            if (this.connection.isReadInfoMsgEnabled()) {
                info = resultPacket.readString(this.connection.getErrorMessageEncoding(), getExceptionInterceptor());
            }
        } catch (Exception ex) {
            SQLException sqlEx = SQLError.createSQLException(SQLError.get("S1000"), "S1000", -1, getExceptionInterceptor());

            sqlEx.initCause(ex);

            throw sqlEx;
        }

        ResultSetInternalMethods updateRs = ResultSetImpl.getInstance(updateCount, updateID, this.connection, callingStatement);

        if (info != null) {
            ((ResultSetImpl) updateRs).setServerInfo(info);
        }

        return (ResultSetImpl) updateRs;
    }

    private void setServerSlowQueryFlags() {
        this.queryBadIndexUsed = ((this.serverStatus & 0x10) != 0);

        this.queryNoIndexUsed = ((this.serverStatus & 0x20) != 0);

        this.serverQueryWasSlow = ((this.serverStatus & 0x800) != 0);
    }

    private void checkForOutstandingStreamingData() throws SQLException {
        if (this.streamingData != null) {
            boolean shouldClobber = this.connection.getClobberStreamingResults();

            if (!shouldClobber) {
                throw SQLError.createSQLException(Messages.getString("MysqlIO.39") + this.streamingData + Messages.getString("MysqlIO.40") + Messages.getString("MysqlIO.41") + Messages.getString("MysqlIO.42"), getExceptionInterceptor());
            }

            this.streamingData.getOwner().realClose(false);

            clearInputStream();
        }
    }

    private Buffer compressPacket(Buffer packet, int offset, int packetLen) throws SQLException {
        int compressedLength = packetLen;
        int uncompressedLength = 0;
        byte[] compressedBytes = null;
        int offsetWrite = offset;

        if (packetLen < 50) {
            compressedBytes = packet.getByteBuffer();
        } else {

            byte[] bytesToCompress = packet.getByteBuffer();
            compressedBytes = new byte[bytesToCompress.length * 2];

            this.deflater.reset();
            this.deflater.setInput(bytesToCompress, offset, packetLen);
            this.deflater.finish();

            compressedLength = this.deflater.deflate(compressedBytes);

            if (compressedLength > packetLen) {

                compressedBytes = packet.getByteBuffer();
                compressedLength = packetLen;
            } else {
                uncompressedLength = packetLen;
                offsetWrite = 0;
            }
        }

        Buffer compressedPacket = new Buffer(7 + compressedLength);

        compressedPacket.setPosition(0);
        compressedPacket.writeLongInt(compressedLength);
        compressedPacket.writeByte(this.compressedPacketSequence);
        compressedPacket.writeLongInt(uncompressedLength);
        compressedPacket.writeBytesNoNull(compressedBytes, offsetWrite, compressedLength);

        return compressedPacket;
    }

    private final void readServerStatusForResultSets(Buffer rowPacket) throws SQLException {
        if (this.use41Extensions) {
            rowPacket.readByte();

            this.warningCount = rowPacket.readInt();

            if (this.warningCount > 0) {
                this.hadWarnings = true;
            }

            this.oldServerStatus = this.serverStatus;
            this.serverStatus = rowPacket.readInt();
            checkTransactionState(this.oldServerStatus);

            setServerSlowQueryFlags();
        }
    }

    private SocketFactory createSocketFactory() throws SQLException {
        try {
            if (this.socketFactoryClassName == null) {
                throw SQLError.createSQLException(Messages.getString("MysqlIO.75"), "08001", getExceptionInterceptor());
            }

            return (SocketFactory) Class.forName(this.socketFactoryClassName).newInstance();
        } catch (Exception ex) {
            SQLException sqlEx = SQLError.createSQLException(Messages.getString("MysqlIO.76") + this.socketFactoryClassName + Messages.getString("MysqlIO.77"), "08001", getExceptionInterceptor());

            sqlEx.initCause(ex);

            throw sqlEx;
        }
    }

    private void enqueuePacketForDebugging(boolean isPacketBeingSent, boolean isPacketReused, int sendLength, byte[] header, Buffer packet) throws SQLException {
        if (this.packetDebugRingBuffer.size() + 1 > this.connection.getPacketDebugBufferSize()) {
            this.packetDebugRingBuffer.removeFirst();
        }

        StringBuffer packetDump = null;

        if (!isPacketBeingSent) {
            int bytesToDump = Math.min(1024, packet.getBufLength());

            Buffer packetToDump = new Buffer(4 + bytesToDump);

            packetToDump.setPosition(0);
            packetToDump.writeBytesNoNull(header);
            packetToDump.writeBytesNoNull(packet.getBytes(0, bytesToDump));

            String packetPayload = packetToDump.dump(bytesToDump);

            packetDump = new StringBuffer(96 + packetPayload.length());

            packetDump.append("Server ");

            if (isPacketReused) {
                packetDump.append("(re-used)");
            } else {
                packetDump.append("(new)");
            }

            packetDump.append(" ");
            packetDump.append(packet.toSuperString());
            packetDump.append(" --------------------> Client\n");
            packetDump.append("\nPacket payload:\n\n");
            packetDump.append(packetPayload);

            if (bytesToDump == 1024) {
                packetDump.append("\nNote: Packet of " + packet.getBufLength() + " bytes truncated to " + 'Ѐ' + " bytes.\n");
            }
        } else {

            int bytesToDump = Math.min(1024, sendLength);

            String packetPayload = packet.dump(bytesToDump);

            packetDump = new StringBuffer(68 + packetPayload.length());

            packetDump.append("Client ");
            packetDump.append(packet.toSuperString());
            packetDump.append("--------------------> Server\n");
            packetDump.append("\nPacket payload:\n\n");
            packetDump.append(packetPayload);

            if (bytesToDump == 1024) {
                packetDump.append("\nNote: Packet of " + sendLength + " bytes truncated to " + 'Ѐ' + " bytes.\n");
            }
        }

        this.packetDebugRingBuffer.addLast(packetDump);
    }

    private RowData readSingleRowSet(long columnCount, int maxRows, int resultSetConcurrency, boolean isBinaryEncoded, Field[] fields) throws SQLException {
        ArrayList<ResultSetRow> rows = new ArrayList<ResultSetRow>();

        boolean useBufferRowExplicit = useBufferRowExplicit(fields);

        ResultSetRow row = nextRow(fields, (int) columnCount, isBinaryEncoded, resultSetConcurrency, false, useBufferRowExplicit, false, null);

        int rowCount = 0;

        if (row != null) {
            rows.add(row);
            rowCount = 1;
        }

        while (row != null) {
            row = nextRow(fields, (int) columnCount, isBinaryEncoded, resultSetConcurrency, false, useBufferRowExplicit, false, null);

            if (row != null && (
                    maxRows == -1 || rowCount < maxRows)) {
                rows.add(row);
                rowCount++;
            }
        }

        RowData rowData = new RowDataStatic(rows);

        return rowData;
    }

    private void reclaimLargeReusablePacket() {
        if (this.reusablePacket != null && this.reusablePacket.getCapacity() > 1048576) {
            this.reusablePacket = new Buffer(1024);
        }
    }

    private final Buffer reuseAndReadPacket(Buffer reuse) throws SQLException {
        return reuseAndReadPacket(reuse, -1);
    }

    private final Buffer reuseAndReadPacket(Buffer reuse, int existingPacketLength) throws SQLException {
        try {
            reuse.setWasMultiPacket(false);
            int packetLength = 0;

            if (existingPacketLength == -1) {
                int lengthRead = readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4);

                if (lengthRead < 4) {
                    forceClose();
                    throw new IOException(Messages.getString("MysqlIO.43"));
                }

                packetLength = (this.packetHeaderBuf[0] & 0xFF) + ((this.packetHeaderBuf[1] & 0xFF) << 8) + ((this.packetHeaderBuf[2] & 0xFF) << 16);
            } else {

                packetLength = existingPacketLength;
            }

            if (this.traceProtocol) {
                StringBuffer traceMessageBuf = new StringBuffer();

                traceMessageBuf.append(Messages.getString("MysqlIO.44"));
                traceMessageBuf.append(packetLength);
                traceMessageBuf.append(Messages.getString("MysqlIO.45"));
                traceMessageBuf.append(StringUtils.dumpAsHex(this.packetHeaderBuf, 4));

                this.connection.getLog().logTrace(traceMessageBuf.toString());
            }

            byte multiPacketSeq = this.packetHeaderBuf[3];

            if (!this.packetSequenceReset) {
                if (this.enablePacketDebug && this.checkPacketSequence) {
                    checkPacketSequencing(multiPacketSeq);
                }
            } else {
                this.packetSequenceReset = false;
            }

            this.readPacketSequence = multiPacketSeq;

            reuse.setPosition(0);

            if ((reuse.getByteBuffer()).length <= packetLength) {
                reuse.setByteBuffer(new byte[packetLength + 1]);
            }

            reuse.setBufLength(packetLength);

            int numBytesRead = readFully(this.mysqlInput, reuse.getByteBuffer(), 0, packetLength);

            if (numBytesRead != packetLength) {
                throw new IOException("Short read, expected " + packetLength + " bytes, only read " + numBytesRead);
            }

            if (this.traceProtocol) {
                StringBuffer traceMessageBuf = new StringBuffer();

                traceMessageBuf.append(Messages.getString("MysqlIO.46"));
                traceMessageBuf.append(getPacketDumpToLog(reuse, packetLength));

                this.connection.getLog().logTrace(traceMessageBuf.toString());
            }

            if (this.enablePacketDebug) {
                enqueuePacketForDebugging(false, true, 0, this.packetHeaderBuf, reuse);
            }

            boolean isMultiPacket = false;

            if (packetLength == this.maxThreeBytes) {
                reuse.setPosition(this.maxThreeBytes);

                isMultiPacket = true;

                packetLength = readRemainingMultiPackets(reuse, multiPacketSeq);
            }

            if (!isMultiPacket) {
                reuse.getByteBuffer()[packetLength] = 0;
            }

            if (this.connection.getMaintainTimeStats()) {
                this.lastPacketReceivedTimeMs = System.currentTimeMillis();
            }

            return reuse;
        } catch (IOException ioEx) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
        } catch (OutOfMemoryError oom) {

            try {
                clearInputStream();
            } catch (Exception ex) {
            }

            try {
                this.connection.realClose(false, false, true, oom);
            } catch (Exception ex) {
            }

            throw oom;
        }
    }

    private int readRemainingMultiPackets(Buffer reuse, byte multiPacketSeq) throws IOException, SQLException {
        int lengthRead = readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4);

        if (lengthRead < 4) {
            forceClose();
            throw new IOException(Messages.getString("MysqlIO.47"));
        }

        int packetLength = (this.packetHeaderBuf[0] & 0xFF) + ((this.packetHeaderBuf[1] & 0xFF) << 8) + ((this.packetHeaderBuf[2] & 0xFF) << 16);

        Buffer multiPacket = new Buffer(packetLength);
        boolean firstMultiPkt = true;

        while (true) {
            if (!firstMultiPkt) {
                lengthRead = readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4);

                if (lengthRead < 4) {
                    forceClose();
                    throw new IOException(Messages.getString("MysqlIO.48"));
                }

                packetLength = (this.packetHeaderBuf[0] & 0xFF) + ((this.packetHeaderBuf[1] & 0xFF) << 8) + ((this.packetHeaderBuf[2] & 0xFF) << 16);
            } else {

                firstMultiPkt = false;
            }

            if (!this.useNewLargePackets && packetLength == 1) {
                clearInputStream();
                break;
            }
            if (packetLength < this.maxThreeBytes) {
                byte b = this.packetHeaderBuf[3];

                if (b != multiPacketSeq + 1) {
                    throw new IOException(Messages.getString("MysqlIO.49"));
                }

                multiPacketSeq = b;

                multiPacket.setPosition(0);

                multiPacket.setBufLength(packetLength);

                byte[] arrayOfByte = multiPacket.getByteBuffer();
                int i = packetLength;

                int j = readFully(this.mysqlInput, arrayOfByte, 0, packetLength);

                if (j != i) {
                    throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, SQLError.createSQLException(Messages.getString("MysqlIO.50") + i + Messages.getString("MysqlIO.51") + j + ".", getExceptionInterceptor()), getExceptionInterceptor());
                }

                reuse.writeBytesNoNull(arrayOfByte, 0, i);

                break;
            }

            byte newPacketSeq = this.packetHeaderBuf[3];

            if (newPacketSeq != multiPacketSeq + 1) {
                throw new IOException(Messages.getString("MysqlIO.53"));
            }

            multiPacketSeq = newPacketSeq;

            multiPacket.setPosition(0);

            multiPacket.setBufLength(packetLength);

            byte[] byteBuf = multiPacket.getByteBuffer();
            int lengthToWrite = packetLength;

            int bytesRead = readFully(this.mysqlInput, byteBuf, 0, packetLength);

            if (bytesRead != lengthToWrite) {
                throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, SQLError.createSQLException(Messages.getString("MysqlIO.54") + lengthToWrite + Messages.getString("MysqlIO.55") + bytesRead + ".", getExceptionInterceptor()), getExceptionInterceptor());
            }

            reuse.writeBytesNoNull(byteBuf, 0, lengthToWrite);
        }

        reuse.setPosition(0);
        reuse.setWasMultiPacket(true);
        return packetLength;
    }

    private void checkPacketSequencing(byte multiPacketSeq) throws SQLException {
        if (multiPacketSeq == Byte.MIN_VALUE && this.readPacketSequence != Byte.MAX_VALUE) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, new IOException("Packets out of order, expected packet # -128, but received packet # " + multiPacketSeq), getExceptionInterceptor());
        }

        if (this.readPacketSequence == -1 && multiPacketSeq != 0) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, new IOException("Packets out of order, expected packet # -1, but received packet # " + multiPacketSeq), getExceptionInterceptor());
        }

        if (multiPacketSeq != Byte.MIN_VALUE && this.readPacketSequence != -1 && multiPacketSeq != this.readPacketSequence + 1) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, new IOException("Packets out of order, expected packet # " + (this.readPacketSequence + 1) + ", but received packet # " + multiPacketSeq), getExceptionInterceptor());
        }
    }

    void enableMultiQueries() throws SQLException {
        Buffer buf = getSharedSendPacket();

        buf.clear();
        buf.writeByte((byte) 27);
        buf.writeInt(0);
        sendCommand(27, null, buf, false, null, 0);
    }

    void disableMultiQueries() throws SQLException {
        Buffer buf = getSharedSendPacket();

        buf.clear();
        buf.writeByte((byte) 27);
        buf.writeInt(1);
        sendCommand(27, null, buf, false, null, 0);
    }

    private final void send(Buffer packet, int packetLen) throws SQLException {
        try {
            if (this.maxAllowedPacket > 0 && packetLen > this.maxAllowedPacket) {
                throw new PacketTooBigException(packetLen, this.maxAllowedPacket);
            }

            if (this.serverMajorVersion >= 4 && (packetLen - 4 >= this.maxThreeBytes || (this.useCompression && packetLen - 4 >= this.maxThreeBytes - 3))) {

                sendSplitPackets(packet, packetLen);
            } else {

                this.packetSequence = (byte) (this.packetSequence + 1);

                Buffer packetToSend = packet;
                packetToSend.setPosition(0);
                packetToSend.writeLongInt(packetLen - 4);
                packetToSend.writeByte(this.packetSequence);

                if (this.useCompression) {
                    this.compressedPacketSequence = (byte) (this.compressedPacketSequence + 1);
                    int originalPacketLen = packetLen;

                    packetToSend = compressPacket(packetToSend, 0, packetLen);
                    packetLen = packetToSend.getPosition();

                    if (this.traceProtocol) {
                        StringBuffer traceMessageBuf = new StringBuffer();

                        traceMessageBuf.append(Messages.getString("MysqlIO.57"));
                        traceMessageBuf.append(getPacketDumpToLog(packetToSend, packetLen));

                        traceMessageBuf.append(Messages.getString("MysqlIO.58"));
                        traceMessageBuf.append(getPacketDumpToLog(packet, originalPacketLen));

                        this.connection.getLog().logTrace(traceMessageBuf.toString());
                    }

                } else if (this.traceProtocol) {
                    StringBuffer traceMessageBuf = new StringBuffer();

                    traceMessageBuf.append(Messages.getString("MysqlIO.59"));
                    traceMessageBuf.append("host: '");
                    traceMessageBuf.append(this.host);
                    traceMessageBuf.append("' threadId: '");
                    traceMessageBuf.append(this.threadId);
                    traceMessageBuf.append("'\n");
                    traceMessageBuf.append(packetToSend.dump(packetLen));

                    this.connection.getLog().logTrace(traceMessageBuf.toString());
                }

                this.mysqlOutput.write(packetToSend.getByteBuffer(), 0, packetLen);
                this.mysqlOutput.flush();
            }

            if (this.enablePacketDebug) {
                enqueuePacketForDebugging(true, false, packetLen + 5, this.packetHeaderBuf, packet);
            }

            if (packet == this.sharedSendPacket) {
                reclaimLargeSharedSendPacket();
            }

            if (this.connection.getMaintainTimeStats()) {
                this.lastPacketSentTimeMs = System.currentTimeMillis();
            }
        } catch (IOException ioEx) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
        }
    }

    private final ResultSetImpl sendFileToServer(StatementImpl callingStatement, String fileName) throws SQLException {
        if (this.useCompression) {
            this.compressedPacketSequence = (byte) (this.compressedPacketSequence + 1);
        }

        Buffer filePacket = (this.loadFileBufRef == null) ? null : this.loadFileBufRef.get();

        int bigPacketLength = Math.min(this.connection.getMaxAllowedPacket() - 12, alignPacketSize(this.connection.getMaxAllowedPacket() - 16, 4096) - 12);

        int oneMeg = 1048576;

        int smallerPacketSizeAligned = Math.min(oneMeg - 12, alignPacketSize(oneMeg - 16, 4096) - 12);

        int packetLength = Math.min(smallerPacketSizeAligned, bigPacketLength);

        if (filePacket == null) {
            try {
                filePacket = new Buffer(packetLength + 4);
                this.loadFileBufRef = new SoftReference<Buffer>(filePacket);
            } catch (OutOfMemoryError oom) {
                throw SQLError.createSQLException("Could not allocate packet of " + packetLength + " bytes required for LOAD DATA LOCAL INFILE operation." + " Try increasing max heap allocation for JVM or decreasing server variable " + "'max_allowed_packet'", "S1001", getExceptionInterceptor());
            }
        }

        filePacket.clear();
        send(filePacket, 0);

        byte[] fileBuf = new byte[packetLength];

        BufferedInputStream fileIn = null;

        try {
            if (!this.connection.getAllowLoadLocalInfile()) {
                throw SQLError.createSQLException(Messages.getString("MysqlIO.LoadDataLocalNotAllowed"), "S1000", getExceptionInterceptor());
            }

            InputStream hookedStream = null;

            if (callingStatement != null) {
                hookedStream = callingStatement.getLocalInfileInputStream();
            }

            if (hookedStream != null) {
                fileIn = new BufferedInputStream(hookedStream);
            } else if (!this.connection.getAllowUrlInLocalInfile()) {
                fileIn = new BufferedInputStream(new FileInputStream(fileName));

            } else if (fileName.indexOf(':') != -1) {
                try {
                    URL urlFromFileName = new URL(fileName);
                    fileIn = new BufferedInputStream(urlFromFileName.openStream());
                } catch (MalformedURLException badUrlEx) {

                    fileIn = new BufferedInputStream(new FileInputStream(fileName));
                }
            } else {

                fileIn = new BufferedInputStream(new FileInputStream(fileName));
            }

            int bytesRead = 0;

            while ((bytesRead = fileIn.read(fileBuf)) != -1) {
                filePacket.clear();
                filePacket.writeBytesNoNull(fileBuf, 0, bytesRead);
                send(filePacket, filePacket.getPosition());
            }
        } catch (IOException ioEx) {
            StringBuffer messageBuf = new StringBuffer(Messages.getString("MysqlIO.60"));

            if (!this.connection.getParanoid()) {
                messageBuf.append("'");

                if (fileName != null) {
                    messageBuf.append(fileName);
                }

                messageBuf.append("'");
            }

            messageBuf.append(Messages.getString("MysqlIO.63"));

            if (!this.connection.getParanoid()) {
                messageBuf.append(Messages.getString("MysqlIO.64"));
                messageBuf.append(Util.stackTraceToString(ioEx));
            }

            throw SQLError.createSQLException(messageBuf.toString(), "S1009", getExceptionInterceptor());
        } finally {

            if (fileIn != null) {
                try {
                    fileIn.close();
                } catch (Exception ex) {
                    SQLException sqlEx = SQLError.createSQLException(Messages.getString("MysqlIO.65"), "S1000", getExceptionInterceptor());

                    sqlEx.initCause(ex);

                    throw sqlEx;
                }

                fileIn = null;
            } else {

                filePacket.clear();
                send(filePacket, filePacket.getPosition());
                checkErrorPacket();
            }
        }

        filePacket.clear();
        send(filePacket, filePacket.getPosition());

        Buffer resultPacket = checkErrorPacket();

        return buildResultSetWithUpdates(callingStatement, resultPacket);
    }

    private Buffer checkErrorPacket(int command) throws SQLException {
        Buffer resultPacket = null;
        this.serverStatus = 0;

        try {
            resultPacket = reuseAndReadPacket(this.reusablePacket);
        } catch (SQLException sqlEx) {

            throw sqlEx;
        } catch (Exception fallThru) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, fallThru, getExceptionInterceptor());
        }

        checkErrorPacket(resultPacket);

        return resultPacket;
    }

    private void checkErrorPacket(Buffer resultPacket) throws SQLException {
        int statusCode = resultPacket.readByte();

        if (statusCode == -1) {

            int errno = 2000;

            if (this.protocolVersion > 9) {
                errno = resultPacket.readInt();

                String xOpen = null;

                String str1 = resultPacket.readString(this.connection.getErrorMessageEncoding(), getExceptionInterceptor());

                if (str1.charAt(0) == '#') {

                    if (str1.length() > 6) {
                        xOpen = str1.substring(1, 6);
                        str1 = str1.substring(6);

                        if (xOpen.equals("HY000")) {
                            xOpen = SQLError.mysqlToSqlState(errno, this.connection.getUseSqlStateCodes());
                        }
                    } else {

                        xOpen = SQLError.mysqlToSqlState(errno, this.connection.getUseSqlStateCodes());
                    }
                } else {

                    xOpen = SQLError.mysqlToSqlState(errno, this.connection.getUseSqlStateCodes());
                }

                clearInputStream();

                StringBuffer stringBuffer = new StringBuffer();

                String xOpenErrorMessage = SQLError.get(xOpen);

                if (!this.connection.getUseOnlyServerErrorMessages() &&
                        xOpenErrorMessage != null) {
                    stringBuffer.append(xOpenErrorMessage);
                    stringBuffer.append(Messages.getString("MysqlIO.68"));
                }

                stringBuffer.append(str1);

                if (!this.connection.getUseOnlyServerErrorMessages() &&
                        xOpenErrorMessage != null) {
                    stringBuffer.append("\"");
                }

                appendDeadlockStatusInformation(xOpen, stringBuffer);

                if (xOpen != null && xOpen.startsWith("22")) {
                    throw new MysqlDataTruncation(stringBuffer.toString(), 0, true, false, 0, 0, errno);
                }
                throw SQLError.createSQLException(stringBuffer.toString(), xOpen, errno, false, getExceptionInterceptor(), this.connection);
            }

            String serverErrorMessage = resultPacket.readString(this.connection.getErrorMessageEncoding(), getExceptionInterceptor());

            clearInputStream();

            if (serverErrorMessage.indexOf(Messages.getString("MysqlIO.70")) != -1) {
                throw SQLError.createSQLException(SQLError.get("S0022") + ", " + serverErrorMessage, "S0022", -1, false, getExceptionInterceptor(), this.connection);
            }

            StringBuffer errorBuf = new StringBuffer(Messages.getString("MysqlIO.72"));

            errorBuf.append(serverErrorMessage);
            errorBuf.append("\"");

            throw SQLError.createSQLException(SQLError.get("S1000") + ", " + errorBuf.toString(), "S1000", -1, false, getExceptionInterceptor(), this.connection);
        }
    }

    private void appendDeadlockStatusInformation(String xOpen, StringBuffer errorBuf) throws SQLException {
        if (this.connection.getIncludeInnodbStatusInDeadlockExceptions() && xOpen != null && (xOpen.startsWith("40") || xOpen.startsWith("41")) && this.streamingData == null) {

            ResultSet rs = null;

            try {
                rs = sqlQueryDirect(null, "SHOW ENGINE INNODB STATUS", this.connection.getEncoding(), null, -1, 1003, 1007, false, this.connection.getCatalog(), null);

                if (rs.next()) {
                    errorBuf.append("\n\n");
                    errorBuf.append(rs.getString("Status"));
                } else {
                    errorBuf.append("\n\n");
                    errorBuf.append(Messages.getString("MysqlIO.NoInnoDBStatusFound"));
                }

            } catch (Exception ex) {
                errorBuf.append("\n\n");
                errorBuf.append(Messages.getString("MysqlIO.InnoDBStatusFailed"));

                errorBuf.append("\n\n");
                errorBuf.append(Util.stackTraceToString(ex));
            } finally {
                if (rs != null) {
                    rs.close();
                }
            }
        }

        if (this.connection.getIncludeThreadDumpInDeadlockExceptions()) {
            errorBuf.append("\n\n*** Java threads running at time of deadlock ***\n\n");

            ThreadMXBean threadMBean = ManagementFactory.getThreadMXBean();
            long[] threadIds = threadMBean.getAllThreadIds();

            ThreadInfo[] threads = threadMBean.getThreadInfo(threadIds, 2147483647);

            List<ThreadInfo> activeThreads = new ArrayList<ThreadInfo>();

            for (ThreadInfo info : threads) {
                if (info != null) {
                    activeThreads.add(info);
                }
            }

            for (ThreadInfo threadInfo : activeThreads) {

                errorBuf.append('"');
                errorBuf.append(threadInfo.getThreadName());
                errorBuf.append("\" tid=");
                errorBuf.append(threadInfo.getThreadId());
                errorBuf.append(" ");
                errorBuf.append(threadInfo.getThreadState());

                if (threadInfo.getLockName() != null) {
                    errorBuf.append(" on lock=" + threadInfo.getLockName());
                }
                if (threadInfo.isSuspended()) {
                    errorBuf.append(" (suspended)");
                }
                if (threadInfo.isInNative()) {
                    errorBuf.append(" (running in native)");
                }

                StackTraceElement[] stackTrace = threadInfo.getStackTrace();

                if (stackTrace.length > 0) {
                    errorBuf.append(" in ");
                    errorBuf.append(stackTrace[0].getClassName());
                    errorBuf.append(".");
                    errorBuf.append(stackTrace[0].getMethodName());
                    errorBuf.append("()");
                }

                errorBuf.append("\n");

                if (threadInfo.getLockOwnerName() != null) {
                    errorBuf.append("\t owned by " + threadInfo.getLockOwnerName() + " Id=" + threadInfo.getLockOwnerId());

                    errorBuf.append("\n");
                }

                for (int j = 0; j < stackTrace.length; j++) {
                    StackTraceElement ste = stackTrace[j];
                    errorBuf.append("\tat " + ste.toString());
                    errorBuf.append("\n");
                }
            }
        }
    }

    private final void sendSplitPackets(Buffer packet, int packetLen) throws SQLException {
        try {
            Buffer packetToSend = (this.splitBufRef == null) ? null : this.splitBufRef.get();
            Buffer toCompress = (!this.useCompression || this.compressBufRef == null) ? null : this.compressBufRef.get();

            if (packetToSend == null) {
                packetToSend = new Buffer(this.maxThreeBytes + 4);
                this.splitBufRef = new SoftReference<Buffer>(packetToSend);
            }
            if (this.useCompression) {
                int cbuflen = packetLen + (packetLen / this.maxThreeBytes + 1) * 4;
                if (toCompress == null) {
                    toCompress = new Buffer(cbuflen);
                } else if (toCompress.getBufLength() < cbuflen) {
                    toCompress.setPosition(toCompress.getBufLength());
                    toCompress.ensureCapacity(cbuflen - toCompress.getBufLength());
                }
            }

            int len = packetLen - 4;
            int splitSize = this.maxThreeBytes;
            int originalPacketPos = 4;
            byte[] origPacketBytes = packet.getByteBuffer();

            int toCompressPosition = 0;

            while (len >= 0) {
                this.packetSequence = (byte) (this.packetSequence + 1);

                if (len < splitSize) {
                    splitSize = len;
                }

                packetToSend.setPosition(0);
                packetToSend.writeLongInt(splitSize);
                packetToSend.writeByte(this.packetSequence);
                if (len > 0) {
                    System.arraycopy(origPacketBytes, originalPacketPos, packetToSend.getByteBuffer(), 4, splitSize);
                }

                if (this.useCompression) {
                    System.arraycopy(packetToSend.getByteBuffer(), 0, toCompress.getByteBuffer(), toCompressPosition, 4 + splitSize);
                    toCompressPosition += 4 + splitSize;
                } else {
                    this.mysqlOutput.write(packetToSend.getByteBuffer(), 0, 4 + splitSize);
                    this.mysqlOutput.flush();
                }

                originalPacketPos += splitSize;
                len -= this.maxThreeBytes;
            }

            if (this.useCompression) {
                len = toCompressPosition;
                toCompressPosition = 0;
                splitSize = this.maxThreeBytes - 3;
                while (len >= 0) {
                    this.compressedPacketSequence = (byte) (this.compressedPacketSequence + 1);

                    if (len < splitSize) {
                        splitSize = len;
                    }

                    Buffer compressedPacketToSend = compressPacket(toCompress, toCompressPosition, splitSize);
                    packetLen = compressedPacketToSend.getPosition();
                    this.mysqlOutput.write(compressedPacketToSend.getByteBuffer(), 0, packetLen);
                    this.mysqlOutput.flush();

                    toCompressPosition += splitSize;
                    len -= this.maxThreeBytes - 3;
                }
            }
        } catch (IOException ioEx) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
        }
    }

    private void reclaimLargeSharedSendPacket() {
        if (this.sharedSendPacket != null && this.sharedSendPacket.getCapacity() > 1048576) {
            this.sharedSendPacket = new Buffer(1024);
        }
    }

    boolean hadWarnings() {
        return this.hadWarnings;
    }

    void scanForAndThrowDataTruncation() throws SQLException {
        if (this.streamingData == null && versionMeetsMinimum(4, 1, 0) && this.connection.getJdbcCompliantTruncation() && this.warningCount > 0) {
            SQLError.convertShowWarningsToSQLWarnings(this.connection, this.warningCount, true);
        }
    }

    private void secureAuth(Buffer packet, int packLength, String user, String password, String database, boolean writeClientParams) throws SQLException {
        if (packet == null) {
            packet = new Buffer(packLength);
        }

        if (writeClientParams) {
            if (this.use41Extensions) {
                if (versionMeetsMinimum(4, 1, 1)) {
                    packet.writeLong(this.clientParam);
                    packet.writeLong(this.maxThreeBytes);

                    packet.writeByte((byte) 8);

                    packet.writeBytesNoNull(new byte[23]);
                } else {
                    packet.writeLong(this.clientParam);
                    packet.writeLong(this.maxThreeBytes);
                }
            } else {
                packet.writeInt((int) this.clientParam);
                packet.writeLongInt(this.maxThreeBytes);
            }
        }

        packet.writeString(user, "Cp1252", this.connection);

        if (password.length() != 0) {

            packet.writeString("xxxxxxxx", "Cp1252", this.connection);
        } else {

            packet.writeString("", "Cp1252", this.connection);
        }

        if (this.useConnectWithDb) {
            packet.writeString(database, "Cp1252", this.connection);
        }

        send(packet, packet.getPosition());

        if (password.length() > 0) {
            Buffer b = readPacket();

            b.setPosition(0);

            byte[] replyAsBytes = b.getByteBuffer();

            if (replyAsBytes.length == 25 && replyAsBytes[0] != 0) {
                if (replyAsBytes[0] != 42) {

                    try {
                        byte[] buff = Security.passwordHashStage1(password);

                        byte[] passwordHash = new byte[buff.length];
                        System.arraycopy(buff, 0, passwordHash, 0, buff.length);

                        passwordHash = Security.passwordHashStage2(passwordHash, replyAsBytes);

                        byte[] packetDataAfterSalt = new byte[replyAsBytes.length - 5];

                        System.arraycopy(replyAsBytes, 4, packetDataAfterSalt, 0, replyAsBytes.length - 5);

                        byte[] mysqlScrambleBuff = new byte[20];

                        Security.passwordCrypt(packetDataAfterSalt, mysqlScrambleBuff, passwordHash, 20);

                        Security.passwordCrypt(mysqlScrambleBuff, buff, buff, 20);

                        Buffer packet2 = new Buffer(25);
                        packet2.writeBytesNoNull(buff);

                        this.packetSequence = (byte) (this.packetSequence + 1);

                        send(packet2, 24);
                    } catch (NoSuchAlgorithmException nse) {
                        throw SQLError.createSQLException(Messages.getString("MysqlIO.91") + Messages.getString("MysqlIO.92"), "S1000", getExceptionInterceptor());
                    }
                } else {

                    try {

                        byte[] passwordHash = Security.createKeyFromOldPassword(password);

                        byte[] netReadPos4 = new byte[replyAsBytes.length - 5];

                        System.arraycopy(replyAsBytes, 4, netReadPos4, 0, replyAsBytes.length - 5);

                        byte[] mysqlScrambleBuff = new byte[20];

                        Security.passwordCrypt(netReadPos4, mysqlScrambleBuff, passwordHash, 20);

                        String scrambledPassword = Util.scramble(StringUtils.toString(mysqlScrambleBuff), password);

                        Buffer packet2 = new Buffer(packLength);
                        packet2.writeString(scrambledPassword, "Cp1252", this.connection);
                        this.packetSequence = (byte) (this.packetSequence + 1);

                        send(packet2, 24);
                    } catch (NoSuchAlgorithmException nse) {
                        throw SQLError.createSQLException(Messages.getString("MysqlIO.93") + Messages.getString("MysqlIO.94"), "S1000", getExceptionInterceptor());
                    }
                }
            }
        }
    }

    void secureAuth411(Buffer packet, int packLength, String user, String password, String database, boolean writeClientParams) throws SQLException {
        if (packet == null) {
            packet = new Buffer(packLength);
        }

        if (writeClientParams) {
            if (this.use41Extensions) {
                if (versionMeetsMinimum(4, 1, 1)) {
                    packet.writeLong(this.clientParam);
                    packet.writeLong(this.maxThreeBytes);

                    packet.writeByte((byte) 33);

                    packet.writeBytesNoNull(new byte[23]);
                } else {
                    packet.writeLong(this.clientParam);
                    packet.writeLong(this.maxThreeBytes);
                }
            } else {
                packet.writeInt((int) this.clientParam);
                packet.writeLongInt(this.maxThreeBytes);
            }
        }

        packet.writeString(user, "utf-8", this.connection);

        if (password.length() != 0) {
            packet.writeByte((byte) 20);

            try {
                packet.writeBytesNoNull(Security.scramble411(password, this.seed, this.connection));
            } catch (NoSuchAlgorithmException nse) {
                throw SQLError.createSQLException(Messages.getString("MysqlIO.95") + Messages.getString("MysqlIO.96"), "S1000", getExceptionInterceptor());

            } catch (UnsupportedEncodingException e) {
                throw SQLError.createSQLException(Messages.getString("MysqlIO.95") + Messages.getString("MysqlIO.96"), "S1000", getExceptionInterceptor());
            }

        } else {

            packet.writeByte((byte) 0);
        }

        if (this.useConnectWithDb) {
            packet.writeString(database, "utf-8", this.connection);
        } else {

            packet.writeByte((byte) 0);
        }

        if ((this.serverCapabilities & 0x200) != 0) {

            String mysqlEncodingName = this.connection.getServerCharacterEncoding();
            if ("ucs2".equalsIgnoreCase(mysqlEncodingName) || "utf16".equalsIgnoreCase(mysqlEncodingName) || "utf16le".equalsIgnoreCase(mysqlEncodingName) || "uft32".equalsIgnoreCase(mysqlEncodingName)) {

                packet.writeByte((byte) 33);
                packet.writeByte((byte) 0);
            }
        }

        send(packet, packet.getPosition());

        byte savePacketSequence = this.packetSequence = (byte) (this.packetSequence + 1);

        Buffer reply = checkErrorPacket();

        if (reply.isLastDataPacket()) {

            this.packetSequence = savePacketSequence = (byte) (savePacketSequence + 1);
            packet.clear();

            String seed323 = this.seed.substring(0, 8);
            packet.writeString(Util.newCrypt(password, seed323));
            send(packet, packet.getPosition());

            checkErrorPacket();
        }
    }

    private final ResultSetRow unpackBinaryResultSetRow(Field[] fields, Buffer binaryData, int resultSetConcurrency) throws SQLException {
        int numFields = fields.length;

        byte[][] unpackedRowData = new byte[numFields][];

        int nullCount = (numFields + 9) / 8;

        byte[] nullBitMask = new byte[nullCount];

        for (int i = 0; i < nullCount; i++) {
            nullBitMask[i] = binaryData.readByte();
        }

        int nullMaskPos = 0;
        int bit = 4;

        for (int j = 0; j < numFields; j++) {
            if ((nullBitMask[nullMaskPos] & bit) != 0) {
                unpackedRowData[j] = null;
            } else if (resultSetConcurrency != 1008) {
                extractNativeEncodedColumn(binaryData, fields, j, unpackedRowData);
            } else {

                unpackNativeEncodedColumn(binaryData, fields, j, unpackedRowData);
            }

            if (((bit <<= 1) & 0xFF) == 0) {
                bit = 1;

                nullMaskPos++;
            }
        }

        return new ByteArrayRow(unpackedRowData, getExceptionInterceptor());
    }

    private final void extractNativeEncodedColumn(Buffer binaryData, Field[] fields, int columnIndex, byte[][] unpackedRowData) throws SQLException {
        int length;
        Field curField = fields[columnIndex];

        switch (curField.getMysqlType()) {
            case 6:
                return;

            case 1:
                (new byte[1])[0] = binaryData.readByte();
                unpackedRowData[columnIndex] = new byte[1];

            case 2:
            case 13:
                unpackedRowData[columnIndex] = binaryData.getBytes(2);

            case 3:
            case 9:
                unpackedRowData[columnIndex] = binaryData.getBytes(4);

            case 8:
                unpackedRowData[columnIndex] = binaryData.getBytes(8);

            case 4:
                unpackedRowData[columnIndex] = binaryData.getBytes(4);

            case 5:
                unpackedRowData[columnIndex] = binaryData.getBytes(8);

            case 11:
                length = (int) binaryData.readFieldLength();

                unpackedRowData[columnIndex] = binaryData.getBytes(length);

            case 10:
                length = (int) binaryData.readFieldLength();

                unpackedRowData[columnIndex] = binaryData.getBytes(length);

            case 7:
            case 12:
                length = (int) binaryData.readFieldLength();

                unpackedRowData[columnIndex] = binaryData.getBytes(length);

            case 0:
            case 15:
            case 246:
            case 249:
            case 250:
            case 251:
            case 252:
            case 253:
            case 254:
            case 255:
                unpackedRowData[columnIndex] = binaryData.readLenByteArray(0);

            case 16:
                unpackedRowData[columnIndex] = binaryData.readLenByteArray(0);
        }

        throw SQLError.createSQLException(Messages.getString("MysqlIO.97") + curField.getMysqlType() + Messages.getString("MysqlIO.98") + columnIndex + Messages.getString("MysqlIO.99") + fields.length + Messages.getString("MysqlIO.100"), "S1000", getExceptionInterceptor());
    }

    private final void unpackNativeEncodedColumn(Buffer binaryData, Field[] fields, int columnIndex, byte[][] unpackedRowData) throws SQLException {
        byte tinyVal;
        short shortVal;
        int intVal;
        long longVal;
        float floatVal;
        double doubleVal;
        int length, hour, minute, seconds;
        byte[] timeAsBytes;
        int year, month, day;
        byte[] arrayOfByte1;
        int i, j, nanos, k;
        byte[] arrayOfByte2, arrayOfByte3;
        byte b;
        boolean bool;
        Field curField = fields[columnIndex];

        switch (curField.getMysqlType()) {
            case 6:
                return;

            case 1:
                tinyVal = binaryData.readByte();

                if (!curField.isUnsigned()) {
                    unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(tinyVal));
                } else {

                    short unsignedTinyVal = (short) (tinyVal & 0xFF);

                    unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(unsignedTinyVal));
                }

            case 2:
            case 13:
                shortVal = (short) binaryData.readInt();

                if (!curField.isUnsigned()) {
                    unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(shortVal));
                } else {

                    int unsignedShortVal = shortVal & 0xFFFF;

                    unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(unsignedShortVal));
                }

            case 3:
            case 9:
                intVal = (int) binaryData.readLong();

                if (!curField.isUnsigned()) {
                    unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(intVal));
                } else {

                    long l = intVal & 0xFFFFFFFFL;

                    unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(l));
                }

            case 8:
                longVal = binaryData.readLongLong();

                if (!curField.isUnsigned()) {
                    unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(longVal));
                } else {

                    BigInteger asBigInteger = ResultSetImpl.convertLongToUlong(longVal);

                    unpackedRowData[columnIndex] = StringUtils.getBytes(asBigInteger.toString());
                }

            case 4:
                floatVal = Float.intBitsToFloat(binaryData.readIntAsLong());

                unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(floatVal));

            case 5:
                doubleVal = Double.longBitsToDouble(binaryData.readLongLong());

                unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(doubleVal));

            case 11:
                length = (int) binaryData.readFieldLength();

                hour = 0;
                minute = 0;
                seconds = 0;

                if (length != 0) {
                    binaryData.readByte();
                    binaryData.readLong();
                    hour = binaryData.readByte();
                    minute = binaryData.readByte();
                    seconds = binaryData.readByte();

                    if (length > 8) {
                        binaryData.readLong();
                    }
                }

                timeAsBytes = new byte[8];

                timeAsBytes[0] = (byte) Character.forDigit(hour / 10, 10);
                timeAsBytes[1] = (byte) Character.forDigit(hour % 10, 10);

                timeAsBytes[2] = 58;

                timeAsBytes[3] = (byte) Character.forDigit(minute / 10, 10);

                timeAsBytes[4] = (byte) Character.forDigit(minute % 10, 10);

                timeAsBytes[5] = 58;

                timeAsBytes[6] = (byte) Character.forDigit(seconds / 10, 10);

                timeAsBytes[7] = (byte) Character.forDigit(seconds % 10, 10);

                unpackedRowData[columnIndex] = timeAsBytes;

            case 10:
                length = (int) binaryData.readFieldLength();

                year = 0;
                month = 0;
                day = 0;

                hour = 0;
                minute = 0;
                seconds = 0;

                if (length != 0) {
                    year = binaryData.readInt();
                    month = binaryData.readByte();
                    day = binaryData.readByte();
                }

                if (year == 0 && month == 0 && day == 0)
                    if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior())) {
                        unpackedRowData[columnIndex] = null;
                    } else {
                        if ("exception".equals(this.connection.getZeroDateTimeBehavior())) {
                            throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Date", "S1009", getExceptionInterceptor());
                        }

                        year = 1;
                        month = 1;
                        day = 1;

                        byte[] dateAsBytes = new byte[10];

                        dateAsBytes[0] = (byte) Character.forDigit(year / 1000, 10);

                        int after1000 = year % 1000;

                        dateAsBytes[1] = (byte) Character.forDigit(after1000 / 100, 10);

                        int after100 = after1000 % 100;

                        dateAsBytes[2] = (byte) Character.forDigit(after100 / 10, 10);

                        dateAsBytes[3] = (byte) Character.forDigit(after100 % 10, 10);

                        dateAsBytes[4] = 45;

                        dateAsBytes[5] = (byte) Character.forDigit(month / 10, 10);

                        dateAsBytes[6] = (byte) Character.forDigit(month % 10, 10);

                        dateAsBytes[7] = 45;

                        dateAsBytes[8] = (byte) Character.forDigit(day / 10, 10);
                        dateAsBytes[9] = (byte) Character.forDigit(day % 10, 10);

                        unpackedRowData[columnIndex] = dateAsBytes;
                    }
                arrayOfByte1 = new byte[10];
                arrayOfByte1[0] = (byte) Character.forDigit(year / 1000, 10);
                i = year % 1000;
                arrayOfByte1[1] = (byte) Character.forDigit(i / 100, 10);
                j = i % 100;
                arrayOfByte1[2] = (byte) Character.forDigit(j / 10, 10);
                arrayOfByte1[3] = (byte) Character.forDigit(j % 10, 10);
                arrayOfByte1[4] = 45;
                arrayOfByte1[5] = (byte) Character.forDigit(month / 10, 10);
                arrayOfByte1[6] = (byte) Character.forDigit(month % 10, 10);
                arrayOfByte1[7] = 45;
                arrayOfByte1[8] = (byte) Character.forDigit(day / 10, 10);
                arrayOfByte1[9] = (byte) Character.forDigit(day % 10, 10);
                unpackedRowData[columnIndex] = arrayOfByte1;

            case 7:
            case 12:
                length = (int) binaryData.readFieldLength();

                year = 0;
                month = 0;
                day = 0;

                hour = 0;
                minute = 0;
                seconds = 0;

                nanos = 0;

                if (length != 0) {
                    year = binaryData.readInt();
                    month = binaryData.readByte();
                    day = binaryData.readByte();

                    if (length > 4) {
                        hour = binaryData.readByte();
                        minute = binaryData.readByte();
                        seconds = binaryData.readByte();
                    }
                }

                if (year == 0 && month == 0 && day == 0)
                    if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior())) {
                        unpackedRowData[columnIndex] = null;
                    } else {
                        if ("exception".equals(this.connection.getZeroDateTimeBehavior())) {
                            throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Timestamp", "S1009", getExceptionInterceptor());
                        }

                        year = 1;
                        month = 1;
                        day = 1;

                        int stringLength = 19;

                        byte[] nanosAsBytes = StringUtils.getBytes(Integer.toString(nanos));

                        stringLength += 1 + nanosAsBytes.length;

                        byte[] datetimeAsBytes = new byte[stringLength];

                        datetimeAsBytes[0] = (byte) Character.forDigit(year / 1000, 10);

                        i = year % 1000;

                        datetimeAsBytes[1] = (byte) Character.forDigit(i / 100, 10);

                        j = i % 100;

                        datetimeAsBytes[2] = (byte) Character.forDigit(j / 10, 10);

                        datetimeAsBytes[3] = (byte) Character.forDigit(j % 10, 10);

                        datetimeAsBytes[4] = 45;

                        datetimeAsBytes[5] = (byte) Character.forDigit(month / 10, 10);

                        datetimeAsBytes[6] = (byte) Character.forDigit(month % 10, 10);

                        datetimeAsBytes[7] = 45;

                        datetimeAsBytes[8] = (byte) Character.forDigit(day / 10, 10);

                        datetimeAsBytes[9] = (byte) Character.forDigit(day % 10, 10);

                        datetimeAsBytes[10] = 32;

                        datetimeAsBytes[11] = (byte) Character.forDigit(hour / 10, 10);

                        datetimeAsBytes[12] = (byte) Character.forDigit(hour % 10, 10);

                        datetimeAsBytes[13] = 58;

                        datetimeAsBytes[14] = (byte) Character.forDigit(minute / 10, 10);

                        datetimeAsBytes[15] = (byte) Character.forDigit(minute % 10, 10);

                        datetimeAsBytes[16] = 58;

                        datetimeAsBytes[17] = (byte) Character.forDigit(seconds / 10, 10);

                        datetimeAsBytes[18] = (byte) Character.forDigit(seconds % 10, 10);

                        datetimeAsBytes[19] = 46;

                        int nanosOffset = 20;

                        int m = 0;
                    }
                k = 19;
                arrayOfByte2 = StringUtils.getBytes(Integer.toString(nanos));
                k += 1 + arrayOfByte2.length;
                arrayOfByte3 = new byte[k];
                arrayOfByte3[0] = (byte) Character.forDigit(year / 1000, 10);
                i = year % 1000;
                arrayOfByte3[1] = (byte) Character.forDigit(i / 100, 10);
                j = i % 100;
                arrayOfByte3[2] = (byte) Character.forDigit(j / 10, 10);
                arrayOfByte3[3] = (byte) Character.forDigit(j % 10, 10);
                arrayOfByte3[4] = 45;
                arrayOfByte3[5] = (byte) Character.forDigit(month / 10, 10);
                arrayOfByte3[6] = (byte) Character.forDigit(month % 10, 10);
                arrayOfByte3[7] = 45;
                arrayOfByte3[8] = (byte) Character.forDigit(day / 10, 10);
                arrayOfByte3[9] = (byte) Character.forDigit(day % 10, 10);
                arrayOfByte3[10] = 32;
                arrayOfByte3[11] = (byte) Character.forDigit(hour / 10, 10);
                arrayOfByte3[12] = (byte) Character.forDigit(hour % 10, 10);
                arrayOfByte3[13] = 58;
                arrayOfByte3[14] = (byte) Character.forDigit(minute / 10, 10);
                arrayOfByte3[15] = (byte) Character.forDigit(minute % 10, 10);
                arrayOfByte3[16] = 58;
                arrayOfByte3[17] = (byte) Character.forDigit(seconds / 10, 10);
                arrayOfByte3[18] = (byte) Character.forDigit(seconds % 10, 10);
                arrayOfByte3[19] = 46;
                b = 20;
                bool = false;

            case 0:
            case 15:
            case 16:
            case 246:
            case 249:
            case 250:
            case 251:
            case 252:
            case 253:
            case 254:
                unpackedRowData[columnIndex] = binaryData.readLenByteArray(0);
        }

        throw SQLError.createSQLException(Messages.getString("MysqlIO.97") + curField.getMysqlType() + Messages.getString("MysqlIO.98") + columnIndex + Messages.getString("MysqlIO.99") + fields.length + Messages.getString("MysqlIO.100"), "S1000", getExceptionInterceptor());
    }

    private void negotiateSSLConnection(String user, String password, String database, int packLength) throws SQLException {
        if (!ExportControlled.enabled()) {
            throw new ConnectionFeatureNotAvailableException(this.connection, this.lastPacketSentTimeMs, null);
        }

        if ((this.serverCapabilities & 0x8000) != 0) {
            this.clientParam |= 0x8000L;
        }

        this.clientParam |= 0x800L;

        Buffer packet = new Buffer(packLength);

        if (this.use41Extensions) {
            packet.writeLong(this.clientParam);
            packet.writeLong(this.maxThreeBytes);
            int charsetIndex = 0;
            if (this.connection.getEncoding() != null) {
                charsetIndex = CharsetMapping.getCharsetIndexForMysqlEncodingName(CharsetMapping.getMysqlEncodingForJavaEncoding(this.connection.getEncoding(), this.connection));
            }
            packet.writeByte((charsetIndex == 0) ? 33 : (byte) charsetIndex);
            packet.writeBytesNoNull(new byte[23]);
        } else {
            packet.writeInt((int) this.clientParam);
        }

        send(packet, packet.getPosition());

        ExportControlled.transformSocketToSSLSocket(this);
    }

    protected int getServerStatus() {
        return this.serverStatus;
    }

    protected List<ResultSetRow> fetchRowsViaCursor(List<ResultSetRow> fetchedRows, long statementId, Field[] columnTypes, int fetchSize, boolean useBufferRowExplicit) throws SQLException {
        if (fetchedRows == null) {
            fetchedRows = new ArrayList<ResultSetRow>(fetchSize);
        } else {
            fetchedRows.clear();
        }

        this.sharedSendPacket.clear();

        this.sharedSendPacket.writeByte((byte) 28);
        this.sharedSendPacket.writeLong(statementId);
        this.sharedSendPacket.writeLong(fetchSize);

        sendCommand(28, null, this.sharedSendPacket, true, null, 0);

        ResultSetRow row = null;

        while ((row = nextRow(columnTypes, columnTypes.length, true, 1007, false, useBufferRowExplicit, false, null)) != null) {
            fetchedRows.add(row);
        }

        return fetchedRows;
    }

    protected long getThreadId() {
        return this.threadId;
    }

    protected boolean useNanosForElapsedTime() {
        return this.useNanosForElapsedTime;
    }

    protected long getSlowQueryThreshold() {
        return this.slowQueryThreshold;
    }

    protected String getQueryTimingUnits() {
        return this.queryTimingUnits;
    }

    protected int getCommandCount() {
        return this.commandCount;
    }

    private void checkTransactionState(int oldStatus) throws SQLException {
        boolean previouslyInTrans = ((oldStatus & 0x1) != 0);
        boolean currentlyInTrans = ((this.serverStatus & 0x1) != 0);

        if (previouslyInTrans && !currentlyInTrans) {
            this.connection.transactionCompleted();
        } else if (!previouslyInTrans && currentlyInTrans) {
            this.connection.transactionBegun();
        }
    }

    protected void setStatementInterceptors(List<StatementInterceptorV2> statementInterceptors) {
        this.statementInterceptors = statementInterceptors;
    }

    protected ExceptionInterceptor getExceptionInterceptor() {
        return this.exceptionInterceptor;
    }

    protected void setSocketTimeout(int milliseconds) throws SQLException {
        try {
            this.mysqlConnection.setSoTimeout(milliseconds);
        } catch (SocketException e) {
            SQLException sqlEx = SQLError.createSQLException("Invalid socket timeout value or state", "S1009", getExceptionInterceptor());
            sqlEx.initCause(e);

            throw sqlEx;
        }
    }
}

