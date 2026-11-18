package core.network.client2game.handler.pvp;

import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerBase;
import business.player.feature.PlayerCurrency;
import business.player.feature.features.PlayerRecord;
import business.player.feature.pvp.DroiyanFeature;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.ref.RefCrystalPrice;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class DroiyanSearch extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        DroiyanFeature droiyan = player.getFeature(DroiyanFeature.class);
        if (droiyan.isFullDroiyan()) {
            throw new WSException(ErrorCode.Droiyan_FullDroiyan, "玩家[%s]的检索列表已满，无法继续检索", player.getPid());
        } else {
            PlayerCurrency currency = player.getFeature(PlayerCurrency.class);
            PlayerRecord recorder = player.getFeature(PlayerRecord.class);
            int times = recorder.getValue(DailyRefresh.DroiyanSearch);
            RefCrystalPrice prize = RefCrystalPrice.getPrize(times);
            if (!currency.check(PrizeType.Crystal, prize.DroiyanSearch)) {
                throw new WSException(ErrorCode.NotEnough_Crystal, "玩家第%s次检索需要钻石%s", times, prize.DroiyanSearch);
            } else {
                currency.consume(PrizeType.Crystal, prize.DroiyanSearch, ItemFlow.DroiyanSearch);
                recorder.addValue(DailyRefresh.DroiyanSearch);
                long pid = droiyan.search();
                Player tar = PlayerMgr.getInstance().getPlayer(pid);
                core.network.proto.Player.Summary summary = tar.getFeature(PlayerBase.class).summary();
                if (tar.getFeature(DroiyanFeature.class).haveTreature()) {
                    summary.name = "神秘玩家";
                }

                request.response(summary);
            }
        }
    }
}
