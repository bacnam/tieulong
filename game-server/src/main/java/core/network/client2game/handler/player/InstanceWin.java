package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.achievement.AchievementFeature;
import business.player.feature.pve.InstanceFeature;
import business.player.item.Reward;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.InstanceType;
import com.zhonglian.server.common.enums.Achievement.AchievementType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class InstanceWin extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        InstanceWin.Request req = new Gson().fromJson(message, InstanceWin.Request.class);
        InstanceWin.WinReward winReward = null;
        Reward reward = null;
        int level = 0;
        int challengTims = 0;
        InstanceFeature feature = player.getFeature(InstanceFeature.class);
        feature.fightInstance(req.level, req.type);
        reward = feature.getReward(req.level, req.sweep, req.type);
        level = feature.getOrCreate().getInstanceMaxLevel(req.type.ordinal());
        challengTims = feature.getOrCreate().getChallengTimes(req.type.ordinal());
        switch (req.type) {
            case EquipInstance:
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.EquipInstance);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.EquipInstance_M1);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.EquipInstance_M2);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.EquipInstance_M3);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.EquipInstance_M4);
                break;
            case GemInstance:
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.GemInstance);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.GemInstance_M1);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.GemInstance_M2);
                break;
            case MeridianInstance:
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.MeridianInstance);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.MeridianInstance_M1);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.MeridianInstance_M2);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.MeridianInstance_M3);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.MeridianInstance_M4);
        }

        winReward = new InstanceWin.WinReward(reward, req.type, level, challengTims);
        request.response(winReward);
    }

    public static class Request {
        InstanceType type;
        int level;
        boolean sweep;
    }

    public static class WinReward {
        Reward reward;
        InstanceType type;
        int instanceMaxLevel;
        int challengTimes;

        public WinReward(Reward reward, InstanceType type, int instanceMaxLevel, int challengTimes) {
            this.reward = reward;
            this.type = type;
            this.instanceMaxLevel = instanceMaxLevel;
            this.challengTimes = challengTimes;
        }
    }
}
