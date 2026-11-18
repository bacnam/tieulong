package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.achievement.AchievementFeature;
import business.player.item.Reward;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefAchievement;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class AchievePickUpPrize extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        AchievePickUpPrize.Request req = new Gson().fromJson(message, AchievePickUpPrize.Request.class);
        int achieveID = req.achieveId;
        RefAchievement ref = RefDataMgr.get(RefAchievement.class, achieveID);
        AchievementFeature achievementContainer = player.getFeature(AchievementFeature.class);
        Reward reward = achievementContainer.cmd_pickUpPrize(achieveID, req.achieveCount);
        request.response(reward);
    }

    public static class Request {
        int achieveId;
        int achieveCount;
    }
}
