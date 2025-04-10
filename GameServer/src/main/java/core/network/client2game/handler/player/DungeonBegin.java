package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.PlayerBase;
import business.player.feature.features.PlayerRecord;
import business.player.feature.pve.DungeonFeature;
import business.player.item.Reward;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefDungeon;
import core.config.refdata.ref.RefReward;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class DungeonBegin
extends PlayerHandler
{
public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
DungeonFeature dungeon = (DungeonFeature)player.getFeature(DungeonFeature.class);

RefDungeon ref = (RefDungeon)RefDataMgr.get(RefDungeon.class, Integer.valueOf(dungeon.getLevel()));
((PlayerBase)player.getFeature(PlayerBase.class)).calOnlineTime();
int onlineSecond = ((PlayerRecord)player.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.OnlineSecond);
Reward reward = new Reward();
if (onlineSecond >= RefDataMgr.getFactor("OnlineLimitSecond", 28800)) {
int num = RefDataMgr.getFactor("OnlineRewardLimit", 5000);
reward = ((RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(ref.Drop))).genLimitReward(num);
reward.add(PrizeType.Gold, ref.Gold * num / 10000);
reward.add(PrizeType.Exp, ref.Exp * num / 10000);
} else {
reward = ((RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(ref.Drop))).genReward();
reward.add(PrizeType.Gold, ref.Gold);
reward.add(PrizeType.Exp, ref.Exp);
} 

dungeon.beginFight(reward);
request.response(reward);
}
}

