package business.global.activity.detail;

import business.global.activity.Activity;
import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.item.Reward;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.SignInType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.http.server.RequestException;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefSignIn;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;
import java.util.List;
import java.util.stream.Collectors;

public class DailySign
extends Activity
{
public DailySign(ActivityBO data) {
super(data);
}

public void load(JsonObject json) throws WSException {}

public String check(JsonObject json) throws RequestException {
return "ok";
}

public ActivityType getType() {
return ActivityType.DailySign;
}

public ActivityStatus getStatus() {
return ActivityStatus.Open;
}

public boolean needNotify(Player player) {
ActivityRecordBO bo = (ActivityRecordBO)this.playerActRecords.get(Long.valueOf(player.getPid()));
if (bo == null) {
return true;
}
if (bo.getExtInt(1) == 0) {
return true;
}
return false;
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

public void handDailyRefresh(Player player, int day) {
try {
ActivityRecordBO bo = getOrCreateRecord(player);
if (bo.getExtInt(1) == 0) {
return;
}
if (CommTime.getFirstDayOfMonth() - day <= 0) {
bo.setExtInt(0, 0);
}
if (bo.getExtInt(0) >= RefSignIn.DailySignInMaxDay) {
bo.setExtInt(0, 0);
}
bo.setExtInt(1, 0);
bo.saveAll();
player.pushProto("dailySignRefresh", dailySignProto(player));
} catch (Exception e) {

e.printStackTrace();
} 
}

public signInfo dailySignProto(Player player) {
ActivityRecordBO bo = getOrCreateRecord(player);
signInfo info = new signInfo(bo.getExtInt(0), (bo.getExtInt(1) == 1));
return info;
}

private int getKey(int signInCount) {
return SignInType.SignIn.ordinal() * 1000 + signInCount;
}

public signInfo doSignIn(Player player) throws WSException {
ActivityRecordBO bo = getOrCreateRecord(player);
if (bo.getExtInt(1) == 1) {
throw new WSException(ErrorCode.Signin_AlreadySigned, "今天已签到过");
}
bo.setExtInt(0, bo.getExtInt(0) + 1);
bo.setExtInt(1, 1);
bo.saveAll();

RefSignIn refSignIn = (RefSignIn)RefDataMgr.get(RefSignIn.class, Integer.valueOf(getKey(bo.getExtInt(0))));
List<Integer> count = refSignIn.Count;
if (player.getVipLevel() >= refSignIn.VIPLevel) {
count = (List<Integer>)refSignIn.Count.stream().map(x -> Integer.valueOf(x.intValue() * paramRefSignIn.VIPTimes)).collect(Collectors.toList());
}
Reward pack = ((PlayerItem)player.getFeature(PlayerItem.class)).gain(refSignIn.UniformitemId, count, ItemFlow.Activity_SignInDaily);

return new signInfo(bo.getExtInt(0), (bo.getExtInt(1) == 1), pack);
}

private static class signInfo
{
int count;

boolean isSigned;
Reward reward;

public signInfo(int count, boolean isSigned) {
this.count = count;
this.isSigned = isSigned;
}

public signInfo(int count, boolean isSigned, Reward reward) {
this.count = count;
this.isSigned = isSigned;
this.reward = reward;
}
}

public void onOpen() {}

public void onEnd() {}

public void onClosed() {}
}

