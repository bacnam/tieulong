package core.network.client2game.handler.pvp;

import business.global.arena.ArenaManager;
import business.global.fight.ArenaFight;
import business.global.fight.FightManager;
import business.player.Player;
import business.player.feature.achievement.AchievementFeature;
import business.player.item.Reward;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.FightResult;
import com.zhonglian.server.common.enums.Achievement.AchievementType;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.Fight;
import java.io.IOException;

public class ArenaEndFight extends PlayerHandler {
    private ArenaManager manager = null;

    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Fight.End req = new Gson().fromJson(message, Fight.End.class);
        if (this.manager == null) {
            this.manager = ArenaManager.getInstance();
        }

        ArenaFight fight = (ArenaFight)FightManager.getInstance().popFight(req.fightId);
        if (fight == null) {
            throw new WSException(ErrorCode.Arena_FightNotFound, "玩家[%s]提交的战斗[%s]不存在", player.getPid(), req.fightId);
        } else {
            if (req.result == FightResult.Win) {
                fight.check(req.checks);
            }

            if (fight.getAtkPid() != player.getPid()) {
                throw new WSException(ErrorCode.Arena_WrongTarget, "玩家[%s]不能提交战斗[%s]", player.getPid(), req.fightId);
            } else {
                Reward reward = fight.settle(req.result);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.ArenaFightTimes);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.ArenaFightTimes_M1);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.ArenaFightTimes_M2);
                int rank = this.manager.getOrCreate(player.getPid()).getRank();
                request.response(new ArenaEndFight.Response(rank, reward));
            }
        }
    }

    static class Response {
        Reward reward;
        int rank;

        public Response(Integer rank, Reward reward) {
            this.reward = reward;
            this.rank = rank;
        }
    }
}
