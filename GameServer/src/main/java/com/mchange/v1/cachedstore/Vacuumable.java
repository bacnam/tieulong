package com.mchange.v1.cachedstore;

public interface Vacuumable {
  void vacuum() throws CachedStoreException;
}

