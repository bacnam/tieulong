package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.features.PlayerRecord;
import business.player.feature.pve.InstanceFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.InstanceType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class InstanceCheck
extends PlayerHandler
{
public static class Request {
InstanceType type;
}

public static class InstanceInfo {
int maxLevel;
int challengTimes;
int buyTimes;
InstanceType type;

public InstanceInfo(int maxLevel, int challengTimes, int buyTimes, InstanceType type) {
this.maxLevel = maxLevel;
this.challengTimes = challengTimes;
this.buyTimes = buyTimes;
this.type = type;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
int level = 0;
int challengTimes = 0;
InstanceFeature feature = (InstanceFeature)player.getFeature(InstanceFeature.class);
level = feature.getOrCreate().getInstanceMaxLevel(req.type.ordinal());
challengTimes = feature.getOrCreate().getChallengTimes(req.type.ordinal());

ConstEnum.DailyRefresh dailyType = null;
PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);
int times = 0;
switch (req.type) {
case null:
dailyType = ConstEnum.DailyRefresh.EquipInstanceBuyTimes;
times = recorder.getValue(dailyType);
break;
case GemInstance:
dailyType = ConstEnum.DailyRefresh.GemInstanceBuyTimes;
times = recorder.getValue(dailyType);
break;
case MeridianInstance:
dailyType = ConstEnum.DailyRefresh.MeridianInstanceBuyTimes;
times = recorder.getValue(dailyType);
break;
} 

request.response(new InstanceInfo(level, challengTimes, times, req.type));
}
}

