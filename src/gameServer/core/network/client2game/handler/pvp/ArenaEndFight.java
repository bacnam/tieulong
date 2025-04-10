package core.network.client2game.handler.pvp;

import business.global.arena.ArenaManager;
import business.global.fight.ArenaFight;
import business.global.fight.FightManager;
import business.player.Player;
import business.player.feature.achievement.AchievementFeature;
import business.player.item.Reward;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.Achievement;
import com.zhonglian.server.common.enums.FightResult;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.Fight;
import java.io.IOException;

public class ArenaEndFight
extends PlayerHandler
{
static class Response
{
Reward reward;
int rank;

public Response(Integer rank, Reward reward) {
this.reward = reward;
this.rank = rank.intValue();
}
}

private ArenaManager manager = null;

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Fight.End req = (Fight.End)(new Gson()).fromJson(message, Fight.End.class);
if (this.manager == null) {
this.manager = ArenaManager.getInstance();
}
ArenaFight fight = (ArenaFight)FightManager.getInstance().popFight(req.fightId);
if (fight == null) {
throw new WSException(ErrorCode.Arena_FightNotFound, "玩家[%s]提交的战斗[%s]不存在", new Object[] { Long.valueOf(player.getPid()), Integer.valueOf(req.fightId) });
}
if (req.result == FightResult.Win) {
fight.check(req.checks);
}
if (fight.getAtkPid() != player.getPid()) {
throw new WSException(ErrorCode.Arena_WrongTarget, "玩家[%s]不能提交战斗[%s]", new Object[] { Long.valueOf(player.getPid()), Integer.valueOf(req.fightId) });
}
Reward reward = (Reward)fight.settle(req.result);

((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.ArenaFightTimes);
((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.ArenaFightTimes_M1);
((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.ArenaFightTimes_M2);

int rank = this.manager.getOrCreate(player.getPid()).getRank();
request.response(new Response(Integer.valueOf(rank), reward));
}
}

