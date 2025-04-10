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

public class AllPeopleReward
extends Activity
{
private int begin;
public List<AccumRechargeInfo> arList;

public AllPeopleReward(ActivityBO data) {
super(data);
}

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

public int getBeginTime() {
return OpenSeverTime.getInstance().getOpenZeroTime() + this.begin;
}

public String check(JsonObject json) throws RequestException {
JsonArray awardArray = json.get("awards").getAsJsonArray();
if (awardArray.size() <= 0) {
throw new RequestException(4000001, "配置的奖励条数要大于0", new Object[0]);
}
StringBuilder desc = new StringBuilder("【累充配置】\n");
for (JsonElement element : awardArray) {
JsonObject obj = element.getAsJsonObject();
desc.append("奖励Id:").append(obj.get("aid").getAsInt()).append(",");
int recharge = obj.get("times").getAsInt();
if (recharge <= 0) {
throw new RequestException(4000001, "充值金额必须大于0", new Object[0]);
}
desc.append("充值金额:").append(recharge).append(",");
desc.append("奖励信息:").append(checkAndSubscribeItem(obj.get("items").getAsJsonArray())).append("\n");
} 
return desc.toString();
}

public ActivityType getType() {
return ActivityType.AllPeopleReward;
}

public void onClosed() {
clearActRecord();
}

public void handlePlayerChange(int money) {
if (getStatus() != ActivityStatus.Open) {
return;
}
synchronized (this) {

this.bo.saveExtInt(0, this.bo.getExtInt(0) + money);

List<Integer> awardList = StringUtils.string2Integer(this.bo.getExtStr(0));

List<Integer> stateList = StringUtils.string2Integer(this.bo.getExtStr(1));

this.arList.forEach(x -> {
if (!paramList1.contains(Integer.valueOf(x.getAwardId())) && this.bo.getExtInt(0) >= x.getRecharge()) {
paramList1.add(Integer.valueOf(x.getAwardId()));
paramList2.add(Integer.valueOf(FetchStatus.Can.ordinal()));
} 
});
this.bo.setExtStr(0, StringUtils.list2String(awardList));
this.bo.setExtStr(1, StringUtils.list2String(stateList));
this.bo.saveAll();
} 
}

public accumRechargeInfoP accumRechargeProto(Player player) {
ActivityRecordBO bo = getOrCreateRecord(player);

List<Integer> stateList_tmp = StringUtils.string2Integer(this.bo.getExtStr(1));

int recharge = this.bo.getExtInt(0);

List<Integer> awardList = null, stateList = null;
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
int index = (awardList == null) ? -1 : awardList.indexOf(Integer.valueOf(ar.getAwardId()));
if (index != -1) {
builder.setStatus(((Integer)stateList.get(index)).intValue());
} else {
builder.setStatus(FetchStatus.Cannot.ordinal());
} 
accumerecharges.add(builder);
} 
return new accumRechargeInfoP(recharge, accumerecharges, null);
}

public ActivityRecordBO createPlayerActRecord(Player player) {
ActivityRecordBO bo = new ActivityRecordBO();
bo.setPid(player.getPid());
bo.setAid(this.bo.getId());
bo.setActivity(getType().toString());
bo.setExtStr(1, this.bo.getExtStr(1));
bo.insert();
return bo;
}

private class accumRechargeInfoP
{
int times;
List<AccumRechargeInfo> accumerecharges;

private accumRechargeInfoP(int times, List<AccumRechargeInfo> accumerecharges) {
this.times = times;
this.accumerecharges = accumerecharges;
}
}

public AccumRechargeInfo doReceivePrize(Player player, int awardId) throws WSException {
Map<Integer, AccumRechargeInfo> arMap = Maps.list2Map(AccumRechargeInfo::getAwardId, this.arList);

AccumRechargeInfo arInfo = arMap.get(Integer.valueOf(awardId));
if (arInfo == null) {
throw new WSException(ErrorCode.NotFound_ActivityAwardId, "奖励ID[%s]未找到", new Object[] { Integer.valueOf(awardId) });
}

ActivityRecordBO bo = getOrCreateRecord(player);

List<Integer> awardList = StringUtils.string2Integer(this.bo.getExtStr(0));

List<Integer> stateList = StringUtils.string2Integer(bo.getExtStr(1));

int index = awardList.indexOf(Integer.valueOf(awardId));
if (index == -1) {
throw new WSException(ErrorCode.AccumRecharge_CanNotReceive, "充值金额:%s<需求金额:%s", new Object[] { Integer.valueOf(this.bo.getExtInt(0)), Integer.valueOf(arInfo.getRecharge()) });
}
int state = ((Integer)stateList.get(index)).intValue();
if (state != FetchStatus.Can.ordinal()) {
throw new WSException(ErrorCode.AccumRecharge_HasReceive, "奖励ID[%s]已领取", new Object[] { Integer.valueOf(awardId) });
}

stateList.set(index, Integer.valueOf(FetchStatus.Fetched.ordinal()));
bo.saveExtStr(1, StringUtils.list2String(stateList));

Reward reward = arInfo.getPrize();

((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.Activity_AccumRecharge);

AccumRechargeInfo builder = new AccumRechargeInfo();
builder.setAwardId(awardId);
builder.setStatus(FetchStatus.Fetched.ordinal());
builder.setRecharge(arInfo.getRecharge());
builder.setPrize(reward);
return builder;
}

public void weekEvent() {
try {
clearActRecord();
this.bo.saveExtIntAll(0);
this.bo.saveExtStrAll("");
} catch (Exception e) {

e.printStackTrace();
} 
}

public void onOpen() {}

public void onEnd() {}
}

