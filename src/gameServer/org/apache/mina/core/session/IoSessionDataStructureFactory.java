package org.apache.mina.core.session;

import org.apache.mina.core.write.WriteRequestQueue;

public interface IoSessionDataStructureFactory {
  IoSessionAttributeMap getAttributeMap(IoSession paramIoSession) throws Exception;
  
  WriteRequestQueue getWriteRequestQueue(IoSession paramIoSession) throws Exception;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/session/IoSessionDataStructureFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */