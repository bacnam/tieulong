package core.network.client2game.handler.pvp;

import business.global.arena.ArenaConfig;
import business.global.arena.ArenaManager;
import business.global.arena.Competitor;
import business.player.Player;
import business.player.feature.features.PlayerRecord;
import com.zhonglian.server.common.enums.UnlockType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefUnlockFunction;
import core.config.refdata.ref.RefVIP;
import core.database.game.bo.ArenaCompetitorBO;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.Arena;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArenaInfo extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        RefUnlockFunction.checkUnlock(player, UnlockType.Arena);
        PlayerRecord recorder = player.getFeature(PlayerRecord.class);
        Competitor competitor = ArenaManager.getInstance().getOrCreate(player.getPid());
        int remains = ArenaConfig.maxChallengeTimes() - recorder.getValue(DailyRefresh.ArenaChallenge);
        int times1 = recorder.getValue(DailyRefresh.ArenaBuyChallengeTimes);
        int remainBuyTimes = RefDataMgr.get(RefVIP.class, player.getVipLevel()).ArenalTimes - times1;
        int times2 = recorder.getValue(DailyRefresh.ArenaResetFightCD);
        int times3 = recorder.getValue(DailyRefresh.ArenaResetRefreshCD);
        request.response(new ArenaInfo.Response(competitor, remains, times1, remainBuyTimes, times2, times3, competitor.getOpponents()));
    }

    private static class Response {
        int rank;
        int refreshCD;
        int fightCD;
        int challengeTimes;
        int buyTimes;
        int remainBuyTimes;
        int clearFightCDTimes;
        int clearRefreshCDTimes;
        List<Arena.CompetitorInfo> opponents;

        public Response(
            Competitor competitor, int remains, int buyTimes, int remainBuyTimes, int clearFightCDTimes, int clearRefreshCDTimes, List<Competitor> opponents
        ) {
            ArenaCompetitorBO bo = competitor.getBo();
            this.rank = bo.getRank();
            this.refreshCD = competitor.getRefreshCD();
            this.fightCD = competitor.getFightCD();
            this.challengeTimes = remains;
            this.buyTimes = buyTimes;
            this.remainBuyTimes = remainBuyTimes;
            this.clearFightCDTimes = clearFightCDTimes;
            this.clearRefreshCDTimes = clearRefreshCDTimes;
            this.opponents = new ArrayList<>();

            for (Competitor opp : opponents) {
                this.opponents.add(new Arena.CompetitorInfo(opp));
            }
        }
    }
}
