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

public class RefRankReward
        extends RefBaseGame {
    @RefField(isfield = false)
    private static Map<RankType, List<RefRankReward>> rankRewardByType = Maps.newConcurrentMap();
    @RefField(iskey = true)
    public int id;
    public RankType Type;
    public int MailId;
    public int MinRank;
    public int MaxRank;

    public static List<RefRankReward> getRewards(RankType rank) {
        return rankRewardByType.get(rank);
    }

    public static RefRankReward getReward(RankType type, int rank) {
        List<RefRankReward> rewardList = rankRewardByType.get(type);
        for (RefRankReward ref : rewardList) {
            if (rank >= ref.MinRank && (rank <= ref.MaxRank || ref.MaxRank == -1)) {
                return ref;
            }
        }
        return null;
    }

    public Reward reward() {
        RefMail refMail = (RefMail) RefDataMgr.get(RefMail.class, Integer.valueOf(this.MailId));
        if (refMail == null) {
            return null;
        }
        RefReward refReward = (RefReward) RefDataMgr.get(RefReward.class, Integer.valueOf(refMail.RewardId));
        if (refReward == null) {
            return null;
        }
        return refReward.genReward();
    }

    public boolean Assert() {
        if (!RefAssert.inRef(Integer.valueOf(this.MailId), RefMail.class, new Object[0])) {
            return false;
        }
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        byte b;
        int i;
        RankType[] arrayOfRankType;
        for (i = (arrayOfRankType = RankType.values()).length, b = 0; b < i; ) {
            RankType rankType = arrayOfRankType[b];
            rankRewardByType.put(rankType, Lists.newArrayList());
            b++;
        }

        for (RefRankReward rankReward : all.values()) {
            ((List<RefRankReward>) rankRewardByType.get(rankReward.Type)).add(rankReward);
        }
        return true;
    }
}

