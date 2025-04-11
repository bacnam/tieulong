package org.apache.mina.filter.codec.demux;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public interface MessageEncoder<T> {
    void encode(IoSession paramIoSession, T paramT, ProtocolEncoderOutput paramProtocolEncoderOutput) throws Exception;
}

