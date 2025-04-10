package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.pve.InstanceFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.InstanceType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class InstanceFight
extends PlayerHandler
{
public static class Request {
InstanceType type;
int level;
}

static class instanceFightInfo {
InstanceType type;
int challengTimes;

public instanceFightInfo(InstanceType type, int challengTimes) {
this.type = type;
this.challengTimes = challengTimes;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
int challengTimes = 0;
InstanceFeature feature = (InstanceFeature)player.getFeature(InstanceFeature.class);
challengTimes = feature.getOrCreate().getChallengTimes(req.type.ordinal());
request.response(new instanceFightInfo(req.type, challengTimes));
}
}

