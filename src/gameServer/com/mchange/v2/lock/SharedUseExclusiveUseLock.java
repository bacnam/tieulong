package com.mchange.v2.lock;

public interface SharedUseExclusiveUseLock {
  void acquireShared() throws InterruptedException;
  
  void relinquishShared();
  
  void acquireExclusive() throws InterruptedException;
  
  void relinquishExclusive();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/lock/SharedUseExclusiveUseLock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */