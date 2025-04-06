package com.mchange.v2.async;

import com.mchange.v1.util.ClosableResource;

public interface AsynchronousRunner extends ClosableResource {
  void postRunnable(Runnable paramRunnable);
  
  void close(boolean paramBoolean);
  
  void close();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/async/AsynchronousRunner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */