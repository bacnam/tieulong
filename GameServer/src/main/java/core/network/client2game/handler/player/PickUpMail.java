package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.features.MailFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class PickUpMail
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        long mailId = req.sid;
        ((MailFeature) player.getFeature(MailFeature.class)).pickUpMail(mailId, request);
    }

    private static class Request {
        long sid;
    }
}

