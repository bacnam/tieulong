package core.network.client2game.handler.activity;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.AccumRechargeDay;
import business.player.Player;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class AccumDayRechargeInfo
extends PlayerHandler
{
public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
AccumRechargeDay accumRecharge = (AccumRechargeDay)ActivityMgr.getActivity(AccumRechargeDay.class);
if (accumRecharge.getStatus() == ActivityStatus.Close) {
throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", new Object[] { accumRecharge.getType() });
}
request.response(accumRecharge.getList(player));
}
}

