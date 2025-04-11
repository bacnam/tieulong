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
import proto.gameworld.ActivityInfo;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ActivitySummary
        extends PlayerHandler {
    public void handle(final Player player, WebSocketRequest request, String message) throws WSException, IOException {
        List<Activity> activities = ActivityMgr.getInstance().getCurActivities();

        List<ActivityInfo> localActivitys = (List<ActivityInfo>) activities.stream().map(x -> x.activitySummary(paramPlayer))

                .collect(Collectors.toList());

        if (WorldConnector.getInstance().isConnected()) {
            WorldConnector.request("activity.ActivitySummary", "", new ResponseHandler() {
                public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException {
                    List<ActivityInfo> list = (List<ActivityInfo>) (new Gson()).fromJson(body, (new TypeToken<List<ActivityInfo>>() {
                    }
                    ).getType());
                    player.pushProto("worldactivitysummary", list);
                }

                public void handleError(WebSocketResponse ssresponse, short statusCode, String message) {
                }
            });
        }
        request.response(localActivitys);
    }
}

