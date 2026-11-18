package core.network.client2game.handler.pvp;

import business.global.fight.DroiyanFight;
import business.global.fight.FightManager;
import business.player.Player;
import business.player.feature.achievement.AchievementFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.FightResult;
import com.zhonglian.server.common.enums.Achievement.AchievementType;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.database.game.bo.DroiyanRecordBO;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.Fight;
import java.io.IOException;

public class DroiyanEndFight extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Fight.End fightend = new Gson().fromJson(message, Fight.End.class);
        DroiyanFight fight = (DroiyanFight)FightManager.getInstance().popFight(fightend.fightId);
        if (fight == null) {
            throw new WSException(ErrorCode.Arena_FightNotFound, "玩家[%s]提交的战斗[%s]不存在", player.getPid(), fightend.fightId);
        } else {
            if (fightend.result == FightResult.Win) {
                fight.check(fightend.checks);
            }

            if (fight.getAtkPid() != player.getPid()) {
                throw new WSException(ErrorCode.Arena_WrongTarget, "玩家[%s]不能提交战斗[%s]", player.getPid(), fightend.fightId);
            } else {
                DroiyanRecordBO reward = fight.settle(fightend.result);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.DroiyanFightTimes);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.DroiyanFightTimes_M1);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.DroiyanFightTimes_M2);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.DroiyanFightTimes_M3);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.DroiyanFightTimes_M4);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.DroiyanAttack);
                request.response(new DroiyanEndFight.Response(reward, fight.getAtkRed()));
            }
        }
    }

    static class Response {
        int point;
        int gold;
        int exp;
        int rob;
        int treasure;
        int red;

        public Response(DroiyanRecordBO reward, int red) {
            this.point = reward.getPoint();
            this.gold = reward.getGold();
            this.exp = reward.getExp();
            this.rob = reward.getRob();
            this.treasure = reward.getTreasure();
            this.red = red;
        }
    }
}
