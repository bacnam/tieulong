package core.network.client2game.handler.pvc;

import business.global.rank.RankManager;
import business.global.rank.Record;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerBase;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorldBossRank extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        WorldBossRank.Request req = new Gson().fromJson(message, WorldBossRank.Request.class);
        RankType type = null;
        switch (req.bossId) {
            case 1:
                type = RankType.WorldBoss1;
                break;
            case 2:
                type = RankType.WorldBoss2;
                break;
            case 3:
                type = RankType.WorldBoss3;
                break;
            case 4:
                type = RankType.WorldBoss4;
        }

        List<Record> listRecord = RankManager.getInstance().getRankList(type, 100);
        List<WorldBossRank.RankInfo> rankinfo = new ArrayList<>();
        if (listRecord.size() > 1) {
            for (int i = 1; i < listRecord.size(); i++) {
                Record record = listRecord.get(i);
                Player tmpPlayer = PlayerMgr.getInstance().getPlayer(record.getPid());
                core.network.proto.Player.Summary playersum = tmpPlayer.getFeature(PlayerBase.class).summary();
                rankinfo.add(new WorldBossRank.RankInfo(playersum, record.getRank(), record.getValue()));
            }
        }

        request.response(rankinfo);
    }

    public static class RankInfo {
        core.network.proto.Player.Summary players;
        int rank;
        long damage;

        public RankInfo(core.network.proto.Player.Summary players, int rank, long damage) {
            this.players = players;
            this.rank = rank;
            this.damage = damage;
        }
    }

    public static class Request {
        int bossId;
    }
}
