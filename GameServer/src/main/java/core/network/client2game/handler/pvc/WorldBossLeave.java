package core.network.client2game.handler.pvc;

import business.player.Player;
import business.player.feature.worldboss.WorldBossFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.database.game.bo.WorldBossChallengeBO;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class WorldBossLeave
extends PlayerHandler
{
public static class Request
{
int bossId;
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
WorldBossChallengeBO challengBo = ((WorldBossFeature)player.getFeature(WorldBossFeature.class)).LeaveWorldBoss(req.bossId);
request.response(challengBo);
}
}

