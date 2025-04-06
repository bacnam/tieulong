package org.apache.mina.filter.codec;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.session.IoSession;

public interface ProtocolDecoderOutput {
  void write(Object paramObject);
  
  void flush(IoFilter.NextFilter paramNextFilter, IoSession paramIoSession);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/ProtocolDecoderOutput.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */