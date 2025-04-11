package org.apache.mina.filter.keepalive;

import org.apache.mina.core.session.IoSession;

public interface KeepAliveMessageFactory {
    boolean isRequest(IoSession paramIoSession, Object paramObject);

    boolean isResponse(IoSession paramIoSession, Object paramObject);

    Object getRequest(IoSession paramIoSession);

    Object getResponse(IoSession paramIoSession, Object paramObject);
}

