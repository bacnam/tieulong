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

public class AccumRechargeDay
extends Activity
{
public List<DayRecharge> ardList;

private static class DayRecharge
{
int aid;
int day;
Reward reward;

private DayRecharge() {}
}

public AccumRechargeDay(ActivityBO bo) {
super(bo);
}

public void load(JsonObject json) throws WSException {
this.ardList = Lists.newArrayList();
for (JsonElement element : json.get("awards").getAsJsonArray()) {
JsonObject obj = element.getAsJsonObject();
DayRecharge builder = new DayRecharge(null);
builder.aid = obj.get("aid").getAsInt();
builder.day = obj.get("day").getAsInt();
builder.reward = new Reward(obj.get("items").getAsJsonArray());
this.ardList.add(builder);
} 
}

public void onOpen() {}

public void onEnd() {}

public void onClosed() {
clearActRecord();
}

public ActivityType getType() {
return ActivityType.AccumDayRecharge;
}

public void handleRecharge(Player player) {
if (getStatus() != ActivityStatus.Open) {
return;
}
ActivityRecordBO bo = getOrCreateRecord(player);
if (bo.getExtInt(0) != 0) {
return;
}
bo.saveExtInt(0, 1);
bo.saveExtInt(1, bo.getExtInt(1) + 1);
player.pushProto("AccumRechargeDay", getList(player));
}

public DayRechargeProtocol pickReward(Player player, int rewardId) throws WSException {
ActivityRecordBO bo = getOrCreateRecord(player);
List<Integer> rewardList = StringUtils.string2Integer(bo.getExtStr(0));
if (rewardList.contains(Integer.valueOf(rewardId))) {
throw new WSException(ErrorCode.AccumRechargeDay_AlreadyPick, "奖励已领取");
}
DayRecharge recharge = null;
Optional<DayRecharge> find = this.ardList.stream().filter(x -> (x.aid == paramInt)).findFirst();
if (find.isPresent()) {
recharge = find.get();
} else {
throw new WSException(ErrorCode.AccumRechargeDay_NotFound, "奖励未找到");
} 

if (bo.getExtInt(1) < recharge.day) {
throw new WSException(ErrorCode.AccumRechargeDay_NotEnough, "天数不足");
}

((PlayerItem)player.getFeature(PlayerItem.class)).gain(recharge.reward, ItemFlow.AccumRechargeDay);
rewardList.add(Integer.valueOf(rewardId));
bo.saveExtStr(0, StringUtils.list2String(rewardList));
return toProtocol(player, recharge);
}

public static class DayRechargeProtocol {
int aid;
int nowDay;
int requireDay;
Reward reward;
int status;
}

public List<DayRechargeProtocol> getList(Player player) {
List<DayRechargeProtocol> list = new ArrayList<>();
this.ardList.stream().forEach(x -> {
DayRechargeProtocol protocol = toProtocol(paramPlayer, x);
paramList.add(protocol);
});
return list;
}

public DayRechargeProtocol toProtocol(Player player, DayRecharge recharge) {
ActivityRecordBO bo = getOrCreateRecord(player);
List<Integer> rewardList = StringUtils.string2Integer(bo.getExtStr(0));
DayRechargeProtocol protocol = new DayRechargeProtocol();
protocol.aid = recharge.aid;
protocol.nowDay = bo.getExtInt(1);
protocol.requireDay = recharge.day;
protocol.reward = recharge.reward;
if (rewardList.contains(Integer.valueOf(recharge.aid))) {
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
if (getStatus() != ActivityStatus.Open) {
return;
}
ActivityRecordBO bo = getOrCreateRecord(player);
if (bo.getExtInt(0) == 0) {
return;
}
bo.saveExtInt(0, 0);
} catch (Exception e) {

e.printStackTrace();
} 
}
}

