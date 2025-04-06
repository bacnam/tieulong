package org.apache.mina.filter.codec;

import org.apache.mina.core.session.IoSession;

public interface ProtocolEncoder {
  void encode(IoSession paramIoSession, Object paramObject, ProtocolEncoderOutput paramProtocolEncoderOutput) throws Exception;
  
  void dispose(IoSession paramIoSession) throws Exception;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/ProtocolEncoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */