package org.apache.mina.filter.codec.demux;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public abstract class MessageDecoderAdapter implements MessageDecoder {
  public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {}
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/demux/MessageDecoderAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */