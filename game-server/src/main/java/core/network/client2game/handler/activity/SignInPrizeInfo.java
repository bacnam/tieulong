package core.network.client2game.handler.activity;

import business.global.activity.ActivityMgr;
import business.player.Player;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class SignInPrizeInfo extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        business.global.activity.detail.SignInPrize SignInPrize = ActivityMgr.getActivity(business.global.activity.detail.SignInPrize.class);
        request.response(SignInPrize.dailySignProto(player));
    }
}
