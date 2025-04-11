package core.network.client2game.handler.marry;

import business.player.Player;
import business.player.feature.marry.MarryFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class MarryAgreed
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        MarryFeature feature = (MarryFeature) player.getFeature(MarryFeature.class);
        feature.agreedMarry(req.pid);
        request.response(feature.getLoveInfo());
    }

    public static class Request {
        long pid;
    }
}

