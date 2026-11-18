package core.network.client2game.handler.pvp;

import business.global.arena.ArenaConfig;
import business.global.arena.ArenaManager;
import business.global.arena.Competitor;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.features.PlayerRecord;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefCrystalPrice;
import core.config.refdata.ref.RefVIP;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class ArenaBuyFightTime extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        ArenaBuyFightTime.Request req = new Gson().fromJson(message, ArenaBuyFightTime.Request.class);
        if (req.times == 0) {
            req.times = 1;
        }

        PlayerCurrency currency = player.getFeature(PlayerCurrency.class);
        PlayerRecord recorder = player.getFeature(PlayerRecord.class);
        int curTimes = recorder.getValue(DailyRefresh.ArenaChallenge);
        if (curTimes - req.times < 0) {
            throw new WSException(ErrorCode.Arena_ChallengeTimesFull, "玩家[%s]的挑战次数已满", player.getPid());
        } else {
            int times = recorder.getValue(DailyRefresh.ArenaBuyChallengeTimes);
            if (times + req.times > RefDataMgr.get(RefVIP.class, player.getVipLevel()).ArenalTimes) {
                throw new WSException(ErrorCode.Arena_ChallengeTimesFull, "玩家[%s]的购买次数已达到最大值", player.getPid());
            } else {
                int finalcount = 0;

                for (int i = 0; i < req.times; i++) {
                    RefCrystalPrice prize = RefCrystalPrice.getPrize(times + i);
                    finalcount += prize.ArenaAddChallenge;
                }

                if (!currency.check(PrizeType.Crystal, finalcount)) {
                    throw new WSException(ErrorCode.NotEnough_Crystal, "玩家增加挑战需要钻石%s", finalcount);
                } else {
                    currency.consume(PrizeType.Crystal, finalcount, ItemFlow.ArenaAddChallenge);
                    recorder.addValue(DailyRefresh.ArenaChallenge, -req.times);
                    recorder.addValue(DailyRefresh.ArenaBuyChallengeTimes, req.times);
                    int remains = ArenaConfig.maxChallengeTimes() - player.getFeature(PlayerRecord.class).getValue(DailyRefresh.ArenaChallenge);
                    int remainBuyTimes = RefDataMgr.get(RefVIP.class, player.getVipLevel()).ArenalTimes
                        - recorder.getValue(DailyRefresh.ArenaBuyChallengeTimes);
                    Competitor competitor = ArenaManager.getInstance().getOrCreate(player.getPid());
                    competitor.setFightCD(0);
                    request.response(new ArenaBuyFightTime.buyTimesInfo(remains, remainBuyTimes, recorder.getValue(DailyRefresh.ArenaBuyChallengeTimes)));
                }
            }
        }
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
