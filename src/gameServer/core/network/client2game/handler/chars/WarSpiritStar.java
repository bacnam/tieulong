package core.network.client2game.handler.chars;

import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.feature.character.WarSpirit;
import business.player.feature.character.WarSpiritFeature;
import business.player.item.UniformItem;
import com.google.gson.Gson;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefWarSpirit;
import core.config.refdata.ref.RefWarSpiritStar;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WarSpiritStar
extends PlayerHandler
{
public static class Request
{
int spiritId;
}

private static class Response {
int spiritId;
int star;

private Response(int spiritId, int star) {
this.spiritId = spiritId;
this.star = star;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
WarSpirit warspirit = ((WarSpiritFeature)player.getFeature(WarSpiritFeature.class)).getWarSpirit(req.spiritId);
if (warspirit == null) {
throw new WSException(ErrorCode.Char_NotFound, "战灵[%s]不存在或未解锁", new Object[] { Integer.valueOf(req.spiritId) });
}
int star = warspirit.getBo().getStar();
if (star >= ((Map)RefWarSpiritStar.spiritMap.get(Integer.valueOf(req.spiritId))).size() - 1) {
throw new WSException(ErrorCode.WarSpiritStarFull, "战灵[%s]星级已满", new Object[] { Integer.valueOf(req.spiritId) });
}

RefWarSpirit ref = (RefWarSpirit)RefDataMgr.get(RefWarSpirit.class, Integer.valueOf(warspirit.getSpiritId()));
int costId = ref.StarMaterial;
RefWarSpiritStar refstar = (RefWarSpiritStar)((Map)RefWarSpiritStar.spiritMap.get(Integer.valueOf(req.spiritId))).get(Integer.valueOf(star + 1));
if (refstar.NeedLv > player.getPlayerBO().getWarspiritLv()) {
throw new WSException(ErrorCode.WarSpiritLevelRequire, "战灵[%s]等级[%s]不足[%s]", new Object[] { Integer.valueOf(req.spiritId), Integer.valueOf(player.getPlayerBO().getWarspiritLv()), Integer.valueOf(refstar.NeedLv) });
}

int costCount = refstar.WarspiritNum;
UniformItem costItem = new UniformItem(costId, costCount);
UniformItem extraItem = new UniformItem(refstar.ExtraId, refstar.ExtraCount);
List<UniformItem> list = new ArrayList<>();
list.add(costItem);
list.add(extraItem);
if (!((PlayerItem)player.getFeature(PlayerItem.class)).check(list)) {
throw new WSException(ErrorCode.NotEnough_Currency, "材料不足");
}
((PlayerItem)player.getFeature(PlayerItem.class)).consume(list, ItemFlow.WarSpiritStar);
warspirit.getBo().saveStar(star + 1);

warspirit.onAttrChanged();
request.response(new Response(req.spiritId, warspirit.getBo().getStar(), null));
}
}

