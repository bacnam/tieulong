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

public class BuyGoods
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        if (req.sid <= 0L) {
            throw new WSException(ErrorCode.InvalidParam, "非法的参数:sId=%s", new Object[]{Long.valueOf(req.sid)});
        }
        if (req.buyTimes <= 0 || req.buyTimes > RefDataMgr.getFactor("BuyItem_MaxBuyTimes", 10000)) {
            throw new WSException(ErrorCode.InvalidParam, "非法的参数:buyTimes=%s", new Object[]{Integer.valueOf(req.buyTimes)});
        }

        PlayerStore store = ((StoreFeature) player.getFeature(StoreFeature.class)).getOrCreate(req.storeType);

        Reward reward = store.doBuyGoods(req.sid, req.buyTimes);
        request.response(reward);
    }

    private static class Request {
        StoreType storeType;
        long sid;
        int buyTimes;
    }
}

