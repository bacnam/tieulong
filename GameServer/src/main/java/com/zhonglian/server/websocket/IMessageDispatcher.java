package com.zhonglian.server.websocket;

import java.nio.ByteBuffer;

public interface IMessageDispatcher<Session extends BaseSession> {
    void init();

    int handleRawMessage(Session paramSession, ByteBuffer paramByteBuffer);
}

