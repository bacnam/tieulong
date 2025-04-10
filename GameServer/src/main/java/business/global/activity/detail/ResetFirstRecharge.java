package business.global.activity.detail;

import business.global.activity.Activity;
import business.global.activity.ActivityMgr;
import business.global.recharge.RechargeMgr;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.enums.Achievement;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefRecharge;
import core.database.game.bo.ActivityBO;
import core.server.OpenSeverTime;

public class ResetFirstRecharge extends Activity {
private int begin;

public ResetFirstRecharge(ActivityBO bo) {
super(bo);

this.begin = 0;
}

public void load(JsonObject json) throws WSException {
this.begin = json.get("begin").getAsInt();
}

public int getBeginTime() {
return OpenSeverTime.getInstance().getOpenZeroTime() + this.begin;
}

public void onOpen() {
for (RefRecharge ref : RefDataMgr.getAll(RefRecharge.class).values()) {
if (ref.RebateAchievement == Achievement.AchievementType.MonthCardCrystal || ref.RebateAchievement == Achievement.AchievementType.YearCardCrystal) {
continue;
}
RechargeMgr.getInstance().reset(ref.id);
} 

((FirstRecharge)ActivityMgr.getActivity(FirstRecharge.class)).clearAllRecharge();
}

public void onEnd() {}

public void onClosed() {}

public ActivityType getType() {
return ActivityType.ResetFirstRecharge;
}
}

