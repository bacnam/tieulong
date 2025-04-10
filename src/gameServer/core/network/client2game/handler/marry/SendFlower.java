package core.network.client2game.handler.marry;

import business.player.Player;
import business.player.feature.features.PlayerRecord;
import business.player.feature.marry.MarryFeature;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class SendFlower
extends PlayerHandler
{
public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
MarryFeature feature = (MarryFeature)player.getFeature(MarryFeature.class);
feature.sendFlower();
PlayerRecord record = (PlayerRecord)player.getFeature(PlayerRecord.class);
int times = record.getValue(ConstEnum.DailyRefresh.LoversSend);
request.response(Integer.valueOf(times));
}
}

