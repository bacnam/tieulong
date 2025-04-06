package com.jolbox.bonecp;

public interface StatisticsMBean {
  double getConnectionWaitTimeAvg();
  
  double getStatementExecuteTimeAvg();
  
  double getStatementPrepareTimeAvg();
  
  int getTotalLeased();
  
  int getTotalFree();
  
  int getTotalCreatedConnections();
  
  long getCacheHits();
  
  long getCacheMiss();
  
  long getStatementsCached();
  
  long getStatementsPrepared();
  
  long getConnectionsRequested();
  
  long getCumulativeConnectionWaitTime();
  
  long getCumulativeStatementExecutionTime();
  
  long getCumulativeStatementPrepareTime();
  
  void resetStats();
  
  double getCacheHitRatio();
  
  long getStatementsExecuted();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/StatisticsMBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */