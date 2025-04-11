package core.network.client2game.handler.player;

import business.global.rank.RankManager;
import business.global.rank.Record;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.character.CharFeature;
import business.player.feature.features.RechargeFeature;
import business.player.feature.pvp.WorshipFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.Achievement;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import com.zhonglian.server.websocket.handler.response.ResponseHandler;
import com.zhonglian.server.websocket.handler.response.WebSocketResponse;
import core.database.game.bo.PlayerBO;
import core.network.client2game.handler.PlayerHandler;
import core.network.game2world.WorldConnector;
import proto.gameworld.WorldRankInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RankList
        extends PlayerHandler {
    public void handle(Player player, final WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);

        if (WorldConnector.getInstance().isConnected()) {
            RankType rankType = null;
            switch (req.rank) {
                case WorldRecharge:
                    rankType = req.rank;
                    break;
                case WorldConsume:
                    rankType = req.rank;
                    break;
                case WorldTreasure:
                    rankType = req.rank;
                    break;
            }

            if (rankType != null) {
                WorldRequest worldRequest = new WorldRequest(null);
                worldRequest.rank = rankType;
                worldRequest.pid = player.getPid();
                WorldConnector.request("activity.ranklist", worldRequest, new ResponseHandler() {
                    public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException {
                        WorldRankInfo req = (WorldRankInfo) (new Gson()).fromJson(body, WorldRankInfo.class);
                        request.response(req);
                    }

                    public void handleError(WebSocketResponse ssresponse, short statusCode, String message) {
                    }
                });
                return;
            }
        }
        List<Record> records = RankManager.getInstance().getRankList(req.rank, 30);
        int rank = RankManager.getInstance().getRank(req.rank, player.getPid());
        long value = RankManager.getInstance().getValue(req.rank, player.getPid());
        int times = ((WorshipFeature) player.getFeature(WorshipFeature.class)).getTimes(req.rank.ordinal());
        request.response(new Response(times, rank, value, records));
    }

    private static class Request {
        RankType rank;
    }

    private static class Response {
        int worshipTimes;
        int rank;
        long value;
        List<RankList.RankInfo> rankList;

        public Response(int worshipTimes, int rank, long value, List<Record> records) {
            this.worshipTimes = worshipTimes;
            this.rank = rank;
            this.value = value;
            this.rankList = new ArrayList<>();
            for (int i = 0; i < records.size(); i++) {
                Record r = records.get(i);
                if (r != null) {

                    this.rankList.add(new RankList.RankInfo(r));
                }
            }
        }
    }

    public static class RankInfo extends Player.Summary {
        static PlayerMgr playerMgr;
        int rank;
        long value;

        public RankInfo(Record record) {
            if (playerMgr == null) {
                playerMgr = PlayerMgr.getInstance();
            }

            Player player = playerMgr.getPlayer(record.getPid());
            PlayerBO bo = player.getPlayerBO();
            this.pid = bo.getId();
            this.name = bo.getName();
            this.lv = bo.getLv();
            this.icon = bo.getIcon();
            this.vipLv = bo.getVipLevel();
            this.power = ((CharFeature) player.getFeature(CharFeature.class)).getPower();
            this.rank = record.getRank();
            this.value = record.getValue();

            RechargeFeature rechargeFeature = (RechargeFeature) player.getFeature(RechargeFeature.class);
            int monthNum = rechargeFeature.getRebateRemains(Achievement.AchievementType.MonthCardCrystal);
            this.MonthCard = (monthNum > 0);
            int yearNum = rechargeFeature.getRebateRemains(Achievement.AchievementType.YearCardCrystal);
            this.YearCard = (yearNum == -1);
        }
    }

    private static class WorldRequest {
        RankType rank;

        long pid;

        private WorldRequest() {
        }
    }
}

