package core.network.client2game.handler.player;

import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerCurrency;
import business.player.feature.pvp.StealGoldFeature;
import business.player.item.Reward;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.utils.Random;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefCrystalPrice;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StealGold extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        StealGold.Request req = new Gson().fromJson(message, StealGold.Request.class);
        StealGoldFeature feature = player.getFeature(StealGoldFeature.class);
        if (!feature.getList().contains(req.targetPid)) {
            throw new WSException(ErrorCode.StealGold_NotFound, "人物不存在");
        } else {
            PlayerCurrency currency = player.getFeature(PlayerCurrency.class);
            int times = feature.getTimes();
            if (!feature.checkTimes()) {
                throw new WSException(ErrorCode.StealGold_NotEnough, "探金手次数不足");
            } else {
                RefCrystalPrice prize = RefCrystalPrice.getPrize(times);
                if (!currency.check(PrizeType.Crystal, prize.StealGoldPrice)) {
                    throw new WSException(ErrorCode.NotEnough_Crystal, "玩家第%s次探金手需要钻石%s", times, prize.StealGoldPrice);
                } else {
                    currency.consume(PrizeType.Crystal, prize.StealGoldPrice, ItemFlow.StealGold);
                    long get = 0L;
                    int index = feature.getList().indexOf(req.targetPid);
                    long money = (long)prize.StealGoldGain * (1000 + feature.getMoneyList().get(index)) / 1000L;
                    if (Random.nextInt(10000) < RefDataMgr.getFactor("StealGoldCrit", 5000)) {
                        get = money * 2L;
                    } else {
                        get = money;
                    }

                    int gain = currency.gain(PrizeType.Gold, (int)get, ItemFlow.StealGold);
                    feature.addTimes();
                    Player target = PlayerMgr.getInstance().getPlayer(req.targetPid);
                    target.getFeature(StealGoldFeature.class).addNews(player.getPid(), gain);
                    Reward reward = new Reward();
                    reward.add(PrizeType.Gold, gain);
                    int nowTime = feature.getTimes();
                    List<Long> moneylist = new ArrayList<>();

                    for (int ext : feature.getMoneyList()) {
                        RefCrystalPrice tmp_prize = RefCrystalPrice.getPrize(nowTime);
                        moneylist.add((long)tmp_prize.StealGoldGain * (1000 + ext) / 1000L);
                    }

                    request.response(new StealGold.StealGoldInfo(feature.getTimes(), reward, moneylist));
                }
            }
        }
    }

    private static class Request {
        long targetPid;
    }

    private static class StealGoldInfo {
        int nowTimes;
        Reward reward;
        List<Long> money;

        private StealGoldInfo(int nowTimes, Reward reward, List<Long> money) {
            this.nowTimes = nowTimes;
            this.reward = reward;
            this.money = money;
        }
    }
}
