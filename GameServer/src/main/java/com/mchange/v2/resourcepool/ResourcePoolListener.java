package com.mchange.v2.resourcepool;

import java.util.EventListener;

public interface ResourcePoolListener extends EventListener {
  void resourceAcquired(ResourcePoolEvent paramResourcePoolEvent);

  void resourceCheckedIn(ResourcePoolEvent paramResourcePoolEvent);

  void resourceCheckedOut(ResourcePoolEvent paramResourcePoolEvent);

  void resourceRemoved(ResourcePoolEvent paramResourcePoolEvent);
}

