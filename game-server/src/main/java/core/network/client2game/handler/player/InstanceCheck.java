package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.features.PlayerRecord;
import business.player.feature.pve.InstanceFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.InstanceType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class InstanceCheck extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        InstanceCheck.Request req = new Gson().fromJson(message, InstanceCheck.Request.class);
        int level = 0;
        int challengTimes = 0;
        InstanceFeature feature = player.getFeature(InstanceFeature.class);
        level = feature.getOrCreate().getInstanceMaxLevel(req.type.ordinal());
        challengTimes = feature.getOrCreate().getChallengTimes(req.type.ordinal());
        DailyRefresh dailyType = null;
        PlayerRecord recorder = player.getFeature(PlayerRecord.class);
        int times = 0;
        switch (req.type) {
            case EquipInstance:
                dailyType = DailyRefresh.EquipInstanceBuyTimes;
                times = recorder.getValue(dailyType);
                break;
            case GemInstance:
                dailyType = DailyRefresh.GemInstanceBuyTimes;
                times = recorder.getValue(dailyType);
                break;
            case MeridianInstance:
                dailyType = DailyRefresh.MeridianInstanceBuyTimes;
                times = recorder.getValue(dailyType);
        }

        request.response(new InstanceCheck.InstanceInfo(level, challengTimes, times, req.type));
    }

    public static class InstanceInfo {
        int maxLevel;
        int challengTimes;
        int buyTimes;
        InstanceType type;

        public InstanceInfo(int maxLevel, int challengTimes, int buyTimes, InstanceType type) {
            this.maxLevel = maxLevel;
            this.challengTimes = challengTimes;
            this.buyTimes = buyTimes;
            this.type = type;
        }
    }

    public static class Request {
        InstanceType type;
    }
}
