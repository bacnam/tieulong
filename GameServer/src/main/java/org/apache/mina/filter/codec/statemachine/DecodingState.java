package org.apache.mina.filter.codec.statemachine;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public interface DecodingState {
  DecodingState decode(IoBuffer paramIoBuffer, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;

  DecodingState finishDecode(ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
}

