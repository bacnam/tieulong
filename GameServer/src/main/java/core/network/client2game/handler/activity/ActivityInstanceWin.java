package core.network.client2game.handler.activity;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.ActivityInstance;
import business.player.Player;
import business.player.item.Reward;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class ActivityInstanceWin
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        ActivityInstance instance = (ActivityInstance) ActivityMgr.getActivity(ActivityInstance.class);
        Reward reward = instance.instanceWin(player);
        request.response(new Response(instance.getLeftTimes(player), reward, null));
    }

    private static class Response {
        int leftTimes;
        Reward rewardItem;

        private Response(int leftTimes, Reward rewardItem) {
            this.leftTimes = leftTimes;
            this.rewardItem = rewardItem;
        }
    }
}

