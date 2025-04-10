package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.treasure.WarSpiritTreasureRecord;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class WarspiritTreasureInfo
extends PlayerHandler
{
public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
request.response(WarSpiritTreasureRecord.getInstance().getRecords());
}
}

