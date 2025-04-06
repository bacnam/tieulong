package org.apache.mina.filter.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

public interface ProtocolDecoder {
  void decode(IoSession paramIoSession, IoBuffer paramIoBuffer, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
  
  void finishDecode(IoSession paramIoSession, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
  
  void dispose(IoSession paramIoSession) throws Exception;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/ProtocolDecoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */