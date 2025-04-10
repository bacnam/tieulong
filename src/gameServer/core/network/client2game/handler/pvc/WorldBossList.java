package core.network.client2game.handler.pvc;

import business.global.worldboss.WorldBossMgr;
import business.player.Player;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.database.game.bo.WorldBossBO;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.WorldBossInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorldBossList
extends PlayerHandler
{
public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
List<WorldBossInfo> infoList = new ArrayList<>();
for (WorldBossBO boss : WorldBossMgr.getInstance().getBOList()) {
infoList.add(new WorldBossInfo(boss));
}

request.response(infoList);
}
}

