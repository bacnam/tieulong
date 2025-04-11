package core.network.client2game.handler.pvp;

import business.global.fight.BossFight;
import business.global.fight.Fight;
import business.global.fight.FightFactory;
import business.global.fight.FightManager;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.features.PlayerRecord;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.ref.RefSkipDungeon;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class DungeonBossSkipBegin
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);

        RefSkipDungeon ref = (RefSkipDungeon) RefSkipDungeon.LevelMap.get(Integer.valueOf(req.level));
        if (ref == null) {
            throw new WSException(ErrorCode.Dungeon_NotFound, "副本不存在");
        }
        if (!req.isRebirth && !((PlayerCurrency) player.getFeature(PlayerCurrency.class)).checkAndConsume(PrizeType.Crystal, ref.Cost, ItemFlow.Dungeon_Skip)) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "元宝不足");
        }
        BossFight fight = FightFactory.createFight(player, req.level);
        FightManager.getInstance().pushFight((Fight) fight);
        PlayerRecord recorder = (PlayerRecord) player.getFeature(PlayerRecord.class);

        recorder.setValue(ConstEnum.DailyRefresh.DungeonRebirth, 0);
        int curTimes = recorder.getValue(ConstEnum.DailyRefresh.DungeonRebirth);

        request.response(new DungeonBossBegin.DungeonBossBeginInfo(new Fight.Begin(fight.getId()), curTimes));
    }

    static class Request {
        int level;
        boolean isRebirth;
    }
}

