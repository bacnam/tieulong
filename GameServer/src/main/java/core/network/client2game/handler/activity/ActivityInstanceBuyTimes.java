package core.network.client2game.handler.activity;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.ActivityInstance;
import business.player.Player;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class ActivityInstanceBuyTimes
extends PlayerHandler
{
private static class Response
{
int leftTimes;
int leftBuyTimes;

private Response(int leftTimes, int leftBuyTimes) {
this.leftTimes = leftTimes;
this.leftBuyTimes = leftBuyTimes;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
ActivityInstance instance = (ActivityInstance)ActivityMgr.getActivity(ActivityInstance.class);

instance.buyTimes(player);
request.response(new Response(instance.getLeftTimes(player), instance.getBuyTimes() - instance.getBuyTimes(player), null));
}
}

