package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.achievement.AchievementFeature;
import business.player.feature.task.TaskActivityFeature;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.AchieveInfo;
import java.io.IOException;
import java.util.List;

public class AchieveList
extends PlayerHandler
{
public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
AchievementFeature achievementContainer = (AchievementFeature)player.getFeature(AchievementFeature.class);
List<AchieveInfo> achieveInfoList = achievementContainer.loadAchieveList();
TaskActivityFeature taskActivityFeature = (TaskActivityFeature)player.getFeature(TaskActivityFeature.class);
taskActivityFeature.pushTaskActiveInfo();
request.response(achieveInfoList);
}
}

