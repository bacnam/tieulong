package business.player.feature.worldboss;

import com.zhonglian.server.common.enums.BattleStatus;
import com.zhonglian.server.common.utils.CommTime;
import core.config.refdata.RefDataMgr;

public class WorldBossConfig {
    public static int getFightCD() {
        int fightCD = RefDataMgr.getFactor("WorldBossAttackCD", 30);
        return (fightCD < 0) ? 0 : fightCD;
    }

    public static int getReliveTaskWaitCD() {
        return RefDataMgr.getFactor("WorldBossReviveCD", 30) * 1000;
    }

    public static BattleStatus getTodayFightStatus() {
        int todaySecond = CommTime.getTodaySecond();
        int beginTime = RefDataMgr.getFactor("WorldBossBeginFightTime");
        int endTime = RefDataMgr.getFactor("WorldBossEndFightTime");
        if (todaySecond < beginTime)
            return BattleStatus.NotBeginning;
        if (todaySecond >= endTime) {
            return BattleStatus.BattleEnded;
        }
        return BattleStatus.Battling;
    }
}

