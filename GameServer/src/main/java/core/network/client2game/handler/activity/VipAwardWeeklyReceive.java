package core.network.client2game.handler.activity;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.VipAward;
import business.player.Player;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class VipAwardWeeklyReceive
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        int awardId = req.awardId;
        if (awardId < 0) {
            throw new WSException(ErrorCode.InvalidParam, "非法的参数awardId=%s", new Object[]{Integer.valueOf(awardId)});
        }
        VipAward vipAward = (VipAward) ActivityMgr.getActivity(VipAward.class);
        if (vipAward.getStatus() == ActivityStatus.Close) {
            throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", new Object[]{vipAward.getType()});
        }
        request.response(vipAward.doWeeklyReceive(player, awardId));
    }

    class Request {
        int awardId;
    }
}

