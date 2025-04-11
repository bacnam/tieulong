package core.network.client2game.handler.pvc;

import business.global.worldboss.WorldBossMgr;
import business.player.Player;
import business.player.feature.PlayerBase;
import business.player.feature.worldboss.WorldBossFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.database.game.bo.WorldBossBO;
import core.database.game.bo.WorldBossChallengeBO;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorldBossEnter
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        WorldBossChallengeBO challengBo = ((WorldBossFeature) player.getFeature(WorldBossFeature.class)).beginFightWorldBoss(req.bossId);
        WorldBossBO boss = WorldBossMgr.getInstance().getBO(req.bossId);
        List<Player> players = WorldBossMgr.getInstance().getPlayerList(boss);
        player.pushProto("challengInfo", challengBo);
        List<Player.FightInfo> fullInfoList = new ArrayList<>();
        for (Player tmpPlay : players) {
            if (tmpPlay != null && tmpPlay != player)
                fullInfoList.add(((PlayerBase) tmpPlay.getFeature(PlayerBase.class)).fightInfo());
            if (fullInfoList.size() > RefDataMgr.getFactor("MaxWorldBossRole", 15)) {
                break;
            }
        }

        int size = fullInfoList.size();
        for (int i = 0; i < RefDataMgr.getFactor("MaxWorldBossRobot", 15) - 1 - size; i++) {
            fullInfoList.add(((PlayerBase) ((Player) (WorldBossMgr.getInstance()).robotPlayers.get(i)).getFeature(PlayerBase.class)).fightInfo());
        }
        request.response(new FightInfo1(fullInfoList, boss));
    }

    public static class Request {
        int bossId;
    }

    public class FightInfo1 {
        List<Player.FightInfo> players;
        WorldBossBO boss;

        public FightInfo1(List<Player.FightInfo> fullInfo, WorldBossBO boss) {
            this.players = fullInfo;
            this.boss = boss;
        }
    }
}

