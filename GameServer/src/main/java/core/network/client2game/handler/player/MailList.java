package core.network.client2game.handler.player;

import business.global.recharge.RechargeMgr;
import business.player.Player;
import business.player.feature.features.MailFeature;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class MailList
extends PlayerHandler
{
public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
request.response(((MailFeature)player.getFeature(MailFeature.class)).cmd_getMailList());
RechargeMgr.getInstance().trySendCachedOrder(player.getPid());
}
}

