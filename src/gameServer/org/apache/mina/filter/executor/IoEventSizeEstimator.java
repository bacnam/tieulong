package org.apache.mina.filter.executor;

import org.apache.mina.core.session.IoEvent;

public interface IoEventSizeEstimator {
  int estimateSize(IoEvent paramIoEvent);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/executor/IoEventSizeEstimator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */