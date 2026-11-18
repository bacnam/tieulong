package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import com.zhonglian.server.common.enums.ConstEnum.RankRewardType;
import com.zhonglian.server.common.utils.Lists;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefOpenServerRankReward extends RefBaseWorld {
    @RefField(
        iskey = true
    )
    public int id;
    public NumberRange RankRange;
    public RankRewardType Type;
    public int MailId;
    @RefField(
        isfield = false
    )
    public static Map<RankRewardType, List<RefOpenServerRankReward>> RankRewardByType = new HashMap<>();

    @Override
    public boolean Assert() {
        return RefAssert.inRef(this.MailId, RefMail.class);
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        RankRewardType[] var5;
        for (RankRewardType type : var5 = RankRewardType.values()) {
            RankRewardByType.put(type, Lists.newArrayList());
        }

        for (RefOpenServerRankReward ref : ((RefContainer<RefOpenServerRankReward>) all).values()) {
            RankRewardByType.get(ref.Type).add(ref);
        }

        return true;
    }
}
