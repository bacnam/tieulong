package core.network.game2world.handler.activity;

import business.global.activity.ActivityMgr;
import business.global.activity.RankActivity;
import business.global.activity.detail.WorldConsume;
import business.global.activity.detail.WorldRecharge;
import business.global.activity.detail.WorldTreasure;
import business.global.rank.RankManager;
import business.global.rank.Record;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.game2world.GRequestHandler;
import java.util.ArrayList;
import java.util.List;
import proto.gameworld.WorldRankInfo;
import proto.gameworld.WorldRankInfo.RankInfo;

public class RankList extends GRequestHandler {
    private static RankInfo getPlayerInfo(Record record) {
        RankInfo info = new RankInfo();
        info.pid = record.recordBO.getOwner();
        info.name = record.recordBO.getName();
        info.lv = record.recordBO.getLv();
        info.icon = record.recordBO.getIcon();
        info.vipLv = record.recordBO.getVipLv();
        info.maxPower = record.recordBO.getMaxPower();
        info.rank = record.getRank();
        info.value = record.getValue();
        return info;
    }

    @Override
    public void handle(WebSocketRequest request, String data) throws WSException {
        RankList.Request req = new Gson().fromJson(data, RankList.Request.class);
        List<Record> records = RankManager.getInstance().getRankList(req.rank, 30);
        int rank = RankManager.getInstance().getRank(req.rank, req.pid);
        long value = RankManager.getInstance().getValue(req.rank, req.pid);
        WorldRankInfo response = new WorldRankInfo();
        response.rank = rank;
        response.value = value;
        List<RankInfo> rankList = new ArrayList<>();

        for (int i = 0; i < records.size(); i++) {
            Record r = records.get(i);
            if (r != null) {
                rankList.add(getPlayerInfo(r));
            }
        }

        response.rankList = rankList;
        RankActivity activity = null;
        switch (req.rank) {
            case WorldRecharge:
                activity = ActivityMgr.getActivity(WorldRecharge.class);
                break;
            case WorldConsume:
                activity = ActivityMgr.getActivity(WorldConsume.class);
                break;
            case WorldTreasure:
                activity = ActivityMgr.getActivity(WorldTreasure.class);
        }

        if (activity != null) {
            response.rewardList = activity.rankrewardList;
        }

        request.response(response);
    }

    private static class Request {
        RankType rank;
        long pid;
    }
}
