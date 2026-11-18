package core.network.client2game.handler.marry;

import business.player.feature.PlayerBase;
import business.player.feature.marry.MarryFeature;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MarryList extends PlayerHandler {
    @Override
    public void handle(business.player.Player player, WebSocketRequest request, String message) throws WSException, IOException {
        List<Player.Summary> summary = new ArrayList<>();
        player.getFeature(MarryFeature.class).getMarryList().stream().forEach(x -> summary.add(x.getFeature(PlayerBase.class).summary()));
        request.response(summary);
    }
}
