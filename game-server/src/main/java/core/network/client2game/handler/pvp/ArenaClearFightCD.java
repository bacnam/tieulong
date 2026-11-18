package core.network.client2game.handler.pvp;

import business.global.arena.ArenaManager;
import business.global.arena.Competitor;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.features.PlayerRecord;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.ref.RefCrystalPrice;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class ArenaClearFightCD extends PlayerHandler {
    static ArenaManager manager = null;

    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        if (manager == null) {
            manager = ArenaManager.getInstance();
        }

        PlayerCurrency currency = player.getFeature(PlayerCurrency.class);
        PlayerRecord recorder = player.getFeature(PlayerRecord.class);
        int times = recorder.getValue(DailyRefresh.ArenaResetFightCD);
        RefCrystalPrice prize = RefCrystalPrice.getPrize(times);
        if (!currency.check(PrizeType.Crystal, prize.ArenaResetFightCD)) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "玩家第%s次清除需要钻石%s", times, prize.ArenaResetFightCD);
        } else {
            currency.consume(PrizeType.Crystal, prize.ArenaResetFightCD, ItemFlow.ArenaClearFightCD);
            recorder.addValue(DailyRefresh.ArenaResetFightCD);
            Competitor competitor = manager.getOrCreate(player.getPid());
            competitor.setFightCD(0);
            request.response(recorder.getValue(DailyRefresh.ArenaResetFightCD));
        }
    }
}
