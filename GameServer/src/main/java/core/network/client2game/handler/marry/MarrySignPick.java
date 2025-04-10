package core.network.client2game.handler.marry;

import business.player.Player;
import business.player.feature.marry.MarryFeature;
import business.player.item.Reward;
import com.google.gson.Gson;
import com.zhonglian.server.common.utils.StringUtils;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.List;

public class MarrySignPick
extends PlayerHandler
{
public static class Request
{
int signId;
}

private static class Response
{
List<Integer> alreadyPick;
Reward reward;

public Response(List<Integer> alreadyPick, Reward reward) {
this.alreadyPick = alreadyPick;
this.reward = reward;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
MarryFeature feature = (MarryFeature)player.getFeature(MarryFeature.class);
Reward reward = feature.pickSignInReward(req.signId);
List<Integer> alreadyPick = StringUtils.string2Integer(feature.bo.getSignReward());
request.response(new Response(alreadyPick, reward));
}
}

