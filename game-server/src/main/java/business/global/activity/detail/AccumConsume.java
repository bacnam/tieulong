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
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;
import core.network.proto.AccumConsumeInfo;
import core.network.proto.AccumRechargeInfo;
import java.util.List;
import java.util.Map;

public class AccumConsume extends Activity {
    public List<AccumConsumeInfo> arList;

    public AccumConsume(ActivityBO data) {
        super(data);
    }

    @Override
    public void load(JsonObject json) throws WSException {
        this.arList = Lists.newArrayList();

        for (JsonElement element : json.get("awards").getAsJsonArray()) {
            JsonObject obj = element.getAsJsonObject();
            AccumConsumeInfo builder = new AccumConsumeInfo();
            builder.setAwardId(obj.get("aid").getAsInt());
            builder.setConsume(obj.get("consume").getAsInt());
            builder.setPrize(new Reward(obj.get("items").getAsJsonArray()));
            this.arList.add(builder);
        }
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
                int recharge = obj.get("consume").getAsInt();
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
        return ActivityType.AccumConsume;
    }

    @Override
    public void onClosed() {
        this.clearActRecord();
        CommLog.info("累消活动关闭");
    }

    public void handlePlayerChange(Player player, int money) {
        if (this.getStatus() == ActivityStatus.Open) {
            ActivityRecordBO bo = this.getOrCreateRecord(player);
            bo.setExtInt(0, bo.getExtInt(0) + money);
            List<Integer> awardList = StringUtils.string2Integer(bo.getExtStr(0));
            List<Integer> stateList = StringUtils.string2Integer(bo.getExtStr(1));
            this.arList.forEach(x -> {
                if (!awardList.contains(x.getAwardId()) && bo.getExtInt(0) >= x.getConsume()) {
                    awardList.add(x.getAwardId());
                    stateList.add(FetchStatus.Can.ordinal());
                }
            });
            bo.setExtStr(0, StringUtils.list2String(awardList));
            bo.setExtStr(1, StringUtils.list2String(stateList));
            bo.saveAll();
            player.pushProto("totalConsume", this.accumRechargeProto(player));
        }
    }

    public AccumConsume.accumRechargeInfoP accumRechargeProto(Player player) {
        ActivityRecordBO bo = this.getRecord(player);
        int recharge = 0;
        if (bo != null) {
            recharge = bo.getExtInt(0);
        }

        List<Integer> awardList = null;
        List<Integer> stateList = null;
        if (bo != null) {
            awardList = StringUtils.string2Integer(bo.getExtStr(0));
            stateList = StringUtils.string2Integer(bo.getExtStr(1));
        }

        List<AccumConsumeInfo> accumerecharges = Lists.newArrayList();

        for (AccumConsumeInfo ar : this.arList) {
            AccumConsumeInfo builder = new AccumConsumeInfo();
            builder.setAwardId(ar.getAwardId());
            builder.setConsume(ar.getConsume());
            builder.setPrize(ar.getPrize());
            int index = awardList == null ? -1 : awardList.indexOf(ar.getAwardId());
            if (index != -1) {
                builder.setStatus(stateList.get(index));
            } else {
                builder.setStatus(FetchStatus.Cannot.ordinal());
            }

            accumerecharges.add(builder);
        }

        return new AccumConsume.accumRechargeInfoP(recharge, accumerecharges);
    }

    public AccumRechargeInfo doReceivePrize(Player player, int awardId) throws WSException {
        Map<Integer, AccumConsumeInfo> arMap = Maps.list2Map(AccumConsumeInfo::getAwardId, this.arList);
        AccumConsumeInfo arInfo = arMap.get(awardId);
        if (arInfo == null) {
            throw new WSException(ErrorCode.NotFound_ActivityAwardId, "奖励ID[%s]未找到", awardId);
        } else {
            ActivityRecordBO bo = this.getOrCreateRecord(player);
            List<Integer> awardList = StringUtils.string2Integer(bo.getExtStr(0));
            List<Integer> stateList = StringUtils.string2Integer(bo.getExtStr(1));
            int index = awardList.indexOf(awardId);
            if (index == -1) {
                throw new WSException(ErrorCode.AccumRecharge_CanNotReceive, "充值金额:%s<需求金额:%s", bo.getExtInt(0), arInfo.getConsume());
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
                    builder.setPrize(reward);
                    return builder;
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

    private class accumRechargeInfoP {
        int consume;
        List<AccumConsumeInfo> accumerecharges;

        private accumRechargeInfoP(int consume, List<AccumConsumeInfo> accumerecharges) {
            this.consume = consume;
            this.accumerecharges = accumerecharges;
        }
    }
}
