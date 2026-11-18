package core.network.game2world;

import BaseCommon.CommLog;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.def.MessageType;
import com.zhonglian.server.websocket.def.TerminalType;
import com.zhonglian.server.websocket.handler.MessageHeader;
import com.zhonglian.server.websocket.handler.requset.NotifyDispatcher;
import com.zhonglian.server.websocket.handler.requset.RequestDispatcher;
import com.zhonglian.server.websocket.handler.response.ResponseDispatcher;
import com.zhonglian.server.websocket.server.ServerMessageDispatcher;
import core.server.ServerConfig;

public class GameMessageDispatcher extends ServerMessageDispatcher<GameSession> {
    @Override
    public void init() {
        RequestDispatcher<GameSession> dRequest = new RequestDispatcher<GameSession>(TerminalType.WorldServer, ServerConfig.ServerID()) {
            public void forward(GameSession session, MessageHeader header, String data) {
                if (header.descType != TerminalType.GameServer.value()) {
                    CommLog.error("[GameSocketServer]收到转发消息不是转发到GameServer的无法处理.转发目标[{},{}]", header.descType, header.descId);
                } else {
                    GameSession targetSession = GameSessionMgr.getInstance().getSessionByServerID((int)header.descId);
                    if (targetSession == null) {
                        CommLog.error("[GameSocketServer]收到转发消息的转发目标[{},{}]不存在", header.descType, header.descId);
                        session.sendError(header, ErrorCode.Server_NotFound, "转发消息的转发目标[" + header.descType + "," + header.descId + "]不存在");
                    } else {
                        targetSession.forward(header, data);
                    }
                }
            }
        };
        this.putDispatcher(MessageType.Request, dRequest);
        NotifyDispatcher<GameSession> dNotify = new NotifyDispatcher<GameSession>(TerminalType.WorldServer, ServerConfig.ServerID(), dRequest) {
            public void forward(GameSession session, MessageHeader header, String data) {
                if (header.descType != TerminalType.GameServer.value()) {
                    CommLog.error("[GameSocketServer]收到转发消息不是转发到GameServer的无法处理.转发目标[{},{}]", header.descType, header.descId);
                } else {
                    GameSession targetSession = GameSessionMgr.getInstance().getSessionByServerID((int)header.descId);
                    if (targetSession == null) {
                        CommLog.error("[GameSocketServer]收到转发消息的转发目标[{},{}]不存在", header.descType, header.descId);
                    } else {
                        targetSession.forward(header, data);
                    }
                }
            }
        };
        this.putDispatcher(MessageType.Notify, dNotify);
        ResponseDispatcher<GameSession> dResponse = new ResponseDispatcher<GameSession>(TerminalType.WorldServer, ServerConfig.ServerID()) {
            public void forward(GameSession session, MessageHeader header, String data) {
                if (header.descType != TerminalType.GameServer.value()) {
                    CommLog.error("[GameSocketServer]收到转发消息不是转发到GameServer的无法处理.转发目标[{},{}]", header.descType, header.descId);
                } else {
                    GameSession targetSession = GameSessionMgr.getInstance().getSessionByServerID((int)header.descId);
                    if (targetSession == null) {
                        CommLog.error("[GameSocketServer]收到转发消息的转发目标[{},{}]不存在", header.descType, header.descId);
                    } else {
                        targetSession.forward(header, data);
                    }
                }
            }
        };
        this.putDispatcher(MessageType.Response, dResponse);
        this.registerRequestHandlers(GRequestHandler.class);
    }
}
