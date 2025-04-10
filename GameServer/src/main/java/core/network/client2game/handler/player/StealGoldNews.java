package core.network.client2game.handler.player;

import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.pvp.StealGoldFeature;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.database.game.bo.StealGoldNewsBO;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StealGoldNews
extends PlayerHandler
{
private static class StealGoldNewsInfo
{
String name;
int money;
int time;

private StealGoldNewsInfo(StealGoldNewsBO bo) {
this.name = PlayerMgr.getInstance().getPlayer(bo.getAtkid()).getName();
this.money = bo.getMoney();
this.time = bo.getTime();
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
StealGoldFeature feature = (StealGoldFeature)player.getFeature(StealGoldFeature.class);

List<StealGoldNewsInfo> list = new ArrayList<>();
for (StealGoldNewsBO bo : feature.getNews()) {
list.add(new StealGoldNewsInfo(bo, null));
}

request.response(list);
}
}

