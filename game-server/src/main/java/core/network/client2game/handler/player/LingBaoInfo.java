package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.player.LingBaoFeature;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class LingBaoInfo extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        LingBaoFeature feature = player.getFeature(LingBaoFeature.class);
        request.response(new LingBaoInfo.Response(feature.getLevel(), feature.getExp()));
    }

    private static class Response {
        int level;
        int exp;

        private Response(int level, int exp) {
            this.level = level;
            this.exp = exp;
        }
    }
}
