package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.player.LingBaoFeature;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class LingBaoUp
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        LingBaoFeature feature = (LingBaoFeature) player.getFeature(LingBaoFeature.class);
        int exp = feature.levelUp();
        request.response(new Response(feature.getLevel(), feature.getExp(), exp, null));
    }

    private static class Response {
        int level;
        int exp;
        int gain;

        private Response(int level, int exp, int gain) {
            this.level = level;
            this.exp = exp;
            this.gain = gain;
        }
    }
}

