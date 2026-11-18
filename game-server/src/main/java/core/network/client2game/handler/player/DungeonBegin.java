package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.PlayerBase;
import business.player.feature.features.PlayerRecord;
import business.player.feature.pve.DungeonFeature;
import business.player.item.Reward;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefDungeon;
import core.config.refdata.ref.RefReward;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class DungeonBegin extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        DungeonFeature dungeon = player.getFeature(DungeonFeature.class);
        RefDungeon ref = RefDataMgr.get(RefDungeon.class, dungeon.getLevel());
        player.getFeature(PlayerBase.class).calOnlineTime();
        int onlineSecond = player.getFeature(PlayerRecord.class).getValue(DailyRefresh.OnlineSecond);
        new Reward();
        Reward reward;
        if (onlineSecond >= RefDataMgr.getFactor("OnlineLimitSecond", 28800)) {
            int num = RefDataMgr.getFactor("OnlineRewardLimit", 5000);
            reward = RefDataMgr.get(RefReward.class, ref.Drop).genLimitReward(num);
            reward.add(PrizeType.Gold, ref.Gold * num / 10000);
            reward.add(PrizeType.Exp, ref.Exp * num / 10000);
        } else {
            reward = RefDataMgr.get(RefReward.class, ref.Drop).genReward();
            reward.add(PrizeType.Gold, ref.Gold);
            reward.add(PrizeType.Exp, ref.Exp);
        }

        dungeon.beginFight(reward);
        request.response(reward);
    }
}
