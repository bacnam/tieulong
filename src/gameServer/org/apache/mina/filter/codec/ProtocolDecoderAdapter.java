package org.apache.mina.filter.codec;

import org.apache.mina.core.session.IoSession;

public abstract class ProtocolDecoderAdapter implements ProtocolDecoder {
  public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {}
  
  public void dispose(IoSession session) throws Exception {}
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/ProtocolDecoderAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */