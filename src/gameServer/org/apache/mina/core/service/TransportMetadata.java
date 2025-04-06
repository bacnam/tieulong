package org.apache.mina.core.service;

import java.net.SocketAddress;
import java.util.Set;
import org.apache.mina.core.session.IoSessionConfig;

public interface TransportMetadata {
  String getProviderName();
  
  String getName();
  
  boolean isConnectionless();
  
  boolean hasFragmentation();
  
  Class<? extends SocketAddress> getAddressType();
  
  Set<Class<? extends Object>> getEnvelopeTypes();
  
  Class<? extends IoSessionConfig> getSessionConfigType();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/service/TransportMetadata.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */