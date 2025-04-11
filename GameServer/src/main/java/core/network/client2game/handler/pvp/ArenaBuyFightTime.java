package core.network.client2game.handler.pvp;

import business.global.arena.ArenaConfig;
import business.global.arena.ArenaManager;
import business.global.arena.Competitor;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.features.PlayerRecord;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefCrystalPrice;
import core.config.refdata.ref.RefVIP;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class ArenaBuyFightTime
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        if (req.times == 0) {
            req.times = 1;
        }
        PlayerCurrency currency = (PlayerCurrency) player.getFeature(PlayerCurrency.class);
        PlayerRecord recorder = (PlayerRecord) player.getFeature(PlayerRecord.class);

        int curTimes = recorder.getValue(ConstEnum.DailyRefresh.ArenaChallenge);
        if (curTimes - req.times < 0) {
            throw new WSException(ErrorCode.Arena_ChallengeTimesFull, "玩家[%s]的挑战次数已满", new Object[]{Long.valueOf(player.getPid())});
        }
        int times = recorder.getValue(ConstEnum.DailyRefresh.ArenaBuyChallengeTimes);

        if (times + req.times > ((RefVIP) RefDataMgr.get(RefVIP.class, Integer.valueOf(player.getVipLevel()))).ArenalTimes) {
            throw new WSException(ErrorCode.Arena_ChallengeTimesFull, "玩家[%s]的购买次数已达到最大值", new Object[]{Long.valueOf(player.getPid())});
        }
        int finalcount = 0;
        for (int i = 0; i < req.times; i++) {
            RefCrystalPrice prize = RefCrystalPrice.getPrize(times + i);
            finalcount += prize.ArenaAddChallenge;
        }

        if (!currency.check(PrizeType.Crystal, finalcount)) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "玩家增加挑战需要钻石%s", new Object[]{Integer.valueOf(finalcount)});
        }
        currency.consume(PrizeType.Crystal, finalcount, ItemFlow.ArenaAddChallenge);

        recorder.addValue(ConstEnum.DailyRefresh.ArenaChallenge, -req.times);
        recorder.addValue(ConstEnum.DailyRefresh.ArenaBuyChallengeTimes, req.times);
        int remains = ArenaConfig.maxChallengeTimes() - ((PlayerRecord) player.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.ArenaChallenge);
        int remainBuyTimes = ((RefVIP) RefDataMgr.get(RefVIP.class, Integer.valueOf(player.getVipLevel()))).ArenalTimes - recorder.getValue(ConstEnum.DailyRefresh.ArenaBuyChallengeTimes);

        Competitor competitor = ArenaManager.getInstance().getOrCreate(player.getPid());
        competitor.setFightCD(0);
        request.response(new buyTimesInfo(remains, remainBuyTimes, recorder.getValue(ConstEnum.DailyRefresh.ArenaBuyChallengeTimes)));
    }

    public static class Request {
        int times;
    }

    private static class buyTimesInfo {
        int remainChallengeTimes;
        int remainBuyTimes;
        int buyTimes;

        public buyTimesInfo(int remainChallengeTimes, int remainBuyTimes, int buyTimes) {
            this.remainChallengeTimes = remainChallengeTimes;
            this.remainBuyTimes = remainBuyTimes;
            this.buyTimes = buyTimes;
        }
    }
}

