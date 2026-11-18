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

public class LevelReward extends Activity {
    private List<LevelReward.Level> rewardList;

    public LevelReward(ActivityBO bo) {
        super(bo);
    }

    @Override
    public void load(JsonObject json) throws WSException {
        this.rewardList = Lists.newArrayList();

        for (JsonElement element : json.get("awards").getAsJsonArray()) {
            JsonObject obj = element.getAsJsonObject();
            LevelReward.Level builder = new LevelReward.Level();
            builder.aid = obj.get("aid").getAsInt();
            builder.level = obj.get("level").getAsInt();
            builder.reward = new Reward(obj.get("items").getAsJsonArray());
            this.rewardList.add(builder);
        }
    }

    @Override
    public void onOpen() {
    }

    @Override
    public void onEnd() {
    }

    @Override
    public void onClosed() {
        this.clearActRecord();
    }

    @Override
    public ActivityType getType() {
        return ActivityType.LevelReward;
    }

    public void handLevelChange(Player player) {
        if (this.getStatus() == ActivityStatus.Open) {
            ActivityRecordBO bo = this.getOrCreateRecord(player);
            if (bo.getExtInt(0) < player.getLv()) {
                bo.saveExtInt(0, player.getLv());
            }
        }
    }

    public List<LevelReward.LevelRewardProtocol> getList(Player player) {
        List<LevelReward.LevelRewardProtocol> list = new ArrayList<>();
        this.rewardList.stream().forEach(x -> {
            LevelReward.LevelRewardProtocol protocol = this.toProtocol(player, x);
            list.add(protocol);
        });
        return list;
    }

    public LevelReward.LevelRewardProtocol toProtocol(Player player, LevelReward.Level recharge) {
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        List<Integer> rewardList = StringUtils.string2Integer(bo.getExtStr(0));
        LevelReward.LevelRewardProtocol protocol = new LevelReward.LevelRewardProtocol();
        protocol.aid = recharge.aid;
        protocol.level = recharge.level;
        protocol.reward = recharge.reward;
        if (rewardList.contains(recharge.aid)) {
            protocol.status = FetchStatus.Fetched.ordinal();
        } else if (bo.getExtInt(0) >= recharge.level) {
            protocol.status = FetchStatus.Can.ordinal();
        } else {
            protocol.status = FetchStatus.Cannot.ordinal();
        }

        return protocol;
    }

    public LevelReward.LevelRewardProtocol pickReward(Player player, int rewardId) throws WSException {
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        List<Integer> rewardList = StringUtils.string2Integer(bo.getExtStr(0));
        if (rewardList.contains(rewardId)) {
            throw new WSException(ErrorCode.Already_Picked, "奖励已领取");
        } else {
            LevelReward.Level recharge = null;
            Optional<LevelReward.Level> find = this.rewardList.stream().filter(x -> x.aid == rewardId).findFirst();
            if (find.isPresent()) {
                recharge = find.get();
                if (bo.getExtInt(0) < recharge.level) {
                    throw new WSException(ErrorCode.Not_Enough, "等级不足");
                } else {
                    player.getFeature(PlayerItem.class).gain(recharge.reward, ItemFlow.LevelReward);
                    rewardList.add(rewardId);
                    bo.saveExtStr(0, StringUtils.list2String(rewardList));
                    return this.toProtocol(player, recharge);
                }
            } else {
                throw new WSException(ErrorCode.NotFound_ActivityAwardId, "奖励未找到");
            }
        }
    }

    @Override
    public ActivityRecordBO createPlayerActRecord(Player player) {
        ActivityRecordBO bo = new ActivityRecordBO();
        bo.setPid(player.getPid());
        bo.setAid(this.bo.getId());
        bo.setActivity(this.getType().toString());
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
