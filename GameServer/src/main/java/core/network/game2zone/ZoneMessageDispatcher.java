package core.network.game2zone;

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
import com.zhonglian.server.websocket.server.ServerSession;
import core.network.client2game.ClientSession;
import core.network.zone2game.handler.ZBaseHandler;
import core.server.ServerConfig;

public class ZoneMessageDispatcher
extends ServerMessageDispatcher<ZoneSession> {
public void init() {
RequestDispatcher<ZoneSession> dRequest = new RequestDispatcher<ZoneSession>(TerminalType.GameServer, ServerConfig.ServerID())
{

public void forward(ZoneSession session, MessageHeader header, String body)
{
CommLog.info("[ZoneServerConnector]从Zone处收到一条转发至[{},{}]的Request信息", Byte.valueOf(header.descType), Long.valueOf(header.descId));
}
};

putDispatcher(MessageType.Request, (MessageDispatcher)dRequest);
NotifyDispatcher<ZoneSession> dNotify = new NotifyDispatcher<ZoneSession>(TerminalType.GameServer, ServerConfig.ServerID(), dRequest)
{
public void forward(ZoneSession session, MessageHeader header, String body)
{
CommLog.error("[ZoneServerConnector]从Zone处收到一条转发至[{},{}]的Notify信息", Byte.valueOf(header.descType), Long.valueOf(header.descId));
}
};
putDispatcher(MessageType.Notify, (MessageDispatcher)dNotify);

ResponseDispatcher<ZoneSession> dResponse = new ResponseDispatcher<ZoneSession>(TerminalType.GameServer, ServerConfig.ServerID())
{

public void forward(ZoneSession session, MessageHeader header, String body)
{
if (header.descType != TerminalType.Client.value()) {
CommLog.error("[ZoneServerConnector]从Zone处收到一条转发至[{},{}]的Response信息", Byte.valueOf(header.descType), Long.valueOf(header.descId));
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
putDispatcher(MessageType.Response, (MessageDispatcher)dResponse);

registerRequestHandlers(ZBaseHandler.class);
}
}

