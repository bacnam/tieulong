package org.apache.mina.transport.socket;

import java.net.InetSocketAddress;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IoSessionRecycler;

public interface DatagramAcceptor extends IoAcceptor {
  InetSocketAddress getLocalAddress();

  InetSocketAddress getDefaultLocalAddress();

  void setDefaultLocalAddress(InetSocketAddress paramInetSocketAddress);

  IoSessionRecycler getSessionRecycler();

  void setSessionRecycler(IoSessionRecycler paramIoSessionRecycler);

  DatagramSessionConfig getSessionConfig();
}

