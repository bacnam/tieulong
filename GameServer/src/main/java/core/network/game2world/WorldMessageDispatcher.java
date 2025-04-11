package core.network.game2world;

import BaseCommon.CommLog;
import business.player.Player;
import business.player.PlayerMgr;
import com.zhonglian.server.websocket.def.MessageType;
import com.zhonglian.server.websocket.def.TerminalType;
import com.zhonglian.server.websocket.handler.MessageDispatcher;
import com.zhonglian.server.websocket.handler.MessageHeader;
import com.zhonglian.server.websocket.handler.requset.NotifyDispatcher;
import com.zhonglian.server.websocket.handler.requset.RequestDispatcher;
import com.zhonglian.server.websocket.handler.response.ResponseDispatcher;
import com.zhonglian.server.websocket.server.ServerMessageDispatcher;
import core.network.client2game.ClientSession;
import core.network.world2game.handler.WBaseHandler;
import core.server.ServerConfig;

public class WorldMessageDispatcher
        extends ServerMessageDispatcher<WorldSession> {
    public void init() {
        RequestDispatcher<WorldSession> dRequest = new RequestDispatcher<WorldSession>(TerminalType.GameServer, ServerConfig.ServerID()) {

            public void forward(WorldSession session, MessageHeader header, String body) {
                CommLog.info("[WorldServerConnector]从World处收到一条转发至[{},{}]的Request信息", Byte.valueOf(header.descType), Long.valueOf(header.descId));
            }
        };

        putDispatcher(MessageType.Request, (MessageDispatcher) dRequest);
        NotifyDispatcher<WorldSession> dNotify = new NotifyDispatcher<WorldSession>(TerminalType.GameServer, ServerConfig.ServerID(), dRequest) {
            public void forward(WorldSession session, MessageHeader header, String body) {
                CommLog.error("[WorldServerConnector]从World处收到一条转发至[{},{}]的Notify信息", Byte.valueOf(header.descType), Long.valueOf(header.descId));
            }
        };
        putDispatcher(MessageType.Notify, (MessageDispatcher) dNotify);

        ResponseDispatcher<WorldSession> dResponse = new ResponseDispatcher<WorldSession>(TerminalType.GameServer, ServerConfig.ServerID()) {

            public void forward(WorldSession session, MessageHeader header, String body) {
                if (header.descType != TerminalType.Client.value()) {
                    CommLog.error("[WorldServerConnector]从World处收到一条转发至[{},{}]的Response信息", Byte.valueOf(header.descType), Long.valueOf(header.descId));
                    return;
                }
                Player player = PlayerMgr.getInstance().getOnlinePlayerByCid(header.descId);
                if (player == null) {
                    return;
                }

                ClientSession clientSession = player.getClientSession();
                if (clientSession == null) {
                    return;
                }

                clientSession.sendPacket(header, body);
            }
        };
        putDispatcher(MessageType.Response, (MessageDispatcher) dResponse);

        registerRequestHandlers(WBaseHandler.class);
    }
}

