package core.network.client2game.handler.pvp;

import business.global.arena.ArenaConfig;
import business.global.arena.ArenaManager;
import business.global.arena.Competitor;
import business.global.fight.ArenaFight;
import business.global.fight.FightFactory;
import business.global.fight.FightManager;
import business.player.Player;
import business.player.feature.features.PlayerRecord;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.UnlockType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.ref.RefUnlockFunction;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.Fight;
import java.io.IOException;

public class ArenaBeginFight extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        RefUnlockFunction.checkUnlock(player, UnlockType.Arena);
        ArenaBeginFight.Request req = new Gson().fromJson(message, ArenaBeginFight.Request.class);
        if (req.targetPid == player.getPid()) {
            throw new WSException(ErrorCode.Arena_WrongTarget, "玩家[%s]不能自攻自受", req.targetPid);
        } else {
            ArenaManager manager = ArenaManager.getInstance();
            Competitor competitor = manager.getOrCreate(player.getPid());
            Competitor opponent = competitor.getOpponent(req.targetPid);
            if (opponent == null) {
                throw new WSException(ErrorCode.Arena_WrongTarget, "玩家[%s]不在玩家[%s]的挑战列表中", player.getPid(), req.targetPid);
            } else {
                PlayerRecord recorder = player.getFeature(PlayerRecord.class);
                int curTimes = recorder.getValue(DailyRefresh.ArenaChallenge);
                if (curTimes >= ArenaConfig.maxChallengeTimes()) {
                    throw new WSException(ErrorCode.Arena_ChallengeTimesRequired, "玩家[%s]的挑战次数不足", player.getPid());
                } else if (competitor.getFightCD() > 0) {
                    throw new WSException(ErrorCode.Arena_FightInCD, "玩家[%s]的冷却中,还有[%s]秒", player.getPid(), competitor.getFightCD());
                } else {
                    competitor.setFightCD(ArenaConfig.fightCD());
                    ArenaFight fight = FightFactory.createFight(competitor, opponent);
                    FightManager.getInstance().pushFight(fight);
                    recorder.addValue(DailyRefresh.ArenaChallenge);
                    request.response(new Fight.Begin(fight.getId()));
                }
            }
        }
    }

    static class Request {
        long targetPid;
    }
}
