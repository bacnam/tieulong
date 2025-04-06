package org.apache.mina.core.service;

import java.net.SocketAddress;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSessionInitializer;

public interface IoConnector extends IoService {
  int getConnectTimeout();
  
  long getConnectTimeoutMillis();
  
  void setConnectTimeout(int paramInt);
  
  void setConnectTimeoutMillis(long paramLong);
  
  SocketAddress getDefaultRemoteAddress();
  
  void setDefaultRemoteAddress(SocketAddress paramSocketAddress);
  
  SocketAddress getDefaultLocalAddress();
  
  void setDefaultLocalAddress(SocketAddress paramSocketAddress);
  
  ConnectFuture connect();
  
  ConnectFuture connect(IoSessionInitializer<? extends ConnectFuture> paramIoSessionInitializer);
  
  ConnectFuture connect(SocketAddress paramSocketAddress);
  
  ConnectFuture connect(SocketAddress paramSocketAddress, IoSessionInitializer<? extends ConnectFuture> paramIoSessionInitializer);
  
  ConnectFuture connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2);
  
  ConnectFuture connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2, IoSessionInitializer<? extends ConnectFuture> paramIoSessionInitializer);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/service/IoConnector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */