package core.network.client2game.handler.player;

import business.global.rank.RankManager;
import business.global.rank.Record;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.character.CharFeature;
import business.player.feature.features.RechargeFeature;
import business.player.feature.pvp.WorshipFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.enums.Achievement.AchievementType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import com.zhonglian.server.websocket.handler.response.ResponseHandler;
import com.zhonglian.server.websocket.handler.response.WebSocketResponse;
import core.database.game.bo.PlayerBO;
import core.network.client2game.handler.PlayerHandler;
import core.network.game2world.WorldConnector;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import proto.gameworld.WorldRankInfo;

public class RankList extends PlayerHandler {
    @Override
    public void handle(Player player, final WebSocketRequest request, String message) throws WSException, IOException {
        RankList.Request req = new Gson().fromJson(message, RankList.Request.class);
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
            }

            if (rankType != null) {
                RankList.WorldRequest worldRequest = new RankList.WorldRequest();
                worldRequest.rank = rankType;
                worldRequest.pid = player.getPid();
                WorldConnector.request("activity.ranklist", worldRequest, new ResponseHandler() {
                    @Override
                    public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException {
                        WorldRankInfo reqx = new Gson().fromJson(body, WorldRankInfo.class);
                        request.response(reqx);
                    }

                    @Override
                    public void handleError(WebSocketResponse ssresponse, short statusCode, String messagex) {
                    }
                });
                return;
            }
        }

        List<Record> records = RankManager.getInstance().getRankList(req.rank, 30);
        int rank = RankManager.getInstance().getRank(req.rank, player.getPid());
        long value = RankManager.getInstance().getValue(req.rank, player.getPid());
        int times = player.getFeature(WorshipFeature.class).getTimes(req.rank.ordinal());
        request.response(new RankList.Response(times, rank, value, records));
    }

    public static class RankInfo extends core.network.proto.Player.Summary {
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
            this.power = player.getFeature(CharFeature.class).getPower();
            this.rank = record.getRank();
            this.value = record.getValue();
            RechargeFeature rechargeFeature = player.getFeature(RechargeFeature.class);
            int monthNum = rechargeFeature.getRebateRemains(AchievementType.MonthCardCrystal);
            this.MonthCard = monthNum > 0;
            int yearNum = rechargeFeature.getRebateRemains(AchievementType.YearCardCrystal);
            this.YearCard = yearNum == -1;
        }
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

    private static class WorldRequest {
        RankType rank;
        long pid;

        private WorldRequest() {
        }
    }
}
