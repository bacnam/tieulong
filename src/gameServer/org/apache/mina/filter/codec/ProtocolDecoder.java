package org.apache.mina.filter.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

public interface ProtocolDecoder {
  void decode(IoSession paramIoSession, IoBuffer paramIoBuffer, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;

  void finishDecode(IoSession paramIoSession, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;

  void dispose(IoSession paramIoSession) throws Exception;
}

