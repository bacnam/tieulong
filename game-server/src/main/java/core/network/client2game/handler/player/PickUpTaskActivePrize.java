package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.feature.task.TaskActivityFeature;
import business.player.item.Reward;
import com.google.gson.Gson;
import com.zhonglian.server.common.utils.StringUtils;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefDailyActive;
import core.config.refdata.ref.RefReward;
import core.database.game.bo.DailyactiveBO;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.List;

public class PickUpTaskActivePrize extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        PickUpTaskActivePrize.Request req = new Gson().fromJson(message, PickUpTaskActivePrize.Request.class);
        int rewardIndex = req.index;
        TaskActivityFeature taskActivityFeature = player.getFeature(TaskActivityFeature.class);
        DailyactiveBO dailyactiveBO = taskActivityFeature.getOrCreate();
        List<Integer> fetchedTaskIndex = StringUtils.string2Integer(dailyactiveBO.getFetchedTaskIndex());
        if (fetchedTaskIndex.contains(rewardIndex)) {
            throw new WSException(ErrorCode.NotEnough_TaskActiveValue, "奖励已领取过");
        } else {
            int value = dailyactiveBO.getValue();
            int refId = RefDailyActive.level2refId.get(dailyactiveBO.getTeamLevel());
            RefDailyActive refDailyActive = RefDataMgr.get(RefDailyActive.class, refId);
            int needValue = refDailyActive.Condition.get(rewardIndex);
            if (value < needValue) {
                throw new WSException(ErrorCode.NotEnough_TaskActiveValue, "玩家 %s 当前活跃度:%s 需要活跃度 : %s  ", player.getPid(), value, needValue);
            } else {
                int rewardId = refDailyActive.RewardID.get(rewardIndex);
                Reward reward = RefDataMgr.get(RefReward.class, rewardId).genReward();
                fetchedTaskIndex.add(rewardIndex);
                dailyactiveBO.saveFetchedTaskIndex(StringUtils.list2String(fetchedTaskIndex));
                Reward prize = player.getFeature(PlayerItem.class).gain(reward, ItemFlow.TaskActiveGain);
                taskActivityFeature.pushTaskActiveInfo();
                request.response(new PickUpTaskActivePrize.Response(req.index, prize));
            }
        }
    }

    public static class Request {
        int index;
    }

    public static class Response {
        int index;
        Reward reward;

        private Response(int index, Reward reward) {
            this.index = index;
            this.reward = reward;
        }
    }
}
