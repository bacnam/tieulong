package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.pve.InstanceFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.InstanceType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class InstanceFight extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        InstanceFight.Request req = new Gson().fromJson(message, InstanceFight.Request.class);
        int challengTimes = 0;
        InstanceFeature feature = player.getFeature(InstanceFeature.class);
        challengTimes = feature.getOrCreate().getChallengTimes(req.type.ordinal());
        request.response(new InstanceFight.instanceFightInfo(req.type, challengTimes));
    }

    public static class Request {
        InstanceType type;
        int level;
    }

    static class instanceFightInfo {
        InstanceType type;
        int challengTimes;

        public instanceFightInfo(InstanceType type, int challengTimes) {
            this.type = type;
            this.challengTimes = challengTimes;
        }
    }
}
