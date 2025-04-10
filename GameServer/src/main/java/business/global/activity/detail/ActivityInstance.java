package business.global.activity.detail;

import business.global.activity.Activity;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.PlayerItem;
import business.player.item.Reward;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.http.server.RequestException;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.ref.RefCrystalPrice;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;
import core.server.OpenSeverTime;

public class ActivityInstance
extends Activity
{
private Integer challengTimes;
private Integer costId;
private Integer costCount;
private Integer gainId;
private Integer gainCount;

public ActivityInstance(ActivityBO data) {
super(data);

this.begin = 0;
this.end = 0;
}
private Integer buyTimes; private RefInstanceInfo instanceref; private int begin; private int end; private int close;

public static class RefInstanceInfo { String MapId;
int Level;
String Name;
String BossName;
String Icon;
int Power;
int BeginDialog;
int EndDialog;
String Wave1;
String Wave2;
String Wave3;
String Wave4;
String Wave5;

private RefInstanceInfo(JsonObject json) {
this.MapId = json.get("MapId").getAsString();
this.Level = json.get("Level").getAsInt();
this.Name = json.get("Name").getAsString();
this.BossName = json.get("BossName").getAsString();
this.Icon = json.get("Icon").getAsString();
this.Power = json.get("Power").getAsInt();
this.BeginDialog = json.get("BeginDialog").getAsInt();
this.EndDialog = json.get("EndDialog").getAsInt();
this.Wave1 = json.get("Wave1").getAsString();
this.Wave2 = json.get("Wave2").getAsString();
this.Wave3 = json.get("Wave3").getAsString();
this.Wave4 = json.get("Wave4").getAsString();
this.Wave5 = json.get("Wave5").getAsString();
} }

public void load(JsonObject json) throws WSException {
this.begin = json.get("begin").getAsInt();
this.end = json.get("end").getAsInt();
this.close = json.get("close").getAsInt();
this.challengTimes = Integer.valueOf(json.get("challengTimes").getAsInt());
this.costId = Integer.valueOf(json.get("costId").getAsInt());
this.costCount = Integer.valueOf(json.get("costCount").getAsInt());
this.gainId = Integer.valueOf(json.get("gainId").getAsInt());
this.gainCount = Integer.valueOf(json.get("gainCount").getAsInt());
this.buyTimes = Integer.valueOf(json.get("buyTimes").getAsInt());
this.instanceref = new RefInstanceInfo(json.get("Ref").getAsJsonObject(), null);
}

public int getBeginTime() {
return OpenSeverTime.getInstance().getOpenZeroTime() + this.begin;
}

public int getEndTime() {
return OpenSeverTime.getInstance().getOpenZeroTime() + this.end;
}

public int getCloseTime() {
return OpenSeverTime.getInstance().getOpenZeroTime() + this.close;
}

public String check(JsonObject json) throws RequestException {
return "";
}

public void handDailyRefresh(Player player) {
try {
if (getStatus() != ActivityStatus.Open) {
return;
}
ActivityRecordBO bo = getRecord(player);
if (bo == null) {
return;
}
bo.setExtInt(0, 0);
bo.setExtInt(1, 0);
bo.saveAll();
player.pushProto("activityInstanceRefresh", protoInfo(player));
} catch (Exception e) {

e.printStackTrace();
} 
}

private static class Response
{
int leftTimes;
int leftBuyTimes;
Reward costItem;
Reward rewardItem;
ActivityInstance.RefInstanceInfo ref;

private Response(int leftTimes, int leftBuyTimes, Reward costItem, Reward rewardItem, ActivityInstance.RefInstanceInfo ref) {
this.leftTimes = leftTimes;
this.leftBuyTimes = leftBuyTimes;
this.costItem = costItem;
this.rewardItem = rewardItem;
this.ref = ref;
}
}

public Response protoInfo(Player player) {
return new Response(getLeftTimes(player), getBuyTimes() - getBuyTimes(player), getCost(), getReward(), getRef(), null);
}

public ActivityRecordBO createPlayerActRecord(Player player) {
ActivityRecordBO bo = new ActivityRecordBO();
bo.setPid(player.getPid());
bo.setAid(this.bo.getId());
bo.setActivity(getType().toString());
bo.setExtInt(0, 0);
bo.setExtInt(1, 0);
bo.insert();
return bo;
}

public ActivityType getType() {
return ActivityType.ActivityInstance;
}

public int getLeftTimes(Player player) {
return this.challengTimes.intValue() - getOrCreateRecord(player).getExtInt(0);
}

public int getBuyTimes(Player player) {
return getOrCreateRecord(player).getExtInt(1);
}

public Reward getCost() {
return new Reward(this.costId.intValue(), this.costCount.intValue());
}

public Reward getReward() {
Reward reward = new Reward(this.gainId.intValue(), this.gainCount.intValue());
return reward;
}

public RefInstanceInfo getRef() {
return this.instanceref;
}

public int getBuyTimes() {
return this.buyTimes.intValue();
}

public Reward instanceWin(Player player) {
if (getStatus() != ActivityStatus.Open) {
return null;
}
ActivityRecordBO bo = getOrCreateRecord(player);
if (bo.getExtInt(0) >= this.challengTimes.intValue()) {
return null;
}
if (!((PlayerItem)player.getFeature(PlayerItem.class)).checkAndConsume(getCost(), ItemFlow.ActivityInstance)) {
return null;
}
Reward reward = getReward();
((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.ActivityInstance);
bo.setExtInt(0, bo.getExtInt(0) + 1);
bo.saveAll();

return reward;
}

public void buyTimes(Player player) {
if (getBuyTimes(player) >= getBuyTimes()) {
return;
}

RefCrystalPrice prize = RefCrystalPrice.getPrize(getBuyTimes(player));
int count = prize.ActivityInstanceAddChallenge;
PlayerCurrency currency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
if (!currency.check(PrizeType.Crystal, count)) {
return;
}
currency.consume(PrizeType.Crystal, count, ItemFlow.AddInstanceChallenge);
ActivityRecordBO bo = getOrCreateRecord(player);
bo.saveExtInt(0, bo.getExtInt(0) - 1);
bo.saveExtInt(1, bo.getExtInt(1) + 1);
}

public void onClosed() {
clearActRecord();
}

public void onOpen() {}

public void onEnd() {}
}

