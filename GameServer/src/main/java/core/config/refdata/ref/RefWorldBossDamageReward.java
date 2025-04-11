package core.config.refdata.ref;

import BaseCommon.CommLog;
import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.utils.Lists;
import com.zhonglian.server.common.utils.Maps;
import core.config.refdata.RefDataMgr;

import java.util.List;
import java.util.Map;

public class RefWorldBossDamageReward
        extends RefBaseGame {
    @RefField(isfield = false)
    private static Map<Integer, List<RefWorldBossDamageReward>> damageRewardByBossId = Maps.newConcurrentMap();
    @RefField(iskey = true)
    public int id;
    public int BossId;
    public int BeginRank;
    public int EndRank;
    public int MailId;

    public static RefWorldBossDamageReward getReward(int bossId, int rank) {
        List<RefWorldBossDamageReward> rewardList = damageRewardByBossId.get(Integer.valueOf(bossId));
        for (RefWorldBossDamageReward ref : rewardList) {
            if (rank >= ref.BeginRank && rank <= ref.EndRank) {
                return ref;
            }
        }
        return null;
    }

    public boolean Assert() {
        if (!RefAssert.inRef(Integer.valueOf(this.MailId), RefMail.class, new Object[0])) {
            return false;
        }
        if (!RefAssert.inRef(Integer.valueOf(this.BossId), RefWorldBoss.class, new Object[0])) {
            return false;
        }
        if (this.BeginRank > this.EndRank) {
            CommLog.error("BeginRank > EndRank");
            return false;
        }
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        for (RefWorldBoss refWorldBoss : RefDataMgr.getAll(RefWorldBoss.class).values()) {
            damageRewardByBossId.put(Integer.valueOf(refWorldBoss.id), Lists.newArrayList());
        }
        for (RefWorldBossDamageReward damageReward : all.values()) {
            ((List<RefWorldBossDamageReward>) damageRewardByBossId.get(Integer.valueOf(damageReward.BossId))).add(damageReward);
        }
        return true;
    }
}

