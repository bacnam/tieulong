package com.zhonglian.server.websocket.server;

import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.handler.MessageHeader;
import com.zhonglian.server.websocket.handler.response.ResponseHandler;
import com.zhonglian.server.websocket.handler.response.WebSocketResponse;

public class ServerSocketRequest {
    private static long TIME_OUT = 8000L;

    private long beginTime = 0L;
    private String operation;
    private short sequence = 0;
    private ServerSession session = null;
    private ResponseHandler handler;

    public ServerSocketRequest(ServerSession session, String operation, short sequence, ResponseHandler handler) {
        this.session = session;
        this.operation = operation;
        this.sequence = sequence;
        this.handler = handler;
        this.beginTime = System.currentTimeMillis();
    }

    public short getSequence() {
        return this.sequence;
    }

    public boolean isTimeout() {
        return (System.currentTimeMillis() > this.beginTime + TIME_OUT);
    }

    public ResponseHandler getResponseHandler() {
        return this.handler;
    }

    public void expired() {
        if (this.handler != null) {
            MessageHeader header = new MessageHeader();
            header.event = this.operation;
            header.sequence = this.sequence;

            header.descType = 0;
            header.descId = 0L;

            WebSocketResponse response = new WebSocketResponse(this.session, header);
            this.handler.handleError(response, ErrorCode.Request_RequestTimeout.value(), "handle timeout");
        }
    }
}

