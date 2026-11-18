package core.network.client2game.handler.player;

import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerBase;
import business.player.feature.pvp.DroiyanFeature;
import business.player.feature.pvp.StealGoldFeature;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefCrystalPrice;
import core.config.refdata.ref.RefVIP;
import core.database.game.bo.StealGoldNewsBO;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StealGoldList extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        StealGoldFeature feature = player.getFeature(StealGoldFeature.class);
        List<Long> faighters = feature.getList();
        List<StealGoldList.StealGoldEnemy> players = new ArrayList<>();

        for (long i : faighters) {
            Player tmp_player = PlayerMgr.getInstance().getPlayer(i);
            if (player.getFeature(DroiyanFeature.class).isEnemy(i)) {
                players.add(new StealGoldList.StealGoldEnemy(tmp_player.getFeature(PlayerBase.class).summary(), true));
            } else {
                players.add(new StealGoldList.StealGoldEnemy(tmp_player.getFeature(PlayerBase.class).summary(), false));
            }
        }

        List<StealGoldNewsBO> news = feature.getNews();
        int nowTime = feature.getTimes();
        int maxTimes = RefDataMgr.get(RefVIP.class, player.getVipLevel()).StealGold;
        List<Long> money = new ArrayList<>();

        for (int ext : feature.getMoneyList()) {
            RefCrystalPrice prize = RefCrystalPrice.getPrize(nowTime);
            money.add((long)prize.StealGoldGain * (1000 + ext) / 1000L);
        }

        request.response(new StealGoldList.StealGoldInfo(players, money, news, nowTime, maxTimes));
    }

    private static class StealGoldEnemy {
        core.network.proto.Player.Summary player;
        boolean isEnemy;

        private StealGoldEnemy(core.network.proto.Player.Summary player, boolean isEnemy) {
            this.player = player;
            this.isEnemy = isEnemy;
        }
    }

    private static class StealGoldInfo {
        List<StealGoldList.StealGoldEnemy> players;
        List<Long> money;
        int nowTimes;
        int maxTimes;

        private StealGoldInfo(List<StealGoldList.StealGoldEnemy> players, List<Long> money, List<StealGoldNewsBO> news, int nowTimes, int maxTimes) {
            this.players = players;
            this.money = money;
            this.nowTimes = nowTimes;
            this.maxTimes = maxTimes;
        }
    }
}
