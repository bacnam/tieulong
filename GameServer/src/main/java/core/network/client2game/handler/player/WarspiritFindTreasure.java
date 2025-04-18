package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.PlayerItem;
import business.player.feature.treasure.WarSpiritTreasureFeature;
import business.player.item.Reward;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.ref.RefTreasureWarspirit;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class WarspiritFindTreasure
extends PlayerHandler
{
public static class Request
{
int times;
}

private static class Response {
Reward reward;
int leftTimes;
int leftTentimes;

private Response(Reward reward, int leftTimes, int leftTentimes) {
this.reward = reward;
this.leftTimes = leftTimes;
this.leftTentimes = leftTentimes;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
WarSpiritTreasureFeature feature = (WarSpiritTreasureFeature)player.getFeature(WarSpiritTreasureFeature.class);

RefTreasureWarspirit ref = feature.selectRef(player.getLv());
int cost = 0;
if (req.times == 1) {
cost = ref.Price;
} else if (req.times == 10) {
cost = ref.TenPrice;
} 
PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
if (!playerCurrency.check(PrizeType.Crystal, cost)) {
throw new WSException(ErrorCode.NotEnough_Money, "玩家元宝:%s<所需元宝:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getCrystal()), Integer.valueOf(cost) });
}

playerCurrency.consume(PrizeType.Crystal, cost, ItemFlow.FindTreasure);
Reward reward = feature.findTreasure(req.times);
((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.FindTreasure);
request.response(reward);
}
}

