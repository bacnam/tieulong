package core.network.client2game.handler.marry;

import business.player.Player;
import business.player.feature.marry.MarryFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class DivorceDisAgreed extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        DivorceDisAgreed.Request req = new Gson().fromJson(message, DivorceDisAgreed.Request.class);
        MarryFeature feature = player.getFeature(MarryFeature.class);
        feature.disagreedDivorce(req.pid);
        request.response(req.pid);
    }

    public static class Request {
        long pid;
    }
}
