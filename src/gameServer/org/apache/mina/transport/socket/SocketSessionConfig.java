package org.apache.mina.transport.socket;

import org.apache.mina.core.session.IoSessionConfig;

public interface SocketSessionConfig extends IoSessionConfig {
  boolean isReuseAddress();
  
  void setReuseAddress(boolean paramBoolean);
  
  int getReceiveBufferSize();
  
  void setReceiveBufferSize(int paramInt);
  
  int getSendBufferSize();
  
  void setSendBufferSize(int paramInt);
  
  int getTrafficClass();
  
  void setTrafficClass(int paramInt);
  
  boolean isKeepAlive();
  
  void setKeepAlive(boolean paramBoolean);
  
  boolean isOobInline();
  
  void setOobInline(boolean paramBoolean);
  
  int getSoLinger();
  
  void setSoLinger(int paramInt);
  
  boolean isTcpNoDelay();
  
  void setTcpNoDelay(boolean paramBoolean);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/transport/socket/SocketSessionConfig.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */