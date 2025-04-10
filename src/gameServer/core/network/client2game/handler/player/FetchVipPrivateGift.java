package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.feature.record.VipRecord;
import business.player.item.Reward;
import com.google.gson.Gson;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefReward;
import core.config.refdata.ref.RefVIP;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.List;

public class FetchVipPrivateGift
extends PlayerHandler
{
private class Request
{
int level;
}

private static class FetchInfo {
Reward reward;
List<Boolean> list;

private FetchInfo(Reward reward, List<Boolean> list) {
this.reward = reward;
this.list = list;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
int reqVipLevel = req.level;
if (reqVipLevel < 0) {
throw new WSException(ErrorCode.InvalidParam, "非法的参数viplevel=%s", new Object[] { Integer.valueOf(reqVipLevel) });
}
RefVIP refVip = (RefVIP)RefDataMgr.get(RefVIP.class, Integer.valueOf(reqVipLevel));
if (refVip == null) {
throw new WSException(ErrorCode.InvalidParam, "找不到vip:%s", new Object[] { Integer.valueOf(reqVipLevel) });
}
if (player.getVipLevel() < reqVipLevel) {
throw new WSException(ErrorCode.NotEnough_VIP, "vip等级不足");
}
int lastFetchTime = ((VipRecord)player.getFeature(VipRecord.class)).getLastFetchPrivateTime(reqVipLevel);
if (lastFetchTime > 0) {
throw new WSException(ErrorCode.Vipinfo_FetchedAlready, "已经领取了%s的vip礼包", new Object[] { Integer.valueOf(reqVipLevel) });
}

Reward reward = ((RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(refVip.PrivateGiftID))).genReward();

((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.VIP_PrivilegeGift);

((VipRecord)player.getFeature(VipRecord.class)).setLastFetchPrivateTime(reqVipLevel);

request.response(new FetchInfo(reward, ((VipRecord)player.getFeature(VipRecord.class)).getFetchGiftStatusList(), null));
}
}

