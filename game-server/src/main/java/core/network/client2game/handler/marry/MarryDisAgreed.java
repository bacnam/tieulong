package core.network.client2game.handler.marry;

import business.player.Player;
import business.player.feature.marry.MarryFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class MarryDisAgreed extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        MarryDisAgreed.Request req = new Gson().fromJson(message, MarryDisAgreed.Request.class);
        MarryFeature feature = player.getFeature(MarryFeature.class);
        feature.disagreedMarry(req.pid);
        request.response(req.pid);
    }

    public static class Request {
        long pid;
    }
}
