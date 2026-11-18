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

public class AccumRechargeDay extends Activity {
    public List<AccumRechargeDay.DayRecharge> ardList;

    public AccumRechargeDay(ActivityBO bo) {
        super(bo);
    }

    @Override
    public void load(JsonObject json) throws WSException {
        this.ardList = Lists.newArrayList();

        for (JsonElement element : json.get("awards").getAsJsonArray()) {
            JsonObject obj = element.getAsJsonObject();
            AccumRechargeDay.DayRecharge builder = new AccumRechargeDay.DayRecharge();
            builder.aid = obj.get("aid").getAsInt();
            builder.day = obj.get("day").getAsInt();
            builder.reward = new Reward(obj.get("items").getAsJsonArray());
            this.ardList.add(builder);
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
        return ActivityType.AccumDayRecharge;
    }

    public void handleRecharge(Player player) {
        if (this.getStatus() == ActivityStatus.Open) {
            ActivityRecordBO bo = this.getOrCreateRecord(player);
            if (bo.getExtInt(0) == 0) {
                bo.saveExtInt(0, 1);
                bo.saveExtInt(1, bo.getExtInt(1) + 1);
                player.pushProto("AccumRechargeDay", this.getList(player));
            }
        }
    }

    public AccumRechargeDay.DayRechargeProtocol pickReward(Player player, int rewardId) throws WSException {
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        List<Integer> rewardList = StringUtils.string2Integer(bo.getExtStr(0));
        if (rewardList.contains(rewardId)) {
            throw new WSException(ErrorCode.AccumRechargeDay_AlreadyPick, "奖励已领取");
        } else {
            AccumRechargeDay.DayRecharge recharge = null;
            Optional<AccumRechargeDay.DayRecharge> find = this.ardList.stream().filter(x -> x.aid == rewardId).findFirst();
            if (find.isPresent()) {
                recharge = find.get();
                if (bo.getExtInt(1) < recharge.day) {
                    throw new WSException(ErrorCode.AccumRechargeDay_NotEnough, "天数不足");
                } else {
                    player.getFeature(PlayerItem.class).gain(recharge.reward, ItemFlow.AccumRechargeDay);
                    rewardList.add(rewardId);
                    bo.saveExtStr(0, StringUtils.list2String(rewardList));
                    return this.toProtocol(player, recharge);
                }
            } else {
                throw new WSException(ErrorCode.AccumRechargeDay_NotFound, "奖励未找到");
            }
        }
    }

    public List<AccumRechargeDay.DayRechargeProtocol> getList(Player player) {
        List<AccumRechargeDay.DayRechargeProtocol> list = new ArrayList<>();
        this.ardList.stream().forEach(x -> {
            AccumRechargeDay.DayRechargeProtocol protocol = this.toProtocol(player, x);
            list.add(protocol);
        });
        return list;
    }

    public AccumRechargeDay.DayRechargeProtocol toProtocol(Player player, AccumRechargeDay.DayRecharge recharge) {
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        List<Integer> rewardList = StringUtils.string2Integer(bo.getExtStr(0));
        AccumRechargeDay.DayRechargeProtocol protocol = new AccumRechargeDay.DayRechargeProtocol();
        protocol.aid = recharge.aid;
        protocol.nowDay = bo.getExtInt(1);
        protocol.requireDay = recharge.day;
        protocol.reward = recharge.reward;
        if (rewardList.contains(recharge.aid)) {
            protocol.status = FetchStatus.Fetched.ordinal();
        } else if (protocol.nowDay >= protocol.requireDay) {
            protocol.status = FetchStatus.Can.ordinal();
        } else {
            protocol.status = FetchStatus.Cannot.ordinal();
        }

        return protocol;
    }

    public void dailyRefresh(Player player) {
        try {
            if (this.getStatus() != ActivityStatus.Open) {
                return;
            }

            ActivityRecordBO bo = this.getOrCreateRecord(player);
            if (bo.getExtInt(0) == 0) {
                return;
            }

            bo.saveExtInt(0, 0);
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    private static class DayRecharge {
        int aid;
        int day;
        Reward reward;

        private DayRecharge() {
        }
    }

    public static class DayRechargeProtocol {
        int aid;
        int nowDay;
        int requireDay;
        Reward reward;
        int status;
    }
}
