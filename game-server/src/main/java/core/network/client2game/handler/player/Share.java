package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.achievement.AchievementFeature;
import com.zhonglian.server.common.enums.Achievement.AchievementType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class Share extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        player.getFeature(AchievementFeature.class).updateInc(AchievementType.SharePhone);
        request.response();
    }
}
