package org.apache.mina.transport.socket;

import java.net.InetSocketAddress;
import org.apache.mina.core.service.IoAcceptor;

public interface SocketAcceptor extends IoAcceptor {
  InetSocketAddress getLocalAddress();

  InetSocketAddress getDefaultLocalAddress();

  void setDefaultLocalAddress(InetSocketAddress paramInetSocketAddress);

  boolean isReuseAddress();

  void setReuseAddress(boolean paramBoolean);

  int getBacklog();

  void setBacklog(int paramInt);

  SocketSessionConfig getSessionConfig();
}

