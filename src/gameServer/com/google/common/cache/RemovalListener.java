package com.google.common.cache;

import com.google.common.annotations.Beta;

@Beta
public interface RemovalListener<K, V> {
  void onRemoval(RemovalNotification<K, V> paramRemovalNotification);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/google/common/cache/RemovalListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */