package core.network.client2game.handler.marry;

import business.player.Player;
import business.player.feature.PlayerBase;
import business.player.feature.marry.MarryFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Search extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Search.Request req = new Gson().fromJson(message, Search.Request.class);
        List<Player> search = player.getFeature(MarryFeature.class).search(req.name);
        List<core.network.proto.Player.Summary> summary = new ArrayList<>();
        search.stream().forEach(x -> summary.add(x.getFeature(PlayerBase.class).summary()));
        request.response(summary);
    }

    public static class Request {
        String name;
    }
}
