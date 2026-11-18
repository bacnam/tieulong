package core.network.client2game.handler.activity;

import business.global.activity.Activity;
import business.global.activity.ActivityMgr;
import business.player.Player;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import com.zhonglian.server.websocket.handler.response.ResponseHandler;
import com.zhonglian.server.websocket.handler.response.WebSocketResponse;
import core.network.client2game.handler.PlayerHandler;
import core.network.game2world.WorldConnector;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import proto.gameworld.ActivityInfo;

public class ActivitySummary extends PlayerHandler {
    @Override
    public void handle(final Player player, WebSocketRequest request, String message) throws WSException, IOException {
        List<Activity> activities = ActivityMgr.getInstance().getCurActivities();
        List<ActivityInfo> localActivitys = activities.stream().map(x -> x.activitySummary(player)).collect(Collectors.toList());
        if (WorldConnector.getInstance().isConnected()) {
            WorldConnector.request("activity.ActivitySummary", "", new ResponseHandler() {
                @Override
                public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException {
                    List<ActivityInfo> list = new Gson().fromJson(body, (new TypeToken<List<ActivityInfo>>() {}).getType());
                    player.pushProto("worldactivitysummary", list);
                }

                @Override
                public void handleError(WebSocketResponse ssresponse, short statusCode, String messagex) {
                }
            });
        }

        request.response(localActivitys);
    }
}
