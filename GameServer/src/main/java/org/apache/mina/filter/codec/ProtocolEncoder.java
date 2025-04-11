package org.apache.mina.filter.codec;

import org.apache.mina.core.session.IoSession;

public interface ProtocolEncoder {
    void encode(IoSession paramIoSession, Object paramObject, ProtocolEncoderOutput paramProtocolEncoderOutput) throws Exception;

    void dispose(IoSession paramIoSession) throws Exception;
}

