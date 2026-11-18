package core.network.game2world.handler.player;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import com.zhonglian.server.websocket.handler.response.ResponseHandler;
import com.zhonglian.server.websocket.handler.response.WebSocketResponse;
import core.network.game2world.GRequestHandler;
import core.network.game2world.GameSessionMgr;
import java.io.IOException;
import proto.gameworld.TeamInfo;

public class GetTeam extends GRequestHandler {
    @Override
    public void handle(final WebSocketRequest request, String data) throws WSException {
        GetTeam.Request req = new Gson().fromJson(data, GetTeam.Request.class);
        GameSessionMgr.getInstance().request(req.serverId, "player.GetTeam", req, new ResponseHandler() {
            @Override
            public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException {
                TeamInfo info = new Gson().fromJson(body, (new TypeToken<TeamInfo>() {}).getType());
                request.response(info);
            }

            @Override
            public void handleError(WebSocketResponse ssresponse, short statusCode, String message) {
                request.response();
            }
        });
    }

    private static class Request {
        long targetPid;
        int serverId;
    }
}
