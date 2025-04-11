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

public class PickUpTaskActivePrize
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        int rewardIndex = req.index;

        TaskActivityFeature taskActivityFeature = (TaskActivityFeature) player.getFeature(TaskActivityFeature.class);
        DailyactiveBO dailyactiveBO = taskActivityFeature.getOrCreate();

        List<Integer> fetchedTaskIndex = StringUtils.string2Integer(dailyactiveBO.getFetchedTaskIndex());
        if (fetchedTaskIndex.contains(Integer.valueOf(rewardIndex))) {
            throw new WSException(ErrorCode.NotEnough_TaskActiveValue, "奖励已领取过");
        }

        int value = dailyactiveBO.getValue();
        int refId = ((Integer) RefDailyActive.level2refId.get(Integer.valueOf(dailyactiveBO.getTeamLevel()))).intValue();
        RefDailyActive refDailyActive = (RefDailyActive) RefDataMgr.get(RefDailyActive.class, Integer.valueOf(refId));
        int needValue = ((Integer) refDailyActive.Condition.get(rewardIndex)).intValue();

        if (value < needValue) {
            throw new WSException(ErrorCode.NotEnough_TaskActiveValue, "玩家 %s 当前活跃度:%s 需要活跃度 : %s  ", new Object[]{Long.valueOf(player.getPid()), Integer.valueOf(value), Integer.valueOf(needValue)});
        }

        int rewardId = ((Integer) refDailyActive.RewardID.get(rewardIndex)).intValue();

        Reward reward = ((RefReward) RefDataMgr.get(RefReward.class, Integer.valueOf(rewardId))).genReward();

        fetchedTaskIndex.add(Integer.valueOf(rewardIndex));
        dailyactiveBO.saveFetchedTaskIndex(StringUtils.list2String(fetchedTaskIndex));
        Reward prize = ((PlayerItem) player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.TaskActiveGain);

        taskActivityFeature.pushTaskActiveInfo();
        request.response(new Response(req.index, prize, null));
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

