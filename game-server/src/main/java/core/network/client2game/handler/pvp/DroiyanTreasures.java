package core.network.client2game.handler.pvp;

import business.player.Player;
import business.player.feature.features.PlayerRecord;
import business.player.feature.pvp.DroiyanFeature;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.database.game.bo.DroiyanTreasureBO;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DroiyanTreasures extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        List<DroiyanTreasureBO> list = player.getFeature(DroiyanFeature.class).getTreasures();
        List<DroiyanTreasures.Treasure> rtn = new ArrayList<>();

        for (DroiyanTreasureBO bo : list) {
            rtn.add(new DroiyanTreasures.Treasure(bo));
        }

        int times = player.getFeature(PlayerRecord.class).getValue(DailyRefresh.OpenTreasure);
        request.response(new DroiyanTreasures.Respose(times, rtn));
    }

    private static class Respose {
        int openTimes;
        List<DroiyanTreasures.Treasure> treasures;

        private Respose(int openTimes, List<DroiyanTreasures.Treasure> treasures) {
            this.openTimes = openTimes;
            this.treasures = treasures;
        }
    }

    public static class Treasure {
        long sid;
        int treasureId;
        int expireTime;

        public Treasure(DroiyanTreasureBO bo) {
            this.sid = bo.getId();
            this.treasureId = bo.getTreasureId();
            this.expireTime = bo.getExpireTime();
        }
    }
}
