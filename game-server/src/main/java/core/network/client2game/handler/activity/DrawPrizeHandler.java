package core.network.client2game.handler.activity;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.DrawPrize;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.PlayerItem;
import business.player.item.Reward;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class DrawPrizeHandler extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        DrawPrizeHandler.Request req = new Gson().fromJson(message, DrawPrizeHandler.Request.class);
        DrawPrize accumRecharge = ActivityMgr.getActivity(DrawPrize.class);
        if (accumRecharge.getStatus() == ActivityStatus.Close) {
            throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", accumRecharge.getType());
        } else {
            int cost = 0;
            if (req.times == 1) {
                cost = accumRecharge.price;
            } else if (req.times == 10) {
                cost = accumRecharge.tenPrice;
            }

            PlayerCurrency playerCurrency = player.getFeature(PlayerCurrency.class);
            if (!playerCurrency.check(PrizeType.Crystal, cost)) {
                throw new WSException(ErrorCode.NotEnough_Money, "玩家元宝:%s<所需元宝:%s", player.getPlayerBO().getCrystal(), cost);
            } else {
                playerCurrency.consume(PrizeType.Crystal, cost, ItemFlow.DrawPrize);
                Reward reward = accumRecharge.find(player, req.times);
                player.getFeature(PlayerItem.class).gain(reward, ItemFlow.FindTreasure);
                request.response(reward);
            }
        }
    }

    public static class Request {
        int times;
    }
}
