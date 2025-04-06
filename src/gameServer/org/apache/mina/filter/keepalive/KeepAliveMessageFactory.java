package org.apache.mina.filter.keepalive;

import org.apache.mina.core.session.IoSession;

public interface KeepAliveMessageFactory {
  boolean isRequest(IoSession paramIoSession, Object paramObject);
  
  boolean isResponse(IoSession paramIoSession, Object paramObject);
  
  Object getRequest(IoSession paramIoSession);
  
  Object getResponse(IoSession paramIoSession, Object paramObject);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/keepalive/KeepAliveMessageFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */