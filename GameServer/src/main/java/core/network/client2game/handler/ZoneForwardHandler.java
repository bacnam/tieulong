package core.network.client2game.handler;

import business.player.Player;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.def.MessageType;
import com.zhonglian.server.websocket.def.TerminalType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.MessageHeader;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.game2zone.ZoneConnector;
import core.network.game2zone.ZoneSession;

import java.io.IOException;

public class ZoneForwardHandler
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        forwardToZone(player, request, message);
    }

    private void forwardToZone(Player player, WebSocketRequest request, String message) {
        ZoneSession zonesession = (ZoneSession) ZoneConnector.getInstance().getSocketSession();
        if (zonesession == null) {
            request.error(ErrorCode.Server_NotConnected, "未连接上Zone跨服服务器", new Object[0]);
            return;
        }
        MessageHeader header = new MessageHeader();
        header.messageType = MessageType.Request;
        header.srcType = TerminalType.Client.value();
        header.srcId = player.getPid();
        header.descType = TerminalType.ZoneServer.value();
        header.descId = zonesession.getRemoteServerID();
        header.event = (request.getHeader()).event;
        header.sequence = (request.getHeader()).sequence;
        zonesession.forward(header, message);
    }
}

