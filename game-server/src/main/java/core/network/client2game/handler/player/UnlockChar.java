package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.character.CharFeature;
import com.google.gson.Gson;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class UnlockChar extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        UnlockChar.Request req = new Gson().fromJson(message, UnlockChar.Request.class);
        req.sid = player.getFeature(CharFeature.class).unlockChar(req.index, ItemFlow.UnlockChar);
        request.response(req);
    }

    private static class Request {
        int index;
        long sid;
    }
}
