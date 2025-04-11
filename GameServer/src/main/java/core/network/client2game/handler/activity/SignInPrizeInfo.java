package core.network.client2game.handler.activity;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.SignInPrize;
import business.player.Player;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class SignInPrizeInfo
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        SignInPrize SignInPrize = (SignInPrize) ActivityMgr.getActivity(SignInPrize.class);
        request.response(SignInPrize.dailySignProto(player));
    }
}

