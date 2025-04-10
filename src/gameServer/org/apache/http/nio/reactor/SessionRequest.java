package org.apache.http.nio.reactor;

import java.io.IOException;
import java.net.SocketAddress;

public interface SessionRequest {
  SocketAddress getRemoteAddress();

  SocketAddress getLocalAddress();

  Object getAttachment();

  boolean isCompleted();

  IOSession getSession();

  IOException getException();

  void waitFor() throws InterruptedException;

  void setConnectTimeout(int paramInt);

  int getConnectTimeout();

  void cancel();
}

