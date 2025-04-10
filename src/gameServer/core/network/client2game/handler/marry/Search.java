package core.network.client2game.handler.marry;

import business.player.Player;
import business.player.feature.PlayerBase;
import business.player.feature.marry.MarryFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Search
extends PlayerHandler
{
public static class Request
{
String name;
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
List<Player> search = ((MarryFeature)player.getFeature(MarryFeature.class)).search(req.name);
List<Player.Summary> summary = new ArrayList<>();
search.stream().forEach(x -> paramList.add(((PlayerBase)x.getFeature(PlayerBase.class)).summary()));

request.response(summary);
}
}

