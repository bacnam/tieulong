package core.network.client2game.handler.activity;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.LevelReward;
import business.player.Player;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class LevelRewardReceiveHandler extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        LevelRewardReceiveHandler.Request req = new Gson().fromJson(message, LevelRewardReceiveHandler.Request.class);
        LevelReward accumRecharge = ActivityMgr.getActivity(LevelReward.class);
        if (accumRecharge.getStatus() == ActivityStatus.Close) {
            throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", accumRecharge.getType());
        } else {
            int awardId = req.awardId;
            if (awardId < 0) {
                throw new WSException(ErrorCode.InvalidParam, "非法的参数awardId=%s", awardId);
            } else {
                request.response(accumRecharge.pickReward(player, req.awardId));
            }
        }
    }

    class Request {
        int awardId;
    }
}
