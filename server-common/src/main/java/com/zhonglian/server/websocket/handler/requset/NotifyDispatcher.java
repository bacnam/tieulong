package com.zhonglian.server.websocket.handler.requset;

import BaseCommon.CommLog;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.def.TerminalType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.MessageHeader;
import com.zhonglian.server.websocket.server.ServerSession;
import java.io.IOException;

public abstract class NotifyDispatcher<Session extends ServerSession> extends RequestDispatcher<Session> {
    public NotifyDispatcher(TerminalType serverType, int serverId, RequestDispatcher<Session> requestDispatcher) {
        super(serverType, serverId, requestDispatcher._requestHandlers);
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
            CommLog.warn("handle [0x{}] notify failed, reason:{}", header.event, var5.getMessage());
            var5.callback();
        } catch (IOException var6) {
            CommLog.error("handle [0x{}] notify parse message error, detail:{}", header.event, var6.getMessage(), var6);
        } catch (Throwable var7) {
            CommLog.error("handle [0x{}] notify failed, reason:{}", header.event, var7.getMessage(), var7);
        }
    }
}
