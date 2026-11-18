package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.features.PlayerRecord;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefCrystalPrice;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class BuyPackage extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        PlayerCurrency currency = player.getFeature(PlayerCurrency.class);
        PlayerRecord recorder = player.getFeature(PlayerRecord.class);
        int curTimes = recorder.getValue(DailyRefresh.PackageBuyTimes);
        RefCrystalPrice prize = RefCrystalPrice.getPrize(curTimes);
        if (prize == null) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "背包已购买最大");
        } else if (!currency.check(PrizeType.Crystal, prize.PackageBuyTimes)) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "玩家第%s次购买背包需要钻石%s", curTimes, prize.PackageBuyTimes);
        } else {
            currency.consume(PrizeType.Crystal, prize.PackageBuyTimes, ItemFlow.BuyPackage);
            player.getPlayerBO().saveExtPackage(player.getPlayerBO().getExtPackage() + RefDataMgr.getFactor("BuyPackageSize", 50));
            recorder.addValue(DailyRefresh.PackageBuyTimes);
            request.response(new BuyPackage.Response(recorder.getValue(DailyRefresh.PackageBuyTimes), player.getPlayerBO().getExtPackage()));
        }
    }

    private static class Response {
        int buyTimes;
        int extpackage;

        public Response(int buyTimes, int extpackage) {
            this.buyTimes = buyTimes;
            this.extpackage = extpackage;
        }
    }
}
