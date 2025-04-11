package org.apache.mina.core.write;

import org.apache.mina.core.session.IoSession;

public interface WriteRequestQueue {
    WriteRequest poll(IoSession paramIoSession);

    void offer(IoSession paramIoSession, WriteRequest paramWriteRequest);

    boolean isEmpty(IoSession paramIoSession);

    void clear(IoSession paramIoSession);

    void dispose(IoSession paramIoSession);

    int size();
}

