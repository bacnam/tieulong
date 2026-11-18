package core.network.client2game.handler.pvp;

import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.pvp.DroiyanFeature;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.database.game.bo.DroiyanBO;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class DroiyanClearRed extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        DroiyanBO bo = player.getFeature(DroiyanFeature.class).getBo();
        int cost = bo.getRed() / 2;
        PlayerCurrency currency = player.getFeature(PlayerCurrency.class);
        if (!currency.checkAndConsume(PrizeType.Crystal, cost, ItemFlow.DroiyanClearRed)) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "消除红名需要[%s]钻石", cost);
        } else {
            bo.saveRed(0);
            request.response();
        }
    }
}
