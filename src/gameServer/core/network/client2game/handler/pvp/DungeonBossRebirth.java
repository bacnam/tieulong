package core.network.client2game.handler.pvp;

import business.global.fight.BossFight;
import business.global.fight.Fight;
import business.global.fight.FightFactory;
import business.global.fight.FightManager;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.features.PlayerRecord;
import business.player.feature.pve.DungeonFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefDungeonRebirth;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.Fight;
import java.io.IOException;

public class DungeonBossRebirth
extends PlayerHandler
{
static class Request
{
int level;
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
PlayerCurrency currency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);

int curTimes = recorder.getValue(ConstEnum.DailyRefresh.DungeonRebirth);
RefDungeonRebirth ref = (RefDungeonRebirth)RefDataMgr.getOrLast(RefDungeonRebirth.class, Integer.valueOf(curTimes + 1));

if (!currency.checkAndConsume(PrizeType.Crystal, ref.Cost, ItemFlow.Dungeon_Rebirth)) {
throw new WSException(ErrorCode.NotEnough_Crystal, "元宝不足");
}

BossFight fight = FightFactory.createFight(player, req.level);
FightManager.getInstance().pushFight((Fight)fight);
recorder.addValue(ConstEnum.DailyRefresh.DungeonRebirth);
((DungeonFeature)player.getFeature(DungeonFeature.class)).setRebirthRef(req.level, ref);
request.response(new DungeonBossBegin.DungeonBossBeginInfo(new Fight.Begin(fight.getId()), recorder.getValue(ConstEnum.DailyRefresh.DungeonRebirth)));
}
}

