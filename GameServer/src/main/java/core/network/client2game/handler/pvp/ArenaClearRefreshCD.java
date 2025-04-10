package core.network.client2game.handler.pvp;

import business.global.arena.ArenaManager;
import business.global.arena.Competitor;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.features.PlayerRecord;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.ref.RefCrystalPrice;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class ArenaClearRefreshCD
extends PlayerHandler
{
static ArenaManager manager = null;

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
if (manager == null) {
manager = ArenaManager.getInstance();
}

PlayerCurrency currency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);

int times = recorder.getValue(ConstEnum.DailyRefresh.ArenaResetRefreshCD);
RefCrystalPrice prize = RefCrystalPrice.getPrize(times);
if (!currency.check(PrizeType.Crystal, prize.ArenaResetRefreshCD)) {
throw new WSException(ErrorCode.NotEnough_Crystal, "玩家第%s次清除需要钻石%s", new Object[] { Integer.valueOf(times), Integer.valueOf(prize.ArenaResetRefreshCD) });
}
currency.consume(PrizeType.Crystal, prize.ArenaResetFightCD, ItemFlow.ArenaClearFightCD);
recorder.addValue(ConstEnum.DailyRefresh.ArenaResetRefreshCD);

Competitor competitor = manager.getOrCreate(player.getPid());
competitor.setRefreshCD(0);
request.response(Integer.valueOf(recorder.getValue(ConstEnum.DailyRefresh.ArenaResetRefreshCD)));
}
}

