package core.network.client2game.handler.pvp;

import business.global.arena.ArenaConfig;
import business.global.arena.ArenaManager;
import business.global.arena.Competitor;
import business.player.Player;
import business.player.feature.pvp.WorshipFeature;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.Arena;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArenaRankList
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        List<Competitor> rank = ArenaManager.getInstance().getRankList(ArenaConfig.showSize());
        List<Arena.CompetitorInfo> rtn = new ArrayList<>();
        for (Competitor opp : rank) {
            rtn.add(new Arena.CompetitorInfo(opp));
        }

        int times = ((WorshipFeature) player.getFeature(WorshipFeature.class)).getTimes(RankType.Arena.ordinal());
        request.response(new Respose(times, rtn, null));
    }

    private static class Respose {
        int worshipTimes;
        List<Arena.CompetitorInfo> ranklist;

        private Respose(int worshipTimes, List<Arena.CompetitorInfo> ranklist) {
            this.worshipTimes = worshipTimes;
            this.ranklist = ranklist;
        }
    }
}

