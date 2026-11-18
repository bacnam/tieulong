package core.config.refdata.ref;

import business.player.item.Reward;
import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.utils.Lists;
import com.zhonglian.server.common.utils.Maps;
import core.config.refdata.RefDataMgr;
import java.util.List;
import java.util.Map;

public class RefRankReward extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public int id;
    public RankType Type;
    public int MailId;
    public int MinRank;
    public int MaxRank;
    @RefField(
        isfield = false
    )
    private static Map<RankType, List<RefRankReward>> rankRewardByType = Maps.newConcurrentMap();

    public Reward reward() {
        RefMail refMail = RefDataMgr.get(RefMail.class, this.MailId);
        if (refMail == null) {
            return null;
        } else {
            RefReward refReward = RefDataMgr.get(RefReward.class, refMail.RewardId);
            return refReward == null ? null : refReward.genReward();
        }
    }

    @Override
    public boolean Assert() {
        return RefAssert.inRef(this.MailId, RefMail.class);
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        RankType[] var5;
        for (RankType rankType : var5 = RankType.values()) {
            rankRewardByType.put(rankType, Lists.newArrayList());
        }

        for (RefRankReward rankReward : ((RefContainer<RefRankReward>) all).values()) {
            rankRewardByType.get(rankReward.Type).add(rankReward);
        }

        return true;
    }

    public static List<RefRankReward> getRewards(RankType rank) {
        return rankRewardByType.get(rank);
    }

    public static RefRankReward getReward(RankType type, int rank) {
        for (RefRankReward ref : rankRewardByType.get(type)) {
            if (rank >= ref.MinRank && (rank <= ref.MaxRank || ref.MaxRank == -1)) {
                return ref;
            }
        }

        return null;
    }
}
