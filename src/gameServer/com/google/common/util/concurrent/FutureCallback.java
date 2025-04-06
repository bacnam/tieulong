package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;

@Beta
public interface FutureCallback<V> {
  void onSuccess(V paramV);
  
  void onFailure(Throwable paramThrowable);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/google/common/util/concurrent/FutureCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */