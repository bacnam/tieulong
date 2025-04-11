package core.network.client2game.handler.pvc;

import business.global.worldboss.WorldBossMgr;
import business.player.Player;
import business.player.feature.worldboss.WorldBossFeature;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class WorldBossAutoCancel
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        WorldBossMgr.getInstance().cancelAuto(player);
        request.response(Boolean.valueOf(((WorldBossFeature) player.getFeature(WorldBossFeature.class)).getOrCreate().getAutoChallenge()));
    }
}

