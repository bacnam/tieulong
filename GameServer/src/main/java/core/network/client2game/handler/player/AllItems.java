package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.features.ItemFeature;
import business.player.item.UniformItem;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;
import java.util.List;

public class AllItems
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        List<UniformItem> items = ((ItemFeature) player.getFeature(ItemFeature.class)).getAllItems();
        request.response(items);
    }
}

