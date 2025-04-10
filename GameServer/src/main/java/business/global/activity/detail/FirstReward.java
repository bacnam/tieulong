package business.global.activity.detail;

import business.global.activity.Activity;
import business.global.gmmail.MailCenter;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.features.RechargeFeature;
import business.player.item.Reward;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.http.server.RequestException;
import com.zhonglian.server.websocket.exception.WSException;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;
import java.util.ArrayList;
import java.util.List;

public class FirstReward
extends Activity
{
public List<DayReward> firstReward;

public FirstReward(ActivityBO data) {
super(data);

this.firstReward = new ArrayList<>();
}

private static class DayReward {
int id;
int value;
int power;
Reward reward;

private DayReward(JsonObject json) throws WSException {
this.id = json.get("aid").getAsInt();
this.value = json.get("value").getAsInt();
this.power = json.get("power").getAsInt();
this.reward = new Reward(json.get("awards").getAsJsonArray());
}
}

public void load(JsonObject json) throws WSException {
for (JsonElement element : json.get("rewards").getAsJsonArray()) {
JsonObject obj = element.getAsJsonObject();
DayReward builder = new DayReward(obj, null);
this.firstReward.add(builder);
} 
}

public String check(JsonObject json) throws RequestException {
return "ok";
}

public ActivityType getType() {
return ActivityType.FirstReward;
}

public boolean needNotify(Player player) {
return false;
}

public void clearAllRecharge() {
clearActRecord();
for (Player player : PlayerMgr.getInstance().getOnlinePlayers()) {
player.pushProto("clearFirstReward", this.firstReward);
}
}

public boolean isFirstRecharge(Player player) {
ActivityRecordBO bo = getRecord(player);
if (bo == null) {
return true;
}
return (bo.getExtInt(0) == 0);
}

public fristRewardProto fristRechargeProto(Player player) {
fristRewardProto proto = new fristRewardProto(null);
RechargeFeature rechargeFeature = (RechargeFeature)player.getFeature(RechargeFeature.class);
proto.isFirstRecharge = (isFirstRecharge(player) && !rechargeFeature.isRecharged());
proto.firstReward = this.firstReward;
ActivityRecordBO bo = getOrCreateRecord(player);
int times = bo.getExtInt(2);
proto.isOver = (times >= this.firstReward.size());

return proto;
}

private static class fristRewardProto
{
boolean isFirstRecharge;

List<FirstReward.DayReward> firstReward;
boolean isOver;

private fristRewardProto() {}
}

public void setFirstReward(Player player) {
synchronized (this) {
boolean isFirstRecharge = isFirstRecharge(player);
if (!isFirstRecharge) {
return;
}
ActivityRecordBO bo = getOrCreateRecord(player);
bo.setExtInt(0, 1);
bo.setExtInt(1, CommTime.nowSecond());
bo.saveAll();

player.pushProto("firstThreeReward", Boolean.valueOf(isFirstRecharge(player)));
} 

sendFirstReward(player);
}

public void sendFirstReward(Player player) {
try {
synchronized (this) {
boolean isFirstRecharge = isFirstRecharge(player);
if (isFirstRecharge) {
return;
}
ActivityRecordBO bo = getOrCreateRecord(player);
int times = bo.getExtInt(2);

if (times > this.firstReward.size() - 1) {
return;
}
Reward reward = getReward(times + 1);
if (reward == null)
return; 
bo.saveExtInt(2, times + 1);

MailCenter.getInstance().sendMail(player.getPid(), this.ref.MailSender, this.ref.MailTitle, this.ref.MailContent, reward.uniformItemIds(), 
reward.uniformItemCounts());
} 
} catch (Exception e) {

e.printStackTrace();
} 
}

public Reward getReward(int times) {
for (DayReward day : this.firstReward) {
if (day.id == times) {
return day.reward;
}
} 
return null;
}

public void onOpen() {}

public void onEnd() {}

public void onClosed() {}
}

