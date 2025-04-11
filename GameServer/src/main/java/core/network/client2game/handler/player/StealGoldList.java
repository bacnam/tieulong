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
import java.util.Iterator;
import java.util.List;

public class StealGoldList
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        StealGoldFeature feature = (StealGoldFeature) player.getFeature(StealGoldFeature.class);
        List<Long> faighters = feature.getList();
        List<StealGoldEnemy> players = new ArrayList<>();
        for (Iterator<Long> iterator = faighters.iterator(); iterator.hasNext(); ) {
            long i = ((Long) iterator.next()).longValue();
            Player tmp_player = PlayerMgr.getInstance().getPlayer(i);
            if (((DroiyanFeature) player.getFeature(DroiyanFeature.class)).isEnemy(i)) {
                players.add(new StealGoldEnemy(((PlayerBase) tmp_player.getFeature(PlayerBase.class)).summary(), true, null));
                continue;
            }
            players.add(new StealGoldEnemy(((PlayerBase) tmp_player.getFeature(PlayerBase.class)).summary(), false, null));
        }

        List<StealGoldNewsBO> news = feature.getNews();
        int nowTime = feature.getTimes();
        int maxTimes = ((RefVIP) RefDataMgr.get(RefVIP.class, Integer.valueOf(player.getVipLevel()))).StealGold;
        List<Long> money = new ArrayList<>();
        for (Iterator<Integer> iterator1 = feature.getMoneyList().iterator(); iterator1.hasNext(); ) {
            int ext = ((Integer) iterator1.next()).intValue();
            RefCrystalPrice prize = RefCrystalPrice.getPrize(nowTime);
            money.add(Long.valueOf(prize.StealGoldGain * (1000 + ext) / 1000L));
        }

        request.response(new StealGoldInfo(players, money, news, nowTime, maxTimes, null));
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

    private static class StealGoldEnemy {
        Player.Summary player;
        boolean isEnemy;

        private StealGoldEnemy(Player.Summary player, boolean isEnemy) {
            this.player = player;
            this.isEnemy = isEnemy;
        }
    }
}

