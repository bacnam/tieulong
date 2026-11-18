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

public class RefWorldBossDamageReward extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public int id;
    public int BossId;
    public int BeginRank;
    public int EndRank;
    public int MailId;
    @RefField(
        isfield = false
    )
    private static Map<Integer, List<RefWorldBossDamageReward>> damageRewardByBossId = Maps.newConcurrentMap();

    @Override
    public boolean Assert() {
        if (!RefAssert.inRef(this.MailId, RefMail.class)) {
            return false;
        } else if (!RefAssert.inRef(this.BossId, RefWorldBoss.class)) {
            return false;
        } else if (this.BeginRank > this.EndRank) {
            CommLog.error("BeginRank > EndRank");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        for (RefWorldBoss refWorldBoss : RefDataMgr.getAll(RefWorldBoss.class).values()) {
            damageRewardByBossId.put(refWorldBoss.id, Lists.newArrayList());
        }

        for (RefWorldBossDamageReward damageReward : ((RefContainer<RefWorldBossDamageReward>) all).values()) {
            damageRewardByBossId.get(damageReward.BossId).add(damageReward);
        }

        return true;
    }

    public static RefWorldBossDamageReward getReward(int bossId, int rank) {
        for (RefWorldBossDamageReward ref : damageRewardByBossId.get(bossId)) {
            if (rank >= ref.BeginRank && rank <= ref.EndRank) {
                return ref;
            }
        }

        return null;
    }
}
