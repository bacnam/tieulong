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

public abstract class ResponseDispatcher<Session extends ServerSession>
        extends MessageDispatcher<Session> {
    protected final Map<String, ResponseHandler> _responseHandlers;

    public ResponseDispatcher(TerminalType thisServerType, int thisServerId) {
        super(thisServerType, thisServerId);
        this._responseHandlers = new HashMap<>();
    }

    public void handle(Session session, MessageHeader header, String body) {
        try {
            WebSocketResponse response = new WebSocketResponse((ServerSession) session, header);
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
                CommLog.info("[{}]handle failed, errorCode:{} , message:{}", new Object[]{header.event, Short.valueOf(header.errcode), body});
            }

        } catch (Exception e) {
            CommLog.error("handle [{}] response error, message: {}", new Object[]{header.event, e.getMessage(), e});
        }
    }

    public void addHandler(IBaseHandler handler) {
        this._responseHandlers.put(handler.getEvent(), (ResponseHandler) handler);
    }
}

