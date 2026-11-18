package core.network.client2game.handler.activity;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.Lottery;
import business.player.Player;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ConstEnum.LotteryType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class LotteryLoadNumber extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        LotteryLoadNumber.Request req = new Gson().fromJson(message, LotteryLoadNumber.Request.class);
        Lottery Lottery = ActivityMgr.getActivity(Lottery.class);
        request.response(Lottery.loadNumber(player, req.type));
    }

    class Request {
        LotteryType type;
    }
}
