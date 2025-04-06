package org.apache.mina.core.write;

import org.apache.mina.core.session.IoSession;

public interface WriteRequestQueue {
  WriteRequest poll(IoSession paramIoSession);
  
  void offer(IoSession paramIoSession, WriteRequest paramWriteRequest);
  
  boolean isEmpty(IoSession paramIoSession);
  
  void clear(IoSession paramIoSession);
  
  void dispose(IoSession paramIoSession);
  
  int size();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/write/WriteRequestQueue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */