package core.network.client2game.handler.player;

import business.global.recharge.RechargeMgr;
import business.player.Player;
import business.player.feature.record.VipRecord;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class PlayerVipChange
extends PlayerHandler
{
public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
request.response(((VipRecord)player.getFeature(VipRecord.class)).getFetchGiftStatusList());
RechargeMgr.getInstance().trySendCachedOrder(player.getPid());
}
}

