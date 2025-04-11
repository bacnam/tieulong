package core.network.client2game.handler.activity;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.SignInOpenServer;
import business.player.Player;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class SignInOpenServerInfo
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        SignInOpenServer openServer = (SignInOpenServer) ActivityMgr.getActivity(SignInOpenServer.class);
        request.response(openServer.dailySignProto(player));
    }
}

