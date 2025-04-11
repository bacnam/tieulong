package org.apache.mina.filter.codec;

import org.apache.mina.core.session.IoSession;

public interface ProtocolCodecFactory {
    ProtocolEncoder getEncoder(IoSession paramIoSession) throws Exception;

    ProtocolDecoder getDecoder(IoSession paramIoSession) throws Exception;
}

