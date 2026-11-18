package business.global.activity.detail;

import business.global.activity.Activity;
import business.global.activity.ActivityMgr;
import business.global.recharge.RechargeMgr;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.Achievement.AchievementType;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefRecharge;
import core.database.game.bo.ActivityBO;
import core.server.OpenSeverTime;

public class ResetFirstRecharge extends Activity {
    private int begin = 0;

    public ResetFirstRecharge(ActivityBO bo) {
        super(bo);
    }

    @Override
    public void load(JsonObject json) throws WSException {
        this.begin = json.get("begin").getAsInt();
    }

    @Override
    public int getBeginTime() {
        return OpenSeverTime.getInstance().getOpenZeroTime() + this.begin;
    }

    @Override
    public void onOpen() {
        for (RefRecharge ref : RefDataMgr.getAll(RefRecharge.class).values()) {
            if (ref.RebateAchievement != AchievementType.MonthCardCrystal && ref.RebateAchievement != AchievementType.YearCardCrystal) {
                RechargeMgr.getInstance().reset(ref.id);
            }
        }

        ActivityMgr.getActivity(FirstRecharge.class).clearAllRecharge();
    }

    @Override
    public void onEnd() {
    }

    @Override
    public void onClosed() {
    }

    @Override
    public ActivityType getType() {
        return ActivityType.ResetFirstRecharge;
    }
}
