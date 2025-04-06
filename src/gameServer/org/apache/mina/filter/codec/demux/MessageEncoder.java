package org.apache.mina.filter.codec.demux;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public interface MessageEncoder<T> {
  void encode(IoSession paramIoSession, T paramT, ProtocolEncoderOutput paramProtocolEncoderOutput) throws Exception;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/demux/MessageEncoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */