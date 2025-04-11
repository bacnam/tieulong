package core.network.client2game.handler.base;

import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerBase;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.ClientSession;
import core.network.client2game.handler.BaseHandler;

import java.io.IOException;

public class PlayerInfo
        extends BaseHandler {
    public void handle(WebSocketRequest request, String message) throws IOException {
        ClientSession session = (ClientSession) request.getSession();
        Player player = PlayerMgr.getInstance().getPlayer(session.getOpenId(), session.getPlayerSid());
        if (player != null) {
            request.response(((PlayerBase) player.getFeature(PlayerBase.class)).fullInfo());
            return;
        }
        player = session.getPlayer();
        if (player != null) {
            request.response(((PlayerBase) player.getFeature(PlayerBase.class)).fullInfo());
            return;
        }
        request.response();
    }
}

