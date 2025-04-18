package com.mysql.jdbc;

import com.mysql.jdbc.log.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.Executor;

public interface Connection extends Connection, ConnectionProperties {
  void changeUser(String paramString1, String paramString2) throws SQLException;

  void clearHasTriedMaster();

  PreparedStatement clientPrepareStatement(String paramString) throws SQLException;

  PreparedStatement clientPrepareStatement(String paramString, int paramInt) throws SQLException;

  PreparedStatement clientPrepareStatement(String paramString, int paramInt1, int paramInt2) throws SQLException;

  PreparedStatement clientPrepareStatement(String paramString, int[] paramArrayOfint) throws SQLException;

  PreparedStatement clientPrepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException;

  PreparedStatement clientPrepareStatement(String paramString, String[] paramArrayOfString) throws SQLException;

  int getActiveStatementCount();

  long getIdleFor();

  Log getLog() throws SQLException;

  String getServerCharacterEncoding();

  TimeZone getServerTimezoneTZ();

  String getStatementComment();

  boolean hasTriedMaster();

  boolean isInGlobalTx();

  void setInGlobalTx(boolean paramBoolean);

  boolean isMasterConnection();

  boolean isNoBackslashEscapesSet();

  boolean isSameResource(Connection paramConnection);

  boolean lowerCaseTableNames();

  boolean parserKnowsUnicode();

  void ping() throws SQLException;

  void resetServerState() throws SQLException;

  PreparedStatement serverPrepareStatement(String paramString) throws SQLException;

  PreparedStatement serverPrepareStatement(String paramString, int paramInt) throws SQLException;

  PreparedStatement serverPrepareStatement(String paramString, int paramInt1, int paramInt2) throws SQLException;

  PreparedStatement serverPrepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException;

  PreparedStatement serverPrepareStatement(String paramString, int[] paramArrayOfint) throws SQLException;

  PreparedStatement serverPrepareStatement(String paramString, String[] paramArrayOfString) throws SQLException;

  void setFailedOver(boolean paramBoolean);

  void setPreferSlaveDuringFailover(boolean paramBoolean);

  void setStatementComment(String paramString);

  void shutdownServer() throws SQLException;

  boolean supportsIsolationLevel();

  boolean supportsQuotedIdentifiers();

  boolean supportsTransactions();

  boolean versionMeetsMinimum(int paramInt1, int paramInt2, int paramInt3) throws SQLException;

  void reportQueryTime(long paramLong);

  boolean isAbonormallyLongQuery(long paramLong);

  void initializeExtension(Extension paramExtension) throws SQLException;

  int getAutoIncrementIncrement();

  boolean hasSameProperties(Connection paramConnection);

  Properties getProperties();

  String getHost();

  void setProxy(MySQLConnection paramMySQLConnection);

  boolean isServerLocal() throws SQLException;

  void setSchema(String paramString) throws SQLException;

  String getSchema() throws SQLException;

  void abort(Executor paramExecutor) throws SQLException;

  void setNetworkTimeout(Executor paramExecutor, int paramInt) throws SQLException;

  int getNetworkTimeout() throws SQLException;
}

