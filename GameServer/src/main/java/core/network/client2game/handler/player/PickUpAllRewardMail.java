package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.features.MailFeature;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class PickUpAllRewardMail
extends PlayerHandler
{
public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
((MailFeature)player.getFeature(MailFeature.class)).pickUpAllReward(request);
}
}

