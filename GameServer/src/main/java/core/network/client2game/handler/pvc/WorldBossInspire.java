package core.network.client2game.handler.pvc;

import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.worldboss.WorldBossFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.database.game.bo.WorldBossChallengeBO;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class WorldBossInspire
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        WorldBossChallengeBO challenBO = ((WorldBossFeature) player.getFeature(WorldBossFeature.class)).getOrCreate();

        if (challenBO.getInspiringTimes(req.bossId - 1) >= RefDataMgr.getFactor("MaxInspireTimes", 10)) {
            throw new WSException(ErrorCode.WorldBoss_InspireFull, "鼓舞已达最大次数");
        }

        int crystalCost = (int) Math.pow(2.0D, challenBO.getInspiringTimes(req.bossId - 1)) * 10;

        PlayerCurrency playerCurrency = (PlayerCurrency) player.getFeature(PlayerCurrency.class);
        if (!playerCurrency.check(PrizeType.Crystal, crystalCost)) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "玩家元宝:%s<鼓励需要元宝:%s", new Object[]{Integer.valueOf(player.getPlayerBO().getCrystal()), Integer.valueOf(crystalCost)});
        }

        playerCurrency.consume(PrizeType.Crystal, crystalCost, ItemFlow.WorldBoss_Inspiring);

        challenBO.saveInspiringTimes(req.bossId - 1, challenBO.getInspiringTimes(req.bossId - 1) + 1L);
        request.response(challenBO);
    }

    public static class Request {
        int bossId;
    }
}

