package core.network.game2world.handler.activity;

import business.global.activity.Activity;
import business.global.activity.ActivityMgr;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.game2world.GRequestHandler;
import java.util.List;
import java.util.stream.Collectors;
import proto.gameworld.ActivityInfo;

public class ActivitySummary extends GRequestHandler {
    @Override
    public void handle(WebSocketRequest request, String data) throws WSException {
        List<Activity> activities = ActivityMgr.getInstance().getCurActivities();
        List<ActivityInfo> localActivitys = activities.stream().map(x -> x.activitySummary()).collect(Collectors.toList());
        request.response(localActivitys);
    }
}
