package business.global.activity.detail;

import business.global.activity.Activity;
import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.item.Reward;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.FetchStatus;
import com.zhonglian.server.common.utils.Lists;
import com.zhonglian.server.common.utils.StringUtils;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LevelReward
        extends Activity {
    private List<Level> rewardList;

    public LevelReward(ActivityBO bo) {
        super(bo);
    }

    public void load(JsonObject json) throws WSException {
        this.rewardList = Lists.newArrayList();
        for (JsonElement element : json.get("awards").getAsJsonArray()) {
            JsonObject obj = element.getAsJsonObject();
            Level builder = new Level(null);
            builder.aid = obj.get("aid").getAsInt();
            builder.level = obj.get("level").getAsInt();
            builder.reward = new Reward(obj.get("items").getAsJsonArray());
            this.rewardList.add(builder);
        }
    }

    public void onOpen() {
    }

    public void onEnd() {
    }

    public void onClosed() {
        clearActRecord();
    }

    public ActivityType getType() {
        return ActivityType.LevelReward;
    }

    public void handLevelChange(Player player) {
        if (getStatus() != ActivityStatus.Open) {
            return;
        }

        ActivityRecordBO bo = getOrCreateRecord(player);
        if (bo.getExtInt(0) >= player.getLv()) {
            return;
        }
        bo.saveExtInt(0, player.getLv());
    }

    public List<LevelRewardProtocol> getList(Player player) {
        List<LevelRewardProtocol> list = new ArrayList<>();
        this.rewardList.stream().forEach(x -> {
            LevelRewardProtocol protocol = toProtocol(paramPlayer, x);
            paramList.add(protocol);
        });
        return list;
    }

    public LevelRewardProtocol toProtocol(Player player, Level recharge) {
        ActivityRecordBO bo = getOrCreateRecord(player);
        List<Integer> rewardList = StringUtils.string2Integer(bo.getExtStr(0));
        LevelRewardProtocol protocol = new LevelRewardProtocol();
        protocol.aid = recharge.aid;
        protocol.level = recharge.level;
        protocol.reward = recharge.reward;
        if (rewardList.contains(Integer.valueOf(recharge.aid))) {
            protocol.status = FetchStatus.Fetched.ordinal();
        } else if (bo.getExtInt(0) >= recharge.level) {
            protocol.status = FetchStatus.Can.ordinal();
        } else {
            protocol.status = FetchStatus.Cannot.ordinal();
        }
        return protocol;
    }

    public LevelRewardProtocol pickReward(Player player, int rewardId) throws WSException {
        ActivityRecordBO bo = getOrCreateRecord(player);
        List<Integer> rewardList = StringUtils.string2Integer(bo.getExtStr(0));
        if (rewardList.contains(Integer.valueOf(rewardId))) {
            throw new WSException(ErrorCode.Already_Picked, "奖励已领取");
        }
        Level recharge = null;
        Optional<Level> find = this.rewardList.stream().filter(x -> (x.aid == paramInt)).findFirst();
        if (find.isPresent()) {
            recharge = find.get();
        } else {
            throw new WSException(ErrorCode.NotFound_ActivityAwardId, "奖励未找到");
        }

        if (bo.getExtInt(0) < recharge.level) {
            throw new WSException(ErrorCode.Not_Enough, "等级不足");
        }

        ((PlayerItem) player.getFeature(PlayerItem.class)).gain(recharge.reward, ItemFlow.LevelReward);
        rewardList.add(Integer.valueOf(rewardId));
        bo.saveExtStr(0, StringUtils.list2String(rewardList));
        return toProtocol(player, recharge);
    }

    public ActivityRecordBO createPlayerActRecord(Player player) {
        ActivityRecordBO bo = new ActivityRecordBO();
        bo.setPid(player.getPid());
        bo.setAid(this.bo.getId());
        bo.setActivity(getType().toString());
        bo.setExtInt(0, player.getLv());
        bo.insert();
        return bo;
    }

    private static class Level {
        int aid;
        int level;
        Reward reward;

        private Level() {
        }
    }

    public static class LevelRewardProtocol {
        int aid;
        int level;
        Reward reward;
        int status;
    }
}

