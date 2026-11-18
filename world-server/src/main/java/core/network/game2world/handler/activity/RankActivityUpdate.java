package core.network.game2world.handler.activity;

import business.global.activity.ActivityMgr;
import business.global.activity.RankActivity;
import business.global.activity.detail.WorldConsume;
import business.global.activity.detail.WorldRecharge;
import business.global.activity.detail.WorldTreasure;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.game2world.GRequestHandler;
import proto.gamezone.Player.PlayerInfo;

public class RankActivityUpdate extends GRequestHandler {
    @Override
    public void handle(WebSocketRequest request, String data) throws WSException {
        RankActivityUpdate.Request req = new Gson().fromJson(data, RankActivityUpdate.Request.class);
        RankActivity activity = null;
        switch (req.rankType) {
            case WorldRecharge:
                activity = ActivityMgr.getActivity(WorldRecharge.class);
                break;
            case WorldConsume:
                activity = ActivityMgr.getActivity(WorldConsume.class);
                break;
            case WorldTreasure:
                activity = ActivityMgr.getActivity(WorldTreasure.class);
        }

        if (activity == null) {
            request.response();
        } else {
            activity.Update(req.player, req.value);
            request.response();
        }
    }

    private static class Request {
        PlayerInfo player;
        long value;
        RankType rankType;
    }
}
