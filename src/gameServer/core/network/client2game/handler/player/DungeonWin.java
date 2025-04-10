package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.PlayerBase;
import business.player.feature.pve.DungeonFeature;
import business.player.item.Reward;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class DungeonWin
extends PlayerHandler
{
public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
DungeonFeature dungeon = (DungeonFeature)player.getFeature(DungeonFeature.class);
if (dungeon.isInWinCD()) {
request.error(ErrorCode.Dungeon_WinCD, "副本挑战CD中", new Object[0]);
if (dungeon.isCheater()) {
((PlayerBase)player.getFeature(PlayerBase.class)).kickout();
}
} else {
Reward reward = dungeon.win();
request.response(reward);
} 
}
}

