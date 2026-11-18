package core.network.client2game.handler.store;

import business.player.Player;
import business.player.feature.store.PlayerStore;
import business.player.feature.store.StoreFeature;
import business.player.item.Reward;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.StoreType;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class BuyGoods extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        BuyGoods.Request req = new Gson().fromJson(message, BuyGoods.Request.class);
        if (req.sid <= 0L) {
            throw new WSException(ErrorCode.InvalidParam, "非法的参数:sId=%s", req.sid);
        } else if (req.buyTimes > 0 && req.buyTimes <= RefDataMgr.getFactor("BuyItem_MaxBuyTimes", 10000)) {
            PlayerStore store = player.getFeature(StoreFeature.class).getOrCreate(req.storeType);
            Reward reward = store.doBuyGoods(req.sid, req.buyTimes);
            request.response(reward);
        } else {
            throw new WSException(ErrorCode.InvalidParam, "非法的参数:buyTimes=%s", req.buyTimes);
        }
    }

    private static class Request {
        StoreType storeType;
        long sid;
        int buyTimes;
    }
}
