package org.apache.mina.transport.socket;

import java.net.InetSocketAddress;
import org.apache.mina.core.service.IoConnector;

public interface SocketConnector extends IoConnector {
  InetSocketAddress getDefaultRemoteAddress();
  
  SocketSessionConfig getSessionConfig();
  
  void setDefaultRemoteAddress(InetSocketAddress paramInetSocketAddress);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/transport/socket/SocketConnector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */