package core.network.client2game.handler.pvp;

import business.global.pvp.EncouterManager;
import business.player.Player;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class DroiyanNews
extends PlayerHandler
{
public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
request.response(EncouterManager.getInstance().getEncouterNews());
}
}

