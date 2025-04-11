package core.network.client2game.handler.pvp;

import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerBase;
import business.player.feature.PlayerCurrency;
import business.player.feature.features.PlayerRecord;
import business.player.feature.pvp.DroiyanFeature;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.ref.RefCrystalPrice;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class DroiyanSearch
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        DroiyanFeature droiyan = (DroiyanFeature) player.getFeature(DroiyanFeature.class);
        if (droiyan.isFullDroiyan()) {
            throw new WSException(ErrorCode.Droiyan_FullDroiyan, "玩家[%s]的检索列表已满，无法继续检索", new Object[]{Long.valueOf(player.getPid())});
        }

        PlayerCurrency currency = (PlayerCurrency) player.getFeature(PlayerCurrency.class);
        PlayerRecord recorder = (PlayerRecord) player.getFeature(PlayerRecord.class);

        int times = recorder.getValue(ConstEnum.DailyRefresh.DroiyanSearch);
        RefCrystalPrice prize = RefCrystalPrice.getPrize(times);
        if (!currency.check(PrizeType.Crystal, prize.DroiyanSearch)) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "玩家第%s次检索需要钻石%s", new Object[]{Integer.valueOf(times), Integer.valueOf(prize.DroiyanSearch)});
        }
        currency.consume(PrizeType.Crystal, prize.DroiyanSearch, ItemFlow.DroiyanSearch);
        recorder.addValue(ConstEnum.DailyRefresh.DroiyanSearch);

        long pid = droiyan.search();

        Player tar = PlayerMgr.getInstance().getPlayer(pid);
        Player.Summary summary = ((PlayerBase) tar.getFeature(PlayerBase.class)).summary();
        if (((DroiyanFeature) tar.getFeature(DroiyanFeature.class)).haveTreature()) {
            summary.name = "神秘玩家";
        }
        request.response(summary);
    }
}

