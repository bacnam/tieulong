package core.network.client2game.handler.chars;

import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.feature.character.WarSpirit;
import business.player.feature.character.WarSpiritFeature;
import com.google.gson.Gson;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefWarSpirit;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.WarSpiritInfo;
import java.io.IOException;

public class WarSpiritActive
extends PlayerHandler
{
public static class Request
{
int spiritId;
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
RefWarSpirit ref = (RefWarSpirit)RefDataMgr.get(RefWarSpirit.class, Integer.valueOf(req.spiritId));
if (ref == null) {
throw new WSException(ErrorCode.WarSpiritTalentFull, "战灵未找到");
}
if (ref.ActiveId != 0 && 
!((PlayerItem)player.getFeature(PlayerItem.class)).check(ref.ActiveId, ref.ActiveCount)) {
throw new WSException(ErrorCode.NotEnough_Currency, "材料不足");
}

int id = ((WarSpiritFeature)player.getFeature(WarSpiritFeature.class)).unlockWarSpirit(req.spiritId);
((PlayerItem)player.getFeature(PlayerItem.class)).consume(ref.ActiveId, ref.ActiveCount, ItemFlow.UnlockWarSpirit);
WarSpirit WarSpirit = ((WarSpiritFeature)player.getFeature(WarSpiritFeature.class)).getWarSpirit(id);

request.response(new WarSpiritInfo(WarSpirit));
}
}

