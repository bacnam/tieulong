package core.network.client2game.handler.pvc;

import business.global.worldboss.WorldBossMgr;
import business.player.Player;
import business.player.feature.worldboss.WorldBossFeature;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class WorldBossAuto extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        WorldBossMgr.getInstance().applyAuto(player);
        request.response(player.getFeature(WorldBossFeature.class).getOrCreate().getAutoChallenge());
    }
}
