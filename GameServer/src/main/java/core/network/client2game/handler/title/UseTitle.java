package core.network.client2game.handler.title;

import business.player.Player;
import business.player.feature.player.NewTitleFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class UseTitle
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        request.response(((NewTitleFeature) player.getFeature(NewTitleFeature.class)).useTitle(req.titleId));
    }

    private static class Request {
        int titleId;
    }
}

