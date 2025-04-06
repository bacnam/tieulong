package org.apache.mina.core.service;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.List;
import java.util.Set;
import org.apache.mina.core.session.IoSession;

public interface IoAcceptor extends IoService {
  SocketAddress getLocalAddress();
  
  Set<SocketAddress> getLocalAddresses();
  
  SocketAddress getDefaultLocalAddress();
  
  List<SocketAddress> getDefaultLocalAddresses();
  
  void setDefaultLocalAddress(SocketAddress paramSocketAddress);
  
  void setDefaultLocalAddresses(SocketAddress paramSocketAddress, SocketAddress... paramVarArgs);
  
  void setDefaultLocalAddresses(Iterable<? extends SocketAddress> paramIterable);
  
  void setDefaultLocalAddresses(List<? extends SocketAddress> paramList);
  
  boolean isCloseOnDeactivation();
  
  void setCloseOnDeactivation(boolean paramBoolean);
  
  void bind() throws IOException;
  
  void bind(SocketAddress paramSocketAddress) throws IOException;
  
  void bind(SocketAddress paramSocketAddress, SocketAddress... paramVarArgs) throws IOException;
  
  void bind(SocketAddress... paramVarArgs) throws IOException;
  
  void bind(Iterable<? extends SocketAddress> paramIterable) throws IOException;
  
  void unbind();
  
  void unbind(SocketAddress paramSocketAddress);
  
  void unbind(SocketAddress paramSocketAddress, SocketAddress... paramVarArgs);
  
  void unbind(Iterable<? extends SocketAddress> paramIterable);
  
  IoSession newSession(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/service/IoAcceptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */