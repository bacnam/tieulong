package com.mchange.v2.async;

import com.mchange.v1.util.ClosableResource;

public interface AsynchronousRunner extends ClosableResource {
  void postRunnable(Runnable paramRunnable);

  void close(boolean paramBoolean);

  void close();
}

