package core.network.client2game.handler.pvp;

import business.global.fight.BossFight;
import business.global.fight.FightFactory;
import business.global.fight.FightManager;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.features.PlayerRecord;
import business.player.feature.pve.DungeonFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefDungeonRebirth;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.Fight;
import java.io.IOException;

public class DungeonBossRebirth extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        DungeonBossRebirth.Request req = new Gson().fromJson(message, DungeonBossRebirth.Request.class);
        PlayerCurrency currency = player.getFeature(PlayerCurrency.class);
        PlayerRecord recorder = player.getFeature(PlayerRecord.class);
        int curTimes = recorder.getValue(DailyRefresh.DungeonRebirth);
        RefDungeonRebirth ref = RefDataMgr.getOrLast(RefDungeonRebirth.class, curTimes + 1);
        if (!currency.checkAndConsume(PrizeType.Crystal, ref.Cost, ItemFlow.Dungeon_Rebirth)) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "元宝不足");
        } else {
            BossFight fight = FightFactory.createFight(player, req.level);
            FightManager.getInstance().pushFight(fight);
            recorder.addValue(DailyRefresh.DungeonRebirth);
            player.getFeature(DungeonFeature.class).setRebirthRef(req.level, ref);
            request.response(new DungeonBossBegin.DungeonBossBeginInfo(new Fight.Begin(fight.getId()), recorder.getValue(DailyRefresh.DungeonRebirth)));
        }
    }

    static class Request {
        int level;
    }
}
