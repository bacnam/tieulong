package core.network.client2game.handler.activity;

import business.global.activity.ActivityMgr;
import business.player.Player;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class SignInSevenResign extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        SignInSevenResign.Request req = new Gson().fromJson(message, SignInSevenResign.Request.class);
        business.global.activity.detail.SignInSeven openServer = ActivityMgr.getActivity(business.global.activity.detail.SignInSeven.class);
        if (openServer.getStatus() == ActivityStatus.Close) {
            throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", openServer.getType());
        } else {
            request.response(openServer.reSign(player, req.day));
        }
    }

    class Request {
        int day;
    }
}
