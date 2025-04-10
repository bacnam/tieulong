package business.global.activity.detail;

import business.global.activity.Activity;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.PlayerItem;
import business.player.item.Reward;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.utils.StringUtils;
import com.zhonglian.server.http.server.RequestException;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;
import core.network.proto.VipAwardInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VipAward
extends Activity
{
private List<VipAwardRef> arList;

private static class VipAwardRef
{
int aid;
int vip;
String icon;
Reward reward;
int price;
int discount;
List<Integer> buytimes;

private VipAwardRef() {}
}

public VipAward(ActivityBO data) {
super(data);
}

public void load(JsonObject json) throws WSException {
this.arList = new ArrayList<>();
for (JsonElement element : json.get("awards").getAsJsonArray()) {
JsonObject obeject = element.getAsJsonObject();
VipAwardRef ref = new VipAwardRef(null);
ref.aid = obeject.get("aid").getAsInt();
ref.vip = obeject.get("vip").getAsInt();
ref.icon = obeject.get("icon").getAsString();
ref.reward = new Reward(obeject.get("items").getAsJsonArray());
ref.price = obeject.get("price").getAsInt();
ref.discount = obeject.get("discount").getAsInt();
ref.buytimes = StringUtils.string2Integer(obeject.get("buytimes").getAsString());
this.arList.add(ref);
} 
}

public String check(JsonObject json) throws RequestException {
return "ok";
}

public ActivityType getType() {
return ActivityType.VipAward;
}

public List<VipAwardInfo> vipAwardProto(Player player) {
ActivityRecordBO bo = getOrCreateRecord(player);

List<Integer> awardList = StringUtils.string2Integer(bo.getExtStr(0));
List<Integer> hasBuyList = StringUtils.string2Integer(bo.getExtStr(1));

List<VipAwardRef> refList = this.arList;

List<VipAwardInfo> weeks = (List<VipAwardInfo>)refList.stream().map(x -> {
VipAwardInfo builder = new VipAwardInfo();
builder.setAwardId(x.aid);
builder.setMaxTimes(((Integer)x.buytimes.get(paramPlayer.getVipLevel())).intValue());
builder.setReward(x.reward);
builder.setIcon(x.icon);
builder.setVip(x.vip);
builder.setPrice(x.price);
builder.setDiscount(x.discount);
builder.setTimeslist(StringUtils.list2String(x.buytimes));
int index = paramList1.indexOf(Integer.valueOf(x.aid));
if (index < 0) {
builder.setBuyTimes(0);
} else {
builder.setBuyTimes(((Integer)paramList2.get(index)).intValue());
} 
return builder;
}).collect(Collectors.toList());
return weeks;
}

public VipAwardRef getVipRef(int rewardId) {
for (VipAwardRef ref : this.arList) {
if (ref.aid == rewardId) {
return ref;
}
} 

return null;
}

public VipAwardReceive doWeeklyReceive(Player player, int awardId) throws WSException {
ActivityRecordBO bo = getOrCreateRecord(player);

VipAwardRef refVipAward = getVipRef(awardId);
if (refVipAward == null) {
throw new WSException(ErrorCode.NotFound_RefVipAward, "未找到[%s]的VIP礼包配置", new Object[] { Integer.valueOf(awardId) });
}
if (refVipAward.vip > player.getVipLevel()) {
throw new WSException(ErrorCode.VipAward_WeekNoNowVIPGift, "礼包VIP等级:%s != 玩家VIP等级:%s", new Object[] { Integer.valueOf(refVipAward.vip), Integer.valueOf(player.getVipLevel()) });
}

int maxTimes = ((Integer)refVipAward.buytimes.get(player.getVipLevel())).intValue();

List<Integer> awardList = StringUtils.string2Integer(bo.getExtStr(0));
List<Integer> hasBuyList = StringUtils.string2Integer(bo.getExtStr(1));

int buyTimes = 0;
int index = awardList.indexOf(Integer.valueOf(refVipAward.aid));
if (index >= 0) {
buyTimes = ((Integer)hasBuyList.get(index)).intValue();
}
if (buyTimes >= maxTimes) {
throw new WSException(ErrorCode.VipAward_WeekVIPGiftSold, "该礼包已卖光");
}

int cryStal = refVipAward.discount;
if (!((PlayerCurrency)player.getFeature(PlayerCurrency.class)).check(PrizeType.Crystal, cryStal)) {
throw new WSException(ErrorCode.NotEnough_Crystal, "玩家水晶:%s<购买水晶:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getCrystal()), Integer.valueOf(cryStal) });
}
Reward reward = refVipAward.reward;

Reward pack = ((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.Activity_WeeklyVipAward);

((PlayerCurrency)player.getFeature(PlayerCurrency.class)).consume(PrizeType.Crystal, cryStal, ItemFlow.Activity_WeeklyVipAward);

if (index < 0) {
awardList.add(Integer.valueOf(refVipAward.aid));
hasBuyList.add(Integer.valueOf(1));
} else {
hasBuyList.set(index, Integer.valueOf(buyTimes + 1));
} 
bo.saveExtStr(0, StringUtils.list2String(awardList));
bo.saveExtStr(1, StringUtils.list2String(hasBuyList));

return new VipAwardReceive(awardId, buyTimes + 1, pack);
}

public static class VipAwardReceive {
int awardId;
int buyTimes;
Reward reward;

public VipAwardReceive(int awardId, int buyTimes, Reward reward) {
this.awardId = awardId;
this.buyTimes = buyTimes;
this.reward = reward;
}
}

public void onOpen() {}

public void onEnd() {}

public void onClosed() {
clearActRecord();
}
}

