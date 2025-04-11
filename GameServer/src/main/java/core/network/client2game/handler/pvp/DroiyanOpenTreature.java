package core.network.client2game.handler.pvp;

import business.player.Player;
import business.player.feature.features.PlayerRecord;
import business.player.feature.pvp.DroiyanFeature;
import business.player.item.Reward;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class DroiyanOpenTreature
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        Reward reward = ((DroiyanFeature) player.getFeature(DroiyanFeature.class)).openTreature(req.sid);
        int times = ((PlayerRecord) player.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.OpenTreasure);
        request.response(new Response(times, reward, null));
    }

    static class Request {
        long sid;
    }

    private static class Response {
        int openTimes;
        Reward reward;

        private Response(int openTimes, Reward reward) {
            this.openTimes = openTimes;
            this.reward = reward;
        }
    }
}

