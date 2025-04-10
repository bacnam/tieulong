package core.network.client2game.handler.pvp;

import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerBase;
import business.player.feature.pvp.DroiyanFeature;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DroiyanEnemies
extends PlayerHandler
{
private static PlayerMgr manager;

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
if (manager == null) {
manager = PlayerMgr.getInstance();
}
List<Long> list = ((DroiyanFeature)player.getFeature(DroiyanFeature.class)).getEnemies();
List<Player.Summary> rtn = new ArrayList<>();
for (Long bo : list) {
rtn.add(((PlayerBase)manager.getPlayer(bo.longValue()).getFeature(PlayerBase.class)).summary());
}
request.response(rtn);
}
}

