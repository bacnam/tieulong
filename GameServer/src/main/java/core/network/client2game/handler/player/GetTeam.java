package core.network.client2game.handler.player;

import business.player.Player;
import business.player.PlayerMgr;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import com.zhonglian.server.websocket.handler.response.ResponseHandler;
import com.zhonglian.server.websocket.handler.response.WebSocketResponse;
import core.network.client2game.handler.PlayerHandler;
import core.network.game2world.WorldConnector;
import core.network.proto.TeamInfo;
import java.io.IOException;

public class GetTeam
extends PlayerHandler
{
private static class Request
{
long targetPid;
int serverId;
}

public void handle(Player player, final WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
Player target = PlayerMgr.getInstance().getPlayer(req.targetPid);
if (target == null && req.serverId != 0 && WorldConnector.getInstance().isConnected()) {
WorldConnector.request("player.GetTeam", req, new ResponseHandler()
{
public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException {
TeamInfo info = (TeamInfo)(new Gson()).fromJson(body, (new TypeToken<TeamInfo>() {  }
).getType());
request.response(info);
}

public void handleError(WebSocketResponse ssresponse, short statusCode, String message) {
request.response();
}
});
} else {
request.response(new TeamInfo(target));
} 
}
}

