package core.network.game2world;

import BaseCommon.CommLog;
import business.player.Player;
import business.player.PlayerMgr;
import com.zhonglian.server.websocket.def.MessageType;
import com.zhonglian.server.websocket.def.TerminalType;
import com.zhonglian.server.websocket.handler.MessageHeader;
import com.zhonglian.server.websocket.handler.requset.NotifyDispatcher;
import com.zhonglian.server.websocket.handler.requset.RequestDispatcher;
import com.zhonglian.server.websocket.handler.response.ResponseDispatcher;
import com.zhonglian.server.websocket.server.ServerMessageDispatcher;
import core.network.client2game.ClientSession;
import core.network.world2game.handler.WBaseHandler;
import core.server.ServerConfig;

public class WorldMessageDispatcher extends ServerMessageDispatcher<WorldSession> {
    @Override
    public void init() {
        RequestDispatcher<WorldSession> dRequest = new RequestDispatcher<WorldSession>(TerminalType.GameServer, ServerConfig.ServerID()) {
            public void forward(WorldSession session, MessageHeader header, String body) {
                CommLog.info("[WorldServerConnector]从World处收到一条转发至[{},{}]的Request信息", header.descType, header.descId);
            }
        };
        this.putDispatcher(MessageType.Request, dRequest);
        NotifyDispatcher<WorldSession> dNotify = new NotifyDispatcher<WorldSession>(TerminalType.GameServer, ServerConfig.ServerID(), dRequest) {
            public void forward(WorldSession session, MessageHeader header, String body) {
                CommLog.error("[WorldServerConnector]从World处收到一条转发至[{},{}]的Notify信息", header.descType, header.descId);
            }
        };
        this.putDispatcher(MessageType.Notify, dNotify);
        ResponseDispatcher<WorldSession> dResponse = new ResponseDispatcher<WorldSession>(TerminalType.GameServer, ServerConfig.ServerID()) {
            public void forward(WorldSession session, MessageHeader header, String body) {
                if (header.descType != TerminalType.Client.value()) {
                    CommLog.error("[WorldServerConnector]从World处收到一条转发至[{},{}]的Response信息", header.descType, header.descId);
                } else {
                    Player player = PlayerMgr.getInstance().getOnlinePlayerByCid(header.descId);
                    if (player != null) {
                        ClientSession clientSession = player.getClientSession();
                        if (clientSession != null) {
                            clientSession.sendPacket(header, body);
                        }
                    }
                }
            }
        };
        this.putDispatcher(MessageType.Response, dResponse);
        this.registerRequestHandlers(WBaseHandler.class);
    }
}
