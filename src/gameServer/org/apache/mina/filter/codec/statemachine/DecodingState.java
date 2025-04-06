package org.apache.mina.filter.codec.statemachine;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public interface DecodingState {
  DecodingState decode(IoBuffer paramIoBuffer, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
  
  DecodingState finishDecode(ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/statemachine/DecodingState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */