package core.network.client2game.handler.pvp;

import business.global.arena.ArenaConfig;
import business.global.arena.ArenaManager;
import business.global.arena.Competitor;
import business.global.fight.ArenaFight;
import business.global.fight.Fight;
import business.global.fight.FightFactory;
import business.global.fight.FightManager;
import business.player.Player;
import business.player.feature.features.PlayerRecord;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.UnlockType;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.ref.RefUnlockFunction;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.Fight;
import java.io.IOException;

public class ArenaBeginFight
extends PlayerHandler
{
static class Request
{
long targetPid;
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
RefUnlockFunction.checkUnlock(player, UnlockType.Arena);

Request req = (Request)(new Gson()).fromJson(message, Request.class);
if (req.targetPid == player.getPid()) {
throw new WSException(ErrorCode.Arena_WrongTarget, "玩家[%s]不能自攻自受", new Object[] { Long.valueOf(req.targetPid) });
}
ArenaManager manager = ArenaManager.getInstance();
Competitor competitor = manager.getOrCreate(player.getPid());
Competitor opponent = competitor.getOpponent(req.targetPid);
if (opponent == null) {
throw new WSException(ErrorCode.Arena_WrongTarget, "玩家[%s]不在玩家[%s]的挑战列表中", new Object[] { Long.valueOf(player.getPid()), Long.valueOf(req.targetPid) });
}
PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);
int curTimes = recorder.getValue(ConstEnum.DailyRefresh.ArenaChallenge);
if (curTimes >= ArenaConfig.maxChallengeTimes()) {
throw new WSException(ErrorCode.Arena_ChallengeTimesRequired, "玩家[%s]的挑战次数不足", new Object[] { Long.valueOf(player.getPid()) });
}
if (competitor.getFightCD() > 0) {
throw new WSException(ErrorCode.Arena_FightInCD, "玩家[%s]的冷却中,还有[%s]秒", new Object[] { Long.valueOf(player.getPid()), Integer.valueOf(competitor.getFightCD()) });
}
competitor.setFightCD(ArenaConfig.fightCD());

ArenaFight fight = FightFactory.createFight(competitor, opponent);
FightManager.getInstance().pushFight((Fight)fight);
recorder.addValue(ConstEnum.DailyRefresh.ArenaChallenge);

request.response(new Fight.Begin(fight.getId()));
}
}

