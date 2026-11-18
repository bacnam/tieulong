package core.network.game2zone;

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
import core.network.zone2game.handler.ZBaseHandler;
import core.server.ServerConfig;

public class ZoneMessageDispatcher extends ServerMessageDispatcher<ZoneSession> {
    @Override
    public void init() {
        RequestDispatcher<ZoneSession> dRequest = new RequestDispatcher<ZoneSession>(TerminalType.GameServer, ServerConfig.ServerID()) {
            public void forward(ZoneSession session, MessageHeader header, String body) {
                CommLog.info("[ZoneServerConnector]从Zone处收到一条转发至[{},{}]的Request信息", header.descType, header.descId);
            }
        };
        this.putDispatcher(MessageType.Request, dRequest);
        NotifyDispatcher<ZoneSession> dNotify = new NotifyDispatcher<ZoneSession>(TerminalType.GameServer, ServerConfig.ServerID(), dRequest) {
            public void forward(ZoneSession session, MessageHeader header, String body) {
                CommLog.error("[ZoneServerConnector]从Zone处收到一条转发至[{},{}]的Notify信息", header.descType, header.descId);
            }
        };
        this.putDispatcher(MessageType.Notify, dNotify);
        ResponseDispatcher<ZoneSession> dResponse = new ResponseDispatcher<ZoneSession>(TerminalType.GameServer, ServerConfig.ServerID()) {
            public void forward(ZoneSession session, MessageHeader header, String body) {
                if (header.descType != TerminalType.Client.value()) {
                    CommLog.error("[ZoneServerConnector]从Zone处收到一条转发至[{},{}]的Response信息", header.descType, header.descId);
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
        this.registerRequestHandlers(ZBaseHandler.class);
    }
}
