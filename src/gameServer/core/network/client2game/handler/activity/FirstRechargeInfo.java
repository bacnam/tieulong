package core.network.client2game.handler.activity;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.FirstRecharge;
import business.player.Player;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class FirstRechargeInfo
extends PlayerHandler
{
public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
request.response(Boolean.valueOf(((FirstRecharge)ActivityMgr.getActivity(FirstRecharge.class)).fristRechargeProto(player)));
}
}

