package business.global.guild;

import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import com.zhonglian.server.common.utils.CommTime;
import core.config.refdata.RefDataMgr;

public class LongnvWarConfig {
    public static int challengeCD() {
        return RefDataMgr.getFactor("Longnv_ChallengeCD", 7200);
    }

    public static int overTime() {
        return RefDataMgr.getFactor("Longnv_OverTime", 60);
    }

    public static int fightTime() {
        return RefDataMgr.getFactor("Longnv_FightTime", 1800);
    }

    public static int oneFightTime() {
        return RefDataMgr.getFactor("Longnv_OneFight", 30000);
    }

    public static boolean pickRewardTime() {
        int begin = RefDataMgr.getFactor("Longnv_RewardBegin", 14);
        int end = RefDataMgr.getFactor("Longnv_RewardEnd", 18);
        NumberRange range = new NumberRange(begin, end);
        return range.within(CommTime.getTodayHour());
    }

    public static int ResultMail() {
        return RefDataMgr.getFactor("Longnv_ResultMail", 30000);
    }
}

