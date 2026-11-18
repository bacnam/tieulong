package business.global.activity.detail;

import BaseCommon.CommLog;
import business.global.activity.Activity;
import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.item.Reward;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.FetchStatus;
import com.zhonglian.server.common.utils.Lists;
import com.zhonglian.server.common.utils.Maps;
import com.zhonglian.server.common.utils.StringUtils;
import com.zhonglian.server.http.server.RequestException;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefRecharge;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;
import core.network.proto.DailyRechargeInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DailyRecharge extends Activity {
    public List<DailyRechargeInfo> drList;
    private List<Integer> rechargeList = new ArrayList<>();

    public DailyRecharge(ActivityBO data) {
        super(data);
    }

    @Override
    public void load(JsonObject json) throws WSException {
        this.drList = Lists.newArrayList();

        for (JsonElement element : json.get("awards").getAsJsonArray()) {
            JsonObject obj = element.getAsJsonObject();
            DailyRechargeInfo builder = new DailyRechargeInfo();
            builder.setAwardId(obj.get("aid").getAsInt());
            builder.setMaxTimes(obj.get("receiveTimes").getAsInt());
            builder.setRecharge(obj.get("recharge").getAsInt());
            builder.setPrize(new Reward(obj.get("items").getAsJsonArray()));
            this.drList.add(builder);
            this.rechargeList.add(obj.get("recharge").getAsInt());
        }

        this.rechargeList.sort((left, right) -> left - right);
    }

    @Override
    public String check(JsonObject json) throws RequestException {
        JsonArray awardArray = json.get("awards").getAsJsonArray();
        if (awardArray.size() <= 0) {
            throw new RequestException(4000001, "配置的奖励条数要大于0");
        } else {
            List<Integer> moneyList = Lists.newArrayList();
            StringBuilder desc = new StringBuilder("【单笔充值指定额度】\n");

            for (JsonElement element : json.get("awards").getAsJsonArray()) {
                JsonObject obj = element.getAsJsonObject();
                desc.append("奖励Id:").append(obj.get("aid").getAsInt()).append(",");
                int receiveTimes = obj.get("receiveTimes").getAsInt();
                if (receiveTimes <= 0) {
                    throw new RequestException(4000001, "购买次数<=0");
                }

                desc.append("购买次数:").append(receiveTimes).append(",");
                int money = obj.get("recharge").getAsInt();
                if (money <= 0) {
                    throw new RequestException(4000001, "充值金额<=0");
                }

                RefRecharge ref = null;

                for (RefRecharge model : RefDataMgr.getAll(RefRecharge.class).values()) {
                    if (model.Price == money) {
                        ref = model;
                    }
                }

                if (ref == null) {
                    throw new RequestException(4000001, "【充值金额】必须在策划配置的recharge表中配置");
                }

                if (moneyList.contains(money)) {
                    throw new RequestException(4000001, "【充值金额】不能重复");
                }

                moneyList.add(money);
                desc.append("充值金额:").append(money).append(",");
                desc.append("奖励信息:").append(this.checkAndSubscribeItem(obj.get("items").getAsJsonArray())).append("\n");
            }

            return desc.toString();
        }
    }

    @Override
    public ActivityType getType() {
        return ActivityType.DailyRecharge;
    }

    @Override
    public void onClosed() {
        this.clearActRecord();
        CommLog.info("单充活动关闭");
    }

    public void dailyRechargeRefresh(Player player) {
        try {
            if (this.getStatus() != ActivityStatus.Open) {
                return;
            }

            ActivityRecordBO bo = this.getOrCreateRecord(player);
            List<Integer> awardsList = StringUtils.string2Integer(bo.getExtStr(0));
            List<Integer> leftRecList = StringUtils.string2Integer(bo.getExtStr(3));
            Map<Integer, DailyRechargeInfo> drMap = Maps.list2Map(DailyRechargeInfo::getAwardId, this.drList);

            for (int i = 0; i < awardsList.size(); i++) {
                DailyRechargeInfo tmp_info = drMap.get(awardsList.get(i));
                leftRecList.set(i, tmp_info.getMaxTimes());
            }

            bo.setExtStr(3, StringUtils.list2String(leftRecList));
            bo.saveAll();
            player.pushProto("dailyRechargeRefresh", this.dailyRechargeProto(player));
        } catch (Exception var8) {
            var8.printStackTrace();
        }
    }

    public void handlePlayerChange(Player player, int money) {
        if (this.getStatus() == ActivityStatus.Open) {
            Map<Integer, DailyRechargeInfo> drMap = Maps.list2Map(DailyRechargeInfo::getRecharge, this.drList);
            DailyRechargeInfo drInfo = drMap.get(money);
            if (drInfo == null) {
                for (int i = this.rechargeList.size() - 1; i >= 0; i--) {
                    if (this.rechargeList.get(i) < money) {
                        drInfo = drMap.get(this.rechargeList.get(i));
                    }
                }

                if (drInfo == null) {
                    return;
                }
            }

            ActivityRecordBO bo = this.getOrCreateRecord(player);
            if (money > bo.getExtInt(0)) {
                bo.setExtInt(0, money);
            }

            bo.setExtInt(1, bo.getExtInt(1) + money);
            int awardId = drInfo.getAwardId();
            List<Integer> awardsList = StringUtils.string2Integer(bo.getExtStr(0));
            List<Integer> canRecList = StringUtils.string2Integer(bo.getExtStr(1));
            List<Integer> hasRecList = StringUtils.string2Integer(bo.getExtStr(2));
            List<Integer> leftRecList = StringUtils.string2Integer(bo.getExtStr(3));
            int index = awardsList.indexOf(awardId);
            if (index == -1) {
                awardsList.add(awardId);
                canRecList.add(1);
                hasRecList.add(0);
                leftRecList.add(drInfo.getMaxTimes() - 1);
            } else {
                int leftTimes = leftRecList.get(index);
                int canTimes = canRecList.get(index);
                if (leftTimes != 0) {
                    canTimes++;
                    leftTimes--;
                }

                canRecList.set(index, canTimes);
                leftRecList.set(index, leftTimes);
            }

            bo.setExtStr(0, StringUtils.list2String(awardsList));
            bo.setExtStr(1, StringUtils.list2String(canRecList));
            bo.setExtStr(2, StringUtils.list2String(hasRecList));
            bo.setExtStr(3, StringUtils.list2String(leftRecList));
            bo.saveAll();
            player.pushProto("dailyRecharge", this.dailyRechargeProto(player));
        }
    }

    public FetchStatus fetchStatus(int leftTimes, int canTimes, int hasTimes) {
        if (leftTimes != 0) {
            return canTimes - hasTimes > 0 ? FetchStatus.Can : FetchStatus.Cannot;
        } else {
            return canTimes - hasTimes > 0 ? FetchStatus.Can : FetchStatus.Fetched;
        }
    }

    public List<DailyRechargeInfo> dailyRechargeProto(Player player) {
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        List<Integer> awardsList = StringUtils.string2Integer(bo.getExtStr(0));
        List<Integer> canRecList = StringUtils.string2Integer(bo.getExtStr(1));
        List<Integer> hasRecList = StringUtils.string2Integer(bo.getExtStr(2));
        List<Integer> leftRecList = StringUtils.string2Integer(bo.getExtStr(3));
        return this.drList.stream().map(x -> {
            DailyRechargeInfo builder = new DailyRechargeInfo();
            builder.setAwardId(x.getAwardId());
            builder.setMaxTimes(x.getMaxTimes());
            int canTimes = 0;
            int hasTimes = 0;
            int leftTimes = x.getMaxTimes();
            int index = awardsList.indexOf(x.getAwardId());
            if (index != -1) {
                canTimes = canRecList.get(index);
                hasTimes = hasRecList.get(index);
                leftTimes = leftRecList.get(index);
            }

            builder.setReceivedTimes(hasTimes);
            builder.setStatus(this.fetchStatus(leftTimes, canTimes, hasTimes).ordinal());
            builder.setRecharge(x.getRecharge());
            builder.setPrize(x.getPrize());
            builder.setLeftTimes(leftTimes);
            return builder;
        }).collect(Collectors.toList());
    }

    public DailyRecharge.doReceivePrizeInfo doReceivePrize(Player player, int awardId) throws WSException {
        Map<Integer, DailyRechargeInfo> drMap = Maps.list2Map(DailyRechargeInfo::getAwardId, this.drList);
        DailyRechargeInfo drInfo = drMap.get(awardId);
        if (drInfo == null) {
            throw new WSException(ErrorCode.NotFound_ActivityAwardId, "奖励ID[%s]未找到", awardId);
        } else {
            ActivityRecordBO bo = this.getOrCreateRecord(player);
            List<Integer> awardsList = StringUtils.string2Integer(bo.getExtStr(0));
            List<Integer> canRecList = StringUtils.string2Integer(bo.getExtStr(1));
            List<Integer> hasRecList = StringUtils.string2Integer(bo.getExtStr(2));
            List<Integer> leftRecList = StringUtils.string2Integer(bo.getExtStr(3));
            int index = awardsList.indexOf(awardId);
            if (index == -1) {
                throw new WSException(ErrorCode.DailyRecharge_CanNotReceive, "充值金额:%s!=需求金额:%s", bo.getExtInt(0), drInfo.getRecharge());
            } else {
                int canTimes = canRecList.get(index);
                int hasTimes = hasRecList.get(index);
                int leftTimes = leftRecList.get(index);
                if (hasTimes >= canTimes) {
                    throw new WSException(ErrorCode.DailyRecharge_HasReceive, "奖励ID[%s]已领取", awardId);
                } else {
                    hasTimes++;
                    bo.saveExtStr(3, StringUtils.list2String(leftRecList));
                    hasRecList.set(index, hasTimes);
                    bo.saveExtStr(2, StringUtils.list2String(hasRecList));
                    Reward reward = drInfo.getPrize();
                    player.getFeature(PlayerItem.class).gain(reward, ItemFlow.Activity_DailyRecharge);
                    FetchStatus fetchState = this.fetchStatus(leftTimes, canTimes, hasTimes);
                    return new DailyRecharge.doReceivePrizeInfo(awardId, hasTimes, fetchState.ordinal(), reward, leftTimes);
                }
            }
        }
    }

    @Override
    public void onOpen() {
    }

    @Override
    public void onEnd() {
    }

    private class doReceivePrizeInfo {
        int awardId;
        int hasTimes;
        int state;
        Reward prize;
        int leftTimes;

        private doReceivePrizeInfo(int awardId, int hasTimes, int state, Reward prize, int leftTimes) {
            this.awardId = awardId;
            this.hasTimes = hasTimes;
            this.state = state;
            this.prize = prize;
            this.leftTimes = leftTimes;
        }
    }
}
