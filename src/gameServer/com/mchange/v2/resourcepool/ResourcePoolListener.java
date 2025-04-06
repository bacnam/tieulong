package com.mchange.v2.resourcepool;

import java.util.EventListener;

public interface ResourcePoolListener extends EventListener {
  void resourceAcquired(ResourcePoolEvent paramResourcePoolEvent);
  
  void resourceCheckedIn(ResourcePoolEvent paramResourcePoolEvent);
  
  void resourceCheckedOut(ResourcePoolEvent paramResourcePoolEvent);
  
  void resourceRemoved(ResourcePoolEvent paramResourcePoolEvent);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/resourcepool/ResourcePoolListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */