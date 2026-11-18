package core.network.client2game.handler.title;

import business.player.Player;
import business.player.feature.player.NewTitleFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class UseTitle extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        UseTitle.Request req = new Gson().fromJson(message, UseTitle.Request.class);
        request.response(player.getFeature(NewTitleFeature.class).useTitle(req.titleId));
    }

    private static class Request {
        int titleId;
    }
}
