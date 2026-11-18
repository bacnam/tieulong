package core.network.client2game.handler.pvp;

import business.global.fight.BossFight;
import business.global.fight.FightFactory;
import business.global.fight.FightManager;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.features.PlayerRecord;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.ref.RefSkipDungeon;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.Fight;
import java.io.IOException;

public class DungeonBossSkipBegin extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        DungeonBossSkipBegin.Request req = new Gson().fromJson(message, DungeonBossSkipBegin.Request.class);
        RefSkipDungeon ref = RefSkipDungeon.LevelMap.get(req.level);
        if (ref == null) {
            throw new WSException(ErrorCode.Dungeon_NotFound, "副本不存在");
        } else if (!req.isRebirth && !player.getFeature(PlayerCurrency.class).checkAndConsume(PrizeType.Crystal, ref.Cost, ItemFlow.Dungeon_Skip)) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "元宝不足");
        } else {
            BossFight fight = FightFactory.createFight(player, req.level);
            FightManager.getInstance().pushFight(fight);
            PlayerRecord recorder = player.getFeature(PlayerRecord.class);
            recorder.setValue(DailyRefresh.DungeonRebirth, 0);
            int curTimes = recorder.getValue(DailyRefresh.DungeonRebirth);
            request.response(new DungeonBossBegin.DungeonBossBeginInfo(new Fight.Begin(fight.getId()), curTimes));
        }
    }

    static class Request {
        int level;
        boolean isRebirth;
    }
}
