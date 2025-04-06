package org.apache.mina.core.session;

import java.util.Set;

public interface IoSessionAttributeMap {
  Object getAttribute(IoSession paramIoSession, Object paramObject1, Object paramObject2);
  
  Object setAttribute(IoSession paramIoSession, Object paramObject1, Object paramObject2);
  
  Object setAttributeIfAbsent(IoSession paramIoSession, Object paramObject1, Object paramObject2);
  
  Object removeAttribute(IoSession paramIoSession, Object paramObject);
  
  boolean removeAttribute(IoSession paramIoSession, Object paramObject1, Object paramObject2);
  
  boolean replaceAttribute(IoSession paramIoSession, Object paramObject1, Object paramObject2, Object paramObject3);
  
  boolean containsAttribute(IoSession paramIoSession, Object paramObject);
  
  Set<Object> getAttributeKeys(IoSession paramIoSession);
  
  void dispose(IoSession paramIoSession) throws Exception;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/session/IoSessionAttributeMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */