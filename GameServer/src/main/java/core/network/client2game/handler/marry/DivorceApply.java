package core.network.client2game.handler.marry;

import business.player.Player;
import business.player.feature.marry.MarryFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.database.game.bo.MarryDivorceApplyBO;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class DivorceApply
extends PlayerHandler
{
public static class Request
{
long pid;
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
MarryFeature feature = (MarryFeature)player.getFeature(MarryFeature.class);
MarryDivorceApplyBO bo = feature.marryDivorceApply(req.pid);
request.response(bo);
}
}

