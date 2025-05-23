package com.mchange.v2.resourcepool;

public interface EventSupportingResourcePool extends ResourcePool {
  void addResourcePoolListener(ResourcePoolListener paramResourcePoolListener) throws ResourcePoolException;

  void removeResourcePoolListener(ResourcePoolListener paramResourcePoolListener) throws ResourcePoolException;
}

