package core.network.client2game.handler.marry;

import business.player.Player;
import business.player.feature.marry.MarryFeature;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class MarrySign extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        MarryFeature feature = player.getFeature(MarryFeature.class);
        feature.signIn();
        request.response(new MarrySign.Response(feature.getSignin(), feature.bo.getIsSign()));
    }

    private static class Response {
        int mySign;
        boolean isSign;

        public Response(int mySign, boolean isSign) {
            this.mySign = mySign;
            this.isSign = isSign;
        }
    }
}
