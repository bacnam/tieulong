package core.network.client2game.handler.activity;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.Lottery;
import business.player.Player;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class LotteryLoadRewardInfo
extends PlayerHandler
{
class Request
{
ConstEnum.LotteryType type;
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
Lottery Lottery = (Lottery)ActivityMgr.getActivity(Lottery.class);
request.response(Lottery.loadLotteryRwardInfo(player, req.type));
}
}

