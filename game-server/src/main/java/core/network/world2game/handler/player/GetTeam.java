package core.network.world2game.handler.player;

import business.player.Player;
import business.player.PlayerMgr;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.proto.TeamInfo;
import core.network.world2game.handler.WBaseHandler;

public class GetTeam extends WBaseHandler {
    @Override
    public void handle(WebSocketRequest request, String message) throws WSException {
        GetTeam.Request req = new Gson().fromJson(message, GetTeam.Request.class);
        Player target = PlayerMgr.getInstance().getPlayer(req.targetPid);
        if (target == null) {
            request.response();
        } else {
            request.response(new TeamInfo(target));
        }
    }

    private static class Request {
        long targetPid;
    }
}
