package com.zhonglian.server.websocket.handler.response;

import BaseCommon.CommLog;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.def.TerminalType;
import com.zhonglian.server.websocket.handler.IBaseHandler;
import com.zhonglian.server.websocket.handler.MessageDispatcher;
import com.zhonglian.server.websocket.handler.MessageHeader;
import com.zhonglian.server.websocket.server.ServerSession;
import com.zhonglian.server.websocket.server.ServerSocketRequest;
import java.util.HashMap;
import java.util.Map;

public abstract class ResponseDispatcher<Session extends ServerSession> extends MessageDispatcher<Session> {
    protected final Map<String, ResponseHandler> _responseHandlers = new HashMap<>();

    public ResponseDispatcher(TerminalType thisServerType, int thisServerId) {
        super(thisServerType, thisServerId);
    }

    @Override
    public void handle(Session session, MessageHeader header, String body) {
        try {
            WebSocketResponse response = new WebSocketResponse(session, header);
            ResponseHandler handler = null;
            ServerSocketRequest request = session.popRequest(header.event, header.sequence);
            if (request != null) {
                handler = request.getResponseHandler();
            }

            if (handler == null) {
                handler = this._responseHandlers.get(header.event);
            }

            if (handler != null) {
                if (header.errcode == ErrorCode.Success.value()) {
                    handler.handleResponse(response, body);
                } else {
                    handler.handleError(response, header.errcode, body);
                }
            } else if (header.errcode == ErrorCode.Success.value()) {
                CommLog.info("[{}]handle success", header.event);
            } else {
                CommLog.info("[{}]handle failed, errorCode:{} , message:{}", header.event, header.errcode, body);
            }
        } catch (Exception var7) {
            CommLog.error("handle [{}] response error, message: {}", header.event, var7.getMessage(), var7);
        }
    }

    @Override
    public void addHandler(IBaseHandler handler) {
        this._responseHandlers.put(handler.getEvent(), (ResponseHandler)handler);
    }
}
