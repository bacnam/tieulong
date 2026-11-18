package core.network.game2world.handler.wforward.client;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.WorldRecharge;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.game2world.LRequestHandler;

public class Test extends LRequestHandler {
    @Override
    public void handle(long player, WebSocketRequest request, String data) throws WSException {
        ActivityMgr.getActivity(WorldRecharge.class).onEnd();
        request.response("{\"msg\":\"hello " + player + ", i am world\"}");
    }
}
