package com.mchange.v2.resourcepool;

public interface EventSupportingResourcePool extends ResourcePool {
  void addResourcePoolListener(ResourcePoolListener paramResourcePoolListener) throws ResourcePoolException;
  
  void removeResourcePoolListener(ResourcePoolListener paramResourcePoolListener) throws ResourcePoolException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/resourcepool/EventSupportingResourcePool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */