package com.zhonglian.server.websocket.handler.requset;

import BaseCommon.CommLog;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.def.TerminalType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.IBaseHandler;
import com.zhonglian.server.websocket.handler.MessageDispatcher;
import com.zhonglian.server.websocket.handler.MessageHeader;
import com.zhonglian.server.websocket.server.ServerSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class RequestDispatcher<Session extends ServerSession> extends MessageDispatcher<Session> {
    protected final Map<String, RequestHandler> _requestHandlers;

    public RequestDispatcher(TerminalType thisServerType, int thisServerId) {
        super(thisServerType, thisServerId);
        this._requestHandlers = new HashMap<>();
    }

    public RequestDispatcher(TerminalType serverType, int serverId, Map<String, RequestHandler> requestHandlers) {
        super(serverType, serverId);
        this._requestHandlers = requestHandlers;
    }

    @Override
    public void handle(Session session, MessageHeader header, String data) {
        try {
            RequestHandler handler = this._requestHandlers.get(header.event);
            if (handler == null) {
                session.sendError(header, ErrorCode.Request_NotFoundHandler, "协议[" + header.event + "]未找到处理器");
                return;
            }

            handler.handleMessage(new WebSocketRequest(session, header), data);
        } catch (WSException var5) {
            CommLog.warn("handle [0x{}] request failed, reason: {}", header.event, var5.getMessage());
            session.sendError(header, var5.getErrorCode(), var5.getMessage());
            var5.callback();
        } catch (IOException var6) {
            CommLog.error("handle [0x{}] request parse message error, detail:{}", header.event, var6.getMessage(), var6);
            session.sendError(header, ErrorCode.Unknown, "proto trans error");
        } catch (Throwable var7) {
            CommLog.error("handle [0x{}] request failed, reason: {}", header.event, var7.getMessage(), var7);
            session.sendError(header, ErrorCode.Unknown, "internal error");
        }
    }

    @Override
    public void addHandler(IBaseHandler handler) {
        if (this._requestHandlers.containsKey(handler.getEvent())) {
            CommLog.error("有Handler重名:", handler.getEvent());
            System.exit(0);
        }

        this._requestHandlers.put(handler.getEvent(), (RequestHandler)handler);
    }

    public RequestHandler getHandler(int opcode) {
        return this._requestHandlers.get(opcode);
    }
}
