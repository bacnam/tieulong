package com.mchange.v2.c3p0;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import javax.sql.DataSource;

public interface PooledDataSource extends DataSource {
  String getIdentityToken();

  String getDataSourceName();

  void setDataSourceName(String paramString);

  Map getExtensions();

  void setExtensions(Map paramMap);

  int getNumConnections() throws SQLException;

  int getNumIdleConnections() throws SQLException;

  int getNumBusyConnections() throws SQLException;

  int getNumUnclosedOrphanedConnections() throws SQLException;

  int getNumConnectionsDefaultUser() throws SQLException;

  int getNumIdleConnectionsDefaultUser() throws SQLException;

  int getNumBusyConnectionsDefaultUser() throws SQLException;

  int getNumUnclosedOrphanedConnectionsDefaultUser() throws SQLException;

  int getStatementCacheNumStatementsDefaultUser() throws SQLException;

  int getStatementCacheNumCheckedOutDefaultUser() throws SQLException;

  int getStatementCacheNumConnectionsWithCachedStatementsDefaultUser() throws SQLException;

  long getStartTimeMillisDefaultUser() throws SQLException;

  long getUpTimeMillisDefaultUser() throws SQLException;

  long getNumFailedCheckinsDefaultUser() throws SQLException;

  long getNumFailedCheckoutsDefaultUser() throws SQLException;

  long getNumFailedIdleTestsDefaultUser() throws SQLException;

  float getEffectivePropertyCycleDefaultUser() throws SQLException;

  int getNumThreadsAwaitingCheckoutDefaultUser() throws SQLException;

  void softResetDefaultUser() throws SQLException;

  int getNumConnections(String paramString1, String paramString2) throws SQLException;

  int getNumIdleConnections(String paramString1, String paramString2) throws SQLException;

  int getNumBusyConnections(String paramString1, String paramString2) throws SQLException;

  int getNumUnclosedOrphanedConnections(String paramString1, String paramString2) throws SQLException;

  int getStatementCacheNumStatements(String paramString1, String paramString2) throws SQLException;

  int getStatementCacheNumCheckedOut(String paramString1, String paramString2) throws SQLException;

  int getStatementCacheNumConnectionsWithCachedStatements(String paramString1, String paramString2) throws SQLException;

  float getEffectivePropertyCycle(String paramString1, String paramString2) throws SQLException;

  int getNumThreadsAwaitingCheckout(String paramString1, String paramString2) throws SQLException;

  void softReset(String paramString1, String paramString2) throws SQLException;

  int getNumBusyConnectionsAllUsers() throws SQLException;

  int getNumIdleConnectionsAllUsers() throws SQLException;

  int getNumConnectionsAllUsers() throws SQLException;

  int getNumUnclosedOrphanedConnectionsAllUsers() throws SQLException;

  int getStatementCacheNumStatementsAllUsers() throws SQLException;

  int getStatementCacheNumCheckedOutStatementsAllUsers() throws SQLException;

  int getStatementCacheNumConnectionsWithCachedStatementsAllUsers() throws SQLException;

  int getStatementDestroyerNumConnectionsInUseAllUsers() throws SQLException;

  int getStatementDestroyerNumConnectionsWithDeferredDestroyStatementsAllUsers() throws SQLException;

  int getStatementDestroyerNumDeferredDestroyStatementsAllUsers() throws SQLException;

  int getThreadPoolSize() throws SQLException;

  int getThreadPoolNumActiveThreads() throws SQLException;

  int getThreadPoolNumIdleThreads() throws SQLException;

  int getThreadPoolNumTasksPending() throws SQLException;

  int getStatementDestroyerNumThreads() throws SQLException;

  int getStatementDestroyerNumActiveThreads() throws SQLException;

  int getStatementDestroyerNumIdleThreads() throws SQLException;

  int getStatementDestroyerNumTasksPending() throws SQLException;

  String sampleThreadPoolStackTraces() throws SQLException;

  String sampleThreadPoolStatus() throws SQLException;

  String sampleStatementDestroyerStackTraces() throws SQLException;

  String sampleStatementDestroyerStatus() throws SQLException;

  String sampleStatementCacheStatusDefaultUser() throws SQLException;

  String sampleStatementCacheStatus(String paramString1, String paramString2) throws SQLException;

  Throwable getLastAcquisitionFailureDefaultUser() throws SQLException;

  Throwable getLastCheckinFailureDefaultUser() throws SQLException;

  Throwable getLastCheckoutFailureDefaultUser() throws SQLException;

  Throwable getLastIdleTestFailureDefaultUser() throws SQLException;

  Throwable getLastConnectionTestFailureDefaultUser() throws SQLException;

  int getStatementDestroyerNumConnectionsInUseDefaultUser() throws SQLException;

  int getStatementDestroyerNumConnectionsWithDeferredDestroyStatementsDefaultUser() throws SQLException;

  int getStatementDestroyerNumDeferredDestroyStatementsDefaultUser() throws SQLException;

  Throwable getLastAcquisitionFailure(String paramString1, String paramString2) throws SQLException;

  Throwable getLastCheckinFailure(String paramString1, String paramString2) throws SQLException;

  Throwable getLastCheckoutFailure(String paramString1, String paramString2) throws SQLException;

  Throwable getLastIdleTestFailure(String paramString1, String paramString2) throws SQLException;

  Throwable getLastConnectionTestFailure(String paramString1, String paramString2) throws SQLException;

  int getStatementDestroyerNumConnectionsInUse(String paramString1, String paramString2) throws SQLException;

  int getStatementDestroyerNumConnectionsWithDeferredDestroyStatements(String paramString1, String paramString2) throws SQLException;

  int getStatementDestroyerNumDeferredDestroyStatements(String paramString1, String paramString2) throws SQLException;

  String sampleLastAcquisitionFailureStackTraceDefaultUser() throws SQLException;

  String sampleLastCheckinFailureStackTraceDefaultUser() throws SQLException;

  String sampleLastCheckoutFailureStackTraceDefaultUser() throws SQLException;

  String sampleLastIdleTestFailureStackTraceDefaultUser() throws SQLException;

  String sampleLastConnectionTestFailureStackTraceDefaultUser() throws SQLException;

  String sampleLastAcquisitionFailureStackTrace(String paramString1, String paramString2) throws SQLException;

  String sampleLastCheckinFailureStackTrace(String paramString1, String paramString2) throws SQLException;

  String sampleLastCheckoutFailureStackTrace(String paramString1, String paramString2) throws SQLException;

  String sampleLastIdleTestFailureStackTrace(String paramString1, String paramString2) throws SQLException;

  String sampleLastConnectionTestFailureStackTrace(String paramString1, String paramString2) throws SQLException;

  void softResetAllUsers() throws SQLException;

  int getNumUserPools() throws SQLException;

  int getNumHelperThreads() throws SQLException;

  Collection getAllUsers() throws SQLException;

  void hardReset() throws SQLException;

  void close() throws SQLException;

  void close(boolean paramBoolean) throws SQLException;
}

