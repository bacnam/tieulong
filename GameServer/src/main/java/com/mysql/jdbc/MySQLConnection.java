package com.mysql.jdbc;

import com.mysql.jdbc.log.Log;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Timer;

public interface MySQLConnection extends Connection, ConnectionProperties {
  boolean isProxySet();

  void abortInternal() throws SQLException;

  void checkClosed() throws SQLException;

  void createNewIO(boolean paramBoolean) throws SQLException;

  void dumpTestcaseQuery(String paramString);

  Connection duplicate() throws SQLException;

  ResultSetInternalMethods execSQL(StatementImpl paramStatementImpl, String paramString1, int paramInt1, Buffer paramBuffer, int paramInt2, int paramInt3, boolean paramBoolean, String paramString2, Field[] paramArrayOfField) throws SQLException;

  ResultSetInternalMethods execSQL(StatementImpl paramStatementImpl, String paramString1, int paramInt1, Buffer paramBuffer, int paramInt2, int paramInt3, boolean paramBoolean1, String paramString2, Field[] paramArrayOfField, boolean paramBoolean2) throws SQLException;

  String extractSqlFromPacket(String paramString, Buffer paramBuffer, int paramInt) throws SQLException;

  StringBuffer generateConnectionCommentBlock(StringBuffer paramStringBuffer);

  int getActiveStatementCount();

  int getAutoIncrementIncrement();

  CachedResultSetMetaData getCachedMetaData(String paramString);

  Calendar getCalendarInstanceForSessionOrNew();

  Timer getCancelTimer();

  String getCharacterSetMetadata();

  SingleByteCharsetConverter getCharsetConverter(String paramString) throws SQLException;

  String getCharsetNameForIndex(int paramInt) throws SQLException;

  TimeZone getDefaultTimeZone();

  String getErrorMessageEncoding();

  ExceptionInterceptor getExceptionInterceptor();

  String getHost();

  long getId();

  long getIdleFor();

  MysqlIO getIO() throws SQLException;

  Log getLog() throws SQLException;

  int getMaxBytesPerChar(String paramString) throws SQLException;

  int getMaxBytesPerChar(Integer paramInteger, String paramString) throws SQLException;

  Statement getMetadataSafeStatement() throws SQLException;

  int getNetBufferLength();

  Properties getProperties();

  boolean getRequiresEscapingEncoder();

  String getServerCharacterEncoding();

  int getServerMajorVersion();

  int getServerMinorVersion();

  int getServerSubMinorVersion();

  TimeZone getServerTimezoneTZ();

  String getServerVariable(String paramString);

  String getServerVersion();

  Calendar getSessionLockedCalendar();

  String getStatementComment();

  List<StatementInterceptorV2> getStatementInterceptorsInstances();

  String getURL();

  String getUser();

  Calendar getUtcCalendar();

  void incrementNumberOfPreparedExecutes();

  void incrementNumberOfPrepares();

  void incrementNumberOfResultSetsCreated();

  void initializeResultsMetadataFromCache(String paramString, CachedResultSetMetaData paramCachedResultSetMetaData, ResultSetInternalMethods paramResultSetInternalMethods) throws SQLException;

  void initializeSafeStatementInterceptors() throws SQLException;

  boolean isAbonormallyLongQuery(long paramLong);

  boolean isClientTzUTC();

  boolean isCursorFetchEnabled() throws SQLException;

  boolean isReadInfoMsgEnabled();

  boolean isReadOnly() throws SQLException;

  boolean isReadOnly(boolean paramBoolean) throws SQLException;

  boolean isRunningOnJDK13();

  boolean isServerTzUTC();

  boolean lowerCaseTableNames();

  void maxRowsChanged(Statement paramStatement);

  void pingInternal(boolean paramBoolean, int paramInt) throws SQLException;

  void realClose(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, Throwable paramThrowable) throws SQLException;

  void recachePreparedStatement(ServerPreparedStatement paramServerPreparedStatement) throws SQLException;

  void registerQueryExecutionTime(long paramLong);

  void registerStatement(Statement paramStatement);

  void reportNumberOfTablesAccessed(int paramInt);

  boolean serverSupportsConvertFn() throws SQLException;

  void setProxy(MySQLConnection paramMySQLConnection);

  void setReadInfoMsgEnabled(boolean paramBoolean);

  void setReadOnlyInternal(boolean paramBoolean) throws SQLException;

  void shutdownServer() throws SQLException;

  boolean storesLowerCaseTableName();

  void throwConnectionClosedException() throws SQLException;

  void transactionBegun() throws SQLException;

  void transactionCompleted() throws SQLException;

  void unregisterStatement(Statement paramStatement);

  void unSafeStatementInterceptors() throws SQLException;

  void unsetMaxRows(Statement paramStatement) throws SQLException;

  boolean useAnsiQuotedIdentifiers();

  boolean useMaxRows();

  MySQLConnection getLoadBalanceSafeProxy();
}

