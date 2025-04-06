package org.apache.mina.core.session;

public interface IoSessionConfig {
  int getReadBufferSize();
  
  void setReadBufferSize(int paramInt);
  
  int getMinReadBufferSize();
  
  void setMinReadBufferSize(int paramInt);
  
  int getMaxReadBufferSize();
  
  void setMaxReadBufferSize(int paramInt);
  
  int getThroughputCalculationInterval();
  
  long getThroughputCalculationIntervalInMillis();
  
  void setThroughputCalculationInterval(int paramInt);
  
  int getIdleTime(IdleStatus paramIdleStatus);
  
  long getIdleTimeInMillis(IdleStatus paramIdleStatus);
  
  void setIdleTime(IdleStatus paramIdleStatus, int paramInt);
  
  int getReaderIdleTime();
  
  long getReaderIdleTimeInMillis();
  
  void setReaderIdleTime(int paramInt);
  
  int getWriterIdleTime();
  
  long getWriterIdleTimeInMillis();
  
  void setWriterIdleTime(int paramInt);
  
  int getBothIdleTime();
  
  long getBothIdleTimeInMillis();
  
  void setBothIdleTime(int paramInt);
  
  int getWriteTimeout();
  
  long getWriteTimeoutInMillis();
  
  void setWriteTimeout(int paramInt);
  
  boolean isUseReadOperation();
  
  void setUseReadOperation(boolean paramBoolean);
  
  void setAll(IoSessionConfig paramIoSessionConfig);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/session/IoSessionConfig.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */