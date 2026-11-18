package business.global.activity.detail;

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
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;
import core.network.proto.AccumRechargeInfo;
import core.server.OpenSeverTime;
import java.util.List;
import java.util.Map;

public class AllPeopleReward extends Activity {
    private int begin;
    public List<AccumRechargeInfo> arList;

    public AllPeopleReward(ActivityBO data) {
        super(data);
    }

    @Override
    public void load(JsonObject json) throws WSException {
        this.arList = Lists.newArrayList();
        this.begin = json.get("begin").getAsInt();

        for (JsonElement element : json.get("awards").getAsJsonArray()) {
            JsonObject obj = element.getAsJsonObject();
            AccumRechargeInfo builder = new AccumRechargeInfo();
            builder.setAwardId(obj.get("aid").getAsInt());
            builder.setRecharge(obj.get("times").getAsInt());
            builder.setPrize(new Reward(obj.get("items").getAsJsonArray()));
            this.arList.add(builder);
        }
    }

    @Override
    public int getBeginTime() {
        return OpenSeverTime.getInstance().getOpenZeroTime() + this.begin;
    }

    @Override
    public String check(JsonObject json) throws RequestException {
        JsonArray awardArray = json.get("awards").getAsJsonArray();
        if (awardArray.size() <= 0) {
            throw new RequestException(4000001, "配置的奖励条数要大于0");
        } else {
            StringBuilder desc = new StringBuilder("【累充配置】\n");

            for (JsonElement element : awardArray) {
                JsonObject obj = element.getAsJsonObject();
                desc.append("奖励Id:").append(obj.get("aid").getAsInt()).append(",");
                int recharge = obj.get("times").getAsInt();
                if (recharge <= 0) {
                    throw new RequestException(4000001, "充值金额必须大于0");
                }

                desc.append("充值金额:").append(recharge).append(",");
                desc.append("奖励信息:").append(this.checkAndSubscribeItem(obj.get("items").getAsJsonArray())).append("\n");
            }

            return desc.toString();
        }
    }

    @Override
    public ActivityType getType() {
        return ActivityType.AllPeopleReward;
    }

    @Override
    public void onClosed() {
        this.clearActRecord();
    }

    public void handlePlayerChange(int money) {
        if (this.getStatus() == ActivityStatus.Open) {
            synchronized (this) {
                this.bo.saveExtInt(0, this.bo.getExtInt(0) + money);
                List<Integer> awardList = StringUtils.string2Integer(this.bo.getExtStr(0));
                List<Integer> stateList = StringUtils.string2Integer(this.bo.getExtStr(1));
                this.arList.forEach(x -> {
                    if (!awardList.contains(x.getAwardId()) && this.bo.getExtInt(0) >= x.getRecharge()) {
                        awardList.add(x.getAwardId());
                        stateList.add(FetchStatus.Can.ordinal());
                    }
                });
                this.bo.setExtStr(0, StringUtils.list2String(awardList));
                this.bo.setExtStr(1, StringUtils.list2String(stateList));
                this.bo.saveAll();
            }
        }
    }

    public AllPeopleReward.accumRechargeInfoP accumRechargeProto(Player player) {
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        List<Integer> stateList_tmp = StringUtils.string2Integer(this.bo.getExtStr(1));
        int recharge = this.bo.getExtInt(0);
        List<Integer> awardList = null;
        List<Integer> stateList = null;
        awardList = StringUtils.string2Integer(this.bo.getExtStr(0));
        stateList = StringUtils.string2Integer(bo.getExtStr(1));
        int begin = stateList.size();
        int end = stateList_tmp.size();

        for (int i = begin; i < end; i++) {
            stateList.add(stateList_tmp.get(i));
        }

        bo.saveExtStr(1, StringUtils.list2String(stateList));
        List<AccumRechargeInfo> accumerecharges = Lists.newArrayList();

        for (AccumRechargeInfo ar : this.arList) {
            AccumRechargeInfo builder = new AccumRechargeInfo();
            builder.setAwardId(ar.getAwardId());
            builder.setRecharge(ar.getRecharge());
            builder.setPrize(ar.getPrize());
            int index = awardList == null ? -1 : awardList.indexOf(ar.getAwardId());
            if (index != -1) {
                builder.setStatus(stateList.get(index));
            } else {
                builder.setStatus(FetchStatus.Cannot.ordinal());
            }

            accumerecharges.add(builder);
        }

        return new AllPeopleReward.accumRechargeInfoP(recharge, accumerecharges);
    }

    @Override
    public ActivityRecordBO createPlayerActRecord(Player player) {
        ActivityRecordBO bo = new ActivityRecordBO();
        bo.setPid(player.getPid());
        bo.setAid(this.bo.getId());
        bo.setActivity(this.getType().toString());
        bo.setExtStr(1, this.bo.getExtStr(1));
        bo.insert();
        return bo;
    }

    public AccumRechargeInfo doReceivePrize(Player player, int awardId) throws WSException {
        Map<Integer, AccumRechargeInfo> arMap = Maps.list2Map(AccumRechargeInfo::getAwardId, this.arList);
        AccumRechargeInfo arInfo = arMap.get(awardId);
        if (arInfo == null) {
            throw new WSException(ErrorCode.NotFound_ActivityAwardId, "奖励ID[%s]未找到", awardId);
        } else {
            ActivityRecordBO bo = this.getOrCreateRecord(player);
            List<Integer> awardList = StringUtils.string2Integer(this.bo.getExtStr(0));
            List<Integer> stateList = StringUtils.string2Integer(bo.getExtStr(1));
            int index = awardList.indexOf(awardId);
            if (index == -1) {
                throw new WSException(ErrorCode.AccumRecharge_CanNotReceive, "充值金额:%s<需求金额:%s", this.bo.getExtInt(0), arInfo.getRecharge());
            } else {
                int state = stateList.get(index);
                if (state != FetchStatus.Can.ordinal()) {
                    throw new WSException(ErrorCode.AccumRecharge_HasReceive, "奖励ID[%s]已领取", awardId);
                } else {
                    stateList.set(index, FetchStatus.Fetched.ordinal());
                    bo.saveExtStr(1, StringUtils.list2String(stateList));
                    Reward reward = arInfo.getPrize();
                    player.getFeature(PlayerItem.class).gain(reward, ItemFlow.Activity_AccumRecharge);
                    AccumRechargeInfo builder = new AccumRechargeInfo();
                    builder.setAwardId(awardId);
                    builder.setStatus(FetchStatus.Fetched.ordinal());
                    builder.setRecharge(arInfo.getRecharge());
                    builder.setPrize(reward);
                    return builder;
                }
            }
        }
    }

    public void weekEvent() {
        try {
            this.clearActRecord();
            this.bo.saveExtIntAll(0);
            this.bo.saveExtStrAll("");
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    @Override
    public void onOpen() {
    }

    @Override
    public void onEnd() {
    }

    private class accumRechargeInfoP {
        int times;
        List<AccumRechargeInfo> accumerecharges;

        private accumRechargeInfoP(int times, List<AccumRechargeInfo> accumerecharges) {
            this.times = times;
            this.accumerecharges = accumerecharges;
        }
    }
}
