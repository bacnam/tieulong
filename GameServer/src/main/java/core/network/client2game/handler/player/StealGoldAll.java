package core.network.client2game.handler.player;

import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerCurrency;
import business.player.feature.pvp.StealGoldFeature;
import business.player.item.Reward;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefCrystalPrice;
import core.config.refdata.ref.RefVIP;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class StealGoldAll
extends PlayerHandler
{
private static class StealGoldInfo
{
int nowTimes;
Reward reward;

private StealGoldInfo(int nowTimes, Reward reward) {
this.nowTimes = nowTimes;
this.reward = reward;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
StealGoldFeature feature = (StealGoldFeature)player.getFeature(StealGoldFeature.class);
if (!feature.checkTimes()) {
throw new WSException(ErrorCode.StealGold_NotEnough, "探金手次数不足");
}

PlayerCurrency currency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
int nowTime = feature.getTimes();
int maxTimes = ((RefVIP)RefDataMgr.get(RefVIP.class, Integer.valueOf(player.getVipLevel()))).StealGold;

int cost = 0;
long gain = 0L;
for (int i = nowTime; i < maxTimes; i++) {
cost += (RefCrystalPrice.getPrize(i)).StealGoldPrice;
gain += (RefCrystalPrice.getPrize(i)).StealGoldGain;
} 

if (!currency.check(PrizeType.Crystal, cost)) {
throw new WSException(ErrorCode.NotEnough_Crystal, "探金手需要钻石%s", new Object[] { Integer.valueOf(cost) });
}
currency.consume(PrizeType.Crystal, cost, ItemFlow.StealGold);
long get = gain * (10000 + RefDataMgr.getFactor("StealGoldCrit", 5000)) / 10000L;

feature.addTimes(maxTimes - nowTime);

currency.gain(PrizeType.Gold, (int)get, ItemFlow.StealGold);

for (int j = 0; j < maxTimes - nowTime; j++) {

Player target = PlayerMgr.getInstance().getPlayer(((Long)feature.getList().get(j % 4)).longValue());
((StealGoldFeature)target.getFeature(StealGoldFeature.class)).addNews(player.getPid(), (int)(get / maxTimes - nowTime));
} 

Reward reward = new Reward();
reward.add(PrizeType.Gold, (int)get);
request.response(new StealGoldInfo(feature.getTimes(), reward, null));
}
}

