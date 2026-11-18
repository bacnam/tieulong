package core.network.client2game.handler.activity;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.DrawPrize;
import business.player.Player;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class DrawPrizePick extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        DrawPrizePick.Request req = new Gson().fromJson(message, DrawPrizePick.Request.class);
        DrawPrize accumRecharge = ActivityMgr.getActivity(DrawPrize.class);
        if (accumRecharge.getStatus() == ActivityStatus.Close) {
            throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", accumRecharge.getType());
        } else {
            request.response(accumRecharge.doReceivePrize(player, req.awardId));
        }
    }

    class Request {
        int awardId;
    }
}
