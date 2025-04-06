package com.mchange.v2.c3p0.management;

import com.mchange.v2.c3p0.PooledDataSource;

public interface ManagementCoordinator {
  void attemptManageC3P0Registry();
  
  void attemptUnmanageC3P0Registry();
  
  void attemptManagePooledDataSource(PooledDataSource paramPooledDataSource);
  
  void attemptUnmanagePooledDataSource(PooledDataSource paramPooledDataSource);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/management/ManagementCoordinator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */