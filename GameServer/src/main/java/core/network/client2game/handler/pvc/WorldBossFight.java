package core.network.client2game.handler.pvc;

import business.global.worldboss.WorldBossMgr;
import business.player.Player;
import business.player.feature.worldboss.WorldBossFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.database.game.bo.WorldBossBO;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class WorldBossFight
extends PlayerHandler
{
public static class Request
{
int bossId;
int damage;
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
if (req.damage < 0)
req.damage = 0; 
((WorldBossFeature)player.getFeature(WorldBossFeature.class)).hurtWorldBoss(req.bossId, req.damage);
WorldBossBO boss = WorldBossMgr.getInstance().getBO(req.bossId);
request.response(boss);
}
}

