package org.apache.mina.filter.codec.demux;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public interface MessageDecoder {
    public static final MessageDecoderResult OK = MessageDecoderResult.OK;

    public static final MessageDecoderResult NEED_DATA = MessageDecoderResult.NEED_DATA;

    public static final MessageDecoderResult NOT_OK = MessageDecoderResult.NOT_OK;

    MessageDecoderResult decodable(IoSession paramIoSession, IoBuffer paramIoBuffer);

    MessageDecoderResult decode(IoSession paramIoSession, IoBuffer paramIoBuffer, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;

    void finishDecode(IoSession paramIoSession, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
}

