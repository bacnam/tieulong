package core.network.client2game.handler.pvp;

import business.global.fight.BossFight;
import business.global.fight.Fight;
import business.global.fight.FightFactory;
import business.global.fight.FightManager;
import business.player.Player;
import business.player.feature.features.PlayerRecord;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.Fight;
import java.io.IOException;

public class DungeonBossBegin
extends PlayerHandler
{
public static class DungeonBossBeginInfo
{
Fight.Begin fight;
int rebirthTime;

public DungeonBossBeginInfo(Fight.Begin fight, int rebirthTime) {
this.fight = fight;
this.rebirthTime = rebirthTime;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
BossFight fight = FightFactory.createFight(player, 0);
FightManager.getInstance().pushFight((Fight)fight);

PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);
recorder.setValue(ConstEnum.DailyRefresh.DungeonRebirth, 0);

int curTimes = recorder.getValue(ConstEnum.DailyRefresh.DungeonRebirth);

request.response(new DungeonBossBeginInfo(new Fight.Begin(fight.getId()), curTimes));
}
}

