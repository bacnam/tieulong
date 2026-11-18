package core.network.client2game.handler.activity;

import business.global.activity.ActivityMgr;
import business.global.activity.RankActivity;
import business.global.activity.detail.RankArena;
import business.global.activity.detail.RankArtifice;
import business.global.activity.detail.RankDroiyan;
import business.global.activity.detail.RankDungeon;
import business.global.activity.detail.RankGuild;
import business.global.activity.detail.RankGumu;
import business.global.activity.detail.RankLevel;
import business.global.activity.detail.RankPower;
import business.global.activity.detail.RankTianLong;
import business.global.activity.detail.RankWing;
import business.global.activity.detail.RankXiaoyao;
import business.player.Player;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ConstEnum.RankRewardType;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class RankRewardOpenServerPick extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        RankRewardOpenServerPick.Request req = new Gson().fromJson(message, RankRewardOpenServerPick.Request.class);
        RankActivity rankActivity = null;
        switch (req.type) {
            case WingRank:
                rankActivity = ActivityMgr.getActivity(RankWing.class);
                break;
            case DungeonRank:
                rankActivity = ActivityMgr.getActivity(RankDungeon.class);
                break;
            case LevelRank:
                rankActivity = ActivityMgr.getActivity(RankLevel.class);
                break;
            case PowerRank:
                rankActivity = ActivityMgr.getActivity(RankPower.class);
                break;
            case DroiyanRank:
                rankActivity = ActivityMgr.getActivity(RankDroiyan.class);
                break;
            case ArenaRank:
                rankActivity = ActivityMgr.getActivity(RankArena.class);
                break;
            case GumuRank:
                rankActivity = ActivityMgr.getActivity(RankGumu.class);
                break;
            case TianLongRank:
                rankActivity = ActivityMgr.getActivity(RankTianLong.class);
                break;
            case XiaoyaoRank:
                rankActivity = ActivityMgr.getActivity(RankXiaoyao.class);
                break;
            case ArtificeRank:
                rankActivity = ActivityMgr.getActivity(RankArtifice.class);
                break;
            case GuildRank:
                rankActivity = ActivityMgr.getActivity(RankGuild.class);
        }

        if (rankActivity.getStatus() == ActivityStatus.Close) {
            throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", rankActivity.getType());
        } else {
            request.response(rankActivity.pickUpReward(player));
        }
    }

    class Request {
        RankRewardType type;
    }
}
