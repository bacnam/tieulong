package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.pve.InstanceFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.InstanceType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class InstanceBuyTimes
extends PlayerHandler
{
public static class Request {
InstanceType type;
int times;
}

static class instanceFightInfo {
InstanceType type;
int challengTimes;
int buyTimes;

public instanceFightInfo(InstanceType type, int challengTimes, int buyTimes) {
this.type = type;
this.challengTimes = challengTimes;
this.buyTimes = buyTimes;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
if (req.times == 0) {
req.times = 1;
}

InstanceFeature feature = (InstanceFeature)player.getFeature(InstanceFeature.class);
String challengAndTimes = feature.buyChallengTimes(req.type, req.times);
String[] info = challengAndTimes.split(";");

request.response(new instanceFightInfo(req.type, Integer.valueOf(info[0]).intValue(), Integer.valueOf(info[1]).intValue()));
}
}

