package core.network.client2game.handler.player;

import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerItem;
import business.player.feature.pvp.WorshipFeature;
import business.player.item.Reward;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefReward;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class RankWorship
extends PlayerHandler
{
private static class Request
{
RankType type;
long pid;
}

private static class Response
{
int times;
Reward reward;

private Response(int times, Reward reward) {
this.times = times;
this.reward = reward;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
WorshipFeature feature = (WorshipFeature)player.getFeature(WorshipFeature.class);
if (feature.getTimes(req.type.ordinal()) > 0) {
throw new WSException(ErrorCode.Already_Worship, "已膜拜");
}
feature.addTimes(req.type.ordinal());
Player target = PlayerMgr.getInstance().getPlayer(req.pid);
((WorshipFeature)target.getFeature(WorshipFeature.class)).beWorshiped(req.type.ordinal());
Reward reward = ((RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(RefDataMgr.getFactor("WorshipReward", 10001)))).genReward();
((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.Worship);
request.response(new Response(feature.getTimes(req.type.ordinal()), reward, null));
}
}

