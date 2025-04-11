package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.utils.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefOpenServerRankReward
        extends RefBaseGame {
    @RefField(isfield = false)
    public static Map<ConstEnum.RankRewardType, List<RefOpenServerRankReward>> RankRewardByType = new HashMap<>();
    @RefField(iskey = true)
    public int id;
    public NumberRange RankRange;
    public ConstEnum.RankRewardType Type;
    public int MailId;

    public boolean Assert() {
        if (!RefAssert.inRef(Integer.valueOf(this.MailId), RefMail.class, new Object[0])) {
            return false;
        }
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        byte b;
        int i;
        ConstEnum.RankRewardType[] arrayOfRankRewardType;
        for (i = (arrayOfRankRewardType = ConstEnum.RankRewardType.values()).length, b = 0; b < i; ) {
            ConstEnum.RankRewardType type = arrayOfRankRewardType[b];
            RankRewardByType.put(type, Lists.newArrayList());
            b++;
        }

        for (RefOpenServerRankReward ref : all.values()) {
            ((List<RefOpenServerRankReward>) RankRewardByType.get(ref.Type)).add(ref);
        }

        return true;
    }
}

