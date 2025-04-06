package com.mchange.v2.c3p0.management;

import com.mchange.v2.c3p0.PooledDataSource;

public class NullManagementCoordinator implements ManagementCoordinator {
  public void attemptManageC3P0Registry() {}
  
  public void attemptUnmanageC3P0Registry() {}
  
  public void attemptManagePooledDataSource(PooledDataSource pds) {}
  
  public void attemptUnmanagePooledDataSource(PooledDataSource pds) {}
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/management/NullManagementCoordinator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */