package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.achievement.AchievementFeature;
import com.zhonglian.server.common.enums.Achievement;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class Share
extends PlayerHandler
{
public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.SharePhone);
request.response();
}
}

