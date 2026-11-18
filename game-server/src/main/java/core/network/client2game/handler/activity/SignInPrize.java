package core.network.client2game.handler.activity;

import business.global.activity.ActivityMgr;
import business.player.Player;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class SignInPrize extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        business.global.activity.detail.SignInPrize SignInPrize = ActivityMgr.getActivity(business.global.activity.detail.SignInPrize.class);
        if (SignInPrize.getStatus() == ActivityStatus.Close) {
            throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", SignInPrize.getType());
        } else {
            request.response(SignInPrize.doSignIn(player));
        }
    }
}
