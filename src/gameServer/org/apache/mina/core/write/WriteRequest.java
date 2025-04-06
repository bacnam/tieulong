package org.apache.mina.core.write;

import java.net.SocketAddress;
import org.apache.mina.core.future.WriteFuture;

public interface WriteRequest {
  WriteRequest getOriginalRequest();
  
  WriteFuture getFuture();
  
  Object getMessage();
  
  SocketAddress getDestination();
  
  boolean isEncoded();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/write/WriteRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */