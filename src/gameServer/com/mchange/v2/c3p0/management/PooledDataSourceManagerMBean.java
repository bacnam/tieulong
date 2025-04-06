package com.mchange.v2.c3p0.management;

import java.sql.SQLException;
import java.util.Collection;

public interface PooledDataSourceManagerMBean {
  String getIdentityToken();
  
  String getDataSourceName();
  
  void setDataSourceName(String paramString);
  
  int getNumConnectionsDefaultUser() throws SQLException;
  
  int getNumIdleConnectionsDefaultUser() throws SQLException;
  
  int getNumBusyConnectionsDefaultUser() throws SQLException;
  
  int getNumUnclosedOrphanedConnectionsDefaultUser() throws SQLException;
  
  float getEffectivePropertyCycleDefaultUser() throws SQLException;
  
  void softResetDefaultUser() throws SQLException;
  
  int getNumConnections(String paramString1, String paramString2) throws SQLException;
  
  int getNumIdleConnections(String paramString1, String paramString2) throws SQLException;
  
  int getNumBusyConnections(String paramString1, String paramString2) throws SQLException;
  
  int getNumUnclosedOrphanedConnections(String paramString1, String paramString2) throws SQLException;
  
  float getEffectivePropertyCycle(String paramString1, String paramString2) throws SQLException;
  
  void softReset(String paramString1, String paramString2) throws SQLException;
  
  int getNumBusyConnectionsAllUsers() throws SQLException;
  
  int getNumIdleConnectionsAllUsers() throws SQLException;
  
  int getNumConnectionsAllUsers() throws SQLException;
  
  int getNumUnclosedOrphanedConnectionsAllUsers() throws SQLException;
  
  int getThreadPoolSize() throws SQLException;
  
  int getThreadPoolNumActiveThreads() throws SQLException;
  
  int getThreadPoolNumIdleThreads() throws SQLException;
  
  int getThreadPoolNumTasksPending() throws SQLException;
  
  String sampleThreadPoolStackTraces() throws SQLException;
  
  String sampleThreadPoolStatus() throws SQLException;
  
  void softResetAllUsers() throws SQLException;
  
  int getNumUserPools() throws SQLException;
  
  Collection getAllUsers() throws SQLException;
  
  void hardReset() throws SQLException;
  
  void close() throws SQLException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/management/PooledDataSourceManagerMBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */