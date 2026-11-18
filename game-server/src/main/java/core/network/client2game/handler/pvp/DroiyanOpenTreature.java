package core.network.client2game.handler.pvp;

import business.player.Player;
import business.player.feature.features.PlayerRecord;
import business.player.feature.pvp.DroiyanFeature;
import business.player.item.Reward;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class DroiyanOpenTreature extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        DroiyanOpenTreature.Request req = new Gson().fromJson(message, DroiyanOpenTreature.Request.class);
        Reward reward = player.getFeature(DroiyanFeature.class).openTreature(req.sid);
        int times = player.getFeature(PlayerRecord.class).getValue(DailyRefresh.OpenTreasure);
        request.response(new DroiyanOpenTreature.Response(times, reward));
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
