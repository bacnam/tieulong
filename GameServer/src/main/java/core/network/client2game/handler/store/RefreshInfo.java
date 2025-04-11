package core.network.client2game.handler.store;

import business.player.Player;
import business.player.feature.store.PlayerStore;
import business.player.feature.store.StoreFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.StoreType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class RefreshInfo
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        PlayerStore store = ((StoreFeature) player.getFeature(StoreFeature.class)).getOrCreate(req.storeType);
        request.response(store.refreshInfo());
    }

    private static class Request {
        StoreType storeType;
    }
}

