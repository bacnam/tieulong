package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.pve.InstanceFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.InstanceType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class InstanceBuyTimes extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        InstanceBuyTimes.Request req = new Gson().fromJson(message, InstanceBuyTimes.Request.class);
        if (req.times == 0) {
            req.times = 1;
        }

        InstanceFeature feature = player.getFeature(InstanceFeature.class);
        String challengAndTimes = feature.buyChallengTimes(req.type, req.times);
        String[] info = challengAndTimes.split(";");
        request.response(new InstanceBuyTimes.instanceFightInfo(req.type, Integer.valueOf(info[0]), Integer.valueOf(info[1])));
    }

    public static class Request {
        InstanceType type;
        int times;
    }

    static class instanceFightInfo {
        InstanceType type;
        int challengTimes;
        int buyTimes;

        public instanceFightInfo(InstanceType type, int challengTimes, int buyTimes) {
            this.type = type;
            this.challengTimes = challengTimes;
            this.buyTimes = buyTimes;
        }
    }
}
