package core.network.client2game.handler.pvc;

import business.global.worldboss.WorldBossMgr;
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
import core.database.game.bo.WorldBossBO;
import core.database.game.bo.WorldBossChallengeBO;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class WorldBossResurrection extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        WorldBossResurrection.Request req = new Gson().fromJson(message, WorldBossResurrection.Request.class);
        WorldBossChallengeBO challenBO = player.getFeature(WorldBossFeature.class).getOrCreate();
        int crystalCost = RefDataMgr.getFactor("WorldBossResurrection", 20)
            + RefDataMgr.getFactor("WorldBossResurrectionAdd", 10) * challenBO.getResurrection();
        PlayerCurrency playerCurrency = player.getFeature(PlayerCurrency.class);
        if (!playerCurrency.check(PrizeType.Crystal, crystalCost)) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "玩家元宝:%s<复活需要元宝:%s", player.getPlayerBO().getCrystal(), crystalCost);
        } else {
            playerCurrency.consume(PrizeType.Crystal, crystalCost, ItemFlow.WorldBoss_Resurrection);
            challenBO.saveResurrection(challenBO.getResurrection() + 1);
            challenBO.saveLeaveFightTime(challenBO.getLeaveFightTime() - RefDataMgr.getFactor("WorldBossAttackCD", 30));
            WorldBossBO boss = WorldBossMgr.getInstance().getBO(req.bossId);
            if (WorldBossMgr.getInstance().fightingPlayers.get(boss) != null && !WorldBossMgr.getInstance().fightingPlayers.get(boss).contains(player)) {
                WorldBossMgr.getInstance().fightingPlayers.get(boss).add(player);
            }

            request.response(challenBO);
        }
    }

    public static class Request {
        int bossId;
    }
}
