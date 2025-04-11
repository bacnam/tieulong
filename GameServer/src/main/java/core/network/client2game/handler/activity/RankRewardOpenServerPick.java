package core.network.client2game.handler.activity;

import business.global.activity.ActivityMgr;
import business.global.activity.RankActivity;
import business.global.activity.detail.*;
import business.player.Player;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class RankRewardOpenServerPick
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);

        RankActivity rankActivity = null;

        switch (req.type) {
            case WingRank:
                rankActivity = (RankActivity) ActivityMgr.getActivity(RankWing.class);
                break;
            case DungeonRank:
                rankActivity = (RankActivity) ActivityMgr.getActivity(RankDungeon.class);
                break;
            case LevelRank:
                rankActivity = (RankActivity) ActivityMgr.getActivity(RankLevel.class);
                break;
            case PowerRank:
                rankActivity = (RankActivity) ActivityMgr.getActivity(RankPower.class);
                break;
            case DroiyanRank:
                rankActivity = (RankActivity) ActivityMgr.getActivity(RankDroiyan.class);
                break;
            case null:
                rankActivity = (RankActivity) ActivityMgr.getActivity(RankArena.class);
                break;
            case GumuRank:
                rankActivity = (RankActivity) ActivityMgr.getActivity(RankGumu.class);
                break;
            case TianLongRank:
                rankActivity = (RankActivity) ActivityMgr.getActivity(RankTianLong.class);
                break;
            case XiaoyaoRank:
                rankActivity = (RankActivity) ActivityMgr.getActivity(RankXiaoyao.class);
                break;
            case ArtificeRank:
                rankActivity = (RankActivity) ActivityMgr.getActivity(RankArtifice.class);
                break;
            case GuildRank:
                rankActivity = (RankActivity) ActivityMgr.getActivity(RankGuild.class);
                break;
        }

        if (rankActivity.getStatus() == ActivityStatus.Close) {
            throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", new Object[]{rankActivity.getType()});
        }

        request.response(rankActivity.pickUpReward(player));
    }

    class Request {
        ConstEnum.RankRewardType type;
    }
}

